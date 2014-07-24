/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.ui;

import com.maa.algo.ikasl.auxi.GNodeVisualizer;
import com.maa.algo.ikasl.core.IKASLFacade;
import com.maa.io.FileUtils;
import com.maa.listeners.ConfigCompleteListener;
import com.maa.listeners.DefaultValueListener;
import com.maa.listeners.IKASLStepListener;
import com.maa.models.AlgoParamModel;
import com.maa.models.BasicParamModel;
import com.maa.models.DataParamModel;
import com.maa.utils.DefaultValues;
import com.maa.utils.ImportantFileNames;
import com.maa.utils.InputParser;
import com.maa.vis.objects.ReducedNode;
import com.maa.xml.AlgoXMLParser;
import com.maa.xml.BasicXMLParser;
import com.maa.xml.DataXMLParser;
import com.maa.xml.IKASLOutputXMLParser;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Thush
 */
public class ResultsUI extends javax.swing.JFrame implements ConfigCompleteListener, DefaultValueListener, IKASLStepListener {

    private BasicParamModel bPModel;
    private DataParamModel dPModel;
    private ArrayList<AlgoParamModel> aPModels;
    private ArrayList<IKASLFacade> ikaslList;

    private int lastLC;
    private ArrayList<String> lastTimeFrames;
    
    private int selectedStreamIdx;
    
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
            initializeIKASLComponents();
        }

        selectedStreamIdx = streamCmb.getSelectedIndex();
    }

    private void initializeStreamsCombo(){
        for (String s : bPModel.getStreamIDs()) {
            streamCmb.addItem(s);
        }
    }
    
    private void initializeIKASLComponents() {
        ikaslList = new ArrayList<>();
        for (AlgoParamModel aPModel : aPModels) {
            ikaslList.add(new IKASLFacade(aPModel.getStreamID(), aPModel, this, this));
        }
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
        jPanel2 = new javax.swing.JPanel();
        anomaliesInfoBtn = new javax.swing.JButton();
        anomalousChk = new javax.swing.JCheckBox();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        freqInfoBtn = new javax.swing.JButton();
        jCheckBox2 = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jComboBox3 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Result Visuailzation"));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 655, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 561, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Anomalies"));

        anomaliesInfoBtn.setText("More Info >");

        anomalousChk.setText("Show Anomalous Clusters");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel1.setText("Show anomalies for:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(anomalousChk)
                        .addGap(46, 46, 46)
                        .addComponent(anomaliesInfoBtn))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 104, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(anomaliesInfoBtn)
                    .addComponent(anomalousChk))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Frequent Behavioral Patterns"));

        freqInfoBtn.setText("More Info >");

        jCheckBox2.setText("Show Frequent Patterns");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jLabel2.setText("Show patterns for time frame:");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setText("to");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jCheckBox2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(freqInfoBtn))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 99, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(freqInfoBtn)
                    .addComponent(jCheckBox2))
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Resource Utilization Prediction"));

        resourceInfoBtn.setText("More Info >");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(resourceInfoBtn)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(147, Short.MAX_VALUE)
                .addComponent(resourceInfoBtn)
                .addContainerGap())
        );

        runBtn.setText("Run Algo");
        runBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runBtnActionPerformed(evt);
            }
        });

        summaryBtn.setText("Show Summary");
        summaryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                summaryBtnActionPerformed(evt);
            }
        });

        jLabel4.setText("Status:");

        statusLbl.setText("Display Current Status");

        jLabel5.setText("Time for Last Execution Cycle:");

        jLabel6.setText("Last Cycle ID: ");

        jLabel7.setText("Show Results For:");

        updateBtn.setText("Update");
        updateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(statusProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(38, 38, 38)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(runBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(summaryBtn))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(statusLbl)
                                .addGap(35, 35, 35)
                                .addComponent(jLabel5))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(304, 304, 304)
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(streamCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(updateBtn)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jSeparator1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(streamCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(updateBtn))
                .addGap(7, 7, 7)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(statusLbl)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(statusProgressBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(runBtn)
                        .addComponent(summaryBtn)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox2ActionPerformed
    int currLC = 0;
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
        ArrayList<ArrayList<ReducedNode>> allNodes = loadLastSetOfLC(DefaultValues.IN_MEMORY_LAYER_COUNT);
        GNodeVisualizer visualizer = new GNodeVisualizer();
        visualizer.assignVisCoordinatesToGNodes(allNodes);
    }//GEN-LAST:event_updateBtnActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ResultsUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ResultsUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ResultsUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ResultsUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.JButton anomaliesInfoBtn;
    private javax.swing.JCheckBox anomalousChk;
    private javax.swing.JButton freqInfoBtn;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton resourceInfoBtn;
    private javax.swing.JButton runBtn;
    private javax.swing.JLabel statusLbl;
    private javax.swing.JProgressBar statusProgressBar;
    private javax.swing.JComboBox streamCmb;
    private javax.swing.JButton summaryBtn;
    private javax.swing.JButton updateBtn;
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

    private int getProgressPortionsForIKASLStep(){
        return 100/bPModel.getNumStreams();
    }
    
    int ikaslStepCount = 0;
    @Override
    public void IKASLStepCompleted(String stream) {
        int currVal = statusProgressBar.getValue()+getProgressPortionsForIKASLStep();
        statusProgressBar.setValue(currVal);
        statusProgressBar.repaint();
        ikaslStepCount++;
        
        statusLbl.setText("Execution Complete for Stream: "+stream);
        if(ikaslStepCount==bPModel.getNumStreams()){
            statusLbl.setText("Execution Complete");
        }
    }
    
    private ArrayList<ArrayList<ReducedNode>> loadLastSetOfLC(int count){
        IKASLOutputXMLParser ikaslXMLParser = new IKASLOutputXMLParser();
        ArrayList<ArrayList<ReducedNode>> allNodes = new ArrayList<>();
        int currLC = ikaslList.get(selectedStreamIdx).getCurrLC();
        if(currLC<count-1){
            for(int i=0;i<=currLC;i++){
                String loc = ImportantFileNames.DATA_DIRNAME+File.separator+
                        bPModel.getStreamIDs().get(selectedStreamIdx)+File.separator+"LC"+i+".xml";
                 allNodes.add(ikaslXMLParser.parseXML(loc));
            }
        } else {
            for(int i=currLC-count+1;i<=currLC;i++){
                String loc = ImportantFileNames.DATA_DIRNAME+File.separator+
                        bPModel.getStreamIDs().get(selectedStreamIdx)+File.separator+"LC"+i+".xml";
                 allNodes.add(ikaslXMLParser.parseXML(loc));
            }
        }
        
        return allNodes;
    }
}
