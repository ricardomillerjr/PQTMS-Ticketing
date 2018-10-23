
/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package ticketing;

/**
 *
 * @author melrose27
 */
import ticketing.dao.DBType;
import java.io.FileInputStream;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.Properties;

import javax.swing.JOptionPane;

public class ConnectionManager {

    private static ConnectionManager instance = null;
    private DBType dbType = DBType.MYSQL;
    Properties p2 = new Properties();
    private Connection conn = null;

    private ConnectionManager() {
        p2 = loadProperties("Application.ini");
    }

    public void close() {
        System.out.println("Closing connection");
        try {
            conn.close();
            conn = null;
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        }
    }

    public static Properties loadProperties(String sFile) {
        Properties p = new Properties();
            try (FileInputStream in = new FileInputStream(sFile)) {
                p.load(in);
        } catch (IOException iOException) {
            System.err.println(iOException.getMessage());
        }
        return p;
    }

    private boolean openConnection() {
        try {
            switch (dbType) {
                case MYSQL: {
                    String M_CONN_STRING = "jdbc:mysql://" + p2.getProperty("TARGET_IP") + ":"+p2.getProperty("TARGET_PORT")+"/counter?useSSL=false";
                    conn = DriverManager.getConnection(M_CONN_STRING, "itmu03", "phic");
                    return true;
                }
                default:
                    return false;
            }
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
    }

    public Connection getConnection() {
        if (conn == null) {
            if (openConnection()) {
                System.out.println("Connection opened");
                return conn;
            } else {
                return null;
            }
        }
        return conn;
    }

    public void setDBType(DBType dbType) {
        this.dbType = dbType;
    }

    public static ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }
}
