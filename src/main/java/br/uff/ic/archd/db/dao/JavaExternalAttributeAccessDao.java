/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao;

import br.uff.ic.archd.javacode.JavaExternalAttributeAccess;
import br.uff.ic.archd.javacode.JavaMethod;
import br.uff.ic.archd.javacode.JavaProject;
import java.util.List;

/**
 *
 * @author wallace
 */
public interface JavaExternalAttributeAccessDao {
    public void saveJavaExternalAttributeAccess(JavaMethod javaMethod);
    public void getJavaExternalAttributeAccessByMethod(JavaMethod javaMethod, JavaProject javaProject);
}
