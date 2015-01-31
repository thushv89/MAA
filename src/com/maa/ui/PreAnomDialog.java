/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.ui;

import com.maa.algo.utils.Constants;
import com.maa.io.FileUtils;
import com.maa.utils.CurrentJobState;
import com.maa.utils.DefaultValues;
import com.maa.utils.ImportantFileNames;
import com.maa.utils.InputParser;
import com.maa.vis.objects.ReducedNode;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;

/**
 *
 * @author Thush
 */
public class PreAnomDialog extends javax.swing.JDialog {

    private ArrayList<String> preAnomLinkList;
    private HashMap<String, ArrayList<String>> preAnomLinkMap;
    private HashMap<String, ArrayList<String>> preAnomDevIDMap;
    private ArrayList<String> normalLinkList;
    private HashMap<String, ArrayList<String>> normalLinkDevIDMap;
    private String nonUtilinputDir;
    private String utilinputDir;
    private HashMap<String, ArrayList<double[]>> linkNonUtilWeightMap;
    HashMap<String, ArrayList<double[]>> nullNonUtilWeightSeq;
    private HashMap<String, ArrayList<double[]>> linkUtilWeightVecMap;
    HashMap<String, ArrayList<double[]>> nullUtilWeightSeq;
    private ArrayList<String> dimNames;
    private ArrayList<ArrayList<ReducedNode>> allNodes;

    private int nonUtilDimCount = 7;
    /**
     * Creates new form PreAnomDialog
     */
    public PreAnomDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public void populateAlertCmb() {
        if (!dimNames.isEmpty()) {
            for (String s : dimNames) {
                alertTypeCmb.addItem(s);
            }
        }
    }

    public void setBasicData(ArrayList<ArrayList<ReducedNode>> allNodes) {
        this.allNodes = allNodes;
    }

    public void setPreAnomData(ArrayList<String> preAnomLinkList, HashMap<String, ArrayList<String>> preAnomLinkMap, HashMap<String, ArrayList<String>> preAnomDevIDMap) {
        this.preAnomDevIDMap = preAnomDevIDMap;
        this.preAnomLinkList = preAnomLinkList;
        this.preAnomLinkMap = preAnomLinkMap;
    }

    public void setNormalData(ArrayList<String> normalLinkList, HashMap<String, ArrayList<String>> normalLinkDevIDMap) {
        this.normalLinkList = normalLinkList;
        this.normalLinkDevIDMap = normalLinkDevIDMap;
    }

