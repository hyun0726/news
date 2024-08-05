package com.korea.news.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class NewsVO {
    private String id;
    private String title;
    private String publisher;
    private String author;
    private String summary;
    private String imageUrl;
    private String publishedAt; 
    
  
}
