/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao.mysql;

import br.uff.ic.archd.db.dao.*;
import br.uff.ic.archd.javacode.JavaAbstract;
import br.uff.ic.archd.javacode.JavaClass;
import br.uff.ic.archd.javacode.JavaInterface;
import br.uff.ic.archd.javacode.JavaPackage;
import br.uff.ic.archd.javacode.JavaProject;
import java.io.File;
import java.sql.Array;
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
public class MySQLClassesDao implements ClassesDao {

    private Connection connection;

    public MySQLClassesDao() {
        


    }

    @Override
    public void save(JavaClass javaClass) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            Statement stm = connection.createStatement();
            System.out.println("Java class: " + javaClass);
            System.out.println("insert into JAVA_CLASSES (name ,"
                    + "original_signature ,"
                    + "superclass ,"
                    + "path ,"
                    + "revision_id ,"
                    + "java_package ) "
                    + " VALUES ('" + javaClass.getFullQualifiedName() + "',"
                    + "'" + javaClass.getOriginalSignature()+ "',"
                    + (javaClass.getSuperClass() == null ? "null" : "'" + javaClass.getSuperClass().getFullQualifiedName() + "'") + ","
                    + "'" + javaClass.getPath() + "',"
                    + "'" + javaClass.getRevisionId() + "');");
            stm.executeUpdate("insert into JAVA_CLASSES (name ,"
                    + "original_signature ,"
                    + "superclass ,"
                    + "path ,"
                    + "revision_id,"
                    + "access_to_foreign_data_number,"
                    + "number_of_direct_connections)"
                    + " VALUES ('" + javaClass.getFullQualifiedName() + "',"
                    + "'" + javaClass.getOriginalSignature()+ "',"
                    + (javaClass.getSuperClass() == null ? "null" : "'" + javaClass.getSuperClass().getFullQualifiedName() + "'") + ","
                    + "'" + javaClass.getPath() + "',"
                    + "'" + javaClass.getRevisionId() + "',"+javaClass.getAccessToForeignDataNumber()+","+javaClass.getNumberOfDirectConnections()+");");


            ResultSet rs = stm.getGeneratedKeys();
            long id = 0;
            if (rs.next()) {
                //System.out.println("ID: "+id);
                id = rs.getLong(1);
            }
            /*ResultSet genKeys = stm.getGeneratedKeys();
             long id = 0;
             if (genKeys.next()) {
             id = genKeys.getLong(1);
             }*/
            javaClass.setId(id);
            System.out.println("Classe ID: " + javaClass.getId());
            stm.close();
            rs.close();

        } catch (SQLException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }
    
    
    public void save(List<JavaClass> javaClasses) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            if(!javaClasses.isEmpty()){
                String query = "insert into JAVA_CLASSES (name ,"
                    + "original_signature ,"
                    + "superclass ,"
                    + "path ,"
                    + "revision_id,"
                    + "access_to_foreign_data_number,"
                    + "number_of_direct_connections) "
                    + " VALUES ";
                JavaClass javaClass = javaClasses.get(0);
                query = query +"('" + javaClass.getFullQualifiedName() + "',"
                    + "'" + javaClass.getOriginalSignature()+ "',"
                    + (javaClass.getSuperClass() == null ? "null" : "'" + javaClass.getSuperClass().getFullQualifiedName() + "'") + ","
                    + "'" + javaClass.getPath() + "',"
                    + "'" + javaClass.getRevisionId() + "',"+javaClass.getAccessToForeignDataNumber()+","+javaClass.getNumberOfDirectConnections()+")";
                for(int i = 1; i < javaClasses.size(); i++){
                    javaClass = javaClasses.get(i);
                    query = query + ", ('" + javaClass.getFullQualifiedName() + "',"
                    + "'" + javaClass.getOriginalSignature()+ "',"
                    + (javaClass.getSuperClass() == null ? "null" : "'" + javaClass.getSuperClass().getFullQualifiedName() + "'") + ","
                    + "'" + javaClass.getPath() + "',"
                    + "'" + javaClass.getRevisionId() + "',"+javaClass.getAccessToForeignDataNumber()+","+javaClass.getNumberOfDirectConnections()+")";
                    
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
                    javaClass = javaClasses.get(i);
                    javaClass.setId(id);
                    //System.out.println("Classe ID i: "+i+": " + javaClass.getId());
                    i++;
                }
                
                stm.close();
                rs.close();
            }
            
            

        } catch (SQLException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }
    
    
