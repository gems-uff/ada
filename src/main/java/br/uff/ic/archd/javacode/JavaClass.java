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
public class JavaClass extends JavaAbstract {

    private List<JavaInterface> implementedInterfaces;
    private List<JavaAttribute> attributes;
    private List<JavaMethod> methods;
    //classes de outros pacotes que dependem de mim
    private HashMap<String, JavaClass> clientClasses;
    //pacotes que dependem de mim
    private HashMap<String, JavaPackage> clientPackages;
    //classes do mesmo pacote que dependem de mim
    private HashMap<String, JavaClass> intraPackageDependentClass;
    //classes do mesmo pacote das quais essa classe depende
    private HashMap<String, JavaClass> internalDependencyClasses;
    //classes de outros pacotes das quais essa classe depende
    private HashMap<String, JavaClass> externalDependencyClasses;
    //pacotes os quais essa classe depende
    private HashMap<String, JavaPackage> externalDependencyPackages;

    //classes de outros pacotes que dependem de mim via interface
    private HashMap<String, JavaClass> clientClassesViaInterface;
    //pacotes que dependem de mim via interface
    private HashMap<String, JavaPackage> clientPackagesViaInterface;
    //classes do mesmo pacote que dependem de mim via interface
    private HashMap<String, JavaClass> intraPackageDependentClassViaInterface;
    //classes do mesmo pacote das quais essa classe depende via interface
    private HashMap<String, JavaClass> internalDependencyClassesViaInterface;
    //classes de outros pacotes das quais essa classe depende via interface
    private HashMap<String, JavaClass> externalDependencyClassesViaInterface;
    //pacotes os quais essa classe depende via classes filhos de interface
    private HashMap<String, JavaPackage> externalDependencyPackagesViaInterface;


    //interfaces do mesmo pacote das quais essa classe depende
    private HashMap<String, JavaInterface> internalDependencyInterfaces;
    //interface de outros pacotes das quais essa classe depende
    private HashMap<String, JavaInterface> externalDependencyInterfaces;
    //pacotes os quais essa classe depende via interface
    private HashMap<String, JavaPackage> externalDependencyPackagesInterface;
    
    private JavaClass superClass;
    private int totalCyclomaticComplexity;
    //numero de dados externos a classe que esta classe acessa, diretamente ou via metodos acessores
    private int accessToForeignDataNumber;
    private int numberOfDirectConnections;

    public JavaClass(String path) {
        super(path);
        implementedInterfaces = new ArrayList();
        attributes = new ArrayList();
        superClass = null;
        methods = new ArrayList();
        clientClasses = new HashMap();
        clientPackages = new HashMap();
        intraPackageDependentClass = new HashMap();
        
        internalDependencyClasses = new HashMap();
        externalDependencyClasses = new HashMap();
        externalDependencyPackages = new HashMap();
        
        clientClassesViaInterface = new HashMap();
        clientPackagesViaInterface = new HashMap();
        intraPackageDependentClassViaInterface = new HashMap();
        internalDependencyClassesViaInterface = new HashMap();
        externalDependencyClassesViaInterface = new HashMap();
        externalDependencyPackagesViaInterface = new HashMap();
        internalDependencyInterfaces = new HashMap();
        externalDependencyInterfaces = new HashMap();
        externalDependencyPackagesInterface = new HashMap();
        
        totalCyclomaticComplexity = 0;
        accessToForeignDataNumber = 0;
    }
    
    public JavaClass(){
        super();
        implementedInterfaces = new ArrayList();
        attributes = new ArrayList();
        superClass = null;
        methods = new ArrayList();
        clientClasses = new HashMap();
        clientPackages = new HashMap();
        intraPackageDependentClass = new HashMap();
        
        internalDependencyClasses = new HashMap();
        externalDependencyClasses = new HashMap();
        externalDependencyPackages = new HashMap();
        
        
        clientClassesViaInterface = new HashMap();
        clientPackagesViaInterface = new HashMap();
        intraPackageDependentClassViaInterface = new HashMap();
        internalDependencyClassesViaInterface = new HashMap();
        externalDependencyClassesViaInterface = new HashMap();
        externalDependencyPackagesViaInterface = new HashMap();
        internalDependencyInterfaces = new HashMap();
        externalDependencyInterfaces = new HashMap();
        externalDependencyPackagesInterface = new HashMap();
        
        totalCyclomaticComplexity = 0;
        accessToForeignDataNumber = 0;
    }

