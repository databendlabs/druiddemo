package org.example;

import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main implements Runnable {
    public static void main(String[] args) throws SQLException {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 20; i++) {
            executorService.submit(new Main());
        }
        executorService.shutdown();

    }

    public static Connection createConnection() throws SQLException {
        Connection connection = DBPool.getDataSource().getConnection();
        return connection;
    }

    public static Connection createConnectio2()
            throws SQLException {
        try {
            Class.forName("com.databend.jdbc.DatabendDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection("jdbc:databend://localhost:8000", "databend", "databend");
    }

    public void testBasic() throws SQLException {
        Connection connection = null;
        try {
            connection = createConnection();
            Statement statement = connection.createStatement();
            statement.execute("select sleep(3)");
            ResultSet r = statement.getResultSet();
            while (r.next()) {
                System.out.println(r.getString(1));
            }
        } finally {
            assert connection != null;
            connection.close();
        }
    }

    @Override
    public void run() {
        try {
            testBasic();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}