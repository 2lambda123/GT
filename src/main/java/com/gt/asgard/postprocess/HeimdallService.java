package com.gt.asgard.postprocess;

import com.gt.common.view.OrderView;
import com.gt.sokovia.service.Wanda;
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
