package com.dashboard.rm;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.dashboard.domain.Application;

/**
 * 
 * It maps one database record to one domain (Application) object.
 */

public class ApplicationRowMapper implements RowMapper<Application> {

	public Application mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Application a = new Application();
		a.setApp_id(rs.getInt("app_id"));
        a.setName(rs.getString("name"));
		a.setUserId(rs.getInt("userId"));
		a.setAppDesc("appDescription");
		a.setImageLocation("imageLocation");
        return a;
		
	}

}
