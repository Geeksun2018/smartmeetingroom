package cn.hephaestus.smartmeetingroom.service;

public interface RedisService {
    /**
     * 将 key，value 存放到redis数据库中，默认设置过期时间为一周
     *
     * @param key
     * @param value
     */
    public void set(String key, String value);

    /**
     * 将 key，value 存放到redis数据库中，设置过期时间单位是秒
     *
     * @param key
     * @param value
     * @param expireTime
     */
    public void set(String key, String value, long expireTime);

    public void hset(String hash,String key,String value);

    public String hget(String hash,String key);


    /**
     * 判断 key 是否在 redis 数据库中
     *
     * @param key
     * @return
     */
    public boolean exists(String key) ;

    public Boolean remove(String key);


    /**
     * 获取 key 对应的对象
     * @param key
     * @return
     */
    public Object get(String key) ;

    /**
     * 向集合中添加某元素
     */

    public void sadd(String key,String... arr);
}
