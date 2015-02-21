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
import br.uff.ic.archd.javacode.JavaClass;
import br.uff.ic.archd.javacode.JavaConstructorService;
import br.uff.ic.archd.javacode.JavaMethod;
import br.uff.ic.archd.javacode.JavaPackage;
import br.uff.ic.archd.javacode.JavaProject;
import br.uff.ic.archd.model.Project;
import br.uff.ic.dyevc.application.branchhistory.model.ProjectRevisions;
import br.uff.ic.dyevc.application.branchhistory.model.Revision;
import java.util.HashMap;
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
        TransformFromTuple transformFromTuple = new TransformFromTuple();
        rev = newProjectRevisions.getRoot();
        AnomalieDao anomalieDao = DataBaseFactory.getInstance().getAnomalieDao();
        HashMap<String, Integer> birthHashMap = new HashMap();
        k = 0;
        while (rev != null) {

            JavaProject jp = null;
            //System.out.println("REV ID: "+rev.getId());
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
                }
                for (JavaMethod javaMethod : javaClass.getMethods()) {
                    if (birthHashMap.get(javaClass.getFullQualifiedName()+":"+javaMethod.getMethodSignature()) == null) {
                        birthHashMap.put(javaClass.getFullQualifiedName()+":"+javaMethod.getMethodSignature(), k);
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
                    projectAnomalies.addPackageAnomalie(javaPackage.getName(), "GOD PACKAGE", k, birthHashMap.get(javaPackage.getName()));
                } else if (anomalieItem.getAnomalieId() == Constants.ANOMALIE_GOD_CLASS) {
                    JavaAbstract javaAbstract = jp.getClassByName(anomalieItem.getItem());
                    projectAnomalies.addClassAnomalie(javaAbstract.getFullQualifiedName(), "GOD CLASS", k, birthHashMap.get(javaAbstract.getFullQualifiedName()));
                } else if (anomalieItem.getAnomalieId() == Constants.ANOMALIE_MISPLACED_CLASS) {
                    JavaAbstract javaAbstract = jp.getClassByName(anomalieItem.getItem());
                    projectAnomalies.addClassAnomalie(javaAbstract.getFullQualifiedName(), "MISPLACED CLASS", k, birthHashMap.get(javaAbstract.getFullQualifiedName()));
                } else if (anomalieItem.getAnomalieId() == Constants.ANOMALIE_FEATURE_ENVY) {
                    String str[] = anomalieItem.getItem().split(":");
                    JavaAbstract javaAbstract = jp.getClassByName(str[0]);
                    if (javaAbstract != null && javaAbstract.getClass() == JavaClass.class) {
                        JavaClass jc = (JavaClass) javaAbstract;
                        JavaMethod javaMethod = jc.getMethodBySignature(str[1]);
                        if (javaMethod != null) {
                            projectAnomalies.addMethodAnomalie(javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), "FEATURE ENVY", k, birthHashMap.get(javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature()));
                        }
                    }
                } else if (anomalieItem.getAnomalieId() == Constants.ANOMALIE_SHOTGUN_SURGERY) {
                    String str[] = anomalieItem.getItem().split(":");
                    JavaAbstract javaAbstract = jp.getClassByName(str[0]);
                    if (javaAbstract != null && javaAbstract.getClass() == JavaClass.class) {
                        JavaClass jc = (JavaClass) javaAbstract;
                        JavaMethod javaMethod = jc.getMethodBySignature(str[1]);
                        if (javaMethod != null) {
                            projectAnomalies.addMethodAnomalie(javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), "SHOTGUN SURGERY", k, birthHashMap.get(javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature()));
                        }
                    }
                } else if (anomalieItem.getAnomalieId() == Constants.ANOMALIE_GOD_METHOD) {
                    String str[] = anomalieItem.getItem().split(":");
                    JavaAbstract javaAbstract = jp.getClassByName(str[0]);
                    if (javaAbstract != null && javaAbstract.getClass() == JavaClass.class) {
                        JavaClass jc = (JavaClass) javaAbstract;
                        JavaMethod javaMethod = jc.getMethodBySignature(str[1]);
                        if (javaMethod != null) {
                            projectAnomalies.addMethodAnomalie(javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), "GOD METHOD", k, birthHashMap.get(javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature()));
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
}
