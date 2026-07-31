package com.infoworks.lab.webapp.config;

import com.infoworks.connect.JDBCDriverClass;
import com.infoworks.entity.Entity;
import com.infoworks.orm.Property;
import com.infoworks.orm.Row;
import com.infoworks.sql.executor.SQLExecutor;
import com.infoworks.sql.query.*;
import com.infoworks.sql.query.models.Predicate;
import org.apache.el.stream.Stream;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Component
public class StartupConfig implements CommandLineRunner {

    private String serverPort;
    private String activeDriverClass;
    private Boolean isH2ConsoleEnabled;
    private String h2ConsolePath;
    private String schemas;
    private String migrationLocations;
    private DataSource dataSource;

    public StartupConfig(@Value("${server.port}") String serverPort
            , @Value("${spring.datasource.driver-class-name}") String activeDriverClass
            , @Value("${spring.h2.console.enabled}") Boolean isH2ConsoleEnabled
            , @Value("${spring.h2.console.path}") String h2ConsolePath
            , @Value("${spring.flyway.schemas}") String schemas
            , @Value("${spring.flyway.locations}") String migrationLocations
            , DataSource dataSource) {
        this.serverPort = serverPort;
        this.activeDriverClass = activeDriverClass;
        this.isH2ConsoleEnabled = isH2ConsoleEnabled;
        this.h2ConsolePath = h2ConsolePath;
        this.schemas = schemas;
        this.migrationLocations = migrationLocations;
        this.dataSource = dataSource;
    }

    @EventListener
    public void handleContextStartedListener(ContextRefreshedEvent event){
        System.out.println("ContextStarted....");
    }

    @EventListener
    public void handleContextStoppedListener(ContextClosedEvent event){
        System.out.println("ContextStopped....");
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Startup Done...");
        System.out.println(String.format("http://localhost:%s/swagger-ui.html", serverPort));
        if (activeDriverClass.equalsIgnoreCase(JDBCDriverClass.H2_EMBEDDED.toString())
                && isH2ConsoleEnabled){
            System.out.println(String.format("http://localhost:%s%s", serverPort, h2ConsolePath));
        }
        //Start tenant migration:
        executeTenantMigration();
    }

    private void executeTenantMigration() {
        List<String> tenants = Arrays.asList(schemas.split(","));
        for (String tenant : tenants) {
            Flyway.configure()
                    .dataSource(dataSource)
                    .schemas(tenant)
                    .locations(migrationLocations)
                    .load()
                    .migrate();
        }
    }

    private void batchInsertInto(Class<? extends Entity> entityType
            , int batchSize, List<Row> batch, SQLExecutor executor) throws SQLException {
        if (batch.isEmpty()) return;
        if (batchSize <= 0) batchSize = 10;
        List<Property> cols = batch.get(0).getProperties();
        SQLInsertQuery query = new SQLQuery.Builder(QueryType.INSERT)
                .into(entityType)
                .values(cols.toArray(new Property[0]))
                .build();
        executor.executeInsert(Entity.isAutoID(entityType), batchSize, query, batch);
    }

    private void batchUpdateOn(Class<? extends Entity> entityType
            , Predicate clause, int batchSize, List<Row> batch
            , SQLExecutor executor) throws SQLException {
        if (batch.isEmpty()) return;
        if (batchSize <= 0) batchSize = 10;
        List<Property> cols = batch.get(0).getProperties();
        SQLUpdateQuery query = new SQLQuery.Builder(QueryType.UPDATE)
                .set(cols.toArray(new Property[0]))
                .from(entityType)
                .where(clause)
                .build();
        executor.executeUpdate(batchSize, query, batch);
    }

    private void batchDeleteFrom(Class<? extends Entity> entityType
            , Predicate clause, int batchSize, List<Row> batch
            , SQLExecutor executor) throws SQLException {
        if (batch.isEmpty()) return;
        if (batchSize <= 0) batchSize = 10;
        SQLDeleteQuery query = new SQLQuery.Builder(QueryType.DELETE)
                .rowsFrom(entityType)
                .where(clause)
                .build();
        executor.executeDelete(batchSize, query, batch);
    }

}
