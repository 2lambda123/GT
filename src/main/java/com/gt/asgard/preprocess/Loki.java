package com.gt.asgard.preprocess;

import com.gt.common.api.OrderRequest;
import com.gt.common.view.OrderView;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;

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
    public OrderView submitRequest(@RequestBody OrderRequest request) throws Exception {
        return service.submitRequest(request);
    }

    @PutMapping("/v1/request/cancel/{orderID}")
    public void cancelOrder(@PathVariable long orderID) throws Exception {
        service.cancelOrder(orderID);
    }

}
