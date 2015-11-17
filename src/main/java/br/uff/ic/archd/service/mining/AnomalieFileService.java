/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.service.mining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author wallace
 */
public class AnomalieFileService {

    String path = System.getProperty("user.home") + "/.archd/anomalies_evolution/";

    private int PACKAGE_TYPE = 1;
    private int CLASS_TYPE = 2;
    private int METHOD_TYPE = 3;

    public boolean anomalieIsInFile(String projectName, int revisionNumber) {
        int revision = 0;
        String fileStr = path + projectName;
        File file = new File(fileStr);
        if (file.exists()) {
            fileStr = fileStr + "/.revisionNumber.txt";
            file = new File(fileStr);
            if (file.exists()) {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(fileStr));
                    String line = null;
                    line = reader.readLine();
                    if (line != null && !line.equals("")) {
                        revision = Integer.valueOf(line);
                    }
                    reader.close();
                } catch (Exception e) {
                    System.out.println("Erro anomalieIsInFile: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return revision == revisionNumber;
    }

    public void saveAnomalie(ProjectAnomalies projectAnomalies, String projectName, int revisions) {
        String fileStr = path + projectName;
        try {
            if (new File(fileStr).exists()) {
                FileUtils.deleteDirectory(new File(fileStr));
            }
            new File(fileStr).mkdirs();
        } catch (Exception e) {
            System.out.println("");
        }

        //setar pacotes
        String fileDir = fileStr + "/packages";
        int type = PACKAGE_TYPE;
        writeAnomalies(type, projectAnomalies, fileDir);

        //setar classes
        fileDir = fileStr + "/classes";
        type = CLASS_TYPE;
        writeAnomalies(type, projectAnomalies, fileDir);

        //setar métodos
        fileDir = fileStr + "/methods";
        type = METHOD_TYPE;
        writeAnomalies(type, projectAnomalies, fileDir);

        try {
            PrintWriter writer0 = new PrintWriter(fileStr + "/.revisionNumber.txt", "UTF-8");
            writer0.println(revisions);
            writer0.close();
        } catch (Exception e) {
            System.out.println("Erro saveAnomalie:" + e.getMessage());
            e.printStackTrace();
        }

    }

    private void writeAnomalies(int type, ProjectAnomalies projectAnomalies, String fileDir) {
        List<String> artefacts = null;
        if (type == PACKAGE_TYPE) {
            artefacts = projectAnomalies.getPackages();
        } else if (type == CLASS_TYPE) {
            artefacts = projectAnomalies.getClasses();
        } else if (type == METHOD_TYPE) {
            artefacts = projectAnomalies.getMethods();
            System.out.println("METHODS ARTEFACTS: " + artefacts.size());
        }

        for (String artefact : artefacts) {
            GenericAnomalies genericAnomalies = null;
            String artefactDir = artefact;
            if (type == PACKAGE_TYPE) {
                genericAnomalies = projectAnomalies.getPackageAnomalies(artefact);
            } else if (type == CLASS_TYPE) {
                genericAnomalies = projectAnomalies.getClassAnomalies(artefact);
            } else if (type == METHOD_TYPE) {
                genericAnomalies = projectAnomalies.getMethodAnomalies(artefact);
                System.out.println("Method artefact: " + artefact);
                String art[] = artefact.split(":");
                System.out.println("Size: " + art.length);
                String art2[] = art[1].split("\\(");
                System.out.println("Size: " + art2.length);
                artefactDir = art[0] + "/" + art2[0] + "/" + art2[1];
                System.out.println("Method artifact Dir: " + artefactDir);
            }

            List<String> anomalies = genericAnomalies.getAnomalies();
            File file = new File(fileDir + "/" + artefactDir + "/");
            file.mkdirs();

            try {
//                file = new File(fileDir + "/" + artefact + "/artifactFeatures.txt", "UTF-8");
//                file.createNewFile();
                System.out.println("File: " + fileDir + "/" + artefactDir + "/artifactFeatures.txt");
                PrintWriter writer0 = new PrintWriter(fileDir + "/" + artefactDir + "/artifactFeatures.txt", "UTF-8");
                //artifact birth number
                writer0.println(genericAnomalies.getArtifactBirthNumber());
                //parent artifact birth number
                writer0.println(genericAnomalies.getParentArtifactBirthNumber());
                //number of revisions
                writer0.println(genericAnomalies.getNumberOfRevisions());
                //parent generic name
                writer0.println(genericAnomalies.getGenericName());
                //parent generic last name
                writer0.println(genericAnomalies.getGenericLastName());
                //alternative names
                for (String alternativeName : genericAnomalies.getAlternativeNames()) {
                    writer0.print(alternativeName + " ");
                }
                writer0.println();
                writer0.close();

            } catch (Exception e) {
                System.out.println("Erro writeAnomalies:" + e.getMessage());
                e.printStackTrace();
            }

            for (String anomalieName : anomalies) {
                AnomalieList anomalieList = genericAnomalies.getAnomalieList(anomalieName);

                String fileAnomalie = fileDir + "/" + artefactDir + "/" + anomalieName + ".txt";

                try {
//                    file = new File(fileAnomalie, "UTF-8");
//                    file.createNewFile();
                    System.out.println("File anomalie: " + fileAnomalie);
                    PrintWriter writer0 = new PrintWriter(fileAnomalie, "UTF-8");
                    //escreve o tipo de anomalia
                    writer0.println(anomalieList.getTypeOfAnomalie());

                    //anomalie birth number
                    writer0.println(anomalieList.getAnomalieBirthNumber());
                    //number of revisions without anomalies
                    writer0.println(anomalieList.getNumberOfRevisionsWithoutAnomalie());
                    //number of revisions with anomalies
                    writer0.println(anomalieList.getNumberOfRevisionsWithAnomalie());
                    // is congenital
                    writer0.println(anomalieList.isIsCongenital());
                    //is corrected
                    writer0.println(anomalieList.isIsCorrected());
                    //after parent artifact
                    writer0.println(anomalieList.isBornAfterParentArtifact());
                    //reccurrence level
                    writer0.println(anomalieList.getRecurrenceLevel());
                    //boolean list
                    for (Boolean anomalieBool : anomalieList.getList()) {
                        writer0.print((anomalieBool ? "1" : "0") + " ");
                    }
                    writer0.println();

                    writer0.close();

                } catch (Exception e) {
                    System.out.println("Erro writeAnomalies:" + e.getMessage());
                    e.printStackTrace();
                }

            }

        }
    }

    public ProjectAnomalies getAnomalie(String projectName) {
        String fileStr = path + projectName;
        int numberOfRevisions = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileStr + "/.revisionNumber.txt"));
            String line = null;
            line = reader.readLine();
            numberOfRevisions = Integer.valueOf(line);
            reader.close();
        } catch (Exception e) {
            System.out.println("Erro getAnomalie: " + e.getMessage());
            e.printStackTrace();
        }
        if (numberOfRevisions == 0) {
            return null;
        }
        ProjectAnomalies projectAnomalies = new ProjectAnomalies(numberOfRevisions);
        //pegar os pacotes
        List<String> artifactNames = new LinkedList();
        File file = new File(fileStr + "/packages");
        if (file.exists()) {
            String files[] = file.list();
            for (int i = 0; i < files.length; i++) {
                artifactNames.add(files[i]);
            }
            int type = PACKAGE_TYPE;
            getAnomalies(artifactNames, artifactNames, fileStr + "/packages", projectAnomalies, type);
        }

