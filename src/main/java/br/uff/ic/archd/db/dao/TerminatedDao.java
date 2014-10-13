/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao;

/**
 *
 * @author wallace
 */
public interface TerminatedDao {
    public void save(String projectName, String revisionId);
    public boolean isTerminated(String projectName, String revisionId);
}
