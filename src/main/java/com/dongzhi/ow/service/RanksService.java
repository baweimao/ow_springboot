package com.dongzhi.ow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.dongzhi.ow.dao.RanksDAO;
import com.dongzhi.ow.pojo.Game;
import com.dongzhi.ow.pojo.GameTable;
import com.dongzhi.ow.pojo.Live;
import com.dongzhi.ow.pojo.Ranks;

/**
 * @ClassName:     RanksService.java
 * @Description:   在Service层启用redis缓存, 避免数据不同步，增删改重置所有缓存
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午10:20:07
 */
@Service
@CacheConfig(cacheNames="ranks")
public class RanksService {

	public final int hide = 0;//隐藏类
	
	@Autowired RanksDAO ranksDAO;
	@Autowired GameService gameService;
	
	@CacheEvict(allEntries=true)
	public void add(Ranks bean) {
		ranksDAO.save(bean);
	}
	
	@CacheEvict(allEntries=true)
	public void delete(int id) {
		ranksDAO.delete(id);
	}

	@Cacheable(key="'ranks-one-'+#p0")
	public Ranks get(int id) {
		return ranksDAO.findOne(id);
	}
	
	@CacheEvict(allEntries=true)
	public void update(Ranks bean) {
		ranksDAO.save(bean);
	}

	@Cacheable(key="'ranks-all'")
	public List<Ranks> list() {
		Sort sort = new Sort(Direction.ASC,"ranksOrder");
		return ranksDAO.findAll(sort);
	}
	
	@Cacheable(key="'ranks-game-'+#p0")
	public List<Ranks> listByGame(int gid) {
		Game game = gameService.get(gid);
		Sort sort = new Sort(Direction.ASC,"ranksOrder");
		return ranksDAO.findByGame(game, sort);
	}
	
	@Cacheable(key="'ranks-game-order-'+#p0+ '-' + #p1")
	public List<Ranks> listByGameAndOrder(int gid, int order) {
		Game game = gameService.get(gid);
		return ranksDAO.findByGameAndRanksOrder(game, order);
	}
	
	@Cacheable(key="'ranks-game-notorder-'+#p0+ '-' + #p1")
	public List<Ranks> listByGameAndOrderNot(int gid, int order) {
		Game game = gameService.get(gid);
		Sort sort = new Sort(Direction.ASC,"ranksOrder");
		return ranksDAO.findByGameAndRanksOrderNot(game, order, sort);
	}

	/**
	 * @Description:  避免序列化死循环，移除Rank中的gid属性
	 * @param:        @param rs    
	 * @return:       void
	 */
	public void removeGameFromRanks(List<Ranks> rs) {
		for(Ranks r : rs) {
			removeGameFromRanks(r);
		}
	}
	
	public void removeGameFromRanks(Ranks r) {
		if(null!=r) {
			r.setGame(null);
		}
	}
}
