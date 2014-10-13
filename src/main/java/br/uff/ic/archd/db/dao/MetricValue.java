/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao;

/**
 *
 * @author wallace
 */
public class MetricValue {
    
    private String value;
    private int metricId;
    private String revisionId;
    private String versionedItem;
    
    

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the metricId
     */
    public int getMetricId() {
        return metricId;
    }

    /**
     * @param metricId the metricId to set
     */
    public void setMetricId(int metricId) {
        this.metricId = metricId;
    }

    /**
     * @return the revisionId
     */
    public String getRevisionId() {
        return revisionId;
    }

    /**
     * @param revisionId the revisionId to set
     */
    public void setRevisionId(String revisionId) {
        this.revisionId = revisionId;
    }

    /**
     * @return the versionedItem
     */
    public String getVersionedItem() {
        return versionedItem;
    }

    /**
     * @param versionedItem the versionedItem to set
     */
    public void setVersionedItem(String versionedItem) {
        this.versionedItem = versionedItem;
    }
    
}
