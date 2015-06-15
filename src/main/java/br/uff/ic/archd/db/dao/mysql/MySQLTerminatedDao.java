/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao.mysql;

import br.uff.ic.archd.db.dao.*;
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
public class MySQLTerminatedDao implements TerminatedDao {

    private Connection connection;

    public MySQLTerminatedDao() {
        
    }

    @Override
    public void save(String projectName, String revisionId) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            Statement stm = connection.createStatement();
            System.out.println("insert into CALCULATED_PROJECTS (project_name , revision_id) "
                    + " VALUES ('" + projectName + "','" + revisionId + "');");
            stm.executeUpdate("insert into CALCULATED_PROJECTS (project_name , revision_id) "
                    + " VALUES ('" + projectName + "','" + revisionId + "');");
            

            stm.close();
        } catch (SQLException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    @Override
    public boolean isTerminated(String projectName, String revisionId) {
        connection = MysqlConnectionFactory.getConnection();
        boolean isTerminated = false;
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = null;
            rs = stm.executeQuery("select * from CALCULATED_PROJECTS where project_name='" + projectName+ "' and revision_id='"+revisionId+"';");
            
            while(rs.next()){
                isTerminated = true;
            }
            
            stm.close();
            rs.close();
            
            //stm.execute("SHUTDOWN");
        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
        }
        
        return isTerminated;
        
    }
}