    public int getSize() {
        //int size = 0;
        //for(JavaAttribute javaAttribute : attributes){
        //if(javaAttribute.getType().getClass() == JavaAbstractExternal.class || javaAttribute.getType().getClass() == JavaPrimitiveType.class){
        //      size++;
        //}
        //}

        return getAttributes().size();
    }

    /**
     * @return the implementedInterfaces
     */
    public List<JavaInterface> getImplementedInterfaces() {
        return implementedInterfaces;
    }


    /**
     * @return the attributes
     */
    public List<JavaAttribute> getAttributes() {
        return attributes;
    }

    /**
     * @return the methods
     */
    public List<JavaMethod> getMethods() {
        return methods;
    }
    
    public List<JavaMethod> getMethodsByName(String name) {
        List<JavaMethod> m = new LinkedList();
        for(JavaMethod javaMethod : methods){
            if(javaMethod.getName().equals(name)){
                m.add(javaMethod);
            }
        }
        return m;
    }



    /**
     * @return the superClass
     */
    public JavaClass getSuperClass() {
        return superClass;
    }

    /**
     * @param superClass the superClass to set
     */
    public void setSuperClass(JavaClass superClass) {
        this.superClass = superClass;
    }

    public void addImplementedInterface(JavaInterface javaInterface) {
        implementedInterfaces.add(javaInterface);
    }

    public void addAttribute(JavaAttribute javaAttribute) {
        getAttributes().add(javaAttribute);
    }
    
    public void addClientClass(JavaClass javaClass){
        clientClasses.put(javaClass.getFullQualifiedName(), javaClass);
    }
    
    public void addClientPackage(JavaPackage javaPackage) {
        clientPackages.put(javaPackage.getName(), javaPackage);
    }
    
    public void addClientClassViaInterface(JavaClass javaClass) {
        clientClassesViaInterface.put(javaClass.getFullQualifiedName(),  javaClass);
    }
    
    public void addClientPackageViaInterface(JavaPackage javaPackage) {
        clientPackagesViaInterface.put(javaPackage.getName(),  javaPackage);
    }
    
    public void addIntraPackageDependentClassViaInterface(JavaClass javaClass) {
        intraPackageDependentClassViaInterface.put(javaClass.getFullQualifiedName(),  javaClass);
    }
    
    public void addInternalDependencyClassViaInterface(JavaClass javaClass) {
        internalDependencyClassesViaInterface.put(javaClass.getFullQualifiedName(),  javaClass);
    }
    
    public void addExternalDependencyClassViaInterface(JavaClass javaClass) {
        externalDependencyClassesViaInterface.put(javaClass.getFullQualifiedName(),  javaClass);
    }
    
    public void addExternalDependencyPackageViaInterface(JavaPackage javaPackage) {
        externalDependencyPackagesViaInterface.put(javaPackage.getName(),  javaPackage);
    }
    
    public void addInternalDependencyInterface(JavaInterface javaInterface) {
        internalDependencyInterfaces.put(javaInterface.getFullQualifiedName(),  javaInterface);
    }
    
    public void addExternalDependencyInterface(JavaInterface javaInterface) {
        externalDependencyInterfaces.put(javaInterface.getFullQualifiedName(),  javaInterface);
    }
    
    public void addExternalDependencyPackageInterface(JavaPackage javaPackage) {
        externalDependencyPackagesInterface.put(javaPackage.getName(),  javaPackage);
    }
    
    
    
    
    
    
    public void addIntraPackageDependentClass(JavaClass javaClass){
        intraPackageDependentClass.put(javaClass.getFullQualifiedName(), javaClass);
    }
    
    public void addInternalDependencyClass(JavaClass javaClass){
        internalDependencyClasses.put(javaClass.getFullQualifiedName(), javaClass);
    }
    
    public void addExternalDependencyClass(JavaClass javaClass){
        externalDependencyClasses.put(javaClass.getFullQualifiedName(), javaClass);
    }
    
    public void addExternalDependencyPackage(JavaPackage javaPackage) {
        externalDependencyPackages.put(javaPackage.getName(), javaPackage);
    }

