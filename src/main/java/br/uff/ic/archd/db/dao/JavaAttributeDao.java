/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao;

import br.uff.ic.archd.javacode.JavaAttribute;
import br.uff.ic.archd.javacode.JavaClass;
import br.uff.ic.archd.javacode.JavaProject;
import java.util.List;

/**
 *
 * @author wallace
 */
public interface JavaAttributeDao {
    public void save(JavaAttribute javaAttribute, long javaClassId);
    public void getJavaAttributesFromClass(JavaClass javaClass, JavaProject javaProject);
    public void save(List<JavaAttribute> javaAttributes);
}
