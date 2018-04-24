package sample;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Board implements Serializable {

    public enum Symbol {NONE, CIRCLE, CROSS };

    private Symbol[] symbols = new Symbol[9];

    private Symbol nextMove;

    public Board() {
        for (int i = 0; i < 9; i++) {
            symbols[i] = Symbol.NONE;
        }
        nextMove = Symbol.CIRCLE;
    }

    private int getIndex(int x, int y) {
        return x * 3 + y;
    }

    public void setSymbol(int x, int y, Symbol symbol) {
        if (nextMove != symbol) {
            return;
        }
        if (isWinner(Board.Symbol.CIRCLE) || isWinner(Board.Symbol.CROSS)) {
            return;
        }
        if (symbols[getIndex(x, y)] != Symbol.NONE) {
            return;
        }
        if (nextMove == Symbol.CIRCLE) {
            nextMove = Symbol.CROSS;
        } else {
            nextMove = Symbol.CIRCLE;
        }
        symbols[getIndex(x, y)] = symbol;
    }

    public Symbol getSymbol(int x, int y) {
        return symbols[getIndex(x, y)];
    }

    public Symbol getNextMove() {
        return nextMove;
    }

    public boolean isWinner(Symbol symbol) {
        for (int i = 0; i < 3; i++) {
            if (symbols[getIndex(i, 0)] == symbol && symbols[getIndex(i, 1)] == symbol && symbols[getIndex(i, 2)] == symbol) {
                return true;
            }
        }
        for (int i = 0; i < 3; i++) {
            if (symbols[getIndex(0, i)] == symbol && symbols[getIndex(1, i)] == symbol && symbols[getIndex(2, i)] == symbol) {
                return true;
            }
        }
        if (symbols[getIndex(0, 0)] == symbol && symbols[getIndex(1, 1)] == symbol && symbols[getIndex(2, 2)] == symbol) {
            return true;
        }
        if (symbols[getIndex(2, 0)] == symbol && symbols[getIndex(1, 1)] == symbol && symbols[getIndex(0, 2)] == symbol) {
            return true;
        }
        return false;
    }
}