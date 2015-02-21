/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.gui.view;

import br.uff.ic.archd.service.mining.AnomalieList;
import br.uff.ic.archd.service.mining.GenericAnomalies;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.SubCategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;

/**
 *
 * @author wallace
 */
public class AnomalieChart extends JPanel {

    public AnomalieChart(GenericAnomalies genericAnomalies, String anomalieName) {
        final CategoryDataset dataset = createDataset(genericAnomalies, anomalieName);
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1500, 500));
        this.add(chartPanel, BorderLayout.CENTER);
    }

    private CategoryDataset createDataset(GenericAnomalies genericAnomalies, String anomalieName) {
        DefaultCategoryDataset result = new DefaultCategoryDataset();

        if (anomalieName.equals("ALL ANOMALIES")) {
            List<String> anomaliesNames = genericAnomalies.getAnomalies();
            for (String str : anomaliesNames) {
                AnomalieList anomalieList = genericAnomalies.getAnomalieList(str);
                List<Boolean> list = anomalieList.getList();
                for (int i = 0; i < list.size(); i++) {
                    Boolean b = list.get(i);
                    int value = 0;
                    if (b) {
                        value = 1;
                    }
                    result.addValue(value, str, String.valueOf(i+anomalieList.getRevisionBirthNumber()));
                }
            }
        } else {
            AnomalieList anomalieList = genericAnomalies.getAnomalieList(anomalieName);
            List<Boolean> list = anomalieList.getList();
            for (int i = 0; i < list.size(); i++) {
                Boolean b = list.get(i);
                int value = 0;
                if (b) {
                    value = 1;
                }
                result.addValue(value, anomalieName, String.valueOf(i+anomalieList.getRevisionBirthNumber()));
            }
        }
        /*

        result.addValue(20.3, "Product 1 (US)", "Jan 04");
        result.addValue(27.2, "Product 1 (US)", "Feb 04");
        result.addValue(19.7, "Product 1 (US)", "Mar 04");
        result.addValue(19.4, "Product 1 (Europe)", "Jan 04");
        result.addValue(10.9, "Product 1 (Europe)", "Feb 04");
        result.addValue(18.4, "Product 1 (Europe)", "Mar 04");
        result.addValue(16.5, "Product 1 (Asia)", "Jan 04");
        result.addValue(15.9, "Product 1 (Asia)", "Feb 04");
        result.addValue(16.1, "Product 1 (Asia)", "Mar 04");
        result.addValue(13.2, "Product 1 (Middle East)", "Jan 04");
        result.addValue(14.4, "Product 1 (Middle East)", "Feb 04");
        result.addValue(13.7, "Product 1 (Middle East)", "Mar 04");

        result.addValue(23.3, "Product 2 (US)", "Jan 04");
        result.addValue(16.2, "Product 2 (US)", "Feb 04");
        result.addValue(28.7, "Product 2 (US)", "Mar 04");
        result.addValue(12.7, "Product 2 (Europe)", "Jan 04");
        result.addValue(17.9, "Product 2 (Europe)", "Feb 04");
        result.addValue(12.6, "Product 2 (Europe)", "Mar 04");
        result.addValue(15.4, "Product 2 (Asia)", "Jan 04");
        result.addValue(21.0, "Product 2 (Asia)", "Feb 04");
        result.addValue(11.1, "Product 2 (Asia)", "Mar 04");
        result.addValue(23.8, "Product 2 (Middle East)", "Jan 04");
        result.addValue(23.4, "Product 2 (Middle East)", "Feb 04");
        result.addValue(19.3, "Product 2 (Middle East)", "Mar 04");

        result.addValue(11.9, "Product 3 (US)", "Jan 04");
        result.addValue(31.0, "Product 3 (US)", "Feb 04");
        result.addValue(22.7, "Product 3 (US)", "Mar 04");
        result.addValue(15.3, "Product 3 (Europe)", "Jan 04");
        result.addValue(14.4, "Product 3 (Europe)", "Feb 04");
        result.addValue(25.3, "Product 3 (Europe)", "Mar 04");
        result.addValue(23.9, "Product 3 (Asia)", "Jan 04");
        result.addValue(19.0, "Product 3 (Asia)", "Feb 04");
        result.addValue(10.1, "Product 3 (Asia)", "Mar 04");
        result.addValue(13.2, "Product 3 (Middle East)", "Jan 04");
        result.addValue(15.5, "Product 3 (Middle East)", "Feb 04");
        result.addValue(10.1, "Product 3 (Middle East)", "Mar 04");
        * */

        return result;
    }

    private JFreeChart createChart(final CategoryDataset dataset) {

        final JFreeChart chart = ChartFactory.createStackedBarChart(
                "Anomalies", // chart title
                "Revision", // domain axis label
                "Value", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // the plot orientation
                true, // legend
                true, // tooltips
                false // urls
                );

        GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
        //KeyToGroupMap map = new KeyToGroupMap("G1");
        /*map.mapKeyToGroup("Product 1 (US)", "G1");
        map.mapKeyToGroup("Product 1 (Europe)", "G1");
        map.mapKeyToGroup("Product 1 (Asia)", "G1");
        map.mapKeyToGroup("Product 1 (Middle East)", "G1");
        map.mapKeyToGroup("Product 2 (US)", "G2");
        map.mapKeyToGroup("Product 2 (Europe)", "G2");
        map.mapKeyToGroup("Product 2 (Asia)", "G2");
        map.mapKeyToGroup("Product 2 (Middle East)", "G2");
        map.mapKeyToGroup("Product 3 (US)", "G3");
        map.mapKeyToGroup("Product 3 (Europe)", "G3");
        map.mapKeyToGroup("Product 3 (Asia)", "G3");
        map.mapKeyToGroup("Product 3 (Middle East)", "G3");*/
        
        //renderer.setSeriesToGroupMap(map);

        renderer.setItemMargin(0.0);
        
        /*
        Paint p1 = new GradientPaint(
                0.0f, 0.0f, new Color(0x22, 0x22, 0xFF), 0.0f, 0.0f, new Color(0x88, 0x88, 0xFF));
        renderer.setSeriesPaint(0, p1);
        renderer.setSeriesPaint(4, p1);
        renderer.setSeriesPaint(8, p1);

        Paint p2 = new GradientPaint(
                0.0f, 0.0f, new Color(0x22, 0xFF, 0x22), 0.0f, 0.0f, new Color(0x88, 0xFF, 0x88));
        renderer.setSeriesPaint(1, p2);
        renderer.setSeriesPaint(5, p2);
        renderer.setSeriesPaint(9, p2);

        Paint p3 = new GradientPaint(
                0.0f, 0.0f, new Color(0xFF, 0x22, 0x22), 0.0f, 0.0f, new Color(0xFF, 0x88, 0x88));
        renderer.setSeriesPaint(2, p3);
        renderer.setSeriesPaint(6, p3);
        renderer.setSeriesPaint(10, p3);

        Paint p4 = new GradientPaint(
                0.0f, 0.0f, new Color(0xFF, 0xFF, 0x22), 0.0f, 0.0f, new Color(0xFF, 0xFF, 0x88));
        renderer.setSeriesPaint(3, p4);
        renderer.setSeriesPaint(7, p4);
        renderer.setSeriesPaint(11, p4);
        renderer.setGradientPaintTransformer(
                new StandardGradientPaintTransformer(GradientPaintTransformType.HORIZONTAL));
                * 
        */

        SubCategoryAxis domainAxis = new SubCategoryAxis("Revision");
        domainAxis.setCategoryMargin(0.05);
        //domainAxis.addSubCategory("Product 1");
        //domainAxis.addSubCategory("Product 2");
        //domainAxis.addSubCategory("Product 3");

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setDomainAxis(domainAxis);
        //plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
        plot.setRenderer(renderer);
        //plot.setFixedLegendItems(createLegendItems());
        return chart;

    }

    
    public static void main(String args[]){
        GenericAnomalies genericAnomalies = new GenericAnomalies("method",20, 1);
        genericAnomalies.addAnomalie("ANOMALIE_1", 3);
        genericAnomalies.addAnomalie("ANOMALIE_1", 6);
        genericAnomalies.addAnomalie("ANOMALIE_1", 7);
        genericAnomalies.addAnomalie("ANOMALIE_1", 8);
        genericAnomalies.addAnomalie("ANOMALIE_1", 9);
        genericAnomalies.addAnomalie("ANOMALIE_1", 15);
        
        genericAnomalies.addAnomalie("ANOMALIE_2", 3);
        genericAnomalies.addAnomalie("ANOMALIE_2", 6);
        genericAnomalies.addAnomalie("ANOMALIE_2", 7);
        genericAnomalies.addAnomalie("ANOMALIE_2", 8);
        genericAnomalies.addAnomalie("ANOMALIE_2", 9);
        genericAnomalies.addAnomalie("ANOMALIE_2", 12);
        
        genericAnomalies.addAnomalie("ANOMALIE_3", 3);
        genericAnomalies.addAnomalie("ANOMALIE_3", 6);
        genericAnomalies.addAnomalie("ANOMALIE_3", 7);
        genericAnomalies.addAnomalie("ANOMALIE_3", 8);
        genericAnomalies.addAnomalie("ANOMALIE_3", 9);
        genericAnomalies.addAnomalie("ANOMALIE_3", 12);
        genericAnomalies.addAnomalie("ANOMALIE_3", 15);
        genericAnomalies.addAnomalie("ANOMALIE_3", 16);
        
        JPanel anomalieChart = new AnomalieChart(genericAnomalies, "ALL ANOMALIES");
        
        JFrame jframe = new JFrame();
        jframe.setContentPane(anomalieChart);
        jframe.setVisible(true);
        
    }
}
