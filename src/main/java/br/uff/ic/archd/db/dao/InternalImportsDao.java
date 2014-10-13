/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao;

import br.uff.ic.archd.javacode.JavaAbstract;
import br.uff.ic.archd.javacode.JavaProject;

/**
 *
 * @author wallace
 */
public interface InternalImportsDao {
    public void saveInternalImport(JavaAbstract javaAbstract, JavaAbstract javaAbstractImport);
    public void getInternalImports(JavaAbstract javaAbstract, JavaProject javaProject);
}
