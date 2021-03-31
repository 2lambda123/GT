package com.bifrost.asgard.objects.children;

import com.bifrost.asgard.cache.Tesseract;
import com.bifrost.asgard.objects.base.Book;
import com.bifrost.asgard.enums.BookType;

public class Asks extends Book {
    public Asks(String underlier) {
        super(BookType.ASK, new Tesseract(BookType.ASK, underlier));
    }
}
