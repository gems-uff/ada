/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wallace
 */
public class Project {
    private int id;
    private String path;
    private String name;
    private List<String> codeDirs;
    
    
    public Project(int id, String path, String name, List<String> codeDirs){
        this.id = id;
        this.path = path;
        this.name = name;
        this.codeDirs = codeDirs;
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
    
    public void addcodeDir(String dir){
        codeDirs.add(dir);
    }
    
    public List<String> getCodeDirs(){
        return codeDirs;
    }
    
    public void setCodeDirs(List<String> codeDirs){
        this.codeDirs = codeDirs;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
    
}
