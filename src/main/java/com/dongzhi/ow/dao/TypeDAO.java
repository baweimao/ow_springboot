package com.dongzhi.ow.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dongzhi.ow.pojo.Type;

public interface TypeDAO extends JpaRepository<Type, Integer>{

	List<Type> findByTypeOrder(int order);
	List<Type> findByTypeOrderNot(int order,Sort sort);
}
