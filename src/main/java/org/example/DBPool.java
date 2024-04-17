package org.example;

import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DBPool {
    private static DruidDataSource dataSource;

    static {
        try {
            dataSource = new DruidDataSource();
            dataSource.setDriverClassName("com.databend.jdbc.DatabendDriver");
            dataSource.setUrl("jdbc:databend://localhost:8000");
            dataSource.setUsername("databend");
            dataSource.setPassword("databend");
            dataSource.setMinEvictableIdleTimeMillis(15*60*1000);
            dataSource.setTimeBetweenEvictionRunsMillis(5*60*1000);
            dataSource.setInitialSize(8); // 设置初始化时创建的连接数
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DataSource getDataSource() {
        try {
            dataSource.init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataSource;
    }
}