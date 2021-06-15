package com.cc.sdk2.jsdk.commons.http;

import com.cc.sdk2.jsdk.commons.http.entities.BaseRequestEntity;
import com.cc.sdk2.jsdk.commons.http.entities.BasicDataRequestEntity;
import com.cc.sdk2.jsdk.commons.http.entities.FormDataRequestEntity;
import com.cc.sdk2.jsdk.commons.http.entities.StringRequestEntity;
import com.cc.sdk2.jsdk.commons.http.ssl.X509TrustAllManager;
import com.cc.sdk2.jsdk.commons.utils.IoUtil;
import com.cc.sdk2.jsdk.commons.utils.StringUtil;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

public class HttpRequest {

    public static final String PC_USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36";
    public static final String MOBILE_USER_AGENT = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Mobile Safari/537.36";

    private String url;
    private final String methodName;
    private int connTimeout = 10000;
    private int readTimeout = 20000;
    private String userAgent;
    private String proxyAddress;
    private Integer proxyPort;

    private URL _url;
    private HttpURLConnection conn;
    private String reqUr;
    private byte[] sendData;


    protected HttpRequest(String url, String methodName) {
        this.url = url;
        this.methodName = methodName;
    }

    protected void genReqUrl(Map<String, Object> urlParams) {
        StringBuilder paramsBuf = new StringBuilder();
        if (urlParams != null) {
            urlParams.forEach((k, v) -> {
                paramsBuf.append(k).append("=").append(v).append("&");
            });
        }
        if (!this.url.contains("?")) {
            paramsBuf.insert(0, "?");
            paramsBuf.insert(0, this.url);
        } else if (this.url.endsWith("?") || this.url.endsWith("&")) {
            paramsBuf.insert(0, this.url);
        } else {
            paramsBuf.insert(0, "&");
            paramsBuf.insert(0, this.url);
        }
        this.reqUr = paramsBuf.toString().substring(0, paramsBuf.toString().length() - 1);
    }

