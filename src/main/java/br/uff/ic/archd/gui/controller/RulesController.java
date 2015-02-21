/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.gui.controller;

import br.uff.ic.archd.git.service.JavaProjectsService;
import br.uff.ic.archd.gui.view.RulesView;
import br.uff.ic.archd.javacode.JavaAbstract;
import br.uff.ic.archd.javacode.JavaClass;
import br.uff.ic.archd.javacode.JavaConstructorService;
import br.uff.ic.archd.javacode.JavaProject;
import br.uff.ic.archd.model.Project;
import br.uff.ic.archd.service.mining.Rule;
import br.uff.ic.archd.service.mining.RuleOcurrence;
import br.uff.ic.archd.service.mining.RulesManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author wallace
 */
public class RulesController implements ActionListener {

    private RulesView rulesView;
    private Rule rule;
    private List<RuleOcurrence> ruleOcurrences;
    private RuleOcurrence ocurrence;
    private int revisionPairIndex;
    private RulesManager rulesManager;
    private Project project;
    private String currentClass;
    private JavaConstructorService javaConstructorService;
    private int methodIndex;

    RulesController(Project project) {

        this.project = project;

        javaConstructorService = new JavaConstructorService();
        rulesManager = new RulesManager();

        rulesView = new RulesView();
        rulesView.setController(this);
        setRules(1);
        rulesView.setVisible(true);
    }

    private void showMethodsOcurrences(int index) {
        methodIndex = index;
        currentClass = rulesManager.getMethodByNumber(rule.getLinesOfOcurrence().get(methodIndex)).split(":")[0];
        ruleOcurrences = rulesManager.getRuleOcurrences(rule, rule.getLinesOfOcurrence().get(methodIndex));
        rulesView.updateOcurrence(ruleOcurrences.size());
    }

    private void showNextItemSet() {
        revisionPairIndex++;
        
        if ((revisionPairIndex + 1) >= ocurrence.getRevisions().size()) {
            rulesView.setNextRevisionDisable();
        }
        rulesView.setPevRevisionEnable();
        setCode();
        rulesView.setRuleColor(rule.toString(), rule.getConfidence(), rule.getLift(), revisionPairIndex);

    }

    private void showPrevItemSet() {
        revisionPairIndex--;
        
        if (revisionPairIndex == 0) {
            rulesView.setPevRevisionDisable();
        }
        rulesView.setNextRevisionEnable();
        setCode();
        rulesView.setRuleColor(rule.toString(), rule.getConfidence(), rule.getLift(),revisionPairIndex);
    }

    private void showOcurrence(int ocurrenceIndex) {
        ocurrence = ruleOcurrences.get(ocurrenceIndex);
        revisionPairIndex = 0;
        setCode();
        rulesView.setRuleColor(rule.toString(), rule.getConfidence(), rule.getLift(),revisionPairIndex);
        if ((revisionPairIndex + 1) < ocurrence.getRevisions().size()) {
            rulesView.setNextRevisionEnable();
        }
        if (revisionPairIndex == 0) {
            rulesView.setPevRevisionDisable();
        }

    }
    
    public void setRules(int typeOfRule){
        List<String> rules = new LinkedList();
        List<Double> confidences = new LinkedList();
        List<Double> lifts = new LinkedList();
        List<Integer> suportes = new LinkedList();
        long ant1 = System.currentTimeMillis();
        long numberOfRules = rulesManager.getNumberOfRules();
        rulesManager.getAllRulesInformation(typeOfRule, rules, confidences, lifts, suportes);
        /*
        for(int i = 1; i <= numberOfRules; i++){
            Rule rule = rulesManager.getRule(i, typeOfRule);
            rules.add(rule.toString());
            confidences.add(rule.getConfidence());
            lifts.add(rule.getLift());
            suportes.add(rule.getSupport());
        }
        **/
        long ant2 = System.currentTimeMillis();
        System.out.println("Tempo Total para pegar todas as "+numberOfRules+" regras: "+((ant2 - ant1)/1000)+"  segundos");
        ant1 = System.currentTimeMillis();
        rulesView.setRules(rules, confidences, lifts,suportes);
        ant2 = System.currentTimeMillis();
        System.out.println("Tempo para escrever as regras na tela: "+(ant2 - ant1)+" milisegundos");
    }

