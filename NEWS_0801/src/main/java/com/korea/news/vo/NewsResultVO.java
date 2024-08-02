package com.korea.news.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class NewsResultVO {
	private String lastBuildDate;
	private int total;
	private int start;
	private int display;
	private List<NewsVO> items;
}
