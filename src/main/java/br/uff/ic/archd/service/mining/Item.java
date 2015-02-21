/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.service.mining;

/**
 *
 * @author wallace
 */
public class Item {
    private int itemNumber;
    private String itemText;
    
    
    Item(int itemNumber, String itemText){
        this.itemNumber = itemNumber;
        this.itemText = itemText;
    }

    /**
     * @return the itemNumber
     */
    public int getItemNumber() {
        return itemNumber;
    }

    /**
     * @return the itemText
     */
    public String getItemText() {
        return itemText;
    }
    
    
}
