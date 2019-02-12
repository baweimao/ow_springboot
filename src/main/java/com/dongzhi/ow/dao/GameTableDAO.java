package com.dongzhi.ow.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dongzhi.ow.pojo.Game;
import com.dongzhi.ow.pojo.GameTable;

public interface GameTableDAO extends JpaRepository<GameTable, Integer>{

	List<GameTable> findByGame(Game game, Sort sort);
	Page<GameTable> findByGame(Game game, Pageable pageable);
	List<GameTable> findByGameAndGameDateBetween(Game game, Date date1,Date date2, Sort sort);
}
