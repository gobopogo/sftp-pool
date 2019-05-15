package com.example.sftp;

import com.example.sftp.pool.ConnectionProvider;
import com.jcraft.jsch.ChannelSftp;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @Author missli
 * @Description
 * @Date 2019/5/15 10:24
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class SftpPoolApplicationTests {

    @Autowired
    private ConnectionProvider provider;

    @Test
    public void contextLoads() throws  Exception{
        //获取连接
        ChannelSftp connection = (ChannelSftp) provider.getConnection();
        //TODO 上传下载
        //connection.put();
        //connection.get();
        connection.cd("/home/weihu/lyr");
        InputStream inputStream = connection.get("uomp.properties");

        //方式1 读取该文件，并把流转成String 显示
        Reader in = new InputStreamReader(inputStream, "UTF-8");  //高效
        final char[] buffer = new char[1024];
        final StringBuilder out = new StringBuilder();
        int rsz = 0;
        while ((rsz = in.read(buffer, 0, buffer.length)) > 0){
            out.append(buffer, 0, rsz);
        }
        System.out.println("=================================================");
        System.out.println(out.toString());

        //方式2
        //InputStream不能被复制，且InputStream被读一次之后，第二次读取时可能已经到结尾了（EOFException），或者已经被close掉了。
        //所以这个函数执行后 只会打印出一次 uomp.properties 文件的内容
        ByteArrayOutputStream result = new ByteArrayOutputStream();   //最高效
        byte[] cbuf = new byte[1024];
        int readLen = 0;
        while ((readLen = inputStream.read(cbuf)) > 0) {   //(readLen = inputStream.read(cbuf)) != -1
            result.write(cbuf, 0, readLen);
        }
        String pro = result.toString();
        System.out.println("=================================================");
        System.out.println(pro);


        //归还连接
        provider.returnObject(connection);
    }
}
