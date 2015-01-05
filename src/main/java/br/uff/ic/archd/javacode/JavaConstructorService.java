/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.javacode;

import br.uff.ic.archd.ast.service.AstService;
import br.uff.ic.archd.ast.service.JavaMethodAstBox;
import br.uff.ic.archd.ast.service.ParameterAst;
import br.uff.ic.archd.db.dao.ClassesDao;
import br.uff.ic.archd.db.dao.DataBaseFactory;
import br.uff.ic.archd.db.dao.ExternalImportsDao;
import br.uff.ic.archd.db.dao.ImplementedInterfacesDao;
import br.uff.ic.archd.db.dao.InterfaceDao;
import br.uff.ic.archd.db.dao.InternalImportsDao;
import br.uff.ic.archd.db.dao.JavaAttributeDao;
import br.uff.ic.archd.db.dao.JavaExternalAttributeAccessDao;
import br.uff.ic.archd.db.dao.JavaMethodDao;
import br.uff.ic.archd.db.dao.MethodInvocationsDao;
import br.uff.ic.archd.db.dao.TerminatedDao;
import br.uff.ic.archd.xml.service.XMLService;
import br.uff.ic.dyevc.application.branchhistory.model.ProjectRevisions;
import br.uff.ic.dyevc.application.branchhistory.model.Revision;
import br.uff.ic.dyevc.tools.vcs.git.GitConnector;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;

/**
 *
 * @author wallace
 */
public class JavaConstructorService {

    private String BRANCHES_HISTORY_PATH = System.getProperty("user.home") + "/.archd/BRANCHES_HISTORY/";

    public List<JavaProject> getAllProjectsRevision(String projectName, List<String> codeDirs, String path, ProjectRevisions projectRevisions) {
        List<JavaProject> javaProjects = new LinkedList();
        Iterator<Revision> it = projectRevisions.getRevisionsBucket().getRevisionCollection().iterator();

        try {




            GitConnector gitConnector = new GitConnector(BRANCHES_HISTORY_PATH + projectName, projectRevisions.getName());
            Git git = new Git(gitConnector.getRepository());
            CheckoutCommand checkoutCommand = null;


            List<String> newCodeDirs = new LinkedList();
            for (String codeDir : codeDirs) {
                String newCodeDir = codeDir.substring(path.length(), codeDir.length());
                if (newCodeDir.startsWith("/")) {
                    newCodeDir = newCodeDir.substring(1);
                }
                newCodeDir = BRANCHES_HISTORY_PATH + projectName + "/" + newCodeDir;
                newCodeDirs.add(newCodeDir);
            }
            while (it.hasNext()) {


                //criando nova pasta
                /*File file = new File(BRANCHES_HISTORY_PATH + projectRevisions.getName());
                 FileUtils.deleteDirectory(file);
                 file = new File(BRANCHES_HISTORY_PATH + projectRevisions.getName());
                 System.out.println("Foi deletado aguardando");
                 System.out.println("Vai criar");
                 file.mkdirs();
                 FileUtils.copyDirectory(new File(path), new File(BRANCHES_HISTORY_PATH + projectRevisions.getName()));*/
                //******** fim criar nova pasta




                gitConnector = new GitConnector(BRANCHES_HISTORY_PATH + projectName, projectRevisions.getName());
                git = new Git(gitConnector.getRepository());



                Revision revision = it.next();


                checkoutCommand = git.checkout();
                checkoutCommand.setName(revision.getId());
                checkoutCommand.call();


                JavaProject javaProject = this.getProjectByRevision(projectName, newCodeDirs, BRANCHES_HISTORY_PATH + projectRevisions.getName(), revision.getId());
                javaProjects.add(javaProject);
                //System.out.println("Salvo revisão: " + revision.getId() + "      Número: " + javaProjects.size());
            }
        } catch (Exception e) {
            System.out.println("Exception getAllProjectsRevision: " + e.getMessage() + "             class: " + e.getClass());
        }

        return javaProjects;
    }

