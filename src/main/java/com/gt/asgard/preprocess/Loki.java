package com.gt.asgard.preprocess;

import com.gt.common.api.OrderRequest;
import com.gt.common.view.OrderView;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Log
@CrossOrigin
@RestController
@RequestMapping("loki")
public class Loki {

    private final LokiService service;

    public Loki(LokiService service) {
        this.service = service;
    }

    @PostMapping("/v1/request/submit")
    public String submitRequest(@RequestBody OrderRequest request) throws Exception {
        return service.submitRequest(request);
    }

    /*
    TODO: Implement cancel endpoint
     */
    @PutMapping("/v1/request/cancel/{id}")
    public void cancelOrder(@PathVariable long orderID) throws NotImplementedException {
        service.cancelOrder(orderID);
    }

}
