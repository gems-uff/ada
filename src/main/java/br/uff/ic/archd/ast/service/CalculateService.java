/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.ast.service;

import br.uff.ic.dyevc.application.branchhistory.metric.MetricValue;
import br.uff.ic.dyevc.application.branchhistory.model.BranchRevisions;
import br.uff.ic.dyevc.application.branchhistory.model.LineRevisions;
import br.uff.ic.dyevc.application.branchhistory.model.ProjectRevisions;
import br.uff.ic.dyevc.application.branchhistory.model.Revision;
import br.uff.ic.dyevc.application.branchhistory.model.VersionedItem;
import br.uff.ic.dyevc.application.branchhistory.model.VersionedProject;
import br.uff.ic.dyevc.application.branchhistory.view.BranchValues;
import br.uff.ic.dyevc.application.branchhistory.view.LineValues;
import br.uff.ic.dyevc.application.branchhistory.view.RevisionValue;
import br.uff.ic.dyevc.exception.VCSException;
import br.uff.ic.dyevc.tools.vcs.git.GitConnector;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;

/**
 *
 * @author wallace
 */
public class CalculateService {
    String TEMP_BRANCHES_HISTORY_PATH = System.getProperty("user.home") + "/.archd/TEMP_BRANCHES_HISTORY/";
    String ARCHITECTURE_ANTI_PATTERN = "LONG_METHOD";
    
    
    private int getNumberOfRevisions(){
        return 2;
    }
    
    public void calculate(ProjectRevisions project){
        try {


            HashMap hashValues = new HashMap<String, Double>();
           /*Daqui pra frente
            for (BranchRevisions branch : project.getBranchesRevisions()) {
                for (LineRevisions line : branch.getLinesRevisions()) {
                    LineValues lineValues = new LineValues();
                    for (Revision revision : line.getRevisions()) {
                        Double v = (Double) hashValues.get(revision.getId());
                        double value = 0;
                        if (v == null) {
                            //posso procurar no banco
                            if(hashMetricValues.containsKey(revision.getId())) {
                                value = hashMetricValues.get(revision.getId()).getValue();
                                hashValues.put(revision.getId(), value);
                                if (value > projectValues.getMaxValue()) {
                                    projectValues.setMaxValue(value);
                                }
                            } else {
                                value = metric.getValue(revision, versionedItem, (VersionedProject) versionedItem, project);
                                MetricValue metricValue = new MetricValue(revision.getId(), versionedItem, (VersionedProject) versionedItem, metric,  value);
                                System.out.println("SALVAR");
                                metricValueDao.save(metricValue);
                                System.out.println("SALVOU");
                                hashValues.put(revision.getId(), value);
                                System.out.println("calculou: " + value);
                                if (value > projectValues.getMaxValue()) {
                                    projectValues.setMaxValue(value);
                                }
                            }

                        } else {
                            value = v;
                        }
                        RevisionValue revisionValue = new RevisionValue(value, revision.getId());
                        lineValues.addRevisionValue(revisionValue);
                        //System.out.println("Terminou revision: "+revision);
                    }
                    branchValues.addLineValues(lineValues);
                }
                projectValues.addBranchValues(branchValues);
            }
            projectValues.setHashValues(hashValues);
            daqui pra tras*/
        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
        }
        
    }
    
    private String[] createDirs(String path, String versionedItem){
        int numberOfRevisions = getNumberOfRevisions();
        String[] auxiliarPaths = new String[numberOfRevisions];
        for(int i = 0; i < numberOfRevisions; i++){
            File file = new File(TEMP_BRANCHES_HISTORY_PATH + versionedItem+"/"+ARCHITECTURE_ANTI_PATTERN+ "_" + (i+1));
            auxiliarPaths[i] = TEMP_BRANCHES_HISTORY_PATH + versionedItem+"/"+ARCHITECTURE_ANTI_PATTERN+  "_" + (i+1)+"/";
            if(!file.exists())
            {
                createDirectory(TEMP_BRANCHES_HISTORY_PATH + versionedItem+"/"+ARCHITECTURE_ANTI_PATTERN+  "_" + (i+1)+"/"+versionedItem);
                try {
                    
                    FileUtils.copyDirectory(new File(path), new File(TEMP_BRANCHES_HISTORY_PATH+ versionedItem+"/"+ARCHITECTURE_ANTI_PATTERN+  "_" + (i+1)+"/"+versionedItem));
                } catch (IOException ex) {
                    System.out.println("Erro de cópia Metric.createDirs: "+ex.getMessage());
                    //Logger.getLogger(Metric.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try{
                GitConnector gitConnector = new GitConnector(TEMP_BRANCHES_HISTORY_PATH+ versionedItem+"/"+ARCHITECTURE_ANTI_PATTERN+  "_" + (i+1)+"/"+versionedItem, versionedItem);

                Git git = new Git(gitConnector.getRepository());
                ResetCommand resetCommand = git.reset();
                resetCommand.setMode(ResetCommand.ResetType.HARD);
                resetCommand.call();
            } catch (VCSException ex) {
                System.out.println("Erro de reset de git (VCSException) Metric.createDirs: "+ex.getMessage());
            } catch (GitAPIException ex) {
                System.out.println("Erro de reset de git (GitAPIException) Metric.createDirs: "+ex.getMessage());
            }
            
        }
        return auxiliarPaths;
    }
    
    private void createDirectory(String name) {
        File file = new File(name);
        if (!file.exists()) {
            file.mkdirs();
        }
        
    }
}
