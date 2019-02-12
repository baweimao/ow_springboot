package com.dongzhi.ow.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dongzhi.ow.pojo.Category;
import com.dongzhi.ow.pojo.Web;

public interface WebDAO extends JpaRepository<Web, Integer>{

	List<Web> findByCategory(Category category, Sort sort);
	List<Web> findByCategory(Category category, Pageable pageable);
	List<Web> findByCategoryAndWebOrder(Category category, int order);
}
