
package com.dashboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dashboard.dao.ApplicationDAO;
import com.dashboard.domain.Application;
import com.singlesignin.dao.BaseDAO;


/**
 * @author Sanika Patil
 * This class provides service implementation to register the application.
 */

@Service
public class ApplicationServiceImpl extends BaseDAO implements ApplicationService {

	@Autowired
	private ApplicationDAO applicationDAO;
	
	/*
	 * (non-Javadoc)
	 * @see com.dashboard.service.ApplicationService#registerApplication(com.dashboard.domain.Application)
	 */
	@Override
	public Application registerApplication(Application a) {
		a = applicationDAO.save(a);
		return a;
	}
	
	
}