    public JavaProject getProjectByRevisionAndSetRevision(String projectName, List<String> codeDirs, String path, Revision revision, ProjectRevisions projectRevisions) {

        try {
            List<String> newCodeDirs = new LinkedList();
            for (String codeDir : codeDirs) {
                String newCodeDir = codeDir.substring(path.length(), codeDir.length());
                if (newCodeDir.startsWith("/")) {
                    newCodeDir = newCodeDir.substring(1);
                }
                newCodeDir = BRANCHES_HISTORY_PATH + projectName + "/" + newCodeDir;
                newCodeDirs.add(newCodeDir);
            }
            
            long tempoclone1 = System.currentTimeMillis();
            GitConnector gitConnector = new GitConnector(BRANCHES_HISTORY_PATH + projectName, projectRevisions.getName());
            Git git = new Git(gitConnector.getRepository());
            CheckoutCommand checkoutCommand = null;

            gitConnector = new GitConnector(BRANCHES_HISTORY_PATH + projectName, projectRevisions.getName());
            git = new Git(gitConnector.getRepository());


            checkoutCommand = git.checkout();
            checkoutCommand.setName(revision.getId());
            checkoutCommand.call();
            
            long tempoclone2 = System.currentTimeMillis();
            System.out.println("TEMPO PRA FAZER UM CLONE: "+(tempoclone2-tempoclone1)+" milisegundos");
            
            JavaProject javaProject = this.getProjectByRevision(projectName, newCodeDirs, BRANCHES_HISTORY_PATH + projectRevisions.getName(), revision.getId());
            return javaProject;
        } catch (Exception e) {
            System.out.println("Exception getProjectByRevisionAndSetRevision: " + e.getMessage() + "             class: " + e.getClass());
        }
        return null;
    }

