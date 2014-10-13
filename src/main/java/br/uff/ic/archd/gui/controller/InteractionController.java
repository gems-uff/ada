/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.gui.controller;

import br.uff.ic.archd.git.service.ProjectRevisionsService;
import br.uff.ic.archd.gui.view.InteractionViewer;
import br.uff.ic.archd.javacode.JavaAbstract;
import br.uff.ic.archd.javacode.JavaAbstractExternal;
import br.uff.ic.archd.javacode.JavaAttribute;
import br.uff.ic.archd.javacode.JavaClass;
import br.uff.ic.archd.javacode.JavaConstructorService;
import br.uff.ic.archd.javacode.JavaData;
import br.uff.ic.archd.javacode.JavaInterface;
import br.uff.ic.archd.javacode.JavaMethod;
import br.uff.ic.archd.javacode.JavaMethodInvocation;
import br.uff.ic.archd.javacode.JavaPrimitiveType;
import br.uff.ic.archd.javacode.JavaProject;
import br.uff.ic.archd.model.Project;
import br.uff.ic.dyevc.application.branchhistory.model.BranchRevisions;
import br.uff.ic.dyevc.application.branchhistory.model.LineRevisions;
import br.uff.ic.dyevc.application.branchhistory.model.ProjectRevisions;
import br.uff.ic.dyevc.application.branchhistory.model.Revision;
import br.uff.ic.dyevc.application.branchhistory.model.RevisionsBucket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author wallace
 */
public class InteractionController implements ActionListener {

    private InteractionViewer interactionViewer;
    private JavaConstructorService javaConstructorService;
    private ProjectRevisionsService projectRevisionsService;
    private JavaProject javaProject;
    private ProjectRevisions projectRevisions;
    List<JavaProject> javaProjects;
    private Project project;

