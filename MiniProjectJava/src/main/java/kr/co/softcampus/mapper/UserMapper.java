package kr.co.softcampus.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.co.softcampus.beans.UserBean;

public interface UserMapper {

	// 아이디 중복 체크
	@Select("select user_name from user_table where user_id = #{user_id}")
	String checkUserIdExist(String user_id);
	
	// 회원가입
	@Insert("insert into user_table (user_idx, user_name, user_id, user_pw) values " + 
			"(user_seq.nextval, #{user_name}, #{user_id}, #{user_pw})")
	void addUserInfo(UserBean joinUserBean);
	
	// 로그인
	@Select("select user_idx, user_name from user_table "+ 
			"where user_id = #{user_id} and user_pw = #{user_pw}")
	UserBean getLoginUserInfo(UserBean tempLoginUserBean);
	
	@Select("select user_id, user_name from user_table " +
			"where user_idx = #{user_idx}")
	UserBean getModifyUserInfo(int user_idx);
	
	@Update("update user_table set user_pw = #{user_pw} where user_idx = #{user_idx}")
	void modifyUserInfo(UserBean modifyUserBean);
	
}
