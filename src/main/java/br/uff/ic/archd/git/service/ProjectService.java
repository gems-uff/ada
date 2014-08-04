/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.git.service;

import br.uff.ic.dyevc.application.branchhistory.model.BranchRevisions;
import br.uff.ic.dyevc.application.branchhistory.model.LineRevisions;
import br.uff.ic.dyevc.application.branchhistory.model.ProjectRevisions;
import br.uff.ic.dyevc.application.branchhistory.model.Revision;
import br.uff.ic.dyevc.application.branchhistory.model.RevisionsBucket;
import br.uff.ic.dyevc.application.branchhistory.model.VersionedFile;
import br.uff.ic.dyevc.application.branchhistory.model.VersionedItem;
import br.uff.ic.dyevc.application.branchhistory.model.VersionedItemsBucket;
import br.uff.ic.dyevc.application.branchhistory.model.VersionedDirectory;
import br.uff.ic.dyevc.application.branchhistory.model.VersionedProject;
import br.uff.ic.dyevc.application.branchhistory.model.constant.Constant;
import br.uff.ic.dyevc.model.CommitRelationship;
import br.uff.ic.dyevc.tools.vcs.git.GitCommitHistory;
import br.uff.ic.dyevc.tools.vcs.git.GitConnector;
import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;

/**
 *
 * @author wallace
 */
public class ProjectService {
    
    private String BRANCHES_HISTORY_PATH = System.getProperty("user.home")+"/.dyevc/BRANCHES_HISTORY/";
    
    public ProjectRevisions getProject(String path, String projectName) throws Exception{
        RevisionsBucket revisionsBucket = new RevisionsBucket();
        
        File file = new File(BRANCHES_HISTORY_PATH+projectName);
        FileUtils.deleteDirectory(file);
        
        
        
        createDirectory(projectName);
        
        FileUtils.copyDirectory(new File(path), new File(BRANCHES_HISTORY_PATH+projectName));
        
        //GitConnector gitConnector = new GitConnector(path,projectName);
        
        //Git git = new Git(gitConnector.getRepository());
        
        //List<Ref> branchRefList = git.branchList().call();
        
        
        
        
        //gitConnector = gitConnector.cloneRepository(path, BRANCHES_HISTORY_PATH+projectName,projectName);
        
        GitConnector gitConnector = new GitConnector(BRANCHES_HISTORY_PATH+projectName,projectName);
        GitCommitHistory gitCommitHistory = GitCommitHistory.getInstance(gitConnector);
            
        Git git = new Git(gitConnector.getRepository());
        
        List<Ref> branchRefList = git.branchList().call();
        
        List<BranchRevisions> branches = new LinkedList<BranchRevisions>();
            
        ProjectRevisions project = new ProjectRevisions(projectName);
        //System.out.println("BRANCHES: "+branchRefList.size());
        for (Ref branchRef : branchRefList) {
            Revision rev = revisionsBucket.getRevisionById(branchRef.getObjectId().getName());
            if(rev == null){
                
                rev = new Revision(branchRef.getObjectId().getName());
                revisionsBucket.addRevision(rev);
            }
            BranchRevisions branch = new BranchRevisions(branchRef.getName(), rev);
            branches.add(branch);                
        }
            
        Collection<CommitRelationship> commitRelationships = gitCommitHistory.getCommitRelationships();
        Iterator<CommitRelationship> commitRelationshipIt = commitRelationships.iterator();
        HashMap<String, String> hash = new HashMap<String, String>();
        int linhas = 1;
        while(commitRelationshipIt.hasNext()){
            CommitRelationship commitRelationship = commitRelationshipIt.next();
            String child = commitRelationship.getChild().getId();
            Date childCommitDate = commitRelationship.getChild().getCommitDate();
            String parent = commitRelationship.getParent().getId();
            Revision revisionChild = revisionsBucket.getRevisionById(child);
            if(revisionChild == null){
                revisionChild = new Revision(child);
                revisionsBucket.addRevision(revisionChild);
            }
            Revision revisionParent = revisionsBucket.getRevisionById(parent);
            if(revisionParent == null){
                revisionParent = new Revision(parent);
                revisionsBucket.addRevision(revisionParent);
            }
            revisionChild.addPrev(revisionParent);
            if(revisionChild.getPrev().size() > 1){
                linhas++;
            }
            revisionParent.addNext(revisionChild);
            
//            if(hash.containsKey(commitRelationship.getChild().getId())){
//                //System.out.println("REPETIDO: "+commitRelationship.getChild().getId());
//                hash.put(commitRelationship.getChild().getId(), hash.get(commitRelationship.getChild().getId())+"="+commitRelationship.getParent().getId());
//            }else{
//                hash.put(commitRelationship.getChild().getId(), commitRelationship.getParent().getId());
//            }
                
            //System.out.println(commitRelationship.getChild().getId()+"  --> "+commitRelationship.getParent().getId());
        }
        //System.out.println("Terminou branches: "+branches.size()+"     linhas: "+linhas);
        //System.out.println("Revisoes: "+revisionsBucket.getRevisionCollection().size());
        for (BranchRevisions branch : branches) {
                
            setHistoryOfBranch(branch, revisionsBucket);    
            //System.out.println("Terminou uma branche");
        }
            
        project.setBranchesRevisions(branches);
        project.setRevisionsBucket(revisionsBucket);
        Iterator<Revision> it = revisionsBucket.getRevisionCollection().iterator();
        while(it.hasNext()){
            Revision rev = it.next();
            if(rev.getPrev().size() == 0){
                project.setRoot(rev);
                break;
            }
        }
            
        

        return project;
    }
    
