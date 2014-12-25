/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.javacode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.eclipse.jdt.core.dom.Block;

/**
 *
 * @author wallace
 */
public class JavaMethod {
    private long id;
    private String name;
    private JavaData returnType;
    private List<Parameter> parameters;
    private List<JavaMethodInvocation> methodInvocations;
    private List<JavaMethod> internalMethodInvocations;
    private List<JavaMethod> changingMethods;
    private HashMap<String, JavaClass> changingClasses;
    private int cyclomaticComplexity;
    private int sizeInChars;
    private boolean isFinal;
    private boolean isStatic;
    private boolean isAbstract;
    private boolean isSynchronized;
    private boolean isPrivate;
    private boolean isPublic;
    private boolean isProtected;
    private Block block;
    private JavaAbstract javaAbstract;
    private boolean changeInternalState;
    private boolean changeInternalStateByMethodInvocations;
    private int internalID;
    
    

    public JavaMethod(String name, JavaData returnType, boolean isFinal, boolean isStatic, boolean isAbstract, boolean isSynchronized,
            boolean isPrivate, boolean isPublic, boolean isProtected,int cyclomaticComplexity, Block block){
        this.name = name;
        this.returnType = returnType;
        this.isFinal = isFinal;
        this.isStatic = isStatic;
        this.isAbstract = isAbstract;
        this.isSynchronized = isSynchronized;
        this.isPrivate = isPrivate;
        this.isPublic = isPublic;
        this.isProtected = isProtected;
        parameters = new ArrayList();
        methodInvocations = new ArrayList();
        internalMethodInvocations = new ArrayList();
        this.cyclomaticComplexity = cyclomaticComplexity;
        this.block = block;
        this.changeInternalState = false;
        this.changeInternalStateByMethodInvocations = false;
        changingMethods = new ArrayList();
        changingClasses = new HashMap();
    }
    
    
    public void addChangingMethod(JavaMethod javaMethod){
        getChangingMethods().add(javaMethod);
        changingClasses.put(javaMethod.getJavaAbstract().getFullQualifiedName(), (JavaClass) javaMethod.getJavaAbstract());
    }

    /**
     * @return the name
     */
    public  String getName() {
        return name;
    }

    /**
     * @return the returnType
     */
    public JavaData getReturnType() {
        return returnType;
    }

    /**
     * @return the parameters
     */
    public List<Parameter> getParameters() {
        return parameters;
    }

    /**
     * @return the cyclomaticComplexity
     */
    public int getCyclomaticComplexity() {
        return cyclomaticComplexity;
    }

    /**
     * @return the sizeInChars
     */
    public int getSizeInChars() {
        return sizeInChars;
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
     * @return the isAbstract
     */
    public boolean isAbstract() {
        return isAbstract;
    }

    /**
     * @return the isSynchronized
     */
    public boolean isSynchronized() {
        return isSynchronized;
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
    
    public void addParameter(Parameter parameter){
        parameters.add(parameter);
    }
    
    public void addMethodInvocation(JavaMethodInvocation javaMethodInvocation){
        getMethodInvocations().add(javaMethodInvocation);
    }
    
    public void addInternalMethodInvocation(JavaMethod javaMethod){
        getInternalMethodInvocations().add(javaMethod);
    }
    
    public String getMethodSignature(){
        String methodSignature = name+"(";
        if(!parameters.isEmpty()){
            methodSignature = methodSignature+(parameters.get(0).getType().getClass() == JavaClass.class || parameters.get(0).getType().getClass() == JavaInterface.class ? (((JavaAbstract) parameters.get(0).getType()).getFullQualifiedName()) : parameters.get(0).getType().getName());
            int i = 1;
            for(i=1; i < parameters.size();i++){
                methodSignature = methodSignature+","+(parameters.get(i).getType().getClass() == JavaClass.class || parameters.get(i).getType().getClass() == JavaInterface.class ? (((JavaAbstract) parameters.get(i).getType()).getFullQualifiedName()) : parameters.get(i).getType().getName());
            }
        }
        methodSignature = methodSignature+")";
        return methodSignature;
    }
    
    public String getParametersSignature(){
        String methodSignature = "";
        if(!parameters.isEmpty()){
            methodSignature = methodSignature+(parameters.get(0).getType().getClass() == JavaClass.class || parameters.get(0).getType().getClass() == JavaInterface.class ? (((JavaAbstract) parameters.get(0).getType()).getFullQualifiedName()) : parameters.get(0).getType().getName());
            methodSignature = methodSignature+":"+parameters.get(0).getName();
            int i = 1;
            for(i=1; i < parameters.size();i++){
                methodSignature = methodSignature+";"+(parameters.get(i).getType().getClass() == JavaClass.class || parameters.get(i).getType().getClass() == JavaInterface.class ? (((JavaAbstract) parameters.get(i).getType()).getFullQualifiedName()) : parameters.get(i).getType().getName())+":"+parameters.get(i).getName();
            }
        }
        return methodSignature;
    }
    
    public int getDiff(){
        int returnSize = 0;
        int parametersSize = 0;
        if(!returnType.getName().equals("void")){
            if(returnType.getClass() == JavaClass.class){
                returnSize = ((JavaClass) returnType).getSize();
            }else{
                returnSize = 1;
            }
        }
        for(Parameter parameter : parameters){
            if(parameter.getType().getClass() == JavaClass.class){
                parametersSize = parametersSize + ((JavaClass) parameter.getType()).getSize();
            }else{
                parametersSize++;
            }
        }
        return parametersSize - returnSize;
    }

    /**
     * @return the block
     */
    public Block getBlock() {
        return block;
    }
    
    public void setSizeInChars(int sizeInChars){
        this.sizeInChars = sizeInChars;
    }

    /**
     * @return the methodInvocations
     */
    public List<JavaMethodInvocation> getMethodInvocations() {
        return methodInvocations;
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
     * @return the changeInternalStateByMethodInvocations
     */
    public boolean isChangeInternalStateByMethodInvocations() {
        return changeInternalStateByMethodInvocations;
    }

    /**
     * @param changeInternalStateByMethodInvocations the changeInternalStateByMethodInvocations to set
     */
    public void setChangeInternalStateByMethodInvocations(boolean changeInternalStateByMethodInvocations) {
        this.changeInternalStateByMethodInvocations = changeInternalStateByMethodInvocations;
    }

    /**
     * @return the internalMethodInvocations
     */
    public List<JavaMethod> getInternalMethodInvocations() {
        return internalMethodInvocations;
    }

    /**
     * @return the internalID
     */
    public int getInternalID() {
        return internalID;
    }

    /**
     * @param internalID the internalID to set
     */
    public void setInternalID(int internalID) {
        this.internalID = internalID;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the changingMethods
     */
    public int getChangingMethodsMetric() {
        return getChangingMethods().size();
    }


    /**
     * @return the changingClasses
     */
    public int getChangingClassesMetric() {
        return changingClasses.size();
    }

    /**
     * @param changingClasses the changingClasses to set
     */

    /**
     * @return the javaAbstract
     */
    public JavaAbstract getJavaAbstract() {
        return javaAbstract;
    }

    /**
     * @param javaAbstract the javaAbstract to set
     */
    public void setJavaAbstract(JavaAbstract javaAbstract) {
        this.javaAbstract = javaAbstract;
    }

    /**
     * @return the changingMethods
     */
    public List<JavaMethod> getChangingMethods() {
        return changingMethods;
    }
    
}
