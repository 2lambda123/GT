package com.gt.asgard;

import com.gt.asgard.preprocess.Loki;
import com.gt.asgard.objects.children.ValidationObject;
import com.gt.asgard.preprocess.LokiService;
import com.gt.common.view.OrderView;
import com.gt.sokovia.service.Wanda;

public class Valkyrie {

//    Loki validator = new Loki();
    Thor engine = new Thor();

    public static void main(String[] args) throws Exception {
        Valkyrie test = new Valkyrie();
//        long start = System.nanoTime();
        for (long i = 0; i <= 999_999; i++) {
            OrderView buyOrder = new OrderView(i, "spx", 100L, 10.0, "sell");
//            ValidationObject validationObject = test.validator.submitRequest(null);
//            if (validationObject.containsErrors()) throw new Exception("Bad order due to -> " + validationObject.errorsToString());
            test.engine.acceptOrder(buyOrder);
        }
//        long midWay = System.nanoTime();
//        double midWayTime = (double) (midWay - start) / 1_000_000_000.0;
//        System.out.println(midWayTime);
        for (long i = 1_000_000; i < 2_000_000; i++) {
            OrderView sellOrder = new OrderView(i, "spx", 100L, 10.0, "buy");
//            ValidationObject validationObject = test.validator.validate(sellOrder);
//            if (validationObject.containsErrors()) throw new Exception("Bad order due to -> " + validationObject.errorsToString());
            if (i % 100_000 == 0) {
                System.out.println("Total Time Taken: " + test.engine.acceptOrder(sellOrder));
                System.out.println("--------------------------------");
            } else {
                test.engine.acceptOrder(sellOrder);
            }
        }
//        long end = System.nanoTime();
//        double seconds = (double) (end - start) / 1_000_000_000.0;
//        System.out.println(seconds);
    }


}
