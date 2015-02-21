/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.service.mining;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author wallace
 */
public class RuleOcurrence {
    private int number;
    private List<String> revisions;
    
    RuleOcurrence(int number){
        this.number = number;
        revisions = new LinkedList();
    }
    
    RuleOcurrence(){
        this.number = 0;
        revisions = new LinkedList();
    }
    
    public void addRevision(String revison){
        revisions.add(revison);
    }
    
    public void addRevisionInHead(String revison){
        revisions.add(0,revison);
    }
    
    public List<String> getRevisions(){
        return revisions;
    }

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
    }
}
