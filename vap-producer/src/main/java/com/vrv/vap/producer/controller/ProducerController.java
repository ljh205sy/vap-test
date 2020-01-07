package com.vrv.vap.producer.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: liujinhui
 * @Date: 2020/1/6 16:35
 */
@RestController
@RequestMapping("/v1")
@Api(tags = "需求管理")
public class ProducerController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/producer")
    @ApiOperation(value = "producer查询", notes = "查询")
    public String hello() {
        logger.info("111");
        return "1";
    }
}

