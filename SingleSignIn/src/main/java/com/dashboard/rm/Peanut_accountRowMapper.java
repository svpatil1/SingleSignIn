package com.dashboard.rm;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.dashboard.domain.Peanut_account;

public class Peanut_accountRowMapper implements RowMapper<Peanut_account> {

	public Peanut_account mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Peanut_account p = new Peanut_account();
		p.setAcc_id(rs.getInt("accId"));
		p.setAvailable_peanuts(rs.getInt("Available_peanuts"));
		p.setUserId(rs.getInt("userId"));
        return p;
		
	}

}
