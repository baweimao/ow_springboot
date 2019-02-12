package com.dongzhi.ow.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dongzhi.ow.pojo.People;
import com.dongzhi.ow.pojo.Social;

public interface SocialDAO extends JpaRepository<Social, Integer>{

	List<Social> findByPeople(People people, Sort sort);
	List<Social> findByPeopleAndSocialOrder(People people, int order);
}
