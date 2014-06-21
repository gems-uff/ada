/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.ast.service;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;

/**
 *
 * @author wallace
 */
public class FieldDeclarationVisitor extends ASTVisitor{
    
    List<VariableDeclarationExpression> variables = new ArrayList<VariableDeclarationExpression>();
    
    @Override
    public boolean visit(VariableDeclarationExpression node) {
        
        variables.add(node);
        return super.visit(node);
    }
    
    public List<VariableDeclarationExpression> getVariables() {
        return variables;
    }
}
