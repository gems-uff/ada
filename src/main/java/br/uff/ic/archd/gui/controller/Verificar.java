/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.gui.controller;

import br.uff.ic.archd.db.dao.AnomalieDao;
import br.uff.ic.archd.db.dao.AnomalieItem;
import br.uff.ic.archd.db.dao.Constants;
import br.uff.ic.archd.db.dao.DataBaseFactory;
import br.uff.ic.archd.git.service.JavaProjectsService;
import br.uff.ic.archd.git.service.ProjectRevisionsService;
import br.uff.ic.archd.javacode.JavaAbstract;
import br.uff.ic.archd.javacode.JavaAttribute;
import br.uff.ic.archd.javacode.JavaClass;
import br.uff.ic.archd.javacode.JavaConstructorService;
import br.uff.ic.archd.javacode.JavaMethod;
import br.uff.ic.archd.javacode.JavaPackage;
import br.uff.ic.archd.javacode.JavaProject;
import br.uff.ic.archd.model.Project;
import br.uff.ic.archd.service.mining.AnomalieList;
import br.uff.ic.archd.service.mining.AnomaliesAnaliser;
import br.uff.ic.archd.service.mining.GenericAnomalies;
import br.uff.ic.archd.service.mining.ProjectAnomalies;
import br.uff.ic.dyevc.application.branchhistory.model.BranchRevisions;
import br.uff.ic.dyevc.application.branchhistory.model.LineRevisions;
import br.uff.ic.dyevc.application.branchhistory.model.ProjectRevisions;
import br.uff.ic.dyevc.application.branchhistory.model.Revision;
import br.uff.ic.dyevc.application.branchhistory.model.RevisionsBucket;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.io.File;

/**
 *
 * @author wallace
 */
public class Verificar {

