/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.model;

/**
 *
 * @author wallace
 */
public class Project {
    private String path;
    private String name;
    
    
    Project(String path, String name){
        this.path = path;
        this.name = name;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
}
