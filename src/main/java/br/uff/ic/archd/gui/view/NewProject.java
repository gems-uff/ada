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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 *
 * @author wallace
 */
public class NewProject extends JFrame{
    
    public final static String ACTION_OK_CREATE_PROJECT = "ACTION_OK_CREATE_PROJECT";

    
    private JTextField projectNameTextField; 
    private JTextField projectPathTextField; 
    private JLabel projectNamelabel;
    private JLabel projectPathlabel;
    private JButton okButton;
    
    public NewProject() {
        createWidgets();
        this.setPreferredSize(new Dimension(800, 600));
        this.setSize(new Dimension(800, 600));
        this.setMaximumSize(new Dimension(800, 600));
    }
    
    public void clean(){
        projectNameTextField.setText("");
        projectPathTextField.setText("");
    }
    
    public String getNameProject(){
        return projectNameTextField.getText();
    }
    
    public String getPathProject(){
        return projectPathTextField.getText();
    }
    
    private void createWidgets() {
        projectNameTextField = new JTextField(50);
        projectPathTextField = new JTextField(50);
        projectNamelabel = new JLabel("Project Name:");
        projectPathlabel = new JLabel("Project Path");
        okButton = new JButton("OK");
        okButton.setActionCommand(ACTION_OK_CREATE_PROJECT);
    

        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = null;

        gridBagConstraints = new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0);
        p.add(projectNameTextField, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 0, 0, 0), 0, 0);
        p.add(projectPathTextField, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 0, 0, 0), 0, 0);
        p.add(projectNamelabel, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 0, 0, 0), 0, 0);
        p.add(projectPathlabel, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(0, 2, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 0, 0, 0), 0, 0);
        p.add(okButton, gridBagConstraints);
                
        this.add(p, BorderLayout.CENTER);
    }
    
    public void setController(ActionListener actionListener) {
        okButton.addActionListener(actionListener);
        
    }
    
}
