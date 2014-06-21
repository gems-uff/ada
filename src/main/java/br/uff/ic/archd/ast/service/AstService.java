/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.ast.service;


import br.uff.ic.archd.javacode.JavaAbstract;
import br.uff.ic.archd.javacode.JavaAbstractExternal;
import br.uff.ic.archd.javacode.JavaClass;
import br.uff.ic.archd.javacode.JavaData;
import br.uff.ic.archd.javacode.JavaInterface;
import br.uff.ic.archd.javacode.JavaMethod;
import br.uff.ic.archd.javacode.JavaMethodInvocation;
import br.uff.ic.archd.javacode.JavaNull;
import br.uff.ic.archd.javacode.JavaPrimitiveType;
import br.uff.ic.archd.javacode.JavaProject;
import br.uff.ic.archd.javacode.Parameter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author wallace
 */
public class AstService {

    public List<String> getAllJavaClassesPath(String path) {
        List<String> classesPath = new LinkedList();

        getClassesPath(classesPath, path);

        return classesPath;
    }

    private void getClassesPath(List<String> classesPath, String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            File files[] = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                getClassesPath(classesPath, files[i].getAbsolutePath());
            }
        } else {
            if (file.getAbsolutePath().endsWith(".java")) {
                classesPath.add(path);
            }
        }
    }

    public boolean isInterface(String path) {

        try {
            String content = readFile(path);
            org.eclipse.jface.text.Document doc = new org.eclipse.jface.text.Document(content);
            ASTParser parser = ASTParser.newParser(AST.JLS3);
            parser.setSource(doc.get().toCharArray());
            CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
            //compilationUnit.recordModifications();
            return ((TypeDeclaration) compilationUnit.types().get(0)).isInterface();

        } catch (Exception e) {
            System.out.println("Erro getClass Name: " + e.getMessage());
        }
        return false;


    }

    private String readFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }

        return stringBuilder.toString();
    }

    public String getClassName(String path) {
        try {
            String content = readFile(path);
            org.eclipse.jface.text.Document doc = new org.eclipse.jface.text.Document(content);
            ASTParser parser = ASTParser.newParser(AST.JLS3);
            parser.setSource(doc.get().toCharArray());
            CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
            //compilationUnit.recordModifications();
            //return compilationUnit.getPackage().getName() + "." + ((TypeDeclaration) compilationUnit.types().get(0)).getName().toString();
            return ((TypeDeclaration) compilationUnit.types().get(0)).getName().toString();



        } catch (Exception e) {
            System.out.println("Erro getClass Name: " + e.getMessage());
        }
        return null;

    }

    public String getPackage(String path) {
        try {
            String content = readFile(path);
            org.eclipse.jface.text.Document doc = new org.eclipse.jface.text.Document(content);
            ASTParser parser = ASTParser.newParser(AST.JLS3);
            parser.setSource(doc.get().toCharArray());
            CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
            //compilationUnit.recordModifications();
            return compilationUnit.getPackage().getName().toString();



        } catch (Exception e) {
            System.out.println("Erro getClass Name: " + e.getMessage());
        }
        return null;
    }

    public List<String> getImports(String classPath) {
        try {
            String content = readFile(classPath);
            //System.out.println("Path: "+path);
            org.eclipse.jface.text.Document doc = new org.eclipse.jface.text.Document(content);
            ASTParser parser = ASTParser.newParser(AST.JLS3);
            parser.setSource(doc.get().toCharArray());
            CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
            AST ast = compilationUnit.getAST();

            ASTNode astNode = compilationUnit.getRoot();

            List imports = compilationUnit.imports();
            //System.out.println("Imports: " + imports.size());

            List<String> importationsList = new LinkedList();
            for (int i = 0; i < imports.size(); i++) {
                ImportDeclaration importation = (ImportDeclaration) imports.get(i);
                importationsList.add(importation.getName().toString());
            }

            return importationsList;


        } catch (Exception e) {
            System.out.println("Erro getMethodsReturnType Name: " + e.getMessage());
        }
        return null;
    }

    public String getSuperClass(String classPath) {
        try {
            String content = readFile(classPath);
            //System.out.println("Path: "+path);
            org.eclipse.jface.text.Document doc = new org.eclipse.jface.text.Document(content);
            ASTParser parser = ASTParser.newParser(AST.JLS3);
            parser.setSource(doc.get().toCharArray());
            CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);


            Type superType = ((TypeDeclaration) compilationUnit.types().get(0)).getSuperclassType();
            String superClass = "java.lang.Object";
            if (superType != null) {
                superClass = superType.toString();
            }

            return superClass;



        } catch (Exception e) {
            System.out.println("Erro getMethodsReturnType Name: " + e.getMessage());
        }
        return null;
    }

    public List<String> getImplementedInterfaces(String classPath) {
        List<String> list = new ArrayList();
        try {
            String content = readFile(classPath);
            //System.out.println("Path: "+path);
            org.eclipse.jface.text.Document doc = new org.eclipse.jface.text.Document(content);
            ASTParser parser = ASTParser.newParser(AST.JLS3);
            parser.setSource(doc.get().toCharArray());
            CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);

            for (int i = 0; i < ((TypeDeclaration) compilationUnit.types().get(0)).superInterfaceTypes().size(); i++) {
                Name name = ((SimpleType) ((TypeDeclaration) compilationUnit.types().get(0)).superInterfaceTypes().get(i)).getName();
                String interfaceName = name.getFullyQualifiedName();
                list.add(interfaceName);
            }

            return list;



        } catch (Exception e) {
            System.out.println("Erro getMethodsReturnType Name: " + e.getMessage());
        }
        return list;
    }

    public List<ParameterAst> getAttributes(String classPath) {
        List<ParameterAst> list = new ArrayList();
        try {
            String content = readFile(classPath);
            //System.out.println("Path: "+path);
            org.eclipse.jface.text.Document doc = new org.eclipse.jface.text.Document(content);
            ASTParser parser = ASTParser.newParser(AST.JLS3);
            parser.setSource(doc.get().toCharArray());
            CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);





            FieldDeclaration fields[] = ((TypeDeclaration) compilationUnit.types().get(0)).getFields();
            for (int i = 0; i < fields.length; i++) {

                boolean isPublic = false;
                boolean isPrivate = false;
                boolean isProtected = false;
                boolean isFinal = false;
                boolean isStatic = false;
                boolean isVolatile = false;
                for (int j = 0; j < fields[i].modifiers().size(); j++) {
                    String modifier = fields[i].modifiers().get(j).toString();

                    if (modifier.equals("public")) {
                        isPublic = true;
                    } else if (modifier.equals("private")) {
                        isPrivate = true;
                    } else if (modifier.equals("final")) {
                        isFinal = true;
                    } else if (modifier.equals("static")) {
                        isStatic = true;
                    } else if (modifier.equals("volatile")) {
                        isVolatile = true;
                    } else if (modifier.equals("protected")) {
                        isProtected = true;
                    }

                }

                String returnType = fields[i].getType().toString();

                List<VariableDeclarationFragment> variables = fields[i].fragments();
                for (VariableDeclarationFragment vdf : variables) {
                    String variableName = vdf.getName().toString();
                    ParameterAst parameterAst = new ParameterAst(variableName, returnType);
                    if (isPublic) {
                        parameterAst.setPublic(true);
                    } else if (isPrivate) {
                        parameterAst.setPrivate(true);
                    } else if (isFinal) {
                        parameterAst.setFinal(true);
                    } else if (isStatic) {
                        parameterAst.setFinal(true);
                    } else if (isVolatile) {
                        parameterAst.setVolatile(true);
                    } else if (isProtected) {
                        parameterAst.setProtected(true);
                    }

                    list.add(parameterAst);

                }

            }


            return list;

        } catch (Exception e) {
            System.out.println("Erro getMethodsReturnType Name: " + e.getMessage());
        }
        return list;
    }

    public List<JavaMethodAstBox> getMethods(String classPath) {
        List<JavaMethodAstBox> list = new ArrayList();
        try {
            String content = readFile(classPath);
            //System.out.println("Path: "+path);
            org.eclipse.jface.text.Document doc = new org.eclipse.jface.text.Document(content);
            ASTParser parser = ASTParser.newParser(AST.JLS3);
            parser.setSource(doc.get().toCharArray());
            CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);

            MethodDeclaration methods[] = ((TypeDeclaration) compilationUnit.types().get(0)).getMethods();
            for (int i = 0; i < methods.length; i++) {


                String methodName = methods[i].getName().toString();


                //System.out.println("Return type do "+methods[i].getName() +"   qualifiedname : "+methodName+" :"+methods[i].getReturnType2());
                String returnTypeName = "void";
                if (methods[i].getReturnType2() != null) {
                    returnTypeName = methods[i].getReturnType2().toString();
                }
                JavaMethodAstBox javaMethodAstBox = new JavaMethodAstBox(methodName, returnTypeName, methods[i].getBody());
                List<ParameterAst> parameters = new ArrayList();

                for (int j = 0; j < methods[i].parameters().size(); j++) {

                    String paramTypeName = ((SingleVariableDeclaration) methods[i].parameters().get(j)).getType().toString();
                    String paramName = ((SingleVariableDeclaration) methods[i].parameters().get(j)).getName().toString();
                    ParameterAst parameterAst = new ParameterAst(paramName, paramTypeName);
                    parameters.add(parameterAst);
                }

                javaMethodAstBox.setParameters(parameters);

                for (int j = 0; j < methods[i].modifiers().size(); j++) {
                    //System.out.println("Mod: " + methods[i].modifiers().get(j).toString());
                    String modifier = methods[i].modifiers().get(j).toString();
                    if (modifier.equals("public")) {
                        javaMethodAstBox.setPublic(true);
                    } else if (modifier.equals("private")) {
                        javaMethodAstBox.setPrivate(true);
                    } else if (modifier.equals("final")) {
                        javaMethodAstBox.setFinal(true);
                    } else if (modifier.equals("static")) {
                        javaMethodAstBox.setStatic(true);
                    } else if (modifier.equals("abstract")) {
                        javaMethodAstBox.setAbstract(true);
                    } else if (modifier.equals("synchronized")) {
                        javaMethodAstBox.setSynchronized(true);
                    } else if (modifier.equals("protected")) {
                        javaMethodAstBox.setProtected(true);
                    }
                }

                list.add(javaMethodAstBox);


                //System.out.println("Metodo: "+methodType.getOficialMethodSignature());
            }

            return list;

        } catch (Exception e) {
            System.out.println("Erro getMethodsReturnType Name: " + e.getMessage());
        }
        return list;
    }

    public void getMethodInvocations(JavaClass javaClass, JavaProject javaProject) {
        try {

            for (JavaMethod javaMethod : javaClass.getMethods()) {
                //System.out.println("Classe: "+javaClass.getFullQualifiedName());
                Block block = javaMethod.getBlock();
                //System.out.println("Classe: "+javaClass.getFullQualifiedName());
                javaMethod.setSizeInChars(block.getLength());
                BlockVariablesBox blockVariablesBox = getVariableDeclarations(block, javaClass, javaProject);
                MethodInvocationVisitor miv = new MethodInvocationVisitor();
                block.accept(miv);
                List<MethodInvocation> listmi = miv.getMethods();
                for (MethodInvocation methinv : listmi) {

                    //System.out.println("METHOD INVOCATION: " + methinv.toString());
                    //System.out.println("MI EXPRESSSION: " + methinv.getExpression());
                    JavaData returnClassType = getClassReturnType(methinv.getExpression(), javaClass, javaMethod,blockVariablesBox);
                    if(javaClass.getFullQualifiedName().equals("org.gjt.sp.util.IOUtilities")){
                        System.out.println("method: "+javaMethod.getName()+" expression: "+methinv.getExpression()+"    return type: "+(returnClassType == null? "nulo" : returnClassType.getName()));
                    }
                    if (returnClassType != null && (returnClassType.getClass() == JavaClass.class || returnClassType.getClass() == JavaInterface.class)) {
                        JavaMethod javaMethodCall = null;
                        JavaAbstract javaAbstract = (JavaAbstract) returnClassType;
                        List<JavaData> arguments = new ArrayList();
                        
                        for(int i =0; i < methinv.arguments().size(); i++){
                            Expression argument = (Expression) methinv.arguments().get(i);
                            JavaData javaData = getClassReturnType(argument, javaClass, javaMethod,blockVariablesBox);
                            if(javaClass.getFullQualifiedName().equals("org.gjt.sp.util.IOUtilities")){
                                System.out.println("javadataClass: "+(argument.getClass())+"   argument: "+argument);
                                System.out.println("javaData: "+(javaData ==null ? "nulo" : javaData.getName()));
                            }
                            arguments.add(javaData);
                        }
                        
                        
                        if (returnClassType.getClass() == JavaClass.class) {
                            javaMethodCall = ((JavaClass) returnClassType).getJavaMethod(methinv.getName().toString(), methinv.arguments().size(), arguments);

                        } else if (returnClassType.getClass() == JavaInterface.class) {
                            javaMethodCall = ((JavaInterface) returnClassType).getJavaMethod(methinv.getName().toString(), methinv.arguments().size(), arguments);
                        }
                        
                        JavaMethodInvocation javaMethoInvocation = new JavaMethodInvocation(javaAbstract, javaMethodCall);
                        if(javaMethodCall == null){
                            javaMethoInvocation.setUnknowMethodName(methinv.getName().toString()+"()");
                            //System.out.println("Java METHOD call : "+javaAbstract.getFullQualifiedName()+"   "+methinv.getName().toString()+"   _ "+methinv.arguments().size());
                        }
                        javaMethod.addMethodInvocation(javaMethoInvocation);

                    }
                }

            }


        } catch (Exception e) {
            System.out.println("Erro getMethodInvocations "+javaClass.getFullQualifiedName()+" Name: " + e.getMessage());
        }
    }

    private JavaData getClassReturnType(Expression expression, JavaClass javaClass, JavaMethod javaMethod, BlockVariablesBox blockVariablesBox) {
        String classExpression = "";

        if (expression == null) {
            return null;
        } else {
            if (expression.getClass() == org.eclipse.jdt.core.dom.ParenthesizedExpression.class) {
                return getClassReturnType(((ParenthesizedExpression) expression).getExpression(), javaClass, javaMethod, blockVariablesBox);
            } else if (expression.getClass() == org.eclipse.jdt.core.dom.CastExpression.class) {
                //get the type expression

                String classReturnType = ((CastExpression) expression).getType().toString();
                JavaData javaAbstract = javaClass.getJavaAbstractImportByName(classReturnType);
                if(javaAbstract == null){

                    return new JavaAbstractExternal(classReturnType);
                    
                }else{
                    return javaAbstract;
                }

            } else if (expression.getClass() == org.eclipse.jdt.core.dom.SimpleName.class) {
                //JavaData classReturnType = hashMethodDeclarations.get(((SimpleName) expression).toString());
                JavaData classReturnType = blockVariablesBox.getJavaDataByVariableName(((SimpleName) expression).toString(), expression.getParent());
                if(classReturnType == null){
                    for(Parameter parameter : javaMethod.getParameters()){
                        if(parameter.getName().equals(((SimpleName) expression).toString())){
                            classReturnType = parameter.getType();
                            break;
                        }
                    }
                        
                }
                if (classReturnType == null) {
                    classReturnType = javaClass.getJavaTypeByVariableName(((SimpleName) expression).toString());
                }
                return classReturnType;
            } else if (expression.getClass() == org.eclipse.jdt.core.dom.MethodInvocation.class) {
                return getClassReturnType(((MethodInvocation) expression).getExpression(), javaClass, javaMethod, blockVariablesBox);
            }else if (expression.getClass() == org.eclipse.jdt.core.dom.NullLiteral.class) {
                return new JavaNull();
            }else if (expression.getClass() == org.eclipse.jdt.core.dom.NumberLiteral.class) {
                if(expression.toString().contains(".")){
                    return new JavaPrimitiveType(JavaPrimitiveType.getType("double"));
                }else if(expression.toString().endsWith("l")){
                    return new JavaPrimitiveType(JavaPrimitiveType.getType("long"));
                }else if(expression.toString().endsWith("f")){
                    return new JavaPrimitiveType(JavaPrimitiveType.getType("float"));
                }else{
                    return new JavaPrimitiveType(JavaPrimitiveType.getType("int"));
                }
            }else if (expression.getClass() == org.eclipse.jdt.core.dom.ArrayAccess.class) {
                return getClassReturnType(((ArrayAccess) expression).getArray(), javaClass, javaMethod, blockVariablesBox);
            }else if (expression.getClass() == org.eclipse.jdt.core.dom.ArrayCreation.class) {
                String classReturnType = ((ArrayCreation) expression).getType().toString();
                if(JavaPrimitiveType.getType(classReturnType) != 0 ){
                    return new JavaPrimitiveType(JavaPrimitiveType.getType(classReturnType));
                }
                JavaData javaAbstract = javaClass.getJavaAbstractImportByName(classReturnType);
                return javaAbstract;
            }else if (expression.getClass() == org.eclipse.jdt.core.dom.BooleanLiteral.class) {
                return new JavaPrimitiveType(JavaPrimitiveType.getType("boolean"));
            }else if (expression.getClass() == org.eclipse.jdt.core.dom.CharacterLiteral.class) {
                return new JavaPrimitiveType(JavaPrimitiveType.getType("char"));
            }else if (expression.getClass() == org.eclipse.jdt.core.dom.ClassInstanceCreation.class) {
                String classReturnType = ((ClassInstanceCreation) expression).getType().toString();
                JavaData javaAbstract = javaClass.getJavaAbstractImportByName(classReturnType);
                return javaAbstract;
            }else if (expression.getClass() == org.eclipse.jdt.core.dom.TypeLiteral.class) {
                String classReturnType = ((TypeLiteral) expression).getType().toString();
                JavaData javaAbstract = javaClass.getJavaAbstractImportByName(classReturnType);
                return javaAbstract;
            }else if (expression.getClass() == org.eclipse.jdt.core.dom.StringLiteral.class) {
                //System.out.println("String literal");
                JavaAbstractExternal javaAbstractExternal = new JavaAbstractExternal("String");
                //System.out.println("String literal: "+javaAbstractExternal.getName());
                return javaAbstractExternal;
            }else if (expression.getClass() == org.eclipse.jdt.core.dom.InfixExpression.class) {
                //System.out.println("Left: "+((InfixExpression) expression).getLeftOperand().getClass());
                return getClassReturnType(((InfixExpression) expression).getLeftOperand(), javaClass, javaMethod, blockVariablesBox);
            }
            
        }
        return null;
    }
    
    
    

    private BlockVariablesBox getVariableDeclarations(Block block, JavaClass javaClass, JavaProject javaProject) {
        BlockVariablesBox blockVariablesBox = new BlockVariablesBox(block);
        MethodInvocationVisitor miv = new MethodInvocationVisitor();
        block.accept(miv);
        List<VariableDeclarationExpression> variables = miv.getDeclarations();
//        if(javaClass.getFullQualifiedName().equals("org.gjt.sp.util.IOUtilities")){
//            for(int i=0; i < block.statements().size();i++){
//                Statement statement = (Statement) block.statements().get(i);
//                System.out.println("Statemente: "+statement+"    tipo: "+statement.getClass());
//                if(statement.getClass() == IfStatement.class){
//                    System.out.println(" then: "+(((IfStatement) statement).getThenStatement() == null ? ((IfStatement) statement).getThenStatement() : ((IfStatement) statement).getThenStatement().getClass()));
//                    System.out.println("else: "+((((IfStatement) statement).getElseStatement() == null ? ((IfStatement) statement).getElseStatement() : ((IfStatement) statement).getElseStatement().getClass())));
//                }
//                
//            }
//        }
        //System.out.println("Declarações de variáveis " + variables.size());
        for (VariableDeclarationExpression var : variables) {
            String type = var.getType().toString();
            //System.out.println("var: "+var+"  Parent: "+var.getParent());
            JavaData javaData = javaClass.getJavaAbstractImportByName(type);
            if (javaData == null) {
                if (JavaPrimitiveType.getType(type) != 0) {
                    // the attribute is primitive type
                    javaData = new JavaPrimitiveType(JavaPrimitiveType.getType(type));
                } else {
                    // the attribute is a external class
                    //get the complete name of the class
                    String typeFullName = javaClass.getExternalImportByLastName(type);
                    //get external class from javaproject
                    JavaAbstractExternal javaAbstractExternal = javaProject.getJavaExternalClassByName(typeFullName);
                    if (javaAbstractExternal == null) {
                        //create new external class and add to the projetc
                        javaAbstractExternal = new JavaAbstractExternal(typeFullName);
                        javaProject.addExternalClass(javaAbstractExternal);
                        
                    }
                    javaData = javaAbstractExternal;
                }
            }
            List<VariableDeclarationFragment> frags = var.fragments();
            for (VariableDeclarationFragment frag : frags) {

                Variable variable = new Variable(frag.getName().toString(),javaData);
                blockVariablesBox.addVariable(variable, var.getParent());
                


            }
        }

        List<VariableDeclarationStatement> variableDeclarations = miv.getVariableDeclarations();
        for (VariableDeclarationStatement var : variableDeclarations) {
            //System.out.println("var: "+var+"  Parent: "+var.getParent());
            String type = var.getType().toString();
            JavaData javaData = javaClass.getJavaAbstractImportByName(type);
            if (javaData == null) {
                if (JavaPrimitiveType.getType(type) != 0) {
                    // the attribute is primitive type
                    javaData = new JavaPrimitiveType(JavaPrimitiveType.getType(type));
                } else {
                    // the attribute is a external class
                    //get the complete name of the class
                    String typeFullName = javaClass.getExternalImportByLastName(type);
                    //get external class from javaproject
                    JavaAbstractExternal javaAbstractExternal = javaProject.getJavaExternalClassByName(typeFullName);
                    if (javaAbstractExternal == null) {
                        //create new external class and add to the projetc
                        javaAbstractExternal = new JavaAbstractExternal(typeFullName);
                        javaProject.addExternalClass(javaAbstractExternal);
                        
                    }
                    javaData = javaAbstractExternal;
                }
            }
            List<VariableDeclarationFragment> frags = var.fragments();
            for (VariableDeclarationFragment frag : frags) {


                Variable variable = new Variable(frag.getName().toString(),javaData);
                blockVariablesBox.addVariable(variable, var.getParent());
                

            }
        }

        List<SingleVariableDeclaration> singleVariables = miv.getSingleDeclarations();
        //System.out.println("Declarações single variáveis " + singleVariables.size());
        for (SingleVariableDeclaration var : singleVariables) {

            //System.out.println("var: "+var+"  Parent: "+var.getParent());
            String type = var.getType().toString();
            JavaData javaData = javaClass.getJavaAbstractImportByName(type);
            if (javaData == null) {
                if (JavaPrimitiveType.getType(type) != 0) {
                    // the attribute is primitive type
                    javaData = new JavaPrimitiveType(JavaPrimitiveType.getType(type));
                } else {
                    // the attribute is a external class
                    //get the complete name of the class
                    String typeFullName = javaClass.getExternalImportByLastName(type);
                    //get external class from javaproject
                    JavaAbstractExternal javaAbstractExternal = javaProject.getJavaExternalClassByName(typeFullName);
                    if (javaAbstractExternal == null) {
                        //create new external class and add to the projetc
                        javaAbstractExternal = new JavaAbstractExternal(typeFullName);
                        javaProject.addExternalClass(javaAbstractExternal);
                        
                    }
                    javaData = javaAbstractExternal;
                }
            }

            Variable variable = new Variable(var.getName().toString(),javaData);
            blockVariablesBox.addVariable(variable, var.getParent());
            

        }
        
        return blockVariablesBox;
    }
}