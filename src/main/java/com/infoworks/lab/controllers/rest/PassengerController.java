package com.infoworks.lab.controllers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infoworks.lab.domain.entities.Passenger;
import com.infoworks.lab.domain.models.ItemCount;
import com.infoworks.lab.services.iServices.PassengerService;
import com.infoworks.sql.executor.QueryExecutor;
import com.infoworks.sql.query.pagination.SearchQuery;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/passenger")
public class PassengerController {

    private PassengerService service;
    private ObjectMapper mapper;
    private String defaultTenant;

    public PassengerController(PassengerService service
            , ObjectMapper mapper
            , @Value("${app.db.default-schema}") String defaultTenant) {
        this.service = service;
        this.mapper = mapper;
        this.defaultTenant = defaultTenant;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> getHello() throws JsonProcessingException {
        ItemCount count = new ItemCount();
        count.setCount(12l);
        return ResponseEntity.ok(mapper.writeValueAsString(count));
    }

    @GetMapping("/rowCount")
    public ResponseEntity<String> getRowCount(@RequestHeader(value = "X-Tenant", required = false) String tenant)
            throws JsonProcessingException {
        tenant = (tenant != null) ? tenant : defaultTenant;
        ItemCount count = new ItemCount();
        count.setStatus(200);
        count.setCount(service.totalCount());
        return ResponseEntity.ok(mapper.writeValueAsString(count));
    }

    @GetMapping
    public List<Passenger> fetch(
            @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit
            , @RequestParam(value = "page", defaultValue = "0", required = false) Integer page
            , @RequestHeader(value = "X-Tenant", required = false) String tenant){
        //TODO: Test with RestExecutor
        tenant = (tenant != null) ? tenant : defaultTenant;
        if (limit < 0) limit = 10;
        if (page < 0) page = 0;
        List<Passenger> passengers = service.findAll(page, limit);
        return passengers;
    }

    @PostMapping
    public Passenger insert(@Valid @RequestBody Passenger passenger
            , @RequestHeader(value = "X-Tenant", required = false) String tenant){
        //TODO: Test with RestExecutor
        tenant = (tenant != null) ? tenant : defaultTenant;
        Passenger nPassenger = service.add(passenger);
        return nPassenger;
    }

    @PutMapping
    public Passenger update(@Valid @RequestBody Passenger passenger
            , @RequestHeader(value = "X-Tenant", required = false) String tenant){
        //TODO: Test with RestExecutor
        tenant = (tenant != null) ? tenant : defaultTenant;
        Passenger old = service.update(passenger);
        return old;
    }

    @DeleteMapping
    public Boolean delete(@RequestParam("userid") Integer userid
            , @RequestHeader(value = "X-Tenant", required = false) String tenant){
        //TODO: Test with RestExecutor
        tenant = (tenant != null) ? tenant : defaultTenant;
        boolean deleted = service.remove(userid);
        return deleted;
    }

    /**
     * Example of inject @Scope beans.
     * e.g. @RequestScope bean SQLExecutor to do JDBC-Calls to database.
     */
    @Resource(name = "executor")
    private QueryExecutor executor;

    /**
     Example = "SearchQuery.java",
     Operators = {"EQUAL", "NOTEQUAL", "LIKE", "NOT_LIKE", "GREATER_THAN", "GREATER_THAN_OR_EQUAL", "LESS_THAN", "LESS_THAN_OR_EQUAL"},
     Notes = """
     {
         "descriptors": [],
         "page": 0,
         "payload": "string",
         "properties": [
             {
                 "key": "age",
                 "logic": "AND",
                 "nextKey": "name",
                 "operator": "GREATER_THAN_OR_EQUAL",
                 "type": "INT",
                 "value": "20"
             },
             {
                 "key": "name",
                 "logic": "AND",
                 "nextKey": "null",
                 "operator": "LIKE",
                 "type": "STRING",
                 "value": "%ha%"
             }
         ],
         "size": 10
     }
     """
     */
    @PostMapping("/search")
    public List<Passenger> search(@RequestBody SearchQuery query
            , @RequestHeader(value = "X-Tenant", required = false) String tenant) {
        //
        tenant = (tenant != null) ? tenant : defaultTenant;
        int limit = query.getSize();
        if (limit <= 0) limit = 10;
        List<Passenger> users = null;
        try {
            users = Passenger.read(Passenger.class, executor, query.getPredicate());
        } catch (Exception e) {}
        //
        limit = users.size() > limit ? limit : users.size();
        users = (users != null && users.size() > 0)
                ? users.subList(0, limit)
                : new ArrayList<>();
        return users;
    }

}
