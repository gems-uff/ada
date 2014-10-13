/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.factory;

import br.uff.ic.archd.db.dao.VersionedItemDao;

/**
 *
 * @author wallace
 */
public class VersionedItemDaoFactory {
    
    private static VersionedItemDaoFactory versionedItemDaoFactory;
    
    public static VersionedItemDaoFactory getInstance(){
        if(versionedItemDaoFactory == null){
            versionedItemDaoFactory = new VersionedItemDaoFactory();
        }
        
        return versionedItemDaoFactory;
    }
    
    private VersionedItemDaoFactory(){
        
    }
    
    public VersionedItemDao getVersionedItemDao(){
        return null;
    }
}
