/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.gui.view;

import br.uff.ic.archd.service.mining.Rule;
import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author wallace
 */
public class RulesView extends JFrame {

    public final static String ACTION_OK_NUMBER_OF_RULES = "ACTION_OK_NUMBER_OF_RULES";
    public final static String ACTION_OK_METHODS = "ACTION_OK_METHODS";
    public final static String ACTION_OK_OCURRENCES = "ACTION_OK_OCURRENCES";
    public final static String ACTION_OK_RULE = "ACTION_OK_RULE";
    public final static String ACTION_OK_NEXT = "ACTION_OK_NEXT";
    public final static String ACTION_OK_PREV = "ACTION_OK_PREV";
    public final static String ACTION_OK_RULES_BUTTON = "ACTION_OK_RULES_BUTTON";
    public final static int RULE_BY_CONFIDENCE = 0;
    public final static int RULE_BY_LIFT = 1;
    private JLabel typeOfRulesLabel;
    private JComboBox typeOfRulesComboBox;
    private JButton typeOfRulesButton;
    private JLabel numberOfRuleLabel;
    private JTextField numerOfRuleTextField;
    private JButton numerOfRuleButton;
    private JLabel methodsLabel;
    private JComboBox methodsComboBox;
    private JButton methodsButton;
    private JLabel ocurrencesLabel;
    private JComboBox ocurrencesComboBox;
    private JButton ocurrencesButton;
    private JLabel ruleTextLabel;
    private JScrollPane ruleScrollPane;
    private ColorPane ruleColorPane;
    private JButton ruleButton;
    private JPanel revisionsPanel;
    private JPanel differencesPanel;
    private JPanel subPanel;
    private JLabel rulesListLabel;
    private JButton nextButton;
    private JButton prevButton;
    private JPanel panel;
    private ColorPane revisionAntCodeArea;
    private ColorPane revisionProxCodeArea;
    private ColorPane rulesListCodeArea;
    private JTextArea diffRevisionsCodeArea;
    private JScrollPane revisionAntScrollPane;
    private JScrollPane revisionProxScrollPane;
    private JScrollPane diffRevisionsScrollPane;
    private JScrollPane rulesListScrollPane;

    public RulesView() {
        createWidgets();
        this.setPreferredSize(new Dimension(1600, 800));
        this.setSize(new Dimension(1600, 800));
        this.setMaximumSize(new Dimension(1600, 800));
    }

    public void updateRules() {
    }

