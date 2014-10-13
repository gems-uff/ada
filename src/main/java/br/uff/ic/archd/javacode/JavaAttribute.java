/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.javacode;

/**
 *
 * @author wallace
 */
public class JavaAttribute {
    private JavaData type;
    private String name;
    private boolean isFinal;
    private boolean isStatic;
    private boolean isVolatile;
    private boolean isPrivate;
    private boolean isPublic;
    private boolean isProtected;
    
    
    public JavaAttribute(JavaData type, String name, boolean isFinal, boolean isStatic, boolean isVolatile, boolean isPrivate, boolean isPublic, boolean isProtected){
        this.type = type;
        this.name = name;
        this.isFinal = isFinal;
        this.isStatic = isStatic;
        this.isVolatile = isVolatile;
        this.isPrivate = isPrivate;
        this.isPublic = isPublic;
        this.isProtected = isProtected;
    }

    /**
     * @return the type
     */
    public JavaData getType() {
        return type;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the isFinal
     */
    public boolean isFinal() {
        return isFinal;
    }

    /**
     * @return the isStatic
     */
    public boolean isStatic() {
        return isStatic;
    }

    /**
     * @return the isPrivate
     */
    public boolean isPrivate() {
        return isPrivate;
    }

    /**
     * @return the isPublic
     */
    public boolean isPublic() {
        return isPublic;
    }

    /**
     * @return the isProtected
     */
    public boolean isProtected() {
        return isProtected;
    }

    /**
     * @return the isVolatile
     */
    public boolean isVolatile() {
        return isVolatile;
    }
}
