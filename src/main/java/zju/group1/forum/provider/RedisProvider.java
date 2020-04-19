package zju.group1.forum.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
public class RedisProvider {
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private String redisPort;

    @Value("${spring.redis.expire}")
    private String redisExpire;

    private JedisPool getRedisPool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(10);
        return new JedisPool(jedisPoolConfig, redisHost, Integer.parseInt(redisPort));
    }

    public String getAuthorizedName(String token) {
        JedisPool pool = getRedisPool();
        Jedis jedis = pool.getResource();
        String username = jedis.get(token);
        if (username == null)
            return null;

        jedis.expire(token, Integer.parseInt(redisExpire));
        jedis.close();
        pool.close();
        return username;
    }

    public void setAuthorizeToken(String authorizeToken, String email) {
        JedisPool pool = getRedisPool();
        Jedis jedis = pool.getResource();
        System.out.println(authorizeToken);
        jedis.set(authorizeToken, email);
        jedis.expire(authorizeToken, Integer.parseInt(redisExpire));
        jedis.close();
        pool.close();
    }
}