    public void verificarClientClasses(ProjectRevisions newProjectRevisions, Project project, JavaConstructorService javaConstructorService) {

        String path = System.getProperty("user.home") + "/.archd/";
        try {
            long t1 = System.currentTimeMillis();
            Revision rev = newProjectRevisions.getRoot();
            int k = 0;

            PrintWriter writer0 = new PrintWriter(path + "map_client_classes.txt", "UTF-8");
            while (rev != null) {
                k++;
                if (rev.getNext().size() == 0) {
                    rev = null;
                } else {
                    rev = rev.getNext().get(0);
                }
            }
            ProjectAnomalies projectAnomalies = new ProjectAnomalies(k);
            rev = newProjectRevisions.getRoot();
            k = 0;
            JavaProject ant = null;
            while (rev != null) {

                JavaProject jp = null;
                //System.out.println("REV ID: "+rev.getId());
                System.gc();
                System.out.println("********************************* vai pegar um projeto completo");
                jp = javaConstructorService.getProjectByRevisionAndSetRevision(project.getName(), project.getCodeDirs(), project.getPath(), rev.getId(), newProjectRevisions.getName());

                List<JavaClass> classesAddList = new LinkedList();
                //if (ant != null) {
                for (JavaAbstract javaAbstract : jp.getClasses()) {
                    JavaClass javaClass = (JavaClass) javaAbstract;

                    //JavaAbstract auxAbstract = ant.getClassByName(javaClass.getFullQualifiedName());
                    if (!javaClass.getClientClasses().isEmpty()) {
                        writer0.println(javaClass.getFullQualifiedName() + " - client_classes: " + javaClass.getClientClasses().size());
                    }
                    if (!javaClass.getClientPackages().isEmpty()) {
                        writer0.println(javaClass.getFullQualifiedName() + " - client_packages: " + javaClass.getClientPackages().size());
                    }
                    if (!javaClass.getExternalDependencyClasses().isEmpty()) {
                        writer0.println(javaClass.getFullQualifiedName() + " - number_of_external_dependency_class: " + javaClass.getExternalDependencyClasses().size());
                    }
                    if (!javaClass.getExternalDependencyPackages().isEmpty()) {
                        writer0.println(javaClass.getFullQualifiedName() + " - number_of_external_dependency_package: " + javaClass.getExternalDependencyPackages().size());
                    }

                }
                //}

                System.out.println("Verificou: " + k);

                ant = jp;
                k++;
                if (rev.getNext().size() == 0) {
                    rev = null;
                } else {
                    rev = rev.getNext().get(0);
                }
            }

            long t2 = System.currentTimeMillis();
            System.out.println("Classificando anomalias");
            projectAnomalies.classifyAnomalies();
            System.out.println("Terminou de classificar");
            System.out.println("Tempo pra pegar anomalias: " + ((t2 - t1) / 60000) + " minutos");

            writer0.close();

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
        return;

    }

    public void verificarClientClassesViaInterface(ProjectRevisions newProjectRevisions, Project project, JavaConstructorService javaConstructorService) {

        String path = System.getProperty("user.home") + "/.archd/";
        try {
            long t1 = System.currentTimeMillis();
            Revision rev = newProjectRevisions.getRoot();
            int k = 0;

            PrintWriter writer0 = new PrintWriter(path + "map_client_classes_via_interface.txt", "UTF-8");
            while (rev != null) {
                k++;
                if (rev.getNext().size() == 0) {
                    rev = null;
                } else {
                    rev = rev.getNext().get(0);
                }
            }
            ProjectAnomalies projectAnomalies = new ProjectAnomalies(k);
            rev = newProjectRevisions.getRoot();
            k = 0;
            JavaProject ant = null;
            while (rev != null) {

                JavaProject jp = null;
                //System.out.println("REV ID: "+rev.getId());
                System.gc();
                System.out.println("********************************* vai pegar um projeto completo");
                jp = javaConstructorService.getProjectByRevisionAndSetRevision(project.getName(), project.getCodeDirs(), project.getPath(), rev.getId(), newProjectRevisions.getName());

                List<JavaClass> classesAddList = new LinkedList();
                //if (ant != null) {
                for (JavaAbstract javaAbstract : jp.getClasses()) {
                    JavaClass javaClass = (JavaClass) javaAbstract;

                    //JavaAbstract auxAbstract = ant.getClassByName(javaClass.getFullQualifiedName());
                    if (!javaClass.getClientClassesViaInterface().isEmpty()) {
                        writer0.println(javaClass.getFullQualifiedName() + " - client_classes_by_interface: " + javaClass.getClientClassesViaInterface().size());
                    }
                    if (!javaClass.getClientPackagesViaInterface().isEmpty()) {
                        writer0.println(javaClass.getFullQualifiedName() + " - client_packages_by_interface: " + javaClass.getClientPackagesViaInterface().size());
                    }
                    if (!javaClass.getExternalDependencyClassesViaInterface().isEmpty()) {
                        writer0.println(javaClass.getFullQualifiedName() + " - number_of_external_dependency_class_by_interface: " + javaClass.getExternalDependencyClassesViaInterface().size());
                    }
                    if (!javaClass.getExternalDependencyPackagesViaInterface().isEmpty()) {
                        writer0.println(javaClass.getFullQualifiedName() + " - number_of_external_dependency_package_by_interface: " + javaClass.getExternalDependencyPackagesViaInterface().size());
                    }

                }
                //}

                System.out.println("Verificou: " + k);

                ant = jp;
                k++;
                if (rev.getNext().size() == 0) {
                    rev = null;
                } else {
                    rev = rev.getNext().get(0);
                }
            }

            long t2 = System.currentTimeMillis();
            System.out.println("Classificando anomalias");
            projectAnomalies.classifyAnomalies();
            System.out.println("Terminou de classificar");
            System.out.println("Tempo pra pegar anomalias: " + ((t2 - t1) / 60000) + " minutos");

            writer0.close();

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
        return;

    }

    public void getChanges(ProjectRevisions newProjectRevisions, Project project, JavaConstructorService javaConstructorService) {

        String path = System.getProperty("user.home") + "/.archd/";
        try {
            long t1 = System.currentTimeMillis();
            Revision rev = newProjectRevisions.getRoot();
            int k = 0;

            PrintWriter writer0 = new PrintWriter(path + "map_difference.txt", "UTF-8");
            while (rev != null) {
                k++;
                if (rev.getNext().size() == 0) {
                    rev = null;
                } else {
                    rev = rev.getNext().get(0);
                }
            }
            ProjectAnomalies projectAnomalies = new ProjectAnomalies(k);
            rev = newProjectRevisions.getRoot();
            k = 0;
            JavaProject ant = null;
            while (rev != null) {

                JavaProject jp = null;
                //System.out.println("REV ID: "+rev.getId());
                System.gc();
                System.out.println("********************************* vai pegar um projeto completo");
                jp = javaConstructorService.getProjectByRevisionAndSetRevision(project.getName(), project.getCodeDirs(), project.getPath(), rev.getId(), newProjectRevisions.getName());

                List<JavaClass> classesAddList = new LinkedList();
                if (ant != null) {
                    for (JavaAbstract javaAbstract : jp.getClasses()) {
                        JavaClass javaClass = (JavaClass) javaAbstract;

                        JavaAbstract auxAbstract = ant.getClassByName(javaClass.getFullQualifiedName());
                        if (auxAbstract != null && auxAbstract.getClass() == JavaClass.class) {
                            JavaClass antClass = (JavaClass) auxAbstract;
                            List<JavaMethod> methodsAddList = new LinkedList();
                            for (JavaMethod javaMethod : javaClass.getMethods()) {
                                JavaMethod auxMethod = antClass.getMethodBySignature(javaMethod.getMethodSignature());
                                if (auxMethod == null) {
                                    methodsAddList.add(javaMethod);

                                }
                            }
                            if (!methodsAddList.isEmpty()) {

                                for (JavaMethod javaMethod : methodsAddList) {
                                    String text = "";
                                    if ((javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature()).equals("org.mapdb.HTreeMap:putInner(K,V,int,int)")
                                            || (javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature()).equals("org.mapdb.SerializerBase:serialize2(DataOutput,Object,FastArrayList<Object>,Class)")
                                            || (javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature()).equals("org.mapdb.StoreWAL:verifyLogFile()")) {

                                        text = text + "add_god_method";
                                    } else if ((javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature()).equals("org.mapdb.SerializerBase:serialize2(DataOutput,Object,FastArrayList<Object>,Class<?>)")) {
                                    } else {
                                        text = text + "add_method";
                                    }
                                    //numero de dados externos a classe que esta classe acessa, diretamente ou via metodos acessores
                                    text = text + " , " + "acess_to_acess_to_foreign_data_number=" + antClass.getAccessToForeignDataNumber();
                                    text = text + " , " + "number_of_attributes=" + antClass.getAttributes().size();
                                    //classes de outros pacotes que dependem de mim
                                    text = text + " , " + "number_of_client_classes=" + antClass.getClientClasses().size();
                                    //pacotes que dependem de mim
                                    text = text + " , " + "number_of_client_packages=" + antClass.getClientPackages().size();
                                    //classes de outros pacotes das quais essa classe depende
                                    text = text + " , " + "number_of_external_dependency_class=" + antClass.getExternalDependencyClasses().size();
                                    //pacotes os quais essa classe depende
                                    text = text + " , " + "number_of_external_dependency_package=" + antClass.getExternalDependencyPackages().size();

                                    text = text + " , " + "number_of_external_imports=" + antClass.getExternalImports().size();

                                    text = text + " , " + "number_of_implemented_interfaces=" + antClass.getImplementedInterfaces().size();
                                    //classes do mesmo pacote das quais essa classe depende
                                    text = text + " , " + "number_of_internal_dependency_classes=" + antClass.getInternalDependencyClasses().size();
                                    text = text + " , " + "number_of_methods=" + antClass.getMethods().size();
                                    text = text + " , " + "number_of_add_methods=" + methodsAddList.size();
                                    double tcc = antClass.getNumberOfDirectConnections();
                                    int n = antClass.getMethods().size();
                                    tcc = tcc / ((n * (n - 1)) / 2);
                                    text = text + " , " + "tight_class_cohesion=" + tcc;
                                    text = text + " , " + "total_cyclomatic_complexity=" + antClass.getTotalCyclomaticComplexity();
                                    //classes do mesmo pacote que dependem de mim
                                    text = text + " , " + "number_of_intra_package_dependent_classes=" + antClass.getintraPackageDependentClass().size();
                                    double classLocality = antClass.getInternalDependencyClasses().size();
                                    classLocality = classLocality / (antClass.getInternalDependencyClasses().size() + antClass.getExternalDependencyClasses().size());
                                    //classes do mesmo pacote das quais essa classe depende
                                    System.out.println("Internal dependency class: " + antClass.getInternalDependencyClasses().size() + "   external dependency class: " + antClass.getExternalDependencyClasses().size());
                                    text = text + " , " + "class_locality=" + classLocality;
                                    double packageCohesion = antClass.getJavaPackage().getPackageCohesion();
                                    text = text + " , " + "package_cohesion=" + packageCohesion;
                                    text = text + " , " + "method_signature=" + antClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature();

                                    writer0.println(text);

                                }

                            }

                        }

                    }
                }

                System.out.println("Verificou: " + k);

                ant = jp;
                k++;
                if (rev.getNext().size() == 0) {
                    rev = null;
                } else {
                    rev = rev.getNext().get(0);
                }
            }

            long t2 = System.currentTimeMillis();
            System.out.println("Classificando anomalias");
            projectAnomalies.classifyAnomalies();
            System.out.println("Terminou de classificar");
            System.out.println("Tempo pra pegar anomalias: " + ((t2 - t1) / 60000) + " minutos");

            writer0.close();

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
        return;

    }

    public void getAddMethods(ProjectRevisions newProjectRevisions, Project project, JavaConstructorService javaConstructorService) {

        long t1 = System.currentTimeMillis();
        Revision rev = newProjectRevisions.getRoot();
        int k = 0;
        while (rev != null) {
            k++;
            if (rev.getNext().size() == 0) {
                rev = null;
            } else {
                rev = rev.getNext().get(0);
            }
        }

        String path = System.getProperty("user.home") + "/.archd/";

        try {
            PrintWriter writer0 = new PrintWriter(path + "map_add_methods_difference.txt", "UTF-8");

            rev = newProjectRevisions.getRoot();
            AnomalieDao anomalieDao = DataBaseFactory.getInstance().getAnomalieDao();
            HashMap<String, Integer> birthHashMap = new HashMap();
            HashMap<String, String> methodsAlternativeNameMap = new HashMap();
            HashMap<String, String> classesAlternativeNameMap = new HashMap();
            k = 0;
            JavaProject ant = null;
            while (rev != null) {

                JavaProject jp = null;
                //System.out.println("REV ID: "+rev.getId());
                System.gc();
                System.out.println("********************************* vai pegar um projeto completo");
                jp = javaConstructorService.getProjectByRevisionAndSetRevision(project.getName(), project.getCodeDirs(), project.getPath(), rev.getId(), newProjectRevisions.getName());

                for (JavaPackage javaPackage : jp.getPackages()) {
                    if (birthHashMap.get(javaPackage.getName()) == null) {
                        birthHashMap.put(javaPackage.getName(), k);
                    }
                }

                for (JavaAbstract javaAbstract : jp.getClasses()) {
                    JavaClass javaClass = (JavaClass) javaAbstract;

                    if (birthHashMap.get(javaClass.getFullQualifiedName()) == null) {
                        birthHashMap.put(javaClass.getFullQualifiedName(), k);

                        if (classesAlternativeNameMap.get(javaClass.getFullQualifiedName()) == null) {
                            if (ant != null) {
                                List<JavaAbstract> antClasses = ant.getClassByLastName(javaAbstract.getName());
                                List<JavaAbstract> sameClasses = new LinkedList();
                                for (JavaAbstract antClass : antClasses) {
                                    if (isSameJavaClass(javaClass, (JavaClass) antClass)) {
                                        sameClasses.add(antClass);
                                    } else {
                                        System.out.println(javaClass.getFullQualifiedName() + " - is not same - " + antClass.getFullQualifiedName());
                                    }
                                }
                                if (sameClasses.size() > 1) {
                                    System.out.println("********** Erro, duas classes parecidas");
                                } else {
                                    if (sameClasses.size() == 1) {
                                        String alternativeName = classesAlternativeNameMap.get(sameClasses.get(0).getFullQualifiedName());

                                        if (alternativeName == null) {
                                            alternativeName = sameClasses.get(0).getFullQualifiedName();
                                        }
                                        System.out.println("Classe mudou de nome: " + javaClass.getFullQualifiedName() + "   -    " + alternativeName);
                                        classesAlternativeNameMap.put(javaClass.getFullQualifiedName(), alternativeName);

                                    }
                                }

                            }
                        }

                    }
                    for (JavaMethod javaMethod : javaClass.getMethods()) {

                        //ainda nao foi dito a existencia desse metodo
                        if (birthHashMap.get(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature()) == null) {

                            birthHashMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), k);
                            if (methodsAlternativeNameMap.get(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature()) == null) {
                                methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature());
                                String alternativeClassName = classesAlternativeNameMap.get(javaClass.getFullQualifiedName());
                                //o mÃ©todo ainda nao surgiu e a classe que ele esta mudou de nome
                                if (alternativeClassName != null) {
                                    //ainda nao surgiu o mÃ©todo
                                    String alternativeMethodName = methodsAlternativeNameMap.get(alternativeClassName + ":" + javaMethod.getMethodSignature());
                                    if (alternativeMethodName == null) {
                                        if (ant != null) {
                                            JavaAbstract antAbstract = ant.getClassByName(javaClass.getFullQualifiedName());
                                            if (antAbstract != null && antAbstract.getClass() == JavaClass.class) {

                                                List<JavaMethod> methodsName = new LinkedList();
                                                List<JavaMethod> methodsNameAnt = ((JavaClass) antAbstract).getMethodsByName(javaMethod.getName());
                                                List<JavaMethod> methodsNameCurrent = javaClass.getMethodsByName(javaMethod.getName());
                                                for (JavaMethod jm : methodsNameAnt) {
                                                    boolean exists = false;
                                                    for (JavaMethod auxJm : methodsNameCurrent) {
                                                        if (auxJm.getMethodSignature().equals(jm.getMethodSignature())) {
                                                            exists = true;
                                                            break;
                                                        }
                                                    }
                                                    if (!exists) {
                                                        methodsName.add(jm);
                                                    }
                                                }

                                                if (!methodsName.isEmpty()) {
                                                    JavaMethod auxJm = closestMethod(javaMethod, methodsName);
                                                    alternativeMethodName = methodsAlternativeNameMap.get(javaClass.getFullQualifiedName() + ":" + auxJm.getMethodSignature());
                                                    if (alternativeMethodName == null) {
                                                        alternativeMethodName = alternativeClassName + ":" + auxJm.getMethodSignature();
                                                    }
                                                    System.out.println("Metodo mudou de assinatura: " + javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + "   -    " + alternativeMethodName);
                                                    methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), alternativeMethodName);

                                                }

                                            }
                                        }
                                    } else {
                                        //a classe mudou de nome e a encontramos com esse nome novo
                                        methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), alternativeMethodName);
                                        System.out.println("Metodo mudou nome completo (por causa da mudanÃ§a do nome da classe): " + javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + "   -    " + alternativeMethodName);
                                    }
                                } else {
                                    // a classe nao mudou de nome
                                    if (ant != null) {
                                        //verificaremos se algum mpetodo existia antes e sumiu depois, pode ter se transformado
                                        JavaAbstract antAbstract = ant.getClassByName(javaClass.getFullQualifiedName());
                                        if (antAbstract != null && antAbstract.getClass() == JavaClass.class) {

                                            List<JavaMethod> methodsName = new LinkedList();
                                            List<JavaMethod> methodsNameAnt = ((JavaClass) antAbstract).getMethodsByName(javaMethod.getName());
                                            List<JavaMethod> methodsNameCurrent = javaClass.getMethodsByName(javaMethod.getName());
                                            for (JavaMethod jm : methodsNameAnt) {
                                                boolean exists = false;
                                                for (JavaMethod auxJm : methodsNameCurrent) {
                                                    if (auxJm.getMethodSignature().equals(jm.getMethodSignature())) {
                                                        exists = true;
                                                        break;
                                                    }
                                                }
                                                if (!exists) {
                                                    methodsName.add(jm);
                                                }
                                            }

                                            if (!methodsName.isEmpty()) {
                                                JavaMethod auxJm = closestMethod(javaMethod, methodsName);
                                                String alternativeMethodName = methodsAlternativeNameMap.get(javaClass.getFullQualifiedName() + ":" + auxJm.getMethodSignature());
                                                if (alternativeMethodName == null) {
                                                    alternativeMethodName = javaClass.getFullQualifiedName() + ":" + auxJm.getMethodSignature();
                                                }
                                                System.out.println("Metodo mudou de assinatura: " + javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + "   -    " + alternativeMethodName);
                                                methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), alternativeMethodName);

                                            }

                                        }
                                    }
                                }
                            }

                        }
                    }
                }

                long i1 = System.currentTimeMillis();
                List<AnomalieItem> items = anomalieDao.getItemsByRevisionId(rev.getId());
                long i2 = System.currentTimeMillis();

                HashMap<String, Integer> hashAnomalies = new HashMap();
                for (AnomalieItem anomalieItem : items) {
                    hashAnomalies.put(anomalieItem.getItem(), anomalieItem.getAnomalieId());
                }

                System.out.println("Pegar anomalies da revisÃ£o " + rev.getId() + " : " + (i2 - i1) + " milisegundos");
                for (JavaAbstract javaAbstract : jp.getClasses()) {
                    if (ant != null) {
                        JavaClass javaClass = (JavaClass) javaAbstract;
                        JavaAbstract auxAbstract = ant.getClassByName(javaClass.getFullQualifiedName());

                        if (auxAbstract == null) {
                            List<JavaAbstract> antClasses = ant.getClassByLastName(javaAbstract.getName());
                            List<JavaAbstract> sameClasses = new LinkedList();
                            for (JavaAbstract antClass : antClasses) {
                                if (isSameJavaClass(javaClass, (JavaClass) antClass)) {
                                    sameClasses.add(antClass);
                                } else {
                                    System.out.println(javaClass.getFullQualifiedName() + " - is not same - " + antClass.getFullQualifiedName());
                                }
                            }
                            if (sameClasses.size() > 1) {
                                System.out.println("********** Erro, duas classes parecidas");
                            } else {
                                if (sameClasses.size() == 1) {
                                    auxAbstract = sameClasses.get(0);
                                }
                            }
                        }

                        if (auxAbstract != null && auxAbstract.getClass() == JavaClass.class) {
                            JavaClass antClass = (JavaClass) auxAbstract;

                            for (JavaMethod javaMethod : javaClass.getMethods()) {

                                String alternativeName = methodsAlternativeNameMap.get(javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature());
                                if (alternativeName == null) {
                                    alternativeName = javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature();
                                }
                                int i = birthHashMap.get(alternativeName);
                                if (k == i) {
                                    String text = "";

                                    Integer anomalieNum = hashAnomalies.get(javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature());
                                    if (anomalieNum != null) {
                                        if (anomalieNum == Constants.ANOMALIE_FEATURE_ENVY) {
                                            text = text + "add_method_anomalie_feature_envy";
                                        } else if (anomalieNum == Constants.ANOMALIE_SHOTGUN_SURGERY) {
                                            text = text + "add_method_anomalie_shotgun_surgery";
                                        } else if (anomalieNum == Constants.ANOMALIE_GOD_METHOD) {
                                            text = text + "add_method_anomalie_god_method";
                                        }
                                    } else {
                                        text = text + "add_method_healthy";
                                    }

                                    //numero de dados externos a classe que esta classe acessa, diretamente ou via metodos acessores
                                    text = text + " , " + "acess_to_acess_to_foreign_data_number=" + antClass.getAccessToForeignDataNumber();
                                    text = text + " , " + "number_of_attributes=" + antClass.getAttributes().size();
                                    //classes de outros pacotes que dependem de mim
                                    text = text + " , " + "number_of_client_classes=" + antClass.getClientClasses().size();
                                    //pacotes que dependem de mim
                                    text = text + " , " + "number_of_client_packages=" + antClass.getClientPackages().size();
                                    //classes de outros pacotes das quais essa classe depende
                                    text = text + " , " + "number_of_external_dependency_class=" + antClass.getExternalDependencyClasses().size();
                                    //pacotes os quais essa classe depende
                                    text = text + " , " + "number_of_external_dependency_package=" + antClass.getExternalDependencyPackages().size();

                                    text = text + " , " + "number_of_external_imports=" + antClass.getExternalImports().size();

                                    text = text + " , " + "number_of_implemented_interfaces=" + antClass.getImplementedInterfaces().size();
                                    //classes do mesmo pacote das quais essa classe depende
                                    text = text + " , " + "number_of_internal_dependency_classes=" + antClass.getInternalDependencyClasses().size();
                                    text = text + " , " + "number_of_methods=" + antClass.getMethods().size();
                                    double tcc = antClass.getNumberOfDirectConnections();
                                    int n = antClass.getMethods().size();
                                    tcc = tcc / ((n * (n - 1)) / 2);
                                    text = text + " , " + "tight_class_cohesion=" + tcc;
                                    text = text + " , " + "total_cyclomatic_complexity=" + antClass.getTotalCyclomaticComplexity();
                                    double averageClassCyclomaticComplexity = antClass.getTotalCyclomaticComplexity();
                                    averageClassCyclomaticComplexity = averageClassCyclomaticComplexity / antClass.getMethods().size();
                                    text = text + " , " + "average_class_cyclomatic_complexity=" + averageClassCyclomaticComplexity;
                                    //classes do mesmo pacote que dependem de mim
                                    text = text + " , " + "number_of_intra_package_dependent_classes=" + antClass.getintraPackageDependentClass().size();
                                    double classLocality = antClass.getInternalDependencyClasses().size();
                                    classLocality = classLocality / (antClass.getInternalDependencyClasses().size() + antClass.getExternalDependencyClasses().size());
                                    //classes do mesmo pacote das quais essa classe depende
                                    System.out.println("Internal dependency class: " + antClass.getInternalDependencyClasses().size() + "   external dependency class: " + antClass.getExternalDependencyClasses().size());
                                    text = text + " , " + "class_locality=" + classLocality;
                                    double packageCohesion = antClass.getJavaPackage().getPackageCohesion();
                                    text = text + " , " + "package_cohesion=" + packageCohesion;
                                    text = text + " , " + "method_signature=" + antClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature();

                                    writer0.println(text);

                                }

                            }
                        }
                    }
                }

                System.out.println("Calculou anomalies: " + k);

                ant = jp;
                k++;
                if (rev.getNext().size() == 0) {
                    rev = null;
                } else {
                    rev = rev.getNext().get(0);
                }
            }

            writer0.close();

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
        long t2 = System.currentTimeMillis();
        System.out.println("Classificando anomalias");
        //projectAnomalies.classifyAnomalies();
        System.out.println("Terminou de classificar");
        System.out.println("Tempo pra pegar anomalias: " + ((t2 - t1) / 60000) + " minutos");

    }

    public void createAprioriFile() {
        String path = System.getProperty("user.home") + "/.archd/";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path + "map_add_methods_analise_emerge_and_correct_difference.txt"));
            PrintWriter writer0 = new PrintWriter(path + "map_add_methods_analise_emerge_and_correct_difference_to_mining.txt", "UTF-8");
            PrintWriter writer1 = new PrintWriter(path + "map_add_methods_analise_emerge_and_correct_difference_map.txt", "UTF-8");
            HashMap<String, Integer> numberToNumberMap = new HashMap();
            String sCurrentLine;
            int count = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                String text[] = sCurrentLine.split(",");
                for (int i = 0; i < (text.length); i++) {
                    Integer num = numberToNumberMap.get(text[i].trim());
                    if (num == null) {
                        count++;
                        num = count;
                        numberToNumberMap.put(text[i].trim(), num);
                    }
                    writer0.print(num + " ");

                }
                writer0.println();
            }

            Set<String> keys = numberToNumberMap.keySet();
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String key = it.next();
                Integer value = numberToNumberMap.get(key);
                writer1.println(value + " " + key);

            }

            writer0.close();
            writer1.close();

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void createFinalFile() {
        String path = System.getProperty("user.home") + "/.archd/";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path + "map_add_methods_difference_map.txt"));
            BufferedReader br1 = new BufferedReader(new FileReader(path + "map_add_methods_difference_to_mining_output.txt"));
            PrintWriter writer0 = new PrintWriter(path + "map_add_methods_difference_final_healthly_to_analiser.txt", "UTF-8");
            HashMap<Integer, String> numberToNameMap = new HashMap();
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                String text[] = sCurrentLine.split(" ");
                numberToNameMap.put(Integer.valueOf(text[0].trim()), text[1].trim());

            }

            while ((sCurrentLine = br1.readLine()) != null) {
                String text[] = sCurrentLine.split("#SUP");
                String sides[] = text[0].split("==>");
                String ruleText = "";
                //side 0
                String side0[] = sides[0].split(" ");
                for (int i = 0; i < side0.length; i++) {
                    if (!side0[i].trim().equals("")) {
                        String aux = numberToNameMap.get(Integer.valueOf(side0[i].trim()));
                        ruleText = ruleText + aux + " ";
                    }
                }
                ruleText = ruleText + "=> ";
                //side 1
                String side1[] = sides[1].split(" ");
                for (int i = 0; i < side1.length; i++) {
                    if (!side1[i].trim().equals("")) {
                        String aux = numberToNameMap.get(Integer.valueOf(side1[i].trim()));
                        ruleText = ruleText + aux + " ";
                    }
                }
                ruleText = ruleText + "#SUP" + text[1];

                writer0.println(ruleText);
            }

            writer0.close();

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void getAddMethodsToAnalizer(ProjectRevisions newProjectRevisions, Project project, JavaConstructorService javaConstructorService) {

        long t1 = System.currentTimeMillis();
        Revision rev = newProjectRevisions.getRoot();
        int k = 0;
        while (rev != null) {
            k++;
            if (rev.getNext().size() == 0) {
                rev = null;
            } else {
                rev = rev.getNext().get(0);
            }
        }

        String path = System.getProperty("user.home") + "/.archd/";

        try {
            PrintWriter writer0 = new PrintWriter(path + "map_add_methods_analise_difference.txt", "UTF-8");

            rev = newProjectRevisions.getRoot();
            AnomalieDao anomalieDao = DataBaseFactory.getInstance().getAnomalieDao();
            HashMap<String, Integer> birthHashMap = new HashMap();
            HashMap<String, String> methodsAlternativeNameMap = new HashMap();
            HashMap<String, String> classesAlternativeNameMap = new HashMap();
            k = 0;
            JavaProject ant = null;
            while (rev != null) {

                JavaProject jp = null;
                //System.out.println("REV ID: "+rev.getId());
                System.gc();
                System.out.println("********************************* vai pegar um projeto completo");
                jp = javaConstructorService.getProjectByRevisionAndSetRevision(project.getName(), project.getCodeDirs(), project.getPath(), rev.getId(), newProjectRevisions.getName());

                for (JavaPackage javaPackage : jp.getPackages()) {
                    if (birthHashMap.get(javaPackage.getName()) == null) {
                        birthHashMap.put(javaPackage.getName(), k);
                    }
                }

                for (JavaAbstract javaAbstract : jp.getClasses()) {
                    JavaClass javaClass = (JavaClass) javaAbstract;

                    if (birthHashMap.get(javaClass.getFullQualifiedName()) == null) {
                        birthHashMap.put(javaClass.getFullQualifiedName(), k);

                        if (classesAlternativeNameMap.get(javaClass.getFullQualifiedName()) == null) {
                            if (ant != null) {
                                List<JavaAbstract> antClasses = ant.getClassByLastName(javaAbstract.getName());
                                List<JavaAbstract> sameClasses = new LinkedList();
                                for (JavaAbstract antClass : antClasses) {
                                    if (isSameJavaClass(javaClass, (JavaClass) antClass)) {
                                        sameClasses.add(antClass);
                                    } else {
                                        System.out.println(javaClass.getFullQualifiedName() + " - is not same - " + antClass.getFullQualifiedName());
                                    }
                                }
                                if (sameClasses.size() > 1) {
                                    System.out.println("********** Erro, duas classes parecidas");
                                } else {
                                    if (sameClasses.size() == 1) {
                                        String alternativeName = classesAlternativeNameMap.get(sameClasses.get(0).getFullQualifiedName());

                                        if (alternativeName == null) {
                                            alternativeName = sameClasses.get(0).getFullQualifiedName();
                                        }
                                        System.out.println("Classe mudou de nome: " + javaClass.getFullQualifiedName() + "   -    " + alternativeName);
                                        classesAlternativeNameMap.put(javaClass.getFullQualifiedName(), alternativeName);

                                    }
                                }

                            }
                        }

                    }
                    for (JavaMethod javaMethod : javaClass.getMethods()) {

                        //ainda nao foi dito a existencia desse metodo
                        if (birthHashMap.get(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature()) == null) {

                            birthHashMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), k);
                            if (methodsAlternativeNameMap.get(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature()) == null) {
                                methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature());
                                String alternativeClassName = classesAlternativeNameMap.get(javaClass.getFullQualifiedName());
                                //o mÃ©todo ainda nao surgiu e a classe que ele esta mudou de nome
                                if (alternativeClassName != null) {
                                    //ainda nao surgiu o mÃ©todo
                                    String alternativeMethodName = methodsAlternativeNameMap.get(alternativeClassName + ":" + javaMethod.getMethodSignature());
                                    if (alternativeMethodName == null) {
                                        if (ant != null) {
                                            JavaAbstract antAbstract = ant.getClassByName(javaClass.getFullQualifiedName());
                                            if (antAbstract != null && antAbstract.getClass() == JavaClass.class) {

                                                List<JavaMethod> methodsName = new LinkedList();
                                                List<JavaMethod> methodsNameAnt = ((JavaClass) antAbstract).getMethodsByName(javaMethod.getName());
                                                List<JavaMethod> methodsNameCurrent = javaClass.getMethodsByName(javaMethod.getName());
                                                for (JavaMethod jm : methodsNameAnt) {
                                                    boolean exists = false;
                                                    for (JavaMethod auxJm : methodsNameCurrent) {
                                                        if (auxJm.getMethodSignature().equals(jm.getMethodSignature())) {
                                                            exists = true;
                                                            break;
                                                        }
                                                    }
                                                    if (!exists) {
                                                        methodsName.add(jm);
                                                    }
                                                }

                                                if (!methodsName.isEmpty()) {
                                                    JavaMethod auxJm = closestMethod(javaMethod, methodsName);
                                                    alternativeMethodName = methodsAlternativeNameMap.get(javaClass.getFullQualifiedName() + ":" + auxJm.getMethodSignature());
                                                    if (alternativeMethodName == null) {
                                                        alternativeMethodName = alternativeClassName + ":" + auxJm.getMethodSignature();
                                                    }
                                                    System.out.println("Metodo mudou de assinatura: " + javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + "   -    " + alternativeMethodName);
                                                    methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), alternativeMethodName);

                                                }

                                            }
                                        }
                                    } else {
                                        //a classe mudou de nome e a encontramos com esse nome novo
                                        methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), alternativeMethodName);
                                        System.out.println("Metodo mudou nome completo (por causa da mudanÃ§a do nome da classe): " + javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + "   -    " + alternativeMethodName);
                                    }
                                } else {
                                    // a classe nao mudou de nome
                                    if (ant != null) {
                                        //verificaremos se algum mpetodo existia antes e sumiu depois, pode ter se transformado
                                        JavaAbstract antAbstract = ant.getClassByName(javaClass.getFullQualifiedName());
                                        if (antAbstract != null && antAbstract.getClass() == JavaClass.class) {

                                            List<JavaMethod> methodsName = new LinkedList();
                                            List<JavaMethod> methodsNameAnt = ((JavaClass) antAbstract).getMethodsByName(javaMethod.getName());
                                            List<JavaMethod> methodsNameCurrent = javaClass.getMethodsByName(javaMethod.getName());
                                            for (JavaMethod jm : methodsNameAnt) {
                                                boolean exists = false;
                                                for (JavaMethod auxJm : methodsNameCurrent) {
                                                    if (auxJm.getMethodSignature().equals(jm.getMethodSignature())) {
                                                        exists = true;
                                                        break;
                                                    }
                                                }
                                                if (!exists) {
                                                    methodsName.add(jm);
                                                }
                                            }

                                            if (!methodsName.isEmpty()) {
                                                JavaMethod auxJm = closestMethod(javaMethod, methodsName);
                                                String alternativeMethodName = methodsAlternativeNameMap.get(javaClass.getFullQualifiedName() + ":" + auxJm.getMethodSignature());
                                                if (alternativeMethodName == null) {
                                                    alternativeMethodName = javaClass.getFullQualifiedName() + ":" + auxJm.getMethodSignature();
                                                }
                                                System.out.println("Metodo mudou de assinatura: " + javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + "   -    " + alternativeMethodName);
                                                methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), alternativeMethodName);

                                            }

                                        }
                                    }
                                }
                            }

                        }
                    }
                }

                long i1 = System.currentTimeMillis();
                List<AnomalieItem> items = anomalieDao.getItemsByRevisionId(rev.getId());
                long i2 = System.currentTimeMillis();

                HashMap<String, Integer> hashAnomalies = new HashMap();
                for (AnomalieItem anomalieItem : items) {
                    hashAnomalies.put(anomalieItem.getItem(), anomalieItem.getAnomalieId());
                }

                System.out.println("Pegar anomalies da revisÃ£o " + rev.getId() + " : " + (i2 - i1) + " milisegundos");
                for (JavaAbstract javaAbstract : jp.getClasses()) {
                    if (ant != null) {
                        JavaClass javaClass = (JavaClass) javaAbstract;
                        JavaAbstract auxAbstract = ant.getClassByName(javaClass.getFullQualifiedName());

                        if (auxAbstract == null) {
                            List<JavaAbstract> antClasses = ant.getClassByLastName(javaAbstract.getName());
                            List<JavaAbstract> sameClasses = new LinkedList();
                            for (JavaAbstract antClass : antClasses) {
                                if (isSameJavaClass(javaClass, (JavaClass) antClass)) {
                                    sameClasses.add(antClass);
                                } else {
                                    System.out.println(javaClass.getFullQualifiedName() + " - is not same - " + antClass.getFullQualifiedName());
                                }
                            }
                            if (sameClasses.size() > 1) {
                                System.out.println("********** Erro, duas classes parecidas");
                            } else {
                                if (sameClasses.size() == 1) {
                                    auxAbstract = sameClasses.get(0);
                                }
                            }
                        }

                        if (auxAbstract != null && auxAbstract.getClass() == JavaClass.class) {
                            JavaClass antClass = (JavaClass) auxAbstract;

                            for (JavaMethod javaMethod : javaClass.getMethods()) {

                                String alternativeName = methodsAlternativeNameMap.get(javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature());
                                if (alternativeName == null) {
                                    alternativeName = javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature();
                                }
                                int i = birthHashMap.get(alternativeName);
                                if (k == i) {
                                    String text = "";

                                    Integer anomalieNum = hashAnomalies.get(javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature());
                                    if (anomalieNum != null) {
                                        if (anomalieNum == Constants.ANOMALIE_FEATURE_ENVY) {
                                            text = text + "add_method_anomalie_feature_envy";
                                        } else if (anomalieNum == Constants.ANOMALIE_SHOTGUN_SURGERY) {
                                            text = text + "add_method_anomalie_shotgun_surgery";
                                        } else if (anomalieNum == Constants.ANOMALIE_GOD_METHOD) {
                                            text = text + "add_method_anomalie_god_method";
                                        }
                                    } else {
                                        text = text + "add_method_healthy";
                                    }

                                    text = text + " , " + "acess_to_acess_to_foreign_data_number=" + antClass.getAccessToForeignDataNumber();
                                    int numberofAttributes = antClass.getAttributes().size();
                                    if (numberofAttributes == 0) {
                                        text = text + " , " + "number_of_attributes=" + numberofAttributes;
                                    } else if (numberofAttributes <= 5) {
                                        text = text + " , " + "number_of_attributes=1-5";
                                    } else if (numberofAttributes <= 10) {
                                        text = text + " , " + "number_of_attributes=6-10";
                                    } else if (numberofAttributes <= 15) {
                                        text = text + " , " + "number_of_attributes=11-15";
                                    } else if (numberofAttributes <= 20) {
                                        text = text + " , " + "number_of_attributes=16-20";
                                    } else if (numberofAttributes <= 25) {
                                        text = text + " , " + "number_of_attributes=20-25";
                                    } else if (numberofAttributes <= 30) {
                                        text = text + " , " + "number_of_attributes=26-30";
                                    } else if (numberofAttributes <= 35) {
                                        text = text + " , " + "number_of_attributes=31-35";
                                    } else if (numberofAttributes <= 40) {
                                        text = text + " , " + "number_of_attributes=36-40";
                                    } else {
                                        text = text + " , " + "number_of_attributes>=41";
                                    }

                                    //text = text + " , " + "number_of_external_imports=" + antClass.getExternalImports().size();
                                    text = text + " , " + "number_of_implemented_interfaces=" + antClass.getImplementedInterfaces().size();
                                    //classes do mesmo pacote das quais essa classe depende
                                    text = text + " , " + "number_of_internal_dependency_classes=" + antClass.getInternalDependencyClasses().size();

                                    //numero de metodos
                                    int numberOfMethods = antClass.getMethods().size();

                                    if (numberOfMethods == 0) {
                                        text = text + " , " + "number_of_methods=" + numberOfMethods;
                                    } else {
                                        int basenumberOfMethods = numberOfMethods / 5;
                                        basenumberOfMethods = basenumberOfMethods * 5;
                                        basenumberOfMethods++;
                                        text = text + " , " + "number_of_methods=" + basenumberOfMethods + "-" + (basenumberOfMethods + 4);
                                    }
                                    //text = text + " , " + "number_of_methods=" + antClass.getMethods().size();
                                    double tcc = antClass.getNumberOfDirectConnections();
                                    int n = antClass.getMethods().size();
                                    tcc = tcc / ((n * (n - 1)) / 2);
                                    if (tcc == Double.NaN) {
                                        text = text + " , " + "tight_class_cohesion=" + tcc;
                                    } else if (tcc == 0) {
                                        text = text + " , " + "tight_class_cohesion=" + tcc;
                                    } else {
                                        int baseTcc = Double.valueOf(tcc * 10).intValue();
                                        if (baseTcc < 9) {
                                            text = text + " , " + "tight_class_cohesion=0." + baseTcc + "-" + "0." + (baseTcc + 1);
                                        } else if (baseTcc == 9) {
                                            text = text + " , " + "tight_class_cohesion=0.9-1.0";
                                        } else {
                                            text = text + " , " + "tight_class_cohesion=1.0";
                                        }
                                    }
                                    int totalCyclomatic = antClass.getTotalCyclomaticComplexity();
                                    if (totalCyclomatic == 0) {
                                        text = text + " , " + "total_cyclomatic_complexity=" + numberOfMethods;
                                    } else {
                                        int baseTotalCiclometic = totalCyclomatic / 100;
                                        baseTotalCiclometic = baseTotalCiclometic * 100;
                                        baseTotalCiclometic++;
                                        text = text + " , " + "total_cyclomatic_complexity=" + baseTotalCiclometic + "-" + (baseTotalCiclometic + 99);
                                    }
                                    //text = text + " , " + "total_cyclomatic_complexity=" + antClass.getTotalCyclomaticComplexity();
                                    double averageClassCyclomaticComplexity = antClass.getTotalCyclomaticComplexity();
                                    averageClassCyclomaticComplexity = averageClassCyclomaticComplexity / antClass.getMethods().size();
                                    int baseAverageClassCyclomaticComplexity = Double.valueOf(averageClassCyclomaticComplexity).intValue();
                                    if (averageClassCyclomaticComplexity == 0) {
                                        text = text + " , " + "average_class_cyclomatic_complexity=" + baseAverageClassCyclomaticComplexity;
                                    } else {
                                        //int baseAverageClassCyclomaticComplexity = Double.valueOf(averageClassCyclomaticComplexity / 5).intValue();
                                        //baseAverageClassCyclomaticComplexity = baseAverageClassCyclomaticComplexity * 5;
                                        //baseAverageClassCyclomaticComplexity++;
                                        text = text + " , " + "average_class_cyclomatic_complexity=" + baseAverageClassCyclomaticComplexity;
                                    }
                                    //classes do mesmo pacote que dependem de mim
                                    int intrapackageDependenClass = antClass.getintraPackageDependentClass().size();
                                    if (intrapackageDependenClass == 0) {
                                        text = text + " , " + "number_of_intra_package_dependent_classes=" + intrapackageDependenClass;
                                    } else {
                                        int baseIntrapackageDependenClass = intrapackageDependenClass / 5;
                                        baseIntrapackageDependenClass = baseIntrapackageDependenClass * 5;
                                        baseIntrapackageDependenClass++;
                                        text = text + " , " + "number_of_intra_package_dependent_classes=" + baseIntrapackageDependenClass + "-" + (baseIntrapackageDependenClass + 4);
                                    }
                                    //text = text + " , " + "number_of_intra_package_dependent_classes=" + antClass.getintraPackageDependentClass().size();

                                    double classLocality = antClass.getInternalDependencyClasses().size();
                                    classLocality = classLocality / (antClass.getInternalDependencyClasses().size() + antClass.getExternalDependencyClasses().size());
                                    //classes do mesmo pacote das quais essa classe depende
                                    System.out.println("Internal dependency class: " + antClass.getInternalDependencyClasses().size() + "   external dependency class: " + antClass.getExternalDependencyClasses().size());
                                    if (classLocality == Double.NaN) {
                                        text = text + " , " + "class_locality=" + classLocality;
                                    } else if (classLocality == 0) {
                                        text = text + " , " + "class_locality=" + classLocality;
                                    } else {
                                        int baseClassLocality = Double.valueOf(classLocality * 10).intValue();
                                        if (baseClassLocality < 9) {
                                            text = text + " , " + "class_locality=0." + baseClassLocality + "-" + "0." + (baseClassLocality + 1);
                                        } else if (baseClassLocality == 9) {
                                            text = text + " , " + "class_locality=0.9-1.0";
                                        } else {
                                            text = text + " , " + "class_locality=1.0";
                                        }
                                    }
                                    //text = text + " , " + "class_locality=" + classLocality;
                                    double packageCohesion = antClass.getJavaPackage().getPackageCohesion();
                                    if (packageCohesion == Double.NaN) {
                                        text = text + " , " + "package_cohesion=" + packageCohesion;
                                    } else if (packageCohesion == 0) {
                                        text = text + " , " + "package_cohesion=" + packageCohesion;
                                    } else {
                                        int basePackageCohesion = Double.valueOf(packageCohesion * 10).intValue();
                                        if (basePackageCohesion < 9) {
                                            text = text + " , " + "package_cohesion=0." + basePackageCohesion + "-" + "0." + (basePackageCohesion + 1);
                                        } else if (basePackageCohesion == 9) {
                                            text = text + " , " + "package_cohesion=0.9-1.0";
                                        } else {
                                            text = text + " , " + "package_cohesion=1.0";
                                        }
                                    }
                                    //text = text + " , " + "package_cohesion=" + packageCohesion;
                                    //text = text + " , " + "method_signature=" + antClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature();

                                    writer0.println(text);

                                }

                            }
                        }
                    }
                }

                System.out.println("Calculou anomalies: " + k);

                ant = jp;
                k++;
                if (rev.getNext().size() == 0) {
                    rev = null;
                } else {
                    rev = rev.getNext().get(0);
                }
            }

            writer0.close();

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
        long t2 = System.currentTimeMillis();
        System.out.println("Classificando anomalias");
        //projectAnomalies.classifyAnomalies();
        System.out.println("Terminou de classificar");
        System.out.println("Tempo pra pegar anomalias: " + ((t2 - t1) / 60000) + " minutos");

    }

    public void getAddMethodsAdquiredToAnalizer(ProjectRevisions newProjectRevisions, Project project, JavaConstructorService javaConstructorService) {

        long t1 = System.currentTimeMillis();
        Revision rev = newProjectRevisions.getRoot();
        int k = 0;
        while (rev != null) {
            k++;
            if (rev.getNext().size() == 0) {
                rev = null;
            } else {
                rev = rev.getNext().get(0);
            }
        }

        String path = System.getProperty("user.home") + "/.archd/";
        HashMap<String, Integer> hashAntAnomalies = null;

        try {
            PrintWriter writer0 = new PrintWriter(path + "map_add_methods_analise_adquired_difference.txt", "UTF-8");

            rev = newProjectRevisions.getRoot();
            AnomalieDao anomalieDao = DataBaseFactory.getInstance().getAnomalieDao();
            HashMap<String, Integer> birthHashMap = new HashMap();
            HashMap<String, String> methodsAlternativeNameMap = new HashMap();
            HashMap<String, String> classesAlternativeNameMap = new HashMap();
            k = 0;
            JavaProject ant = null;
            while (rev != null) {

                JavaProject jp = null;
                //System.out.println("REV ID: "+rev.getId());
                System.gc();
                System.out.println("********************************* vai pegar um projeto completo");
                jp = javaConstructorService.getProjectByRevisionAndSetRevision(project.getName(), project.getCodeDirs(), project.getPath(), rev.getId(), newProjectRevisions.getName());

                for (JavaPackage javaPackage : jp.getPackages()) {
                    if (birthHashMap.get(javaPackage.getName()) == null) {
                        birthHashMap.put(javaPackage.getName(), k);
                    }
                }

                for (JavaAbstract javaAbstract : jp.getClasses()) {
                    JavaClass javaClass = (JavaClass) javaAbstract;

                    if (birthHashMap.get(javaClass.getFullQualifiedName()) == null) {
                        birthHashMap.put(javaClass.getFullQualifiedName(), k);

                        if (classesAlternativeNameMap.get(javaClass.getFullQualifiedName()) == null) {
                            if (ant != null) {
                                List<JavaAbstract> antClasses = ant.getClassByLastName(javaAbstract.getName());
                                List<JavaAbstract> sameClasses = new LinkedList();
                                for (JavaAbstract antClass : antClasses) {
                                    if (isSameJavaClass(javaClass, (JavaClass) antClass)) {
                                        sameClasses.add(antClass);
                                    } else {
                                        System.out.println(javaClass.getFullQualifiedName() + " - is not same - " + antClass.getFullQualifiedName());
                                    }
                                }
                                if (sameClasses.size() > 1) {
                                    System.out.println("********** Erro, duas classes parecidas");
                                } else {
                                    if (sameClasses.size() == 1) {
                                        String alternativeName = classesAlternativeNameMap.get(sameClasses.get(0).getFullQualifiedName());

                                        if (alternativeName == null) {
                                            alternativeName = sameClasses.get(0).getFullQualifiedName();
                                        }
                                        System.out.println("Classe mudou de nome: " + javaClass.getFullQualifiedName() + "   -    " + alternativeName);
                                        classesAlternativeNameMap.put(javaClass.getFullQualifiedName(), alternativeName);

                                    }
                                }

                            }
                        }

                    }
                    for (JavaMethod javaMethod : javaClass.getMethods()) {

                        //ainda nao foi dito a existencia desse metodo
                        if (birthHashMap.get(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature()) == null) {

                            birthHashMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), k);
                            if (methodsAlternativeNameMap.get(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature()) == null) {
                                methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature());
                                String alternativeClassName = classesAlternativeNameMap.get(javaClass.getFullQualifiedName());
                                //o mÃ©todo ainda nao surgiu e a classe que ele esta mudou de nome
                                if (alternativeClassName != null) {
                                    //ainda nao surgiu o mÃ©todo
                                    String alternativeMethodName = methodsAlternativeNameMap.get(alternativeClassName + ":" + javaMethod.getMethodSignature());
                                    if (alternativeMethodName == null) {
                                        if (ant != null) {
                                            JavaAbstract antAbstract = ant.getClassByName(javaClass.getFullQualifiedName());
                                            if (antAbstract != null && antAbstract.getClass() == JavaClass.class) {

                                                List<JavaMethod> methodsName = new LinkedList();
                                                List<JavaMethod> methodsNameAnt = ((JavaClass) antAbstract).getMethodsByName(javaMethod.getName());
                                                List<JavaMethod> methodsNameCurrent = javaClass.getMethodsByName(javaMethod.getName());
                                                for (JavaMethod jm : methodsNameAnt) {
                                                    boolean exists = false;
                                                    for (JavaMethod auxJm : methodsNameCurrent) {
                                                        if (auxJm.getMethodSignature().equals(jm.getMethodSignature())) {
                                                            exists = true;
                                                            break;
                                                        }
                                                    }
                                                    if (!exists) {
                                                        methodsName.add(jm);
                                                    }
                                                }

                                                if (!methodsName.isEmpty()) {
                                                    JavaMethod auxJm = closestMethod(javaMethod, methodsName);
                                                    alternativeMethodName = methodsAlternativeNameMap.get(javaClass.getFullQualifiedName() + ":" + auxJm.getMethodSignature());
                                                    if (alternativeMethodName == null) {
                                                        alternativeMethodName = alternativeClassName + ":" + auxJm.getMethodSignature();
                                                    }
                                                    System.out.println("Metodo mudou de assinatura: " + javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + "   -    " + alternativeMethodName);
                                                    methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), alternativeMethodName);

                                                }

                                            }
                                        }
                                    } else {
                                        //a classe mudou de nome e a encontramos com esse nome novo
                                        methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), alternativeMethodName);
                                        System.out.println("Metodo mudou nome completo (por causa da mudanÃ§a do nome da classe): " + javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + "   -    " + alternativeMethodName);
                                    }
                                } else {
                                    // a classe nao mudou de nome
                                    if (ant != null) {
                                        //verificaremos se algum mpetodo existia antes e sumiu depois, pode ter se transformado
                                        JavaAbstract antAbstract = ant.getClassByName(javaClass.getFullQualifiedName());
                                        if (antAbstract != null && antAbstract.getClass() == JavaClass.class) {

                                            List<JavaMethod> methodsName = new LinkedList();
                                            List<JavaMethod> methodsNameAnt = ((JavaClass) antAbstract).getMethodsByName(javaMethod.getName());
                                            List<JavaMethod> methodsNameCurrent = javaClass.getMethodsByName(javaMethod.getName());
                                            for (JavaMethod jm : methodsNameAnt) {
                                                boolean exists = false;
                                                for (JavaMethod auxJm : methodsNameCurrent) {
                                                    if (auxJm.getMethodSignature().equals(jm.getMethodSignature())) {
                                                        exists = true;
                                                        break;
                                                    }
                                                }
                                                if (!exists) {
                                                    methodsName.add(jm);
                                                }
                                            }

                                            if (!methodsName.isEmpty()) {
                                                JavaMethod auxJm = closestMethod(javaMethod, methodsName);
                                                String alternativeMethodName = methodsAlternativeNameMap.get(javaClass.getFullQualifiedName() + ":" + auxJm.getMethodSignature());
                                                if (alternativeMethodName == null) {
                                                    alternativeMethodName = javaClass.getFullQualifiedName() + ":" + auxJm.getMethodSignature();
                                                }
                                                System.out.println("Metodo mudou de assinatura: " + javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + "   -    " + alternativeMethodName);
                                                methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), alternativeMethodName);

                                            }

                                        }
                                    }
                                }
                            }

                        }
                    }
                }

                long i1 = System.currentTimeMillis();
                List<AnomalieItem> items = anomalieDao.getItemsByRevisionId(rev.getId());
                long i2 = System.currentTimeMillis();

                HashMap<String, Integer> hashAnomalies = new HashMap();
                for (AnomalieItem anomalieItem : items) {
                    String alternativeName = methodsAlternativeNameMap.get(anomalieItem.getItem());
                    if (alternativeName == null) {
                        alternativeName = anomalieItem.getItem();
                    }
                    hashAnomalies.put(alternativeName, anomalieItem.getAnomalieId());
                }

                System.out.println("Pegar anomalies da revisÃ£o " + rev.getId() + " : " + (i2 - i1) + " milisegundos");
                for (JavaAbstract javaAbstract : jp.getClasses()) {
                    if (ant != null && hashAntAnomalies != null) {
                        JavaClass javaClass = (JavaClass) javaAbstract;
                        JavaAbstract auxAbstract = ant.getClassByName(javaClass.getFullQualifiedName());

                        if (auxAbstract == null) {
                            List<JavaAbstract> antClasses = ant.getClassByLastName(javaAbstract.getName());
                            List<JavaAbstract> sameClasses = new LinkedList();
                            for (JavaAbstract antClass : antClasses) {
                                if (isSameJavaClass(javaClass, (JavaClass) antClass)) {
                                    sameClasses.add(antClass);
                                } else {
                                    System.out.println(javaClass.getFullQualifiedName() + " - is not same - " + antClass.getFullQualifiedName());
                                }
                            }
                            if (sameClasses.size() > 1) {
                                System.out.println("********** Erro, duas classes parecidas");
                            } else {
                                if (sameClasses.size() == 1) {
                                    auxAbstract = sameClasses.get(0);
                                }
                            }
                        }

                        if (auxAbstract != null && auxAbstract.getClass() == JavaClass.class) {
                            JavaClass antClass = (JavaClass) auxAbstract;

                            for (JavaMethod javaMethod : javaClass.getMethods()) {

                                String alternativeName = methodsAlternativeNameMap.get(javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature());
                                if (alternativeName == null) {
                                    alternativeName = javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature();
                                }
                                int i = birthHashMap.get(alternativeName);
                                if (k != i) {

                                    String text = "";

                                    Integer anomalieNum = hashAnomalies.get(alternativeName);
                                    Integer anomalieAntNum = hashAntAnomalies.get(alternativeName);
                                    if (anomalieNum != null && anomalieAntNum == null) {
                                        if (anomalieNum == Constants.ANOMALIE_FEATURE_ENVY) {
                                            text = text + "emerge_method_anomalie_feature_envy";
                                        } else if (anomalieNum == Constants.ANOMALIE_SHOTGUN_SURGERY) {
                                            text = text + "emerge_method_anomalie_shotgun_surgery";
                                        } else if (anomalieNum == Constants.ANOMALIE_GOD_METHOD) {
                                            text = text + "emerge_method_anomalie_god_method";
                                        }
                                        text = text + " , " + "acess_to_acess_to_foreign_data_number=" + antClass.getAccessToForeignDataNumber();
                                        int numberofAttributes = antClass.getAttributes().size();
                                        if (numberofAttributes == 0) {
                                            text = text + " , " + "number_of_attributes=" + numberofAttributes;
                                        } else if (numberofAttributes <= 5) {
                                            text = text + " , " + "number_of_attributes=1-5";
                                        } else if (numberofAttributes <= 10) {
                                            text = text + " , " + "number_of_attributes=6-10";
                                        } else if (numberofAttributes <= 15) {
                                            text = text + " , " + "number_of_attributes=11-15";
                                        } else if (numberofAttributes <= 20) {
                                            text = text + " , " + "number_of_attributes=16-20";
                                        } else if (numberofAttributes <= 25) {
                                            text = text + " , " + "number_of_attributes=20-25";
                                        } else if (numberofAttributes <= 30) {
                                            text = text + " , " + "number_of_attributes=26-30";
                                        } else if (numberofAttributes <= 35) {
                                            text = text + " , " + "number_of_attributes=31-35";
                                        } else if (numberofAttributes <= 40) {
                                            text = text + " , " + "number_of_attributes=36-40";
                                        } else {
                                            text = text + " , " + "number_of_attributes>=41";
                                        }

                                        //text = text + " , " + "number_of_external_imports=" + antClass.getExternalImports().size();
                                        text = text + " , " + "number_of_implemented_interfaces=" + antClass.getImplementedInterfaces().size();
                                        //classes do mesmo pacote das quais essa classe depende
                                        text = text + " , " + "number_of_internal_dependency_classes=" + antClass.getInternalDependencyClasses().size();

                                        //numero de metodos
                                        int numberOfMethods = antClass.getMethods().size();

                                        if (numberOfMethods == 0) {
                                            text = text + " , " + "number_of_methods=" + numberOfMethods;
                                        } else {
                                            int basenumberOfMethods = numberOfMethods / 5;
                                            basenumberOfMethods = basenumberOfMethods * 5;
                                            basenumberOfMethods++;
                                            text = text + " , " + "number_of_methods=" + basenumberOfMethods + "-" + (basenumberOfMethods + 4);
                                        }
                                        //text = text + " , " + "number_of_methods=" + antClass.getMethods().size();
                                        double tcc = antClass.getNumberOfDirectConnections();
                                        int n = antClass.getMethods().size();
                                        tcc = tcc / ((n * (n - 1)) / 2);
                                        if (tcc == Double.NaN) {
                                            text = text + " , " + "tight_class_cohesion=" + tcc;
                                        } else if (tcc == 0) {
                                            text = text + " , " + "tight_class_cohesion=" + tcc;
                                        } else {
                                            int baseTcc = Double.valueOf(tcc * 10).intValue();
                                            if (baseTcc < 9) {
                                                text = text + " , " + "tight_class_cohesion=0." + baseTcc + "-" + "0." + (baseTcc + 1);
                                            } else if (baseTcc == 9) {
                                                text = text + " , " + "tight_class_cohesion=0.9-1.0";
                                            } else {
                                                text = text + " , " + "tight_class_cohesion=1.0";
                                            }
                                        }
                                        int totalCyclomatic = antClass.getTotalCyclomaticComplexity();
                                        if (totalCyclomatic == 0) {
                                            text = text + " , " + "total_cyclomatic_complexity=" + numberOfMethods;
                                        } else {
                                            int baseTotalCiclometic = totalCyclomatic / 100;
                                            baseTotalCiclometic = baseTotalCiclometic * 100;
                                            baseTotalCiclometic++;
                                            text = text + " , " + "total_cyclomatic_complexity=" + baseTotalCiclometic + "-" + (baseTotalCiclometic + 99);
                                        }
                                        //text = text + " , " + "total_cyclomatic_complexity=" + antClass.getTotalCyclomaticComplexity();
                                        double averageClassCyclomaticComplexity = antClass.getTotalCyclomaticComplexity();
                                        averageClassCyclomaticComplexity = averageClassCyclomaticComplexity / antClass.getMethods().size();
                                        int baseAverageClassCyclomaticComplexity = Double.valueOf(averageClassCyclomaticComplexity).intValue();
                                        if (averageClassCyclomaticComplexity == 0) {
                                            text = text + " , " + "average_class_cyclomatic_complexity=" + baseAverageClassCyclomaticComplexity;
                                        } else {
                                            //int baseAverageClassCyclomaticComplexity = Double.valueOf(averageClassCyclomaticComplexity / 5).intValue();
                                            //baseAverageClassCyclomaticComplexity = baseAverageClassCyclomaticComplexity * 5;
                                            //baseAverageClassCyclomaticComplexity++;
                                            text = text + " , " + "average_class_cyclomatic_complexity=" + baseAverageClassCyclomaticComplexity;
                                        }
                                        //classes do mesmo pacote que dependem de mim
                                        int intrapackageDependenClass = antClass.getintraPackageDependentClass().size();
                                        if (intrapackageDependenClass == 0) {
                                            text = text + " , " + "number_of_intra_package_dependent_classes=" + intrapackageDependenClass;
                                        } else {
                                            int baseIntrapackageDependenClass = intrapackageDependenClass / 5;
                                            baseIntrapackageDependenClass = baseIntrapackageDependenClass * 5;
                                            baseIntrapackageDependenClass++;
                                            text = text + " , " + "number_of_intra_package_dependent_classes=" + baseIntrapackageDependenClass + "-" + (baseIntrapackageDependenClass + 4);
                                        }
                                        //text = text + " , " + "number_of_intra_package_dependent_classes=" + antClass.getintraPackageDependentClass().size();

                                        double classLocality = antClass.getInternalDependencyClasses().size();
                                        classLocality = classLocality / (antClass.getInternalDependencyClasses().size() + antClass.getExternalDependencyClasses().size());
                                        //classes do mesmo pacote das quais essa classe depende
                                        System.out.println("Internal dependency class: " + antClass.getInternalDependencyClasses().size() + "   external dependency class: " + antClass.getExternalDependencyClasses().size());
                                        if (classLocality == Double.NaN) {
                                            text = text + " , " + "class_locality=" + classLocality;
                                        } else if (classLocality == 0) {
                                            text = text + " , " + "class_locality=" + classLocality;
                                        } else {
                                            int baseClassLocality = Double.valueOf(classLocality * 10).intValue();
                                            if (baseClassLocality < 9) {
                                                text = text + " , " + "class_locality=0." + baseClassLocality + "-" + "0." + (baseClassLocality + 1);
                                            } else if (baseClassLocality == 9) {
                                                text = text + " , " + "class_locality=0.9-1.0";
                                            } else {
                                                text = text + " , " + "class_locality=1.0";
                                            }
                                        }
                                        //text = text + " , " + "class_locality=" + classLocality;
                                        double packageCohesion = antClass.getJavaPackage().getPackageCohesion();
                                        if (packageCohesion == Double.NaN) {
                                            text = text + " , " + "package_cohesion=" + packageCohesion;
                                        } else if (packageCohesion == 0) {
                                            text = text + " , " + "package_cohesion=" + packageCohesion;
                                        } else {
                                            int basePackageCohesion = Double.valueOf(packageCohesion * 10).intValue();
                                            if (basePackageCohesion < 9) {
                                                text = text + " , " + "package_cohesion=0." + basePackageCohesion + "-" + "0." + (basePackageCohesion + 1);
                                            } else if (basePackageCohesion == 9) {
                                                text = text + " , " + "package_cohesion=0.9-1.0";
                                            } else {
                                                text = text + " , " + "package_cohesion=1.0";
                                            }
                                        }
                                        //text = text + " , " + "package_cohesion=" + packageCohesion;
                                        //text = text + " , " + "method_signature=" + antClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature();

                                        writer0.println(text);
                                    } else {
                                        text = text + "add_method_healthy";
                                    }

                                }

                            }
                        }
                    }
                }

                System.out.println("Calculou anomalies: " + k);

                ant = jp;
                hashAntAnomalies = hashAnomalies;
                k++;
                if (rev.getNext().size() == 0) {
                    rev = null;
                } else {
                    rev = rev.getNext().get(0);
                }
            }

            writer0.close();

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
        long t2 = System.currentTimeMillis();
        System.out.println("Classificando anomalias");
        //projectAnomalies.classifyAnomalies();
        System.out.println("Terminou de classificar");
        System.out.println("Tempo pra pegar anomalias: " + ((t2 - t1) / 60000) + " minutos");

    }

    public void getAddMethodsEmergeAndCorrectToAnalizer(ProjectRevisions newProjectRevisions, Project project, JavaConstructorService javaConstructorService) {

        long t1 = System.currentTimeMillis();
        Revision rev = newProjectRevisions.getRoot();
        int k = 0;
        while (rev != null) {
            k++;
            if (rev.getNext().size() == 0) {
                rev = null;
            } else {
                rev = rev.getNext().get(0);
            }
        }

        String path = System.getProperty("user.home") + "/.archd/";
        HashMap<String, Integer> hashAntAnomalies = null;

        try {
            PrintWriter writer0 = new PrintWriter(path + "map_add_methods_analise_emerge_and_correct_difference.txt", "UTF-8");

            rev = newProjectRevisions.getRoot();
            AnomalieDao anomalieDao = DataBaseFactory.getInstance().getAnomalieDao();
            HashMap<String, Integer> birthHashMap = new HashMap();
            HashMap<String, String> methodsAlternativeNameMap = new HashMap();
            HashMap<String, String> classesAlternativeNameMap = new HashMap();
            k = 0;
            JavaProject ant = null;
            while (rev != null) {

                JavaProject jp = null;
                //System.out.println("REV ID: "+rev.getId());
                System.gc();
                System.out.println("********************************* vai pegar um projeto completo");
                jp = javaConstructorService.getProjectByRevisionAndSetRevision(project.getName(), project.getCodeDirs(), project.getPath(), rev.getId(), newProjectRevisions.getName());

                for (JavaPackage javaPackage : jp.getPackages()) {
                    if (birthHashMap.get(javaPackage.getName()) == null) {
                        birthHashMap.put(javaPackage.getName(), k);
                    }
                }

                for (JavaAbstract javaAbstract : jp.getClasses()) {
                    JavaClass javaClass = (JavaClass) javaAbstract;

                    if (birthHashMap.get(javaClass.getFullQualifiedName()) == null) {
                        birthHashMap.put(javaClass.getFullQualifiedName(), k);

                        if (classesAlternativeNameMap.get(javaClass.getFullQualifiedName()) == null) {
                            if (ant != null) {
                                List<JavaAbstract> antClasses = ant.getClassByLastName(javaAbstract.getName());
                                List<JavaAbstract> sameClasses = new LinkedList();
                                for (JavaAbstract antClass : antClasses) {
                                    if (isSameJavaClass(javaClass, (JavaClass) antClass)) {
                                        sameClasses.add(antClass);
                                    } else {
                                        System.out.println(javaClass.getFullQualifiedName() + " - is not same - " + antClass.getFullQualifiedName());
                                    }
                                }
                                if (sameClasses.size() > 1) {
                                    System.out.println("********** Erro, duas classes parecidas");
                                } else {
                                    if (sameClasses.size() == 1) {
                                        String alternativeName = classesAlternativeNameMap.get(sameClasses.get(0).getFullQualifiedName());

                                        if (alternativeName == null) {
                                            alternativeName = sameClasses.get(0).getFullQualifiedName();
                                        }
                                        System.out.println("Classe mudou de nome: " + javaClass.getFullQualifiedName() + "   -    " + alternativeName);
                                        classesAlternativeNameMap.put(javaClass.getFullQualifiedName(), alternativeName);

                                    }
                                }

                            }
                        }

                    }
                    List<JavaMethod> newMethods = new LinkedList();
                    for (JavaMethod javaMethod : javaClass.getMethods()) {

                        //ainda nao foi dito a existencia desse metodo
                        if (birthHashMap.get(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature()) == null) {

                            birthHashMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), k);
                            if (methodsAlternativeNameMap.get(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature()) == null) {
                                methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature());
                                String alternativeClassName = classesAlternativeNameMap.get(javaClass.getFullQualifiedName());
                                //o mÃ©todo ainda nao surgiu e a classe que ele esta mudou de nome
                                if (alternativeClassName != null) {
                                    //ainda nao surgiu o mÃ©todo
                                    String alternativeMethodName = methodsAlternativeNameMap.get(alternativeClassName + ":" + javaMethod.getMethodSignature());
                                    if (alternativeMethodName == null) {
                                        if (ant != null) {
                                            JavaAbstract antAbstract = ant.getClassByName(javaClass.getFullQualifiedName());
                                            if (antAbstract != null && antAbstract.getClass() == JavaClass.class) {

                                                List<JavaMethod> methodsName = new LinkedList();
                                                List<JavaMethod> methodsNameAnt = ((JavaClass) antAbstract).getMethodsByName(javaMethod.getName());
                                                List<JavaMethod> methodsNameCurrent = javaClass.getMethodsByName(javaMethod.getName());
                                                for (JavaMethod jm : methodsNameAnt) {
                                                    boolean exists = false;
                                                    for (JavaMethod auxJm : methodsNameCurrent) {
                                                        if (auxJm.getMethodSignature().equals(jm.getMethodSignature())) {
                                                            exists = true;
                                                            break;
                                                        }
                                                    }
                                                    if (!exists) {
                                                        methodsName.add(jm);
                                                    }
                                                }

                                                if (!methodsName.isEmpty()) {
                                                    JavaMethod auxJm = closestMethod(javaMethod, methodsName);
                                                    alternativeMethodName = methodsAlternativeNameMap.get(javaClass.getFullQualifiedName() + ":" + auxJm.getMethodSignature());
                                                    if (alternativeMethodName == null) {
                                                        alternativeMethodName = alternativeClassName + ":" + auxJm.getMethodSignature();
                                                    }
                                                    System.out.println("Metodo mudou de assinatura: " + javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + "   -    " + alternativeMethodName);
                                                    methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), alternativeMethodName);

                                                }

                                            }
                                        }
                                    } else {
                                        //a classe mudou de nome e a encontramos com esse nome novo
                                        methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), alternativeMethodName);
                                        System.out.println("Metodo mudou nome completo (por causa da mudanÃ§a do nome da classe): " + javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + "   -    " + alternativeMethodName);
                                    }
                                } else {
                                    // a classe nao mudou de nome
                                    if (ant != null) {
                                        //adiciona ao campo dos metodos novos
                                        newMethods.add(javaMethod);
                                        //verificaremos se algum mpetodo existia antes e sumiu depois, pode ter se transformado
//                                        JavaAbstract antAbstract = ant.getClassByName(javaClass.getFullQualifiedName());
//                                        if (antAbstract != null && antAbstract.getClass() == JavaClass.class) {
//
//                                            List<JavaMethod> methodsName = new LinkedList();
//                                            List<JavaMethod> methodsNameAnt = ((JavaClass) antAbstract).getMethodsByName(javaMethod.getName());
//                                            List<JavaMethod> methodsNameCurrent = javaClass.getMethodsByName(javaMethod.getName());
//                                            for (JavaMethod jm : methodsNameAnt) {
//                                                boolean exists = false;
//                                                for (JavaMethod auxJm : methodsNameCurrent) {
//                                                    if (auxJm.getMethodSignature().equals(jm.getMethodSignature())) {
//                                                        exists = true;
//                                                        break;
//                                                    }
//                                                }
//                                                if (!exists) {
//                                                    methodsName.add(jm);
//                                                }
//                                            }
//
//                                            if (!methodsName.isEmpty()) {
//                                                JavaMethod auxJm = closestMethod(javaMethod, methodsName);
//                                                String alternativeMethodName = methodsAlternativeNameMap.get(javaClass.getFullQualifiedName() + ":" + auxJm.getMethodSignature());
//                                                if (alternativeMethodName == null) {
//                                                    alternativeMethodName = javaClass.getFullQualifiedName() + ":" + auxJm.getMethodSignature();
//                                                }
//                                                System.out.println("Metodo mudou de assinatura: " + javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + "   -    " + alternativeMethodName);
//                                                methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), alternativeMethodName);
//
//                                            }
//
//                                        }
                                    }
                                }
                            }

                        }
                    }
                    boolean terminar = false;
                    if (!newMethods.isEmpty()) {
                        while (!terminar) {
                            if (newMethods.isEmpty()) {
                                terminar = true;
                            } else {
                                JavaMethod javaMethodFirst = newMethods.get(0);
                                List<JavaMethod> methodsWithSameName = new LinkedList();
                                methodsWithSameName.add(javaMethodFirst);
                                for (int i = 1; i < newMethods.size(); i++) {
                                    if (newMethods.get(i).getName().equals(javaMethodFirst.getName())) {
                                        methodsWithSameName.add(newMethods.get(i));
                                    }
                                }
                                for (JavaMethod jm : methodsWithSameName) {
                                    newMethods.remove(jm);
                                }

                                for (JavaMethod javaMethod : methodsWithSameName) {
                                    //verificaremos se algum mpetodo existia antes e sumiu depois, pode ter se transformado
                                    JavaAbstract antAbstract = ant.getClassByName(javaClass.getFullQualifiedName());
                                    if (antAbstract != null && antAbstract.getClass() == JavaClass.class) {

                                        List<JavaMethod> methodsName = new LinkedList();
                                        List<JavaMethod> methodsNameAnt = ((JavaClass) antAbstract).getMethodsByName(javaMethod.getName());
                                        List<JavaMethod> methodsNameCurrent = javaClass.getMethodsByName(javaMethod.getName());
                                        for (JavaMethod jm : methodsNameAnt) {
                                            boolean exists = false;
                                            for (JavaMethod auxJm : methodsNameCurrent) {
                                                if (auxJm.getMethodSignature().equals(jm.getMethodSignature())) {
                                                    exists = true;
                                                    break;
                                                }
                                            }
                                            if (!exists) {
                                                methodsName.add(jm);
                                            }
                                        }

                                        if (!methodsName.isEmpty()) {
                                            JavaMethod auxJm = closestMethod(javaMethod, methodsWithSameName, methodsName);
                                            if (auxJm != null) {
                                                String alternativeMethodName = methodsAlternativeNameMap.get(javaClass.getFullQualifiedName() + ":" + auxJm.getMethodSignature());
                                                if (alternativeMethodName == null) {
                                                    alternativeMethodName = javaClass.getFullQualifiedName() + ":" + auxJm.getMethodSignature();
                                                }
                                                System.out.println("Metodo mudou de assinatura: " + javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + "   -    " + alternativeMethodName);
                                                methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), alternativeMethodName);
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }
                }

                long i1 = System.currentTimeMillis();
                List<AnomalieItem> items = anomalieDao.getItemsByRevisionId(rev.getId());
                long i2 = System.currentTimeMillis();

                HashMap<String, Integer> hashAnomalies = new HashMap();
                for (AnomalieItem anomalieItem : items) {
                    String alternativeName = methodsAlternativeNameMap.get(anomalieItem.getItem());
                    if (alternativeName == null) {
                        alternativeName = anomalieItem.getItem();
                    }
                    hashAnomalies.put(alternativeName, anomalieItem.getAnomalieId());
                }

                System.out.println("Pegar anomalies da revisÃ£o " + rev.getId() + " : " + (i2 - i1) + " milisegundos");
                for (JavaAbstract javaAbstract : jp.getClasses()) {
                    if (ant != null && hashAntAnomalies != null) {
                        JavaClass javaClass = (JavaClass) javaAbstract;
                        JavaAbstract auxAbstract = ant.getClassByName(javaClass.getFullQualifiedName());

                        if (auxAbstract == null) {
                            List<JavaAbstract> antClasses = ant.getClassByLastName(javaAbstract.getName());
                            List<JavaAbstract> sameClasses = new LinkedList();
                            for (JavaAbstract antClass : antClasses) {
                                if (isSameJavaClass(javaClass, (JavaClass) antClass)) {
                                    sameClasses.add(antClass);
                                } else {
                                    System.out.println(javaClass.getFullQualifiedName() + " - is not same - " + antClass.getFullQualifiedName());
                                }
                            }
                            if (sameClasses.size() > 1) {
                                System.out.println("********** Erro, duas classes parecidas");
                            } else {
                                if (sameClasses.size() == 1) {
                                    auxAbstract = sameClasses.get(0);
                                }
                            }
                        }

                        if (auxAbstract != null && auxAbstract.getClass() == JavaClass.class) {
                            JavaClass antClass = (JavaClass) auxAbstract;

                            boolean possuiAlgumMetodo = false;

                            for (JavaMethod javaMethod : javaClass.getMethods()) {

                                String alternativeName = methodsAlternativeNameMap.get(javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature());
                                if (alternativeName == null) {
                                    alternativeName = javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature();
                                }
                                int i = birthHashMap.get(alternativeName);
                                if (k != i) {

                                    String text = "";

                                    Integer anomalieNum = hashAnomalies.get(alternativeName);
                                    Integer anomalieAntNum = hashAntAnomalies.get(alternativeName);
                                    if (anomalieNum == null && anomalieAntNum != null) {
                                        possuiAlgumMetodo = true;
                                        if (anomalieAntNum == Constants.ANOMALIE_FEATURE_ENVY) {
                                            text = text + "correct_method_anomalie_feature_envy";
                                        } else if (anomalieAntNum == Constants.ANOMALIE_SHOTGUN_SURGERY) {
                                            text = text + "correct_method_anomalie_shotgun_surgery";
                                        } else if (anomalieAntNum == Constants.ANOMALIE_GOD_METHOD) {
                                            text = text + "correct_method_anomalie_god_method";
                                        }
                                        text = text + " , " + "acess_to_acess_to_foreign_data_number=" + antClass.getAccessToForeignDataNumber();
                                        int numberofAttributes = antClass.getAttributes().size();
                                        if (numberofAttributes == 0) {
                                            text = text + " , " + "number_of_attributes=" + numberofAttributes;
                                        } else if (numberofAttributes <= 5) {
                                            text = text + " , " + "number_of_attributes=1-5";
                                        } else if (numberofAttributes <= 10) {
                                            text = text + " , " + "number_of_attributes=6-10";
                                        } else if (numberofAttributes <= 15) {
                                            text = text + " , " + "number_of_attributes=11-15";
                                        } else if (numberofAttributes <= 20) {
                                            text = text + " , " + "number_of_attributes=16-20";
                                        } else if (numberofAttributes <= 25) {
                                            text = text + " , " + "number_of_attributes=20-25";
                                        } else if (numberofAttributes <= 30) {
                                            text = text + " , " + "number_of_attributes=26-30";
                                        } else if (numberofAttributes <= 35) {
                                            text = text + " , " + "number_of_attributes=31-35";
                                        } else if (numberofAttributes <= 40) {
                                            text = text + " , " + "number_of_attributes=36-40";
                                        } else {
                                            text = text + " , " + "number_of_attributes>=41";
                                        }

                                        //text = text + " , " + "number_of_external_imports=" + antClass.getExternalImports().size();
                                        text = text + " , " + "number_of_implemented_interfaces=" + antClass.getImplementedInterfaces().size();
                                        //classes do mesmo pacote das quais essa classe depende
                                        text = text + " , " + "number_of_internal_dependency_classes=" + antClass.getInternalDependencyClasses().size();

                                        //numero de metodos
                                        int numberOfMethods = antClass.getMethods().size();

                                        if (numberOfMethods == 0) {
                                            text = text + " , " + "number_of_methods=" + numberOfMethods;
                                        } else {
                                            int basenumberOfMethods = numberOfMethods / 5;
                                            basenumberOfMethods = basenumberOfMethods * 5;
                                            basenumberOfMethods++;
                                            text = text + " , " + "number_of_methods=" + basenumberOfMethods + "-" + (basenumberOfMethods + 4);
                                        }
                                        //text = text + " , " + "number_of_methods=" + antClass.getMethods().size();
                                        double tcc = antClass.getNumberOfDirectConnections();
                                        int n = antClass.getMethods().size();
                                        tcc = tcc / ((n * (n - 1)) / 2);
                                        if (tcc == Double.NaN) {
                                            text = text + " , " + "tight_class_cohesion=" + tcc;
                                        } else if (tcc == 0) {
                                            text = text + " , " + "tight_class_cohesion=" + tcc;
                                        } else {
                                            int baseTcc = Double.valueOf(tcc * 10).intValue();
                                            if (baseTcc < 9) {
                                                text = text + " , " + "tight_class_cohesion=0." + baseTcc + "-" + "0." + (baseTcc + 1);
                                            } else if (baseTcc == 9) {
                                                text = text + " , " + "tight_class_cohesion=0.9-1.0";
                                            } else {
                                                text = text + " , " + "tight_class_cohesion=1.0";
                                            }
                                        }
                                        int totalCyclomatic = antClass.getTotalCyclomaticComplexity();
                                        if (totalCyclomatic == 0) {
                                            text = text + " , " + "total_cyclomatic_complexity=" + numberOfMethods;
                                        } else {
                                            int baseTotalCiclometic = totalCyclomatic / 100;
                                            baseTotalCiclometic = baseTotalCiclometic * 100;
                                            baseTotalCiclometic++;
                                            text = text + " , " + "total_cyclomatic_complexity=" + baseTotalCiclometic + "-" + (baseTotalCiclometic + 99);
                                        }
                                        //text = text + " , " + "total_cyclomatic_complexity=" + antClass.getTotalCyclomaticComplexity();
                                        double averageClassCyclomaticComplexity = antClass.getTotalCyclomaticComplexity();
                                        averageClassCyclomaticComplexity = averageClassCyclomaticComplexity / antClass.getMethods().size();
                                        int baseAverageClassCyclomaticComplexity = Double.valueOf(averageClassCyclomaticComplexity).intValue();
                                        if (averageClassCyclomaticComplexity == 0) {
                                            text = text + " , " + "average_class_cyclomatic_complexity=" + baseAverageClassCyclomaticComplexity;
                                        } else {
                                            //int baseAverageClassCyclomaticComplexity = Double.valueOf(averageClassCyclomaticComplexity / 5).intValue();
                                            //baseAverageClassCyclomaticComplexity = baseAverageClassCyclomaticComplexity * 5;
                                            //baseAverageClassCyclomaticComplexity++;
                                            text = text + " , " + "average_class_cyclomatic_complexity=" + baseAverageClassCyclomaticComplexity;
                                        }
                                        //classes do mesmo pacote que dependem de mim
                                        int intrapackageDependenClass = antClass.getintraPackageDependentClass().size();
                                        if (intrapackageDependenClass == 0) {
                                            text = text + " , " + "number_of_intra_package_dependent_classes=" + intrapackageDependenClass;
                                        } else {
                                            int baseIntrapackageDependenClass = intrapackageDependenClass / 5;
                                            baseIntrapackageDependenClass = baseIntrapackageDependenClass * 5;
                                            baseIntrapackageDependenClass++;
                                            text = text + " , " + "number_of_intra_package_dependent_classes=" + baseIntrapackageDependenClass + "-" + (baseIntrapackageDependenClass + 4);
                                        }
                                        //text = text + " , " + "number_of_intra_package_dependent_classes=" + antClass.getintraPackageDependentClass().size();

                                        double classLocality = antClass.getInternalDependencyClasses().size();
                                        classLocality = classLocality / (antClass.getInternalDependencyClasses().size() + antClass.getExternalDependencyClasses().size());
                                        //classes do mesmo pacote das quais essa classe depende
                                        System.out.println("Internal dependency class: " + antClass.getInternalDependencyClasses().size() + "   external dependency class: " + antClass.getExternalDependencyClasses().size());
                                        if (classLocality == Double.NaN) {
                                            text = text + " , " + "class_locality=" + classLocality;
                                        } else if (classLocality == 0) {
                                            text = text + " , " + "class_locality=" + classLocality;
                                        } else {
                                            int baseClassLocality = Double.valueOf(classLocality * 10).intValue();
                                            if (baseClassLocality < 9) {
                                                text = text + " , " + "class_locality=0." + baseClassLocality + "-" + "0." + (baseClassLocality + 1);
                                            } else if (baseClassLocality == 9) {
                                                text = text + " , " + "class_locality=0.9-1.0";
                                            } else {
                                                text = text + " , " + "class_locality=1.0";
                                            }
                                        }
                                        //text = text + " , " + "class_locality=" + classLocality;
                                        double packageCohesion = antClass.getJavaPackage().getPackageCohesion();
                                        if (packageCohesion == Double.NaN) {
                                            text = text + " , " + "package_cohesion=" + packageCohesion;
                                        } else if (packageCohesion == 0) {
                                            text = text + " , " + "package_cohesion=" + packageCohesion;
                                        } else {
                                            int basePackageCohesion = Double.valueOf(packageCohesion * 10).intValue();
                                            if (basePackageCohesion < 9) {
                                                text = text + " , " + "package_cohesion=0." + basePackageCohesion + "-" + "0." + (basePackageCohesion + 1);
                                            } else if (basePackageCohesion == 9) {
                                                text = text + " , " + "package_cohesion=0.9-1.0";
                                            } else {
                                                text = text + " , " + "package_cohesion=1.0";
                                            }
                                        }
                                        //text = text + " , " + "package_cohesion=" + packageCohesion;
                                        //text = text + " , " + "method_signature=" + antClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature();

                                        writer0.println(text);
                                    } else if (anomalieNum != null && anomalieAntNum == null) {
                                        possuiAlgumMetodo = true;
                                        if (anomalieNum == Constants.ANOMALIE_FEATURE_ENVY) {
                                            text = text + "emerge_method_anomalie_feature_envy";
                                        } else if (anomalieNum == Constants.ANOMALIE_SHOTGUN_SURGERY) {
                                            text = text + "emerge_method_anomalie_shotgun_surgery";
                                        } else if (anomalieNum == Constants.ANOMALIE_GOD_METHOD) {
                                            text = text + "emerge_method_anomalie_god_method";
                                        }
                                        text = text + " , " + "acess_to_acess_to_foreign_data_number=" + antClass.getAccessToForeignDataNumber();
                                        int numberofAttributes = antClass.getAttributes().size();
                                        if (numberofAttributes == 0) {
                                            text = text + " , " + "number_of_attributes=" + numberofAttributes;
                                        } else if (numberofAttributes <= 5) {
                                            text = text + " , " + "number_of_attributes=1-5";
                                        } else if (numberofAttributes <= 10) {
                                            text = text + " , " + "number_of_attributes=6-10";
                                        } else if (numberofAttributes <= 15) {
                                            text = text + " , " + "number_of_attributes=11-15";
                                        } else if (numberofAttributes <= 20) {
                                            text = text + " , " + "number_of_attributes=16-20";
                                        } else if (numberofAttributes <= 25) {
                                            text = text + " , " + "number_of_attributes=20-25";
                                        } else if (numberofAttributes <= 30) {
                                            text = text + " , " + "number_of_attributes=26-30";
                                        } else if (numberofAttributes <= 35) {
                                            text = text + " , " + "number_of_attributes=31-35";
                                        } else if (numberofAttributes <= 40) {
                                            text = text + " , " + "number_of_attributes=36-40";
                                        } else {
                                            text = text + " , " + "number_of_attributes>=41";
                                        }

                                        //text = text + " , " + "number_of_external_imports=" + antClass.getExternalImports().size();
                                        text = text + " , " + "number_of_implemented_interfaces=" + antClass.getImplementedInterfaces().size();
                                        //classes do mesmo pacote das quais essa classe depende
                                        text = text + " , " + "number_of_internal_dependency_classes=" + antClass.getInternalDependencyClasses().size();

                                        //numero de metodos
                                        int numberOfMethods = antClass.getMethods().size();

                                        if (numberOfMethods == 0) {
                                            text = text + " , " + "number_of_methods=" + numberOfMethods;
                                        } else {
                                            int basenumberOfMethods = numberOfMethods / 5;
                                            basenumberOfMethods = basenumberOfMethods * 5;
                                            basenumberOfMethods++;
                                            text = text + " , " + "number_of_methods=" + basenumberOfMethods + "-" + (basenumberOfMethods + 4);
                                        }
                                        //text = text + " , " + "number_of_methods=" + antClass.getMethods().size();
                                        double tcc = antClass.getNumberOfDirectConnections();
                                        int n = antClass.getMethods().size();
                                        tcc = tcc / ((n * (n - 1)) / 2);
                                        if (tcc == Double.NaN) {
                                            text = text + " , " + "tight_class_cohesion=" + tcc;
                                        } else if (tcc == 0) {
                                            text = text + " , " + "tight_class_cohesion=" + tcc;
                                        } else {
                                            int baseTcc = Double.valueOf(tcc * 10).intValue();
                                            if (baseTcc < 9) {
                                                text = text + " , " + "tight_class_cohesion=0." + baseTcc + "-" + "0." + (baseTcc + 1);
                                            } else if (baseTcc == 9) {
                                                text = text + " , " + "tight_class_cohesion=0.9-1.0";
                                            } else {
                                                text = text + " , " + "tight_class_cohesion=1.0";
                                            }
                                        }
                                        int totalCyclomatic = antClass.getTotalCyclomaticComplexity();
                                        if (totalCyclomatic == 0) {
                                            text = text + " , " + "total_cyclomatic_complexity=" + numberOfMethods;
                                        } else {
                                            int baseTotalCiclometic = totalCyclomatic / 100;
                                            baseTotalCiclometic = baseTotalCiclometic * 100;
                                            baseTotalCiclometic++;
                                            text = text + " , " + "total_cyclomatic_complexity=" + baseTotalCiclometic + "-" + (baseTotalCiclometic + 99);
                                        }
                                        //text = text + " , " + "total_cyclomatic_complexity=" + antClass.getTotalCyclomaticComplexity();
                                        double averageClassCyclomaticComplexity = antClass.getTotalCyclomaticComplexity();
                                        averageClassCyclomaticComplexity = averageClassCyclomaticComplexity / antClass.getMethods().size();
                                        int baseAverageClassCyclomaticComplexity = Double.valueOf(averageClassCyclomaticComplexity).intValue();
                                        if (averageClassCyclomaticComplexity == 0) {
                                            text = text + " , " + "average_class_cyclomatic_complexity=" + baseAverageClassCyclomaticComplexity;
                                        } else {
                                            //int baseAverageClassCyclomaticComplexity = Double.valueOf(averageClassCyclomaticComplexity / 5).intValue();
                                            //baseAverageClassCyclomaticComplexity = baseAverageClassCyclomaticComplexity * 5;
                                            //baseAverageClassCyclomaticComplexity++;
                                            text = text + " , " + "average_class_cyclomatic_complexity=" + baseAverageClassCyclomaticComplexity;
                                        }
                                        //classes do mesmo pacote que dependem de mim
                                        int intrapackageDependenClass = antClass.getintraPackageDependentClass().size();
                                        if (intrapackageDependenClass == 0) {
                                            text = text + " , " + "number_of_intra_package_dependent_classes=" + intrapackageDependenClass;
                                        } else {
                                            int baseIntrapackageDependenClass = intrapackageDependenClass / 5;
                                            baseIntrapackageDependenClass = baseIntrapackageDependenClass * 5;
                                            baseIntrapackageDependenClass++;
                                            text = text + " , " + "number_of_intra_package_dependent_classes=" + baseIntrapackageDependenClass + "-" + (baseIntrapackageDependenClass + 4);
                                        }
                                        //text = text + " , " + "number_of_intra_package_dependent_classes=" + antClass.getintraPackageDependentClass().size();

                                        double classLocality = antClass.getInternalDependencyClasses().size();
                                        classLocality = classLocality / (antClass.getInternalDependencyClasses().size() + antClass.getExternalDependencyClasses().size());
                                        //classes do mesmo pacote das quais essa classe depende
                                        System.out.println("Internal dependency class: " + antClass.getInternalDependencyClasses().size() + "   external dependency class: " + antClass.getExternalDependencyClasses().size());
                                        if (classLocality == Double.NaN) {
                                            text = text + " , " + "class_locality=" + classLocality;
                                        } else if (classLocality == 0) {
                                            text = text + " , " + "class_locality=" + classLocality;
                                        } else {
                                            int baseClassLocality = Double.valueOf(classLocality * 10).intValue();
                                            if (baseClassLocality < 9) {
                                                text = text + " , " + "class_locality=0." + baseClassLocality + "-" + "0." + (baseClassLocality + 1);
                                            } else if (baseClassLocality == 9) {
                                                text = text + " , " + "class_locality=0.9-1.0";
                                            } else {
                                                text = text + " , " + "class_locality=1.0";
                                            }
                                        }
                                        //text = text + " , " + "class_locality=" + classLocality;
                                        double packageCohesion = antClass.getJavaPackage().getPackageCohesion();
                                        if (packageCohesion == Double.NaN) {
                                            text = text + " , " + "package_cohesion=" + packageCohesion;
                                        } else if (packageCohesion == 0) {
                                            text = text + " , " + "package_cohesion=" + packageCohesion;
                                        } else {
                                            int basePackageCohesion = Double.valueOf(packageCohesion * 10).intValue();
                                            if (basePackageCohesion < 9) {
                                                text = text + " , " + "package_cohesion=0." + basePackageCohesion + "-" + "0." + (basePackageCohesion + 1);
                                            } else if (basePackageCohesion == 9) {
                                                text = text + " , " + "package_cohesion=0.9-1.0";
                                            } else {
                                                text = text + " , " + "package_cohesion=1.0";
                                            }
                                        }
                                        //text = text + " , " + "package_cohesion=" + packageCohesion;
                                        //text = text + " , " + "method_signature=" + antClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature();

                                        writer0.println(text);
                                    } else if (anomalieNum != null && anomalieAntNum != null) {
                                        possuiAlgumMetodo = true;
                                        if (anomalieNum == Constants.ANOMALIE_FEATURE_ENVY) {
                                            text = text + "continue_method_anomalie_feature_envy";
                                        } else if (anomalieNum == Constants.ANOMALIE_SHOTGUN_SURGERY) {
                                            text = text + "continue_method_anomalie_shotgun_surgery";
                                        } else if (anomalieNum == Constants.ANOMALIE_GOD_METHOD) {
                                            text = text + "continue_method_anomalie_god_method";
                                        }
                                        text = text + " , " + "acess_to_acess_to_foreign_data_number=" + antClass.getAccessToForeignDataNumber();
                                        int numberofAttributes = antClass.getAttributes().size();
                                        if (numberofAttributes == 0) {
                                            text = text + " , " + "number_of_attributes=" + numberofAttributes;
                                        } else if (numberofAttributes <= 5) {
                                            text = text + " , " + "number_of_attributes=1-5";
                                        } else if (numberofAttributes <= 10) {
                                            text = text + " , " + "number_of_attributes=6-10";
                                        } else if (numberofAttributes <= 15) {
                                            text = text + " , " + "number_of_attributes=11-15";
                                        } else if (numberofAttributes <= 20) {
                                            text = text + " , " + "number_of_attributes=16-20";
                                        } else if (numberofAttributes <= 25) {
                                            text = text + " , " + "number_of_attributes=20-25";
                                        } else if (numberofAttributes <= 30) {
                                            text = text + " , " + "number_of_attributes=26-30";
                                        } else if (numberofAttributes <= 35) {
                                            text = text + " , " + "number_of_attributes=31-35";
                                        } else if (numberofAttributes <= 40) {
                                            text = text + " , " + "number_of_attributes=36-40";
                                        } else {
                                            text = text + " , " + "number_of_attributes>=41";
                                        }

                                        //text = text + " , " + "number_of_external_imports=" + antClass.getExternalImports().size();
                                        text = text + " , " + "number_of_implemented_interfaces=" + antClass.getImplementedInterfaces().size();
                                        //classes do mesmo pacote das quais essa classe depende
                                        text = text + " , " + "number_of_internal_dependency_classes=" + antClass.getInternalDependencyClasses().size();

                                        //numero de metodos
                                        int numberOfMethods = antClass.getMethods().size();

                                        if (numberOfMethods == 0) {
                                            text = text + " , " + "number_of_methods=" + numberOfMethods;
                                        } else {
                                            int basenumberOfMethods = numberOfMethods / 5;
                                            basenumberOfMethods = basenumberOfMethods * 5;
                                            basenumberOfMethods++;
                                            text = text + " , " + "number_of_methods=" + basenumberOfMethods + "-" + (basenumberOfMethods + 4);
                                        }
                                        //text = text + " , " + "number_of_methods=" + antClass.getMethods().size();
                                        double tcc = antClass.getNumberOfDirectConnections();
                                        int n = antClass.getMethods().size();
                                        tcc = tcc / ((n * (n - 1)) / 2);
                                        if (tcc == Double.NaN) {
                                            text = text + " , " + "tight_class_cohesion=" + tcc;
                                        } else if (tcc == 0) {
                                            text = text + " , " + "tight_class_cohesion=" + tcc;
                                        } else {
                                            int baseTcc = Double.valueOf(tcc * 10).intValue();
                                            if (baseTcc < 9) {
                                                text = text + " , " + "tight_class_cohesion=0." + baseTcc + "-" + "0." + (baseTcc + 1);
                                            } else if (baseTcc == 9) {
                                                text = text + " , " + "tight_class_cohesion=0.9-1.0";
                                            } else {
                                                text = text + " , " + "tight_class_cohesion=1.0";
                                            }
                                        }
                                        int totalCyclomatic = antClass.getTotalCyclomaticComplexity();
                                        if (totalCyclomatic == 0) {
                                            text = text + " , " + "total_cyclomatic_complexity=" + numberOfMethods;
                                        } else {
                                            int baseTotalCiclometic = totalCyclomatic / 100;
                                            baseTotalCiclometic = baseTotalCiclometic * 100;
                                            baseTotalCiclometic++;
                                            text = text + " , " + "total_cyclomatic_complexity=" + baseTotalCiclometic + "-" + (baseTotalCiclometic + 99);
                                        }
                                        //text = text + " , " + "total_cyclomatic_complexity=" + antClass.getTotalCyclomaticComplexity();
                                        double averageClassCyclomaticComplexity = antClass.getTotalCyclomaticComplexity();
                                        averageClassCyclomaticComplexity = averageClassCyclomaticComplexity / antClass.getMethods().size();
                                        int baseAverageClassCyclomaticComplexity = Double.valueOf(averageClassCyclomaticComplexity).intValue();
                                        if (averageClassCyclomaticComplexity == 0) {
                                            text = text + " , " + "average_class_cyclomatic_complexity=" + baseAverageClassCyclomaticComplexity;
                                        } else {
                                            //int baseAverageClassCyclomaticComplexity = Double.valueOf(averageClassCyclomaticComplexity / 5).intValue();
                                            //baseAverageClassCyclomaticComplexity = baseAverageClassCyclomaticComplexity * 5;
                                            //baseAverageClassCyclomaticComplexity++;
                                            text = text + " , " + "average_class_cyclomatic_complexity=" + baseAverageClassCyclomaticComplexity;
                                        }
                                        //classes do mesmo pacote que dependem de mim
                                        int intrapackageDependenClass = antClass.getintraPackageDependentClass().size();
                                        if (intrapackageDependenClass == 0) {
                                            text = text + " , " + "number_of_intra_package_dependent_classes=" + intrapackageDependenClass;
                                        } else {
                                            int baseIntrapackageDependenClass = intrapackageDependenClass / 5;
                                            baseIntrapackageDependenClass = baseIntrapackageDependenClass * 5;
                                            baseIntrapackageDependenClass++;
                                            text = text + " , " + "number_of_intra_package_dependent_classes=" + baseIntrapackageDependenClass + "-" + (baseIntrapackageDependenClass + 4);
                                        }
                                        //text = text + " , " + "number_of_intra_package_dependent_classes=" + antClass.getintraPackageDependentClass().size();

                                        double classLocality = antClass.getInternalDependencyClasses().size();
                                        classLocality = classLocality / (antClass.getInternalDependencyClasses().size() + antClass.getExternalDependencyClasses().size());
                                        //classes do mesmo pacote das quais essa classe depende
                                        System.out.println("Internal dependency class: " + antClass.getInternalDependencyClasses().size() + "   external dependency class: " + antClass.getExternalDependencyClasses().size());
                                        if (classLocality == Double.NaN) {
                                            text = text + " , " + "class_locality=" + classLocality;
                                        } else if (classLocality == 0) {
                                            text = text + " , " + "class_locality=" + classLocality;
                                        } else {
                                            int baseClassLocality = Double.valueOf(classLocality * 10).intValue();
                                            if (baseClassLocality < 9) {
                                                text = text + " , " + "class_locality=0." + baseClassLocality + "-" + "0." + (baseClassLocality + 1);
                                            } else if (baseClassLocality == 9) {
                                                text = text + " , " + "class_locality=0.9-1.0";
                                            } else {
                                                text = text + " , " + "class_locality=1.0";
                                            }
                                        }
                                        //text = text + " , " + "class_locality=" + classLocality;
                                        double packageCohesion = antClass.getJavaPackage().getPackageCohesion();
                                        if (packageCohesion == Double.NaN) {
                                            text = text + " , " + "package_cohesion=" + packageCohesion;
                                        } else if (packageCohesion == 0) {
                                            text = text + " , " + "package_cohesion=" + packageCohesion;
                                        } else {
                                            int basePackageCohesion = Double.valueOf(packageCohesion * 10).intValue();
                                            if (basePackageCohesion < 9) {
                                                text = text + " , " + "package_cohesion=0." + basePackageCohesion + "-" + "0." + (basePackageCohesion + 1);
                                            } else if (basePackageCohesion == 9) {
                                                text = text + " , " + "package_cohesion=0.9-1.0";
                                            } else {
                                                text = text + " , " + "package_cohesion=1.0";
                                            }
                                        }
                                        //text = text + " , " + "package_cohesion=" + packageCohesion;
                                        //text = text + " , " + "method_signature=" + antClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature();

                                        writer0.println(text);
                                    } else {
                                    }

                                }

                            }
                            if (!possuiAlgumMetodo) {

                                String text = "continue_healthly_method";

                                text = text + " , " + "acess_to_acess_to_foreign_data_number=" + antClass.getAccessToForeignDataNumber();
                                int numberofAttributes = antClass.getAttributes().size();
                                if (numberofAttributes == 0) {
                                    text = text + " , " + "number_of_attributes=" + numberofAttributes;
                                } else if (numberofAttributes <= 5) {
                                    text = text + " , " + "number_of_attributes=1-5";
                                } else if (numberofAttributes <= 10) {
                                    text = text + " , " + "number_of_attributes=6-10";
                                } else if (numberofAttributes <= 15) {
                                    text = text + " , " + "number_of_attributes=11-15";
                                } else if (numberofAttributes <= 20) {
                                    text = text + " , " + "number_of_attributes=16-20";
                                } else if (numberofAttributes <= 25) {
                                    text = text + " , " + "number_of_attributes=20-25";
                                } else if (numberofAttributes <= 30) {
                                    text = text + " , " + "number_of_attributes=26-30";
                                } else if (numberofAttributes <= 35) {
                                    text = text + " , " + "number_of_attributes=31-35";
                                } else if (numberofAttributes <= 40) {
                                    text = text + " , " + "number_of_attributes=36-40";
                                } else {
                                    text = text + " , " + "number_of_attributes>=41";
                                }

                                //text = text + " , " + "number_of_external_imports=" + antClass.getExternalImports().size();
                                text = text + " , " + "number_of_implemented_interfaces=" + antClass.getImplementedInterfaces().size();
                                //classes do mesmo pacote das quais essa classe depende
                                text = text + " , " + "number_of_internal_dependency_classes=" + antClass.getInternalDependencyClasses().size();

                                //numero de metodos
                                int numberOfMethods = antClass.getMethods().size();

                                if (numberOfMethods == 0) {
                                    text = text + " , " + "number_of_methods=" + numberOfMethods;
                                } else {
                                    int basenumberOfMethods = numberOfMethods / 5;
                                    basenumberOfMethods = basenumberOfMethods * 5;
                                    basenumberOfMethods++;
                                    text = text + " , " + "number_of_methods=" + basenumberOfMethods + "-" + (basenumberOfMethods + 4);
                                }
                                //text = text + " , " + "number_of_methods=" + antClass.getMethods().size();
                                double tcc = antClass.getNumberOfDirectConnections();
                                int n = antClass.getMethods().size();
                                tcc = tcc / ((n * (n - 1)) / 2);
                                if (tcc == Double.NaN) {
                                    text = text + " , " + "tight_class_cohesion=" + tcc;
                                } else if (tcc == 0) {
                                    text = text + " , " + "tight_class_cohesion=" + tcc;
                                } else {
                                    int baseTcc = Double.valueOf(tcc * 10).intValue();
                                    if (baseTcc < 9) {
                                        text = text + " , " + "tight_class_cohesion=0." + baseTcc + "-" + "0." + (baseTcc + 1);
                                    } else if (baseTcc == 9) {
                                        text = text + " , " + "tight_class_cohesion=0.9-1.0";
                                    } else {
                                        text = text + " , " + "tight_class_cohesion=1.0";
                                    }
                                }
                                int totalCyclomatic = antClass.getTotalCyclomaticComplexity();
                                if (totalCyclomatic == 0) {
                                    text = text + " , " + "total_cyclomatic_complexity=" + numberOfMethods;
                                } else {
                                    int baseTotalCiclometic = totalCyclomatic / 100;
                                    baseTotalCiclometic = baseTotalCiclometic * 100;
                                    baseTotalCiclometic++;
                                    text = text + " , " + "total_cyclomatic_complexity=" + baseTotalCiclometic + "-" + (baseTotalCiclometic + 99);
                                }
                                //text = text + " , " + "total_cyclomatic_complexity=" + antClass.getTotalCyclomaticComplexity();
                                double averageClassCyclomaticComplexity = antClass.getTotalCyclomaticComplexity();
                                averageClassCyclomaticComplexity = averageClassCyclomaticComplexity / antClass.getMethods().size();
                                int baseAverageClassCyclomaticComplexity = Double.valueOf(averageClassCyclomaticComplexity).intValue();
                                if (averageClassCyclomaticComplexity == 0) {
                                    text = text + " , " + "average_class_cyclomatic_complexity=" + baseAverageClassCyclomaticComplexity;
                                } else {
                                    //int baseAverageClassCyclomaticComplexity = Double.valueOf(averageClassCyclomaticComplexity / 5).intValue();
                                    //baseAverageClassCyclomaticComplexity = baseAverageClassCyclomaticComplexity * 5;
                                    //baseAverageClassCyclomaticComplexity++;
                                    text = text + " , " + "average_class_cyclomatic_complexity=" + baseAverageClassCyclomaticComplexity;
                                }
                                //classes do mesmo pacote que dependem de mim
                                int intrapackageDependenClass = antClass.getintraPackageDependentClass().size();
                                if (intrapackageDependenClass == 0) {
                                    text = text + " , " + "number_of_intra_package_dependent_classes=" + intrapackageDependenClass;
                                } else {
                                    int baseIntrapackageDependenClass = intrapackageDependenClass / 5;
                                    baseIntrapackageDependenClass = baseIntrapackageDependenClass * 5;
                                    baseIntrapackageDependenClass++;
                                    text = text + " , " + "number_of_intra_package_dependent_classes=" + baseIntrapackageDependenClass + "-" + (baseIntrapackageDependenClass + 4);
                                }
                                //text = text + " , " + "number_of_intra_package_dependent_classes=" + antClass.getintraPackageDependentClass().size();

                                double classLocality = antClass.getInternalDependencyClasses().size();
                                classLocality = classLocality / (antClass.getInternalDependencyClasses().size() + antClass.getExternalDependencyClasses().size());
                                //classes do mesmo pacote das quais essa classe depende
                                System.out.println("Internal dependency class: " + antClass.getInternalDependencyClasses().size() + "   external dependency class: " + antClass.getExternalDependencyClasses().size());
                                if (classLocality == Double.NaN) {
                                    text = text + " , " + "class_locality=" + classLocality;
                                } else if (classLocality == 0) {
                                    text = text + " , " + "class_locality=" + classLocality;
                                } else {
                                    int baseClassLocality = Double.valueOf(classLocality * 10).intValue();
                                    if (baseClassLocality < 9) {
                                        text = text + " , " + "class_locality=0." + baseClassLocality + "-" + "0." + (baseClassLocality + 1);
                                    } else if (baseClassLocality == 9) {
                                        text = text + " , " + "class_locality=0.9-1.0";
                                    } else {
                                        text = text + " , " + "class_locality=1.0";
                                    }
                                }
                                //text = text + " , " + "class_locality=" + classLocality;
                                double packageCohesion = antClass.getJavaPackage().getPackageCohesion();
                                if (packageCohesion == Double.NaN) {
                                    text = text + " , " + "package_cohesion=" + packageCohesion;
                                } else if (packageCohesion == 0) {
                                    text = text + " , " + "package_cohesion=" + packageCohesion;
                                } else {
                                    int basePackageCohesion = Double.valueOf(packageCohesion * 10).intValue();
                                    if (basePackageCohesion < 9) {
                                        text = text + " , " + "package_cohesion=0." + basePackageCohesion + "-" + "0." + (basePackageCohesion + 1);
                                    } else if (basePackageCohesion == 9) {
                                        text = text + " , " + "package_cohesion=0.9-1.0";
                                    } else {
                                        text = text + " , " + "package_cohesion=1.0";
                                    }
                                }
                                //text = text + " , " + "package_cohesion=" + packageCohesion;
                                //text = text + " , " + "method_signature=" + antClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature();

                                writer0.println(text);
                            }
                        }
                    }
                }

                System.out.println("Calculou anomalies: " + k);

                ant = jp;
                hashAntAnomalies = hashAnomalies;
                k++;
                if (rev.getNext().size() == 0) {
                    rev = null;
                } else {
                    rev = rev.getNext().get(0);
                }
            }

            writer0.close();

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
        long t2 = System.currentTimeMillis();
        System.out.println("Classificando anomalias");
        //projectAnomalies.classifyAnomalies();
        System.out.println("Terminou de classificar");
        System.out.println("Tempo pra pegar anomalias: " + ((t2 - t1) / 60000) + " minutos");

    }

    public void verificarCongenitalAfterClass(ProjectRevisions newProjectRevisions, Project project, JavaConstructorService javaConstructorService) {
        long t1 = System.currentTimeMillis();
        Revision rev = newProjectRevisions.getRoot();
        int k = 0;
        while (rev != null) {
            k++;
            if (rev.getNext().size() == 0) {
                rev = null;
            } else {
                rev = rev.getNext().get(0);
            }
        }

        String path = System.getProperty("user.home") + "/.archd/histogramas_anomalias/histogramas_congenital_anomalie_god_method/";
        HashMap<String, Integer> hashAntAnomalies = null;

        HashMap<String, Integer> hashmapNumberOfAttributesCongenitalAfterClass = new HashMap();
        //HashMap<String, Integer> hashmapNumberOfAttributesCongenitalAfterClassCorrectedWithouChanges = new HashMap();
        //HashMap<String, Integer> hashmapNumberOfAttributesCongenitalAfterClassNotCorrected = new HashMap();
        HashMap<String, Integer> hashmapNumberOfAttributesSaudavel = new HashMap();
        HashMap<String, Integer> hashmapNumberOfMethodsCongenitalAfterClass = new HashMap();
        HashMap<String, Integer> hashmapNumberOfMethodsSaudavel = new HashMap();
        HashMap<String, Integer> hashmapNumberOfImplementedInterfacesCongenitalAfterClass = new HashMap();
        HashMap<String, Integer> hashmapNumberOfImplementedInterfacesSaudavel = new HashMap();
        HashMap<String, Integer> hashmapNumberOfTotalComplexityCongenitalAfterClass = new HashMap();
        HashMap<String, Integer> hashmapNumberOfTotalComplexitySaudavel = new HashMap();
        HashMap<String, Integer> hashmapNumberOfComplexityAverageCongenitalAfterClass = new HashMap();
        HashMap<String, Integer> hashmapNumberOfComplexityAverageSaudavel = new HashMap();
        HashMap<String, Integer> hashmapNumberOfAcessoDadoEstrangeiroCongenitalAfterClass = new HashMap();
        HashMap<String, Integer> hashmapNumberOfAcessoDadoEstrangeiroSaudavel = new HashMap();
        HashMap<String, Integer> hashmapNumeroDeDependenciaAClassesInternasCongenitalAfterClass = new HashMap();
        HashMap<String, Integer> hashmapNumeroDeDependenciaAClassesInternasSaudavel = new HashMap();
        HashMap<String, Integer> hashmapTightClassCohesionCongenitalAfterClass = new HashMap();
        HashMap<String, Integer> hashmapTightClassCohesionSaudavel = new HashMap();
        HashMap<String, Integer> hashmapNumeroDeClassesClientesInternasCongenitalAfterClass = new HashMap();
        HashMap<String, Integer> hashmapNumeroDeClassesClientesInternasSaudavel = new HashMap();
        HashMap<String, Integer> hashmapClassLocalityCongenitalAfterClass = new HashMap();
        HashMap<String, Integer> hashmapClassLocalitySaudavel = new HashMap();

        //classificar as anomalias quanto aos seus 24 tipos
        //JavaConstructorService javaContructorService = new JavaConstructorService();
        //AnomaliesAnaliser anomaliesAnaliser = new AnomaliesAnaliser();
        //ProjectAnomalies projectAnomalies = anomaliesAnaliser.getAnomalies(newProjectRevisions, project, javaContructorService);
        try {
            PrintWriter writer0 = new PrintWriter(path + "map_add_methods_analise_emerge_and_correct_difference.txt", "UTF-8");

            rev = newProjectRevisions.getRoot();
            AnomalieDao anomalieDao = DataBaseFactory.getInstance().getAnomalieDao();
            HashMap<String, Integer> birthHashMap = new HashMap();
            HashMap<String, String> methodsAlternativeNameMap = new HashMap();
            HashMap<String, String> classesAlternativeNameMap = new HashMap();
            k = 0;

            //List<GenericAnomalies> methodGenericAnomalies = projectAnomalies.getAllMethodAnomalies();
            JavaProject ant = null;
            while (rev != null) {
                JavaProject jp = null;
                //System.out.println("REV ID: "+rev.getId());
                System.gc();
                System.out.println("********************************* vai pegar um projeto completo");
                jp = javaConstructorService.getProjectByRevisionAndSetRevision(project.getName(), project.getCodeDirs(), project.getPath(), rev.getId(), newProjectRevisions.getName());

                for (JavaPackage javaPackage : jp.getPackages()) {
                    if (birthHashMap.get(javaPackage.getName()) == null) {
                        birthHashMap.put(javaPackage.getName(), k);
                    }
                }

                for (JavaAbstract javaAbstract : jp.getClasses()) {
                    JavaClass javaClass = (JavaClass) javaAbstract;

                    if (birthHashMap.get(javaClass.getFullQualifiedName()) == null) {
                        birthHashMap.put(javaClass.getFullQualifiedName(), k);

                        if (classesAlternativeNameMap.get(javaClass.getFullQualifiedName()) == null) {
                            if (ant != null) {
                                List<JavaAbstract> antClasses = ant.getClassByLastName(javaAbstract.getName());
                                List<JavaAbstract> sameClasses = new LinkedList();
                                for (JavaAbstract antClass : antClasses) {
                                    if (isSameJavaClass(javaClass, (JavaClass) antClass)) {
                                        sameClasses.add(antClass);
                                    } else {
                                        System.out.println(javaClass.getFullQualifiedName() + " - is not same - " + antClass.getFullQualifiedName());
                                    }
                                }
                                if (sameClasses.size() > 1) {
                                    System.out.println("********** Erro, duas classes parecidas");
                                } else {
                                    if (sameClasses.size() == 1) {
                                        String alternativeName = classesAlternativeNameMap.get(sameClasses.get(0).getFullQualifiedName());

                                        if (alternativeName == null) {
                                            alternativeName = sameClasses.get(0).getFullQualifiedName();
                                        }
                                        System.out.println("Classe mudou de nome: " + javaClass.getFullQualifiedName() + "   -    " + alternativeName);
                                        classesAlternativeNameMap.put(javaClass.getFullQualifiedName(), alternativeName);

                                    }
                                }

                            }
                        }

                    }
                    for (JavaMethod javaMethod : javaClass.getMethods()) {

                        //ainda nao foi dito a existencia desse metodo
                        if (birthHashMap.get(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature()) == null) {

                            birthHashMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), k);
                            if (methodsAlternativeNameMap.get(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature()) == null) {
                                methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature());
                                String alternativeClassName = classesAlternativeNameMap.get(javaClass.getFullQualifiedName());
                                //o mÃ©todo ainda nao surgiu e a classe que ele esta mudou de nome
                                if (alternativeClassName != null) {
                                    //ainda nao surgiu o mÃ©todo
                                    String alternativeMethodName = methodsAlternativeNameMap.get(alternativeClassName + ":" + javaMethod.getMethodSignature());
                                    if (alternativeMethodName == null) {
                                        if (ant != null) {
                                            JavaAbstract antAbstract = ant.getClassByName(javaClass.getFullQualifiedName());
                                            if (antAbstract != null && antAbstract.getClass() == JavaClass.class) {

                                                List<JavaMethod> methodsName = new LinkedList();
                                                List<JavaMethod> methodsNameAnt = ((JavaClass) antAbstract).getMethodsByName(javaMethod.getName());
                                                List<JavaMethod> methodsNameCurrent = javaClass.getMethodsByName(javaMethod.getName());
                                                for (JavaMethod jm : methodsNameAnt) {
                                                    boolean exists = false;
                                                    for (JavaMethod auxJm : methodsNameCurrent) {
                                                        if (auxJm.getMethodSignature().equals(jm.getMethodSignature())) {
                                                            exists = true;
                                                            break;
                                                        }
                                                    }
                                                    if (!exists) {
                                                        methodsName.add(jm);
                                                    }
                                                }

                                                if (!methodsName.isEmpty()) {
                                                    JavaMethod auxJm = closestMethod(javaMethod, methodsName);
                                                    alternativeMethodName = methodsAlternativeNameMap.get(javaClass.getFullQualifiedName() + ":" + auxJm.getMethodSignature());
                                                    if (alternativeMethodName == null) {
                                                        alternativeMethodName = alternativeClassName + ":" + auxJm.getMethodSignature();
                                                    }
                                                    System.out.println("Metodo mudou de assinatura: " + javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + "   -    " + alternativeMethodName);
                                                    methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), alternativeMethodName);

                                                }

                                            }
                                        }
                                    } else {
                                        //a classe mudou de nome e a encontramos com esse nome novo
                                        methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), alternativeMethodName);
                                        System.out.println("Metodo mudou nome completo (por causa da mudanÃ§a do nome da classe): " + javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + "   -    " + alternativeMethodName);
                                    }
                                } else {
                                    // a classe nao mudou de nome
                                    if (ant != null) {
                                        //verificaremos se algum mpetodo existia antes e sumiu depois, pode ter se transformado
                                        JavaAbstract antAbstract = ant.getClassByName(javaClass.getFullQualifiedName());
                                        if (antAbstract != null && antAbstract.getClass() == JavaClass.class) {

                                            List<JavaMethod> methodsName = new LinkedList();
                                            List<JavaMethod> methodsNameAnt = ((JavaClass) antAbstract).getMethodsByName(javaMethod.getName());
                                            List<JavaMethod> methodsNameCurrent = javaClass.getMethodsByName(javaMethod.getName());
                                            for (JavaMethod jm : methodsNameAnt) {
                                                boolean exists = false;
                                                for (JavaMethod auxJm : methodsNameCurrent) {
                                                    if (auxJm.getMethodSignature().equals(jm.getMethodSignature())) {
                                                        exists = true;
                                                        break;
                                                    }
                                                }
                                                if (!exists) {
                                                    methodsName.add(jm);
                                                }
                                            }

                                            if (!methodsName.isEmpty()) {
                                                JavaMethod auxJm = closestMethod(javaMethod, methodsName);
                                                String alternativeMethodName = methodsAlternativeNameMap.get(javaClass.getFullQualifiedName() + ":" + auxJm.getMethodSignature());
                                                if (alternativeMethodName == null) {
                                                    alternativeMethodName = javaClass.getFullQualifiedName() + ":" + auxJm.getMethodSignature();
                                                }
                                                System.out.println("Metodo mudou de assinatura: " + javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + "   -    " + alternativeMethodName);
                                                methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), alternativeMethodName);

                                            }

                                        }
                                    }
                                }
                            }

                        }
                    }
                }

                List<AnomalieItem> items = anomalieDao.getItemsByRevisionId(rev.getId());
                HashMap<String, Integer> hashAnomalies = new HashMap();
                for (AnomalieItem anomalieItem : items) {
                    String alternativeName = methodsAlternativeNameMap.get(anomalieItem.getItem());
                    if (alternativeName == null) {
                        alternativeName = anomalieItem.getItem();
                    }
                    hashAnomalies.put(alternativeName, anomalieItem.getAnomalieId());
                }

                for (JavaAbstract javaAbstract : jp.getClasses()) {

                    if (ant != null && hashAntAnomalies != null) {
                        JavaClass javaClass = (JavaClass) javaAbstract;
                        JavaAbstract auxAbstract = ant.getClassByName(javaClass.getFullQualifiedName());

                        if (auxAbstract == null) {
                            List<JavaAbstract> antClasses = ant.getClassByLastName(javaAbstract.getName());
                            List<JavaAbstract> sameClasses = new LinkedList();
                            for (JavaAbstract antClass : antClasses) {
                                if (isSameJavaClass(javaClass, (JavaClass) antClass)) {
                                    sameClasses.add(antClass);
                                } else {
                                    System.out.println(javaClass.getFullQualifiedName() + " - is not same - " + antClass.getFullQualifiedName());
                                }
                            }
                            if (sameClasses.size() > 1) {
                                System.out.println("********** Erro, duas classes parecidas");
                            } else {
                                if (sameClasses.size() == 1) {
                                    auxAbstract = sameClasses.get(0);
                                }
                            }
                        }

                        if (auxAbstract != null && auxAbstract.getClass() == JavaClass.class) {
                            JavaClass antClass = (JavaClass) auxAbstract;

                            boolean possuiAlgumMetodo = false;

                            for (JavaMethod javaMethod : javaClass.getMethods()) {
                                String alternativeName = methodsAlternativeNameMap.get(javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature());
                                if (alternativeName == null) {
                                    alternativeName = javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature();
                                }
                                int i = birthHashMap.get(alternativeName);
                                if (k != i) {

                                    String text = "";

                                    Integer anomalieNum = hashAnomalies.get(alternativeName);
                                    Integer anomalieAntNum = hashAntAnomalies.get(alternativeName);
                                    if (anomalieNum != null && anomalieAntNum == null) {
                                        //surgiu uma anomalia
                                        if (anomalieNum == Constants.ANOMALIE_GOD_METHOD) {
                                            System.out.println("Surgi anomalia god method no método: " + javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + "      revision: " + k);

                                            Integer num = null;
                                            int numberofAttributes = antClass.getAttributes().size();
                                            num = hashmapNumberOfAttributesCongenitalAfterClass.get(String.valueOf(numberofAttributes));
                                            if (num == null) {
                                                hashmapNumberOfAttributesCongenitalAfterClass.put(String.valueOf(numberofAttributes), 1);
                                            } else {
                                                num++;
                                                hashmapNumberOfAttributesCongenitalAfterClass.put(String.valueOf(numberofAttributes), num);
                                            }

                                            int numberofImplementedInterfaces = antClass.getImplementedInterfaces().size();
                                            num = hashmapNumberOfImplementedInterfacesCongenitalAfterClass.get(String.valueOf(numberofImplementedInterfaces));
                                            if (num == null) {
                                                hashmapNumberOfImplementedInterfacesCongenitalAfterClass.put(String.valueOf(numberofImplementedInterfaces), 1);
                                            } else {
                                                num++;
                                                hashmapNumberOfImplementedInterfacesCongenitalAfterClass.put(String.valueOf(numberofImplementedInterfaces), num);
                                            }

                                            int numberofMethods = antClass.getMethods().size();
                                            num = hashmapNumberOfMethodsCongenitalAfterClass.get(String.valueOf(numberofMethods));
                                            if (num == null) {
                                                hashmapNumberOfMethodsCongenitalAfterClass.put(String.valueOf(numberofMethods), 1);
                                            } else {
                                                num++;
                                                hashmapNumberOfMethodsCongenitalAfterClass.put(String.valueOf(numberofMethods), num);
                                            }

                                            int numberofTotalCiclomaticComplxity = antClass.getTotalCyclomaticComplexity();
                                            num = hashmapNumberOfTotalComplexityCongenitalAfterClass.get(String.valueOf(numberofTotalCiclomaticComplxity));
                                            if (num == null) {
                                                hashmapNumberOfTotalComplexityCongenitalAfterClass.put(String.valueOf(numberofTotalCiclomaticComplxity), 1);
                                            } else {
                                                num++;
                                                hashmapNumberOfTotalComplexityCongenitalAfterClass.put(String.valueOf(numberofTotalCiclomaticComplxity), num);
                                            }

                                            double complexityAverage = 0;
                                            if (numberofMethods != 0) {
                                                complexityAverage = numberofTotalCiclomaticComplxity / numberofMethods;
                                            }
                                            num = hashmapNumberOfComplexityAverageCongenitalAfterClass.get(String.valueOf(complexityAverage));
                                            if (num == null) {
                                                hashmapNumberOfComplexityAverageCongenitalAfterClass.put(String.valueOf(complexityAverage), 1);
                                            } else {
                                                num++;
                                                hashmapNumberOfComplexityAverageCongenitalAfterClass.put(String.valueOf(complexityAverage), num);
                                            }

                                            int acessoDadoEstrangeiro = antClass.getAccessToForeignDataNumber();
                                            num = hashmapNumberOfAcessoDadoEstrangeiroCongenitalAfterClass.get(String.valueOf(acessoDadoEstrangeiro));
                                            if (num == null) {
                                                hashmapNumberOfAcessoDadoEstrangeiroCongenitalAfterClass.put(String.valueOf(acessoDadoEstrangeiro), 1);
                                            } else {
                                                num++;
                                                hashmapNumberOfAcessoDadoEstrangeiroCongenitalAfterClass.put(String.valueOf(acessoDadoEstrangeiro), num);
                                            }

                                            int numeroDeDependenciaAClassesInternas = antClass.getInternalDependencyClasses().size();
                                            num = hashmapNumeroDeDependenciaAClassesInternasCongenitalAfterClass.get(String.valueOf(numeroDeDependenciaAClassesInternas));
                                            if (num == null) {
                                                hashmapNumeroDeDependenciaAClassesInternasCongenitalAfterClass.put(String.valueOf(numeroDeDependenciaAClassesInternas), 1);
                                            } else {
                                                num++;
                                                hashmapNumeroDeDependenciaAClassesInternasCongenitalAfterClass.put(String.valueOf(numeroDeDependenciaAClassesInternas), num);
                                            }

                                            double tcc = 1;
                                            if (antClass.getMethods().size() >= 2) {
                                                tcc = antClass.getNumberOfDirectConnections();
                                                int n = antClass.getMethods().size();
                                                tcc = tcc / ((n * (n - 1)) / 2);
                                            }
                                            num = hashmapTightClassCohesionCongenitalAfterClass.get(String.valueOf(tcc));
                                            if (num == null) {
                                                hashmapTightClassCohesionCongenitalAfterClass.put(String.valueOf(tcc), 1);
                                            } else {
                                                num++;
                                                hashmapTightClassCohesionCongenitalAfterClass.put(String.valueOf(tcc), num);
                                            }

                                            int numeroDeClassesClientesInternas = antClass.getintraPackageDependentClass().size();
                                            num = hashmapNumeroDeClassesClientesInternasCongenitalAfterClass.get(String.valueOf(numeroDeClassesClientesInternas));
                                            if (num == null) {
                                                hashmapNumeroDeClassesClientesInternasCongenitalAfterClass.put(String.valueOf(numeroDeClassesClientesInternas), 1);
                                            } else {
                                                num++;
                                                hashmapNumeroDeClassesClientesInternasCongenitalAfterClass.put(String.valueOf(numeroDeClassesClientesInternas), num);
                                            }

                                            double classLocality = -1;
                                            if (antClass.getInternalDependencyClasses().size() + antClass.getExternalDependencyClasses().size() > 0) {
                                                classLocality = antClass.getInternalDependencyClasses().size();
                                                classLocality = classLocality / (antClass.getInternalDependencyClasses().size() + antClass.getExternalDependencyClasses().size());
                                            }
                                            num = hashmapClassLocalityCongenitalAfterClass.get(String.valueOf(classLocality));
                                            if (num == null) {
                                                hashmapClassLocalityCongenitalAfterClass.put(String.valueOf(classLocality), 1);
                                            } else {
                                                num++;
                                                hashmapClassLocalityCongenitalAfterClass.put(String.valueOf(classLocality), num);
                                            }

                                        }
                                    } else if (anomalieNum == null && anomalieAntNum == null) {
                                        //continuo de forma sadia
                                        Integer num = null;

                                        int numberofAttributes = antClass.getAttributes().size();
                                        num = hashmapNumberOfAttributesSaudavel.get(String.valueOf(numberofAttributes));
                                        if (num == null) {
                                            hashmapNumberOfAttributesSaudavel.put(String.valueOf(numberofAttributes), 1);
                                        } else {
                                            num++;
                                            hashmapNumberOfAttributesSaudavel.put(String.valueOf(numberofAttributes), num);
                                        }

                                        int numberofImplementedInterfaces = antClass.getImplementedInterfaces().size();
                                        num = hashmapNumberOfImplementedInterfacesSaudavel.get(String.valueOf(numberofImplementedInterfaces));
                                        if (num == null) {
                                            hashmapNumberOfImplementedInterfacesSaudavel.put(String.valueOf(numberofImplementedInterfaces), 1);
                                        } else {
                                            num++;
                                            hashmapNumberOfImplementedInterfacesSaudavel.put(String.valueOf(numberofImplementedInterfaces), num);
                                        }

                                        int numberofMethods = antClass.getMethods().size();
                                        num = hashmapNumberOfMethodsSaudavel.get(String.valueOf(numberofMethods));
                                        if (num == null) {
                                            hashmapNumberOfMethodsSaudavel.put(String.valueOf(numberofMethods), 1);
                                        } else {
                                            num++;
                                            hashmapNumberOfMethodsSaudavel.put(String.valueOf(numberofMethods), num);
                                        }

                                        int numberofTotalCiclomaticComplxity = antClass.getTotalCyclomaticComplexity();
                                        num = hashmapNumberOfTotalComplexitySaudavel.get(String.valueOf(numberofTotalCiclomaticComplxity));
                                        if (num == null) {
                                            hashmapNumberOfTotalComplexitySaudavel.put(String.valueOf(numberofTotalCiclomaticComplxity), 1);
                                        } else {
                                            num++;
                                            hashmapNumberOfTotalComplexitySaudavel.put(String.valueOf(numberofTotalCiclomaticComplxity), num);
                                        }

                                        double complexityAverage = 0;
                                        if (numberofMethods != 0) {
                                            complexityAverage = numberofTotalCiclomaticComplxity / numberofMethods;
                                        }
                                        num = hashmapNumberOfComplexityAverageSaudavel.get(String.valueOf(complexityAverage));
                                        if (num == null) {
                                            hashmapNumberOfComplexityAverageSaudavel.put(String.valueOf(complexityAverage), 1);
                                        } else {
                                            num++;
                                            hashmapNumberOfComplexityAverageSaudavel.put(String.valueOf(complexityAverage), num);
                                        }

                                        int acessoDadoEstrangeiro = antClass.getAccessToForeignDataNumber();
                                        num = hashmapNumberOfAcessoDadoEstrangeiroSaudavel.get(String.valueOf(acessoDadoEstrangeiro));
                                        if (num == null) {
                                            hashmapNumberOfAcessoDadoEstrangeiroSaudavel.put(String.valueOf(acessoDadoEstrangeiro), 1);
                                        } else {
                                            num++;
                                            hashmapNumberOfAcessoDadoEstrangeiroSaudavel.put(String.valueOf(acessoDadoEstrangeiro), num);
                                        }

                                        int numeroDeDependenciaAClassesInternas = antClass.getInternalDependencyClasses().size();
                                        num = hashmapNumeroDeDependenciaAClassesInternasSaudavel.get(String.valueOf(numeroDeDependenciaAClassesInternas));
                                        if (num == null) {
                                            hashmapNumeroDeDependenciaAClassesInternasSaudavel.put(String.valueOf(numeroDeDependenciaAClassesInternas), 1);
                                        } else {
                                            num++;
                                            hashmapNumeroDeDependenciaAClassesInternasSaudavel.put(String.valueOf(numeroDeDependenciaAClassesInternas), num);
                                        }

                                        double tcc = 1;
                                        if (antClass.getMethods().size() >= 2) {
                                            tcc = antClass.getNumberOfDirectConnections();
                                            int n = antClass.getMethods().size();
                                            tcc = tcc / ((n * (n - 1)) / 2);
                                        }
                                        num = hashmapTightClassCohesionSaudavel.get(String.valueOf(tcc));
                                        if (num == null) {
                                            hashmapTightClassCohesionSaudavel.put(String.valueOf(tcc), 1);
                                        } else {
                                            num++;
                                            hashmapTightClassCohesionSaudavel.put(String.valueOf(tcc), num);
                                        }

                                        int numeroDeClassesClientesInternas = antClass.getintraPackageDependentClass().size();
                                        num = hashmapNumeroDeClassesClientesInternasSaudavel.get(String.valueOf(numeroDeClassesClientesInternas));
                                        if (num == null) {
                                            hashmapNumeroDeClassesClientesInternasSaudavel.put(String.valueOf(numeroDeClassesClientesInternas), 1);
                                        } else {
                                            num++;
                                            hashmapNumeroDeClassesClientesInternasSaudavel.put(String.valueOf(numeroDeClassesClientesInternas), num);
                                        }

                                        double classLocality = -1;
                                        if (antClass.getInternalDependencyClasses().size() + antClass.getExternalDependencyClasses().size() > 0) {
                                            classLocality = antClass.getInternalDependencyClasses().size();
                                            classLocality = classLocality / (antClass.getInternalDependencyClasses().size() + antClass.getExternalDependencyClasses().size());
                                        }
                                        num = hashmapClassLocalitySaudavel.get(String.valueOf(classLocality));
                                        if (num == null) {
                                            hashmapClassLocalitySaudavel.put(String.valueOf(classLocality), 1);
                                        } else {
                                            num++;
                                            hashmapClassLocalitySaudavel.put(String.valueOf(classLocality), num);
                                        }
                                    }
                                }
                            }
                        }
                    }

//                    JavaClass javaClass = (JavaClass) javaAbstract;
//
//                    for (JavaMethod javaMethod : javaClass.getMethods()) {
//                        GenericAnomalies auxGenericAnomalies = null;
//                        for (GenericAnomalies genericAnomalies : methodGenericAnomalies) {
//                            if (genericAnomalies.isGenericName(javaMethod.getMethodSignature())) {
//                                auxGenericAnomalies = genericAnomalies;
//                                break;
//                            }
//                        }
//                        if (auxGenericAnomalies != null) {
//                            //aqui se codifica o defeito congenito nascido posteriormente
//                        }
//                    }
                }

                System.out.println("Calculou: " + k);

                ant = jp;
                hashAntAnomalies = hashAnomalies;
                k++;
                if (rev.getNext().size() == 0) {
                    rev = null;
                } else {
                    rev = rev.getNext().get(0);
                }

            }

            writer0.close();

        } catch (Exception e) {
            System.out.println("Erro verificarHistogramasCongenitalAfterClass: " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        try {

            PrintWriter writer0 = new PrintWriter(path + "congenital_after_class_classe_NumberOfAttributes.txt", "UTF-8");
            Set<String> auxSet = hashmapNumberOfAttributesCongenitalAfterClass.keySet();
            Iterator it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumberOfAttributesCongenitalAfterClass.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "congenital_after_class_classe_NumberOfMethods.txt", "UTF-8");
            auxSet = hashmapNumberOfMethodsCongenitalAfterClass.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumberOfMethodsCongenitalAfterClass.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "congenital_after_class_classe_NumberOfImplementedInterfaces.txt", "UTF-8");
            auxSet = hashmapNumberOfImplementedInterfacesCongenitalAfterClass.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumberOfImplementedInterfacesCongenitalAfterClass.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "congenital_after_class_classe_NumberOfTotalComplexity.txt", "UTF-8");
            auxSet = hashmapNumberOfTotalComplexityCongenitalAfterClass.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumberOfTotalComplexityCongenitalAfterClass.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "congenital_after_class_classe_NumberOfComplexityAverage.txt", "UTF-8");
            auxSet = hashmapNumberOfComplexityAverageCongenitalAfterClass.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumberOfComplexityAverageCongenitalAfterClass.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "congenital_after_class_classe_NumberOfAcessoDadoEstrangeiro.txt", "UTF-8");
            auxSet = hashmapNumberOfAcessoDadoEstrangeiroCongenitalAfterClass.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumberOfAcessoDadoEstrangeiroCongenitalAfterClass.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "congenital_after_class_classe_NumeroDeDependenciaAClassesInternas.txt", "UTF-8");
            auxSet = hashmapNumeroDeDependenciaAClassesInternasCongenitalAfterClass.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumeroDeDependenciaAClassesInternasCongenitalAfterClass.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "congenital_after_class_classe_TightClassCohesion.txt", "UTF-8");
            auxSet = hashmapTightClassCohesionCongenitalAfterClass.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapTightClassCohesionCongenitalAfterClass.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "congenital_after_class_classe_NumeroDeClassesClientesInternas.txt", "UTF-8");
            auxSet = hashmapNumeroDeClassesClientesInternasCongenitalAfterClass.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumeroDeClassesClientesInternasCongenitalAfterClass.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "congenital_after_class_classe_ClassLocality.txt", "UTF-8");
            auxSet = hashmapClassLocalityCongenitalAfterClass.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapClassLocalityCongenitalAfterClass.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            //saudavel
            writer0 = new PrintWriter(path + "saudavel_classe_NumberOfAttributes.txt", "UTF-8");
            auxSet = hashmapNumberOfAttributesSaudavel.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumberOfAttributesSaudavel.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "saudavel_classe_NumberOfMethods.txt", "UTF-8");
            auxSet = hashmapNumberOfMethodsSaudavel.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumberOfMethodsSaudavel.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "saudavel_classe_NumberOfImplementedInterfaces.txt", "UTF-8");
            auxSet = hashmapNumberOfImplementedInterfacesSaudavel.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumberOfImplementedInterfacesSaudavel.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "saudavel_classe_NumberOfTotalComplexity.txt", "UTF-8");
            auxSet = hashmapNumberOfTotalComplexitySaudavel.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumberOfTotalComplexitySaudavel.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "saudavel_classe_NumberOfComplexityAverage.txt", "UTF-8");
            auxSet = hashmapNumberOfComplexityAverageSaudavel.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumberOfComplexityAverageSaudavel.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "saudavel_classe_NumberOfAcessoDadoEstrangeiro.txt", "UTF-8");
            auxSet = hashmapNumberOfAcessoDadoEstrangeiroSaudavel.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumberOfAcessoDadoEstrangeiroSaudavel.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "saudavel_classe_NumeroDeDependenciaAClassesInternas.txt", "UTF-8");
            auxSet = hashmapNumeroDeDependenciaAClassesInternasSaudavel.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumeroDeDependenciaAClassesInternasSaudavel.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "saudavel_classe_TightClassCohesion.txt", "UTF-8");
            auxSet = hashmapTightClassCohesionSaudavel.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapTightClassCohesionSaudavel.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "saudavel_classe_NumeroDeClassesClientesInternas.txt", "UTF-8");
            auxSet = hashmapNumeroDeClassesClientesInternasSaudavel.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumeroDeClassesClientesInternasSaudavel.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "saudavel_classe_ClassLocality.txt", "UTF-8");
            auxSet = hashmapClassLocalitySaudavel.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapClassLocalitySaudavel.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

        } catch (Exception e) {
            System.out.println("Erro verificarHistogramOfValues: " + e.getLocalizedMessage());
            e.printStackTrace();
        }

    }
    private int typeMetricNumberOfAttributes = 0;
    private int typeMetricNumberOfMethods = 1;
    private int typeMetricNumberOfImplementedInterfaces = 2;
    private int typeMetricNumberOfTotalComplexity = 3;
    private int typeMetricNumberOfComplexityAverage = 4;
    private int typeMetricNumberOfAcessoDadoEstrangeiro = 5;
    private int typeMetricNumeroDeDependenciaAClassesInternas = 6;
    private int typeMetricTightClassCohesion = 7;
    private int typeMetricNumeroDeClassesClientesInternas = 8;
    private int typeMetricClassLocality = 9;

    private int typeMetricMaxCyclomaticComplexityMethod = 10;
    private int typeMetricMinCyclomaticComplexityMethod = 11;
    private int typeMetricNumberOfClientClasses = 12;
    private int typeMetricNumberOfClientPackages = 13;
    private int typeMetricExternalDependencyClasses = 14;
    private int typeMetricNumberOfExternalDependencyPackages = 15;

    public void calculateMetrics(JavaClass javaClass, List<HashMap<String, Integer>> hashMapMetric) {

        Integer num = null;

        HashMap<String, Integer> hashMapAux = hashMapMetric.get(typeMetricNumberOfAttributes);

        int numberofAttributes = javaClass.getAttributes().size();
        num = hashMapAux.get(String.valueOf(numberofAttributes));
        if (num == null) {
            hashMapAux.put(String.valueOf(numberofAttributes), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(numberofAttributes), num);
        }

        hashMapAux = hashMapMetric.get(typeMetricNumberOfImplementedInterfaces);
        int numberofImplementedInterfaces = javaClass.getImplementedInterfaces().size();
        num = hashMapAux.get(String.valueOf(numberofImplementedInterfaces));
        if (num == null) {
            hashMapAux.put(String.valueOf(numberofImplementedInterfaces), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(numberofImplementedInterfaces), num);
        }

        hashMapAux = hashMapMetric.get(typeMetricNumberOfMethods);
        int numberofMethods = javaClass.getMethods().size();
        num = hashMapAux.get(String.valueOf(numberofMethods));
        if (num == null) {
            hashMapAux.put(String.valueOf(numberofMethods), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(numberofMethods), num);
        }

        hashMapAux = hashMapMetric.get(typeMetricNumberOfTotalComplexity);
        int numberofTotalCiclomaticComplxity = javaClass.getTotalCyclomaticComplexity();
        num = hashMapAux.get(String.valueOf(numberofTotalCiclomaticComplxity));
        if (num == null) {
            hashMapAux.put(String.valueOf(numberofTotalCiclomaticComplxity), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(numberofTotalCiclomaticComplxity), num);
        }

        hashMapAux = hashMapMetric.get(typeMetricNumberOfComplexityAverage);
        double complexityAverage = 0;
        if (numberofMethods != 0) {
            complexityAverage = numberofTotalCiclomaticComplxity / numberofMethods;
        }
        num = hashMapAux.get(String.valueOf(complexityAverage));
        if (num == null) {
            hashMapAux.put(String.valueOf(complexityAverage), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(complexityAverage), num);
        }

        hashMapAux = hashMapMetric.get(typeMetricNumberOfAcessoDadoEstrangeiro);
        int acessoDadoEstrangeiro = javaClass.getAccessToForeignDataNumber();
        num = hashMapAux.get(String.valueOf(acessoDadoEstrangeiro));
        if (num == null) {
            hashMapAux.put(String.valueOf(acessoDadoEstrangeiro), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(acessoDadoEstrangeiro), num);
        }

        hashMapAux = hashMapMetric.get(typeMetricNumeroDeDependenciaAClassesInternas);
        int numeroDeDependenciaAClassesInternas = javaClass.getInternalDependencyClasses().size();
        num = hashMapAux.get(String.valueOf(numeroDeDependenciaAClassesInternas));
        if (num == null) {
            hashMapAux.put(String.valueOf(numeroDeDependenciaAClassesInternas), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(numeroDeDependenciaAClassesInternas), num);
        }

        hashMapAux = hashMapMetric.get(typeMetricTightClassCohesion);
        double tcc = 1;
        if (javaClass.getMethods().size() >= 2) {
            tcc = javaClass.getNumberOfDirectConnections();
            int n = javaClass.getMethods().size();
            tcc = tcc / ((n * (n - 1)) / 2);
        }
        num = hashMapAux.get(String.valueOf(tcc));
        if (num == null) {
            hashMapAux.put(String.valueOf(tcc), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(tcc), num);
        }

        hashMapAux = hashMapMetric.get(typeMetricNumeroDeClassesClientesInternas);
        int numeroDeClassesClientesInternas = javaClass.getintraPackageDependentClass().size();
        num = hashMapAux.get(String.valueOf(numeroDeClassesClientesInternas));
        if (num == null) {
            hashMapAux.put(String.valueOf(numeroDeClassesClientesInternas), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(numeroDeClassesClientesInternas), num);
        }

        hashMapAux = hashMapMetric.get(typeMetricClassLocality);
        double classLocality = -1;
        if (javaClass.getInternalDependencyClasses().size() + javaClass.getExternalDependencyClasses().size() > 0) {
            classLocality = javaClass.getInternalDependencyClasses().size();
            classLocality = classLocality / (javaClass.getInternalDependencyClasses().size() + javaClass.getExternalDependencyClasses().size());
        }
        num = hashMapAux.get(String.valueOf(classLocality));
        if (num == null) {
            hashMapAux.put(String.valueOf(classLocality), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(classLocality), num);
        }

        int maxComplexity = 0;
        for (JavaMethod javaMethod : javaClass.getMethods()) {
            if (javaMethod.getCyclomaticComplexity() > maxComplexity) {
                maxComplexity = javaMethod.getCyclomaticComplexity();
            }
        }
        hashMapAux = hashMapMetric.get(typeMetricMaxCyclomaticComplexityMethod);
        num = hashMapAux.get(String.valueOf(maxComplexity));
        if (num == null) {
            hashMapAux.put(String.valueOf(maxComplexity), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(maxComplexity), num);
        }

        int minComplexity = 0;
        for (JavaMethod javaMethod : javaClass.getMethods()) {
            if (javaMethod.getCyclomaticComplexity() < minComplexity) {
                minComplexity = javaMethod.getCyclomaticComplexity();
            }
        }
        hashMapAux = hashMapMetric.get(typeMetricMinCyclomaticComplexityMethod);
        num = hashMapAux.get(String.valueOf(minComplexity));
        if (num == null) {
            hashMapAux.put(String.valueOf(minComplexity), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(minComplexity), num);
        }

        hashMapAux = hashMapMetric.get(typeMetricNumberOfClientClasses);
        int numberOfClientClasses = javaClass.getClientClasses().size();
        num = hashMapAux.get(String.valueOf(numberOfClientClasses));
        if (num == null) {
            hashMapAux.put(String.valueOf(numberOfClientClasses), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(numberOfClientClasses), num);
        }

        hashMapAux = hashMapMetric.get(typeMetricNumberOfClientPackages);
        int numberOfClientPackages = javaClass.getClientPackages().size();
        num = hashMapAux.get(String.valueOf(numberOfClientPackages));
        if (num == null) {
            hashMapAux.put(String.valueOf(numberOfClientPackages), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(numberOfClientPackages), num);
        }

        hashMapAux = hashMapMetric.get(typeMetricExternalDependencyClasses);
        int numberOfExternalDependencyClasses = javaClass.getExternalDependencyClasses().size();
        num = hashMapAux.get(String.valueOf(numberOfExternalDependencyClasses));
        if (num == null) {
            hashMapAux.put(String.valueOf(numberOfExternalDependencyClasses), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(numberOfExternalDependencyClasses), num);
        }

        hashMapAux = hashMapMetric.get(typeMetricNumberOfExternalDependencyPackages);
        int numberOfExternalDependencyPackages = javaClass.getExternalDependencyPackages().size();
        num = hashMapAux.get(String.valueOf(numberOfExternalDependencyPackages));
        if (num == null) {
            hashMapAux.put(String.valueOf(numberOfExternalDependencyPackages), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(numberOfExternalDependencyPackages), num);
        }

    }

    int tipoAcessoPublico = 0;
    int tipoAcessoProtegido = 1;
    int tipoAcessoPrivado = 2;

    int tipoNaoEstatico = 0;
    int tipoEstatico = 1;

    int tipoNaoSincronizado = 0;
    int tipoSincronizado = 1;

    int tipoNaoAbstrato = 0;
    int tipoAbstrato = 1;

    int tipoMetodoNaoAcessor = 0;
    int tipoMetodoAcessor = 1;

    int tipoMetodoNotChangeInternalState = 0;
    int tipoMetodoChangeInternalState = 1;

    int tipoMetodoNotChangeInternalStateByMethodInvocations = 0;
    int tipoMetodoChangeInternalStateByMethodInvocations = 1;

    int typeMethodMetricAccessToForeignDataNumber = 0;
    int typeMethodMetricAccessToLocalDataNumber = 1;
    int typeMethodMetricChangingClassesMetric = 2;
    int typeMethodMetricChangingMethodsMetric = 3;
    int typeMethodMetricCyclomaticComplexity = 4;
    int typeMethodMetricForeignDataProviderNumber = 5;
    int typeMethodMetricNumberOfInternalMethodInvocations = 6;
    int typeMethodMetricNumberOfInternalMethodsThatCallMe = 7;
    int typeMethodMetricNumberOfMethodInvocations = 8;
    int typeMethodMetricNumberOfLines = 9;
    int typeMethodMetricNumberOfLocalVariables = 10;
    int typeMethodMetricNumberOfParameters = 11;
    int typeMethodMetricTypeOfAccess = 12;
    int typeMethodMetricIsStatic = 13;
    int typeMethodMetricisSynchronized = 14;
    int typeMethodMetricIsAbstract = 15;
    int typeMethodMetricIsAnAcessorMethod = 16;
    int typeMethodMetricIsChangeInternalState = 17;
    int typeMethodMetricIsChangeInternalStateByMethodInvocations = 18;

    public void calculateMetricsofMethods(JavaMethod javaMethod, List<HashMap<String, Integer>> hashMapMethodMetric) {

        Integer num = null;

        HashMap<String, Integer> hashMapAux = hashMapMethodMetric.get(typeMethodMetricAccessToForeignDataNumber);

        int accessToForeignDataNumber = javaMethod.getAccessToForeignDataNumber();
        num = hashMapAux.get(String.valueOf(accessToForeignDataNumber));
        if (num == null) {
            hashMapAux.put(String.valueOf(accessToForeignDataNumber), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(accessToForeignDataNumber), num);
        }

        hashMapAux = hashMapMethodMetric.get(typeMethodMetricAccessToLocalDataNumber);

        int accessToLocalDataNumber = javaMethod.getAccessToLocalDataNumber();
        num = hashMapAux.get(String.valueOf(accessToLocalDataNumber));
        if (num == null) {
            hashMapAux.put(String.valueOf(accessToLocalDataNumber), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(accessToLocalDataNumber), num);
        }

        hashMapAux = hashMapMethodMetric.get(typeMethodMetricChangingClassesMetric);

        int changingClassesMetric = javaMethod.getChangingClassesMetric();
        num = hashMapAux.get(String.valueOf(changingClassesMetric));
        if (num == null) {
            hashMapAux.put(String.valueOf(changingClassesMetric), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(changingClassesMetric), num);
        }

        hashMapAux = hashMapMethodMetric.get(typeMethodMetricChangingMethodsMetric);

        int changingMethodsMetric = javaMethod.getChangingMethodsMetric();
        num = hashMapAux.get(String.valueOf(changingMethodsMetric));
        if (num == null) {
            hashMapAux.put(String.valueOf(changingMethodsMetric), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(changingMethodsMetric), num);
        }

        hashMapAux = hashMapMethodMetric.get(typeMethodMetricCyclomaticComplexity);

        int cyclomaticComplexity = javaMethod.getCyclomaticComplexity();
        num = hashMapAux.get(String.valueOf(cyclomaticComplexity));
        if (num == null) {
            hashMapAux.put(String.valueOf(cyclomaticComplexity), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(cyclomaticComplexity), num);
        }

        hashMapAux = hashMapMethodMetric.get(typeMethodMetricForeignDataProviderNumber);

        int foreignDataProviderNumber = javaMethod.getForeignDataProviderNumber();
        num = hashMapAux.get(String.valueOf(foreignDataProviderNumber));
        if (num == null) {
            hashMapAux.put(String.valueOf(foreignDataProviderNumber), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(foreignDataProviderNumber), num);
        }

        hashMapAux = hashMapMethodMetric.get(typeMethodMetricNumberOfInternalMethodInvocations);

        int numberOfInternalMethodInvocations = javaMethod.getInternalMethodInvocations().size();
        num = hashMapAux.get(String.valueOf(numberOfInternalMethodInvocations));
        if (num == null) {
            hashMapAux.put(String.valueOf(numberOfInternalMethodInvocations), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(numberOfInternalMethodInvocations), num);
        }

        hashMapAux = hashMapMethodMetric.get(typeMethodMetricNumberOfInternalMethodsThatCallMe);

        int numberOfInternalMethodsThatCallMe = javaMethod.getInternalMethodsThatCallMe().size();
        num = hashMapAux.get(String.valueOf(numberOfInternalMethodsThatCallMe));
        if (num == null) {
            hashMapAux.put(String.valueOf(numberOfInternalMethodsThatCallMe), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(numberOfInternalMethodsThatCallMe), num);
        }

        hashMapAux = hashMapMethodMetric.get(typeMethodMetricNumberOfMethodInvocations);

        int numberOfMethodInvocations = javaMethod.getMethodInvocations().size();
        num = hashMapAux.get(String.valueOf(numberOfMethodInvocations));
        if (num == null) {
            hashMapAux.put(String.valueOf(numberOfMethodInvocations), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(numberOfMethodInvocations), num);
        }

        hashMapAux = hashMapMethodMetric.get(typeMethodMetricNumberOfLines);

        int numberOfLines = javaMethod.getNumberOfLines();
        num = hashMapAux.get(String.valueOf(numberOfLines));
        if (num == null) {
            hashMapAux.put(String.valueOf(numberOfLines), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(numberOfLines), num);
        }

        hashMapAux = hashMapMethodMetric.get(typeMethodMetricNumberOfLocalVariables);

        int numberOfLocalVariables = javaMethod.getNumberOfLocalVariables();
        num = hashMapAux.get(String.valueOf(numberOfLocalVariables));
        if (num == null) {
            hashMapAux.put(String.valueOf(numberOfLocalVariables), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(numberOfLocalVariables), num);
        }

        hashMapAux = hashMapMethodMetric.get(typeMethodMetricNumberOfParameters);

        int numberOfParameters = javaMethod.getParameters().size();
        num = hashMapAux.get(String.valueOf(numberOfParameters));
        if (num == null) {
            hashMapAux.put(String.valueOf(numberOfParameters), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(numberOfParameters), num);
        }

        hashMapAux = hashMapMethodMetric.get(typeMethodMetricIsAbstract);

        int isAbstract = 0;
        if (javaMethod.isAbstract()) {
            isAbstract = 1;
        }
        num = hashMapAux.get(String.valueOf(isAbstract));
        if (num == null) {
            hashMapAux.put(String.valueOf(isAbstract), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(isAbstract), num);
        }

        hashMapAux = hashMapMethodMetric.get(typeMethodMetricTypeOfAccess);

        int acessType = 0;
        if (javaMethod.isProtected()) {
            acessType = 1;
        } else if (javaMethod.isPrivate()) {
            acessType = 2;
        }
        num = hashMapAux.get(String.valueOf(acessType));
        if (num == null) {
            hashMapAux.put(String.valueOf(acessType), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(acessType), num);
        }

        hashMapAux = hashMapMethodMetric.get(typeMethodMetricIsStatic);

        int isStatic = 0;
        if (javaMethod.isStatic()) {
            isStatic = 1;
        }
        num = hashMapAux.get(String.valueOf(isStatic));
        if (num == null) {
            hashMapAux.put(String.valueOf(isStatic), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(isStatic), num);
        }

        hashMapAux = hashMapMethodMetric.get(typeMethodMetricisSynchronized);

        int isSynchronized = 0;
        if (javaMethod.isSynchronized()) {
            isSynchronized = 1;
        }
        num = hashMapAux.get(String.valueOf(isSynchronized));
        if (num == null) {
            hashMapAux.put(String.valueOf(isSynchronized), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(isSynchronized), num);
        }

        hashMapAux = hashMapMethodMetric.get(typeMethodMetricIsAnAcessorMethod);

        int isAnAcessorMethod = 0;
        if (javaMethod.isAnAcessorMethod()) {
            isAnAcessorMethod = 1;
        }
        num = hashMapAux.get(String.valueOf(isAnAcessorMethod));
        if (num == null) {
            hashMapAux.put(String.valueOf(isAnAcessorMethod), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(isAnAcessorMethod), num);
        }

        hashMapAux = hashMapMethodMetric.get(typeMethodMetricIsChangeInternalState);

        int isChangeInternalState = 0;
        if (javaMethod.isChangeInternalState()) {
            isChangeInternalState = 1;
        }
        num = hashMapAux.get(String.valueOf(isChangeInternalState));
        if (num == null) {
            hashMapAux.put(String.valueOf(isChangeInternalState), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(isChangeInternalState), num);
        }

        hashMapAux = hashMapMethodMetric.get(typeMethodMetricIsChangeInternalStateByMethodInvocations);

        int isChangeInternalStateByMethodInvocations = 0;
        if (javaMethod.isChangeInternalStateByMethodInvocations()) {
            isChangeInternalStateByMethodInvocations = 1;
        }
        num = hashMapAux.get(String.valueOf(isChangeInternalStateByMethodInvocations));
        if (num == null) {
            hashMapAux.put(String.valueOf(isChangeInternalStateByMethodInvocations), 1);
        } else {
            num++;
            hashMapAux.put(String.valueOf(isChangeInternalStateByMethodInvocations), num);
        }

    }

    public void verificarAnomaliasDeMetodos(ProjectRevisions newProjectRevisions, Project project, JavaConstructorService javaConstructorService) {

        long t1 = System.currentTimeMillis();
        Revision rev = newProjectRevisions.getRoot();
        int k = 0;
        while (rev != null) {
            k++;
            if (rev.getNext().size() == 0) {
                rev = null;
            } else {
                rev = rev.getNext().get(0);
            }
        }

        HashMap<Integer, String> dirAnomalies = new HashMap();
        dirAnomalies.put(Constants.ANOMALIE_FEATURE_ENVY, "histogram_feature_envy");
        dirAnomalies.put(Constants.ANOMALIE_SHOTGUN_SURGERY, "histogram_shotgun_surgery");
        dirAnomalies.put(Constants.ANOMALIE_GOD_METHOD, "histogram_god_method");

        HashMap<Integer, String> metricsHashMap = new HashMap();
        metricsHashMap.put(0, "number_of_attributes");
        metricsHashMap.put(1, "number_of_methods");
        metricsHashMap.put(2, "number_of_implemented_interfaces");
        metricsHashMap.put(3, "number_of_total_complexity");
        metricsHashMap.put(4, "average_complexity");
        metricsHashMap.put(5, "acesso_dado_estrangeiro");
        metricsHashMap.put(6, "numero_dependencia_classes_internas");
        metricsHashMap.put(7, "tight_class_cohesion");
        metricsHashMap.put(8, "numero_classes_clientes_internas");
        metricsHashMap.put(9, "class_locality");

        metricsHashMap.put(10, "max_cyclomatic_complexity");
        metricsHashMap.put(11, "min_cyclomatic_complexity");
        metricsHashMap.put(12, "number_of_client_classes");
        metricsHashMap.put(13, "number_of_client_packages");
        metricsHashMap.put(14, "external_dependency_classes");
        metricsHashMap.put(15, "external_dependency_packages");

        HashMap<Integer, String> metricsMethodHashMap = new HashMap();
        metricsMethodHashMap.put(0, "access_to_foreign_data_number");
        metricsMethodHashMap.put(1, "access_to_local_data_number");
        metricsMethodHashMap.put(2, "changing_classes_metric");
        metricsMethodHashMap.put(3, "changing_methods_metric");
        metricsMethodHashMap.put(4, "cyclomatic_complexity");
        metricsMethodHashMap.put(5, "foreign_data_provider_number");
        metricsMethodHashMap.put(6, "number_of_internal_method_invocations");
        metricsMethodHashMap.put(7, "number_of_internalMethods_that_call_me");
        metricsMethodHashMap.put(8, "number_of_method_invocations");
        metricsMethodHashMap.put(9, "number_of_lines");
        metricsMethodHashMap.put(10, "number_of_local_variables");
        metricsMethodHashMap.put(11, "number_of_parameters");
        metricsMethodHashMap.put(12, "type_of_access");
        metricsMethodHashMap.put(13, "is_static");
        metricsMethodHashMap.put(14, "is_synchronized");
        metricsMethodHashMap.put(15, "is_abstract");
        metricsMethodHashMap.put(16, "is_an_acessor_method");
        metricsMethodHashMap.put(17, "is_change_internal_state");
        metricsMethodHashMap.put(18, "is_change_internal_state_by_method_invocations");

        //como estava imediatamente antes de nascer algum defeito congenito
        int typeCongenitalAfterClass = 0;
        //como estava quando surgiu um metodo novo e estava saudavel
        int typeSurgiuMetodoEContinuaSaudavel = 1;
        //como estava a classe anteriormente quando surgiu a anomalia adquirida
        int typeAdquiredAnomalie = 2;
        //como estava a classe quando surge uma anomalia congenita (diferente dos outros mostra a classe no momento que ocorreu)
        int typeCongenitalWithClass = 3;
        //como estava a classe no momento do surgimento da anomalia (portanto é msito)
        int typeCongenital = 4;
        //como estava a classe quando surge a anomalia
        int typeAnomalies = 5;
        //como estava a classe e está sadia
        int typeSurgiuClasseEContinuaSaudavel = 6;
        //como estava a classe quando houve modificacao no metodo, entretanto continuou sendo sadia
        int typeMudouMetodoContinuaSaudavel = 7;
        //mudou o metodo e foi corrigido pra sempre
        int typeCorrigidoMetodoPraSempre = 8;
        //mudou o metodo e foi corrigido mas volta;
        int typeCorrigidoMasVolta = 9;
        //jamais possuiu alguma anomalia;
        int typeJamaisAnomalia = 10;
        //anomalia volta
        int typeAnomaliaVolta = 11;
        //como a classe esta enquanto possui alguma anomalia
        int typeClasseComAlgumaAnomalia = 12;
        //como a classe esta quando esta sem anomalia
        int typeClasseSemAnomalia = 13;
        //como a classe esta quando jamais tem anomalia
        int typeClasseNuncaTeveAnomalia = 14;

        //esta atualmetne com anomalia
        int typeMethodEstaComAnomalia = 0;
        //nao esta atualmetne com anomalia
        int typeMethodEstaSemAnomalia = 1;
        //nunca terá anomalia 
        int typeMethodNuncaTeraAnomalia = 2;
        //como esta imeditamente antes de surgir a anomalia
        int typeMethodLogoAntesDeSurgirAnomalia = 3;
        //foi corrigido pra mas volta
        int typeMethodFoiCorrigidoMasVolta = 4;
        //foi corrigido pra sempre
        int typeMethodFoiCorrigidoPraSempre = 5;

        HashMap<Integer, String> typeOfAnomalieHashMap = new HashMap();
        typeOfAnomalieHashMap.put(0, "congenital_after_class");
        typeOfAnomalieHashMap.put(1, "add_method_and_healthly");
        typeOfAnomalieHashMap.put(2, "adquired_anomalie");
        typeOfAnomalieHashMap.put(3, "congenital_with_class");
        typeOfAnomalieHashMap.put(4, "congenital");
        typeOfAnomalieHashMap.put(5, "all_anomalies");
        typeOfAnomalieHashMap.put(6, "new_method_healthly_class");
        typeOfAnomalieHashMap.put(7, "change_and_healthly");
        typeOfAnomalieHashMap.put(8, "corrected_forever_healthly");
        typeOfAnomalieHashMap.put(9, "corrrected_and_back_anomalie_in_future");
        typeOfAnomalieHashMap.put(10, "healthly_forever");
        typeOfAnomalieHashMap.put(11, "return_anomalie");
        typeOfAnomalieHashMap.put(12, "class_with_some_anomalie");
        typeOfAnomalieHashMap.put(13, "class_without_anomalie");
        typeOfAnomalieHashMap.put(14, "class_never_anomalie");

        HashMap<Integer, String> typeOfMethodAnomalieHashMap = new HashMap();
        typeOfMethodAnomalieHashMap.put(0, "method_with_anomalie");
        typeOfMethodAnomalieHashMap.put(1, "method_without_anomalie");
        typeOfMethodAnomalieHashMap.put(2, "method_never_anomalie");
        typeOfMethodAnomalieHashMap.put(3, "method_right_before_anomalie");
        typeOfMethodAnomalieHashMap.put(4, "method_corrected_but_return");
        typeOfMethodAnomalieHashMap.put(5, "method_corrected_forever");

        //Lista por tipo (congenita, saudavel, etc)
        List<List<HashMap<String, Integer>>> hashMapsFeatureEnvyAnomalie = new LinkedList();
        List<List<HashMap<String, Integer>>> hashMapsShotgunSurgeryAnomalies = new LinkedList();
        List<List<HashMap<String, Integer>>> hashMapsGodMethodAnomalies = new LinkedList();

        //ccongenito ou saudavel, ou que mais seja
        for (int i = 0; i < 15; i++) {
            List featureEnvyMetric = new LinkedList();
            List featureShotgunSurgeryMetric = new LinkedList();
            List featureGodMethodMetric = new LinkedList();
            //lista de métricas
            for (int j = 0; j < 16; j++) {
                HashMap<String, Integer> featureEnvyHashMap = new HashMap();
                HashMap<String, Integer> featureShotgunSurgeryHashMap = new HashMap();
                HashMap<String, Integer> featureGodMethodHashMap = new HashMap();

                featureEnvyMetric.add(featureEnvyHashMap);
                featureShotgunSurgeryMetric.add(featureShotgunSurgeryHashMap);
                featureGodMethodMetric.add(featureGodMethodHashMap);
            }

            hashMapsFeatureEnvyAnomalie.add(featureEnvyMetric);
            hashMapsShotgunSurgeryAnomalies.add(featureShotgunSurgeryMetric);
            hashMapsGodMethodAnomalies.add(featureGodMethodMetric);

        }

        //evolução dos metodos com anomalia ou sem anomalia
        List<List<HashMap<String, Integer>>> hashMapsEvolutionMethodsGodMethodAnomalies = new LinkedList();
        //ccongenito ou saudavel, ou que mais seja
        for (int i = 0; i < 6; i++) {
            List featureEnvyMetric = new LinkedList();
            List featureShotgunSurgeryMetric = new LinkedList();
            List featureGodMethodMetric = new LinkedList();
            //lista de métricas
            for (int j = 0; j < 19; j++) {
                HashMap<String, Integer> featureEnvyHashMap = new HashMap();
                HashMap<String, Integer> featureShotgunSurgeryHashMap = new HashMap();
                HashMap<String, Integer> featureGodMethodHashMap = new HashMap();

                featureEnvyMetric.add(featureEnvyHashMap);
                featureShotgunSurgeryMetric.add(featureShotgunSurgeryHashMap);
                featureGodMethodMetric.add(featureGodMethodHashMap);
            }

            hashMapsEvolutionMethodsGodMethodAnomalies.add(featureEnvyMetric);
            hashMapsEvolutionMethodsGodMethodAnomalies.add(featureShotgunSurgeryMetric);
            hashMapsEvolutionMethodsGodMethodAnomalies.add(featureGodMethodMetric);

        }

        String path = System.getProperty("user.home") + "/.archd/histogramas_anomalias/";
        HashMap<String, Integer> hashAntAnomalies = null;

        int numberofAddmethods = 0;

        //classificar as anomalias quanto aos seus 24 tipos
        JavaConstructorService javaContructorService = new JavaConstructorService();
        AnomaliesAnaliser anomaliesAnaliser = new AnomaliesAnaliser();
        ProjectAnomalies projectAnomalies = anomaliesAnaliser.getAnomalies(newProjectRevisions, project, javaContructorService);

        long tm = 0;

        try {
            //PrintWriter writer0 = new PrintWriter(path + "map_add_methods_analise_emerge_and_correct_difference.txt", "UTF-8");

            rev = newProjectRevisions.getRoot();
            AnomalieDao anomalieDao = DataBaseFactory.getInstance().getAnomalieDao();
            HashMap<String, Integer> birthHashMap = new HashMap();
            HashMap<String, String> methodsAlternativeNameMap = new HashMap();
            HashMap<String, String> classesAlternativeNameMap = new HashMap();
            k = 0;

            List<GenericAnomalies> methodGenericAnomalies = projectAnomalies.getAllMethodAnomalies();

            tm = System.currentTimeMillis();

            JavaProject ant = null;
            while (rev != null) {

                JavaProject jp = null;
                //System.out.println("REV ID: "+rev.getId());
                System.gc();
                System.out.println("********************************* vai pegar um projeto completo");
                jp = javaConstructorService.getProjectByRevisionAndSetRevision(project.getName(), project.getCodeDirs(), project.getPath(), rev.getId(), newProjectRevisions.getName());

                for (JavaPackage javaPackage : jp.getPackages()) {
                    if (birthHashMap.get(javaPackage.getName()) == null) {
                        birthHashMap.put(javaPackage.getName(), k);
                    }
                }

                for (JavaAbstract javaAbstract : jp.getClasses()) {
                    JavaClass javaClass = (JavaClass) javaAbstract;

                    if (birthHashMap.get(javaClass.getFullQualifiedName()) == null) {
                        birthHashMap.put(javaClass.getFullQualifiedName(), k);

                        if (classesAlternativeNameMap.get(javaClass.getFullQualifiedName()) == null) {
                            if (ant != null) {
                                List<JavaAbstract> antClasses = ant.getClassByLastName(javaAbstract.getName());
                                List<JavaAbstract> sameClasses = new LinkedList();
                                for (JavaAbstract antClass : antClasses) {
                                    if (isSameJavaClass(javaClass, (JavaClass) antClass)) {
                                        sameClasses.add(antClass);
                                    } else {
                                        System.out.println(javaClass.getFullQualifiedName() + " - is not same - " + antClass.getFullQualifiedName());
                                    }
                                }
                                if (sameClasses.size() > 1) {
                                    System.out.println("********** Erro, duas classes parecidas");
                                } else {
                                    if (sameClasses.size() == 1) {
                                        String alternativeName = classesAlternativeNameMap.get(sameClasses.get(0).getFullQualifiedName());

                                        if (alternativeName == null) {
                                            alternativeName = sameClasses.get(0).getFullQualifiedName();
                                        }
                                        System.out.println("Classe mudou de nome: " + javaClass.getFullQualifiedName() + "   -    " + alternativeName);
                                        classesAlternativeNameMap.put(javaClass.getFullQualifiedName(), alternativeName);

                                    }
                                }

                            }
                        }

                    }
                    List<JavaMethod> newMethods = new LinkedList();
                    List<JavaMethod> newMethodsClasseAlternativeName = new LinkedList();
                    
                    for (JavaMethod javaMethod : javaClass.getMethods()) {

                        //ainda nao foi dito a existencia desse metodo
                        if (birthHashMap.get(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature()) == null) {

                            birthHashMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), k);
                            if (methodsAlternativeNameMap.get(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature()) == null) {
                                methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature());
                                String alternativeClassName = classesAlternativeNameMap.get(javaClass.getFullQualifiedName());
                                //o mÃ©todo ainda nao surgiu e a classe que ele esta mudou de nome
                                if (alternativeClassName != null) {
                                    //ainda nao surgiu o mÃ©todo
                                    String alternativeMethodName = methodsAlternativeNameMap.get(alternativeClassName + ":" + javaMethod.getMethodSignature());
                                    if (alternativeMethodName == null) {
                                        if (ant != null) {
                                            
                                            //adiciona ao campo dos metodos novos
                                            newMethodsClasseAlternativeName.add(javaMethod);
                                            
//                                            JavaAbstract antAbstract = ant.getClassByName(javaClass.getFullQualifiedName());
//                                            if (antAbstract != null && antAbstract.getClass() == JavaClass.class) {
//
//                                                List<JavaMethod> methodsName = new LinkedList();
//                                                List<JavaMethod> methodsNameAnt = ((JavaClass) antAbstract).getMethodsByName(javaMethod.getName());
//                                                List<JavaMethod> methodsNameCurrent = javaClass.getMethodsByName(javaMethod.getName());
//                                                for (JavaMethod jm : methodsNameAnt) {
//                                                    boolean exists = false;
//                                                    for (JavaMethod auxJm : methodsNameCurrent) {
//                                                        if (auxJm.getMethodSignature().equals(jm.getMethodSignature())) {
//                                                            exists = true;
//                                                            break;
//                                                        }
//                                                    }
//                                                    if (!exists) {
//                                                        methodsName.add(jm);
//                                                    }
//                                                }
//
//                                                if (!methodsName.isEmpty()) {
//                                                    JavaMethod auxJm = closestMethod(javaMethod, methodsName);
//                                                    alternativeMethodName = methodsAlternativeNameMap.get(javaClass.getFullQualifiedName() + ":" + auxJm.getMethodSignature());
//                                                    if (alternativeMethodName == null) {
//                                                        alternativeMethodName = alternativeClassName + ":" + auxJm.getMethodSignature();
//                                                    }
//                                                    System.out.println("Metodo mudou de assinatura: " + javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + "   -    " + alternativeMethodName);
//                                                    methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), alternativeMethodName);
//
//                                                }
//
//                                            }
                                        }
                                    } else {
                                        //a classe mudou de nome e a encontramos com esse nome novo
                                        methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), alternativeMethodName);
                                        System.out.println("Metodo mudou nome completo (por causa da mudanÃ§a do nome da classe): " + javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + "   -    " + alternativeMethodName);
                                    }
                                } else {
                                    // a classe nao mudou de nome
                                    if (ant != null) {
                                        //adiciona ao campo dos metodos novos
                                        newMethods.add(javaMethod);
                                        //verificaremos se algum mpetodo existia antes e sumiu depois, pode ter se transformado
//                                        JavaAbstract antAbstract = ant.getClassByName(javaClass.getFullQualifiedName());
//                                        if (antAbstract != null && antAbstract.getClass() == JavaClass.class) {
//
//                                            List<JavaMethod> methodsName = new LinkedList();
//                                            List<JavaMethod> methodsNameAnt = ((JavaClass) antAbstract).getMethodsByName(javaMethod.getName());
//                                            List<JavaMethod> methodsNameCurrent = javaClass.getMethodsByName(javaMethod.getName());
//                                            for (JavaMethod jm : methodsNameAnt) {
//                                                boolean exists = false;
//                                                for (JavaMethod auxJm : methodsNameCurrent) {
//                                                    if (auxJm.getMethodSignature().equals(jm.getMethodSignature())) {
//                                                        exists = true;
//                                                        break;
//                                                    }
//                                                }
//                                                if (!exists) {
//                                                    methodsName.add(jm);
//                                                }
//                                            }
//
//                                            if (!methodsName.isEmpty()) {
//                                                JavaMethod auxJm = closestMethod(javaMethod, methodsName);
//                                                String alternativeMethodName = methodsAlternativeNameMap.get(javaClass.getFullQualifiedName() + ":" + auxJm.getMethodSignature());
//                                                if (alternativeMethodName == null) {
//                                                    alternativeMethodName = javaClass.getFullQualifiedName() + ":" + auxJm.getMethodSignature();
//                                                }
//                                                System.out.println("Metodo mudou de assinatura: " + javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + "   -    " + alternativeMethodName);
//                                                methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), alternativeMethodName);
//
//                                            }
//
//                                        }
                                    }
                                }
                            }

                        }
                    }

                    boolean terminar = false;
                    if (!newMethods.isEmpty()) {
                        while (!terminar) {
                            if (newMethods.isEmpty()) {
                                terminar = true;
                            } else {
                                JavaMethod javaMethodFirst = newMethods.get(0);
                                List<JavaMethod> methodsWithSameName = new LinkedList();
                                methodsWithSameName.add(javaMethodFirst);
                                for (int i = 1; i < newMethods.size(); i++) {
                                    if (newMethods.get(i).getName().equals(javaMethodFirst.getName())) {
                                        methodsWithSameName.add(newMethods.get(i));
                                    }
                                }
                                for (JavaMethod jm : methodsWithSameName) {
                                    newMethods.remove(jm);
                                }

                                for (JavaMethod javaMethod : methodsWithSameName) {
                                    //verificaremos se algum mpetodo existia antes e sumiu depois, pode ter se transformado
                                    JavaAbstract antAbstract = ant.getClassByName(javaClass.getFullQualifiedName());
                                    if (antAbstract != null && antAbstract.getClass() == JavaClass.class) {

                                        List<JavaMethod> methodsName = new LinkedList();
                                        List<JavaMethod> methodsNameAnt = ((JavaClass) antAbstract).getMethodsByName(javaMethod.getName());
                                        List<JavaMethod> methodsNameCurrent = javaClass.getMethodsByName(javaMethod.getName());
                                        for (JavaMethod jm : methodsNameAnt) {
                                            boolean exists = false;
                                            for (JavaMethod auxJm : methodsNameCurrent) {
                                                if (auxJm.getMethodSignature().equals(jm.getMethodSignature())) {
                                                    exists = true;
                                                    break;
                                                }
                                            }
                                            if (!exists) {
                                                methodsName.add(jm);
                                            }
                                        }

                                        if (!methodsName.isEmpty()) {
                                            JavaMethod auxJm = closestMethod(javaMethod, methodsWithSameName, methodsName);
                                            if (auxJm != null) {
                                                String alternativeMethodName = methodsAlternativeNameMap.get(javaClass.getFullQualifiedName() + ":" + auxJm.getMethodSignature());
                                                if (alternativeMethodName == null) {
                                                    alternativeMethodName = javaClass.getFullQualifiedName() + ":" + auxJm.getMethodSignature();
                                                }
                                                System.out.println("Metodo mudou de assinatura: " + javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + "   -    " + alternativeMethodName);
                                                methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), alternativeMethodName);
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }

                    terminar = false;
                    if (!newMethodsClasseAlternativeName.isEmpty()) {
                        while (!terminar) {
                            if (newMethodsClasseAlternativeName.isEmpty()) {
                                terminar = true;
                            } else {
                                JavaMethod javaMethodFirst = newMethodsClasseAlternativeName.get(0);
                                List<JavaMethod> methodsWithSameName = new LinkedList();
                                methodsWithSameName.add(javaMethodFirst);
                                for (int i = 1; i < newMethodsClasseAlternativeName.size(); i++) {
                                    if (newMethodsClasseAlternativeName.get(i).getName().equals(javaMethodFirst.getName())) {
                                        methodsWithSameName.add(newMethodsClasseAlternativeName.get(i));
                                    }
                                }
                                for (JavaMethod jm : methodsWithSameName) {
                                    newMethodsClasseAlternativeName.remove(jm);
                                }

                                String alternativeClassName = classesAlternativeNameMap.get(javaClass.getFullQualifiedName());
                                for (JavaMethod javaMethod : methodsWithSameName) {
                                    JavaAbstract antAbstract = ant.getClassByName(javaClass.getFullQualifiedName());
                                    if (antAbstract != null && antAbstract.getClass() == JavaClass.class) {

                                        List<JavaMethod> methodsName = new LinkedList();
                                        List<JavaMethod> methodsNameAnt = ((JavaClass) antAbstract).getMethodsByName(javaMethod.getName());
                                        List<JavaMethod> methodsNameCurrent = javaClass.getMethodsByName(javaMethod.getName());
                                        for (JavaMethod jm : methodsNameAnt) {
                                            boolean exists = false;
                                            for (JavaMethod auxJm : methodsNameCurrent) {
                                                if (auxJm.getMethodSignature().equals(jm.getMethodSignature())) {
                                                    exists = true;
                                                    break;
                                                }
                                            }
                                            if (!exists) {
                                                methodsName.add(jm);
                                            }
                                        }

                                        if (!methodsName.isEmpty()) {
                                            JavaMethod auxJm = closestMethod(javaMethod, methodsName);
                                            String alternativeMethodName = methodsAlternativeNameMap.get(javaClass.getFullQualifiedName() + ":" + auxJm.getMethodSignature());
                                            if (alternativeMethodName == null) {
                                                alternativeMethodName = alternativeClassName + ":" + auxJm.getMethodSignature();
                                            }
                                            System.out.println("Metodo mudou de assinatura: " + javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + "   -    " + alternativeMethodName);
                                            methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), alternativeMethodName);

                                        }

                                    }
                                }
                            }
                        }
                    }
                }

                List<AnomalieItem> items = anomalieDao.getItemsByRevisionId(rev.getId());
                HashMap<String, Integer> hashAnomalies = new HashMap();
                for (AnomalieItem anomalieItem : items) {
                    String alternativeName = methodsAlternativeNameMap.get(anomalieItem.getItem());
                    if (alternativeName == null) {
                        alternativeName = anomalieItem.getItem();
                    }
                    hashAnomalies.put(alternativeName, anomalieItem.getAnomalieId());
                }

                //System.out.println("Pegar anomalies da revisÃ£o " + rev.getId() + " : " + (i2 - i1) + " milisegundos");
                for (JavaAbstract javaAbstract : jp.getClasses()) {
                    //posui alguem antes
                    if (ant != null && hashAntAnomalies != null) {
                        JavaClass javaClass = (JavaClass) javaAbstract;
                        JavaAbstract auxAbstract = ant.getClassByName(javaClass.getFullQualifiedName());

                        if (auxAbstract == null) {
                            List<JavaAbstract> antClasses = ant.getClassByLastName(javaAbstract.getName());
                            List<JavaAbstract> sameClasses = new LinkedList();
                            for (JavaAbstract antClass : antClasses) {
                                if (isSameJavaClass(javaClass, (JavaClass) antClass)) {
                                    sameClasses.add(antClass);
                                } else {
                                    System.out.println(javaClass.getFullQualifiedName() + " - is not same - " + antClass.getFullQualifiedName());
                                }
                            }
                            if (sameClasses.size() > 1) {
                                System.out.println("********** Erro, duas classes parecidas");
                            } else {
                                if (sameClasses.size() == 1) {
                                    auxAbstract = sameClasses.get(0);
                                }
                            }
                        }

                        //a classe já existia
                        if (auxAbstract != null && auxAbstract.getClass() == JavaClass.class) {
                            JavaClass antClass = (JavaClass) auxAbstract;

                            boolean possuiAlgumMetodo = false;
                            boolean surgiuMetodoNovo = false;
                            //boolean surgiuMetodoNovoComDefeito = false;

                            boolean possuiAlgumMetodoComDefeito = false;
                            boolean alguemFoiCorrigidoMasVolta = false;
                            boolean alguemFoiCorrigidoDefinitivamente = false;
                            boolean algumaAnomaliaVoltou = false;

                            boolean algumMetodoMudou = false;
                            boolean teraAlgumaAnomaliaAlgumDia = false;

                            for (JavaMethod javaMethod : javaClass.getMethods()) {

                                boolean metodoMudou = false;

                                String alternativeName = methodsAlternativeNameMap.get(javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature());
                                if (alternativeName == null) {
                                    alternativeName = javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature();
                                }
                                int i = birthHashMap.get(alternativeName);
                                //o método não nasceu agora,veremos então se alguma método ficou com anomalia agora
                                if (k != i) {

                                    JavaMethod antMethod = null;
                                    //System.out.println("Nome original Of Method: "+javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature());
                                    //System.out.println("AltName Of Method: "+alternativeName);
                                    //System.out.println("Ele nasceu em: "+i+"    e estamos em: "+k);

                                    //se não nasceu veremos como ela era antes
                                    for (JavaMethod jm : antClass.getMethods()) {
                                        String auxName = methodsAlternativeNameMap.get(jm.getJavaAbstract().getFullQualifiedName() + ":" + jm.getMethodSignature());
                                        //System.out.println("name Of Method: "+jm.getJavaAbstract().getFullQualifiedName() + ":" + jm.getMethodSignature());
                                        //System.out.println("auxname Of same Method: "+auxName);
                                        if (auxName != null) {
                                            if (alternativeName.equals(auxName)) {
                                                antMethod = jm;
                                                break;
                                            }
                                        }
                                    }

                                    if (antMethod != null) {
                                        //verfica se o metodo mudou em alguma de suas métricas
                                        if (javaMethod.isAbstract() != antMethod.isAbstract()) {
                                            algumMetodoMudou = true;
                                            metodoMudou = true;
                                        } else if (javaMethod.isAnAcessorMethod() != antMethod.isAnAcessorMethod()) {
                                            algumMetodoMudou = true;
                                            metodoMudou = true;
                                        } else if (javaMethod.isChangeInternalState() != antMethod.isChangeInternalState()) {
                                            algumMetodoMudou = true;
                                            metodoMudou = true;
                                        } else if (javaMethod.isChangeInternalStateByMethodInvocations() != antMethod.isChangeInternalStateByMethodInvocations()) {
                                            algumMetodoMudou = true;
                                            metodoMudou = true;
                                        } else if (javaMethod.isFinal() != antMethod.isFinal()) {
                                            algumMetodoMudou = true;
                                            metodoMudou = true;
                                        } else if (javaMethod.isPrivate() != antMethod.isPrivate()) {
                                            algumMetodoMudou = true;
                                            metodoMudou = true;
                                        } else if (javaMethod.isProtected() != antMethod.isProtected()) {
                                            algumMetodoMudou = true;
                                            metodoMudou = true;
                                        } else if (javaMethod.isPublic() != antMethod.isPublic()) {
                                            algumMetodoMudou = true;
                                            metodoMudou = true;
                                        } else if (javaMethod.isStatic() != antMethod.isStatic()) {
                                            algumMetodoMudou = true;
                                            metodoMudou = true;
                                        } else if (javaMethod.isSynchronized() != antMethod.isSynchronized()) {
                                            algumMetodoMudou = true;
                                            metodoMudou = true;
                                        } else if (javaMethod.getAccessToForeignDataNumber() != antMethod.getAccessToForeignDataNumber()) {
                                            algumMetodoMudou = true;
                                            metodoMudou = true;
                                        } else if (javaMethod.getAccessToLocalDataNumber() != antMethod.getAccessToLocalDataNumber()) {
                                            algumMetodoMudou = true;
                                            metodoMudou = true;
                                        } else if (javaMethod.getChangingClassesMetric() != antMethod.getChangingClassesMetric()) {
                                            algumMetodoMudou = true;
                                            metodoMudou = true;
                                        } else if (javaMethod.getCyclomaticComplexity() != antMethod.getCyclomaticComplexity()) {
                                            algumMetodoMudou = true;
                                            metodoMudou = true;
                                        } else if (javaMethod.getForeignDataProviderNumber() != antMethod.getForeignDataProviderNumber()) {
                                            algumMetodoMudou = true;
                                            metodoMudou = true;
                                        } else if (javaMethod.getInternalMethodInvocations().size() != antMethod.getInternalMethodInvocations().size()) {
                                            algumMetodoMudou = true;
                                            metodoMudou = true;
                                        } else if (javaMethod.getInternalMethodsThatCallMe().size() != antMethod.getInternalMethodsThatCallMe().size()) {
                                            algumMetodoMudou = true;
                                            metodoMudou = true;
                                        } else if (javaMethod.getJavaExternalAttributeAccessList().size() != antMethod.getJavaExternalAttributeAccessList().size()) {
                                            algumMetodoMudou = true;
                                            metodoMudou = true;
                                        } else if (javaMethod.getMethodInvocations().size() != antMethod.getMethodInvocations().size()) {
                                            algumMetodoMudou = true;
                                            metodoMudou = true;
                                        } else if (javaMethod.getNumberOfLines() != antMethod.getNumberOfLines()) {
                                            algumMetodoMudou = true;
                                            metodoMudou = true;
                                        } else if (javaMethod.getNumberOfLocalVariables() != antMethod.getNumberOfLocalVariables()) {
                                            algumMetodoMudou = true;
                                            metodoMudou = true;
                                        } else if (javaMethod.getParameters().size() != antMethod.getParameters().size()) {
                                            algumMetodoMudou = true;
                                            metodoMudou = true;
                                        }
                                    }

                                    String alternativeMethodName = methodsAlternativeNameMap.get(javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature());
                                    GenericAnomalies auxGenericAnomalies = null;
                                    for (GenericAnomalies genericAnomalies : methodGenericAnomalies) {
                                        if (genericAnomalies.isGenericName(alternativeMethodName)) {
                                            auxGenericAnomalies = genericAnomalies;
                                            break;
                                        }
                                    }
                                    //esse método possui uma anomalia
                                    if (auxGenericAnomalies != null) {
                                        AnomalieList anomalieList = auxGenericAnomalies.getAnomalieList("GOD METHOD");
                                        if (anomalieList != null) {
                                            boolean surgiuAnomaliaAgoraNesseMetodo = false;
                                            teraAlgumaAnomaliaAlgumDia = true;
                                            //hashMapsGodMethodAnomalies
                                            int anomalieNumberBirth = anomalieList.getAnomalieBirthNumber();
                                            int methodNumberBirth = anomalieList.getRevisionBirthNumber();
                                            //nasceu agora a anomalia
                                            if (anomalieNumberBirth + methodNumberBirth == k) {
                                                if (!anomalieList.isIsCongenital()) {
                                                    List<HashMap<String, Integer>> hashMapMetric = hashMapsGodMethodAnomalies.get(typeAdquiredAnomalie);

                                                    calculateMetrics(antClass, hashMapMetric);

                                                    hashMapMetric = hashMapsGodMethodAnomalies.get(typeAnomalies);
                                                    calculateMetrics(antClass, hashMapMetric);

                                                }
                                                surgiuAnomaliaAgoraNesseMetodo = true;
                                                List<HashMap<String, Integer>> hashMapMethodMetric = hashMapsEvolutionMethodsGodMethodAnomalies.get(typeMethodEstaComAnomalia);
                                                this.calculateMetricsofMethods(javaMethod, hashMapMethodMetric);
                                                System.out.println("1 Anomalia em: " + k + "  de  " + alternativeName + "   que agora é ( " + javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + " )ciclomatic complexity: " + javaMethod.getCyclomaticComplexity());

                                                if (antMethod != null) {
                                                    hashMapMethodMetric = hashMapsEvolutionMethodsGodMethodAnomalies.get(typeMethodLogoAntesDeSurgirAnomalia);
                                                    this.calculateMetricsofMethods(antMethod, hashMapMethodMetric);
                                                }

                                            }
                                            //System.out.println("K: "+k+"    methodNumberBirth: "+methodNumberBirth);
                                            //System.out.println("Alternative method name: "+alternativeMethodName);
                                            //System.out.println("method Name: "+auxGenericAnomalies.getGenericName());
                                            //anomalia possui defeito agora
                                            if (anomalieList.getList().get(k - methodNumberBirth)) {
                                                possuiAlgumMetodoComDefeito = true;

                                                boolean anomaliaVoltouAgora = false;
                                                //vamos ver se ela voltou agora
                                                if (k > (anomalieNumberBirth + methodNumberBirth)) {
                                                    //a anomalia voltou
                                                    if (!anomalieList.getList().get((k - methodNumberBirth) - 1)) {
                                                        algumaAnomaliaVoltou = true;
                                                        anomaliaVoltouAgora = true;
                                                    }
                                                }
                                                if (!surgiuAnomaliaAgoraNesseMetodo && anomaliaVoltouAgora) {
                                                    List<HashMap<String, Integer>> hashMapMethodMetric = hashMapsEvolutionMethodsGodMethodAnomalies.get(typeMethodEstaComAnomalia);
                                                    this.calculateMetricsofMethods(javaMethod, hashMapMethodMetric);

                                                    System.out.println("2 Anomalia em: " + k + "  de  " + alternativeName + "   que agora é ( " + javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + " )ciclomatic complexity: " + javaMethod.getCyclomaticComplexity());

                                                    if (antMethod != null) {
                                                        hashMapMethodMetric = hashMapsEvolutionMethodsGodMethodAnomalies.get(typeMethodLogoAntesDeSurgirAnomalia);
                                                        this.calculateMetricsofMethods(antMethod, hashMapMethodMetric);

                                                    }
                                                }
                                                if (!surgiuAnomaliaAgoraNesseMetodo && (metodoMudou || antMethod == null)) {
                                                    List<HashMap<String, Integer>> hashMapMethodMetric = hashMapsEvolutionMethodsGodMethodAnomalies.get(typeMethodEstaComAnomalia);
                                                    this.calculateMetricsofMethods(javaMethod, hashMapMethodMetric);

                                                    System.out.println("3 Anomalia em: " + k + "  de  " + alternativeName + "   que agora é ( " + javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + " )ciclomatic complexity: " + javaMethod.getCyclomaticComplexity());

                                                }

                                            } else {

                                                //nao possui anomalia agora
                                                boolean anomaliaFoiCorrigidaMasVolta = false;
                                                boolean anomaliaFoiCorrigidaDefinitivamente = false;
                                                //vamos ver se foi corrigido agora
                                                if (k > (anomalieNumberBirth + methodNumberBirth)) {
                                                    //acaba de ser corrigido
                                                    if (anomalieList.getList().get((k - methodNumberBirth) - 1)) {
                                                        //vamos ver se volta ou não volta
                                                        if (anomalieList.returnAnomalieInTheFuture(i)) {
                                                            //corrigido mas vai voltar
                                                            alguemFoiCorrigidoMasVolta = true;
                                                            anomaliaFoiCorrigidaMasVolta = true;
                                                        } else {
                                                            //foi corrigido e nao volta mais
                                                            alguemFoiCorrigidoDefinitivamente = true;
                                                            anomaliaFoiCorrigidaDefinitivamente = true;
                                                        }
                                                    }
                                                }
                                                if (metodoMudou || antMethod == null) {
                                                    List<HashMap<String, Integer>> hashMapMethodMetric = hashMapsEvolutionMethodsGodMethodAnomalies.get(typeMethodEstaSemAnomalia);
                                                    this.calculateMetricsofMethods(javaMethod, hashMapMethodMetric);

                                                    if (anomalieList.returnAnomalieInTheFuture(i)) {
                                                        hashMapMethodMetric = hashMapsEvolutionMethodsGodMethodAnomalies.get(typeMethodFoiCorrigidoMasVolta);
                                                        this.calculateMetricsofMethods(javaMethod, hashMapMethodMetric);
                                                    } else {
                                                        hashMapMethodMetric = hashMapsEvolutionMethodsGodMethodAnomalies.get(typeMethodFoiCorrigidoPraSempre);
                                                        this.calculateMetricsofMethods(javaMethod, hashMapMethodMetric);
                                                    }

                                                }

                                                if (!metodoMudou && anomaliaFoiCorrigidaMasVolta) {
                                                    List<HashMap<String, Integer>> hashMapMethodMetric = hashMapsEvolutionMethodsGodMethodAnomalies.get(typeMethodFoiCorrigidoMasVolta);
                                                    this.calculateMetricsofMethods(javaMethod, hashMapMethodMetric);

                                                } else if (!metodoMudou && anomaliaFoiCorrigidaDefinitivamente) {
                                                    List<HashMap<String, Integer>> hashMapMethodMetric = hashMapsEvolutionMethodsGodMethodAnomalies.get(typeMethodFoiCorrigidoPraSempre);
                                                    this.calculateMetricsofMethods(javaMethod, hashMapMethodMetric);

                                                }
                                            }

                                        } else {
                                            if (metodoMudou || antMethod == null) {
                                                List<HashMap<String, Integer>> hashMapMethodMetric = hashMapsEvolutionMethodsGodMethodAnomalies.get(typeMethodEstaSemAnomalia);
                                                this.calculateMetricsofMethods(javaMethod, hashMapMethodMetric);

                                                hashMapMethodMetric = hashMapsEvolutionMethodsGodMethodAnomalies.get(typeMethodNuncaTeraAnomalia);
                                                this.calculateMetricsofMethods(javaMethod, hashMapMethodMetric);
                                            }
                                        }
                                    } else {
                                        if (metodoMudou || antMethod == null) {
                                            List<HashMap<String, Integer>> hashMapMethodMetric = hashMapsEvolutionMethodsGodMethodAnomalies.get(typeMethodEstaSemAnomalia);
                                            this.calculateMetricsofMethods(javaMethod, hashMapMethodMetric);

                                            hashMapMethodMetric = hashMapsEvolutionMethodsGodMethodAnomalies.get(typeMethodNuncaTeraAnomalia);
                                            this.calculateMetricsofMethods(javaMethod, hashMapMethodMetric);

                                        }
                                    }

                                } else {
                                    //o método nasceu agora
                                    numberofAddmethods++;
                                    System.out.println("Nasceu metodo: " + javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature());
                                    surgiuMetodoNovo = true;
                                    String alternativeMethodName = methodsAlternativeNameMap.get(javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature());
                                    GenericAnomalies auxGenericAnomalies = null;
                                    for (GenericAnomalies genericAnomalies : methodGenericAnomalies) {
                                        if (genericAnomalies.isGenericName(alternativeMethodName)) {
                                            auxGenericAnomalies = genericAnomalies;
                                            break;
                                        }
                                    }
                                    //esse método possui uma anomalia
                                    if (auxGenericAnomalies != null) {
                                        System.out.println("Metodo " + javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + " possui anomalia");

                                        //pego a anomalia god method
                                        AnomalieList anomalieList = auxGenericAnomalies.getAnomalieList("GOD METHOD");
                                        if (anomalieList != null) {
                                            //surgiuMetodoNovoComDefeito = true;
                                            teraAlgumaAnomaliaAlgumDia = true;
                                            //hashMapsGodMethodAnomalies
                                            int anomalieNumberBirth = anomalieList.getAnomalieBirthNumber();
                                            int methodNumberBirth = anomalieList.getRevisionBirthNumber();
                                            System.out.println("Possui GOD Method que nasceu em: " + anomalieNumberBirth + "        e esse metodo nasceu em " + methodNumberBirth + "     lembrando que estamos em: " + k);
                                            //é uma anomalia congenita
                                            if (anomalieNumberBirth + methodNumberBirth == k) {
                                                possuiAlgumMetodoComDefeito = true;
                                                //surgiu agora a anomalia
                                                int typeOfAnomalie = anomalieList.getTypeOfAnomalie();
//                                            if(typeOfAnomalie == Constants.ANOMALIE_TYPE_CONGENITAL_CORRECTED_BORN_AFTER_THE_CLASS || 
//                                                    typeOfAnomalie == Constants.ANOMALIE_TYPE_CONGENITAL_CORRECTED_BUT_CORRECTED_UM_TIME_BEFORE_BORN_AFTER_THE_CLASS ||
//                                                    typeOfAnomalie == Constants.ANOMALIE_TYPE_CONGENITAL_CORRECTED_RECURRENT_CORRECTED_BORN_AFTER_THE_CLASS &&
//                                                    typeOfAnomalie == Constants.ANOMALIE_TYPE_CONGENITAL_NEVER_CORRECTED_BORN_AFTER_THE_CLASS &&
//                                                    typeOfAnomalie == Constants.ANOMALIE_TYPE_CONGENITAL_NOT_CORRECTED_BUT_CORRECTED_ONE_TIME_BORN_AFTER_THE_CLASS &&
//                                                    typeOfAnomalie == Constants.ANOMALIE_TYPE_CONGENITAL_NOT_CORRECTED_RECURRENT_CORRECTED_BORN_AFTER_THE_CLASS){
//                                                
//                                            }
                                                if (anomalieList.isIsCongenital() && anomalieList.isAfterSuperiorArtefact()) {

                                                    System.out.println("Metodo congenito nascido apos artefato: " + javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature());

                                                    List<HashMap<String, Integer>> hashMapMetric = hashMapsGodMethodAnomalies.get(typeCongenitalAfterClass);

                                                    calculateMetrics(antClass, hashMapMetric);

                                                    hashMapMetric = hashMapsGodMethodAnomalies.get(typeCongenital);
                                                    calculateMetrics(antClass, hashMapMetric);

                                                    hashMapMetric = hashMapsGodMethodAnomalies.get(typeAnomalies);
                                                    calculateMetrics(antClass, hashMapMetric);

                                                } else if (anomalieList.isIsCongenital() && !anomalieList.isAfterSuperiorArtefact()) {
                                                    List<HashMap<String, Integer>> hashMapMetric = hashMapsGodMethodAnomalies.get(typeCongenitalWithClass);

                                                    calculateMetrics(javaClass, hashMapMetric);

                                                    hashMapMetric = hashMapsGodMethodAnomalies.get(typeCongenital);
                                                    calculateMetrics(javaClass, hashMapMetric);

                                                    hashMapMetric = hashMapsGodMethodAnomalies.get(typeAnomalies);
                                                    calculateMetrics(javaClass, hashMapMetric);

                                                }
                                                List<HashMap<String, Integer>> hashMapMethodMetric = hashMapsEvolutionMethodsGodMethodAnomalies.get(typeMethodEstaComAnomalia);
                                                this.calculateMetricsofMethods(javaMethod, hashMapMethodMetric);

                                                System.out.println("Nasceu com Anomalia em: " + k + "  de  " + alternativeName + "   que agora é ( " + javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + " )ciclomatic complexity: " + javaMethod.getCyclomaticComplexity());

                                            } else {
                                                List<HashMap<String, Integer>> hashMapMethodMetric = hashMapsEvolutionMethodsGodMethodAnomalies.get(typeMethodEstaSemAnomalia);
                                                this.calculateMetricsofMethods(javaMethod, hashMapMethodMetric);
                                            }
                                        } else {

                                            List<HashMap<String, Integer>> hashMapMethodMetric = hashMapsEvolutionMethodsGodMethodAnomalies.get(typeMethodEstaSemAnomalia);
                                            this.calculateMetricsofMethods(javaMethod, hashMapMethodMetric);

                                            hashMapMethodMetric = hashMapsEvolutionMethodsGodMethodAnomalies.get(typeMethodNuncaTeraAnomalia);
                                            this.calculateMetricsofMethods(javaMethod, hashMapMethodMetric);
                                        }
                                    } else {
                                        List<HashMap<String, Integer>> hashMapMethodMetric = hashMapsEvolutionMethodsGodMethodAnomalies.get(typeMethodEstaSemAnomalia);
                                        this.calculateMetricsofMethods(javaMethod, hashMapMethodMetric);

                                        hashMapMethodMetric = hashMapsEvolutionMethodsGodMethodAnomalies.get(typeMethodNuncaTeraAnomalia);
                                        this.calculateMetricsofMethods(javaMethod, hashMapMethodMetric);
                                    }

                                }
                                if (!possuiAlgumMetodo) {

                                    String text = "continue_healthly_method";

                                }
                            }
                            //verifica se a classe adicionou um metodo e mesmo assim continuou pura
                            if (surgiuMetodoNovo && !possuiAlgumMetodoComDefeito) {
                                List<HashMap<String, Integer>> hashMapMetric = hashMapsGodMethodAnomalies.get(typeSurgiuMetodoEContinuaSaudavel);

                                calculateMetrics(javaClass, hashMapMetric);

                            }

                            if (algumMetodoMudou && !possuiAlgumMetodoComDefeito) {
                                List<HashMap<String, Integer>> hashMapMetric = hashMapsGodMethodAnomalies.get(typeMudouMetodoContinuaSaudavel);

                                calculateMetrics(javaClass, hashMapMetric);
                            }

                            if (algumMetodoMudou && !teraAlgumaAnomaliaAlgumDia) {
                                List<HashMap<String, Integer>> hashMapMetric = hashMapsGodMethodAnomalies.get(typeJamaisAnomalia);

                                calculateMetrics(javaClass, hashMapMetric);
                            }

                            if (algumaAnomaliaVoltou) {
                                List<HashMap<String, Integer>> hashMapMetric = hashMapsGodMethodAnomalies.get(typeAnomaliaVolta);

                                calculateMetrics(javaClass, hashMapMetric);
                            }

                            if (alguemFoiCorrigidoMasVolta && !possuiAlgumMetodoComDefeito) {
                                List<HashMap<String, Integer>> hashMapMetric = hashMapsGodMethodAnomalies.get(typeCorrigidoMasVolta);

                                calculateMetrics(javaClass, hashMapMetric);
                            }

                            if (!possuiAlgumMetodoComDefeito && !alguemFoiCorrigidoMasVolta && alguemFoiCorrigidoDefinitivamente) {
                                List<HashMap<String, Integer>> hashMapMetric = hashMapsGodMethodAnomalies.get(typeCorrigidoMetodoPraSempre);

                                calculateMetrics(javaClass, hashMapMetric);
                            }

                            //vamos ver se a classe mudou
                            boolean classeMudouAtributos = false;
                            if (antClass.getAttributes().size() != javaClass.getAttributes().size()) {
                                classeMudouAtributos = true;
                            } else if (antClass.getImplementedInterfaces().size() != javaClass.getImplementedInterfaces().size()) {
                                classeMudouAtributos = true;
                            } else if (antClass.getMethods().size() != javaClass.getMethods().size()) {
                                classeMudouAtributos = true;
                            } else if (antClass.getTotalCyclomaticComplexity() != javaClass.getTotalCyclomaticComplexity()) {
                                classeMudouAtributos = true;
                            } else if (antClass.getAccessToForeignDataNumber() != javaClass.getAccessToForeignDataNumber()) {
                                classeMudouAtributos = true;
                            } else if (antClass.getInternalDependencyClasses().size() != javaClass.getInternalDependencyClasses().size()) {
                                classeMudouAtributos = true;
                            } else if (antClass.getNumberOfDirectConnections() != javaClass.getNumberOfDirectConnections()) {
                                classeMudouAtributos = true;
                            } else if (antClass.getintraPackageDependentClass().size() != javaClass.getintraPackageDependentClass().size()) {
                                classeMudouAtributos = true;
                            } else if (antClass.getExternalDependencyClasses().size() != javaClass.getExternalDependencyClasses().size()) {
                                classeMudouAtributos = true;
                            }

                            if (classeMudouAtributos) {
                                if (possuiAlgumMetodoComDefeito) {
                                    List<HashMap<String, Integer>> hashMapMetric = hashMapsGodMethodAnomalies.get(typeClasseComAlgumaAnomalia);

                                    calculateMetrics(javaClass, hashMapMetric);
                                } else {
                                    List<HashMap<String, Integer>> hashMapMetric = hashMapsGodMethodAnomalies.get(typeClasseSemAnomalia);

                                    calculateMetrics(javaClass, hashMapMetric);
                                }
                                if (!teraAlgumaAnomaliaAlgumDia) {
                                    List<HashMap<String, Integer>> hashMapMetric = hashMapsGodMethodAnomalies.get(typeClasseNuncaTeveAnomalia);

                                    calculateMetrics(javaClass, hashMapMetric);
                                }

                            }

                        } else if (auxAbstract == null) {
                            //a classe surgiu agora
                            boolean possuiAnomaliaQuandoSurgiu = false;
                            boolean possuiAlgumaAnomalia = false;
                            for (JavaMethod javaMethod : javaClass.getMethods()) {
                                String alternativeMethodName = methodsAlternativeNameMap.get(javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature());
                                GenericAnomalies auxGenericAnomalies = null;
                                for (GenericAnomalies genericAnomalies : methodGenericAnomalies) {
                                    if (genericAnomalies.isGenericName(alternativeMethodName)) {
                                        auxGenericAnomalies = genericAnomalies;
                                        break;
                                    }
                                }
                                //esse método possui uma anomalia
                                if (auxGenericAnomalies != null) {
                                    System.out.println("Metodo " + javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + " possui anomalia");

                                    //pego a anomalia god method
                                    AnomalieList anomalieList = auxGenericAnomalies.getAnomalieList("GOD METHOD");
                                    if (anomalieList != null) {
                                        possuiAlgumaAnomalia = true;

                                        //hashMapsGodMethodAnomalies
                                        int anomalieNumberBirth = anomalieList.getAnomalieBirthNumber();
                                        int methodNumberBirth = anomalieList.getRevisionBirthNumber();
                                        //é uma anomalia congenita
                                        if (anomalieNumberBirth + methodNumberBirth == k) {
                                            possuiAnomaliaQuandoSurgiu = true;
                                            if (anomalieList.isIsCongenital() && !anomalieList.isAfterSuperiorArtefact()) {
                                                List<HashMap<String, Integer>> hashMapMetric = hashMapsGodMethodAnomalies.get(typeCongenitalWithClass);

                                                calculateMetrics(javaClass, hashMapMetric);

                                                hashMapMetric = hashMapsGodMethodAnomalies.get(typeCongenital);
                                                calculateMetrics(javaClass, hashMapMetric);

                                                hashMapMetric = hashMapsGodMethodAnomalies.get(typeAnomalies);
                                                calculateMetrics(javaClass, hashMapMetric);

                                            }
                                        }
                                    }
                                }
                            }

                            if (!possuiAnomaliaQuandoSurgiu) {
                                List<HashMap<String, Integer>> hashMapMetric = hashMapsGodMethodAnomalies.get(typeSurgiuClasseEContinuaSaudavel);
                                calculateMetrics(javaClass, hashMapMetric);
                            }

                            if (!possuiAlgumaAnomalia) {
                                List<HashMap<String, Integer>> hashMapMetric = hashMapsGodMethodAnomalies.get(typeJamaisAnomalia);
                                calculateMetrics(javaClass, hashMapMetric);
                            }

                            if (possuiAnomaliaQuandoSurgiu) {
                                List<HashMap<String, Integer>> hashMapMetric = hashMapsGodMethodAnomalies.get(typeClasseComAlgumaAnomalia);

                                calculateMetrics(javaClass, hashMapMetric);
                            } else {
                                List<HashMap<String, Integer>> hashMapMetric = hashMapsGodMethodAnomalies.get(typeClasseSemAnomalia);

                                calculateMetrics(javaClass, hashMapMetric);
                            }
                            if (!possuiAlgumaAnomalia) {
                                List<HashMap<String, Integer>> hashMapMetric = hashMapsGodMethodAnomalies.get(typeClasseNuncaTeveAnomalia);

                                calculateMetrics(javaClass, hashMapMetric);
                            }

                        }
                    }

                }

                System.out.println("Calculou: " + k);

                ant = jp;
                hashAntAnomalies = hashAnomalies;
                k++;
                if (rev.getNext().size() == 0) {
                    rev = null;
                } else {
                    rev = rev.getNext().get(0);
                }

            }

            //writer0.close();
        } catch (Exception e) {
            System.out.println("Erro verificarHistogramasCongenitalAfterClass: " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        long t2 = System.currentTimeMillis();
        System.out.println("Tempo pra calcular anomalias: " + ((tm - t1) / 60000) + " minutos");
        System.out.println("Tempo pra calcular os histogramas: " + ((t2 - tm) / 60000) + " minutos");
        System.out.println("Tempo pra processar tudo: " + ((t2 - t1) / 60000) + " minutos");

        try {

            //ccongenito ou saudavel, ou qualqur um dos outros tipos
            for (int i = 0; i < 15; i++) {

                String dir = path + "classes_metrics/" + dirAnomalies.get(Constants.ANOMALIE_GOD_METHOD) + "/";

                List<HashMap<String, Integer>> hashMapMetric = hashMapsGodMethodAnomalies.get(i);
                dir = dir + typeOfAnomalieHashMap.get(i) + "/";
                File f = new File(dir);
                f.mkdirs();
                //lista de métricas
                for (int j = 0; j < 16; j++) {

                    String file = dir + metricsHashMap.get(j) + ".txt";
                    System.out.println("File: " + file);
                    HashMap<String, Integer> hashMapAux = hashMapMetric.get(j);

                    PrintWriter writer0 = new PrintWriter(file, "UTF-8");
                    //lista pra ajudar a ordenar
                    List<String> aux = new LinkedList();
                    Set<String> auxSet = hashMapAux.keySet();
                    Iterator it = auxSet.iterator();
                    while (it.hasNext()) {
                        String str = (String) it.next();
                        //Integer num = hashMapAux.get(str);
                        //writer0.println(str + "  " + num);
                        //adicionar na lista ordenada
                        boolean inseriu = false;
                        for (int index = 0; index < aux.size(); index++) {
                            if (Double.valueOf(aux.get(index)) > Double.valueOf(str)) {
                                inseriu = true;
                                aux.add(index, str);
                                break;
                            }
                        }
                        if (!inseriu) {
                            aux.add(str);

                        }
                    }
                    for (String str : aux) {
                        Integer num = hashMapAux.get(str);
                        writer0.println(str + "  " + num);
                    }
                    writer0.close();

                }

                //fazer o mesmo abaixo sobre as outras anomalias ou seja:
                //file = path + dirAnomalies.get(Constants.ANOMALIE_GOD_METHOD) + "/";
            }

            //verificar metodos
            for (int i = 0; i < 6; i++) {

                String dir = path + "methods_metrics/" + dirAnomalies.get(Constants.ANOMALIE_GOD_METHOD) + "/";

                List<HashMap<String, Integer>> hashMapMetric = hashMapsEvolutionMethodsGodMethodAnomalies.get(i);
                dir = dir + typeOfMethodAnomalieHashMap.get(i) + "/";
                File f = new File(dir);
                f.mkdirs();
                //lista de métricas
                for (int j = 0; j < 16; j++) {

                    String file = dir + metricsMethodHashMap.get(j) + ".txt";
                    System.out.println("File: " + file);
                    HashMap<String, Integer> hashMapAux = hashMapMetric.get(j);

                    PrintWriter writer0 = new PrintWriter(file, "UTF-8");
                    //lista pra ajudar a ordenar
                    List<String> aux = new LinkedList();
                    Set<String> auxSet = hashMapAux.keySet();
                    Iterator it = auxSet.iterator();
                    while (it.hasNext()) {
                        String str = (String) it.next();
                        //Integer num = hashMapAux.get(str);
                        //writer0.println(str + "  " + num);
                        //adicionar na lista ordenada
                        boolean inseriu = false;
                        for (int index = 0; index < aux.size(); index++) {
                            if (Double.valueOf(aux.get(index)) > Double.valueOf(str)) {
                                inseriu = true;
                                aux.add(index, str);
                                break;
                            }
                        }
                        if (!inseriu) {
                            aux.add(str);

                        }
                    }
                    for (String str : aux) {
                        Integer num = hashMapAux.get(str);
                        writer0.println(str + "  " + num);
                    }
                    writer0.close();

                }

                //fazer o mesmo abaixo sobre as outras anomalias ou seja:
                //file = path + dirAnomalies.get(Constants.ANOMALIE_GOD_METHOD) + "/";
            }

        } catch (Exception e) {
            System.out.println("Erro verificarHistogramOfValues: " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        System.out.println("Numero de métodos adicionados: " + numberofAddmethods);

    }

    public void verificarHistogramOfValues(ProjectRevisions newProjectRevisions, Project project, JavaConstructorService javaConstructorService) {
        long t1 = System.currentTimeMillis();
        Revision rev = newProjectRevisions.getRoot();
        int k = 0;
        while (rev != null) {
            k++;
            if (rev.getNext().size() == 0) {
                rev = null;
            } else {
                rev = rev.getNext().get(0);
            }
        }

        String path = System.getProperty("user.home") + "/.archd/histogramas/";

        //coisas de pacote
        HashMap<String, Integer> hashmapNumberOfClasse = new HashMap();
        HashMap<String, Integer> hashmapNumberOfInterfaces = new HashMap();
        HashMap<String, Integer> hashmapNumberOfClientClasses = new HashMap();
        HashMap<String, Integer> hashmapNumberOfClientPackages = new HashMap();
        HashMap<String, Integer> hashmapPackageCohesion = new HashMap();

        HashMap<String, Integer> hashmapNumberOfAttributes = new HashMap();
        HashMap<String, Integer> hashmapNumberOfMethods = new HashMap();
        HashMap<String, Integer> hashmapNumberOfImplementedInterfaces = new HashMap();
        HashMap<String, Integer> hashmapNumberOfTotalComplexity = new HashMap();
        HashMap<String, Integer> hashmapNumberOfComplexityAverage = new HashMap();
        HashMap<String, Integer> hashmapNumberOfAcessoDadoEstrangeiro = new HashMap();
        HashMap<String, Integer> hashmapNumeroDeClassesClientes = new HashMap();
        HashMap<String, Integer> hashmapNumeroDepacotesClientes = new HashMap();
        HashMap<String, Integer> hashmapNumberOfExternalDependecyClasses = new HashMap();
        HashMap<String, Integer> hashmapNumberOfExternalDependecyPackages = new HashMap();
        HashMap<String, Integer> hashmapNumeroDeDependenciaAClassesInternas = new HashMap();
        HashMap<String, Integer> hashmapTightClassCohesion = new HashMap();
        HashMap<String, Integer> hashmapNumeroDeClassesClientesInternas = new HashMap();
        HashMap<String, Integer> hashmapClassLocality = new HashMap();

        //metodos
        HashMap<String, Integer> hashmapNumberOfLines = new HashMap();
        HashMap<String, Integer> hashmapSizeInChars = new HashMap();
        HashMap<String, Integer> hashmapNumberOfLocalVariables = new HashMap();
        HashMap<String, Integer> hashmapNumberOfparameters = new HashMap();
        HashMap<String, Integer> hashmapComplexityCliclomatic = new HashMap();
        HashMap<String, Integer> hashmapNumberOfInvocacoesAMetodosExternos = new HashMap();
        HashMap<String, Integer> hashmapAcessoADadoEstrangeiro = new HashMap();
        HashMap<String, Integer> hashmapAcessoADadoLocal = new HashMap();
        HashMap<String, Integer> hashmapChangingClasses = new HashMap();
        HashMap<String, Integer> hashmapChangingMethods = new HashMap();
        HashMap<String, Integer> hashmapProvedorEstrangeiroDeDados = new HashMap();
        HashMap<String, Integer> hashmapInvocacoesMetodosInternos = new HashMap();

        try {
            rev = newProjectRevisions.getRoot();
            k = 0;

            JavaProject ant = null;
            while (rev != null) {
                JavaProject jp = null;
                //System.out.println("REV ID: "+rev.getId());
                System.gc();
                System.out.println("********************************* vai pegar um projeto completo");
                jp = javaConstructorService.getProjectByRevisionAndSetRevision(project.getName(), project.getCodeDirs(), project.getPath(), rev.getId(), newProjectRevisions.getName());

                for (JavaPackage javaPackage : jp.getPackages()) {
                    Integer num = null;

                    int numberofClasses = 0;
                    int numberofInterfaces = 0;
                    List<JavaAbstract> list = javaPackage.getClasses();
                    for (JavaAbstract javaAbstract : list) {
                        if (javaAbstract.getClass().equals(JavaClass.class)) {
                            numberofClasses++;
                        } else {
                            numberofInterfaces++;
                        }
                    }

                    num = hashmapNumberOfClasse.get(String.valueOf(numberofClasses));
                    if (num == null) {
                        hashmapNumberOfClasse.put(String.valueOf(numberofClasses), 1);
                    } else {
                        num++;
                        hashmapNumberOfClasse.put(String.valueOf(numberofClasses), num);
                    }

                    num = hashmapNumberOfInterfaces.get(String.valueOf(numberofInterfaces));
                    if (num == null) {
                        hashmapNumberOfInterfaces.put(String.valueOf(numberofInterfaces), 1);
                    } else {
                        num++;
                        hashmapNumberOfInterfaces.put(String.valueOf(numberofInterfaces), num);
                    }

                    int numberOfClientClasses = javaPackage.getClientClasses().size();
                    num = hashmapNumberOfClientClasses.get(String.valueOf(numberOfClientClasses));
                    if (num == null) {
                        hashmapNumberOfClientClasses.put(String.valueOf(numberOfClientClasses), 1);
                    } else {
                        num++;
                        hashmapNumberOfClientClasses.put(String.valueOf(numberOfClientClasses), num);
                    }

                    int numberOfClientPackages = javaPackage.getClientPackages().size();
                    num = hashmapNumberOfClientPackages.get(String.valueOf(numberOfClientPackages));
                    if (num == null) {
                        hashmapNumberOfClientPackages.put(String.valueOf(numberOfClientPackages), 1);
                    } else {
                        num++;
                        hashmapNumberOfClientPackages.put(String.valueOf(numberOfClientPackages), num);
                    }

                    double packageCohesion = javaPackage.getPackageCohesion();

                    num = hashmapPackageCohesion.get(String.valueOf(packageCohesion));
                    if (num == null) {
                        hashmapPackageCohesion.put(String.valueOf(packageCohesion), 1);
                    } else {
                        num++;
                        hashmapPackageCohesion.put(String.valueOf(packageCohesion), num);
                    }
                }

                for (JavaAbstract javaAbstract : jp.getClasses()) {
                    JavaClass javaClass = (JavaClass) javaAbstract;
                    //coisas de classes
                    Integer num = null;

                    int numberofAttributes = javaClass.getAttributes().size();
                    num = hashmapNumberOfAttributes.get(String.valueOf(numberofAttributes));
                    if (num == null) {
                        hashmapNumberOfAttributes.put(String.valueOf(numberofAttributes), 1);
                    } else {
                        num++;
                        hashmapNumberOfAttributes.put(String.valueOf(numberofAttributes), num);
                    }

                    int numberofImplementedInterfaces = javaClass.getImplementedInterfaces().size();
                    num = hashmapNumberOfImplementedInterfaces.get(String.valueOf(numberofImplementedInterfaces));
                    if (num == null) {
                        hashmapNumberOfImplementedInterfaces.put(String.valueOf(numberofImplementedInterfaces), 1);
                    } else {
                        num++;
                        hashmapNumberOfImplementedInterfaces.put(String.valueOf(numberofImplementedInterfaces), num);
                    }

                    int numberofMethods = javaClass.getMethods().size();
                    num = hashmapNumberOfMethods.get(String.valueOf(numberofMethods));
                    if (num == null) {
                        hashmapNumberOfMethods.put(String.valueOf(numberofMethods), 1);
                    } else {
                        num++;
                        hashmapNumberOfMethods.put(String.valueOf(numberofMethods), num);
                    }

                    int numberofTotalCiclomaticComplxity = javaClass.getTotalCyclomaticComplexity();
                    num = hashmapNumberOfTotalComplexity.get(String.valueOf(numberofTotalCiclomaticComplxity));
                    if (num == null) {
                        hashmapNumberOfTotalComplexity.put(String.valueOf(numberofTotalCiclomaticComplxity), 1);
                    } else {
                        num++;
                        hashmapNumberOfTotalComplexity.put(String.valueOf(numberofTotalCiclomaticComplxity), num);
                    }

                    double complexityAverage = 0;
                    if (numberofMethods != 0) {
                        complexityAverage = numberofTotalCiclomaticComplxity;
                        complexityAverage = complexityAverage / numberofMethods;
                    }
                    num = hashmapNumberOfComplexityAverage.get(String.valueOf(complexityAverage));
                    if (num == null) {
                        hashmapNumberOfComplexityAverage.put(String.valueOf(complexityAverage), 1);
                    } else {
                        num++;
                        hashmapNumberOfComplexityAverage.put(String.valueOf(complexityAverage), num);
                    }

                    int acessoDadoEstrangeiro = javaClass.getAccessToForeignDataNumber();
                    num = hashmapNumberOfAcessoDadoEstrangeiro.get(String.valueOf(acessoDadoEstrangeiro));
                    if (num == null) {
                        hashmapNumberOfAcessoDadoEstrangeiro.put(String.valueOf(acessoDadoEstrangeiro), 1);
                    } else {
                        num++;
                        hashmapNumberOfAcessoDadoEstrangeiro.put(String.valueOf(acessoDadoEstrangeiro), num);
                    }

                    int numberOfClientClasses = javaClass.getClientClasses().size();
                    num = hashmapNumeroDeClassesClientes.get(String.valueOf(numberOfClientClasses));
                    if (num == null) {
                        hashmapNumeroDeClassesClientes.put(String.valueOf(numberOfClientClasses), 1);
                    } else {
                        num++;
                        hashmapNumeroDeClassesClientes.put(String.valueOf(numberOfClientClasses), num);
                    }

                    int numberOfClientPackages = javaClass.getClientPackages().size();
                    num = hashmapNumeroDepacotesClientes.get(String.valueOf(numberOfClientPackages));
                    if (num == null) {
                        hashmapNumeroDepacotesClientes.put(String.valueOf(numberOfClientPackages), 1);
                    } else {
                        num++;
                        hashmapNumeroDepacotesClientes.put(String.valueOf(numberOfClientPackages), num);
                    }

                    int numberOfExternalDependecyClasses = javaClass.getExternalDependencyClasses().size();
                    num = hashmapNumberOfExternalDependecyClasses.get(String.valueOf(numberOfExternalDependecyClasses));
                    if (num == null) {
                        hashmapNumberOfExternalDependecyClasses.put(String.valueOf(numberOfExternalDependecyClasses), 1);
                    } else {
                        num++;
                        hashmapNumberOfExternalDependecyClasses.put(String.valueOf(numberOfExternalDependecyClasses), num);
                    }

                    int numberOfExternalDependecyPackages = javaClass.getExternalDependencyPackages().size();
                    num = hashmapNumberOfExternalDependecyPackages.get(String.valueOf(numberOfExternalDependecyPackages));
                    if (num == null) {
                        hashmapNumberOfExternalDependecyPackages.put(String.valueOf(numberOfExternalDependecyPackages), 1);
                    } else {
                        num++;
                        hashmapNumberOfExternalDependecyPackages.put(String.valueOf(numberOfExternalDependecyPackages), num);
                    }

                    int numeroDeDependenciaAClassesInternas = javaClass.getInternalDependencyClasses().size();
                    num = hashmapNumeroDeDependenciaAClassesInternas.get(String.valueOf(numeroDeDependenciaAClassesInternas));
                    if (num == null) {
                        hashmapNumeroDeDependenciaAClassesInternas.put(String.valueOf(numeroDeDependenciaAClassesInternas), 1);
                    } else {
                        num++;
                        hashmapNumeroDeDependenciaAClassesInternas.put(String.valueOf(numeroDeDependenciaAClassesInternas), num);
                    }

                    double tcc = 1;
                    if (javaClass.getMethods().size() >= 2) {
                        tcc = javaClass.getNumberOfDirectConnections();
                        int n = javaClass.getMethods().size();
                        tcc = tcc / ((n * (n - 1)) / 2);
                    }
                    num = hashmapTightClassCohesion.get(String.valueOf(tcc));
                    if (num == null) {
                        hashmapTightClassCohesion.put(String.valueOf(tcc), 1);
                    } else {
                        num++;
                        hashmapTightClassCohesion.put(String.valueOf(tcc), num);
                    }

                    int numeroDeClassesClientesInternas = javaClass.getintraPackageDependentClass().size();
                    num = hashmapNumeroDeClassesClientesInternas.get(String.valueOf(numeroDeClassesClientesInternas));
                    if (num == null) {
                        hashmapNumeroDeClassesClientesInternas.put(String.valueOf(numeroDeClassesClientesInternas), 1);
                    } else {
                        num++;
                        hashmapNumeroDeClassesClientesInternas.put(String.valueOf(numeroDeClassesClientesInternas), num);
                    }

                    double classLocality = -1;
                    if (javaClass.getInternalDependencyClasses().size() + javaClass.getExternalDependencyClasses().size() > 0) {
                        classLocality = javaClass.getInternalDependencyClasses().size();
                        classLocality = classLocality / (javaClass.getInternalDependencyClasses().size() + javaClass.getExternalDependencyClasses().size());
                    }
                    num = hashmapClassLocality.get(String.valueOf(classLocality));
                    if (num == null) {
                        hashmapClassLocality.put(String.valueOf(classLocality), 1);
                    } else {
                        num++;
                        hashmapClassLocality.put(String.valueOf(classLocality), num);
                    }

                    for (JavaMethod javaMethod : javaClass.getMethods()) {
                        //coisas de metodos
                        num = null;

                        int numberofLines = javaMethod.getNumberOfLines();
                        num = hashmapNumberOfLines.get(String.valueOf(numberofLines));
                        if (num == null) {
                            hashmapNumberOfLines.put(String.valueOf(numberofLines), 1);
                        } else {
                            num++;
                            hashmapNumberOfLines.put(String.valueOf(numberofLines), num);
                        }

                        int sizeInChars = javaMethod.getSizeInChars();
                        num = hashmapSizeInChars.get(String.valueOf(sizeInChars));
                        if (num == null) {
                            hashmapSizeInChars.put(String.valueOf(sizeInChars), 1);
                        } else {
                            num++;
                            hashmapSizeInChars.put(String.valueOf(sizeInChars), num);
                        }

                        int numberOfLocalVariables = javaMethod.getNumberOfLocalVariables();
                        num = hashmapNumberOfLocalVariables.get(String.valueOf(numberOfLocalVariables));
                        if (num == null) {
                            hashmapNumberOfLocalVariables.put(String.valueOf(numberOfLocalVariables), 1);
                        } else {
                            num++;
                            hashmapNumberOfLocalVariables.put(String.valueOf(numberOfLocalVariables), num);
                        }

                        int numberOfParameters = javaMethod.getParameters().size();
                        num = hashmapNumberOfparameters.get(String.valueOf(numberOfParameters));
                        if (num == null) {
                            hashmapNumberOfparameters.put(String.valueOf(numberOfParameters), 1);
                        } else {
                            num++;
                            hashmapNumberOfparameters.put(String.valueOf(numberOfParameters), num);
                        }

                        int ciclomaticComplexity = javaMethod.getCyclomaticComplexity();
                        num = hashmapComplexityCliclomatic.get(String.valueOf(ciclomaticComplexity));
                        if (num == null) {
                            hashmapComplexityCliclomatic.put(String.valueOf(ciclomaticComplexity), 1);
                        } else {
                            num++;
                            hashmapComplexityCliclomatic.put(String.valueOf(ciclomaticComplexity), num);
                        }

                        int numberOfInvocacoesAMetodosExternos = javaMethod.getMethodInvocations().size();
                        num = hashmapNumberOfInvocacoesAMetodosExternos.get(String.valueOf(numberOfInvocacoesAMetodosExternos));
                        if (num == null) {
                            hashmapNumberOfInvocacoesAMetodosExternos.put(String.valueOf(numberOfInvocacoesAMetodosExternos), 1);
                        } else {
                            num++;
                            hashmapNumberOfInvocacoesAMetodosExternos.put(String.valueOf(numberOfInvocacoesAMetodosExternos), num);
                        }

                        int acessoDadoExtrangeiro = javaMethod.getAccessToForeignDataNumber();
                        num = hashmapAcessoADadoEstrangeiro.get(String.valueOf(acessoDadoExtrangeiro));
                        if (num == null) {
                            hashmapAcessoADadoEstrangeiro.put(String.valueOf(acessoDadoExtrangeiro), 1);
                        } else {
                            num++;
                            hashmapAcessoADadoEstrangeiro.put(String.valueOf(acessoDadoExtrangeiro), num);
                        }

                        int acessoDadoLocal = javaMethod.getAccessToLocalDataNumber();
                        num = hashmapAcessoADadoLocal.get(String.valueOf(acessoDadoLocal));
                        if (num == null) {
                            hashmapAcessoADadoLocal.put(String.valueOf(acessoDadoLocal), 1);
                        } else {
                            num++;
                            hashmapAcessoADadoLocal.put(String.valueOf(acessoDadoLocal), num);
                        }

                        int changingClasses = javaMethod.getChangingClassesMetric();
                        num = hashmapChangingClasses.get(String.valueOf(changingClasses));
                        if (num == null) {
                            hashmapChangingClasses.put(String.valueOf(changingClasses), 1);
                        } else {
                            num++;
                            hashmapChangingClasses.put(String.valueOf(changingClasses), num);
                        }

                        int changingMethods = javaMethod.getChangingMethodsMetric();
                        num = hashmapChangingMethods.get(String.valueOf(changingMethods));
                        if (num == null) {
                            hashmapChangingMethods.put(String.valueOf(changingMethods), 1);
                        } else {
                            num++;
                            hashmapChangingMethods.put(String.valueOf(changingMethods), num);
                        }

                        int foreignDataProviderNumber = javaMethod.getForeignDataProviderNumber();
                        num = hashmapProvedorEstrangeiroDeDados.get(String.valueOf(foreignDataProviderNumber));
                        if (num == null) {
                            hashmapProvedorEstrangeiroDeDados.put(String.valueOf(foreignDataProviderNumber), 1);
                        } else {
                            num++;
                            hashmapProvedorEstrangeiroDeDados.put(String.valueOf(foreignDataProviderNumber), num);
                        }

                        int invocacoesMetodosInternos = javaMethod.getInternalMethodInvocations().size();
                        num = hashmapInvocacoesMetodosInternos.get(String.valueOf(invocacoesMetodosInternos));
                        if (num == null) {
                            hashmapInvocacoesMetodosInternos.put(String.valueOf(invocacoesMetodosInternos), 1);
                        } else {
                            num++;
                            hashmapInvocacoesMetodosInternos.put(String.valueOf(invocacoesMetodosInternos), num);
                        }

                    }
                }

                System.out.println("Calculou: " + k);

                ant = jp;
                //hashAntAnomalies = hashAnomalies;
                k++;
                if (rev.getNext().size() == 0) {
                    rev = null;
                } else {
                    rev = rev.getNext().get(0);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro verificarHistogramOfValues: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        try {

            PrintWriter writer0 = new PrintWriter(path + "pacote_NumberOfClasse.txt", "UTF-8");
            Set<String> auxSet = hashmapNumberOfClasse.keySet();
            Iterator it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumberOfClasse.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "pacote_NumberOfInterfaces.txt", "UTF-8");
            auxSet = hashmapNumberOfInterfaces.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumberOfInterfaces.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "pacote_NumberOfClientClasses.txt", "UTF-8");
            auxSet = hashmapNumberOfClientClasses.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumberOfClientClasses.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "pacote_NumberOfInterfaces.txt", "UTF-8");
            auxSet = hashmapNumberOfInterfaces.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumberOfInterfaces.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "pacote_PackageCohesion.txt", "UTF-8");
            auxSet = hashmapPackageCohesion.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapPackageCohesion.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "classe_NumberOfAttributes.txt", "UTF-8");
            auxSet = hashmapNumberOfAttributes.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumberOfAttributes.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "classe_NumberOfMethods.txt", "UTF-8");
            auxSet = hashmapNumberOfMethods.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumberOfMethods.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "classe_NumberOfImplementedInterfaces.txt", "UTF-8");
            auxSet = hashmapNumberOfImplementedInterfaces.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumberOfImplementedInterfaces.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "classe_NumberOfTotalComplexity.txt", "UTF-8");
            auxSet = hashmapNumberOfTotalComplexity.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumberOfTotalComplexity.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "classe_NumberOfComplexityAverage.txt", "UTF-8");
            auxSet = hashmapNumberOfComplexityAverage.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumberOfComplexityAverage.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "classe_NumberOfAcessoDadoEstrangeiro.txt", "UTF-8");
            auxSet = hashmapNumberOfAcessoDadoEstrangeiro.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumberOfAcessoDadoEstrangeiro.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "classe_NumeroDeClassesClientes.txt", "UTF-8");
            auxSet = hashmapNumeroDeClassesClientes.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumeroDeClassesClientes.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "classe_NumeroDepacotesClientes.txt", "UTF-8");
            auxSet = hashmapNumeroDepacotesClientes.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumeroDepacotesClientes.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "classe_NumberOfExternalDependecyClasses.txt", "UTF-8");
            auxSet = hashmapNumberOfExternalDependecyClasses.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumberOfExternalDependecyClasses.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "classe_NumberOfExternalDependecyPackages.txt", "UTF-8");
            auxSet = hashmapNumberOfExternalDependecyPackages.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumberOfExternalDependecyPackages.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "classe_NumeroDeDependenciaAClassesInternas.txt", "UTF-8");
            auxSet = hashmapNumeroDeDependenciaAClassesInternas.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumeroDeDependenciaAClassesInternas.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "classe_TightClassCohesion.txt", "UTF-8");
            auxSet = hashmapTightClassCohesion.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapTightClassCohesion.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "classe_NumeroDeClassesClientesInternas.txt", "UTF-8");
            auxSet = hashmapNumeroDeClassesClientesInternas.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumeroDeClassesClientesInternas.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "classe_ClassLocality.txt", "UTF-8");
            auxSet = hashmapClassLocality.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapClassLocality.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "metodo_NumberOfLines.txt", "UTF-8");
            auxSet = hashmapNumberOfLines.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumberOfLines.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "metodo_SizeInChars.txt", "UTF-8");
            auxSet = hashmapSizeInChars.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapSizeInChars.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "metodo_NumberOfLocalVariables.txt", "UTF-8");
            auxSet = hashmapNumberOfLocalVariables.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumberOfLocalVariables.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "metodo_NumberOfparameters.txt", "UTF-8");
            auxSet = hashmapNumberOfparameters.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumberOfparameters.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "metodo_ComplexityCliclomatic.txt", "UTF-8");
            auxSet = hashmapComplexityCliclomatic.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapComplexityCliclomatic.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "metodo_NumberOfInvocacoesAMetodosExternos.txt", "UTF-8");
            auxSet = hashmapNumberOfInvocacoesAMetodosExternos.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapNumberOfInvocacoesAMetodosExternos.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "metodo_AcessoADadoEstrangeiroc.txt", "UTF-8");
            auxSet = hashmapAcessoADadoEstrangeiro.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapAcessoADadoEstrangeiro.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "metodo_AcessoADadoLocalc.txt", "UTF-8");
            auxSet = hashmapAcessoADadoLocal.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapAcessoADadoLocal.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "metodo_ChangingClasses.txt", "UTF-8");
            auxSet = hashmapChangingClasses.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapChangingClasses.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "metodo_ChangingMethods.txt", "UTF-8");
            auxSet = hashmapChangingMethods.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapChangingMethods.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "metodo_ProvedorEstrangeiroDeDadosc.txt", "UTF-8");
            auxSet = hashmapProvedorEstrangeiroDeDados.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapProvedorEstrangeiroDeDados.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

            writer0 = new PrintWriter(path + "metodo_InvocacoesMetodosInternos.txt", "UTF-8");
            auxSet = hashmapInvocacoesMetodosInternos.keySet();
            it = auxSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Integer num = hashmapInvocacoesMetodosInternos.get(str);
                writer0.println(str + "  " + num);
            }
            writer0.close();

        } catch (Exception e) {
            System.out.println("Erro verificarHistogramOfValues: " + e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    public static void main(String args[]) {
        String path = System.getProperty("user.home") + "/.archd/";
        JavaProjectsService javaprojectsService = new JavaProjectsService();
        List<Project> projects = javaprojectsService.getProjects();
        Project p = null;
        for (Project project : projects) {
            if (project.getName().equals("MapDB")) {
                p = project;
                break;
            }
        }
        if (p != null) {
            try {
                JavaConstructorService javaContructorService = new JavaConstructorService();
                ProjectRevisionsService projectRevisionsService = new ProjectRevisionsService();
                ProjectRevisions projectRevisions = projectRevisionsService.getProject(p.getPath(), p.getName());
                System.out.println("ORIGINAL ROOT: " + projectRevisions.getRoot().getId());
                System.out.println("ORIGINAL HEAD: " + projectRevisions.getBranchesRevisions().get(0).getHead().getId());
                System.out.println("Vai limpar");
                ProjectRevisions newProjectRevisions = cleanProjectRevisionsLine(projectRevisions);
                System.out.println("Limpou");
                Verificar verificar = new Verificar();
                //verificar.getChanges(newProjectRevisions, p, javaContructorService);
                //verificar.verificarClientClasses(newProjectRevisions, p, javaContructorService);
                //verificar.verificarClientClassesViaInterface(newProjectRevisions, p, javaContructorService);
                //verificar.getAddMethods(newProjectRevisions, p, javaContructorService);
                //verificar.getAddMethodsToAnalizer(newProjectRevisions, p, javaContructorService);
                //verificar.createAprioriFile();
                //verificar.createFinalFile();
                //verificar.getAddMethodsAdquiredToAnalizer(newProjectRevisions, p, javaContructorService);
                //verificar.getAddMethodsEmergeAndCorrectToAnalizer(newProjectRevisions, p, javaContructorService);
                //verificar.verificarHistogramOfValues(newProjectRevisions, p, javaContructorService);
                //verificar.verificarCongenitalAfterClass(newProjectRevisions, p, javaContructorService);
                verificar.verificarAnomaliasDeMetodos(newProjectRevisions, p, javaContructorService);
                //verificar.getAddMethodsCorrectToAnalizer(newProjectRevisions, p, javaContructorService);
            } catch (Exception e) {
            }
        }
    }

    private static ProjectRevisions cleanProjectRevisionsLine(ProjectRevisions projectRevisions) {
        List<BranchRevisions> branches = new LinkedList();
        ProjectRevisions newProjectRevisions = new ProjectRevisions(projectRevisions.getName());
        RevisionsBucket revisionsBucket = new RevisionsBucket();
        //Revision newRoot = new Revision(projectRevisions.getRoot().getId());
        int count = 0;
        //newProjectRevisions.setRoot(newRoot);
        for (BranchRevisions branchRevisions : projectRevisions.getBranchesRevisions()) {
            Revision newHead = revisionsBucket.getRevisionById(branchRevisions.getHead().getId());
            if (newHead == null) {
                newHead = new Revision(branchRevisions.getHead().getId());
                revisionsBucket.addRevision(newHead);
            }
            BranchRevisions newBranchRevisions = new BranchRevisions(branchRevisions.getName(), newHead);
            LineRevisions lineRevisions = branchRevisions.getLinesRevisions().get(0);
            LineRevisions newLineRevisions = new LineRevisions(newHead);
            Revision aux = lineRevisions.getHead();
            Revision newRevision = revisionsBucket.getRevisionById(aux.getId());
            if (newRevision == null) {
                newRevision = new Revision(aux.getId());
                revisionsBucket.addRevision(newRevision);
            }
            Revision prox = newRevision;
            newLineRevisions.addRevision(newRevision);
            revisionsBucket.addRevision(newRevision);
            int i = 0;
            while (aux != null) {
                i++;
                //System.out.println("I: "+i);
                aux = aux.getPrev().get(aux.getPrev().size() - 1);
                newRevision = revisionsBucket.getRevisionById(aux.getId());
                if (newRevision == null) {
                    newRevision = new Revision(aux.getId());
                    revisionsBucket.addRevision(newRevision);
                }
                newRevision.addNext(prox);
                prox.addPrev(newRevision);
                prox = newRevision;
                newLineRevisions.addRevision(newRevision);
                revisionsBucket.addRevision(newRevision);
                count++;
                if (aux.getPrev().size() == 0) {
                    aux = null;
                }
            }
            newProjectRevisions.setRoot(prox);
            newBranchRevisions.addLineRevisions(lineRevisions);
            branches.add(newBranchRevisions);

        }
        System.out.println("Count: " + count);
        newProjectRevisions.setBranchesRevisions(branches);
        newProjectRevisions.setRevisionsBucket(revisionsBucket);
        return newProjectRevisions;
    }

    public boolean isSameJavaClass(JavaClass currentClass, JavaClass antClass) {
        boolean isSame = false;
        int numberOfMethods = 0;
        int numberOfAttributes = 0;
        for (JavaMethod javaMethod : currentClass.getMethods()) {
            boolean encontrou = false;
            for (JavaMethod antMethod : antClass.getMethods()) {
                if (javaMethod.getMethodSignature().equals(antMethod.getMethodSignature())) {
                    int numberOfCurrentChars = javaMethod.getSizeInChars();
                    int numberOfAntChars = antMethod.getSizeInChars();
                    int numberOfCurrentNumberOfLocalVariables = javaMethod.getNumberOfLocalVariables();
                    int numberOfAntNumberOfLocalVariables = antMethod.getNumberOfLocalVariables();
                    int numberOfCurrentCyclomatic = javaMethod.getCyclomaticComplexity();
                    int numberOfAntCyclomatic = antMethod.getCyclomaticComplexity();

                    if ((numberOfCurrentChars <= (numberOfAntChars + (numberOfAntChars * 0.2)) && numberOfCurrentChars >= (numberOfAntChars - (numberOfAntChars * 0.2)))
                            && (numberOfCurrentNumberOfLocalVariables <= (numberOfAntNumberOfLocalVariables + (numberOfAntNumberOfLocalVariables * 0.2)) && numberOfCurrentNumberOfLocalVariables >= (numberOfAntNumberOfLocalVariables - (numberOfAntNumberOfLocalVariables * 0.2)))
                            && (numberOfCurrentCyclomatic <= (numberOfAntCyclomatic + (numberOfAntCyclomatic * 0.2)) && numberOfCurrentCyclomatic >= (numberOfAntCyclomatic - (numberOfAntCyclomatic * 0.2)))) {
                        numberOfMethods++;
                    }
                    encontrou = true;
                    break;

                }

            }
            if (!encontrou) {
                List<JavaMethod> methodsName = new LinkedList();
                List<JavaMethod> methodsNameAnt = antClass.getMethodsByName(javaMethod.getName());
                List<JavaMethod> methodsNameCurrent = currentClass.getMethodsByName(javaMethod.getName());
                for (JavaMethod jm : methodsNameCurrent) {
                    boolean exists = false;
                    for (JavaMethod auxJm : methodsNameAnt) {
                        if (auxJm.getMethodSignature().equals(jm.getMethodSignature())) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        methodsName.add(jm);
                    }
                }

                if (!methodsName.isEmpty()) {
                    JavaMethod auxJm = closestMethod(javaMethod, methodsName);
                    int numberOfCurrentChars = javaMethod.getSizeInChars();
                    int numberOfAntChars = auxJm.getSizeInChars();
                    int numberOfCurrentNumberOfLocalVariables = javaMethod.getNumberOfLocalVariables();
                    int numberOfAntNumberOfLocalVariables = auxJm.getNumberOfLocalVariables();
                    int numberOfCurrentCyclomatic = javaMethod.getCyclomaticComplexity();
                    int numberOfAntCyclomatic = auxJm.getCyclomaticComplexity();

                    if ((numberOfCurrentChars <= (numberOfAntChars + (numberOfAntChars * 0.2)) && numberOfCurrentChars >= (numberOfAntChars - (numberOfAntChars * 0.2)))
                            && (numberOfCurrentNumberOfLocalVariables <= (numberOfAntNumberOfLocalVariables + (numberOfAntNumberOfLocalVariables * 0.2)) && numberOfCurrentNumberOfLocalVariables >= (numberOfAntNumberOfLocalVariables - (numberOfAntNumberOfLocalVariables * 0.2)))
                            && (numberOfCurrentCyclomatic <= (numberOfAntCyclomatic + (numberOfAntCyclomatic * 0.2)) && numberOfCurrentCyclomatic >= (numberOfAntCyclomatic - (numberOfAntCyclomatic * 0.2)))) {
                        numberOfMethods++;
                    }

                }
            }
        }

        for (JavaAttribute javaAttribute : currentClass.getAttributes()) {
            for (JavaAttribute antAttribute : antClass.getAttributes()) {
                if (javaAttribute.getName().equals(antAttribute.getName()) && javaAttribute.getType().getName().equals(antAttribute.getType().getName())) {
                    numberOfAttributes++;
                }
            }
        }

        if ((currentClass.getMethods().size() <= (numberOfMethods + (numberOfMethods * 0.2)) && currentClass.getMethods().size() >= (numberOfMethods - (numberOfMethods * 0.2)))
                && (currentClass.getAttributes().size() <= (numberOfAttributes + (numberOfAttributes * 0.2)) && currentClass.getAttributes().size() >= (numberOfAttributes - (numberOfAttributes * 0.2)))) {
            isSame = true;
        }
        //System.out.println("Number of methods: " + currentClass.getMethods().size() + "     -     " + numberOfMethods);
        //System.out.println("Number of attributes: " + currentClass.getAttributes().size() + "     -     " + numberOfAttributes);

        return isSame;
    }

    public JavaMethod closestMethod(JavaMethod currentMethod, List<JavaMethod> newMethods, List<JavaMethod> methods) {
        boolean isSame = false;

        JavaMethod closestJavaMethod = null;

        int closestMethod = 0;

        List<Double> diffs = new LinkedList();

        for (JavaMethod javaMethod : methods) {
            int numberOfParameters = currentMethod.getParameters().size() - javaMethod.getParameters().size();
            int numberOfMethodInvocations = currentMethod.getMethodInvocations().size() - javaMethod.getMethodInvocations().size();
            int numberOfCharsDiff = currentMethod.getSizeInChars() - javaMethod.getSizeInChars();
            int numberOfLocalVariablesDiff = currentMethod.getNumberOfLocalVariables() - javaMethod.getNumberOfLocalVariables();
            int cyclomaticComplexityDiff = currentMethod.getCyclomaticComplexity() - javaMethod.getCyclomaticComplexity();
            double diff = Math.sqrt(Math.pow(numberOfParameters, 2) + Math.pow(numberOfMethodInvocations, 2) + Math.pow(numberOfCharsDiff, 2) + Math.pow(numberOfLocalVariablesDiff, 2) + Math.pow(cyclomaticComplexityDiff, 2));
            diffs.add(diff);
        }
        double minDiff = diffs.get(0);
        for (int i = 1; i < diffs.size(); i++) {
            Double diff = diffs.get(i);
            if (diff < minDiff) {
                closestMethod = i;
                minDiff = diff;
            }
        }

        //agora vemos se é possível algum outro método é mais proximo de mim do que ele mesmo
        double menorDiffDosOutrosMetodos = Double.MAX_VALUE;
        for (JavaMethod newMethod : newMethods) {
            if (newMethod != currentMethod) {
                List<Double> diffsNews = new LinkedList();

                for (JavaMethod javaMethod : methods) {
                    int numberOfParameters = newMethod.getParameters().size() - javaMethod.getParameters().size();
                    int numberOfMethodInvocations = newMethod.getMethodInvocations().size() - javaMethod.getMethodInvocations().size();
                    int numberOfCharsDiff = newMethod.getSizeInChars() - javaMethod.getSizeInChars();
                    int numberOfLocalVariablesDiff = newMethod.getNumberOfLocalVariables() - javaMethod.getNumberOfLocalVariables();
                    int cyclomaticComplexityDiff = newMethod.getCyclomaticComplexity() - javaMethod.getCyclomaticComplexity();
                    double diff = Math.sqrt(Math.pow(numberOfParameters, 2) + Math.pow(numberOfMethodInvocations, 2) + Math.pow(numberOfCharsDiff, 2) + Math.pow(numberOfLocalVariablesDiff, 2) + Math.pow(cyclomaticComplexityDiff, 2));
                    diffsNews.add(diff);
                }
                double minDiffNew = diffsNews.get(0);
                for (int i = 1; i < diffsNews.size(); i++) {
                    Double diff = diffsNews.get(i);
                    if (diff < minDiffNew) {
                        minDiffNew = diff;
                    }
                }
                if (minDiffNew < menorDiffDosOutrosMetodos) {
                    menorDiffDosOutrosMetodos = minDiffNew;
                }
            }
        }
        //vejo aqui se sou o mais proximo mesmo
        if (minDiff < menorDiffDosOutrosMetodos) {
            closestJavaMethod = methods.get(closestMethod);
        }

        return closestJavaMethod;
    }

    public JavaMethod closestMethod(JavaMethod currentMethod, List<JavaMethod> methods) {
        boolean isSame = false;

        int closestMethod = 0;

        List<Double> diffs = new LinkedList();

        for (JavaMethod javaMethod : methods) {
            int numberOfParameters = currentMethod.getParameters().size() - javaMethod.getParameters().size();
            int numberOfMethodInvocations = currentMethod.getMethodInvocations().size() - javaMethod.getMethodInvocations().size();
            int numberOfCharsDiff = currentMethod.getSizeInChars() - javaMethod.getSizeInChars();
            int numberOfLocalVariablesDiff = currentMethod.getNumberOfLocalVariables() - javaMethod.getNumberOfLocalVariables();
            int cyclomaticComplexityDiff = currentMethod.getCyclomaticComplexity() - javaMethod.getCyclomaticComplexity();
            double diff = Math.sqrt(Math.pow(numberOfParameters, 2) + Math.pow(numberOfMethodInvocations, 2) + Math.pow(numberOfCharsDiff, 2) + Math.pow(numberOfLocalVariablesDiff, 2) + Math.pow(cyclomaticComplexityDiff, 2));
            diffs.add(diff);
        }
        double minDiff = diffs.get(0);
        for (int i = 1; i < diffs.size(); i++) {
            Double diff = diffs.get(i);
            if (diff < minDiff) {
                closestMethod = i;
            }
        }

        return methods.get(closestMethod);
    }
}
