/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao.mysql;

import br.uff.ic.archd.db.dao.*;
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
public class MySQLAnomalieDao implements AnomalieDao {
    private Connection connection;
    private PreparedStatement prepareStatement;
    private Statement statement;

    public MySQLAnomalieDao() {
        


    }
    
    public void save(int anomalieId, String itemName, String revisionId){
        connection = MysqlConnectionFactory.getConnection();
        System.out.println("insert into ANOMALIES (anomalie_id ,"
                        + "item_name ,"
                        + "revision_id )"
                        + " VALUES (" + anomalieId + ",'"
                        + itemName + "','"
                        + revisionId+ "'"
                        + ");");
        String query = "insert into ANOMALIES (anomalie_id ,"
                        + "item_name ,"
                        + "revision_id )"
                        + " VALUES (" + anomalieId + ",'"
                        + itemName + "','"
                        + revisionId+ "'"
                        + ");";

        ResultSet generatedKeys = null;
        try {
            connection = MysqlConnectionFactory.getConnection();
            prepareStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);


            int affectedRows = prepareStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating event failed, no rows affected.");
            }

            generatedKeys = prepareStatement.getGeneratedKeys();
            
            if (generatedKeys.next()) {
                //alert.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("Creating event failed, no generated key obtained.");
            }
            prepareStatement.close();
            generatedKeys.close();
        }catch(Exception e){
            System.out.println("MySQLAnomalieDao save: "+e.getMessage());
            e.printStackTrace();
        } finally {
//            DBUtil.close(generatedKeys);
//            DBUtil.close(prepareStatement);
//            DBUtil.close(connection);
        }
        
    }
    
    public void save(int anomalieId, List<String> itemNames, String revisionId){
        connection = MysqlConnectionFactory.getConnection();
        try {
            if(!itemNames.isEmpty()){
                String query = "insert into ANOMALIES (anomalie_id ,"
                        + "item_name ,"
                        + "revision_id )"
                        + " VALUES ";
                String itemName = itemNames.get(0);
                query = query + "(" + anomalieId + ",'"
                        + itemName + "','"
                        + revisionId+ "'"
                        + ")";
                for(int i = 1; i < itemNames.size(); i++){
                    itemName = itemNames.get(i);
                    query = query + ", (" + anomalieId + ",'"
                        + itemName + "','"
                        + revisionId+ "'"
                        + ")";
                }
                query = query + ";";
                System.out.println(query);
                PreparedStatement stm = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                stm.execute();
                prepareStatement.close();
            }
            

            

        } catch (SQLException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
        
        
        
    }
    
    public List<AnomalieItem> getItemsByRevisionId(String revisionId){
        List<AnomalieItem> items = new LinkedList();
        connection = MysqlConnectionFactory.getConnection();
        try {
            Statement stm = connection.createStatement();
            
            //long t1 = System.currentTimeMillis();           
            ResultSet rs = stm.executeQuery("select * from ANOMALIES where revision_id='" + revisionId + "';");
            int i = 0;
            while (rs.next()) {
                AnomalieItem anomalieItem = new AnomalieItem(Integer.valueOf(rs.getString("anomalie_id")), rs.getString("item_name"));
                System.out.println("Anomalie: "+rs.getString("anomalie_id")+"      -    "+rs.getString("item_name"));
                items.add(anomalieItem);
                i++;
            }
            stm.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("ERRO anomalie: " + e.getMessage());
        }
        
        return items;
    }
    
    /*public void drop(){
        try {
            Statement stm = connection.createStatement();
            //stm.executeQuery("DROP INDEX revision_id;");
            stm.execute("drop table ANOMALIES;");
            ResultSet rs = stm.executeQuery("select * from ANOMALIES;");
            int i = 0;
            
            while (rs.next()) {
                i++  ;  
                System.out.println("Anomalie: "+rs.getString("id")+"   -  "+rs.getString("revision_id"));
                int n = stm.executeUpdate("delete from ANOMALIES where id = "+rs.getString("id")+";");
                System.out.println("Resultado: "+n);
            }
            
            System.out.println("Tamanho total de ANOMALIES: "+i);
            //stm.executeUpdate("create index revision_id on ANOMALIES (revision_id);");
            System.out.println("delete from ANOMALIES where id > 0;");
            stm.executeUpdate("delete from ANOMALIES where id > 0;");
            System.out.println("Dropou o banco");
            
        } catch (SQLException e) {
            System.out.println("ERRO save anomalie: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String args[]){
        HsqldbAnomalieDao hsqldbAnomalieDao = new HsqldbAnomalieDao();
        hsqldbAnomalieDao.drop();
    }*/
    

}
