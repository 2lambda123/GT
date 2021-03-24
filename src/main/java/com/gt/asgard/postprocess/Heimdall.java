package com.gt.asgard.postprocess;

import com.gt.common.view.OrderView;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;

@Log
@CrossOrigin
@RestController
@RequestMapping("heimdall")
public class Heimdall {

    private final HeimdallService service;

    public Heimdall(HeimdallService service) {
        this.service = service;
    }

    @PutMapping("/v1/order/update")
    public String updateUser(@RequestBody OrderView inputOrder) {
        return service.updateOrder(inputOrder);
    }

}
