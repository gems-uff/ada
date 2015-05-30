/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao.mysql;

import br.uff.ic.archd.db.dao.*;
import br.uff.ic.archd.javacode.JavaInterface;
import br.uff.ic.archd.javacode.JavaPackage;
import br.uff.ic.archd.javacode.JavaProject;
import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author wallace
 */
public class MySQLInterfaceDao implements InterfaceDao{

    private Connection connection;
    
    public MySQLInterfaceDao(){
        

    }
    @Override
    public void save(JavaInterface javaInterface) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            Statement stm = connection.createStatement();
            System.out.println("insert into JAVA_INTERFACES (name ,"
                    + "original_signature,"
                    + "path,"
                    + "revision_id ,"
                    + "java_package ) "
                    + " VALUES ('" + javaInterface.getFullQualifiedName() + "',"
                    + "'" + javaInterface.getOriginalSignature() + "',"
                    + "'" + javaInterface.getPath() + "',"
                    + "'" + javaInterface.getRevisionId() + "');");
            stm.executeUpdate("insert into JAVA_INTERFACES (name ,"
                    + "original_signature ,"
                    + "path ,"
                    + "revision_id )"
                    + " VALUES ('" + javaInterface.getFullQualifiedName() + "',"
                    + "'" + javaInterface.getOriginalSignature() + "',"
                    + "'" + javaInterface.getPath() + "',"
                    + "'" + javaInterface.getRevisionId() + "');");
            ResultSet rs = stm.getGeneratedKeys();
            long id = 0;
            if (rs.next()) {
                id = rs.getLong(1);
            }
            javaInterface.setId(id);
            System.out.println("Interface ID: "+javaInterface.getId());
        } catch (SQLException e) {
            System.out.println("ERRO interface: " + e.getMessage());
        }
    }
    
    public void save(List<JavaInterface> javaInterfaces) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            if(!javaInterfaces.isEmpty()){
                String query = "insert into JAVA_INTERFACES (name ,"
                    + "original_signature ,"
                    + "path ,"
                    + "revision_id )"
                    + " VALUES ";
                JavaInterface javaInterface = javaInterfaces.get(0);
                query = query + " ('" + javaInterface.getFullQualifiedName() + "',"
                    + "'" + javaInterface.getOriginalSignature() + "',"
                    + "'" + javaInterface.getPath() + "',"
                    + "'" + javaInterface.getRevisionId() + "')";
                for(int i = 1; i < javaInterfaces.size(); i++){
                    javaInterface = javaInterfaces.get(i);
                    query = query + ", ('" + javaInterface.getFullQualifiedName() + "',"
                    + "'" + javaInterface.getOriginalSignature() + "',"
                    + "'" + javaInterface.getPath() + "',"
                    + "'" + javaInterface.getRevisionId() + "')";
                }
                query = query + ";";
                System.out.println(query);
                PreparedStatement stm = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                stm.execute();
                ResultSet rs = stm.getGeneratedKeys();
                int i = 0;
                long id = 0;
                while (rs.next()) {
                    id = rs.getLong(1);
                    javaInterface = javaInterfaces.get(i);
                    javaInterface.setId(id);
                    System.out.println("Interface ID i: "+i+": " + javaInterface.getId());
                    i++;
                }
                    
            }
            
        } catch (SQLException e) {
            System.out.println("ERRO interface: " + e.getMessage());
        }
    }

    @Override
    public List<JavaInterface> getJavaInterfacesByRevisionId(JavaProject javaProject, String revisionId) {
        connection = MysqlConnectionFactory.getConnection();
        List<JavaInterface> javaInterfaces = new LinkedList();
        try {
            Statement stm = connection.createStatement();
            
            //long t1 = System.currentTimeMillis();
            ResultSet rs = stm.executeQuery("select * from JAVA_INTERFACES where revision_id='" + revisionId + "';");
            //long t2 = System.currentTimeMillis();
            //System.out.println("Pegar todas as interfaces de uma revisão (somente o select) : "+(t2-t1)+"  milisegundos");
            int i = 0;

            while (rs.next()) {
                
                String path = rs.getString("path");
                String classString = rs.getString("name");
                String classSplit[] = classString.split("\\.");
                String className = classSplit[classSplit.length - 1];
                int n = classString.length() - (className.length()+1);
                if(n < 0){
                    n = 0;
                }
                String classPackage = classString.substring(0, n);
                JavaInterface javaInterface = (JavaInterface) javaProject.getClassByName(classString);
                if (javaInterface == null) {
                    javaInterface = new JavaInterface(path);

                    javaInterface.setName(className);
                    JavaPackage javaPackage = javaProject.getPackageByName(classPackage);
                    if (javaPackage == null) {
                        javaPackage = new JavaPackage(classPackage);
                        javaProject.addPackage(javaPackage);
                    }
                    javaInterface.setJavaPackage(javaPackage);
                    javaPackage.addJavaAbstract(javaInterface);
                }
                javaInterface.setOriginalSignature(rs.getString("original_signature"));
                javaInterface.setRevisionId(revisionId);
                javaInterface.setId(Long.valueOf(rs.getString("id")));
                javaInterfaces.add(javaInterface);
                javaProject.addClass(javaInterface);
                i++;
            }
            //stm.execute("SHUTDOWN");
            //System.out.println("QUANTIDADE: " + i);
        } catch (Exception e) {
            System.out.println("ERRO get interfaces: " + e.getMessage());
        }
        return javaInterfaces;
    }
    
}
