/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao;

import br.uff.ic.archd.javacode.JavaClass;
import br.uff.ic.archd.javacode.JavaInterface;
import br.uff.ic.archd.javacode.JavaProject;
import java.util.List;

/**
 *
 * @author wallace
 */
public interface ImplementedInterfacesDao {
    public void saveImplementedInterface(JavaClass javaClass,JavaInterface javaInterface);
    public void setImplementedInterfacesDao(JavaClass javaClass, JavaProject javaProject);
    public void saveImplementedInterface(List<JavaClass> javaClasses, List<JavaInterface> javaInterfaces);
}
