/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.gui.controller;

import br.uff.ic.archd.git.service.JavaProjectsService;
import br.uff.ic.archd.gui.view.MainView;
import br.uff.ic.archd.gui.view.NewProject;
import br.uff.ic.archd.model.Project;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    
    public static void main(String args[]){
        MainController mainController = new MainController();
    }
}