    public VersionedProject getVersionedProject(ProjectRevisions projectRevisions){
        VersionedProject versionedProject = new VersionedProject(projectRevisions.getName(), BRANCHES_HISTORY_PATH+projectRevisions.getName());
        
        VersionedItemsBucket versionedItemsBucket = new VersionedItemsBucket();
        versionedItemsBucket.addVersionedItem(versionedProject);
        
        try{
            GitConnector gitConnector = new GitConnector(BRANCHES_HISTORY_PATH+projectRevisions.getName(), projectRevisions.getName());
            Git git = new Git(gitConnector.getRepository());
            CheckoutCommand checkoutCommand = null;
        
            RevisionsBucket revisionsBucket = projectRevisions.getRevisionsBucket();
        
            Collection<Revision> collection = revisionsBucket.getRevisionCollection();
            Iterator<Revision> it = collection.iterator();
            while(it.hasNext()){
                Revision revision = it.next();
                checkoutCommand = git.checkout();
                checkoutCommand.setName(revision.getId());
                            
                checkoutCommand.call();
                File file = new File(versionedProject.getRelativePath());
                getVersionedItems(versionedItemsBucket, file, revision, versionedProject);
                
                
            }
        }catch(Exception e){
            System.out.println("ERRO getversioned: "+e.getMessage());
        }
        
        
        return versionedProject;
    }
    
    private void setHistoryOfBranch(BranchRevisions branch, RevisionsBucket revisionsBucket){
        List<Revision> parents = branch.getHead().getPrev();
        LineRevisions line = new LineRevisions(branch.getHead());
        //System.out.println("pais: "+parents.size());
        while(parents.size() != 0){
            
            line.addRevision(parents.get(0));
            //System.out.println("pais dentro: "+parents.size());
            for (int i = 1; i < parents.size(); i++) {
                
                Revision revision = parents.get(i);
                if(!branch.haveLineRevisionByHeadId(revision.getId()))
                {
                    LineRevisions newLine = new LineRevisions(revision);
                    getParents(branch, newLine, 1);
                }
                
            }
            parents = parents.get(0).getPrev();

        }
        branch.addLineRevisions(line);
        
        //System.out.println("TERMINOU LINHAS: "+branch.getLinesRevisions().size());
        
        
    }
    
    private void getParents(BranchRevisions branch, LineRevisions line, int profundidade){
        List<Revision> parents = line.getHead().getPrev();
        //System.out.println("get parents: "+parents.size()+"    profundidade: "+profundidade);
        while(parents.size() != 0){
            
            line.addRevision(parents.get(0));
            //System.out.println("get parents dntro: "+parents.size());
            for (int i = 1; i < parents.size(); i++) {
                Revision revision = parents.get(i);
                //System.out.println("Revision: "+revision.getId());
                if(!branch.haveLineRevisionByHeadId(revision.getId()))
                {
                    LineRevisions newLine = new LineRevisions(revision);
                    getParents(branch, newLine, 1);
                }
                
            }
            //System.out.println("PAI: "+parents.get(0).getId());
            parents = parents.get(0).getPrev();
        }
        //System.out.println("TERMINOU UMA LINHA ------------------------------ profdade: "+profundidade);
        branch.addLineRevisions(line);
    }
    
    private void createDirectory(String name){
        File file = new File(BRANCHES_HISTORY_PATH+name);
        if(!file.exists()){
            file.mkdirs();
        }
    }
    
    
    
    
    private void getVersionedItems(VersionedItemsBucket versionedItemsBucket, File file, Revision revision, VersionedItem antVersionedItem){
        if(!file.getName().startsWith(".")){
            if(file.isFile()){
                VersionedItem versionedItem = versionedItemsBucket.getVersionedItemByPath(file.getAbsolutePath());
                if(versionedItem == null){
                    
                    versionedItem = new  VersionedFile(file.getName(), file.getAbsolutePath());
                    versionedItemsBucket.addVersionedItem(versionedItem);
                    if(antVersionedItem.getType() == Constant.PROJECT){
                        ((VersionedProject) antVersionedItem).addVersionedItem(versionedItem);
                    }else if(antVersionedItem.getType() == Constant.DIRECTORY){
                        ((VersionedDirectory) antVersionedItem).addVersionedItem(versionedItem);
                    }
                }
                versionedItem.addRevison(revision);
            }else{
                File files[] = file.listFiles();
                VersionedItem versionedItem = versionedItemsBucket.getVersionedItemByPath(file.getAbsolutePath());
                if(versionedItem == null){
                    versionedItem = new  VersionedDirectory(file.getName(), file.getAbsolutePath());
                    versionedItemsBucket.addVersionedItem(versionedItem);
                    if(antVersionedItem.getType() == Constant.PROJECT){
                        ((VersionedProject) antVersionedItem).addVersionedItem(versionedItem);
                    }else if(antVersionedItem.getType() == Constant.DIRECTORY){
                        ((VersionedDirectory) antVersionedItem).addVersionedItem(versionedItem);
                    }
                }
                versionedItem.addRevison(revision);
                for (int i = 0; i < files.length; i++) {
                    
                    File file1 = files[i];
                    getVersionedItems(versionedItemsBucket, file1, revision, versionedItem);
                 }
             }
        }
    }
    
    public static void main(String args[]){
        try{
            ProjectService projectService = new ProjectService();
            ProjectRevisions pr = projectService.getProject("/home/wallace/mestrado/projetos_alvos/neo4j/neo4j", "neo4j");
            System.out.println("branches: "+pr.getBranchesRevisions().size());
            System.out.println("Revisions: "+pr.getRevisionsBucket().getRevisionCollection().size());
            System.out.println("TERMINOU");
        }catch(Exception e){
            System.out.println("ERRO: "+e.getMessage());
        }
    }
    
     
}
