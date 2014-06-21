/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.ast.service;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author wallace
 */
public class ParserProject {
    
    private AstParser astParser;
    public void parser(String projectPath){
        HashMap<String, List<String>> hash = new HashMap();
        astParser = new AstParser();
        getJavaProject(hash, projectPath);
        
    }
    
    public void getJavaProject(HashMap<String, List<String>> hash, String path){
        File file = new File(path);
        if(file.isDirectory()){
           File files[] = file.listFiles();
           for(int i=0; i < files.length; i++){
               getJavaProject(hash, files[i].getAbsolutePath());
           }
        }else{
            if(file.getAbsolutePath().endsWith(".java")){
                List<String> list = hash.get(file.getName().substring(0, file.getName().length()-5));
                if(list == null){
                    list = new LinkedList();
                }
                list.add(astParser.getClassName(path));
                hash.put(file.getName().substring(0, file.getName().length()-5), list);
            }
        }
    }
    
    
}
