/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao.mysql;

import br.uff.ic.archd.db.dao.*;
import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author wallace
 */
public class MySQLOriginalNameDao implements OriginalNameDao{
    
    
    private Connection connection;

    public MySQLOriginalNameDao() {
        


    }

    @Override
    public void save(String artifactName, String originalName) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            Statement stm = connection.createStatement();
            System.out.println("insert into ORIGINAL_NAME (artifact_signature ,"
                    + "original_name )"

                    + " VALUES ('" + artifactName + "',"
                    + "'" + originalName + "');");
            stm.executeUpdate("insert into ORIGINAL_NAME (artifact_signature ,"
                    + "original_name )"

                    + " VALUES ('" + artifactName + "',"
                    + "'" + originalName + "');");

            

        } catch (SQLException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }
    
    public void save(List<String> artifactNames) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            if(!artifactNames.isEmpty()){
                String query = "insert into ORIGINAL_NAME (artifact_signature ,"
                    + "original_name )"

                    + " VALUES ";
                String artifactName = artifactNames.get(0);
                query = query + "('" + artifactName + "',"
                    + "'" + artifactName + "')";
                for(int i = 1; i < artifactNames.size(); i++){
                    artifactName = artifactNames.get(i);
                    query = query + ", ('" + artifactName + "',"
                    + "'" + artifactName + "')";
                }
                query = query + ";";
                System.out.println(query);
                PreparedStatement stm = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                stm.execute();
            }
            
            

        } catch (SQLException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    @Override
    public String getOriginalName(String artifactSignature) {
        connection = MysqlConnectionFactory.getConnection();
        String originalName = null;
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = null;
            rs = stm.executeQuery("select * from ORIGINAL_NAME where artifact_signature='" + artifactSignature+ "';");
            
            while(rs.next()){
                originalName = rs.getString("original_name");
            }
            
            //stm.execute("SHUTDOWN");
        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
        }
        
        return originalName;
    }

    @Override
    public List<String> getArtifactsAlternativesNames(String originalName) {
        connection = MysqlConnectionFactory.getConnection();
        List<String> list = new LinkedList();
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = null;
            rs = stm.executeQuery("select * from ORIGINAL_NAME where original_name='" + originalName+ "';");
            
            while(rs.next()){
                list.add(rs.getString("artifact_signature"));
            }
            
            //stm.execute("SHUTDOWN");
        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
        }
        return list;
    }
    
}
