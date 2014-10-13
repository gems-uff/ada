/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author wallace
 */
public class VersionedItem {
    
    public static final int CLASS_TYPE = 1;
    public static final int METHOD_TYPE = 2;
    private int projectId;
    private int type;
    private String assignature;
    private List<String> revisions;
    
    VersionedItem(int type, String assignature, int projectId){
        this.type = type;
        this.assignature = assignature;
        this.projectId = projectId;
        revisions = new LinkedList();
    }
    
    public void addRevision(String revision){
        revisions.add(revision);
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @return the assignature
     */
    public String getAssignature() {
        return assignature;
    }

    /**
     * @return the revisions
     */
    public List<String> getRevisions() {
        return revisions;
    }

    /**
     * @return the projectId
     */
    public int getProjectId() {
        return projectId;
    }
    
    
    
}
