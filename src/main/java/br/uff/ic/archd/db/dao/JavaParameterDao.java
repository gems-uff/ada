/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao;

import br.uff.ic.archd.javacode.Parameter;
import java.util.List;

/**
 *
 * @author wallace
 */
public interface JavaParameterDao {
    public void save(Parameter parameter);
    public List<Parameter> getAllParameterByMethodId(long methodId);
}
