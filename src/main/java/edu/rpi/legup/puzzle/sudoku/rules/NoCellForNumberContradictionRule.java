package edu.rpi.legup.puzzle.sudoku.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.sudoku.Sudoku;
import edu.rpi.legup.puzzle.sudoku.SudokuBoard;
import edu.rpi.legup.puzzle.sudoku.SudokuCell;

import java.util.HashSet;
import java.util.Set;

public class NoCellForNumberContradictionRule extends ContradictionRule {

    public NoCellForNumberContradictionRule() {
        super("SUDO-CONT-0001", "No Cell for Number (Region)",
                "Process of elimination yields no valid numbers for an empty cell in a region.",
                "edu/rpi/legup/images/sudoku/NoSolution.png");
    }

    /**
     * Checks whether the transition has a contradiction at the specific puzzleElement index using this rule
     *
     * @param board         board to check contradiction
     * @param puzzleElement equivalent puzzleElement
     * @return null if the transition contains a contradiction at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        SudokuBoard sudokuBoard = (SudokuBoard) board;
        SudokuCell cell = (SudokuCell) sudokuBoard.getPuzzleElement(puzzleElement);
        if (cell.getData() != 0) {
            return super.getNoContradictionMessage();
        }

        int groupSize = sudokuBoard.getSize();

        Set<SudokuCell> region = sudokuBoard.getRegion(cell.getGroupIndex());
        Set<Integer> numbersNotInRegion = new HashSet<>();
        Set<Integer> numberNotFit = new HashSet<>();
        Set<Integer> numberCanFit = new HashSet<>();

        for (int i = 1; i <= groupSize; i++) {
            numbersNotInRegion.add(i);
        }
        for (SudokuCell c : region) {
            if(c.getData() != 0) {
                numbersNotInRegion.remove(c.getData());
            }
        }
        for (SudokuCell c : region) {

            Set<SudokuCell> row = sudokuBoard.getRow(c.getLocation().y);
            Set<SudokuCell> col = sudokuBoard.getCol(c.getLocation().x);
            for(SudokuCell c1 : row) {
                numberNotFit.add(c1.getData());
            }
            for(SudokuCell c2 : col) {
                numberNotFit.add(c2.getData());
            }

            for(int i : numbersNotInRegion) {
                numbersNotInRegion.remove(numberNotFit);
            }
        }


        return super.getNoContradictionMessage();
    }
}