    InteractionController(Project project) {
        projectRevisionsService = new ProjectRevisionsService();
        javaConstructorService = new JavaConstructorService();
        ProjectRevisions newProjectRevisions = null;
        try {
            projectRevisions = projectRevisionsService.getProject(project.getPath(), project.getName());
            System.out.println("Vai limpar");
            newProjectRevisions = cleanProjectRevisionsLast(projectRevisions);
            System.out.println("Limpou");
            System.out.println("Number of Revisions: " + newProjectRevisions.getRevisionsBucket().getRevisionCollection().size());
            System.out.println("Branches: " + newProjectRevisions.getBranchesRevisions().size());
            System.out.println("Ultima revisão: " + newProjectRevisions.getBranchesRevisions().get(0).getHead().getId() + "    next: " + newProjectRevisions.getBranchesRevisions().get(0).getHead().getNext().size()
                    + "     prev: " + newProjectRevisions.getBranchesRevisions().get(0).getHead().getPrev().size());
            System.out.println("penultima revisão: " + newProjectRevisions.getBranchesRevisions().get(0).getHead().getPrev().get(0).getId());
        } catch (Exception e) {
            System.out.println("Erro Revisions: " + e.getMessage());
        }



        //javaProject = javaConstructorService.createProjects(project.getCodeDirs(), project.getPath());
        /*List<String> newCodeDirs = new LinkedList();
         for(String codeDir :  project.getCodeDirs()){
         String newCodeDir = codeDir.substring(project.getPath().length(),codeDir.length());
         if(newCodeDir.startsWith("/")){
         newCodeDir = newCodeDir.substring(1);
         }
         System.out.println("Code Dir: "+newCodeDir);
         newCodeDirs.add(newCodeDir);
         }*/
        //javaProject = javaConstructorService.getProjectByRevision(project.getName(), project.getCodeDirs(), project.getPath(), "revisionteste");
        javaProjects = javaConstructorService.getAllProjectsRevision(project.getName(), project.getCodeDirs(), project.getPath(), newProjectRevisions);
        javaProject = javaProjects.get(javaProjects.size() - 1);
        System.out.println("Revision do projeto: " + javaProject.getRevisionId());


        this.project = project;
        //javaProject = javaConstructorService.createProjectsFromXML("/home/wallace/.archd/HISTORY/1/");
        String classesString[] = new String[javaProject.getClasses().size()];
        for (int i = 0; i < javaProject.getClasses().size(); i++) {
            classesString[i] = javaProject.getClasses().get(i).getFullQualifiedName();
        }
        String InterfacesString[] = new String[javaProject.getInterfaces().size()];
        for (int i = 0; i < javaProject.getInterfaces().size(); i++) {
            InterfacesString[i] = javaProject.getInterfaces().get(i).getFullQualifiedName();
        }

        interactionViewer = new InteractionViewer(classesString, InterfacesString);
        interactionViewer.setController(this);


        interactionViewer.appendDadosText("******************* Número total de classes: " + classesString.length);
        interactionViewer.appendDadosText("******************* Número total de interfaces: " + InterfacesString.length);
        interactionViewer.appendDadosText("******************* Número total de classes externas chamadas diretamente: " + javaProject.getNumberOfViewExternalClasses());
        interactionViewer.appendDadosText("");
        interactionViewer.appendDadosText("************ Classes Mestres ************** numero: " + javaProject.getLeaderClasses().size() + "");
        for (JavaAbstract javaClazz : javaProject.getLeaderClasses()) {
            interactionViewer.appendDadosText(javaClazz.getFullQualifiedName());
        }

        interactionViewer.appendDadosText("");
        interactionViewer.appendDadosText("");
        interactionViewer.appendDadosText("************ Classes Possivelmente Mestres ************** numero: " + javaProject.getPossibleLeaderClasses().size() + "");
        for (JavaAbstract javaClazz : javaProject.getPossibleLeaderClasses()) {
            interactionViewer.appendDadosText(javaClazz.getFullQualifiedName());
        }

        interactionViewer.appendDadosText("");
        interactionViewer.appendDadosText("");
        interactionViewer.appendDadosText("");
        interactionViewer.appendDadosText("");
        interactionViewer.appendDadosText("");
        interactionViewer.appendDadosText("");
        interactionViewer.appendDadosText("************ Classes Inteligentes ************** numero: " + javaProject.getSimpleSmartClasses().size() + "");
        for (JavaClass javaClazz : javaProject.getSimpleSmartClasses()) {
            interactionViewer.appendDadosText(javaClazz.getFullQualifiedName());
        }

        interactionViewer.appendDadosText("");
        interactionViewer.appendDadosText("");
        interactionViewer.appendDadosText("************ Classes Completamente Inteligentes ************** numero: " + javaProject.getFullSmartClasses().size() + "");
        for (JavaClass javaClazz : javaProject.getFullSmartClasses()) {
            interactionViewer.appendDadosText(javaClazz.getFullQualifiedName());
        }

        interactionViewer.appendDadosText("");
        interactionViewer.appendDadosText("");
        interactionViewer.appendDadosText("************ Classes Ingenuas ************** numero: " + javaProject.getFoolClasses().size() + "");
        for (JavaClass javaClazz : javaProject.getFoolClasses()) {
            interactionViewer.appendDadosText(javaClazz.getFullQualifiedName());
        }



        System.out.println("\n\n\n\n\n\n\n\n\n\n\n***************************8 ESTATISTICA ***************************");
        int count = 0;

        /*for(int i=0; i < javaProjects.size(); i++){
         JavaProject jp = javaProjects.get(i);
         boolean flag = false;
         for(JavaAbstract javaAbstract : jp.getClasses()){
         boolean flag2 = false;
         JavaClass jc = (JavaClass) javaAbstract;
         for(JavaMethod jm : jc.getMethods()){
         if(jm.getCyclomaticComplexity() > 10){
         count++;
         if(!flag){
         flag = true;
         System.out.println("\n\n*********** REVISION: "+jp.getRevisionId()+"\n");
         }
         if(!flag2){
         flag2 = true;
         System.out.println("\n************** CLASS: "+jc.getFullQualifiedName()+"\n");
         }
         System.out.println("Método: "+jm.getMethodSignature()+"     Complexity: "+jm.getCyclomaticComplexity()+"         size: "+jm.getSizeInChars());
         }
         }
         }
         }*/






        Revision rev = newProjectRevisions.getRoot();
        int revs = 0;
        int k = 0;
        while (rev != null) {
            //JavaProject jp = javaProjects.get(i);
            JavaProject jp = null;
            //System.out.println("REV ID: "+rev.getId());
            for (int i = 0; i < javaProjects.size(); i++) {
                //System.out.println("RevisonsID: "+javaProjects.get(i).getRevisionId());
                if (javaProjects.get(i).getRevisionId().equals(rev.getId())) {
                    jp = javaProjects.get(i);
                    break;
                }
            }
            k++;
            boolean flag = false;
            for (JavaAbstract javaAbstract : jp.getClasses()) {
                boolean flag2 = false;
                JavaClass jc = (JavaClass) javaAbstract;
                for (JavaMethod jm : jc.getMethods()) {
                    if (jm.getCyclomaticComplexity() > 20) {
                        count++;
                        if (!flag) {
                            flag = true;
                            System.out.println("\n\n*********** REVISION: " + jp.getRevisionId() + "      num: " + k + "\n");
                        }
                        if (!flag2) {
                            flag2 = true;
                            System.out.println("\n************** CLASS: " + jc.getFullQualifiedName() + "\n");
                        }
                        System.out.println("Método: " + jm.getMethodSignature() + "     Complexity: " + jm.getCyclomaticComplexity() + "         size: " + jm.getSizeInChars());
                    }
                }
            }
            if (rev.getNext().size() == 0) {
                rev = null;
            } else {
                rev = rev.getNext().get(0);
            }
            revs++;
        }


        System.out.println("REVISOES VISTAS: " + revs);
        System.out.println("COUNT: " + count);


        rev = newProjectRevisions.getRoot();
        revs = 0;
        k = 0;
        while (rev != null) {
            //JavaProject jp = javaProjects.get(i);
            JavaProject jp = null;
            //System.out.println("REV ID: "+rev.getId());
            for (int i = 0; i < javaProjects.size(); i++) {
                //System.out.println("RevisonsID: "+javaProjects.get(i).getRevisionId());
                if (javaProjects.get(i).getRevisionId().equals(rev.getId())) {
                    jp = javaProjects.get(i);
                    break;
                }
            }
            k++;
            boolean flag = false;
            int methodCont = 0;
            int mc = 0;
            for (JavaAbstract javaAbstract : jp.getClasses()) {
                boolean flag2 = false;
                JavaClass jc = (JavaClass) javaAbstract;

                for (JavaMethod jm : jc.getMethods()) {

                    if (jm.getCyclomaticComplexity() > 20) {
                        count++;
                        mc++;
                        //System.out.println("Método: "+jm.getMethodSignature()+"     Complexity: "+jm.getCyclomaticComplexity()+"         size: "+jm.getSizeInChars());
                    }
                    methodCont++;
                }

            }
            System.out.println("\n\n*********** REVISION: " + jp.getRevisionId() + "      num: " + k + "");
            System.out.println("Methods: " + methodCont + "     metodos complexos: " + mc);
            if (rev.getNext().size() == 0) {
                rev = null;
            } else {
                rev = rev.getNext().get(0);
            }
            revs++;
        }

        System.out.println("***************************************************************************8");


        rev = newProjectRevisions.getRoot();
        revs = 0;
        k = 0;
        while (rev != null) {
            //JavaProject jp = javaProjects.get(i);
            JavaProject jp = null;
            //System.out.println("REV ID: "+rev.getId());
            for (int i = 0; i < javaProjects.size(); i++) {
                //System.out.println("RevisonsID: "+javaProjects.get(i).getRevisionId());
                if (javaProjects.get(i).getRevisionId().equals(rev.getId())) {
                    jp = javaProjects.get(i);
                    break;
                }
            }
            k++;
            boolean flag = false;
            int methodCont = 0;
            int mc = 0;
            for (JavaAbstract javaAbstract : jp.getClasses()) {
                boolean flag2 = false;
                JavaClass jc = (JavaClass) javaAbstract;
                int ctc = jp.getClassesThatCall(javaAbstract).size();
                int ctu = jp.getClassesThatUsing(javaAbstract).size();
                if (ctc > 5 || ctu > 5) {
                    if (!flag) {
                        flag = true;
                        System.out.println("\n\n*********** REVISION: " + jp.getRevisionId() + "      num: " + k + "");
                    }
                    System.out.println("CLASS: " + jc.getFullQualifiedName() + "      Class using: " + ctu + "   class call: " + ctc);
                }


            }


            if (rev.getNext().size() == 0) {
                rev = null;
            } else {
                rev = rev.getNext().get(0);
            }
            revs++;
        }



        System.out.println("***************************************************************************8");


        rev = newProjectRevisions.getRoot();
        revs = 0;
        k = 0;
        JavaProject ant = null;
        while (rev != null) {
            //JavaProject jp = javaProjects.get(i);
            JavaProject jp = null;
            //System.out.println("REV ID: "+rev.getId());
            for (int i = 0; i < javaProjects.size(); i++) {
                //System.out.println("RevisonsID: "+javaProjects.get(i).getRevisionId());
                if (javaProjects.get(i).getRevisionId().equals(rev.getId())) {
                    jp = javaProjects.get(i);
                    break;
                }
            }
            k++;
            boolean flag = false;
            for (JavaAbstract javaAbstract : jp.getClasses()) {
                boolean flag2 = false;
                JavaClass jc = (JavaClass) javaAbstract;
                JavaClass antClass = null;
                if (ant != null) {
                    antClass = (JavaClass) ant.getClassByName(jc.getFullQualifiedName());
                }
                for (JavaMethod jm : jc.getMethods()) {
                    if (antClass != null) {
                        JavaMethod antMethod = antClass.getMethodBySignature(jm.getMethodSignature());
                        if (antMethod != null) {
                            if (jm.getCyclomaticComplexity() != antMethod.getCyclomaticComplexity()) {
                                if (!flag) {
                                    flag = true;
                                    System.out.println("\n\n*********** REVISION: " + jp.getRevisionId() + "      num: " + k + "\n");
                                }
                                if (!flag2) {
                                    flag2 = true;
                                    System.out.println("\n************** CLASS: " + jc.getFullQualifiedName() + "\n");
                                }
                                System.out.println("Método: " + jm.getMethodSignature() + "     Complexity: " + jm.getCyclomaticComplexity() + "         size: " + jm.getSizeInChars() +
                                        "     diff complexity: "+(jm.getCyclomaticComplexity() - antMethod.getCyclomaticComplexity())+"      diff size: "+(jm.getSizeInChars() - antMethod.getSizeInChars()));
                            }
                        }
                    }
                    
                }
            }
            if (rev.getNext().size() == 0) {
                rev = null;
            } else {
                rev = rev.getNext().get(0);
            }
            ant = jp;
            revs++;
        }



        interactionViewer.setVisible(true);
    }

