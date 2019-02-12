package com.dongzhi.ow.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dongzhi.ow.pojo.People;
import com.dongzhi.ow.pojo.Type;

public interface PeopleDAO extends JpaRepository<People, Integer>{

	List<People> findByType(Type type, Sort sort);
	List<People> findByTypeAndPeopleOrder(Type type, int order);
}
