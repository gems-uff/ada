/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.xml.service;

import br.uff.ic.archd.ast.service.JavaMethodAstBox;
import br.uff.ic.archd.ast.service.ParameterAst;
import br.uff.ic.archd.javacode.JavaAbstract;
import br.uff.ic.archd.javacode.JavaAttribute;
import br.uff.ic.archd.javacode.JavaClass;
import br.uff.ic.archd.javacode.JavaInterface;
import br.uff.ic.archd.javacode.JavaMethod;
import br.uff.ic.archd.javacode.JavaMethodInvocation;
import br.uff.ic.archd.javacode.Parameter;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author wallace
 */
public class XMLService {

    private static final String PROJECT_FILES_DIRECTORY = System.getProperty("user.home") + "/.archd/HISTORY/";

    public void setXML(JavaAbstract javaAbstract, String revisionId) {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder builder = factory.newDocumentBuilder();

            String xmlPath = PROJECT_FILES_DIRECTORY + revisionId + "/" + javaAbstract.getFullQualifiedName() + ".xml";
            Document myDoc = builder.newDocument();
            Node nodeClazz = createXMLNodeClass(javaAbstract, myDoc);
            myDoc.appendChild(nodeClazz);
            saveXML(xmlPath, myDoc);
        } catch (Exception e) {
            System.out.println("Erro SetXML: " + e.getMessage());
        }

    }

    public Node createXMLNodeClass(JavaAbstract javaAbstract, Document myDoc) {

        Node clazzNode = myDoc.createElement("class");

        Node javaPackageNode = myDoc.createElement("package");
        javaPackageNode.appendChild(myDoc.createTextNode(javaAbstract.getJavaPackage().getName()));

        Node nameNode = myDoc.createElement("name");
        nameNode.appendChild(myDoc.createTextNode(javaAbstract.getName()));

        Node pathNode = myDoc.createElement("path");
        pathNode.appendChild(myDoc.createTextNode(javaAbstract.getPath()));

        Node imports = myDoc.createElement("imports");
        for (JavaAbstract importJavaAbstract : javaAbstract.getClassesImports()) {
            Node importNode = myDoc.createElement("import");
            importNode.appendChild(myDoc.createTextNode(importJavaAbstract.getFullQualifiedName()));
            imports.appendChild(importNode);
        }



        Node externalImports = myDoc.createElement("external_imports");
        for (String externalImport : javaAbstract.getExternalImports()) {
            Node importNode = myDoc.createElement("external_import");
            importNode.appendChild(myDoc.createTextNode(externalImport));
            externalImports.appendChild(importNode);
        }

        clazzNode.appendChild(nameNode);
        clazzNode.appendChild(javaPackageNode);
        clazzNode.appendChild(pathNode);
        clazzNode.appendChild(imports);
        clazzNode.appendChild(externalImports);

        if (javaAbstract.getClass() == JavaClass.class) {
            JavaClass javaClass = (JavaClass) javaAbstract;
            Node isInterfaceNode = myDoc.createElement("is_interface");
            isInterfaceNode.appendChild(myDoc.createTextNode(String.valueOf(false)));

            Node superClass = myDoc.createElement("superclass");
            superClass.appendChild(myDoc.createTextNode(javaClass.getSuperClass() == null ? "" : javaClass.getSuperClass().getFullQualifiedName()));

            Node implementedInterfaces = myDoc.createElement("implemented_interfaces");
            for (JavaInterface javaInterface : javaClass.getImplementedInterfaces()) {
                Node implementedInterface = myDoc.createElement("implemented_interface");
                implementedInterface.appendChild(myDoc.createTextNode(javaInterface.getFullQualifiedName()));
                implementedInterfaces.appendChild(implementedInterface);
            }

            Node attributesNodes = myDoc.createElement("attributes");
            for (JavaAttribute javaAttribute : javaClass.getAttributes()) {
                Node attributeNode = myDoc.createElement("attribute");
                Node attributeNameNode = myDoc.createElement("attribute_name");
                attributeNameNode.appendChild(myDoc.createTextNode(javaAttribute.getName()));
                Node attributeTypeNode = myDoc.createElement("attribute_type");
                String attributeTypeName = null;
                if (javaAttribute.getType().getClass() == JavaClass.class || javaAttribute.getType().getClass() == JavaInterface.class) {
                    attributeTypeName = ((JavaAbstract) javaAttribute.getType()).getFullQualifiedName();
                } else {
                    attributeTypeName = javaAttribute.getType().getName();
                }
                attributeTypeNode.appendChild(myDoc.createTextNode(attributeTypeName));

                Node isFinalNode = myDoc.createElement("is_final");
                isFinalNode.appendChild(myDoc.createTextNode(String.valueOf(javaAttribute.isFinal())));
                Node isPrivateNode = myDoc.createElement("is_private");
                isPrivateNode.appendChild(myDoc.createTextNode(String.valueOf(javaAttribute.isPrivate())));
                Node isProtectedNode = myDoc.createElement("is_protected");
                isProtectedNode.appendChild(myDoc.createTextNode(String.valueOf(javaAttribute.isProtected())));
                Node isPublicNode = myDoc.createElement("is_public");
                isPublicNode.appendChild(myDoc.createTextNode(String.valueOf(javaAttribute.isPublic())));
                Node isStaticNode = myDoc.createElement("is_static");
                isStaticNode.appendChild(myDoc.createTextNode(String.valueOf(javaAttribute.isStatic())));
                Node isVolatileNode = myDoc.createElement("is_volatile");
                isVolatileNode.appendChild(myDoc.createTextNode(String.valueOf(javaAttribute.isVolatile())));

                attributeNode.appendChild(attributeNameNode);
                attributeNode.appendChild(attributeTypeNode);
                attributeNode.appendChild(isFinalNode);
                attributeNode.appendChild(isPrivateNode);
                attributeNode.appendChild(isProtectedNode);
                attributeNode.appendChild(isPublicNode);
                attributeNode.appendChild(isStaticNode);
                attributeNode.appendChild(isVolatileNode);


                attributesNodes.appendChild(attributeNode);
            }

            Node methodsNode = myDoc.createElement("methods");
            for (JavaMethod javaMethod : javaClass.getMethods()) {
                Node methodNode = createMethodNodeFromJavaClass(javaMethod, myDoc);
                methodsNode.appendChild(methodNode);
            }

            clazzNode.appendChild(isInterfaceNode);
            clazzNode.appendChild(superClass);
            clazzNode.appendChild(implementedInterfaces);
            clazzNode.appendChild(attributesNodes);
            clazzNode.appendChild(methodsNode);

        } else if (javaAbstract.getClass() == JavaInterface.class) {
            JavaInterface javaInterface = (JavaInterface) javaAbstract;
            Node isInterfaceNode = myDoc.createElement("is_interface");
            isInterfaceNode.appendChild(myDoc.createTextNode(String.valueOf(true)));


            Node methodsNode = myDoc.createElement("methods");
            for (JavaMethod javaMethod : javaInterface.getMethods()) {
                Node methodNode = createMethodNodeFromJavaInterface(javaMethod, myDoc);
                methodsNode.appendChild(methodNode);
            }

            clazzNode.appendChild(isInterfaceNode);
            clazzNode.appendChild(methodsNode);


        }

        return clazzNode;

        //Node pathtNode = myDoc.createElement("path");
        //Node mainNode = myDoc.createElement("main");


    }

    private Node createMethodNodeFromJavaClass(JavaMethod javaMethod, Document myDoc) {


        Node methodNode = myDoc.createElement("method");

        Node methodName = myDoc.createElement("method_name");
        methodName.appendChild(myDoc.createTextNode(javaMethod.getName()));

        Node methodInternalIdNode = myDoc.createElement("method_internal_id");
        methodInternalIdNode.appendChild(myDoc.createTextNode(String.valueOf(javaMethod.getInternalID())));

        Node isFinalNode = myDoc.createElement("is_final");
        isFinalNode.appendChild(myDoc.createTextNode(String.valueOf(javaMethod.isFinal())));
        Node isPrivateNode = myDoc.createElement("is_private");
        isPrivateNode.appendChild(myDoc.createTextNode(String.valueOf(javaMethod.isPrivate())));
        Node isProtectedNode = myDoc.createElement("is_protected");
        isProtectedNode.appendChild(myDoc.createTextNode(String.valueOf(javaMethod.isProtected())));
        Node isPublicNode = myDoc.createElement("is_public");
        isPublicNode.appendChild(myDoc.createTextNode(String.valueOf(javaMethod.isPublic())));
        Node isStaticNode = myDoc.createElement("is_static");
        isStaticNode.appendChild(myDoc.createTextNode(String.valueOf(javaMethod.isStatic())));
        Node isAbstractNode = myDoc.createElement("is_abstract");
        isAbstractNode.appendChild(myDoc.createTextNode(String.valueOf(javaMethod.isAbstract())));
        Node isSynchronizedNode = myDoc.createElement("is_synchronized");
        isSynchronizedNode.appendChild(myDoc.createTextNode(String.valueOf(javaMethod.isSynchronized())));

        Node returnTypeNode = myDoc.createElement("return_type");
        String typeName = null;
        if (javaMethod.getReturnType().getClass() == JavaClass.class || javaMethod.getReturnType().getClass() == JavaInterface.class) {
            typeName = ((JavaAbstract) javaMethod.getReturnType()).getFullQualifiedName();
        } else {
            typeName = javaMethod.getReturnType().getName();
        }
        returnTypeNode.appendChild(myDoc.createTextNode(typeName));

        Node sizeIncharsNode = myDoc.createElement("size_in_chars");
        sizeIncharsNode.appendChild(myDoc.createTextNode(String.valueOf(javaMethod.getSizeInChars())));

        Node cyclomaticComplexityNode = myDoc.createElement("cyclomatic_complexity");
        cyclomaticComplexityNode.appendChild(myDoc.createTextNode(String.valueOf(javaMethod.getCyclomaticComplexity())));

        Node changeInternalStateNode = myDoc.createElement("change_internal_state");
        changeInternalStateNode.appendChild(myDoc.createTextNode(String.valueOf(javaMethod.isChangeInternalState())));

        Node changeInternalStateByMethodInvocationNode = myDoc.createElement("change_internal_state_by_method_invocation");
        changeInternalStateByMethodInvocationNode.appendChild(myDoc.createTextNode(String.valueOf(javaMethod.isChangeInternalStateByMethodInvocations())));

        Node parametersNode = myDoc.createElement("parameters");
        for (Parameter parameter : javaMethod.getParameters()) {

            Node parameterNode = myDoc.createElement("parameter");

            typeName = null;
            if (parameter.getType().getClass() == JavaClass.class || parameter.getType().getClass() == JavaInterface.class) {
                typeName = ((JavaAbstract) parameter.getType()).getFullQualifiedName();
            } else {
                typeName = parameter.getType().getName();
            }

            Node parameterNameNode = myDoc.createElement("parameter_name");
            parameterNameNode.appendChild(myDoc.createTextNode(String.valueOf(parameter.getName())));

            Node parameterTypeNode = myDoc.createElement("parameter_type");
            parameterTypeNode.appendChild(myDoc.createTextNode(String.valueOf(typeName)));

            parameterNode.appendChild(parameterNameNode);
            parameterNode.appendChild(parameterTypeNode);
            parametersNode.appendChild(parameterNode);

        }

        Node methodInternalInvocationsNode = myDoc.createElement("method_internal_invocations");
        for (JavaMethodInvocation internalInvocation : javaMethod.getInternalMethodInvocations()) {
            Node methodInternalInvocationNode = myDoc.createElement("method_internal_invocation_id");
            methodInternalInvocationNode.appendChild(myDoc.createTextNode(String.valueOf(internalInvocation.getJavaMethod().getInternalID())));
            methodInternalInvocationsNode.appendChild(methodInternalInvocationNode);
        }

        Node methodInvocationsNode = myDoc.createElement("method_invocations");
        for (JavaMethodInvocation javaMethodInvocation : javaMethod.getMethodInvocations()) {
            String methodInvocationStr = null;
            if (javaMethodInvocation.getJavaMethod() != null) {
                methodInvocationStr = javaMethodInvocation.getJavaAbstract().getFullQualifiedName() + ":" + javaMethodInvocation.getJavaMethod().getInternalID();
            } else {
                methodInvocationStr = javaMethodInvocation.getJavaAbstract().getFullQualifiedName() + ":" + javaMethodInvocation.getUnknowMethodName();
            }
            Node methodInvocationNode = myDoc.createElement("method_invocation");
            methodInvocationNode.appendChild(myDoc.createTextNode(String.valueOf(methodInvocationStr)));
            methodInvocationsNode.appendChild(methodInvocationNode);
        }


        methodNode.appendChild(methodName);
        methodNode.appendChild(methodInternalIdNode);
        methodNode.appendChild(isFinalNode);
        methodNode.appendChild(isPrivateNode);
        methodNode.appendChild(isProtectedNode);
        methodNode.appendChild(isPublicNode);
        methodNode.appendChild(isStaticNode);
        methodNode.appendChild(isAbstractNode);
        methodNode.appendChild(isSynchronizedNode);
        methodNode.appendChild(returnTypeNode);
        methodNode.appendChild(sizeIncharsNode);
        methodNode.appendChild(cyclomaticComplexityNode);
        methodNode.appendChild(changeInternalStateNode);
        methodNode.appendChild(changeInternalStateByMethodInvocationNode);
        methodNode.appendChild(parametersNode);
        methodNode.appendChild(methodInternalInvocationsNode);
        methodNode.appendChild(methodInvocationsNode);



        return methodNode;
    }

    private Node createMethodNodeFromJavaInterface(JavaMethod javaMethod, Document myDoc) {


        Node methodNode = myDoc.createElement("method");

        Node methodName = myDoc.createElement("method_name");
        methodName.appendChild(myDoc.createTextNode(javaMethod.getName()));

        Node methodInternalIdNode = myDoc.createElement("method_internal_id");
        methodInternalIdNode.appendChild(myDoc.createTextNode(String.valueOf(javaMethod.getInternalID())));

        Node isFinalNode = myDoc.createElement("is_final");
        isFinalNode.appendChild(myDoc.createTextNode(String.valueOf(javaMethod.isFinal())));
        Node isPrivateNode = myDoc.createElement("is_private");
        isPrivateNode.appendChild(myDoc.createTextNode(String.valueOf(javaMethod.isPrivate())));
        Node isProtectedNode = myDoc.createElement("is_protected");
        isProtectedNode.appendChild(myDoc.createTextNode(String.valueOf(javaMethod.isProtected())));
        Node isPublicNode = myDoc.createElement("is_public");
        isPublicNode.appendChild(myDoc.createTextNode(String.valueOf(javaMethod.isPublic())));
        Node isStaticNode = myDoc.createElement("is_static");
        isStaticNode.appendChild(myDoc.createTextNode(String.valueOf(javaMethod.isStatic())));
        Node isAbstractNode = myDoc.createElement("is_abstract");
        isAbstractNode.appendChild(myDoc.createTextNode(String.valueOf(javaMethod.isAbstract())));
        Node isSynchronizedNode = myDoc.createElement("is_synchronized");
        isSynchronizedNode.appendChild(myDoc.createTextNode(String.valueOf(javaMethod.isSynchronized())));

        Node returnTypeNode = myDoc.createElement("return_type");
        String typeName = null;
        if (javaMethod.getReturnType().getClass() == JavaClass.class || javaMethod.getReturnType().getClass() == JavaInterface.class) {
            typeName = ((JavaAbstract) javaMethod.getReturnType()).getFullQualifiedName();
        } else {
            typeName = javaMethod.getReturnType().getName();
        }
        returnTypeNode.appendChild(myDoc.createTextNode(typeName));


        Node parametersNode = myDoc.createElement("parameters");
        for (Parameter parameter : javaMethod.getParameters()) {

            Node parameterNode = myDoc.createElement("parameter");

            typeName = null;
            if (parameter.getType().getClass() == JavaClass.class || parameter.getType().getClass() == JavaInterface.class) {
                typeName = ((JavaAbstract) parameter.getType()).getFullQualifiedName();
            } else {
                typeName = parameter.getType().getName();
            }

            Node parameterNameNode = myDoc.createElement("parameter_name");
            parameterNameNode.appendChild(myDoc.createTextNode(String.valueOf(parameter.getName())));

            Node parameterTypeNode = myDoc.createElement("parameter_type");
            parameterTypeNode.appendChild(myDoc.createTextNode(String.valueOf(typeName)));

            parameterNode.appendChild(parameterNameNode);
            parameterNode.appendChild(parameterTypeNode);
            parametersNode.appendChild(parameterNode);

        }




        methodNode.appendChild(methodName);
        methodNode.appendChild(methodInternalIdNode);
        methodNode.appendChild(isFinalNode);
        methodNode.appendChild(isPrivateNode);
        methodNode.appendChild(isProtectedNode);
        methodNode.appendChild(isPublicNode);
        methodNode.appendChild(isStaticNode);
        methodNode.appendChild(isAbstractNode);
        methodNode.appendChild(isSynchronizedNode);
        methodNode.appendChild(returnTypeNode);
        methodNode.appendChild(parametersNode);

        return methodNode;
    }

    private void saveXML(String fileXMLClass, Document myDoc) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            File file = new File(fileXMLClass);
            if (file.getParentFile() != null) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
            }
            if (!file.exists()) {
                //System.out.println("PAssou1");
                file.createNewFile();
            }
            //System.out.println("PAssou2");
            DOMSource source = new DOMSource(myDoc);
            StreamResult result = new StreamResult(file);

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(source, result);
        } catch (Exception e) {
            System.out.println("Erro save XML: " + e.getMessage());
        }
    }

    public JavaAbstract createJavaAbstractFromXMLFile(String fileXMLClass) {
        JavaAbstract javaAbstract = null;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();

            InputStream ips = new ByteArrayInputStream(readFile(fileXMLClass).getBytes());
            Document myDoc = builder.parse(ips);


            NodeList isInterfaceList = myDoc.getElementsByTagName("is_interface");
            Node isInterfaceNode = isInterfaceList.item(0);

            boolean isInterface = Boolean.parseBoolean(isInterfaceNode.getChildNodes().item(0).getNodeValue());
            Node nameNode = myDoc.getElementsByTagName("name").item(0);
            String name = nameNode.getChildNodes().item(0).getNodeValue();

            if (isInterface) {
                javaAbstract = new JavaInterface(fileXMLClass);
            } else {
                javaAbstract = new JavaClass(fileXMLClass);
            }

            javaAbstract.setName(name);


        } catch (ParserConfigurationException ex) {
            System.out.println("Erro createJavaAbstractFromXMLFile: " + ex.getMessage());
        } catch (SAXException ex) {
            System.out.println("Erro createJavaAbstractFromXMLFile: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Erro createJavaAbstractFromXMLFile: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Erro createJavaAbstractFromXMLFile: " + ex.getMessage());
        }


        return javaAbstract;
    }

    public String getPackageName(String fileXMLClass) {
        String packageName = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();

            InputStream ips = new ByteArrayInputStream(readFile(fileXMLClass).getBytes());
            Document myDoc = builder.parse(ips);


            NodeList packageList = myDoc.getElementsByTagName("package");
            Node packageNode = packageList.item(0);
            packageName = packageNode.getChildNodes().item(0).getNodeValue();

        } catch (ParserConfigurationException ex) {
            System.out.println("Erro getPackageName: " + ex.getMessage());
        } catch (SAXException ex) {
            System.out.println("Erro getPackageName: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Erro getPackageName: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Erro getPackageName: " + ex.getMessage());
        }

        return packageName;
    }

    public List<String> getImports(String fileXMLClass) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        List<String> importList = new ArrayList();
        try {
            builder = factory.newDocumentBuilder();

            InputStream ips = new ByteArrayInputStream(readFile(fileXMLClass).getBytes());
            Document myDoc = builder.parse(ips);


            NodeList importsNodeList = myDoc.getElementsByTagName("import");
            for (int i = 0; i < importsNodeList.getLength(); i++) {
                Node importNode = importsNodeList.item(i);
                importList.add(importNode.getChildNodes().item(0).getNodeValue());
            }

        } catch (ParserConfigurationException ex) {
            System.out.println("Erro getImports: " + ex.getMessage());
        } catch (SAXException ex) {
            System.out.println("Erro getImports: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Erro getImports: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Erro getImports: " + ex.getMessage());
        }

        return importList;
    }

    public String getSuperClass(String fileXMLClass) {
        String superClassName = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();

            InputStream ips = new ByteArrayInputStream(readFile(fileXMLClass).getBytes());
            Document myDoc = builder.parse(ips);


            NodeList superClassList = myDoc.getElementsByTagName("superclass");
            Node superClassNode = superClassList.item(0);
            if (superClassNode.getChildNodes().getLength() > 0) {
                superClassName = superClassNode.getChildNodes().item(0).getNodeValue();
            }

        } catch (ParserConfigurationException ex) {
            System.out.println("Erro getSuperClass: " + ex.getMessage());
        } catch (SAXException ex) {
            System.out.println("Erro getSuperClass: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Erro getSuperClass: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Erro getSuperClass: " + ex.getMessage());
        }

        return superClassName;
    }

    public List<String> getImplementedInterfaces(String fileXMLClass) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        List<String> implementedInterfacesList = new ArrayList();
        try {
            builder = factory.newDocumentBuilder();

            InputStream ips = new ByteArrayInputStream(readFile(fileXMLClass).getBytes());
            Document myDoc = builder.parse(ips);


            NodeList importsNodeList = myDoc.getElementsByTagName("implemented_interface");
            for (int i = 0; i < importsNodeList.getLength(); i++) {
                Node importNode = importsNodeList.item(i);
                implementedInterfacesList.add(importNode.getChildNodes().item(0).getNodeValue());
            }

        } catch (ParserConfigurationException ex) {
            System.out.println("Erro getImplementedInterfaces: " + ex.getMessage());
        } catch (SAXException ex) {
            System.out.println("Erro getImplementedInterfaces: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Erro getImplementedInterfaces: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Erro getImplementedInterfaces: " + ex.getMessage());
        }

        return implementedInterfacesList;
    }

    public List<ParameterAst> getAttributes(String fileXMLClass) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        List<ParameterAst> attributeList = new ArrayList();
        try {
            builder = factory.newDocumentBuilder();

            InputStream ips = new ByteArrayInputStream(readFile(fileXMLClass).getBytes());
            Document myDoc = builder.parse(ips);


            NodeList attributesNodeList = myDoc.getElementsByTagName("attribute");
            for (int i = 0; i < attributesNodeList.getLength(); i++) {
                Node attributeNode = attributesNodeList.item(i);
                String attributeName = null;
                String attributeType = null;
                boolean isFinal = false;
                boolean isPrivate = false;
                boolean isProtected = false;
                boolean isPublic = false;
                boolean isStatic = false;
                boolean isVolatile = false;

                //System.out.println("node Name: "+attributeNode.getNodeName()+"    node value "+attributeNode.getChildNodes().item(0).getNodeValue());
                //System.out.println("node Name: "+attributeNode.getNodeName());


                for (int j = 0; j < attributeNode.getChildNodes().getLength(); j++) {
                    if (attributeNode.getChildNodes().item(j).getNodeName().equals("attribute_name")) {
                        attributeName = attributeNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue();
                    } else if (attributeNode.getChildNodes().item(j).getNodeName().equals("attribute_type")) {
                        attributeType = attributeNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue();
                    } else if (attributeNode.getChildNodes().item(j).getNodeName().equals("is_final")) {
                        isFinal = Boolean.parseBoolean(attributeNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (attributeNode.getChildNodes().item(j).getNodeName().equals("is_private")) {
                        isPrivate = Boolean.parseBoolean(attributeNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (attributeNode.getChildNodes().item(j).getNodeName().equals("is_protected")) {
                        isProtected = Boolean.parseBoolean(attributeNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (attributeNode.getChildNodes().item(j).getNodeName().equals("is_public")) {
                        isPublic = Boolean.parseBoolean(attributeNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (attributeNode.getChildNodes().item(j).getNodeName().equals("is_static")) {
                        isStatic = Boolean.parseBoolean(attributeNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (attributeNode.getChildNodes().item(j).getNodeName().equals("is_volatile")) {
                        isVolatile = Boolean.parseBoolean(attributeNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    }
                    //System.out.println("node Name: "+attributeNode.getChildNodes().item(j).getNodeName()+"    node value "+attributeNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                }


                ParameterAst parameterAst = new ParameterAst(attributeName, attributeType);
                parameterAst.setFinal(isFinal);
                parameterAst.setPrivate(isPrivate);
                parameterAst.setProtected(isProtected);
                parameterAst.setPublic(isPublic);
                parameterAst.setStatic(isStatic);
                parameterAst.setVolatile(isVolatile);

                attributeList.add(parameterAst);
            }

        } catch (ParserConfigurationException ex) {
            System.out.println("Erro getAttributes: " + ex.getMessage());
        } catch (SAXException ex) {
            System.out.println("Erro getAttributes: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Erro getAttributes: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Erro getAttributes: " + ex.getMessage());
        }

        return attributeList;
    }

    public List<JavaMethodAstBox> getMethods(String fileXMLClass) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        List<JavaMethodAstBox> javaMethodList = new ArrayList();
        try {
            builder = factory.newDocumentBuilder();

            InputStream ips = new ByteArrayInputStream(readFile(fileXMLClass).getBytes());
            Document myDoc = builder.parse(ips);


            NodeList methodsNodeList = myDoc.getElementsByTagName("method");
            for (int i = 0; i < methodsNodeList.getLength(); i++) {
                Node methodNode = methodsNodeList.item(i);
                NodeList parameters = null;
                NodeList methodInternalInvocations = null;
                NodeList methodInvocations = null;
                String methodName = null;
                String methodReturnType = null;
                int methodInternalId = 0;
                int sizeInChars = 0;
                int cyclomaticComplexity = 0;
                boolean changeInternalState = false;
                boolean changeInternalStateByMethodInvocation = false;
                boolean isFinal = false;
                boolean isPrivate = false;
                boolean isProtected = false;
                boolean isPublic = false;
                boolean isStatic = false;
                boolean isAbstract = false;
                boolean isSynchronized = false;

                for (int j = 0; j < methodNode.getChildNodes().getLength(); j++) {
                    if (methodNode.getChildNodes().item(j).getNodeName().equals("method_name")) {
                        methodName = methodNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue();
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("return_type")) {
                        methodReturnType = methodNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue();
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("method_internal_id")) {
                        methodInternalId = Integer.valueOf(methodNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("is_final")) {
                        isFinal = Boolean.parseBoolean(methodNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("is_private")) {
                        isPrivate = Boolean.parseBoolean(methodNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("is_protected")) {
                        isProtected = Boolean.parseBoolean(methodNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("is_public")) {
                        isPublic = Boolean.parseBoolean(methodNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("is_static")) {
                        isStatic = Boolean.parseBoolean(methodNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("is_abstract")) {
                        isAbstract = Boolean.parseBoolean(methodNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("is_synchronized")) {
                        isSynchronized = Boolean.parseBoolean(methodNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("parameters")) {
                        parameters = methodNode.getChildNodes().item(j).getChildNodes();
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("size_in_chars")) {
                        sizeInChars = Integer.getInteger(methodNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("cyclomatic_complexity")) {
                        cyclomaticComplexity = Integer.getInteger(methodNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("change_internal_state")) {
                        changeInternalState = Boolean.parseBoolean(methodNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("change_internal_state_by_method_invocation")) {
                        changeInternalStateByMethodInvocation = Boolean.parseBoolean(methodNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("method_internal_invocations")) {
                        methodInternalInvocations = methodNode.getChildNodes().item(j).getChildNodes();
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("method_invocations")) {
                        methodInvocations = methodNode.getChildNodes().item(j).getChildNodes();
                    }
                }

                JavaMethodAstBox javaMethodAstBox = new JavaMethodAstBox(methodName, methodReturnType, null);
                javaMethodAstBox.setMethodInternalId(methodInternalId);
                javaMethodAstBox.setSizeInChars(sizeInChars);
                javaMethodAstBox.setCyclomaticComplexity(cyclomaticComplexity);
                javaMethodAstBox.setChangeInternalState(changeInternalState);
                javaMethodAstBox.setChangeInternalStateByMethodInvocation(changeInternalStateByMethodInvocation);
                javaMethodAstBox.setFinal(isFinal);
                javaMethodAstBox.setPrivate(isPrivate);
                javaMethodAstBox.setProtected(isProtected);
                javaMethodAstBox.setPublic(isPublic);
                javaMethodAstBox.setStatic(isStatic);
                javaMethodAstBox.setAbstract(isAbstract);
                javaMethodAstBox.setSynchronized(isSynchronized);

                List<ParameterAst> parametersAst = new ArrayList();
                for (int j = 0; j < parameters.getLength(); j++) {
                    Node parameter = parameters.item(j);
                    if (parameter.getNodeName().equals("parameter")) {
                        String parameterName = null;
                        String parameterType = null;
                        for (int k = 0; k < parameter.getChildNodes().getLength(); k++) {
                            if (parameter.getChildNodes().item(k).getNodeName().equals("parameter_name")) {
                                parameterName = parameter.getChildNodes().item(k).getChildNodes().item(0).getNodeValue();
                            } else if (parameter.getChildNodes().item(k).getNodeName().equals("parameter_type")) {
                                parameterType = parameter.getChildNodes().item(k).getChildNodes().item(0).getNodeValue();
                            }
                        }
                        ParameterAst parameterAst = new ParameterAst(parameterName, parameterType);
                        parametersAst.add(parameterAst);
                    }
                }

                javaMethodAstBox.setParameters(parametersAst);


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

        return javaMethodList;
    }

    public List<JavaMethodAstBox> getMethods(String fileXMLClass, boolean fromJavaClass) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        List<JavaMethodAstBox> javaMethodList = new ArrayList();
        try {
            builder = factory.newDocumentBuilder();

            InputStream ips = new ByteArrayInputStream(readFile(fileXMLClass).getBytes());
            Document myDoc = builder.parse(ips);


            NodeList methodsNodeList = myDoc.getElementsByTagName("method");
            for (int i = 0; i < methodsNodeList.getLength(); i++) {
                Node methodNode = methodsNodeList.item(i);
                NodeList parameters = null;
                NodeList methodInternalInvocationsNodeList = null;
                NodeList methodInvocationsNodeList = null;
                String methodName = null;
                String methodReturnType = null;
                int methodInternalId = 0;
                int sizeInChars = 0;
                int cyclomaticComplexity = 0;
                boolean changeInternalState = false;
                boolean changeInternalStateByMethodInvocation = false;
                boolean isFinal = false;
                boolean isPrivate = false;
                boolean isProtected = false;
                boolean isPublic = false;
                boolean isStatic = false;
                boolean isAbstract = false;
                boolean isSynchronized = false;
                for (int j = 0; j < methodNode.getChildNodes().getLength(); j++) {
                    if (methodNode.getChildNodes().item(j).getNodeName().equals("method_name")) {
                        methodName = methodNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue();
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("return_type")) {
                        methodReturnType = methodNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue();
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("method_internal_id")) {
                        methodInternalId = Integer.valueOf(methodNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("is_final")) {
                        isFinal = Boolean.parseBoolean(methodNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("is_private")) {
                        isPrivate = Boolean.parseBoolean(methodNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("is_protected")) {
                        isProtected = Boolean.parseBoolean(methodNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("is_public")) {
                        isPublic = Boolean.parseBoolean(methodNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("is_static")) {
                        isStatic = Boolean.parseBoolean(methodNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("is_abstract")) {
                        isAbstract = Boolean.parseBoolean(methodNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("is_synchronized")) {
                        isSynchronized = Boolean.parseBoolean(methodNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("parameters")) {
                        parameters = methodNode.getChildNodes().item(j).getChildNodes();
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("size_in_chars")) {
                        sizeInChars = Integer.parseInt(methodNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("cyclomatic_complexity")) {
                        cyclomaticComplexity = Integer.parseInt(methodNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("change_internal_state")) {
                        changeInternalState = Boolean.parseBoolean(methodNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("change_internal_state_by_method_invocation")) {
                        changeInternalStateByMethodInvocation = Boolean.parseBoolean(methodNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("method_internal_invocations")) {
                        methodInternalInvocationsNodeList = methodNode.getChildNodes().item(j).getChildNodes();
                    } else if (methodNode.getChildNodes().item(j).getNodeName().equals("method_invocations")) {
                        methodInvocationsNodeList = methodNode.getChildNodes().item(j).getChildNodes();
                    }
                }

                JavaMethodAstBox javaMethodAstBox = new JavaMethodAstBox(methodName, methodReturnType, null);
                javaMethodAstBox.setMethodInternalId(methodInternalId);
                javaMethodAstBox.setFinal(isFinal);
                javaMethodAstBox.setPrivate(isPrivate);
                javaMethodAstBox.setProtected(isProtected);
                javaMethodAstBox.setPublic(isPublic);
                javaMethodAstBox.setStatic(isStatic);
                javaMethodAstBox.setAbstract(isAbstract);
                javaMethodAstBox.setSynchronized(isSynchronized);

                List<ParameterAst> parametersAst = new ArrayList();
                for (int j = 0; j < parameters.getLength(); j++) {
                    Node parameter = parameters.item(j);
                    if (parameter.getNodeName().equals("parameter")) {
                        String parameterName = null;
                        String parameterType = null;
                        for (int k = 0; k < parameter.getChildNodes().getLength(); k++) {
                            if (parameter.getChildNodes().item(k).getNodeName().equals("parameter_name")) {
                                parameterName = parameter.getChildNodes().item(k).getChildNodes().item(0).getNodeValue();
                            } else if (parameter.getChildNodes().item(k).getNodeName().equals("parameter_type")) {
                                parameterType = parameter.getChildNodes().item(k).getChildNodes().item(0).getNodeValue();
                            }
                        }
                        ParameterAst parameterAst = new ParameterAst(parameterName, parameterType);
                        parametersAst.add(parameterAst);
                    }
                }

                javaMethodAstBox.setParameters(parametersAst);

                if (fromJavaClass) {

                    javaMethodAstBox.setSizeInChars(sizeInChars);
                    javaMethodAstBox.setCyclomaticComplexity(cyclomaticComplexity);
                    javaMethodAstBox.setChangeInternalState(changeInternalState);
                    javaMethodAstBox.setChangeInternalStateByMethodInvocation(changeInternalStateByMethodInvocation);




                    List<String> methodInternalInvocantions = new ArrayList();
                    for (int j = 0; j < methodInternalInvocationsNodeList.getLength(); j++) {
                        Node methodInternalInvocation = methodInternalInvocationsNodeList.item(j);
                        if (methodInternalInvocation.getNodeName().equals("method_internal_invocation_id")) {
                            String methodInternalInvocationId = methodInternalInvocation.getChildNodes().item(0).getNodeValue();
                            methodInternalInvocantions.add(methodInternalInvocationId);
                        }
                    }

                    javaMethodAstBox.setMethodInternalInvocations(methodInternalInvocantions);

                    List<String> methodInvocantions = new ArrayList();
                    for (int j = 0; j < methodInvocationsNodeList.getLength(); j++) {
                        Node methodInvocation = methodInvocationsNodeList.item(j);
                        if (methodInvocation.getNodeName().equals("method_invocation")) {
                            String methodInternalStr = methodInvocation.getChildNodes().item(0).getNodeValue();
                            methodInvocantions.add(methodInternalStr);
                        }
                    }

                    javaMethodAstBox.setMethodInvocations(methodInvocantions);
                }

                javaMethodList.add(javaMethodAstBox);
            }

        } catch (ParserConfigurationException ex) {
            System.out.println("Erro getMethods: " + ex.getMessage());
        } catch (SAXException ex) {
            System.out.println("Erro getMethods: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Erro getMethods: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Erro getMethods: " + ex.getMessage());
        }

        return javaMethodList;
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
}
