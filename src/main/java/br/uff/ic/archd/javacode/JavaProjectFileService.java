/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.javacode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author wallace
 */
public class JavaProjectFileService {
    String path = System.getProperty("user.home") + "/.archd/calculate_metrics_offmemmory/";
    private String projectName;
    
    //ordem dos dados das classes
    //1 path
    
    //ordem dos dados das classes
    //1 path
    
    JavaProjectFileService(String projectName){
        this.projectName = projectName;
        String fileStr = path + projectName;
        try {
            if (new File(fileStr).exists()) {
                FileUtils.deleteDirectory(new File(fileStr));
            }
        } catch (Exception e) {
            System.out.println("");
        }
    }
    
    
    public void createJavaPackage(){
        
    }
    
    public void createJavaClass(String className, String packageName, String path){
        try {
            File file = new File(path + projectName+"/"+packageName+"/"+className+".class");
            file.mkdirs();
            PrintWriter writer0 = new PrintWriter(path + projectName+"/"+packageName+"/"+className+".class"+"/.classFeatures.txt", "UTF-8");
            writer0.println(path);
            writer0.close();
        } catch (Exception e) {
            System.out.println("Erro saveAnomalie:" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void createJavaInterface(String interfaceName, String packageName, String path){
        try {
            File file = new File(path + projectName+"/"+packageName+"/"+interfaceName+".interface");
            file.mkdirs();
            PrintWriter writer0 = new PrintWriter(path + projectName+"/"+packageName+"/"+interfaceName+".interface"+"/.classFeatures.txt", "UTF-8");
            writer0.println(path);
            writer0.close();
        } catch (Exception e) {
            System.out.println("Erro saveAnomalie:" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void addImportListToClass(String className, String packageName, List<String> list){
        try {
            PrintWriter writer0 = new PrintWriter(path + projectName+"/"+packageName+"/"+className+".class"+"/.classFeatures.txt", "UTF-8");
            for(String javaName : list){
                writer0.print(javaName+" ");
            }
            writer0.println();
            writer0.close();
        } catch (Exception e) {
            System.out.println("Erro saveAnomalie:" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void setSuperClass(String className, String packageName, String superClassName){
        try {
            PrintWriter writer0 = new PrintWriter(path + projectName+"/"+packageName+"/"+className+".class"+"/.classFeatures.txt", "UTF-8");
            writer0.println(superClassName);
            writer0.close();
        } catch (Exception e) {
            System.out.println("Erro saveAnomalie:" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void addImplementedInterfaces(String className, String packageName, List<String> list){
        try {
            PrintWriter writer0 = new PrintWriter(path + projectName+"/"+packageName+"/"+className+".class"+"/.classFeatures.txt", "UTF-8");
            for(String javaName : list){
                writer0.print(javaName+" ");
            }
            writer0.println();
            writer0.close();
        } catch (Exception e) {
            System.out.println("Erro saveAnomalie:" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void addImportListToInterface(String interfaceName, String packageName, List<String> list){
        try {
            PrintWriter writer0 = new PrintWriter(path + projectName+"/"+packageName+"/"+interfaceName+".interface"+"/.classFeatures.txt", "UTF-8");
            for(String javaName : list){
                writer0.print(javaName+" ");
            }
            writer0.println();
            writer0.close();
        } catch (Exception e) {
            System.out.println("Erro saveAnomalie:" + e.getMessage());
            e.printStackTrace();
        }
    }
    //method
    //1 return type
    //2 is final
    //3 is static
    //4 is abstract
    //5 is synchonized
    //6 is private
    //7 is public
    //8 is protected
    //9 cyclomatic complexity
    
    public void createJavaMethodFromClass(String className, String packageName, String methodSignature, String returnName, boolean isFinal, boolean isStatic, 
            boolean isAbstract, boolean isSynchronized, boolean isPrivate, boolean isPublic, boolean isProtected, int cyclometicComplexity, String code){
        try {
            PrintWriter writer0 = new PrintWriter(path + projectName+"/"+packageName+"/"+className+".class/"+methodSignature+".txt", "UTF-8");
            writer0.print(returnName);
            writer0.print(isFinal);
            writer0.print(isStatic);
            writer0.print(isAbstract);
            writer0.print(isSynchronized);
            writer0.print(isPrivate);
            writer0.print(isPublic);
            writer0.print(isProtected);
            writer0.print(cyclometicComplexity);
            writer0.close();
            writer0 = new PrintWriter(path + projectName+"/"+packageName+"/"+className+".class/"+methodSignature+".code", "UTF-8");
            writer0.print(code);
            writer0.close();
            
        } catch (Exception e) {
            System.out.println("Erro saveAnomalie:" + e.getMessage());
            e.printStackTrace();
        }
        

    }
    //method
    //1 return type
    //2 is final
    //3 is static
    //4 is abstract
    //5 is synchonized
    //6 is private
    //7 is public
    //8 is protected
    public void createJavaMethodFromInterface(String interfaceName, String packageName, String methodSignature, String returnName, boolean isFinal, boolean isStatic, 
            boolean isAbstract, boolean isSynchronized, boolean isPrivate, boolean isPublic, boolean isProtected){
        try {
            PrintWriter writer0 = new PrintWriter(path + projectName+"/"+packageName+"/"+interfaceName+".interface/"+methodSignature+".txt", "UTF-8");
            writer0.print(returnName);
            writer0.print(isFinal);
            writer0.print(isStatic);
            writer0.print(isAbstract);
            writer0.print(isSynchronized);
            writer0.print(isPrivate);
            writer0.print(isPublic);
            writer0.print(isProtected);
            writer0.close();
            
            
        } catch (Exception e) {
            System.out.println("Erro saveAnomalie:" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public List<String> getMethodsSignatures(String className, String packageName){
        List<String> methods = new LinkedList();
        try{
            File file = new File(path + projectName+"/"+packageName+"/"+className+".class/");
            if (file.exists()) {
                String files[] = file.list();
                for (int i = 0; i < files.length; i++) {
                    if(files[i].endsWith("txt")){
                        methods.add(files[i].split("\\.")[0]);
                    }
                }
            
            }

        
        }catch(Exception e){
            
        }
        return methods;
    }
    
    public String getMethodCode(String className, String packageName, String methodSignature){
        StringBuilder stringBuilder = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(path + projectName+"/"+packageName+"/"+className+".class/"+methodSignature+".code"));
            String line = null;
            
            String ls = System.getProperty("line.separator");

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            

        
        }catch(Exception e){
            
        }
        return stringBuilder.toString();
    }
    
    
    
}
