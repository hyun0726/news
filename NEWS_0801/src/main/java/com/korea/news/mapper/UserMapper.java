package com.korea.news.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.korea.news.vo.UserVO;
@Mapper
public interface UserMapper {
	UserVO loginCheck(String id);
	UserVO idCheck(String id);
	int insert(UserVO vo);
	

	

}
