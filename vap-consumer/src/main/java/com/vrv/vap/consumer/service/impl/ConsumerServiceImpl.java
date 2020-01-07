package com.vrv.vap.consumer.service.impl;

import com.vrv.vap.consumer.model.User;
import com.vrv.vap.consumer.service.ConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: liujinhui
 * @Date: 2020/1/7 14:47
 */
@Service
public class ConsumerServiceImpl implements ConsumerService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestTemplate restTemplate;

    /**
     * unless： 如果返回的结果为空，则标识不缓存
     * redis存储的类型为zset，存储的值为返回的值
     *
     * @param key， key为在config中定义的cachename的值，否则就不能自定义缓存失效时间
     * @return String
     *
     * TODO 一定要与 RedisAutoConfiguration 类中 private RedisCacheConfiguration getDefConf() 定义的键值一致， 不需要value值一样，只是需要key一样即可
     *  如果发送的请求是/v1/echo/2222 , 在redis中的key为  cache::qax:aaa:2222
     *  RedisAutoConfiguration.getDefConf() 定义了  .computePrefixWith(cacheName -> "cache".concat(":").concat(cacheName).concat(":"))
     *  "cache" 为前缀
     */
    @Override
    @Cacheable(value = "qax", key = "'aaa:'+#key", unless = "#result==null")
    public String cacheQax(String key) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(new Date());
        logger.info(" 进入缓存队列，cacheable create. key为cache::qax:aaa:2222" + key + ", " + format);
        String object = restTemplate.getForObject("http://vap-producer/v1/echo/" + key, String.class);
        return object;
    }

    /**
     *  自定义时间为300秒
     * @param key
     * @return
     */
    @Override
    @Cacheable(value = "menu", key = "'bbb:'+#key", unless = "#result==null")
    public String cacheMenu(String key) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(new Date());
        logger.info(" 进入缓存队列，cacheable create. key:menu"  + ", " + format);
        String object = restTemplate.getForObject("http://vap-producer/v1/echo/" + key, String.class);
        return object;
    }


    /**
     * 删除缓存失效
     * @param id
     * @return
     */
    @CacheEvict(value = "qax", key = "'aaa:'+#key")
    public String delete(@PathVariable Long id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info(sdf.format(new Date()) + " : Cacheable delete,  query id is " + id);
        logger.info("----------->id:" + id);
        return null;
    }

    /**
     *  更新
     * @param user
     * @return
     */
    @CachePut(value = "qax", key = "'aaa:'+#user.id")
    public User update(@RequestBody User user) {
        User s = new User();
        s.setId(user.getId());
        s.setUsername(user.getUsername());
        logger.info("更新:" + s.toString());
        return s;
    }
}
