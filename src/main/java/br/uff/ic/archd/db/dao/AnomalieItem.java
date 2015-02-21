/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao;

/**
 *
 * @author wallace
 */
public class AnomalieItem {
    private int anomalieId;
    private String item;
    
    AnomalieItem(int anomalieId, String item){
        this.anomalieId = anomalieId;
        this.item = item;
    }

    /**
     * @return the anomalieId
     */
    public int getAnomalieId() {
        return anomalieId;
    }

    /**
     * @return the item
     */
    public String getItem() {
        return item;
    }
    
}
