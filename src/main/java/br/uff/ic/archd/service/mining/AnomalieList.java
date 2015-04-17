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
    private int revisionBirthNumber;
    private int anomalieBirthNumber;
    private int classBirthNumber;
    private int numberOfRevisionsWithoutAnomalie;
    private int numberOfRevisionsWithAnomalie;

    AnomalieList(int numberOfRevisions, int revisionBirthNumber, int classBirthNumber) {
        this.classBirthNumber = classBirthNumber;
        this.revisionBirthNumber = revisionBirthNumber;
        list = new LinkedList();
        for (int i = 0; i < numberOfRevisions; i++) {
            list.add(false);
        }
    }

    /**
     * @return the list
     */
    public List<Boolean> getList() {
        return list;
    }

    public void setAnomalieOcurrence(int k) {
        list.set(k, Boolean.TRUE);
    }

    public void classifyAnomalie() {
        list = list.subList(getRevisionBirthNumber(), list.size());
        boolean congenital = false;
        boolean bornWithTheClass = false;
        if(getClassBirthNumber() == getRevisionBirthNumber()){
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
        anomalieBirthNumber = i;
        int aux = 0;
        if (!congenital) {
            aux = 6;
        }
        if(!bornWithTheClass){
            aux = aux + 12;
        }
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
        if (list.get(list.size() - 1)) {
            if (numOfCorrectness == 0) {
                typeOfAnomalie = aux + 1;
            } else if (numOfCorrectness == 1) {
                typeOfAnomalie = aux + 2;
            } else if (numOfCorrectness >= 2) {
                typeOfAnomalie = aux + 3;
            }
        } else {
            //System.out.println("Number of correctness: " + numOfCorrectness);
            if (numOfCorrectness == 1) {
                typeOfAnomalie = aux + 4;
            } else if (numOfCorrectness == 2) {
                typeOfAnomalie = aux + 5;
            } else if (numOfCorrectness >= 3) {
                typeOfAnomalie = aux + 6;
            }
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
    public int getRevisionBirthNumber() {
        return revisionBirthNumber;
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
    public int getClassBirthNumber() {
        return classBirthNumber;
    }
}
