package com.korea.news.list;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NewsResponse {
	 private Detail detail;
	 private int total_items; // JSON의 필드 이름과 일치시킴
	 private int total_pages; // JSON의 필드 이름과 일치시킴
	 private int page;
	 private int page_size; // JSON의 필드 이름과 일치시킴
	 private List<NewsItem> data;
}
