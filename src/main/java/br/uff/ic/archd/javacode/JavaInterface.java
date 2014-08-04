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
public class JavaInterface extends JavaAbstract {

    private List<JavaMethod> methods;
    private List<JavaInterface> interfaceExtends;

    public JavaInterface(String path) {
        super(path);
        interfaceExtends = new ArrayList();
        methods = new ArrayList();
    }

    /**
     * @return the methods
     */
    public List<JavaMethod> getMethods() {
        return methods;
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

        if (javaMethodCall == null) {
            for (JavaInterface javaInterface : interfaceExtends) {
                javaMethodCall = javaInterface.getJavaMethod(name, numberOfParams, arguments);
                if (javaMethodCall != null) {
                    break;
                }
            }
        }

        return javaMethodCall;
    }

    public void addJavaMethod(JavaMethod javaMethod) {
        methods.add(javaMethod);
        javaMethod.setInternalID(methods.size());
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
}
