package com.cc.test.sdk2.jsdk.dastasource;


import com.cc.sdk2.jsdk.commons.jdbc.DbUtils;
import com.cc.sdk2.jsdk.datasource.GroupDataSource;
import com.cc.sdk2.jsdk.datasource.annotation.Router;

public class LiveRoomSelector {


    @Router(group = "blive")
    public void selectOne(GroupDataSource dataSource) {
        try {
            String topic = DbUtils.getValue(dataSource.getConnection(), "select topic from live_room limit 1");
            System.out.println("topic====>" + topic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
