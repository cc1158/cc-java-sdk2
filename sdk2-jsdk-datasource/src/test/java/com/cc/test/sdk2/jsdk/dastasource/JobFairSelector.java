package com.cc.test.sdk2.jsdk.dastasource;

import com.cc.sdk2.jsdk.commons.jdbc.DbUtils;
import com.cc.sdk2.jsdk.datasource.GroupDataSource;
import com.cc.sdk2.jsdk.datasource.annotation.Router;

public class JobFairSelector {

    @Router(group = "default")
    public void selectOne(GroupDataSource dataSource) {
        try {
            String name = DbUtils.getValue(dataSource.getConnection(), "select job_fair_name from t_job_fair_info limit 1");
            System.out.println("name====>" + name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
