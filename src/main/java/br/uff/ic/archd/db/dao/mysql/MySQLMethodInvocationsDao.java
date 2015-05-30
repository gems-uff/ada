/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao.mysql;

import br.uff.ic.archd.db.dao.*;
import br.uff.ic.archd.javacode.JavaAbstract;
import br.uff.ic.archd.javacode.JavaClass;
import br.uff.ic.archd.javacode.JavaMethod;
import br.uff.ic.archd.javacode.JavaMethodInvocation;
import br.uff.ic.archd.javacode.JavaProject;
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
public class MySQLMethodInvocationsDao implements MethodInvocationsDao {

    private Connection connection;

    public MySQLMethodInvocationsDao() {
        
    }

    @Override
    public void saveMethodInvocations(JavaMethod javaMethod, JavaClass javaClass) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            Statement stm = connection.createStatement();
            for (JavaMethodInvocation javaMethodInvocation : javaMethod.getMethodInvocations()) {
                JavaAbstract javaAbstract = javaMethodInvocation.getJavaAbstract();
                if (javaAbstract.getClass() == JavaClass.class) {
                    if (javaMethodInvocation.getUnknowMethodName() != null) {
                        System.out.println("insert into METHOD_INVOCATIONS (method_id, class_id , unknow_method_name) "
                                + " VALUES (" +javaMethod.getId()+","+ javaAbstract.getId() + ",'" + javaMethodInvocation.getUnknowMethodName() + "');");
                        stm.executeUpdate("insert into METHOD_INVOCATIONS (method_id, class_id , unknow_method_name) "
                                + " VALUES (" +javaMethod.getId()+","+ javaAbstract.getId() + ",'" + javaMethodInvocation.getUnknowMethodName() + "');");
                    } else {
                        System.out.println("insert into METHOD_INVOCATIONS (method_id, class_id , invocated_method) "
                                + " VALUES (" +javaMethod.getId()+","+ javaAbstract.getId() + "," + javaMethodInvocation.getJavaMethod().getId() + ");");
                        stm.executeUpdate("insert into METHOD_INVOCATIONS (method_id, class_id , invocated_method) "
                                + " VALUES (" +javaMethod.getId()+","+ javaAbstract.getId() + "," + javaMethodInvocation.getJavaMethod().getId() + ");");
                    }
                } else {
                    if (javaMethodInvocation.getUnknowMethodName() != null) {
                        System.out.println("insert into METHOD_INVOCATIONS (method_id, interface_id , unknow_method_name) "
                                + " VALUES (" +javaMethod.getId()+","+ javaAbstract.getId() + ",'" + javaMethodInvocation.getUnknowMethodName() + "');");
                        stm.executeUpdate("insert into METHOD_INVOCATIONS (method_id, interface_id , unknow_method_name) "
                                + " VALUES (" +javaMethod.getId()+","+ javaAbstract.getId() + ",'" + javaMethodInvocation.getUnknowMethodName() + "');");
                    } else {
                        System.out.println("insert into METHOD_INVOCATIONS (method_id, interface_id , invocated_method) "
                                + " VALUES (" +javaMethod.getId()+","+ javaAbstract.getId() + "," + javaMethodInvocation.getJavaMethod().getId() + ");");
                        stm.executeUpdate("insert into METHOD_INVOCATIONS (method_id, interface_id , invocated_method) "
                                + " VALUES (" +javaMethod.getId()+","+ javaAbstract.getId() + "," + javaMethodInvocation.getJavaMethod().getId() + ");");
                    }
                }
            }
            for (JavaMethod invocatedMethod : javaMethod.getInternalMethodInvocations()) {                
                System.out.println("insert into METHOD_INVOCATIONS (method_id, class_id , invocated_method) "
                        + " VALUES (" +javaMethod.getId()+","+ javaClass.getId() + "," + invocatedMethod.getId() + ");");
                stm.executeUpdate("insert into METHOD_INVOCATIONS (method_id, class_id , invocated_method) "
                        + " VALUES (" +javaMethod.getId()+","+ javaClass.getId() + "," + invocatedMethod.getId() + ");");
            }
            
        } catch (SQLException e) {
            System.out.println("ERRO save methodinvocation: " + e.getMessage());
        }
    }
    
    
    public void saveMethodInvocations(List<JavaMethod> methods) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            int counter1 = 0;
            int counter2 = 0;
            int counter3 = 0;
            int counter4 = 0;
            int counter5 = 0;
            String query1 = "insert into METHOD_INVOCATIONS (method_id, class_id , unknow_method_name) "
                    + " VALUES ";
            String query2 = "insert into METHOD_INVOCATIONS (method_id, class_id , invocated_method) "
                    + " VALUES ";
            String query3 = "insert into METHOD_INVOCATIONS (method_id, interface_id , unknow_method_name) "
                    + " VALUES ";
            String query4 = "insert into METHOD_INVOCATIONS (method_id, interface_id , invocated_method) "
                    + " VALUES  ";
            
            String query5 = "insert into METHOD_INVOCATIONS (method_id, class_id , invocated_method) "
                        + " VALUES ";

            for (JavaMethod javaMethod : methods) {
                for (JavaMethodInvocation javaMethodInvocation : javaMethod.getMethodInvocations()) {
                    JavaAbstract javaAbstract = javaMethodInvocation.getJavaAbstract();
                    if (javaAbstract.getClass() == JavaClass.class) {
                        if (javaMethodInvocation.getUnknowMethodName() != null) {
                            if(counter1 == 0){
                                query1 = query1 + " (" + javaMethod.getId() + "," + javaAbstract.getId() + ",'" + javaMethodInvocation.getUnknowMethodName() + "')";
                                counter1++;
                            }
                            else{
                                query1 = query1 + ", (" + javaMethod.getId() + "," + javaAbstract.getId() + ",'" + javaMethodInvocation.getUnknowMethodName() + "')";
                            }
                        } else {
                            if(counter2 == 0){
                                query2 = query2 + " (" + javaMethod.getId() + "," + javaAbstract.getId() + "," + javaMethodInvocation.getJavaMethod().getId() + ")";
                                counter2++;
                            }
                            else{
                                query2 = query2 + ", (" + javaMethod.getId() + "," + javaAbstract.getId() + "," + javaMethodInvocation.getJavaMethod().getId() + ")";
                            }
                            
                        }
                    } else {
                        if (javaMethodInvocation.getUnknowMethodName() != null) {
                            if(counter3 == 0){
                                query3 = query3 + " (" + javaMethod.getId() + "," + javaAbstract.getId() + ",'" + javaMethodInvocation.getUnknowMethodName() + "')";
                                counter3++;
                            }
                            else{
                                query3 = query3 + ", (" + javaMethod.getId() + "," + javaAbstract.getId() + ",'" + javaMethodInvocation.getUnknowMethodName() + "')";
                            }
                            
                        } else {
                            if(counter4 == 0){
                                query4 = query4 + " (" + javaMethod.getId() + "," + javaAbstract.getId() + "," + javaMethodInvocation.getJavaMethod().getId() + ")";
                                counter4++;
                            }
                            else{
                                query4 = query4 + ", (" + javaMethod.getId() + "," + javaAbstract.getId() + "," + javaMethodInvocation.getJavaMethod().getId() + ")";
                            }
                            
                        }
                    }
                }
                
                for (JavaMethod invocatedMethod : javaMethod.getInternalMethodInvocations()) {
                    if(counter5 == 0){
                        query5 = query5 + "(" + javaMethod.getId() + "," + javaMethod.getItemId()+ "," + invocatedMethod.getId() + ")";
                        counter5++;
                    }else{
                        query5 = query5 + ", (" + javaMethod.getId() + "," + javaMethod.getItemId() + "," + invocatedMethod.getId() + ")";
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
            if(counter3 != 0){
                query3 = query3 +";";
                PreparedStatement stm = connection.prepareStatement(query3, Statement.RETURN_GENERATED_KEYS);
                stm.execute();
            }
            if(counter4 != 0){
                query4 = query4 +";";
                PreparedStatement stm = connection.prepareStatement(query4, Statement.RETURN_GENERATED_KEYS);
                stm.execute();
            }
            if(counter5 != 0){
                query5 = query5 +";";
                PreparedStatement stm = connection.prepareStatement(query5, Statement.RETURN_GENERATED_KEYS);
                stm.execute();
            }
            

            
        } catch (SQLException e) {
            System.out.println("ERRO save methodinvocation: " + e.getMessage());
        }
    }

    @Override
    public void getInvocatedMethods(JavaMethod javaMethod, JavaClass javaClass, JavaProject javaProject) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = null;
            //long t1 = System.currentTimeMillis();
            rs = stm.executeQuery("select * from METHOD_INVOCATIONS where method_id=" + javaMethod.getId() + ";");
            //long t2 = System.currentTimeMillis();
            //System.out.println("Pegar todas as invocações de métodos de uma classe de uma revisão (somente o select) : "+(t2-t1)+"  milisegundos");
            
            int i = 0;

            while (rs.next()) {
                
                String classId = rs.getString("class_id");
                String interfaceId = rs.getString("interface_id");
                String unknowMethodName = rs.getString("unknow_method_name");
                String invocatedMethodString = rs.getString("invocated_method");
                JavaAbstract javaAbstract = null;
                if(classId != null){
                    javaAbstract = javaProject.getClassById(Long.valueOf(classId));
                }else{
                    javaAbstract = javaProject.getInterfaceById(Long.valueOf(interfaceId));
                }
                if(javaAbstract == javaClass){
                    
                    /*System.out.println("Invocated Method: "+invocatedMethodString);
                    System.out.println("Class ID: "+javaAbstract.getId());
                    System.out.println("Class ID MY: "+javaClass.getId());
                    System.out.println("unknow_method_name: "+unknowMethodName);
                    System.out.println("JAVA METHOD: "+javaMethod.getMethodSignature());
                    System.out.println("JAVA CLASS: "+javaClass.getPath());*/
                    if(unknowMethodName != null){
                        JavaMethodInvocation javaInternalInvocation = new JavaMethodInvocation(javaAbstract, null);
                        javaInternalInvocation.setUnknowMethodName(unknowMethodName);
                        javaMethod.addMethodInvocation(javaInternalInvocation);
                    }else{
                        javaMethod.addInternalMethodInvocation(javaClass.getMethodById(Long.valueOf(invocatedMethodString)));
                    }
                }else{
                    if(unknowMethodName != null){
                        JavaMethodInvocation javaInternalInvocation = new JavaMethodInvocation(javaAbstract, null);
                        javaInternalInvocation.setUnknowMethodName(unknowMethodName);
                        javaMethod.addMethodInvocation(javaInternalInvocation);
                    }else{
                        JavaMethod javaInvocatedMethod = javaAbstract.getMethodById(Long.valueOf(invocatedMethodString));
                        JavaMethodInvocation javaInternalInvocation = new JavaMethodInvocation(javaAbstract, javaInvocatedMethod);
                        javaMethod.addMethodInvocation(javaInternalInvocation);
                    }
                }
                
                
               
                i++;
            }
            //stm.execute("SHUTDOWN");
            //System.out.println("QUANTIDADE: " + i);
        } catch (Exception e) {
            System.out.println("ERRO getInvocatedMethods: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
