package com.cc.sdk2.jsdk.base.classloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * jar 文件file 加载器
 * @author sen.hu
 * @date 2019/4/26 17:21
 **/
public class JarFileClassLoader extends URLClassLoader{

    private final String rootPath;

    public JarFileClassLoader(String rootPath) {
        super(new URL[0]);
        this.rootPath = rootPath;
        if (this.rootPath == null || "".equals(rootPath.trim())) {
            throw new IllegalArgumentException("rootPath Is Null");
        }
        File check = new File(this.rootPath);
        if (check.isFile()) {
            throw new IllegalArgumentException("rootPath Is Not A directory");
        }
        loadJarFiles();
    }

    private void loadJarFiles() {
        List<String> dirList = new ArrayList<>();
        dirList.add(this.rootPath);
        while (dirList.size() > 0) {
            File dirFile = new File(dirList.get(0));
            File[] files = dirFile.listFiles();
            for (File subFile : files) {
                if (subFile.isDirectory()) {
                    dirList.add(subFile.getAbsolutePath());
                } else if (subFile.getName().toLowerCase().endsWith(".jar")) {
                   //加载jar文件
                    try {
                        URL url = subFile.toURI().toURL();
                        System.out.println(url);
                        addURL(url);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("unknown file format");
                }
            }
            dirList.remove(0);
        }
    }

    public void reLoadJars() {
        URL[] urls = this.getURLs();
        for (URL url : urls) {
            url = null;
        }
        loadJarFiles();
    }

}
