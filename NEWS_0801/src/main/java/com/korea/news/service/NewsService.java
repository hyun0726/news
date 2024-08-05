package com.korea.news.service;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.korea.news.dao.NewsDAO;
import com.korea.news.list.NewsItem;
import com.korea.news.list.NewsResponse;
import com.korea.news.vo.NewsVO;

@Service
public class NewsService {
	final NewsDAO newsDAO;

    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);

    @Value("${deepsearch.api.url}")
    private String apiUrl;

    @Value("${deepsearch.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public NewsService(NewsDAO newsDAO, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.newsDAO = newsDAO;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<NewsItem> fetchNews(String keyword) {
        URI uri = UriComponentsBuilder
                .fromUriString(apiUrl)
                .path("/v1/articles")
                .queryParam("keyword", keyword)
                .queryParam("api_key", apiKey)
                .encode()
                .build()
                .toUri();

        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .build();

        ResponseEntity<String> resp;
        NewsResponse resultVO = null;

        try {
            resp = restTemplate.exchange(req, String.class);
            logger.info("Response Status: {}", resp.getStatusCode());
            logger.info("Response Body: {}", resp.getBody());
            resultVO = objectMapper.readValue(resp.getBody(), NewsResponse.class);
        } catch (JsonMappingException e) {
            logger.error("JSON Mapping Exception: ", e);
        } catch (JsonProcessingException e) {
            logger.error("JSON Processing Exception: ", e);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }

        if (resultVO == null || resultVO.getData() == null) {
            logger.warn("No data found in the response or response is null");
            return Collections.emptyList(); // 빈 리스트 반환
        }

        return resultVO.getData();
    }

    }
