package com.example.mintos.backend.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class StaticHelpers {

    private static final Integer DEFAULT_PAGE_SIZE = 20;
    private static final Integer DEFAULT_PAGE_NUMBER = 0;

    public static Pageable getPageable(Integer page, Integer size){
        if (page != null && size != null) {
            return PageRequest.of(page, size);
        } else {
            return PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        }
    }
}
