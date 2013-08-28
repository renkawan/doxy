/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package doxy;

import global.DoxyApp;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import java.awt.Color;
import java.awt.Paint;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    public FrmParseFile() throws FileNotFoundException, ParseException, IOException {
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
    private void splitSource() throws FileNotFoundException, ParseException, IOException {
        DescB.setText(FileKit.getFileName(DoxyApp.bridge.getSelectedSrcFile()));
        
        // Clear both textarea, Comments and Methods section
        CommentsSection.setText("");
        MethodsSection.setText("");
        
        StringBuilder sb_cm = new StringBuilder();
        StringBuilder sb_fn = new StringBuilder();
        StringBuilder sb_en = new StringBuilder();
        StringBuilder sb_or = new StringBuilder();
        StringBuilder sb_ol = new StringBuilder();
        String readFile = FileKit.readSrcFile(DoxyApp.bridge.getSelectedSrcFile());
        
        /**
         * Optional
         * Parse the source code
         * 
         * Start
         */
        FileInputStream in = new FileInputStream(DoxyApp.bridge.getSelectedSrcFile());
        CompilationUnit cu;
        try {
            cu = JavaParser.parse(in);
        } finally {
            in.close();
        }
        
        String [] modifierType = {"", "public", "private", "", "protected"};
        List<TypeDeclaration> types = cu.getTypes();
        List<String> methodList = new ArrayList<>();
        List<String> methodName = new ArrayList<>();
        List<String> methodParm = new ArrayList<>();
        for (TypeDeclaration type : types) {
            List<BodyDeclaration> members = type.getMembers();
            for (BodyDeclaration member : members) {
                if (member instanceof MethodDeclaration) {
                    MethodDeclaration method = (MethodDeclaration) member;
                    String currMethod = modifierType[method.getModifiers()]+" "+method.getType()+" "+method.getName()+"("+method.getParameters()+") {";
                    methodList.add(currMethod);
                    methodName.add(method.getName());
                    String [] explode = currMethod.split("\\(");
                    String currParams = explode[1].replace(") {", "").replace("[", "").replace("]", "");
                    methodParm.add(currParams);
                }
            }
        }
        
        for (int i=0;i<methodList.size();i++) {
            int a = 0;
            for (int j=0;j<methodList.size();j++) {
                if (methodName.get(i).equals(methodName.get(j))) {
                    if (methodParm.get(i).equals(methodParm.get(j))) {
                        a++;
                    }
                }
            }
            if (a>1) {
                sb_ol.append(methodList.get(i)).append("\n\n");
            }
        }
        // End Optional Parse
        
        total_partcode = new int[4];
        total_empty_lines = 0;
        total_effectiveln = 0;
        total_comments_ln = 0;
        total_linesofcode = DoxyApp.bridge.getLineOfCode();
        
        /**
         * Komponen PBO
         * Enkapsulasi, Overriding, Overloading
         */
        Pattern p_e = Pattern.compile(DoxyApp.encMethod, Pattern.MULTILINE);
        Matcher m_e = p_e.matcher(readFile);
        while (m_e.find()) {
            sb_en.append(m_e.group()).append("\n\n");
        }
        
        Pattern p_o = Pattern.compile(DoxyApp.ovrMethod, Pattern.MULTILINE);
        Matcher m_o = p_o.matcher(readFile);
        while (m_o.find()) {
            sb_or.append(m_o.group()).append("\n\n");
        }
        
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
        
        EncapSection.setText(sb_en.toString());
        EncapSection.setCaretPosition(0);
        OverrSection.setText(sb_or.toString());
        OverrSection.setCaretPosition(0);
        OverlSection.setText(sb_ol.toString());
        OverlSection.setCaretPosition(0);
        
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
        DescA = new javax.swing.JLabel();
        DescB = new javax.swing.JLabel();
        LblComments = new javax.swing.JLabel();
        LblMethods = new javax.swing.JLabel();
        ParseGraph = new javax.swing.JPanel();
        ParsePBO = new javax.swing.JPanel();
        LblEnc = new javax.swing.JLabel();
        SPEnc = new javax.swing.JScrollPane();
        EncapSection = new javax.swing.JTextArea();
        LblOverr = new javax.swing.JLabel();
        SPOverr = new javax.swing.JScrollPane();
        OverrSection = new javax.swing.JTextArea();
        LblOverl = new javax.swing.JLabel();
        SPOverl = new javax.swing.JScrollPane();
        OverlSection = new javax.swing.JTextArea();

        CommentsSection.setColumns(20);
        CommentsSection.setRows(9);
        SPComments.setViewportView(CommentsSection);

        MethodsSection.setColumns(20);
        MethodsSection.setRows(8);
        SPMethods.setViewportView(MethodsSection);

        DescA.setText("Loaded file :");

        DescB.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        DescB.setText("...");

        LblComments.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        LblComments.setText("Comments");

        LblMethods.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        LblMethods.setText("Methods");

        javax.swing.GroupLayout ParseDiffLayout = new javax.swing.GroupLayout(ParseDiff);
        ParseDiff.setLayout(ParseDiffLayout);
        ParseDiffLayout.setHorizontalGroup(
            ParseDiffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ParseDiffLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ParseDiffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SPComments, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                    .addComponent(SPMethods)
                    .addGroup(ParseDiffLayout.createSequentialGroup()
                        .addGroup(ParseDiffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LblComments)
                            .addComponent(LblMethods)
                            .addGroup(ParseDiffLayout.createSequentialGroup()
                                .addComponent(DescA)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(DescB)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        ParseDiffLayout.setVerticalGroup(
            ParseDiffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ParseDiffLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LblComments)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SPComments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblMethods)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SPMethods, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ParseDiffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DescA)
                    .addComponent(DescB))
                .addContainerGap())
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
            .addGap(0, 431, Short.MAX_VALUE)
        );

        ParseTabbedPane.addTab("Source Graph", new javax.swing.ImageIcon(getClass().getResource("/assets/chart_pie.png")), ParseGraph); // NOI18N

        LblEnc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        LblEnc.setText("Enkapsulasi");

        EncapSection.setColumns(20);
        EncapSection.setRows(9);
        SPEnc.setViewportView(EncapSection);

        LblOverr.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        LblOverr.setText("Override");

        OverrSection.setColumns(20);
        OverrSection.setRows(9);
        SPOverr.setViewportView(OverrSection);

        LblOverl.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        LblOverl.setText("Overload");

        OverlSection.setColumns(20);
        OverlSection.setRows(9);
        SPOverl.setViewportView(OverlSection);

        javax.swing.GroupLayout ParsePBOLayout = new javax.swing.GroupLayout(ParsePBO);
        ParsePBO.setLayout(ParsePBOLayout);
        ParsePBOLayout.setHorizontalGroup(
            ParsePBOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ParsePBOLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ParsePBOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SPEnc, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                    .addComponent(SPOverr, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                    .addComponent(SPOverl, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                    .addGroup(ParsePBOLayout.createSequentialGroup()
                        .addGroup(ParsePBOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LblEnc)
                            .addComponent(LblOverr)
                            .addComponent(LblOverl))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        ParsePBOLayout.setVerticalGroup(
            ParsePBOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ParsePBOLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LblEnc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SPEnc, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblOverr)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SPOverr, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblOverl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SPOverl, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                .addContainerGap())
        );

        ParseTabbedPane.addTab("Komponen PBO", ParsePBO);

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
    private javax.swing.JTextArea EncapSection;
    private javax.swing.JLabel LblComments;
    private javax.swing.JLabel LblEnc;
    private javax.swing.JLabel LblMethods;
    private javax.swing.JLabel LblOverl;
    private javax.swing.JLabel LblOverr;
    private javax.swing.JTextArea MethodsSection;
    private javax.swing.JTextArea OverlSection;
    private javax.swing.JTextArea OverrSection;
    private javax.swing.JPanel ParseDiff;
    private javax.swing.JPanel ParseGraph;
    private javax.swing.JPanel ParsePBO;
    private javax.swing.JTabbedPane ParseTabbedPane;
    private javax.swing.JScrollPane SPComments;
    private javax.swing.JScrollPane SPEnc;
    private javax.swing.JScrollPane SPMethods;
    private javax.swing.JScrollPane SPOverl;
    private javax.swing.JScrollPane SPOverr;
    // End of variables declaration//GEN-END:variables
}
