/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.ast.service;

import br.uff.ic.archd.javacode.JavaData;


/**
 *
 * @author wallace
 */
public class Variable {
    private String name;
    private JavaData javaData;
    
    Variable(String name, JavaData javaData){
        this.name = name;
        this.javaData = javaData;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the javaData
     */
    public JavaData getJavaData() {
        return javaData;
    }
    
    
}
