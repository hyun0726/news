package com.korea.news.dto;

import java.util.List;

import lombok.Data;
@Data
public class NewsResponse {
    private List<NewsItem> data;

    
    public List<NewsItem> getData() {
        return data;
    }

    public void setData(List<NewsItem> data) {
        this.data = data;
    }
}

