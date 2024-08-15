package com.korea.news.controller;

import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.korea.news.dto.NewsDTO;
import com.korea.news.service.CommentService;
import com.korea.news.service.LoginService;
import com.korea.news.service.NewsService;
import com.korea.news.util.TimeUtils;
import com.korea.news.vo.DaegulVO;
import com.korea.news.vo.UserVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {

	private final NewsService newsService;
	private final LoginService loginService;
	private final CommentService commentService;
	
	@Autowired
	HttpSession session;
	
	@Autowired
	HttpServletRequest request;


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
        System.out.println("User ID from session: " + userId);  
        model.addAttribute("userId", userId);

        // 1. newsId로 뉴스 정보 가져오기
        NewsDTO news = newsService.findById(newsId);
        model.addAttribute("news", news);
        
        // 댓글 목록 가져오기
        List<DaegulVO> comments = commentService.findCommentsByNewsId(newsId);
        model.addAttribute("comments", comments);
        
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
   // 댓글 작성
    @PostMapping("/{id}/comment")
    public RedirectView postComment(@PathVariable("id") String newsId, DaegulVO vo) {

        String userId = (String) session.getAttribute("userId");
        String ip = (String) session.getAttribute("userIp");

        vo.setUserid(userId);
        vo.setIp(ip);

        int res = commentService.insert(vo);
        return new RedirectView("/news/detail/" + newsId);
    }
    //대댓글 작성
    @PostMapping("/{id}/reply")
    public RedirectView postReply(@PathVariable("id") String newsId,
                                  DaegulVO vo,
                                  @RequestParam("num") int num) {
        String userId = (String) session.getAttribute("userId");
        String ip = (String) session.getAttribute("userIp");

        DaegulVO base_vo = commentService.selectOne(num);
        int res = commentService.update_step(base_vo);

        vo.setUserid(userId);
        vo.setIp(ip);
        vo.setRef(base_vo.getRef());
        vo.setStep(base_vo.getStep() + 1);
        vo.setDepth(base_vo.getDepth() + 1);

        res = commentService.reply(vo);
        return new RedirectView("/news/detail/" + newsId);
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
		
		String userid = data.get("userid");
		String pwd = data.get("pwd");
		
		UserVO vo = loginService.loginCheck(userid);
		
		if(vo == null || !vo.getPwd().equals(pwd)) {
			return "{\"param\":\"no\"}";
		}
		
		session.setAttribute("userId", vo.getUserid());
		session.setAttribute("userIp", request.getRemoteAddr()); 
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
	public String check_id(@RequestBody String userid) {
		ObjectMapper om = new ObjectMapper();
		Map<String, String> data =null;
		try {
			data = om.readValue(userid, new TypeReference<Map<String, String>>(){});
		} catch (Exception e) {
			e.printStackTrace();
		}
		userid = data.get("userid");
	
		UserVO vo = loginService.idCheck(userid);

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
  	
  	@PostMapping("/edit")
  	@ResponseBody
  	public Map<String, String> editComment(@RequestBody Map<String, String> data) {
  	    Map<String, String> response = new HashMap<>();
  	    try {
  	        int intNum = Integer.parseInt(data.get("num"));
  	        String updatedContent = data.get("content");

  	        // 현재 로그인한 사용자의 ID를 가져옴
  	      String userId = (String) session.getAttribute("userId");

  	        // 수정할 댓글 객체를 조회
  	        DaegulVO vo = commentService.selectOne(intNum);

  	        if (vo != null) {
  	            // 로그인한 사용자와 댓글 작성자가 같은지 확인
  	            if (vo.getUserid().equals(userId)) {
  	                // 댓글 내용 수정
  	                vo.setContent(updatedContent);
  	                int result = commentService.updateComment(vo); // updateComment 메서드로 DB에 업데이트

  	                if (result > 0) {
  	                    response.put("param", "success");
  	                } else {
  	                    response.put("param", "fail");
  	                }
  	            } 
  	        } 
  	    } catch (Exception e) {
  	        e.printStackTrace();
  	        response.put("param", "error");
  	    }
  	    return response;
  	}


  	
  	//삭제
  	@PostMapping("/delete")
  	@ResponseBody
  	public Map<String, String> delete(@RequestBody Map<String, String> data) {
  	    Map<String, String> response = new HashMap<>();
  	    try {
  	        int num = Integer.parseInt(data.get("num"));
  	        DaegulVO vo = commentService.selectOne(num);
  	      String userId = (String) session.getAttribute("userId");
  	       
  	        
  	      if (vo != null) {
	            // 로그인한 사용자와 댓글 작성자가 같은지 확인
	            if (vo.getUserid().equals(userId)) {
	           
	                int result = commentService.delete(vo);

	                if (result > 0) {
	                    response.put("param", "success");
	                } else {
	                    response.put("param", "fail");
	                }
	            } 
	        } 
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("param", "error");
	    }
	    return response;
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
