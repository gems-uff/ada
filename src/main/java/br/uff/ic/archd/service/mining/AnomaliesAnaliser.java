/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.service.mining;

import br.uff.ic.archd.db.dao.AnomalieDao;
import br.uff.ic.archd.db.dao.AnomalieItem;
import br.uff.ic.archd.db.dao.Constants;
import br.uff.ic.archd.db.dao.DataBaseFactory;
import br.uff.ic.archd.javacode.JavaAbstract;
import br.uff.ic.archd.javacode.JavaAttribute;
import br.uff.ic.archd.javacode.JavaClass;
import br.uff.ic.archd.javacode.JavaConstructorService;
import br.uff.ic.archd.javacode.JavaMethod;
import br.uff.ic.archd.javacode.JavaPackage;
import br.uff.ic.archd.javacode.JavaProject;
import br.uff.ic.archd.model.Project;
import br.uff.ic.dyevc.application.branchhistory.model.ProjectRevisions;
import br.uff.ic.dyevc.application.branchhistory.model.Revision;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author wallace
 */
public class AnomaliesAnaliser {

    public ProjectAnomalies getAnomalies(ProjectRevisions newProjectRevisions, Project project, JavaConstructorService javaConstructorService) {

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
        ProjectAnomalies projectAnomalies = new ProjectAnomalies(k);
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
                List<JavaMethod> newMethodsClasseAlternativeName = new LinkedList();

                for (JavaMethod javaMethod : javaClass.getMethods()) {

                    //ainda nao foi dito a existencia desse metodo
                    if (birthHashMap.get(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature()) == null) {

                        birthHashMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), k);
                        if (methodsAlternativeNameMap.get(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature()) == null) {
                            methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature());
                            String alternativeClassName = classesAlternativeNameMap.get(javaClass.getFullQualifiedName());
                            //o método ainda nao surgiu e a classe que ele esta mudou de nome
                            if (alternativeClassName != null) {
                                //ainda nao surgiu o método
                                String alternativeMethodName = methodsAlternativeNameMap.get(alternativeClassName + ":" + javaMethod.getMethodSignature());
                                if (alternativeMethodName == null) {
                                    if (ant != null) {
                                        //adiciona ao campo dos metodos novos
                                        newMethodsClasseAlternativeName.add(javaMethod);

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
//                                                alternativeMethodName = methodsAlternativeNameMap.get(javaClass.getFullQualifiedName() + ":" + auxJm.getMethodSignature());
//                                                if (alternativeMethodName == null) {
//                                                    alternativeMethodName = alternativeClassName + ":" + auxJm.getMethodSignature();
//                                                }
//                                                System.out.println("Metodo mudou de assinatura: " + javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + "   -    " + alternativeMethodName);
//                                                methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), alternativeMethodName);
//
//                                            }
//
//                                        }
                                    }
                                } else {
                                    //a classe mudou de nome e a encontramos com esse nome novo
                                    methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), alternativeMethodName);
                                    System.out.println("Metodo mudou nome completo (por causa da mudança do nome da classe): " + javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + "   -    " + alternativeMethodName);
                                }
                            } else {
                                // a classe nao mudou de nome
                                if (ant != null) {
                                    //adiciona ao campo dos metodos novos
                                    newMethods.add(javaMethod);
                                    //verificaremos se algum mpetodo existia antes e sumiu depois, pode ter se transformado
//                                    JavaAbstract antAbstract = ant.getClassByName(javaClass.getFullQualifiedName());
//                                    if (antAbstract != null && antAbstract.getClass() == JavaClass.class) {
//
//                                        List<JavaMethod> methodsName = new LinkedList();
//                                        List<JavaMethod> methodsNameAnt = ((JavaClass) antAbstract).getMethodsByName(javaMethod.getName());
//                                        List<JavaMethod> methodsNameCurrent = javaClass.getMethodsByName(javaMethod.getName());
//                                        for (JavaMethod jm : methodsNameAnt) {
//                                            boolean exists = false;
//                                            for (JavaMethod auxJm : methodsNameCurrent) {
//                                                if (auxJm.getMethodSignature().equals(jm.getMethodSignature())) {
//                                                    exists = true;
//                                                    break;
//                                                }
//                                            }
//                                            if (!exists) {
//                                                methodsName.add(jm);
//                                            }
//                                        }
//
//                                        if (!methodsName.isEmpty()) {
//                                            JavaMethod auxJm = closestMethod(javaMethod, methodsName);
//                                            String alternativeMethodName = methodsAlternativeNameMap.get(javaClass.getFullQualifiedName() + ":" + auxJm.getMethodSignature());
//                                            if (alternativeMethodName == null) {
//                                                alternativeMethodName = javaClass.getFullQualifiedName() + ":" + auxJm.getMethodSignature();
//                                            }
//                                            System.out.println("Metodo mudou de assinatura: " + javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + "   -    " + alternativeMethodName);
//                                            methodsAlternativeNameMap.put(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), alternativeMethodName);
//
//                                        }
//
//                                    }
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

            long i1 = System.currentTimeMillis();
            List<AnomalieItem> items = anomalieDao.getItemsByRevisionId(rev.getId());
            long i2 = System.currentTimeMillis();
            System.out.println("Pegar anomalies da revisão " + rev.getId() + " : " + (i2 - i1) + " milisegundos");
            for (AnomalieItem anomalieItem : items) {
                if (anomalieItem.getAnomalieId() == Constants.ANOMALIE_GOD_PACKAGE) {
                    JavaPackage javaPackage = jp.getPackageByName(anomalieItem.getItem());
                    projectAnomalies.addPackageAnomalie(javaPackage.getName(), "GOD PACKAGE", k, birthHashMap.get(javaPackage.getName()), birthHashMap.get(javaPackage.getName()));
                } else if (anomalieItem.getAnomalieId() == Constants.ANOMALIE_GOD_CLASS) {
                    JavaAbstract javaAbstract = jp.getClassByName(anomalieItem.getItem());
                    String alternativeName = classesAlternativeNameMap.get(javaAbstract.getFullQualifiedName());
                    if (alternativeName == null) {
                        alternativeName = javaAbstract.getFullQualifiedName();
                    }
                    projectAnomalies.addClassAnomalie(alternativeName, javaAbstract.getFullQualifiedName(), "GOD CLASS", k, birthHashMap.get(alternativeName), birthHashMap.get(alternativeName));
                } else if (anomalieItem.getAnomalieId() == Constants.ANOMALIE_MISPLACED_CLASS) {
                    JavaAbstract javaAbstract = jp.getClassByName(anomalieItem.getItem());
                    String alternativeName = classesAlternativeNameMap.get(javaAbstract.getFullQualifiedName());
                    if (alternativeName == null) {
                        alternativeName = javaAbstract.getFullQualifiedName();
                    }
                    projectAnomalies.addClassAnomalie(alternativeName, javaAbstract.getFullQualifiedName(), "MISPLACED CLASS", k, birthHashMap.get(alternativeName), birthHashMap.get(alternativeName));
                } else if (anomalieItem.getAnomalieId() == Constants.ANOMALIE_FEATURE_ENVY) {
                    String str[] = anomalieItem.getItem().split(":");
                    JavaAbstract javaAbstract = jp.getClassByName(str[0]);
                    if (javaAbstract != null && javaAbstract.getClass() == JavaClass.class) {
                        JavaClass jc = (JavaClass) javaAbstract;
                        JavaMethod javaMethod = jc.getMethodBySignature(str[1]);
                        if (javaMethod != null) {
                            String alternativeName = methodsAlternativeNameMap.get(javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature());
                            if (alternativeName == null) {
                                alternativeName = javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature();
                            }
                            String classAlternativeName = classesAlternativeNameMap.get(javaMethod.getJavaAbstract().getFullQualifiedName());
                            if (classAlternativeName == null) {
                                classAlternativeName = javaMethod.getJavaAbstract().getFullQualifiedName();
                            }
                            projectAnomalies.addMethodAnomalie(alternativeName, javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), "FEATURE ENVY", k, birthHashMap.get(alternativeName), birthHashMap.get(classAlternativeName));
                        }
                    }
                } else if (anomalieItem.getAnomalieId() == Constants.ANOMALIE_SHOTGUN_SURGERY) {
                    String str[] = anomalieItem.getItem().split(":");
                    JavaAbstract javaAbstract = jp.getClassByName(str[0]);
                    if (javaAbstract != null && javaAbstract.getClass() == JavaClass.class) {
                        JavaClass jc = (JavaClass) javaAbstract;
                        JavaMethod javaMethod = jc.getMethodBySignature(str[1]);
                        if (javaMethod != null) {
                            String alternativeName = methodsAlternativeNameMap.get(javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature());
                            if (alternativeName == null) {
                                alternativeName = javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature();
                            }
                            String classAlternativeName = classesAlternativeNameMap.get(javaMethod.getJavaAbstract().getFullQualifiedName());
                            if (classAlternativeName == null) {
                                classAlternativeName = javaMethod.getJavaAbstract().getFullQualifiedName();
                            }
                            projectAnomalies.addMethodAnomalie(alternativeName, javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), "SHOTGUN SURGERY", k, birthHashMap.get(alternativeName), birthHashMap.get(classAlternativeName));
                        }
                    }
                } else if (anomalieItem.getAnomalieId() == Constants.ANOMALIE_GOD_METHOD) {
                    String str[] = anomalieItem.getItem().split(":");
                    JavaAbstract javaAbstract = jp.getClassByName(str[0]);
                    if (javaAbstract != null && javaAbstract.getClass() == JavaClass.class) {
                        JavaClass jc = (JavaClass) javaAbstract;
                        JavaMethod javaMethod = jc.getMethodBySignature(str[1]);
                        if (javaMethod != null) {
                            String alternativeName = methodsAlternativeNameMap.get(javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature());
                            if (alternativeName == null) {
                                alternativeName = javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature();
                            }
                            String classAlternativeName = classesAlternativeNameMap.get(javaMethod.getJavaAbstract().getFullQualifiedName());
                            if (classAlternativeName == null) {
                                classAlternativeName = javaMethod.getJavaAbstract().getFullQualifiedName();
                            }
                            projectAnomalies.addMethodAnomalie(alternativeName, javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), "GOD METHOD", k, birthHashMap.get(alternativeName), birthHashMap.get(classAlternativeName));
                        }
                    }
                }
            }

            /*List<JavaPackage> godPackages = transformFromTuple.getGodPackage(jp);
             for (JavaPackage javaPackge : godPackages) {
             System.out.println("GOD PACKAGE");
             projectAnomalies.addPackageAnomalie(javaPackge.getName(), "GOD PACKAGE", k);
             }
             List<JavaClass> godClasses = transformFromTuple.getGodClass(jp);
             for (JavaClass javaClass : godClasses) {
             System.out.println("GOD CLASS");
             projectAnomalies.addClassAnomalie(javaClass.getFullQualifiedName(), "GOD CLASS", k);
             }
             List<JavaClass> misplacedClasses = transformFromTuple.getMisplacedClass(jp);
             for (JavaClass javaClass : misplacedClasses) {
             projectAnomalies.addClassAnomalie(javaClass.getFullQualifiedName(), "MISPLACED CLASS", k);
             }

             for (JavaAbstract javaAbstract : jp.getClasses()) {
             if (javaAbstract.getClass() == JavaClass.class) {
             JavaClass jc = (JavaClass) javaAbstract;
             List<JavaMethod> featureEnvyMethods = transformFromTuple.getFeatureEnvy(jc);
             for (JavaMethod javaMethod : featureEnvyMethods) {
             System.out.println("FEATURE ENVY");
             projectAnomalies.addMethodAnomalie(javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), "FEATURE ENVY", k);
             }
             List<JavaMethod> shotgunSurgeryMethods = transformFromTuple.getShotgunSurgery(jc);
             for (JavaMethod javaMethod : shotgunSurgeryMethods) {
             System.out.println("SHOTGUN SURGERY");
             projectAnomalies.addMethodAnomalie(javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), "SHOTGUN SURGERY", k);
             }
             List<JavaMethod> godMethods = transformFromTuple.getGodMethod(jc);
             for (JavaMethod javaMethod : godMethods) {
             System.out.println("GOD METHOD");
             projectAnomalies.addMethodAnomalie(javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), "GOD METHOD", k);
             }
             }
             }*/
            System.out.println("Calculou anomalies: " + k);

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
        return projectAnomalies;

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

    public boolean isSameJavaMethod(JavaMethod currentMethod, JavaMethod antMethod) {
        boolean isSame = false;

        if (currentMethod.getName().equals(antMethod.getName())) {
            int numberOfCurrentChars = currentMethod.getSizeInChars();
            int numberOfAntChars = antMethod.getSizeInChars();
            int numberOfCurrentNumberOfLocalVariables = currentMethod.getNumberOfLocalVariables();
            int numberOfAntNumberOfLocalVariables = antMethod.getNumberOfLocalVariables();
            int numberOfCurrentCyclomatic = currentMethod.getCyclomaticComplexity();
            int numberOfAntCyclomatic = antMethod.getCyclomaticComplexity();

            if ((numberOfCurrentChars < (numberOfAntChars + (numberOfAntChars * 0.2)) && numberOfCurrentChars > (numberOfAntChars - (numberOfAntChars * 0.2)))
                    && (numberOfCurrentNumberOfLocalVariables < (numberOfAntNumberOfLocalVariables + (numberOfAntNumberOfLocalVariables * 0.2)) && numberOfCurrentNumberOfLocalVariables > (numberOfAntNumberOfLocalVariables - (numberOfAntNumberOfLocalVariables * 0.2)))
                    && (numberOfCurrentCyclomatic < (numberOfAntCyclomatic + (numberOfAntCyclomatic * 0.2)) && numberOfCurrentCyclomatic > (numberOfAntCyclomatic - (numberOfAntCyclomatic * 0.2)))) {
                isSame = true;
            }

        }

        return isSame;
    }
}
