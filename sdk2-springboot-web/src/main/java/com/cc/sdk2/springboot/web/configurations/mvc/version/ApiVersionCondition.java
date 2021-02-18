package com.cc.sdk2.springboot.web.configurations.mvc.version;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description 请求api version判断
 * @Author sen.hu
 * @Date 2018/11/29 17:44
 **/
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {

    private static final Pattern VERSION_PATTERN = Pattern.compile("v([1-9]\\.[0-9])");

    private float version;

    public ApiVersionCondition(float version) {
        this.version = version;
    }

    public float getVersion() {
        return version;
    }

    @Override
    public ApiVersionCondition combine(ApiVersionCondition other) {
        return new ApiVersionCondition(other.getVersion());
    }

    @Nullable
    @Override
    public ApiVersionCondition getMatchingCondition(HttpServletRequest request) {
        String path = request.getRequestURI();
        Matcher m = VERSION_PATTERN.matcher(path);
        if (m.find()) {
            float reqVersion = Float.valueOf(m.group(1));
            //如果请求版本号>=配置版本号
            if (reqVersion >= this.version) {
                return this;
            }
        }
        return new ApiVersionCondition(1.0F);
    }

    @Override
    public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
        float diff = other.getVersion() - this.getVersion();
        if (diff > 0) { return 1; }
        else if (diff == 0) { return 0; }
        else { return -1; }
    }
}
