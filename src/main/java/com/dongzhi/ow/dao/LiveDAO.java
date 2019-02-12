package com.dongzhi.ow.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dongzhi.ow.pojo.Game;
import com.dongzhi.ow.pojo.Live;

public interface LiveDAO extends JpaRepository<Live, Integer>{

	List<Live> findByGame(Game game, Sort sort);
	List<Live> findByGameAndLiveOrder(Game game, int order);
}
