/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.algo.ui;

import com.maa.algo.objects.VisGNode;
import java.awt.EventQueue;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Thush
 */
public class VisualizationUI extends JFrame {

    private JPanel visPanel;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new VisualizationUI().setVisible(true);
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public VisualizationUI(){
        //visPanel = getVisJPanel(allVisNodes);
        initComponents();
    }
    
    public void initComponents(){
        int frameWidth = 1000;
        int frameHeight = 800;
        setSize(frameWidth, frameHeight);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 10, 800, 700);
        this.getContentPane().add(scrollPane);
        
        scrollPane.setViewportView(visPanel); 
    }
    
    
    private JPanel getVisJPanel(ArrayList<VisGNode> allVisNodes){
        return null;
    }
}
