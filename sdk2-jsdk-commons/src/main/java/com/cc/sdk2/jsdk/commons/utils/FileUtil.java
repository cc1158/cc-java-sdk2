package com.cc.sdk2.jsdk.commons.utils;


import com.cc.sdk2.jsdk.base.BaseConstant;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * All rights reserved, copyright@cc.hu
 * 文件操作工具类
 * @author cc
 * @version 1.0
 * @date 2019/5/30 23:34
 **/
public class FileUtil {

    public static void copy(String filePath) throws IOException {
        File file = new File(filePath);
        File copy = new File(file.getParent() + File.separator + file.getName() + ".copy");
        copy(file, copy);
    }

    public static void copy(String srcPath, String destPath) throws IOException {
        copy(new File(srcPath), new File(destPath));
    }

    public static void copy(File src, File dest) throws IOException {
        if (!src.exists()) {
            throw new RuntimeException("source file do not exist");
        }
        if (!dest.exists()) {
            dest.createNewFile();
        }
        FileInputStream srcIn = new FileInputStream(src);
        FileOutputStream destOut = new FileOutputStream(dest);
        try {
            byte[] file = IoUtil.read(srcIn);
            IoUtil.write(destOut, file);
        } finally {
            srcIn.close();
            destOut.flush();
            destOut.close();
        }
    }

    public static void truncate(String filePath, long size) throws IOException {
        truncate(new RandomAccessFile(filePath, "rw"), size);
    }

    public static void truncate(RandomAccessFile file, long size) throws IOException {
        FileChannel fileChannel = file.getChannel();
        fileChannel.truncate(size);
        fileChannel.close();
    }

    public static void split(String filePath, long eachSize) throws IOException {
        File file = new File(filePath);
        if (!file.exists() || file.isDirectory()) {
            throw new IOException("");
        }
        long pieces = file.length() % eachSize == 0 ? (file.length() / eachSize) : (file.length() / eachSize) + 1;
        split(new RandomAccessFile(file, "rw"), file.getParent() + File.separator, file.getName(), eachSize, (int)pieces);
    }

    public static void split(String filePath, int pieces) throws IOException {
        File file = new File(filePath);
        if (!file.exists() || file.isDirectory()) {
            throw new IOException("");
        }
        long eachSize = file.length() / pieces;
        split(new RandomAccessFile(file, "rw"), file.getParent() + File.separator, file.getName(), eachSize, pieces);
    }

    private static void split(RandomAccessFile file, String path, String splitName, long eachSize, int pieces) throws IOException {
        FileChannel srcChannel = file.getChannel();
        ByteBuffer srcBuf = ByteBuffer.allocate(1024);
        ByteBuffer destBuf = ByteBuffer.allocate(1024);
        for (int i = pieces; i > 1; i--) {
            //create dest file
            File destFile = new File(path + splitName + "-" + (i - 1));
            destFile.createNewFile();
            RandomAccessFile destRaf = new RandomAccessFile(destFile, "rw");
            FileChannel destChannel = destRaf.getChannel();

            long position = (i - 1) * eachSize;
            srcChannel.position(position);
            //read source file and write out
            while (srcChannel.read(srcBuf) > 0) {
                srcBuf.flip();
                destBuf.put(srcBuf);
                destBuf.flip();
                while (destBuf.hasRemaining()) {
                    destChannel.write(destBuf);
                }
                srcBuf.clear();
                destBuf.clear();
            }
            srcChannel.truncate(position);
            destBuf.clear();
        }
    }

    /**
     * read a text file
     * @param filePath  file abs path
     * @return file content
     * @throws Exception
     */
    public static String readTextFile(String filePath) throws Exception {
        File file = new File(filePath);
        return readTextFile(file, BaseConstant.CHARSET_NAME_UTF8);
    }

