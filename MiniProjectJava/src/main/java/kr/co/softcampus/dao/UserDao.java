package kr.co.softcampus.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.softcampus.beans.UserBean;
import kr.co.softcampus.mapper.UserMapper;

@Repository
public class UserDao {

	@Autowired
	private UserMapper userMapper;
	
	// 회원가입 > 중복확인
	public String checkUserIdExist(String user_id) {
		return userMapper.checkUserIdExist(user_id);
	}
	
	// 회원가입
	public void addUserInfo(UserBean joinUserBean) {
		userMapper.addUserInfo(joinUserBean);
	}
	
	// 로그인
	public UserBean getLoginUserInfo(UserBean tempLoginUserInfo) {
		return userMapper.getLoginUserInfo(tempLoginUserInfo);
	}
	
	// 회원수정 > 로그인 유저 정보 select
	public UserBean getModifyUserInfo(int user_idx) {
		return userMapper.getModifyUserInfo(user_idx);
	}
	
	// 회원수정 > 비밀번호 변경
	public void modifyUserInfo(UserBean modifyUserBean) {
		userMapper.modifyUserInfo(modifyUserBean);
	}
	
}
