/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.javacode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author wallace
 */
public class JavaProject {

    private String name;
    private HashMap<String, JavaPackage> packages;
    private HashMap<String, JavaAbstract> classes;
    private List<JavaAbstractExternal> externalClasses;
    private List<JavaAbstract> leaderClasses;
    private List<JavaAbstract> possibleLeaderClasses;
    private int numberOfClasses;
    private int numberOfInterfaces;
    private int numberOfViewExternalClasses;

    JavaProject(String name) {
        this.name = name;
        packages = new HashMap();
        classes = new HashMap();
        externalClasses = new ArrayList();
        leaderClasses = new ArrayList();
        possibleLeaderClasses = new ArrayList();
        numberOfClasses = 0;
        numberOfInterfaces = 0;
        numberOfViewExternalClasses = 0;
    }

    /**
     * @return the packages
     */
    public List<JavaPackage> getPackages() {
        return new LinkedList<JavaPackage>(packages.values());
        //return packages;
    }

    public List<JavaAbstract> getAllClasses() {
        List<JavaAbstract> javaClasses = new LinkedList();
        Collection<JavaPackage> packageCollection = packages.values();
        Iterator it = packageCollection.iterator();
        while (it.hasNext()) {
            JavaPackage javaPackage = (JavaPackage) it.next();
            javaClasses.addAll(javaPackage.getClasses());
        }
        return javaClasses;
    }

    public List<JavaAbstract> getClasses() {
        List<JavaAbstract> javaClasses = new LinkedList();
        Collection<JavaPackage> packageCollection = packages.values();
        Iterator it = packageCollection.iterator();
        while (it.hasNext()) {
            JavaPackage javaPackage = (JavaPackage) it.next();
            for (JavaAbstract javaAbstract : javaPackage.getClasses()) {
                if (javaAbstract.getClass() == JavaClass.class) {
                    javaClasses.add(javaAbstract);
                }
            }
        }
        return javaClasses;
    }

    public List<JavaAbstract> getInterfaces() {
        List<JavaAbstract> javaClasses = new LinkedList();
        Collection<JavaPackage> packageCollection = packages.values();
        Iterator it = packageCollection.iterator();
        while (it.hasNext()) {
            JavaPackage javaPackage = (JavaPackage) it.next();
            for (JavaAbstract javaAbstract : javaPackage.getClasses()) {
                if (javaAbstract.getClass() == JavaInterface.class) {
                    javaClasses.add(javaAbstract);
                }
            }
        }
        return javaClasses;
    }

    public void addPackage(JavaPackage javaPackage) {
        packages.put(javaPackage.getName(), javaPackage);
    }

    public void addClass(JavaAbstract javaAbstract) {
        classes.put(javaAbstract.getFullQualifiedName(), javaAbstract);
    }

