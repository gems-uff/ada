/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author wallace
 */
public class HsqldbMetricValueDao {
    private Connection connection;
    public HsqldbMetricValueDao() {
        try {
            File file = new File(Constants.DB_DIR);
            if(!file.exists()){
                file.mkdirs();
                
            }
            Class.forName("org.hsqldb.jdbcDriver");
            connection = DriverManager.getConnection("jdbc:hsqldb:file:" + Constants.DB_DIR, "archd", "123");
            DatabaseMetaData dbData = connection.getMetaData();
            ResultSet tables = dbData.getTables(null, null, "METRIC_VALUES", null);
            //System.out.println("NEXT "+tables.next());
            if (!tables.next()) {


                System.out.println("NAO POSSUI TABELA METRIC_VALUES");
                Statement stm = connection.createStatement();

                stm.executeUpdate("create table METRIC_VALUES (project_name varchar(255) , "
                        + "metric varchar(255),"
                        + "revision_id varchar(255),"
                        + "versioned_item varchar(1000),"
                        + "value real,"
                        + "timestamp bigint);");
            }else{
                System.out.println("TABELA JAH EXISTE METRIC_VALUES");
            }

            //stm.execute("SHUTDOWN");

        } catch (ClassNotFoundException e) {
            System.out.println("Erro ao carregar o driver JDBC. ");
        } catch (SQLException e) {
            System.out.println("Erro de SQL: " + e);
            //e.printStackTrace();
        } catch (Exception e) {
            System.out.println("ERRO " + e.getMessage());
        }


    }
}
