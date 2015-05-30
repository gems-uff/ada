/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao;

import br.uff.ic.archd.javacode.JavaClass;
import br.uff.ic.archd.javacode.JavaInterface;
import br.uff.ic.archd.javacode.JavaProject;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author wallace
 */
public interface ClassesDao {
    public void save(JavaClass javaClass);
    public List<JavaClass> getAllJavaClass();
    public List<JavaClass> getJavaClassesByRevisionId(JavaProject javaProject, String id);
    public void save(List<JavaClass> javaClasses);
}