    public JavaPackage getPackageByName(String name) {
        return packages.get(name);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    public List<JavaAbstract> getPackagesByName(String className) {
        List<JavaAbstract> packagesImport = new LinkedList();
        JavaAbstract javaAbstract = classes.get(className);
        if (javaAbstract == null) {
            JavaPackage javaPackage = packages.get(className);
            if (javaPackage != null) {
                packagesImport = javaPackage.getClasses();
            }

        } else {
            packagesImport.add(javaAbstract);
        }
        return packagesImport;
    }

    public void addExternalClass(JavaAbstractExternal externalClass) {
        externalClasses.add(externalClass);
        numberOfViewExternalClasses++;
    }

    public JavaAbstractExternal getJavaExternalClassByName(String externalClassName) {
        JavaAbstractExternal javaAbstractExternal = null;
        for (JavaAbstractExternal javaExternalClass : externalClasses) {
            if (javaExternalClass.getName().equals(externalClassName)) {
                javaAbstractExternal = javaExternalClass;
                break;
            }
        }
        return javaAbstractExternal;
    }

    public JavaAbstract getClassByName(String name) {
        return classes.get(name);
    }

    public List<JavaClass> getClassesThatCall(JavaAbstract javaAbstract) {
        List<JavaAbstract> list = getAllClasses();
        List<JavaClass> classesThatCall = new LinkedList();
        for (JavaAbstract javac : list) {
            if (javac.getClass() == JavaClass.class && javac != javaAbstract) {
                boolean call = false;
                for (JavaMethod javaMethod : ((JavaClass) javac).getMethods()) {
                    for (JavaMethodInvocation javaMethodInvocation : javaMethod.getMethodInvocations()) {
                        JavaAbstract javaAbtractMi = javaMethodInvocation.getJavaAbstract();
                        if (javaAbtractMi.getFullQualifiedName().equals(javaAbstract.getFullQualifiedName())) {
                            call = true;
                            break;
                        }
                    }
                    if (call) {
                        break;
                    }
                }
                if (call) {
                    classesThatCall.add((JavaClass) javac);
                }
            }
        }
        return classesThatCall;
    }

    public void addLeaderClass(JavaAbstract javaClass) {
        getLeaderClasses().add(javaClass);
    }

    public void addPossibleLeaderClass(JavaAbstract javaClass) {
        getPossibleLeaderClasses().add(javaClass);
    }

    public List<JavaAbstract> getClassesThatUsing(JavaAbstract javaAbstract) {
        List<JavaAbstract> list = getAllClasses();
        List<JavaAbstract> classesThatUsing = new LinkedList();
        for (JavaAbstract javac : list) {
            boolean using = false;
            if (javac.getClass() == JavaClass.class && javac != javaAbstract) {


                if (!using) {
                    for (JavaAttribute javaAttribute : ((JavaClass) javac).getAttributes()) {
                        if (javaAttribute.getType().getClass() == JavaClass.class || javaAttribute.getType().getClass() == JavaInterface.class) {
                            JavaAbstract javaAbs = (JavaAbstract) javaAttribute.getType();
                            if (javaAbs.getFullQualifiedName().equals(javaAbstract.getFullQualifiedName())) {
                                using = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (!using) {
                List<JavaMethod> methods = new ArrayList();
                if (javac.getClass() == JavaClass.class) {
                    methods = ((JavaClass) javac).getMethods();
                } else if (javac.getClass() == JavaInterface.class) {
                    methods = ((JavaInterface) javac).getMethods();
                }
                for (JavaMethod javaMethod : methods) {
                    for (Parameter parameter : javaMethod.getParameters()) {
                        if (parameter.getType().getClass() == JavaClass.class || parameter.getType().getClass() == JavaInterface.class) {
                            JavaAbstract javaAbs = (JavaAbstract) parameter.getType();
                            if (javaAbs.getFullQualifiedName().equals(javaAbstract.getFullQualifiedName())) {
                                using = true;
                                break;
                            }
                        }
                    }
                    if (using) {
                        break;
                    }
                }
            }
            if (using) {
                classesThatUsing.add(javac);
            }
        }
        return classesThatUsing;
    }

    public List<JavaPackage> getPackagesThatCall(JavaPackage javaPackage) {
        List<JavaPackage> packagesThatCall = new ArrayList();
        for (JavaPackage javap : getPackages()) {
            if (javap != javaPackage) {
                boolean call = false;
                for (JavaPackage packageCall : javap.getPackagesCall()) {
                    if (packageCall == javaPackage) {
                        call = true;
                        break;
                    }
                }
                if (call) {
                    packagesThatCall.add(javap);
                }
            }
        }
        return packagesThatCall;
    }

    /*public List<JavaPackage> getPackagesThatUsing(JavaPackage javaPackage) {
     List<JavaPackage> packagesThatCall = new ArrayList();
     for (JavaPackage javap : getPackages()) {
     if (javap != javaPackage) {
     boolean call = false;
     for (JavaPackage packageCall : javap.getPackagesCall()) {
     if (packageCall == javaPackage) {
     call = true;
     break;
     }
     }
     if (call) {
     packagesThatCall.add(javap);
     }
     }
     }
     return packagesThatCall;
     }*/
    /**
     * @return the leaderClasses
     */
    public List<JavaAbstract> getLeaderClasses() {
        return leaderClasses;
    }

    /**
     * @return the possibleLeaderClasses
     */
    public List<JavaAbstract> getPossibleLeaderClasses() {
        return possibleLeaderClasses;
    }

    /**
     * @return the numberOfClasses
     */
    public int getNumberOfClasses() {
        return numberOfClasses;
    }

    /**
     * @param numberOfClasses the numberOfClasses to set
     */
    public void setNumberOfClasses(int numberOfClasses) {
        this.numberOfClasses = numberOfClasses;
    }

    /**
     * @return the numberOfInterfaces
     */
    public int getNumberOfInterfaces() {
        return numberOfInterfaces;
    }

    /**
     * @param numberOfInterfaces the numberOfInterfaces to set
     */
    public void setNumberOfInterfaces(int numberOfInterfaces) {
        this.numberOfInterfaces = numberOfInterfaces;
    }

    /**
     * @return the numberOfViewExternalClasses
     */
    public int getNumberOfViewExternalClasses() {
        return numberOfViewExternalClasses;
    }
}
