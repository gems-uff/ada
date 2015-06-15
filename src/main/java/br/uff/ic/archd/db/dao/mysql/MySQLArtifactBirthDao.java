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
public class MySQLArtifactBirthDao implements ArtifactBirthDao{
    
    
    private Connection connection;

    public MySQLArtifactBirthDao() {
        


    }

    @Override
    public void save(String artifactSignature, String revisionBirthNumber) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            Statement stm = connection.createStatement();
            System.out.println("insert into ARTIFACT_BIRTH (artifact_signature ,"
                    + "revision_id_birth )"

                    + " VALUES ('" + artifactSignature + "',"
                    + "'" + revisionBirthNumber + "');");
            stm.executeUpdate("insert into ARTIFACT_BIRTH (artifact_signature ,"
                    + "revision_id_birth )"

                    + " VALUES ('" + artifactSignature + "',"
                    + "'" + revisionBirthNumber + "');");
            
            stm.close();

            

        } catch (SQLException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }
    
    public void save(List<String> artifactSignatures, String revisionBirthNumber) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            if(!artifactSignatures.isEmpty()){
                String query = "insert into ARTIFACT_BIRTH (artifact_signature ,"
                    + "revision_id_birth )"

                    + " VALUES ";
                String artifactSignature = artifactSignatures.get(0);
                query = query + "('" + artifactSignature + "',"
                    + "'" + revisionBirthNumber + "')";
                for(int i = 1; i < artifactSignatures.size(); i++){
                    artifactSignature = artifactSignatures.get(i);
                    query = query + ", ('" + artifactSignature + "',"
                    + "'" + revisionBirthNumber + "')";
                }
                query = query + ";";
                System.out.println(query);
                PreparedStatement stm = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                stm.execute();
                
                stm.close();
            }
            

            

        } catch (SQLException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    @Override
    public String getBirthIdBirth(String artifactSignature) {
        connection = MysqlConnectionFactory.getConnection();
        String revisionbirthId = null;
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = null;
            rs = stm.executeQuery("select * from ARTIFACT_BIRTH where artifact_signature='" + artifactSignature+ "' order by id desc;");
            
            while(rs.next()){
                revisionbirthId = rs.getString("revision_id_birth");
            }
            
            stm.close();
            rs.close();
            
            //stm.execute("SHUTDOWN");
        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
        }
        
        return revisionbirthId;
    }

    @Override
    public List<String> getArtifactsBorn(String revisionBirthNumber) {
        connection = MysqlConnectionFactory.getConnection();
        List<String> list = new LinkedList();
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = null;
            rs = stm.executeQuery("select * from ARTIFACT_BIRTH where revision_id_birth='" + revisionBirthNumber+ "';");
            
            while(rs.next()){
                list.add(rs.getString("artifact_signature"));
            }
            
            stm.close();
            rs.close();
            
            //stm.execute("SHUTDOWN");
        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
        }
        return list;
    }
    
    @Override
    public void printAll(){
        connection = MysqlConnectionFactory.getConnection();
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = null;
            rs = stm.executeQuery("select * from ARTIFACT_BIRTH;");
            
            while(rs.next()){
                System.out.println("Artifact Signature: "+rs.getString("artifact_signature")+"   birth: "+rs.getString("revision_id_birth"));
            }
            
            stm.close();
            rs.close();
            
            //stm.execute("SHUTDOWN");
        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }
    
    
}
