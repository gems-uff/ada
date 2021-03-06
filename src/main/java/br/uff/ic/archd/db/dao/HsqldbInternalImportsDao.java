/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao;

import br.uff.ic.archd.javacode.JavaAbstract;
import br.uff.ic.archd.javacode.JavaClass;
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
public class HsqldbInternalImportsDao implements InternalImportsDao {

    private Connection connection;

    HsqldbInternalImportsDao() {
        try {
            File file = new File(Constants.DB_DIR);
            if(!file.exists()){
                file.mkdirs();
                
            }
            Class.forName("org.hsqldb.jdbcDriver");
            connection = DriverManager.getConnection("jdbc:hsqldb:file:" + Constants.DB_DIR, "archd", "123");
            DatabaseMetaData dbData = connection.getMetaData();
            ResultSet tables = dbData.getTables(null, null, "INTERNAL_IMPORTS", null);
            //System.out.println("NEXT "+tables.next());
            if (!tables.next()) {


                System.out.println("NAO POSSUI TABELA INTERNAL_IMPORTS");
                Statement stm = connection.createStatement();

                stm.executeUpdate("create table INTERNAL_IMPORTS (id bigint GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1), "
                        + "class_id bigint,"
                        + "interface_id bigint,"
                        + "class_import_id bigint,"
                        + "interface_import_id bigint,"
                        + "PRIMARY KEY (id),"
                        + "FOREIGN KEY (class_id) REFERENCES JAVA_CLASSES(id),"
                        + "FOREIGN KEY (interface_id) REFERENCES JAVA_INTERFACES(id),"
                        + "FOREIGN KEY (class_import_id) REFERENCES JAVA_CLASSES(id),"
                        + "FOREIGN KEY (interface_import_id) REFERENCES JAVA_INTERFACES(id));");
            } else {
                System.out.println("TABELA JAH EXISTE INTERNAL_IMPORTS");
            }

            //stm.execute("SHUTDOWN");

        } catch (ClassNotFoundException e) {
            System.out.println("Erro ao carregar o driver JDBC. ");
        } catch (SQLException e) {
            System.out.println("Erro de SQL: " + e);
            //e.printStackTrace();
        } catch (Exception e) {
            System.out.println("ERRO internal imports" + e.getMessage());
        }
    }

    @Override
    public void saveInternalImport(JavaAbstract javaAbstract, JavaAbstract javaAbstractImport) {
        try {
            Statement stm = connection.createStatement();
            if (javaAbstract.getClass() == JavaClass.class) {
                if (javaAbstractImport.getClass() == JavaClass.class) {
                    System.out.println("insert into INTERNAL_IMPORTS (class_id , class_import_id) "
                            + " VALUES (" + javaAbstract.getId() + ","+javaAbstractImport.getId()+");");
                    stm.executeUpdate("insert into INTERNAL_IMPORTS (class_id , class_import_id) "
                            + " VALUES (" + javaAbstract.getId() + ","+javaAbstractImport.getId()+");");
                } else {
                    System.out.println("insert into INTERNAL_IMPORTS (class_id , interface_import_id) "
                            + " VALUES (" + javaAbstract.getId() + ","+javaAbstractImport.getId()+");");
                    stm.executeUpdate("insert into INTERNAL_IMPORTS (class_id , interface_import_id) "
                            + " VALUES (" + javaAbstract.getId() + ","+javaAbstractImport.getId()+");");
                }
            } else {
                if (javaAbstractImport.getClass() == JavaClass.class) {
                    System.out.println("insert into INTERNAL_IMPORTS (interface_id , class_import_id) "
                            + " VALUES (" + javaAbstract.getId() + ","+javaAbstractImport.getId()+");");
                    stm.executeUpdate("insert into INTERNAL_IMPORTS (interface_id , class_import_id) "
                            + " VALUES (" + javaAbstract.getId() + ","+javaAbstractImport.getId()+");");
                } else {
                    System.out.println("insert into INTERNAL_IMPORTS (interface_id , interface_import_id) "
                            + " VALUES (" + javaAbstract.getId() + ","+javaAbstractImport.getId()+");");
                    stm.executeUpdate("insert into INTERNAL_IMPORTS (interface_id , interface_import_id) "
                            + " VALUES (" + javaAbstract.getId() + ","+javaAbstractImport.getId()+");");
                }
            }

        } catch (SQLException e) {
            System.out.println("ERRO internal imports: " + e.getMessage());
        }
    }

    @Override
    public void getInternalImports(JavaAbstract javaAbstract, JavaProject javaProject) {
        try {
            List<JavaAbstract> importClasses = new LinkedList();
            Statement stm = connection.createStatement();
            ResultSet rs = null;
            //long t1 = System.currentTimeMillis();
            if(javaAbstract.getClass() == JavaClass.class){
                rs = stm.executeQuery("select * from INTERNAL_IMPORTS where class_id=" + javaAbstract.getId() + ";");
            }else{
                rs = stm.executeQuery("select * from INTERNAL_IMPORTS where interface_id=" + javaAbstract.getId() + ";");
            }
            //long t2 = System.currentTimeMillis();
            //System.out.println("Pegar todos os imports internos de uma revisão (somente select) : "+(t2-t1)+"  milisegundos");
            
            int i = 0;

            while (rs.next()) {
                
                String classId = rs.getString("class_import_id");
                String interfaceId = rs.getString("interface_import_id");
                if(classId != null){
                    importClasses.add(javaProject.getClassById(Long.valueOf(classId)));
                }else{
                    importClasses.add(javaProject.getInterfaceById(Long.valueOf(interfaceId)));
                }
               
                i++;
            }
            javaAbstract.addImportClasses(importClasses);
            //stm.execute("SHUTDOWN");
            //System.out.println("QUANTIDADE: " + i);
        } catch (Exception e) {
            System.out.println("ERRO internal imports: " + e.getMessage());
        }
    }

    @Override
    public void saveInternalImport(JavaAbstract javaAbstract) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void saveInternalImport(List<JavaAbstract> javaAbstracts) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
