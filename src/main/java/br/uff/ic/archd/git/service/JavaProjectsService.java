/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.git.service;

import br.uff.ic.archd.model.Project;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
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
public class JavaProjectsService {

    private static final String PROJECT_XML_FILE = System.getProperty("user.home") + "/.archd/.projects.xml";
    private Document myDoc;

    public JavaProjectsService() {
        getProjects();
        
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

    public void addProject(int projectId, String projectName, String projectPath, List<String> codeDirs) {
        NodeList projectsList = myDoc.getElementsByTagName("projects");
        Node node = myDoc.createElement("project");
        Node projectNameNode = myDoc.createElement("project_name");
        Node projectPathNode = myDoc.createElement("project_path");
        Node projectIdNode = myDoc.createElement("project_id");
        projectNameNode.appendChild(myDoc.createTextNode(projectName));
        projectPathNode.appendChild(myDoc.createTextNode(projectPath));
        projectIdNode.appendChild(myDoc.createTextNode(String.valueOf(projectId)));
        Node codeDirsNode = myDoc.createElement("code_dirs");
        for(String codeDir : codeDirs){
            Node codeDirNode = myDoc.createElement("code_dir");
            codeDirNode.appendChild(myDoc.createTextNode(codeDir));
            codeDirsNode.appendChild(codeDirNode);
        }
        
        node.appendChild(projectNameNode);
        node.appendChild(projectPathNode);
        node.appendChild(projectIdNode);
        node.appendChild(codeDirsNode);
        projectsList.item(0).appendChild(node);
        saveXML();
    }

    private void saveXML() {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            File file = new File(PROJECT_XML_FILE);
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

    public List<Project> getProjects() {
        String superClassName = null;
        List<Project> projects = new LinkedList();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();

            if (new File(PROJECT_XML_FILE).exists()) {
                if(myDoc == null){
                    InputStream ips = new ByteArrayInputStream(readFile(PROJECT_XML_FILE).getBytes());
                    myDoc = builder.parse(ips);
                }


                NodeList projectsList = myDoc.getElementsByTagName("project");
                if (projectsList.getLength() != 0) {
                    for (int i = 0; i < projectsList.getLength(); i++) {
                        Node projectNode = projectsList.item(i);
                        List<String> codeDirs = new LinkedList();
                        String projectName = null;
                        String projectPath = null;
                        int projectId = 0;
                        for (int j = 0; j < projectNode.getChildNodes().getLength(); j++) {
                            if (projectNode.getChildNodes().item(j).getNodeName().equals("project_name")) {
                                projectName = projectNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue();
                            } else if (projectNode.getChildNodes().item(j).getNodeName().equals("project_path")) {
                                projectPath = projectNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue();
                            } else if (projectNode.getChildNodes().item(j).getNodeName().equals("project_id")) {
                                projectId = Integer.valueOf(projectNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
                            } else if(projectNode.getChildNodes().item(j).getNodeName().equals("code_dirs")){
                                Node dirsNode = projectNode.getChildNodes().item(j);
                                for(int k = 0; k < dirsNode.getChildNodes().getLength(); k++){
                                    if (dirsNode.getChildNodes().item(k).getNodeName().equals("code_dir")) {
                                        codeDirs.add(dirsNode.getChildNodes().item(k).getChildNodes().item(0).getNodeValue());
                                    }
                                }
                            }
                        }
                        Project project = new Project(projectId, projectPath, projectName, codeDirs);
                        projects.add(project);
                    }
                }

            }else{
                myDoc = builder.newDocument();
                Node node = myDoc.createElement("projects");
                myDoc.appendChild(node);
                saveXML();
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

        return projects;
    }
}
