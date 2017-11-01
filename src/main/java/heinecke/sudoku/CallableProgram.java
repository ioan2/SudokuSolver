/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heinecke.sudoku;

/**
 * Interface for the callback in Sudoku.calculate
 * @author Johannes Heinecke <johannes.heinecke@orange.fr>
 */
public interface CallableProgram {
    public void display(Sudoku sk);
}
