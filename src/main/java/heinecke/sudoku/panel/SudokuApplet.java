/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */

package heinecke.sudoku.panel;

import javax.swing.JApplet;
import heinecke.sudoku.Sudoku;

/**
 *
 * @author Johannes Heinecke <johannes heinecke at wanadoo dotfr>
 */
public class SudokuApplet extends JApplet {

    private FieldPanel fieldPanel;
    private Sudoku sk;

    /**
     * The applet's init() method creates the button and display panel and
     * adds them to the applet, and it sets up a listener to respond to
     * clicks on the button.
     */
    public void init() {
        fieldPanel = new FieldPanel();
        fieldPanel.setVisible(true);
        this.getContentPane().add(fieldPanel);
    }
}
