package com.bifrost.asgard.cache;

import com.bifrost.common.view.OrderView;

public interface InfinityStone {

    /**
     * Adds element into cache
     * @param order -- Order to be added
     */
    void add(OrderView order) throws Exception;

    /**
     * Removes element from cache
     *
     * Throws exception if not found
     * @param orderID -- OrderID to be removed
     * @return
     */
    OrderView remove(long orderID) throws Exception;

    /**
     * Updates element from cache with the details of input order
     *
     * Input Order's ID MUST match an existing order in the cache
     *      - If condition not met, exception is thrown
     * @param order -- Order which contains the new details
     */
    void update(OrderView order, long quantityToChange) throws Exception;

    /**
     * Retrieves order from cache (does not add/remove/update)
     *
     * Throws exception if orderID not found
     * @param orderID -- OrderID to be found in cache
     * @return
     */
    OrderView find(long orderID) throws Exception;

}
