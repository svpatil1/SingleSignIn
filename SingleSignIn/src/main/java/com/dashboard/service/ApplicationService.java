package com.dashboard.service;

import com.dashboard.domain.Application;

public interface ApplicationService {

	/*
	 * This method will register the application, when ISV uploads the new application
	 */
	public Application registerApplication(Application a);
}
