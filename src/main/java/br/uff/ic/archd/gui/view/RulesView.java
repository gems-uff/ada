/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.gui.view;

import java.awt.Dimension;
import javax.swing.JComboBox;
import javax.swing.JFrame;

/**
 *
 * @author wallace
 */
public class RulesView extends JFrame{
    
    private JComboBox rulesComboBox;
    
    public RulesView(){
        createWidgets();
        this.setPreferredSize(new Dimension(1600, 800));
        this.setSize(new Dimension(1600, 800));
        this.setMaximumSize(new Dimension(1600, 800));
    }
    
    private void createWidgets(){
        rulesComboBox = new JComboBox();
    }
}
