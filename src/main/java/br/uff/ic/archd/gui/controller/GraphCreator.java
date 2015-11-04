/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.gui.controller;

import br.uff.ic.archd.javacode.JavaAbstract;
import br.uff.ic.archd.javacode.JavaClass;
import br.uff.ic.archd.javacode.JavaMethod;
import br.uff.ic.archd.javacode.JavaMethodInvocation;
import br.uff.ic.archd.javacode.JavaProject;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.swing.JFrame;

/**
 *
 * @author wallace
 */
public class GraphCreator {

    public void createGraph(JavaProject javaProject) {
        Graph<String, String> g;
        g = new DirectedSparseMultigraph();
        for (JavaAbstract javaAbstract : javaProject.getClasses()) {
            JavaClass javaClass = (JavaClass) javaAbstract;
            g.addVertex(javaClass.getFullQualifiedName());

        }
        int i = 0;
        for (JavaAbstract javaAbstract : javaProject.getClasses()) {
            JavaClass javaClass = (JavaClass) javaAbstract;
            for (JavaMethod javaMethod : javaClass.getMethods()) {
                for (JavaMethodInvocation javaMethodInvocation : javaMethod.getMethodInvocations()) {
                    if (javaMethodInvocation.getJavaAbstract() != null && javaMethodInvocation.getJavaAbstract().getClass() == JavaClass.class) {
                        i++;
                        String edgeName = javaMethod.getName() + " -> " + (javaMethodInvocation.getJavaMethod() == null ? javaMethodInvocation.getUnknowMethodName() : javaMethodInvocation.getJavaMethod().getName()) + "  - (" + i + ")";
                        g.addEdge(edgeName, javaClass.getFullQualifiedName(), javaMethodInvocation.getJavaAbstract().getFullQualifiedName());
                    }
                }
            }

        }

        Layout<String, String> layout = new CircleLayout(g);
        layout.setSize(new Dimension(1000, 700));
        VisualizationViewer<String, String> vv
                = new VisualizationViewer<String, String>(layout);
        vv.setPreferredSize(new Dimension(1000, 700));

        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        //vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

        DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
        //gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        gm.setMode(ModalGraphMouse.Mode.PICKING);
        vv.setGraphMouse(gm);

        JFrame frame = new JFrame("Simple Graph View");
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);

    }

    public void createSimpleGraph(JavaProject javaProject) {
        Graph<String, String> g;
        g = new DirectedSparseMultigraph();
        for (JavaAbstract javaAbstract : javaProject.getClasses()) {
            JavaClass javaClass = (JavaClass) javaAbstract;
            g.addVertex(javaClass.getFullQualifiedName());

        }
        int i = 0;
        for (JavaAbstract javaAbstract : javaProject.getClasses()) {
            JavaClass javaClass = (JavaClass) javaAbstract;
            HashMap<String, String> hash = new HashMap();
            for (JavaMethod javaMethod : javaClass.getMethods()) {
                for (JavaMethodInvocation javaMethodInvocation : javaMethod.getMethodInvocations()) {
                    if (javaMethodInvocation.getJavaAbstract() != null && javaMethodInvocation.getJavaAbstract().getClass() == JavaClass.class) {
                        hash.put(javaMethodInvocation.getJavaAbstract().getFullQualifiedName(), javaMethodInvocation.getJavaAbstract().getFullQualifiedName());
                    }
                }
            }
            Set<String> set = hash.keySet();
            Iterator<String> it = set.iterator();
            while (it.hasNext()) {
                i++;
                g.addEdge(String.valueOf(i), javaClass.getFullQualifiedName(), it.next());
            }

        }

        Layout<String, String> layout = new CircleLayout(g);
        layout.setSize(new Dimension(1000, 700));
        VisualizationViewer<String, String> vv
                = new VisualizationViewer<String, String>(layout);
        vv.setPreferredSize(new Dimension(1000, 700));

        //vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        //vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

        DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
        //gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        gm.setMode(ModalGraphMouse.Mode.PICKING);
        vv.setGraphMouse(gm);

        JFrame frame = new JFrame(javaProject.getName());
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
        taxaSimplesGraph(javaProject);

    }

