package com.online.mall.shoppv.common.util;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class CacheUtil {

	//最大数量
	public static final int DEFAULT_MAXSIZE = 10000;
	//存活时间以s为单位
    public static final int DEFAULT_TTL = 600;

    /**
     * 定义cache名称、超时时长（秒）、最大容量
     * 每个cache缺省：10秒超时、最多缓存50000条数据，需要修改可以在构造方法的参数中指定。
     */
    public enum Caches{
        getGoodsWithPage(3600,1000),
        GoodsCacheWithId(1800,1000),
    	ShopCarToSettle(600,4000),
    	SettleOrder(1800,3000),
    	goodsTrace(1800,1000),
    	recvAddr(1800,500),
    	GoodsCacheWithTitle(1800,1000);
    	
        Caches() {
        }
        Caches(int ttl) {
            this.ttl = ttl;
        }
        Caches(int ttl, int maxSize) {
            this.ttl = ttl;
            this.maxSize = maxSize;
        }
        private int maxSize=DEFAULT_MAXSIZE;    //最大數量
        private int ttl=DEFAULT_TTL;        //过期时间（秒）
        public int getMaxSize() {
            return maxSize;
        }
        public int getTtl() {
            return ttl;
        }
    }
    
    @Bean
    @Primary
    public CacheManager caffeineCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        ArrayList<CaffeineCache> caches = new ArrayList<CaffeineCache>();
        for(Caches c : Caches.values()){
            caches.add(new CaffeineCache(c.name(),
                    Caffeine.newBuilder().recordStats()
                            .expireAfterWrite(c.getTtl(), TimeUnit.SECONDS)
                            .maximumSize(c.getMaxSize())
                            .build())
            );
        }
        cacheManager.setCaches(caches);
        return cacheManager;
    }
	
}
