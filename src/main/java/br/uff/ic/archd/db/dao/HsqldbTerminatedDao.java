/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao;

import br.uff.ic.archd.javacode.JavaClass;
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
public class HsqldbTerminatedDao implements TerminatedDao {

    private Connection connection;

    HsqldbTerminatedDao() {
        try {
            File file = new File(Constants.DB_DIR);
            if(!file.exists()){
                file.mkdirs();
                
            }
            Class.forName("org.hsqldb.jdbcDriver");
            connection = DriverManager.getConnection("jdbc:hsqldb:file:" + Constants.DB_DIR, "archd", "123");
            DatabaseMetaData dbData = connection.getMetaData();
            ResultSet tables = dbData.getTables(null, null, "TERMINATED", null);
            //System.out.println("NEXT "+tables.next());
            if (!tables.next()) {


                System.out.println("NAO POSSUI TABELA TERMINATED");
                Statement stm = connection.createStatement();

                stm.executeUpdate("create table TERMINATED (id bigint IDENTITY, "
                        + "project_name varchar(1000),"
                        + "revision_id varchar(1000));");
            } else {
                System.out.println("TABELA JAH EXISTE TERMINATED");
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

    @Override
    public void save(String projectName, String revisionId) {
        try {
            Statement stm = connection.createStatement();
            System.out.println("insert into TERMINATED (project_name , revision_id) "
                    + " VALUES ('" + projectName + "','" + revisionId + "');");
            stm.executeUpdate("insert into TERMINATED (project_name , revision_id) "
                    + " VALUES ('" + projectName + "','" + revisionId + "');");
            

        } catch (SQLException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    @Override
    public boolean isTerminated(String projectName, String revisionId) {
        boolean isTerminated = false;
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = null;
            rs = stm.executeQuery("select * from TERMINATED where project_name='" + projectName+ "' and revision_id='"+revisionId+"';");
            
            while(rs.next()){
                isTerminated = true;
            }
            
            //stm.execute("SHUTDOWN");
        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
        }
        
        return isTerminated;
        
    }
}
