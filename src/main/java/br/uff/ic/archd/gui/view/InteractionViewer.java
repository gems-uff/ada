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
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author wallace
 */
public class InteractionViewer extends JFrame{
    
    public final static String ACTION_UPDATE_CLASS = "ACTION_UPDATE_CLASS";
    public final static String ACTION_UPDATE_INTERFACE = "ACTION_UPDATE_INTERFACE";
    public final static String ACTION_SEARCH_REVISION = "ACTION_SEARCH_REVISION";
    public final static String ACTION_PROX_REVISION = "ACTION_PROX_REVISION";
    public final static String ACTION_ANT_REVISION = "ACTION_ANT_REVISION";
    public final static String ACTION_CODE_CLASS = "ACTION_CODE_CLASS";
    public final static String ACTION_CODE_INTERFACE = "ACTION_CODE_INTERFACE";
    public final static String ACTION_VIEW_GRAPH = "ACTION_VIEW_GRAPH";
    public final static String ACTION_VIEW_PACKAGE_GRAPH = "ACTION_VIEW_PACKAGE_GRAPH";

    private JComboBox comboClasses;
    private JButton buttonClass;
    private JButton viewGraphButton;
    private JButton viewPackageGraphButton;
    private JComboBox comboInterfaces;
    private JButton buttonInterface;
    private JLabel classLabel;
    private JLabel interfaceLabel;
    private JScrollPane projectsScrollPane;
    private JScrollPane dadosProjetoScrollPane;
    private JTextArea textArea;
    private JTextArea dadosProjetoTextArea;
    private JButton classCodeButton;
    private JButton interfaceCodeButton;
    
    private JLabel revisionLabel;
    private JButton proxRevisionButton;
    private JButton antRevisionButton;
    private JTextArea codeTextArea;
    private JScrollPane codeScrollPane;
    
    private JTextField revisionTextField;
    private JLabel searchRevisionLabel;
    private JButton searchRevisionButton;
    
    
    
    public InteractionViewer( String branchesItemsClasses[], String branchesItemsInterfaces[]){
        createWidgets(branchesItemsClasses, branchesItemsInterfaces);
        this.setTitle("Project Structure");
        this.setPreferredSize(new Dimension(1400, 800));
        this.setSize(new Dimension(1400, 800));
        this.setMaximumSize(new Dimension(1400, 800));
    }
    
    
    
