package sample;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ITicTacToe extends Remote {

    void setSymbol(int x, int y, Board.Symbol symbol) throws RemoteException;

    Board getBoard() throws RemoteException;

}
