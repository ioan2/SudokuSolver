/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */

package heinecke.sudoku;

/**
 *
 * @author Johannes Heinecke <johannes dot heinecke at orange dot fr>
 */
public class Row extends CellGroup {

    public Row(int size) {
        super(size);
    }

    public String toString() {
        String s = "";
        for (int x = 0; x < size; x++) {
            s += cells[x].show() + "\t";
        }
        return s;
    }
}
