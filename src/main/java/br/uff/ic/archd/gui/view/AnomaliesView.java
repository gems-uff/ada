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
import java.util.List;
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
public class AnomaliesView extends JFrame{
    
    
    public final static String ACTION_OK_ANOMALIES = "ACTION_OK_ANOMALIES";
    public final static String ACTION_OK_METHODS = "ACTION_OK_METHODS";
    public final static String ACTION_OK_CLASSES = "ACTION_OK_CLASSES";
    public final static String ACTION_OK_PACKAGES = "ACTION_OK_PACKAGES";
    
    private JComboBox anomaliesComboBox;
    private JComboBox methodsComboBox;
    private JComboBox classesComboBox;
    private JComboBox packagesComboBox;
    private JLabel anomaliesLabel;
    private JLabel methodsLabel;
    private JLabel classesLabel;
    private JLabel packagesLabel;
    
    private JButton anomaliesButton;
    private JButton methodsButton;
    private JButton classesButton;
    private JButton packagesButton;
    
    private JScrollPane informationPane;
    private JTextArea informationTextArea;
    
    //private JPanel informationPanel;
    private JPanel chartPanel;
    private JScrollPane chartPane;
    
    private JPanel panel;
    
    

    public AnomaliesView(String anomalies[], String packages[], String classes[], String methods[]) {
        createWidgets(anomalies, packages, classes, methods);
        this.setTitle("Anomalies");
        this.setPreferredSize(new Dimension(1600, 800));
        this.setSize(new Dimension(1600, 800));
        this.setMaximumSize(new Dimension(1600, 800));
    }

    private void createWidgets(String anomalies[], String packages[], String classes[], String methods[]) {
        anomaliesComboBox = new JComboBox(anomalies);
        methodsComboBox = new JComboBox(methods);
        classesComboBox = new JComboBox(classes);
        packagesComboBox = new JComboBox(packages);
        
        anomaliesComboBox.setPreferredSize(new Dimension(550,25));
        methodsComboBox.setPreferredSize(new Dimension(550,25));
        classesComboBox.setPreferredSize(new Dimension(550,25));
        packagesComboBox.setPreferredSize(new Dimension(550,25));
        
        
        anomaliesLabel = new JLabel("Anomalies:");
        methodsLabel = new JLabel("Methods:");
        classesLabel = new JLabel("Classes:");
        packagesLabel = new JLabel("Packages:");
        
        anomaliesButton = new JButton("OK");
        methodsButton = new JButton("OK");
        classesButton = new JButton("OK");
        packagesButton = new JButton("OK");
        
        anomaliesButton.setActionCommand(ACTION_OK_ANOMALIES);
        methodsButton.setActionCommand(ACTION_OK_METHODS);
        classesButton.setActionCommand(ACTION_OK_CLASSES);
        packagesButton.setActionCommand(ACTION_OK_PACKAGES);
        
        informationTextArea = new JTextArea();
        informationTextArea.setEditable(false);
        informationPane = new JScrollPane();
        informationPane.setViewportView(informationTextArea);
        informationPane.setPreferredSize(new Dimension(750,160));
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        //informationPanel = new JPanel();
        chartPanel = new JPanel();
        chartPane = new JScrollPane();
        chartPane.setViewportView(chartPanel);
        chartPane.setPreferredSize(new Dimension(1500,500));
        
        //informationPanel.add(informationPane, BorderLayout.CENTER);
        
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = null;

        gridBagConstraints = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(25, 20, 0, 0), 0, 0);
        panel.add(anomaliesLabel, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(25, 20, 0, 0), 0, 0);
        panel.add(anomaliesComboBox, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(25, 20, 0, 0), 0, 0);
        panel.add(anomaliesButton, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(25, 20, 0, 0), 0, 0);
        panel.add(methodsLabel, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(25, 20, 0, 0), 0, 0);
        panel.add(methodsComboBox, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints(2, 1, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(25, 20, 0, 0), 0, 0);
        panel.add(methodsButton, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(25, 20, 0, 0), 0, 0);
        panel.add(classesLabel, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints(1, 2, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(25, 20, 0, 0), 0, 0);
        panel.add(classesComboBox, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints(2, 2, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(25, 20, 0, 0), 0, 0);
        panel.add(classesButton, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints(0, 3, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(25, 20, 0, 0), 0, 0);
        panel.add(packagesLabel, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints(1, 3, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(25, 20, 0, 0), 0, 0);
        panel.add(packagesComboBox, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints(2, 3, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(25, 20, 0, 0), 0, 0);
        panel.add(packagesButton, gridBagConstraints);
        
        
        tabbedPane.add("Information", informationPane);
        tabbedPane.add("Chart", chartPane);
        
        gridBagConstraints = new GridBagConstraints(0, 4, 3, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(25, 20, 0, 20), 0, 0);
        panel.add(tabbedPane, gridBagConstraints);
        
//        gridBagConstraints = new GridBagConstraints(3, 0, 1, 4, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(25, 20, 0, 20), 0, 0);
//        panel.add(informationPane, gridBagConstraints);
//        
//        gridBagConstraints = new GridBagConstraints(0, 4, 4, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(25, 20, 0, 0), 0, 0);
//        panel.add(chartPane, gridBagConstraints);
        
        this.add(panel, BorderLayout.CENTER);
        
        
    }
    
    public void setChartPanel(JPanel jpanel){
        chartPanel.removeAll();
        chartPanel.add(jpanel, BorderLayout.CENTER);
        
        chartPanel.updateUI();
        panel.updateUI();
    }
    
    public void setInformation(String text){
        informationTextArea.setText(text);
        informationTextArea.updateUI();
        //informationPanel.updateUI();
        //panel.updateUI();
    }
    
    public void setPackageDisable(){
        packagesButton.setEnabled(false);
    }
    
    public void setPackageEnable(){
        packagesButton.setEnabled(true);
    }
    
    public void setClassDisable(){
        classesButton.setEnabled(false);
    }
    
    public void setClassEnable(){
        classesButton.setEnabled(true);
    }
    
    public void setMethodDisable(){
        methodsButton.setEnabled(false);
    }
    
    public void setMethodEnable(){
        methodsButton.setEnabled(true);
    }
    
    public int getAnomalieIndex(){
        return anomaliesComboBox.getSelectedIndex();
    }
    
   
    
    public int getPackageIndex(){
        return packagesComboBox.getSelectedIndex();
    }
    
    public int getClassIndex(){
        return classesComboBox.getSelectedIndex();
    }
    
    public int getMethodIndex(){
        return methodsComboBox.getSelectedIndex();
    }
    
    
    public void setController(ActionListener actionListener) {
        anomaliesButton.addActionListener(actionListener);
        methodsButton.addActionListener(actionListener);
        classesButton.addActionListener(actionListener);
        packagesButton.addActionListener(actionListener);
    }
    
    public void setPackages(List<String> packages){
        packagesComboBox.removeAllItems();
        for(String str : packages){
            packagesComboBox.addItem(str);
        }
        packagesComboBox.updateUI();
        panel.updateUI();
    }
    
    public void setClasses(List<String> classes){
        classesComboBox.removeAllItems();
        for(String str : classes){
            classesComboBox.addItem(str);
        }
        classesComboBox.updateUI();
        panel.updateUI();
    }
    
    public void setMethods(List<String> methods){
        methodsComboBox.removeAllItems();
        for(String str : methods){
            methodsComboBox.addItem(str);
        }
        methodsComboBox.updateUI();
        panel.updateUI();
    }
    
    
}
