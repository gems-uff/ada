/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.javacode;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author wallace
 */
public class JavaPackage {

    private String name;
    private List<JavaAbstract> classes;

    JavaPackage(String name) {
        this.name = name;
        classes = new LinkedList();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the classes
     */
    public List<JavaAbstract> getClasses() {
        return classes;
    }

    public void addJavaAbstract(JavaAbstract javaAbstract) {
        classes.add(javaAbstract);
    }

    public List<JavaPackage> getPackagesCall() {
        List<JavaPackage> packagesCall = new LinkedList();
        for (JavaAbstract javaAbstract : classes) {
            if (javaAbstract.getClass() == JavaClass.class) {
                for (JavaMethod javaMethod : ((JavaClass) javaAbstract).getMethods()) {
                    for (JavaMethodInvocation javaMethodInvocation : javaMethod.getMethodInvocations()) {
                        JavaAbstract javaAbtractMi = javaMethodInvocation.getJavaAbstract();
                        if(!packagesCall.contains(javaAbtractMi.getJavaPackage()) && !javaAbtractMi.getJavaPackage().getName().equals(this.getName())){
                            packagesCall.add(javaAbtractMi.getJavaPackage());
                        }
                    }
                }
            }
        }
        return packagesCall;
    }
    
    
    
}
