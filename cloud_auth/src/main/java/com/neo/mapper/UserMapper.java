package com.neo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.neo.pojo.UserLogin;

@Mapper
public interface UserMapper {

	@Select ( " select * from t_user where user_Name = #{arg0} and user_password = #{arg1}")
	UserLogin selectByNameAndPassword(String userName ,String password);
}
