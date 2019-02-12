package com.dongzhi.ow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.dongzhi.ow.dao.FootDAO;
import com.dongzhi.ow.pojo.Foot;

/**
 * @ClassName:     FootService.java
 * @Description:   在Service层启用redis缓存, 避免数据不同步，增删改重置所有缓存
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午9:55:31
 */
@Service
@CacheConfig(cacheNames="foots")
public class FootService {

	@Autowired FootDAO footDAO;
	
	@Cacheable(key="'foots-one-'+#p0")
	public Foot get(int id) {
		return footDAO.findOne(id);
	}
	
	@CacheEvict(allEntries=true)
	public void update(Foot bean) {
		footDAO.save(bean);
	}
	
	@Cacheable(key="'foots-all'")
	public List<Foot> list() {
		return footDAO.findAll();
	}
}
