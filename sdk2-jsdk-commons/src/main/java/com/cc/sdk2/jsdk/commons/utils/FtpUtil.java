package com.cc.sdk2.jsdk.commons.utils;

import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpProtocolException;

import java.io.*;
import java.net.InetSocketAddress;

/**
 * All rights reserved, copyright@cc.hu
 * ftp代码操作
 * @author cc
 * @version 1.0
 * @date 2020/4/30 14:58
 **/
public class FtpUtil {

    private static final int DEFAULT_PORT = 21;

    public static FtpClient connect(String ip, String user, String password) throws IOException, FtpProtocolException {
        return connect(ip, DEFAULT_PORT, user, password);
    }

    public static FtpClient connect(String ip, Integer port, String user, String password) throws IOException, FtpProtocolException {
        FtpClient ftpClient = FtpClient.create();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, port);
        ftpClient.connect(inetSocketAddress);
        ftpClient.login(user, password.toCharArray());
        return ftpClient;
    }

    /**
     * ftp上传文件
     * @param localAbsFilePath  本地文件的绝对路径
     * @param serverPath    服务器路径
     * @param remoteFileName    服务器上传的文件名
     * @param ftpClient     ftp客户端
     * @return
     * @throws IOException
     * @throws FtpProtocolException
     */
    public static int upload(String localAbsFilePath, String serverPath, String remoteFileName, FtpClient ftpClient) throws IOException, FtpProtocolException {
        if (StringUtil.isNullOrEmpty(serverPath)) {serverPath = "./";}
        File uploadFile = new File(localAbsFilePath);
        if (!uploadFile.exists() || ftpClient == null) {
            return -1;
        }
        ftpClient.changeDirectory(serverPath);//转到server path
        if (uploadFile.isDirectory()) {//目录
            String dirName = uploadFile.getName();
            ftpClient.makeDirectory(dirName);
        } else if (uploadFile.isFile()) {//文件
            String ftpFile = remoteFileName;
            if (StringUtil.isNullOrEmpty(remoteFileName)) {ftpFile = uploadFile.getName();}
            OutputStream os = null;
            FileInputStream fis = null;
            try {
                os = ftpClient.putFileStream(ftpFile);
                fis = new FileInputStream(uploadFile);
                byte[] buf = new byte[1024];
                int llen = 0;
                while ((llen = fis.read(buf)) != -1) {os.write(buf, 0, llen);}
                os.flush();
            } finally {
                if (fis != null) {fis.close();}
                if (os != null) { os.close(); }
            }
        }
        return 0;
    }

    /**
     * ftp下载
     * @param ftpClient
     * @param remoteAbsFilePath
     * @param localAbsFilePath
     * @throws IOException
     * @throws FtpProtocolException
     */
    public static void download(FtpClient ftpClient, String remoteAbsFilePath, String localAbsFilePath) throws IOException, FtpProtocolException {
        if (StringUtil.isNullOrEmpty(remoteAbsFilePath) || StringUtil.isNullOrEmpty(localAbsFilePath)) {return ;}

        InputStream is = null;
        FileOutputStream fos = null;
        File file = new File(localAbsFilePath);
        try {
            if (!file.exists()) {file.createNewFile();}
            is = ftpClient.getFileStream(remoteAbsFilePath);
            fos = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int llen = 0;
            while ((llen = is.read(buf)) != -1) {fos.write(buf, 0, llen);}

            fos.flush();
        } finally {
            if (is != null) {is.close();}
            if (fos != null) {fos.close();}
        }
    }
}
