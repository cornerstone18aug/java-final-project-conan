package ca.ciccc.silverBullet.gameBoard;

import ca.ciccc.silverBullet.Player;
import ca.ciccc.silverBullet.enums.gameplay.PlayerAction;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public class GameScene extends Pane {

    private GridBoard gameBoard;
    private Player testFirstPlayer;
    private Player testSecondPlayer;
    private double t = 0;
    private boolean isExecuting;
    private int currentActionNumber = 0;
    private int controllingPlayer = 0;
    AnimationTimer testTimer;
    static GameScene instance;


    public GameScene() {
        gameBoard = new GridBoard(9, 9);
        instance = this;
        testFirstPlayer = gameBoard.addPlayer(1, 1, 1);
        testSecondPlayer = gameBoard.addPlayer(5, 5, 2);

        testFirstPlayer.getPlayerActionCounter().setTranslateX(175);
        testFirstPlayer.getPlayerActionCounter().setTranslateY(630);

        testSecondPlayer.getPlayerActionCounter().setTranslateX(400);
        testSecondPlayer.getPlayerActionCounter().setTranslateY(630);

        this.getChildren().addAll(gameBoard.gridBoard,
                testFirstPlayer.getPlayerNode(),
                testSecondPlayer.getPlayerNode(),
                testFirstPlayer.getPlayerActionCounter(),
                testSecondPlayer.getPlayerActionCounter());

    }

    public void boardUpdate(){
        if(isExecuting){
            if(t <= 0){
                t = .4;
                testFirstPlayer.takeAction(currentActionNumber);
                testSecondPlayer.takeAction(currentActionNumber);
                currentActionNumber++;
                if(currentActionNumber>4){
                    isExecuting = false;
                    testFirstPlayer.getPlayerActionCounter().clearActions();
                    testSecondPlayer.getPlayerActionCounter().clearActions();
                    currentActionNumber = 0;
                    controllingPlayer = 0;
                    testFirstPlayer.resetActions();
                    testSecondPlayer.resetActions();
                }
            } else {
                t -= 0.016;
            }
        }

    }

    public void onKeyPressed(KeyCode key){
        switch (key) {
            case Q:
                if (controllingPlayer == 0){
                    testFirstPlayer.addAction(PlayerAction.TURN_LEFT);
                } else{
                    testSecondPlayer.addAction(PlayerAction.TURN_LEFT);
                }
                break;
            case W:
                if (controllingPlayer == 0){
                    testFirstPlayer.addAction(PlayerAction.MOVE);
                } else{
                    testSecondPlayer.addAction(PlayerAction.MOVE);
                }
                break;
            case E:
                if (controllingPlayer == 0){
                    testFirstPlayer.addAction(PlayerAction.TURN_RIGHT);
                } else{
                    testSecondPlayer.addAction(PlayerAction.TURN_RIGHT);
                }
                break;
            case R:
                if (controllingPlayer == 0){
                    testFirstPlayer.addAction(PlayerAction.SHOOT);
                } else{
                    testSecondPlayer.addAction(PlayerAction.SHOOT);
                }
                break;
            case T:
                if (controllingPlayer == 0){
                    testFirstPlayer.addAction(PlayerAction.WAIT);
                } else{
                    testSecondPlayer.addAction(PlayerAction.WAIT);
                }
                break;
            case SPACE:
                currentActionNumber  = 0;
                controllingPlayer = 0;
                isExecuting = true;
                testFirstPlayer.setActionsFull(false);
                testSecondPlayer.setActionsFull(false);

                break;
        }
        if(testFirstPlayer.isActionsFull()){
            controllingPlayer++;
        }
    }


}