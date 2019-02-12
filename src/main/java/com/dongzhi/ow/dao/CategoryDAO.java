package com.dongzhi.ow.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dongzhi.ow.pojo.Category;

public interface CategoryDAO extends JpaRepository<Category, Integer>{

	List<Category> findByCategoryOrder(int order, Sort sort);
	List<Category> findByCategoryOrder(int order, Pageable pageable);
	List<Category> findByCategoryOrderNot(int order, Sort sort);
	List<Category> findByCategoryOrderNot(int order, Pageable pageable);
}
