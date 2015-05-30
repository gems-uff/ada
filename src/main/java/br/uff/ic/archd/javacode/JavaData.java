/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.javacode;

/**
 *
 * @author wallace
 */
public class JavaData {
    
    private String name;
    private String originalSignature;
     /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    public String getFullQualifiedName(){
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the originalSignature
     */
    public String getOriginalSignature() {
        return originalSignature;
    }

    /**
     * @param originalSignature the originalSignature to set
     */
    public void setOriginalSignature(String originalSignature) {
        this.originalSignature = originalSignature;
    }
}
