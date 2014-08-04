/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.gui.controller;


import br.uff.ic.archd.gui.view.InteractionViewer;
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
    InteractionController(String projectPath){
        javaConstructorService = new JavaConstructorService();
        javaProject = javaConstructorService.createProjects(projectPath);
        //javaProject = javaConstructorService.createProjectsFromXML("/home/wallace/.archd/HISTORY/1/");
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
            interactionViewer.appendText("\n\n ********** Classe: "+className);
            if(((JavaClass) javaAbstract).getSuperClass() != null){
                System.out.println("SuperClass: "+((JavaClass) javaAbstract).getSuperClass().getFullQualifiedName());
                interactionViewer.appendText("SuperClass: "+((JavaClass) javaAbstract).getSuperClass().getFullQualifiedName());
            }
            if(((JavaClass) javaAbstract).getImplementedInterfaces().size() > 0){
                System.out.println("Impemented Interfaces: ");
                interactionViewer.appendText("Impemented Interfaces: ");
                for(JavaInterface javaInterface : ((JavaClass) javaAbstract).getImplementedInterfaces()){
                    System.out.println("- "+javaInterface.getFullQualifiedName());
                    interactionViewer.appendText("- "+javaInterface.getFullQualifiedName());
                }
                System.out.println("");
                interactionViewer.appendText("");
            }
            if(((JavaClass) javaAbstract).getExternalImports().size() > 0){
                System.out.println("External imports: ");
                interactionViewer.appendText("External imports: ");
                for(String externalImport : ((JavaClass) javaAbstract).getExternalImports()){
                    System.out.println("- "+externalImport);
                    interactionViewer.appendText("- "+externalImport);
                }
                System.out.println("");
                interactionViewer.appendText("");
            }
            if(((JavaClass) javaAbstract).getAttributes().size() > 0){
                System.out.println("Attributes: ");
                interactionViewer.appendText("Attributes: ");
                for(JavaAttribute javaAttribute : ((JavaClass) javaAbstract).getAttributes()){
                    JavaData javaData = javaAttribute.getType();
                    if(javaData.getClass() == JavaClass.class || javaData.getClass() == JavaInterface.class){
                        System.out.println("- "+((JavaAbstract) javaAttribute.getType()).getFullQualifiedName()+"    "+javaAttribute.getName());
                        interactionViewer.appendText("- "+((JavaAbstract) javaAttribute.getType()).getFullQualifiedName()+"    "+javaAttribute.getName());
                    }else if(javaData.getClass() == JavaPrimitiveType.class){
                        System.out.println("- "+((JavaPrimitiveType) javaAttribute.getType()).getName()+"    "+javaAttribute.getName());
                        interactionViewer.appendText("- "+((JavaPrimitiveType) javaAttribute.getType()).getName()+"    "+javaAttribute.getName());
                    }else if(javaData.getClass() == JavaAbstractExternal.class){
                        System.out.println("- "+((JavaAbstractExternal) javaAttribute.getType()).getName()+"    "+javaAttribute.getName());
                        interactionViewer.appendText("- "+((JavaAbstractExternal) javaAttribute.getType()).getName()+"    "+javaAttribute.getName());
                    }
                }
                System.out.println("");
                interactionViewer.appendText("");
            }
            methods = new String[((JavaClass) javaAbstract).getMethods().size()];
            System.out.println("Métodos: ");
            interactionViewer.appendText("Métodos: ");
            for(int i = 0; i < ((JavaClass) javaAbstract).getMethods().size(); i++){
                JavaData returnType = ((JavaClass) javaAbstract).getMethods().get(i).getReturnType();
                String retType = returnType == null ? "vazio" : returnType.getName();
                System.out.println("- "+retType+"       "+((JavaClass) javaAbstract).getMethods().get(i).getMethodSignature());
                System.out.println("Diff (in - out): "+((JavaClass) javaAbstract).getMethods().get(i).getDiff());
                System.out.println("Modifie internal state: "+((JavaClass) javaAbstract).getMethods().get(i).isChangeInternalState());
                System.out.println("Modifie internal state by call method: "+((JavaClass) javaAbstract).getMethods().get(i).isChangeInternalState());
                System.out.println("Size: "+((JavaClass) javaAbstract).getMethods().get(i).getSizeInChars());
                System.out.println("Cyclomatic complexity: "+((JavaClass) javaAbstract).getMethods().get(i).getCyclomaticComplexity());
                interactionViewer.appendText("- "+retType+"       "+((JavaClass) javaAbstract).getMethods().get(i).getMethodSignature());
                interactionViewer.appendText("Diff (in - out): "+((JavaClass) javaAbstract).getMethods().get(i).getDiff());
                interactionViewer.appendText("Modifie internal state: "+((JavaClass) javaAbstract).getMethods().get(i).isChangeInternalState());
                interactionViewer.appendText("Modifie internal state by call method: "+((JavaClass) javaAbstract).getMethods().get(i).isChangeInternalState());
                interactionViewer.appendText("Size: "+((JavaClass) javaAbstract).getMethods().get(i).getSizeInChars());
                interactionViewer.appendText("Cyclomatic complexity: "+((JavaClass) javaAbstract).getMethods().get(i).getCyclomaticComplexity());
                methods[i] = ((JavaClass) javaAbstract).getMethods().get(i).getMethodSignature();
                for(JavaMethodInvocation jmi : ((JavaClass) javaAbstract).getMethods().get(i).getMethodInvocations()){
                    //System.out.println("------ "+jmi.getJavaAbstract().getFullQualifiedName()+":"+jmi.getJavaMethod().getMethodSignature());
                    //System.out.println("JMI JAVA METHOD: "+jmi.getJavaAbstract().getFullQualifiedName());
                    if(jmi.getJavaMethod() != null){
                        System.out.println("------ "+jmi.getJavaAbstract().getFullQualifiedName()+":"+jmi.getJavaMethod().getMethodSignature());
                        interactionViewer.appendText("------ "+jmi.getJavaAbstract().getFullQualifiedName()+":"+jmi.getJavaMethod().getMethodSignature());
                    }else{
                        System.out.println("----um "+jmi.getJavaAbstract().getFullQualifiedName()+":"+jmi.getUnknowMethodName());
                        interactionViewer.appendText("----um "+jmi.getJavaAbstract().getFullQualifiedName()+":"+jmi.getUnknowMethodName());

                    }
                }
                System.out.println("");
                interactionViewer.appendText("");
            }
            
            System.out.println("Classes that call the methods of this class: ");
            interactionViewer.appendText("Classes that call the methods of this class: ");
            for(JavaClass javaClass : javaProject.getClassesThatCall(javaAbstract)){
                System.out.println("- "+javaClass.getFullQualifiedName());
                interactionViewer.appendText("- "+javaClass.getFullQualifiedName());
            }
            System.out.println("Classes uses this class: ");
            interactionViewer.appendText("Classes uses this class: ");
            for(JavaAbstract javaAbs : javaProject.getClassesThatUsing(javaAbstract)){
                System.out.println("- "+javaAbs.getFullQualifiedName());
                interactionViewer.appendText("- "+javaAbs.getFullQualifiedName());
            }
            
//            for(int i = 0; i < ((JavaClass) javaAbstract).getMethods().size(); i++){
//                invocations[i] = ((JavaClass) javaAbstract).getMethods().get(i).getMethodInvocations();
//            }
        }else if(javaAbstract.getClass() == JavaInterface.class){
            System.out.println("\n\n ********** Interface: "+className);
            interactionViewer.appendText("\n\n ********** Interface: "+className);
            System.out.println("Métodos: ");
            interactionViewer.appendText("Métodos: ");
            methods = new String[((JavaInterface) javaAbstract).getMethods().size()];
            for(int i = 0; i < ((JavaInterface) javaAbstract).getMethods().size(); i++){
                JavaData returnType = ((JavaInterface) javaAbstract).getMethods().get(i).getReturnType();
                String retType = returnType == null ? "void" : returnType.getName();
                System.out.println("- "+retType +"   "+((JavaInterface) javaAbstract).getMethods().get(i).getMethodSignature());
                interactionViewer.appendText("- "+retType +"   "+((JavaInterface) javaAbstract).getMethods().get(i).getMethodSignature());
                methods[i] = ((JavaInterface) javaAbstract).getMethods().get(i).getMethodSignature();
            }
        }

        
    }
    
    public static void main(String args[]){
        InteractionController interactionController = new InteractionController("/home/wallace/mestrado/projetos_alvos/neo4j/neo4j");
        
    }
    
}
