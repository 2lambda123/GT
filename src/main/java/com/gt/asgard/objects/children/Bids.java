package com.gt.asgard.objects.children;

import com.gt.asgard.cache.Tesseract;
import com.gt.asgard.objects.base.Book;
import com.gt.asgard.enums.BookType;

public class Bids extends Book {
    public Bids() {
        super(BookType.BID, new Tesseract(BookType.BID));
    }
}
