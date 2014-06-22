/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.ast.service;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

/**
 *
 * @author wallace
 */
public class MethodInvocationVisitor extends ASTVisitor {
	List<MethodInvocation> methods = new ArrayList<MethodInvocation>();
        private List<ClassInstanceCreation> instances = new ArrayList<ClassInstanceCreation>();
        private List<VariableDeclarationExpression> declarations = new ArrayList<VariableDeclarationExpression>();
        private List<SingleVariableDeclaration> singleDeclarations = new ArrayList<SingleVariableDeclaration>();
        private List<VariableDeclarationStatement> variableDeclarations = new ArrayList<VariableDeclarationStatement>();
        private List<Assignment> assignments = new ArrayList<Assignment>();

	@Override
	public boolean visit(MethodInvocation node) {
		methods.add(node);
		return super.visit(node);
	}
        
        public boolean visit(ClassInstanceCreation node){
            getInstances().add(node);
            return super.visit(node);    
        }
        
        public boolean visit(VariableDeclarationExpression node){
            declarations.add(node);
            return super.visit(node);
        }
        
        public boolean visit(SingleVariableDeclaration node) {
            singleDeclarations.add(node);
            return super.visit(node);
        }
        
        public boolean visit(VariableDeclarationStatement node){
            variableDeclarations.add(node);
            return super.visit(node);
        }
        
        public boolean visit(Assignment node) {
            getAssignments().add(node);
            return super.visit(node);
        }

	public List<MethodInvocation> getMethods() {
		return methods;
	}
        
        public List<SingleVariableDeclaration> getSingleDeclarations() {
		return singleDeclarations;
	}
        
        public List<VariableDeclarationStatement> getVariableDeclarations() {
                return variableDeclarations;
        }

        
        

    /**
     * @return the instances
     */
    public List<ClassInstanceCreation> getInstances() {
        return instances;
    }
    
    public List<VariableDeclarationExpression> getDeclarations(){
        return declarations;
    }

    /**
     * @return the assignments
     */
    public List<Assignment> getAssignments() {
        return assignments;
    }
        
}