    protected void initConnection(BaseRequestEntity requestEntity) throws Exception {
        genReqUrl(requestEntity.getQueryParams());
        this._url = new URL(this.reqUr);
        if (reqUr.toLowerCase().startsWith("https")) {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManager[] tms = {new X509TrustAllManager()};
            sslContext.init(null, tms, new java.security.SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            if (StringUtil.isNullOrEmpty(proxyAddress)) {
                this.conn = (HttpsURLConnection) _url.openConnection();
            } else {
                this.conn = (HttpsURLConnection) _url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress, proxyPort)));
            }
            ((HttpsURLConnection) this.conn).setSSLSocketFactory(sslSocketFactory);
        } else {
            if (StringUtil.isNullOrEmpty(proxyAddress)) {
                this.conn = (HttpURLConnection) _url.openConnection();
            } else {
                this.conn = (HttpURLConnection) _url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress, proxyPort)));
            }
        }
    }

    protected void initRequest(String contentType, Map<String, Object> headers) throws ProtocolException {
        conn.setRequestMethod(methodName);
        conn.setConnectTimeout(this.connTimeout);
        conn.setReadTimeout(this.readTimeout);
        conn.setRequestProperty("Accept", "*/*");
        conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
        conn.setRequestProperty("Cache-Control", "no-cache");
        conn.setRequestProperty("Connection", "keep-alive");
        if (!StringUtil.isNullOrEmpty(userAgent)) {
            conn.setRequestProperty("User-Agent", userAgent);
        }
        conn.setRequestProperty("Content-Type", contentType);
        headers.forEach((k, v) -> conn.setRequestProperty(k, v.toString()));
        conn.setDoInput(true);
        conn.setDoOutput(true);
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private HttpRequest request;
        private BaseRequestEntity baseDataEntity = new BasicDataRequestEntity();
        private BaseRequestEntity requestEntity;
        private String reqCharset = "UTF-8";

        protected Builder() {
        }

        public Builder createHttpGetRequest(String url) {
            this.request = new HttpRequest(url, "GET");
            return this;
        }

        public Builder createHttpPostRequest(String url) {
            this.request = new HttpRequest(url, "POST");
            return this;
        }

        public Builder createHttpPutRequest(String url) {
            this.request = new HttpRequest(url, "PUT");
            return this;
        }

        public Builder createHttpDeleteRequest(String url) {
            this.request = new HttpRequest(url, "DELETE");
            return this;
        }

        public Builder addUrlParams(String name, Object value) {
            this.baseDataEntity.addUrlParam(name, value);
            return this;
        }

        public Builder addHeader(String key, Object value) {
            this.baseDataEntity.addHeader(key, value);
            return this;
        }

        public Builder setContentType(String contentType) {
            this.baseDataEntity.setContentType(contentType);
            return this;
        }

        public Builder setReqCharset(String reqCharset) {
            this.reqCharset = reqCharset;
            return this;
        }

        public Builder setRawBody(String rawBody) {
            if (requestEntity == null || !(requestEntity instanceof StringRequestEntity)) {
                requestEntity = new StringRequestEntity();
            }
            ((StringRequestEntity) this.requestEntity).setRawBody(rawBody);
            return this;
        }

        public Builder addUrlEncodedFormData(String key, Object value) {
            if (requestEntity == null || !(requestEntity instanceof StringRequestEntity)) {
                requestEntity = new StringRequestEntity();
            }
            ((StringRequestEntity) this.requestEntity).addFormData(key, value);
            return this;
        }

        public Builder addMultipartFormData(String key, Object value) {
            if (requestEntity == null || !(requestEntity instanceof FormDataRequestEntity)) {
                requestEntity = new FormDataRequestEntity();
            }
            ((FormDataRequestEntity) this.requestEntity).addMultipartFormData(key, value);
            return this;
        }

        private Builder addMultipartFormData(String key, File file) {
            if (requestEntity == null || !(requestEntity instanceof FormDataRequestEntity)) {
                requestEntity = new FormDataRequestEntity();
            }
            ((FormDataRequestEntity) this.requestEntity).addMultipartFormData(key, file);
            return this;
        }

        public Builder setConnTimeout(int connTimeout) {
            request.connTimeout = connTimeout > 0 ? connTimeout : 10000;
            return this;
        }

        public Builder setReadTimeout(int readTimeout) {
            request.readTimeout = readTimeout > 0 ? readTimeout : 20000;
            return this;
        }

        public Builder addUserAgent() {
            request.userAgent = PC_USER_AGENT;
            return this;
        }

        public Builder addUserAgent(String agent) {
            request.userAgent = agent;
            return this;
        }

        public Builder addProxy(String address, Integer port) {
            request.proxyAddress = address;
            request.proxyPort = port;
            return this;
        }

        public HttpRequest build() throws Exception {
            if (this.request == null) {
                throw new RuntimeException("Please Call Create HttpRequest");
            }
            //init connection
            request.initConnection(baseDataEntity);
            if (requestEntity instanceof FormDataRequestEntity) {
                baseDataEntity.setContentType(requestEntity.getContentType());
            }
            //init content type
            if (StringUtil.isNullOrEmpty(baseDataEntity.getContentType())) {
                if (requestEntity != null) {
                    if (requestEntity.getFormParams().size() > 0) {
                        baseDataEntity.setContentType(ContentTypeEnum.APPLICATION_FORM_URL_ENCODE.value);
                    } else {
                        baseDataEntity.setContentType(ContentTypeEnum.APPLICATION_JSON.value);
                    }
                } else {
                    baseDataEntity.setContentType(ContentTypeEnum.TEXT_HTML.value);
                }
            }
            //init request
            request.initRequest(baseDataEntity.getContentType(), baseDataEntity.getHeaders());
            if (requestEntity != null) {
                request.sendData = requestEntity.genSendData(reqCharset);
            }
            return request;
        }
    }
    private static final Pattern CHARSET_PATTERN = Pattern.compile("^(.*)(charset=)(.+)$");

    private String getContentTypeCharset(String contentTypeHeader) {
        if (StringUtil.isNullOrEmpty(contentTypeHeader) ||  !contentTypeHeader.toLowerCase().contains("charset")) {
            return null;
        }
        Matcher m = CHARSET_PATTERN.matcher(contentTypeHeader.toLowerCase());
        if (m.matches()) {
            return m.group(3);
        }
        return null;
    }

    public RequestResult execute() {
        long startTime = System.currentTimeMillis();
        int statusCode;
        String charset = null;
        byte[] respData;
        try {
            conn.connect();
            //输出信息
            if (sendData != null && sendData.length > 0) {
                try(OutputStream out = conn.getOutputStream()) {
                    IoUtil.write(out, sendData);
                    out.flush();
                }
            }
            //读取返回
            statusCode = conn.getResponseCode();
            InputStream in = null;
            charset = getContentTypeCharset(conn.getHeaderField("Content-Type"));
            String contentEncoding = conn.getHeaderField("Content-Encoding");
            if (contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip")) {
                in = new GZIPInputStream(conn.getInputStream());
            } else if (!StringUtil.isNullOrEmpty(contentEncoding) && contentEncoding.equalsIgnoreCase("zip")) {
                in = new ZipInputStream(conn.getInputStream());
            } else {
                in = conn.getInputStream();
            }
            respData = IoUtil.read(in);

        } catch (Exception e) {
            e.printStackTrace();
            statusCode = 10900;
            respData = ("未知异常：" + e.getMessage()).getBytes();
        }
        //设置返回
        RequestResult result = new RequestResult(reqUr, statusCode, System.currentTimeMillis() - startTime);
        result.setRespCharset(charset);
        result.setData(respData);
        result.setRedirectUrl(conn.getHeaderField("Location"));
        result.setRespHeaders(conn.getHeaderFields());
        return result;
    }
}
