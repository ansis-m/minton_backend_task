package com.example.mintos.backend.controllers;

import org.springframework.data.domain.*;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SerializablePageMock<T> implements Page<T> {

    private final List<T> content;
    private final Pageable pageable;
    private final long total;

    public SerializablePageMock(List<T> content) {
        this.content = content;
        this.pageable = PageRequest.of(0, 10);
        this.total = content.size();
    }

    @Override
    public int getNumber() {
        return pageable.isPaged() ? pageable.getPageNumber() : 0;
    }

    @Override
    public int getSize() {
        return pageable.isPaged() ? pageable.getPageSize() : content.size();
    }

    @Override
    public int getNumberOfElements() {
        return content.size();
    }

    @Override
    public List<T> getContent() {
        return content;
    }

    @Override
    public boolean hasContent() {
        return !content.isEmpty();
    }

    @Override
    public Sort getSort() {
        return pageable.getSort();
    }

    @Override
    public boolean isFirst() {
        return !pageable.hasPrevious();
    }

    @Override
    public boolean isLast() {
        return false;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public boolean hasPrevious() {
        return pageable.hasPrevious();
    }

    @Override
    public Pageable nextPageable() {
        return pageable.next();
    }

    @Override
    public Pageable previousPageable() {
        return pageable.previousOrFirst();
    }

    @Override
    public Iterator<T> iterator() {
        return content.iterator();
    }

    @Override
    public int getTotalPages() {
        return getSize() == 0 ? 1 : (int) Math.ceil((double) total / (double) getSize());
    }

    @Override
    public long getTotalElements() {
        return total;
    }

    @Override
    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        return new SerializablePageMock<>(getConvertedContent(converter));
    }

    private <U> List<U> getConvertedContent(Function<? super T, ? extends U> converter) {
        return getContent().stream().map(converter).collect(Collectors.toList());
    }
}
