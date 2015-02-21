/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.service.mining;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author wallace
 */
public class TemporaryCompoentsFileManager {
 
    private HashMap<String, String> hashClassMap;
    private HashMap<String, String> hashMethodMap;
    private HashMap<String, String> hashPackageMap;
    private int fileNumber;
    private static String pathClass;
    private static String pathMethods;
    private static String pathPackage;
    
    TemporaryCompoentsFileManager(String projectName){
        hashClassMap = new HashMap();
        hashMethodMap = new HashMap();
        hashPackageMap = new HashMap();
        fileNumber = 0;
        pathClass = System.getProperty("user.home") + "/.archd/FILES_TO_MINING/"+projectName+"/components/methods/";
        pathMethods = System.getProperty("user.home") + "/.archd/FILES_TO_MINING/"+projectName+"/components/class/";
        pathPackage = System.getProperty("user.home") + "/.archd/FILES_TO_MINING/"+projectName+"/components/package/";
        deleteFiles();
        
    }
    
    private void deleteFiles(){
        File file = new File(pathClass);
        
        if(file.exists()){
            File[] files = file.listFiles();
            for(int i = 0; i < files.length; i++){
                files[i].delete();
            }
        }else{
            file.mkdirs();
        }
        
        file = new File(pathMethods);
        
        if(file.exists()){
            File[] files = file.listFiles();
            for(int i = 0; i < files.length; i++){
                files[i].delete();
            }
        }else{
            file.mkdirs();
        }
        
        file = new File(pathPackage);
        
        if(file.exists()){
            File[] files = file.listFiles();
            for(int i = 0; i < files.length; i++){
                files[i].delete();
            }
        }else{
            file.mkdirs();
        }
    }
    
    public String getMethodFilePath(String methodSignature){
        String filePath = hashMethodMap.get(methodSignature);
        if(filePath == null){
            filePath = pathMethods+methodSignature+".txt";
            fileNumber++;
            hashMethodMap.put(methodSignature, filePath);
        }
        return filePath;
    }
    
    public List<String> getPathFiles(){
        List<String> files = new LinkedList();
        
        for(int i = 0; i < fileNumber; i++){
            //String filePath = path+"sequence_file_"+i+".txt";
            //files.add(filePath);
            //File file = new File(filePath);
            //file.m
        }
        
        return files;
    }
    
    public int getNumberOfFiles(){
        return fileNumber;
    }
    
    
}
