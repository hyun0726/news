package com.korea.news.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.korea.news.dto.NewsDTO;
import com.korea.news.service.LoginService;
import com.korea.news.service.NewsService;
import com.korea.news.util.TimeUtils;
import com.korea.news.vo.UserVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {

	private final NewsService newsService;
	private final LoginService loginService;
	
	@Autowired
	HttpSession session;

    @GetMapping("/home")
    public String home(Model model) {
         // 사용자 ID를 세션에서 가져와 모델에 추가
         String userId = (String) session.getAttribute("userId");
         model.addAttribute("userId", userId);
        
    	 List<NewsDTO> politics = newsService.fetchNews("속보").stream()
                 .filter(news -> news.getImageUrl() != null && !news.getImageUrl().isEmpty())
                 .collect(Collectors.toList());
         List<NewsDTO> entertainments = newsService.fetchNews("연예").stream()
                 .filter(news -> news.getImageUrl() != null && !news.getImageUrl().isEmpty())
                 .collect(Collectors.toList());
         List<NewsDTO> sports = newsService.fetchNews("스포츠").stream()
                 .filter(news -> news.getImageUrl() != null && !news.getImageUrl().isEmpty())
                 .collect(Collectors.toList());
         
         // 메인 화면에 표시할 뉴스 항목 선택
         List<NewsDTO> selectedPolitics = politics.size() > 7 ? politics.subList(0, 7) : politics;
         List<NewsDTO> selectedEntertainments = entertainments.size() > 6 ? entertainments.subList(0, 6) : entertainments;
         List<NewsDTO> selectedSports = sports.size() > 6 ? sports.subList(0, 6) : sports;

         // 선택된 뉴스 항목을 DB에 저장
         newsService.saveAllNews(selectedPolitics, "속보");
         newsService.saveAllNews(selectedEntertainments, "연예");
         newsService.saveAllNews(selectedSports, "스포츠");
        
        model.addAttribute("politics", politics);
        model.addAttribute("entertainments", entertainments);
        model.addAttribute("sports", sports);
        
        TimeUtils.CurrentDate(model);
        
        return "news/home";
    }
    	
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String newsId, Model model) {
        // 사용자 ID를 세션에서 가져와 모델에 추가
        String userId = (String) session.getAttribute("userId");
        model.addAttribute("userId", userId);

        // 1. newsId로 뉴스 정보 가져오기
        NewsDTO news = newsService.findById(newsId);
        model.addAttribute("news", news);       
        
        // 2. 뉴스의 키워드 가져오기
        String keyword = newsService.findKeywordById(newsId);
        
        // 3. 키워드로 관련 뉴스 가져오기 (외부 API 호출)
        List<NewsDTO> recommendedNews = newsService.fetchNews(keyword).stream()
                .filter(n -> n.getImageUrl() != null && !n.getImageUrl().isEmpty())
                .collect(Collectors.toList());

        // 추천 뉴스 중 최대 10개 선택
        List<NewsDTO> selectedRecommendedNews = recommendedNews.size() > 10 ? recommendedNews.subList(0, 10) : recommendedNews;

        // 4. 추천 뉴스 저장
        newsService.saveAllNews(selectedRecommendedNews, keyword);
        
        // 5. 추천 뉴스를 모델에 추가
        model.addAttribute("relatedNews", selectedRecommendedNews);
        
        // 6. 현재 날짜를 모델에 추가
        TimeUtils.CurrentDate(model);

        return "news/detail";
    }

    //로그인화면
  	@GetMapping("/login")
  	 public String login(@ModelAttribute("vo") UserVO vo, Model model) {
  		TimeUtils.CurrentDate(model);
  	     return "news/login"; 
  	 }
  	
  	@PostMapping("/access")
	@ResponseBody
	public String login(@RequestBody String body) {
		ObjectMapper om = new ObjectMapper();
		Map<String, String> data =null;
		try {
			data = om.readValue(body, new TypeReference<Map<String, String>>(){});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String id = data.get("id");
		String pwd = data.get("pwd");
		
		UserVO vo = loginService.loginCheck(id);
		
		if(vo == null || !vo.getPwd().equals(pwd)) {
			return "{\"param\":\"no\"}";
		}
		
		session.setAttribute("userId", vo.getId());
		return "{\"param\":\"yes\"}";
	}
  	
  	//로그아웃
  	@GetMapping("/logout")
	public RedirectView logout() {
		session.removeAttribute("userId");
		return new RedirectView("/news/home");
	}
  	
  	//회원가입화면
  	 @GetMapping("/register")
  	 public String register(@ModelAttribute("vo") UserVO vo, Model model) {
  		TimeUtils.CurrentDate(model);
  	     return "news/register"; 
  	  }
  	 
  	@PostMapping("/check_id")
	@ResponseBody
	public String check_id(@RequestBody String id) {
		ObjectMapper om = new ObjectMapper();
		Map<String, String> data =null;
		try {
			data = om.readValue(id, new TypeReference<Map<String, String>>(){});
		} catch (Exception e) {
			e.printStackTrace();
		}
		id = data.get("id");
	
		UserVO vo = loginService.idCheck(id);

		if(vo==null) {
			return "{\"param\":\"yes\"}";
		}else {
			return "{\"param\":\"no\"}";
		}
	}
  	
  	@PostMapping("/join")
	public RedirectView join(UserVO vo) {
		int res = loginService.insert(vo);
		if(res > 0) {
			return new RedirectView("/news/login");
		}
		return null;
	}
  	 
  	// 날씨 화면
  	@GetMapping("/weather")
  	public String weather(Model model) {
  		// 사용자 ID를 세션에서 가져와 모델에 추가
        String userId = (String) session.getAttribute("userId");
        model.addAttribute("userId", userId);

  		TimeUtils.CurrentDate(model);
  	    return "weather/weather";
  	}
}
