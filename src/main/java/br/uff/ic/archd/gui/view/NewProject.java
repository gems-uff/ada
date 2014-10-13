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
import java.util.LinkedList;
import java.util.List;
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
    public final static String ACTION_ADD_DIR = "ACTION_ADD_DIR";

    
    private JTextField projectNameTextField; 
    private JTextField projectPathTextField; 
    private JTextField codeDirsTextField; 
    private JLabel projectNamelabel;
    private JLabel projectPathlabel;
    private JLabel codeDirslabel;
    private JButton okButton;
    private JButton addButton;
    private JScrollPane codeDirsScrollPane;
    private JTable codeDirsTable;
    private List<String> codeDirs; 
    
    public NewProject() {
        createWidgets();
        this.setPreferredSize(new Dimension(800, 600));
        this.setSize(new Dimension(800, 600));
        this.setMaximumSize(new Dimension(800, 600));
        codeDirs = new LinkedList();
    }
    
    public void clean(){
        projectNameTextField.setText("");
        projectPathTextField.setText("");
        codeDirsTextField.setText("");
        codeDirs.clear();
        codeDirsTable = new JTable();
        codeDirsScrollPane.setViewportView(codeDirsTable);
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
        codeDirsTextField = new JTextField(50);
        projectNamelabel = new JLabel("Project Name:");
        projectPathlabel = new JLabel("Project Path:");
        codeDirslabel = new JLabel("Code Dir:");
        okButton = new JButton("OK");
        okButton.setActionCommand(ACTION_OK_CREATE_PROJECT);
        addButton = new JButton("Add");
        addButton.setActionCommand(ACTION_ADD_DIR);
        
        codeDirsTable = new JTable();
        codeDirsScrollPane = new JScrollPane();
        codeDirsScrollPane.setViewportView(codeDirsTable);
    

        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = null;

        gridBagConstraints = new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0);
        p.add(projectNameTextField, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 0, 0, 0), 0, 0);
        p.add(projectPathTextField, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(1, 2, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 0, 0, 0), 0, 0);
        p.add(codeDirsTextField, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 0, 0, 0), 0, 0);
        p.add(projectNamelabel, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 0, 0, 0), 0, 0);
        p.add(projectPathlabel, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(0, 2, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 0, 0, 0), 0, 0);
        p.add(codeDirslabel, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(0, 3, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 0, 0, 0), 0, 0);
        p.add(addButton, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(0, 4, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 0, 0, 0), 0, 0);
        p.add(codeDirsScrollPane, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(0, 5, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 0, 0, 0), 0, 0);
        p.add(okButton, gridBagConstraints);
                
        this.add(p, BorderLayout.CENTER);
    }
    
    public void updateCodeDirs() {
        codeDirs.add(codeDirsTextField.getText());
        String codeDir[][] = new String[codeDirs.size()][1];
        for(int i =0; i < codeDirs.size(); i++){
            codeDir[i][0] = codeDirs.get(i); 
        }
        String columns[] = {"Dir"};
        codeDirsTable = new JTable(codeDir, columns);
        codeDirsScrollPane.setViewportView(codeDirsTable);
        codeDirsScrollPane.updateUI();
    }
    
    public List<String> getCodeDirs(){
        return codeDirs;
    }
    
    public void setController(ActionListener actionListener) {
        okButton.addActionListener(actionListener);
        addButton.addActionListener(actionListener);
    }
    
}
