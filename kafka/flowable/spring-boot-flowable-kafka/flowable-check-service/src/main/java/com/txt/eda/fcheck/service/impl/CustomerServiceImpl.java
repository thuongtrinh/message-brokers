package com.txt.eda.fcheck.service.impl;

import com.txt.eda.fcheck.common.JsonHelper;
import com.txt.eda.fcheck.repositories.FCheckConfigRepository;
import com.txt.eda.fcheck.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    public static final String CUSTOMER = "CUSTOMER-SERVICE";

    @Autowired
    public Environment env;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JsonHelper jsonHelper;

    @Autowired
    private FCheckConfigRepository amlConfigRepository;

}
