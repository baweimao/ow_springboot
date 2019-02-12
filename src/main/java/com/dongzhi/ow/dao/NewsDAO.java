package com.dongzhi.ow.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dongzhi.ow.pojo.News;

public interface NewsDAO extends JpaRepository<News, Integer>{

	List<News> findByUp(int up, Sort sort);
	Page<News> findByUp(int up, Pageable pageable);
	List<News> findByUpAndNewsOrder(int up, int order);
	Page<News> findByNewsOrderNot(int order, Pageable pageable);
	List<News> findByNewsOrderNot(int order, Sort sort);
	List<News> findByUpAndNewsOrderNot(int up, int order, Sort sort);
}
