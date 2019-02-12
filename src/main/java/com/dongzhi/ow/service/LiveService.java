package com.dongzhi.ow.service;

import java.util.List;

import org.hibernate.criterion.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.dongzhi.ow.dao.LiveDAO;
import com.dongzhi.ow.pojo.Game;
import com.dongzhi.ow.pojo.GameTable;
import com.dongzhi.ow.pojo.Live;

/**
 * @ClassName:     LiveService.java
 * @Description:   在Service层启用redis缓存, 避免数据不同步，增删改重置所有缓存
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午10:09:51
 */
@Service
@CacheConfig(cacheNames="lives")
public class LiveService {

	@Autowired LiveDAO liveDAO;
	@Autowired GameService gameService;
	
	@CacheEvict(allEntries=true)
	public void add(Live bean) {
		liveDAO.save(bean);
	}
	
	@CacheEvict(allEntries=true)
	public void delete(int id) {
		liveDAO.delete(id);
	}

	@Cacheable(key="'lives-one-'+#p0")
	public Live get(int id) {
		return liveDAO.findOne(id);
	}
	
	@CacheEvict(allEntries=true)
	public void update(Live bean) {
		liveDAO.save(bean);
	}

	@Cacheable(key="'lives-all'")
	public List<Live> list() {
		return liveDAO.findAll();
	}
	
	@Cacheable(key="'lives-game-'+#p0")
	public List<Live> listByGame(int gid) {
		Game game = gameService.get(gid);
		Sort sort = new Sort(Direction.ASC,"liveOrder");
		return liveDAO.findByGame(game, sort);
	}

	@Cacheable(key="'lives-game-order-'+#p0+ '-' + #p1")
	public List<Live> listByGameAndOrder(int gid, int order) {
		Game game = gameService.get(gid);
		return liveDAO.findByGameAndLiveOrder(game, order);
	}
}
