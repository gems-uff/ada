/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.ast.service;

/**
 *
 * @author wallace
 */
public class ParameterAst {
    private String name;
    private String type;
    private boolean isFinal;
    private boolean isStatic;
    private boolean isVolatile;
    private boolean isPrivate;
    private boolean isPublic;
    private boolean isProtected;
    
    
    ParameterAst(String name, String type){
        this.name = name;
        this.type = type;
        isFinal = false;
        isStatic = false;
        isVolatile = false;
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
     * @return the type
     */
    public String getType() {
        return type;
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
     * @return the isVolatile
     */
    public boolean isVolatile() {
        return isVolatile;
    }

    /**
     * @param isVolatile the isVolatile to set
     */
    public void setVolatile(boolean isVolatile) {
        this.isVolatile = isVolatile;
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
}
