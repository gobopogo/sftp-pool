package com.example.sftp.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.sftp.pool.ConnectionProvider;

/**
 * @Author missli
 * @Description
 * @Date 2019/5/15 10:10
 **/
@Configuration
public class SftpConfig {

    @Bean
    public ConnectionProvider getProvider(){
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(3);
        ConnectionProvider connectionProvider = new ConnectionProvider("",2222,"","",config);
        return connectionProvider;
    }
}