    private void createWidgets() {
        String typesOfRulesString[] = {"Rules By Condifence", "Rules By Lift"};
        typeOfRulesComboBox = new JComboBox(typesOfRulesString);
        typeOfRulesComboBox.setSelectedIndex(0);
        numerOfRuleTextField = new JTextField();
        numerOfRuleTextField.setColumns(20);
        numerOfRuleButton = new JButton("OK");

        methodsComboBox = new JComboBox();
        methodsButton = new JButton("OK");

        typeOfRulesButton = new JButton("OK");

        ocurrencesComboBox = new JComboBox();
        ocurrencesButton = new JButton("OK");
        ruleColorPane = new ColorPane();
        ruleButton = new JButton("Search");

        nextButton = new JButton("NEXT");
        prevButton = new JButton("PREV");

        typeOfRulesLabel = new JLabel("Type Of Rule:");
        numberOfRuleLabel = new JLabel("Number Of Rule:");
        methodsLabel = new JLabel("Method:");
        ocurrencesLabel = new JLabel("Occurrence:");
        ruleTextLabel = new JLabel("Rule:");
        rulesListLabel = new JLabel("RULES");

        //ruleColorPane = new ColorPane();

        ruleScrollPane = new JScrollPane();
        ruleScrollPane.setViewportView(ruleColorPane);


        numerOfRuleButton.setActionCommand(ACTION_OK_NUMBER_OF_RULES);
        methodsButton.setActionCommand(ACTION_OK_METHODS);
        ocurrencesButton.setActionCommand(ACTION_OK_OCURRENCES);
        ruleButton.setActionCommand(ACTION_OK_RULE);
        nextButton.setActionCommand(ACTION_OK_NEXT);
        prevButton.setActionCommand(ACTION_OK_PREV);
        typeOfRulesButton.setActionCommand(ACTION_OK_RULES_BUTTON);


        methodsComboBox.setEnabled(false);
        ocurrencesComboBox.setEnabled(false);

        methodsButton.setEnabled(false);
        ocurrencesButton.setEnabled(false);
        ruleButton.setEnabled(false);
        nextButton.setEnabled(false);
        prevButton.setEnabled(false);


        revisionsPanel = new JPanel();
        differencesPanel = new JPanel();
        subPanel = new JPanel();
        ruleScrollPane.setPreferredSize(new Dimension(800, 80));


        revisionAntCodeArea = new ColorPane();
        revisionProxCodeArea = new ColorPane();
        rulesListCodeArea = new ColorPane();
        diffRevisionsCodeArea = new JTextArea();
        revisionAntScrollPane = new JScrollPane();
        revisionProxScrollPane = new JScrollPane();
        diffRevisionsScrollPane = new JScrollPane();
        rulesListScrollPane = new JScrollPane();

        rulesListScrollPane.setViewportView(rulesListCodeArea);
        rulesListCodeArea.setEditable(false);

        revisionAntScrollPane.setViewportView(revisionAntCodeArea);
        revisionAntCodeArea.setEditable(false);

        revisionProxScrollPane.setViewportView(revisionProxCodeArea);
        revisionProxCodeArea.setEditable(false);

        diffRevisionsScrollPane.setViewportView(diffRevisionsCodeArea);
        diffRevisionsCodeArea.setEditable(false);

        revisionAntScrollPane.setPreferredSize(new Dimension(400, 500));
        revisionProxScrollPane.setPreferredSize(new Dimension(400, 500));
        diffRevisionsScrollPane.setPreferredSize(new Dimension(400, 500));
        ruleColorPane.setEditable(false);
        rulesListScrollPane.setPreferredSize(new Dimension(400, 700));

        //methodsComboBox.setPreferredSize(new Dimension(100,40));
        //ruleScrollPane.setMinimumSize(new Dimension(600,80));



        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = null;

        gridBagConstraints = new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(25, 20, 0, 0), 0, 0);
        panel.add(typeOfRulesLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints(1, 0, 2, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(25, 2, 0, 0), 0, 0);
        panel.add(typeOfRulesComboBox, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints(2, 0, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(25, 2, 0, 0), 0, 0);
        panel.add(typeOfRulesButton, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(5, 20, 0, 0), 0, 0);
        panel.add(numberOfRuleLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 2, 0, 0), 0, 0);
        panel.add(numerOfRuleTextField, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints(2, 1, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 2, 0, 0), 0, 0);
        panel.add(numerOfRuleButton, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints(0, 2, 1, 1, 1, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(5, 2, 0, 0), 0, 0);
        panel.add(methodsLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints(1, 2, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 2, 0, 0), 0, 0);
        panel.add(methodsComboBox, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints(2, 2, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 2, 0, 0), 0, 0);
        panel.add(methodsButton, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints(0, 3, 1, 1, 1, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(5, 2, 0, 0), 0, 0);
        panel.add(ocurrencesLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints(1, 3, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 2, 0, 0), 0, 0);
        panel.add(ocurrencesComboBox, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints(2, 3, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 2, 0, 0), 0, 0);
        panel.add(ocurrencesButton, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints(0, 4, 1, 1, 0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(5, 2, 0, 0), 0, 0);
        panel.add(ruleTextLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints(1, 4, 2, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 2, 0, 0), 0, 0);
        panel.add(ruleScrollPane, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints(3, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 2, 0, 0), 0, 0);
        panel.add(rulesListLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints(3, 1, 1, 5, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 2, 0, 0), 0, 0);
        panel.add(rulesListScrollPane, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 0, 0, 0), 0, 0);
        subPanel.add(prevButton, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 0, 0, 0), 0, 0);
        subPanel.add(revisionsPanel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints(2, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 0, 0, 0), 0, 0);
        subPanel.add(differencesPanel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints(3, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 0, 0, 0), 0, 0);
        subPanel.add(nextButton, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints(0, 5, 3, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 20, 0, 0), 0, 0);
        panel.add(subPanel, gridBagConstraints);

        this.add(panel, BorderLayout.CENTER);

        gridBagConstraints = new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(6, 0, 0, 0), 0, 0);
        revisionsPanel.add(revisionAntScrollPane, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(6, 6, 0, 0), 0, 0);
        revisionsPanel.add(revisionProxScrollPane, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(6, 6, 0, 0), 0, 0);
        differencesPanel.add(diffRevisionsScrollPane, gridBagConstraints);

        //remover isso depois:
        differencesPanel.setVisible(false);

    }

    public void setController(ActionListener actionListener) {
        numerOfRuleButton.addActionListener(actionListener);
        methodsButton.addActionListener(actionListener);
        ocurrencesButton.addActionListener(actionListener);
        ruleButton.addActionListener(actionListener);
        nextButton.addActionListener(actionListener);
        prevButton.addActionListener(actionListener);
        typeOfRulesButton.addActionListener(actionListener);
    }

