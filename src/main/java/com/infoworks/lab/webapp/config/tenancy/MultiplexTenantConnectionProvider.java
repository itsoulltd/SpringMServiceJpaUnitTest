package com.infoworks.lab.webapp.config.tenancy;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class MultiplexTenantConnectionProvider implements MultiTenantConnectionProvider {

    private DataSource dataSource;

    public MultiplexTenantConnectionProvider(DataSource dataSource) {
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
        connection.setSchema(tenantIdentifier);
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        connection.setSchema(tenantIdentifier);
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
