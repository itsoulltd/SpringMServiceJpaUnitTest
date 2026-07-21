package com.infoworks.lab.webapp.config.tenancy;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class MultiplexConnectionProvider implements MultiTenantConnectionProvider {

    private DataSource dataSource;

    public MultiplexConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        Connection connection = getAnyConnection();
        /**
         * Not all databases support schema based multi-tenancy.
         * PostgreSql, Oracle, H2DB(v1.4.200+) supports Hibernate's SCHEMA multi-tenancy (MultiTenancyStrategy.SCHEMA).
         * For such databases, jdbc::conn.setSchema(...) will works.
         * In other cases, like MySql is schemas-as-databases and supports only MultiTenancyStrategy.DATABASE.
         * have to use 'USE database' or a separate DataSource per database.
         */
        String db = connection.getMetaData().getDatabaseProductName();
        if ("PostgreSQL".equals(db)) {
            connection.setSchema(tenantIdentifier);
        } else if ("H2".equals(db)) {
            connection.setSchema(tenantIdentifier.toUpperCase());
        } else if ("Oracle".equals(db)) {
            //Since, not all oracle jdbc driver implements setSchema(...) ( available from Oracle 12c+)
            //A more universally compatible approach is:
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("ALTER SESSION SET CURRENT_SCHEMA = " + tenantIdentifier); //CAUTION: SQL-INJECTION
            }
        } else if ("MySQL".equals(db)) {
            /*try (Statement stmt = connection.createStatement()) {
                stmt.execute("USE " + tenantIdentifier); //CAUTION: SQL-INJECTION
            }*/
        } else {
            throw new SQLException("Unsupported database: " + db);
        }
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        //And when releasing the connection, restore the default schema:
        String db = connection.getMetaData().getDatabaseProductName();
        if ("PostgreSQL".equals(db)) {
            connection.setSchema("public");
        } else if ("H2".equals(db)) {
            connection.setSchema("PUBLIC");
        } else if ("Oracle".equals(db)) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("ALTER SESSION SET CURRENT_SCHEMA = SYSTEM"); // or your application's default schema
            }
        } else if ("MySQL".equals(db)) {
            /*try (Statement stmt = connection.createStatement()) {
                stmt.execute("USE <default-database>"); // your application's default database
            }*/
        }
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return null;
    }
}
