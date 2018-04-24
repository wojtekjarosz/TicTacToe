package sample;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class TicTacToeServer implements ITicTacToe {

    private Board board = new Board();

    public static String name = "TicTacToe";

    public TicTacToeServer() {
        try {
            ITicTacToe stub =
                    (ITicTacToe) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("TicTacToeEngine bound");
        } catch (Exception e) {
            System.err.println("TicTacToeEngine exception:");
            e.printStackTrace();
        }
    }

    @Override
    public void setSymbol(int x, int y, Board.Symbol symbol) {
        board.setSymbol(x, y, symbol);
    }

    @Override
    public Board getBoard() {
        return board;
    }
}
