/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.javacode;

/**
 *
 * @author wallace
 */
public class Parameter {
    
    private JavaData type;
    private String name;
    
    public Parameter(JavaData type, String name){
        this.type = type;
        this.name = name;
    }

    /**
     * @return the type
     */
    public JavaData getType() {
        return type;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}
