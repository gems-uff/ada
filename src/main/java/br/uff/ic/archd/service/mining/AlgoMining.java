/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.service.mining;

import ca.pfv.spmf.algorithms.associationrules.IGB.AlgoIGB;

/**
 *
 * @author wallace
 */
public class AlgoMining {

    public static void main(String args[]) {

        try {



            String path = System.getProperty("user.home") + "/.archd/";
            String inputFile = path + "map_add_methods_difference_to_mining.txt";
            String outputFile = path + "map_add_methods_difference_to_mining_output.txt";
            double minsup = 0.1;
            double minconf = 1;
            double minlift = 1;

            ca.pfv.spmf.algorithms.frequentpatterns.fpgrowth.AlgoFPGrowth fpgrowth = new ca.pfv.spmf.algorithms.frequentpatterns.fpgrowth.AlgoFPGrowth();
            ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemsets patterns = fpgrowth.runAlgorithm(inputFile, null, minsup);
            fpgrowth.printStats();
            int databaseSize = fpgrowth.getDatabaseSize();

            // STEP 2: Generating all rules from the set of frequent itemsets (based on Agrawal & Srikant, 94)
            SpecialAlgoAgrawalFaster94 algoAgrawal = new SpecialAlgoAgrawalFaster94();
            algoAgrawal.runAlgorithm(patterns, outputFile, databaseSize, minconf, minlift);
            algoAgrawal.printStats();
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
