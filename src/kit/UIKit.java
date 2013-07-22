/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kit;

import javax.swing.JSplitPane;
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
}