    private ProjectRevisions cleanProjectRevisions(ProjectRevisions projectRevisions) {
        List<BranchRevisions> branches = new LinkedList();
        ProjectRevisions newProjectRevisions = new ProjectRevisions(projectRevisions.getName());
        RevisionsBucket revisionsBucket = new RevisionsBucket();
        Revision newRoot = revisionsBucket.getRevisionById(projectRevisions.getRoot().getId());
        if (newRoot == null) {
            newRoot = new Revision(projectRevisions.getRoot().getId());
            revisionsBucket.addRevision(newRoot);
        }
        int count = 0;
        newProjectRevisions.setRoot(newRoot);

        for (BranchRevisions branchRevisions : projectRevisions.getBranchesRevisions()) {
            Revision newHead = revisionsBucket.getRevisionById(branchRevisions.getHead().getId());
            if (newHead == null) {
                newHead = new Revision(branchRevisions.getHead().getId());
            }
            BranchRevisions newBranchRevisions = new BranchRevisions(branchRevisions.getName(), newHead);

            for (LineRevisions lineRevisions : branchRevisions.getLinesRevisions()) {
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
                while (aux != null) {
                    int i = 0;
                    while (i < 900 && aux.getPrev().size() != 0) {
                        aux = aux.getPrev().get(0);
                        i++;
                    }
                    newRevision = revisionsBucket.getRevisionById(aux.getId());
                    if (newRevision == null) {
                        newRevision = new Revision(aux.getId());
                        revisionsBucket.addRevision(newRevision);
                    }
                    newRevision.addNext(prox);
                    prox.addPrev(newRevision);
                    prox = newRevision;
                    newLineRevisions.addRevision(newRevision);

                    count++;
                    if (aux.getPrev().size() == 0) {
                        aux = null;
                    }
                }
                newBranchRevisions.addLineRevisions(lineRevisions);
            }
            branches.add(newBranchRevisions);

        }
        System.out.println("Count: " + count);
        newProjectRevisions.setBranchesRevisions(branches);
        newProjectRevisions.setRevisionsBucket(revisionsBucket);
        return newProjectRevisions;
    }

