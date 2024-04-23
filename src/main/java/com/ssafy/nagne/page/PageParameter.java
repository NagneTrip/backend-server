package com.ssafy.nagne.page;

public class PageParameter implements Pageable {

    private final long lastIndex;

    private final int size;

    public PageParameter() {
        this(Long.MAX_VALUE, 10);
    }

    public PageParameter(Long lastIndex, int size) {
        this.lastIndex = lastIndex;
        this.size = size;
    }

    @Override
    public long getLastIndex() {
        return lastIndex;
    }

    @Override
    public int getSize() {
        return size;
    }
}