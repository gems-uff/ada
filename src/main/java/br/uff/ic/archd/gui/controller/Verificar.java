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
import br.uff.ic.archd.service.mining.AnomaliesAnaliser;
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
    
    
    public void verificarHistogramasCongenitalAfterClass(ProjectRevisions newProjectRevisions, Project project, JavaConstructorService javaConstructorService){
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
        
        
        //classificar as anomalias quanto aos seus 24 tipos
        
        
        
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
                verificar.getAddMethodsEmergeAndCorrectToAnalizer(newProjectRevisions, p, javaContructorService);
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

