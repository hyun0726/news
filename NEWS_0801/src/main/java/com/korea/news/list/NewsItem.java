package com.korea.news.list;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true) // 정의되지 않은 필드 무시
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

    private List<String> sections; // List<String> 사용

    @JsonProperty("companies") // JSON 필드와 매핑
    private List<Company> companies; // 적절한 데이터 타입 사용
}
