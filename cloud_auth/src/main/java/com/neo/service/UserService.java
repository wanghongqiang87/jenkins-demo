package com.neo.service;

import java.util.List;

import com.neo.pojo.UserLogin;

public interface UserService {

	UserLogin findByid(String name,String pwd);

	List<?> findAllMenu( String userId);
}
