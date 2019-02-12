package com.dongzhi.ow.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.dongzhi.ow.dao.GameDAO;
import com.dongzhi.ow.pojo.Category;
import com.dongzhi.ow.pojo.Game;
import com.dongzhi.ow.pojo.GameTable;
import com.dongzhi.ow.pojo.Live;
import com.dongzhi.ow.pojo.News;
import com.dongzhi.ow.pojo.Ranks;
import com.dongzhi.ow.pojo.Web;
import com.dongzhi.ow.util.SpringContextUtil;

/**
 * @ClassName:     GameService.java
 * @Description:   在Service层启用redis缓存, 避免数据不同步，增删改重置所有缓存
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午9:55:50
 */
@Service
@CacheConfig(cacheNames="games")
public class GameService {

	public final int top = 0;//官方赛事
	
	public final int art = 1;//第三方赛事
	
	public final int hide = 0;//隐藏类
	
	@Autowired GameDAO gameDAO;
	@Autowired GameTableService gameTableService;
	@Autowired LiveService liveService;
	@Autowired RanksService ranksService;
	
	@CacheEvict(allEntries=true)
	public void add(Game bean) {
		gameDAO.save(bean);
	}

	@CacheEvict(allEntries=true)
	public void delete(int id) {
		gameDAO.delete(id);
	}

	@Cacheable(key="'games-one-'+#p0")
	public Game get(int id) {
		return gameDAO.findOne(id);
	}
	
	@CacheEvict(allEntries=true)
	public void update(Game bean) {
		gameDAO.save(bean);
	}

	@Cacheable(key="'games-all'")
	public List<Game> list() {
		Sort sort = new Sort(Direction.ASC,"gameOrder");
		return gameDAO.findAll(sort);
	}
	
	@Cacheable(key="'games-up-order-'+#p0+ '-' + #p1")
	public List<Game> listByUpAndOrder(int up, int gameorder) {
		return gameDAO.findByUpAndGameOrder(up, gameorder);
	}
	
	@Cacheable(key="'games-up-'+#p0")
	public List<Game> listByUp(int up) {
		Sort sort = new Sort(Direction.ASC,"gameOrder");
		return gameDAO.findByUp(up, sort);
	}
	
	@Cacheable(key="'games-up-notorder-'+#p0+ '-' + #p1")
	public List<Game> listByUpAndOrderNot(int up, int gameorder) {
		Sort sort = new Sort(Direction.ASC,"gameOrder");
		return gameDAO.findByUpAndGameOrderNot(up, gameorder, sort);
	}
	
	/**
	 * @Description:  根据时间查询并初始化首页赛事表GameTable和直播Live
	 * @param:        @param date
	 * @param:        @return    
	 * @return:       List<Game>
	 */
	public List<Game> fillGameTableAndLive(Date date) {
		Date d1 = DateUtils.truncate(date, Calendar.DATE);
		Calendar c = new GregorianCalendar();
		c.setTime(d1);
		c.add(Calendar.DAY_OF_MONTH, 1);
		Date d2 = c.getTime();
		
		List<Game> gs = list();
		List<Game> noEmptyGs = new ArrayList<Game>();

		for(Game g:gs) {
			fillGameTableAndLive(noEmptyGs, g, d1, d2);
		}
		return noEmptyGs;
	}

	public void fillGameTableAndLive(List<Game> noEmptyGs, Game g, Date d1, Date d2){
		GameTableService gameTableService = SpringContextUtil.getBean(GameTableService.class);
		LiveService liveService = SpringContextUtil.getBean(LiveService.class);
		
		int gid = g.getId();
		List<GameTable> gts = gameTableService.listByGameAndDate(gid, d1, d2);

		if(!gts.isEmpty()) {
			g.setGts(gts);
			List<Live> ls = liveService.listByGame(gid);
			g.setLs(ls);
			noEmptyGs.add(g);
		}
	}
	
	/**
	 * @Description:  初始化每个赛事的队伍集合
	 * @param:        @param gs    
	 * @return:       void
	 */
	public void fillGameRanks(List<Game> gs) {
		
		for(Game g:gs) {
			int gid = g.getId();
			List<Ranks> rs = ranksService.listByGame(gid);
			g.setRs(rs);
		}
	}
	
	/**
	 * @Description:  避免序列化死循环，移除Game中GameTable的gid属性, 移除GameTable中Ranks的gid属性
	 * @param:        @param gs    
	 * @return:       void
	 */
	public void removeGameFromGameTableLive(List<Game> gs) {
		for(Game g : gs) {
			removeGameFromGameTableLive(g);
		}
	}
	
	public void removeGameFromGameTableLive(Game g) {
		List<GameTable> gts = g.getGts();
		List<Live> ls = g.getLs();

		if(null!=gts) {
			for(GameTable gt : gts) {
				Ranks ra = gt.getRanks_a();
				ra.setGame(null);
				Ranks rb = gt.getRanks_b();
				rb.setGame(null);
				gt.setGame(null);
			}
		}
		if(null!=ls) {
			for(Live l : ls) {
				l.setGame(null);
			}
		}
	}
	
	public void removeGameFromRanks(List<Game> gs) {
		for(Game g : gs) {
			removeGameFromRanks(g);
		}
	}
	
	public void removeGameFromRanks(Game g) {
		List<Ranks> rs = g.getRs();
		for(Ranks r : rs) {
			r.setGame(null);
		}
	}
}
