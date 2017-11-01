/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */

package heinecke.sudoku;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * The sudoku field
 * @author Johannes Heinecke <johannes dot heinecke at orange fr>
 * TODO naked/hidden pairs,
 * Wenn man statt Ziffern etwas anderes eingibt, wird der Wert nicht abgelehnt
 */
public class Sudoku extends Thread {

    public boolean pleaseWait; // wait if demanded
    //private boolean totallySolved; // need by thread
    Status totallySolved;
    private boolean stopAtStep;
    private CallableProgram cp; // callback

    public static int size = 9; // must be a number with an integer root (4, 9)
    public static int sqaresize = 3; // (int)Math.sqrt(size);
    public static String inValidSymbol = "[^0-9]"; // regexp to check whether symbol is valid

    int countIterations = 0;
    //int countRecursions = 0;
    private String errorMessage;
    //Cell[][] cells; // keep the Cells
    Field field;
    int[][] initialValues; //= new int[][]{
//        {5, 6, 0, 7, 0, 8, 0, 2, 0},
//        {3, 0, 0, 0, 0, 0, 0, 0, 9},
//        {0, 0, 9, 0, 0, 0, 1, 0, 0},
//        {2, 0, 0, 9, 7, 4, 0, 0, 6},
//        {0, 1, 0, 0, 0, 0, 0, 4, 0},
//        {7, 0, 0, 2, 1, 3, 0, 0, 8},
//        {0, 0, 7, 0, 0, 0, 5, 0, 0},
//        {6, 0, 0, 0, 0, 0, 0, 0, 1},
//        {0, 9, 0, 3, 0, 1, 0, 8, 0}
//    };


    public enum Status { SOLVED, NOT_YET_SOLVED, NO_SOLUTION };

    public static void setSize(int s) {
        size = s*s;
        sqaresize = s;
        switch (s) {
            case 4: inValidSymbol = "[^0-9A-Ga-g]"; break;
            case 5: inValidSymbol = "[^0-9A-Pa-p]"; break;
            case 6: inValidSymbol = "[^0-9A-Za-z]"; break;
            default:
                case 3: inValidSymbol = "[^0-9]";
                size = 9;
                sqaresize = 3;
        }
    }

    public static boolean checkSymbol(String s) {
        if (s.length() != 1) return false;
        else if (!s.matches(inValidSymbol)) return false;
        return true;
    }

    public Sudoku() {
        // create arrays
        initialValues = new int[size][size];
        field = new Field(size);
        //totallySolved = false;
        totallySolved = Status.NOT_YET_SOLVED;
        stopAtStep = false;
    }

    public void setStopAtStep(boolean stopAtStep) {
        this.stopAtStep = stopAtStep;
    }

    public void setCp(CallableProgram cp) {
        this.cp = cp;
    }

    public Status getTotallySolved() {
        return totallySolved;
    }

    public Sudoku copy(Sudoku s) {
        Sudoku sk = new Sudoku();
        sk.field = s.getField().copy();
        return sk;
    }

    public void setValue(int[][] vals) {
        initialValues = vals;
    }

    public Field getField() {
        return field;
    }

