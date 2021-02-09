package com.cc.sdk2.test.jsdk.commons;

import com.alibaba.fastjson.JSON;
import com.cc.sdk2.jsdk.commons.http.HttpRequest;
import com.cc.sdk2.jsdk.commons.http.RequestResult;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2021/2/9 10:00
 **/
public class TestHttp {
    @Test
    public void testHttpGet() {
        try {
            RequestResult result = HttpRequest.builder().createHttpGetRequest("http://www.sina.com")
                    .addUrlParams("name", "cc")
                    .addUserAgent()
                    .build()
                    .execute();
            System.out.println(JSON.toJSONString(result));
            System.out.println(new String(result.getData(), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testHttpPost() {
        try {
            RequestResult result = HttpRequest.builder().createHttpPostRequest("https://sxh.zhaopin.com/api/buAd/getActiveList")
                    .addUrlEncodedFormData("jobFairId", 73)
                    .build()
                    .execute()
                    ;
            System.out.println(JSON.toJSONString(result));
            System.out.println(new String(result.getData(), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testHttpPostJson() {
        try {
            RequestResult result = HttpRequest.builder().createHttpPostRequest("https://sxh.zhaopin.com/api/buAd/getActiveList")
                    .setRawBody("{\"jobFairId\": 73}")
                    .build()
                    .execute()
                    ;
            System.out.println(JSON.toJSONString(result));
            System.out.println(new String(result.getData(), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDownloadImage() {
        try {
            RequestResult ret = HttpRequest.builder().createHttpGetRequest("https://mmbiz.qpic.cn/mmbiz_jpg/3qH4G0VEiaAscic2ZqOkKqFsJ9bVwekzPpJLs3RAboO9drUVhG5PJ0TjqNPtiautQWfibiavmQ5ibiasEos1MV411picDQ/640?wx_fmt=jpeg")
                    .build()
                    .execute()
                    ;
            System.out.println(ret.getData().length);
            ImageIO.write(ImageIO.read(new ByteArrayInputStream(ret.getData())), "png", new File("E:\\httpTest.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUploadFile() {
        try {
            RequestResult result = HttpRequest.builder().createHttpPostRequest("http://irpo-qa.zhaopin.com/api/Resume/ResumeAnalysisToJson")
                    .addMultipartFormData("sign", "7DF3683CA8621C37B93D11F82AD972E3")
                    .addMultipartFormData("fils", new File("D:\\testPost.doc"))
                    .addMultipartFormData("resumeSource", "5")
                    .addMultipartFormData("fileNameWithExt", "20190410211813699__周欣瑶_1.doc")
                    .addMultipartFormData("timeSpan", "1555063795")
                    .build()
                    .execute()
                    ;
            System.out.println(JSON.toJSONString(result));
            System.out.println(new String(result.getData(), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
