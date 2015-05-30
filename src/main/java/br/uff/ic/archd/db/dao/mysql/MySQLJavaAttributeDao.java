/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao.mysql;

import br.uff.ic.archd.db.dao.*;
import br.uff.ic.archd.javacode.JavaAbstractExternal;
import br.uff.ic.archd.javacode.JavaAttribute;
import br.uff.ic.archd.javacode.JavaClass;
import br.uff.ic.archd.javacode.JavaData;
import br.uff.ic.archd.javacode.JavaPrimitiveType;
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
public class MySQLJavaAttributeDao implements JavaAttributeDao {

    private Connection connection;

    public MySQLJavaAttributeDao() {
        
    }

    @Override
    public void save(JavaAttribute javaAttribute, long javaClassId) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            Statement stm = connection.createStatement();
            System.out.println("insert into JAVA_ATTRIBUTES (class_id ,"
                    + "is_final ,"
                    + "is_static ,"
                    + "is_volatile ,"
                    + "is_private ,"
                    + "is_public ,"
                    + "is_protected ,"
                    + "java_data_type ,"
                    + "name ) "
                    + " VALUES (" + javaClassId + ","
                    + javaAttribute.isFinal() + ","
                    + javaAttribute.isStatic() + ","
                    + javaAttribute.isVolatile() + ","
                    + javaAttribute.isPrivate() + ","
                    + javaAttribute.isPublic() + ","
                    + javaAttribute.isProtected() + ",'"
                    + javaAttribute.getType().getFullQualifiedName() + "','"
                    + javaAttribute.getName() + "'"
                    + ");");
            stm.executeUpdate("insert into JAVA_ATTRIBUTES (class_id ,"
                    + "is_final ,"
                    + "is_static ,"
                    + "is_volatile ,"
                    + "is_private ,"
                    + "is_public ,"
                    + "is_protected ,"
                    + "java_data_type ,"
                    + "name ) "
                    + " VALUES (" + javaClassId + ","
                    + javaAttribute.isFinal() + ","
                    + javaAttribute.isStatic() + ","
                    + javaAttribute.isVolatile() + ","
                    + javaAttribute.isPrivate() + ","
                    + javaAttribute.isPublic() + ","
                    + javaAttribute.isProtected() + ",'"
                    + javaAttribute.getType().getFullQualifiedName() + "','"
                    + javaAttribute.getName() + "'"
                    + ");");
        } catch (SQLException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }
    
    public void save(List<JavaAttribute> javaAttributes) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            if(!javaAttributes.isEmpty()){
                String query = "insert into JAVA_ATTRIBUTES (class_id ,"
                    + "is_final ,"
                    + "is_static ,"
                    + "is_volatile ,"
                    + "is_private ,"
                    + "is_public ,"
                    + "is_protected ,"
                    + "java_data_type ,"
                    + "name ) "
                    + " VALUES ";
                JavaAttribute javaAttribute = javaAttributes.get(0);
                query = query +"(" + javaAttribute.getJavaClassId() + ","
                    + javaAttribute.isFinal() + ","
                    + javaAttribute.isStatic() + ","
                    + javaAttribute.isVolatile() + ","
                    + javaAttribute.isPrivate() + ","
                    + javaAttribute.isPublic() + ","
                    + javaAttribute.isProtected() + ",'"
                    + javaAttribute.getType().getFullQualifiedName() + "','"
                    + javaAttribute.getName() + "'"
                    + ")";
                for(int i = 1; i < javaAttributes.size(); i++){
                    javaAttribute = javaAttributes.get(i);
                    query = query +", (" + javaAttribute.getJavaClassId() + ","
                    + javaAttribute.isFinal() + ","
                    + javaAttribute.isStatic() + ","
                    + javaAttribute.isVolatile() + ","
                    + javaAttribute.isPrivate() + ","
                    + javaAttribute.isPublic() + ","
                    + javaAttribute.isProtected() + ",'"
                    + javaAttribute.getType().getFullQualifiedName() + "','"
                    + javaAttribute.getName() + "'"
                    + ")";
                    
                }
                query = query + ";";
                System.out.println(query);
                PreparedStatement stm = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                stm.execute();
            }
            
        } catch (SQLException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    @Override
    public void getJavaAttributesFromClass(JavaClass javaClass, JavaProject javaProject) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            Statement stm = connection.createStatement();
            
            //long t1 = System.currentTimeMillis();
            ResultSet rs = stm.executeQuery("select * from JAVA_ATTRIBUTES where class_id=" + javaClass.getId() + ";");
            //long t2 = System.currentTimeMillis();
            //System.out.println("Pegar todas os atributos de uma classe de uma revisão (somente o select) : "+(t2-t1)+"  milisegundos");
            
            int i = 0;
            while (rs.next()) {
                JavaData javaData = javaProject.getClassByName(rs.getString("java_data_type"));
                if (javaData == null) {
                    if (JavaPrimitiveType.getType(rs.getString("java_data_type")) != 0) {
                        javaData = new JavaPrimitiveType(JavaPrimitiveType.getType(rs.getString("java_data_type")));
                    } else {
                        javaData = javaProject.getJavaExternalClassByName(rs.getString("java_data_type"));
                        if (javaData == null) {
                            javaData = new JavaAbstractExternal(rs.getString("java_data_type"));
                            javaProject.addExternalClass((JavaAbstractExternal) javaData);
                        }
                    }
                }
                JavaAttribute javaAttribute = new JavaAttribute(javaData, rs.getString("name"),
                        Boolean.valueOf(rs.getString("is_final")),
                        Boolean.valueOf(rs.getString("is_static")), Boolean.valueOf(rs.getString("is_volatile")),
                        Boolean.valueOf(rs.getString("is_private")),
                        Boolean.valueOf(rs.getString("is_public")), Boolean.valueOf(rs.getString("is_protected")));

                javaClass.addAttribute(javaAttribute);
                i++;
            }
            //stm.execute("SHUTDOWN");
            //System.out.println("QUANTIDADE: " + i);
        } catch (Exception e) {
            System.out.println("ERRO attribute: " + e.getMessage());
        }
    }
}
