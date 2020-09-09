package com.neo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neo.mapper.UserMapper;
import com.neo.pojo.UserLogin;
import com.neo.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public UserLogin findByid(String userName, String password) {
		// TODO Auto-generated method stub
		// return userMapper.selectByNameAndPassword(userName, password);
		return new UserLogin(userName, password, "121", "55");
	}

	@Override
	public List<?> findAllMenu(String roleId) {
		// TODO Auto-generated method stub
		// findbyRoleId()
		// 1 if admin return allMenu findAllMenu()
		// 2 not admin return select m.* from t_role r , t_menu m , t_role_menu
		// rm
		// where r.id= rm.roleid and rm.menuid = m.id
		// 2.1 menu --> treeMenu
		// 2.2 user register need add role; role and menu need banding;
		return new ArrayList<>();
	}

}