        //pegar as classes
        artifactNames = new LinkedList();
        file = new File(fileStr + "/classes");
        if (file.exists()) {
            String files[] = file.list();
            for (int i = 0; i < files.length; i++) {
                artifactNames.add(files[i]);
            }
            int type = CLASS_TYPE;
            getAnomalies(artifactNames, artifactNames, fileStr + "/classes", projectAnomalies, type);
        }

        //pegar os metodos
        System.out.println("PEGAR METHODS ARTIFACTS: ");
        artifactNames = new LinkedList();
        List artifactDirs = new LinkedList();
        file = new File(fileStr + "/methods");
        System.out.println("File: " + file.getAbsolutePath());
        if (file.exists()) {
            String files[] = file.list();
            for (int i = 0; i < files.length; i++) {
                //nome da classe
                if (!files[i].startsWith(".")) {
                    File fileClass = new File(file.getAbsolutePath() + "/" + files[i]);
                    System.out.println("File class: " + fileClass.getAbsolutePath());
                    String filesMethods[] = fileClass.list();
                    for (int j = 0; j < filesMethods.length; j++) {
                        //nome do metodo
                        if (!filesMethods[j].startsWith(".")) {
                            File fileMethod = new File(fileClass.getAbsolutePath() + "/" + filesMethods[j]);
                            System.out.println("File Method: " + fileMethod.getAbsolutePath());
                            String fileParams[] = fileMethod.list();
                            for (int k = 0; k < fileParams.length; k++) {
                                if (!fileParams[k].startsWith(".")) {
                                    String artifactPath = files[i] + "/" + filesMethods[j] + "/" + fileParams[k];
                                    String artifactName = files[i] + ":" + filesMethods[j] + "(" + fileParams[k];
                                    System.out.println("artpath: " + artifactPath);
                                    System.out.println("artName: " + artifactName);
                                    artifactDirs.add(artifactPath);
                                    artifactNames.add(artifactName);
                                }
                            }
                        }
                    }
                }
                //artifactNames.add(files[i]);
            }
            int type = METHOD_TYPE;
            getAnomalies(artifactNames, artifactDirs, fileStr + "/methods", projectAnomalies, type);
        }

        projectAnomalies.classifyAnomalies();

