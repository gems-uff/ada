/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.gui.controller;

import br.uff.ic.archd.git.service.JavaProjectsService;
import br.uff.ic.archd.git.service.ProjectRevisionsService;
import br.uff.ic.archd.gui.view.MainView;
import br.uff.ic.archd.gui.view.NewProject;
import br.uff.ic.archd.javacode.JavaConstructorService;
import br.uff.ic.archd.model.Project;
import br.uff.ic.archd.service.mining.AnomaliesAnaliser;
import br.uff.ic.archd.service.mining.ProjectAnomalies;
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
public class MainController implements ActionListener {

    private MainView mainView;
    private NewProject newProject;
    private JavaProjectsService javaprojectsService;
    //private String projectsItems[][];
    private List<Project> projects;

    MainController() {

         
        javaprojectsService = new JavaProjectsService();
        projects = javaprojectsService.getProjects();
        String projectsItems[][] = new String[projects.size()][2];
        for(int i = 0; i < projects.size(); i++){
            projectsItems[i][0] = projects.get(i).getName();
            projectsItems[i][1] = projects.get(i).getPath();
        }
        mainView = new MainView(projectsItems);
        newProject = new NewProject();
        newProject.setController(this);
        mainView.setController(this);
        mainView.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(MainView.ACTION_CREATE_PROJECT)) {
            showNewProject();
        } else if (e.getActionCommand().equals(NewProject.ACTION_OK_CREATE_PROJECT)) {
            newProject.setVisible(false);
            javaprojectsService.addProject(projects.size() + 1, newProject.getNameProject(), newProject.getPathProject(), newProject.getCodeDirs());
            projects = javaprojectsService.getProjects();
            updateMainView();
        } else if (e.getActionCommand().equals(MainView.ACTION_VIEW_PROJECT)) {
            int index = mainView.getProjectIndex();
            if (index >= 0) {
                showProject(index);
            }
        }else if(e.getActionCommand().equals(NewProject.ACTION_ADD_DIR)){
            newProject.updateCodeDirs();
        }else if (e.getActionCommand().equals(MainView.ACTION_VIEW_RULES)) {
            int index = mainView.getProjectIndex();
            if (index >= 0) {
                showRules(index);
            }
        }else if (e.getActionCommand().equals(MainView.ACTION_VIEW_ANOMALIES)) {
            int index = mainView.getProjectIndex();
            if (index >= 0) {
                showAnomalies(index);
            }
        }
    }

    private void updateMainView() {
        String projectsItems[][] = new String[projects.size()][2];
        for(int i = 0; i < projects.size(); i++){
            projectsItems[i][0] = projects.get(i).getName();
            projectsItems[i][1] = projects.get(i).getPath();
        }
        mainView.updateProject(projectsItems);
    }

    private void showNewProject() {
        newProject.clean();
        newProject.setVisible(true);
    }

    private void showProject(int index) {
        new InteractionController(projects.get(index));
    }
    
    private void showRules(int index) {
        new RulesController(projects.get(index));
    }
    
    private void showAnomalies(int index) {
        new AnomaliesController(projects.get(index) , createAnomalies(projects.get(index)));
    }
    
    public ProjectAnomalies createAnomalies(Project project) {
        JavaConstructorService javaContructorService = new JavaConstructorService();
        ProjectRevisionsService projectRevisionsService = new ProjectRevisionsService();
        AnomaliesAnaliser anomaliesAnaliser = new AnomaliesAnaliser();
        ProjectAnomalies projectAnomalies = null;
        try {
            ProjectRevisions projectRevisions = projectRevisionsService.getProject(project.getPath(), project.getName());
            System.out.println("ORIGINAL ROOT: " + projectRevisions.getRoot().getId());
            System.out.println("ORIGINAL HEAD: " + projectRevisions.getBranchesRevisions().get(0).getHead().getId());
            System.out.println("Vai limpar");
            ProjectRevisions newProjectRevisions = cleanProjectRevisionsLine(projectRevisions);
            System.out.println("Limpou");
            projectAnomalies = anomaliesAnaliser.getAnomalies(newProjectRevisions, project, javaContructorService);
        } catch (Exception e) {
            System.out.println("Rrro: " + e.getMessage());
        }
        return projectAnomalies;
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

    
    public static void main(String args[]){
        MainController mainController = new MainController();
    }
}
