package com.basoft.service.impl.healthcheck;

import com.basoft.service.dao.healthcheck.HealthCheckMapper;
import com.basoft.service.definition.healthcheck.HealthCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HealthCheckServiceImpl implements HealthCheckService {
	@Autowired
	private HealthCheckMapper healthCheckMapper;


	@Override
	public String getData() {
		return healthCheckMapper.getData();
	}
}