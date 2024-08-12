package com.korea.news.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsItem {
    private String id;
    private String title;
    private String publisher;
    private String author;
    private String summary;

    @JsonProperty("image_url")  // JSON 필드와 매핑
    private String imageUrl;

    @JsonProperty("content_url") // JSON 필드와 매핑
    private String contentUrl;

    @JsonProperty("published_at") // JSON 필드와 매핑
    private Date publishedAt;

}
