package com.infoworks.lab.webapp.config;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@Component
public class FlywayConfig {

    private static Logger LOG = LoggerFactory.getLogger(FlywayConfig.class);
    private String schemas;
    private String migrationLocations;
    private DataSource dataSource;

    public FlywayConfig(@Value("${spring.flyway.schemas}") String schemas
            , @Value("${spring.flyway.locations}") String migrationLocations
            , DataSource dataSource) {
        this.schemas = schemas;
        this.migrationLocations = migrationLocations;
        this.dataSource = dataSource;
    }

    public void executeFlywayMigration() {
        List<String> tenants = Arrays.asList(schemas.split(","));
        for (String tenant : tenants) {
            try {
                Flyway.configure()
                        .dataSource(dataSource)
                        .schemas(tenant)
                        .locations(migrationLocations)
                        .load()
                        .migrate();
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }
}