    public JavaProject getProjectByRevision(String projectName, List<String> codeDirs, String path, String revisionId) {
        TerminatedDao terminatedDao = DataBaseFactory.getInstance().getTerminatedDao();
        JavaProject javaProject = null;
        ClassesDao classesDao = DataBaseFactory.getInstance().getClassesDao();
        InterfaceDao interfaceDao = DataBaseFactory.getInstance().getInterfaceDao();
        JavaMethodDao javaMethodDao = DataBaseFactory.getInstance().getJavaMethodDao();
        JavaAttributeDao javaAttributeDao = DataBaseFactory.getInstance().getJavaAttributeDao();
        ImplementedInterfacesDao implementedInterfacesDao = DataBaseFactory.getInstance().getImplementedInterfacesDao();
        InternalImportsDao internalImportsDao = DataBaseFactory.getInstance().getInternalImportsDao();
        ExternalImportsDao externalImportsDao = DataBaseFactory.getInstance().getExternalImportsDao();
        MethodInvocationsDao methodInvocationDao = DataBaseFactory.getInstance().getMethodInvocationsDao();
        JavaExternalAttributeAccessDao javaExternalAttributeAccessDao = DataBaseFactory.getInstance().getJavaExternalAttributeAccessDao();
        if (terminatedDao.isTerminated(projectName, revisionId)) {
            System.out.println("Vai pegar do banco");
            System.out.println("ESTATISTICAS:");
            long tempototal1 = System.currentTimeMillis();
            javaProject = new JavaProject(path);
            //long t1 = System.currentTimeMillis();
            classesDao.getJavaClassesByRevisionId(javaProject, revisionId);
            //long t2 = System.currentTimeMillis();
            //System.out.println("Pegar todas as classes de uma revisão: "+(t2-t1)+"  milisegundos");
            
            //t1 = System.currentTimeMillis();
            interfaceDao.getJavaInterfacesByRevisionId(javaProject, revisionId);
            //t2 = System.currentTimeMillis();
            //System.out.println("Pegar todas as interfaces de uma revisão: "+(t2-t1)+"  milisegundos");
            //complete the java abstract with imports, attributes, implements, superclasses and methods
            for (JavaAbstract javaAbstract : javaProject.getAllClasses()) {
                //t1 = System.currentTimeMillis();
                internalImportsDao.getInternalImports(javaAbstract, javaProject);
                //t2 = System.currentTimeMillis();
                //System.out.println("Pegar todos os imports internos de uma revisão: "+(t2-t1)+"  milisegundos");
                //t1 = System.currentTimeMillis();
                externalImportsDao.getExternalImports(javaAbstract, javaProject);
                //t2 = System.currentTimeMillis();
                //System.out.println("Pegar todas os imports externos de uma revisão: "+(t2-t1)+"  milisegundos");
                //System.out.println("Nome: " + javaAbstract.getFullQualifiedName());
                if (javaAbstract.getClass() == JavaClass.class) {
                    
                    //t1 = System.currentTimeMillis();
                    List<JavaMethod> javaMethods = javaMethodDao.getJavaMethodsByClassId(javaProject, javaAbstract.getId());
                    //t2 = System.currentTimeMillis();
                    //System.out.println("Pegar todas os métodos de uma classe de uma revisão: "+(t2-t1)+"  milisegundos");
                    
                    for (JavaMethod javaMethod : javaMethods) {
                        ((JavaClass) javaAbstract).addMethod(javaMethod);
                        javaMethod.setJavaAbstract(javaAbstract);
                    }
                    //t1 = System.currentTimeMillis();
                    implementedInterfacesDao.setImplementedInterfacesDao((JavaClass) javaAbstract, javaProject);
                    //t2 = System.currentTimeMillis();
                    //System.out.println("Pegar todas as implemented interfaces de uma classe de uma revisão: "+(t2-t1)+"  milisegundos");
                    
                    //t1 = System.currentTimeMillis();
                    javaAttributeDao.getJavaAttributesFromClass((JavaClass) javaAbstract, javaProject);
                    //t2 = System.currentTimeMillis();
                    //System.out.println("Pegar todas os atributos de uma classe de uma revisão: "+(t2-t1)+"  milisegundos");

                } else {
                    //t1 = System.currentTimeMillis();
                    List<JavaMethod> javaMethods = javaMethodDao.getJavaMethodsByInterfaceId(javaProject, javaAbstract.getId());
                    //t2 = System.currentTimeMillis();
                    //System.out.println("Pegar todas os métodos de uma interface de uma revisão: "+(t2-t1)+"  milisegundos");
                    for (JavaMethod javaMethod : javaMethods) {
                        ((JavaInterface) javaAbstract).addJavaMethod(javaMethod);
                        javaMethod.setJavaAbstract(javaAbstract);
                    }
                }
            }
            for (JavaAbstract javaAbstract : javaProject.getAllClasses()) {
                if (javaAbstract.getClass() == JavaClass.class) {
                    for (JavaMethod javaMethod : ((JavaClass) javaAbstract).getMethods()) {
                        //t1 = System.currentTimeMillis();
                        methodInvocationDao.getInvocatedMethods(javaMethod, (JavaClass) javaAbstract, javaProject);
                        javaExternalAttributeAccessDao.getJavaExternalAttributeAccessByMethod(javaMethod, javaProject);
                        //t2 = System.currentTimeMillis();
                        //System.out.println("Pegar todas as invocações de métodos de uma classe de uma revisão: "+(t2-t1)+"  milisegundos");
                    }
                }
            }
            System.out.println("Pegou do banco");
            long tempototal2 = System.currentTimeMillis();
            System.out.println("TEMPO TOTAL PRA PEGAR UMA REVISÃO Do BANCO: "+(tempototal2 - tempototal1)+" milisegundos");
            setProjectProperties(javaProject);

        } else {
            javaProject = this.createProjects(codeDirs, path, revisionId);

            for (JavaAbstract javaAbstract : javaProject.getClasses()) {
                JavaClass javaClass = (JavaClass) javaAbstract;
                classesDao.save(javaClass);

                for (String externalImport : javaClass.getExternalImports()) {
                    externalImportsDao.save(javaAbstract, externalImport);
                }
                for (JavaAttribute javaAttribute : javaClass.getAttributes()) {
                    javaAttributeDao.save(javaAttribute, javaClass.getId());
                }
                for (JavaMethod javaMethod : javaClass.getMethods()) {
                    javaMethodDao.save(javaMethod, true, javaClass.getId());
                }

            }
            for (JavaAbstract javaAbstract : javaProject.getInterfaces()) {
                JavaInterface javaInterface = (JavaInterface) javaAbstract;
                interfaceDao.save(javaInterface);
                for (JavaMethod javaMethod : javaInterface.getMethods()) {
                    javaMethodDao.save(javaMethod, false, javaInterface.getId());
                }
            }

            for (JavaAbstract javaAbstract : javaProject.getClasses()) {
                JavaClass javaClass = (JavaClass) javaAbstract;
                for (JavaInterface javaInterface : javaClass.getImplementedInterfaces()) {
                    implementedInterfacesDao.saveImplementedInterface(javaClass, javaInterface);
                }
                for (JavaAbstract javaAbstractImport : javaClass.getClassesImports()) {
                    internalImportsDao.saveInternalImport(javaClass, javaAbstractImport);
                }
                for (JavaMethod javaMethod : javaClass.getMethods()) {
                    methodInvocationDao.saveMethodInvocations(javaMethod, javaClass);
                    javaExternalAttributeAccessDao.saveJavaExternalAttributeAccess(javaMethod);
                }
            }
            terminatedDao.save(projectName, revisionId);
            System.out.println("Salvou a revisão: "+revisionId);

        }
        javaProject.setRevisionId(revisionId);
        System.out.println("Vai setar changing methods");
        javaProject.setChangingMethodsAndClasses();
        System.out.println("Setou changing methods");
        return javaProject;
    }

