/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.ast.service;

import br.uff.ic.archd.javacode.JavaData;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTNode;

/**
 *
 * @author wallace
 */
public class BlockVariablesBox {
    private ASTNode rootNode;
    private List<BlockVariables> blockVariables;
    
    BlockVariablesBox(ASTNode rootNode){
        this.rootNode = rootNode;
        blockVariables = new ArrayList();
        
    }
    
    public void addVariable(Variable variable, ASTNode astNode){
        BlockVariables blockVariable = null;
        for(BlockVariables aux : blockVariables){
            if(aux.getAstNode() == astNode){
                blockVariable = aux;
                break;
            }
        }
        if(blockVariable == null){
            blockVariable = new BlockVariables(astNode);
            blockVariables.add(blockVariable);
        }
        blockVariable.addVariable(variable);
    }
    
    public JavaData getJavaDataByVariableName(String name, ASTNode astNode){
        JavaData javaData = null;
        do{
            BlockVariables blockVariable = null;
            for(BlockVariables aux : blockVariables){
                if(aux.getAstNode() == astNode){
                    blockVariable = aux;
                    break;
            
                }
            }
            if(blockVariable != null){
                Variable variable = blockVariable.getVariable(name);
                if(variable != null){
                    javaData = variable.getJavaData();
                }
            }
            if(astNode != rootNode){
                astNode = astNode.getParent();
            }
        }while(astNode != rootNode && javaData == null);
        return javaData;
    }
    
    
}
