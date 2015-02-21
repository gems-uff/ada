package br.uff.ic.archd.service.mining;

import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.abstractions.ItemAbstractionPair;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.patterns.Pattern;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.savers.Saver;
import java.util.HashMap;

/**
 * This is an implementation of a class implementing the Saver interface. By
 * means of these lines, the user choose to keep his patterns in a file whose
 * path is given to this class.
 *
 * Copyright Antonio Gomariz PeÃ±alver 2013
 *
 * This file is part of the SPMF DATA MINING SOFTWARE
 * (http://www.philippe-fournier-viger.com/spmf).
 *
 * SPMF is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * SPMF is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * SPMF. If not, see <http://www.gnu.org/licenses/>.
 *
 * @author agomariz
 */
public class SpecialSaverIntoFile implements Saver {

    private BufferedWriter writer = null;
    private String path = null;
    private HashMap hashOfPatterns;
    private long temporaryCounter;
    private long counter;

    public SpecialSaverIntoFile(String outputFilePath, HashMap<Integer, Integer> hash) throws IOException {
        path = outputFilePath;
        writer = new BufferedWriter(new FileWriter(outputFilePath));
        this.hashOfPatterns = hash;
        temporaryCounter = 0;
        counter = 0;
    }

    @Override
    public void savePattern(Pattern p) {
        String aux1[] = p.toStringToFile().split("#SUP")[0].split("-1");
        String lastSequence[] = aux1[aux1.length - 2].trim().split(" ");
        boolean flag = true;
        for (int i = 0; i < lastSequence.length; i++) {
            if (hashOfPatterns.get(Integer.valueOf(lastSequence[i])) == null) {
                flag = false;
            }
        }
        //System.out.println("Ultimo elemento: " + aux1[aux1.length - 2] + " ---- numero de elmentos: " + aux1[aux1.length - 2].trim().split(" ").length + " ----      Entra? " + flag);
        if (flag) {
            if (writer != null) {
                // create a stringbuffer
                StringBuilder r = new StringBuilder("");
                // for each itemset in this sequential pattern
                r.append(p.toStringToFile());
                try {
                    // write the string to the file

                    writer.write(r.toString());
                    // start a new line
                    writer.newLine();
                } catch (IOException ex) {
                    Logger.getLogger(SpecialSaverIntoFile.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        temporaryCounter++;
        if(temporaryCounter == 1000000000l){
            temporaryCounter = 0;
            counter++;
            System.out.println("Registros calculados: "+counter+"000000000");
        }
    }

    @Override
    public void finish() {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(SpecialSaverIntoFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void clear() {
        writer = null;
    }

    @Override
    public String print() {
        return "Content at file " + path;
    }

    @Override
    public void savePatterns(Collection<Pattern> patterns) {
        for (Pattern pattern : patterns) {
            this.savePattern(pattern);
        }
    }
}
