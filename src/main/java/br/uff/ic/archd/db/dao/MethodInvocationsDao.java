/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao;

import br.uff.ic.archd.javacode.JavaClass;
import br.uff.ic.archd.javacode.JavaMethod;
import br.uff.ic.archd.javacode.JavaProject;
import java.util.List;

/**
 *
 * @author wallace
 */
public interface MethodInvocationsDao {
    public void saveMethodInvocations(JavaMethod javaMethod, JavaClass javaClass);
    public void getInvocatedMethods(JavaMethod javaMethod, JavaClass javaClass, JavaProject javaProject);
    public void saveMethodInvocations(List<JavaMethod> methods);
}
