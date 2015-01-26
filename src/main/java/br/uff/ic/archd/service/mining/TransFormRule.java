/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.service.mining;

import br.uff.ic.archd.javacode.JavaAbstract;
import br.uff.ic.archd.javacode.JavaClass;
import br.uff.ic.archd.javacode.JavaProject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author wallace
 */
public class TransFormRule {

    public List<String> getRuleFromClasses(JavaProject javaProjectBefore, JavaProject javaProjectAfter) {
        String rule = "";

        List<String> rules = new ArrayList();

        //number of classes
        int numberBefore = javaProjectBefore.getClasses().size();
        int numberAfter = javaProjectAfter.getClasses().size();
        if (numberBefore == numberAfter) {
            rule = rule + "number_of_project_classes:=:0";
        } else if (numberAfter > numberBefore) {
            rule = rule + "number_of_project_classes:+:" + (numberAfter - numberBefore);
        } else {
            rule = rule + "number_of_project_classes:-:" + (numberBefore - numberAfter);
        }

        //number of interfaces
        numberBefore = javaProjectBefore.getInterfaces().size();
        numberAfter = javaProjectAfter.getInterfaces().size();
        if (numberBefore == numberAfter) {
            rule = rule + " , " + "number_of_project_interfaces:=:0";
        } else if (numberAfter > numberBefore) {
            rule = rule + " , " + "number_of_project_interfaces:+:" + (numberAfter - numberBefore);
        } else {
            rule = rule + " , " + "number_of_project_interfaces:-:" + (numberBefore - numberAfter);
        }

        //number of methods
        numberBefore = 0;
        for (JavaAbstract javaAbstract : javaProjectBefore.getClasses()) {
            numberBefore = numberBefore + ((JavaClass) javaAbstract).getMethods().size();
        }
        numberAfter = 0;
        for (JavaAbstract javaAbstract : javaProjectAfter.getClasses()) {
            numberAfter = numberAfter + ((JavaClass) javaAbstract).getMethods().size();
        }
        if (numberBefore == numberAfter) {
            rule = rule + " , " + "number_of_project_methods:=:0";
        } else if (numberAfter > numberBefore) {
            rule = rule + " , " + "number_of_project_methods:+:" + (numberAfter - numberBefore);
        } else {
            rule = rule + " , " + "number_of_project_methods:-:" + (numberBefore - numberAfter);
        }

        //number of packages
        numberBefore = javaProjectBefore.getPackages().size();
        numberAfter = javaProjectAfter.getPackages().size();
        if (numberBefore == numberAfter) {
            rule = rule + " , " + "number_of_project_packages:=:0";
        } else if (numberAfter > numberBefore) {
            rule = rule + " , " + "number_of_project_packages:+:" + (numberAfter - numberBefore);
        } else {
            rule = rule + " , " + "number_of_project_packages:-:" + (numberBefore - numberAfter);
        }

        //regras para cada classe
        for (JavaAbstract javaAbstract : javaProjectAfter.getClasses()) {
            String ruleClass = rule;
            JavaClass jc = (JavaClass) javaAbstract;
            JavaClass antClass = null;
            JavaAbstract antAbstract = javaProjectBefore.getClassByName(jc.getFullQualifiedName());
            if (antAbstract != null) {
                if (antAbstract.getClass() == JavaClass.class) {
                    antClass = (JavaClass) javaProjectBefore.getClassByName(jc.getFullQualifiedName());
                } else {
                    ruleClass = ruleClass + " , " + "change_from_interface_to_class";
                }
            }

            if (antClass != null) {
            }



            rules.add(ruleClass);
        }



        return rules;

    }

    public String getChangeFromInterfaceToClass(JavaAbstract javaAbstract, String rule, JavaProject javaProjectBefore) {
        String ruleClass = rule;
        JavaClass jc = (JavaClass) javaAbstract;
        JavaClass antClass = null;
        JavaAbstract antAbstract = javaProjectBefore.getClassByName(jc.getFullQualifiedName());
        if (antAbstract != null) {
            if (antAbstract.getClass() == JavaClass.class) {
                antClass = (JavaClass) javaProjectBefore.getClassByName(jc.getFullQualifiedName());
            } else {
                ruleClass = ruleClass + " , " + "change_from_interface_to_class";
            }
        }
        
        return ruleClass;
    }

    public void verifieGodClass(JavaProject javaProjectBefore, JavaProject javaProjectAfter) {
        List<JavaClass> topValuesClasses = new LinkedList();
        List<JavaClass> auxList = new LinkedList();
        List<JavaClass> godClassList = new LinkedList();
        auxList.add((JavaClass) javaProjectBefore.getClasses().get(0));
        for (int i = 1; i < javaProjectBefore.getClasses().size(); i++) {
            JavaClass javaClass = (JavaClass) javaProjectBefore.getClasses().get(i);
            boolean inseriu = false;
            for (int j = 0; j < auxList.size(); j++) {
                JavaClass jc2 = auxList.get(j);
                if (javaClass.getAccessToForeignDataNumber() > jc2.getAccessToForeignDataNumber()) {
                    auxList.add(j, javaClass);
                    inseriu = true;
                    break;
                }
            }
            if (!inseriu) {
                auxList.add(javaClass);
            }
        }
        int topNumber = javaProjectBefore.getClasses().size() / 5;
        if (topNumber * 5 != javaProjectBefore.getClasses().size()) {
            topNumber++;
        }
        for (int i = 0; i < topNumber; i++) {
            topValuesClasses.add(auxList.get(i));
        }


        for (JavaClass javaClass : topValuesClasses) {
            double tcc = javaClass.getNumberOfDirectConnections();
            int n = javaClass.getMethods().size();
            tcc = tcc / ((n * (n - 1)) / 2);
            if ((javaClass.getAccessToForeignDataNumber() >= 4)
                    && (javaClass.getTotalCyclomaticComplexity() >= 20)
                    && (tcc <= 0.33)) {
                godClassList.add(javaClass);
            }
        }
        if (!godClassList.isEmpty()) {
            //writer10.println("\n\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ REVISION: " + jp.getRevisionId() + "      num: " + k + "");
            for (JavaClass javaClass : godClassList) {
                double tcc = javaClass.getNumberOfDirectConnections();
                int n = javaClass.getMethods().size();
                tcc = tcc / ((n * (n - 1)) / 2);
                //writer10.println("******* CLASS: " + javaClass.getFullQualifiedName() + "        AFDN: " + javaClass.getAccessToForeignDataNumber() + "    WMC: " + javaClass.getTotalCyclomaticComplexity() + "     TCC: " + tcc);
            }
        }


    }
}
