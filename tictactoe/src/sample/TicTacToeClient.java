package sample;

import javax.swing.*;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TicTacToeClient implements ITicTacToe {

    private ITicTacToe ticTacToe;

    public TicTacToeClient(String ip) throws Exception {
        Registry registry = LocateRegistry.getRegistry(ip);
        ticTacToe = (ITicTacToe) registry.lookup(TicTacToeServer.name);;
    }

    @Override
    public void setSymbol(int x, int y, Board.Symbol symbol) throws RemoteException {
        ticTacToe.setSymbol(x, y, symbol);
    }

    @Override
    public Board getBoard() throws RemoteException {
        return ticTacToe.getBoard();
    }
}
