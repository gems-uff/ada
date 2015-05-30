/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao;

import br.uff.ic.archd.javacode.JavaAbstract;
import br.uff.ic.archd.javacode.JavaProject;
import java.util.List;

/**
 *
 * @author wallace
 */
public interface ExternalImportsDao {
    public void save(JavaAbstract javaAbstract, String externalImport);
    public void getExternalImports(JavaAbstract javaAbstract, JavaProject javaProject);
    public void save(JavaAbstract javaAbstract, List<String> externalImports);
}
