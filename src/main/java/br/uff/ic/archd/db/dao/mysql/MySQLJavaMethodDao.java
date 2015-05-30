/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao.mysql;

import br.uff.ic.archd.db.dao.*;
import br.uff.ic.archd.javacode.JavaAbstract;
import br.uff.ic.archd.javacode.JavaAbstractExternal;
import br.uff.ic.archd.javacode.JavaClass;
import br.uff.ic.archd.javacode.JavaData;
import br.uff.ic.archd.javacode.JavaInterface;
import br.uff.ic.archd.javacode.JavaMethod;
import br.uff.ic.archd.javacode.JavaPrimitiveType;
import br.uff.ic.archd.javacode.JavaProject;
import br.uff.ic.archd.javacode.Parameter;
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
public class MySQLJavaMethodDao implements JavaMethodDao {

    private Connection connection;

    public MySQLJavaMethodDao() {

    }

    @Override
    public void save(JavaMethod javaMethod, boolean fromClass, long itemId) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            Statement stm = connection.createStatement();
            if (fromClass) {
                System.out.println("insert into JAVA_METHODS (name ,"
                        + "original_signature ,"
                        + "return_type ,"
                        + "parameters ,"
                        + "cyclomatic_complexity ,"
                        + "size_in_chars ,"
                        + "number_of_lines,"
                        + "access_to_foreign_data,"
                        + "access_to_local_data,"
                        + "foreign_data_provider,"
                        + "number_of_local_variables,"
                        + "is_final ,"
                        + "is_static ,"
                        + "is_abstract ,"
                        + "is_synchronized ,"
                        + "is_private ,"
                        + "is_public ,"
                        + "is_protected ,"
                        + "change_internal_state ,"
                        + "change_internal_state_by_method_invocation ,"
                        + "is_an_acessor_method,"
                        + "accessed_attribute,"
                        + "internal_id ,"
                        + "class_id )"
                        + " VALUES ('" + javaMethod.getName() + "','"
                        + javaMethod.getOriginalSignature() + "','"
                        + javaMethod.getReturnType().getName() + "','"
                        + javaMethod.getParametersSignature() + "',"
                        + javaMethod.getCyclomaticComplexity() + ","
                        + javaMethod.getSizeInChars() + ","
                        + javaMethod.getNumberOfLines() + ","
                        + javaMethod.getAccessToForeignDataNumber() + ","
                        + javaMethod.getAccessToLocalDataNumber() + ","
                        + javaMethod.getForeignDataProviderNumber() + ","
                        + javaMethod.getNumberOfLocalVariables() + ","
                        + javaMethod.isFinal() + ","
                        + javaMethod.isStatic() + ","
                        + javaMethod.isAbstract() + ","
                        + javaMethod.isSynchronized() + ","
                        + javaMethod.isPrivate() + ","
                        + javaMethod.isPublic() + ","
                        + javaMethod.isProtected() + ","
                        + javaMethod.isChangeInternalState() + ","
                        + javaMethod.isChangeInternalStateByMethodInvocations() + ","
                        + javaMethod.isAnAcessorMethod() + ",'"
                        + javaMethod.getAccessedAttribute() + "',"
                        + javaMethod.getInternalID() + ","
                        + itemId + ""
                        + ");");
                stm.executeUpdate("insert into JAVA_METHODS (name ,"
                        + "original_signature ,"
                        + "return_type ,"
                        + "parameters ,"
                        + "cyclomatic_complexity ,"
                        + "size_in_chars ,"
                        + "number_of_lines,"
                        + "access_to_foreign_data,"
                        + "access_to_local_data,"
                        + "foreign_data_provider,"
                        + "number_of_local_variables,"
                        + "is_final ,"
                        + "is_static ,"
                        + "is_abstract ,"
                        + "is_synchronized ,"
                        + "is_private ,"
                        + "is_public ,"
                        + "is_protected ,"
                        + "change_internal_state ,"
                        + "change_internal_state_by_method_invocation ,"
                        + "is_an_acessor_method,"
                        + "accessed_attribute,"
                        + "internal_id ,"
                        + "class_id )"
                        + " VALUES ('" + javaMethod.getName() + "','"
                        + javaMethod.getOriginalSignature() + "','"
                        + javaMethod.getReturnType().getName() + "','"
                        + javaMethod.getParametersSignature() + "',"
                        + javaMethod.getCyclomaticComplexity() + ","
                        + javaMethod.getSizeInChars() + ","
                        + javaMethod.getNumberOfLines() + ","
                        + javaMethod.getAccessToForeignDataNumber() + ","
                        + javaMethod.getAccessToLocalDataNumber() + ","
                        + javaMethod.getForeignDataProviderNumber() + ","
                        + javaMethod.getNumberOfLocalVariables() + ","
                        + javaMethod.isFinal() + ","
                        + javaMethod.isStatic() + ","
                        + javaMethod.isAbstract() + ","
                        + javaMethod.isSynchronized() + ","
                        + javaMethod.isPrivate() + ","
                        + javaMethod.isPublic() + ","
                        + javaMethod.isProtected() + ","
                        + javaMethod.isChangeInternalState() + ","
                        + javaMethod.isChangeInternalStateByMethodInvocations() + ","
                        + javaMethod.isAnAcessorMethod() + ",'"
                        + javaMethod.getAccessedAttribute() + "',"
                        + javaMethod.getInternalID() + ","
                        + itemId + ""
                        + ");");
                //stm.execute("SHUTDOWN");
            } else {
                System.out.println("insert into JAVA_METHODS (name ,"
                        + "original_signature ,"
                        + "return_type ,"
                        + "parameters ,"
                        + "cyclomatic_complexity ,"
                        + "size_in_chars ,"
                        + "number_of_lines,"
                        + "access_to_foreign_data,"
                        + "access_to_local_data,"
                        + "foreign_data_provider,"
                        + "number_of_local_variables,"
                        + "is_final ,"
                        + "is_static ,"
                        + "is_abstract ,"
                        + "is_synchronized ,"
                        + "is_private ,"
                        + "is_public ,"
                        + "is_protected ,"
                        + "change_internal_state ,"
                        + "change_internal_state_by_method_invocation ,"
                        + "is_an_acessor_method,"
                        + "accessed_attribute,"
                        + "internal_id ,"
                        + "interface_id )"
                        + " VALUES ('" + javaMethod.getName() + "','"
                        + javaMethod.getOriginalSignature() + "','"
                        + javaMethod.getReturnType().getName() + "','"
                        + javaMethod.getParametersSignature() + "',"
                        + javaMethod.getCyclomaticComplexity() + ","
                        + javaMethod.getSizeInChars() + ","
                        + javaMethod.getNumberOfLines() + ","
                        + javaMethod.getAccessToForeignDataNumber() + ","
                        + javaMethod.getAccessToLocalDataNumber() + ","
                        + javaMethod.getForeignDataProviderNumber() + ","
                        + javaMethod.getNumberOfLocalVariables() + ","
                        + javaMethod.isFinal() + ","
                        + javaMethod.isStatic() + ","
                        + javaMethod.isAbstract() + ","
                        + javaMethod.isSynchronized() + ","
                        + javaMethod.isPrivate() + ","
                        + javaMethod.isPublic() + ","
                        + javaMethod.isProtected() + ","
                        + javaMethod.isChangeInternalState() + ","
                        + javaMethod.isChangeInternalStateByMethodInvocations() + ","
                        + javaMethod.isAnAcessorMethod() + ",'"
                        + javaMethod.getAccessedAttribute() + "',"
                        + javaMethod.getInternalID() + ","
                        + itemId + ""
                        + ");");
                stm.executeUpdate("insert into JAVA_METHODS (name ,"
                        + "original_signature ,"
                        + "return_type ,"
                        + "parameters ,"
                        + "cyclomatic_complexity ,"
                        + "size_in_chars ,"
                        + "number_of_lines,"
                        + "access_to_foreign_data,"
                        + "access_to_local_data,"
                        + "foreign_data_provider,"
                        + "number_of_local_variables,"
                        + "is_final ,"
                        + "is_static ,"
                        + "is_abstract ,"
                        + "is_synchronized ,"
                        + "is_private ,"
                        + "is_public ,"
                        + "is_protected ,"
                        + "change_internal_state ,"
                        + "change_internal_state_by_method_invocation ,"
                        + "is_an_acessor_method,"
                        + "accessed_attribute,"
                        + "internal_id ,"
                        + "interface_id )"
                        + " VALUES ('" + javaMethod.getName() + "','"
                        + javaMethod.getOriginalSignature() + "','"
                        + javaMethod.getReturnType().getName() + "','"
                        + javaMethod.getParametersSignature() + "',"
                        + javaMethod.getCyclomaticComplexity() + ","
                        + javaMethod.getSizeInChars() + ","
                        + javaMethod.getNumberOfLines() + ","
                        + javaMethod.getAccessToForeignDataNumber() + ","
                        + javaMethod.getAccessToLocalDataNumber() + ","
                        + javaMethod.getForeignDataProviderNumber() + ","
                        + javaMethod.getNumberOfLocalVariables() + ","
                        + javaMethod.isFinal() + ","
                        + javaMethod.isStatic() + ","
                        + javaMethod.isAbstract() + ","
                        + javaMethod.isSynchronized() + ","
                        + javaMethod.isPrivate() + ","
                        + javaMethod.isPublic() + ","
                        + javaMethod.isProtected() + ","
                        + javaMethod.isChangeInternalState() + ","
                        + javaMethod.isChangeInternalStateByMethodInvocations() + ","
                        + javaMethod.isAnAcessorMethod() + ",'"
                        + javaMethod.getAccessedAttribute() + "',"
                        + javaMethod.getInternalID() + ","
                        + itemId + ""
                        + ");");
            }
            ResultSet rs = stm.getGeneratedKeys();
            long id = 0;
            if (rs.next()) {
                id = rs.getLong(1);
            }
            javaMethod.setId(id);
            //System.out.println("Method ID: " + javaMethod.getId());
        } catch (SQLException e) {
            System.out.println("ERRO method: " + e.getMessage());
        }
    }

    public void save(List<JavaMethod> javaMethods) {
        connection = MysqlConnectionFactory.getConnection();
        try {

            if (!javaMethods.isEmpty()) {
                JavaMethod javaMethod = javaMethods.get(0);
                String query = "";
                if (javaMethod.isFromClass()) {
                    query = "insert into JAVA_METHODS (name ,"
                            + "original_signature ,"
                            + "return_type ,"
                            + "parameters ,"
                            + "cyclomatic_complexity ,"
                            + "size_in_chars ,"
                            + "number_of_lines,"
                            + "access_to_foreign_data,"
                            + "access_to_local_data,"
                            + "foreign_data_provider,"
                            + "number_of_local_variables,"
                            + "is_final ,"
                            + "is_static ,"
                            + "is_abstract ,"
                            + "is_synchronized ,"
                            + "is_private ,"
                            + "is_public ,"
                            + "is_protected ,"
                            + "change_internal_state ,"
                            + "change_internal_state_by_method_invocation ,"
                            + "is_an_acessor_method,"
                            + "accessed_attribute,"
                            + "internal_id ,"
                            + "class_id ) ";
                } else {
                    query = "insert into JAVA_METHODS (name ,"
                            + "original_signature ,"
                            + "return_type ,"
                            + "parameters ,"
                            + "cyclomatic_complexity ,"
                            + "size_in_chars ,"
                            + "number_of_lines,"
                            + "access_to_foreign_data,"
                            + "access_to_local_data,"
                            + "foreign_data_provider,"
                            + "number_of_local_variables,"
                            + "is_final ,"
                            + "is_static ,"
                            + "is_abstract ,"
                            + "is_synchronized ,"
                            + "is_private ,"
                            + "is_public ,"
                            + "is_protected ,"
                            + "change_internal_state ,"
                            + "change_internal_state_by_method_invocation ,"
                            + "is_an_acessor_method,"
                            + "accessed_attribute,"
                            + "internal_id ,"
                            + "interface_id )";
                }
                query = query + " VALUES ('" + javaMethod.getName() + "','"
                        + javaMethod.getOriginalSignature() + "','"
                        + javaMethod.getReturnType().getName() + "','"
                        + javaMethod.getParametersSignature() + "',"
                        + javaMethod.getCyclomaticComplexity() + ","
                        + javaMethod.getSizeInChars() + ","
                        + javaMethod.getNumberOfLines() + ","
                        + javaMethod.getAccessToForeignDataNumber() + ","
                        + javaMethod.getAccessToLocalDataNumber() + ","
                        + javaMethod.getForeignDataProviderNumber() + ","
                        + javaMethod.getNumberOfLocalVariables() + ","
                        + javaMethod.isFinal() + ","
                        + javaMethod.isStatic() + ","
                        + javaMethod.isAbstract() + ","
                        + javaMethod.isSynchronized() + ","
                        + javaMethod.isPrivate() + ","
                        + javaMethod.isPublic() + ","
                        + javaMethod.isProtected() + ","
                        + javaMethod.isChangeInternalState() + ","
                        + javaMethod.isChangeInternalStateByMethodInvocations() + ","
                        + javaMethod.isAnAcessorMethod() + ",'"
                        + javaMethod.getAccessedAttribute() + "',"
                        + javaMethod.getInternalID() + ","
                        + javaMethod.getItemId() + ""
                        + ")";

                for (int i = 1; i < javaMethods.size(); i++) {
                    javaMethod = javaMethods.get(i);
                    query = query + ", ('" + javaMethod.getName() + "','"
                            + javaMethod.getOriginalSignature() + "','"
                            + javaMethod.getReturnType().getName() + "','"
                            + javaMethod.getParametersSignature() + "',"
                            + javaMethod.getCyclomaticComplexity() + ","
                            + javaMethod.getSizeInChars() + ","
                            + javaMethod.getNumberOfLines() + ","
                            + javaMethod.getAccessToForeignDataNumber() + ","
                            + javaMethod.getAccessToLocalDataNumber() + ","
                            + javaMethod.getForeignDataProviderNumber() + ","
                            + javaMethod.getNumberOfLocalVariables() + ","
                            + javaMethod.isFinal() + ","
                            + javaMethod.isStatic() + ","
                            + javaMethod.isAbstract() + ","
                            + javaMethod.isSynchronized() + ","
                            + javaMethod.isPrivate() + ","
                            + javaMethod.isPublic() + ","
                            + javaMethod.isProtected() + ","
                            + javaMethod.isChangeInternalState() + ","
                            + javaMethod.isChangeInternalStateByMethodInvocations() + ","
                            + javaMethod.isAnAcessorMethod() + ",'"
                            + javaMethod.getAccessedAttribute() + "',"
                            + javaMethod.getInternalID() + ","
                            + javaMethod.getItemId() + ""
                            + ")";
                }
                query = query + ";";
                System.out.println(query);
                //System.out.println(query);
                PreparedStatement stm = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                stm.execute();
                ResultSet rs = stm.getGeneratedKeys();
                int i = 0;
                long id = 0;
                while (rs.next()) {
                    id = rs.getLong(1);
                    javaMethod = javaMethods.get(i);
                    javaMethod.setId(id);
                    //System.out.println("Classe ID i: "+i+": " + javaClass.getId());
                    i++;
                }
                System.out.println("METHOD QUERY SIZE: "+query.length()+"    Methods: "+javaMethods.size()+"    TAXA: "+(query.length()/javaMethods.size()));
                //javaMethod.setId(id);

            }

            //System.out.println("Method ID: " + javaMethod.getId());
        } catch (SQLException e) {
            System.out.println("ERRO method: " + e.getMessage());
        }
    }

    @Override
    public List<JavaMethod> getAllJavaMethod(JavaProject javaProject) {
        connection = MysqlConnectionFactory.getConnection();
        List<JavaMethod> javaMethods = new LinkedList();
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("select * from JAVA_METHODS ;");
            int i = 0;
            while (rs.next()) {
                JavaData javaData = JavaDataHash.getInstance().getJavaData(rs.getString("return_type"));
                JavaMethod javaMethod = new JavaMethod(rs.getString("name"), rs.getString("original_signature"), javaData,
                        Boolean.valueOf(rs.getString("is_final")),
                        Boolean.valueOf(rs.getString("is_static")), Boolean.valueOf(rs.getString("is_abstract")),
                        Boolean.valueOf(rs.getString("is_synchronized")), Boolean.valueOf(rs.getString("is_private")),
                        Boolean.valueOf(rs.getString("is_public")), Boolean.valueOf(rs.getString("is_protected")),
                        Integer.valueOf(rs.getString("cyclomatic_complexity")), null);
                javaMethod.setId(Long.parseLong(rs.getString("id")));
                String parametersSignature = rs.getString("parameters");
                if (!parametersSignature.equals("")) {
                    String paramsArray[] = parametersSignature.split(";");
                    for (int j = 0; j < paramsArray.length; j++) {
                        String param[] = paramsArray[j].split(":");
                        JavaAbstract javaAbstract = javaProject.getClassByName(param[0]);
                        if (javaAbstract == null) {
                            JavaAbstractExternal javaAbstractExternal = javaProject.getJavaExternalClassByName(param[0]);
                            if (javaAbstractExternal == null) {
                                JavaPrimitiveType javaPrimitiveType = new JavaPrimitiveType(JavaPrimitiveType.getType(param[0]));
                                Parameter parameter = new Parameter(javaPrimitiveType, param[1]);
                                javaMethod.addParameter(parameter);
                            } else {
                                Parameter parameter = new Parameter(javaAbstractExternal, param[1]);
                                javaMethod.addParameter(parameter);
                            }
                        } else {
                            Parameter parameter = new Parameter(javaAbstract, param[1]);
                            javaMethod.addParameter(parameter);
                        }
                    }
                }
                javaMethods.add(javaMethod);
                i++;
            }
            //stm.execute("SHUTDOWN");
            //System.out.println("QUANTIDADE: " + i);
        } catch (Exception e) {
            System.out.println("ERRO method: " + e.getMessage());
        }
        return javaMethods;
    }

    @Override
    public List<JavaMethod> getJavaMethodsByInterfaceId(JavaProject javaProject, long id) {
        connection = MysqlConnectionFactory.getConnection();
        List<JavaMethod> javaMethods = new LinkedList();
        try {
            Statement stm = connection.createStatement();

            //long t1 = System.currentTimeMillis();           
            ResultSet rs = stm.executeQuery("select * from JAVA_METHODS where interface_id=" + id + ";");
            //long t2 = System.currentTimeMillis();
            //System.out.println("Pegar todas os métodos de uma interface de uma revisão (somente o select) : "+(t2-t1)+"  milisegundos");
            int i = 0;
            while (rs.next()) {
                JavaData javaData = javaProject.getClassByName(rs.getString("return_type"));
                if (javaData == null) {
                    if (JavaPrimitiveType.getType(rs.getString("return_type")) != 0) {
                        javaData = new JavaPrimitiveType(JavaPrimitiveType.getType(rs.getString("return_type")));
                    } else {
                        javaData = javaProject.getJavaExternalClassByName(rs.getString("return_type"));
                        if (javaData == null) {
                            javaData = new JavaAbstractExternal(rs.getString("return_type"));
                            javaProject.addExternalClass((JavaAbstractExternal) javaData);
                        }
                    }
                }
                JavaMethod javaMethod = new JavaMethod(rs.getString("name"), rs.getString("original_signature"), javaData,
                        Boolean.valueOf(rs.getString("is_final")),
                        Boolean.valueOf(rs.getString("is_static")), Boolean.valueOf(rs.getString("is_abstract")),
                        Boolean.valueOf(rs.getString("is_synchronized")), Boolean.valueOf(rs.getString("is_private")),
                        Boolean.valueOf(rs.getString("is_public")), Boolean.valueOf(rs.getString("is_protected")),
                        Integer.valueOf(rs.getString("cyclomatic_complexity")), null);

                javaMethod.setSizeInChars(Integer.valueOf(rs.getString("size_in_chars")));
                javaMethod.setChangeInternalState(Boolean.valueOf(rs.getString("change_internal_state")));
                javaMethod.setIsAnAcessorMethod(Boolean.valueOf(rs.getString("is_an_acessor_method")));
                javaMethod.setAccessedAttribute(rs.getString("accessed_attribute"));
                javaMethod.setNumberOfLines(Integer.valueOf(rs.getString("number_of_lines")));
                javaMethod.setAccessToForeignDataNumber(Integer.valueOf(rs.getString("access_to_foreign_data")));
                javaMethod.setAccessToLocalDataNumber(Integer.valueOf(rs.getString("access_to_local_data")));
                javaMethod.setForeignDataProviderNumber(Integer.valueOf(rs.getString("foreign_data_provider")));
                javaMethod.setNumberOfLocalVariables(Integer.valueOf(rs.getString("number_of_local_variables")));

                javaMethod.setChangeInternalStateByMethodInvocations(Boolean.valueOf(rs.getString("change_internal_state_by_method_invocation")));
                String parameterString = rs.getString("parameters");
                if (!parameterString.equals("")) {
                    String parameters[] = parameterString.split(";");
                    for (int j = 0; j < parameters.length; j++) {
                        String parameter[] = parameters[j].split(":");
                        JavaData parameterType = javaProject.getClassByName(parameter[0]);
                        if (parameterType == null) {
                            if (JavaPrimitiveType.getType(parameter[0]) != 0) {
                                parameterType = new JavaPrimitiveType(JavaPrimitiveType.getType(parameter[0]));
                            } else {
                                parameterType = javaProject.getJavaExternalClassByName(parameter[0]);
                                if (parameterType == null) {
                                    parameterType = new JavaAbstractExternal(parameter[0]);
                                    javaProject.addExternalClass((JavaAbstractExternal) parameterType);
                                }
                            }

                        }
                        Parameter javaParameter = new Parameter(parameterType, parameter[1]);
                        javaMethod.addParameter(javaParameter);
                    }
                }

                javaMethod.setId(Long.parseLong(rs.getString("id")));
                javaMethods.add(javaMethod);
                i++;
            }
            //stm.execute("SHUTDOWN");
            //System.out.println("QUANTIDADE: " + i);
        } catch (Exception e) {
            System.out.println("ERRO method: " + e.getMessage());
        }
        return javaMethods;
    }

    @Override
    public List<JavaMethod> getJavaMethodsByClassId(JavaProject javaProject, long id) {
        connection = MysqlConnectionFactory.getConnection();
        List<JavaMethod> javaMethods = new LinkedList();
        try {
            Statement stm = connection.createStatement();
            //long t1 = System.currentTimeMillis();
            ResultSet rs = stm.executeQuery("select * from JAVA_METHODS where class_id=" + id + ";");
            //long t2 = System.currentTimeMillis();
            //System.out.println("Pegar todas os métodos de uma revisão (somente o select) : "+(t2-t1)+"  milisegundos");

            int i = 0;
            while (rs.next()) {
                //System.out.println("Java method: " + rs.getString("name") + "   return type: " + rs.getString("return_type"));
                JavaData javaData = javaProject.getClassByName(rs.getString("return_type"));
                if (javaData == null) {
                    if (JavaPrimitiveType.getType(rs.getString("return_type")) != 0) {
                        javaData = new JavaPrimitiveType(JavaPrimitiveType.getType(rs.getString("return_type")));
                    } else {
                        javaData = javaProject.getJavaExternalClassByName(rs.getString("return_type"));
                        if (javaData == null) {
                            javaData = new JavaAbstractExternal(rs.getString("return_type"));
                            javaProject.addExternalClass((JavaAbstractExternal) javaData);
                        }
                    }
                }
                JavaMethod javaMethod = new JavaMethod(rs.getString("name"), rs.getString("original_signature"), javaData,
                        Boolean.valueOf(rs.getString("is_final")),
                        Boolean.valueOf(rs.getString("is_static")), Boolean.valueOf(rs.getString("is_abstract")),
                        Boolean.valueOf(rs.getString("is_synchronized")), Boolean.valueOf(rs.getString("is_private")),
                        Boolean.valueOf(rs.getString("is_public")), Boolean.valueOf(rs.getString("is_protected")),
                        Integer.valueOf(rs.getString("cyclomatic_complexity")), null);
                String parameterString = rs.getString("parameters");
                javaMethod.setSizeInChars(Integer.valueOf(rs.getString("size_in_chars")));
                javaMethod.setChangeInternalState(Boolean.valueOf(rs.getString("change_internal_state")));
                javaMethod.setIsAnAcessorMethod(Boolean.valueOf(rs.getString("is_an_acessor_method")));
                javaMethod.setAccessedAttribute(rs.getString("accessed_attribute"));
                javaMethod.setNumberOfLines(Integer.valueOf(rs.getString("number_of_lines")));
                javaMethod.setAccessToForeignDataNumber(Integer.valueOf(rs.getString("access_to_foreign_data")));
                javaMethod.setAccessToLocalDataNumber(Integer.valueOf(rs.getString("access_to_local_data")));
                javaMethod.setForeignDataProviderNumber(Integer.valueOf(rs.getString("foreign_data_provider")));
                javaMethod.setNumberOfLocalVariables(Integer.valueOf(rs.getString("number_of_local_variables")));

                javaMethod.setChangeInternalStateByMethodInvocations(Boolean.valueOf(rs.getString("change_internal_state_by_method_invocation")));
                if (!parameterString.equals("")) {
                    //System.out.println("parameter string: "+parameterString);
                    String parameters[] = parameterString.split(";");
                    for (int j = 0; j < parameters.length; j++) {
                        String parameter[] = parameters[j].split(":");
                        JavaData parameterType = javaProject.getClassByName(parameter[0]);
                        if (parameterType == null) {
                            if (JavaPrimitiveType.getType(parameter[0]) != 0) {
                                parameterType = new JavaPrimitiveType(JavaPrimitiveType.getType(parameter[0]));
                            } else {
                                parameterType = javaProject.getJavaExternalClassByName(parameter[0]);
                                if (parameterType == null) {
                                    parameterType = new JavaAbstractExternal(parameter[0]);
                                    javaProject.addExternalClass((JavaAbstractExternal) parameterType);
                                }
                            }
                        }
                        Parameter javaParameter = new Parameter(parameterType, parameter[1]);
                        javaMethod.addParameter(javaParameter);
                    }
                }
                javaMethod.setId(Long.parseLong(rs.getString("id")));
                javaMethods.add(javaMethod);
                i++;
            }
            //stm.execute("SHUTDOWN");
            //System.out.println("QUANTIDADE Métodos  " + i);
        } catch (Exception e) {
            System.out.println("ERRO method: " + e.getMessage());
        }
        return javaMethods;
    }

    public void getJavaMethods(JavaProject javaProject, JavaAbstract javaAbstract) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = null;
            if (javaAbstract.getClass() == JavaClass.class) {
                rs = stm.executeQuery("select * from JAVA_METHODS where class_id=" + javaAbstract.getId() + ";");
            } else {
                rs = stm.executeQuery("select * from JAVA_METHODS where interface_id=" + javaAbstract.getId() + ";");
            }
            int i = 0;
            while (rs.next()) {
                JavaData javaData = javaProject.getClassByName(rs.getString("return_type"));
                if (javaData == null) {
                    javaData = javaProject.getJavaExternalClassByName(rs.getString("return_type"));
                    if (javaData == null) {
                        javaData = new JavaPrimitiveType(JavaPrimitiveType.getType(rs.getString("return_type")));
                    }
                }
                JavaMethod javaMethod = new JavaMethod(rs.getString("name"), rs.getString("original_signature"), javaData,
                        Boolean.valueOf(rs.getString("is_final")),
                        Boolean.valueOf(rs.getString("is_static")), Boolean.valueOf(rs.getString("is_abstract")),
                        Boolean.valueOf(rs.getString("is_synchronized")), Boolean.valueOf(rs.getString("is_private")),
                        Boolean.valueOf(rs.getString("is_public")), Boolean.valueOf(rs.getString("is_protected")),
                        Integer.valueOf(rs.getString("cyclomatic_complexity")), null);
                String parameterString = rs.getString("parameters");
                if (!parameterString.equals("")) {
                    String parameters[] = parameterString.split(";");
                    for (int j = 0; j < parameters.length; j++) {
                        String parameter[] = parameters[j].split(":");
                        JavaData parameterType = javaProject.getClassByName(parameter[0]);
                        if (parameterType == null) {
                            parameterType = javaProject.getJavaExternalClassByName(parameter[0]);
                            if (parameterType == null) {
                                parameterType = new JavaPrimitiveType(JavaPrimitiveType.getType(parameter[0]));
                            }
                        }
                        Parameter javaParameter = new Parameter(parameterType, parameter[1]);
                        javaMethod.addParameter(javaParameter);
                    }
                }
                javaMethod.setId(Long.parseLong(rs.getString("id")));
                if (javaAbstract.getClass() == JavaClass.class) {
                    ((JavaClass) javaAbstract).addMethod(javaMethod);
                } else {
                    ((JavaInterface) javaAbstract).addJavaMethod(javaMethod);
                }
                i++;
            }
            //stm.execute("SHUTDOWN");
            //System.out.println("QUANTIDADE: " + i);
        } catch (Exception e) {
            System.out.println("ERRO method: " + e.getMessage());
        }
    }
}
