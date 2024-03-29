/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.ui;

import com.maa.algo.ikasl.links.GlobalRawLinkGenerator;
import com.maa.algo.ikasl.links.IKASLLinkExtractor;
import com.maa.algo.utils.Constants;
import com.maa.vis.main.GNodeVisualizer;
import com.maa.main.IKASLFacade;
import com.maa.io.FileUtils;
import com.maa.listeners.ConfigCompleteListener;
import com.maa.listeners.DefaultValueListener;
import com.maa.listeners.IKASLStepListener;
import com.maa.models.AlgoParamModel;
import com.maa.models.BasicParamModel;
import com.maa.models.DataParamModel;
import com.maa.utils.CurrentJobState;
import com.maa.utils.DefaultValues;
import com.maa.utils.ImportantFileNames;
import com.maa.utils.PreAnomUtil;
import com.maa.utils.Tokenizers;
import com.maa.vis.objects.ReducedNode;
import com.maa.vis.objects.VisGNode;
import com.maa.xml.AlgoXMLParser;
import com.maa.xml.BasicXMLParser;
import com.maa.xml.DataXMLParser;
import com.maa.xml.IKASLOutputXMLParser;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Thush
 */
public class ResultsUI extends javax.swing.JFrame implements ChangeListener, ConfigCompleteListener, DefaultValueListener, IKASLStepListener {

    private BasicParamModel bPModel;
    private DataParamModel dPModel;
    private ArrayList<AlgoParamModel> aPModels;
    private ArrayList<IKASLFacade> ikaslList;
    private ArrayList<ArrayList<ReducedNode>> allNodes;
    ArrayList<VisGNode> allVisNodes;
    private Map<String, ArrayList<String>> dimensions;
    private int selectedStreamIdx;
    private String selectedStreamName;
    private VisualizeUIUtils visUtils;
    private JPanel visPanel;
    private IKASLLinkExtractor lnkExtractor;
    private GlobalRawLinkGenerator rawLinkGenerator;
    private int startLC = 0;
    /**
     * Creates new form ResultsUI
     */
    public ResultsUI() {
        initComponents();
        setLocationRelativeTo(null);
        setVisible(true);

        FileUtils.setUpConfigDir();

        if (!FileUtils.allConfigFilesExist()) {
            this.setFocusableWindowState(false);
            FileUtils.cleanConfigDir();
            introUI = new IntroUI();
            introUI.setVisible(true);
            introUI.setAlwaysOnTop(true);
            introUI.setListener(this);
        } else {
            this.setFocusableWindowState(true);
            bPModel = (BasicParamModel) new BasicXMLParser().parseXML();
            dPModel = (DataParamModel) new DataXMLParser().parseXML();
            aPModels = new AlgoXMLParser().parseMultiXML();

            ImportantFileNames.DATA_DIRNAME = dPModel.getHomeDir();
            FileUtils.setUpDataDir(bPModel.getStreamIDs());
            FileUtils.createDirs(bPModel.getStreamIDs(), ImportantFileNames.DATA_DIRNAME);

            initializeStreamsCombo();
            dimensions = getDimensionsOfStreams();
            initializeIKASLComponents();


        }

        selectedStreamIdx = streamCmb.getSelectedIndex();
        selectedStreamName = (String) streamCmb.getSelectedItem();

    }

    /**
     * Initialize the combo box which shows the Job (pattern extraction task) ID
     */
    private void initializeStreamsCombo() {
        for (String s : bPModel.getStreamIDs()) {
            streamCmb.addItem(s);
        }
    }

    /**
     * Initialize a list of IKASLFacade objects with user-specified parameters.
     * A single IKASLFacades is created for each Job
     */
    private void initializeIKASLComponents() {
        ikaslList = new ArrayList<>();
        for (AlgoParamModel aPModel : aPModels) {
            ikaslList.add(new IKASLFacade(aPModel.getStreamID(), aPModel, this, this));
        }
    }