        return projectAnomalies;

    }

    private void getAnomalies(List<String> artifactNames, List<String> artifactDirs, String fileStr, ProjectAnomalies projectAnomalies, int type) {
        for (int index = 0; index < artifactNames.size(); index++) {
            String artifactPath = artifactDirs.get(index);
            String artifactName = artifactNames.get(index);
            //if(!artifactPath.equals(artifactName)){
            System.out.println("ArtiName: " + artifactName);
            System.out.println("ArtiPath: " + artifactPath);
            //}
            String fileDir = fileStr + "/" + artifactPath;
            try {
                BufferedReader reader = new BufferedReader(new FileReader(fileDir + "/artifactFeatures.txt"));
                String line = null;
                //artifact birth number
                line = reader.readLine();
                int artifactBirthNumber = Integer.valueOf(line);
                //parent artifact birth number
                line = reader.readLine();
                int parentArtifactBirthNumber = Integer.valueOf(line);
                //number of revisions
                line = reader.readLine();
                int numberOfRevisions = Integer.valueOf(line);
                //parent generic name
                line = reader.readLine();
                String genericName = line;
                //parent generic last name
                line = reader.readLine();
                String genericLastName = line;
                //alternative names
                line = reader.readLine();
                List<String> alternativeNames = new LinkedList();
                String atifactNamesArray[] = line.split(" ");
                for (int i = 0; i < atifactNamesArray.length; i++) {
                    if (!atifactNamesArray[i].equals("")) {
                        alternativeNames.add(atifactNamesArray[i]);
                    }
                }
                reader.close();

                File file = new File(fileDir);
                String files[] = file.list();
                for (int i = 0; i < files.length; i++) {
                    if (!files[i].equals("artifactFeatures.txt")) {
                        //pegar o arquivo
                        String anomalieFile = fileDir + "/" + files[i];
                        System.out.println("File: " + files[i]);
                        String anomalieName = files[i].split("\\.")[0];

                        reader = new BufferedReader(new FileReader(anomalieFile));
                        line = null;
                        //tipo de anomalia
                        line = reader.readLine();
                        //anomalie birth number
                        line = reader.readLine();
                        int anomalieBirthNumber = Integer.valueOf(line);
                        //number of revisions without anomalies
                        line = reader.readLine();
                        //number of revisions with anomalies
                        line = reader.readLine();
                        // is congenital
                        line = reader.readLine();
                        //is corrected
                        line = reader.readLine();
                        //after parent artifact
                        line = reader.readLine();
                        //reccurrence level
                        line = reader.readLine();
                        //boolean list
                        line = reader.readLine();
                        String booleanArray[] = line.split(" ");
                        for (int j = 0; j < booleanArray.length; j++) {
                            if (booleanArray[j].equals("1")) {
                                if (type == PACKAGE_TYPE) {
                                    projectAnomalies.addPackageAnomalie(artifactName, anomalieName, j + artifactBirthNumber, artifactBirthNumber, parentArtifactBirthNumber);
                                } else if (type == CLASS_TYPE) {
                                    projectAnomalies.addClassAnomalie(artifactName, artifactName, anomalieName, j + artifactBirthNumber, artifactBirthNumber, parentArtifactBirthNumber);
                                } else if (type == METHOD_TYPE) {
                                    projectAnomalies.addMethodAnomalie(artifactName, artifactName, anomalieName, j + artifactBirthNumber, artifactBirthNumber, parentArtifactBirthNumber);
                                }
                            }else{
                                if (type == PACKAGE_TYPE) {
                                    projectAnomalies.addPackageNotAnomalie(artifactName, anomalieName, j + artifactBirthNumber, artifactBirthNumber, parentArtifactBirthNumber);
                                } else if (type == CLASS_TYPE) {
                                    projectAnomalies.addClassNotAnomalie(artifactName, artifactName, anomalieName, j + artifactBirthNumber, artifactBirthNumber, parentArtifactBirthNumber);
                                } else if (type == METHOD_TYPE) {
                                    projectAnomalies.addMethodNotAnomalie(artifactName, artifactName, anomalieName, j + artifactBirthNumber, artifactBirthNumber, parentArtifactBirthNumber);
                                }
                            }
                        }

                        reader.close();
                    }

                }
                if (type == PACKAGE_TYPE) {
                    GenericAnomalies genericAnomalies = projectAnomalies.getPackageAnomalies(artifactName);
                    for (String alternativeName : alternativeNames) {
                        genericAnomalies.addAlternativeName(alternativeName);
                    }
                    genericAnomalies.setGenericLastName(genericLastName);

                } else if (type == CLASS_TYPE) {
                    GenericAnomalies genericAnomalies = projectAnomalies.getClassAnomalies(artifactName);
                    for (String alternativeName : alternativeNames) {
                        genericAnomalies.addAlternativeName(alternativeName);
                    }
                    genericAnomalies.setGenericLastName(genericLastName);
                } else if (type == METHOD_TYPE) {
                    GenericAnomalies genericAnomalies = projectAnomalies.getMethodAnomalies(artifactName);
                    for (String alternativeName : alternativeNames) {
                        genericAnomalies.addAlternativeName(alternativeName);
                    }
                    genericAnomalies.setGenericLastName(genericLastName);
                }

            } catch (Exception e) {
                System.out.println("Erro getAnomalies: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
