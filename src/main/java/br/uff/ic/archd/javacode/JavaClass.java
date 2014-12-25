/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.javacode;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wallace
 */
public class JavaClass extends JavaAbstract {

    private List<JavaInterface> implementedInterfaces;
    private List<JavaAttribute> attributes;
    private List<JavaMethod> methods;
    private JavaClass superClass;
    private int totalCyclomaticComplexity;

    public JavaClass(String path) {
        super(path);
        implementedInterfaces = new ArrayList();
        attributes = new ArrayList();
        superClass = null;
        methods = new ArrayList();
        totalCyclomaticComplexity = 0;
    }
    
    public JavaClass(){
        super();
        implementedInterfaces = new ArrayList();
        attributes = new ArrayList();
        superClass = null;
        methods = new ArrayList();
        totalCyclomaticComplexity = 0;
    }

    public int getSize() {
        //int size = 0;
        //for(JavaAttribute javaAttribute : attributes){
        //if(javaAttribute.getType().getClass() == JavaAbstractExternal.class || javaAttribute.getType().getClass() == JavaPrimitiveType.class){
        //      size++;
        //}
        //}

        return attributes.size();
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
        attributes.add(javaAttribute);
    }

    public JavaData getJavaTypeByVariableName(String name) {
        JavaData javaData = null;
        for (JavaAttribute javaAttribute : attributes) {
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
        
}