    private void setCode() {
        String revisionPair = ocurrence.getRevisions().get(revisionPairIndex);
        String str[] = revisionPair.split(" ");
        String previousRevision = str[0];
        String nextRevision = str[1];
        JavaProject jpAnt = javaConstructorService.getProjectByRevisionAndSetRevision(project.getName(), project.getCodeDirs(), project.getPath(), previousRevision, project.getName());
        JavaAbstract ja = jpAnt.getClassByName(currentClass);
        if (ja != null) {
            if (ja.getClass() == JavaClass.class) {
                JavaClass jc = (JavaClass) ja;
                System.out.println("Caminho da classe antes: " + jc.getPath());

                String code = "";
                String sCurrentLine = null;
                try {
                    BufferedReader br = new BufferedReader(new FileReader(jc.getPath()));
                    while ((sCurrentLine = br.readLine()) != null) {
                        code = code + sCurrentLine + "\n";
                    }
                    boolean encontrou = false;
                    int fromIndex = 0;
                    String methodName = rulesManager.getMethodByNumber(rule.getLinesOfOcurrence().get(methodIndex)).split(":")[1].split("\\(")[0];
                    System.out.println("Method Name: " + methodName);
                    int indexFinal = 0;
                    int indexInicio = 0;
                    char[] codeChar = code.toCharArray();
                    while (!encontrou) {
                        boolean parar = false;
                        int index = code.indexOf(methodName, fromIndex);
                        int i = index + methodName.length();
                        System.out.println("Index: "+i);
                        while (codeChar[i] != '(') {
                            if (codeChar[i] != ' ') {
                                parar = true;
                                break;
                            }
                            i++;
                        }
                        //System.out.println("AQUI");
                        if (!parar) {
                            i++;
                            while (codeChar[i] != ')') {
                                if (codeChar[i] == '(' || codeChar[i] == '{' || codeChar[i] == '}') {
                                    parar = true;
                                    break;
                                }
                                i++;
                            }
                            //System.out.println("AQUI2");
                            if (!parar) {
                                i++;
                                while (codeChar[i] != '{') {
                                    if (codeChar[i] != ' ') {
                                        parar = true;
                                        break;
                                    }
                                    i++;
                                }
                                //System.out.println("AQUI3");
                                if(!parar){
                                    i++;
                                    int num = 1;
                                    boolean procurar = true;
                                    while(!encontrou){
                                        if(codeChar[i] == '{'){
                                            num++;
                                        }
                                        if(codeChar[i] == '}'){
                                            num--;
                                            if(num == 0){
                                                encontrou = true;
                                                indexFinal = i+1;
                                                indexInicio = index;
                                            }
                                        }
                                        i++;
                                    }
                                }
                            }
                        }
                        if(!encontrou){
                            fromIndex = i;
                        }

                    }
                    System.out.println("Index inicio: "+indexInicio);
                    System.out.println("Index final: "+indexFinal);
                    String codeInit = code.substring(0, indexInicio);
                    String codeMethod = code.substring(indexInicio, indexFinal);
                    String codeFinal = code.substring(indexFinal, code.length());
                    rulesView.setCodeInAntRevision(codeInit, codeMethod, codeFinal);
                } catch (Exception e) {
                    System.out.println("Erro em ler o codigo anterior: " + e.getMessage());
                }

            }
        }

        JavaProject jpProx = javaConstructorService.getProjectByRevisionAndSetRevision(project.getName(), project.getCodeDirs(), project.getPath(), nextRevision, project.getName());
        ja = jpProx.getClassByName(currentClass);
        if (ja != null) {
            if (ja.getClass() == JavaClass.class) {
                JavaClass jc = (JavaClass) ja;
                System.out.println("Caminho da classe depois: " + jc.getPath());

                String code = "";
                String sCurrentLine = null;
                try {
                    BufferedReader br = new BufferedReader(new FileReader(jc.getPath()));
                    while ((sCurrentLine = br.readLine()) != null) {
                        code = code + sCurrentLine + "\n";
                    }
                    boolean encontrou = false;
                    int fromIndex = 0;
                    String methodName = rulesManager.getMethodByNumber(rule.getLinesOfOcurrence().get(methodIndex)).split(":")[1].split("\\(")[0];
                    System.out.println("Method Name: " + methodName);
                    int indexFinal = 0;
                    int indexInicio = 0;
                    char[] codeChar = code.toCharArray();
                    while (!encontrou) {
                        boolean parar = false;
                        int index = code.indexOf(methodName, fromIndex);
                        int i = index + methodName.length();
                        System.out.println("Index: "+i);
                        while (codeChar[i] != '(') {
                            if (codeChar[i] != ' ') {
                                parar = true;
                                break;
                            }
                            i++;
                        }
                        //System.out.println("AQUI");
                        if (!parar) {
                            i++;
                            while (codeChar[i] != ')') {
                                if (codeChar[i] == '(' || codeChar[i] == '{' || codeChar[i] == '}') {
                                    parar = true;
                                    break;
                                }
                                i++;
                            }
                            //System.out.println("AQUI2");
                            if (!parar) {
                                i++;
                                while (codeChar[i] != '{') {
                                    if (codeChar[i] != ' ') {
                                        parar = true;
                                        break;
                                    }
                                    i++;
                                }
                                //System.out.println("AQUI3");
                                if(!parar){
                                    i++;
                                    int num = 1;
                                    boolean procurar = true;
                                    while(!encontrou){
                                        if(codeChar[i] == '{'){
                                            num++;
                                        }
                                        if(codeChar[i] == '}'){
                                            num--;
                                            if(num == 0){
                                                encontrou = true;
                                                indexFinal = i+1;
                                                indexInicio = index;
                                            }
                                        }
                                        i++;
                                    }
                                }
                            }
                        }
                        if(!encontrou){
                            fromIndex = i;
                        }

                    }
                    System.out.println("Index inicio: "+indexInicio);
                    System.out.println("Index final: "+indexFinal);
                    String codeInit = code.substring(0, indexInicio);
                    String codeMethod = code.substring(indexInicio, indexFinal);
                    String codeFinal = code.substring(indexFinal, code.length());
                    rulesView.setCodeInProxRevision(codeInit, codeMethod, codeFinal);
                } catch (Exception e) {
                    System.out.println("Erro em ler o codigo anterior: " + e.getMessage());
                }

            }
        }
    }

