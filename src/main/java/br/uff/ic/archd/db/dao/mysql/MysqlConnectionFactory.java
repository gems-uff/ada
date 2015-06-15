/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnectionFactory {

    //static reference to itself
    private static MysqlConnectionFactory instance = new MysqlConnectionFactory();
    private Connection connection;
    public static final String URL = "jdbc:mysql://localhost:3306/archd";
    public static final String USER = "root";
    public static final String PASSWORD = "admin";
    public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
    private int number;

    private MysqlConnectionFactory() {
        try {
            Class.forName(DRIVER_CLASS);
            connection = null;
            number = 0;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection createConnection() {

        if (connection == null) {
            System.out.println("MysqlConnectionFactory.createConnection()");
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                System.out.println("ERROR: Unable to Connect to Database. " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
        } else {
            number++;
            if (number >= 100000) {
                number = 0;
                System.out.println("----------------------------------------------------------------");
                System.out.println("----------------------------------------------------------------");
                System.out.println("----------------------------------------------------------------");
                System.out.println("----------------------------------------------------------------");
                System.out.println("---------------- VAI FECHAR A CONEXÂO, VAI ABRIR OUTRA");
                System.out.println("----------------------------------------------------------------");
                System.out.println("----------------------------------------------------------------");
                System.out.println("----------------------------------------------------------------");
                System.out.println("----------------------------------------------------------------");
                try {
                    connection.close();
                    connection = DriverManager.getConnection(URL, USER, PASSWORD);
                } catch (Exception e) {
                    System.out.println("Erro fechar conexão MySqlConnectionFactory: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return connection;
    }

    public static Connection getConnection() {
        return instance.createConnection();
    }
}
