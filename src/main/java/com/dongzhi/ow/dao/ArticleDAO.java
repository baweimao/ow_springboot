package com.dongzhi.ow.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dongzhi.ow.pojo.Article;

public interface ArticleDAO extends JpaRepository<Article, Integer>{

	List<Article> findByUp(int up, Sort sort);
	Page<Article> findByUp(int up, Pageable pageable);
	List<Article> findByUpAndArticleOrder(int up, int articleorder);
	
	List<Article> findByUpAndArticleOrderNot(int up, int articleorder, Sort sort);
	Page<Article> findByUpNotAndArticleOrderNot(int up, int articleorder, Pageable pageable);
	List<Article> findByUpNotAndArticleOrderNot(int up, int articleorder, Sort sort);
}
