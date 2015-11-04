/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.ast.service;

import br.uff.ic.archd.javacode.JavaAbstract;
import br.uff.ic.archd.javacode.JavaAbstractExternal;
import br.uff.ic.archd.javacode.JavaAttribute;
import br.uff.ic.archd.javacode.JavaClass;
import br.uff.ic.archd.javacode.JavaData;
import br.uff.ic.archd.javacode.JavaExternalAttributeAccess;
import br.uff.ic.archd.javacode.JavaInterface;
import br.uff.ic.archd.javacode.JavaMethod;
import br.uff.ic.archd.javacode.JavaMethodInvocation;
import br.uff.ic.archd.javacode.JavaNull;
import br.uff.ic.archd.javacode.JavaPrimitiveType;
import br.uff.ic.archd.javacode.JavaProject;
import br.uff.ic.archd.javacode.JavaProjectFileService;
import br.uff.ic.archd.javacode.Parameter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javancss.FunctionMetric;
import javancss.Javancss;
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
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.ReturnStatement;
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
            System.out.println("Erro isinterface Name: " + e.getMessage());
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
            System.out.println("Erro getClass Name: " + path + "     " + e.getMessage());
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
            System.out.println("Erro package Name: " + e.getMessage());
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
            System.out.println("Erro getImports Name: " + e.getMessage());
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
            System.out.println("Erro getsuperclass Name: " + e.getMessage());
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
                if (((TypeDeclaration) compilationUnit.types().get(0)).superInterfaceTypes().get(i).getClass() == org.eclipse.jdt.core.dom.ParameterizedType.class) {
                    //((ParameterizedType) ((TypeDeclaration) compilationUnit.types().get(0)).superInterfaceTypes().get(i)).
                } else if (((TypeDeclaration) compilationUnit.types().get(0)).superInterfaceTypes().get(i).getClass() == org.eclipse.jdt.core.dom.SimpleType.class) {
                    //System.out.println("interface: "+((TypeDeclaration) compilationUnit.types().get(0)).superInterfaceTypes().get(i));
                    Name name = ((SimpleType) ((TypeDeclaration) compilationUnit.types().get(0)).superInterfaceTypes().get(i)).getName();
                    String interfaceName = name.getFullyQualifiedName();
                    list.add(interfaceName);
                }
            }

            return list;

        } catch (Exception e) {
            System.out.println("Erro getImplementedInterfaces " + classPath + "  Name: " + e.getMessage());
        }
        return list;
    }

    /*
     public void getExternalAttributeAccessFromClass(JavaClass javaClass, String classPath){
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
                
     MethodInvocationVisitor miv = new MethodInvocationVisitor();
     fields[i].accept(miv);
                
     miv.getSimpleNames();

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



     } catch (Exception e) {
     System.out.println("Erro getExternalAttributeAccessFromClass Name: " + e.getMessage());
     }
     }
     * 
     * */
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
            System.out.println("Erro getAttributes Name: " + e.getMessage());
        }
        return list;
    }

    public List<JavaMethodAstBox> getMethods(String classPath) {
        List<JavaMethodAstBox> list = new ArrayList();
        String contentForJavancss = null;

        try {
            String content = readFile(classPath);
            //System.out.println("Path: "+classPath);

            org.eclipse.jface.text.Document doc = new org.eclipse.jface.text.Document(content);
            ASTParser parser = ASTParser.newParser(AST.JLS3);
            parser.setSource(doc.get().toCharArray());
            CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);

            List functionMetrics = new ArrayList();
            contentForJavancss = content.replace("<>", "");
            content = null;

            if (contentForJavancss.contains("try")) {
                System.out.println("AQUI 1");
                contentForJavancss = removeComments(contentForJavancss);
                System.out.println("AQUI 2");
                //System.gc();
                //System.out.println("Foi removecomments");
                contentForJavancss = removeLiteralStrings(contentForJavancss);
                System.out.println("AQUI 3");
                //System.gc();
                //System.out.println("Foi removeLiteralStrings");
                contentForJavancss = removeTryWithResources(contentForJavancss);
                System.out.println("AQUI 4");
                //System.gc();
                //System.out.println("Foi removeTryWithResources");
                contentForJavancss = removeMultipleCatch(contentForJavancss);
                System.out.println("AQUI 5");
                //System.gc();
                //System.out.println("Foi removeMultipleCatch");
            }
            //System.gc();
            Reader reader = new StringReader(contentForJavancss);

            try {
                Javancss javancss = new Javancss(reader);
                functionMetrics = javancss.getFunctionMetrics();
                //System.out.println("Funcion metrics: " + functionMetrics.size());
                /*for (Object object : functionMetrics) {
                 FunctionMetric functionMetric = (FunctionMetric) object;
                 //System.out.println("Name: " + functionMetric.toString());
                 }*/

            } catch (Exception e) {
                System.out.println("Error javancss: " + e.getMessage());
            }

            //System.out.println("Passou: "+classPath);
            MethodDeclaration methods[] = ((TypeDeclaration) compilationUnit.types().get(0)).getMethods();
            System.out.println("Methods size: "+methods.length);
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

                if (functionMetrics.size() > i) {
                    javaMethodAstBox.setCyclomaticComplexity(((FunctionMetric) functionMetrics.get(i)).ccn);
                }

                list.add(javaMethodAstBox);

                //System.out.println("Metodo: "+methodType.getOficialMethodSignature());
            }

            return list;

        } catch (Exception e) {
            System.out.println("Erro getMethods Name: " + e.getMessage() + "    - " + classPath);
            e.printStackTrace();
            //System.out.println(contentForJavancss);
        }
        return list;
    }

    public void getMethodInvocations(JavaClass javaClass, JavaProject javaProject) {
        try {

            for (JavaMethod javaMethod : javaClass.getMethods()) {
                //System.out.println("Classe: "+javaClass.getFullQualifiedName()+":"+javaMethod.getMethodSignature());
                Block block = javaMethod.getBlock();
                if (block != null) {
                    //System.out.println("Classe: "+javaClass.getFullQualifiedName());
                    javaMethod.setSizeInChars(block.getLength());
                    javaMethod.setNumberOfLines(block.toString().split("\r\n|\r|\n").length);
                    BlockVariablesBox blockVariablesBox = getVariableDeclarations(block, javaClass, javaProject);
                    MethodInvocationVisitor miv = new MethodInvocationVisitor();
                    block.accept(miv);
                    List<ReturnStatement> returnStatements = miv.getReturnStatements();
                    //verificar se o método retorna algum atributo
                    if (returnStatements.size() == 1) {
                        ReturnStatement returnStatement = returnStatements.get(0);
                        if (returnStatement.getExpression() != null && returnStatement.getExpression().getClass() == org.eclipse.jdt.core.dom.SimpleName.class) {
                            if (blockVariablesBox.getJavaDataByVariableName(((SimpleName) returnStatement.getExpression()).toString(), returnStatement.getExpression().getParent()) == null) {
                                for (JavaAttribute javaAttribute : javaClass.getAttributes()) {
                                    if (javaAttribute.getName().equals(((SimpleName) returnStatement.getExpression()).toString())) {
                                        javaMethod.setIsAnAcessorMethod(true);
                                        javaMethod.setAccessedAttribute(javaAttribute.getName());
                                        break;
                                    }
                                }
                            }
                        } else if (returnStatement.getExpression() != null && returnStatement.getExpression().getClass() == org.eclipse.jdt.core.dom.FieldAccess.class) {
                            if (((FieldAccess) returnStatement.getExpression()).getExpression().toString().equals("this")) {
                                for (JavaAttribute javaAttribute : javaClass.getAttributes()) {
                                    if (javaAttribute.getName().equals(((SimpleName) ((FieldAccess) returnStatement.getExpression()).getName()).toString())) {
                                        javaMethod.setIsAnAcessorMethod(true);
                                        javaMethod.setAccessedAttribute(javaAttribute.getName());
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    List<Assignment> assignments = miv.getAssignments();
                    for (Assignment assignment : assignments) {
                        boolean changeInternalState = false;
                        if (assignment.getLeftHandSide().getClass() == org.eclipse.jdt.core.dom.SimpleName.class) {
                            if (blockVariablesBox.getJavaDataByVariableName(((SimpleName) assignment.getLeftHandSide()).toString(), assignment.getLeftHandSide().getParent()) == null) {
                                for (JavaAttribute javaAttribute : javaClass.getAttributes()) {
                                    if (javaAttribute.getName().equals(((SimpleName) assignment.getLeftHandSide()).toString())) {
                                        changeInternalState = true;
                                    }
                                }
                            }
                        } else if (assignment.getLeftHandSide().getClass() == org.eclipse.jdt.core.dom.FieldAccess.class) {
                            if (((FieldAccess) assignment.getLeftHandSide()).getExpression().toString().equals("this")) {
                                for (JavaAttribute javaAttribute : javaClass.getAttributes()) {
                                    if (javaAttribute.getName().equals(((SimpleName) ((FieldAccess) assignment.getLeftHandSide()).getName()).toString())) {
                                        changeInternalState = true;
                                    }
                                }
                            }
                        } else if (assignment.getLeftHandSide().getClass() == org.eclipse.jdt.core.dom.ArrayAccess.class) {
                            String arrayName = assignment.getLeftHandSide().toString().substring(0, assignment.getLeftHandSide().toString().indexOf("["));
                            if (blockVariablesBox.getJavaDataByVariableName(arrayName, assignment.getLeftHandSide().getParent()) == null) {
                                for (JavaAttribute javaAttribute : javaClass.getAttributes()) {
                                    if (javaAttribute.getName().equals(arrayName)) {
                                        changeInternalState = true;
                                    }
                                }
                            }
                        }
                        if (assignment.getLeftHandSide().getClass() != org.eclipse.jdt.core.dom.SimpleName.class
                                && assignment.getLeftHandSide().getClass() != org.eclipse.jdt.core.dom.FieldAccess.class
                                && assignment.getLeftHandSide().getClass() != org.eclipse.jdt.core.dom.ArrayAccess.class
                                && assignment.getLeftHandSide().getClass() != org.eclipse.jdt.core.dom.QualifiedName.class) {
                            System.out.println("Assignment: class: " + javaClass.getFullQualifiedName() + "     " + assignment.getLeftHandSide() + "    class: " + assignment.getLeftHandSide().getClass());
                        }
                        if (changeInternalState) {
                            javaMethod.setChangeInternalState(true);
                        }
                    }

                    List<MethodInvocation> listmi = miv.getMethods();
                    for (MethodInvocation methinv : listmi) {

                        //System.out.println("METHOD INVOCATION: " + methinv.toString());
                        //System.out.println("MI EXPRESSSION: " + methinv.getExpression());
                        if (methinv.getExpression() != null && !methinv.getExpression().toString().equals("this")) {
                            JavaData returnClassType = getClassReturnType(methinv.getExpression(), javaClass, javaMethod, blockVariablesBox);
//                    if(javaClass.getFullQualifiedName().equals("org.gjt.sp.util.IOUtilities")){
//                        System.out.println("method: "+javaMethod.getName()+" expression: "+methinv.getExpression()+"    return type: "+(returnClassType == null? "nulo" : returnClassType.getName()));
//                    }
                            if (returnClassType != null && (returnClassType.getClass() == JavaClass.class || returnClassType.getClass() == JavaInterface.class)) {
                                JavaMethod javaMethodCall = null;
                                JavaAbstract javaAbstract = (JavaAbstract) returnClassType;
                                List<JavaData> arguments = new ArrayList();

                                for (int i = 0; i < methinv.arguments().size(); i++) {
                                    Expression argument = (Expression) methinv.arguments().get(i);
                                    JavaData javaData = getClassReturnType(argument, javaClass, javaMethod, blockVariablesBox);
//                            if(javaClass.getFullQualifiedName().equals("org.gjt.sp.util.IOUtilities")){
//                                System.out.println("javadataClass: "+(argument.getClass())+"   argument: "+argument);
//                                System.out.println("javaData: "+(javaData ==null ? "nulo" : javaData.getName()));
//                            }
                                    arguments.add(javaData);
                                }

                                if (returnClassType.getClass() == JavaClass.class) {
                                    javaMethodCall = ((JavaClass) returnClassType).getJavaMethod(methinv.getName().toString(), methinv.arguments().size(), arguments);

                                } else if (returnClassType.getClass() == JavaInterface.class) {
                                    javaMethodCall = ((JavaInterface) returnClassType).getJavaMethod(methinv.getName().toString(), methinv.arguments().size(), arguments);
                                }

                                if (javaAbstract.getClass() == JavaClass.class) {
                                    JavaClass jc = (JavaClass) javaAbstract;
                                    if (javaClass == jc || javaClass.isInheritedClass(jc)) {

                                        JavaMethodInvocation javaMethoInvocation = new JavaMethodInvocation(javaAbstract, javaMethodCall);
                                        if (javaMethodCall == null) {
                                            javaMethoInvocation.setUnknowMethodName(methinv.getName().toString() + "()");
                                            //System.out.println("Java METHOD call : " + javaAbstract.getFullQualifiedName() + "   " + methinv.getName().toString() + "   _ " + methinv.arguments().size());
                                        }
//                                        if (javaMethoInvocation.getJavaMethod() != null) {
//                                            System.out.println(" 1 -" + javaMethod.getJavaAbstract().getFullQualifiedName() + ":" + javaMethod.getMethodSignature() + "   size: " + javaMethod.getMethodInvocations().size());
//
//                                            System.out.println("------" + javaMethoInvocation.getJavaMethod().getJavaAbstract().getFullQualifiedName() + ":" + javaMethoInvocation.getJavaMethod().getMethodSignature() + "    CM: " + javaMethoInvocation.getJavaMethod().getChangingMethodsMetric() + "   CC: " + javaMethoInvocation.getJavaMethod().getChangingClassesMetric()
//                                                    + "     code: " + javaMethoInvocation.getJavaMethod() + "    " + (javaMethoInvocation.getJavaMethod().getJavaAbstract().getClass() == JavaClass.class ? "classe" : "interface"));
//                                        }
                                        javaMethod.addInternalMethodInvocation(javaMethoInvocation);
                                    } else {
                                        JavaMethodInvocation javaMethoInvocation = new JavaMethodInvocation(javaAbstract, javaMethodCall);
                                        if (javaMethodCall == null) {
                                            javaMethoInvocation.setUnknowMethodName(methinv.getName().toString() + "()");
                                            //System.out.println("Java METHOD call : " + javaAbstract.getFullQualifiedName() + "   " + methinv.getName().toString() + "   _ " + methinv.arguments().size());
                                        }
                                        javaMethod.addMethodInvocation(javaMethoInvocation);
                                    }
                                } else {
                                    JavaInterface ji = (JavaInterface) javaAbstract;
                                    if (javaClass.implementsInterface(ji)) {

                                        JavaMethodInvocation javaMethoInvocation = new JavaMethodInvocation(javaAbstract, javaMethodCall);
                                        if (javaMethodCall == null) {
                                            javaMethoInvocation.setUnknowMethodName(methinv.getName().toString() + "()");
                                            //System.out.println("Java METHOD call : " + javaAbstract.getFullQualifiedName() + "   " + methinv.getName().toString() + "   _ " + methinv.arguments().size());
                                        }

                                        javaMethod.addInternalMethodInvocation(javaMethoInvocation);
                                    } else {
                                        JavaMethodInvocation javaMethoInvocation = new JavaMethodInvocation(javaAbstract, javaMethodCall);
                                        if (javaMethodCall == null) {
                                            javaMethoInvocation.setUnknowMethodName(methinv.getName().toString() + "()");
                                            //System.out.println("Java METHOD call : " + javaAbstract.getFullQualifiedName() + "   " + methinv.getName().toString() + "   _ " + methinv.arguments().size());
                                        }
                                        javaMethod.addMethodInvocation(javaMethoInvocation);
                                    }
                                }

                                //System.out.println("AQUI PASSOU");
                            }
                        } else {
                            JavaMethod javaMethodCall = null;
                            List<JavaData> arguments = new ArrayList();

                            for (int i = 0; i < methinv.arguments().size(); i++) {
                                Expression argument = (Expression) methinv.arguments().get(i);
                                JavaData javaData = getClassReturnType(argument, javaClass, javaMethod, blockVariablesBox);
                                arguments.add(javaData);
                            }

                            javaMethodCall = javaClass.getJavaMethod(methinv.getName().toString(), arguments.size(), arguments);
                            if (javaMethodCall != null) {
                                JavaMethodInvocation javaMethoInvocation = new JavaMethodInvocation(javaClass, javaMethodCall);

                                javaMethod.addInternalMethodInvocation(javaMethoInvocation);
                            }

                        }
                    }
                }

            }

        } catch (Exception e) {
            System.out.println("Erro getMethodInvocations " + javaClass.getFullQualifiedName() + " Name: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /*public void getMethodInvocationsOffMemory(JavaClass javaClass, JavaProject javaProject, JavaProjectFileService javaProjectFileService) {
     try {

     List<String> javaMethods = javaProjectFileService.getMethodsSignatures(javaClass.getName(), javaClass.getJavaPackage().getName());
     for (String javaMethod : javaMethods) {
     //System.out.println("Classe: "+javaClass.getFullQualifiedName()+":"+javaMethod.getMethodSignature());
     String block = javaProjectFileService.getMethodCode(javaClass.getName(), javaClass.getJavaPackage().getName(), javaMethod);
     if (block != null) {
     //System.out.println("Classe: "+javaClass.getFullQualifiedName());
     javaMethod.setSizeInChars(block.length());
     javaMethod.setNumberOfLines(block.split("\n").length);
     BlockVariablesBox blockVariablesBox = getVariableDeclarations(block, javaClass, javaProject);
     MethodInvocationVisitor miv = new MethodInvocationVisitor();
     block.accept(miv);
     List<ReturnStatement> returnStatements = miv.getReturnStatements();
     //verificar se o método retorna algum atributo
     if (returnStatements.size() == 1) {
     ReturnStatement returnStatement = returnStatements.get(0);
     if (returnStatement.getExpression() != null && returnStatement.getExpression().getClass() == org.eclipse.jdt.core.dom.SimpleName.class) {
     if (blockVariablesBox.getJavaDataByVariableName(((SimpleName) returnStatement.getExpression()).toString(), returnStatement.getExpression().getParent()) == null) {
     for (JavaAttribute javaAttribute : javaClass.getAttributes()) {
     if (javaAttribute.getName().equals(((SimpleName) returnStatement.getExpression()).toString())) {
     javaMethod.setIsAnAcessorMethod(true);
     javaMethod.setAccessedAttribute(javaAttribute.getName());
     break;
     }
     }
     }
     } else if (returnStatement.getExpression() != null && returnStatement.getExpression().getClass() == org.eclipse.jdt.core.dom.FieldAccess.class) {
     if (((FieldAccess) returnStatement.getExpression()).getExpression().toString().equals("this")) {
     for (JavaAttribute javaAttribute : javaClass.getAttributes()) {
     if (javaAttribute.getName().equals(((SimpleName) ((FieldAccess) returnStatement.getExpression()).getName()).toString())) {
     javaMethod.setIsAnAcessorMethod(true);
     javaMethod.setAccessedAttribute(javaAttribute.getName());
     break;
     }
     }
     }
     }
     }

     List<Assignment> assignments = miv.getAssignments();
     for (Assignment assignment : assignments) {
     boolean changeInternalState = false;
     if (assignment.getLeftHandSide().getClass() == org.eclipse.jdt.core.dom.SimpleName.class) {
     if (blockVariablesBox.getJavaDataByVariableName(((SimpleName) assignment.getLeftHandSide()).toString(), assignment.getLeftHandSide().getParent()) == null) {
     for (JavaAttribute javaAttribute : javaClass.getAttributes()) {
     if (javaAttribute.getName().equals(((SimpleName) assignment.getLeftHandSide()).toString())) {
     changeInternalState = true;
     }
     }
     }
     } else if (assignment.getLeftHandSide().getClass() == org.eclipse.jdt.core.dom.FieldAccess.class) {
     if (((FieldAccess) assignment.getLeftHandSide()).getExpression().toString().equals("this")) {
     for (JavaAttribute javaAttribute : javaClass.getAttributes()) {
     if (javaAttribute.getName().equals(((SimpleName) ((FieldAccess) assignment.getLeftHandSide()).getName()).toString())) {
     changeInternalState = true;
     }
     }
     }
     } else if (assignment.getLeftHandSide().getClass() == org.eclipse.jdt.core.dom.ArrayAccess.class) {
     String arrayName = assignment.getLeftHandSide().toString().substring(0, assignment.getLeftHandSide().toString().indexOf("["));
     if (blockVariablesBox.getJavaDataByVariableName(arrayName, assignment.getLeftHandSide().getParent()) == null) {
     for (JavaAttribute javaAttribute : javaClass.getAttributes()) {
     if (javaAttribute.getName().equals(arrayName)) {
     changeInternalState = true;
     }
     }
     }
     }
     if (assignment.getLeftHandSide().getClass() != org.eclipse.jdt.core.dom.SimpleName.class
     && assignment.getLeftHandSide().getClass() != org.eclipse.jdt.core.dom.FieldAccess.class
     && assignment.getLeftHandSide().getClass() != org.eclipse.jdt.core.dom.ArrayAccess.class
     && assignment.getLeftHandSide().getClass() != org.eclipse.jdt.core.dom.QualifiedName.class) {
     System.out.println("Assignment: class: " + javaClass.getFullQualifiedName() + "     " + assignment.getLeftHandSide() + "    class: " + assignment.getLeftHandSide().getClass());
     }
     if (changeInternalState) {
     javaMethod.setChangeInternalState(true);
     }
     }





     List<MethodInvocation> listmi = miv.getMethods();
     for (MethodInvocation methinv : listmi) {

     //System.out.println("METHOD INVOCATION: " + methinv.toString());
     //System.out.println("MI EXPRESSSION: " + methinv.getExpression());
     if (methinv.getExpression() != null && !methinv.getExpression().toString().equals("this")) {
     JavaData returnClassType = getClassReturnType(methinv.getExpression(), javaClass, javaMethod, blockVariablesBox);
     //                    if(javaClass.getFullQualifiedName().equals("org.gjt.sp.util.IOUtilities")){
     //                        System.out.println("method: "+javaMethod.getName()+" expression: "+methinv.getExpression()+"    return type: "+(returnClassType == null? "nulo" : returnClassType.getName()));
     //                    }
     if (returnClassType != null && (returnClassType.getClass() == JavaClass.class || returnClassType.getClass() == JavaInterface.class)) {
     JavaMethod javaMethodCall = null;
     JavaAbstract javaAbstract = (JavaAbstract) returnClassType;
     List<JavaData> arguments = new ArrayList();

     for (int i = 0; i < methinv.arguments().size(); i++) {
     Expression argument = (Expression) methinv.arguments().get(i);
     JavaData javaData = getClassReturnType(argument, javaClass, javaMethod, blockVariablesBox);
     //                            if(javaClass.getFullQualifiedName().equals("org.gjt.sp.util.IOUtilities")){
     //                                System.out.println("javadataClass: "+(argument.getClass())+"   argument: "+argument);
     //                                System.out.println("javaData: "+(javaData ==null ? "nulo" : javaData.getName()));
     //                            }
     arguments.add(javaData);
     }



     if (returnClassType.getClass() == JavaClass.class) {
     javaMethodCall = ((JavaClass) returnClassType).getJavaMethod(methinv.getName().toString(), methinv.arguments().size(), arguments);

     } else if (returnClassType.getClass() == JavaInterface.class) {
     javaMethodCall = ((JavaInterface) returnClassType).getJavaMethod(methinv.getName().toString(), methinv.arguments().size(), arguments);
     }



     JavaMethodInvocation javaMethoInvocation = new JavaMethodInvocation(javaAbstract, javaMethodCall);
     if (javaMethodCall == null) {
     javaMethoInvocation.setUnknowMethodName(methinv.getName().toString() + "()");
     //System.out.println("Java METHOD call : " + javaAbstract.getFullQualifiedName() + "   " + methinv.getName().toString() + "   _ " + methinv.arguments().size());
     }
     javaMethod.addMethodInvocation(javaMethoInvocation);

     //System.out.println("AQUI PASSOU");

     }
     } else {
     JavaMethod javaMethodCall = null;
     List<JavaData> arguments = new ArrayList();

     for (int i = 0; i < methinv.arguments().size(); i++) {
     Expression argument = (Expression) methinv.arguments().get(i);
     JavaData javaData = getClassReturnType(argument, javaClass, javaMethod, blockVariablesBox);
     arguments.add(javaData);
     }


     javaMethodCall = javaClass.getJavaMethod(methinv.getName().toString(), arguments.size(), arguments);
     if (javaMethodCall == null) {
     javaMethod.addInternalMethodInvocation(javaMethod);
     }

     }
     }
     }

     }


     } catch (Exception e) {
     System.out.println("Erro getMethodInvocations " + javaClass.getFullQualifiedName() + " Name: " + e.getMessage());
     e.printStackTrace();
     }
     }*/
    public void getAccessDataMetrics(JavaClass javaClass, JavaProject javaProject) {
        try {

            HashMap<String, String> accessToForeignDataMap = new HashMap();

            HashMap<String, HashMap<String, String>> accessedAttributes = new HashMap();

            //setando os hashs
            for (JavaMethod javaMethod : javaClass.getMethods()) {
                HashMap<String, String> hash = new HashMap();
                accessedAttributes.put(javaMethod.getMethodSignature(), hash);
            }

            int j = 0;
            for (JavaMethod javaMethod : javaClass.getMethods()) {
                //System.gc();
                //System.out.println("Classe: "+javaClass.getFullQualifiedName()+":"+javaMethod.getMethodSignature());
                //hashmap para calcular as tres métricas
                HashMap<String, String> accessForeignDataMap = new HashMap();
                HashMap<String, String> accessLocalDataMap = new HashMap();
                HashMap<String, String> foreignDataProviderMap = new HashMap();
                Block block = javaMethod.getBlock();

                j++;

                if (block != null) {
                    //System.out.println("Classe: "+javaClass.getFullQualifiedName());
                    //javaMethod.setSizeInChars(block.getLength());
                    BlockVariablesBox blockVariablesBox = getVariableDeclarations(block, javaClass, javaProject);
                    MethodInvocationVisitor miv = new MethodInvocationVisitor();
                    block.accept(miv);

                    //setando o numero de declaracoes de variaveis locais
                    int numberOfLocalVariables = 0;
                    List<VariableDeclarationExpression> variables = miv.getDeclarations();
                    for (VariableDeclarationExpression var : variables) {
                        numberOfLocalVariables = numberOfLocalVariables + var.fragments().size();
                    }
                    List<VariableDeclarationStatement> variableDeclarations = miv.getVariableDeclarations();
                    for (VariableDeclarationStatement var : variableDeclarations) {
                        numberOfLocalVariables = numberOfLocalVariables + var.fragments().size();
                    }
                    List<SingleVariableDeclaration> singleVariables = miv.getSingleDeclarations();
                    numberOfLocalVariables = numberOfLocalVariables + singleVariables.size();
                    javaMethod.setNumberOfLocalVariables(numberOfLocalVariables);

                    //System.out.println("Variaveis locais:  j = "+j);
                    //verificar todos os simple names entretanto apenas aqueles que não são filhos de um field access externo nem uma declaração de variavel
                    // tambem vai criar os JavaExternalAttributeAccess,, ou seja verá quais atributos de classes externas esse método acessa
                    List<SimpleName> simpleNames = miv.getSimpleNames();

                    for (SimpleName simpleName : simpleNames) {
                        if (simpleName.getParent().getClass() == org.eclipse.jdt.core.dom.FieldAccess.class) {
                            if (((FieldAccess) simpleName.getParent()).getExpression().toString().equals("this")
                                    || ((FieldAccess) simpleName.getParent()).getExpression().toString().equals(javaClass.getFullQualifiedName())
                                    || ((FieldAccess) simpleName.getParent()).getExpression().toString().equals(javaClass.getName())) {
                                //boolean que indica se deve-se continuar procurando o atributo nas classes pai
                                boolean continua = true;
                                for (JavaAttribute javaAttribute : javaClass.getAttributes()) {
                                    if (javaAttribute.getName().equals(simpleName.toString())) {
                                        accessLocalDataMap.put(simpleName.toString(), simpleName.toString());

                                        HashMap<String, String> hash = accessedAttributes.get(javaMethod.getMethodSignature());
                                        hash.put(simpleName.toString(), simpleName.toString());

                                        continua = false;
                                        break;

                                    }
                                }
                                //verifica nas classes pai
                                JavaClass javaAuxClass = javaClass.getSuperClass();
                                while (continua && javaAuxClass != null) {
                                    for (JavaAttribute javaAttribute : javaAuxClass.getAttributes()) {
                                        if (javaAttribute.getName().equals(simpleName.toString())) {
                                            accessLocalDataMap.put(simpleName.toString(), simpleName.toString());

                                            HashMap<String, String> hash = accessedAttributes.get(javaMethod.getMethodSignature());
                                            hash.put(simpleName.toString(), simpleName.toString());

                                            continua = false;
                                            break;

                                        }
                                    }
                                    javaAuxClass = javaAuxClass.getSuperClass();
                                }
                            } else {
                                JavaData returnClassType = getClassReturnType(((FieldAccess) simpleName.getParent()).getExpression(), javaClass, javaMethod, blockVariablesBox);
                                if (returnClassType != null && (returnClassType.getClass() == JavaClass.class || returnClassType.getClass() == JavaInterface.class)) {
                                    JavaAbstract javaAbstract = (JavaAbstract) returnClassType;

                                    if (javaAbstract.getClass() == JavaClass.class && javaClass.isInheritedClass((JavaClass) javaAbstract)) {
                                        accessLocalDataMap.put(simpleName.toString(), simpleName.toString());

                                        HashMap<String, String> hash = accessedAttributes.get(javaMethod.getMethodSignature());
                                        hash.put(simpleName.toString(), simpleName.toString());

                                    } else {

                                        accessForeignDataMap.put(javaAbstract.getFullQualifiedName() + "." + simpleName.toString(), javaAbstract.getFullQualifiedName() + "." + simpleName.toString());
                                        foreignDataProviderMap.put(javaAbstract.getFullQualifiedName(), javaAbstract.getFullQualifiedName());
                                        accessToForeignDataMap.put(javaAbstract.getFullQualifiedName(), javaAbstract.getFullQualifiedName());

                                        JavaExternalAttributeAccess javaExternalAttributeAccess = new JavaExternalAttributeAccess(javaAbstract, simpleName.toString());
                                        javaMethod.addExternalAttributeAccess(javaExternalAttributeAccess);

                                    }
                                }
                            }
                        } else if (!(simpleName.getParent().getClass() == org.eclipse.jdt.core.dom.VariableDeclaration.class)) {
                            if (blockVariablesBox.getJavaDataByVariableName(simpleName.toString(), simpleName.getParent()) == null) {
                                //boolean que indica se deve-se continuar procurando o atributo nas classes pai
                                boolean continua = true;
                                for (JavaAttribute javaAttribute : javaClass.getAttributes()) {
                                    if (javaAttribute.getName().equals(simpleName.toString())) {
                                        accessLocalDataMap.put(simpleName.toString(), simpleName.toString());

                                        HashMap<String, String> hash = accessedAttributes.get(javaMethod.getMethodSignature());
                                        hash.put(simpleName.toString(), simpleName.toString());

                                        continua = false;
                                        break;

                                    }
                                }
                                //verifica nas classes pai
                                JavaClass javaAuxClass = javaClass.getSuperClass();
                                while (continua && javaAuxClass != null) {
                                    for (JavaAttribute javaAttribute : javaAuxClass.getAttributes()) {
                                        if (javaAttribute.getName().equals(simpleName.toString())) {
                                            accessLocalDataMap.put(simpleName.toString(), simpleName.toString());

                                            HashMap<String, String> hash = accessedAttributes.get(javaMethod.getMethodSignature());
                                            hash.put(simpleName.toString(), simpleName.toString());

                                            continua = false;
                                            break;

                                        }
                                    }
                                    javaAuxClass = javaAuxClass.getSuperClass();
                                }

                            }
                        }
                    }

                    //System.out.println("simple names:  j = "+j);
                    //vendo os métodos que são chamados e se eles são 
                    List<MethodInvocation> listmi = miv.getMethods();
                    for (MethodInvocation methinv : listmi) {
                        //System.gc();

                        //System.out.println("METHOD INVOCATION: " + methinv.toString());
                        //System.out.println("MI EXPRESSSION: " + methinv.getExpression());
                        if (methinv.getExpression() != null && !methinv.getExpression().toString().equals("this")) {
                            JavaData returnClassType = getClassReturnType(methinv.getExpression(), javaClass, javaMethod, blockVariablesBox);
//                    if(javaClass.getFullQualifiedName().equals("org.gjt.sp.util.IOUtilities")){
//                        System.out.println("method: "+javaMethod.getName()+" expression: "+methinv.getExpression()+"    return type: "+(returnClassType == null? "nulo" : returnClassType.getName()));
//                    }
                            if (returnClassType != null && (returnClassType.getClass() == JavaClass.class || returnClassType.getClass() == JavaInterface.class)) {
                                JavaMethod javaMethodCall = null;
                                JavaAbstract javaAbstract = (JavaAbstract) returnClassType;
                                List<JavaData> arguments = new ArrayList();

                                for (int i = 0; i < methinv.arguments().size(); i++) {
                                    Expression argument = (Expression) methinv.arguments().get(i);
                                    JavaData javaData = getClassReturnType(argument, javaClass, javaMethod, blockVariablesBox);
//                            if(javaClass.getFullQualifiedName().equals("org.gjt.sp.util.IOUtilities")){
//                                System.out.println("javadataClass: "+(argument.getClass())+"   argument: "+argument);
//                                System.out.println("javaData: "+(javaData ==null ? "nulo" : javaData.getName()));
//                            }
                                    arguments.add(javaData);
                                }

                                if (returnClassType.getClass() == JavaClass.class) {
                                    javaMethodCall = ((JavaClass) returnClassType).getJavaMethod(methinv.getName().toString(), methinv.arguments().size(), arguments);

                                } else if (returnClassType.getClass() == JavaInterface.class) {
                                    javaMethodCall = ((JavaInterface) returnClassType).getJavaMethod(methinv.getName().toString(), methinv.arguments().size(), arguments);
                                }

                                if (javaMethodCall != null) {

                                    if (javaMethodCall.isAnAcessorMethod()) {
                                        accessForeignDataMap.put(javaAbstract.getFullQualifiedName() + "." + javaMethodCall.getAccessedAttribute(), javaAbstract.getFullQualifiedName() + "." + javaMethodCall.getAccessedAttribute());
                                        foreignDataProviderMap.put(javaAbstract.getFullQualifiedName(), javaAbstract.getFullQualifiedName());
                                        accessToForeignDataMap.put(javaAbstract.getFullQualifiedName(), javaAbstract.getFullQualifiedName());
                                    }
                                }

                                //javaMethod.addMethodInvocation(javaMethoInvocation);
                                //System.out.println("AQUI PASSOU");
                            }
                        } else {
                            JavaMethod javaMethodCall = null;
                            List<JavaData> arguments = new ArrayList();

                            for (int i = 0; i < methinv.arguments().size(); i++) {
                                Expression argument = (Expression) methinv.arguments().get(i);
                                JavaData javaData = getClassReturnType(argument, javaClass, javaMethod, blockVariablesBox);
                                arguments.add(javaData);
                            }

                            javaMethodCall = javaClass.getJavaMethod(methinv.getName().toString(), arguments.size(), arguments);
                            if (javaMethodCall != null) {
                                if (javaMethodCall.isAnAcessorMethod()) {
                                    accessLocalDataMap.put(javaMethodCall.getAccessedAttribute(), javaMethodCall.getAccessedAttribute());
                                }
                                //javaMethod.addInternalMethodInvocation(javaMethod);
                            }

                        }
                    }
                    //System.out.println("method invocations:  j = "+j);
                }

                javaMethod.setAccessToForeignDataNumber(accessForeignDataMap.size());
                javaMethod.setAccessToLocalDataNumber(accessLocalDataMap.size());
                javaMethod.setForeignDataProviderNumber(foreignDataProviderMap.size());

            }

            //System.out.println("Calcular NumberOfDirectConnections");
            //System.out.println("ver direct connections");
            setNumberOfDirectConnections(javaClass, javaProject, accessedAttributes);
            //System.out.println("viu direct connections");

            javaClass.setAccessToForeignDataNumber(accessToForeignDataMap.size());

        } catch (Exception e) {
            System.out.println("Erro getAccessDataMetrics: " + javaClass.getFullQualifiedName() + " Name: " + e.getMessage());
        }
    }

    public void getAccessDataMetricsOffMemory(JavaClass javaClass, JavaProject javaProject) {
        try {

            HashMap<String, String> accessToForeignDataMap = new HashMap();

            HashMap<String, HashMap<String, String>> accessedAttributes = new HashMap();

            //setando os hashs
            for (JavaMethod javaMethod : javaClass.getMethods()) {
                HashMap<String, String> hash = new HashMap();
                accessedAttributes.put(javaMethod.getMethodSignature(), hash);
            }

            for (JavaMethod javaMethod : javaClass.getMethods()) {
                System.gc();
                //System.out.println("Classe: "+javaClass.getFullQualifiedName()+":"+javaMethod.getMethodSignature());
                //hashmap para calcular as tres métricas
                HashMap<String, String> accessForeignDataMap = new HashMap();
                HashMap<String, String> accessLocalDataMap = new HashMap();
                HashMap<String, String> foreignDataProviderMap = new HashMap();
                Block block = javaMethod.getBlock();

                if (block != null) {
                    //System.out.println("Classe: "+javaClass.getFullQualifiedName());
                    //javaMethod.setSizeInChars(block.getLength());
                    BlockVariablesBox blockVariablesBox = getVariableDeclarations(block, javaClass, javaProject);
                    MethodInvocationVisitor miv = new MethodInvocationVisitor();
                    block.accept(miv);

                    //setando o numero de declaracoes de variaveis locais
                    int numberOfLocalVariables = 0;
                    List<VariableDeclarationExpression> variables = miv.getDeclarations();
                    for (VariableDeclarationExpression var : variables) {
                        numberOfLocalVariables = numberOfLocalVariables + var.fragments().size();
                    }
                    List<VariableDeclarationStatement> variableDeclarations = miv.getVariableDeclarations();
                    for (VariableDeclarationStatement var : variableDeclarations) {
                        numberOfLocalVariables = numberOfLocalVariables + var.fragments().size();
                    }
                    List<SingleVariableDeclaration> singleVariables = miv.getSingleDeclarations();
                    numberOfLocalVariables = numberOfLocalVariables + singleVariables.size();
                    javaMethod.setNumberOfLocalVariables(numberOfLocalVariables);

                    //verificar todos os simple names entretanto apenas aqueles que não são filhos de um field access externo nem uma declaração de variavel
                    // tambem vai criar os JavaExternalAttributeAccess,, ou seja verá quais atributos de classes externas esse método acessa
                    List<SimpleName> simpleNames = miv.getSimpleNames();

                    for (SimpleName simpleName : simpleNames) {
                        if (simpleName.getParent().getClass() == org.eclipse.jdt.core.dom.FieldAccess.class) {
                            if (((FieldAccess) simpleName.getParent()).getExpression().toString().equals("this")
                                    || ((FieldAccess) simpleName.getParent()).getExpression().toString().equals(javaClass.getFullQualifiedName())
                                    || ((FieldAccess) simpleName.getParent()).getExpression().toString().equals(javaClass.getName())) {
                                //boolean que indica se deve-se continuar procurando o atributo nas classes pai
                                boolean continua = true;
                                for (JavaAttribute javaAttribute : javaClass.getAttributes()) {
                                    if (javaAttribute.getName().equals(simpleName.toString())) {
                                        accessLocalDataMap.put(simpleName.toString(), simpleName.toString());

                                        HashMap<String, String> hash = accessedAttributes.get(javaMethod.getMethodSignature());
                                        hash.put(simpleName.toString(), simpleName.toString());

                                        continua = false;
                                        break;

                                    }
                                }
                                //verifica nas classes pai
                                JavaClass javaAuxClass = javaClass.getSuperClass();
                                while (continua && javaAuxClass != null) {
                                    for (JavaAttribute javaAttribute : javaAuxClass.getAttributes()) {
                                        if (javaAttribute.getName().equals(simpleName.toString())) {
                                            accessLocalDataMap.put(simpleName.toString(), simpleName.toString());

                                            HashMap<String, String> hash = accessedAttributes.get(javaMethod.getMethodSignature());
                                            hash.put(simpleName.toString(), simpleName.toString());

                                            continua = false;
                                            break;

                                        }
                                    }
                                    javaAuxClass = javaAuxClass.getSuperClass();
                                }
                            } else {
                                JavaData returnClassType = getClassReturnType(((FieldAccess) simpleName.getParent()).getExpression(), javaClass, javaMethod, blockVariablesBox);
                                if (returnClassType != null && (returnClassType.getClass() == JavaClass.class || returnClassType.getClass() == JavaInterface.class)) {
                                    JavaAbstract javaAbstract = (JavaAbstract) returnClassType;

                                    if (javaAbstract.getClass() == JavaClass.class && javaClass.isInheritedClass((JavaClass) javaAbstract)) {
                                        accessLocalDataMap.put(simpleName.toString(), simpleName.toString());

                                        HashMap<String, String> hash = accessedAttributes.get(javaMethod.getMethodSignature());
                                        hash.put(simpleName.toString(), simpleName.toString());

                                    } else {

                                        accessForeignDataMap.put(javaAbstract.getFullQualifiedName() + "." + simpleName.toString(), javaAbstract.getFullQualifiedName() + "." + simpleName.toString());
                                        foreignDataProviderMap.put(javaAbstract.getFullQualifiedName(), javaAbstract.getFullQualifiedName());
                                        accessToForeignDataMap.put(javaAbstract.getFullQualifiedName(), javaAbstract.getFullQualifiedName());

                                        JavaExternalAttributeAccess javaExternalAttributeAccess = new JavaExternalAttributeAccess(javaAbstract, simpleName.toString());
                                        javaMethod.addExternalAttributeAccess(javaExternalAttributeAccess);

                                    }
                                }
                            }
                        } else if (!(simpleName.getParent().getClass() == org.eclipse.jdt.core.dom.VariableDeclaration.class)) {
                            if (blockVariablesBox.getJavaDataByVariableName(simpleName.toString(), simpleName.getParent()) == null) {
                                //boolean que indica se deve-se continuar procurando o atributo nas classes pai
                                boolean continua = true;
                                for (JavaAttribute javaAttribute : javaClass.getAttributes()) {
                                    if (javaAttribute.getName().equals(simpleName.toString())) {
                                        accessLocalDataMap.put(simpleName.toString(), simpleName.toString());

                                        HashMap<String, String> hash = accessedAttributes.get(javaMethod.getMethodSignature());
                                        hash.put(simpleName.toString(), simpleName.toString());

                                        continua = false;
                                        break;

                                    }
                                }
                                //verifica nas classes pai
                                JavaClass javaAuxClass = javaClass.getSuperClass();
                                while (continua && javaAuxClass != null) {
                                    for (JavaAttribute javaAttribute : javaAuxClass.getAttributes()) {
                                        if (javaAttribute.getName().equals(simpleName.toString())) {
                                            accessLocalDataMap.put(simpleName.toString(), simpleName.toString());

                                            HashMap<String, String> hash = accessedAttributes.get(javaMethod.getMethodSignature());
                                            hash.put(simpleName.toString(), simpleName.toString());

                                            continua = false;
                                            break;

                                        }
                                    }
                                    javaAuxClass = javaAuxClass.getSuperClass();
                                }

                            }
                        }
                    }

                    //vendo os métodos que são chamados e se eles são 
                    List<MethodInvocation> listmi = miv.getMethods();
                    for (MethodInvocation methinv : listmi) {
                        System.gc();

                        //System.out.println("METHOD INVOCATION: " + methinv.toString());
                        //System.out.println("MI EXPRESSSION: " + methinv.getExpression());
                        if (methinv.getExpression() != null && !methinv.getExpression().toString().equals("this")) {
                            JavaData returnClassType = getClassReturnType(methinv.getExpression(), javaClass, javaMethod, blockVariablesBox);
//                    if(javaClass.getFullQualifiedName().equals("org.gjt.sp.util.IOUtilities")){
//                        System.out.println("method: "+javaMethod.getName()+" expression: "+methinv.getExpression()+"    return type: "+(returnClassType == null? "nulo" : returnClassType.getName()));
//                    }
                            if (returnClassType != null && (returnClassType.getClass() == JavaClass.class || returnClassType.getClass() == JavaInterface.class)) {
                                JavaMethod javaMethodCall = null;
                                JavaAbstract javaAbstract = (JavaAbstract) returnClassType;
                                List<JavaData> arguments = new ArrayList();

                                for (int i = 0; i < methinv.arguments().size(); i++) {
                                    Expression argument = (Expression) methinv.arguments().get(i);
                                    JavaData javaData = getClassReturnType(argument, javaClass, javaMethod, blockVariablesBox);
//                            if(javaClass.getFullQualifiedName().equals("org.gjt.sp.util.IOUtilities")){
//                                System.out.println("javadataClass: "+(argument.getClass())+"   argument: "+argument);
//                                System.out.println("javaData: "+(javaData ==null ? "nulo" : javaData.getName()));
//                            }
                                    arguments.add(javaData);
                                }

                                if (returnClassType.getClass() == JavaClass.class) {
                                    javaMethodCall = ((JavaClass) returnClassType).getJavaMethod(methinv.getName().toString(), methinv.arguments().size(), arguments);

                                } else if (returnClassType.getClass() == JavaInterface.class) {
                                    javaMethodCall = ((JavaInterface) returnClassType).getJavaMethod(methinv.getName().toString(), methinv.arguments().size(), arguments);
                                }

                                if (javaMethodCall != null) {

                                    if (javaMethodCall.isAnAcessorMethod()) {
                                        accessForeignDataMap.put(javaAbstract.getFullQualifiedName() + "." + javaMethodCall.getAccessedAttribute(), javaAbstract.getFullQualifiedName() + "." + javaMethodCall.getAccessedAttribute());
                                        foreignDataProviderMap.put(javaAbstract.getFullQualifiedName(), javaAbstract.getFullQualifiedName());
                                        accessToForeignDataMap.put(javaAbstract.getFullQualifiedName(), javaAbstract.getFullQualifiedName());
                                    }
                                }

                                //javaMethod.addMethodInvocation(javaMethoInvocation);
                                //System.out.println("AQUI PASSOU");
                            }
                        } else {
                            JavaMethod javaMethodCall = null;
                            List<JavaData> arguments = new ArrayList();

                            for (int i = 0; i < methinv.arguments().size(); i++) {
                                Expression argument = (Expression) methinv.arguments().get(i);
                                JavaData javaData = getClassReturnType(argument, javaClass, javaMethod, blockVariablesBox);
                                arguments.add(javaData);
                            }

                            javaMethodCall = javaClass.getJavaMethod(methinv.getName().toString(), arguments.size(), arguments);
                            if (javaMethodCall != null) {
                                if (javaMethodCall.isAnAcessorMethod()) {
                                    accessLocalDataMap.put(javaMethodCall.getAccessedAttribute(), javaMethodCall.getAccessedAttribute());
                                }
                                //javaMethod.addInternalMethodInvocation(javaMethod);
                            }

                        }
                    }
                }

                javaMethod.setAccessToForeignDataNumber(accessForeignDataMap.size());
                javaMethod.setAccessToLocalDataNumber(accessLocalDataMap.size());
                javaMethod.setForeignDataProviderNumber(foreignDataProviderMap.size());

            }

            //System.out.println("Calcular NumberOfDirectConnections");
            setNumberOfDirectConnections(javaClass, javaProject, accessedAttributes);

            javaClass.setAccessToForeignDataNumber(accessToForeignDataMap.size());

        } catch (Exception e) {
            System.out.println("Erro getAccessDataMetrics: " + javaClass.getFullQualifiedName() + " Name: " + e.getMessage());
        }
    }

    private void setNumberOfDirectConnections(JavaClass javaClass, JavaProject javaProject, HashMap<String, HashMap<String, String>> accessedAttributes) {
        try {

            for (JavaAttribute javaAttribute : javaClass.getAttributes()) {

                //System.gc();
                List<JavaMethod> completeMethods = new ArrayList();
                List<JavaMethod> incompleteMethods = new ArrayList();
                //System.out.println("Metodos da classe: "+javaClass.getMethods().size());
                for (JavaMethod javaMethod : javaClass.getMethods()) {
                    HashMap<String, String> hash = accessedAttributes.get(javaMethod.getMethodSignature());
                    if (hash.get(javaAttribute.getName()) != null || javaMethod.getInternalMethodInvocations().isEmpty()) {
                        completeMethods.add(javaMethod);
                        if (!javaMethod.getInternalMethodInvocations().isEmpty() && hash.get(javaAttribute.getName()) != null) {
                            //System.out.println("Metodo "+javaMethod.getMethodSignature()+"  acessa: "+javaAttribute.getName());
                        } else {
                            //System.out.println("Metodo completo "+javaMethod.getMethodSignature());
                        }
                    } else {
                        //System.out.println("Metodo incompleto: "+javaMethod.getMethodSignature());
                        incompleteMethods.add(javaMethod);
                    }
                }
                boolean flag = true;
                while (flag) {
                    List<JavaMethod> listToRemove = new ArrayList();
                    for (JavaMethod javaMethod : incompleteMethods) {
                        int completeCalls = 0;
                        //System.out.println("Metodo "+javaMethod.getMethodSignature()+"   chama: ");
                        for (JavaMethodInvocation methodCall : javaMethod.getInternalMethodInvocations()) {
                            //System.out.println("chama: "+methodCall.getJavaMethod().getMethodSignature());
                            if (methodCall.getJavaMethod() != null && methodCall.getJavaMethod().getJavaAbstract() == javaClass) {
                                if (completeMethods.contains(methodCall.getJavaMethod())) {
                                    completeCalls++;
                                    HashMap<String, String> hash = accessedAttributes.get(methodCall.getJavaMethod().getMethodSignature());
                                    if (hash.get(javaAttribute.getName()) != null) {
                                        HashMap<String, String> hash2 = accessedAttributes.get(javaMethod.getMethodSignature());
                                        hash2.put(javaAttribute.getName(), javaAttribute.getName());
                                    }
                                }
                            } else {
                                completeCalls++;

                            }
                        }

                        if (completeCalls == javaMethod.getInternalMethodInvocations().size()) {
                            listToRemove.add(javaMethod);
                            //System.out.println("Vai remover: "+javaMethod.getMethodSignature()+"   virou completo");
                            completeMethods.add(javaMethod);
                        }
                    }
                    if (listToRemove.isEmpty()) {
                        flag = false;
                    }
                    //System.out.println("Imcomplete methods: "+incompleteMethods.size());
                    //System.out.println("A remover: "+listToRemove.size());
                    for (JavaMethod javaMethod : listToRemove) {
                        incompleteMethods.remove(javaMethod);
                    }

                    //System.out.println("Imcomplete methods final: "+incompleteMethods.size());
                }
            }

            int numberOfDirectConnections = 0;
            for (int i = 0; i < javaClass.getMethods().size(); i++) {
                JavaMethod jm1 = javaClass.getMethods().get(i);
                for (int j = i + 1; j < javaClass.getMethods().size(); j++) {
                    JavaMethod jm2 = javaClass.getMethods().get(j);
                    HashMap<String, String> hash1 = accessedAttributes.get(jm1.getMethodSignature());
                    HashMap<String, String> hash2 = accessedAttributes.get(jm2.getMethodSignature());
                    for (String attributeName : hash1.keySet()) {
                        if (hash2.get(attributeName) != null) {
                            numberOfDirectConnections++;
                            break;
                        }
                    }
                    //System.gc();
                }
            }

            javaClass.setNumberOfDirectConnections(numberOfDirectConnections);

        } catch (Exception e) {
            System.out.println("Erro setAttributeAccessMethods " + e.getMessage());
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
                if (javaAbstract == null) {

                    return new JavaAbstractExternal(classReturnType);

                } else {
                    return javaAbstract;
                }

            } else if (expression.getClass() == org.eclipse.jdt.core.dom.SimpleName.class) {
                //JavaData classReturnType = hashMethodDeclarations.get(((SimpleName) expression).toString());
                JavaData classReturnType = blockVariablesBox.getJavaDataByVariableName(((SimpleName) expression).toString(), expression.getParent());
                if (classReturnType == null) {
                    for (Parameter parameter : javaMethod.getParameters()) {
                        //System.gc();
                        if (parameter.getName().equals(((SimpleName) expression).toString())) {
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
            } else if (expression.getClass() == org.eclipse.jdt.core.dom.NullLiteral.class) {
                return new JavaNull();
            } else if (expression.getClass() == org.eclipse.jdt.core.dom.NumberLiteral.class) {
                if (expression.toString().contains(".")) {
                    return new JavaPrimitiveType(JavaPrimitiveType.getType("double"));
                } else if (expression.toString().endsWith("l")) {
                    return new JavaPrimitiveType(JavaPrimitiveType.getType("long"));
                } else if (expression.toString().endsWith("f")) {
                    return new JavaPrimitiveType(JavaPrimitiveType.getType("float"));
                } else {
                    return new JavaPrimitiveType(JavaPrimitiveType.getType("int"));
                }
            } else if (expression.getClass() == org.eclipse.jdt.core.dom.ArrayAccess.class) {
                return getClassReturnType(((ArrayAccess) expression).getArray(), javaClass, javaMethod, blockVariablesBox);
            } else if (expression.getClass() == org.eclipse.jdt.core.dom.ArrayCreation.class) {
                String classReturnType = ((ArrayCreation) expression).getType().toString();
                if (JavaPrimitiveType.getType(classReturnType) != 0) {
                    return new JavaPrimitiveType(JavaPrimitiveType.getType(classReturnType));
                }
                JavaData javaAbstract = javaClass.getJavaAbstractImportByName(classReturnType);
                return javaAbstract;
            } else if (expression.getClass() == org.eclipse.jdt.core.dom.BooleanLiteral.class) {
                return new JavaPrimitiveType(JavaPrimitiveType.getType("boolean"));
            } else if (expression.getClass() == org.eclipse.jdt.core.dom.CharacterLiteral.class) {
                return new JavaPrimitiveType(JavaPrimitiveType.getType("char"));
            } else if (expression.getClass() == org.eclipse.jdt.core.dom.ClassInstanceCreation.class) {
                String classReturnType = ((ClassInstanceCreation) expression).getType().toString();
                JavaData javaAbstract = javaClass.getJavaAbstractImportByName(classReturnType);
                return javaAbstract;
            } else if (expression.getClass() == org.eclipse.jdt.core.dom.TypeLiteral.class) {
                String classReturnType = ((TypeLiteral) expression).getType().toString();
                JavaData javaAbstract = javaClass.getJavaAbstractImportByName(classReturnType);
                return javaAbstract;
            } else if (expression.getClass() == org.eclipse.jdt.core.dom.StringLiteral.class) {
                //System.out.println("String literal");
                JavaAbstractExternal javaAbstractExternal = new JavaAbstractExternal("String");
                //System.out.println("String literal: "+javaAbstractExternal.getName());
                return javaAbstractExternal;
            } else if (expression.getClass() == org.eclipse.jdt.core.dom.InfixExpression.class) {
                //System.out.println("Left: "+((InfixExpression) expression).getLeftOperand().getClass());
                return getClassReturnType(((InfixExpression) expression).getLeftOperand(), javaClass, javaMethod, blockVariablesBox);
            }

        }
        return null;
    }

    public void setAttributeModificationMethod(JavaClass javaClass, JavaProject javaProject) {
        try {

            List<JavaMethod> completeMethods = new ArrayList();
            List<JavaMethod> incompleteMethods = new ArrayList();

            for (JavaMethod javaMethod : javaClass.getMethods()) {
                if (javaMethod.isChangeInternalState() || javaMethod.getInternalMethodInvocations().isEmpty()) {
                    completeMethods.add(javaMethod);
                } else {
                    incompleteMethods.add(javaMethod);
                }
            }
            boolean flag = true;
            while (flag) {
                List<JavaMethod> listToRemove = new ArrayList();
                for (JavaMethod javaMethod : incompleteMethods) {
                    int completeCalls = 0;
                    boolean change = false;
                    for (JavaMethodInvocation methodCall : javaMethod.getInternalMethodInvocations()) {
                        if (methodCall.getJavaMethod() != null && methodCall.getJavaMethod().getJavaAbstract() == javaClass) {
                            if (completeMethods.contains(methodCall.getJavaMethod())) {
                                completeCalls++;
                                if (methodCall.getJavaMethod().isChangeInternalState() || methodCall.getJavaMethod().isChangeInternalStateByMethodInvocations()) {
                                    change = true;
                                    //System.out.println("Chnage " + javaClass.getFullQualifiedName() + "   method: " + javaMethod.getMethodSignature());
                                    break;
                                }
                            }
                        } else {
                            completeCalls++;
                        }
                    }

                    if (completeCalls == javaMethod.getInternalMethodInvocations().size() || change) {
                        listToRemove.add(javaMethod);
                        completeMethods.add(javaMethod);
                        javaMethod.setChangeInternalStateByMethodInvocations(change);
                    }
                }
                if (listToRemove.isEmpty()) {
                    flag = false;
                }
                for (JavaMethod javaMethod : listToRemove) {
                    incompleteMethods.remove(javaMethod);
                }
            }

        } catch (Exception e) {
            System.out.println("Erro setAttributeModificationMethod " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setAttributeModificationMethodOffMemory(JavaClass javaClass, JavaProject javaProject) {
        try {

            List<JavaMethod> completeMethods = new ArrayList();
            List<JavaMethod> incompleteMethods = new ArrayList();

            for (JavaMethod javaMethod : javaClass.getMethods()) {
                if (javaMethod.isChangeInternalState() || javaMethod.getInternalMethodInvocations().isEmpty()) {
                    completeMethods.add(javaMethod);
                } else {
                    incompleteMethods.add(javaMethod);
                }
            }
            boolean flag = true;
            while (flag) {
                List<JavaMethod> listToRemove = new ArrayList();
                for (JavaMethod javaMethod : incompleteMethods) {
                    int completeCalls = 0;
                    boolean change = false;
                    for (JavaMethodInvocation methodCall : javaMethod.getInternalMethodInvocations()) {
                        if (methodCall.getJavaMethod() != null && methodCall.getJavaMethod().getJavaAbstract() == javaClass) {
                            if (completeMethods.contains(methodCall.getJavaMethod())) {
                                completeCalls++;
                                if (methodCall.getJavaMethod().isChangeInternalState() || methodCall.getJavaMethod().isChangeInternalStateByMethodInvocations()) {
                                    change = true;
                                    //System.out.println("Chnage " + javaClass.getFullQualifiedName() + "   method: " + javaMethod.getMethodSignature());
                                    break;
                                }
                            }
                        } else {
                            completeCalls++;
                        }
                    }

                    if (completeCalls == javaMethod.getInternalMethodInvocations().size() || change) {
                        listToRemove.add(javaMethod);
                        completeMethods.add(javaMethod);
                        javaMethod.setChangeInternalStateByMethodInvocations(change);
                    }
                }
                if (listToRemove.isEmpty()) {
                    flag = false;
                }
                for (JavaMethod javaMethod : listToRemove) {
                    incompleteMethods.remove(javaMethod);
                }
            }

        } catch (Exception e) {
            System.out.println("Erro setAttributeModificationMethod " + e.getMessage());
        }
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

                Variable variable = new Variable(frag.getName().toString(), javaData);
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

                Variable variable = new Variable(frag.getName().toString(), javaData);
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

            Variable variable = new Variable(var.getName().toString(), javaData);
            blockVariablesBox.addVariable(variable, var.getParent());

        }

        return blockVariablesBox;
    }

    private String removeTryWithResources(String content) {
        String newContent = null;
        char[] contentChars = content.toCharArray();
        List<Character> chars = new LinkedList();
        int i = 0;
        while (i < contentChars.length) {
            if (contentChars[i] == 't') {
                if ((i + 3) < contentChars.length && i > 0 && (!Character.isAlphabetic(contentChars[i - 1]))) {

                    if (contentChars[i + 1] == 'r' && contentChars[i + 2] == 'y' && (contentChars[i + 3] == ' ' || contentChars[i + 3] == '{' || contentChars[i + 3] == '(')) {
                        chars.add(contentChars[i]);
                        chars.add(contentChars[i + 1]);
                        chars.add(contentChars[i + 2]);
                        int j = i + 3;
                        while (j < contentChars.length && contentChars[j] != '{') {
                            j++;
                        }
                        chars.add(contentChars[j]);
                        i = j + 1;

                    } else {
                        chars.add(contentChars[i]);
                        i++;
                    }
                } else {
                    chars.add(contentChars[i]);
                    i++;
                }
            } else {
                chars.add(contentChars[i]);
                i++;
            }
        }
        char[] tmp = new char[chars.size()];
        System.out.println("Size: "+chars.size());
        for (i = 0; i < tmp.length; i++) {
            tmp[i] = chars.get(i);
            if(i%100000 == 99999){
                System.out.println("I: "+(i+1));
            }
        }
        newContent = new String(tmp);
        return newContent;
    }

    private String removeComments(String content) {
        String newContent = null;
        char[] contentChars = content.toCharArray();
        List<Character> chars = new LinkedList();
        int i = 0;
        while (i < contentChars.length) {
            if (contentChars[i] == '/' && (i + 1) < contentChars.length) {
                if (contentChars[i + 1] == '/') {
                    int j = i + 2;
                    while (j < contentChars.length && contentChars[j] != '\n') {
                        j++;
                    }
                    i = j + 1;

                } else if (contentChars[i + 1] == '*') {
                    int j = i + 2;
                    while ((j + 1) < contentChars.length && !(contentChars[j] == '*' && contentChars[j + 1] == '/')) {
                        j++;
                    }
                    i = j + 2;
                } else {
                    chars.add(contentChars[i]);
                    i++;
                }
            } else {
                chars.add(contentChars[i]);
                i++;
            }
        }
        System.out.println("chars size: "+chars.size());
        char[] tmp = new char[chars.size()];
        for (i = 0; i < tmp.length; i++) {
            tmp[i] = chars.get(i);
            if(i%100000 == 99999){
                System.out.println("I: "+(i+1));
            }
        }
        newContent = new String(tmp);
        return newContent;
    }

    private String removeLiteralStrings(String content) {
        String newContent = null;
        char[] contentChars = content.toCharArray();
        List<Character> chars = new LinkedList();
        int i = 0;
        while (i < contentChars.length) {
            if (contentChars[i] == '"') {
                chars.add(contentChars[i]);
                int j = i + 1;
                boolean exit = false;
                while (j < contentChars.length && !exit) {
                    if (contentChars[j] == '"' && contentChars[j - 1] != '\\') {
                        exit = true;
                    } else {
                        j++;
                    }
                }
                if (j < contentChars.length) {
                    chars.add(contentChars[j]);
                }
                i = j + 1;
            } else {
                chars.add(contentChars[i]);
                i++;
            }
        }
        char[] tmp = new char[chars.size()];
        System.out.println("Size: "+chars.size());
        for (i = 0; i < tmp.length; i++) {
            tmp[i] = chars.get(i);
            if(i%100000 == 99999){
                System.out.println("I: "+(i+1));
            }
        }
        newContent = new String(tmp);
        return newContent;
    }

    private String removeMultipleCatch(String content) {
        String newContent = null;
        char[] contentChars = content.toCharArray();
        List<Character> chars = new LinkedList();
        int i = 0;
        while (i < contentChars.length) {
            if ((i + 5) < contentChars.length && i > 0) {
                if (contentChars[i] == 'c' && contentChars[i + 1] == 'a' && contentChars[i + 2] == 't' && contentChars[i + 3] == 'c'
                        && contentChars[i + 4] == 'h' && (!Character.isAlphabetic(contentChars[i - 1])) && (!Character.isAlphabetic(contentChars[i + 5]))) {
                    chars.add(contentChars[i]);
                    chars.add(contentChars[i + 1]);
                    chars.add(contentChars[i + 2]);
                    chars.add(contentChars[i + 3]);
                    chars.add(contentChars[i + 4]);
                    int j = i + 5;

                    while (j < contentChars.length && contentChars[j] != '(') {
                        j++;
                    }
                    if (j < contentChars.length) {
                        chars.add(contentChars[j]);
                        while (j < contentChars.length && contentChars[j] != ')') {
                            j++;
                        }
                        if (j < contentChars.length) {
                            chars.add('E');
                            chars.add('x');
                            chars.add('c');
                            chars.add('e');
                            chars.add('p');
                            chars.add('t');
                            chars.add('i');
                            chars.add('o');
                            chars.add('n');
                            chars.add(' ');
                            chars.add('e');
                            chars.add(contentChars[j]);
                        }
                    }
                    i = j + 1;
                } else {
                    chars.add(contentChars[i]);
                    i++;
                }
            } else {
                chars.add(contentChars[i]);
                i++;
            }
        }
        char[] tmp = new char[chars.size()];
        System.out.println("Size: "+chars.size());
        for (i = 0; i < tmp.length; i++) {
            tmp[i] = chars.get(i);
            if(i%100000 == 99999){
                System.out.println("I: "+(i+1));
            }
        }
        newContent = new String(tmp);
        return newContent;
    }

//    public static void main(String args[]) {
//        AstService astService = new AstService();
//        List<JavaMethodAstBox> list = astService.getMethods("/home/wallace/mestrado/Utils.java");
//        System.out.println("Tamanho: " + list.size());
//    }
}
