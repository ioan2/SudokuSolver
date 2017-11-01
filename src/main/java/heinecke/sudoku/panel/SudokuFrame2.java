/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heinecke.sudoku.panel;

import heinecke.sudoku.Sudoku;

/**
 *
 * @author johannes
 */
public class SudokuFrame2 extends javax.swing.JFrame {
    // Variables declaration - do not modify

    int[][] initialValues;
//    private javax.swing.JButton reset;
//    private javax.swing.JButton solve;
    private FieldPanel fieldPanel;
    Sudoku sk;
    // Sudoku skReset;

    // End of variables declaration
    public SudokuFrame2(String fn) {
        super();
        initComponents();

        if (fn != null) {
            readFile(fn);
        }
    }

    public void readFile(String filename) {
        sk = new Sudoku();
        sk.readFile(filename);
        sk.init(true);
        fieldPanel.setValues(sk.getField(), false);
        //System.out.println("DDD\n" + sk);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        //reset = new javax.swing.JButton();
        //solve = new javax.swing.JButton();
        fieldPanel = new FieldPanel();
        fieldPanel.setVisible(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);


        getContentPane().setLayout(new java.awt.GridBagLayout());

//        reset.setText("reset");
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.gridx = 0;
//        gridBagConstraints.gridy = 1;
//        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
//        getContentPane().add(reset, gridBagConstraints);
//
//        reset.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                resetAction(evt);
//            }
//        });


//        solve.setText("solve!");
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.gridx = 1;
//        gridBagConstraints.gridy = 1;
//        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
//        getContentPane().add(solve, gridBagConstraints);
//
//        solve.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                solveAction(evt);
//            }
//        });


        fieldPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        //gridBagConstraints.ipadx = 1;
        //gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(fieldPanel, gridBagConstraints);



//        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
//        jPanel1.setLayout(jPanel1Layout);
//        jPanel1Layout.setHorizontalGroup(
//            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGap(0, 102, Short.MAX_VALUE)
//        );
//        jPanel1Layout.setVerticalGroup(
//            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGap(0, 121, Short.MAX_VALUE)
//        );

        pack();
    }

//     private void resetAction(java.awt.event.ActionEvent evt) {
//        if (skReset != null) {
//              fieldPanel.setValues(skReset.getField(), false);
//        }
//     }
//
//    private void solveAction(java.awt.event.ActionEvent evt) {
//       Sudoku sk = new Sudoku();
//       sk.init(false);
//       //Field f = sk.getField();
//        fieldPanel.putValues(sk.getField());
//        skReset = sk.copy(sk);
//        // System.out.println("START\n" + sk);
//        sk.calculate();
//        //System.out.println("SOLUTION\n" + sk);
//        fieldPanel.setValues(sk.getField(), true);
//        //System.out.println("OLD " + sk2);
//    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        System.out.println("Sudoku Solver. Copyright Johannes Heinecke");
        System.out.println("Version " + FieldPanel.Version);
        System.out.println("\nusage:");
        System.out.println("\n java -jar Sudoku.jar [--size {2|3|4|5}] [[--text] [<file>]");
        System.out.println("\n<file> is a text file with the initial numbers, spaces for empty cells");
        System.out.println("the file must have at least size*size lines with at least size*size caracters each\n");
        System.out.println("\nExample");
        System.out.println("\t123......");
        System.out.println("\t456......");
        System.out.println("\t789......");
        System.out.println("\t...987...");
        System.out.println("\t...654...");
        System.out.println("\t...321...");
        System.out.println("\t......147");
        System.out.println("\t......258");
        System.out.println("\t......369");
        String fn = null;
        boolean text = false;

        int size = 3;
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("--text")) {
                    text = true;
                } else if (args[i].equals("--size") && (i+1 < args.length)) {
                    size = Integer.parseInt(args[i + 1]);
                    Sudoku.setSize(size);
                    i++;
                } else {
                    fn = args[i];
                }
            }
        }
       
        if (!text) {
            final String filename = fn;
            java.awt.EventQueue.invokeLater(new Runnable() {

                public void run() {
                    SudokuFrame2 sf = new SudokuFrame2(filename);
                    sf.setVisible(true);
                    // sf.setPreferredSize(new Dimension(100,300));
                }
            });

        } else {
            // text mode
            Sudoku sk = new Sudoku();
            boolean ok = sk.readFile(fn);
            if (!ok) {
                return;
            }
            sk.init(true);
            System.out.println("Start\n" + sk);
            ok = sk.getField().check();
            if (!ok) {
                System.out.println("Invalid input file (repeated numbers in rows, columns or squares");
            } else {
                Sudoku.Status res = sk.calculate();
                if (res == Sudoku.Status.SOLVED) {
                    System.out.println("Solution\n" + sk);
                } else {
                    System.out.println("No solution possible");
                }
            }
        }
    }
}
