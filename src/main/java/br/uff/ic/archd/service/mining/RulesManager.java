/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.service.mining;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author wallace
 */
public class RulesManager {

    private HashMap<Integer, String> hash;
    private HashMap<Integer, String> hashOfMethods;
    private int numberOfSequencesFromDataToMining;
    private long numberOfRules;
    public static final int RULES_BY_CONFIDENCE = 1;
    public static final int RULES_BY_LIFT = 2;

    public RulesManager() {
        hash = new HashMap();
        hashOfMethods = new HashMap();
        String path = System.getProperty("user.home") + "/.archd/";
        try {
            String sCurrentLine;
            BufferedReader br = new BufferedReader(new FileReader(path + "map_number_to_text.txt"));
            while ((sCurrentLine = br.readLine()) != null) {
                String text[] = sCurrentLine.split(" ");
                hash.put(Integer.valueOf(text[0]), text[1]);
            }

            //methods
            br = new BufferedReader(new FileReader(path + "methods_to_number.txt"));
            while ((sCurrentLine = br.readLine()) != null) {
                String text[] = sCurrentLine.split(" ");
                hashOfMethods.put(Integer.valueOf(text[0]), text[1]);
            }
            //get number of lines from data to ming
            br = new BufferedReader(new FileReader(path + "data_to_mining_methods.txt"));
            numberOfSequencesFromDataToMining = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                numberOfSequencesFromDataToMining++;
            }
            System.out.println("Number of sequences from data to mining: " + numberOfSequencesFromDataToMining);

            br = new BufferedReader(new FileReader(path + "rules_from_mining_methods_by_confidence.txt"));

            numberOfRules = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                numberOfRules++;
            }
            System.out.println("Number of rules: " + numberOfRules);
        } catch (Exception e) {
            System.out.println("Erro RulesManager.RulesManager: " + e.getMessage());
        }
    }

    public void writeRulesOfConfidence(int number) {
        List<Rule> rules = new LinkedList();
        try {
            String path = System.getProperty("user.home") + "/.archd/";
            String sCurrentLine;
            BufferedReader br = new BufferedReader(new FileReader(path + "rules_from_mining_methods.txt"));
        } catch (Exception e) {
        }

    }

    public long getNumberOfRules() {
        return numberOfRules;
    }

    public Rule getRule(long ruleNumber, int ruleType) {
        Rule rule = null;
        String path = System.getProperty("user.home") + "/.archd/FILES_TO_MINING/MapDB/";
        try {
            String sCurrentLine;
            BufferedReader br = null;
            if (ruleType == RULES_BY_CONFIDENCE) {
                br = new BufferedReader(new FileReader(path + "rules_from_mining_methods_by_confidence.txt"));
            } else {
                br = new BufferedReader(new FileReader(path + "rules_from_mining_methods_by_lift.txt"));
            }
            int k = 0;
            String ruleStr = null;
            while ((sCurrentLine = br.readLine()) != null && k < ruleNumber) {
                k++;
                ruleStr = sCurrentLine;
            }
            //System.out.println("Rule: "+ruleStr);
            if (ruleStr != null) {
                String text[] = ruleStr.split("#SUP:");
                int suporte = Integer.valueOf(text[1].trim());
                rule = transformRule(text[0]);
                List<Integer> suporteOfRuleList = countSuporte(rule);
                rule.setLinesOfOcurrence(suporteOfRuleList);
                Rule ruleOfAntecessor = new Rule();
                for (int i = 0; i < (rule.getSequence().size() - 1); i++) {
                    ItemSet itemSet = rule.getSequence().get(i);
                    ruleOfAntecessor.addItemSet(itemSet);
                }
                Rule ruleOfConsequence = new Rule();
                ruleOfConsequence.addItemSet(rule.getSequence().get(rule.getSequence().size() - 1));
                List<Integer> supportOfAntecessor = countSuporte(ruleOfAntecessor);
                List<Integer> supportOfConsequence = countSuporte(ruleOfConsequence);
                double confidence = suporte;
                confidence = confidence / supportOfAntecessor.size();
                rule.setSupport(suporte);
                rule.setConfidence(confidence);

                double lift = supportOfConsequence.size();
                lift = lift / numberOfSequencesFromDataToMining;
                lift = confidence / lift;
                rule.setLift(lift);



            }
        } catch (Exception e) {
            System.out.println("Erro RulesManager.getRule: " + e.getMessage());
        }
        return rule;
    }

    public void getAllRulesInformation(int ruleType, List<String> rules, List<Double> confidences, List<Double> lifts, List<Integer> suportes) {
        String path = System.getProperty("user.home") + "/.archd/FILES_TO_MINING/MapDB/";
        try {
            String sCurrentLine;
            BufferedReader br = null;
            if (ruleType == RULES_BY_CONFIDENCE) {
                br = new BufferedReader(new FileReader(path + "rules_from_mining_methods_by_confidence.txt"));
            } else {
                br = new BufferedReader(new FileReader(path + "rules_from_mining_methods_by_lift.txt"));
            }
            int k = 0;
            String ruleStr = null;
            while ((sCurrentLine = br.readLine()) != null) {
                k++;
                ruleStr = sCurrentLine;

                //System.out.println("Rule: "+ruleStr);
                if (ruleStr != null) {
                    String text[] = ruleStr.split("#SUP:");
                    int suporte = Integer.valueOf(text[1].trim());
                    Rule rule = transformRule(text[0]);
                    List<Integer> suporteOfRuleList = countSuporte(rule);
                    rule.setLinesOfOcurrence(suporteOfRuleList);
                    Rule ruleOfAntecessor = new Rule();
                    for (int i = 0; i < (rule.getSequence().size() - 1); i++) {
                        ItemSet itemSet = rule.getSequence().get(i);
                        ruleOfAntecessor.addItemSet(itemSet);
                    }
                    Rule ruleOfConsequence = new Rule();
                    ruleOfConsequence.addItemSet(rule.getSequence().get(rule.getSequence().size() - 1));
                    List<Integer> supportOfAntecessor = countSuporte(ruleOfAntecessor);
                    List<Integer> supportOfConsequence = countSuporte(ruleOfConsequence);
                    double confidence = suporte;
                    confidence = confidence / supportOfAntecessor.size();
                    rule.setSupport(suporte);
                    rule.setConfidence(confidence);

                    double lift = supportOfConsequence.size();
                    lift = lift / numberOfSequencesFromDataToMining;
                    lift = confidence / lift;
                    rule.setLift(lift);
                    rules.add(rule.toString());
                    confidences.add(rule.getConfidence());
                    lifts.add(rule.getLift());
                    suportes.add(rule.getSupport());



                }
            }
        } catch (Exception e) {
            System.out.println("Erro RulesManager.getRule: " + e.getMessage());
        }
    }

    public String getMethodByNumber(int number) {
        return hashOfMethods.get(number);
    }

    public List<RuleOcurrence> getRuleOcurrences(Rule rule, int methodNumber) {
        List<RuleOcurrence> ruleOcurrences = new LinkedList();
        String path = System.getProperty("user.home") + "/.archd/";


        try {
            BufferedReader br = new BufferedReader(new FileReader(path + "data_to_mining_methods.txt"));
            String sCurrentLine;
            int i = 0;
            Rule ruleAux = null;
            System.out.println("Rule number: " + methodNumber);
            while ((sCurrentLine = br.readLine()) != null && i <= methodNumber) {
                if (i == methodNumber) {
                    String text[] = sCurrentLine.split("-2");
                    ruleAux = transformRule(text[0].trim());
                }
                i++;
            }


            if (ruleAux != null) {

                HashMap<Integer, String> hashOfRevisions = new HashMap();
                br = new BufferedReader(new FileReader(path + "temp_mining_files/sequence_file_revision_" + methodNumber + ".txt"));
                i = 0;
                while ((sCurrentLine = br.readLine()) != null) {
                    hashOfRevisions.put(i, sCurrentLine.trim());
                    i++;
                }

                System.out.println("A regra maior tem como quantidade de itemsets: " + ruleAux.getSequence().size());
                System.out.println("Vai pegar as regras");
                ruleOcurrences = getListOfOcurrences(rule, ruleAux, hashOfRevisions);
                System.out.println("Pegou as regras num: " + ruleOcurrences.size());
            }
        } catch (Exception e) {
            System.out.println("Exception getRuleOcurrences " + e.getMessage());
        }

        return ruleOcurrences;
    }

    //pega todas as ocorrencias da regra do parametro rule dentro da regra ruleAux
    private List<RuleOcurrence> getListOfOcurrences(Rule rule, Rule ruleAux, HashMap<Integer, String> hashOfRevisions) {
        List<RuleOcurrence> listOfOcurrences = new LinkedList();
        //procuro por todas os subitems do parametro
        ItemSet littleItemSet = ruleAux.getSequence().get(0);
        System.out.println("I irá de 0 até: " + (ruleAux.getSequence().size() - (rule.getSequence().size() - 1)));
        for (int i = 0; i < (ruleAux.getSequence().size() - (rule.getSequence().size() - 1)); i++) {
            ItemSet itemSet = ruleAux.getSequence().get(i);
            if (itemSet.isSubItem(littleItemSet)) {
                List<RuleOcurrence> auxList = getListOfOcurrences(ruleAux.getSequence(), rule.getSequence(), i + 1, 1, hashOfRevisions);
                if (auxList.isEmpty()) {
                    break;
                } else {
                    for (RuleOcurrence ruleOcurrence : auxList) {
                        ruleOcurrence.addRevisionInHead(hashOfRevisions.get(i));
                    }
                    listOfOcurrences.addAll(auxList);
                }
            }
        }

        return listOfOcurrences;
    }

    private List<RuleOcurrence> getListOfOcurrences(List<ItemSet> bigSequence, List<ItemSet> littleSequence, int bsIndex, int lsIndex, HashMap<Integer, String> hashOfRevisions) {
        List<RuleOcurrence> listOfOcurrences = new LinkedList();
        ItemSet littleItemSet = littleSequence.get(lsIndex);
        if ((littleSequence.size() - 1) == lsIndex) {
            for (int i = bsIndex; i < bigSequence.size(); i++) {
                ItemSet bigItemSet = bigSequence.get(i);
                if (bigItemSet.isSubItem(littleItemSet)) {
                    RuleOcurrence ruleOcurrence = new RuleOcurrence();
                    ruleOcurrence.addRevision(hashOfRevisions.get(i));
                    listOfOcurrences.add(ruleOcurrence);
                }
            }
        } else {
            for (int i = bsIndex; i < bigSequence.size() - (littleSequence.size() - (lsIndex + 1)); i++) {
                ItemSet bigItemSet = bigSequence.get(i);
                if (bigItemSet.isSubItem(littleItemSet)) {
                    List<RuleOcurrence> auxList = getListOfOcurrences(bigSequence, littleSequence, i + 1, lsIndex + 1, hashOfRevisions);
                    if (auxList.isEmpty()) {
                        break;
                    } else {
                        for (RuleOcurrence ruleOcurrence : auxList) {
                            ruleOcurrence.addRevisionInHead(hashOfRevisions.get(i));
                        }
                        listOfOcurrences.addAll(auxList);
                    }
                }
            }
        }
        return listOfOcurrences;
    }

    private Rule transformRule(String ruleStr) {
        Rule rule = new Rule();
        String rulesStr[] = ruleStr.trim().split("-1");
        for (int i = 0; i < rulesStr.length; i++) {
            if (!rulesStr[i].equals("")) {
                String itens[] = rulesStr[i].trim().split(" ");
                ItemSet itemSet = new ItemSet();
                for (int j = 0; j < itens.length; j++) {
                    Integer num = Integer.valueOf(itens[j]);
                    Item item = new Item(num, hash.get(num));
                    itemSet.addItem(item);
                }
                rule.addItemSet(itemSet);
            }
        }
        return rule;
    }

    private List<Integer> countSuporte(Rule rule) {
        int suporte = 0;
        List<Integer> list = new LinkedList();
        String path = System.getProperty("user.home") + "/.archd/";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path + "data_to_mining_methods.txt"));
            String sCurrentLine;
            int i = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                String text[] = sCurrentLine.split("-2");
                Rule ruleAux = transformRule(text[0].trim());
                if (ruleAux.isSubRule(rule)) {
                    list.add(i);

                }
                i++;
            }
        } catch (Exception e) {
        }
        return list;
    }
}
