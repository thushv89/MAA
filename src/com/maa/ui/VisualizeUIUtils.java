/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.ui;

import com.maa.algo.enums.NodeHitType;
import com.maa.algo.ikasl.auxi.GNodeVisualizer;
import com.maa.algo.objects.GNode;
import com.maa.utils.Tokenizers;
import com.maa.vis.objects.ReducedNode;
import com.maa.vis.objects.VisGNode;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Stroke;
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

    private JPanel btnPanel;
    private Map<String, JButton> jButtons;
    private boolean drawAnomalies = false;
    private boolean drawFrequentPatterns = false;
    private ArrayList<String> interLinks;

    public VisualizeUIUtils() {
        jButtons = new HashMap<>();
    }

    public JPanel getVisJPanel(final ArrayList<VisGNode> allVisNodes) {

        int widthFreeSpace = 50;
        int heightFreeSpace = 50;
        final int btnWidth = 25;
        final int btnHeight = 25;
        final int hGap = 50;
        final int vGap = 100;

        int[] maxXY = getMaxCoordsVisNodes(allVisNodes);

        int frameWidth = ((maxXY[0] + 1) * btnWidth) + (maxXY[0] * hGap);
        int frameHeight = ((maxXY[1] + 1) * btnHeight) + (maxXY[1] * vGap);

        btnPanel = new JPanel() {
            public void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                for (VisGNode gn : allVisNodes) {
                    int[] xy = getVisCoordinates(gn.getCoordinates()[0], gn.getCoordinates()[1],
                            btnWidth, btnHeight, hGap, vGap);
                    if (gn.getParent() != null) {
                        int[] pXY = getVisCoordinates(gn.getParent().getCoordinates()[0], gn.getParent().getCoordinates()[1],
                                btnWidth, btnHeight, hGap, vGap);
                        if (gn.getVgnType() == NodeHitType.HIT) {
                            g.drawLine(xy[0], xy[1], pXY[0], pXY[1]);
                        } else {
                            Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
                            g2d.setStroke(dashed);
                            g2d.drawLine(xy[0], xy[1], pXY[0], pXY[1]);
                        }

                    }
                }

                if (drawFrequentPatterns) {
                    for (String link : interLinks) {
                        String[] tokens = link.split(Tokenizers.NODE_TOKENIZER);
                        for (int i=1;i<tokens.length;i++) {
                            String[] idStr = tokens[i].split(Tokenizers.I_J_TOKENIZER);
                            VisGNode cVgn = getVisGNodeWithID(allVisNodes, Integer.parseInt(idStr[0]), Integer.parseInt(idStr[1]));
                            int[] xy = getVisCoordinates(cVgn.getCoordinates()[0],cVgn.getCoordinates()[1],
                                    btnWidth, btnHeight, hGap, vGap);
                            
                            String[] prevIDStr = tokens[i-1].split(Tokenizers.I_J_TOKENIZER);
                            VisGNode pVgn = getVisGNodeWithID(allVisNodes, Integer.parseInt(prevIDStr[0]), Integer.parseInt(prevIDStr[1]));
                                int[] prevXY = getVisCoordinates(pVgn.getCoordinates()[0], pVgn.getCoordinates()[1],
                                        btnWidth, btnHeight, hGap, vGap);
                                g.setColor(Color.red);
                                g.drawLine(xy[0], xy[1], prevXY[0], prevXY[1]);
                            
                        }
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

                    if (vgn.getParent() != null) {
                        btn.setToolTipText(x + "," + y + ";" + vgn.getParent().getCoordinates()[0] + "," + vgn.getParent().getCoordinates()[1]
                                + "-" + vgn.getID()[0] + "," + vgn.getID()[1] + ";" + vgn.getParent().getID()[0] + "," + vgn.getParent().getID()[1]);
                    } else {
                        btn.setToolTipText(x + "," + y + "-" + vgn.getID()[0] + "," + vgn.getID()[1]);
                    }
                    btn.setSize(new Dimension(btnWidth, btnHeight));
                    btnPanel.add(btn);
                    jButtons.put(x + "," + y, btn);

                } else {
                    JLabel lbl = new JLabel();
                    lbl.setSize(btnWidth, btnHeight);
                    btnPanel.add(lbl);
                }
            }
        }

        return btnPanel;
    }

        public static VisGNode getVisGNodeWithID(ArrayList<VisGNode> list, int lc, int id) {
        for (int i = list.size() - 1; i >= 0; i--) {
            VisGNode vgn = list.get(i);
            if (vgn.getID()[0] == lc && vgn.getID()[1] == id) {
                return vgn;
            }
        }
        return null;
    }

    public static ArrayList<Integer> getNodeIdxToRightWithLC(ArrayList<VisGNode> list, int lc, VisGNode node) {
        ArrayList<Integer> rightNodes = new ArrayList<>();
        int[] coords = node.getCoordinates();
        for (VisGNode n : list) {
            int x = n.getCoordinates()[0];
            int y = n.getCoordinates()[1];
            if (y == lc && x > coords[0]) {
                rightNodes.add(list.indexOf(n));
            }
        }

        return rightNodes;
    }

    public static int getRightMostIndexWithLC(ArrayList<VisGNode> list, int lc) {
        int maxIdx = 0;
        for (VisGNode n : list) {
            if (n.getID()[0] == lc) {
                if (maxIdx < n.getCoordinates()[0]) {
                    maxIdx = n.getCoordinates()[0];
                }
            }
        }
        return maxIdx;
    }

    public static int getRightMostIndex(ArrayList<VisGNode> list) {
        int maxIdx = 0;
        for (VisGNode n : list) {
            if (maxIdx < n.getCoordinates()[0]) {
                maxIdx = n.getCoordinates()[0];
            }
        }
        return maxIdx;
    }
    
    public void showAnomalousClusters(ArrayList<VisGNode> anoNodes) {
        for (VisGNode vgn : anoNodes) {
            String coords = vgn.getCoordinates()[0] + "," + vgn.getCoordinates()[1];
            JButton btn = jButtons.get(coords);
            if (btn != null) {
                btn.setBackground(Color.red);
            }
        }
    }

    public void hideAnomalousClusters(ArrayList<VisGNode> anoNodes) {
        for (VisGNode vgn : anoNodes) {
            String coords = vgn.getCoordinates()[0] + "," + vgn.getCoordinates()[1];
            JButton btn = jButtons.get(coords);
            if (btn != null) {
                btn.setBackground(null);
            }
        }
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

    public void drawIntersectionLinks(ArrayList<String> links) {
        this.interLinks = links;
        this.drawFrequentPatterns = true;        
    }
}
