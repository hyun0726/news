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
public class NewsController {
	
	private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }
	
	//메인화면
	@GetMapping("/home")
	 public String home(Model model) {
		List<NewsItem> politics = newsService.fetchNews("속보");  
		if (politics.size() > 7) {
			politics = politics.subList(0, 7);
        }
		List<NewsItem> entertainments = newsService.fetchNews("연예");    
		if (entertainments.size() > 6) {
			entertainments = entertainments.subList(0, 6);
        }
        List<NewsItem> sports  = newsService.fetchNews("스포츠"); 
        if (sports.size() > 6) {
        	sports = sports.subList(0, 6);
        }
        model.addAttribute("politics", politics);
        model.addAttribute("entertainments", entertainments);
        model.addAttribute("sports", sports);
		
		
		
		//현재 시간 
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String formattedDate = now.format(formatter);

        model.addAttribute("currentDate", formattedDate);
        return "news/home";  
	 }
	
	//로그인화면
	@GetMapping("/login")
	 public String login() {
	     return "login"; 
	 }
	//회원가입화면
	 @GetMapping("/register")
	 public String register() {
	     return "register"; 
	  }	   
	 // 뉴스 화면
	 @GetMapping("/news")
	 public String news() {
	     return "news"; 
	  }

	  // 연예 화면
	  @GetMapping("/entertainment")
	  public String entertainment() {
	      return "entertainment"; 
	  }

	  // 스포츠 화면
	  @GetMapping("/sports")
	  public String sports() {
	      return "sports"; 
	  }

	  // 날씨 화면
	  @GetMapping("/weather")
	  public String weather() {
	      return "weather";
	  }

}	  
	 
	 


