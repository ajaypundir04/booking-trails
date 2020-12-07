package com.element.insurance.bookings.dto;

import java.util.List;
import java.util.Objects;

public class PageableResponse<T> {
    private long total;
    private int pageSize;
    private List<T> data;

    public PageableResponse( long total, int pageSize, List< T > data ) {
        this.total = total;
        this.pageSize = pageSize;
        this.data = data;
    }

    public PageableResponse( long total, List< T > data ) {
        this.total = total;
        this.data = data;
    }

    public PageableResponse() {
    }

    public long getTotal() {
        return total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize( int pageSize ) {
        this.pageSize = pageSize;
    }

    public List< T > getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PageableResponse)) return false;
        PageableResponse<?> that = (PageableResponse<?>) o;
        return getTotal() == that.getTotal() &&
                getPageSize() == that.getPageSize() &&
                Objects.equals(getData(), that.getData());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTotal(), getPageSize(), getData());
    }

    @Override
    public String toString() {
        return "PageableResponse{" +
                "total=" + total +
                ", pageSize=" + pageSize +
                ", data=" + data +
                '}';
    }
}