    /**
     * read a text file
     * @param file  the file
     * @param charset  file charset
     * @return file context
     * @throws Exception
     */
    public static String readTextFile(File file, String charset) throws Exception {
        if (file == null || file.isDirectory()) {
            throw new IllegalArgumentException("file is not exist or is a directory");
        }
        BufferedReader reader = null;
        try {
            reader  = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
            String line;
            StringBuilder buff = new StringBuilder(100);
            while ((line = reader.readLine()) != null) {
                buff.append(line);
            }
            return buff.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * 生成zip文件
     * @param filePath  要压缩的文件或目录
     * @param zipFilePath   生成zip文件的位置
     * @throws Exception    操作文件的异常
     */
    public static void genZipFile(String filePath, String zipFilePath) throws Exception {
        File destFile = zipFilePath.endsWith(".zip") ? new File(zipFilePath) : new File(zipFilePath + ".zip");
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(destFile));
        File[] files = new File[] {new File(filePath)};
        compressFileByZip("", files, zipOutputStream);

    }

    private static void compressFileByZip(String baseDir, File[] files, ZipOutputStream zipOutputStream) throws IOException {
        for (File item : files) {
            System.out.println(item.getAbsolutePath());
            FileInputStream fileInputStream = null;
            if (item.isFile()) {
                try {
                    String entryDir= (baseDir == null || "".equals(baseDir)) ? "" : baseDir + File.separator + item.getName();
                    zipOutputStream.putNextEntry(new ZipEntry((entryDir)));
                    fileInputStream = new FileInputStream(item);
                    byte[] buff = new byte[1024];
                    int len;
                    while ((len = fileInputStream.read(buff)) > 0) {
                        zipOutputStream.write(buff, 0, len);
                    }
                    zipOutputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (Exception e) {
                        }
                    }
                }
            } else if (item.isDirectory()) {
                File[] subFiles = item.listFiles();
                if (subFiles == null || subFiles.length == 0) {
                    zipOutputStream.putNextEntry(new ZipEntry(baseDir + File.separator + item.getName() + File.separator));
                } else {
                    String subBaseDir = (baseDir == null || "".equals(baseDir)) ? item.getName() : baseDir + File.separator + item.getName();
                    compressFileByZip(subBaseDir, subFiles, zipOutputStream);
                }
            }
        }
    }

    /**
     * 读取properties配置文件
     * @param absPath   property路径
     * @return  键值对
     */
    public static Map<String, String> readProperties(String absPath) throws IOException {
        if (StringUtil.isNullOrEmpty(absPath)) { return null; }
        InputStream in = null;
        try {
            Map<String, String> ret = new HashMap<>();
            File file = new File(absPath);
            if (file.exists() && file.isFile()) {
                in = new FileInputStream(absPath);
            } else {
                in = FileUtil.class.getClassLoader().getResourceAsStream(absPath);
            }
            Properties properties = new Properties();
            properties.load(in);
            Enumeration<Object> keys = properties.keys();
            while (keys.hasMoreElements()) {
                String key = String.valueOf(keys.nextElement());
                ret.put(key, properties.getProperty(key));
            }
            return ret;
        } finally {
            if (in != null) { in.close(); }
        }
    }

    /**
     * 写入一个property
     * @param key     名称
     * @param value     值
     * @param absFilePath   property文件路径
     * @throws IOException      io 异常
     * @return -1 file not exist  0  successful  1  failed
     */
    public static int writeProperty(String key, String value, String absFilePath) throws IOException {
        InputStream in = null;
        FileOutputStream fileOutputStream = null;
        if (StringUtil.isNullOrEmpty(key) || StringUtil.isNullOrEmpty(absFilePath)) {
            return -1;
        }
        try {
            File file = new File(absFilePath);
            if (file.exists() && file.isFile()) {
                in = new FileInputStream(file);
            } else {
                in = FileUtil.class.getClassLoader().getResourceAsStream(absFilePath);
            }
            Properties properties = new Properties();
            properties.load(in);
            properties.setProperty(key, value);
            fileOutputStream = new FileOutputStream(new File(FileUtil.class.getResource(absFilePath).getPath()));
            properties.store(fileOutputStream, "last edit");
            return 0;
        } finally {
            if (in != null) { in.close(); }

            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }
    }

   /**
     * 设置系统属性变量
     * @param filePath      文件地址
     * @throws IOException
     */
    public static void setSystemProperties(String filePath) throws IOException {
        setSystemProperties(readProperties(filePath));
    }

    /**
     * 设置系统属性
     * @param propertiesMap 属性map
     */
    public static void setSystemProperties(Map<String, String> propertiesMap) {
        if (propertiesMap == null) {
            return ;
        }
        propertiesMap.forEach(System::setProperty);
    }

}