    public JavaData getJavaTypeByVariableName(String name) {
        JavaData javaData = null;
        for (JavaAttribute javaAttribute : getAttributes()) {
            if (javaAttribute.getName().equals(name)) {
                javaData = javaAttribute.getType();
                break;
            }
        }
        if (javaData == null && superClass != null) {
            javaData = superClass.getJavaTypeByVariableName(name);
        }

        if (javaData == null) {

            for (JavaAbstract javaImport : getClassesImports()) {
//                if(this.getFullQualifiedName().equals("org.gjt.sp.util.IOUtilities")){
//                    System.out.println("Imports: "+javaImport.getName());
//                }
                if (javaImport.getFullQualifiedName().endsWith("." + name)) {
                    javaData = javaImport;
                    break;
                }
            }


        }

        if (javaData == null) {
            for (JavaAbstract javaImport : getJavaPackage().getClasses()) {
//                if(this.getFullQualifiedName().equals("org.gjt.sp.util.IOUtilities")){
//                    System.out.println("Imports: "+javaImport.getName());
//                }
                if (javaImport.getFullQualifiedName().endsWith("." + name)) {
                    javaData = javaImport;
                    break;
                }
            }
        }

        return javaData;

    }

    public JavaMethod getJavaMethod(String name, int numberOfParams, List<JavaData> arguments) {
        JavaMethod javaMethodCall = null;
        List<JavaMethod> possibleMethods = new ArrayList();
        for (JavaMethod javaMethod : methods) {
            if (javaMethod.getName().equals(name)
                    && javaMethod.getParameters().size() == numberOfParams) {
                possibleMethods.add(javaMethod);
            }
        }
        if(possibleMethods.size() == 1){
            javaMethodCall = possibleMethods.get(0);
            
        }else if(possibleMethods.size() > 1){
            javaMethodCall = possibleMethods.get(0);
            for (int i = 1; i < possibleMethods.size(); i++) {
                JavaMethod possibleMethod = possibleMethods.get(i);
                boolean isTheMethod = true;
                for (int j = 0; j < possibleMethod.getParameters().size(); j++) {
                    Parameter parameter = possibleMethod.getParameters().get(j);
                    JavaData argument = arguments.get(j);
                    if (argument != null) {
                        if (!parameter.getType().getName().equals(argument.getName())) {
                            if (argument.getClass() != JavaNull.class) {
                                if (argument.getClass() == JavaPrimitiveType.class && parameter.getType().getClass() == JavaPrimitiveType.class) {
                                    if (((JavaPrimitiveType) parameter.getType()).getType() == JavaPrimitiveType.DOUBLE
                                                && (((JavaPrimitiveType) argument).getType() == JavaPrimitiveType.FLOAT
                                                || ((JavaPrimitiveType) argument).getType() == JavaPrimitiveType.INT
                                                || ((JavaPrimitiveType) argument).getType() == JavaPrimitiveType.LONG)) {
                                    } else if (((JavaPrimitiveType) parameter.getType()).getType() == JavaPrimitiveType.FLOAT
                                                && (((JavaPrimitiveType) argument).getType() == JavaPrimitiveType.INT
                                                || ((JavaPrimitiveType) argument).getType() == JavaPrimitiveType.LONG)) {
                                    } else if (((JavaPrimitiveType) parameter.getType()).getType() == JavaPrimitiveType.LONG
                                                && (((JavaPrimitiveType) argument).getType() == JavaPrimitiveType.INT)) {
                                    } else {
                                        isTheMethod = false;
                                        break;
                                    }
                                }else{
                                    isTheMethod = false;
                                    break;
                                }
                            } else {
                                if (parameter.getType().getClass() == JavaPrimitiveType.class) {
                                    isTheMethod = false;
                                    break;
                                }
                            }
                        }
                    }else{
                        isTheMethod = false;
                        break;
                    }
                }
                if(isTheMethod){
                    javaMethodCall = possibleMethod;
                }
            }
        }
        
        if (javaMethodCall == null && superClass != null) {
            javaMethodCall = superClass.getJavaMethod(name, numberOfParams, arguments);
        }

        return javaMethodCall;
    }

    public void addMethod(JavaMethod javaMethod) {
        methods.add(javaMethod);
        javaMethod.setInternalID(methods.size());
        totalCyclomaticComplexity = totalCyclomaticComplexity + javaMethod.getCyclomaticComplexity();
    }
    