    public void setAlertTypes(ArrayList<String> dimNames) {
        this.dimNames = dimNames;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        browseBtn = new javax.swing.JButton();
        dataFolderLbl = new javax.swing.JLabel();
        calcWeightBtn = new javax.swing.JButton();
        writeCSVBtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        browseUtilBtn = new javax.swing.JButton();
        writeCSVUtilBtn = new javax.swing.JButton();
        calcWeightUtilBtn = new javax.swing.JButton();
        dataFolderUtilLbl = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        calcWeightAlertBtn = new javax.swing.JButton();
        alertTypeCmb = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        alertResultTxt = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Non-Util Tags"));

        browseBtn.setText("Select Data Folder");
        browseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseBtnActionPerformed(evt);
            }
        });

        dataFolderLbl.setText("Folder location will appear here");

        calcWeightBtn.setText("Calculate Weight Vectors");
        calcWeightBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calcWeightBtnActionPerformed(evt);
            }
        });

        writeCSVBtn.setText("Write to CSV");
        writeCSVBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                writeCSVBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(browseBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dataFolderLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(calcWeightBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(writeCSVBtn)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(browseBtn)
                    .addComponent(dataFolderLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(calcWeightBtn)
                    .addComponent(writeCSVBtn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Util Tags"));

        browseUtilBtn.setText("Select Data Folder");
        browseUtilBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseUtilBtnActionPerformed(evt);
            }
        });

        writeCSVUtilBtn.setText("Write to CSV");
        writeCSVUtilBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                writeCSVUtilBtnActionPerformed(evt);
            }
        });

        calcWeightUtilBtn.setText("Calculate Weight Vectors");
        calcWeightUtilBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calcWeightUtilBtnActionPerformed(evt);
            }
        });

        dataFolderUtilLbl.setText("Folder location will appear here");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(browseUtilBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dataFolderUtilLbl, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(calcWeightUtilBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(writeCSVUtilBtn)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(browseUtilBtn)
                    .addComponent(dataFolderUtilLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(calcWeightUtilBtn)
                    .addComponent(writeCSVUtilBtn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Alerts"));

        calcWeightAlertBtn.setText("Calculate Weight Vectors");
        calcWeightAlertBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calcWeightAlertBtnActionPerformed(evt);
            }
        });

        alertResultTxt.setColumns(20);
        alertResultTxt.setRows(5);
        jScrollPane1.setViewportView(alertResultTxt);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(alertTypeCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(calcWeightAlertBtn)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(calcWeightAlertBtn)
                    .addComponent(alertTypeCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void browseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseBtnActionPerformed
        JFileChooser chooser = new JFileChooser(".");//E:\GSOM2_v3\GSOM2
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int choice = chooser.showOpenDialog(null);

        if (choice != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File chosenFile = chooser.getSelectedFile();
        //JOptionPane.showMessageDialog(null,chosenFile.getAbsolutePath());
        dataFolderLbl.setText(chosenFile.getAbsolutePath());
        nonUtilinputDir = chosenFile.getAbsolutePath();
    }//GEN-LAST:event_browseBtnActionPerformed

    private void calcWeightBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calcWeightBtnActionPerformed
        nullNonUtilWeightSeq = new HashMap<>();

        HashMap<String, ArrayList<double[]>> tempNonUtilMap = getPreAnomWeightSeq(preAnomLinkMap, preAnomDevIDMap, nullNonUtilWeightSeq, ImportantFileNames.NON_UTIL_TAG_FILE_PREFIX, nonUtilinputDir);
        linkNonUtilWeightMap = new HashMap<>(tempNonUtilMap);

        String key = "Normal";
        HashMap<String, ArrayList<String>> normLinkMap = new HashMap<>();
        normLinkMap.put(key, normalLinkList);

        tempNonUtilMap = getPreAnomWeightSeq(normLinkMap, normalLinkDevIDMap, nullNonUtilWeightSeq, ImportantFileNames.NON_UTIL_TAG_FILE_PREFIX, nonUtilinputDir);
        linkNonUtilWeightMap.putAll(tempNonUtilMap);
    }//GEN-LAST:event_calcWeightBtnActionPerformed

    private void writeCSVBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_writeCSVBtnActionPerformed
        ArrayList<String> wData = new ArrayList<>();
        String result;
        for (Map.Entry<String, ArrayList<double[]>> e : linkNonUtilWeightMap.entrySet()) {
            result = e.getKey();
            for (double[] arr : e.getValue()) {
                for (double d : arr) {
                    result += Constants.INPUT_TOKENIZER + d;
                }
            }
            wData.add(result);
        }

        for (Map.Entry<String, ArrayList<double[]>> e : nullNonUtilWeightSeq.entrySet()) {
            result = e.getKey();
            for (double[] arr : e.getValue()) {
                if (arr == null) {
                    for(int i=0;i<nonUtilDimCount;i++){
                        result += ","+"null";
                    }
                } else {
                    for (double d : arr) {
                        result += Constants.INPUT_TOKENIZER + d;
                    }
                }
            }
            wData.add(result);
        }

        FileUtils.writeData(wData, "PreAnomNonUtil.csv");
    }//GEN-LAST:event_writeCSVBtnActionPerformed

    private void browseUtilBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseUtilBtnActionPerformed
        JFileChooser chooser = new JFileChooser(".");//E:\GSOM2_v3\GSOM2
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int choice = chooser.showOpenDialog(null);

        if (choice != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File chosenFile = chooser.getSelectedFile();
        //JOptionPane.showMessageDialog(null,chosenFile.getAbsolutePath());
        dataFolderUtilLbl.setText(chosenFile.getAbsolutePath());
        utilinputDir = chosenFile.getAbsolutePath();
    }//GEN-LAST:event_browseUtilBtnActionPerformed

    private void calcWeightUtilBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calcWeightUtilBtnActionPerformed
        nullUtilWeightSeq = new HashMap<>();
        linkUtilWeightVecMap = new HashMap<>(getPreAnomWeightSeq(preAnomLinkMap, preAnomDevIDMap, nullUtilWeightSeq, ImportantFileNames.UTIL_TAG_FILE_PREFIX, utilinputDir));

        String key = "Normal";
        HashMap<String, ArrayList<String>> normLinkMap = new HashMap<>();
        normLinkMap.put(key, normalLinkList);
        linkUtilWeightVecMap.putAll(getPreAnomWeightSeq(normLinkMap, normalLinkDevIDMap, nullUtilWeightSeq, ImportantFileNames.UTIL_TAG_FILE_PREFIX, utilinputDir));

    }//GEN-LAST:event_calcWeightUtilBtnActionPerformed

    private void writeCSVUtilBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_writeCSVUtilBtnActionPerformed
        ArrayList<String> wData = new ArrayList<>();

        ArrayList<ArrayList<double[]>> values = new ArrayList<>(linkUtilWeightVecMap.values());
        int dims = values.get(0).get(0).length;
        int numOfUtilTags = 3;  //Proc, NetR, NetS
        int seqCountPerTag = dims / numOfUtilTags;
        int duration = values.get(0).size();
        String[] utilTagNames = new String[]{"Proc", "NetR", "NetS"};

        for (int i = 0; i < numOfUtilTags; i++) {
            for (Map.Entry<String, ArrayList<double[]>> e : linkUtilWeightVecMap.entrySet()) {
                if (e.getValue().size() == duration) {
                    String result = e.getKey() + "-" + utilTagNames[i];
                    for (int j = 0; j < duration; j++) {
                        for (int k = i * seqCountPerTag; k < (i + 1) * seqCountPerTag; k++) {
                            result += "," + e.getValue().get(j)[k];
                        }
                    }
                    wData.add(result);
                }
            }
        }

        for (int i = 0; i < numOfUtilTags; i++) {
            for (Map.Entry<String, ArrayList<double[]>> e : nullUtilWeightSeq.entrySet()) {

                String result = e.getKey() + "-" + utilTagNames[i];
                for (int j = 0; j < duration; j++) {
                    if (e.getValue().get(j) == null) {
                        for (int k = 0; k < seqCountPerTag; k++) {
                            result += "," + "null";
                        }
                    } else {
                        for (int k = i * seqCountPerTag; k < (i + 1) * seqCountPerTag; k++) {
                            result += "," + e.getValue().get(j)[k];
                        }
                    }
                }
                wData.add(result);
            }
        }

        FileUtils.writeData(wData, "PreAnomUtil.csv");
    }//GEN-LAST:event_writeCSVUtilBtnActionPerformed

    private void calcWeightAlertBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calcWeightAlertBtnActionPerformed
        String selKey = (String) alertTypeCmb.getSelectedItem();
        ArrayList<String> anomNodes = CurrentJobState.ANOM_GNODES_BY_TYPE.get(selKey);
        ArrayList<double[]> totWeight = new ArrayList<>();
        for (int i = 0; i < DefaultValues.PAST_NODES_PRE_ANOMALY; i++) {
            double[] arr = new double[dimNames.size()];
            totWeight.add(arr);
        }

        int totLinks = 0;
        if (anomNodes == null || anomNodes.isEmpty()) {
            return;
        }

        ArrayList<String> copyAnomNodes = new ArrayList<>(anomNodes);
        for (int i = 0; i < 5 && i < copyAnomNodes.size(); i++) {
            int idx = (int) (Math.random() * copyAnomNodes.size());
            String s = copyAnomNodes.get(idx);
            copyAnomNodes.remove(idx);

            for (String key : CurrentJobState.FILT_LINKS.keySet()) {
                String[] kTokens = key.split(Constants.NODE_TOKENIZER);
                if (containsNode(key, s)) {
                    boolean notEnoughNodes = false;
                    for (int j = 0; j < DefaultValues.PAST_NODES_PRE_ANOMALY - 1; j++) {
                        if (kTokens[j].equals(s)) {
                            notEnoughNodes = true;
                            break;
                        }
                    }

                    if (notEnoughNodes) {
                        continue;
                    }

                    for (int j = 0; j < kTokens.length; j++) {
                        if (s.equals(kTokens[j])) {
                            int posIdx = 0;
                            for (int k = j - DefaultValues.PAST_NODES_PRE_ANOMALY + 1; k <= j; k++) {
                                if (k < 0) {
                                    int xx = 0;
                                }
                                int lc = Integer.parseInt(kTokens[k].split(Constants.I_J_TOKENIZER)[0]);
                                int id = Integer.parseInt(kTokens[k].split(Constants.I_J_TOKENIZER)[1]);
                                for (ReducedNode rn : allNodes.get(lc)) {
                                    if (rn.getId()[0] == lc && rn.getId()[1] == id) {
                                        double[] arr = totWeight.get(posIdx);
                                        for (int l = 0; l < arr.length; l++) {
                                            arr[l] += rn.getWeights()[l];
                                        }
                                        totWeight.set(posIdx, arr);
                                        posIdx++;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    totLinks++;
                }
            }

        }

        for (int i = 0; i < DefaultValues.PAST_NODES_PRE_ANOMALY; i++) {
            double[] arr = totWeight.get(i);
            for (int j = 0; j < arr.length; j++) {
                if (totLinks > 0) {
                    arr[j] = arr[j] / totLinks;
                }
            }
            totWeight.set(i, arr);
        }

        String result = "";
        DecimalFormat df = new DecimalFormat("#.###");
        for (int i = 0; i < DefaultValues.PAST_NODES_PRE_ANOMALY; i++) {
            double[] arr = totWeight.get(i);
            for (int j = 0; j < arr.length; j++) {
                result += df.format(arr[j]) + ",";
            }
            result += "\n";
        }
        alertResultTxt.setText(result);
    }//GEN-LAST:event_calcWeightAlertBtnActionPerformed

    public boolean containsNode(String link, String n) {
        String[] lTokens = link.split(Constants.NODE_TOKENIZER);
        for (String s : lTokens) {
            if (n.equals(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PreAnomDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PreAnomDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PreAnomDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PreAnomDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PreAnomDialog dialog = new PreAnomDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea alertResultTxt;
    private javax.swing.JComboBox alertTypeCmb;
    private javax.swing.JButton browseBtn;
    private javax.swing.JButton browseUtilBtn;
    private javax.swing.JButton calcWeightAlertBtn;
    private javax.swing.JButton calcWeightBtn;
    private javax.swing.JButton calcWeightUtilBtn;
    private javax.swing.JLabel dataFolderLbl;
    private javax.swing.JLabel dataFolderUtilLbl;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton writeCSVBtn;
    private javax.swing.JButton writeCSVUtilBtn;
    // End of variables declaration//GEN-END:variables

    private HashMap<String, ArrayList<double[]>> getPreAnomWeightSeq(HashMap<String, ArrayList<String>> preAnomLinkMap, HashMap<String, ArrayList<String>> preAnomDevIDMap, HashMap<String, ArrayList<double[]>> nullWeightSeq, String fileNamePrefix, String inputDir) {
        InputParser iParser = new InputParser();
        HashMap<String, ArrayList<double[]>> linkWeightMap = new HashMap<>();
        for (Map.Entry<String, ArrayList<String>> e : preAnomLinkMap.entrySet()) {
            String key = e.getKey();

            int totalDevsForAnom = 0;
            int totalLinksForAnom = 0;

            ArrayList<double[]> weightSeqForAnom = new ArrayList<>();   //has tagchange vectors for all the link elements of an anom
            for (String link : e.getValue()) {
                ArrayList<String> allDevIDs = preAnomDevIDMap.get(link);
                totalDevsForAnom += allDevIDs.size();

                //this was introduced because of an observed bug. If the any of the devices are not present
                //in a given file, then it will result in an incomplete nonUtilWeightSeqForAnom
                //(because if devices are not there that double[] is empty
                ArrayList<double[]> tempWeightSeq = new ArrayList<>();

                String[] lTokens = link.split(Constants.NODE_TOKENIZER);
                for (int i = 0; i < DefaultValues.PAST_NODES_PRE_ANOMALY; i++) {
                    String s = lTokens[i];
                    String fileName = fileNamePrefix + (Integer.parseInt(s.split(Constants.I_J_TOKENIZER)[0]) + 1) + ".txt";

                    iParser.parseInput(inputDir + File.separator + fileName, allDevIDs);
                    ArrayList<double[]> weightsForLToken = iParser.getIWeights();

                    if (!weightsForLToken.isEmpty()) {
                        double[] tempWeightVec = new double[weightsForLToken.get(0).length];

                        //what happens here is that we cata all the double[] in tagChageList and
                        //accumulate them column wise into a single double[] (tempWeightVec)
                        for (double[] dArr : weightsForLToken) {
                            for (int j = 0; j < dArr.length; j++) {
                                tempWeightVec[j] += dArr[j];
                            }
                        }

                        tempWeightSeq.add(tempWeightVec);

                    } else {
                        tempWeightSeq.add(null);
                    }
                }

                if (!tempWeightSeq.contains(null)) {
                    for (int i = 0; i < DefaultValues.PAST_NODES_PRE_ANOMALY; i++) {
                        double[] currArr = new double[tempWeightSeq.get(i).length];;
                        if (weightSeqForAnom.size() == DefaultValues.PAST_NODES_PRE_ANOMALY) {
                            currArr = weightSeqForAnom.get(i);
                        }

                        for (int j = 0; j < currArr.length; j++) {
                            currArr[j] += tempWeightSeq.get(i)[j];
                        }

                        if (totalLinksForAnom == 0) {
                            weightSeqForAnom.add(currArr);
                        } else {
                            weightSeqForAnom.set(i, currArr);
                        }
                    }
                    totalLinksForAnom++;
                } else {
                    //we add items with null to a different arraylist
                    //because they are valuable because suddenly dissapearing before an anomaly is interesting
                    if (!nullWeightSeq.containsKey(key)) {
                        nullWeightSeq.put(key, tempWeightSeq);
                    } else {
                        int keyIdx = 1;
                        String newKey = key + keyIdx;
                        while (nullWeightSeq.containsKey(newKey)) {
                            keyIdx++;
                            newKey = key + keyIdx;
                        }
                        nullWeightSeq.put(newKey, tempWeightSeq);
                    }
                }
            }

            //Normalization - We normalize the nonUtilWeightSeqForAnom with #of Devs & #of Links
            for (int i = 0; i < weightSeqForAnom.size(); i++) {
                double[] arr = weightSeqForAnom.get(i);
                for (int j = 0; j < weightSeqForAnom.get(i).length; j++) {
                    if (totalDevsForAnom > 0) {
                        arr[j] = arr[j] / (totalDevsForAnom * totalLinksForAnom);
                    }
                }
                weightSeqForAnom.set(i, arr);
            }
            linkWeightMap.put(key, weightSeqForAnom);
        }
        return linkWeightMap;
    }
}