//    public static void main(String args[]){
//        MySQLClassesDao mySQLClassesDao = new MySQLClassesDao();
//        List<JavaClass> jclasses = new LinkedList();
//        JavaPackage jp = new JavaPackage("d");
//        JavaClass jc1 = new JavaClass();
//        jc1.setName("e");
//        jc1.setJavaPackage(jp);
//        JavaClass jc2 = new JavaClass();
//        jc2.setName("f");
//        jc2.setJavaPackage(jp);
//        JavaClass jc3 = new JavaClass();
//        jc3.setName("g");
//        jc3.setJavaPackage(jp);
//        JavaClass jc4 = new JavaClass();
//        jc4.setName("h");
//        jc4.setJavaPackage(jp);
//        JavaClass jc5 = new JavaClass();
//        jc5.setName("i");
//        jc5.setJavaPackage(jp);
//        JavaClass jc6 = new JavaClass();
//        jc6.setName("j");
//        jc6.setJavaPackage(jp);
//        JavaClass jc7 = new JavaClass();
//        jc7.setName("k");
//        jc7.setJavaPackage(jp);
//        JavaClass jc8 = new JavaClass();
//        jc8.setName("l");
//        jc8.setJavaPackage(jp);
//        JavaClass jc9 = new JavaClass();
//        jc9.setName("m");
//        jc9.setJavaPackage(jp);
//        JavaClass jc10 = new JavaClass();
//        jc10.setName("n");
//        jc10.setJavaPackage(jp);
//        JavaClass jc[] = new JavaClass[100];
//        for(int i = 0 ; i < 100; i++){
//            jc[i] = new JavaClass();
//            jc[i].setName("n"+String.valueOf(i));
//            jc[i].setJavaPackage(jp);
//        }
//        
//        
//        
//        
////        jclasses.add(jc1);
////        jclasses.add(jc2);
////        jclasses.add(jc3);
////        jclasses.add(jc4);
////        jclasses.add(jc5);
////        jclasses.add(jc6);
////        jclasses.add(jc7);
////        jclasses.add(jc8);
////        jclasses.add(jc9);
////        jclasses.add(jc10);
//        
//        for(int i = 0 ; i < 100; i++){
//            jclasses.add(jc[i]);
//        }
//        
//        long t1 = System.currentTimeMillis();
////        mySQLClassesDao.save(jc1);
////        mySQLClassesDao.save(jc2);
////        mySQLClassesDao.save(jc3);
////        mySQLClassesDao.save(jc4);
////        mySQLClassesDao.save(jc5);
////        mySQLClassesDao.save(jc6);
////        mySQLClassesDao.save(jc7);
////        mySQLClassesDao.save(jc8);
////        mySQLClassesDao.save(jc9);
////        mySQLClassesDao.save(jc10);
//        
////        for(int i = 0 ; i < 100; i++){
////            mySQLClassesDao.save(jc[i]);
////        }
//        
//        mySQLClassesDao.save(jclasses);
//        
//        long t2 = System.currentTimeMillis();
//        System.out.println("Terminou tempo: "+(t2-t1));
//
//        
//        
//    }
    


    @Override
    public List<JavaClass> getAllJavaClass() {
        return null;

    }

    @Override
    public List<JavaClass> getJavaClassesByRevisionId(JavaProject javaProject, String revisionId) {
        connection = MysqlConnectionFactory.getConnection();
        List<JavaClass> javaClasses = new LinkedList();
        try {
            Statement stm = connection.createStatement();
            
            //long t1 = System.currentTimeMillis();
            ResultSet rs = stm.executeQuery("select * from JAVA_CLASSES where revision_id='" + revisionId + "';");
            //long t2 = System.currentTimeMillis();
            //System.out.println("Pegar todas as classes de uma revisão (somente o select) : "+(t2-t1)+"  milisegundos");
            
            int i = 0;

            while (rs.next()) {
                String superClassString = rs.getString("superclass");

                JavaClass superClass = null;
                if (superClassString != null) {
                    String superClassSplit[] = superClassString.split("\\.");
                    String superClassName = superClassSplit[superClassSplit.length - 1];
                    int n = superClassString.length() - (superClassName.length() + 1);
                    if (n < 0) {
                        n = 0;
                    }
                    String superClassPackage = superClassString.substring(0, n);
                    superClass = (JavaClass) javaProject.getClassByName(superClassString);
                    if (superClass == null) {
                        superClass = new JavaClass();

                        superClass.setName(superClassName);
                        superClass.setOriginalSignature(rs.getString("original_signature"));
                        JavaPackage javaPackage = javaProject.getPackageByName(superClassPackage);
                        if (javaPackage == null) {
                            javaPackage = new JavaPackage(superClassPackage);
                            javaPackage.setOriginalSignature(superClassPackage);
                            javaProject.addPackage(javaPackage);
                        }
                        superClass.setRevisionId(revisionId);
                        superClass.setJavaPackage(javaPackage);
                        javaPackage.addJavaAbstract(superClass);
                        javaProject.addClass(superClass);

                    }

                }
                String path = rs.getString("path");
                String classString = rs.getString("name");
                String classSplit[] = classString.split("\\.");
                String className = classSplit[classSplit.length - 1];
                int n = classString.length() - (className.length() + 1);
                if (n < 0) {
                    n = 0;
                }
                String classPackage = classString.substring(0, n);
                JavaClass javaClass = (JavaClass) javaProject.getClassByName(classString);
                if (javaClass == null) {
                    javaClass = new JavaClass();

                    javaClass.setName(className);
                    JavaPackage javaPackage = javaProject.getPackageByName(classPackage);
                    if (javaPackage == null) {
                        javaPackage = new JavaPackage(classPackage);
                        javaPackage.setOriginalSignature(classPackage);
                        javaProject.addPackage(javaPackage);
                    }
                    javaClass.setJavaPackage(javaPackage);
                    javaPackage.addJavaAbstract(javaClass);
                }
                javaClass.setPath(path);
                javaClass.setOriginalSignature(rs.getString("original_signature"));
                javaClass.setRevisionId(revisionId);
                javaClass.setSuperClass(superClass);
                javaClass.setId(Long.valueOf(rs.getString("id")));
                javaClass.setAccessToForeignDataNumber(Integer.valueOf(rs.getString("access_to_foreign_data_number")));
                javaClass.setNumberOfDirectConnections(Integer.valueOf(rs.getString("number_of_direct_connections")));
                javaClasses.add(javaClass);
                javaProject.addClass(javaClass);
                i++;
            }
            
            stm.close();
            rs.close();
            //stm.execute("SHUTDOWN");
            //System.out.println("QUANTIDADE: " + i);
        } catch (Exception e) {
            System.out.println("ERRO get classes: " + e.getMessage());
        }
        return javaClasses;
    }
}
