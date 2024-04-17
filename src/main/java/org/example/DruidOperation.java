package org.example;

import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DruidOperation {

    // Druid 数据源
    private static DataSource dataSource;

    // 初始化 Druid 连接池
    static {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName("com.databend.jdbc.DatabendDriver");
        druidDataSource.setUrl("jdbc:databend://localhost:8000/default"); // 数据库 URL
        druidDataSource.setUsername("databend"); // 数据库用户名
        druidDataSource.setPassword("databend"); // 数据库密码
        druidDataSource.setInitialSize(10); // 初始连接池大小
        druidDataSource.setMaxActive(20); // 最大活跃连接数
        druidDataSource.setMinIdle(5); // 最小空闲连接数
        druidDataSource.setMaxWait(60000); // 获取连接最大等待时间，单位毫秒
        dataSource = druidDataSource;
    }

    public static void main(String[] args) {
        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(20);

        // 执行操作 A
        System.out.println("Performing operation A with 20 threads");
        for (int i = 0; i < 20; i++) {
            executor.execute(new OperationA(dataSource));
        }

        // 关闭线程池
        executor.shutdown();

        // 创建新的线程池
        executor = Executors.newFixedThreadPool(10);

        // 执行操作 B
        System.out.println("Performing operation B with 10 threads");
        for (int i = 0; i < 10; i++) {
            executor.execute(new OperationB(dataSource));
        }

        // 关闭线程池
        executor.shutdown();
    }
}

class OperationA implements Runnable {
    private DataSource dataSource;

    OperationA(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run() {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute("select 1");
            ResultSet r = statement.getResultSet();
            while (r.next()) {
                System.out.println("Operation A: "+r.getString(1));
            }
            // 在这里使用连接执行操作 A 的代码
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class OperationB implements Runnable {
    private DataSource dataSource;

    OperationB(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run() {
        try (Connection connection = dataSource.getConnection()) {
            // 在这里使用连接执行操作 B 的代码
            Statement statement = connection.createStatement();
            statement.execute("select 2");
            ResultSet r = statement.getResultSet();
            while (r.next()) {
                System.out.println("Operation B:"+r.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

