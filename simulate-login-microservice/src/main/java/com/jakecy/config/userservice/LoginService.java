package com.jakecy.config.userservice;

import com.jakecy.config.dto.AccountDetail;

public interface LoginService {

	AccountDetail  getAccountDetail(String username);
}
