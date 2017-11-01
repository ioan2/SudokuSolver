/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */

/*
 * CellGroup.java
 *
 * Created on 8 dec. 2011, 16:35:26
 */
package heinecke.sudoku.panel;

import java.awt.Dimension;
import heinecke.sudoku.Cell;
import heinecke.sudoku.Square;
import heinecke.sudoku.Sudoku;

/**
 * Contains 9 cells
 * @author Johannes Heinecke <johannes heinecke at orange dot fr>
 */
public class SquarePanel extends javax.swing.JPanel {

    CellPanel[] cellPanels;

    /** Creates new form CellGroup */
    public SquarePanel() {
        super();

        initComponents();
        cellPanels = new CellPanel[Sudoku.size];
        setPreferredSize(new Dimension(200, 200));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        setVisible(true);

        for (int i = 0; i < Sudoku.size; i++) {
            CellPanel cp = addCell(i / Sudoku.sqaresize, i % Sudoku.sqaresize);
            cellPanels[i] = cp;
        }
    }

    public void setEditable(boolean v) {
        for (int i = 0; i < Sudoku.size; i++) {
            //System.out.println("ddd " + i + " "+ cells[i].show());
            cellPanels[i].setEditable(v);
        }
    }

    public void setValues(Square s, boolean color) {
        if (s != null) {
            Cell[] cells = s.getCells();
            for (int i = 0; i < Sudoku.size; i++) {
                //System.out.println("ddd " + i + " "+ cells[i].show());
                if (cells[i].getValue() != null) {
                    cellPanels[i].setValue(cells[i], color);
                } else {
                    cellPanels[i].setValue(cells[i], false);
                    cellPanels[i].setCandidates(cells[i].getCandidates());

                }
            }
        } else {
            for (int i = 0; i < Sudoku.size; i++) {
                cellPanels[i].setValue(null, false);
            }
        }
    }

    public void putValues(Square s) {
        Cell[] cells = s.getCells();
        for (int i = 0; i < Sudoku.size; i++) {
            //System.out.println("eee " + i + " "+cellPanels[i].getValue());

            if (cellPanels[i].getValue() != 0) {
                cells[i].setValue(cellPanels[i].getValue());
            }
        }
    }

//    public CellPanel[] getCells() { // Kann weg
//        return cellPanels;
//    }
    private CellPanel addCell(int row, int col) {
        //System.out.println("inserting " + row + " " + col);
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = col;
        gridBagConstraints.gridy = row;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        //gridBagConstraints.ipadx = 1;
        //gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;

        CellPanel cp = new CellPanel();
        add(cp, gridBagConstraints);
        return cp;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(246, 186, 15));
        setPreferredSize(new java.awt.Dimension(122, 292));
        setLayout(new java.awt.GridBagLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}