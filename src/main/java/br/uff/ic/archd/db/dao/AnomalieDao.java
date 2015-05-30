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
public interface AnomalieDao {
    public void save(int anomalieId, String itemName, String revisionId);
    public List<AnomalieItem> getItemsByRevisionId(String revisionId);
    public void save(int anomalieId, List<String> itemNames, String revisionId);
}
