package com.dashboard.service;

import com.dashboard.domain.Peanut_account;

public interface Peanut_accountService {
	/*
	 * It will create Peanut account for the user immediately after the user registers.
	 */
	public Peanut_account createAccount(Peanut_account p, Object attribute);
}
