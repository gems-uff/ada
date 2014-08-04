/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.javacode;

import br.uff.ic.archd.ast.service.AstService;
import br.uff.ic.archd.ast.service.JavaMethodAstBox;
import br.uff.ic.archd.ast.service.ParameterAst;
import br.uff.ic.archd.xml.service.XMLService;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wallace
 */
public class JavaConstructorService {

    public JavaProject createProjects(String path) {
        long inicio = System.currentTimeMillis();
        AstService astService = new AstService();
        JavaProject javaProject = new JavaProject(path);
        List<String> pathList = astService.getAllJavaClassesPath(path);
        //create java classes and interfaces
        for (String classPath : pathList) {
            JavaAbstract javaAbstract = null;
            String className = astService.getClassName(classPath);
            if (className != null) {
                boolean isInterface = astService.isInterface(classPath);
                if (isInterface) {
                    javaAbstract = new JavaInterface(classPath);
                } else {
                    javaAbstract = new JavaClass(classPath);
                }
                javaAbstract.setName(className);
                String packageName = astService.getPackage(classPath);
                JavaPackage javaPackage = javaProject.getPackageByName(packageName);
                if (javaPackage == null) {
                    javaPackage = new JavaPackage(packageName);
                    javaProject.addPackage(javaPackage);
                }
                javaAbstract.setJavaPackage(javaPackage);
                javaPackage.addJavaAbstract(javaAbstract);
                javaProject.addClass(javaAbstract);
            }
        }

        //complete the java abstract with imports, attributes, implements, superclasses and methods
        for (JavaAbstract javaAbstract : javaProject.getAllClasses()) {
            //get import classes of classes
            List<String> importList = astService.getImports(javaAbstract.getPath());
            for (String packageImport : importList) {
                List<JavaAbstract> javaAbstractList = javaProject.getPackagesByName(packageImport);
                javaAbstract.addImportClasses(javaAbstractList);
                if (javaAbstractList.isEmpty()) {
                    javaAbstract.addImportClasses(javaAbstractList);
                }
            }

            if (javaAbstract.getClass() == JavaClass.class) {
                //get superclass
                String superClassString = astService.getSuperClass(javaAbstract.getPath());
                if (superClassString != null) {
                    JavaClass superClass = (JavaClass) javaAbstract.getJavaAbstractImportByName(superClassString);
                    ((JavaClass) javaAbstract).setSuperClass(superClass);
                }
                //get all implemented interfaces of internal interfaces
                List<String> implementedInterfacesNames = astService.getImplementedInterfaces(javaAbstract.getPath());
                for (String implementedInterface : implementedInterfacesNames) {
                    //System.out.println("implemented interface ("+javaAbstract.getFullQualifiedName()+"): "+implementedInterface);
                    JavaAbstract javaInterface = javaAbstract.getJavaAbstractImportByName(implementedInterface);
                    if (javaInterface != null && javaInterface.getClass() == JavaInterface.class) {
                        ((JavaClass) javaAbstract).addImplementedInterface((JavaInterface) javaInterface);
                    }
                }

                //get attributes
                List<ParameterAst> attributes = astService.getAttributes(javaAbstract.getPath());

                for (ParameterAst attribute : attributes) {
                    JavaAbstract javaAbstractAttribute = javaAbstract.getJavaAbstractImportByName(attribute.getType());
                    if (javaAbstractAttribute == null) {
                        if (JavaPrimitiveType.getType(attribute.getType()) != 0) {
                            // the attribute is primitive type
                            JavaData javaData = new JavaPrimitiveType(JavaPrimitiveType.getType(attribute.getType()));
                            JavaAttribute javaAttribute = new JavaAttribute(javaData, attribute.getName(), attribute.isFinal(),
                                    attribute.isStatic(), attribute.isVolatile(), attribute.isPrivate(), attribute.isPublic(), attribute.isProtected());
                            ((JavaClass) javaAbstract).addAttribute(javaAttribute);
                        } else {
                            // the attribute is a external class
                            //get the complete name of the class
                            String externalClassName = getClassName(importList, attribute.getType());
                            //get external class from javaproject
                            JavaAbstractExternal javaAbstractExternal = javaProject.getJavaExternalClassByName(externalClassName);
                            if (javaAbstractExternal == null) {
                                //create new external class and add to the projetc
                                javaAbstractExternal = new JavaAbstractExternal(externalClassName);
                                javaProject.addExternalClass(javaAbstractExternal);
                            }
                            JavaAttribute javaAttribute = new JavaAttribute(javaAbstractExternal, attribute.getName(), attribute.isFinal(),
                                    attribute.isStatic(), attribute.isVolatile(), attribute.isPrivate(), attribute.isPublic(), attribute.isProtected());
                            ((JavaClass) javaAbstract).addAttribute(javaAttribute);
                        }
                    } else {
                        //the attribute is a internal class
                        JavaAttribute javaAttribute = new JavaAttribute(javaAbstractAttribute, attribute.getName(), attribute.isFinal(),
                                attribute.isStatic(), attribute.isVolatile(), attribute.isPrivate(), attribute.isPublic(), attribute.isProtected());
                        ((JavaClass) javaAbstract).addAttribute(javaAttribute);
                    }
                }
            }

            //get list of methods
            List<JavaMethodAstBox> list = astService.getMethods(javaAbstract.getPath());
            setJavaMethods(javaAbstract, list, importList, javaProject, false);

        }


        //get calls of the methods
        for (JavaAbstract javaAbstract : javaProject.getAllClasses()) {
            if (javaAbstract.getClass() == JavaClass.class) {
                astService.getMethodInvocations((JavaClass) javaAbstract, javaProject);
            }
        }



        //get the assignments
        for (JavaAbstract javaAbstract : javaProject.getAllClasses()) {
            if (javaAbstract.getClass() == JavaClass.class) {
                astService.setAttributeModificationMethod((JavaClass) javaAbstract, javaProject);
            }
        }

        long fim = System.currentTimeMillis();
        System.out.println("Tempo para gerar: " + (fim - inicio));
        /*inicio = System.currentTimeMillis();
        XMLService xmlService = new XMLService();
        for (JavaAbstract javaAbstract : javaProject.getAllClasses()) {
            xmlService.setXML(javaAbstract, "1");
        }

        fim = System.currentTimeMillis();
        System.out.println("Tempo para salva em XML: " + (fim - inicio));*/

        return javaProject;


    }

