/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package doxy;

import global.DoxyApp;
import java.awt.Color;
import java.awt.Paint;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import kit.FileKit;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Rendra
 */
public class FrmParseFile extends javax.swing.JInternalFrame {
    
    private int [] total_partcode;
    private int total_empty_lines, total_effectiveln, total_comments_ln;
    private int total_linesofcode;
    
    /**
     * Creates new form FrmParseFile
     */
    public FrmParseFile() {
        initComponents();
        if (DoxyApp.bridge.getSelectedSrcFile()==null) {
            JOptionPane.showMessageDialog(null, "Please choose a file first", "Choose file", JOptionPane.WARNING_MESSAGE);
        } else {
            splitSource();
            createGraph();
        }
    }

    /**
     * This method split the source into two parts
     * First part is record all comments of source code
     * Second part is record all methods
     */
    private void splitSource() {
        DescB.setText(FileKit.getFileName(DoxyApp.bridge.getSelectedSrcFile()));
        
        // Clear both textarea, Comments and Methods section
        CommentsSection.setText("");
        MethodsSection.setText("");
        
        StringBuilder sb_cm = new StringBuilder();
        StringBuilder sb_fn = new StringBuilder();
        String readFile = FileKit.readSrcFile(DoxyApp.bridge.getSelectedSrcFile());
        
        total_partcode = new int[4];
        total_empty_lines = 0;
        total_effectiveln = 0;
        total_comments_ln = 0;
        total_linesofcode = DoxyApp.bridge.getLineOfCode();
        
        Pattern p_c = Pattern.compile(DoxyApp.rxComment, Pattern.MULTILINE);
        Matcher m_c = p_c.matcher(readFile);
        while (m_c.find()) {
            total_comments_ln += DoxyApp.bridge.getCommentsLine(m_c.group());
            sb_cm.append(m_c.group()).append("\n\n");
        }
        
        Pattern p_s = Pattern.compile(DoxyApp.rxMethod, Pattern.MULTILINE);
        Matcher m_s = p_s.matcher(readFile);
        while (m_s.find()){
            sb_fn.append(m_s.group()).append("}").append("\n\n");
        }
        
        Pattern p_t = Pattern.compile(DoxyApp.rxEmptyLine, Pattern.MULTILINE);
        Matcher m_t = p_t.matcher(readFile);
        while (m_t.find()){
            total_empty_lines++;
        }
        
        total_effectiveln = total_linesofcode - total_empty_lines;
        total_partcode[0] = total_linesofcode;
        total_partcode[1] = total_empty_lines;
        total_partcode[2] = total_comments_ln;
        total_partcode[3] = total_effectiveln;
        
        CommentsSection.setText(sb_cm.toString());
        MethodsSection.setText(sb_fn.toString());
        CommentsSection.setCaretPosition(0);
        MethodsSection.setCaretPosition(0);
        
        // Reset visited last line from selected source code file
        DoxyApp.bridge.setLastLine(0);
    }
    
