/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.javacode;

import br.uff.ic.archd.ast.service.AstService;
import br.uff.ic.archd.ast.service.JavaMethodAstBox;
import br.uff.ic.archd.ast.service.ParameterAst;
import br.uff.ic.archd.db.dao.AnomalieDao;
import br.uff.ic.archd.db.dao.ArtifactBirthDao;
import br.uff.ic.archd.db.dao.ClassesDao;
import br.uff.ic.archd.db.dao.Constants;
import br.uff.ic.archd.db.dao.DataBaseFactory;
import br.uff.ic.archd.db.dao.ExternalImportsDao;
import br.uff.ic.archd.db.dao.ImplementedInterfacesDao;
import br.uff.ic.archd.db.dao.InterfaceDao;
import br.uff.ic.archd.db.dao.InternalImportsDao;
import br.uff.ic.archd.db.dao.JavaAttributeDao;
import br.uff.ic.archd.db.dao.JavaExternalAttributeAccessDao;
import br.uff.ic.archd.db.dao.JavaMethodDao;
import br.uff.ic.archd.db.dao.MethodInvocationsDao;
import br.uff.ic.archd.db.dao.OriginalNameDao;
import br.uff.ic.archd.db.dao.TerminatedDao;
import br.uff.ic.archd.xml.service.XMLService;
import br.uff.ic.dyevc.application.branchhistory.model.BranchRevisions;
import br.uff.ic.dyevc.application.branchhistory.model.LineRevisions;
import br.uff.ic.dyevc.application.branchhistory.model.ProjectRevisions;
import br.uff.ic.dyevc.application.branchhistory.model.Revision;
import br.uff.ic.dyevc.application.branchhistory.model.RevisionsBucket;
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

                JavaProject javaProject = this.getProjectByRevision(projectName, newCodeDirs, BRANCHES_HISTORY_PATH + projectRevisions.getName(), revision.getId(), null);
                javaProjects.add(javaProject);
                //System.out.println("Salvo revisão: " + revision.getId() + "      Número: " + javaProjects.size());
            }
        } catch (Exception e) {
            System.out.println("Exception getAllProjectsRevision: " + e.getMessage() + "             class: " + e.getClass());
            e.printStackTrace();
        }

        return javaProjects;
    }

    public void calculateAllProjectsRevision(String projectName, List<String> codeDirs, String path, ProjectRevisions projectRevisions) {

        ProjectRevisions newProjectRevisions = cleanProjectRevisionsLine(projectRevisions);
        String revisionAnt = null;
        Revision rev = newProjectRevisions.getRoot();
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
            TerminatedDao terminatedDao = DataBaseFactory.getInstance().getTerminatedDao();
            int k = 0;
            while (rev != null) {

                long t1 = System.currentTimeMillis();
                gitConnector = new GitConnector(BRANCHES_HISTORY_PATH + projectName, projectRevisions.getName());
                git = new Git(gitConnector.getRepository());

                checkoutCommand = git.checkout();
                checkoutCommand.setName(rev.getId());
                checkoutCommand.call();

                if (!terminatedDao.isTerminated(projectName, rev.getId())) {
                    JavaProject javaProject = this.getProjectByRevision(projectName, newCodeDirs, BRANCHES_HISTORY_PATH + projectRevisions.getName(), rev.getId(), revisionAnt);
                    javaProject = null;
                }
                long t2 = System.currentTimeMillis();
                k++;
                System.out.println("Calculou :" + k + "        demorou: " + (t2 - t1));
                revisionAnt = rev.getId();
                if (rev.getNext().size() == 0) {
                    rev = null;
                } else {
                    rev = rev.getNext().get(0);
                }

            }

        } catch (Exception e) {
            System.out.println("Exception getAllProjectsRevision: " + e.getMessage() + "             class: " + e.getClass());
            e.printStackTrace();
        }

    }

    public JavaProject getProjectByRevisionAndSetRevision(String projectName, List<String> codeDirs, String path, String revisionId, String projectRevisionsName) {

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
            GitConnector gitConnector = new GitConnector(BRANCHES_HISTORY_PATH + projectName, projectRevisionsName);
            Git git = new Git(gitConnector.getRepository());
            CheckoutCommand checkoutCommand = null;

            gitConnector = new GitConnector(BRANCHES_HISTORY_PATH + projectName, projectRevisionsName);
            git = new Git(gitConnector.getRepository());

            checkoutCommand = git.checkout();
            checkoutCommand.setName(revisionId);
            checkoutCommand.call();

            long tempoclone2 = System.currentTimeMillis();
            System.out.println("TEMPO PRA FAZER UM CLONE: " + (tempoclone2 - tempoclone1) + " milisegundos");

            long t1 = System.currentTimeMillis();
            JavaProject javaProject = this.getProjectByRevision(projectName, newCodeDirs, BRANCHES_HISTORY_PATH + projectRevisionsName, revisionId, null);
            long t2 = System.currentTimeMillis();
            System.out.println("Tempo para criar um projeto de uma revisão: " + (t2 - t1) + " milisegundos");
            return javaProject;
        } catch (Exception e) {
            System.out.println("Exception getProjectByRevisionAndSetRevision: " + e.getMessage() + "             class: " + e.getClass());
            e.printStackTrace();
        }
        return null;
    }

    public void calculateProjectByRevisionAndSetRevisionOffMemory(String projectName, List<String> codeDirs, String path, String revisionId, String projectRevisionsName) {

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
            GitConnector gitConnector = new GitConnector(BRANCHES_HISTORY_PATH + projectName, projectRevisionsName);
            Git git = new Git(gitConnector.getRepository());
            CheckoutCommand checkoutCommand = null;

            gitConnector = new GitConnector(BRANCHES_HISTORY_PATH + projectName, projectRevisionsName);
            git = new Git(gitConnector.getRepository());

            checkoutCommand = git.checkout();
            checkoutCommand.setName(revisionId);
            checkoutCommand.call();

            long tempoclone2 = System.currentTimeMillis();
            System.out.println("TEMPO PRA FAZER UM CLONE: " + (tempoclone2 - tempoclone1) + " milisegundos");

            long t1 = System.currentTimeMillis();
            this.getProjectByRevisionOffMemory(projectName, newCodeDirs, BRANCHES_HISTORY_PATH + projectRevisionsName, revisionId);
            long t2 = System.currentTimeMillis();
            System.out.println("Tempo para criar um projeto de uma revisão: " + (t2 - t1) + " milisegundos");

        } catch (Exception e) {
            System.out.println("Exception getProjectByRevisionAndSetRevision: " + e.getMessage() + "             class: " + e.getClass());
            e.printStackTrace();
        }
    }

    public JavaProject getProjectByRevision(String projectName, List<String> codeDirs, String path, String revisionId, String antRevision) {
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
        ArtifactBirthDao artifactBirthDao = DataBaseFactory.getInstance().getArtifactBirthDao();
        AnomalieDao anomalieDao = DataBaseFactory.getInstance().getAnomalieDao();
        OriginalNameDao originalNameDao = DataBaseFactory.getInstance().getOriginalNameDao();
        System.out.println("Vai verificar se possui no banco");
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
            //set the package original signatures
            for(JavaPackage javaPackage : javaProject.getPackages()){
                int num = 1;
                JavaAbstract javaAbstract = javaPackage.getClasses().get(0);
                String classString = javaAbstract.getOriginalSignature();
                String classSplit[] = classString.split("\\.");
                String className = classSplit[classSplit.length - 1];
                int n = classString.length() - (className.length() + 1);
                if (n < 0) {
                    n = 0;
                }
                String classPackageName = classString.substring(0, n);
                for(int j = 1; j < javaPackage.getClasses().size(); j++){
                    javaAbstract = javaPackage.getClasses().get(j);
                    classString = javaAbstract.getOriginalSignature();
                    classSplit = classString.split("\\.");
                    className = classSplit[classSplit.length - 1];
                    n = classString.length() - (className.length() + 1);
                    if (n < 0) {
                        n = 0;
                    }
                    String classPackageAux = classString.substring(0, n);
                    if(classPackageAux.equals(classPackageName)){
                        num++;
                    }
                }
                if(num == javaPackage.getClasses().size()){
                    javaPackage.setOriginalSignature(classPackageName);
                }
            }
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
                    //System.gc();
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
            System.out.println("TEMPO TOTAL PRA PEGAR UMA REVISÃO Do BANCO: " + (tempototal2 - tempototal1) + " milisegundos");
            setProjectProperties(javaProject);

        } else {
            System.out.println("Vai calcular métricas");
            javaProject = this.createProjects(codeDirs, path, revisionId);

            //System.gc();
            //verificar o nascimetno dos pacotes caso eles tenham nascidos outro dia
            if (antRevision != null) {
                System.out.println("TEM REVISAO ANTERIOR");
                JavaProject antProject = getProjectByRevision(projectName, codeDirs, path, antRevision, null);
                List<JavaPackage> newPackages = new LinkedList();
                List<JavaPackage> pacotesSumidos = new LinkedList();
                for (JavaPackage javaPackage : javaProject.getPackages()) {
                    String originalName = originalNameDao.getOriginalName(javaPackage.getName());
                    if (originalName == null) {
                        newPackages.add(javaPackage);
                    } else {
                        javaPackage.setOriginalSignature(originalName);
                    }
                }
                for (JavaPackage javaPackage : antProject.getPackages()) {
                    if (javaProject.getPackageByName(javaPackage.getName()) == null) {
                        pacotesSumidos.add(javaPackage);
                    }

                }
                //salvar os novos pacotes
                packagesSemelhanca(newPackages, pacotesSumidos, originalNameDao, artifactBirthDao, revisionId);

                //verificar as classes e os metodos
                for (JavaAbstract javaAbstract : javaProject.getClasses()) {
                    JavaClass javaClass = (JavaClass) javaAbstract;

                    String originalName = originalNameDao.getOriginalName(javaClass.getFullQualifiedName());
                    //birthHashMap.put(javaClass.getFullQualifiedName(), k);

                    if (originalName == null) {
                        List<JavaAbstract> antClasses = antProject.getClassByLastName(javaAbstract.getName());
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
                                String originalSignature = originalNameDao.getOriginalName(sameClasses.get(0).getFullQualifiedName());

                                if (originalSignature == null) {
                                    originalSignature = sameClasses.get(0).getFullQualifiedName();
                                }
                                System.out.println("Classe mudou de nome: " + javaClass.getFullQualifiedName() + "   -    " + originalSignature);
                                originalNameDao.save(javaClass.getFullQualifiedName(), originalSignature);
                                javaClass.setOriginalSignature(originalSignature);

                            } else {
                                //classe nova
                                originalNameDao.save(javaClass.getFullQualifiedName(), javaClass.getFullQualifiedName());
                                artifactBirthDao.save(javaClass.getFullQualifiedName(), revisionId);
                                javaClass.setOriginalSignature(javaClass.getFullQualifiedName());
                            }
                        }

                    } else {
                        javaClass.setOriginalSignature(originalName);
                    }

                    List<JavaMethod> newMethods = new LinkedList();
                    List<JavaMethod> newMethodsClasseAlternativeName = new LinkedList();

                    for (JavaMethod javaMethod : javaClass.getMethods()) {

                        //ainda nao foi dito a existencia desse metodo
                        String orignalClasseSignature = originalNameDao.getOriginalName(javaClass.getFullQualifiedName());
                        String orignalMethodSignature = originalNameDao.getOriginalName(orignalClasseSignature + ":" + javaMethod.getMethodSignature());
                        if (orignalMethodSignature == null) {

                            //System.out.println("%%%%%%%%%%%%%%%%%%% METODO NOVO: "+orignalClasseSignature+":"+javaMethod.getMethodSignature());
                            newMethods.add(javaMethod);

                        } else {
                            
                            javaMethod.setOriginalSignature(orignalMethodSignature);
                            //System.out.println("ZZZZZZZZZZZZZZZZZZ METODO ANTIGO: "+orignalClasseSignature+":"+javaMethod.getMethodSignature()+"          original name: "+javaMethod.getOriginalSignature());
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

                                //percorre cada um dos metodos de mesmo nome
                                for (JavaMethod javaMethod : methodsWithSameName) {
                                    //verificaremos se algum mpetodo existia antes e sumiu depois, pode ter se transformado
                                    JavaAbstract antAbstract = antProject.getClassByOriginalSignature(javaClass.getOriginalSignature());

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
                                                if (javaMethod.getName().equals("HashMap2")) {
                                                    System.out.println("Entrou 1: " + javaMethod.getMethodSignature());
                                                }
                                                String alternativeMethodName = originalNameDao.getOriginalName(javaClass.getOriginalSignature() + ":" + auxJm.getMethodSignature());
                                                if (alternativeMethodName == null) {
                                                    alternativeMethodName = javaClass.getOriginalSignature() + ":" + auxJm.getMethodSignature();
                                                }
                                                System.out.println("Metodo mudou de assinatura: " + javaClass.getOriginalSignature() + ":" + javaMethod.getMethodSignature() + "   -    " + alternativeMethodName);
                                                originalNameDao.save(javaClass.getOriginalSignature() + ":" + javaMethod.getMethodSignature(), alternativeMethodName);
                                                javaMethod.setOriginalSignature(alternativeMethodName);
                                            } else {
                                                //nao existe ninguem proximo desse metodo, ele é novo
                                                if (javaMethod.getName().equals("HashMap2")) {
                                                    System.out.println("Entrou 2: " + javaMethod.getMethodSignature());
                                                }
                                                originalNameDao.save(javaClass.getOriginalSignature() + ":" + javaMethod.getMethodSignature(), javaClass.getOriginalSignature() + ":" + javaMethod.getMethodSignature());
                                                javaMethod.setOriginalSignature(javaClass.getOriginalSignature() + ":" + javaMethod.getMethodSignature());
                                                artifactBirthDao.save(javaClass.getOriginalSignature() + ":" + javaMethod.getMethodSignature(), revisionId);
                                            }
                                        } else {
                                            if (javaMethod.getName().equals("HashMap2")) {
                                                System.out.println("Entrou 2: " + javaMethod.getMethodSignature());
                                            }
                                            originalNameDao.save(javaClass.getOriginalSignature() + ":" + javaMethod.getMethodSignature(), javaClass.getOriginalSignature() + ":" + javaMethod.getMethodSignature());
                                            javaMethod.setOriginalSignature(javaClass.getOriginalSignature() + ":" + javaMethod.getMethodSignature());
                                            artifactBirthDao.save(javaClass.getOriginalSignature() + ":" + javaMethod.getMethodSignature(), revisionId);

                                        }

                                    } else {
                                        //metodo novo
                                        originalNameDao.save(javaClass.getOriginalSignature() + ":" + javaMethod.getMethodSignature(), javaClass.getOriginalSignature() + ":" + javaMethod.getMethodSignature());
                                        javaMethod.setOriginalSignature(javaClass.getOriginalSignature() + ":" + javaMethod.getMethodSignature());
                                        artifactBirthDao.save(javaClass.getOriginalSignature() + ":" + javaMethod.getMethodSignature(), revisionId);

                                    }
                                }
                            }
                        }
                    }


                }

            } else {
                //primeira revisão, portanto todos eles nasceram agora


                List<String> artifactNames = new LinkedList();
                for (JavaPackage javaPackage : javaProject.getPackages()) {
                    artifactNames.add(javaPackage.getName());
//                    artifactBirthDao.save(javaPackage.getName(), revisionId);
//                    originalNameDao.save(javaPackage.getName(), javaPackage.getName());
                    javaPackage.setOriginalSignature(javaPackage.getName());
                }

                for (JavaAbstract javaAbstract : javaProject.getAllClasses()) {
                    artifactNames.add(javaAbstract.getFullQualifiedName());
                    //artifactBirthDao.save(javaAbstract.getFullQualifiedName(), revisionId);
                    //originalNameDao.save(javaAbstract.getFullQualifiedName(), javaAbstract.getFullQualifiedName());
                    if (javaAbstract.getClass() == JavaClass.class) {
                        JavaClass javaClass = (JavaClass) javaAbstract;
                        for (JavaMethod javaMethod : javaClass.getMethods()) {
                            artifactNames.add(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature());
                            //artifactBirthDao.save(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), revisionId);
                            //originalNameDao.save(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature());
                            javaMethod.setOriginalSignature(javaClass.getFullQualifiedName() + ":" + javaMethod.getMethodSignature());
                        }
                        javaAbstract.setOriginalSignature(javaAbstract.getFullQualifiedName());
                    }
                }

                //salva todos juntos
                artifactBirthDao.save(artifactNames, revisionId);
                originalNameDao.save(artifactNames);

            }
            //salvar todas as classes
            List<JavaClass> javaClasses = new LinkedList();
            for (JavaAbstract javaAbstract : javaProject.getClasses()) {
                javaClasses.add((JavaClass) javaAbstract);
            }
            classesDao.save(javaClasses);

            List<JavaAttribute> javaAttributes = new LinkedList();
            List<JavaMethod> javaMethods = new LinkedList();
            for (JavaAbstract javaAbstract : javaProject.getClasses()) {
                JavaClass javaClass = (JavaClass) javaAbstract;
                //classesDao.save(javaClass);
                //System.gc();

                List<String> externalImports = new LinkedList();
                for (String externalImport : javaClass.getExternalImports()) {
                    //externalImportsDao.save(javaAbstract, externalImport);
                    externalImports.add(externalImport);
                }
                //System.gc();
                for (JavaAttribute javaAttribute : javaClass.getAttributes()) {
                    javaAttribute.setJavaClassId(javaClass.getId());
                    javaAttributes.add(javaAttribute);
                    //javaAttributeDao.save(javaAttribute, javaClass.getId());
                }
                //System.gc();
                for (JavaMethod javaMethod : javaClass.getMethods()) {
                    javaMethod.setFromClass(true);
                    javaMethod.setItemId(javaClass.getId());
                    javaMethods.add(javaMethod);
                    //javaMethodDao.save(javaMethod, true, javaClass.getId());
                }
                //salvar tudo de uma vez
                externalImportsDao.save(javaAbstract, externalImports);

                if (javaAttributes.size() >= 1000) {
                    javaAttributeDao.save(javaAttributes);
                    javaAttributes.clear();
                }
                if (javaMethods.size() >= 800) {
                    javaMethodDao.save(javaMethods);
                    javaMethods.clear();
                }

            }
            if (!javaAttributes.isEmpty()) {
                javaAttributeDao.save(javaAttributes);
                javaAttributes.clear();
            }
            
            if (!javaMethods.isEmpty()) {
                javaMethodDao.save(javaMethods);
                javaMethods.clear();
            }
            //System.gc();
            List<JavaInterface> javaInterfaces = new LinkedList();
            for (JavaAbstract javaAbstract : javaProject.getInterfaces()) {
                javaInterfaces.add((JavaInterface) javaAbstract);
            }
            //salvar tudo de uma vez
            interfaceDao.save(javaInterfaces);
            for (JavaAbstract javaAbstract : javaProject.getInterfaces()) {
                JavaInterface javaInterface = (JavaInterface) javaAbstract;
                //interfaceDao.save(javaInterface);
                //System.gc();
                for (JavaMethod javaMethod : javaInterface.getMethods()) {
                    //javaMethodDao.save(javaMethod, false, javaInterface.getId());
                    javaMethod.setFromClass(false);
                    javaMethod.setItemId(javaInterface.getId());
                    javaMethods.add(javaMethod);
                }
                //salvar de uma vez
                //javaMethodDao.save(javaMethods, false, javaInterface.getId());
                if (javaMethods.size() >= 800) {
                    javaMethodDao.save(javaMethods);
                    javaMethods.clear();
                }
            }

            if (!javaMethods.isEmpty()) {
                javaMethodDao.save(javaMethods);
                javaMethods.clear();
            }
            
            

            //System.gc();
            List<JavaClass> implemententedInterfacesClassList = new LinkedList();
            List<JavaInterface> implemententedInterfacesInterfaceList = new LinkedList();
            List<JavaAbstract> javaAbstracts = new LinkedList();
            for (JavaAbstract javaAbstract : javaProject.getClasses()) {
                JavaClass javaClass = (JavaClass) javaAbstract;
                javaAbstracts.add(javaAbstract);
                //System.gc();
                for (JavaInterface javaInterface : javaClass.getImplementedInterfaces()) {
                    implemententedInterfacesClassList.add(javaClass);
                    implemententedInterfacesInterfaceList.add(javaInterface);
                    //implementedInterfacesDao.saveImplementedInterface(javaClass, javaInterface);
                }
                //System.gc();
//                for (JavaAbstract javaAbstractImport : javaClass.getClassesImports()) {
//                    internalImportsDao.saveInternalImport(javaClass, javaAbstractImport);
//                }
                //internalImportsDao.saveInternalImport(javaAbstract);
                //System.gc();
                for (JavaMethod javaMethod : javaClass.getMethods()) {
                    javaMethods.add(javaMethod);
//                    methodInvocationDao.saveMethodInvocations(javaMethod, javaClass);
//                    javaExternalAttributeAccessDao.saveJavaExternalAttributeAccess(javaMethod);
                }
                if (javaMethods.size() >= 500) {
                    methodInvocationDao.saveMethodInvocations(javaMethods);
                    javaExternalAttributeAccessDao.saveJavaExternalAttributeAccess(javaMethods);
                    javaMethods.clear();
                }

                if (javaAbstracts.size() >= 1000) {
                    internalImportsDao.saveInternalImport(javaAbstracts);
                    javaAbstracts.clear();
                }

                if (implemententedInterfacesClassList.size() >= 4000) {
                    implementedInterfacesDao.saveImplementedInterface(implemententedInterfacesClassList, implemententedInterfacesInterfaceList);
                    implemententedInterfacesClassList.clear();
                    implemententedInterfacesInterfaceList.clear();
                }

                //methodInvocationDao.saveMethodInvocations(javaClass);
                //javaExternalAttributeAccessDao.saveJavaExternalAttributeAccess(javaClass);
            }

            if (!javaMethods.isEmpty()) {
                methodInvocationDao.saveMethodInvocations(javaMethods);
                javaExternalAttributeAccessDao.saveJavaExternalAttributeAccess(javaMethods);
                javaMethods.clear();
            }

            if (!javaAbstracts.isEmpty()) {
                internalImportsDao.saveInternalImport(javaAbstracts);
                javaAbstracts.clear();
            }

            if (!implemententedInterfacesClassList.isEmpty()) {
                implementedInterfacesDao.saveImplementedInterface(implemententedInterfacesClassList, implemententedInterfacesInterfaceList);
                implemententedInterfacesClassList.clear();
                implemententedInterfacesInterfaceList.clear();
            }

            for (JavaAbstract javaAbstract : javaProject.getClasses()) {
                JavaClass jc = (JavaClass) javaAbstract;
                for (JavaMethod jm : jc.getMethods()) {
                    jm.removeBlock();
                }
            }
            //terminatedDao.save(projectName, revisionId);

        }
        javaProject.setRevisionId(revisionId);
        System.out.println("Vai setar changing methods");
        javaProject.setChangingMethodsAndClasses();
        System.out.println("Setou changing methods");
        javaProject.setClassesMetrics();
        System.out.println("Vai setar classes metrics");

        //trocar para não terminado
        if (!terminatedDao.isTerminated(projectName, revisionId)) {
            //colocar so design flaws

            //System.gc();
            List<JavaPackage> godPackages = getGodPackage(javaProject);
            for (JavaPackage javaPackge : godPackages) {
                System.out.println("GOD PACKAGE ()");
                anomalieDao.save(Constants.ANOMALIE_GOD_PACKAGE, javaPackge.getName(), revisionId);
            }
            //System.gc();
            List<JavaClass> godClasses = getGodClass(javaProject);
            for (JavaClass javaClass : godClasses) {
                System.out.println("GOD CLASS ()");
                anomalieDao.save(Constants.ANOMALIE_GOD_CLASS, javaClass.getFullQualifiedName(), revisionId);
            }
            //System.gc();
            List<JavaClass> misplacedClasses = getMisplacedClass(javaProject);
            for (JavaClass javaClass : misplacedClasses) {
                System.out.println("MISPLACED CLASS ()");
                anomalieDao.save(Constants.ANOMALIE_MISPLACED_CLASS, javaClass.getFullQualifiedName(), revisionId);
            }
            //System.gc();
            for (JavaAbstract javaAbstract : javaProject.getClasses()) {
                if (javaAbstract.getClass() == JavaClass.class) {
                    JavaClass jc = (JavaClass) javaAbstract;
                    List<JavaMethod> featureEnvyMethods = getFeatureEnvy(jc);
                    for (JavaMethod javaMethod : featureEnvyMethods) {
                        System.out.println("FEATURE ENVY ()");
                        anomalieDao.save(Constants.ANOMALIE_FEATURE_ENVY, javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), revisionId);
                    }
                    //System.gc();
                    List<JavaMethod> shotgunSurgeryMethods = getShotgunSurgery(jc);
                    for (JavaMethod javaMethod : shotgunSurgeryMethods) {
                        System.out.println("SHOTGUN SURGERY ()");
                        anomalieDao.save(Constants.ANOMALIE_SHOTGUN_SURGERY, javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), revisionId);
                    }
                    //System.gc();
                    List<JavaMethod> godMethods = getGodMethod(jc);
                    for (JavaMethod javaMethod : godMethods) {
                        System.out.println("GOD METHOD ()");
                        anomalieDao.save(Constants.ANOMALIE_GOD_METHOD, javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), revisionId);
                    }
                    //System.gc();
                }
            }

            //**************** terminou so design flwas
            //adicionar depois
            terminatedDao.save(projectName, revisionId);
            System.out.println("Salvou a revisão: " + revisionId);
        }

        return javaProject;
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

    public void packagesSemelhanca(List<JavaPackage> newPackages, List<JavaPackage> packagesSumidos, OriginalNameDao originalNameDao, ArtifactBirthDao artifactBirthDao, String revisionId) {
        List<JavaPackage> pacotesNovos = new LinkedList();
        List<JavaPackage> pacotesDisponiveis = new LinkedList();
        List<JavaPackage> pacotesConcorrentes = new LinkedList();
        for (JavaPackage javaPackage : packagesSumidos) {
            pacotesDisponiveis.add(javaPackage);
        }
        for (JavaPackage javaPackage : newPackages) {
            pacotesConcorrentes.add(javaPackage);
        }
        while (!newPackages.isEmpty()) {
            //verifica a semelhança com cada pacote disponivel
            JavaPackage javaPackage = newPackages.get(0);
            JavaPackage pacoteMaisSemelhante = null;
            double semelhanca = 0;
            for (JavaPackage pacoteDisponivel : pacotesDisponiveis) {
                int classesDeNomeIgual = 0;
                for (JavaAbstract javaAbstract : javaPackage.getClasses()) {
                    if (pacoteDisponivel.getClassByLastName(javaAbstract.getName()) != null) {
                        classesDeNomeIgual++;
                    }
                }
                double aux = classesDeNomeIgual;
                if (!javaPackage.getClasses().isEmpty()) {
                    aux = aux / javaPackage.getClasses().size();
                }
                if (aux > semelhanca) {
                    semelhanca = aux;
                    pacoteMaisSemelhante = pacoteDisponivel;
                }

            }
            //se o pacote mais semelhante não é null, verfica se nao existe ninguem mais parecido
            if (pacoteMaisSemelhante != null) {
                JavaPackage pacoteConcorrenteVencedor = null;
                double semelhancaPacoteConcorrente = 0;
                for (JavaPackage pacoteConcorrente : pacotesConcorrentes) {
                    if (pacoteConcorrente != javaPackage) {
                        int classesDeNomeIgual = 0;
                        for (JavaAbstract javaAbstract : pacoteConcorrente.getClasses()) {
                            if (pacoteMaisSemelhante.getClassByLastName(javaAbstract.getName()) != null) {
                                classesDeNomeIgual++;
                            }
                        }
                        double aux = classesDeNomeIgual;
                        if (!javaPackage.getClasses().isEmpty()) {
                            aux = aux / javaPackage.getClasses().size();
                        }
                        if (aux > semelhanca) {
                            semelhancaPacoteConcorrente = aux;
                            pacoteConcorrenteVencedor = pacoteConcorrente;
                        }
                    }
                }
                if (semelhancaPacoteConcorrente > semelhanca) {
                    //quem ganhou foi o pacote concorrente mesmo
                    originalNameDao.save(pacoteConcorrenteVencedor.getName(), pacoteMaisSemelhante.getOriginalSignature());
                    newPackages.remove(pacoteConcorrenteVencedor);
                    pacotesConcorrentes.remove(pacoteConcorrenteVencedor);
                    pacotesDisponiveis.remove(pacoteMaisSemelhante);
                    pacoteConcorrenteVencedor.setOriginalSignature(pacoteMaisSemelhante.getOriginalSignature());
                } else {
                    //eu sou o pacote mais semelhante
                    originalNameDao.save(javaPackage.getName(), pacoteMaisSemelhante.getOriginalSignature());
                    newPackages.remove(javaPackage);
                    pacotesConcorrentes.remove(javaPackage);
                    pacotesDisponiveis.remove(pacoteMaisSemelhante);
                    javaPackage.setOriginalSignature(pacoteMaisSemelhante.getOriginalSignature());
                }
            } else {
                pacotesNovos.add(javaPackage);
                newPackages.remove(javaPackage);
                originalNameDao.save(javaPackage.getName(), javaPackage.getName());
                javaPackage.setOriginalSignature(javaPackage.getName());
                artifactBirthDao.save(javaPackage.getName(), revisionId);
            }
        }
    }

    public void getProjectByRevisionOffMemory(String projectName, List<String> codeDirs, String path, String revisionId) {
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
        AnomalieDao anomalieDao = DataBaseFactory.getInstance().getAnomalieDao();
        System.out.println("Vai verificar se possui no banco");
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
                    //System.gc();
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
            System.out.println("TEMPO TOTAL PRA PEGAR UMA REVISÃO Do BANCO: " + (tempototal2 - tempototal1) + " milisegundos");
            setProjectProperties(javaProject);

        } else {
            System.out.println("Vai calcular métricas");
            //createProjectsOffMemory(codeDirs, path, revisionId);

            for (JavaAbstract javaAbstract : javaProject.getClasses()) {
                JavaClass javaClass = (JavaClass) javaAbstract;
                classesDao.save(javaClass);
                //System.gc();

                for (String externalImport : javaClass.getExternalImports()) {
                    externalImportsDao.save(javaAbstract, externalImport);
                }
                //System.gc();
                for (JavaAttribute javaAttribute : javaClass.getAttributes()) {
                    javaAttributeDao.save(javaAttribute, javaClass.getId());
                }
                //System.gc();
                for (JavaMethod javaMethod : javaClass.getMethods()) {
                    javaMethodDao.save(javaMethod, true, javaClass.getId());
                }

            }
            //System.gc();
            for (JavaAbstract javaAbstract : javaProject.getInterfaces()) {
                JavaInterface javaInterface = (JavaInterface) javaAbstract;
                interfaceDao.save(javaInterface);
                //System.gc();
                for (JavaMethod javaMethod : javaInterface.getMethods()) {
                    javaMethodDao.save(javaMethod, false, javaInterface.getId());
                }
            }

            //System.gc();
            for (JavaAbstract javaAbstract : javaProject.getClasses()) {
                JavaClass javaClass = (JavaClass) javaAbstract;
                //System.gc();
                for (JavaInterface javaInterface : javaClass.getImplementedInterfaces()) {
                    implementedInterfacesDao.saveImplementedInterface(javaClass, javaInterface);
                }
                //System.gc();
                for (JavaAbstract javaAbstractImport : javaClass.getClassesImports()) {
                    internalImportsDao.saveInternalImport(javaClass, javaAbstractImport);
                }
                //System.gc();
                for (JavaMethod javaMethod : javaClass.getMethods()) {
                    methodInvocationDao.saveMethodInvocations(javaMethod, javaClass);
                    javaExternalAttributeAccessDao.saveJavaExternalAttributeAccess(javaMethod);
                }
            }
            //terminatedDao.save(projectName, revisionId);

        }
        javaProject.setRevisionId(revisionId);
        System.out.println("Vai setar changing methods");
        javaProject.setChangingMethodsAndClasses();
        System.out.println("Setou changing methods");
        javaProject.setClassesMetrics();
        System.out.println("Vai setar classes metrics");

        //trocar para não terminado
        if (!terminatedDao.isTerminated(projectName, revisionId)) {
            //colocar so design flaws

            //System.gc();
            List<JavaPackage> godPackages = getGodPackage(javaProject);
            for (JavaPackage javaPackge : godPackages) {
                System.out.println("GOD PACKAGE");
                anomalieDao.save(Constants.ANOMALIE_GOD_PACKAGE, javaPackge.getName(), revisionId);
            }
            //System.gc();
            List<JavaClass> godClasses = getGodClass(javaProject);
            for (JavaClass javaClass : godClasses) {
                System.out.println("GOD CLASS");
                anomalieDao.save(Constants.ANOMALIE_GOD_CLASS, javaClass.getFullQualifiedName(), revisionId);
            }
            //System.gc();
            List<JavaClass> misplacedClasses = getMisplacedClass(javaProject);
            for (JavaClass javaClass : misplacedClasses) {
                System.out.println("MISPLACED CLASS");
                anomalieDao.save(Constants.ANOMALIE_MISPLACED_CLASS, javaClass.getFullQualifiedName(), revisionId);
            }
            //System.gc();
            for (JavaAbstract javaAbstract : javaProject.getClasses()) {
                if (javaAbstract.getClass() == JavaClass.class) {
                    JavaClass jc = (JavaClass) javaAbstract;
                    List<JavaMethod> featureEnvyMethods = getFeatureEnvy(jc);
                    for (JavaMethod javaMethod : featureEnvyMethods) {
                        System.out.println("FEATURE ENVY");
                        anomalieDao.save(Constants.ANOMALIE_FEATURE_ENVY, javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), revisionId);
                    }
                    //System.gc();
                    List<JavaMethod> shotgunSurgeryMethods = getShotgunSurgery(jc);
                    for (JavaMethod javaMethod : shotgunSurgeryMethods) {
                        System.out.println("SHOTGUN SURGERY");
                        anomalieDao.save(Constants.ANOMALIE_SHOTGUN_SURGERY, javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), revisionId);
                    }
                    //System.gc();
                    List<JavaMethod> godMethods = getGodMethod(jc);
                    for (JavaMethod javaMethod : godMethods) {
                        System.out.println("GOD METHOD");
                        anomalieDao.save(Constants.ANOMALIE_GOD_METHOD, javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature(), revisionId);
                    }
                    //System.gc();
                }
            }

            //**************** terminou so design flwas
            //adicionar depois
            terminatedDao.save(projectName, revisionId);
            System.out.println("Salvou a revisão: " + revisionId);
        }

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
            System.out.println("Numero de classes: " + pathList.size());
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

        int i = 0;
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
                        ((JavaInterface) javaInterface).addClassesThatImplements((JavaClass) javaAbstract);
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

            i++;
            System.out.println("Get java attributes: " + i+"   for java class: "+javaAbstract.getFullQualifiedName());

            //get list of methods
            List<JavaMethodAstBox> list = astService.getMethods(javaAbstract.getPath());
            System.out.println("Get java methods: " + i);
            setJavaMethods(javaAbstract, list, importList, javaProject, false);
            //System.gc();

        }

        System.out.println("Get all java methods invocations");
        i = 0;
        //get calls of the methods
        for (JavaAbstract javaAbstract : javaProject.getAllClasses()) {
            //System.gc();
            if (javaAbstract.getClass() == JavaClass.class) {

                astService.getMethodInvocations((JavaClass) javaAbstract, javaProject);
                System.out.println("Get method invocation");
                //System.out.println("Calculando métricas de acesso de dados");
                astService.getAccessDataMetrics((JavaClass) javaAbstract, javaProject);
                System.out.println("Get data metric");
                JavaClass jc = (JavaClass) javaAbstract;
                for (JavaMethod jm : jc.getMethods()) {
                    jm.removeBlock();
                }
                //System.gc();
                i++;
                System.out.println("Method invocations: " + i);
            }

        }

        System.out.println("Get all methods java assigments");
        //get the assignments
        for (JavaAbstract javaAbstract : javaProject.getAllClasses()) {
            //System.gc();
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

    public JavaProject createProjectsOffMemory(List<String> codeDirs, String path, String revisionId, ClassesDao classesDao, InterfaceDao interfaceDao, JavaMethodDao javaMethodDao, String projectName) {
        long inicio = System.currentTimeMillis();
        AstService astService = new AstService();
        JavaProject javaProject = new JavaProject(path);
        if (codeDirs.isEmpty()) {
            codeDirs.add(path);
        }
        JavaProjectFileService javaProjectFileService = new JavaProjectFileService(projectName);
        for (String codeDir : codeDirs) {
            System.out.println("CodeDir: " + codeDir);
            List<String> pathList = astService.getAllJavaClassesPath(codeDir);
            System.out.println("Numero de classes: " + pathList.size());
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
                    //criar arquivo
                    if (isInterface) {
                        javaProjectFileService.createJavaInterface(className, packageName, path);
                    } else {
                        javaProjectFileService.createJavaClass(className, packageName, path);
                    }
                }
            }
        }

        //complete the java abstract with imports, attributes, implements, superclasses and methods
        //1 nome do path
        //2 imports of classes
        //3 superclass
        //4 implemented interface
        //path da classe
        for (JavaAbstract javaAbstract : javaProject.getAllClasses()) {
            //get import classes of classes
            List<String> listOfImportClasses = new LinkedList();
            List<String> importList = astService.getImports(javaAbstract.getPath());
            for (String packageImport : importList) {
                List<JavaAbstract> javaAbstractList = javaProject.getPackagesByName(packageImport);
//                javaAbstract.addImportClasses(javaAbstractList);
//                
//                if (javaAbstractList.isEmpty()) {
//                    javaAbstract.addImportClasses(javaAbstractList);
//                }
                //adicionar os imports para uma lista
                for (JavaAbstract ja : javaAbstractList) {
                    listOfImportClasses.add(ja.getFullQualifiedName());
                }

            }

            if (javaAbstract.getClass() == JavaClass.class) {
                //adicionar os imports
                javaProjectFileService.addImportListToClass(javaAbstract.getName(), javaAbstract.getJavaPackage().getName(), listOfImportClasses);
                //get superclass
                String superClassString = astService.getSuperClass(javaAbstract.getPath());
                if (superClassString != null) {
                    JavaClass superClass = (JavaClass) javaAbstract.getJavaAbstractImportByName(superClassString);
                    //((JavaClass) javaAbstract).setSuperClass(superClass);
                    javaProjectFileService.setSuperClass(javaAbstract.getName(), javaAbstract.getJavaPackage().getName(), superClass.getFullQualifiedName());
                }
                //get all implemented interfaces of internal interfaces
                List<String> implementedInterfacesNames = astService.getImplementedInterfaces(javaAbstract.getPath());
                List<String> interfacesList = new LinkedList();
                for (String implementedInterface : implementedInterfacesNames) {
                    //System.out.println("implemented interface ("+javaAbstract.getFullQualifiedName()+"): "+implementedInterface);
                    JavaAbstract javaInterface = javaAbstract.getJavaAbstractImportByName(implementedInterface);
                    if (javaInterface != null && javaInterface.getClass() == JavaInterface.class) {
                        //((JavaClass) javaAbstract).addImplementedInterface((JavaInterface) javaInterface);
                        interfacesList.add(((JavaInterface) javaInterface).getFullQualifiedName());
                        //((JavaInterface) javaInterface).addClassesThatImplements((JavaClass) javaAbstract);
                    }
                }
                //adicionar interfaces
                javaProjectFileService.addImplementedInterfaces(javaAbstract.getName(), javaAbstract.getJavaPackage().getName(), interfacesList);

                //get attributes
                List<ParameterAst> attributes = astService.getAttributes(javaAbstract.getPath());
                //List<String> attributesList = new LinkedList();

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
            } else {
                //adicionar os imports
                javaProjectFileService.addImportListToInterface(javaAbstract.getName(), javaAbstract.getJavaPackage().getName(), listOfImportClasses);
            }

            //get list of methods
            List<JavaMethodAstBox> list = astService.getMethods(javaAbstract.getPath());
            setJavaMethodsOffMemory(javaAbstract, list, importList, javaProject, false, javaMethodDao, javaProjectFileService);
            //System.gc();

        }

        //get calls of the methods
        for (JavaAbstract javaAbstract : javaProject.getAllClasses()) {
            //System.gc();
            if (javaAbstract.getClass() == JavaClass.class) {
                astService.getMethodInvocations((JavaClass) javaAbstract, javaProject);
                //System.out.println("Calculando métricas de acesso de dados");
                astService.getAccessDataMetricsOffMemory((JavaClass) javaAbstract, javaProject);
            }
        }

        //get the assignments
        for (JavaAbstract javaAbstract : javaProject.getAllClasses()) {
            //System.gc();
            if (javaAbstract.getClass() == JavaClass.class) {
                astService.setAttributeModificationMethodOffMemory((JavaClass) javaAbstract, javaProject);
            }
        }

        long fim = System.currentTimeMillis();
        System.out.println("Tempo para gerar: " + (fim - inicio));

        setProjectProperties(javaProject);

        return javaProject;

    }

    public void setProjectProperties(JavaProject javaProject) {
        //pegando as classes lideres        
        //pegando tambem as classes inteligentes, burras e parcialmente inteligentes
        int numberOfClasses = 0;
        int numberOfInterfaces = 0;
        List<JavaAbstract> classes = javaProject.getAllClasses();
        for (JavaAbstract javaClazz : classes) {
            //System.gc();
            if (javaClazz.getClass() == JavaClass.class) {
                numberOfClasses++;
                List<JavaClass> classesThatCall = javaProject.getClassesThatCall(javaClazz);
                List<JavaAbstract> classesThatUsing = javaProject.getClassesThatUsing(javaClazz);
                if (classesThatCall.isEmpty() && classesThatUsing.isEmpty()) {
                    List<JavaInterface> implementedInterfaces = ((JavaClass) javaClazz).getImplementedInterfaces();
                    for (JavaInterface implementedInterface : implementedInterfaces) {
                        //System.gc();
                        classesThatCall.addAll(javaProject.getClassesThatCall(implementedInterface));
                        classesThatUsing.addAll(javaProject.getClassesThatUsing(implementedInterface));
                    }
                    JavaClass JavaSuperClazz = ((JavaClass) javaClazz).getSuperClass();
                    while (JavaSuperClazz != null) {
                        //System.gc();
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
                    //System.gc();
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

            JavaMethod javaMethod = new JavaMethod(javaMethodAstBox.getName(), null, javaDataReturnType, javaMethodAstBox.isFinal(), javaMethodAstBox.isStatic(),
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

    private void setJavaMethodsOffMemory(JavaAbstract javaAbstract, List<JavaMethodAstBox> list, List<String> importList, JavaProject javaProject, boolean fromXML, JavaMethodDao javaMethodDao, JavaProjectFileService javaProjectFileService) {
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

            //JavaMethod javaMethod = new JavaMethod(javaMethodAstBox.getName(), null, javaDataReturnType, javaMethodAstBox.isFinal(), javaMethodAstBox.isStatic(),
            //        javaMethodAstBox.isAbstract(), javaMethodAstBox.isSynchronized(), javaMethodAstBox.isPrivate(), javaMethodAstBox.isPublic(), javaMethodAstBox.isProtected(), javaMethodAstBox.getCyclomaticComplexity(), javaMethodAstBox.getBlock());
            List<String> parameters = new LinkedList();
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

                //javaMethod.addParameter(parameter);
                parameters.add(parameter.getType().getClass() == JavaClass.class || parameter.getType().getClass() == JavaInterface.class ? (((JavaAbstract) parameter.getType()).getFullQualifiedName()) : parameter.getType().getName());

            }
//            javaMethod.setJavaAbstract(javaAbstract);
//            if (javaAbstract.getClass() == JavaClass.class) {
//                ((JavaClass) javaAbstract).addMethod(javaMethod);
//                if (fromXML) {
//                    javaMethod.setInternalID(javaMethodAstBox.getMethodInternalId());
//                }
//            } else if (javaAbstract.getClass() == JavaInterface.class) {
//                ((JavaInterface) javaAbstract).addJavaMethod(javaMethod);
//                if (fromXML) {
//                    javaMethod.setInternalID(javaMethodAstBox.getMethodInternalId());
//                }
//            }
            //criando assinatura de metodo
            String methodSignature = javaMethodAstBox.getName() + "(";
            if (!parameters.isEmpty()) {
                methodSignature = methodSignature + parameters.get(0);
                for (int i = 1; i < parameters.size(); i++) {
                    methodSignature = methodSignature + "," + parameters.get(i);
                }
            }
            methodSignature = methodSignature + ")";
            String returnTypeStr = javaDataReturnType.getClass() == JavaClass.class || javaDataReturnType.getClass() == JavaInterface.class ? (((JavaAbstract) javaDataReturnType).getFullQualifiedName()) : javaDataReturnType.getName();
            if (javaAbstract.getClass() == JavaClass.class) {
                javaProjectFileService.createJavaMethodFromClass(javaAbstract.getName(), javaAbstract.getJavaPackage().getName(), methodSignature, returnTypeStr,
                        javaMethodAstBox.isFinal(), javaMethodAstBox.isStatic(),
                        javaMethodAstBox.isAbstract(), javaMethodAstBox.isSynchronized(), javaMethodAstBox.isPrivate(), javaMethodAstBox.isPublic(), javaMethodAstBox.isProtected(), javaMethodAstBox.getCyclomaticComplexity(), javaMethodAstBox.getBlock().toString());
            } else {
                javaProjectFileService.createJavaMethodFromInterface(javaAbstract.getName(), javaAbstract.getJavaPackage().getName(), methodSignature, returnTypeStr,
                        javaMethodAstBox.isFinal(), javaMethodAstBox.isStatic(),
                        javaMethodAstBox.isAbstract(), javaMethodAstBox.isSynchronized(), javaMethodAstBox.isPrivate(), javaMethodAstBox.isPublic(), javaMethodAstBox.isProtected());
            }

        }
    }

    public List<JavaMethod> getFeatureEnvy(JavaClass jc) {
        //feature envy
        List<JavaMethod> featureEnvyList = new LinkedList();
        if (!jc.getMethods().isEmpty()) {
            List<JavaMethod> topValuesMethods = new LinkedList();
            List<JavaMethod> auxList = new LinkedList();

            auxList.add(jc.getMethods().get(0));
            for (int i = 1; i < jc.getMethods().size(); i++) {
                JavaMethod javaMethod = jc.getMethods().get(i);
                boolean inseriu = false;
                for (int j = 0; j < auxList.size(); j++) {
                    JavaMethod jm2 = auxList.get(j);
                    if (javaMethod.getAccessToForeignDataNumber() > jm2.getAccessToForeignDataNumber()) {
                        auxList.add(j, javaMethod);
                        inseriu = true;
                        break;
                    }
                }
                if (!inseriu) {
                    auxList.add(javaMethod);
                }
            }
            int topNumber = jc.getMethods().size() / 10;
            if (topNumber * 10 != jc.getMethods().size()) {
                topNumber++;
            }
            for (int i = 0; i < topNumber; i++) {
                topValuesMethods.add(auxList.get(i));
            }

            for (JavaMethod javaMethod : topValuesMethods) {
                if ((javaMethod.getAccessToForeignDataNumber() > 4)
                        && (javaMethod.getAccessToLocalDataNumber() < 3)
                        && (javaMethod.getForeignDataProviderNumber() < 3)) {
                    featureEnvyList.add(javaMethod);
                }
            }

        }

        return featureEnvyList;
    }

    public List<JavaMethod> getShotgunSurgery(JavaClass jc) {
        //feature envy
        List<JavaMethod> shotgunList = new LinkedList();
        if (!jc.getMethods().isEmpty()) {

            for (JavaMethod jm : jc.getMethods()) {
                //System.out.println("VERIFICAR CC CM ########## "+jc.getFullQualifiedName()+":"+jm.getMethodSignature()+"  CC: "+jm.getChangingClassesMetric()+"    CM: "+jm.getChangingMethodsMetric()+"    code: "+jm);
                if (jm.getChangingMethodsMetric() > 7 && jm.getChangingClassesMetric() > 5) {
                    shotgunList.add(jm);
                }
            }

        }

        return shotgunList;
    }

    public List<JavaMethod> getGodMethod(JavaClass jc) {
        //feature envy
        List<JavaMethod> godMethodList = new LinkedList();
        if (!jc.getMethods().isEmpty()) {
            List<JavaMethod> topValuesMethods = new LinkedList();
            List<JavaMethod> auxList = new LinkedList();
            auxList.add(jc.getMethods().get(0));
            for (int i = 1; i < jc.getMethods().size(); i++) {
                JavaMethod javaMethod = jc.getMethods().get(i);
                boolean inseriu = false;
                for (int j = 0; j < auxList.size(); j++) {
                    JavaMethod jm2 = auxList.get(j);
                    if (javaMethod.getNumberOfLines() > jm2.getNumberOfLines()) {
                        auxList.add(j, javaMethod);
                        inseriu = true;
                        break;
                    }
                }
                if (!inseriu) {
                    auxList.add(javaMethod);
                }
            }
            int topNumber = jc.getMethods().size() / 5;
            if (topNumber * 5 != jc.getMethods().size()) {
                topNumber++;
            }
            for (int i = 0; i < topNumber; i++) {
                topValuesMethods.add(auxList.get(i));
            }

            for (JavaMethod javaMethod : topValuesMethods) {
                if ((javaMethod.getNumberOfLines() >= 70)
                        && (javaMethod.getParameters().size() > 4 || javaMethod.getNumberOfLocalVariables() > 4)
                        && (javaMethod.getCyclomaticComplexity() > 4)) {
                    godMethodList.add(javaMethod);
                }
            }

        }
        return godMethodList;
    }

    public List<JavaClass> getGodClass(JavaProject jp) {
        List<JavaClass> godClassList = new LinkedList();
        if (!jp.getClasses().isEmpty()) {
            List<JavaClass> topValuesClasses = new LinkedList();
            List<JavaClass> auxList = new LinkedList();

            auxList.add((JavaClass) jp.getClasses().get(0));
            for (int i = 1; i < jp.getClasses().size(); i++) {
                JavaClass javaClass = (JavaClass) jp.getClasses().get(i);
                boolean inseriu = false;
                for (int j = 0; j < auxList.size(); j++) {
                    JavaClass jc2 = auxList.get(j);
                    if (javaClass.getAccessToForeignDataNumber() > jc2.getAccessToForeignDataNumber()) {
                        auxList.add(j, javaClass);
                        inseriu = true;
                        break;
                    }
                }
                if (!inseriu) {
                    auxList.add(javaClass);
                }
            }
            int topNumber = jp.getClasses().size() / 5;
            if (topNumber * 5 != jp.getClasses().size()) {
                topNumber++;
            }
            for (int i = 0; i < topNumber; i++) {
                topValuesClasses.add(auxList.get(i));
            }

            for (JavaClass javaClass : topValuesClasses) {
                double tcc = javaClass.getNumberOfDirectConnections();
                int n = javaClass.getMethods().size();
                tcc = tcc / ((n * (n - 1)) / 2);
                if ((javaClass.getAccessToForeignDataNumber() > 4)
                        && (javaClass.getTotalCyclomaticComplexity() > 20)
                        && (tcc < 0.33)) {
                    godClassList.add(javaClass);
                }
            }

        }
        return godClassList;
    }

    public List<JavaPackage> getGodPackage(JavaProject jp) {
        List<JavaPackage> godPackageList = new LinkedList();
        if (!jp.getPackages().isEmpty()) {
            List<JavaPackage> topValuesPackages = new LinkedList();
            List<JavaPackage> auxList = new LinkedList();

            auxList.add(jp.getPackages().get(0));
            for (int i = 1; i < jp.getPackages().size(); i++) {
                JavaPackage javaPackage = jp.getPackages().get(i);
                boolean inseriu = false;
                for (int j = 0; j < auxList.size(); j++) {
                    JavaPackage jp2 = auxList.get(j);
                    if (javaPackage.getOnlyClasses().size() > jp2.getOnlyClasses().size()) {
                        auxList.add(j, javaPackage);
                        inseriu = true;
                        break;
                    }
                }
                if (!inseriu) {
                    auxList.add(javaPackage);
                }
            }
            int topNumber = jp.getPackages().size() / 4;
            if (topNumber * 4 != jp.getPackages().size()) {
                topNumber++;
            }
            for (int i = 0; i < topNumber; i++) {
                topValuesPackages.add(auxList.get(i));
            }

            for (JavaPackage javaPackage : topValuesPackages) {
                double packageCohesion = javaPackage.getPackageCohesion();

                if ((javaPackage.getOnlyClasses().size() > 20)
                        && (javaPackage.getClientClasses().size() > 20)
                        && (javaPackage.getClientPackages().size() > 3)) {
                    godPackageList.add(javaPackage);
                }
            }

        }
        return godPackageList;
    }

    public List<JavaClass> getMisplacedClass(JavaProject jp) {
        List<JavaClass> misplacedClassList = new LinkedList();
        if (!jp.getClasses().isEmpty()) {
            List<JavaClass> topValuesClasses = new LinkedList();
            List<JavaClass> auxList = new LinkedList();
            auxList.add((JavaClass) jp.getClasses().get(0));
            for (int i = 1; i < jp.getClasses().size(); i++) {
                JavaClass javaClass = (JavaClass) jp.getClasses().get(i);
                boolean inseriu = false;
                for (int j = 0; j < auxList.size(); j++) {
                    JavaClass jc2 = auxList.get(j);
                    if (javaClass.getExternalDependencyClasses().size() > jc2.getExternalDependencyClasses().size()) {
                        auxList.add(j, javaClass);
                        inseriu = true;
                        break;
                    }
                }
                if (!inseriu) {
                    auxList.add(javaClass);
                }
            }
            int topNumber = jp.getClasses().size() / 4;
            if (topNumber * 4 != jp.getClasses().size()) {
                topNumber++;
            }
            for (int i = 0; i < topNumber; i++) {
                topValuesClasses.add(auxList.get(i));
            }

            for (JavaClass javaClass : topValuesClasses) {
                double classLocality = javaClass.getInternalDependencyClasses().size();
                classLocality = classLocality / (javaClass.getInternalDependencyClasses().size() + javaClass.getExternalDependencyClasses().size());
                if ((javaClass.getExternalDependencyClasses().size() > 6)
                        && (javaClass.getExternalDependencyPackages().size() < 3)
                        && (classLocality < 0.33)) {
                    misplacedClassList.add(javaClass);
                }
            }

        }
        return misplacedClassList;
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
