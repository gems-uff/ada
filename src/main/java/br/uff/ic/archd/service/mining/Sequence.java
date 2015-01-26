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
public class Sequence {
    private List<String> itemSet;
    
    Sequence(){
        itemSet = new LinkedList();
    }
    
    public void addItem(String item){
        itemSet.add(item);
    }
    
    public List<String> getItemSet(){
        return itemSet;
    }
}
