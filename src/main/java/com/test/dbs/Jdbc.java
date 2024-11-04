package com.test.dbs;

import java.sql.*;

public class Jdbc {
    private static final String URL = "jdbc:mysql://localhost:3306/javasql";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Jdbc instance;
    private Connection connection;

    private Jdbc() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Kết nối thành công");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static Jdbc getInstance() {
        if (instance == null) {
            synchronized (Jdbc.class) {
                if (instance == null) {
                    instance = new Jdbc();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

}
