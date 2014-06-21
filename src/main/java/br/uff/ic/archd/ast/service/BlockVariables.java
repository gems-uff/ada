/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.ast.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.IfStatement;

/**
 *
 * @author wallace
 */
public class BlockVariables {

    private ASTNode astNode;
    private HashMap<String, Variable> variables;
    
    BlockVariables(ASTNode astNode){
        this.astNode = astNode;
        variables = new HashMap();
    }
    
    public void addVariable(Variable variable){
        variables.put(variable.getName(), variable);
    }
    
    
    public Variable getVariable(String name){
        return variables.get(name);
    }

    /**
     * @return the astNode
     */
    public ASTNode getAstNode() {
        return astNode;
    }

}
