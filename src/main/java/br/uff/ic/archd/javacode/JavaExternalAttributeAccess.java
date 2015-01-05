/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.javacode;

/**
 *
 * @author wallace
 */
public class JavaExternalAttributeAccess {
    
    private JavaAbstract javaAbstract;
    private String attributeName;
    
    public JavaExternalAttributeAccess(JavaAbstract javaAbstract, String attributeName){
        this.javaAbstract = javaAbstract;
        this.attributeName = attributeName;
    }


    /**
     * @return the javaAbstract
     */
    public JavaAbstract getJavaAbstract() {
        return javaAbstract;
    }

    /**
     * @return the attributeName
     */
    public String getAttributeName() {
        return attributeName;
    }

}
