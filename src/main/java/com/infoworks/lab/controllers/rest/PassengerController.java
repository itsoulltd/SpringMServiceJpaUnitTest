package com.infoworks.lab.controllers.rest;

import com.infoworks.lab.domain.entities.Passenger;
import com.infoworks.lab.rest.models.ItemCount;
import com.infoworks.lab.services.iServices.iPassengerService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/passenger")
public class PassengerController {

    private iPassengerService service;

    public PassengerController(iPassengerService service) {
        this.service = service;
    }

    @GetMapping("/rowCount")
    public ItemCount getRowCount(){
        ItemCount count = new ItemCount();
        count.setCount(service.totalCount());
        return count;
    }

    @GetMapping
    public List<Passenger> query(@RequestParam("limit") Integer size
            , @RequestParam("offset") Integer page){
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
