/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao.mysql;

import br.uff.ic.archd.db.dao.*;
import br.uff.ic.archd.javacode.JavaAbstract;
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
public class MySQLInternalImportsDao implements InternalImportsDao {

    private Connection connection;

    public MySQLInternalImportsDao() {

    }

    @Override
    public void saveInternalImport(JavaAbstract javaAbstract, JavaAbstract javaAbstractImport) {
        connection = MysqlConnectionFactory.getConnection();
        try {

            Statement stm = connection.createStatement();
            if (javaAbstract.getClass() == JavaClass.class) {
                if (javaAbstractImport.getClass() == JavaClass.class) {
                    System.out.println("insert into INTERNAL_IMPORTS (class_id , class_import_id) "
                            + " VALUES (" + javaAbstract.getId() + "," + javaAbstractImport.getId() + ");");
                    stm.executeUpdate("insert into INTERNAL_IMPORTS (class_id , class_import_id) "
                            + " VALUES (" + javaAbstract.getId() + "," + javaAbstractImport.getId() + ");");
                } else {
                    System.out.println("insert into INTERNAL_IMPORTS (class_id , interface_import_id) "
                            + " VALUES (" + javaAbstract.getId() + "," + javaAbstractImport.getId() + ");");
                    stm.executeUpdate("insert into INTERNAL_IMPORTS (class_id , interface_import_id) "
                            + " VALUES (" + javaAbstract.getId() + "," + javaAbstractImport.getId() + ");");
                }
            } else {
                if (javaAbstractImport.getClass() == JavaClass.class) {
                    System.out.println("insert into INTERNAL_IMPORTS (interface_id , class_import_id) "
                            + " VALUES (" + javaAbstract.getId() + "," + javaAbstractImport.getId() + ");");
                    stm.executeUpdate("insert into INTERNAL_IMPORTS (interface_id , class_import_id) "
                            + " VALUES (" + javaAbstract.getId() + "," + javaAbstractImport.getId() + ");");
                } else {
                    System.out.println("insert into INTERNAL_IMPORTS (interface_id , interface_import_id) "
                            + " VALUES (" + javaAbstract.getId() + "," + javaAbstractImport.getId() + ");");
                    stm.executeUpdate("insert into INTERNAL_IMPORTS (interface_id , interface_import_id) "
                            + " VALUES (" + javaAbstract.getId() + "," + javaAbstractImport.getId() + ");");
                }
            }
            stm.close();

        } catch (SQLException e) {
            System.out.println("ERRO internal imports: " + e.getMessage());
        }
    }

    public void saveInternalImport(JavaAbstract javaAbstract) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            int counter1 = 0;
            int counter2 = 0;
            int counter3 = 0;
            int counter4 = 0;
            String query1 = "insert into INTERNAL_IMPORTS (class_id , class_import_id) "
                    + " VALUES  ";
            String query2 = "insert into INTERNAL_IMPORTS (class_id , interface_import_id) "
                    + " VALUES ";
            String query3 = "insert into INTERNAL_IMPORTS (interface_id , class_import_id) "
                    + " VALUES ";
            String query4 = "insert into INTERNAL_IMPORTS (interface_id , interface_import_id) "
                    + " VALUES  ";

