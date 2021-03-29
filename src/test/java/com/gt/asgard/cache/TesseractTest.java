package com.gt.asgard.cache;

import com.gt.asgard.enums.BookType;
import com.gt.common.view.OrderView;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TesseractTest {

    Tesseract bids;
    Tesseract asks;
    OrderView buyOrder;
    OrderView sellOrder;

    @BeforeEach
    public void setup() {
        bids = new Tesseract(BookType.BID);
        asks = new Tesseract(BookType.ASK);
        buyOrder = new OrderView(1L, "testUser", "spx.us", 100L, 100.0, "buy");
        sellOrder = new OrderView(2L, "testUser", "spx.us", 100L, 100.0, "sell");
    }

    @Test
    public void fullAdd_Bids() throws Exception {
        bids.add(buyOrder);
        Assertions.assertEquals(100L, bids.getAmountAvailable());
        Assertions.assertEquals(100L, bids.getNumberOfSharesPerPrice().get(buyOrder.getPrice()));
        Assertions.assertEquals(buyOrder, bids.getOrdersGroupedByPrice().get(buyOrder.getPrice()).get(buyOrder.getId()));
        Assertions.assertEquals(buyOrder, bids.getCache().get(buyOrder.getId()));
        Assertions.assertEquals(0, bids.getCompletedOrders().size());
    }

    @Test()
    public void fullAdd_Duplicate_Bids() {
       assertThrows(Exception.class, () -> {
            bids.add(buyOrder);
            bids.add(buyOrder);
        });
    }

    @Test
    public void fullAdd_Asks() throws Exception {
        asks.add(sellOrder);
        Assertions.assertEquals(100L, asks.getAmountAvailable());
        Assertions.assertEquals(100L, asks.getNumberOfSharesPerPrice().get(sellOrder.getPrice()));
        Assertions.assertEquals(sellOrder, asks.getOrdersGroupedByPrice().get(sellOrder.getPrice()).get(sellOrder.getId()));
        Assertions.assertEquals(sellOrder, asks.getCache().get(sellOrder.getId()));
        Assertions.assertEquals(0, asks.getCompletedOrders().size());
    }

    @Test
    public void fullAdd_DuplicateAsks() {
        assertThrows(Exception.class, () -> {
            asks.add(sellOrder);
            asks.add(sellOrder);
        });
    }

    @Test
    public void fullDelete_Bids() throws Exception {
        bids.add(buyOrder);
        bids.remove(buyOrder.getId());
        Assertions.assertEquals(0L, bids.getAmountAvailable());
        Assertions.assertEquals(0, bids.getNumberOfSharesPerPrice().size());
        Assertions.assertEquals(0, bids.getOrdersGroupedByPrice().size());
        Assertions.assertEquals(0, bids.getCache().size());
        Assertions.assertEquals(1, bids.getCompletedOrders().size());
    }

    @Test
    public void fullDelete_Asks() throws Exception {
        asks.add(sellOrder);
        asks.remove(sellOrder.getId());
        Assertions.assertEquals(0L, asks.getAmountAvailable());
        Assertions.assertEquals(0, asks.getNumberOfSharesPerPrice().size());
        Assertions.assertEquals(0, asks.getOrdersGroupedByPrice().size());
        Assertions.assertEquals(0, asks.getCache().size());
        Assertions.assertEquals(1, asks.getCompletedOrders().size());
    }

    @Test
    public void fullFind_Bids() throws Exception {
        bids.add(buyOrder);
        OrderView order = bids.find(buyOrder.getId());
        Assertions.assertEquals(1L, order.getId());
        Assertions.assertEquals("testUser", order.getUserID());
        Assertions.assertEquals("spx.us", order.getSymbol());
        Assertions.assertEquals(100L, order.getQuantity());
        Assertions.assertEquals(100L, order.getQuantityRemaining());
        Assertions.assertEquals(100.0, order.getPrice());
        Assertions.assertEquals("buy", order.getSide());
    }

    @Test
    public void fullFind_Asks() throws Exception {
        asks.add(sellOrder);
        OrderView order = asks.find(sellOrder.getId());
        Assertions.assertEquals(2L, order.getId());
        Assertions.assertEquals("testUser", order.getUserID());
        Assertions.assertEquals("spx.us", order.getSymbol());
        Assertions.assertEquals(100L, order.getQuantity());
        Assertions.assertEquals(100L, order.getQuantityRemaining());
        Assertions.assertEquals(100.0, order.getPrice());
        Assertions.assertEquals("sell", order.getSide());
    }

    @Test
    @Ignore
    public void fullUpdate_Bids() throws Exception {
        OrderView updateOrder = new OrderView(1L, "updateUser", "nflx.q", 500L, 500.0, "buy");
//        buyOrder = new OrderView(1L, "testUser", "spx.us", 100L, 100.0, "buy");
        bids.add(buyOrder);
        bids.update(updateOrder, 200);
        Assertions.assertEquals(1L, buyOrder.getId());
        Assertions.assertEquals("updateUser", buyOrder.getUserID());
        Assertions.assertEquals("nflx.q", buyOrder.getSymbol());
        Assertions.assertEquals(500L, buyOrder.getQuantity());
        Assertions.assertEquals(300L, buyOrder.getQuantityRemaining());
        Assertions.assertEquals(100.0, buyOrder.getPrice());
        Assertions.assertEquals("buy", buyOrder.getSide());
    }

    @Test
    @Ignore
    public void fullUpdate_Asks() throws Exception {
        asks.add(sellOrder);
        OrderView order = asks.find(sellOrder.getId());
        Assertions.assertEquals(2L, order.getId());
        Assertions.assertEquals("testUser", order.getUserID());
        Assertions.assertEquals("spx.us", order.getSymbol());
        Assertions.assertEquals(100L, order.getQuantity());
        Assertions.assertEquals(100L, order.getQuantityRemaining());
        Assertions.assertEquals(100.0, order.getPrice());
        Assertions.assertEquals("sell", order.getSide());
    }

    @Test
    public void findOrderThatDoesNotExist_Bids() {
        assertThrows(Exception.class, () -> {
            bids.add(buyOrder);
            bids.find(-1L);
        });
    }

    @Test
    public void findOrderThatDoesNotExist_Asks() {
        assertThrows(Exception.class, () -> {
            asks.add(sellOrder);
            asks.find(-1L);
        });
    }

    @Test
    public void bids_add_givenBuyOrder_bidsBookGetsAffected() throws Exception {
        bids.add(buyOrder);
        Assertions.assertEquals(100L, bids.getAmountAvailable());
    }

    @Test
    public void bids_add_givenBuyOrder_asksBookDoesNotGetAffected() throws Exception {
        bids.add(buyOrder);
        Assertions.assertEquals(0L, asks.getAmountAvailable());
    }

    @Test
    public void bids_add_amountAvailable_goesUp() throws Exception {
        bids.add(buyOrder);
        Assertions.assertEquals(100L, bids.getAmountAvailable());
    }

    @Test
    public void bids_add_numberOfSharesPerPrice_goesUp() throws Exception {
        bids.add(buyOrder);
        Assertions.assertEquals(100L, bids.getNumberOfSharesPerPrice().get(buyOrder.getPrice()));
    }

    @Test
    public void bids_add_ordersGroupedByPrice_orderGetsAdded() throws Exception {
        bids.add(buyOrder);
        Assertions.assertEquals(buyOrder, bids.getOrdersGroupedByPrice().get(buyOrder.getPrice()).get(buyOrder.getId()));
    }

    @Test
    public void bids_add_completedOrders_doesNotChange() throws Exception {
        bids.add(buyOrder);
        Assertions.assertEquals(0, bids.getCompletedOrders().size());
    }

}
