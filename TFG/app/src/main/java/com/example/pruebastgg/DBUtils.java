package com.example.pruebastgg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {

    public static Connection getConnection(String BD) throws SQLException {
        String url = "jdbc:postgresql://probandocositas-14481.8nj.gcp-europe-west1.cockroachlabs.cloud:26257/" + BD;
        String user = "tfg";
        String password = "3bvrFaxbuFv-9V-6vg09kQ";

        return DriverManager.getConnection(url, user, password);
    }
}
