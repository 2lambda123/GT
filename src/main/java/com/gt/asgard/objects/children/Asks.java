package com.gt.asgard.objects.children;

import com.gt.asgard.cache.Tesseract;
import com.gt.asgard.objects.base.Book;
import com.gt.asgard.enums.BookType;

public class Asks extends Book {
    public Asks() {
        super(BookType.ASK, new Tesseract(BookType.ASK));
    }
}
