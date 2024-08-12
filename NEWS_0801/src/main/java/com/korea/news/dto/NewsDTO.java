package com.korea.news.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsDTO {
    private String id;
    private String title;
    private String publisher;
    private String author;
    private String summary;
    private String imageUrl;
    private String contentUrl;
    private Date publishedAt; 
    private String publishedAtAgo;
    private String keyword; 
    
}