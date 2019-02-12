package com.dongzhi.ow.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dongzhi.ow.pojo.Game;

public interface GameDAO extends JpaRepository<Game, Integer>{

	List<Game> findByUp(int up, Sort sort);
	List<Game> findByUpAndGameOrder(int up, int order);
	List<Game> findByUpAndGameOrderNot(int up, int order, Sort sort);
}
