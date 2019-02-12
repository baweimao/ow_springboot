package com.dongzhi.ow.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dongzhi.ow.pojo.Game;
import com.dongzhi.ow.pojo.Ranks;

public interface RanksDAO extends JpaRepository<Ranks, Integer>{

	List<Ranks> findByGame(Game game, Sort sort);
	List<Ranks> findByGameAndRanksOrder(Game game, int order);
	List<Ranks> findByGameAndRanksOrderNot(Game game, int order, Sort sort);
}
