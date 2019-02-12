package com.dongzhi.ow.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dongzhi.ow.pojo.User;

public interface UserDAO extends JpaRepository<User, Integer>{

	List<User> findByName(String name);
	List<User> findByNameAndPassword(String name, String password);
}