    private ProjectRevisions cleanProjectRevisionsLast(ProjectRevisions projectRevisions) {
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
            System.out.println("ID: " + newRevision.getId());
            int i = 0;
            while (i < 50 && aux != null) {
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
            /*for (LineRevisions lineRevisions : branchRevisions.getLinesRevisions()) {
             System.out.println("Linha numero: " + lineRevisions.getRevisions().size());
             LineRevisions newLineRevisions = new LineRevisions(newHead);
             Revision aux = lineRevisions.getHead();
             Revision newRevision = new Revision(aux.getId());
             Revision prox = newRevision;
             newLineRevisions.addRevision(newRevision);
             revisionsBucket.addRevision(newRevision);
             int i = 0;
             while (i < 5 && aux != null) {
             i++;
             //System.out.println("I: "+i);
             aux = aux.getPrev().get(0);
             newRevision = new Revision(aux.getId());
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
             }*/
            branches.add(newBranchRevisions);

        }
        System.out.println("Count: " + count);
        newProjectRevisions.setBranchesRevisions(branches);
        newProjectRevisions.setRevisionsBucket(revisionsBucket);
        return newProjectRevisions;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(InteractionViewer.ACTION_UPDATE_CLASS)) {
            showClassFunctions(interactionViewer.getClassSelected());
        } else if (e.getActionCommand().equals(InteractionViewer.ACTION_UPDATE_INTERFACE)) {
            showClassFunctions(interactionViewer.getInterfaceSelected());
        }
    }

    private void showClassFunctions(String className) {
        JavaAbstract javaAbstract = javaProject.getClassByName(className);
        String methods[] = null;
        String invocations[] = null;


        if (javaAbstract.getClass() == JavaClass.class) {
            System.out.println("\n\n ********** Classe: " + className);
            interactionViewer.appendText("\n\n ********** Classe: " + className);
            System.out.println("  Path: " + javaAbstract.getPath());
            interactionViewer.appendText("  Path: " + javaAbstract.getPath());
            if (((JavaClass) javaAbstract).getSuperClass() != null) {
                System.out.println("SuperClass: " + ((JavaClass) javaAbstract).getSuperClass().getFullQualifiedName());
                interactionViewer.appendText("SuperClass: " + ((JavaClass) javaAbstract).getSuperClass().getFullQualifiedName());
            }
            if (((JavaClass) javaAbstract).getImplementedInterfaces().size() > 0) {
                System.out.println("Impemented Interfaces: ");
                interactionViewer.appendText("Impemented Interfaces: ");
                for (JavaInterface javaInterface : ((JavaClass) javaAbstract).getImplementedInterfaces()) {
                    System.out.println("- " + javaInterface.getFullQualifiedName());
                    interactionViewer.appendText("- " + javaInterface.getFullQualifiedName());
                }
                System.out.println("");
                interactionViewer.appendText("");
            }
            if (((JavaClass) javaAbstract).getExternalImports().size() > 0) {
                System.out.println("External imports: ");
                interactionViewer.appendText("External imports: ");
                for (String externalImport : ((JavaClass) javaAbstract).getExternalImports()) {
                    System.out.println("- " + externalImport);
                    interactionViewer.appendText("- " + externalImport);
                }
                System.out.println("");
                interactionViewer.appendText("");
            }
            if (((JavaClass) javaAbstract).getAttributes().size() > 0) {
                System.out.println("Attributes: ");
                interactionViewer.appendText("Attributes: ");
                for (JavaAttribute javaAttribute : ((JavaClass) javaAbstract).getAttributes()) {
                    JavaData javaData = javaAttribute.getType();
                    if (javaData.getClass() == JavaClass.class || javaData.getClass() == JavaInterface.class) {
                        System.out.println("- " + ((JavaAbstract) javaAttribute.getType()).getFullQualifiedName() + "    " + javaAttribute.getName());
                        interactionViewer.appendText("- " + ((JavaAbstract) javaAttribute.getType()).getFullQualifiedName() + "    " + javaAttribute.getName());
                    } else if (javaData.getClass() == JavaPrimitiveType.class) {
                        System.out.println("- " + ((JavaPrimitiveType) javaAttribute.getType()).getName() + "    " + javaAttribute.getName());
                        interactionViewer.appendText("- " + ((JavaPrimitiveType) javaAttribute.getType()).getName() + "    " + javaAttribute.getName());
                    } else if (javaData.getClass() == JavaAbstractExternal.class) {
                        System.out.println("- " + ((JavaAbstractExternal) javaAttribute.getType()).getName() + "    " + javaAttribute.getName());
                        interactionViewer.appendText("- " + ((JavaAbstractExternal) javaAttribute.getType()).getName() + "    " + javaAttribute.getName());
                    }
                }
                System.out.println("");
                interactionViewer.appendText("");
            }
            methods = new String[((JavaClass) javaAbstract).getMethods().size()];
            System.out.println("Métodos: ");
            interactionViewer.appendText("Métodos: ");
            for (int i = 0; i < ((JavaClass) javaAbstract).getMethods().size(); i++) {
                JavaData returnType = ((JavaClass) javaAbstract).getMethods().get(i).getReturnType();
                String retType = returnType == null ? "vazio" : returnType.getName();
                System.out.println("- " + retType + "       " + ((JavaClass) javaAbstract).getMethods().get(i).getMethodSignature());
                System.out.println("Diff (in - out): " + ((JavaClass) javaAbstract).getMethods().get(i).getDiff());
                System.out.println("Modifie internal state: " + ((JavaClass) javaAbstract).getMethods().get(i).isChangeInternalState());
                System.out.println("Modifie internal state by call method: " + ((JavaClass) javaAbstract).getMethods().get(i).isChangeInternalState());
                System.out.println("Size: " + ((JavaClass) javaAbstract).getMethods().get(i).getSizeInChars());
                System.out.println("Cyclomatic complexity: " + ((JavaClass) javaAbstract).getMethods().get(i).getCyclomaticComplexity());
                interactionViewer.appendText("- " + retType + "       " + ((JavaClass) javaAbstract).getMethods().get(i).getMethodSignature());
                interactionViewer.appendText("Diff (in - out): " + ((JavaClass) javaAbstract).getMethods().get(i).getDiff());
                interactionViewer.appendText("Modifie internal state: " + ((JavaClass) javaAbstract).getMethods().get(i).isChangeInternalState());
                interactionViewer.appendText("Modifie internal state by call method: " + ((JavaClass) javaAbstract).getMethods().get(i).isChangeInternalState());
                interactionViewer.appendText("Size: " + ((JavaClass) javaAbstract).getMethods().get(i).getSizeInChars());
                interactionViewer.appendText("Cyclomatic complexity: " + ((JavaClass) javaAbstract).getMethods().get(i).getCyclomaticComplexity());
                methods[i] = ((JavaClass) javaAbstract).getMethods().get(i).getMethodSignature();
                for (JavaMethodInvocation jmi : ((JavaClass) javaAbstract).getMethods().get(i).getMethodInvocations()) {
                    //System.out.println("------ "+jmi.getJavaAbstract().getFullQualifiedName()+":"+jmi.getJavaMethod().getMethodSignature());
                    //System.out.println("JMI JAVA METHOD: "+jmi.getJavaAbstract().getFullQualifiedName());
                    if (jmi.getJavaMethod() != null) {
                        System.out.println("------ " + jmi.getJavaAbstract().getFullQualifiedName() + ":" + jmi.getJavaMethod().getMethodSignature());
                        interactionViewer.appendText("------ " + jmi.getJavaAbstract().getFullQualifiedName() + ":" + jmi.getJavaMethod().getMethodSignature());
                    } else {
                        System.out.println("----um " + jmi.getJavaAbstract().getFullQualifiedName() + ":" + jmi.getUnknowMethodName());
                        interactionViewer.appendText("----um " + jmi.getJavaAbstract().getFullQualifiedName() + ":" + jmi.getUnknowMethodName());

                    }
                }
                System.out.println("");
                interactionViewer.appendText("");
            }

            System.out.println("Classes that call the methods of this class: ");
            interactionViewer.appendText("Classes that call the methods of this class: ");
            for (JavaClass javaClass : javaProject.getClassesThatCall(javaAbstract)) {
                System.out.println("- " + javaClass.getFullQualifiedName());
                interactionViewer.appendText("- " + javaClass.getFullQualifiedName());
            }
            System.out.println("Classes uses this class: ");
            interactionViewer.appendText("Classes uses this class: ");
            for (JavaAbstract javaAbs : javaProject.getClassesThatUsing(javaAbstract)) {
                System.out.println("- " + javaAbs.getFullQualifiedName());
                interactionViewer.appendText("- " + javaAbs.getFullQualifiedName());
            }

//            for(int i = 0; i < ((JavaClass) javaAbstract).getMethods().size(); i++){
//                invocations[i] = ((JavaClass) javaAbstract).getMethods().get(i).getMethodInvocations();
//            }
        } else if (javaAbstract.getClass() == JavaInterface.class) {
            System.out.println("\n\n ********** Interface: " + className);
            interactionViewer.appendText("\n\n ********** Interface: " + className);
            System.out.println("  Path: " + javaAbstract.getPath());
            interactionViewer.appendText("  Path: " + javaAbstract.getPath());
            System.out.println("Métodos: ");
            interactionViewer.appendText("Métodos: ");
            methods = new String[((JavaInterface) javaAbstract).getMethods().size()];
            for (int i = 0; i < ((JavaInterface) javaAbstract).getMethods().size(); i++) {
                JavaData returnType = ((JavaInterface) javaAbstract).getMethods().get(i).getReturnType();
                String retType = returnType == null ? "void" : returnType.getName();
                System.out.println("- " + retType + "   " + ((JavaInterface) javaAbstract).getMethods().get(i).getMethodSignature());
                interactionViewer.appendText("- " + retType + "   " + ((JavaInterface) javaAbstract).getMethods().get(i).getMethodSignature());
                methods[i] = ((JavaInterface) javaAbstract).getMethods().get(i).getMethodSignature();
            }
        }


    }
//    public static void main(String args[]){
//        InteractionController interactionController = new InteractionController("/home/wallace/mestrado/projetos_alvos/neo4j/neo4j", "neo4j");
//        
//    }
}
