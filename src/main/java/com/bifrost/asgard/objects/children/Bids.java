package com.bifrost.asgard.objects.children;

import com.bifrost.asgard.cache.Tesseract;
import com.bifrost.asgard.objects.base.Book;
import com.bifrost.asgard.enums.BookType;

public class Bids extends Book {
    public Bids(String underlier) {
        super(BookType.BID, new Tesseract(BookType.BID, underlier));
    }
}
