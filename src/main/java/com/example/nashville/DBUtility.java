package com.example.nashville;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtility {
    private static final String Driver = "org.postgresql.Driver";
    private static final String ConnUrl = "jdbc:postgresql://34.29.216.150:5432/nash";
    private static final String Username = "user1";
    private static final String Password = "geog_576";

    // This is a constructor
    public DBUtility() {
    }

    // create a Connection to the database
    private Connection connectDB() {
        Connection conn = null;
        try {
            Class.forName(Driver);
            conn = DriverManager.getConnection(ConnUrl, Username, Password);
            System.out.println(conn);
            return conn;
        } catch (Exception e) {
            System.out.println("failed connection");
            e.printStackTrace();
        }
        System.out.println(conn);
        return conn;
    }

    // execute a sql query (e.g. SELECT) and return a ResultSet
    public ResultSet queryDB(String sql) {
        Connection conn = connectDB();
        ResultSet res = null;
        try {
            if (conn != null) {
                System.out.println("querying db");
                Statement stmt = conn.createStatement();
                res = stmt.executeQuery(sql);
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("connection failed");
        }
        return res;
    }

    // execute a sql query (e.g. INSERT) to modify the database;
    // no return value needed
    public void modifyDB(String sql) {
        Connection conn = connectDB();
        try {
            if (conn != null) {
                System.out.println("DBUtility modifyDB sql: " + sql);
                Statement stmt = conn.createStatement();
                stmt.execute(sql);
                stmt.close();
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        // You can test the methods you created here
        DBUtility util = new DBUtility();

        // 1. create a user
        util.modifyDB("insert into reviews (fn, ln) values ('test_user_1_fN', 'test_user_1_lN')");

        // 2. query the database
        ResultSet res = util.queryDB("select * from pois");
        while (res.next()) {
            System.out.println(res.getString("title"));
        }

    }

}