    private void showRule(long numberOfRule, int typeOfRule) {
        /*String path = null;
         if(typeOfRule == RulesView.RULE_BY_CONFIDENCE){
         path = ruleByConfidencePath;
         }else if(typeOfRule == RulesView.RULE_BY_LIFT){
         path = ruleByLiftPath;
         }*/

        rule = rulesManager.getRule(numberOfRule, typeOfRule);
        String methods[] = new String[rule.getLinesOfOcurrence().size()];
        for (int i = 0; i < rule.getLinesOfOcurrence().size(); i++) {
            methods[i] = rulesManager.getMethodByNumber(rule.getLinesOfOcurrence().get(i));
        }
        rulesView.updateMethods(methods, rule.toString(), rule.getConfidence(), rule.getLift(), rule.getSupport());

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(RulesView.ACTION_OK_METHODS)) {
            int index = rulesView.getMethodIndex();
            if (index != -1) {
                showMethodsOcurrences(rulesView.getMethodIndex());
            }
        } else if (e.getActionCommand().equals(RulesView.ACTION_OK_NEXT)) {
            showNextItemSet();
        } else if (e.getActionCommand().equals(RulesView.ACTION_OK_PREV)) {
            showPrevItemSet();
        } else if (e.getActionCommand().equals(RulesView.ACTION_OK_NUMBER_OF_RULES)) {
            long num = rulesView.getNumberOfRule();
            if (num != -1) {
                showRule(num, rulesView.getTypeOfRuleIndex()+1);
            }
        } else if (e.getActionCommand().equals(RulesView.ACTION_OK_OCURRENCES)) {
            int num = rulesView.getOcurrenceIndex();
            if (num != -1) {
                showOcurrence(num);
            }
        }else if (e.getActionCommand().equals(RulesView.ACTION_OK_RULES_BUTTON)){
            int num = rulesView.getTypeOfRuleIndex();
            if (num != -1) {
                setRules(num+1);
            }
        }
    }

    /*public static void main(String args[]) {
        String path = System.getProperty("user.home") + "/.archd/";
        JavaProjectsService javaprojectsService = new JavaProjectsService();
        List<Project> projects = javaprojectsService.getProjects();
        Project p = null;
        for (Project project : projects) {
            if (project.getName().equals("MapDB")) {
                p = project;
                break;
            }
        }
        if (p != null) {
            RulesController rulesController = new RulesController(p);
        }

    }*/
}
