/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */

package heinecke.sudoku;

import java.util.HashSet;
import java.util.Set;

/**
 * Class to group nine Fields (via subclasses Column, Row and Square
 * @author Johannes Heinecke <johannes dot heinecke at orange dot fr>
 */
public class CellGroup {

    protected  int size;
    protected  Cell[] cells;

    public CellGroup(int size) {
        this.size = size;
        cells = new Cell[size];
    }

    public Cell[] getCells() {
        return cells;
    }

    
    public void insert(int pos, Cell f) {//throws SkException {
        //if (this.getClass() == Square.class) System.out.println("  POS:" + pos + "");
        //if (pos < 0 || pos > size-1) throw new SkException("invalid position: " + pos);
        cells[pos] = f;
        if (this.getClass() == Row.class) {
            f.setMyRow((Row) this);
        } else if (this.getClass() == Column.class) {
            f.setMyColumn((Column) this);
        } else if (this.getClass() == Square.class) {
            f.setMySquare((Square) this);
        }
        //System.out.println(" " + getClass());
    }

    /** returns true, if the current group contains the number i */
    boolean hasNumber(Integer i) {
        for (int x = 0; x < size; x++) {
            //System.out.println("cells[" + x + "] = " + cells[x].getValue());
            Integer cur = cells[x].getValue();
            if (cur != null && cur.equals(i)) {
                return true;
            }
        }
        return false;
    }

    /** return all values of his group (make more intelligently ... */
    public Set<Integer> contains() {
        Set s = new HashSet<Integer>();
        for (int x = 0; x < size; x++) {
            //System.out.println("cells[" + x + "] = " + cells[x].getValue());
            Integer cur = cells[x].getValue();
            if (cur != null) {
                s.add(cur);
            }
        }
        return s;
    }

    /** return all values of his group (make more intelligently ... */
    public Set<Integer> misses() {
        Set s = new HashSet<Integer>();
        for (int x = 1; x <= size; x++) {
            if (!hasNumber(x)) {
                s.add(new Integer(x));
            }
        }

        return s;
    }

    public boolean solveHiddenSingles() {
        boolean modified = false;
        int countOccurrences;
        for (int x = 1; x<= size; x++) {
            countOccurrences = 0;
            Integer val = new Integer(x);
            for (Cell c : cells) {
                if (c.getCandidates().contains(val)) {
                    countOccurrences++;
                }
            }

            if (countOccurrences == 1) {
                // hidden single found
                for (Cell c : cells) {
                    if (c.getCandidates().contains(val)) {
                        modified = true;
                        c.setValue(val);
                    }
                }
            }
        }
        return modified;
    }
 
}
