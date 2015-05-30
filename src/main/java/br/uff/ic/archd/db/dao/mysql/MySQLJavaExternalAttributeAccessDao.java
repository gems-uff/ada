/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao.mysql;

import br.uff.ic.archd.db.dao.*;
import br.uff.ic.archd.javacode.JavaAbstract;
import br.uff.ic.archd.javacode.JavaClass;
import br.uff.ic.archd.javacode.JavaExternalAttributeAccess;
import br.uff.ic.archd.javacode.JavaMethod;
import br.uff.ic.archd.javacode.JavaProject;
import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *
 * @author wallace
 */
public class MySQLJavaExternalAttributeAccessDao implements JavaExternalAttributeAccessDao {

    private Connection connection;

    public MySQLJavaExternalAttributeAccessDao() {

    }

    @Override
    public void saveJavaExternalAttributeAccess(JavaMethod javaMethod) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            Statement stm = connection.createStatement();
            for (JavaExternalAttributeAccess javaExternalAttributeAccess : javaMethod.getJavaExternalAttributeAccessList()) {
                JavaAbstract javaAbstract = javaExternalAttributeAccess.getJavaAbstract();
                if (javaAbstract.getClass() == JavaClass.class) {
                    System.out.println("insert into JAVA_EXTERNAL_ATTRIBUTE_ACCESS (method_id, class_id , attribute_name) "
                            + " VALUES (" + javaMethod.getId() + "," + javaAbstract.getId() + ",'" + javaExternalAttributeAccess.getAttributeName() + "');");
                    stm.executeUpdate("insert into JAVA_EXTERNAL_ATTRIBUTE_ACCESS (method_id, class_id , attribute_name) "
                            + " VALUES (" + javaMethod.getId() + "," + javaAbstract.getId() + ",'" + javaExternalAttributeAccess.getAttributeName() + "');");

                } else {
                    System.out.println("insert into JAVA_EXTERNAL_ATTRIBUTE_ACCESS (method_id, interface_id , attribute_name) "
                            + " VALUES (" + javaMethod.getId() + "," + javaAbstract.getId() + ",'" + javaExternalAttributeAccess.getAttributeName() + "');");
                    stm.executeUpdate("insert into JAVA_EXTERNAL_ATTRIBUTE_ACCESS (method_id, interface_id , attribute_name) "
                            + " VALUES (" + javaMethod.getId() + "," + javaAbstract.getId() + ",'" + javaExternalAttributeAccess.getAttributeName() + "');");
                }
            }

        } catch (SQLException e) {
            System.out.println("ERRO saveJavaExternalAttributeAccess: " + e.getMessage());
        }
    }

    public void saveJavaExternalAttributeAccess(List<JavaMethod> javaMethods) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            int counter1 = 0;
            int counter2 = 0;
            String query1 = "insert into JAVA_EXTERNAL_ATTRIBUTE_ACCESS (method_id, class_id , attribute_name) "
                    + " VALUES ";
            String query2 = "insert into JAVA_EXTERNAL_ATTRIBUTE_ACCESS (method_id, interface_id , attribute_name) "
                    + " VALUES ";
            for (JavaMethod javaMethod : javaMethods) {
                for (JavaExternalAttributeAccess javaExternalAttributeAccess : javaMethod.getJavaExternalAttributeAccessList()) {
                    JavaAbstract javaAbstract = javaExternalAttributeAccess.getJavaAbstract();
                    if (javaAbstract.getClass() == JavaClass.class) {
                        if(counter1 == 0){
                            
                            query1 = query1 + "(" + javaMethod.getId() + "," + javaAbstract.getId() + ",'" + javaExternalAttributeAccess.getAttributeName() + "')";
                            counter1++;
                        }else{
                            query1 = query1 + ", (" + javaMethod.getId() + "," + javaAbstract.getId() + ",'" + javaExternalAttributeAccess.getAttributeName() + "')";
                        }
                        

                    } else {
                        if(counter2 == 0){
                            query2 = query2 + "(" + javaMethod.getId() + "," + javaAbstract.getId() + ",'" + javaExternalAttributeAccess.getAttributeName() + "')";
                            counter2++;
                        }else{
                            query2 = query2 + ", (" + javaMethod.getId() + "," + javaAbstract.getId() + ",'" + javaExternalAttributeAccess.getAttributeName() + "')";
                        }
                        
                    }
                }
            }
            if(counter1 != 0){
                query1 = query1 +";";
                PreparedStatement stm = connection.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
                stm.execute();
            }
            if(counter2 != 0){
                query2 = query2 +";";
                PreparedStatement stm = connection.prepareStatement(query2, Statement.RETURN_GENERATED_KEYS);
                stm.execute();
            }
            

        } catch (SQLException e) {
            System.out.println("ERRO saveJavaExternalAttributeAccess: " + e.getMessage());
        }
    }

    @Override
    public void getJavaExternalAttributeAccessByMethod(JavaMethod javaMethod, JavaProject javaProject) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = null;
            //long t1 = System.currentTimeMillis();
            rs = stm.executeQuery("select * from JAVA_EXTERNAL_ATTRIBUTE_ACCESS where method_id=" + javaMethod.getId() + ";");
            //long t2 = System.currentTimeMillis();
            //System.out.println("Pegar todas as invocações de métodos de uma classe de uma revisão (somente o select) : "+(t2-t1)+"  milisegundos");

            int i = 0;

            while (rs.next()) {

                String classId = rs.getString("class_id");
                String interfaceId = rs.getString("interface_id");
                String attributeName = rs.getString("attribute_name");
                JavaAbstract javaAbstract = null;
                if (classId != null) {
                    javaAbstract = javaProject.getClassById(Long.valueOf(classId));
                } else {
                    javaAbstract = javaProject.getInterfaceById(Long.valueOf(interfaceId));
                }

                JavaExternalAttributeAccess javaExternalAttributeAccess = new JavaExternalAttributeAccess(javaAbstract, attributeName);
                javaMethod.addExternalAttributeAccess(javaExternalAttributeAccess);

                i++;
            }
            //stm.execute("SHUTDOWN");
            //System.out.println("QUANTIDADE: " + i);
        } catch (Exception e) {
            System.out.println("ERRO getJavaExternalAttributeAccessByMethod: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
