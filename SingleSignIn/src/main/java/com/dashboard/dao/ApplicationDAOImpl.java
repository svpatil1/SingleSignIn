package com.dashboard.dao;

import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.dashboard.domain.Application;
import com.singlesignin.dao.BaseDAO;


@Repository
public class ApplicationDAOImpl extends BaseDAO implements ApplicationDAO {
	
	@Override
	public Application save(Application a) {
		/**
		 *  This will save the application information in database
		 */
		String name = a.getName();
		Integer userId = a.getUserId();
		String imageLocation = a.getImageLocation();
		String appDescription = a.getAppDesc();
		
		String sql = "INSERT INTO application(name, userId, imageLocation, appDescription)"
                + " VALUES(:name, :userId, :imageLocation, :appDescription)";
		
		Map m = new HashMap();
		m.put("name", name);
		m.put("userId", userId);
		m.put("imageLocation", imageLocation);
		m.put("appDescription", appDescription);
			
		 	KeyHolder kh = new GeneratedKeyHolder();
	        SqlParameterSource ps = new MapSqlParameterSource(m);
	        super.getNamedParameterJdbcTemplate().update(sql, ps, kh);
	        Integer app_id = kh.getKey().intValue();
	        a.setApp_id(app_id);
	        
	    return a;
	}

}
