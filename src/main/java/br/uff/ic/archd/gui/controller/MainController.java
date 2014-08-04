/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.gui.controller;

import br.uff.ic.archd.git.service.JavaProjectsService;
import br.uff.ic.archd.gui.view.InteractionViewer;
import br.uff.ic.archd.gui.view.MainView;
import br.uff.ic.archd.gui.view.NewProject;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author wallace
 */
public class MainController implements ActionListener {

    private MainView mainView;
    private NewProject newProject;
    private JavaProjectsService javaprojectsService;
    private String projectsItems[][];

    MainController() {

        javaprojectsService = new JavaProjectsService();
        projectsItems = javaprojectsService.getProjects();
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
            javaprojectsService.addProject(newProject.getNameProject(), newProject.getPathProject());
            projectsItems = javaprojectsService.getProjects();
            updateMainView();
        } else if (e.getActionCommand().equals(MainView.ACTION_VIEW_PROJECT)) {
            int index = mainView.getProjectIndex();
            if (index >= 0) {
                showProject(index);
            }
        }
    }

    private void updateMainView() {
        mainView.updateProject(projectsItems);
    }

    private void showNewProject() {
        newProject.clean();
        newProject.setVisible(true);
    }

    private void showProject(int index) {
        new InteractionController(projectsItems[index][1]);
    }
    
    public static void main(String args[]){
        MainController mainController = new MainController();
    }
}
