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
import br.uff.ic.archd.javacode.JavaPackage;
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
public class GraphPackageCreator {

    public void createGraph(JavaProject javaProject) {
        Graph<String, String> g;
        g = new DirectedSparseMultigraph();
        for (JavaPackage javaPackage : javaProject.getPackages()) {
            g.addVertex(javaPackage.getName());

        }
        int i = 0;
        for (JavaPackage javaPackage : javaProject.getPackages()) {
            for(JavaPackage clientPackage : javaPackage.getClientPackages()){
                String edgeName = clientPackage.getName() + " -> " + javaPackage.getName();
                g.addEdge(edgeName, clientPackage.getName(), javaPackage.getName());
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
        double averageCohesion = 0;
        double averageCC = 0;
        double averageCP = 0;
        double averagePS = 0;
        for (JavaPackage javaPackage : javaProject.getPackages()) {
            g.addVertex(javaPackage.getName()+" : "+javaPackage.getOnlyClasses().size());
            System.out.println("pakcage: "+javaPackage.getName()+"    Cohesion: "+javaPackage.getPackageCohesion()+"    NOCC: "+javaPackage.getClientClasses().size()+"    NOCP: "+javaPackage.getClientPackages().size()
                    +"  package size: "+javaPackage.getOnlyClasses().size());
            averageCohesion = averageCohesion + javaPackage.getPackageCohesion();
            averageCC = averageCC + javaPackage.getClientClasses().size();
            averageCP = averageCP + javaPackage.getClientPackages().size();
            averagePS = averagePS + javaPackage.getOnlyClasses().size();

        }
        averageCohesion = averageCohesion/javaProject.getPackages().size();
        averageCC = averageCC/javaProject.getPackages().size();
        averageCP = averageCP/javaProject.getPackages().size();
        averagePS = averagePS/javaProject.getPackages().size();
        
        System.out.println("AVERAGE COHESION: "+averageCohesion);
        System.out.println("AVERAGE CC: "+averageCC);
        System.out.println("AVERAGE CP: "+averageCP);
        System.out.println("AVERAGE PS: "+averagePS);
        
        System.out.println("CLIENT CLASSES | CLIENT PACKAGES");
        for (JavaPackage javaPackage : javaProject.getPackages()) {
            System.out.println(javaPackage.getClientClasses().size()+"    "+javaPackage.getClientPackages().size());

        }
        System.out.println("CLIENT CLASSES ");
        for (JavaPackage javaPackage : javaProject.getPackages()) {
            System.out.println(javaPackage.getClientClasses().size());

        }
        System.out.println("CLIENT PACKAGES");
        for (JavaPackage javaPackage : javaProject.getPackages()) {
            System.out.println(javaPackage.getClientPackages().size());

        }
        int i = 0;
        for (JavaPackage javaPackage : javaProject.getPackages()) {
            for(JavaPackage clientPackage : javaPackage.getClientPackages()){
                i++;
                g.addEdge(String.valueOf(i), clientPackage.getName(), javaPackage.getName()+" : "+javaPackage.getOnlyClasses().size());
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

        JFrame frame = new JFrame(javaProject.getName());
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
        taxaSimplesGraph(javaProject);
        taxaSimplesClientClassesPackage(javaProject);

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

        int utilizacaoTotalPacotes = 0;
        int utilizadoTotalPacotes = 0;
        int numberPacotes = javaProject.getPackages().size();
        HashMap<String, Set<String>> hashUtilizacao = new HashMap();
        HashMap<String, Set<String>> hashUtilizado = new HashMap();
        for (JavaPackage javaPackage : javaProject.getPackages()) {
            Set<String> set = hashUtilizacao.get(javaPackage.getName());
            if(set == null){
                set = new HashSet();
                hashUtilizacao.put(javaPackage.getName(), set);
            }
            set = hashUtilizado.get(javaPackage.getName());
            if(set == null){
                set = new HashSet();
                hashUtilizado.put(javaPackage.getName(), set);
            }
        }
        for (JavaPackage javaPackage : javaProject.getPackages()) {
            Set<String> set = hashUtilizado.get(javaPackage.getName());
            if(set == null){
                set = new HashSet();
                hashUtilizado.put(javaPackage.getName(), set);
            }
            for(JavaPackage clientPackage : javaPackage.getClientPackages()){
                set.add(clientPackage.getName());
                Set<String> set2 = hashUtilizacao.get(clientPackage.getName());
                if(set2 == null){
                    set2 = new HashSet();
                    hashUtilizacao.put(clientPackage.getName(), set2);
                }
                set2.add(javaPackage.getName());
            }

        }
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
            utilizacaoTotalPacotes = utilizacaoTotalPacotes + set.size();

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
            utilizadoTotalPacotes = utilizadoTotalPacotes + set.size();

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
        System.out.println("Numero de pacotes: " + numberPacotes);
        System.out.println("Numero media de utilizacao de outros pacotes por cada pacote: " + (((double) utilizacaoTotalPacotes) / numberPacotes));
        System.out.println("Numero media  de utilizacao do pacote por outros pacotes: " + (((double) utilizadoTotalPacotes) / numberPacotes));

        double media = ((double) utilizadoTotalPacotes) / numberPacotes;
        System.out.println("Faixa de utilizacao de outros pacotes por cada pacote:");
        for (int index = 0; index < listUtilizacoes.size(); index++) {
            Integer num = hashDeNumeroDeUtilizacoes.get(listUtilizacoes.get(index));
            System.out.println("Utilizando " + listUtilizacoes.get(index) + " pacotes     -  numero de pacotes: " + num);
        }

        System.out.println("Faixa de utilizacao da pacotes por outros pacotes:");
        //System.out.println("Utilizado por 0 pacotes     -  numero de pacotes: " + (numberPacotes - hashUtilizado.size()));
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
            System.out.println("Utilizado por " + listUtilizados.get(index) + " pacotes     -  numero de pacotes: " + num);

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
        System.out.println("DESVIO PADRAO DO PACOTE: " + desvioPadrao);

        System.out.println("PORCENTAGEM:   MENOR QUE 5 pacotes: " + (((double) totalUtilizadoMenor) / totalUtilizado) + "   ---  MAIOR QUE 5 pacotes " + (((double) totalUtilizadoMaior) / totalUtilizado));

        System.out.println("Faixa de Diff (positivo utiliza mais chamadas do que é chamado):");
        for (int index = 0; index < listDiff.size(); index++) {
            Integer num = hashMapDiff.get(listDiff.get(index));
            System.out.println("Diff " + listDiff.get(index) + " pacotes     -  numero de pacotes: " + num);
        }

    }
    
    public void taxaSimplesClientClassesPackage(JavaProject javaProject) {
        List<Integer> lista = new LinkedList();
        double somatoria = 0;
        for (JavaPackage javaPackage : javaProject.getPackages()) {
            lista.add(javaPackage.getClientClasses().size());
            somatoria = somatoria + javaPackage.getClientClasses().size();
        }
        double average = somatoria/lista.size();
        somatoria = 0;
        for (Integer num : lista) {

            somatoria = somatoria + Math.pow(((double) num) - average, 2);
        }
        double multiplicador = 1;
        multiplicador = multiplicador / (lista.size() - 1);
        System.out.println("SOMATORIA: "+somatoria);
        double desvioPadrao = somatoria * multiplicador;
        desvioPadrao = Math.sqrt(desvioPadrao);
        System.out.println("MEDIA DE CLIENTES DE CLASSES DE PACOTES: " + average);
        System.out.println("DESVIO PADRAO DO PROJETO POR CLIENTES DE CLASSES DE PACOTES: " + desvioPadrao);
    }
    
    public void taxaSimplesClientClassesGraph(JavaProject javaProject) {

        int utilizacaoTotalPacotes = 0;
        int utilizadoTotalPacotes = 0;
        int numberPacotes = javaProject.getPackages().size();
        HashMap<String, Set<String>> hashUtilizacao = new HashMap();
        HashMap<String, Set<String>> hashUtilizado = new HashMap();
        for (JavaPackage javaPackage : javaProject.getPackages()) {
            Set<String> set = hashUtilizacao.get(javaPackage.getName());
            if(set == null){
                set = new HashSet();
                hashUtilizacao.put(javaPackage.getName(), set);
            }
            set = hashUtilizado.get(javaPackage.getName());
            if(set == null){
                set = new HashSet();
                hashUtilizado.put(javaPackage.getName(), set);
            }
        }
        for (JavaPackage javaPackage : javaProject.getPackages()) {
            Set<String> set = hashUtilizado.get(javaPackage.getName());
            if(set == null){
                set = new HashSet();
                hashUtilizado.put(javaPackage.getName(), set);
            }
            for(JavaPackage clientPackage : javaPackage.getClientPackages()){
                set.add(clientPackage.getName());
                Set<String> set2 = hashUtilizacao.get(clientPackage.getName());
                if(set2 == null){
                    set2 = new HashSet();
                    hashUtilizacao.put(clientPackage.getName(), set2);
                }
                set2.add(javaPackage.getName());
            }

        }
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
            utilizacaoTotalPacotes = utilizacaoTotalPacotes + set.size();

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
            utilizadoTotalPacotes = utilizadoTotalPacotes + set.size();

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
        System.out.println("Numero de pacotes: " + numberPacotes);
        System.out.println("Numero media de utilizacao de outros pacotes por cada pacote: " + (((double) utilizacaoTotalPacotes) / numberPacotes));
        System.out.println("Numero media  de utilizacao do pacote por outros pacotes: " + (((double) utilizadoTotalPacotes) / numberPacotes));

        double media = ((double) utilizadoTotalPacotes) / numberPacotes;
        System.out.println("Faixa de utilizacao de outros pacotes por cada pacote:");
        for (int index = 0; index < listUtilizacoes.size(); index++) {
            Integer num = hashDeNumeroDeUtilizacoes.get(listUtilizacoes.get(index));
            System.out.println("Utilizando " + listUtilizacoes.get(index) + " pacotes     -  numero de pacotes: " + num);
        }

        System.out.println("Faixa de utilizacao da pacotes por outros pacotes:");
        //System.out.println("Utilizado por 0 pacotes     -  numero de pacotes: " + (numberPacotes - hashUtilizado.size()));
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
            System.out.println("Utilizado por " + listUtilizados.get(index) + " pacotes     -  numero de pacotes: " + num);

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
        System.out.println("DESVIO PADRAO DO PACOTE: " + desvioPadrao);

        System.out.println("PORCENTAGEM:   MENOR QUE 5 pacotes: " + (((double) totalUtilizadoMenor) / totalUtilizado) + "   ---  MAIOR QUE 5 pacotes " + (((double) totalUtilizadoMaior) / totalUtilizado));

        System.out.println("Faixa de Diff (positivo utiliza mais chamadas do que é chamado):");
        for (int index = 0; index < listDiff.size(); index++) {
            Integer num = hashMapDiff.get(listDiff.get(index));
            System.out.println("Diff " + listDiff.get(index) + " pacotes     -  numero de pacotes: " + num);
        }

    }

    public static void main(String args[]) {

    }
}
