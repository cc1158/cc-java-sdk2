package com.cc.sdk2.jsdk.base.classloader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 从文件中读取类
 * @author sen.hu
 * @date 2019/4/26 14:28
 **/
public class FileClassLoader extends ClassLoader {
    /**类名 类对应关系*/
    private Map<String, Class<?>> packageClassMap = new ConcurrentHashMap<>();

    private final String rootPath;

    public FileClassLoader(String rootPath) {
        this.rootPath = rootPath;
        if (this.rootPath == null || "".equals(rootPath.trim())) {
            throw new IllegalArgumentException("rootPath Is Null");
        }
        File check = new File(this.rootPath);
        if (check.isFile()) {
            throw new IllegalArgumentException("rootPath Is Not A directory");
        }
        this.initClass();
    }

    /**
     * 重新更新class
     * 当类发生变化时
     */
    public void reInitClass() {
        this.packageClassMap.clear();
        this.initClass();
    }

    private void initClass() {
        List<String> dirList = new ArrayList<>();
        dirList.add(this.rootPath);
        while (dirList.size() > 0) {
            File dirFile = new File(dirList.get(0));
            File[] files = dirFile.listFiles();
            if (files != null) {
                for (File subFile : files) {
                    if (subFile.isDirectory()) {
                        dirList.add(subFile.getAbsolutePath());
                    } else if (subFile.getName().toLowerCase().endsWith(".class")) {
                        //java 字节码
                        byte[] classBytes = readClassFile(subFile);
                        Class<?> clazz = defineClass(null, classBytes, 0, classBytes.length);
                        this.packageClassMap.put(clazz.getName(), clazz);
                    } else {
                        System.out.println("unknown file format");
                    }
                }
            }
            dirList.remove(0);
        }
    }
    //读取文件
    private byte[] readClassFile(File file) {
        try {
            InputStream in = new FileInputStream(file);
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int readNum = 0;
            while ((readNum = in.read(buffer)) != -1) {
                byteOut.write(buffer, 0, readNum);
            }
            return byteOut.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> ret = this.packageClassMap.get(name);
        if (ret == null) {
            ret = super.findClass(name);
        }
        return ret;
    }

}
