/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */

package heinecke.sudoku;


/**
 * The entire 81 cells of the Sudoku field
 * @author Johannes Heinecke <johannes dot heinecke at orange dot fr>
 */
public class Field {

    private Cell[][] cells;
    private int size;
    //private int squaresize;
    private Row[] rows;
    private Column[] columns;
    private Square[] squares;
    private String errorMessage;

    public Field(int size) {
        this.size = size;
        //squaresize = (int)Math.sqrt(size);
        cells = new Cell[size][size];

        rows = new Row[size];
        columns = new Column[size];
        squares = new Square[size];
        //cells = new Cell[size][size];

        // create fields
        for (int x = 0; x < size; x++) {
            columns[x] = new Column(size);
            squares[x] = new Square(size);
            rows[x] = new Row(size);
        }
    }

    public Square[] getSquares() {
        return squares;
    }

    public boolean solveHiddenSingles() {
        boolean modified = false;
        for (Square sq : squares) {
            modified = sq.solveHiddenSingles() | modified;
        }
        for (Row sq : rows) {
            modified = sq.solveHiddenSingles() | modified ;
        }
        for (Column sq : columns) {
            modified = sq.solveHiddenSingles() | modified;
        }
        return modified;
    }

    public void insertCell(int row, int col, Cell cell) {
        cells[row][col] = cell;
        // make a link with the current row
        rows[row].insert(col, cell);
        // make a link with approriate column
        columns[col].insert(row, cell);

        // not very good ... make it more general !
        // System.out.println("CELL " + row/3 + " " + col/3);
        //squares[(row/Sudoku.sqaresize)*3 + (col/Sudoku.sqaresize)].insert((row/Sudoku.sqaresize)*3 + col/Sudoku.sqaresize, cell);
        int squarerow = row/Sudoku.sqaresize;
        int squarecol = col/Sudoku.sqaresize;

         //System.out.println("AA "  + (((row%Sudoku.sqaresize)*Sudoku.sqaresize)+((col%Sudoku.sqaresize))));
        //System.out.println("ROW:"+row+" COL:"+col);
        squares[squarecol + Sudoku.sqaresize*squarerow].insert(((row%Sudoku.sqaresize)*Sudoku.sqaresize) + ((col%Sudoku.sqaresize)), cell);
        /*if (col < 3) {
            if (row < 3) {
                //System.out.println("SR0 " + (squarecol + 3*squarerow) + " : " + col + " " + row*3);
                squares[0].insert(row * 3 + col, cell);
            } else if (row < 6) {
                 //System.out.println("SR3 " + (squarecol +  3*squarerow) + " : " + col + " " + (row-3)*3);
                squares[3].insert((row - 3) * 3 + col, cell);
            } else {
                 //System.out.println("SR6 " + (squarecol + 3*squarerow) + " : " + col + " " + (row-6)*3);
                squares[6].insert((row - 6) * 3 + col, cell);
            }
        } else if (col < 6) {
            if (row < 3) {
                 //System.out.println("SR1 " + (squarecol +  3*squarerow) + " : " + (col-3) + " " + row*3);
                squares[1].insert(row * 3 + col - 3, cell);
            } else if (row < 6) {
                 //System.out.println("SR4 " + (squarecol +  3*squarerow) + " : " + (col-3) + " " + (row-3)*3);
                squares[4].insert((row - 3) * 3 + col - 3, cell);
            } else {
                //System.out.println("SR7 " + (squarecol +  3*squarerow) + " : " + (col-3) + " " + (row-6)*3);
                squares[7].insert((row - 6) * 3 + col - 3, cell);
            }
        } else {
            if (row < 3) {
                squares[2].insert(row * 3 + col - 6, cell);
            } else if (row < 6) {
                squares[5].insert((row - 3) * 3 + col - 6, cell);
            } else {
                squares[8].insert((row - 6) * 3 + col - 6, cell);
            }
        }
*/
    }

    public Cell getCell(int row, int col) {
        return cells[row][col];
    }

    public Field copy() {
        Field f = new Field(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                f.insertCell(row, col, this.getCell(row, col).copy());
            }
        }
        return f;
    }

    public boolean check() {
        //Set<Cell> invalidCells = new HashSet<Cell>();
        boolean ok = true;
        ok = subcheck(rows);
        if (!ok) {
            return ok;
        }
        ok = subcheck(columns);
        if (!ok) {
            return ok;
        }
        ok = subcheck(squares);
        return ok;
    }

    /** check a row, column or square */
    private boolean subcheck(CellGroup[] groups) {
        boolean ok = true;
        for (CellGroup r : groups) {
            //Set<Integer>values = new HashSet<Integer>();
            // not very efficient
            for (Cell c : r.getCells()) {
                //if (!Sudoku.checkSymbol(c.getValue())) {
                //}
                for (Cell c2 : r.getCells()) {
                    if (c != c2
                            && c.getValue() != null
                            && c2.getValue() != null
                            && c.getValue().intValue() == c2.getValue().intValue()) {
                        c.setValid(false);
                        c2.setValid(false);
                        //System.out.println("CCC : " + c.getCol());
                      
                        errorMessage = "Fields " + c.getPos() + " and " + c2.getPos() + " are invalid.";
                        //invalidCells.add(c);
                        // invalidCells.add(c2);
                        ok = false;
                    }
                }
            }
        }
        return ok;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    

    public String toString() {
        String s = "";

        for (int row = 0; row < size; row++) {
            if (row % Sudoku.sqaresize == 0) {
                for (int x = 0; x < size; x++) {
                    s += "--------";
                }
                s += "\n";
            }
            s += "|";
            for (int col = 0; col < size; col++) {
                s += cells[row][col].show() + "\t";
                if (col % Sudoku.sqaresize == Sudoku.sqaresize-1) {
                    s += "|";
                }
            }
            s += "\n";
        }
        for (int x = 0; x < size; x++) {
            s += "--------";
        }
        s += "\n";

        return s;
    }
}
