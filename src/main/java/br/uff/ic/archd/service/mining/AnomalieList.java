/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.service.mining;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author wallace
 */
public class AnomalieList {

    private List<Boolean> list;
    private int typeOfAnomalie;
    private int artifactBirthNumber;
    private int anomalieBirthNumber;
    private int parentArtifactBirthNumber;
    private int numberOfRevisionsWithoutAnomalie;
    private int numberOfRevisionsWithAnomalie;
    
    private boolean isCongenital;
    private boolean isCorrected;
    private boolean birthAfterParentArtifact;
    private int recurrenceLevel;
    private int last;

    AnomalieList(int numberOfRevisions, int artifactBirthNumber, int parentArtifactBirthNumber) {
        this.parentArtifactBirthNumber = parentArtifactBirthNumber;
        this.artifactBirthNumber = artifactBirthNumber;
        list = new LinkedList();
        for (int i = 0; i < numberOfRevisions; i++) {
            list.add(false);
        }
        last = 0;
    }

    /**
     * @return the list
     */
    public List<Boolean> getList() {
        return list;
    }

    public void setAnomalieOcurrence(int k) {
        list.set(k, Boolean.TRUE);
        if(k > last){
            last = k;
        }
    }
    
    public void setNotAnomalieOcurrence(int k) {
        list.set(k, Boolean.FALSE);
        if(k > last){
            last = k;
        }
    }

    public void classifyAnomalie() {
        //System.out.println("Classificacao da anomalia");
        //System.out.println("Revision Birth: "+getRevisionBirthNumber());
        //System.out.println("Size: "+list.size());
        list = list.subList(getArtifactBirthNumber(), list.size());
        last = last - getArtifactBirthNumber();
        //System.out.println("Size depois: "+list.size());
        boolean congenital = false;
        boolean bornWithTheClass = false;
        if(getParentArtifactBirthNumber() == getArtifactBirthNumber()){
            bornWithTheClass = true;
        }
        if (list.get(0)) {
            congenital = true;
        }
        int i = 0;
        if (!congenital) {
            while (i < list.size() && !list.get(i)) {
                i++;
            }
        }
        
        birthAfterParentArtifact = !bornWithTheClass;
        
        anomalieBirthNumber = i;
        int aux = 0;
        if (!congenital) {
            aux = 6;
        }
        if(!bornWithTheClass){
            aux = aux + 12;
        }
        
        isCongenital = congenital;
        int numOfCorrectness = 0;
        while (i < list.size() && list.get(i)) {
            if (list.get(i)) {
                i++;
            }
        }
        if (i < list.size() && !list.get(i)) {
            numOfCorrectness++;
        }
        if (i < list.size()) {
            while (i < list.size() && !list.get(i)) {
                i++;
            }
            while (i < list.size() && list.get(i)) {
                if (list.get(i)) {
                    i++;
                }
            }
            if (i < list.size() && !list.get(i)) {
                numOfCorrectness++;
            }
            if (i < list.size()) {
                while (i < list.size() && !list.get(i)) {
                    i++;
                }
                while (i < list.size() && list.get(i)) {
                    if (list.get(i)) {
                        i++;
                    }
                }
                if (i < list.size() && !list.get(i)) {
                    numOfCorrectness++;
                }
            }
        }
        //se o ultimo tem erro então não foi corrigido
        if (list.get(last)) {
            if (numOfCorrectness == 0) {
                typeOfAnomalie = aux + 1;
            } else if (numOfCorrectness == 1) {
                typeOfAnomalie = aux + 2;
            } else if (numOfCorrectness >= 2) {
                typeOfAnomalie = aux + 3;
            }
            
            recurrenceLevel = numOfCorrectness;
            
            isCorrected = false;
        } else {
            //System.out.println("Number of correctness: " + numOfCorrectness);
            if (numOfCorrectness == 1) {
                typeOfAnomalie = aux + 4;
            } else if (numOfCorrectness == 2) {
                typeOfAnomalie = aux + 5;
            } else if (numOfCorrectness >= 3) {
                typeOfAnomalie = aux + 6;
            }
            
            
            recurrenceLevel = numOfCorrectness-1;
            isCorrected = true;
        }

        numberOfRevisionsWithoutAnomalie = 0;
        numberOfRevisionsWithAnomalie = 0;
        for (Boolean b : list) {
            if (b) {
                numberOfRevisionsWithAnomalie++;
            } else {
                numberOfRevisionsWithoutAnomalie++;
            }
        }
    }

    /**
     * @return the typeOfAnomalie
     */
    public int getTypeOfAnomalie() {
        return typeOfAnomalie;
    }

    /**
     * @return the numberOfRevisionsWithoutAnomalie
     */
    public int getNumberOfRevisionsWithoutAnomalie() {
        return numberOfRevisionsWithoutAnomalie;
    }

    /**
     * @return the numberOfRevisionsWithAnomalie
     */
    public int getNumberOfRevisionsWithAnomalie() {
        return numberOfRevisionsWithAnomalie;
    }

    /**
     * @return the revisionBirthNumber
     */
    public int getArtifactBirthNumber() {
        return artifactBirthNumber;
    }

    /**
     * @return the anomalieBirthNumber
     */
    public int getAnomalieBirthNumber() {
        return anomalieBirthNumber;
    }

    /**
     * @return the classBirthNumber
     */
    public int getParentArtifactBirthNumber() {
        return parentArtifactBirthNumber;
    }

    /**
     * @return the isCongenital
     */
    public boolean isIsCongenital() {
        return isCongenital;
    }
    
    public boolean returnAnomalieInTheFuture(int num){
        boolean returnAnomalie = false;
        for(int i = num; i < list.size(); i++){
            if(list.get(i)){
                returnAnomalie = true;
                break;
            }
        }
        
        return returnAnomalie;
    }

    /**
     * @return the isCorrected
     */
    public boolean isIsCorrected() {
        return isCorrected;
    }

    /**
     * @return the afterSuperiorArtefact
     */
    public boolean isBornAfterParentArtifact() {
        return birthAfterParentArtifact;
    }

    /**
     * @return the recurrenceLevel
     */
    public int getRecurrenceLevel() {
        return recurrenceLevel;
    }
}