//    public void createSimpleGraph(String filePath) {
//        Graph<String, String> g;
//        g = new DirectedSparseMultigraph();
//        FileBuffereader fib 
//        for (JavaAbstract javaAbstract : javaProject.getClasses()) {
//            JavaClass javaClass = (JavaClass) javaAbstract;
//            g.addVertex(javaClass.getFullQualifiedName());
//
//        }
//        int i = 0;
//        for (JavaAbstract javaAbstract : javaProject.getClasses()) {
//            JavaClass javaClass = (JavaClass) javaAbstract;
//            HashMap<String, String> hash = new HashMap();
//            for (JavaMethod javaMethod : javaClass.getMethods()) {
//                for (JavaMethodInvocation javaMethodInvocation : javaMethod.getMethodInvocations()) {
//                    if (javaMethodInvocation.getJavaAbstract() != null && javaMethodInvocation.getJavaAbstract().getClass() == JavaClass.class) {
//                        hash.put(javaMethodInvocation.getJavaAbstract().getFullQualifiedName(), javaMethodInvocation.getJavaAbstract().getFullQualifiedName());
//                    }
//                }
//            }
//            Set<String> set = hash.keySet();
//            Iterator<String> it = set.iterator();
//            while (it.hasNext()) {
//                i++;
//                g.addEdge(String.valueOf(i), javaClass.getFullQualifiedName(), it.next());
//            }
//
//        }
//
//        Layout<String, String> layout = new CircleLayout(g);
//        layout.setSize(new Dimension(1000, 700));
//        VisualizationViewer<String, String> vv
//                = new VisualizationViewer<String, String>(layout);
//        vv.setPreferredSize(new Dimension(1000, 700));
//
//        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
//        //vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
//
//        DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
//        //gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
//        gm.setMode(ModalGraphMouse.Mode.PICKING);
//        vv.setGraphMouse(gm);
//
//        JFrame frame = new JFrame(javaProject.getName());
//        frame.getContentPane().add(vv);
//        frame.pack();
//        frame.setVisible(true);
//        taxaSimplesGraph(javaProject);
//
//    }
    public void taxaSimplesGraph(JavaProject javaProject) {

        int utilizacaoTotalClasses = 0;
        int utilizadoTotalClasses = 0;
        int numberClasses = javaProject.getClasses().size();
        HashMap<String, Set<String>> hashUtilizacao = new HashMap();
        HashMap<String, Set<String>> hashUtilizado = new HashMap();
        for (JavaAbstract javaAbstract : javaProject.getClasses()) {
            JavaClass javaClass = (JavaClass) javaAbstract;
            //HashMap<String, String> hash = new HashMap();
            Set<String> setUtilizacao = hashUtilizacao.get(javaClass.getFullQualifiedName());
            if (setUtilizacao == null) {
                setUtilizacao = new HashSet();
                hashUtilizacao.put(javaClass.getFullQualifiedName(), setUtilizacao);

            }
            for (JavaMethod javaMethod : javaClass.getMethods()) {

                for (JavaMethodInvocation javaMethodInvocation : javaMethod.getMethodInvocations()) {
                    if (javaMethodInvocation.getJavaAbstract() != null && javaMethodInvocation.getJavaAbstract().getClass() == JavaClass.class) {
                        Set<String> setUtilizado = hashUtilizado.get(javaMethodInvocation.getJavaAbstract().getFullQualifiedName());
                        if (setUtilizado == null) {
                            setUtilizado = new HashSet();
                            hashUtilizado.put(javaMethodInvocation.getJavaAbstract().getFullQualifiedName(), setUtilizado);
                        }
                        setUtilizado.add(javaClass.getFullQualifiedName());
                        setUtilizacao.add(javaMethodInvocation.getJavaAbstract().getFullQualifiedName());
                    }
                }
            }
//            Set<String> set = hash.keySet();
//            Iterator<String> it = set.iterator();
//            while(it.hasNext()){
//                i++;
//                g.addEdge(String.valueOf(i), javaClass.getFullQualifiedName(), it.next());
//            }

        }
        System.out.println("ACCES TO FOREIGN DATA:");
        for (JavaAbstract javaAbstract : javaProject.getClasses()) {
            JavaClass jc = (JavaClass) javaAbstract;
            System.out.print(jc.getAccessToForeignDataNumber()+" ");
        }
        System.out.println("\nWEIGHT METHOD COUNT:");
        for (JavaAbstract javaAbstract : javaProject.getClasses()) {
            JavaClass jc = (JavaClass) javaAbstract;
            int weigth = 0;
            for(JavaMethod jm : jc.getMethods()){
                weigth = weigth + jm.getCyclomaticComplexity();
            }
            System.out.print(weigth+" ");
        }
        System.out.println("\nCLASS COHESION:");
        for (JavaAbstract javaAbstract : javaProject.getClasses()) {
            JavaClass jc = (JavaClass) javaAbstract;
            double tcc = jc.getNumberOfDirectConnections();
            int n = jc.getMethods().size();
            tcc = tcc / ((n * (n - 1)) / 2);
            System.out.print(tcc+" ");
        }
        System.out.println("\nNUMEROS DE ATRIBUTOS:");
        for (JavaAbstract javaAbstract : javaProject.getClasses()) {
            JavaClass jc = (JavaClass) javaAbstract;
            System.out.print(jc.getAttributes().size()+" ");
        }
        System.out.println("\nNUMERO DE METODOS:");
        for (JavaAbstract javaAbstract : javaProject.getClasses()) {
            JavaClass jc = (JavaClass) javaAbstract;
            System.out.print(jc.getMethods().size()+" ");
        }
        System.out.println("");
        HashMap<Integer, Integer> hashDeNumeroDeUtilizacoes = new HashMap();
        HashMap<Integer, Integer> hashDeNumeroDeUtilizados = new HashMap();
        HashMap<Integer, Integer> hashMapDiff = new HashMap();

        List<Integer> listUtilizacoes = new LinkedList();
        List<Integer> listUtilizados = new LinkedList();
        List<Integer> listDiff = new LinkedList();

        Set<String> keys = hashUtilizacao.keySet();
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            Set<String> set = hashUtilizacao.get(key);
            Integer num = hashDeNumeroDeUtilizacoes.get(set.size());
            if (num == null) {
                num = 0;
            }
            num++;
            hashDeNumeroDeUtilizacoes.put(set.size(), num);
            utilizacaoTotalClasses = utilizacaoTotalClasses + set.size();

            Set<String> auxSet = hashUtilizado.get(key);
            int aux = 0;
            if (auxSet != null) {
                aux = auxSet.size();
            }
            int diff = set.size() - aux;

            num = hashMapDiff.get(diff);
            if (num == null) {
                num = 0;
            }
            num++;
            hashMapDiff.put(diff, num);

            //ordenando
            if (!listUtilizacoes.contains(set.size())) {
                boolean inseriu = false;
                for (int index = 0; index < listUtilizacoes.size(); index++) {
                    if (listUtilizacoes.get(index) > set.size()) {
                        inseriu = true;
                        listUtilizacoes.add(index, set.size());
                        break;
                    }
                }
                if (!inseriu) {
                    listUtilizacoes.add(set.size());

                }
            }

            if (!listDiff.contains(diff)) {
                boolean inseriu = false;
                for (int index = 0; index < listDiff.size(); index++) {
                    if (listDiff.get(index) > diff) {
                        inseriu = true;
                        listDiff.add(index, diff);
                        break;
                    }
                }
                if (!inseriu) {
                    listDiff.add(diff);

                }
            }

        }
        //utilizados
        keys = hashUtilizado.keySet();
        it = keys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            Set<String> set = hashUtilizado.get(key);
            Integer num = hashDeNumeroDeUtilizados.get(set.size());
            if (num == null) {
                num = 0;
            }
            num++;
            hashDeNumeroDeUtilizados.put(set.size(), num);
            utilizadoTotalClasses = utilizadoTotalClasses + set.size();

            //ordenando
            if (!listUtilizados.contains(set.size())) {
                boolean inseriu = false;
                for (int index = 0; index < listUtilizados.size(); index++) {
                    if (listUtilizados.get(index) > set.size()) {
                        inseriu = true;
                        listUtilizados.add(index, set.size());
                        break;
                    }
                }
                if (!inseriu) {
                    listUtilizados.add(set.size());

                }
            }
        }

        System.out.println("***********************************");
        System.out.println("Numero de classes: " + numberClasses);
        System.out.println("Numero media de utilizacao de outras classes por cada classe: " + (((double) utilizacaoTotalClasses) / numberClasses));
        System.out.println("Numero media  de utilizacao da classe por outras classes: " + (((double) utilizadoTotalClasses) / numberClasses));

        double media = ((double) utilizadoTotalClasses) / numberClasses;
        System.out.println("Faixa de utilizacao de outras classes por cada classe:");
        for (int index = 0; index < listUtilizacoes.size(); index++) {
            Integer num = hashDeNumeroDeUtilizacoes.get(listUtilizacoes.get(index));
            System.out.println("Utilizando " + listUtilizacoes.get(index) + " classes     -  numero de classes: " + num);
        }

        System.out.println("Faixa de utilizacao da classe por outras classes:");
        System.out.println("Utilizado por 0 classes     -  numero de classes: " + (numberClasses - hashUtilizado.size()));
        int totalUtilizado = 0;
        int totalUtilizadoMenor = 0;
        int totalUtilizadoMaior = 0;

        int n = 0;
        
        
