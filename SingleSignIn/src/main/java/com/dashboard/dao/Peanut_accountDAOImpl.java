package com.dashboard.dao;

import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.dashboard.domain.Peanut_account;
import com.singlesignin.dao.BaseDAO;

@Repository
public class Peanut_accountDAOImpl extends BaseDAO implements Peanut_accountDAO {

	@Override
	public Peanut_account save(Peanut_account p, Object attribute) {
		String sql = "INSERT INTO peanut_account(available_peanuts, userId)"
                + " VALUES(:available_peanuts, :userId)";
		Integer def = 1000;
		Map m = new HashMap();
		m.put("available_peanuts", def);
		m.put("userId", attribute);		
		
		
		 KeyHolder kh = new GeneratedKeyHolder();
	        SqlParameterSource ps = new MapSqlParameterSource(m);
	        super.getNamedParameterJdbcTemplate().update(sql, ps, kh);
	        Integer acc_id = kh.getKey().intValue();
	        p.setAcc_id(acc_id);
	        
	        return p;
	}		

}
