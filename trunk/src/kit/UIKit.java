/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kit;

import global.DoxyApp;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JProgressBar;
import javax.swing.JSplitPane;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/**
 *
 * @author Rendra
 */
public class UIKit {
    /**
     * Set the divider split pane flat
     * 
     * @param jSplitPane 
     */
    public static void flattenSplitPane(JSplitPane jSplitPane) {
        jSplitPane.setUI(new BasicSplitPaneUI() {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    @Override
                    public void setBorder(Border b) { }
                };
            }
        });
        jSplitPane.setBorder(null);
    }
    
    /**
     * Used to show progress bar which waiting the background process
     * @param MainProgressBar 
     */
    public static void showProgressBar(final JProgressBar MainProgressBar) {
        MainProgressBar.setVisible(true);
        DoxyApp.progressCounter = 0;
        SwingWorker<Integer, Void> worker = new SwingWorker<Integer,Void>() {
            @Override
            public Integer doInBackground() {
                while (DoxyApp.progressCounter <= 100) {
                    setProgress(DoxyApp.progressCounter++);
                    try { Thread.sleep(300); } catch (InterruptedException e) {}
                }
                MainProgressBar.setVisible(false);
                return 0;
            }
        };
        worker.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                if ("progress".equals(event.getPropertyName())) {
                    MainProgressBar.setValue((Integer)event.getNewValue());
                }
            }
        });
        worker.execute();
    }
}
