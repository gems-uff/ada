/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.gui.controller;

import br.uff.ic.archd.db.dao.DataBaseFactory;
import br.uff.ic.archd.git.service.JavaProjectsService;
import br.uff.ic.archd.git.service.ProjectRevisionsService;
import br.uff.ic.archd.javacode.JavaConstructorService;
import br.uff.ic.archd.javacode.JavaProject;
import br.uff.ic.archd.model.Project;
import br.uff.ic.dyevc.application.branchhistory.model.BranchRevisions;
import br.uff.ic.dyevc.application.branchhistory.model.LineRevisions;
import br.uff.ic.dyevc.application.branchhistory.model.ProjectRevisions;
import br.uff.ic.dyevc.application.branchhistory.model.Revision;
import br.uff.ic.dyevc.application.branchhistory.model.RevisionsBucket;
import br.uff.ic.dyevc.tools.vcs.git.GitConnector;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;

/**
 *
 * @author wallace
 */
public class CalculateMetrics {

    private String BRANCHES_HISTORY_PATH = System.getProperty("user.home") + "/.archd/BRANCHES_HISTORY/";

    public void calculateMetrics(Project project, int number) {
        try {
            JavaConstructorService javaContructorService = new JavaConstructorService();
            ProjectRevisionsService projectRevisionsService = new ProjectRevisionsService();
            ProjectRevisions projectRevisions = projectRevisionsService.getProject(project.getPath(), project.getName());
            System.out.println("ORIGINAL ROOT: " + projectRevisions.getRoot().getId());
            System.out.println("ORIGINAL HEAD: " + projectRevisions.getBranchesRevisions().get(0).getHead().getId());
            System.out.println("Vai limpar");
            DataBaseFactory.getInstance();
            ProjectRevisions newProjectRevisions = cleanProjectRevisionsLine(projectRevisions);
            Revision rev = newProjectRevisions.getRoot();
            int numberOfRevisions = 0;
            while (rev != null) {
                numberOfRevisions++;
                if (rev.getNext().size() == 0) {
                    rev = null;
                } else {
                    rev = rev.getNext().get(0);
                }
            }


            System.out.println("Number of Revisions: " + numberOfRevisions);
            rev = newProjectRevisions.getRoot();
            String antRev = null;
            int k = 0;
            while (k < number) {

                GitConnector gitConnector = null;
                Git git = null;
                CheckoutCommand checkoutCommand = null;
                System.gc();
                long tempoclone1 = System.currentTimeMillis();
                gitConnector = new GitConnector(BRANCHES_HISTORY_PATH + project.getName(), newProjectRevisions.getName());
                

                gitConnector = new GitConnector(BRANCHES_HISTORY_PATH + project.getName(), newProjectRevisions.getName());
                git = new Git(gitConnector.getRepository());


                checkoutCommand = git.checkout();
                checkoutCommand.setName(rev.getId());
                checkoutCommand.call();

                long tempoclone2 = System.currentTimeMillis();
                System.out.println("TEMPO PRA FAZER UM CLONE: " + (tempoclone2 - tempoclone1) + " milisegundos       :" + k);

                k++;

                if (rev.getNext().size() == 0) {
                    rev = null;
                } else {
                    rev = rev.getNext().get(0);
                }
            }
            System.out.println("Vai limpar a memoria");
            System.gc();
            System.out.println("Limpou a memoria");

            while (rev != null) {

                System.gc();
                JavaProject jp = null;
                jp = javaContructorService.getProjectByRevisionAndSetRevision(project.getName(), project.getCodeDirs(), project.getPath(), rev.getId(),  newProjectRevisions.getName());

                System.out.println("Calculou: " + k);
                k++;
                antRev = rev.getId();
                if (rev.getNext().size() == 0) {
                    rev = null;
                } else {
                    rev = rev.getNext().get(0);
                }
            }

        } catch (Exception e) {
            System.out.println("Rrro: " + e.getMessage());
        }
        System.out.println("TERMINOU");
    }

    private ProjectRevisions cleanProjectRevisionsLine(ProjectRevisions projectRevisions) {
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

    public static void main(String args[]) {
        JavaProjectsService javaprojectsService = new JavaProjectsService();
        List<Project> projects = javaprojectsService.getProjects();
        Project p = null;
        for (Project project : projects) {
            if (project.getName().equals("titan")) {
                p = project;
                break;
            }
        }
        if (p != null) {

            CalculateMetrics calculateMetrics = new CalculateMetrics();
            //calculateMetrics.calculateMetrics(p, 382);
            calculateMetrics.calculateMetrics(p, 0);

        }
    }
}
