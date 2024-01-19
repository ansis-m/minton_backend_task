package com.example.mintos.backend.utils;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StaticHelpersTest {

    @Test
    void testGetPageableWithNonNullValues() {
        int testPage = 1;
        int testSize = 10;
        Pageable pageable = StaticHelpers.getPageable(testPage, testSize);

        assertNotNull(pageable);
        assertEquals(testPage, pageable.getPageNumber());
        assertEquals(testSize, pageable.getPageSize());
    }

    @Test
    void testGetPageableWithNullValues() {
        Pageable pageable = StaticHelpers.getPageable(null, null);

        assertNotNull(pageable);
        assertEquals(StaticHelpers.DEFAULT_PAGE_NUMBER, pageable.getPageNumber());
        assertEquals(StaticHelpers.DEFAULT_PAGE_SIZE, pageable.getPageSize());
    }
}