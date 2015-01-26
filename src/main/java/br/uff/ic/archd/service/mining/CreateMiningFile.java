/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.service.mining;

import br.uff.ic.archd.javacode.JavaConstructorService;
import br.uff.ic.archd.model.Project;
import br.uff.ic.dyevc.application.branchhistory.model.ProjectRevisions;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author wallace
 */
public class CreateMiningFile {
    
    public static final int MIN_NUMBER = 1001;

    public void createMethodsFileMining(ProjectRevisions newProjectRevisions, Project project, JavaConstructorService javaConstructorService) {
        TransformFromTuple transformFromTuple = new TransformFromTuple();
        List<Sequence> sequenceList = transformFromTuple.transfFromMethodsToTuples(newProjectRevisions, project, javaConstructorService);
        try {
            //tranformando em numeros
            String path = System.getProperty("user.home") + "/.archd/";
            PrintWriter writer0 = new PrintWriter(path + "map_number_to_text.txt", "UTF-8");
            HashMap<Integer, String> numberToNameMap = new HashMap();
            HashMap<String, Integer> nameToNumberMap = new HashMap();
            int x = CreateMiningFile.MIN_NUMBER;
            for (Sequence sequence : sequenceList) {
                if (!sequence.getItemSet().isEmpty()) {
                    for (String itemSet : sequence.getItemSet()) {
                        String itens[] = itemSet.split(" ");
                        for (int i = 0; i < itens.length; i++) {
                            if(nameToNumberMap.get(itens[i]) == null){
                                nameToNumberMap.put(itens[i], x);
                                numberToNameMap.put(x, itens[i]);
                                writer0.println(x+" "+itens[i]);
                                x++;
                            }
                        }
                    }
                }
            }
            writer0.close();
            
            PrintWriter writer1 = new PrintWriter(path + "data_to_mining_methods.txt", "UTF-8");
            for (Sequence sequence : sequenceList) {
                if (!sequence.getItemSet().isEmpty()) {
                    for (String itemSet : sequence.getItemSet()) {
                        String itens[] = itemSet.split(" ");
                        for (int i = 0; i < itens.length; i++) {
                            writer1.print(nameToNumberMap.get(itens[i]) + " ");
                        }
                        writer1.print("-1 ");
                    }
                    writer1.println("-2 ");
                }
            }
            writer1.close();
        } catch (Exception e) {
            System.out.println("Erro CreateFileMining.createFileMining: " + e.getMessage());
        }
    }

    /*public void createClassesFileMining(ProjectRevisions newProjectRevisions, Project project, JavaConstructorService javaConstructorService) {
        TransformFromTuple transformFromTuple = new TransformFromTuple();
        List<Sequence> sequenceList = transformFromTuple.transfFromClassesToTuples(newProjectRevisions, project, javaConstructorService);
        try {
            String path = System.getProperty("user.home") + "/.archd/";
            PrintWriter writer1 = new PrintWriter(path + "data_to_mining_classes.txt", "UTF-8");
            for (Sequence sequence : sequenceList) {
                if (!sequence.getItemSet().isEmpty()) {
                    for (String itemSet : sequence.getItemSet()) {
                        String itens[] = itemSet.split(" ");
                        for (int i = 0; i < itens.length; i++) {
                            writer1.print(transformFromTuple.changeTextToNumber(itens[i]) + " ");
                        }
                        writer1.print("-1 ");
                    }
                    writer1.println("-2 ");
                }
            }
            writer1.close();
        } catch (Exception e) {
            System.out.println("Erro CreateFileMining.createClassesFileMining: " + e.getMessage());
        }
    }*/

    public void createFinalText() {
        TransformFromTuple transformFromTuple = new TransformFromTuple();
        try {
            String path = System.getProperty("user.home") + "/.archd/";
            HashMap<Integer, String> numberToNameMap = new HashMap();
            String sCurrentLine;
            BufferedReader br = new BufferedReader(new FileReader(path + "map_number_to_text.txt"));
            while ((sCurrentLine = br.readLine()) != null) {
                String text[] = sCurrentLine.split(" ");
                numberToNameMap.put(Integer.valueOf(text[0]), text[1]);
            }
            
            
            
            PrintWriter writer1 = new PrintWriter(path + "rules_from_mining_methods_in_text.txt", "UTF-8");
            br = new BufferedReader(new FileReader(path + "rules_from_mining_methods.txt"));

            int numberOfSequences = 1188;
            HashMap<String, Integer> suportes = new HashMap();
            while ((sCurrentLine = br.readLine()) != null) {
                String text[] = sCurrentLine.split("#");
                suportes.put(text[0], Integer.valueOf(text[1].split(" ")[1]));
            }
            br = new BufferedReader(new FileReader(path + "rules_from_mining_methods.txt"));
            while ((sCurrentLine = br.readLine()) != null) {
                String text[] = sCurrentLine.split("#");
                String sequenceStr[] = text[0].split("-1");
                //vendo os antecessores
                String antecessor = "";
                for (int j = 0; j < sequenceStr.length - 2; j++) {
                    antecessor = antecessor+sequenceStr[j]+"-1";
                }
                double confidence = 0;
                if(!antecessor.equals("")){
                    antecessor = antecessor+" ";
                    Integer value = suportes.get(antecessor);
                    if(value != null){
                        confidence = Integer.valueOf(text[1].split(" ")[1]);
                        confidence = confidence/value;
                    }
                    
                }
                boolean imprimir = false;
                String itemAux[] = sequenceStr[sequenceStr.length - 2].split(" ");
                //System.out.println("Ultimo: "+sequenceStr[sequenceStr.length - 2]);
                for (int j = 0; j < itemAux.length; j++) {
                    if (!itemAux[j].equals("")) {
                        if (Integer.valueOf(itemAux[j]) >= 45) {
                            imprimir = true;
                            break;
                        }
                    }
                }
                if (imprimir && confidence >= 0.9) {
                    for (int i = 0; i < sequenceStr.length; i++) {
                        if (!sequenceStr[i].equals("")) {
                            String itemSet[] = sequenceStr[i].split(" ");
                            for (int j = 0; j < itemSet.length; j++) {
                                if (!itemSet[j].equals("")) {
                                    writer1.print(numberToNameMap.get(Integer.valueOf(itemSet[j])) + " , ");
                                }
                            }
                            writer1.print(" => ");
                        }
                    }
                    double porcentagem = 0;
                    String auxStr = sequenceStr[sequenceStr.length - 2]+"-1 ";
                    auxStr = auxStr.substring(1);
                    //System.out.println("Value: "+auxStr);
                    Integer value = suportes.get(auxStr);
                    double lift = 0;
                    if(value != null){
                        porcentagem = Integer.valueOf(text[1].split(" ")[1]);
                        porcentagem = porcentagem/value;
                        lift = value;
                        lift = lift/numberOfSequences;
                        lift = confidence/lift;
                    }
                    
                    writer1.println(text[1]+"   confianca: "+(confidence*100)+" %    lift: "+lift+" %");
                }
            }

            writer1.close();
            System.out.println("Terminou de criar texto final");
        } catch (Exception e) {
            System.out.println("Erro CreateFileMining.createFinalText: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        CreateMiningFile createMiningFile = new CreateMiningFile();
        createMiningFile.createFinalText();
        
    }
}
