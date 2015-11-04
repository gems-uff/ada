/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.javacode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author wallace
 */
public class ProjectAuthors {
    
    private HashMap<String, RevisionAuthor> hashMap;
    private Set<String> authorsSet;
    
    public ProjectAuthors(){
        hashMap = new HashMap();
        authorsSet = new HashSet();
    }
    
    public void addRevisionAuthor(RevisionAuthor revisionAuthor){
        hashMap.put(revisionAuthor.getId(), revisionAuthor);
        authorsSet.add(revisionAuthor.getAuthor());
    }
    
    public List<String> getAuthors(){
        List auth = new LinkedList();
        Iterator<String> it = authorsSet.iterator();
        while(it.hasNext()){
            auth.add(it.next());
        }
        return auth;
    }
    
    
    public RevisionAuthor getRevisionAuthor(String id){
        return hashMap.get(id);

    }
}
