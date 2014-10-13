/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao;

import br.uff.ic.archd.javacode.JavaInterface;
import br.uff.ic.archd.javacode.JavaProject;
import java.util.List;

/**
 *
 * @author wallace
 */
public interface InterfaceDao {
    public void save(JavaInterface javaInterface);
    //public List<JavaInterface> getAllJavaClass();
    public List<JavaInterface> getJavaInterfacesByRevisionId(JavaProject javaProject, String id);

}
