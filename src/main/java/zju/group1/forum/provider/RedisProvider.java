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

    /*本地测试用*/
    private JedisPool getLocalRedisPool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(10);
        return new JedisPool(jedisPoolConfig, "127.0.0.1", Integer.parseInt(redisPort));
    }

    public String getAuthorizedName(String token) {
        JedisPool pool = getRedisPool();
        Jedis jedis = pool.getResource();

        /*token-email存储到db0中*/
        jedis.select(0);

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

        /*token-email存储到db0中*/
        jedis.select(0);

        System.out.println(authorizeToken);
        jedis.set(authorizeToken, email);
        jedis.expire(authorizeToken, Integer.parseInt(redisExpire));
        jedis.close();
        pool.close();
    }

    public void setBannedUser(String author, int banningDays) {

        JedisPool pool = getRedisPool();
//      JedisPool pool = getLocalRedisPool();   //本地测试用

        Jedis jedis = pool.getResource();

        /*author-banningDays存储到db1中*/
        jedis.select(1);

        long banningSeconds = banningDays*24*3600;    //禁言时长（秒数）
        long epoch = System.currentTimeMillis()/1000; //当前unix时间戳
        long freeTimeStamp = epoch + banningSeconds;

        jedis.set(author,Integer.toString(banningDays));
        jedis.expireAt(author,freeTimeStamp);

        jedis.close();
        pool.close();
    }


    public void freeBannedUser(String author){

        JedisPool pool = getRedisPool();
//      JedisPool pool = getLocalRedisPool();   //本地测试用

        Jedis jedis = pool.getResource();

        /*author-banningDays存储到db1中*/
        jedis.select(1);

        jedis.del(author); //不存在的key会被忽略

        jedis.close();
        pool.close();

    }

    public boolean checkIfBanned(String author){
        JedisPool pool = getRedisPool();
//      JedisPool pool = getLocalRedisPool();   //本地测试用

        Jedis jedis = pool.getResource();

        /*author-banningDays存储在db1中*/
        jedis.select(1);

        return jedis.exists(author);
    }



}
