/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */


package heinecke.sudoku;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A class wich contains the number of a single sudoku cell
 * aor its potential numbers as well as impossible numbers
 * @author Johannes Heinecke
 */
public class Cell {

    private Integer value = null;
    private Set<Integer> candidates = null;
    //Set<Integer> ruledOut = null;
    private Row myRow;
    private Column myColumn;
    private Square mySquare;
    private int row;
    private int col;
    private boolean valid; // there is no other cell in row/column/square with same value
    public boolean beingModified;

    public Cell(int row, int col) {
        candidates = new HashSet<Integer>();
        //ruledOut = new HashSet<Integer>();
        this.row = row;
        this.col = col;
        beingModified = false;
        valid = true;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    

    public void setMyColumn(Column myColumn) {
        this.myColumn = myColumn;
    }

    public void setMyRow(Row myRow) {
        this.myRow = myRow;
    }

    public void setMySquare(Square mySquare) {
        this.mySquare = mySquare;
    }

    public void setCandidates(Set<Integer> candidates) {
        this.candidates = candidates;
    }

    public Set<Integer> getCandidates() {
        return candidates;
    }

//    public void setRuledOut(Set<Integer> ruledOut) {
//        this.ruledOut = ruledOut;
//    }
    public void setValue(Integer value) {
        this.value = value;
        valid = true;
    }

    /** force the first value of candidates */
    public Integer forceValue(Integer exclude) {
        valid = true;
        while (!candidates.isEmpty()) {
            value = (Integer) candidates.toArray()[0]; //[candidates.size()-1];
            candidates.remove(this.value);
            if (exclude == null || exclude.equals(value))
            return value;
        }
        return null;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }
    
    public Integer getValue() {
        return value;
    }

    public String show() {
        if (value != null) {
            return "  " + Integer.toString(value, 36) + "  ";
        } else if (!candidates.isEmpty()) {
            Iterator<Integer> it = candidates.iterator();
            String s = "[";
            while (it.hasNext()) {
                Integer i = it.next();
                s += Integer.toString(i, 36);// + ",";
            }
            s += "]";
            return s;
        } else {
            return " ... ";
        }
    }

    /** return the position in the field as string */
    public String getPos() {
        return "[" + col + "," + row + "]";
    }

    /** calculate values if possible */
    public boolean calculate() {
        boolean modified = false;
        candidates.clear();
        // solving "naked single "
        if (value == null) {
            for (int x = 1; x <= Sudoku.size; x++) {
                //System.out.println("ddd " + myRow);
                if (!myRow.hasNumber(x)
                        && !myColumn.hasNumber(x)
                        && !mySquare.hasNumber(x)) {
                    //System.out.println(row + " " + col + ": GOT " + x );
                    candidates.add(new Integer(x));
                }
            }
            Set<Integer> missing = mySquare.misses();
            if (candidates.size() == 1) {
                value = (Integer) candidates.toArray()[0];
                //System.out.println("FINALLY " + row + " " + col + ": GOT " + value);
                candidates.clear();
                modified = true;
            }

            if (missing.size() == 1) {
                value = (Integer) missing.toArray()[0];
                //System.out.println("FINALLY " + row + " " + col + ": GOT " + value);
                candidates.clear();
                modified = true;
            }
            
        }

        return modified;
    }

    /** copy of the Field (needed for backtracking to store the current state */
    public Cell copy() {
        Cell f = new Cell(row, col);
        if (value != null) {
            f.setValue(value);
        }

        Set<Integer> clone = new HashSet<Integer>();
        Iterator<Integer> it = candidates.iterator();
        while (it.hasNext()) {
            Integer v = it.next();
            clone.add(new Integer(v));
        }
        f.setCandidates(clone);

        clone = new HashSet<Integer>();
//        it = ruledOut.iterator();
//        while (it.hasNext()) {
//            Integer v = it.next();
//            clone.add(new Integer(v));
//        }
//        f.setRuledOut(clone);

        return f;
    }
}
