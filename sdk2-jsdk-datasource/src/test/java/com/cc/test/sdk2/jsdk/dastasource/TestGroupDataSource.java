package com.cc.test.sdk2.jsdk.dastasource;

import com.cc.sdk2.jsdk.datasource.GroupDataSource;
import com.cc.sdk2.jsdk.datasource.router.DataSourceRouter;
import com.cc.sdk2.jsdk.datasource.router.DefaultDataSourceRouter;
import com.cc.sdk2.jsdk.datasource.utils.DataSourceSelector;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.util.ArrayList;
import java.util.List;

public class TestGroupDataSource {

    private static GroupDataSource getDataSource() {
        try {
            ComboPooledDataSource sxh = new ComboPooledDataSource();
            sxh.setJdbcUrl("jdbc:postgresql://192.168.5.220:5432/changditong?prepareThreshold=0");
            sxh.setUser("zp_cdt");
            sxh.setPassword("zhaopin");
            sxh.setDriverClass("org.postgresql.Driver");

            ComboPooledDataSource blive = new ComboPooledDataSource();
            blive.setJdbcUrl("jdbc:postgresql://172.17.202.203:1602/camx_live_business");
            blive.setUser("web_camx_live_business");
            blive.setPassword("D7EA8EFA-3C85-4971-afe0-2322b6929d22");
            blive.setDriverClass("org.postgresql.Driver");

            return GroupDataSource.builder()
                    .setDefaultGroupDefaultRealDataSource(sxh)
                    .setGroupDefaultRealDataSource("blive", blive)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<DataSourceRouter> getDataSourceRouters() {
        List<DataSourceRouter> list = new ArrayList<>();
        list.add(new DefaultDataSourceRouter());
        return list;
    }

    public static void main(String[] args) {
        GroupDataSource groupDataSource = getDataSource();
        List<DataSourceRouter> dataSourceRouters = getDataSourceRouters();
        DataSourceSelector dataSourceSelector = new DataSourceSelector(groupDataSource, dataSourceRouters);
        //测试1
        LiveRoomSelector liveRoomSelector = dataSourceSelector.createDaoProxy(LiveRoomSelector.class);
        liveRoomSelector.selectOne(groupDataSource);
        //测试2
        JobFairSelector jobFairSelector = dataSourceSelector.createDaoProxy(JobFairSelector.class);
        jobFairSelector.selectOne(groupDataSource);

    }


}
