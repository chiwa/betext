package com.betext.transportation.service;

import com.betext.transportation.Exception.MemcachedException;
import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


public class MemcachedClientService implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(MemcachedClientService.class);
    protected  MemCachedClient memCachedClient = new MemCachedClient();
    private  String server = "127.0.0.1";
    private  int port = 11211;
    private  final String[] servers = {server + ":" + port};
    private  SockIOPool pool;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = SockIOPool.getInstance();
        pool.setServers(servers);
        pool.getAliveCheck();
        pool.setInitConn(20);
        pool.setMinConn(20);
        pool.setMaxConn(500);
        pool.setMaxIdle(1000 * 60 * 60 * 6);
        pool.setMaintSleep(30);
        pool.setNagle(false);
        pool.setSocketTO(3000);
        pool.setSocketConnectTO(0);
        pool.initialize();
    }

    private  void isConnectionSuccess() {
        InetSocketAddress addr = new java.net.InetSocketAddress(server, port);
        try {
            Socket sock = new Socket(addr.getAddress(), addr.getPort());
            sock.close();
        } catch (IOException e) {
            log.error("Can not connect to Memcached Server : " + e.getMessage());
            throw new MemcachedException("Can not connect to Memcached Server : " + e.getMessage());
        }
    }

    public  boolean set(String key, Object value) {
        isConnectionSuccess();
        boolean result = false;
        try {
            result = memCachedClient.set(key, value);
        } catch (Exception ex) {
            log.error("Can not set key = " + key + " with object = " + value.getClass().toString() + " to MemCache " + ex.getMessage());
        }
        return result;
    }

    public  Object get(String key) {
        isConnectionSuccess();
        Object result = null;
        try {
            result = memCachedClient.get(key);
        } catch (Exception ex) {
            log.error("Can not get key = " + key + " : " + ex.getMessage());
        }
        return result;
    }

    public  boolean delete(String key) {
        isConnectionSuccess();
        boolean result = false;
        try {
            if (memCachedClient.keyExists(key)) {
                result = memCachedClient.delete(key);
            }
        } catch (Exception ex) {
            log.error("Can not delete key = " + key + " : " + ex.getMessage());
        }
        return result;
    }

    public  boolean clearAll() {
        isConnectionSuccess();
        try {
            return memCachedClient.flushAll();
        } catch (Exception ex) {
            log.error("Can not clear all MemCached : " + ex.getMessage());
        }
        return false;
    }

}
