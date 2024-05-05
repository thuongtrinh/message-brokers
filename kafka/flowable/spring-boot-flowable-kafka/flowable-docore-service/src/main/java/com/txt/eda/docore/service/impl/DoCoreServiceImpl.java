package com.txt.eda.docore.service.impl;

import com.txt.eda.docore.common.JsonHelper;
import com.txt.eda.docore.service.DoCoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@Slf4j
public class DoCoreServiceImpl implements DoCoreService {

    public static final String DOCORESERVICE = "DO-CORE-SERVICE";

    @Autowired
    public Environment env;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JsonHelper jsonHelper;

    @Override
    public String doSomeThing() {
        log.info("doSomeThing is running...");
        return "doSomeThing";
    }
}
