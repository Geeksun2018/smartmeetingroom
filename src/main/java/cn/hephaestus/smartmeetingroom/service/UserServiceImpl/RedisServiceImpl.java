package cn.hephaestus.smartmeetingroom.service.UserServiceImpl;

import cn.hephaestus.smartmeetingroom.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;


@Service
public class RedisServiceImpl implements RedisService {


    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key,value);
    }

    @Override
    public void set(String key, String value, long expireTime) {
        redisTemplate.opsForValue().set(key, 19+"", expireTime, TimeUnit.SECONDS);
    }

    @Override
    public void hset(String hash, String key, String value) {
        redisTemplate.opsForHash().put(hash,key,value);
    }

    @Override
    public String hget(String hash, String key) {
        return (String) redisTemplate.opsForHash().get(hash,key);
    }


    @Override
    public void sadd(String key, String... values){
//        redisTemplate.opsForSet().add(key,values,100);
    }

    @Override
    public Boolean remove(String key){
        return redisTemplate.delete(key);
    }

    @Override
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }


    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
