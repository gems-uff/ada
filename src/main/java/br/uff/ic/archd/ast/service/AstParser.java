/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.ast.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jface.text.Document;

/**
 *
 * @author wallace
 */
public class AstParser {

    private HashMap<String, List<String>> hashMap;

    AstParser() {
    }

    public String getClassName(String path) {
        try {
            String content = readFile(path);
            Document doc = new Document(content);
            ASTParser parser = ASTParser.newParser(AST.JLS3);
            parser.setSource(doc.get().toCharArray());
            CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
            //compilationUnit.recordModifications();
            return compilationUnit.getPackage().getName() + "." + ((TypeDeclaration) compilationUnit.types().get(0)).getName().toString();



        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        return null;

    }

    public void parser(String path) {

        try {
            String content = readFile(path);
            Document doc = new Document(content);
            ASTParser parser = ASTParser.newParser(AST.JLS3);
            parser.setSource(doc.get().toCharArray());
            CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
            //compilationUnit.recordModifications();
            AST ast = compilationUnit.getAST();
            List imports = compilationUnit.imports();
            System.out.println("Imports: " + imports.size());
            for (int i = 0; i < imports.size(); i++) {
                ImportDeclaration importation = (ImportDeclaration) imports.get(i);

                System.out.println("Importação: " + importation.getName() + "    classe: " + importation.getClass());
            }
            System.out.println("Pacote: " + compilationUnit.getPackage());
            List types = compilationUnit.types();
            System.out.println("Tipos: " + types.size());
            for (int i = 0; i < types.size(); i++) {
                Object type = types.get(i);
                System.out.println("Tipo: " + type + "    classe: " + type.getClass());
            }
            ASTNode astNode = compilationUnit.getRoot();
            System.out.println("ASTNODE: " + astNode.getClass());
            System.out.println("ROOT tipo: " + compilationUnit.getTypeRoot());
            System.out.println("AST: " + compilationUnit.getAST());
            System.out.println("TIPOS: " + (compilationUnit.types().size()));
            System.out.println("Métodos: " + ((TypeDeclaration) compilationUnit.types().get(0)).getMethods().length);
            System.out.println("Classe: " + ((TypeDeclaration) compilationUnit.types().get(0)).getName());
            System.out.println("Interfaces full: " + ((SimpleType) ((TypeDeclaration) compilationUnit.types().get(0)).superInterfaceTypes().get(0)).getName().getFullyQualifiedName());
            System.out.println("Campos: " + ((TypeDeclaration) compilationUnit.types().get(0)).getFields().length);
            System.out.println("Bodies: " + ((TypeDeclaration) compilationUnit.types().get(0)).bodyDeclarations().size());
            for (int i = 0; i < ((TypeDeclaration) compilationUnit.types().get(0)).bodyDeclarations().size(); i++) {
                System.out.println("body: " + ((TypeDeclaration) compilationUnit.types().get(0)).bodyDeclarations().get(i).getClass());
            }
            System.out.println("Tipos: " + ((TypeDeclaration) compilationUnit.types().get(0)).getTypes().length);
            List modificadores = ((TypeDeclaration) compilationUnit.types().get(0)).modifiers();
            for (int i = 0; i < modificadores.size(); i++) {
                System.out.println("Mod: " + modificadores.get(i));
            }
            MethodDeclaration metodos[] = ((TypeDeclaration) compilationUnit.types().get(0)).getMethods();
            for (int i = 0; i < metodos.length; i++) {
                System.out.println("Metodo: " + metodos[i].getName());
                System.out.println("Modifiers: " + metodos[i].modifiers().size());
                for (int j = 0; j < metodos[i].modifiers().size(); j++) {
                    System.out.println("Mod: " + metodos[i].modifiers().get(j).toString());
                }
                System.out.println("retorno tipo: " + metodos[i].getReturnType2());
                System.out.println("Parametros: " + metodos[i].parameters().size());
                for (int j = 0; j < metodos[i].parameters().size(); j++) {
                    System.out.println("param: " + metodos[i].parameters().get(j).toString());
                    System.out.println("Class: " + metodos[i].parameters().get(j).getClass());
                }

                System.out.println("BODY: ");
                Block block = metodos[i].getBody();
                MethodInvocationVisitor miv = new MethodInvocationVisitor();
                block.accept(miv);
                List<VariableDeclarationExpression> variables = miv.getDeclarations();
                System.out.println("Declarações de variáveis " + variables.size());
                for (VariableDeclarationExpression var : variables) {
                    System.out.println("Varivel declaration: " + var);
                    System.out.println("Varivel declaration tipo: " + var.getType());
                    System.out.println("Varivel declaration frags: " + var.fragments().size());
                    List<VariableDeclarationFragment> frags = var.fragments();
                    for (VariableDeclarationFragment frag : frags) {
                        System.out.println("Variavel declaration: " + frag.getName());
                        System.out.println("Class declaration: " + frag.getClass());
                    }
                }
                
                
                List<VariableDeclarationStatement> variableDeclarations = miv.getVariableDeclarations();
                System.out.println("Declarações de variáveis statement " + variableDeclarations.size());
                for (VariableDeclarationStatement var : variableDeclarations) {
                    System.out.println("Varivel  statement: " + var);
                    System.out.println("Varivel statement tipo: " + var.getType());
                    System.out.println("Varivel statement name: " + ((VariableDeclarationFragment) var.fragments().get(0)).getName());
                    System.out.println("Varivel statement frags: " + var.fragments().size());
                    List<VariableDeclarationFragment> frags = var.fragments();
                    for (VariableDeclarationFragment frag : frags) {
                        System.out.println("Variavel statement: " + frag.getName());
                        System.out.println("Class statement: " + frag.getClass());
                    }
                }
                
                List<SingleVariableDeclaration> singleVariables = miv.getSingleDeclarations();
                System.out.println("Declarações single variáveis " + singleVariables.size());
                for (SingleVariableDeclaration var : singleVariables) {
                    System.out.println("Varivel single declaration: " + var);
                    System.out.println("Varivel single declaration tipo: " + var.getType());
//                    System.out.println("Varivel single declaration frags: " + var.fragments().size());
//                    List<VariableDeclarationFragment> frags = var.fragments();
//                    for (VariableDeclarationFragment frag : frags) {
//                        System.out.println("Variavel single declaration: " + frag.getName());
//                        System.out.println("Class single declaration: " + frag.getClass());
//                    }
                }

                List<MethodInvocation> listmi = miv.getMethods();
                for (MethodInvocation methinv : listmi) {

                    System.out.println("METHOD INVOCATION: " + methinv.toString());
                    System.out.println("MI EXPRESSSION: " + methinv.getExpression());
                    System.out.println("Get class expression: " + getClassExpression(methinv.getExpression()));
                    System.out.println("Tipo de expression: " + (methinv.getExpression() == null ? "" : methinv.getExpression().getClass()));
                    System.out.println("MI NAME: " + methinv.getName());

                    List arguments = methinv.arguments();
                    System.out.println("Argumentos: " + arguments.size());
                    for (Object arg : arguments) {
                        System.out.println("Argumento: " + arg.toString());
                        System.out.println("Classe do argumento: " + getClassExpression((Expression) arg));
                        System.out.println("Classe: " + arg.getClass());
                    }

                }
                List<ClassInstanceCreation> listinstances = miv.getInstances();
                System.out.println("Instances: " + listinstances.size());
                for (ClassInstanceCreation instance : listinstances) {
                    System.out.println("");
                    System.out.println("instance: " + instance.toString());
                }


                List<VariableDeclarationExpression> declarations = miv.getDeclarations();
                System.out.println("Declarations: " + declarations.size());
                for (VariableDeclarationExpression declaration : declarations) {
                    System.out.println("Declaration: " + declaration);
                }

                //ASTNode aNode = block.getRoot();
                //pegarRecursivamente(aNode);
                System.out.println("FIM DO BODY");
            }
            FieldDeclaration fields[] = ((TypeDeclaration) compilationUnit.types().get(0)).getFields();
            System.out.println("Fields: " + fields.length);
            for (int i = 0; i < fields.length; i++) {
                System.out.println("Field: " + fields[i].toString());
                System.out.println("Tipo de field: " + fields[i].getClass());

                FieldDeclarationVisitor filedDeclaration = new FieldDeclarationVisitor();

                fields[i].accept(filedDeclaration);

                List<VariableDeclarationFragment> variables = fields[i].fragments();
                System.out.println("Variaveis: " + variables.size());
                for (VariableDeclarationFragment variable : variables) {
                    System.out.println("Variavel: " + variable.getName());
                    System.out.println("Class: " + variable.getClass());
                }

                System.out.println("Tipo field: " + fields[i].getType());
            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }

    }

    private String getClassExpression(Expression expression) {
        String classExpression = "";
        if (expression == null) {
            return null;
        } else {
            if (expression.getClass() == org.eclipse.jdt.core.dom.ParenthesizedExpression.class) {
                return getClassExpression(((ParenthesizedExpression) expression).getExpression());
            } else if (expression.getClass() == org.eclipse.jdt.core.dom.CastExpression.class) {
                return ((CastExpression) expression).getType().toString();
            } else if (expression.getClass() == org.eclipse.jdt.core.dom.SimpleName.class) {
                return ((SimpleName) expression).toString();
            } else if (expression.getClass() == org.eclipse.jdt.core.dom.MethodInvocation.class) {
                return ((MethodInvocation) expression).getName().toString();
            }
        }
        return null;
    }

    private void pegarRecursivamente(List<MethodInvocation> methodInvocationList) {
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

//    public static void main(String args[]) {
//        AstParser astParser = new AstParser();
//        //astParser.parser("/home/wallace/mestrado/jEdit/org/jedit/core/FileOpenerService.java");
//        astParser.parser("/home/wallace/mestrado/jEdit/org/jedit/io/Native2ASCIIEncoding.java");
//        //System.out.println("NOme da classe: "+astParser.getClassName("/home/wallace/mestrado/jEdit/org/jedit/io/Native2ASCIIEncoding.java"));
//    }
}
