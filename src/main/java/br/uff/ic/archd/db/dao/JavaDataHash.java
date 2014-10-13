/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao;

import br.uff.ic.archd.javacode.JavaData;
import java.util.HashMap;

/**
 *
 * @author wallace
 */
public class JavaDataHash {
    private static JavaDataHash javaDataHash;
    private HashMap<String, JavaData> hashMap;
    
    public static JavaDataHash getInstance(){
        if(javaDataHash == null){
            javaDataHash = new JavaDataHash();
        }
        return javaDataHash;
    }
    
    private JavaDataHash(){
        hashMap = new HashMap();
    }
    
    public JavaData getJavaData(String javaDataSignature){
        JavaData javaData = hashMap.get(javaDataSignature);
        if(javaData == null){
            javaData  = new JavaData();
            javaData.setName(javaDataSignature);
            hashMap.put(javaDataSignature, javaData);
        }        
        return javaData;
    }
    
}
