/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package doxy;

import global.DoxyApp;
import japa.parser.JavaParser;
import japa.parser.ast.Comment;
import japa.parser.ast.CompilationUnit;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import kit.FileKit;

/**
 *
 * @author Rendra
 */
public class FrmParseTranslate extends javax.swing.JInternalFrame {
    private CompilationUnit cu;
    
    /**
     * Creates new form FrmParseTranslate
     */
    public FrmParseTranslate() throws Exception {
        initComponents();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (DoxyApp.bridge.getSelectedSrcFile()!=null) {
                    new Translate().execute();
                } else {
                    TAOriginal.setText("");
                    TAResult.setText("");
                    TAOriginal.setEnabled(false);
                    TAResult.setEnabled(false);
                    LblStatus.setText("Please wait ...");            
                }
            }
        });
    }
    
    /**
     * Translate process
     * With loading status
     */
    private class Translate extends SwingWorker<String[], String> {
        private final JLabel label = new JLabel("  Please wait ...", 
                new ImageIcon(this.getClass().getResource("/assets/loading.gif")), 
                SwingConstants.TRAILING);
        private final JPanel panel;
        private final JDialog dialog;
        
        public Translate() {
            JFrame frm = (JFrame)getTopLevelAncestor();
            dialog = new JDialog(frm);
            panel = new JPanel();
            TAOriginal.setText("");
            TAResult.setText("");
            TAOriginal.setEnabled(false);
            TAResult.setEnabled(false);
            LblStatus.setText("Please wait ...");
            dialog.setUndecorated(true);
            dialog.setUndecorated(true);
            label.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
            panel.setBorder(BorderFactory.createLineBorder(new Color(189, 199, 216), 1));
            panel.setBackground(new Color(237, 239, 244));
            panel.setLayout(new GridBagLayout());
            panel.add(label);
            dialog.add(panel);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        }
        
        @Override
        protected String[] doInBackground() throws Exception {
            Thread.sleep(700);
            publish("  Translating ...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ix) { }

            // Starting process
            String slctdFile = DoxyApp.bridge.getSelectedSrcFile();
            FileInputStream in = new FileInputStream(slctdFile);
            CompilationUnit comp;
            try {
                comp = JavaParser.parse(in);
            } finally {
                in.close();
            }
            List<Comment> srcComments = comp.getComments();
            List<Comment> newComments = new ArrayList<>();
            StringBuilder oriComments = new StringBuilder();
            StringBuilder resComments = new StringBuilder();
            for (Comment cm : srcComments){
                oriComments.append(cm.toString());
                String [] contents = cm.getContent().split("\n");
                StringBuilder bContent = FileKit.extractAndTranslate(contents);
                cm.setContent(bContent.toString());
                resComments.append(cm.toString());
                newComments.add(cm);
            }
            publish("  Done");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ix) { }
            String[] res = {oriComments.toString(), newComments.toString()};
            return res;
        }

        @Override
        protected void process(List<String> chunks) {
            label.setText(chunks.get(0));
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.repaint();
        }

        @Override
        protected void done() {
            try {                    
                String[] comments = this.get();
                TAOriginal.setEnabled(true);
                TAResult.setEnabled(true);
                TAOriginal.setText(comments[0]);
                TAResult.setText(comments[1]);
                TAOriginal.setCaretPosition(0);
                TAResult.setCaretPosition(0);
                LblStatus.setText(FileKit.getFileName(DoxyApp.bridge.getSelectedSrcFile())+" is successfully translated");
                dialog.setVisible(false);
            } catch (InterruptedException | ExecutionException e) { }
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

        SPOri = new javax.swing.JScrollPane();
        TAOriginal = new javax.swing.JTextArea();
        LblOriginal = new javax.swing.JLabel();
        LblResult = new javax.swing.JLabel();
        SPRes = new javax.swing.JScrollPane();
        TAResult = new javax.swing.JTextArea();
        LblWarning = new javax.swing.JLabel();
        SaveSrcBtn = new javax.swing.JButton();
        LblStatus = new javax.swing.JLabel();

        TAOriginal.setEditable(false);
        TAOriginal.setColumns(20);
        TAOriginal.setRows(9);
        SPOri.setViewportView(TAOriginal);

        LblOriginal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        LblOriginal.setText("Original");

        LblResult.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        LblResult.setText("Result");

        TAResult.setColumns(20);
        TAResult.setRows(5);
        SPRes.setViewportView(TAResult);

        LblWarning.setForeground(new java.awt.Color(255, 0, 0));
        LblWarning.setText("Please make sure if the original source is in english.");

        SaveSrcBtn.setText("Save Source As");
        SaveSrcBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveSrcBtnActionPerformed(evt);
            }
        });

        LblStatus.setText("successfully translated.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(LblWarning)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(LblStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(SaveSrcBtn))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SPOri, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LblOriginal, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(LblResult)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(SPRes, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(LblWarning)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LblOriginal)
                    .addComponent(LblResult))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SPRes)
                    .addComponent(SPOri, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SaveSrcBtn)
                    .addComponent(LblStatus)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SaveSrcBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveSrcBtnActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("D:/"));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Java", ".java"));
        chooser.setSelectedFile(new File(FileKit.getFileName(DoxyApp.bridge.getSelectedSrcFile())));
        int retrival = chooser.showSaveDialog(null);
        if (retrival == JFileChooser.APPROVE_OPTION) {
            try {
                String selectedName = chooser.getSelectedFile().toString();
                File newFile;
                if (selectedName.endsWith(".java")) {
                    newFile = new File(selectedName);
                } else {
                    newFile = new File(selectedName+".java");
                }
                FileWriter fw = new FileWriter(newFile);
                fw.write(cu.toString());
                fw.close();
                JOptionPane.showMessageDialog(null, "Source saved successfully", "File Saved", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException | HeadlessException ex) { 
                Logger.getLogger(FrmParseTranslate.class.getName()).log(Level.SEVERE, null, ex); 
            }
        }
    }//GEN-LAST:event_SaveSrcBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LblOriginal;
    private javax.swing.JLabel LblResult;
    private javax.swing.JLabel LblStatus;
    private javax.swing.JLabel LblWarning;
    private javax.swing.JScrollPane SPOri;
    private javax.swing.JScrollPane SPRes;
    private javax.swing.JButton SaveSrcBtn;
    private javax.swing.JTextArea TAOriginal;
    private javax.swing.JTextArea TAResult;
    // End of variables declaration//GEN-END:variables
}