    public void init(boolean setValues) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                // create a cell and stock it in the field
                Cell curCell = new Cell(row, col);
                if (setValues && initialValues[row][col] != 0) {
                    curCell.setValue(new Integer(initialValues[row][col]));
                }
                //cells[row][col] = curCell;
                field.insertCell(row, col, curCell);
            }
        }
        //Set<Cell> invalid = field.check();
        //System.out.println("IVC:" + invalid.size());
    }

    public boolean readFile(String filename) {
        try {
            BufferedReader ifp = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "ISO-8859-1"));
            int countlines = 0;
            while (ifp.ready() && countlines < size) {
                String line = ifp.readLine();

                if (line.length() < size) {
                    System.err.println("Input line '" + line + "' too short ("+size+" digits needed");
                    return false;
                }
                for (int i = 0; i < size; i++) {
                    if (line.substring(i, i + 1).matches(inValidSymbol)) {
//                    if ( (size == 9  && line.substring(i, i + 1).matches("[^0-9]") )
//                         || (size == 16 && line.substring(i, i + 1).matches("[^0-9A-Ga-g]") )
//                         || (size == 25 && line.substring(i, i + 1).matches("[^0-9A-Pa-p]") )
//                         || (size == 4  && line.substring(i, i + 1).matches("[^0-4]") )
//                         ) {
                        initialValues[countlines][i] = 0;
                    } else {
                        initialValues[countlines][i] = Integer.parseInt(line.substring(i, i + 1), 36);
                    }
                }
                countlines++;
            }
            if (countlines < size) {
                System.err.println("Not enough lines");
                return false;
            }
        } catch (Exception ex) {
            System.err.println("ERROR: " + ex.getMessage());
            return false;
        }
        return true;
    }

    /** get cell with the least number of candidates */
    private Cell getCell() {
        Cell chosenCell = null;
        int candidateNumber = Sudoku.size;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Cell curCell = field.getCell(row, col);
                if (!curCell.getCandidates().isEmpty() && curCell.beingModified == false) {
                    //return curCell;

                    if (curCell.getCandidates().size() < candidateNumber) {
                        candidateNumber = curCell.getCandidates().size();
                        chosenCell = curCell;
                    }
                }
            }
        }
        // return null;
        return chosenCell;
    }

    /** for usage as a thread
    This method is called when the thread runs
     */
    public void run() {
        totallySolved = calculate();
        //System.out.println("DISPLAY " + this);
        if (cp != null) {
            cp.display(this);
        }
    }

    public String getErrorMessage() {
        return errorMessage;
    }



    /* @return true if solved */
    public Status calculate() {
        //System.out.println("CALC\n" + field);
        //countRecursions++;
        boolean modified = false;
        Status solved = Status.SOLVED;
        do {
            modified = false;
            solved = Status.SOLVED;

            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    //System.out.println("..CALCULATE " + row + " " + col);
                    // calculating naked single
                    modified = field.getCell(row, col).calculate() || modified;
                    if (field.getCell(row, col).getValue() == null) {
                        // toujours pas de solution
                        if (field.getCell(row, col).getCandidates().isEmpty()) {
                            //System.out.println("NO SOLUTION FOR: [" + row + "," + col + "]");//"\n" + field);
                            errorMessage = "No solution found for " + field.getCell(row, col).getPos();
                            //return false; // dead end
                            return Status.NO_SOLUTION;
                        }
                        solved = Status.NOT_YET_SOLVED;
                    }
                    //System.out.println(" MOD:" + modified + " solved:"+solved);
                }
            }
            //System.out.println("FFF "+field);

            //System.out.println(" MOD:" + modified + " solved:"+solved);
        } while (modified);

        // now all candidates are known: calculating hidden singles
        field.solveHiddenSingles();

        if (solved == Status.SOLVED) {
            return Status.SOLVED;
        }

        //System.out.println("STILL UNSOLVED\n" + field);
        if (stopAtStep) {
            pleaseWait = true;
            if (cp != null) {
                cp.display(this);
            }
            synchronized (this) {
                //System.out.println("should wait ?: " + countRecursions);
                while (pleaseWait) {
                    try {
                        //System.out.println("waiting: " + countRecursions);
                        wait();
                        pleaseWait = false;
                    } catch (Exception e) {
                    }
                }
            }
        }

        while (true) {
            //List<Cell[][]> filo = new ArrayList<Cell[][]>();
            // get a cell on which we modify
            Cell cellToModify = getCell();

            cellToModify.beingModified = true;
            int curcol = cellToModify.getCol();
            int currow = cellToModify.getRow();
            //Set<Integer>candidates = cellToModify.getCandidates();
            //Iterator<Integer>it = candidates.iterator();
            Integer[] candidates = cellToModify.getCandidates().toArray(new Integer[]{});
            //while(it.hasNext()) {
            int i = 0;
            for (Integer it : candidates) {
               
                i++;
                //   create a copy of all cells
                Field backup = field.copy();
                Integer chosenValue = it;//.next();
                // choose a value
                cellToModify.setValue(chosenValue);
                //Object o = (Object)cellToModify;
                //System.out.println("SETTING " + i + " " + cellToModify.getRow() + "," + cellToModify.getCol() + " to " + chosenValue + "::" + o);
                //System.out.println("ITERATE:\n" + field);

//                if (stopAtStep) {
//                    pleaseWait = true;
//                    if (cp != null) cp.display(this);
//                    synchronized (this) {
//                        //System.out.println("should wait ?: " + countRecursions);
//                        while (pleaseWait) {
//                            try {
//                                //System.out.println("waiting: " + countRecursions);
//                                wait();
//                            } catch (Exception e) {
//                            }
//                        }
//                    }
//                }
                solved = calculate();

                if (solved == Status.SOLVED) {
                    return Status.SOLVED;
                } else {
                    // reinstall the original cells before trying next value

                    field = backup.copy();
                    //  System.out.println("REINSTALL\n"+ field);
                    cellToModify = field.getCell(currow, curcol);
                    cellToModify.beingModified = true;
                    countIterations++;
                }
            }
            cellToModify.beingModified = false;
            return Status.NO_SOLUTION; // no solution
        }
        // backtracking: copy cells[][]
        //return solved;
    }

    public String toString() {
        String s = field.toString();
//        if (countRecursions > 0) {
//            s += "\n" + countRecursions + " recursions";
//        }
        if (countIterations > 0) {
            s += "\nwith " + countIterations + " incorrect decision(s)";
        }
        return s;
    }

    public static void oomain(String[] args) {
        Sudoku sk = new Sudoku();
        if (args.length > 0) {
            boolean ok = sk.readFile(args[0]);
            if (!ok) {
                return;
            }
        }
        sk.init(true);
        System.out.println("START\n" + sk);
        sk.calculate();
        System.out.println("SOLUTION\n" + sk);

    }
}
