package com.gt.enigma;

import com.gt.enigma.preprocess.Loki;
import com.gt.enigma.preprocess.ValidationObject;
import com.gt.model.view.OrderView;

public class Valkyrie {

    Loki validator = new Loki("spx");
    Thor engine = new Thor();

    public static void main(String[] args) throws Exception {
        Valkyrie test = new Valkyrie();
        long start = System.nanoTime();
        for (long i = 0; i < 1; i++) {
            OrderView buyOrder = new OrderView(i, "spx", 200L, 10.0, "buy");
            ValidationObject validationObject = test.validator.validate(buyOrder);
            if (validationObject.containsErrors()) throw new Exception("Bad order due to -> " + validationObject.errorsToString());
            test.engine.acceptOrder(buyOrder);
        }
        for (long i = 0; i < 1; i++) {
            OrderView sellOrder = new OrderView(i, "spx", 100L, 10.0, "sell");
            ValidationObject validationObject = test.validator.validate(sellOrder);
            if (validationObject.containsErrors()) throw new Exception("Bad order due to -> " + validationObject.errorsToString());
            test.engine.acceptOrder(sellOrder);
        }
        long end = System.nanoTime();
        double seconds = (double) (end - start) / 1_000_000_000.0;
        System.out.println(seconds);
    }


}