    public void setRules(List<String> rules, List<Double> confidences, List<Double> lifts, List<Integer> supports) {
        rulesListCodeArea.setEditable(true);
        rulesListCodeArea.setText("");
        for (int i = 0; i < rules.size(); i++) {
            rulesListCodeArea.append(Color.red, (i+1)+" - ");
            rulesListCodeArea.append(Color.black, rules.get(i));
            rulesListCodeArea.append(Color.blue, "   - "+"Support: "+supports.get(i)+"    Confidence: "+confidences.get(i)*100+" %    Lift: "+lifts.get(i));
            rulesListCodeArea.append(Color.black, "\n");
        }
        rulesListCodeArea.setEditable(false);
        revisionAntCodeArea.updateUI();
    }

    public void updateMethods(String methods[], String rule, double confidence, double lift, int support) {
        methodsComboBox.removeAllItems();
        for (int i = 0; i < methods.length; i++) {
            methodsComboBox.addItem(methods[i]);
            System.out.println("Method: " + methods[i]);
        }

        methodsComboBox.setEnabled(true);
        methodsButton.setEnabled(true);
        ocurrencesButton.setEnabled(false);
        ocurrencesComboBox.setEnabled(false);
        nextButton.setEnabled(false);
        prevButton.setEnabled(false);
        revisionAntCodeArea.setText("");
        revisionProxCodeArea.setText("");
        ruleColorPane.setEditable(true);
        ruleColorPane.setText("");
        ruleColorPane.append(Color.black, rule);
        ruleColorPane.append(Color.blue, "\n\nSupport: "+support+"    Confidence: " + confidence * 100 + "%    Lift: " + lift);
        ruleColorPane.setEditable(false);
        ruleScrollPane.updateUI();
        methodsComboBox.updateUI();
        panel.updateUI();



    }

    public void updateOcurrence(int numberOfOcurrences) {
        ocurrencesComboBox.removeAllItems();
        for (int i = 0; i < numberOfOcurrences; i++) {
            ocurrencesComboBox.addItem("Ocurrence " + i);
        }
        ocurrencesButton.setEnabled(true);
        ocurrencesComboBox.setEnabled(true);
        nextButton.setEnabled(false);
        prevButton.setEnabled(false);
        revisionAntCodeArea.setText("");
        revisionProxCodeArea.setText("");
        ocurrencesComboBox.updateUI();
        panel.updateUI();
    }

    public int getMethodIndex() {
        return methodsComboBox.getSelectedIndex();
    }

    public int getTypeOfRuleIndex() {
        return typeOfRulesComboBox.getSelectedIndex();
    }

    public int getOcurrenceIndex() {
        return ocurrencesComboBox.getSelectedIndex();
    }

    public void setCodeInAntRevision(String initcode, String methodCode, String finalCode) {
        revisionAntCodeArea.setEditable(true);
        revisionAntCodeArea.setText("");
        revisionAntCodeArea.append(Color.black, initcode);
        revisionAntCodeArea.append(Color.red, methodCode);
        revisionAntCodeArea.append(Color.black, finalCode);
        revisionAntCodeArea.setEditable(false);
        revisionAntCodeArea.updateUI();
    }

    public void setCodeInProxRevision(String initcode, String methodCode, String finalCode) {
        revisionProxCodeArea.setEditable(true);
        revisionProxCodeArea.setText("");
        revisionProxCodeArea.append(Color.black, initcode);
        revisionProxCodeArea.append(Color.red, methodCode);
        revisionProxCodeArea.append(Color.black, finalCode);
        revisionProxCodeArea.setEditable(false);
        revisionProxCodeArea.updateUI();
        //revisionProxCodeArea.setText(code);
    }

    public void setRuleColor(String rule, double confidence, double lift, int revisionPairIndex) {

        String rules[] = rule.split(" => ");
        //System.out.println("RULE: "+rule);
        //System.out.println("RULE LENGTH: "+rules.length);
        ruleColorPane.setEditable(true);
        ruleColorPane.setText("");
        for (int i = 0; i < rules.length; i++) {
            if (i == revisionPairIndex) {
                ruleColorPane.append(Color.red, rules[i]);
            } else {
                ruleColorPane.append(Color.black, rules[i]);
            }
            if ((i + 1) != rules.length) {
                ruleColorPane.append(Color.black, " => ");
            }
        }
        ruleColorPane.append(Color.blue, "\n\nConfidence: " + confidence * 100 + "%    Lift: " + lift);
        ruleColorPane.setEditable(false);
        ruleColorPane.updateUI();

    }

    public void setNextRevisionEnable() {
        nextButton.setEnabled(true);
    }

    public void setPevRevisionEnable() {
        prevButton.setEnabled(true);
    }

    public void setNextRevisionDisable() {
        nextButton.setEnabled(false);
    }

    public void setPevRevisionDisable() {
        prevButton.setEnabled(false);
    }

    public long getNumberOfRule() {
        long numberOfRule = -1;
        try {
            numberOfRule = Long.valueOf(numerOfRuleTextField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        return numberOfRule;
    }
}
