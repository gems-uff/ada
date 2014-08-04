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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author wallace
 */
public class InteractionViewer extends JFrame{
    
    public final static String ACTION_UPDATE = "ACTION_UPDATE";
    private JComboBox comboClasses;
    private JButton button;
    private JScrollPane projectsScrollPane;
    private JTextArea textArea;
    
    
    public InteractionViewer( String branchesItems[]){
        createWidgets(branchesItems);
        this.setPreferredSize(new Dimension(800, 600));
        this.setSize(new Dimension(800, 600));
        this.setMaximumSize(new Dimension(800, 600));
    }
    
    private void createWidgets( String branchesItems[]) {
        comboClasses = new JComboBox(branchesItems);
        button = new JButton("PROCURAR");
        
        button.setActionCommand(ACTION_UPDATE);
        projectsScrollPane = new JScrollPane();
        textArea = new JTextArea();
        projectsScrollPane.setViewportView(textArea);
        textArea.setEditable(false);


        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = null;

        gridBagConstraints = new GridBagConstraints(0, 0, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 10, 20, 10), 0, 0);
        p.add(comboClasses, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints(0, 1, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 10, 20, 10), 0, 0);
        p.add(button, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(0, 2, 2, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(20, 10, 10, 10), 0, 0);
        p.add(projectsScrollPane, gridBagConstraints);


        this.add(p, BorderLayout.CENTER);




    }
    
    public void appendText(String text){
        textArea.append(text+"\n");
        projectsScrollPane.updateUI();
    }
    
    public String getClassSelected(){
        return (String) comboClasses.getSelectedItem();
    }

    public void setController(ActionListener actionListener) {
        button.addActionListener(actionListener);
        
    }
    
    
}
