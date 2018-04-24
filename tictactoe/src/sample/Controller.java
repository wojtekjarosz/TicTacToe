package sample;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.rmi.RemoteException;

public class Controller {

    @FXML
    private GridPane gridPane;

    @FXML
    private Button createGame;

    @FXML
    private Button joinGame;

    @FXML
    private Label nextMoveLabel;

    @FXML
    private TextField ipTextField;

    private ITicTacToe ticTacToe;

    private boolean isServer;

    private Object lock = new Object();

    private ImageView[] imageViews = new ImageView[9];

    @FXML
    private void initialize() throws IOException {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        createGame.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                synchronized (lock) {
                    isServer = true;
                    ticTacToe = new TicTacToeServer();
                }
            }
        });
        joinGame.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    synchronized (lock) {
                        isServer = false;
                        ticTacToe = new TicTacToeClient(ipTextField.getText());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Thread thread = new Thread(() -> {
            while (true) {
                Platform.runLater(() -> reloadBoard());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                Image image = new Image("assets/x.png");
                ImageView imageView = new ImageView(image);
                imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("Tile pressed ");
                        Node source = (Node)event.getSource();
                        int x = GridPane.getColumnIndex(source);
                        int y = GridPane.getRowIndex(source);
                        if (ticTacToe != null) {
                            try {
                                ticTacToe.setSymbol(x, y, isServer ? Board.Symbol.CIRCLE : Board.Symbol.CROSS);
                                reloadBoard();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                        event.consume();
                    }
                });
                imageViews[x * 3 + y] = imageView;
                gridPane.add(imageView, x, y);
            }
        }

    }

    private void reloadBoard() {
        if (ticTacToe != null) {
            try {
                Board board;
                synchronized (lock) {
                    board = ticTacToe.getBoard();
                }
                System.out.println("pobrany board");
                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 3; y++) {
                        String path;

                        switch (board.getSymbol(x, y)) {
                            case NONE:
                                path = "assets/n.png";
                                break;
                            case CROSS:
                                path = "assets/x.png";
                                break;
                            case CIRCLE:
                                path = "assets/o.png";
                                break;
                            default:
                                throw new IllegalArgumentException();
                        }
                        imageViews[x * 3 + y].setImage(new Image(path));

                        String text = "";
                        if ((isServer && board.getNextMove() == Board.Symbol.CIRCLE) || (!isServer && board.getNextMove() == Board.Symbol.CROSS)) {
                            text = "Your move!";
                        } else {
                            text = "Waiting for opponent...";
                        }

                        if (board.isWinner(Board.Symbol.CIRCLE) || board.isWinner(Board.Symbol.CROSS)) {
                            if (board.isWinner(Board.Symbol.CIRCLE) && board.isWinner(Board.Symbol.CROSS)) {
                                text = "DRAW";
                            } else {
                                if ((board.isWinner(Board.Symbol.CIRCLE) && isServer) || (board.isWinner(Board.Symbol.CROSS) && !isServer)) {
                                    text = "YOU WIN";
                                } else {
                                    text = "YOU LOSE";
                                }
                            }
                        }

                        nextMoveLabel.setText(text);
                    }
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

}