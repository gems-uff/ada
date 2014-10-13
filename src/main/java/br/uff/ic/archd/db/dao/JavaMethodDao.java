/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao;

import br.uff.ic.archd.javacode.JavaAbstract;
import br.uff.ic.archd.javacode.JavaMethod;
import br.uff.ic.archd.javacode.JavaProject;
import java.util.List;

/**
 *
 * @author wallace
 */
public interface JavaMethodDao {
    public void save(JavaMethod javaMethod, boolean fromClass, long itemId);
    public List<JavaMethod> getAllJavaMethod(JavaProject javaProject);
    public List<JavaMethod> getJavaMethodsByInterfaceId(JavaProject javaProject, long id);
    public List<JavaMethod> getJavaMethodsByClassId(JavaProject javaProject, long id);
    public void getJavaMethods(JavaProject javaProject, JavaAbstract javaAbstract);
}
