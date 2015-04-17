/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.javacode;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author wallace
 */
public class JavaPackage {

    private String name;
    private List<JavaAbstract> classes;
    private HashMap<String, JavaClass> clientClasses;
    private HashMap<String, JavaPackage> clientPackages;
    //coesao entre as classes do acote, numero de dependencias entre as classes dividido por
    // o numero total de pares totais
    private double packageCohesion;

    public JavaPackage(String name) {
        this.name = name;
        classes = new LinkedList();
        clientClasses = new HashMap();
        clientPackages = new HashMap();
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

    public List<JavaClass> getOnlyClasses() {
        List<JavaClass> list = new LinkedList();
        for (JavaAbstract javaAbstract : classes) {
            if (javaAbstract.getClass() == JavaClass.class) {
                list.add((JavaClass) javaAbstract);
            }
        }
        return list;
    }

    public void addJavaAbstract(JavaAbstract javaAbstract) {
        classes.add(javaAbstract);
    }

    public void addClientClass(JavaClass javaClass) {
        clientClasses.put(javaClass.getFullQualifiedName(), javaClass);
    }

    public void addClientPackage(JavaPackage javaPackage) {
        clientPackages.put(javaPackage.getName(), javaPackage);
    }

    public List<JavaPackage> getPackagesCall() {
        List<JavaPackage> packagesCall = new LinkedList();
        for (JavaAbstract javaAbstract : classes) {
            if (javaAbstract.getClass() == JavaClass.class) {
                for (JavaMethod javaMethod : ((JavaClass) javaAbstract).getMethods()) {
                    for (JavaMethodInvocation javaMethodInvocation : javaMethod.getMethodInvocations()) {
                        JavaAbstract javaAbtractMi = javaMethodInvocation.getJavaAbstract();
                        if (!packagesCall.contains(javaAbtractMi.getJavaPackage()) && !javaAbtractMi.getJavaPackage().getName().equals(this.getName())) {
                            packagesCall.add(javaAbtractMi.getJavaPackage());
                        }
                    }
                }
            }
        }
        return packagesCall;
    }

    /**
     * @return the clientClasses
     */
    public List<JavaClass> getClientClasses() {
        List<JavaClass> javaClasses = new LinkedList();
        Collection<JavaClass> classCollection = clientClasses.values();
        Iterator it = classCollection.iterator();
        while (it.hasNext()) {
            JavaClass javaClass = (JavaClass) it.next();
            javaClasses.add(javaClass);
        }
        return javaClasses;
    }

    /**
     * @return the clientPackages
     */
    public List<JavaPackage> getClientPackages() {
        List<JavaPackage> javaPackages = new LinkedList();
        Collection<JavaPackage> packageCollection = clientPackages.values();
        Iterator it = packageCollection.iterator();
        while (it.hasNext()) {
            JavaPackage javaPackage = (JavaPackage) it.next();
            javaPackages.add(javaPackage);
        }
        return javaPackages;
    }

    /**
     * @return the packageCohesion
     */
    public double getPackageCohesion() {
        return packageCohesion;
    }

    /**
     * @param packageCohesion the packageCohesion to set
     */
    public void setPackageCohesion(double packageCohesion) {
        this.packageCohesion = packageCohesion;
    }
}
