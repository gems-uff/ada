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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author wallace
 */
public class InteractionViewer extends JFrame{
    
    public final static String ACTION_UPDATE_CLASS = "ACTION_UPDATE_CLASS";
    public final static String ACTION_UPDATE_INTERFACE = "ACTION_UPDATE_INTERFACE";
    private JComboBox comboClasses;
    private JButton buttonClass;
    private JComboBox comboInterfaces;
    private JButton buttonInterface;
    private JLabel classLabel;
    private JLabel interfaceLabel;
    private JScrollPane projectsScrollPane;
    private JScrollPane dadosProjetoScrollPane;
    private JTextArea textArea;
    private JTextArea dadosProjetoTextArea;
    
    
    public InteractionViewer( String branchesItemsClasses[], String branchesItemsInterfaces[]){
        createWidgets(branchesItemsClasses, branchesItemsInterfaces);
        this.setPreferredSize(new Dimension(1400, 800));
        this.setSize(new Dimension(1400, 800));
        this.setMaximumSize(new Dimension(1400, 800));
    }
    
    private void createWidgets( String branchesItemsClasses[], String branchesItemsInterfaces[]) {
        comboClasses = new JComboBox(branchesItemsClasses);
        buttonClass = new JButton("PROCURAR");
        
        buttonClass.setActionCommand(ACTION_UPDATE_CLASS);
        
        comboInterfaces = new JComboBox(branchesItemsInterfaces);
        buttonInterface = new JButton("PROCURAR");
        
        buttonInterface.setActionCommand(ACTION_UPDATE_INTERFACE);
        
        classLabel = new JLabel("Classes:");
        interfaceLabel = new JLabel("Interfaces:");
        
        projectsScrollPane = new JScrollPane();
        textArea = new JTextArea();
        projectsScrollPane.setViewportView(textArea);
        textArea.setEditable(false);
        
        dadosProjetoScrollPane = new JScrollPane();
        dadosProjetoTextArea = new JTextArea();
        dadosProjetoScrollPane.setViewportView(dadosProjetoTextArea);
        dadosProjetoTextArea.setEditable(false);


        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = null;

        gridBagConstraints = new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 10, 20, 10), 0, 0);
        p.add(classLabel, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 0, 20, 10), 0, 0);
        p.add(comboClasses, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(2, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 10, 20, 10), 0, 0);
        p.add(interfaceLabel, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(3, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 0, 20, 10), 0, 0);
        p.add(comboInterfaces, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints(0, 1, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 10, 20, 10), 0, 0);
        p.add(buttonClass, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(2, 1, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 10, 20, 10), 0, 0);
        p.add(buttonInterface, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(0, 2, 4, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0);
        p.add(dadosProjetoScrollPane, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(0, 3, 4, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0);
        p.add(projectsScrollPane, gridBagConstraints);


        this.add(p, BorderLayout.CENTER);




    }
    
    public void appendText(String text){
        textArea.append(text+"\n");
        projectsScrollPane.updateUI();
    }
    
    public void appendDadosText(String text){
        dadosProjetoTextArea.append(text+"\n");
        dadosProjetoScrollPane.updateUI();
    }
    
    public String getClassSelected(){
        return (String) comboClasses.getSelectedItem();
    }
    
    public String getInterfaceSelected(){
        return (String) comboInterfaces.getSelectedItem();        
    }

    public void setController(ActionListener actionListener) {
        buttonClass.addActionListener(actionListener);
        buttonInterface.addActionListener(actionListener);
        
    }
    
    
}
