/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao.mysql;

import br.uff.ic.archd.db.dao.*;
import br.uff.ic.archd.javacode.JavaAbstract;
import br.uff.ic.archd.javacode.JavaAbstractExternal;
import br.uff.ic.archd.javacode.JavaClass;
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
public class MySQLExternalImportsDao implements ExternalImportsDao {

    private Connection connection;

    public MySQLExternalImportsDao() {

    }

    @Override
    public void save(JavaAbstract javaAbstract, String externalImport) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            Statement stm = connection.createStatement();
            if (javaAbstract.getClass() == JavaClass.class) {
                System.out.println("insert into EXTERNAL_IMPORTS (class_id , external_import) "
                        + " VALUES (" + javaAbstract.getId() + ",'" + externalImport + "');");
                stm.executeUpdate("insert into EXTERNAL_IMPORTS (class_id , external_import) "
                        + " VALUES (" + javaAbstract.getId() + ",'" + externalImport + "');");

            } else {
                System.out.println("insert into EXTERNAL_IMPORTS (interface_id , external_import) "
                        + " VALUES (" + javaAbstract.getId() + ",'" + externalImport + "');");
                stm.executeUpdate("insert into EXTERNAL_IMPORTS (interface_id , external_import) "
                        + " VALUES (" + javaAbstract.getId() + ",'" + externalImport + "');");

            }
            
            stm.close();

        } catch (SQLException e) {
            System.out.println("ERRO external imports: " + e.getMessage());
        }
    }

    public void save(JavaAbstract javaAbstract, List<String> externalImports) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            if (!externalImports.isEmpty()) {
                String query = "";
                if (javaAbstract.getClass() == JavaClass.class) {
                    query = "insert into EXTERNAL_IMPORTS (class_id , external_import) "
                            + " VALUES ";

                } else {
                    query = "insert into EXTERNAL_IMPORTS (interface_id , external_import) "
                            + " VALUES ";

                }
                
                String externalImport = externalImports.get(0);
                query = query + "(" + javaAbstract.getId() + ",'" + externalImport + "')";
                for (int i = 1; i < externalImports.size(); i++) {
                    externalImport = externalImports.get(i);
                    query = query + ", (" + javaAbstract.getId() + ",'" + externalImport + "')";

                }
                query = query + ";";
                System.out.println(query);
                PreparedStatement stm = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                stm.execute();
                
                stm.close();
            }
            

        } catch (SQLException e) {
            System.out.println("ERRO external imports: " + e.getMessage());
        }
    }

    @Override
    public void getExternalImports(JavaAbstract javaAbstract, JavaProject javaProject) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = null;
            //long t1 = System.currentTimeMillis();
            if (javaAbstract.getClass() == JavaClass.class) {
                rs = stm.executeQuery("select * from EXTERNAL_IMPORTS where class_id=" + javaAbstract.getId() + ";");
            } else {
                rs = stm.executeQuery("select * from EXTERNAL_IMPORTS where interface_id=" + javaAbstract.getId() + ";");
            }
            //long t2 = System.currentTimeMillis();
            //System.out.println("Pegar todos os imports externos de uma revisão (somente select) : "+(t2-t1)+"  milisegundos");

            int i = 0;

            while (rs.next()) {

                String externalImport = rs.getString("external_import");
                JavaAbstractExternal javaAbstractExternal = javaProject.getJavaExternalClassByName(externalImport);
                if (javaAbstractExternal == null) {
                    javaAbstractExternal = new JavaAbstractExternal(externalImport);
                    javaProject.addExternalClass(javaAbstractExternal);
                }
                javaAbstract.addExternalImport(externalImport);

                i++;
            }
            
            stm.close();
            rs.close();
            //stm.execute("SHUTDOWN");
            //System.out.println("QUANTIDADE: " + i);
        } catch (Exception e) {
            System.out.println("ERRO external imports: " + e.getMessage());
        }
    }

}
