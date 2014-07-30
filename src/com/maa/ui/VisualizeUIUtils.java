/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.ui;

import com.maa.algo.enums.NodeHitType;
import com.maa.algo.ikasl.auxi.GNodeVisualizer;
import com.maa.algo.objects.GNode;
import com.maa.vis.objects.VisGNode;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Thush
 */
public class VisualizeUIUtils {
    

    
    public JPanel getVisJPanel(final ArrayList<VisGNode> allVisNodes){
    GNodeVisualizer viser = new GNodeVisualizer();

        int widthFreeSpace = 50;
        int heightFreeSpace = 50;
        final int btnWidth = 25;
        final int btnHeight = 25;
        final int hGap = 50;
        final int vGap = 100;

        int[] maxXY = getMaxCoordsVisNodes(allVisNodes);

        int frameWidth = ((maxXY[0] + 1) * btnWidth) + (maxXY[0] * hGap);
        int frameHeight = ((maxXY[1] + 1) * btnHeight) + (maxXY[1] * vGap);

        JPanel btnPanel = new JPanel() {
            public void paintComponent(Graphics g) {
                for (VisGNode gn : allVisNodes) {
                    int[] xy = getVisCoordinates(gn.getCoordinates()[0], gn.getCoordinates()[1],
                            btnWidth, btnHeight, hGap, vGap);
                    if (gn.getParent() != null) {
                        int[] pXY = getVisCoordinates(gn.getParent().getCoordinates()[0], gn.getParent().getCoordinates()[1],
                                btnWidth, btnHeight, hGap, vGap);
                        g.drawLine(xy[0], xy[1], pXY[0], pXY[1]);

                    }
                }

            }
        };
        btnPanel.setPreferredSize(new Dimension(frameWidth, frameHeight));
        btnPanel.setLayout(new GridLayout(maxXY[1] + 1, maxXY[0] + 1, hGap, vGap));

        for (int y = 0; y <= maxXY[1]; y++) {
            for (int x = 0; x <= maxXY[0]; x++) {

                if (allVisNodes.contains(new VisGNode(null, x, y, null, NodeHitType.HIT))) {

                    JButton btn = new JButton();
                    int gnIdx = allVisNodes.indexOf(new VisGNode(null, x, y, null, NodeHitType.HIT));
                    VisGNode vgn = allVisNodes.get(gnIdx);
                    int[] coords = allVisNodes.get(gnIdx).getID();
                    
                    if(vgn.getParent()!=null){
                    btn.setToolTipText(x + "," + y + ";" + vgn.getParent().getCoordinates()[0] + "," + vgn.getParent().getCoordinates()[1]
                            + "-" + vgn.getID()[0] + "," + vgn.getID()[1] + ";" + vgn.getParent().getID()[0] + "," + vgn.getParent().getID()[1]);
                    }
                    btn.setSize(new Dimension(btnWidth, btnHeight));
                    btnPanel.add(btn);

                    
                } else {
                    JLabel lbl = new JLabel();
                    lbl.setSize(btnWidth, btnHeight);
                    btnPanel.add(lbl);
                }
            }
        }

        //btnPanel.setBounds(0, 0, frameWidth,frameHeight);
        return btnPanel;
    }
    
    private int[] getVisCoordinates(int x, int y, int btnWidth, int btnHeight, int hGap, int vGap) {
        double visX = ((double) btnWidth) / 2 + ((hGap + btnWidth) * x);
        double visY = ((double) btnHeight) / 2 + ((vGap + btnHeight) * y);

        return new int[]{(int) visX, (int) visY};
    }

    private int[] getMaxCoordsVisNodes(ArrayList<VisGNode> visNodes) {
        int maxX = 0;
        int maxY = 0;
        for (VisGNode vgn : visNodes) {
            if (vgn.getCoordinates()[0] > maxX) {
                maxX = vgn.getCoordinates()[0];
            }
            if (vgn.getCoordinates()[1] > maxY) {
                maxY = vgn.getCoordinates()[1];
            }
        }
        return new int[]{maxX, maxY};
    }
}
