package com.infoworks.lab.controllers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infoworks.lab.domain.entities.Passenger;
import com.infoworks.lab.rest.models.ItemCount;
import com.infoworks.lab.services.iServices.PassengerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/passenger")
public class PassengerController {

    private PassengerService service;
    private ObjectMapper mapper;

    public PassengerController(PassengerService service, ObjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> getHello() throws JsonProcessingException {
        ItemCount count = new ItemCount();
        count.setCount(12l);
        return ResponseEntity.ok(mapper.writeValueAsString(count));
    }

    @GetMapping("/rowCount")
    public ResponseEntity<String> getRowCount() throws JsonProcessingException {
        ItemCount count = new ItemCount();
        count.setCount(service.totalCount());
        return ResponseEntity.ok(mapper.writeValueAsString(count));
    }

    @GetMapping
    public List<Passenger> query(@RequestParam("limit") Integer size
            , @RequestParam("page") Integer page){
        //TODO: Test with RestExecutor
        List<Passenger> passengers = service.findAll(page, size);
        return passengers;
    }

    @PostMapping
    public Passenger insert(@Valid @RequestBody Passenger passenger){
        //TODO: Test with RestExecutor
        Passenger nPassenger = service.add(passenger);
        return nPassenger;
    }

    @PutMapping
    public Passenger update(@Valid @RequestBody Passenger passenger){
        //TODO: Test with RestExecutor
        Passenger old = service.update(passenger);
        return old;
    }

    @DeleteMapping
    public Boolean delete(@RequestParam("userid") Integer userid){
        //TODO: Test with RestExecutor
        boolean deleted = service.remove(userid);
        return deleted;
    }

}