    /**
     * Create summary graph from selected source
     */
    private void createGraph() {
        CategoryDataset dataset = createBarDataset();
        JFreeChart chart = createBarChart(dataset, "Source Graph Summary");
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(575, 405));
        ParseGraph.setLayout(new BoxLayout(ParseGraph, BoxLayout.LINE_AXIS));
        ParseGraph.add(chartPanel);
    }
    
    private CategoryDataset createBarDataset() {
        DefaultCategoryDataset result = new DefaultCategoryDataset();
        result.addValue(total_partcode[2], "Total", "Comment "+total_partcode[2]);
        result.addValue(total_partcode[1], "Total", "Empty Line "+total_partcode[1]);
        result.addValue(total_partcode[3], "Total", "Effective Line "+total_partcode[3]);
        result.addValue(total_partcode[0], "Total", "Line Of Code "+total_partcode[0]);
        return result;
    }
    
    private JFreeChart createBarChart(CategoryDataset dataset, String title) {
        JFreeChart chart = ChartFactory.createBarChart(title, "Source Code Component", "Total", dataset, PlotOrientation.VERTICAL, false, true, false);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setRangeGridlinePaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.BLACK);
        final CategoryItemRenderer renderer = new CustomRenderer(
            new Paint[] {Color.orange, Color.magenta, Color.yellow, Color.red}
        );
        plot.setRenderer(renderer);
        chart.getTitle().setPadding(5, 5, 5, 5);
        chart.getTitle().setFont(new java.awt.Font("Tahoma", 1, 16));
        return chart;
    }
    
    class CustomRenderer extends BarRenderer {

        /** The colors. */
        private Paint[] colors;

        /**
         * Creates a new renderer.
         *
         * @param colors  the colors.
         */
        public CustomRenderer(final Paint[] colors) {
            this.colors = colors;
        }

        /**
         * Returns the paint for an item.  Overrides the default behaviour inherited from
         * AbstractSeriesRenderer.
         *
         * @param row  the series.
         * @param column  the category.
         *
         * @return The item color.
         */
        @Override
        public Paint getItemPaint(final int row, final int column) {
            return this.colors[column % this.colors.length];
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

        ParseTabbedPane = new javax.swing.JTabbedPane();
        ParseDiff = new javax.swing.JPanel();
        SPComments = new javax.swing.JScrollPane();
        CommentsSection = new javax.swing.JTextArea();
        SPMethods = new javax.swing.JScrollPane();
        MethodsSection = new javax.swing.JTextArea();
        DescC = new javax.swing.JLabel();
        DescA = new javax.swing.JLabel();
        DescB = new javax.swing.JLabel();
        ParseGraph = new javax.swing.JPanel();

        CommentsSection.setColumns(20);
        CommentsSection.setRows(9);
        SPComments.setViewportView(CommentsSection);

        MethodsSection.setColumns(20);
        MethodsSection.setRows(8);
        SPMethods.setViewportView(MethodsSection);

        DescC.setText("Top : Comments Section. Bottom : Methods Section");

        DescA.setText("Loaded file :");

        DescB.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        DescB.setText("...");

        javax.swing.GroupLayout ParseDiffLayout = new javax.swing.GroupLayout(ParseDiff);
        ParseDiff.setLayout(ParseDiffLayout);
        ParseDiffLayout.setHorizontalGroup(
            ParseDiffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ParseDiffLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ParseDiffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SPComments)
                    .addComponent(SPMethods)
                    .addGroup(ParseDiffLayout.createSequentialGroup()
                        .addGroup(ParseDiffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(DescC)
                            .addGroup(ParseDiffLayout.createSequentialGroup()
                                .addComponent(DescA)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(DescB)))
                        .addGap(0, 123, Short.MAX_VALUE)))
                .addContainerGap())
        );
        ParseDiffLayout.setVerticalGroup(
            ParseDiffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ParseDiffLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SPComments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SPMethods, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ParseDiffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DescA)
                    .addComponent(DescB))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DescC)
                .addGap(6, 6, 6))
        );

        ParseTabbedPane.addTab("Comments & Method", new javax.swing.ImageIcon(getClass().getResource("/assets/application_tile_horizontal.png")), ParseDiff); // NOI18N

        javax.swing.GroupLayout ParseGraphLayout = new javax.swing.GroupLayout(ParseGraph);
        ParseGraph.setLayout(ParseGraphLayout);
        ParseGraphLayout.setHorizontalGroup(
            ParseGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 389, Short.MAX_VALUE)
        );
        ParseGraphLayout.setVerticalGroup(
            ParseGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 346, Short.MAX_VALUE)
        );

        ParseTabbedPane.addTab("Source Graph", new javax.swing.ImageIcon(getClass().getResource("/assets/chart_pie.png")), ParseGraph); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ParseTabbedPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ParseTabbedPane)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea CommentsSection;
    private javax.swing.JLabel DescA;
    private javax.swing.JLabel DescB;
    private javax.swing.JLabel DescC;
    private javax.swing.JTextArea MethodsSection;
    private javax.swing.JPanel ParseDiff;
    private javax.swing.JPanel ParseGraph;
    private javax.swing.JTabbedPane ParseTabbedPane;
    private javax.swing.JScrollPane SPComments;
    private javax.swing.JScrollPane SPMethods;
    // End of variables declaration//GEN-END:variables
}
