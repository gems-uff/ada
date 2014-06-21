/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.ast.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author wallace
 */
public class XMLService {

    HashMap<String, HashMap<String, List<MethodType>>> classesAndMethods;

    public void createFiles(String path, String outPath) {
        HashMap<String, List<String>> hash = getAllClassNames(path);
        classesAndMethods = new HashMap();
        getJavaProjectMethodsReturnType(hash, path);
        createXMLFiles(hash, path, outPath);
    }

    public void getStringService() {
    }

    public void createXMLFiles(HashMap<String, List<String>> hash, String path, String outPath) {

        File file = new File(path);
        if (file.isDirectory()) {
            File files[] = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                createXMLFiles(hash, files[i].getAbsolutePath(), outPath);
            }
        } else {
            if (file.getAbsolutePath().endsWith(".java")) {
                String xmlText = outPath + getClassName(path) + ".xml";
                //System.out.println("Salvara para: " + xmlText);
                setXML(path, xmlText, hash);
            }
        }
    }

    public void setXML(String javaFile, String xmlText, HashMap<String, List<String>> hash) {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {

            System.out.println("Java File: " + javaFile);
            String content = readFile(javaFile);
            org.eclipse.jface.text.Document doc = new org.eclipse.jface.text.Document(content);
            ASTParser parser = ASTParser.newParser(AST.JLS3);
            parser.setSource(doc.get().toCharArray());
            CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);


            DocumentBuilder builder = factory.newDocumentBuilder();

            Document myDoc = builder.newDocument();
            Node nodeClazz = createXMLNodeClass(compilationUnit, myDoc, hash);
            myDoc.appendChild(nodeClazz);
            saveXML(xmlText, myDoc);
        } catch (Exception e) {
            System.out.println("Erro SetXML: " + e.getMessage());
        }

    }

    private Node createImplementsNode(CompilationUnit compilationUnit, Document myDoc, HashMap<String, List<String>> hash) {

        Node implsNode = myDoc.createElement("implements");
        for (int i = 0; i < ((TypeDeclaration) compilationUnit.types().get(0)).superInterfaceTypes().size(); i++) {
            Name name = ((SimpleType) ((TypeDeclaration) compilationUnit.types().get(0)).superInterfaceTypes().get(i)).getName();
            Node implNode = myDoc.createElement("interface");
            String interfaceName = name.getFullyQualifiedName();
            if (!isQualified(interfaceName)) {
                List<String> names = hash.get(interfaceName);
                if (names != null) {
                    String qualifiedName = null;
                    for (String namep : names) {
                        if (namep.endsWith(interfaceName)) {
                            qualifiedName = namep;
                            break;
                        }
                    }
                    if (qualifiedName != null) {
                        interfaceName = qualifiedName;
                    }
                }

            }
            implNode.appendChild(myDoc.createTextNode(interfaceName));
            implsNode.appendChild(implNode);
        }

        return implsNode;
    }

    private Node createAttributesNode(CompilationUnit compilationUnit, Document myDoc, HashMap<String, String> hashAttributes, HashMap<String, List<String>> hash) {
        Node attrNode = myDoc.createElement("attributes");
        FieldDeclaration fields[] = ((TypeDeclaration) compilationUnit.types().get(0)).getFields();
        for (int i = 0; i < fields.length; i++) {
            Node attrModifiers = myDoc.createElement("modifiers");
            for (int y = 0; y < fields[i].modifiers().size(); y++) {
                Modifier modifier = (Modifier) fields[i].modifiers().get(y);
                Node attrModifier = myDoc.createElement("modifier");
                attrModifier.appendChild(myDoc.createTextNode(modifier.toString()));
                attrModifiers.appendChild(attrModifier);
            }
            fields[i].modifiers();

            String returnType = fields[i].getType().toString();

            if (!hash.get(returnType).isEmpty()) {
                returnType = hash.get(returnType).get(0);
            }
            List<VariableDeclarationFragment> variables = fields[i].fragments();
            for (VariableDeclarationFragment vdf : variables) {
                String variableName = vdf.getName().toString();
                hashAttributes.put(variableName, fields[i].getType().toString());

                Node attribute = myDoc.createElement("attribute");
                Node name = myDoc.createElement("attribute_name");
                name.appendChild(myDoc.createTextNode(variableName));
                Node type = myDoc.createElement("attribute_type");
                type.appendChild(myDoc.createTextNode(fields[i].getType().toString()));
                attribute.appendChild(name);
                attribute.appendChild(type);
                attribute.appendChild(attrModifiers);
                attrNode.appendChild(attribute);

            }
            //System.out.println("Variaveis: " + variables.size());

            //System.out.println("Tipo field: " + fields[i].getType());





        }
        return attrNode;
    }

    private Node createMethodsNode(CompilationUnit compilationUnit, Document myDoc, HashMap<String, String> hashAttributes, HashMap<String, List<String>> hash) {
        Node methodsNode = myDoc.createElement("methods");
        MethodDeclaration methods[] = ((TypeDeclaration) compilationUnit.types().get(0)).getMethods();
        for (int i = 0; i < methods.length; i++) {
            Node method = myDoc.createElement("method");
            Node name = myDoc.createElement("method_name");

            String methodName = methods[i].getName().toString();
            if (!isQualified(methodName)) {
                List<String> names = hash.get(methodName);
                if (names != null) {
                    String qualifiedName = null;
                    for (String namep : names) {
                        if (namep.endsWith(methodName)) {
                            qualifiedName = namep;
                            break;
                        }
                    }
                    if (qualifiedName != null) {
                        methodName = qualifiedName;
                    }
                }

            }

            name.appendChild(myDoc.createTextNode(methodName));
            Node modifiers = myDoc.createElement("modifiers");
            //System.out.println("Modifiers: " + methods[i].modifiers().size());
            for (int j = 0; j < methods[i].modifiers().size(); j++) {
                //System.out.println("Mod: " + methods[i].modifiers().get(j).toString());
                Node modifier = myDoc.createElement("modifier");
                modifier.appendChild(myDoc.createTextNode(methods[i].modifiers().get(j).toString()));
                modifiers.appendChild(modifier);
            }


            String returnTypeName = methods[i].getReturnType2().toString();
            if (!isQualified(returnTypeName)) {
                List<String> names = hash.get(returnTypeName);
                if (names != null) {
                    String qualifiedName = null;
                    for (String namep : names) {
                        if (namep.endsWith(returnTypeName)) {
                            qualifiedName = namep;
                            break;
                        }
                    }
                    if (qualifiedName != null) {
                        returnTypeName = qualifiedName;
                    }
                }

            }


            Node returnType = myDoc.createElement("return_type");
            returnType.appendChild(myDoc.createTextNode(returnTypeName));
            //System.out.println("retorno tipo: " + methods[i].getReturnType2());
            //System.out.println("Parametros: " + methods[i].parameters().size());

            Node params = myDoc.createElement("parameters");
            for (int j = 0; j < methods[i].parameters().size(); j++) {
                Node param = myDoc.createElement("parameter");
                Node paramName = myDoc.createElement("parameter_name");
                paramName.appendChild(myDoc.createTextNode(((SingleVariableDeclaration) methods[i].parameters().get(j)).getName().toString()));
                Node paramType = myDoc.createElement("parameter_type");

                String paramTypeName = ((SingleVariableDeclaration) methods[i].parameters().get(j)).getType().toString();
                if (!isQualified(paramTypeName)) {
                    List<String> names = hash.get(paramTypeName);
                    if (names != null) {
                        String qualifiedName = null;
                        for (String namep : names) {
                            if (namep.endsWith(paramTypeName)) {
                                qualifiedName = namep;
                                break;
                            }
                        }
                        if (qualifiedName != null) {
                            paramTypeName = qualifiedName;
                        }
                    }

                }
                paramType.appendChild(myDoc.createTextNode(paramTypeName));
                //System.out.println("param: " + methods[i].parameters().get(j).toString());
                param.appendChild(paramName);
                param.appendChild(paramType);
                params.appendChild(param);

            }


            //verificar as chamadas:
            List<String> invocations = new ArrayList();
            HashMap<String, String> hashMethodDeclarations = new HashMap();
            //System.out.println("BODY: ");
            Block block = methods[i].getBody();
            getVariableDeclarations(block, hashMethodDeclarations, hash);

            MethodInvocationVisitor miv = new MethodInvocationVisitor();
            block.accept(miv);
            List<MethodInvocation> listmi = miv.getMethods();
            for (MethodInvocation methinv : listmi) {

                //System.out.println("METHOD INVOCATION: " + methinv.toString());
                //System.out.println("MI EXPRESSSION: " + methinv.getExpression());
                String returnClassType = getClassReturnType(methinv.getExpression(), hashAttributes, hashMethodDeclarations, hash);
                if (returnClassType != null) {
                    List arguments = methinv.arguments();
                    String methodSignature = methinv.getName() + ":" + methinv.arguments().size();
                    HashMap<String, List<MethodType>> methodAndTypes = classesAndMethods.get(returnClassType);
                    List<MethodType> listNames = methodAndTypes.get(methodSignature);
                    if (!listNames.isEmpty()) {
//                        if (listNames.size() == 1) {
//                            invocations.add(listNames.get(0).getOficialMethodSignature());
//                        } else {
//                            List<String> argumentsTypes = new ArrayList();
//                            for(Object argument : arguments){
//                                
//                            }
//
//                            for (MethodType methodType : listNames) {
//                            }
//                        }
                        invocations.add(returnClassType + ":" + listNames.get(0).getOficialMethodSignature());
                        System.out.println("Invocation: " + returnClassType + ":" + listNames.get(0).getOficialMethodSignature());

                    }

                }
            }

            Node invocationsNode = myDoc.createElement("invocations");
            for (String invocation : invocations) {
                Node invocationNode = myDoc.createElement("invocation");
                invocationNode.appendChild(myDoc.createTextNode(invocation));
                invocationsNode.appendChild(invocationNode);
            }

            Node bodySize = myDoc.createElement("body_size");
            //System.out.println("Body: " + methods[i].getBody().toString());
            bodySize.appendChild(myDoc.createTextNode(String.valueOf(methods[i].getBody().toString().length())));
            method.appendChild(name);
            method.appendChild(modifiers);
            method.appendChild(returnType);
            method.appendChild(params);
            method.appendChild(bodySize);
            method.appendChild(invocationsNode);
            methodsNode.appendChild(method);
        }
        return methodsNode;
    }

    public Node createXMLNodeClass(CompilationUnit compilationUnit, Document myDoc, HashMap<String, List<String>> hash) {
        AST ast = compilationUnit.getAST();

        ASTNode astNode = compilationUnit.getRoot();

        List imports = compilationUnit.imports();
        //System.out.println("Imports: " + imports.size());

        List<String> importationsList = new LinkedList();
        for (int i = 0; i < imports.size(); i++) {
            ImportDeclaration importation = (ImportDeclaration) imports.get(i);
            importationsList.add(importation.getName().toString());
        }



        Node clazzNode = myDoc.createElement("class");
        boolean isInterface = ((TypeDeclaration) compilationUnit.types().get(0)).isInterface();
        Node isInterfaceNode = myDoc.createElement("is_interface");
        isInterfaceNode.appendChild(myDoc.createTextNode(String.valueOf(isInterface)));
        Node clazzNameNode = myDoc.createElement("class_name");
        clazzNameNode.appendChild(myDoc.createTextNode(compilationUnit.getPackage().getName() + "." + ((TypeDeclaration) compilationUnit.types().get(0)).getName().toString()));

        Node implsNode = createImplementsNode(compilationUnit, myDoc, hash);

        Node superClazz = myDoc.createElement("superclass");
        Type superType = ((TypeDeclaration) compilationUnit.types().get(0)).getSuperclassType();
        superClazz.appendChild(myDoc.createTextNode(superType == null ? "java.lang.Object" : superType.toString()));

        HashMap<String, String> hashAttributes = new HashMap();
        Node attrNode = createAttributesNode(compilationUnit, myDoc, hashAttributes, hash);

        Node methodsNode = createMethodsNode(compilationUnit, myDoc, hashAttributes, hash);
        clazzNode.appendChild(isInterfaceNode);
        clazzNode.appendChild(clazzNameNode);
        clazzNode.appendChild(superClazz);
        clazzNode.appendChild(implsNode);
        clazzNode.appendChild(attrNode);
        clazzNode.appendChild(methodsNode);
        return clazzNode;

        //Node pathtNode = myDoc.createElement("path");
        //Node mainNode = myDoc.createElement("main");


    }

    private void getVariableDeclarations(Block block, HashMap<String, String> hashMethodDeclarations, HashMap<String, List<String>> hash) {
        MethodInvocationVisitor miv = new MethodInvocationVisitor();
        block.accept(miv);
        List<VariableDeclarationExpression> variables = miv.getDeclarations();
        //System.out.println("Declarações de variáveis " + variables.size());
        for (VariableDeclarationExpression var : variables) {
            List<VariableDeclarationFragment> frags = var.fragments();
            for (VariableDeclarationFragment frag : frags) {
                List<String> list = hash.get(var.getType().toString());
                if (!list.isEmpty()) {
                    hashMethodDeclarations.put(frag.getName().toString(), list.get(0));
                } else {
                    hashMethodDeclarations.put(frag.getName().toString(), var.getType().toString());
                }

            }
        }

        List<VariableDeclarationStatement> variableDeclarations = miv.getVariableDeclarations();
        for (VariableDeclarationStatement var : variableDeclarations) {

            List<VariableDeclarationFragment> frags = var.fragments();
            for (VariableDeclarationFragment frag : frags) {
                List<String> list = hash.get(var.getType().toString());
                if (!list.isEmpty()) {
                    hashMethodDeclarations.put(frag.getName().toString(), list.get(0));
                }

            }
        }

        List<SingleVariableDeclaration> singleVariables = miv.getSingleDeclarations();
        System.out.println("Declarações single variáveis " + singleVariables.size());
        for (SingleVariableDeclaration var : singleVariables) {
            List<String> list = hash.get(var.getType().toString());
            if (!list.isEmpty()) {
                hashMethodDeclarations.put(var.getName().toString(), list.get(0));
            }
        }
    }

    private String getClassReturnType(Expression expression, HashMap<String, String> hashAttributes, HashMap<String, String> hashMethodDeclarations, HashMap<String, List<String>> hash) {
        String classExpression = "";
        if (expression == null) {
            return null;
        } else {
            if (expression.getClass() == org.eclipse.jdt.core.dom.ParenthesizedExpression.class) {
                return getClassReturnType(((ParenthesizedExpression) expression).getExpression(), hashAttributes, hashMethodDeclarations, hash);
            } else if (expression.getClass() == org.eclipse.jdt.core.dom.CastExpression.class) {
                String classReturnType = ((CastExpression) expression).getType().toString();
                List<String> list = hash.get(classReturnType);
                if (!list.isEmpty()) {
                    return list.get(0);
                } else {
                    return null;
                }
            } else if (expression.getClass() == org.eclipse.jdt.core.dom.SimpleName.class) {
                String classReturnType = hashMethodDeclarations.get(((SimpleName) expression).toString());
                if (classReturnType == null) {
                    classReturnType = hashAttributes.get(((SimpleName) expression).toString());
                }
                return classReturnType;
            } else if (expression.getClass() == org.eclipse.jdt.core.dom.MethodInvocation.class) {
                return getClassReturnType(((MethodInvocation) expression).getExpression(), hashAttributes, hashMethodDeclarations, hash);
            }
        }
        return null;
    }

    private void getCalls(String body, HashMap<String, String> hashAttributes, HashMap<String, String> methodInvocations) {
        char[] bodyChars = body.toCharArray();
        int i = 0;
        //while(i < )
    }

    private void saveXML(String fileXMLClass, Document myDoc) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            File file = new File(fileXMLClass);
            if (!file.exists()) {
                System.out.println("PAssou1");
                file.createNewFile();
            }
            System.out.println("PAssou2");
            DOMSource source = new DOMSource(myDoc);
            StreamResult result = new StreamResult(file);

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);
        } catch (Exception e) {
            System.out.println("Erro save XML: " + e.getMessage());
        }
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

    public List<String> getImports(String classPath) {
        try {
            HashMap<String, List<MethodType>> methodsHash = new HashMap();
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
    
    public List<ParameterAst> getAttributes(String classPath){
        try {
            String content = readFile(classPath);
            //System.out.println("Path: "+path);
            org.eclipse.jface.text.Document doc = new org.eclipse.jface.text.Document(content);
            ASTParser parser = ASTParser.newParser(AST.JLS3);
            parser.setSource(doc.get().toCharArray());
            CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
            
            List<ParameterAst> list = new ArrayList();
            
            
            
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
        return null;
    }

    public List<JavaMethodAstBox> getMethods(String classPath) {
        try {
            String content = readFile(classPath);
            //System.out.println("Path: "+path);
            org.eclipse.jface.text.Document doc = new org.eclipse.jface.text.Document(content);
            ASTParser parser = ASTParser.newParser(AST.JLS3);
            parser.setSource(doc.get().toCharArray());
            CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
            List<JavaMethodAstBox> list = new ArrayList();
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
        try {
            String content = readFile(classPath);
            //System.out.println("Path: "+path);
            org.eclipse.jface.text.Document doc = new org.eclipse.jface.text.Document(content);
            ASTParser parser = ASTParser.newParser(AST.JLS3);
            parser.setSource(doc.get().toCharArray());
            CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
            List<String> list = new ArrayList();
            for (int i = 0; i < ((TypeDeclaration) compilationUnit.types().get(0)).superInterfaceTypes().size(); i++) {
                Name name = ((SimpleType) ((TypeDeclaration) compilationUnit.types().get(0)).superInterfaceTypes().get(i)).getName();
                String interfaceName = name.getFullyQualifiedName();
                list.add(interfaceName);
            }

            return list;



        } catch (Exception e) {
            System.out.println("Erro getMethodsReturnType Name: " + e.getMessage());
        }
        return null;
    }

    public HashMap<String, List<MethodType>> getMethodsReturnType(HashMap<String, List<String>> hash, String path, String className) {
        try {
            HashMap<String, List<MethodType>> methodsHash = new HashMap();
            String content = readFile(path);
            //System.out.println("Path: "+path);
            org.eclipse.jface.text.Document doc = new org.eclipse.jface.text.Document(content);
            ASTParser parser = ASTParser.newParser(AST.JLS3);
            parser.setSource(doc.get().toCharArray());
            CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
            MethodDeclaration methods[] = ((TypeDeclaration) compilationUnit.types().get(0)).getMethods();
            for (int i = 0; i < methods.length; i++) {


                String methodName = methods[i].getName().toString();
//                if (!isQualified(methodName)) {
//                    List<String> names = hash.get(methodName);
//                    if (names != null) {
//                        String qualifiedName = null;
//                        for (String namep : names) {
//                            if (namep.endsWith(methodName)) {
//                                qualifiedName = namep;
//                                break;
//                            }
//                        }
//                        if (qualifiedName != null) {
//                            methodName = qualifiedName;
//                        }
//                    }
//
//                }






                //System.out.println("Return type do "+methods[i].getName() +"   qualifiedname : "+methodName+" :"+methods[i].getReturnType2());
                String returnTypeName = className;
                if (methods[i].getReturnType2() != null) {
                    returnTypeName = methods[i].getReturnType2().toString();
                }
                //System.out.println("Return type: "+returnTypeName);
                if (!isQualified(returnTypeName)) {
                    List<String> names = hash.get(returnTypeName);
                    if (names != null) {
                        String qualifiedName = null;
                        for (String namep : names) {
                            if (namep.endsWith(returnTypeName)) {
                                qualifiedName = namep;
                                break;
                            }
                        }
                        if (qualifiedName != null) {
                            returnTypeName = qualifiedName;
                        }
                    }

                }



                //System.out.println("retorno tipo: " + methods[i].getReturnType2());
                //System.out.println("Parametros: " + methods[i].parameters().size());
                List<String> arguments = new ArrayList();

                for (int j = 0; j < methods[i].parameters().size(); j++) {

                    String paramTypeName = ((SingleVariableDeclaration) methods[i].parameters().get(j)).getType().toString();
                    if (!isQualified(paramTypeName)) {
                        List<String> names = hash.get(paramTypeName);
                        if (names != null) {
                            String qualifiedName = null;
                            for (String namep : names) {
                                if (namep.endsWith(paramTypeName)) {
                                    qualifiedName = namep;
                                    break;
                                }
                            }
                            if (qualifiedName != null) {
                                paramTypeName = qualifiedName;
                            }
                        }

                    }
                    arguments.add(paramTypeName);
                }

                MethodType methodType = new MethodType(methodName, returnTypeName, arguments);
                List<MethodType> methodTypes = methodsHash.get(methodType.getMethodSignature());
                if (methodTypes == null) {
                    methodTypes = new ArrayList();
                }
                methodTypes.add(methodType);
                methodsHash.put(methodType.getMethodSignature(), methodTypes);
                //System.out.println("Metodo: "+methodType.getOficialMethodSignature());
            }

            return methodsHash;

        } catch (Exception e) {
            System.out.println("Erro getMethodsReturnType Name: " + e.getMessage());
        }
        return null;
    }

    private boolean isQualified(String name) {
        return name.contains(".");
    }

    public HashMap<String, List<String>> getAllClassNames(String path) {
        HashMap<String, List<String>> hashMap = new HashMap();

        getJavaProjectClassNames(hashMap, path);

        return hashMap;
    }

    public List<String> getAllJavaClassesPath(String path) {
        List<String> classesPath = new LinkedList();

        getClassesPath(classesPath, path);

        return classesPath;
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

    public void getJavaProjectClassNames(HashMap<String, List<String>> hash, String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            File files[] = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                getJavaProjectClassNames(hash, files[i].getAbsolutePath());
            }
        } else {
            if (file.getAbsolutePath().endsWith(".java")) {
                String fileName = file.getName().substring(0, file.getName().length() - 5);
                List<String> list = hash.get(fileName);
                if (list == null) {
                    list = new LinkedList();
                }
                list.add(getClassName(path));
                hash.put(fileName, list);
            }
        }
    }

    public void getJavaProjectMethodsReturnType(HashMap<String, List<String>> hash, String path) {

        File file = new File(path);
        if (file.isDirectory()) {
            File files[] = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                getJavaProjectMethodsReturnType(hash, files[i].getAbsolutePath());
            }
        } else {
            if (file.getAbsolutePath().endsWith(".java")) {
                String className = getClassName(path);
                HashMap<String, List<MethodType>> methodHashMap = getMethodsReturnType(hash, path, className);
                classesAndMethods.put(className, methodHashMap);
            }
        }
    }

    public String[] getClassesNames(String path) {
        File file = new File(path);
        String[] filesNames = file.list();
        String[] classesNames = new String[filesNames.length];
        for (int i = 0; i < filesNames.length; i++) {
            classesNames[i] = filesNames[i].substring(0, filesNames[i].length() - 4);
        }
        return classesNames;
    }

    public String[] getInvocations(String path) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        String methodsNames[] = null;
        File file = new File(path);
        try {
            builder = factory.newDocumentBuilder();

            Document myDoc = builder.parse(file);

            NodeList invocations = myDoc.getElementsByTagName("invocation");


            methodsNames = new String[invocations.getLength()];
            for (int i = 0; i < invocations.getLength(); i++) {
                String name = invocations.item(i).getChildNodes().item(0).getNodeValue();

                methodsNames[i] = name;

            }

        } catch (ParserConfigurationException ex) {
            System.out.println("");
        } catch (SAXException ex) {
            System.out.println("");
        } catch (IOException ex) {
            System.out.println("");
        } catch (Exception ex) {
            System.out.println("");
        }
        return methodsNames;
    }

    /*public String[] getMethods(String path) {
     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
     DocumentBuilder builder;
     String methodsNames[] = null;
     File file = new File(path);
     try {
     builder = factory.newDocumentBuilder();

     Document myDoc = builder.parse(file);

     NodeList methods = myDoc.getElementsByTagName("method");


     methodsNames = new String[methods.getLength()];
     String retType = "";
     for (int i = 0; i < methods.getLength(); i++) {
     NodeList childs = methods.item(i).getChildNodes();
     String name = "";
     String params = "";
     for (int j = 0; j < childs.getLength(); j++) {
     if (childs.item(j).getNodeName().equals("method_name")) {

     name = childs.item(j).getChildNodes().item(0).getNodeValue();
     } else if (childs.item(j).getNodeName().equals("parameters")) {
     NodeList parameters = childs.item(j).getChildNodes();
     for (int k = 0; k < parameters.getLength(); k++) {
     if (parameters.item(k).getNodeName().equals("parameter")) {
     NodeList parameterNames = parameters.item(k).getChildNodes();
     for (int l = 0; l < parameterNames.getLength(); l++) {
     if (parameterNames.item(l).getNodeName().equals("parameter_type")) {
     if (params.equals("")) {
     params = parameterNames.item(l).getChildNodes().item(0).getNodeValue();
     } else {
     params = params + "," + parameterNames.item(l).getChildNodes().item(0).getNodeValue();
     }
     }
     }
     }
     }


     } else if (childs.item(j).getNodeName().equals("return_type")) {

     retType = childs.item(j).getChildNodes().item(0).getNodeValue();
     }

     }

     methodsNames[i] = retType + " " + name + "(" + params + ")";

     }

     } catch (ParserConfigurationException ex) {
     System.out.println("");
     } catch (SAXException ex) {
     System.out.println("");
     } catch (IOException ex) {
     System.out.println("");
     } catch (Exception ex) {
     System.out.println("");
     }
     return methodsNames;
     }*/
    public static void main(String args[]) {
        XMLService xmlService = new XMLService();
        //xmlService.setXML("/home/wallace/mestrado/jEdit/org/jedit/io/Native2ASCIIEncoding.java", "/home/wallace/mestrado/testes/01.xml");
        //xmlService.createFiles("/home/wallace/mestrado/jEdit", "/home/wallace/mestrado/testes/");
        String name = xmlService.getClassName("/home/wallace/mestrado/jEdit/org/jedit/core/FileOpenerService.java");
        System.out.println("Name: " + name);
    }
}
