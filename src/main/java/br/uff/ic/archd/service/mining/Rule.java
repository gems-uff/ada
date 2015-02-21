/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.service.mining;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author wallace
 */
public class Rule {
    private List<ItemSet> sequence;
    private List<Integer> linesOfOcurrence;
    private int support;
    private double confidence;
    private double lift;
    
    Rule(){
        sequence = new LinkedList();
    }
    
    
    public void addItemSet(ItemSet itemSet){
        sequence.add(itemSet);
    }
    

    /**
     * @return the sequence
     */
    public List<ItemSet> getSequence() {
        return sequence;
    }

    /**
     * @return the confidence
     */
    public double getConfidence() {
        return confidence;
    }

    /**
     * @return the lift
     */
    public double getLift() {
        return lift;
    }
    
    
    
    //se a regra do parametro é uma subregra deste objeto
    public boolean isSubRule(Rule rule){
        boolean flag = true;
        
        int sequenceAux = 0;
        for(int i = 0; i < rule.getSequence().size(); i++){
            ItemSet itemSet = rule.sequence.get(i);
            boolean exist = false;
            for(int j = sequenceAux; j < this.sequence.size(); j++){
                ItemSet myItemSet = this.sequence.get(j);
                if(myItemSet.isSubItem(itemSet)){
                    sequenceAux = j+1;
                    exist = true;
                    break;
                }
            }
            if(!exist){
                flag = false;
                break;
            }
        }
        
        return flag;
    }

    /**
     * @return the linesOfOcurrence
     */
    public List<Integer> getLinesOfOcurrence() {
        return linesOfOcurrence;
    }

    /**
     * @param linesOfOcurrence the linesOfOcurrence to set
     */
    public void setLinesOfOcurrence(List<Integer> linesOfOcurrence) {
        this.linesOfOcurrence = linesOfOcurrence;
    }
    
    
    @Override
    public String toString(){
        String str = "";
        for(ItemSet itemSet : sequence){
            str = str + itemSet + " => ";
        }
        str = str.substring(0, str.length() - 3);
        return str;
    }
    
    
    public String toRuleNumber(){
        String str = "";
        for(ItemSet itemSet : sequence){
            str = str + itemSet.toRuleNumber();
        }
        str = str+"#SUP: "+getSupport();
        return str;
    }

    /**
     * @return the support
     */
    public int getSupport() {
        return support;
    }

    /**
     * @param support the support to set
     */
    public void setSupport(int support) {
        this.support = support;
    }

    /**
     * @param confidence the confidence to set
     */
    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    /**
     * @param lift the lift to set
     */
    public void setLift(double lift) {
        this.lift = lift;
    }


}
