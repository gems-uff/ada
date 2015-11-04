/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.service.mining;

import ca.pfv.spmf.algorithms.frequentpatterns.apriori.AlgoApriori;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.AlgoSPADE;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.candidatePatternsGeneration.CandidateGenerator;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.candidatePatternsGeneration.CandidateGenerator_Qualitative;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.creators.AbstractionCreator;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.creators.AbstractionCreator_Qualitative;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.database.SequenceDatabase;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.idLists.creators.IdListCreator;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.idLists.creators.IdListCreator_Bitmap;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.idLists.creators.IdListCreator_FatBitmap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

/**
 *
 * @author wallace
 */
public class SpadeAlgorithmRun {

//    public static void main(String args[]) {
//        HashMap<Integer, Integer> hash = new HashMap();
//
//        String path = System.getProperty("user.home") + "/.archd/";
//        String sCurrentLine;
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(path + "map_archtecture_flaws.txt"));
//            while ((sCurrentLine = br.readLine()) != null) {
//                String text[] = sCurrentLine.trim().split(" ");
//                for (int i = 0; i < text.length; i++) {
//                    hash.put(Integer.valueOf(text[i]), Integer.valueOf(text[i]));
//                    //System.out.print("     Flaw: " + Integer.valueOf(text[i]));
//                }
//                
//            }
//        } catch (Exception e) {
//            System.out.println("Erro: " + e.getMessage());
//            e.printStackTrace();
//        }
//        System.out.println("");
//
//
//        double support = 0.02;
//
//        String inputFile = path+"FILES_TO_MINING/MapDB/data_to_mining_methods.txt";
//
//        String outputFile = path+"FILES_TO_MINING/MapDB/rules_from_mining_methods.txt";
//
//        ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.creators.AbstractionCreator abstractionCreator = ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.creators.AbstractionCreator_Qualitative.getInstance();
//        IdListCreator idListCreator = IdListCreator_FatBitmap.getInstance();
//        CandidateGenerator candidateGenerator = CandidateGenerator_Qualitative.getInstance();
//        ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.database.SequenceDatabase sd = new ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.database.SequenceDatabase(abstractionCreator, idListCreator);
//        try {
//            sd.loadFile(inputFile, support);
//
//            System.out.println("Rodando SpecialAlgoSpade");
//            System.out.println("Intput: "+inputFile);
//            System.out.println("Output: "+outputFile);
//            SpecialAlgoSpade specialSpade = new SpecialAlgoSpade(support, true, abstractionCreator);
//            specialSpade.runAlgorithmParallelized(sd, candidateGenerator, true, true, outputFile, hash);
//            System.out.println("Terminou de rodar SpecialAlgoSpade");
//
//        } catch (Exception e) {
//            System.out.println("Erro: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
}
