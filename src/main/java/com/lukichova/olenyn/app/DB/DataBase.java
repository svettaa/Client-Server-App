package com.lukichova.olenyn.app.DB;

import java.sql.Connection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.lukichova.olenyn.app.resoures.Resoures.DATABASE_NAME;

public class DataBase {
    static Connection connection;

    public static void connect() {
        try {
            String url = "jdbc:sqlite:" + "test.db";
            connection = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");
            System.out.println();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void close() {
        try {
            connection.close();

            System.out.println("Connection closed");
            System.out.println();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
