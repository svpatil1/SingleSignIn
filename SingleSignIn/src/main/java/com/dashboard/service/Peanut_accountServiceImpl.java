
package com.dashboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dashboard.dao.Peanut_accountDAO;
import com.dashboard.domain.Peanut_account;
import com.singlesignin.dao.BaseDAO;

/**
 * @author Sanika Patil
 * This class provides service implementation to create the peanut_account.
 */

@Service
public class Peanut_accountServiceImpl extends BaseDAO implements Peanut_accountService {
	
	@Autowired
	private Peanut_accountDAO peanut_accountDAO;

	@Override
	public Peanut_account createAccount(Peanut_account p, Object attribute) {
		 p = peanut_accountDAO.save(p, attribute);
		 return p;
	}

}
