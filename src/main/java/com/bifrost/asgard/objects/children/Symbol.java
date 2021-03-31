package com.bifrost.asgard.objects.children;

import com.bifrost.asgard.objects.base.Book;

public class Symbol {

    public Book bids;
    public Book asks;
    public String underlier;

    public Symbol(String underlier) {
        this.bids = new Bids(underlier);
        this.asks = new Asks(underlier);
        this.underlier = underlier;
    }

}
