/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.ast.service;


import br.uff.ic.archd.javacode.JavaAbstract;
import br.uff.ic.archd.javacode.JavaAbstractExternal;
import br.uff.ic.archd.javacode.JavaAttribute;
import br.uff.ic.archd.javacode.JavaClass;
import br.uff.ic.archd.javacode.JavaConstructorService;
import br.uff.ic.archd.javacode.JavaData;
import br.uff.ic.archd.javacode.JavaInterface;
import br.uff.ic.archd.javacode.JavaMethodInvocation;
import br.uff.ic.archd.javacode.JavaPrimitiveType;
import br.uff.ic.archd.javacode.JavaProject;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author wallace
 */
public class InteractionController implements ActionListener{

    
    private InteractionViewer interactionViewer;
    private JavaConstructorService javaConstructorService;
    private JavaProject javaProject;
    InteractionController(){
        javaConstructorService = new JavaConstructorService();
        javaProject = javaConstructorService.createProjects("/home/wallace/mestrado/jEdit");
        String classesString[] = new String[javaProject.getAllClasses().size()];
        for(int i = 0; i < javaProject.getAllClasses().size(); i++){
            classesString[i] = javaProject.getAllClasses().get(i).getFullQualifiedName();
        }
        interactionViewer = new InteractionViewer(classesString);
        interactionViewer.setController(this);
        interactionViewer.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(InteractionViewer.ACTION_UPDATE)) {
            showClassFunctions(interactionViewer.getClassSelected());
        }
    }
    
    
    private void showClassFunctions(String className){
        JavaAbstract javaAbstract = javaProject.getClassByName(className);
        String methods[] = null;
        String invocations[] = null;
        
        
        if(javaAbstract.getClass() == JavaClass.class){
            System.out.println("\n\n ********** Classe: "+className);
            if(((JavaClass) javaAbstract).getSuperClass() != null){
                System.out.println("SuperClass: "+((JavaClass) javaAbstract).getSuperClass().getFullQualifiedName());
            }
            if(((JavaClass) javaAbstract).getImplementedInterfaces().size() > 0){
                System.out.println("Impemented Interfaces: ");
                for(JavaInterface javaInterface : ((JavaClass) javaAbstract).getImplementedInterfaces()){
                    System.out.println("- "+javaInterface.getFullQualifiedName());
                }
                System.out.println("");
            }
            if(((JavaClass) javaAbstract).getAttributes().size() > 0){
                System.out.println("Attributes: ");
                for(JavaAttribute javaAttribute : ((JavaClass) javaAbstract).getAttributes()){
                    JavaData javaData = javaAttribute.getType();
                    if(javaData.getClass() == JavaClass.class || javaData.getClass() == JavaInterface.class){
                        System.out.println("- "+((JavaAbstract) javaAttribute.getType()).getFullQualifiedName()+"    "+javaAttribute.getName());
                    }else if(javaData.getClass() == JavaPrimitiveType.class){
                        System.out.println("- "+((JavaPrimitiveType) javaAttribute.getType()).getName()+"    "+javaAttribute.getName());
                    }else if(javaData.getClass() == JavaAbstractExternal.class){
                        System.out.println("- "+((JavaAbstractExternal) javaAttribute.getType()).getName()+"    "+javaAttribute.getName());
                    }
                }
                System.out.println("");
            }
            methods = new String[((JavaClass) javaAbstract).getMethods().size()];
            System.out.println("Métodos: ");
            for(int i = 0; i < ((JavaClass) javaAbstract).getMethods().size(); i++){
                JavaData returnType = ((JavaClass) javaAbstract).getMethods().get(i).getReturnType();
                String retType = returnType == null ? "vazio" : returnType.getName();
                System.out.println("- "+retType+"       "+((JavaClass) javaAbstract).getMethods().get(i).getMethodSignature());
                System.out.println("Diff (in - out): "+((JavaClass) javaAbstract).getMethods().get(i).getDiff());
                methods[i] = ((JavaClass) javaAbstract).getMethods().get(i).getMethodSignature();
                for(JavaMethodInvocation jmi : ((JavaClass) javaAbstract).getMethods().get(i).getMethodInvocations()){
                    //System.out.println("------ "+jmi.getJavaAbstract().getFullQualifiedName()+":"+jmi.getJavaMethod().getMethodSignature());
                    //System.out.println("JMI JAVA METHOD: "+jmi.getJavaAbstract().getFullQualifiedName());
                    if(jmi.getJavaMethod() != null){
                        System.out.println("------ "+jmi.getJavaAbstract().getFullQualifiedName()+":"+jmi.getJavaMethod().getMethodSignature());
                    }else{
                        System.out.println("----um "+jmi.getJavaAbstract().getFullQualifiedName()+":"+jmi.getUnknowMethodName());

                    }
                }
                System.out.println("");
            }
            
            System.out.println("Classes that call the methods of this class: ");
            for(JavaClass javaClass : javaProject.getClassesThatCall(javaAbstract)){
                System.out.println("- "+javaClass.getFullQualifiedName());
            }
            System.out.println("Classes uses this class: ");
            for(JavaAbstract javaAbs : javaProject.getClassesThatUsing(javaAbstract)){
                System.out.println("- "+javaAbs.getFullQualifiedName());
            }
            
//            for(int i = 0; i < ((JavaClass) javaAbstract).getMethods().size(); i++){
//                invocations[i] = ((JavaClass) javaAbstract).getMethods().get(i).getMethodInvocations();
//            }
        }else if(javaAbstract.getClass() == JavaInterface.class){
            System.out.println("\n\n ********** Interface: "+className);
            System.out.println("Métodos: ");
            methods = new String[((JavaInterface) javaAbstract).getMethods().size()];
            for(int i = 0; i < ((JavaInterface) javaAbstract).getMethods().size(); i++){
                JavaData returnType = ((JavaInterface) javaAbstract).getMethods().get(i).getReturnType();
                String retType = returnType == null ? "void" : returnType.getName();
                System.out.println("- "+retType +"   "+((JavaInterface) javaAbstract).getMethods().get(i).getMethodSignature());
                methods[i] = ((JavaInterface) javaAbstract).getMethods().get(i).getMethodSignature();
            }
        }

        
    }
    
    public static void main(String args[]){
        InteractionController interactionController = new InteractionController();
        
    }
    
}