    /**
     * This method returns the dimensions (attributes) of each job
     * Each job can use different attributes in data, to learn
     * @return A Hashmap where key is Job ID (String) and dimensions (Arraylist) are the value
     */
    private Map<String, ArrayList<String>> getDimensionsOfStreams() {
        Map<String, ArrayList<String>> dimNames = new HashMap<>();

        for (String s : bPModel.getStreamIDs()) {
            String loc = ImportantFileNames.DATA_DIRNAME + File.separator + s + File.separator + ImportantFileNames.ATTR_NAME_FILENAME;
            String text = FileUtils.readLines(loc).get(0);
            dimNames.put(s, new ArrayList<>(Arrays.asList(text.split(Tokenizers.INPUT_TOKENIZER))));
        }
        return dimNames;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        visContainerPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        anomaliesInfoBtn = new javax.swing.JButton();
        anomalousChk = new javax.swing.JCheckBox();
        anoTFCmb = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        anoSummaryLbl = new javax.swing.JLabel();
        potAnoChk = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        freqInfoBtn = new javax.swing.JButton();
        freqPatChk = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        fromPatTFCmb = new javax.swing.JComboBox();
        minLengthTxt = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        minStrengthTxt = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        freqPatLbl = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        minDevCountTxt = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        resourceInfoBtn = new javax.swing.JButton();
        runBtn = new javax.swing.JButton();
        summaryBtn = new javax.swing.JButton();
        statusProgressBar = new javax.swing.JProgressBar();
        jLabel4 = new javax.swing.JLabel();
        statusLbl = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        streamCmb = new javax.swing.JComboBox();
        updateBtn = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        tempLinksChk = new javax.swing.JCheckBox();
        saveImgBtn = new javax.swing.JButton();
        saveGnodeBtn = new javax.swing.JButton();
        saveAnomBtn = new javax.swing.JButton();
        printLinksBtn = new javax.swing.JButton();
        clusterQualityBtn = new javax.swing.JButton();
        preAnomBehavExtBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        visContainerPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Result Visuailzation"));

        javax.swing.GroupLayout visContainerPanelLayout = new javax.swing.GroupLayout(visContainerPanel);
        visContainerPanel.setLayout(visContainerPanelLayout);
        visContainerPanelLayout.setHorizontalGroup(
            visContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 708, Short.MAX_VALUE)
        );
        visContainerPanelLayout.setVerticalGroup(
            visContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 561, Short.MAX_VALUE)
        );

        getContentPane().add(visContainerPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(389, 52, 720, -1));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Anomalies"));

        anomaliesInfoBtn.setText("More Info >");

        anomalousChk.setText("Show Anomalous Clusters");
        anomalousChk.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                anomalousChkItemStateChanged(evt);
            }
        });
        anomalousChk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                anomalousChkActionPerformed(evt);
            }
        });

        anoTFCmb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                anoTFCmbItemStateChanged(evt);
            }
        });

        jLabel1.setText("Show anomalies for:");

        anoSummaryLbl.setText("Anomaly information will appear here");
        anoSummaryLbl.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        potAnoChk.setText("Show Pot. Ano. Clusters");
        potAnoChk.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                potAnoChkItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(anoSummaryLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(anoTFCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(potAnoChk, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(anomalousChk, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
                        .addComponent(anomaliesInfoBtn)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(anoTFCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(anoSummaryLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(potAnoChk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(anomaliesInfoBtn)
                    .addComponent(anomalousChk))
                .addContainerGap())
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 52, 365, -1));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Frequent Behavioral Patterns"));

        freqInfoBtn.setText("More Info >");
        freqInfoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                freqInfoBtnActionPerformed(evt);
            }
        });

        freqPatChk.setText("Show Frequent Patterns");
        freqPatChk.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                freqPatChkItemStateChanged(evt);
            }
        });
        freqPatChk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                freqPatChkActionPerformed(evt);
            }
        });

        jLabel2.setText("From");

        fromPatTFCmb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                fromPatTFCmbItemStateChanged(evt);
            }
        });

        minLengthTxt.setText("3");

        jLabel8.setText("Min Length:");

        jLabel9.setText("Min Strength:");

        minStrengthTxt.setText("60");

        freqPatLbl.setText("Patterns will appear here");
        freqPatLbl.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jScrollPane1.setViewportView(freqPatLbl);

        jLabel10.setText("Min # Devices:");

        minDevCountTxt.setText("10");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(freqPatChk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(freqInfoBtn))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(minDevCountTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(fromPatTFCmb, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(minStrengthTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(minLengthTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(minStrengthTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(fromPatTFCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(minLengthTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)))
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(minDevCountTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(freqInfoBtn)
                    .addComponent(freqPatChk))
                .addContainerGap())
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 239, 370, 260));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Resource Utilization Prediction"));

        resourceInfoBtn.setText("More Info >");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(252, Short.MAX_VALUE)
                .addComponent(resourceInfoBtn)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(63, Short.MAX_VALUE)
                .addComponent(resourceInfoBtn)
                .addContainerGap())
        );

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 516, 365, 120));

        runBtn.setText("Run Algo");
        runBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runBtnActionPerformed(evt);
            }
        });
        getContentPane().add(runBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 660, -1, -1));

        summaryBtn.setText("Show Summary");
        summaryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                summaryBtnActionPerformed(evt);
            }
        });
        getContentPane().add(summaryBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 660, -1, -1));
        getContentPane().add(statusProgressBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 672, -1, -1));

        jLabel4.setText("Status:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 647, -1, -1));

        statusLbl.setText("Display Current Status");
        getContentPane().add(statusLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(51, 647, -1, -1));

        jLabel5.setText("Time for Last Execution Cycle:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 644, -1, 20));

        jLabel6.setText("Last Cycle ID: ");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 670, -1, -1));

        jLabel7.setText("Show Results For:");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(304, 15, -1, -1));

        streamCmb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                streamCmbItemStateChanged(evt);
            }
        });
        getContentPane().add(streamCmb, new org.netbeans.lib.awtextra.AbsoluteConstraints(409, 12, -1, -1));

        updateBtn.setText("Update");
        updateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBtnActionPerformed(evt);
            }
        });
        getContentPane().add(updateBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(447, 11, -1, -1));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 41, 1119, -1));

        tempLinksChk.setSelected(true);
        tempLinksChk.setText("Show Temporal Links");
        tempLinksChk.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                tempLinksChkItemStateChanged(evt);
            }
        });
        getContentPane().add(tempLinksChk, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 10, -1, -1));

        saveImgBtn.setText("Save Img");
        saveImgBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveImgBtnActionPerformed(evt);
            }
        });
        getContentPane().add(saveImgBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 660, -1, -1));

        saveGnodeBtn.setText("Save GNodes (CSV)");
        saveGnodeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveGnodeBtnActionPerformed(evt);
            }
        });
        getContentPane().add(saveGnodeBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 660, -1, -1));

        saveAnomBtn.setText("Save Anom");
        saveAnomBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAnomBtnActionPerformed(evt);
            }
        });
        getContentPane().add(saveAnomBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 660, -1, -1));

        printLinksBtn.setText("Print Global Links");
        printLinksBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printLinksBtnActionPerformed(evt);
            }
        });
        getContentPane().add(printLinksBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 690, -1, -1));

        clusterQualityBtn.setText("Print Clus Quality");
        clusterQualityBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clusterQualityBtnActionPerformed(evt);
            }
        });
        getContentPane().add(clusterQualityBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 690, -1, -1));

        preAnomBehavExtBtn.setText("Pre-Anomaly Behavior");
        preAnomBehavExtBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                preAnomBehavExtBtnActionPerformed(evt);
            }
        });
        getContentPane().add(preAnomBehavExtBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 690, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void freqPatChkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_freqPatChkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_freqPatChkActionPerformed

    /**
     * Currently ikasl algorithm needs to be performed manually for each step.
     * But by using a timer's execute step and same logic specified in the methods, this can be automated
     * @param evt 
     */
    private void runBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runBtnActionPerformed

        if (!ikaslList.isEmpty()) {
            for (IKASLFacade ikasl : ikaslList) {

                ikasl.runSingleStep();
            }
        }
    }//GEN-LAST:event_runBtnActionPerformed

    private void summaryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_summaryBtnActionPerformed
        JOptionPane.showMessageDialog(null, createSettingsSummary(), "Settings Overview", JOptionPane.PLAIN_MESSAGE);

    }//GEN-LAST:event_summaryBtnActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        selectedStreamIdx = streamCmb.getSelectedIndex();
        selectedStreamName = (String) streamCmb.getSelectedItem();

        CurrentJobState.CURR_LC = ikaslList.get(selectedStreamIdx).getCurrLC();

        allNodes = loadLastSetOfLC(DefaultValues.IN_MEMORY_LAYER_COUNT);
        

        if (CurrentJobState.CURR_LC >= DefaultValues.IN_MEMORY_LAYER_COUNT) {
            startLC = CurrentJobState.CURR_LC - DefaultValues.IN_MEMORY_LAYER_COUNT + 1;
        }

        fillCombos();

        visUtils = new VisualizeUIUtils();
        GNodeVisualizer visualizer = new GNodeVisualizer();
        allVisNodes = visualizer.assignVisCoordinatesToGNodes(allNodes, startLC);

        HashMap<String, ArrayList<String>> anomIDs = getAnomalousClusters(startLC, CurrentJobState.CURR_LC, DefaultValues.ANOMALY_HIGH_THRESHOLD_DEFAULT);

        ArrayList<String> normIDs = getNormalClusters(startLC, CurrentJobState.CURR_LC, DefaultValues.NORMAL_THRESHOLD_DEFAULT);
        CurrentJobState.ALL_NORM_GNODES = new ArrayList<>(normIDs);

        ArrayList<String> potAnomIDs = getPotentialAnomalousClusters(startLC, CurrentJobState.CURR_LC);

        CurrentJobState.ANOM_GNODES_BY_TYPE = new HashMap<>(anomIDs);

        ArrayList<String> allAnom = new ArrayList<>();
        for (ArrayList<String> list : anomIDs.values()) {
            allAnom.addAll(list);
        }
        CurrentJobState.ALL_ANOM_GNODES = new ArrayList<>(allAnom);

        ArrayList<VisGNode> anoVNodes = getVisGNodesByID(allVisNodes, allAnom);
        ArrayList<VisGNode> potAnoVNodes = getVisGNodesByID(allVisNodes, potAnomIDs);

        String jobID = selectedStreamName;
        visUtils.setData(dimensions.get(jobID), allNodes, allVisNodes, anoVNodes, potAnoVNodes, null);
        visUtils.setParameters(startLC);
        initiateAndVisualizeResult(startLC);

        updateAnomalySummary();

        ikaslList.get(selectedStreamIdx).saveGlobalLinkUpdater();
    }//GEN-LAST:event_updateBtnActionPerformed

    private void anoTFCmbItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_anoTFCmbItemStateChanged
        updateAnomalySummary();
    }//GEN-LAST:event_anoTFCmbItemStateChanged

    private void streamCmbItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_streamCmbItemStateChanged
        selectedStreamIdx = streamCmb.getSelectedIndex();
        selectedStreamName = (String) streamCmb.getSelectedItem();
    }//GEN-LAST:event_streamCmbItemStateChanged

    private void anomalousChkItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_anomalousChkItemStateChanged
        if (anomalousChk.isSelected()) {
            visUtils.showAnomalousClusters();
        } else {
            visUtils.hideAnomalousClusters();
        }
    }//GEN-LAST:event_anomalousChkItemStateChanged

    private void freqPatChkItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_freqPatChkItemStateChanged
    }//GEN-LAST:event_freqPatChkItemStateChanged

    private void tempLinksChkItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_tempLinksChkItemStateChanged
        if (tempLinksChk.isSelected()) {
            if (!freqPatChk.isSelected()) {
                visUtils.showTemporalLinks(false);
                this.revalidate();
                this.repaint();
            } else {
                visUtils.showTemporalLinks(true);
                this.revalidate();
                this.repaint();
            }
        } else {
            if (!freqPatChk.isSelected()) {
                visUtils.hideLinks();
                this.revalidate();
                this.repaint();
            } else {
                visUtils.showIntersectionLinks(false);
                this.revalidate();
                this.repaint();
            }
        }
    }//GEN-LAST:event_tempLinksChkItemStateChanged

    private void fromPatTFCmbItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_fromPatTFCmbItemStateChanged
    }//GEN-LAST:event_fromPatTFCmbItemStateChanged

    private void saveImgBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveImgBtnActionPerformed
        if (visPanel != null) {
            createImage(visPanel);
        }
    }//GEN-LAST:event_saveImgBtnActionPerformed

    private void saveGnodeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveGnodeBtnActionPerformed
        String dimTokenizer = "_";
        ArrayList<ArrayList<String>> dataByTF = new ArrayList<>();
        ArrayList<String> reqDims = dimensions.get(selectedStreamName);

        for (int i = 0; i < allNodes.size(); i++) {
            ArrayList<String> data = new ArrayList<>();
            for (ReducedNode rn : allNodes.get(i)) {

                String currStr = "";
                currStr = reqDims.get(0).split(dimTokenizer)[0] + "-(" + rn.getId()[0] + ";" + rn.getId()[1] + ")" + "," + rn.getWeights()[0];
                for (int j = 1; j < reqDims.size(); j++) {

                    String cDim = reqDims.get(j).split(dimTokenizer)[0];
                    String pDim = reqDims.get(j - 1).split(dimTokenizer)[0];

                    if (cDim.equals(pDim)) {
                        currStr += "," + rn.getWeights()[j];
                    }
                    if (!cDim.equals(pDim)) {
                        data.add(currStr);
                        currStr = cDim + "-(" + rn.getId()[0] + ";" + rn.getId()[1] + ")" + "," + rn.getWeights()[j];
                    }
                }
            }
            dataByTF.add(data);
        }
        String[] attrPrefixes = new String[]{"MemoryPhysical", "MemoryVirtual", "ProcessorLoad", "NetworkSent", "NetworkRecv"};

        ArrayList<String> wData = new ArrayList<>();
        for (int i = 0; i < dataByTF.size(); i++) {
            wData.add("TimeFrame," + CurrentJobState.ALL_TIME_FRMS.get(i));
            for (String attr : reqDims) {
                for (String s : dataByTF.get(i)) {
                    if (s.contains(attr)) {
                        wData.add(s);
                    }
                }
                wData.add("\n");
            }
            wData.add("\n");
        }
        FileUtils.writeData(wData, "GNodeWeights.csv");
    }//GEN-LAST:event_saveGnodeBtnActionPerformed

    private void saveAnomBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAnomBtnActionPerformed
        populateAnomSummaryForGraph();
        ArrayList<String> wData = new ArrayList<>();

        for (Map.Entry<String, ArrayList<Double>> e : CurrentJobState.ANOM_SUMMARY.entrySet()) {
            wData.add(e.getKey());
            for (int i = 0; i < CurrentJobState.ALL_TIME_FRMS.size(); i++) {
                wData.add(CurrentJobState.ALL_TIME_FRMS.get(i) + "," + e.getValue().get(i));
            }
            wData.add("\n");
        }

        FileUtils.writeData(wData, "Anom.csv");
    }//GEN-LAST:event_saveAnomBtnActionPerformed

    private void freqInfoBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_freqInfoBtnActionPerformed
    }//GEN-LAST:event_freqInfoBtnActionPerformed

    private void potAnoChkItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_potAnoChkItemStateChanged
        if (potAnoChk.isSelected()) {
            visUtils.showPotAnomalousClusters();
        } else {
            visUtils.hidePotAnomalousClusters();
        }
    }//GEN-LAST:event_potAnoChkItemStateChanged

    private void printLinksBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printLinksBtnActionPerformed
        allNodes = loadLastSetOfLC(DefaultValues.IN_MEMORY_LAYER_COUNT);

        ArrayList<String> links = ikaslList.get(selectedStreamIdx).getGlobalLinkUpdater().getAllLinksWithoutMergedLeftOvers();
        ArrayList<String> linksWithMLeftOvers = ikaslList.get(selectedStreamIdx).getGlobalLinkUpdater().getAllLinksWithMergedLeftOvers();

        System.out.println("\n Links Before Filtering ---------------------------------------");
        for (String s : links) {
            System.out.println(s);
        }

        
        System.out.println("");
        HashMap<String, String> mLinks = ikaslList.get(selectedStreamIdx).getGlobalLinkUpdater().getMergedLinks();
        /*
        for (Map.Entry<String, String> e : mLinks.entrySet()) {
            System.out.println(e.getKey() + " -> " + e.getValue());
        }*/

        rawLinkGenerator = new GlobalRawLinkGenerator();
        ArrayList<String> rawLinks = rawLinkGenerator.getRawLinkGenerator2(linksWithMLeftOvers, mLinks);
        lnkExtractor = new IKASLLinkExtractor(rawLinks, mLinks, allNodes,startLC);
        HashMap<String, ArrayList<String>> linkMap = lnkExtractor.performFiltering();
        CurrentJobState.FILT_LINKS = new HashMap<>(linkMap);

        ArrayList<String> wData = new ArrayList<>();
        System.out.println("Filtered Links ---------------------------------");
        
        for (Map.Entry<String, ArrayList<String>> e : linkMap.entrySet()) {
            String result = e.getKey() + " -> ";
            for (String s : e.getValue()) {
                result += s + ",";
            }
            System.out.println(result);

            String startLink = "\n";
            wData.add(startLink);
            String[] linkTokens = e.getKey().split(Constants.NODE_TOKENIZER);

            //formatting to match CSV format
            for (int i = 0; i < linkTokens.length; i++) {
                String s = linkTokens[i];
                String line = s + ",";
                int lc = Integer.parseInt(s.split(Constants.I_J_TOKENIZER)[0]);
                int id = Integer.parseInt(s.split(Constants.I_J_TOKENIZER)[1]);

                for (int j = 0; j < allNodes.get(lc-startLC).size(); j++) {
                    ReducedNode rn = allNodes.get(lc-startLC).get(j);
                    if (rn.getId()[0] == lc && rn.getId()[1] == id) {
                        String weightStr = "";
                        for (double d : rn.getWeights()) {
                            if (d < 0.1) {
                                d = 0;
                            } else if (d > 0.9) {
                                d = 1;
                            }
                            weightStr += d + ",";
                        }
                        line += weightStr;
                        break;
                    }
                }

                wData.add(line);
            }
        }

        //writing to CSV
        FileUtils.writeData(wData, "LinkData.csv");

        //percentage of nodes with 1st attr <0.1 or >0.9
        int ambIdx0 = 0;
        int ambIdx2 = 0;
        int ambIdx15 = 0;
        int total = 0;
        for (int i = 0; i < allNodes.size(); i++) {
            for (int j = 0; j < allNodes.get(i).size(); j++) {
                ReducedNode rn = allNodes.get(i).get(j);
                if (rn.getWeights()[0] > 0.2 && rn.getWeights()[0] < 0.7) {
                    ambIdx0++;
                }
                if ((rn.getWeights()[2] > 0.1 && rn.getWeights()[2] < 0.4) || (rn.getWeights()[2] > 0.6 && rn.getWeights()[2] < 0.9)) {
                    ambIdx2++;
                }
                if ((rn.getWeights()[15] > 0.1 && rn.getWeights()[15] < 0.4) || (rn.getWeights()[15] > 0.6 && rn.getWeights()[15] < 0.9)) {
                    ambIdx15++;
                }
                total++;
            }
        }

        System.out.println("Percentage Ambiguous Idx 0: " + (double) (ambIdx0 * 100 / total));
        System.out.println("Percentage Ambiguous Idx 2: " + (double) (ambIdx2 * 100 / total));
        System.out.println("Percentage Ambiguous Idx 15: " + (double) (ambIdx15 * 100 / total));
    }//GEN-LAST:event_printLinksBtnActionPerformed

    private void clusterQualityBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clusterQualityBtnActionPerformed
        ArrayList<ArrayList<double[]>> allClusterQVals = ikaslList.get(selectedStreamIdx).getClusterQualityMeasures();
        System.out.println("RMS_STD,R_SQR");
        for (int i = 0; i < allClusterQVals.size(); i++) {
            System.out.println("Layer " + i);
            for (double[] arr : allClusterQVals.get(i)) {
                System.out.println(arr[0] + "," + arr[1]);
            }
        }
    }//GEN-LAST:event_clusterQualityBtnActionPerformed

    private void anomalousChkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_anomalousChkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_anomalousChkActionPerformed

    private void preAnomBehavExtBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_preAnomBehavExtBtnActionPerformed
        ArrayList<String> preAnomLinkList = new ArrayList<>();
        HashMap<String, ArrayList<String>> preAnomDevIDMap = new HashMap<>();

        ArrayList<String> normalLinkList = new ArrayList<>();
        HashMap<String, ArrayList<String>> normalLinkDevIDMap = new HashMap<>();
        PreAnomUtil.updatePreAnomalyData(preAnomLinkList, preAnomDevIDMap, normalLinkList, normalLinkDevIDMap, false, true);

        //create a hash map with preAnomLinkList which maps Anom to Link
        HashMap<String, ArrayList<String>> preAnomLinkMap = new HashMap<>();
        PreAnomUtil.mapAnomsToPreAnomLinks(preAnomLinkList, preAnomLinkMap);

        PreAnomDialog paDialog = new PreAnomDialog(null, false);
        paDialog.setBasicData(allNodes);
        paDialog.setPreAnomData(preAnomLinkList, preAnomLinkMap, preAnomDevIDMap);
        paDialog.setNormalData(normalLinkList, normalLinkDevIDMap);
        paDialog.setAlertTypes(dimensions.get(selectedStreamName));
        paDialog.populateAlertCmb();
        paDialog.setVisible(true);
    }//GEN-LAST:event_preAnomBehavExtBtnActionPerformed

    private ArrayList<VisGNode> getVisGNodesByID(ArrayList<VisGNode> nodes, ArrayList<String> copyOfIdStrings) {
        ArrayList<VisGNode> vNodes = new ArrayList<>();
        Iterator<String> it = copyOfIdStrings.iterator();
        while (it.hasNext()) {
            String s = it.next();
            String[] tokens = s.split(Tokenizers.I_J_TOKENIZER);
            for (VisGNode vgn : nodes) {
                if (copyOfIdStrings.isEmpty()) {
                    return vNodes;
                }
                if (vgn.getID()[0] == Integer.parseInt(tokens[0])
                        && vgn.getID()[1] == Integer.parseInt(tokens[1])) {
                    vNodes.add(vgn);
                    it.remove();
                }
            }
        }
        return vNodes;
    }

    private void updateAnomalySummary() {
        Map<String, Double> anomaliesHigh = new HashMap<>(getHighestAnomalyPercentage(selectedStreamName, anoTFCmb.getSelectedIndex()));
        Map<String, Double> maxAnomalies = new HashMap<>();
        int count = Math.min(5, anomaliesHigh.size());
        for (int i = 0; i < count; i++) {
            double maxVal = 0.0;
            String maxKey = null;
            for (String key : anomaliesHigh.keySet()) {
                if (anomaliesHigh.get(key) > maxVal) {
                    maxVal = anomaliesHigh.get(key);
                    maxKey = key;
                }
            }
            if (maxKey != null) {
                anomaliesHigh.remove(maxKey);
                maxAnomalies.put(maxKey, maxVal);
            }
        }

        String result = "<html>";
        for (String s : maxAnomalies.keySet()) {
            result += s + ": " + "<b>" + maxAnomalies.get(s) + "%" + "</b><br/>";
        }
        result += "<html/>";
        anoSummaryLbl.setText(result);

    }

    private void fillCombos() {

        Vector anoVec = new Vector();
        Vector fromPatVec = new Vector();

        for (String s : CurrentJobState.ALL_TIME_FRMS) {
            anoVec.add(s);
            fromPatVec.add(s);
        }

        DefaultComboBoxModel defAnoCmbModel = new DefaultComboBoxModel(anoVec);
        DefaultComboBoxModel defFromPatCmbModel = new DefaultComboBoxModel(fromPatVec);

        anoTFCmb.setModel(defAnoCmbModel);
        fromPatTFCmb.setModel(defFromPatCmbModel);
    }

    private String createSettingsSummary() {
        String result = "<html><b>Basic Parameters</b><br/> "
                + "Frequency: " + bPModel.getFreq().name() + "<br/>"
                + "Number of Streams: " + bPModel.getNumStreams() + "<br/>"
                + "Stream IDs: " + getStreamIDString() + "<br/>"
                + "<br/>"
                + "<b> Data Parameters </b> <br/>"
                + "Home Dir: " + dPModel.getHomeDir() + "<br/>"
                + "<br/>"
                + "<b> Algo Parameters </b> <br/>"
                + getAlgoParametersString()
                + "</html>";

        return result;
    }

    private String getStreamIDString() {
        String result = bPModel.getStreamIDs().get(0);
        for (String s : bPModel.getStreamIDs()) {
            if (!s.equals(bPModel.getStreamIDs().get(0))) {
                result += ", " + s;
            }
        }
        return result;
    }

    private String getAlgoParametersString() {
        String result = "";
        for (AlgoParamModel aPModel : aPModels) {
            result += "Stream ID: " + aPModel.getStreamID() + "<br/>"
                    + "Spread Factor: " + aPModel.getSpreadFactor() + "<br/>"
                    + "Learning Rate: " + aPModel.getLearningRate() + "<br/>"
                    + "Neighborhood Radius: " + aPModel.getNeighRad() + "<br/>"
                    + "Iterations: " + aPModel.getIterations() + "<br/>"
                    + "Hit Threshold: " + aPModel.getHitThreshold() + "<br/>"
                    + "Aggregation: " + aPModel.getAggrType().name() + "<br/>"
                    + "<br/>";
        }

        return result;
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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;


                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ResultsUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ResultsUI().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel anoSummaryLbl;
    private javax.swing.JComboBox anoTFCmb;
    private javax.swing.JButton anomaliesInfoBtn;
    private javax.swing.JCheckBox anomalousChk;
    private javax.swing.JButton clusterQualityBtn;
    private javax.swing.JButton freqInfoBtn;
    private javax.swing.JCheckBox freqPatChk;
    private javax.swing.JLabel freqPatLbl;
    private javax.swing.JComboBox fromPatTFCmb;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField minDevCountTxt;
    private javax.swing.JTextField minLengthTxt;
    private javax.swing.JTextField minStrengthTxt;
    private javax.swing.JCheckBox potAnoChk;
    private javax.swing.JButton preAnomBehavExtBtn;
    private javax.swing.JButton printLinksBtn;
    private javax.swing.JButton resourceInfoBtn;
    private javax.swing.JButton runBtn;
    private javax.swing.JButton saveAnomBtn;
    private javax.swing.JButton saveGnodeBtn;
    private javax.swing.JButton saveImgBtn;
    private javax.swing.JLabel statusLbl;
    private javax.swing.JProgressBar statusProgressBar;
    private javax.swing.JComboBox streamCmb;
    private javax.swing.JButton summaryBtn;
    private javax.swing.JCheckBox tempLinksChk;
    private javax.swing.JButton updateBtn;
    private javax.swing.JPanel visContainerPanel;
    // End of variables declaration//GEN-END:variables
    private IntroUI introUI;
    private BasicParameterUI bUI;
    private DataParameterUI dataUI;
    private AlgoParameterUI algoUI;

    @Override
    public void allConfigCompleted() {
        this.setFocusableWindowState(true);
        aPModels = algoUI.getAlgoParamModels();
        algoUI.dispose();

        selectedStreamIdx = streamCmb.getSelectedIndex();
        selectedStreamName = (String) streamCmb.getSelectedItem();

        ImportantFileNames.DATA_DIRNAME = dPModel.getHomeDir();
        FileUtils.setUpDataDir(bPModel.getStreamIDs());

        initializeStreamsCombo();
        initializeIKASLComponents();
    }

    @Override
    public void basicConfigCompleted() {
        dataUI = new DataParameterUI();
        dataUI.setVisible(true);
        dataUI.setListener(this);
        bPModel = bUI.getBasicParamModel();
        bUI.dispose();
    }

    @Override
    public void dataConfigCompleted() {
        algoUI = new AlgoParameterUI();
        algoUI.setVisible(true);
        algoUI.setListener(this);
        dPModel = dataUI.getDataParamModel();
        dataUI.dispose();
    }

    @Override
    public void introConfigCompleted() {
        bUI = new BasicParameterUI();
        bUI.setVisible(true);
        bUI.setListener(this);
        introUI.dispose();
    }

    @Override
    public void useDefaultBounds(String stream) {
        JOptionPane.showMessageDialog(null, "Stream: " + stream + " Using Default Values for Min Max Bounds", "Warning", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void useDefaultWeights(String stream) {
        JOptionPane.showMessageDialog(null, "Stream: " + stream + " Using Default Values for Weights", "Warning", JOptionPane.WARNING_MESSAGE);
    }

    private int getProgressPortionsForIKASLStep() {
        return 100 / bPModel.getNumStreams();
    }
    int ikaslStepCount = 0;

    @Override
    public void IKASLStepCompleted(String stream) {
        int currVal = statusProgressBar.getValue() + getProgressPortionsForIKASLStep();
        statusProgressBar.setValue(currVal);
        statusProgressBar.repaint();
        ikaslStepCount++;

        statusLbl.setText("Execution Complete for Stream: " + stream);
        if (ikaslStepCount == bPModel.getNumStreams()) {
            statusLbl.setText("Execution Complete");
        }
    }

    private ArrayList<ArrayList<ReducedNode>> loadLastSetOfLC(int count) {
        CurrentJobState.ALL_TIME_FRMS = new ArrayList<>();
        IKASLOutputXMLParser ikaslXMLParser = new IKASLOutputXMLParser();
        ArrayList<ArrayList<ReducedNode>> nodes = new ArrayList<>();

        if (CurrentJobState.CURR_LC < count) {
            for (int i = 0; i <= CurrentJobState.CURR_LC; i++) {
                String loc = ImportantFileNames.DATA_DIRNAME + File.separator
                        + bPModel.getStreamIDs().get(selectedStreamIdx) + File.separator + "LC" + i + ".xml";
                nodes.add(ikaslXMLParser.parseXML(loc));
                CurrentJobState.ALL_TIME_FRMS.add(ikaslXMLParser.getTimeFram());
            }
        } else {
            for (int i = CurrentJobState.CURR_LC - count + 1; i <= CurrentJobState.CURR_LC; i++) {
                String loc = ImportantFileNames.DATA_DIRNAME + File.separator
                        + bPModel.getStreamIDs().get(selectedStreamIdx) + File.separator + "LC" + i + ".xml";
                nodes.add(ikaslXMLParser.parseXML(loc));
                CurrentJobState.ALL_TIME_FRMS.add(ikaslXMLParser.getTimeFram());
            }
        }

        return nodes;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        this.revalidate();
        this.repaint();
    }

    private void initiateAndVisualizeResult(int startLC) {
        visContainerPanel.removeAll();

        visPanel = visUtils.getVisJPanel();

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.getViewport().addChangeListener(this);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(new EtchedBorder());

        int horizontalStartingGap = 8;
        int verticalStartngGap = 15;
        int scrollPaneWidth = visContainerPanel.getPreferredSize().width - (2 * horizontalStartingGap);
        int scrollPaneHeight = visContainerPanel.getPreferredSize().height - (2 * verticalStartngGap);

        if (visPanel.getPreferredSize().width < visContainerPanel.getPreferredSize().width) {
            scrollPaneWidth = visPanel.getPreferredSize().width + 30;
        }
        if (visPanel.getPreferredSize().height < visContainerPanel.getPreferredSize().height) {
            //20 is there because otherwise when this conditions occur, vertical bar appears
            scrollPaneHeight = visPanel.getPreferredSize().height + 25;
        }

        scrollPane.setBounds(horizontalStartingGap, verticalStartngGap, scrollPaneWidth, scrollPaneHeight);
        scrollPane.setViewportView(visPanel);
        visContainerPanel.add(scrollPane);

        this.revalidate();
        this.repaint();
    }

    private void createImage(JPanel panel) {
        BufferedImage bi = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        panel.print(g);
        try {
            ImageIO.write(bi, "png", new File("result.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void populateAnomSummaryForGraph() {
        CurrentJobState.ANOM_SUMMARY = new HashMap<>();
        for (int i = 0; i < CurrentJobState.ALL_TIME_FRMS.size(); i++) {
            Map<String, Double> currAnom = getHighestAnomalyPercentage(selectedStreamName, i);
            for (Map.Entry<String, Double> e : currAnom.entrySet()) {
                if (!CurrentJobState.ANOM_SUMMARY.containsKey(e.getKey())) {
                    ArrayList<Double> valList = new ArrayList<>();
                    for (int j = 0; j < i; j++) {
                        valList.add(0.0);
                    }
                    valList.add(e.getValue());
                    CurrentJobState.ANOM_SUMMARY.put(e.getKey(), valList);
                } else {
                    ArrayList<Double> currValList = CurrentJobState.ANOM_SUMMARY.get(e.getKey());
                    currValList.add(e.getValue());
                    CurrentJobState.ANOM_SUMMARY.put(e.getKey(), currValList);
                }
            }

            //bringing all vectors to same number of elements
            for (Map.Entry<String, ArrayList<Double>> e : CurrentJobState.ANOM_SUMMARY.entrySet()) {
                if (e.getValue().size() != CurrentJobState.ALL_TIME_FRMS.size()) {
                    ArrayList<Double> currValList = e.getValue();
                    for (int k = 0; k <= i - currValList.size(); k++) {
                        currValList.add(0.0);
                    }
                    e.setValue(currValList);
                }
            }
        }
    }

    private Map<String, Double> getHighestAnomalyPercentage(String stream, int lc) {
        Map<String, Double> anomaliesWithPercent = new HashMap<>();
        ArrayList<ReducedNode> nodes = allNodes.get(lc);
        int total = 0;

        for (ReducedNode rn : nodes) {
            for (int i = 0; i < rn.getWeights().length; i++) {
                if (rn.getWeights()[i] > DefaultValues.ANOMALY_HIGH_THRESHOLD_DEFAULT) {
                    String key = dimensions.get(stream).get(i);
                    if (!anomaliesWithPercent.containsKey(key)) {
                        anomaliesWithPercent.put(key, (double)rn.getInputs().size());
                    } else {
                        double val = anomaliesWithPercent.get(key);
                        val += rn.getInputs().size();
                        anomaliesWithPercent.put(key, val);
                    }
                }
            }
            total += rn.getInputs().size();
        }

        for (Map.Entry<String, Double> e : anomaliesWithPercent.entrySet()) {
            e.setValue(e.getValue() * 100.0 / total);
        }

        return anomaliesWithPercent;
    }

    private HashMap<String, ArrayList<String>> getAnomalousClusters(int startLC, int endLC, double thresh) {
        HashMap<String, ArrayList<String>> anomClusters = new HashMap<>();
        for (int i = 0; i <= endLC - startLC; i++) {
            ArrayList<ReducedNode> nodes = allNodes.get(i);
            for (ReducedNode rn : nodes) {
                String id = rn.getId()[0] + Tokenizers.I_J_TOKENIZER + rn.getId()[1];
                String key = "";
                for (int j = 0; j < rn.getWeights().length; j++) {
                    if (ikaslList.get(selectedStreamIdx).getAlgoParam().getATTR_WEIGHTS()[j] != 0
                            && rn.getWeights()[j] > thresh) {

                        key += getDimensionsOfStreams().get(selectedStreamName).get(j) + Constants.INPUT_TOKENIZER;
                    }
                }
                //we add new finindgs to hashmap only if key is not empty
                if (!key.isEmpty()) {
                    key = key.substring(0, key.length() - 1);
                    ArrayList<String> anomIDsForKey = anomClusters.get(key);
                    if (anomIDsForKey == null) {
                        anomIDsForKey = new ArrayList<>();
                    }
                    if (!anomIDsForKey.contains(id)) {
                        anomIDsForKey.add(id);
                        anomClusters.put(key, anomIDsForKey);
                    }
                }
            }
        }

        return anomClusters;
    }

    private ArrayList<String> getNormalClusters(int startLC, int endLC, double thresh) {
        ArrayList<String> normClusters = new ArrayList<>();
        for (int i = 0; i <= endLC - startLC; i++) {
            ArrayList<ReducedNode> nodes = allNodes.get(i);
            for (ReducedNode rn : nodes) {
                boolean isNormal = false;
                String id = rn.getId()[0] + Tokenizers.I_J_TOKENIZER + rn.getId()[1];
                for (int j = 0; j < rn.getWeights().length; j++) {
                    if (ikaslList.get(selectedStreamIdx).getAlgoParam().getATTR_WEIGHTS()[j] != 0
                            && rn.getWeights()[j] > thresh) {
                        isNormal = false;
                        break;
                    }
                    isNormal = true;
                }
                if (isNormal) {
                    normClusters.add(id);
                }
            }
        }
        return normClusters;
    }

    private ArrayList<String> getPotentialAnomalousClusters(int startLC, int endLC) {
        ArrayList<String> potAnomClusters = new ArrayList<>();
        for (int i = 0; i <= endLC - startLC; i++) {
            ArrayList<ReducedNode> nodes = allNodes.get(i);
            for (ReducedNode rn : nodes) {
                for (int j = 0; j < rn.getWeights().length; j++) {
                    if (rn.getWeights()[j] > DefaultValues.POT_ANOMALY_HIGH_THRESHOLD_DEFAULT
                            && rn.getWeights()[j] < DefaultValues.ANOMALY_HIGH_THRESHOLD_DEFAULT) {
                        String id = rn.getId()[0] + Tokenizers.I_J_TOKENIZER + rn.getId()[1];
                        if (!potAnomClusters.contains(id)) {
                            potAnomClusters.add(id);
                        }
                    }
                }
            }
        }

        return potAnomClusters;
    }

    private VisGNode getVisGNodeWithID(ArrayList<VisGNode> list, int lc, int id) {
        for (VisGNode vgn : list) {
            if (vgn.getID()[0] == lc && vgn.getID()[1] == id) {
                return vgn;
            }
        }
        return null;
    }
    
    class ReducedNodeInputComparator implements Comparator<ReducedNode> {

        @Override
        public int compare(ReducedNode o1, ReducedNode o2) {
            if (o1.getInputs().size() >= o2.getInputs().size()) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    class ValueComparator implements Comparator<String> {

        Map<String, Integer> base;

        public ValueComparator(Map<String, Integer> base) {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with equals.    
        public int compare(String a, String b) {
            if (base.get(a) >= base.get(b)) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
    }
}
