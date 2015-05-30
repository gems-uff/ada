/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao.mysql;

import br.uff.ic.archd.db.dao.*;
import br.uff.ic.archd.javacode.JavaClass;
import br.uff.ic.archd.javacode.JavaInterface;
import br.uff.ic.archd.javacode.JavaProject;
import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author wallace
 */
public class MySQLImplementedInterfacesDao implements ImplementedInterfacesDao{

    private Connection connection;
    
    public MySQLImplementedInterfacesDao(){
        
    }
    
    @Override
    public void setImplementedInterfacesDao(JavaClass javaClass, JavaProject javaProject) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            Statement stm = connection.createStatement();
            
            //long t1 = System.currentTimeMillis();
            ResultSet rs = stm.executeQuery("select * from IMPLEMENTED_INTERFACES where class_id=" + javaClass.getId() + ";");
            //long t2 = System.currentTimeMillis();
            //System.out.println("Pegar todas as implemented interfaces de uma classe de uma revisão (somente o select) : "+(t2-t1)+"  milisegundos");
            
            int i = 0;

            while (rs.next()) {
                
                String interfaceId = rs.getString("interface_id");
                JavaInterface javaInterface = javaProject.getInterfaceById(Long.valueOf(interfaceId));
                javaClass.addImplementedInterface(javaInterface);
                
                
                i++;
            }
            //stm.execute("SHUTDOWN");
            //System.out.println("QUANTIDADE: " + i);
        } catch (Exception e) {
            System.out.println("ERRO implemented interface: " + e.getMessage());
        }
    }

    @Override
    public void saveImplementedInterface(JavaClass javaClass, JavaInterface javaInterface) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            Statement stm = connection.createStatement();
            System.out.println("insert into IMPLEMENTED_INTERFACES (class_id, interface_id) "
                    + " VALUES (" + javaClass.getId() + ","
                    + "" + javaInterface.getId() + ");");
            stm.executeUpdate("insert into IMPLEMENTED_INTERFACES (class_id, interface_id) "
                    + " VALUES (" + javaClass.getId() + ","
                    + "" + javaInterface.getId() + ");");

        } catch (SQLException e) {
            System.out.println("ERRO implemented interfaces: " + e.getMessage());
        }
    }
    
    
}
