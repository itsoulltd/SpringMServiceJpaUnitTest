package com.infoworks.lab.webapp.config;

import com.it.soul.lab.sql.SQLExecutor;
import com.it.soul.lab.sql.entity.Entity;
import com.it.soul.lab.sql.query.QueryType;
import com.it.soul.lab.sql.query.SQLInsertQuery;
import com.it.soul.lab.sql.query.SQLQuery;
import com.it.soul.lab.sql.query.models.Property;
import com.it.soul.lab.sql.query.models.Row;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component
public class StartupConfig implements CommandLineRunner {

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

}
