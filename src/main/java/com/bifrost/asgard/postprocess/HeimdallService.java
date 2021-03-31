package com.bifrost.asgard.postprocess;

import com.bifrost.common.view.OrderView;
import com.bifrost.sokovia.service.Wanda;
import lombok.extern.java.Log;

@Log
public class HeimdallService {

    private final Wanda database;

    public HeimdallService(Wanda database) {
        this.database = database;
    }

    public String updateOrder(OrderView inputOrder) {
        return database.updateOrder(inputOrder);
    }

}