//        media = 0;
//        for (int index = 0; index < listUtilizados.size(); index++) {
//            Integer num = hashDeNumeroDeUtilizados.get(listUtilizados.get(index));
//            if (listUtilizados.get(index) != 0) {
//                media = media + (listUtilizados.get(index)*num);
//                n = n + num;
//            }
//        }
//        media = media / n;
//        System.out.println("MEDIA ACIMA DE 0: "+media+"   numero de classes: "+n);        
        
        

        double somatoria = 0;
        n = 0;
        for (int index = 0; index < listUtilizados.size(); index++) {
            Integer num = hashDeNumeroDeUtilizados.get(listUtilizados.get(index));
            System.out.println("Utilizado por " + listUtilizados.get(index) + " classes     -  numero de classes: " + num);

//            if (listUtilizados.get(index) != 0) {
                n = n + num;
                somatoria = somatoria + ((Math.pow(((double) listUtilizados.get(index)) - media, 2)) * num);
//            }

            totalUtilizado = totalUtilizado + num;
            if (listUtilizados.get(index) <= 5) {
                totalUtilizadoMenor = totalUtilizadoMenor + num;
            } else {
                totalUtilizadoMaior = totalUtilizadoMaior + num;
            }
        }
        double multiplicador = 1;
        multiplicador = multiplicador / (n - 1);
        double desvioPadrao = somatoria * multiplicador;
        desvioPadrao = Math.sqrt(desvioPadrao);
        System.out.println("DESVIO PADRAO: " + desvioPadrao);

        System.out.println("PORCENTAGEM:   MENOR QUE 5 classes: " + (((double) totalUtilizadoMenor) / totalUtilizado) + "   ---  MAIOR QUE 5 classes " + (((double) totalUtilizadoMaior) / totalUtilizado));

        System.out.println("Faixa de Diff (positivo utiliza mais chamadas do que é chamado):");
        for (int index = 0; index < listDiff.size(); index++) {
            Integer num = hashMapDiff.get(listDiff.get(index));
            System.out.println("Diff " + listDiff.get(index) + " classes     -  numero de classes: " + num);
        }

    }

//    public static void main(String args[]) {
//
//    }
}
