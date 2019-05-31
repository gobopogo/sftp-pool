package com.example.sftp.singlesftpconnect;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author missli
 * @Description SFTP 客户端 工具类
 * @Date 2019/4/15 10:58
 **/
public class SFTPClientUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SFTPClientUtils.class.getName());

    /**
     * 测试加锁情况
     */
    public static void main(String[] args) {
        int i = 0;
        do{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String, String> sftpDetails = new HashMap<String, String>();
                    // 设置主机ip，端口，用户名，密码
                    sftpDetails.put(SFTPConstants.SFTP_REQ_HOST, "127.0.0.1");
                    sftpDetails.put(SFTPConstants.SFTP_REQ_USERNAME, "111111");
                    sftpDetails.put(SFTPConstants.SFTP_REQ_PASSWORD, "111111");
                    sftpDetails.put(SFTPConstants.SFTP_REQ_PORT, "2222");
                    try {
                        SFTPClientUtils.upload(null, "/home/weihu/hfzq_resource/checkBefore","q4432", sftpDetails);
                    } catch (SftpException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            i++;
        }while (i<50);

    }

    /**
     * @Description 上传 inputStream流 到 SFTP
     * @param inputStream 输入流
     * @param directory 待上传的目标服务器路径
     * @param sftpFileName 上传目标文件名
     * @param sftpDetails 连接服务器参数
     * @return
     */
    public synchronized static void upload(InputStream inputStream, String directory, String sftpFileName, Map<String, String> sftpDetails) throws SftpException {
        ChannelSftp sftp= SFTPConnectionFactory.getInstance(sftpDetails).getClient();
        System.out.println("线程"+Thread.currentThread().getName()+"得到sftp锁，初始路径="+sftp.pwd());
        try {
            sftp.cd(directory);
        } catch (SftpException e) {
            //目录不存在，则创建文件夹
            String [] dirs=directory.split("/");
            String tempPath="";
            for(String dir:dirs){
                if(null== dir || "".equals(dir)) continue;
                tempPath+="/"+dir;
                try{
                    sftp.cd(tempPath);
                }catch(SftpException ex){
                    try {
                        sftp.mkdir(tempPath);
                        sftp.cd(tempPath);
                    } catch (SftpException e1) {
                        e1.printStackTrace();
                    }

                }
            }
        }

        System.out.println("线程"+Thread.currentThread().getName()+"得到sftp锁，上传路径="+sftp.pwd());
        System.out.println("====================================");
        //sftp.put(inputStream, sftpFileName);  //上传文件
        //登出SFTP
        //SFTPConnectionFactory.logout();
    }

    /**
     * 下载文件。
     * @param directory 下载目录 
     * @param downloadFile 下载的文件
     * @param saveFile 存在本地的路径
     * @param sftpDetails 连接服务器参数
     */
    /*public synchronized static boolean download(String directory, String downloadFile, String saveFile, Map<String, String> sftpDetails){
        boolean result = false;
        while(!result){
            ChannelSftp sftp = SFTPConnectionFactory.getInstance().makeConnection(sftpDetails);
            if (directory != null && !"".equals(directory)) { 
                try {
                    sftp.cd(directory);
                } catch (SftpException e) {
                    LOGGER.error("sftp文件下载，目录不存在，错误信息"+e.getMessage());
                } 
            } 
            File file = new File(saveFile+downloadFile);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e1) {
                LOGGER.error("sftp文件下载失败，本地目录不存在"+e1.getMessage());
            }
            try {
                sftp.get(downloadFile, fileOutputStream);
                result = true;
            } catch (SftpException e1) {
                e1.printStackTrace();
            }finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    } */

            
            
    /**
     * 删除文件
     * @param directory 要删除文件所在目录
     * @param deleteFile 要删除的文件
     * @param sftpDetails 连接服务器参数
     */
   /* public synchronized static boolean delete(String directory, String deleteFile, Map<String, String> sftpDetails){
        boolean result = false;
        ChannelSftp sftp = SFTPConnectionFactory.getInstance().makeConnection(sftpDetails);
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
        } catch (SftpException e) {
            e.printStackTrace();
        } 
      result = true;
      return result;
    }*/
}
