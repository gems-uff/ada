/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao.mysql;

import br.uff.ic.archd.db.dao.*;
import br.uff.ic.archd.javacode.Parameter;
import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *
 * @author wallace
 */
public class MySQLJavaParameterDao implements JavaParameterDao{

    private Connection connection;
    
    MySQLJavaParameterDao(){
        
    }
    
    @Override
    public void save(Parameter parameter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Parameter> getAllParameterByMethodId(long methodId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
