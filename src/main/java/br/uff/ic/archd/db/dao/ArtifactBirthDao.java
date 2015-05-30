/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao;

import java.util.List;

/**
 *
 * @author wallace
 */
public interface ArtifactBirthDao {
    public void save(String artifactSignature, String revisionBirthId);
    public String getBirthIdBirth(String artifactSignature);
    public List<String> getArtifactsBorn(String revisionBirthId);
    public void printAll();
    public void save(List<String> artifactSignatures, String revisionBirthNumber);
        
    
}
