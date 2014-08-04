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
    private List<String> methodInternalInvocations;
    private List<String> methodInvocations;
    private String returnType;
    private boolean isFinal;
    private boolean isStatic;
    private boolean isAbstract;
    private boolean isSynchronized;
    private boolean isPrivate;
    private boolean isPublic;
    private boolean isProtected;
    private int cyclomaticComplexity;
    private Block block;
    private int sizeInChars;
    private int methodInternalId;
    private boolean changeInternalState;
    private boolean changeInternalStateByMethodInvocation;
    
    public JavaMethodAstBox(String name, String returnType, Block block){
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
        cyclomaticComplexity = 0;
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

    /**
     * @return the cyclomaticComplexity
     */
    public int getCyclomaticComplexity() {
        return cyclomaticComplexity;
    }

    /**
     * @param cyclomaticComplexity the cyclomaticComplexity to set
     */
    public void setCyclomaticComplexity(int cyclomaticComplexity) {
        this.cyclomaticComplexity = cyclomaticComplexity;
    }

    /**
     * @return the sizeInChars
     */
    public int getSizeInChars() {
        return sizeInChars;
    }

    /**
     * @param sizeInChars the sizeInChars to set
     */
    public void setSizeInChars(int sizeInChars) {
        this.sizeInChars = sizeInChars;
    }

    /**
     * @return the methodInternalId
     */
    public int getMethodInternalId() {
        return methodInternalId;
    }

    /**
     * @param methodInternalId the methodInternalId to set
     */
    public void setMethodInternalId(int methodInternalId) {
        this.methodInternalId = methodInternalId;
    }

    /**
     * @return the changeInternalState
     */
    public boolean isChangeInternalState() {
        return changeInternalState;
    }

    /**
     * @param changeInternalState the changeInternalState to set
     */
    public void setChangeInternalState(boolean changeInternalState) {
        this.changeInternalState = changeInternalState;
    }

    /**
     * @return the changeInternalStateByMethodInvocation
     */
    public boolean isChangeInternalStateByMethodInvocation() {
        return changeInternalStateByMethodInvocation;
    }

    /**
     * @param changeInternalStateByMethodInvocation the changeInternalStateByMethodInvocation to set
     */
    public void setChangeInternalStateByMethodInvocation(boolean changeInternalStateByMethodInvocation) {
        this.changeInternalStateByMethodInvocation = changeInternalStateByMethodInvocation;
    }

    /**
     * @return the methodInternalInvocations
     */
    public List<String> getMethodInternalInvocations() {
        return methodInternalInvocations;
    }

    /**
     * @param methodInternalInvocations the methodInternalInvocations to set
     */
    public void setMethodInternalInvocations(List<String> methodInternalInvocations) {
        this.methodInternalInvocations = methodInternalInvocations;
    }

    /**
     * @return the methodInvocations
     */
    public List<String> getMethodInvocations() {
        return methodInvocations;
    }

    /**
     * @param methodInvocations the methodInvocations to set
     */
    public void setMethodInvocations(List<String> methodInvocations) {
        this.methodInvocations = methodInvocations;
    }
}
