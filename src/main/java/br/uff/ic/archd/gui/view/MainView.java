/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.gui.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author wallace
 */
public class MainView extends JFrame {

    public final static String ACTION_CREATE_PROJECT = "ACTION_CREATE_PROJECT";
    public final static String ACTION_VIEW_PROJECT = "ACTION_VIEW_PROJECT";
    private JButton createProjectButton;
    private JButton viewProjectButton;
    private JScrollPane projectsScrollPane;
    private JTable projectsTable;

    public MainView(String projectsItems[][]) {
        createWidgets(projectsItems);
        this.setPreferredSize(new Dimension(800, 600));
        this.setSize(new Dimension(800, 600));
        this.setMaximumSize(new Dimension(800, 600));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void createWidgets(String projectsItems[][]) {
        createProjectButton = new JButton("Create New Project");
        viewProjectButton = new JButton("View Project");
        createProjectButton.setActionCommand(ACTION_CREATE_PROJECT);
        viewProjectButton.setActionCommand(ACTION_VIEW_PROJECT);
        String columns[] = {"Name", "Path"};
        if(projectsItems != null){
            projectsTable = new JTable(projectsItems, columns);
        }else{
            projectsTable = new JTable();
        }

        projectsScrollPane = new JScrollPane();
        projectsScrollPane.setViewportView(projectsTable);
        
        


        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = null;

        gridBagConstraints = new GridBagConstraints(0, 0, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0);
        p.add(createProjectButton, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints(0, 1, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 0, 0, 0), 0, 0);
        p.add(viewProjectButton, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(0, 2, 2, 2, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 0, 0, 0), 0, 0);
        p.add(projectsScrollPane, gridBagConstraints);
                
        this.add(p, BorderLayout.CENTER);
    }

    public void updateProject(String projectsItems[][]) {
        String columns[] = {"Name", "Path"};
        projectsTable = new JTable(projectsItems, columns);
        projectsScrollPane.setViewportView(projectsTable);
        projectsScrollPane.updateUI();
    }
    
    public void setController(ActionListener actionListener) {
        createProjectButton.addActionListener(actionListener);
        viewProjectButton.addActionListener(actionListener);
        
    }
    
    public int getProjectIndex(){
        return  projectsTable.getSelectedRow();
    }
}
