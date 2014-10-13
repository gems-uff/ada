/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao;

import br.uff.ic.archd.javacode.JavaData;
import br.uff.ic.archd.javacode.JavaPackage;
import java.util.List;

/**
 *
 * @author wallace
 */
public interface JavaPackageDao {
    public void save(JavaPackage javaData);
    public List<JavaPackage> getAllJavaPackage();
}
