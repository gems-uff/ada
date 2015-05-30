/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao.mysql;

import br.uff.ic.archd.db.dao.*;
import br.uff.ic.archd.javacode.JavaPackage;
import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author wallace
 */
public class MySQLJavaPackageDao implements JavaPackageDao{

    private Connection connection;
    
    public MySQLJavaPackageDao() {
        


    }
    
    @Override
    public void save(JavaPackage javaPackage) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            Statement stm = connection.createStatement();
            System.out.println("insert into JAVA_PACKAGES (package_signature, original_signature)"
                    + " VALUES ('" + javaPackage.getName() + "'"
                    +", '" + javaPackage.getOriginalSignature() + "'"
                    + ");");
            stm.executeUpdate("insert into JAVA_PACKAGES (package_signature, original_signature)"
                    + " VALUES ('" + javaPackage.getName() + "'"
                    +", '" + javaPackage.getOriginalSignature() + "'"
                    + ");");
            //stm.execute("SHUTDOWN");
        } catch (SQLException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    @Override
    public List<JavaPackage> getAllJavaPackage() {
        connection = MysqlConnectionFactory.getConnection();
        List<JavaPackage> javaPackages = new LinkedList();
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("select * from JAVA_PACKAGES ;");
            int i = 0;
            while (rs.next()) {
                JavaPackage  javaPackage = new JavaPackage(rs.getString("package_signature"));
                javaPackage.setOriginalSignature(rs.getString("original_signature"));
                javaPackages.add(javaPackage);
                i++;
            }
            //stm.execute("SHUTDOWN");
            System.out.println("QUANTIDADE: " + i);
        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
        }
        return javaPackages;
    }
    
}