    private void createWidgets( String branchesItemsClasses[], String branchesItemsInterfaces[]) {
        comboClasses = new JComboBox(branchesItemsClasses);
        buttonClass = new JButton("Search");
        
        buttonClass.setActionCommand(ACTION_UPDATE_CLASS);
        
        comboInterfaces = new JComboBox(branchesItemsInterfaces);
        buttonInterface = new JButton("Search");
        viewGraphButton = new JButton("Class Graph");
        viewPackageGraphButton = new JButton("Package Graph");
        
        buttonInterface.setActionCommand(ACTION_UPDATE_INTERFACE);
        viewGraphButton.setActionCommand(ACTION_VIEW_GRAPH);
        viewPackageGraphButton.setActionCommand(ACTION_VIEW_PACKAGE_GRAPH);
        
        classLabel = new JLabel("Classes:");
        interfaceLabel = new JLabel("Interfaces:");
        
        searchRevisionLabel = new JLabel("Revision:");
        
        classCodeButton = new JButton("Code of Class");
        interfaceCodeButton = new JButton("Code of Interface");
        revisionLabel = new JLabel("");
        proxRevisionButton = new JButton("Next");
        antRevisionButton = new JButton("Previous");
        codeTextArea = new JTextArea();
    
        revisionTextField = new JTextField();
        revisionTextField.setColumns(20);
        searchRevisionButton = new JButton("Search Revision");
        
        
        
        codeScrollPane = new JScrollPane();
        codeScrollPane.setViewportView(codeTextArea);
        codeTextArea.setEditable(false);
        
        
        searchRevisionButton.setActionCommand(ACTION_SEARCH_REVISION);
        proxRevisionButton.setActionCommand(ACTION_PROX_REVISION);
        antRevisionButton.setActionCommand(ACTION_ANT_REVISION);
        classCodeButton.setActionCommand(ACTION_CODE_CLASS);
        interfaceCodeButton.setActionCommand(ACTION_CODE_INTERFACE);
        viewGraphButton.setActionCommand(ACTION_VIEW_GRAPH);
        
        
        
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
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        gridBagConstraints = new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(20, 10, 20, 10), 0, 0);
        p.add(searchRevisionLabel, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(20, 10, 20, 10), 0, 0);
        p.add(revisionTextField, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(2, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 10, 20, 10), 0, 0);
        p.add(searchRevisionButton, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(3, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 10, 20, 10), 0, 0);
        p.add(viewGraphButton, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(4, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 10, 20, 10), 0, 0);
        p.add(viewPackageGraphButton, gridBagConstraints);
        
        
        gridBagConstraints = new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 10, 20, 10), 0, 0);
        p.add(antRevisionButton, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 10, 20, 10), 0, 0);
        p.add(revisionLabel, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(2, 1, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 10, 20, 10), 0, 0);
        p.add(proxRevisionButton, gridBagConstraints);
        
        
        
        

        gridBagConstraints = new GridBagConstraints(0, 2, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 10, 20, 10), 0, 0);
        p.add(classLabel, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(1, 2, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 0, 20, 10), 0, 0);
        p.add(comboClasses, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(2, 2, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 10, 20, 10), 0, 0);
        p.add(interfaceLabel, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(3, 2, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 0, 20, 10), 0, 0);
        p.add(comboInterfaces, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints(0, 3, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 10, 20, 10), 0, 0);
        p.add(buttonClass, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(2, 3, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 10, 20, 10), 0, 0);
        p.add(buttonInterface, gridBagConstraints);
        
        
        gridBagConstraints = new GridBagConstraints(1, 3, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 10, 20, 10), 0, 0);
        p.add(classCodeButton, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(3, 3, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 10, 20, 10), 0, 0);
        p.add(interfaceCodeButton, gridBagConstraints);
        
        
        //tabbedPane.add("Informations", dadosProjetoScrollPane);
        tabbedPane.add("Information", projectsScrollPane);
        tabbedPane.add("Code", codeScrollPane);
        
        
        gridBagConstraints = new GridBagConstraints(0, 5, 4, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0);
        p.add(tabbedPane, gridBagConstraints);
        
        /*
        gridBagConstraints = new GridBagConstraints(0, 6, 4, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0);
        p.add(projectsScrollPane, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(0, 7, 4, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0);
        p.add(codeScrollPane, gridBagConstraints);*/


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
    
    public void cleanText(){
        dadosProjetoTextArea.setText("");
    }
    
    public String getClassSelected(){
        return (String) comboClasses.getSelectedItem();
    }
    
    public String getInterfaceSelected(){
        return (String) comboInterfaces.getSelectedItem();        
    }
    
    public String getSearchRevision(){
        return revisionTextField.getText();
    }
    
    public void setRevisionLabel(String revision){
        revisionLabel.setText(revision);
    }
    
    public void setCodeText(String code){
        codeTextArea.setText(code);
    }
    
    public void setClassesAndInterfaces(String branchesItemsClasses[], String branchesItemsInterfaces[]){
        //comboClasses = new JComboBox(branchesItemsClasses);
        comboClasses.removeAllItems();
        for(int i = 0; i < branchesItemsClasses.length; i++){
            comboClasses.addItem(branchesItemsClasses[i]);
        }
        //comboInterfaces = new JComboBox(branchesItemsInterfaces);
        comboInterfaces.removeAllItems();
        for(int i = 0; i < branchesItemsInterfaces.length; i++){
            comboInterfaces.addItem(branchesItemsInterfaces[i]);
        }
        this.repaint();
    }

    public void setController(ActionListener actionListener) {
        buttonClass.addActionListener(actionListener);
        buttonInterface.addActionListener(actionListener);
        searchRevisionButton.addActionListener(actionListener);
        proxRevisionButton.addActionListener(actionListener);
        antRevisionButton.addActionListener(actionListener);
        classCodeButton.addActionListener(actionListener);
        interfaceCodeButton.addActionListener(actionListener);
        viewGraphButton.addActionListener(actionListener);
        viewPackageGraphButton.addActionListener(actionListener);
        
    }
    
    
}
