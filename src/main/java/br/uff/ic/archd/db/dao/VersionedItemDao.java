/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao;

import java.util.List;

/**
 *
 * @author wallace
 */
public interface VersionedItemDao {
    public void save(VersionedItem versionedItem);
    public void saveVersionedItemRevision(VersionedItem versionedItem, String revision);
    public List<VersionedItem> getAllVersionedItens();
}
