package com.ptip.dto;

import java.util.List;

public class CustomPageResponse <T>{
    private List<T> contents;
    private long totalElements; // 최대 50개 정도 되겠지만 확장성을 위해 long
    private int totalPages;
    private boolean last;

    public CustomPageResponse(List<T> contents, long totalElements, int totalPages, boolean last){
        this.contents = contents;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }

    public List<T> getContents() {
        return contents;
    }

    public void setContents(List<T> contents) {
        this.contents = contents;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

}
