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
public class ProjectAnomalies {

    private HashMap<String, GenericAnomalies> packagesHashMap;
    private HashMap<String, GenericAnomalies> classesHashMap;
    private HashMap<String, GenericAnomalies> methodsHashMap;
    private HashMap<String, String> anomalies;
    private int numberOfRevisions;

    public ProjectAnomalies(int numberOfRevisions) {
        this.numberOfRevisions = numberOfRevisions;
        packagesHashMap = new HashMap();
        classesHashMap = new HashMap();
        methodsHashMap = new HashMap();
        anomalies = new HashMap();
    }

    public void addPackageAnomalie(String packageName, String anomalieName, int revisionNumber, int revisionBirthNumber, int classBirthNumber) {
        GenericAnomalies genericAnomalies = packagesHashMap.get(packageName);
        String anomalieStr = anomalies.get(anomalieName);
        if (anomalieStr == null) {
            anomalies.put(anomalieName, anomalieName);
        }
        if (genericAnomalies == null) {
            genericAnomalies = new GenericAnomalies(packageName, numberOfRevisions, revisionBirthNumber, classBirthNumber);
            packagesHashMap.put(packageName, genericAnomalies);
        }
        genericAnomalies.addAnomalie(anomalieName, revisionNumber);
    }

    public void addClassAnomalie(String className, String alternativeName,  String anomalieName, int revisionNumber, int revisionBirthNumber, int classBirthNumber) {
        GenericAnomalies genericAnomalies = classesHashMap.get(className);
        String anomalieStr = anomalies.get(anomalieName);
        if (anomalieStr == null) {
            anomalies.put(anomalieName, anomalieName);
        }
        if (genericAnomalies == null) {
            genericAnomalies = new GenericAnomalies(className, numberOfRevisions, revisionBirthNumber, classBirthNumber);
            classesHashMap.put(className, genericAnomalies);
        }
        genericAnomalies.addAlternativeName(alternativeName);
        genericAnomalies.addAnomalie(anomalieName, revisionNumber);
        genericAnomalies.setGenericLastName(alternativeName);
    }

    public void addMethodAnomalie(String methodName, String alternativeName, String anomalieName, int revisionNumber, int revisionBirthNumber, int classBirthNumber) {
        GenericAnomalies genericAnomalies = methodsHashMap.get(methodName);
        String anomalieStr = anomalies.get(anomalieName);
        if (anomalieStr == null) {
            anomalies.put(anomalieName, anomalieName);
        }
        if (genericAnomalies == null) {
            genericAnomalies = new GenericAnomalies(methodName, numberOfRevisions, revisionBirthNumber, classBirthNumber);
            methodsHashMap.put(methodName, genericAnomalies);
        }
        genericAnomalies.addAlternativeName(alternativeName);
        genericAnomalies.addAnomalie(anomalieName, revisionNumber);
        genericAnomalies.setGenericLastName(alternativeName);
    }

    public List<String> getAnomalies() {
        List<String> anomaliesList = new LinkedList();
        Collection<String> anomaliesCollection = anomalies.values();
        Iterator it = anomaliesCollection.iterator();
        while (it.hasNext()) {
            String anomalieStr = (String) it.next();
            anomaliesList.add(anomalieStr);
        }
        return anomaliesList;
    }

    public GenericAnomalies getPackageAnomalies(String str) {
        return packagesHashMap.get(str);
    }

    public GenericAnomalies getClassAnomalies(String str) {
        return classesHashMap.get(str);
    }

    public GenericAnomalies getMethodAnomalies(String str) {
        return methodsHashMap.get(str);
    }

    public List<String> getPackages() {
        List<String> auxList = new LinkedList();
        Set<String> auxSet = packagesHashMap.keySet();
        Iterator it = auxSet.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            auxList.add(str);
        }
        return auxList;
    }

    public List<String> getPackagesByAnomalie(String anomalieName) {
        List<String> auxList = new LinkedList();
        Set<String> auxSet = packagesHashMap.keySet();
        Iterator it = auxSet.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (packagesHashMap.get(str).haveAnomalie(anomalieName)) {
                auxList.add(str);
            }
        }
        return auxList;
    }

    public List<String> getClasses() {
        List<String> auxList = new LinkedList();
        Set<String> auxSet = classesHashMap.keySet();
        Iterator it = auxSet.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();

            auxList.add(str);
        }
        return auxList;
    }
    
    public List<String> getClassesByAnomalie(String anomalieName) {
        List<String> auxList = new LinkedList();
        Set<String> auxSet = classesHashMap.keySet();
        Iterator it = auxSet.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (classesHashMap.get(str).haveAnomalie(anomalieName)) {
                auxList.add(str);
            }
        }
        return auxList;
    }

    public List<String> getMethods() {
        List<String> auxList = new LinkedList();
        Set<String> auxSet = methodsHashMap.keySet();
        Iterator it = auxSet.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            auxList.add(str);
        }
        return auxList;
    }
    
    public List<String> getMethodsByAnomalie(String anomalieName) {
        List<String> auxList = new LinkedList();
        Set<String> auxSet = methodsHashMap.keySet();
        Iterator it = auxSet.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (methodsHashMap.get(str).haveAnomalie(anomalieName)) {
                auxList.add(str);
            }
        }
        return auxList;
    }
    
    public void classifyAnomalies(){
        Collection<GenericAnomalies> anomaliesCollection = packagesHashMap.values();
        Iterator it = anomaliesCollection.iterator();
        while (it.hasNext()) {
            GenericAnomalies genericAnomalie = (GenericAnomalies) it.next();
            genericAnomalie.classifyAnomalies();
        }
        anomaliesCollection = classesHashMap.values();
        it = anomaliesCollection.iterator();
        while (it.hasNext()) {
            GenericAnomalies genericAnomalie = (GenericAnomalies) it.next();
            genericAnomalie.classifyAnomalies();
        }
        anomaliesCollection = methodsHashMap.values();
        it = anomaliesCollection.iterator();
        while (it.hasNext()) {
            GenericAnomalies genericAnomalie = (GenericAnomalies) it.next();
            genericAnomalie.classifyAnomalies();
        }
    }
}
