/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.ast.service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wallace
 */
public class MethodType {
    private String methodSignature;
    private String methodName;
    private List<String> argumentsTypes;
    private String returnType;
    private String oficialMethodSignature;
    public MethodType(String methodName, String returnType, List<String> argumentsTypes){
        this.methodName = methodName;
        this.returnType = returnType;
        this.argumentsTypes = new ArrayList();
        oficialMethodSignature= methodName+"(";
        if(argumentsTypes.isEmpty()){
            oficialMethodSignature = oficialMethodSignature+")";
        }
        for(String argumentType : argumentsTypes){
            this.argumentsTypes.add(argumentType);
            oficialMethodSignature = oficialMethodSignature + argumentType + ",";
        }
        methodSignature = methodName+":"+argumentsTypes.size();
        oficialMethodSignature = oficialMethodSignature.substring(0, oficialMethodSignature.length() - 1) + ")";
        
    }
    
    public boolean matchTypes(List<String> argumentsTypes){
        boolean match = true;
        for(int i=0; i < argumentsTypes.size(); i++){
            String argumentIn = this.argumentsTypes.get(i);
            String argumentOut = argumentsTypes.get(i);
            if(!argumentIn.equals(argumentOut)){
                match = false;
                break;
            }
        }
        
        return match;
    }

    /**
     * @return the methodSignature
     */
    public String getMethodSignature() {
        return methodSignature;
    }
    
    public String getOficialMethodSignature() {
        return oficialMethodSignature;
    }

    /**
     * @return the methodName
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * @return the returnType
     */
    public String getReturnType() {
        return returnType;
    }
    
    
    
    
}
