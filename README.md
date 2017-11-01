# Sudoku Solver

Small java utility which solves Sudokus (if a Sudoku has several possible solutions, only one is presented).

## License

This application and library is under the Mozilla Public License, v. 2.0


## Author

Johannes Heinecke

## Run

    java -jar Sudoku.jar [--size {2|3|4|5}] [[--text] [<file>]

<file> is a text file with the initial numbers, spaces for empty cells
the file must have at least size * size lines with at least size * size caracters each

### Example ###
    123......
    456......
    789......
    ...987...
    ...654...
    ...321...
    ......147
    ......258
    ......369

