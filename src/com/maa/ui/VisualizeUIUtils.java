/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.ui;

import com.maa.algo.enums.NodeHitType;
import com.maa.enums.VisOperations;
import com.maa.utils.Tokenizers;
import com.maa.vis.objects.GNodeInfo;
import com.maa.vis.objects.ReducedNode;
import com.maa.vis.objects.VisGNode;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 *
 * @author Thush
 */
public class VisualizeUIUtils {

    private JPanel btnPanel;
    private Map<String, JButton> jButtons;
    private ArrayList<String> interLinks;
    private ArrayList<String> dims;
    private ArrayList<VisGNode> allVisNodes;
    private ArrayList<ArrayList<ReducedNode>> allRedNodes;
    private ArrayList<VisGNode> anoNodes;
    private ArrayList<Line> tempLines;
    private ArrayList<Line> intLines;
    private VisOperations currOp;

    public VisualizeUIUtils() {
        jButtons = new HashMap<>();
        currOp = VisOperations.DRAW_TEMP_LINKS;
    }

    public void setData(ArrayList<String> dims, ArrayList<ArrayList<ReducedNode>> redNodes, ArrayList<VisGNode> allVisNodes, ArrayList<VisGNode> anoNodes, ArrayList<String> intLinks) {
        if (dims != null){
            this.dims = dims;
        }
        
        if (allVisNodes != null) {
            this.allVisNodes = allVisNodes;
        }
        if (intLinks != null) {
            this.interLinks = intLinks;
        }
        if (anoNodes != null) {
            this.anoNodes = anoNodes;
        }
        if (redNodes != null) {
            this.allRedNodes = redNodes;
        }
    }