    public JavaMethod getMethodByInternalId(int internalId){
        JavaMethod javaMethod = null;
        for(JavaMethod aux : methods){
            if(aux.getInternalID() == internalId){
                javaMethod = aux;
                break;
            }
        }
        return javaMethod;
    }
    
    public JavaMethod getMethodById(long id){
        JavaMethod javaMethod = null;
        for(JavaMethod aux : methods){
            if(aux.getId() == id){
                javaMethod = aux;
                break;
            }
        }
        return javaMethod;
    }
    
    public JavaMethod getMethodBySignature(String signature){
        JavaMethod javaMethod = null;
        for(JavaMethod aux : methods){
            if(aux.getMethodSignature().equals(signature)){
                javaMethod = aux;
                break;
            }
        }
        return javaMethod;   
    }
    


    /**
     * @return the totalCyclomaticComplexity
     */
    public int getTotalCyclomaticComplexity() {
        return totalCyclomaticComplexity;
    }

    /**
     * @return the accessToForeignDataNumber
     */
    public int getAccessToForeignDataNumber() {
        return accessToForeignDataNumber;
    }

    /**
     * @param accessToForeignDataNumber the accessToForeignDataNumber to set
     */
    public void setAccessToForeignDataNumber(int accessToForeignDataNumber) {
        this.accessToForeignDataNumber = accessToForeignDataNumber;
    }

    /**
     * @return the numberOfDirectConnections
     */
    public int getNumberOfDirectConnections() {
        return numberOfDirectConnections;
    }

    /**
     * @param numberOfDirectConnections the numberOfDirectConnections to set
     */
    public void setNumberOfDirectConnections(int numberOfDirectConnections) {
        this.numberOfDirectConnections = numberOfDirectConnections;
    }
    
