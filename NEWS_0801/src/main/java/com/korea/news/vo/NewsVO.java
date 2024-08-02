package com.korea.news.vo;



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
public class NewsVO {
	private String title;
	private String originallink;
	private String link;
	private String description;
	private String pubDate;
	

}
