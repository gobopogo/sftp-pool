package com.example.sftp.singlesftpconnect;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;

/**
 * @Author missli
 * @Description SFTP连接工厂
 * @Date 2019/4/12 14:42
 */
public class SFTPConnectionFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(SFTPConnectionFactory.class.getName());

    private static SFTPConnectionFactory factory = new SFTPConnectionFactory();

    private static ChannelSftp client;

    private static Session session;

    public static SFTPConnectionFactory getInstance(Map<String, String> sftpDetails) {
        String ftpHost = sftpDetails.get(SFTPConstants.SFTP_REQ_HOST);
        String port = sftpDetails.get(SFTPConstants.SFTP_REQ_PORT);
        String ftpUserName = sftpDetails.get(SFTPConstants.SFTP_REQ_USERNAME);
        String ftpPassword = sftpDetails.get(SFTPConstants.SFTP_REQ_PASSWORD);
        int ftpPort = SFTPConstants.SFTP_DEFAULT_PORT;
        if (port != null && !port.equals("")) {
            ftpPort = Integer.valueOf(port);
        }
        synchronized (factory){
            System.out.println("====================================");
            System.out.println("线程"+Thread.currentThread().getName()+"得到factory锁");
            if (client == null || session == null || !client.isConnected() || !session.isConnected()) {
                try {
                    JSch jsch = new JSch();
                    session = jsch.getSession(ftpUserName, ftpHost, ftpPort); // 根据用户名，主机ip，端口获取一个Session对象
                    if (ftpPassword != null) {
                        session.setPassword(ftpPassword);  //设置密码
                    }
                    Properties config = new Properties();
                    config.put("StrictHostKeyChecking", "no");
                    config.put("kex","diffie-hellman-group1-sha1");
                    session.setConfig(config);  // 为Session对象设置properties
                    session.connect();
                    Channel channel = session.openChannel("sftp");  // 打开SFTP通道
                    channel.connect();
                    client = (ChannelSftp) channel;
                    LOGGER.info("sftp服务器连接成功");
                } catch (JSchException e) {
                    LOGGER.error("sftp登录失败，检测登录ip，端口号，用户名密码是否正确，错误信息为" + e.getMessage());
                }
            }
        }
        return factory;
    }

    /**
     * @return
     * @Description 关闭连接 Server
     * @Param
     */
    public static void logout() {
        System.out.println("线程"+Thread.currentThread().getName()+"登出");
        if (client != null) {
            if (client.isConnected()) {
                client.disconnect();
            }
        }
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
            }
        }
    }

    public ChannelSftp getClient() {
        return client;
    }

    public Session getSession() {
        return session;
    }
}
