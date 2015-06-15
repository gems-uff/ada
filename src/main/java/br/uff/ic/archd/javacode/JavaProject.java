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
    private List<JavaClass> simpleSmartClasses;
    private List<JavaClass> fullSmartClasses;
    private List<JavaClass> foolClasses;
    private int numberOfClasses;
    private int numberOfInterfaces;
    private int numberOfViewExternalClasses;
    private String revisionId;

    JavaProject(String name) {
        this.name = name;
        packages = new HashMap();
        classes = new HashMap();
        externalClasses = new ArrayList();
        leaderClasses = new ArrayList();
        possibleLeaderClasses = new ArrayList();

        simpleSmartClasses = new ArrayList();
        fullSmartClasses = new ArrayList();
        foolClasses = new ArrayList();

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

    public JavaClass getClassById(long id) {
        JavaClass ja = null;
        for (JavaAbstract javaAbstract : getClasses()) {
            if (javaAbstract.getId() == id) {
                ja = (JavaClass) javaAbstract;
                break;
            }
        }
        return ja;
    }

    public JavaInterface getInterfaceById(long id) {
        JavaInterface ja = null;
        for (JavaAbstract javaAbstract : getInterfaces()) {
            if (javaAbstract.getId() == id) {
                ja = (JavaInterface) javaAbstract;
                break;
            }
        }
        return ja;
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

    public void addSimpleSmartClass(JavaClass javaClass) {
        simpleSmartClasses.add(javaClass);
    }

    public void addFullSmartClass(JavaClass javaClass) {
        fullSmartClasses.add(javaClass);
    }

    public void addFoolClass(JavaClass javaClass) {
        foolClasses.add(javaClass);
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

    public JavaAbstract getClassByOriginalSignature(String name) {
        JavaAbstract javaAbstract = null;

        for (JavaAbstract jaux : getClasses()) {
            if (jaux.getOriginalSignature().equals(name)) {
                javaAbstract = jaux;
                break;
            }
        }
        return javaAbstract;
    }

    public List<JavaAbstract> getClassByLastName(String name) {

        List<JavaAbstract> javaClasses = new LinkedList();
        Collection<JavaPackage> packageCollection = packages.values();
        Iterator it = packageCollection.iterator();
        while (it.hasNext()) {
            JavaPackage javaPackage = (JavaPackage) it.next();
            for (JavaAbstract javaAbstract : javaPackage.getClasses()) {
                if (javaAbstract.getClass() == JavaClass.class) {
                    if (((JavaClass) javaAbstract).getName().equals(name)) {
                        javaClasses.add(javaAbstract);
                    }
                }
            }
        }
        return javaClasses;
    }

    public List<JavaClass> getClassesThatCall(JavaAbstract javaAbstract) {
        List<JavaAbstract> list = getAllClasses();
        List<JavaClass> classesThatCall = new LinkedList();
        for (JavaAbstract javac : list) {
            if (javac.getClass() == JavaClass.class && javac != javaAbstract) {
                boolean call = false;
                for (JavaMethod javaMethod : ((JavaClass) javac).getMethods()) {
                    for (JavaMethodInvocation javaMethodInvocation : javaMethod.getMethodInvocations()) {
                        //System.gc();
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

    public void setChangingMethodsAndClasses() {
        System.out.println("********************* setchanging class");
        for (JavaAbstract javaAbstract : getClasses()) {
            //System.gc();
            JavaClass javaClass = (JavaClass) javaAbstract;
            for (JavaMethod javaMethod : javaClass.getMethods()) {
                
                List<JavaMethodInvocation> listMethodInvocation = javaMethod.getMethodInvocations();  
                //System.out.println(javaClass.getFullQualifiedName()+":"+javaMethod.getMethodSignature()+"   size: "+listMethodInvocation.size());
                for (JavaMethodInvocation javaMethodInvocation : listMethodInvocation) {
                    //System.gc();
                    if (javaMethodInvocation.getJavaMethod() != null) {
                        
                        javaMethodInvocation.getJavaMethod().addChangingMethod(javaMethod);
                        //System.out.println("------"+javaMethodInvocation.getJavaMethod().getJavaAbstract().getFullQualifiedName()+":"+javaMethodInvocation.getJavaMethod().getMethodSignature()+"    CM: "+javaMethodInvocation.getJavaMethod().getChangingMethodsMetric()+"   CC: "+javaMethodInvocation.getJavaMethod().getChangingClassesMetric()+
                        //        "     code: "+javaMethodInvocation.getJavaMethod()+"    "+(javaMethodInvocation.getJavaMethod().getJavaAbstract().getClass() == JavaClass.class ? "classe" : "interface"));
                    }
                }
                List<JavaMethodInvocation> listInternalMethodInvocation = javaMethod.getInternalMethodInvocations();
                for (JavaMethodInvocation javaInternalMethodThatCall : listInternalMethodInvocation) {
                    //System.gc();
                    if (javaInternalMethodThatCall.getJavaMethod() != null) {
                        javaInternalMethodThatCall.getJavaMethod().addInternalMethodThatCallMe(javaInternalMethodThatCall.getJavaMethod());
                    }
                }
            }
        }
    }

    public void setClassesMetrics() {
        for (JavaAbstract javaAbstract : getClasses()) {
            JavaClass javaClass = (JavaClass) javaAbstract;
            for (JavaMethod javaMethod : javaClass.getMethods()) {
                List<JavaMethodInvocation> listMethodInvocation = javaMethod.getMethodInvocations();
                for (JavaMethodInvocation javaMethodInvocation : listMethodInvocation) {
                    JavaAbstract ja1 = javaMethodInvocation.getJavaAbstract();
                    if (ja1.getClass() == JavaClass.class) {
                        JavaClass jc1 = (JavaClass) ja1;
                        //as classes pertencem a pacotes diferentes
                        if (!javaClass.getJavaPackage().getName().equals(jc1.getJavaPackage().getName())) {
                            jc1.addClientClass(javaClass);
                            jc1.addClientPackage(javaClass.getJavaPackage());
                            jc1.getJavaPackage().addClientClass(javaClass);
                            jc1.getJavaPackage().addClientPackage(javaClass.getJavaPackage());

                            javaClass.addExternalDependencyClass(jc1);
                            javaClass.addExternalDependencyPackage(jc1.getJavaPackage());
                        } else {
                            //as classes são do mesmo pacote

                            jc1.addIntraPackageDependentClass(javaClass);

                            javaClass.addInternalDependencyClass(jc1);
                        }

                    }
                }
                for (JavaExternalAttributeAccess javaExternalAttributeAccess : javaMethod.getJavaExternalAttributeAccessList()) {
                    JavaAbstract ja1 = javaExternalAttributeAccess.getJavaAbstract();
                    if (ja1.getClass() == JavaClass.class) {
                        JavaClass jc1 = (JavaClass) ja1;
                        if (!javaClass.getJavaPackage().getName().equals(jc1.getJavaPackage().getName())) {
                            jc1.addClientClass(javaClass);
                            jc1.addClientPackage(javaClass.getJavaPackage());
                            jc1.getJavaPackage().addClientClass(javaClass);
                            jc1.getJavaPackage().addClientPackage(javaClass.getJavaPackage());

                            javaClass.addExternalDependencyClass(jc1);
                            javaClass.addExternalDependencyPackage(jc1.getJavaPackage());
                        } else {
                            jc1.addIntraPackageDependentClass(javaClass);

                            javaClass.addInternalDependencyClass(jc1);
                        }

                    }
                }
                //verifica de quem JavaClass herda
                JavaClass jc1 = javaClass.getSuperClass();
                while (jc1 != null) {
                    if (!javaClass.getJavaPackage().getName().equals(jc1.getJavaPackage().getName())) {
                        jc1.addClientClass(javaClass);
                        jc1.addClientPackage(javaClass.getJavaPackage());
                        jc1.getJavaPackage().addClientClass(javaClass);
                        jc1.getJavaPackage().addClientPackage(javaClass.getJavaPackage());

                        javaClass.addExternalDependencyClass(jc1);
                        javaClass.addExternalDependencyPackage(jc1.getJavaPackage());
                    } else {
                        jc1.addIntraPackageDependentClass(javaClass);

                        javaClass.addInternalDependencyClass(jc1);
                    }
                    jc1 = jc1.getSuperClass();
                }
            }
        }
        for (JavaPackage javaPackage : getPackages()) {

            int numberOfPairs = 0;
            List<JavaClass> javaClasses = javaPackage.getOnlyClasses();
            if (javaClasses.size() > 1) {
                for (int i = 0; i < javaClasses.size(); i++) {
                    JavaClass jc1 = javaClasses.get(i);
                    for (int j = i + 1; j < javaClasses.size(); j++) {
                        JavaClass jc2 = javaClasses.get(j);
                        if (jc1.dependsOnClass(jc2) || jc2.dependsOnClass(jc1)) {
                            numberOfPairs++;
                        }
                    }
                }
                double packageCohesion = numberOfPairs;
                int n = javaClasses.size();
                packageCohesion = packageCohesion / ((n * (n - 1)) / 2);
                javaPackage.setPackageCohesion(packageCohesion);
            } else {
                javaPackage.setPackageCohesion(1);
            }
        }

        //verficar interfaces
        for (JavaAbstract javaAbstract : getClasses()) {
            JavaClass javaClass = (JavaClass) javaAbstract;
            for (JavaMethod javaMethod : javaClass.getMethods()) {
                List<JavaMethodInvocation> listMethodInvocation = javaMethod.getMethodInvocations();
                for (JavaMethodInvocation javaMethodInvocation : listMethodInvocation) {
                    JavaAbstract ja1 = javaMethodInvocation.getJavaAbstract();
                    if (ja1.getClass() == JavaInterface.class) {
                        JavaInterface ji1 = (JavaInterface) ja1;
                        //as classes pertencem a pacotes diferentes
                        for (JavaClass jc1 : ji1.getClassesThatImplements()) {
                            if (!javaClass.getJavaPackage().getName().equals(jc1.getJavaPackage().getName())) {
                                jc1.addClientClassViaInterface(javaClass);
                                jc1.addClientPackageViaInterface(javaClass.getJavaPackage());

                                //jc1.getJavaPackage().addClientClass(javaClass);
                                //jc1.getJavaPackage().addClientPackage(javaClass.getJavaPackage());
                                javaClass.addExternalDependencyClassViaInterface(jc1);
                                javaClass.addExternalDependencyPackageViaInterface(jc1.getJavaPackage());

                                javaClass.addExternalDependencyInterface(ji1);
                                javaClass.addExternalDependencyPackageInterface(ji1.getJavaPackage());
                            } else {
                                //as classes são do mesmo pacote

                                jc1.addIntraPackageDependentClassViaInterface(javaClass);

                                javaClass.addInternalDependencyClassViaInterface(jc1);

                                javaClass.addInternalDependencyInterface(ji1);
                            }
                        }

                    }
                }

                //verifica de quem JavaClass herda
                /*JavaClass jc1 = javaClass.getSuperClass();
                 while (jc1 != null) {
                 if (!javaClass.getJavaPackage().getName().equals(jc1.getJavaPackage().getName())) {
                 jc1.addClientClass(javaClass);
                 jc1.addClientPackage(javaClass.getJavaPackage());
                 jc1.getJavaPackage().addClientClass(javaClass);
                 jc1.getJavaPackage().addClientPackage(javaClass.getJavaPackage());

                 javaClass.addExternalDependencyClass(jc1);
                 javaClass.addExternalDependencyPackage(jc1.getJavaPackage());
                 } else {
                 jc1.addIntraPackageDependentClass(javaClass);

                 javaClass.addInternalDependencyClass(jc1);
                 }
                 jc1 = jc1.getSuperClass();
                 }*/
            }
        }

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

    /**
     * @return the simpleSmartClasses
     */
    public List<JavaClass> getSimpleSmartClasses() {
        return simpleSmartClasses;
    }

    /**
     * @return the fullSmartClasses
     */
    public List<JavaClass> getFullSmartClasses() {
        return fullSmartClasses;
    }

    /**
     * @return the foolClasses
     */
    public List<JavaClass> getFoolClasses() {
        return foolClasses;
    }

    /**
     * @return the revisionId
     */
    public String getRevisionId() {
        return revisionId;
    }

    /**
     * @param revisionId the revisionId to set
     */
    public void setRevisionId(String revisionId) {
        this.revisionId = revisionId;
    }
}