            for (JavaAbstract javaAbstractImport : javaAbstract.getClassesImports()) {
                if (javaAbstract.getClass() == JavaClass.class) {
                    if (javaAbstractImport.getClass() == JavaClass.class) {
                        if (counter1 == 0) {
                            query1 = query1 + "(" + javaAbstract.getId() + "," + javaAbstractImport.getId() + ")";
                            counter1++;
                        } else {
                            query1 = query1 + ", (" + javaAbstract.getId() + "," + javaAbstractImport.getId() + ")";
                        }

                    } else {
                        if (counter2 == 0) {
                            query2 = query2 + "(" + javaAbstract.getId() + "," + javaAbstractImport.getId() + ")";
                            counter2++;
                        } else {
                            query2 = query2 + ", (" + javaAbstract.getId() + "," + javaAbstractImport.getId() + ")";
                        }

                    }
                } else {
                    if (javaAbstractImport.getClass() == JavaClass.class) {
                        if (counter3 == 0) {
                            query3 = query3 + "(" + javaAbstract.getId() + "," + javaAbstractImport.getId() + ")";
                            counter3++;
                        } else {
                            query3 = query3 + ", (" + javaAbstract.getId() + "," + javaAbstractImport.getId() + ")";
                        }

                    } else {
                        if (counter4 == 0) {
                            query4 = query4 + "(" + javaAbstract.getId() + "," + javaAbstractImport.getId() + ")";
                            counter4++;
                        } else {
                            query4 = query4 + ", (" + javaAbstract.getId() + "," + javaAbstractImport.getId() + ")";
                        }

                    }
                }
            }
            if (counter1 != 0) {
                query1 = query1 + ";";
                PreparedStatement stm = connection.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
                stm.execute();
                stm.close();
            }
            if (counter2 != 0) {
                query2 = query2 + ";";
                PreparedStatement stm = connection.prepareStatement(query2, Statement.RETURN_GENERATED_KEYS);
                stm.execute();
                stm.close();
            }
            if (counter3 != 0) {
                query3 = query3 + ";";
                PreparedStatement stm = connection.prepareStatement(query3, Statement.RETURN_GENERATED_KEYS);
                stm.execute();
                stm.close();
            }
            if (counter4 != 0) {
                query4 = query4 + ";";
                PreparedStatement stm = connection.prepareStatement(query4, Statement.RETURN_GENERATED_KEYS);
                stm.execute();
                stm.close();
            }
            
            

        } catch (SQLException e) {
            System.out.println("ERRO internal imports: " + e.getMessage());
        }
    }

    public void saveInternalImport(List<JavaAbstract> javaAbstracts) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            int counter1 = 0;
            int counter2 = 0;
            int counter3 = 0;
            int counter4 = 0;
            String query1 = "insert into INTERNAL_IMPORTS (class_id , class_import_id) "
                    + " VALUES  ";
            String query2 = "insert into INTERNAL_IMPORTS (class_id , interface_import_id) "
                    + " VALUES ";
            String query3 = "insert into INTERNAL_IMPORTS (interface_id , class_import_id) "
                    + " VALUES ";
            String query4 = "insert into INTERNAL_IMPORTS (interface_id , interface_import_id) "
                    + " VALUES  ";

            for (JavaAbstract javaAbstract : javaAbstracts) {
                for (JavaAbstract javaAbstractImport : javaAbstract.getClassesImports()) {
                    if (javaAbstract.getClass() == JavaClass.class) {
                        if (javaAbstractImport.getClass() == JavaClass.class) {
                            if (counter1 == 0) {
                                query1 = query1 + "(" + javaAbstract.getId() + "," + javaAbstractImport.getId() + ")";
                                counter1++;
                            } else {
                                query1 = query1 + ", (" + javaAbstract.getId() + "," + javaAbstractImport.getId() + ")";
                            }

                        } else {
                            if (counter2 == 0) {
                                query2 = query2 + "(" + javaAbstract.getId() + "," + javaAbstractImport.getId() + ")";
                                counter2++;
                            } else {
                                query2 = query2 + ", (" + javaAbstract.getId() + "," + javaAbstractImport.getId() + ")";
                            }

                        }
                    } else {
                        if (javaAbstractImport.getClass() == JavaClass.class) {
                            if (counter3 == 0) {
                                query3 = query3 + "(" + javaAbstract.getId() + "," + javaAbstractImport.getId() + ")";
                                counter3++;
                            } else {
                                query3 = query3 + ", (" + javaAbstract.getId() + "," + javaAbstractImport.getId() + ")";
                            }

                        } else {
                            if (counter4 == 0) {
                                query4 = query4 + "(" + javaAbstract.getId() + "," + javaAbstractImport.getId() + ")";
                                counter4++;
                            } else {
                                query4 = query4 + ", (" + javaAbstract.getId() + "," + javaAbstractImport.getId() + ")";
                            }

                        }
                    }
                }
            }
            if (counter1 != 0) {
                query1 = query1 + ";";
                PreparedStatement stm = connection.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
                stm.execute();
                stm.close();
            }
            if (counter2 != 0) {
                query2 = query2 + ";";
                PreparedStatement stm = connection.prepareStatement(query2, Statement.RETURN_GENERATED_KEYS);
                stm.execute();
                stm.close();
            }
            if (counter3 != 0) {
                query3 = query3 + ";";
                PreparedStatement stm = connection.prepareStatement(query3, Statement.RETURN_GENERATED_KEYS);
                stm.execute();
                stm.close();
            }
            if (counter4 != 0) {
                query4 = query4 + ";";
                PreparedStatement stm = connection.prepareStatement(query4, Statement.RETURN_GENERATED_KEYS);
                stm.execute();
                stm.close();
            }
            


        } catch (SQLException e) {
            System.out.println("ERRO internal imports: " + e.getMessage());
        }
    }

    @Override
    public void getInternalImports(JavaAbstract javaAbstract, JavaProject javaProject) {
        connection = MysqlConnectionFactory.getConnection();
        try {
            List<JavaAbstract> importClasses = new LinkedList();
            Statement stm = connection.createStatement();
            ResultSet rs = null;
            //long t1 = System.currentTimeMillis();
            if (javaAbstract.getClass() == JavaClass.class) {
                rs = stm.executeQuery("select * from INTERNAL_IMPORTS where class_id=" + javaAbstract.getId() + ";");
            } else {
                rs = stm.executeQuery("select * from INTERNAL_IMPORTS where interface_id=" + javaAbstract.getId() + ";");
            }
            //long t2 = System.currentTimeMillis();
            //System.out.println("Pegar todos os imports internos de uma revisão (somente select) : "+(t2-t1)+"  milisegundos");

            int i = 0;

            while (rs.next()) {

                String classId = rs.getString("class_import_id");
                String interfaceId = rs.getString("interface_import_id");
                if (classId != null) {
                    importClasses.add(javaProject.getClassById(Long.valueOf(classId)));
                } else {
                    importClasses.add(javaProject.getInterfaceById(Long.valueOf(interfaceId)));
                }

                i++;
            }
            javaAbstract.addImportClasses(importClasses);
            
            stm.close();
            rs.close();
            //stm.execute("SHUTDOWN");
            //System.out.println("QUANTIDADE: " + i);
        } catch (Exception e) {
            System.out.println("ERRO internal imports: " + e.getMessage());
        }
    }
}
