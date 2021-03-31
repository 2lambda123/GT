package com.bifrost.asgard.preprocess;

import com.bifrost.common.api.OrderRequest;
import com.bifrost.common.view.OrderView;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

@Log
@CrossOrigin
@RestController
@RequestMapping("loki")
public class Loki {

    private final LokiService service;

    public Loki(LokiService service) {
        this.service = service;
    }

    @GetMapping("/queue")
    public String testQueue() {
        try (ZContext context = new ZContext()) {
            ZMQ.Socket requester = context.createSocket(SocketType.REQ);
            boolean didConnect = requester.connect("tcp://0.0.0.0:5559");

            log.info("(Loki) Connected to bifrost: " + didConnect);
            for (int request_nbr = 0; request_nbr < 10; request_nbr++) {
                requester.send("One", 0);
                String reply = requester.recvStr(0);
                log.info("Received reply " + request_nbr + " [" + reply + "]");
            }
        }
        return "Worked!";
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
