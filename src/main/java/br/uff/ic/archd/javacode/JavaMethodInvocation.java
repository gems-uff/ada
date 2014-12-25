/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.javacode;

/**
 *
 * @author wallace
 */
public class JavaMethodInvocation {
    private JavaAbstract javaAbstract;
    private JavaMethod javaMethod;
    private String unknowMethodName;
    
    public JavaMethodInvocation(JavaAbstract javaAbstract , JavaMethod javaMethod){
        this.javaAbstract = javaAbstract;
        this.javaMethod = javaMethod;
    }

    /**
     * @return the javaAbstract
     */
    public JavaAbstract getJavaAbstract() {
        return javaAbstract;
    }

    /**
     * @return the javaMethod
     */
    public JavaMethod getJavaMethod() {
        return javaMethod;
    }

    /**
     * @return the unknowMethodName
     */
    public String getUnknowMethodName() {
        return unknowMethodName;
    }

    /**
     * @param unknowMethodName the unknowMethodName to set
     */
    public void setUnknowMethodName(String unknowMethodName) {
        this.unknowMethodName = unknowMethodName;
    }
    
    public String getMethodName(){
        if(javaMethod != null){
            return javaMethod.getMethodSignature();
        }else{
            return unknowMethodName;
        }
    }
}
