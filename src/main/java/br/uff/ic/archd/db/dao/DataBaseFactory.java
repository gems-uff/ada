/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao;

import br.uff.ic.archd.db.dao.mysql.MySQLAnomalieDao;
import br.uff.ic.archd.db.dao.mysql.MySQLArtifactBirthDao;
import br.uff.ic.archd.db.dao.mysql.MySQLClassesDao;
import br.uff.ic.archd.db.dao.mysql.MySQLExternalImportsDao;
import br.uff.ic.archd.db.dao.mysql.MySQLImplementedInterfacesDao;
import br.uff.ic.archd.db.dao.mysql.MySQLInterfaceDao;
import br.uff.ic.archd.db.dao.mysql.MySQLInternalImportsDao;
import br.uff.ic.archd.db.dao.mysql.MySQLJavaAttributeDao;
import br.uff.ic.archd.db.dao.mysql.MySQLJavaExternalAttributeAccessDao;
import br.uff.ic.archd.db.dao.mysql.MySQLJavaMethodDao;
import br.uff.ic.archd.db.dao.mysql.MySQLMethodInvocationsDao;
import br.uff.ic.archd.db.dao.mysql.MySQLOriginalNameDao;
import br.uff.ic.archd.db.dao.mysql.MySQLTerminatedDao;

/**
 *
 * @author wallace
 */
public class DataBaseFactory {
    private static DataBaseFactory dataBaseFactory;
    private TerminatedDao terminatedDao;
    private ClassesDao classesDao;
    private InterfaceDao interfaceDao;
    private JavaMethodDao javaMethodDao;
    private JavaAttributeDao javaAttributeDao;
    private ImplementedInterfacesDao implementedInterfacesDao;
    private InternalImportsDao internalImportsDao;
    private ExternalImportsDao externalImportsDao;
    private MethodInvocationsDao methodInvocationsDao;
    private JavaExternalAttributeAccessDao javaExternalAttributeAccessDao;
    private AnomalieDao anomalieDao;
    private ArtifactBirthDao artifactBirthDao;
    private OriginalNameDao originalNameDao;
    
    public static DataBaseFactory getInstance(){
        if(dataBaseFactory == null){
            dataBaseFactory = new DataBaseFactory();
        }
        return dataBaseFactory;
    }
    
//    private DataBaseFactory(){
//        terminatedDao = new HsqldbTerminatedDao();
//        classesDao = new HsqldbClassesDao();
//        interfaceDao = new HsqldbInterfaceDao();
//        javaMethodDao = new HsqldbJavaMethodDao();
//        javaAttributeDao = new HsqldbJavaAttributeDao();
//        implementedInterfacesDao = new HsqldbImplementedInterfacesDao();
//        internalImportsDao = new HsqldbInternalImportsDao();
//        externalImportsDao = new HsqldbExternalImportsDao();
//        methodInvocationsDao = new HsqldbMethodInvocationsDao();
//        javaExternalAttributeAccessDao = new HsqldbJavaExternalAttributeAccessDao();
//        anomalieDao = new HsqldbAnomalieDao();
//        artifactBirthDao = new HsqldbArtifactBirthDao();
//        originalNameDao = new HsqldbOriginalNameDao();
//        
//    }
    
    
    private DataBaseFactory(){
        terminatedDao = new MySQLTerminatedDao();
        classesDao = new MySQLClassesDao();
        interfaceDao = new MySQLInterfaceDao();
        javaMethodDao = new MySQLJavaMethodDao();
        javaAttributeDao = new MySQLJavaAttributeDao();
        implementedInterfacesDao = new MySQLImplementedInterfacesDao();
        internalImportsDao = new MySQLInternalImportsDao();
        externalImportsDao = new MySQLExternalImportsDao();
        methodInvocationsDao = new MySQLMethodInvocationsDao();
        javaExternalAttributeAccessDao = new MySQLJavaExternalAttributeAccessDao();
        anomalieDao = new MySQLAnomalieDao();
        artifactBirthDao = new MySQLArtifactBirthDao();
        originalNameDao = new MySQLOriginalNameDao();
        
    }
    
    public TerminatedDao getTerminatedDao(){
        return terminatedDao;
    }

    /**
     * @return the classesDao
     */
    public ClassesDao getClassesDao() {
        return classesDao;
    }

    /**
     * @return the interfaceDao
     */
    public InterfaceDao getInterfaceDao() {
        return interfaceDao;
    }

    /**
     * @return the javaMethodDao
     */
    public JavaMethodDao getJavaMethodDao() {
        return javaMethodDao;
    }

    /**
     * @return the javaAttributeDao
     */
    public JavaAttributeDao getJavaAttributeDao() {
        return javaAttributeDao;
    }

    /**
     * @return the implementedInterfacesDao
     */
    public ImplementedInterfacesDao getImplementedInterfacesDao() {
        return implementedInterfacesDao;
    }

    /**
     * @return the internalImportsDao
     */
    public InternalImportsDao getInternalImportsDao() {
        return internalImportsDao;
    }

    /**
     * @return the externalImportsDao
     */
    public ExternalImportsDao getExternalImportsDao() {
        return externalImportsDao;
    }

    /**
     * @return the methodInvocationsDao
     */
    public MethodInvocationsDao getMethodInvocationsDao() {
        return methodInvocationsDao;
    }
    
    public JavaExternalAttributeAccessDao getJavaExternalAttributeAccessDao(){
        return javaExternalAttributeAccessDao;
    }

    /**
     * @return the anomalieDao
     */
    public AnomalieDao getAnomalieDao() {
        return anomalieDao;
    }

    /**
     * @return the artifactBirthDao
     */
    public ArtifactBirthDao getArtifactBirthDao() {
        return artifactBirthDao;
    }

    /**
     * @return the originalNameDao
     */
    public OriginalNameDao getOriginalNameDao() {
        return originalNameDao;
    }
    
}