    public JPanel getVisJPanel() {

        UIDefaults def = UIManager.getLookAndFeelDefaults();
        final Color bg = def.getColor("control");
        int widthFreeSpace = 50;
        int heightFreeSpace = 50;
        final int btnWidth = 60;
        final int btnHeight = 25;
        final int hGap = 50;
        final int vGap = 100;

        int[] maxXY = getMaxCoordsVisNodes(allVisNodes);

        int frameWidth = ((maxXY[0] + 1) * btnWidth) + (maxXY[0] * hGap);
        int frameHeight = ((maxXY[1] + 1) * btnHeight) + (maxXY[1] * vGap);

        btnPanel = new JPanel() {
            public void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                if (currOp == VisOperations.DRAW_TEMP_LINKS) {
                    tempLines = new ArrayList<>();
                    drawTemporalLinks(g, g2d, Color.BLUE, true);
                }
                if (currOp == VisOperations.DRAW_INT_LINKS) {
                    intLines = new ArrayList<>();
                    drawIntersectionLinks(g, Color.RED, true);
                }
                if (currOp == VisOperations.CLEAR_TEMP_LINKS) {
                    drawTemporalLinks(g, g2d, bg, false);
                    drawIntersectionLinks(g, Color.RED, false);
                }
                if (currOp == VisOperations.CLEAR_INT_LINKS) {
                    drawIntersectionLinks(g, bg, false);
                    drawTemporalLinks(g, g2d, Color.BLUE, false);
                }
                if (currOp == VisOperations.DRAW_BOTH_LINKS) {
                    tempLines = new ArrayList<>();
                    intLines = new ArrayList<>();
                    drawIntersectionLinks(g, Color.RED, true);
                    drawTemporalLinks(g, g2d, Color.BLUE, true);
                }
                if (currOp == VisOperations.CLEAR_BOTH_LINKS) {
                    drawIntersectionLinks(g, bg, false);
                    drawTemporalLinks(g, g2d, bg, false);
                }


            }

            private void drawIntersectionLinks(Graphics g, Color c, Boolean addToArray) throws NumberFormatException {

                for (String link : interLinks) {
                    String[] tokens = link.split(Tokenizers.NODE_TOKENIZER);
                    for (int i = 1; i < tokens.length; i++) {
                        String[] idStr = tokens[i].split(Tokenizers.I_J_TOKENIZER);
                        VisGNode cVgn = getVisGNodeWithID(allVisNodes, Integer.parseInt(idStr[0]), Integer.parseInt(idStr[1]));
                        int[] xy = getVisCoordinates(cVgn.getCoordinates()[0], cVgn.getCoordinates()[1],
                                btnWidth, btnHeight, hGap, vGap);

                        String[] prevIDStr = tokens[i - 1].split(Tokenizers.I_J_TOKENIZER);
                        VisGNode pVgn = getVisGNodeWithID(allVisNodes, Integer.parseInt(prevIDStr[0]), Integer.parseInt(prevIDStr[1]));
                        int[] prevXY = getVisCoordinates(pVgn.getCoordinates()[0], pVgn.getCoordinates()[1],
                                btnWidth, btnHeight, hGap, vGap);
                        g.setColor(c);
                        g.drawLine(xy[0], xy[1], prevXY[0], prevXY[1]);
                        if (addToArray) {
                            intLines.add(new Line(xy[0], xy[1], prevXY[0], prevXY[1]));
                        }
                    }
                }
            }

            private void drawTemporalLinks(Graphics g, Graphics2D g2d, Color c, boolean addToArray) {
                for (VisGNode gn : allVisNodes) {
                    int[] xy = getVisCoordinates(gn.getCoordinates()[0], gn.getCoordinates()[1],
                            btnWidth, btnHeight, hGap, vGap);
                    if (gn.getParent() != null) {
                        int[] pXY = getVisCoordinates(gn.getParent().getCoordinates()[0], gn.getParent().getCoordinates()[1],
                                btnWidth, btnHeight, hGap, vGap);
                        g.setColor(c);
                        g.drawLine(xy[0], xy[1], pXY[0], pXY[1]);
                        if (addToArray) {
                            tempLines.add(new Line(xy[0], xy[1], pXY[0], pXY[1]));
                        }
                        //if there are multiple parents to the node
                        if (gn.getOtherParentCoords() != null && !gn.getOtherParentCoords().isEmpty()) {
                            for (VisGNode oVgn : gn.getOtherParentCoords()) {
                                int[] oPXY = getVisCoordinates(oVgn.getCoordinates()[0], oVgn.getCoordinates()[1], btnWidth, btnHeight, hGap, vGap);
                                g.setColor(c);
                                g.drawLine(xy[0], xy[1], oPXY[0], oPXY[1]);
                                if (addToArray) {
                                    tempLines.add(new Line(xy[0], xy[1], oPXY[0], oPXY[1]));
                                }
                            }
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
                    final VisGNode vgn = allVisNodes.get(gnIdx);
                    int[] coords = allVisNodes.get(gnIdx).getID();


                    //btn.setToolTipText(x + "," + y + ";" + vgn.getParent().getCoordinates()[0] + "," + vgn.getParent().getCoordinates()[1]
                    //        + " - " + vgn.getID()[0] + "," + vgn.getID()[1] + ";" + vgn.getParent().getID()[0] + "," + vgn.getParent().getID()[1]);
                    ArrayList<String> inputs = null;
                    //get the layer of reduced nodes corresponding to vgn learn cycle
                    ArrayList<ReducedNode> redNodes = allRedNodes.get(vgn.getID()[0]);
                    
                    ReducedNode tempRedNode = null;
                    for (ReducedNode rn : redNodes) {
                        if (rn.getId()[0] == vgn.getID()[0] && rn.getId()[1] == vgn.getID()[1]) {
                            tempRedNode = rn;
                            inputs = rn.getInputs();
                            break;
                        }
                    }
                    final ReducedNode finalRedNode = tempRedNode;
                    
                    if (inputs != null) {
                        String inputsTxt = "";
                        for (String s : inputs) {
                            inputsTxt += s + ", ";
                        }
                        btn.setToolTipText(inputsTxt);
                    }

                    btn.setSize(new Dimension(btnWidth, btnHeight));
                    btn.setText(vgn.getID()[0] + "," + vgn.getID()[1]);
                    btnPanel.add(btn);
                    jButtons.put(x + "," + y, btn);
                    btn.addActionListener(new ActionListener() {
                        
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            GNodeInfo gInfo = new GNodeInfo();
                            gInfo.setNodeID(vgn.getID()[0]+ Tokenizers.I_J_TOKENIZER + vgn.getID()[1]);
                            gInfo.setNodeCoords(vgn.getCoordinates());
                            gInfo.setSynopsis(dims, finalRedNode.getWeights());
                            gInfo.setInputs(finalRedNode.getInputs());
                            GNodeInfoDialog gnodeDialog = new GNodeInfoDialog(null, false, gInfo);
                            gnodeDialog.setVisible(true);
                        }
                    }); 
                } else {
                    JLabel lbl = new JLabel();
                    lbl.setSize(btnWidth, btnHeight);
                    btnPanel.add(lbl);
                }
            }
        }

        btnPanel.setBackground(Color.LIGHT_GRAY);
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

    public static VisGNode getVisGNodeWithXY(ArrayList<VisGNode> list, int x, int y) {
        for (int i = list.size() - 1; i >= 0; i--) {
            VisGNode vgn = list.get(i);
            if (vgn.getCoordinates()[0] == x && vgn.getCoordinates()[1] == y) {
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

    public void showAnomalousClusters() {
        for (VisGNode vgn : anoNodes) {
            String coords = vgn.getCoordinates()[0] + "," + vgn.getCoordinates()[1];
            JButton btn = jButtons.get(coords);
            if (btn != null) {
                btn.setBackground(Color.red);
            }
        }
    }

    public void hideAnomalousClusters() {
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

    public void showIntersectionLinks(boolean showBoth) {
        if (showBoth) {
            this.currOp = VisOperations.DRAW_BOTH_LINKS;
        } else {
            this.currOp = VisOperations.DRAW_INT_LINKS;
        }
    }

    public void showTemporalLinks(boolean showBoth) {
        if (showBoth) {
            this.currOp = VisOperations.DRAW_BOTH_LINKS;
        } else {
            this.currOp = VisOperations.DRAW_TEMP_LINKS;
        }
    }

    public void hideLinks() {
        this.currOp = VisOperations.CLEAR_BOTH_LINKS;
    }

    public Map<String, JButton> getJButtonMap() {
        return jButtons;
    }
}
