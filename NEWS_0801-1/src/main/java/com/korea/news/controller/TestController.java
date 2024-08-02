package com.korea.news.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.korea.news.list.NewsItem;
import com.korea.news.service.NewsService;

@Controller
@RequestMapping("/news/*")
public class TestController {

    private final NewsService newsService;

    @Autowired
    public TestController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/test")
    public String home(Model model) {
        List<NewsItem> korea = newsService.fetchNews("한국");  // 경제 뉴스 4개
        if (korea.size() > 4) {
            korea = korea.subList(0, 4);
        }
        List<NewsItem> stockNews = newsService.fetchNews("주식");    // 주식 뉴스 6개
        List<NewsItem> cultureNews = newsService.fetchNews("문화");  // 문화 뉴스 4개

        model.addAttribute("korea", korea);
        model.addAttribute("stockNews", stockNews);
        model.addAttribute("cultureNews", cultureNews);

        return "news/test";
    }
}

