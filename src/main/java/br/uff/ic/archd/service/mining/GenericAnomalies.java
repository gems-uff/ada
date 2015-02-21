/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.service.mining;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author wallace
 */
public class GenericAnomalies {
    
    
    private int revisionBirthNumber;
    private String genericName;
    private int numberOfRevisions;
    private HashMap<String, AnomalieList> anomalieHashMap;
    
    public GenericAnomalies(String genericName, int numberOfRevisions, int revisionBirthNumber){
        this.genericName = genericName;
        this.revisionBirthNumber = revisionBirthNumber;
        this.numberOfRevisions = numberOfRevisions;
        anomalieHashMap = new HashMap();
    }
    
    public void addAnomalie(String anomalie, int k){
        AnomalieList anomalieList = anomalieHashMap.get(anomalie);
        if(anomalieList == null){
            anomalieList = new AnomalieList(numberOfRevisions, revisionBirthNumber);
            
            anomalieHashMap.put(anomalie, anomalieList);
        }
        anomalieList.setAnomalieOcurrence(k);
    }

    /**
     * @return the className
     */
    public String getGenericName() {
        return genericName;
    }
    
    public List<String> getAnomalies(){
        List<String> auxList = new LinkedList();
        Set<String> auxSet = anomalieHashMap.keySet();
        Iterator it = auxSet.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            auxList.add(str);
        }
        return auxList;
    }
    
    public AnomalieList getAnomalieList(String anomalieName){
        return anomalieHashMap.get(anomalieName);
    }
    
    public boolean haveAnomalie(String anomalieName){
        if(anomalieHashMap.get(anomalieName) == null){
            return false;
        }else{
            return true;
        }
    }
    
    public void classifyAnomalies(){
        Collection<AnomalieList> anomaliesCollection = anomalieHashMap.values();
        Iterator it = anomaliesCollection.iterator();
        while (it.hasNext()) {
            AnomalieList anomalieList = (AnomalieList) it.next();
            anomalieList.classifyAnomalie();
        }
    }

    /**
     * @return the revisionBirthNumber
     */
    public int getRevisionBirthNumber() {
        return revisionBirthNumber;
    }
}
