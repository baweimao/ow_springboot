package com.dongzhi.ow.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.dongzhi.ow.dao.GameTableDAO;
import com.dongzhi.ow.pojo.Game;
import com.dongzhi.ow.pojo.GameTable;
import com.dongzhi.ow.util.Page4Navigator;

/**
 * @ClassName:     GameTableService.java
 * @Description:   在Service层启用redis缓存, 避免数据不同步，增删改重置所有缓存
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午9:56:07
 */
@Service
@CacheConfig(cacheNames="gametables")
public class GameTableService {

	@Autowired GameTableDAO gameTableDAO;
	@Autowired GameService gameService;
	
	@CacheEvict(allEntries=true)
	public void add(GameTable bean) {
		gameTableDAO.save(bean);
	}
	
	@CacheEvict(allEntries=true)
	public void delete(int id) {
		gameTableDAO.delete(id);
	}

	@Cacheable(key="'gametables-one-'+#p0")
	public GameTable get(int id) {
		return gameTableDAO.findOne(id);
	}
	
	@CacheEvict(allEntries=true)
	public void update(GameTable bean) {
		gameTableDAO.save(bean);
	}
	
	@Cacheable(key="'gametables-all'")
	public List<GameTable> list(){
		Sort sort = new Sort(Direction.DESC, "gameDate");
		return gameTableDAO.findAll(sort);
	}
	
	@Cacheable(key="'gametables-game-'+#p0")
	public List<GameTable> listByGame(int gid){
		Game game = gameService.get(gid);
		Sort sort = new Sort(Direction.DESC, "gameDate");
		return gameTableDAO.findByGame(game, sort);
	}
	
	@Cacheable(key="'gametables-game-page-'+#p0+ '-' + #p1+ '-' + #p2")
	public Page4Navigator<GameTable> listByGame(int gid, int start, int size, int navigatePages){
		Game game = gameService.get(gid);
		Sort sort = new Sort(Direction.DESC, "gameDate");
		Pageable pageable = new PageRequest(start, size, sort);
		Page pageFromJPA = gameTableDAO.findByGame(game, pageable);
		return new Page4Navigator<>(pageFromJPA, navigatePages);
	}
	
	@Cacheable(key="'gametables-game-date-'+#p0+ '-' + #p1+ '-' + #p2")
	public List<GameTable> listByGameAndDate(int gid, Date d1, Date d2){
		Game game = gameService.get(gid);
		Sort sort = new Sort(Direction.ASC, "gameDate");
		return gameTableDAO.findByGameAndGameDateBetween(game, d1, d2, sort);
	}
}