    public JavaProject createProjects(List<String> codeDirs, String path, String revisionId) {
        long inicio = System.currentTimeMillis();
        AstService astService = new AstService();
        JavaProject javaProject = new JavaProject(path);
        if (codeDirs.isEmpty()) {
            codeDirs.add(path);
        }
        for (String codeDir : codeDirs) {
            System.out.println("CodeDir: " + codeDir);
            List<String> pathList = astService.getAllJavaClassesPath(codeDir);
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
                    javaAbstract.setRevisionId(revisionId);
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
                //System.out.println("Calculando métricas de acesso de dados");
                astService.getAccessDataMetrics((JavaClass) javaAbstract, javaProject);
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
        setProjectProperties(javaProject);
        /*int numberOfClasses = 0;
         int numberOfInterfaces = 0;
         List<JavaAbstract> classes = javaProject.getAllClasses();
         for (JavaAbstract javaClazz : classes) {
         if (javaClazz.getClass() == JavaClass.class) {
         numberOfClasses++;
         List<JavaClass> classesThatCall = javaProject.getClassesThatCall(javaClazz);
         List<JavaAbstract> classesThatUsing = javaProject.getClassesThatUsing(javaClazz);
         if (classesThatCall.isEmpty() && classesThatUsing.isEmpty()) {
         List<JavaInterface> implementedInterfaces = ((JavaClass) javaClazz).getImplementedInterfaces();
         for (JavaInterface implementedInterface : implementedInterfaces) {
         classesThatCall.addAll(javaProject.getClassesThatCall(implementedInterface));
         classesThatUsing.addAll(javaProject.getClassesThatUsing(implementedInterface));
         }
         JavaClass JavaSuperClazz = ((JavaClass) javaClazz).getSuperClass();
         while (JavaSuperClazz != null) {
         implementedInterfaces = JavaSuperClazz.getImplementedInterfaces();
         for (JavaInterface implementedInterface : implementedInterfaces) {
         classesThatCall.addAll(javaProject.getClassesThatCall(implementedInterface));
         classesThatUsing.addAll(javaProject.getClassesThatUsing(implementedInterface));
         }
         JavaSuperClazz = JavaSuperClazz.getSuperClass();
         }
         if (classesThatCall.isEmpty() && classesThatUsing.isEmpty()) {
         javaProject.addLeaderClass(javaClazz);
         } else {
         javaProject.addPossibleLeaderClass(javaClazz);
         }
         }


         //vendo a inteligencia
         boolean containSmartMethod = false;
         boolean containFoolMethod = false;
         for (JavaMethod javaMethod : ((JavaClass) javaClazz).getMethods()) {
         if (javaMethod.getCyclomaticComplexity() <= 1) {
         containFoolMethod = true;
         } else {
         containSmartMethod = true;
         }
         if (containSmartMethod && containFoolMethod) {
         break;
         }
         }
         if (containSmartMethod && containFoolMethod) {
         javaProject.addSimpleSmartClass((JavaClass) javaClazz);
         } else if (containSmartMethod) {
         javaProject.addFullSmartClass((JavaClass) javaClazz);
         } else if (containFoolMethod) {
         javaProject.addFoolClass((JavaClass) javaClazz);
         }



         } else {
         numberOfInterfaces++;
         }
         }
         javaProject.setNumberOfClasses(numberOfClasses);
         javaProject.setNumberOfInterfaces(numberOfInterfaces);

         //verificar as classes burras e inteligentes*/



        return javaProject;


    }

    public void setProjectProperties(JavaProject javaProject) {
        //pegando as classes lideres        
        //pegando tambem as classes inteligentes, burras e parcialmente inteligentes
        int numberOfClasses = 0;
        int numberOfInterfaces = 0;
        List<JavaAbstract> classes = javaProject.getAllClasses();
        for (JavaAbstract javaClazz : classes) {
            if (javaClazz.getClass() == JavaClass.class) {
                numberOfClasses++;
                List<JavaClass> classesThatCall = javaProject.getClassesThatCall(javaClazz);
                List<JavaAbstract> classesThatUsing = javaProject.getClassesThatUsing(javaClazz);
                if (classesThatCall.isEmpty() && classesThatUsing.isEmpty()) {
                    List<JavaInterface> implementedInterfaces = ((JavaClass) javaClazz).getImplementedInterfaces();
                    for (JavaInterface implementedInterface : implementedInterfaces) {
                        classesThatCall.addAll(javaProject.getClassesThatCall(implementedInterface));
                        classesThatUsing.addAll(javaProject.getClassesThatUsing(implementedInterface));
                    }
                    JavaClass JavaSuperClazz = ((JavaClass) javaClazz).getSuperClass();
                    while (JavaSuperClazz != null) {
                        implementedInterfaces = JavaSuperClazz.getImplementedInterfaces();
                        for (JavaInterface implementedInterface : implementedInterfaces) {
                            classesThatCall.addAll(javaProject.getClassesThatCall(implementedInterface));
                            classesThatUsing.addAll(javaProject.getClassesThatUsing(implementedInterface));
                        }
                        JavaSuperClazz = JavaSuperClazz.getSuperClass();
                    }
                    if (classesThatCall.isEmpty() && classesThatUsing.isEmpty()) {
                        javaProject.addLeaderClass(javaClazz);
                    } else {
                        javaProject.addPossibleLeaderClass(javaClazz);
                    }
                }


                //vendo a inteligencia
                boolean containSmartMethod = false;
                boolean containFoolMethod = false;
                for (JavaMethod javaMethod : ((JavaClass) javaClazz).getMethods()) {
                    if (javaMethod.getCyclomaticComplexity() <= 1) {
                        containFoolMethod = true;
                    } else {
                        containSmartMethod = true;
                    }
                    if (containSmartMethod && containFoolMethod) {
                        break;
                    }
                }
                if (containSmartMethod && containFoolMethod) {
                    javaProject.addSimpleSmartClass((JavaClass) javaClazz);
                } else if (containSmartMethod) {
                    javaProject.addFullSmartClass((JavaClass) javaClazz);
                } else if (containFoolMethod) {
                    javaProject.addFoolClass((JavaClass) javaClazz);
                }



            } else {
                numberOfInterfaces++;
            }
        }
        javaProject.setNumberOfClasses(numberOfClasses);
        javaProject.setNumberOfInterfaces(numberOfInterfaces);
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
            javaMethod.setJavaAbstract(javaAbstract);
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
    
/*
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
                            } else {
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
    */

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
