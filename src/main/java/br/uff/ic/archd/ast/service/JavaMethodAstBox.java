/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.ast.service;

import java.util.List;
import org.eclipse.jdt.core.dom.Block;

/**
 *
 * @author wallace
 */
public class JavaMethodAstBox {
    private String name;
    private List<ParameterAst> parameters;
    private String returnType;
    private boolean isFinal;
    private boolean isStatic;
    private boolean isAbstract;
    private boolean isSynchronized;
    private boolean isPrivate;
    private boolean isPublic;
    private boolean isProtected;
    private int sizeInChars;
    private int cyclomaticComplexity;
    private Block block;
    
    JavaMethodAstBox(String name, String returnType, Block block){
        this.name = name;
        this.returnType = returnType;
        this.block = block;
        isFinal = false;
        isStatic = false;
        isAbstract = false;
        isSynchronized = false;
        isPrivate = false;
        isPublic = false;
        isProtected = true;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the parameters
     */
    public List<ParameterAst> getParameters() {
        return parameters;
    }

    /**
     * @return the returnType
     */
    public String getReturnType() {
        return returnType;
    }

    /**
     * @return the isFinal
     */
    public boolean isFinal() {
        return isFinal;
    }

    /**
     * @param isFinal the isFinal to set
     */
    public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    /**
     * @return the isStatic
     */
    public boolean isStatic() {
        return isStatic;
    }

    /**
     * @param isStatic the isStatic to set
     */
    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    /**
     * @return the isAbstract
     */
    public boolean isAbstract() {
        return isAbstract;
    }

    /**
     * @param isAbstract the isAbstract to set
     */
    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    /**
     * @return the isSynchronized
     */
    public boolean isSynchronized() {
        return isSynchronized;
    }

    /**
     * @param isSynchronized the isSynchronized to set
     */
    public void setSynchronized(boolean isSynchronized) {
        this.isSynchronized = isSynchronized;
    }

    /**
     * @return the isPrivate
     */
    public boolean isPrivate() {
        return isPrivate;
        
    }

    /**
     * @param isPrivate the isPrivate to set
     */
    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
        if(isPrivate){
            isProtected = false;
            isPublic = false;
        }
    }

    /**
     * @return the isPublic
     */
    public boolean isPublic() {
        return isPublic;
    }

    /**
     * @param isPublic the isPublic to set
     */
    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
        if(isPublic){
            isProtected = false;
            isPrivate = false;
        }
    }

    /**
     * @return the isProtected
     */
    public boolean isProtected() {
        return isProtected;
    }

    /**
     * @param isProtected the isProtected to set
     */
    public void setProtected(boolean isProtected) {
        this.isProtected = isProtected;
        if(isProtected){
            isPublic = false;
            isPrivate = false;
        }
    }



    /**
     * @param parameters the parameters to set
     */
    public void setParameters(List<ParameterAst> parameters) {
        this.parameters = parameters;
    }

    /**
     * @return the block
     */
    public Block getBlock() {
        return block;
    }
}