    private String getClassName(List<String> importList, String name) {
        String className = name;
        for (String importName : importList) {
            if (importName.endsWith(name)) {
                className = importName;
                break;
            }
        }
        return className;
    }

    private void setJavaMethods(JavaAbstract javaAbstract, List<JavaMethodAstBox> list, List<String> importList, JavaProject javaProject, boolean fromXML) {
        for (JavaMethodAstBox javaMethodAstBox : list) {
            String returnTypeString = javaMethodAstBox.getReturnType();
            //String returnTypeClassName = getClassName(importList, returnTypeString);


            JavaData javaDataReturnType = null;
            JavaAbstract javaAbstractAttribute = javaAbstract.getJavaAbstractImportByName(returnTypeString);
            if (javaAbstractAttribute == null) {
                if (JavaPrimitiveType.getType(returnTypeString) != 0) {
                    // the attribute is primitive type
                    javaDataReturnType = new JavaPrimitiveType(JavaPrimitiveType.getType(returnTypeString));
                } else {
                    // the attribute is a external class
                    //get the complete name of the class
                    String externalClassName = null;
                    if (fromXML) {
                        externalClassName = returnTypeString;
                    } else {
                        externalClassName = getClassName(importList, returnTypeString);
                    }

                    //get external class from javaproject
                    JavaAbstractExternal javaAbstractExternal = javaProject.getJavaExternalClassByName(externalClassName);
                    if (javaAbstractExternal == null) {
                        //create new external class and add to the projetc
                        javaAbstractExternal = new JavaAbstractExternal(externalClassName);
                        javaProject.addExternalClass(javaAbstractExternal);

                    }
                    javaDataReturnType = javaAbstractExternal;
                }
            } else {
                //the attribute is a internal class
                javaDataReturnType = javaAbstractAttribute;
            }

            JavaMethod javaMethod = new JavaMethod(javaMethodAstBox.getName(), javaDataReturnType, javaMethodAstBox.isFinal(), javaMethodAstBox.isStatic(),
                    javaMethodAstBox.isAbstract(), javaMethodAstBox.isSynchronized(), javaMethodAstBox.isPrivate(), javaMethodAstBox.isPublic(), javaMethodAstBox.isProtected(), javaMethodAstBox.getCyclomaticComplexity(), javaMethodAstBox.getBlock());


            for (ParameterAst parameterAst : javaMethodAstBox.getParameters()) {
                String parameterTypeName = parameterAst.getType();
                JavaData parameterType = null;

                JavaAbstract javaAbstractParameter = javaAbstract.getJavaAbstractImportByName(parameterTypeName);
                if (javaAbstractParameter == null) {
                    if (JavaPrimitiveType.getType(parameterTypeName) != 0) {
                        // the attribute is primitive type
                        parameterType = new JavaPrimitiveType(JavaPrimitiveType.getType(parameterTypeName));
                    } else {
                        // the attribute is a external class
                        //get the complete name of the class
                        String externalClassName = null;
                        if (fromXML) {
                            externalClassName = parameterTypeName;
                        } else {
                            externalClassName = getClassName(importList, parameterTypeName);
                        }
                        //get external class from javaproject
                        JavaAbstractExternal javaAbstractExternal = javaProject.getJavaExternalClassByName(externalClassName);
                        if (javaAbstractExternal == null) {
                            //create new external class and add to the projetc
                            javaAbstractExternal = new JavaAbstractExternal(externalClassName);
                            javaProject.addExternalClass(javaAbstractExternal);

                        }
                        parameterType = javaAbstractExternal;
                    }
                } else {
                    //the attribute is a internal class
                    parameterType = javaAbstractParameter;
                }


                Parameter parameter = new Parameter(parameterType, parameterAst.getName());

                javaMethod.addParameter(parameter);


            }
            if (javaAbstract.getClass() == JavaClass.class) {
                ((JavaClass) javaAbstract).addMethod(javaMethod);
                if (fromXML) {
                    javaMethod.setInternalID(javaMethodAstBox.getMethodInternalId());
                }
            } else if (javaAbstract.getClass() == JavaInterface.class) {
                ((JavaInterface) javaAbstract).addJavaMethod(javaMethod);
                if (fromXML) {
                    javaMethod.setInternalID(javaMethodAstBox.getMethodInternalId());
                }
            }


        }
    }

