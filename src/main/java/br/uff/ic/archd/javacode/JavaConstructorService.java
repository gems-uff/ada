/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.javacode;

import br.uff.ic.archd.ast.service.AstService;
import br.uff.ic.archd.ast.service.JavaMethodAstBox;
import br.uff.ic.archd.ast.service.ParameterAst;
import java.util.List;

/**
 *
 * @author wallace
 */
public class JavaConstructorService {

    public JavaProject createProjects(String path) {
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
            setJavaMethods(javaAbstract, list, importList, javaProject);

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

    private void setJavaMethods(JavaAbstract javaAbstract, List<JavaMethodAstBox> list, List<String> importList, JavaProject javaProject) {
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
                    String externalClassName = getClassName(importList, returnTypeString);

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
                        String externalClassName = getClassName(importList, parameterTypeName);
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
            } else if (javaAbstract.getClass() == JavaInterface.class) {
                ((JavaInterface) javaAbstract).addJavaMethod(javaMethod);
            }


        }
    }
}
