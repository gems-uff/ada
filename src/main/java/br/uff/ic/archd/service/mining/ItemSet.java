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
public class ItemSet {
    private List<Item> items;
    
    ItemSet(){
        items = new LinkedList();
    }
    
    public void addItem(Item item){
        getItems().add(item);
    }

    /**
     * @return the items
     */
    public List<Item> getItems() {
        return items;
    }
    
    //se o itemset do parametro é um um subitemset deste objeto
    public boolean isSubItem(ItemSet itemSet){
        boolean flag = true;
        
        for(Item item : itemSet.items){
            boolean exist = false;
            for(Item myItem : this.items){
                if(item.getItemNumber() == myItem.getItemNumber()){
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
    
    @Override
    public String toString(){
        String str = "( ";
        for(Item item : items){
            str = str + item.getItemText()+" , ";
        }
        str = str.substring(0, str.length() - 2);
        str = str + ")";
        return str;
    }
    
    public String toRuleNumber(){
        String str = "";
        for(Item item : items){
            str = str + item.getItemNumber()+" ";
        }
        str = str + "-1 ";
        return str;
    }
    
}
