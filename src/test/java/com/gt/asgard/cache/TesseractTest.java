package com.gt.asgard.cache;

import com.gt.asgard.enums.BookType;
import com.gt.common.view.OrderView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TesseractTest {
//
//    // These 4 must always be affected together
//    private double amountAvailable;
//    private TreeMap<Double, Long> numberOfSharesPerPrice;
//    private Map<Double, Map<Long, OrderView>> ordersGroupedByPrice;
//
//    // This one may not
//    private List<OrderView> completedOrders;

    Tesseract bids;
    Tesseract asks;
    OrderView buyOrder;
    OrderView sellOrder;

    @BeforeAll
    public void setup() {
        bids = new Tesseract(BookType.BID);
        asks = new Tesseract(BookType.ASK);
        buyOrder = new OrderView(1L, "testUser", "spx.us", 100L, 100.0, "buy");
        sellOrder = new OrderView(2L, "testUser", "spx.us", 100L, 100.0, "sell");
    }

    @AfterEach
    public void reset() {
        bids = new Tesseract(BookType.BID);
        asks = new Tesseract(BookType.ASK);
        buyOrder = new OrderView(1L, "testUser", "spx.us", 100L, 100.0, "buy");
        sellOrder = new OrderView(2L, "testUser", "spx.us", 100L, 100.0, "sell");
    }

    @Test
    public void bids_add_givenBuyOrder_bidsBookGetsAffected() throws Exception {
        bids.add(buyOrder);
        Assertions.assertEquals(bids.getAmountAvailable(), 100L);
    }

    @Test
    public void bids_add_givenBuyOrder_asksBookDoesNotGetAffected() throws Exception {
        bids.add(buyOrder);
        Assertions.assertEquals(asks.getAmountAvailable(), 0L);
    }

    @Test
    public void bids_add_amountAvailable_goesUp() throws Exception {
        bids.add(buyOrder);
        Assertions.assertEquals(bids.getAmountAvailable(), 100L);
    }

    @Test
    public void bids_add_numberOfSharesPerPrice_goesUp() throws Exception {
        bids.add(buyOrder);
        Assertions.assertEquals(bids.getNumberOfSharesPerPrice().get(buyOrder.getPrice()), 100L);
    }

    @Test
    public void bids_add_ordersGroupedByPrice_orderGetsAdded() throws Exception {
        bids.add(buyOrder);
        Assertions.assertEquals(bids.getOrdersGroupedByPrice().get(buyOrder.getPrice()).get(buyOrder.getId()), buyOrder);
    }

    @Test
    public void bids_add_completedOrders_doesNotChange() throws Exception {
        bids.add(buyOrder);
        Assertions.assertEquals(bids.getCompletedOrders().size(), 0);
    }

}
