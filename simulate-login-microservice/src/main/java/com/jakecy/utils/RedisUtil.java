package com.jakecy.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {

	@Autowired
	private StringRedisTemplate template;

	/**
	 * 存String
	 * 
	 * @param key
	 * @param value
	 * @param expire
	 * @author chihaojie 2018年5月5日
	 */
	public void set(String key, String value, long expire) {
		string().set(key, value, expire, TimeUnit.SECONDS);
	}
	

	/**
	 * 给指定的key设置生存时间 以秒为单位
	 *
	 * @param key
	 * @param seconds
	 * @author liyuanyuan 2018年12月07日
	 */
	public Long expire(String key, int seconds) {
		boolean success = template.expire(key,seconds, TimeUnit.SECONDS);
		return success ? 1l : 0l;
	}

	/**
	 * 存String
	 * 
	 * @param key
	 * @param value
	 * @author chihaojie 2018年5月5日
	 */
	public void set(String key, String value) {
		string().set(key, value);
	}

	/**
	 * 对应setnx命令
	 * 
	 * SET if Not eXists』(如果不存在，则 SET)
	 *    
	 * @param key
	 * @param value      
	 * @return 
	 * @return: void
	 */
	public Boolean setIfAbsent(String key, String value) {
		return string().setIfAbsent(key, value);
	}
	
	/**
	 * 获取字符串
	 * 
	 * @param key
	 * @return
	 * @author chihaojie 2018年5月2日
	 */
	public String get(String key) {
		String result = string().get(key);
		return result;
	}

	/**
	 * 返回key的值，并将key的值更新为value
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @author chihaojie 2018年5月10日
	 */
	public String getAndSet(String key, String value) {
		return string().getAndSet(key, value);
	}

	/**
	 * 如果 key 已经存在，并且值为字符串，那么这个命令会把 value 追加到原来值（value）的结尾。 如果 key
	 * 不存在，那么它将首先创建一个空字符串的key，再执行追加操作
	 * 
	 * @param key
	 * @param value
	 * @return 返回append后字符串值（value）的长度
	 * @author chihaojie 2018年5月10日
	 */
	public int appendString(String key, String value) {
		return string().append(key, value);
	}

	/**
	 * 将key对应的数字加value。如果key不存在，操作之前，key就会被置为0。如果key的value类型错误或者是个不能表示成数字的字符串，就返回错误。这个操作最多支持64位有符号的正型数字。
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @author chihaojie 2018年5月10日
	 */
	public long increment(String key, long value) {
		return string().increment(key, value);
	}

	/**
	 * 将key对应的数字加value。如果key不存在，操作之前，key就会被置为0。如果key的value类型错误或者是个不能表示成数字的字符串，就返回错误。这个操作最多支持64位有符号的正型数字。
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @author chihaojie 2018年5月10日
	 */
	public double increment(String key, double value) {
		return string().increment(key, value);
	}

	/**
	 * 返回所有指定的key的value。对于每个不对应string或者不存在的key，都返回特殊值nil
	 * 
	 * @param keys
	 * @return
	 * @author chihaojie 2018年5月10日
	 */
	public List<String> multiGet(List<String> keys) {
		return string().multiGet(keys);
	}

	/**
	 * 对应给定的keys到他们相应的values上。MSET会用新的value替换已经存在的value
	 * 
	 * @param map
	 * @author chihaojie 2018年5月10日
	 */
	public void multiSet(Map<String, String> map) {
		string().multiSet(map);
	}

	/**
	 * 删除指定的key
	 * 
	 * @param key
	 * @author chihaojie 2018年7月5日
	 */
	public void delete(String key) {
		template.delete(key);
	}

	/**
	 * 删除指定的多个key
	 * 
	 * @param keys
	 * @author chihaojie 2018年7月5日
	 */
	public void delete(Collection<String> keys) {
		template.delete(keys);
	}
	
	/**
	 * 获取超时时间
	 *    
	 * @param key
	 * @return: Long
	 */
	public Long getExpire(String key) {
		return template.getExpire(key,TimeUnit.SECONDS);
	}

	/**
	 * 获取操作string的类
	 * 
	 * @return
	 * @author chihaojie 2018年5月10日
	 */
	private ValueOperations<String, String> string() {
		return template.opsForValue();
	}

}
