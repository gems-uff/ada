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
public interface OriginalNameDao {
    public void save(String artifactName, String originalName);
    public String getOriginalName(String artifactSignature);
    public List<String> getArtifactsAlternativesNames(String originalName);
    public void save(List<String> artifactNames);
        
    
}