    public boolean isInheritedClass(JavaClass auxJavaClass){
        boolean isInheritedClass = false;
        JavaClass aux = this.getSuperClass();
        while(aux != null){
            if(aux.getFullQualifiedName().equals(auxJavaClass.getFullQualifiedName())){
                isInheritedClass = true;
                break;
            }
            aux = aux.getSuperClass();
        }
        return isInheritedClass;
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
        
    
    public List<JavaClass> getintraPackageDependentClass() {
        List<JavaClass> javaClasses = new LinkedList();
        Collection<JavaClass> classCollection = intraPackageDependentClass.values();
        Iterator it = classCollection.iterator();
        while (it.hasNext()) {
            JavaClass javaClass = (JavaClass) it.next();
            javaClasses.add(javaClass);
        }
        return javaClasses;
    }
    
    public boolean dependsOnClass(JavaClass javaClass){
        boolean dependsOnClass = false;
        if(intraPackageDependentClass.containsKey(javaClass.getFullQualifiedName())){
            dependsOnClass = true;
        }
        return dependsOnClass;
    }

    /**
     * @return the internalDependencyClasses
     */
    public List<JavaClass> getInternalDependencyClasses() {
        List<JavaClass> javaClasses = new LinkedList();
        Collection<JavaClass> classCollection = internalDependencyClasses.values();
        Iterator it = classCollection.iterator();
        while (it.hasNext()) {
            JavaClass javaClass = (JavaClass) it.next();
            javaClasses.add(javaClass);
        }
        return javaClasses;
    }

    /**
     * @return the externalDependencyClasses
     */
    public List<JavaClass> getExternalDependencyClasses() {
        List<JavaClass> javaClasses = new LinkedList();
        Collection<JavaClass> classCollection = externalDependencyClasses.values();
        Iterator it = classCollection.iterator();
        while (it.hasNext()) {
            JavaClass javaClass = (JavaClass) it.next();
            javaClasses.add(javaClass);
        }
        return javaClasses;
    }

    /**
     * @return the externalDependencyPackages
     */
    public List<JavaPackage> getExternalDependencyPackages() {
        List<JavaPackage> javaPackages = new LinkedList();
        Collection<JavaPackage> packageCollection = externalDependencyPackages.values();
        Iterator it = packageCollection.iterator();
        while (it.hasNext()) {
            JavaPackage javaPackage = (JavaPackage) it.next();
            javaPackages.add(javaPackage);
        }
        return javaPackages;
    }

    /**
     * @return the clientClassesViaInterface
     */
    public List<JavaClass> getClientClassesViaInterface() {
        List<JavaClass> javaClasses = new LinkedList();
        Collection<JavaClass> classCollection = clientClassesViaInterface.values();
        Iterator it = classCollection.iterator();
        while (it.hasNext()) {
            JavaClass javaClass = (JavaClass) it.next();
            javaClasses.add(javaClass);
        }
        return javaClasses;
    }

    /**
     * @return the clientPackagesViaInterface
     */
    public List<JavaPackage> getClientPackagesViaInterface() {
        List<JavaPackage> javaPackages = new LinkedList();
        Collection<JavaPackage> packageCollection = clientPackagesViaInterface.values();
        Iterator it = packageCollection.iterator();
        while (it.hasNext()) {
            JavaPackage javaPackage = (JavaPackage) it.next();
            javaPackages.add(javaPackage);
        }
        return javaPackages;
    }

    /**
     * @return the intraPackageDependentClassViaInterface
     */
    public List<JavaClass> getIntraPackageDependentClassViaInterface() {
        List<JavaClass> javaClasses = new LinkedList();
        Collection<JavaClass> classCollection = intraPackageDependentClassViaInterface.values();
        Iterator it = classCollection.iterator();
        while (it.hasNext()) {
            JavaClass javaClass = (JavaClass) it.next();
            javaClasses.add(javaClass);
        }
        return javaClasses;
    }

    /**
     * @return the internalDependencyClassesViaInterface
     */
    public List<JavaClass> getInternalDependencyClassesViaInterface() {
        List<JavaClass> javaClasses = new LinkedList();
        Collection<JavaClass> classCollection = internalDependencyClassesViaInterface.values();
        Iterator it = classCollection.iterator();
        while (it.hasNext()) {
            JavaClass javaClass = (JavaClass) it.next();
            javaClasses.add(javaClass);
        }
        return javaClasses;
    }

    /**
     * @return the externalDependencyClassesViaInterface
     */
    public List<JavaClass> getExternalDependencyClassesViaInterface() {
        List<JavaClass> javaClasses = new LinkedList();
        Collection<JavaClass> classCollection = externalDependencyClassesViaInterface.values();
        Iterator it = classCollection.iterator();
        while (it.hasNext()) {
            JavaClass javaClass = (JavaClass) it.next();
            javaClasses.add(javaClass);
        }
        return javaClasses;
    }

    /**
     * @return the externalDependencyPackagesViaInterface
     */
    public List<JavaPackage> getExternalDependencyPackagesViaInterface() {
        List<JavaPackage> javaPackages = new LinkedList();
        Collection<JavaPackage> packageCollection = externalDependencyPackagesViaInterface.values();
        Iterator it = packageCollection.iterator();
        while (it.hasNext()) {
            JavaPackage javaPackage = (JavaPackage) it.next();
            javaPackages.add(javaPackage);
        }
        return javaPackages;
    }

    /**
     * @return the internalDependencyInterfaces
     */
    public List<JavaInterface> getInternalDependencyInterfaces() {
        List<JavaInterface> javaInterfaces = new LinkedList();
        Collection<JavaInterface> interfaceCollection = internalDependencyInterfaces.values();
        Iterator it = interfaceCollection.iterator();
        while (it.hasNext()) {
            JavaInterface Javainterface= (JavaInterface) it.next();
            javaInterfaces.add(Javainterface);
        }
        return javaInterfaces;
    }

    /**
     * @return the externalDependencyInterfaces
     */
    public List<JavaInterface> getExternalDependencyInterfaces() {
        List<JavaInterface> javaInterfaces = new LinkedList();
        Collection<JavaInterface> interfaceCollection = externalDependencyInterfaces.values();
        Iterator it = interfaceCollection.iterator();
        while (it.hasNext()) {
            JavaInterface Javainterface= (JavaInterface) it.next();
            javaInterfaces.add(Javainterface);
        }
        return javaInterfaces;
    }

    /**
     * @return the externalDependencyPackagesInterface
     */
    public List<JavaPackage> getExternalDependencyPackagesInterface() {
        List<JavaPackage> javaPackages = new LinkedList();
        Collection<JavaPackage> packageCollection = externalDependencyPackagesInterface.values();
        Iterator it = packageCollection.iterator();
        while (it.hasNext()) {
            JavaPackage javaPackage = (JavaPackage) it.next();
            javaPackages.add(javaPackage);
        }
        return javaPackages;
    }
}