    public JavaProject createProjectsFromXML(String path) {
        long inicio = System.currentTimeMillis();
        XMLService xmlService = new XMLService();
        JavaProject javaProject = new JavaProject(path);
        List<String> pathList = getXMLPaths(path);
        //create java classes and interfaces
        for (String classPath : pathList) {
            JavaAbstract javaAbstract = xmlService.createJavaAbstractFromXMLFile(classPath);
            String packageName = xmlService.getPackageName(classPath);
            JavaPackage javaPackage = javaProject.getPackageByName(packageName);
            if (javaPackage == null) {
                javaPackage = new JavaPackage(packageName);
                javaProject.addPackage(javaPackage);
            }
            javaAbstract.setJavaPackage(javaPackage);
            javaPackage.addJavaAbstract(javaAbstract);
            javaProject.addClass(javaAbstract);
        }

        //complete the java abstract with imports, attributes, implements, superclasses and methods
        for (JavaAbstract javaAbstract : javaProject.getAllClasses()) {
            //get import classes of classes
            List<String> importList = xmlService.getImports(javaAbstract.getPath());
            for (String packageImport : importList) {
                List<JavaAbstract> javaAbstractList = javaProject.getPackagesByName(packageImport);
                javaAbstract.addImportClasses(javaAbstractList);
                if (javaAbstractList.isEmpty()) {
                    javaAbstract.addImportClasses(javaAbstractList);
                }
            }

            if (javaAbstract.getClass() == JavaClass.class) {
                //get superclass
                String superClassString = xmlService.getSuperClass(javaAbstract.getPath());
                if (superClassString != null && !superClassString.equals("")) {
                    JavaClass superClass = (JavaClass) javaAbstract.getJavaAbstractImportByName(superClassString);
                    ((JavaClass) javaAbstract).setSuperClass(superClass);
                }
                //get all implemented interfaces of internal interfaces
                List<String> implementedInterfacesNames = xmlService.getImplementedInterfaces(javaAbstract.getPath());
                for (String implementedInterface : implementedInterfacesNames) {
                    //System.out.println("implemented interface ("+javaAbstract.getFullQualifiedName()+"): "+implementedInterface);
                    JavaAbstract javaInterface = javaAbstract.getJavaAbstractImportByName(implementedInterface);
                    if (javaInterface != null && javaInterface.getClass() == JavaInterface.class) {
                        ((JavaClass) javaAbstract).addImplementedInterface((JavaInterface) javaInterface);
                    }
                }

                //get attributes
                List<ParameterAst> attributes = xmlService.getAttributes(javaAbstract.getPath());

                for (ParameterAst attribute : attributes) {
                    //System.out.println("Attribute "+javaAbstract.getFullQualifiedName()+"    "+attribute.getName()+"  : "+attribute.getType());
                    JavaAbstract javaAbstractAttribute = javaAbstract.getJavaAbstractImportByName(attribute.getType());
                    if (javaAbstractAttribute == null) {
                        if (JavaPrimitiveType.getType(attribute.getType()) != 0) {
                            // the attribute is primitive type

                            JavaData javaData = new JavaPrimitiveType(JavaPrimitiveType.getType(attribute.getType()));
                            JavaAttribute javaAttribute = new JavaAttribute(javaData, attribute.getName(), attribute.isFinal(),
                                    attribute.isStatic(), attribute.isVolatile(), attribute.isPrivate(), attribute.isPublic(), attribute.isProtected());
                            ((JavaClass) javaAbstract).addAttribute(javaAttribute);
                        } else {
                            // the attribute is a external class
                            //get the complete name of the class
                            String externalClassName = getClassName(importList, attribute.getType());
                            //get external class from javaproject
                            JavaAbstractExternal javaAbstractExternal = javaProject.getJavaExternalClassByName(externalClassName);
                            if (javaAbstractExternal == null) {
                                //create new external class and add to the projetc
                                javaAbstractExternal = new JavaAbstractExternal(externalClassName);
                                javaProject.addExternalClass(javaAbstractExternal);
                            }
                            JavaAttribute javaAttribute = new JavaAttribute(javaAbstractExternal, attribute.getName(), attribute.isFinal(),
                                    attribute.isStatic(), attribute.isVolatile(), attribute.isPrivate(), attribute.isPublic(), attribute.isProtected());
                            ((JavaClass) javaAbstract).addAttribute(javaAttribute);
                        }
                    } else {
                        //the attribute is a internal class
                        JavaAttribute javaAttribute = new JavaAttribute(javaAbstractAttribute, attribute.getName(), attribute.isFinal(),
                                attribute.isStatic(), attribute.isVolatile(), attribute.isPrivate(), attribute.isPublic(), attribute.isProtected());
                        ((JavaClass) javaAbstract).addAttribute(javaAttribute);
                    }
                }
            }

            //get list of methods
            boolean fromJavaClass = false;
            if (javaAbstract.getClass() == JavaClass.class) {
                fromJavaClass = true;
            }
            List<JavaMethodAstBox> list = xmlService.getMethods(javaAbstract.getPath(), fromJavaClass);
            setJavaMethods(javaAbstract, list, importList, javaProject, true);

        }

        //get calls of the methods
        for (JavaAbstract javaAbstract : javaProject.getAllClasses()) {
            if (javaAbstract.getClass() == JavaClass.class) {
                List<JavaMethodAstBox> list = xmlService.getMethods(javaAbstract.getPath(), true);
                for (JavaMethodAstBox javaMethodAstBox : list) {
                    JavaMethod javaMethod = ((JavaClass) javaAbstract).getMethodByInternalId(javaMethodAstBox.getMethodInternalId());
                    if (javaMethod != null) {
                        javaMethod.setSizeInChars(javaMethodAstBox.getSizeInChars());
                        javaMethod.setChangeInternalState(javaMethodAstBox.isChangeInternalState());
                        javaMethod.setChangeInternalStateByMethodInvocations(javaMethodAstBox.isChangeInternalStateByMethodInvocation());
                        for (String methodInternalInvocation : javaMethodAstBox.getMethodInternalInvocations()) {
                            int methodInternalId = Integer.valueOf(methodInternalInvocation);
                            javaMethod.addInternalMethodInvocation(((JavaClass) javaAbstract).getMethodByInternalId(methodInternalId));
                        }

                        for (String methodInvocation : javaMethodAstBox.getMethodInvocations()) {
                            String methodInvocationArray[] = methodInvocation.split(":");
                            String classInvocation = methodInvocationArray[0];
                            JavaAbstract javaAbstractInvocation = javaProject.getClassByName(classInvocation);
                            if (methodInvocationArray[1].matches("[+-]?\\d*(\\.\\d+)?")) {
                                int methodInternalId = Integer.valueOf(methodInvocationArray[1]);
                                
                                if (javaAbstractInvocation != null) {
                                    if (javaAbstractInvocation.getClass() == JavaClass.class) {
                                        JavaMethodInvocation javaMethoInvocation = new JavaMethodInvocation(javaAbstractInvocation, ((JavaClass) javaAbstractInvocation).getMethodByInternalId(methodInternalId));
                                        javaMethod.addMethodInvocation(javaMethoInvocation);
                                    } else {
                                        JavaMethodInvocation javaMethoInvocation = new JavaMethodInvocation(javaAbstractInvocation, ((JavaInterface) javaAbstractInvocation).getMethodByInternalId(methodInternalId));
                                        javaMethod.addMethodInvocation(javaMethoInvocation);
                                    }

                                }
                            }else{
                                JavaMethodInvocation javaMethoInvocation = new JavaMethodInvocation(javaAbstractInvocation, null);
                                javaMethoInvocation.setUnknowMethodName(methodInvocationArray[1]);
                            }
                        }
                    }
                }
            }
        }

        long fim = System.currentTimeMillis();
        System.out.println("Tempo para gerar: " + (fim - inicio));

        return javaProject;
    }

    private List<String> getXMLPaths(String path) {
        List<String> paths = new ArrayList();
        File file = new File(path);
        File files[] = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].getAbsolutePath().endsWith(".xml")) {
                paths.add(files[i].getAbsolutePath());
            }
        }

        return paths;
    }
}
