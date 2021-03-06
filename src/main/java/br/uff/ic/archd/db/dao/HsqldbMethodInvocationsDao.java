/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao;

import br.uff.ic.archd.javacode.JavaAbstract;
import br.uff.ic.archd.javacode.JavaClass;
import br.uff.ic.archd.javacode.JavaMethod;
import br.uff.ic.archd.javacode.JavaMethodInvocation;
import br.uff.ic.archd.javacode.JavaProject;
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
public class HsqldbMethodInvocationsDao implements MethodInvocationsDao {

    private Connection connection;

    HsqldbMethodInvocationsDao() {
        try {
            File file = new File(Constants.DB_DIR);
            if (!file.exists()) {
                file.mkdirs();

            }
            Class.forName("org.hsqldb.jdbcDriver");
            connection = DriverManager.getConnection("jdbc:hsqldb:file:" + Constants.DB_DIR, "archd", "123");
            DatabaseMetaData dbData = connection.getMetaData();
            ResultSet tables = dbData.getTables(null, null, "METHOD_INVOCATIONS", null);
            //System.out.println("NEXT "+tables.next());
            if (!tables.next()) {

                System.out.println("NAO POSSUI TABELA METHOD_INVOCATIONS");
                Statement stm = connection.createStatement();

                stm.executeUpdate("create table METHOD_INVOCATIONS (id bigint GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1), "
                        + "method_id bigint,"
                        + "class_id bigint,"
                        + "interface_id bigint,"
                        + "unknow_method_name varchar(1000),"
                        + "invocated_method bigint,"
                        + "PRIMARY KEY (id),"
                        + "FOREIGN KEY (method_id) REFERENCES JAVA_METHODS(id),"
                        + "FOREIGN KEY (class_id) REFERENCES JAVA_CLASSES(id),"
                        + "FOREIGN KEY (interface_id) REFERENCES JAVA_INTERFACES(id));");
            } else {
                System.out.println("TABELA JAH EXISTE METHOD_INVOCATIONS");
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
    public void saveMethodInvocations(JavaMethod javaMethod, JavaClass javaClass) {
        try {
            Statement stm = connection.createStatement();
            for (JavaMethodInvocation javaMethodInvocation : javaMethod.getMethodInvocations()) {
                JavaAbstract javaAbstract = javaMethodInvocation.getJavaAbstract();
                if (javaAbstract.getClass() == JavaClass.class) {
                    if (javaMethodInvocation.getUnknowMethodName() != null) {
                        System.out.println("insert into METHOD_INVOCATIONS (method_id, class_id , unknow_method_name) "
                                + " VALUES (" + javaMethod.getId() + "," + javaAbstract.getId() + ",'" + javaMethodInvocation.getUnknowMethodName() + "');");
                        stm.executeUpdate("insert into METHOD_INVOCATIONS (method_id, class_id , unknow_method_name) "
                                + " VALUES (" + javaMethod.getId() + "," + javaAbstract.getId() + ",'" + javaMethodInvocation.getUnknowMethodName() + "');");
                    } else {
                        System.out.println("insert into METHOD_INVOCATIONS (method_id, class_id , invocated_method) "
                                + " VALUES (" + javaMethod.getId() + "," + javaAbstract.getId() + "," + javaMethodInvocation.getJavaMethod().getId() + ");");
                        stm.executeUpdate("insert into METHOD_INVOCATIONS (method_id, class_id , invocated_method) "
                                + " VALUES (" + javaMethod.getId() + "," + javaAbstract.getId() + "," + javaMethodInvocation.getJavaMethod().getId() + ");");
                    }
                } else {
                    if (javaMethodInvocation.getUnknowMethodName() != null) {
                        System.out.println("insert into METHOD_INVOCATIONS (method_id, interface_id , unknow_method_name) "
                                + " VALUES (" + javaMethod.getId() + "," + javaAbstract.getId() + ",'" + javaMethodInvocation.getUnknowMethodName() + "');");
                        stm.executeUpdate("insert into METHOD_INVOCATIONS (method_id, interface_id , unknow_method_name) "
                                + " VALUES (" + javaMethod.getId() + "," + javaAbstract.getId() + ",'" + javaMethodInvocation.getUnknowMethodName() + "');");
                    } else {
                        System.out.println("insert into METHOD_INVOCATIONS (method_id, interface_id , invocated_method) "
                                + " VALUES (" + javaMethod.getId() + "," + javaAbstract.getId() + "," + javaMethodInvocation.getJavaMethod().getId() + ");");
                        stm.executeUpdate("insert into METHOD_INVOCATIONS (method_id, interface_id , invocated_method) "
                                + " VALUES (" + javaMethod.getId() + "," + javaAbstract.getId() + "," + javaMethodInvocation.getJavaMethod().getId() + ");");
                    }
                }
            }
//            for (JavaMethod invocatedMethod : javaMethod.getInternalMethodInvocations()) {
//                System.out.println("insert into METHOD_INVOCATIONS (method_id, class_id , invocated_method) "
//                        + " VALUES (" + javaMethod.getId() + "," + javaClass.getId() + "," + invocatedMethod.getId() + ");");
//                stm.executeUpdate("insert into METHOD_INVOCATIONS (method_id, class_id , invocated_method) "
//                        + " VALUES (" + javaMethod.getId() + "," + javaClass.getId() + "," + invocatedMethod.getId() + ");");
//            }

        } catch (SQLException e) {
            System.out.println("ERRO save methodinvocation: " + e.getMessage());
        }
    }

    @Override
    public void getInvocatedMethods(JavaMethod javaMethod, JavaClass javaClass, JavaProject javaProject) {
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
                if (classId != null) {
                    javaAbstract = javaProject.getClassById(Long.valueOf(classId));
                } else {
                    javaAbstract = javaProject.getInterfaceById(Long.valueOf(interfaceId));
                }
                if (javaAbstract == javaClass) {

                    /*System.out.println("Invocated Method: "+invocatedMethodString);
                     System.out.println("Class ID: "+javaAbstract.getId());
                     System.out.println("Class ID MY: "+javaClass.getId());
                     System.out.println("unknow_method_name: "+unknowMethodName);
                     System.out.println("JAVA METHOD: "+javaMethod.getMethodSignature());
                     System.out.println("JAVA CLASS: "+javaClass.getPath());*/
                    if (unknowMethodName != null) {
                        JavaMethodInvocation javaInternalInvocation = new JavaMethodInvocation(javaAbstract, null);
                        javaInternalInvocation.setUnknowMethodName(unknowMethodName);
                        javaMethod.addMethodInvocation(javaInternalInvocation);
                    } else {
                        //javaMethod.addInternalMethodInvocation(javaClass.getMethodById(Long.valueOf(invocatedMethodString)));
                    }
                } else {
                    if (unknowMethodName != null) {
                        JavaMethodInvocation javaInternalInvocation = new JavaMethodInvocation(javaAbstract, null);
                        javaInternalInvocation.setUnknowMethodName(unknowMethodName);
                        javaMethod.addMethodInvocation(javaInternalInvocation);
                    } else {
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

    @Override
    public void saveMethodInvocations(List<JavaMethod> methods) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
