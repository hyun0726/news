package com.korea.news.dao;

import org.springframework.stereotype.Repository;

import com.korea.news.mapper.NewsMapper;

import lombok.RequiredArgsConstructor;
@Repository
@RequiredArgsConstructor
public class NewsDAO {
	 private final NewsMapper newsMapper;

	   
}
