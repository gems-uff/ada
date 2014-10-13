/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao;

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
    
    public static DataBaseFactory getInstance(){
        if(dataBaseFactory == null){
            dataBaseFactory = new DataBaseFactory();
        }
        return dataBaseFactory;
    }
    
    private DataBaseFactory(){
        terminatedDao = new HsqldbTerminatedDao();
        classesDao = new HsqldbClassesDao();
        interfaceDao = new HsqldbInterfaceDao();
        javaMethodDao = new HsqldbJavaMethodDao();
        javaAttributeDao = new HsqldbJavaAttributeDao();
        implementedInterfacesDao = new HsqldbImplementedInterfacesDao();
        internalImportsDao = new HsqldbInternalImportsDao();
        externalImportsDao = new HsqldbExternalImportsDao();
        methodInvocationsDao = new HsqldbMethodInvocationsDao();
        
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
    
}
