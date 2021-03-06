package ca.ciccc.silverBullet.playerElements;

import ca.ciccc.silverBullet.enums.gameplay.Directions;
import ca.ciccc.silverBullet.enums.gameplay.Orientation;
import ca.ciccc.silverBullet.enums.gameplay.PlayerAction;
import ca.ciccc.silverBullet.gameBoard.GridBoard;
import ca.ciccc.silverBullet.gameBoard.Move;
import ca.ciccc.silverBullet.utils.MediaUtil;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class Player {


  private int numberOfShots;

  private int playerNumber;
  private Directions facingDirection;
  private int gridPositionX;
  private int gridPositionY;
  private Move targetMove;
  private Node playerNode;
  private PlayerAction[] playerActions = {PlayerAction.NONE, PlayerAction.NONE, PlayerAction.NONE,
      PlayerAction.NONE, PlayerAction.NONE};
  private int currentAction = 0;
  private ActionCounter playerActionCounter;
  private boolean actionsFull;

  private boolean isDead;

  private int currentAmo;

  public void setActionsFull(boolean actionsFull) {
    this.actionsFull = actionsFull;
  }

  public boolean isActionsFull() {
    return actionsFull;
  }

  public boolean isDead() {
    return isDead;
  }

  public Player(int numberOfShots, int playerNumber, int gridX, int gridY,
      Directions facingDirection) {

    this.numberOfShots = numberOfShots;
    this.playerNumber = playerNumber;
    this.facingDirection = facingDirection;
    this.currentAmo = 3;
    gridPositionX = gridX;
    gridPositionY = gridY;
    playerNode = new Circle(30, Color.GREEN);
    Image image = MediaUtil.createImage("/images/Character/Fire/Fire.png");
    switch (playerNumber) {

      case 1:
        image = MediaUtil.createImage("/images/Character/Fire/Fire.png");
        break;
      case 2:
        image = MediaUtil.createImage("/images/Character/Rock/Rock.png");
        break;
      case 3:
        image = MediaUtil.createImage("/images/Character/Water/Water.png");
        break;
      case 4:
        image = MediaUtil.createImage("/images/Character/Wind/Wind.png");
        break;

    }
    ((Circle) playerNode).setFill(new ImagePattern(image));
    playerActionCounter = new ActionCounter(this);

  }

  private void rotatePlayer(Orientation move) {
    switch (facingDirection) {
      case NORTH:
        if (move == Orientation.LEFT) {
          facingDirection = Directions.WEST;
          this.setimage();
        } else {
          facingDirection = Directions.EAST;
          this.setimage();

        }
        break;
      case SOUTH:
        if (move == Orientation.LEFT) {
          facingDirection = Directions.EAST;
          this.setimage();

        } else {
          facingDirection = Directions.WEST;
          this.setimage();

        }
        break;
      case EAST:
        if (move == Orientation.LEFT) {
          facingDirection = Directions.NORTH;
          this.setimage();

        } else {
          facingDirection = Directions.SOUTH;
          this.setimage();

        }
        break;
      case WEST:
        if (move == Orientation.LEFT) {
          facingDirection = Directions.SOUTH;
          this.setimage();

        } else {
          facingDirection = Directions.NORTH;
          this.setimage();

        }
        break;
    }
  }

  void Die() {
    isDead = true;
    GridBoard.instance.removePlayer(this);
  }

  public void addAction(PlayerAction actionToAdd) {
    if (!isActionsFull()) {
      playerActions[currentAction] = actionToAdd;
      currentAction++;
      if (currentAction > 4) {

        currentAction = 0;
        actionsFull = true;

      }

      playerActionCounter.addAction();
    }
  }

  public boolean takeAction(int actionNumber) {
    if (playerActions[actionNumber] == null) {
      return false;
    }
    switch (playerActions[actionNumber]) {
      case MOVE:
        this.targetMove = GridBoard.instance.tryMovePlayer(this);

        break;
      case TURN_LEFT:
        this.rotatePlayer(Orientation.LEFT);
        break;
      case TURN_RIGHT:
        this.rotatePlayer(Orientation.RIGHT);
        break;
      case SHOOT:
        GridBoard.instance.shootBullet(this);
        removeShot();
        break;
      case WAIT:
        break;
    }
    playerActionCounter.removeAction();
    return true;
  }


  public void passTurn() {
    if (!isActionsFull()) {
      for (PlayerAction playerAction : playerActions) {
        if (playerAction == PlayerAction.NONE) {
          addAction(PlayerAction.WAIT);
        }
      }
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Player)) {
      return false;
    }
    Player otherPlayer = (Player) obj;
    return playerNumber == otherPlayer.getPlayerNumber();
  }

  public void resetActions() {
    for (int i = 0; i < 5; i++) {
      playerActions[i] = PlayerAction.NONE;
    }
    actionsFull = false;
  }

  public void addShot() {
    numberOfShots++;

    if (numberOfShots > 3) {
      numberOfShots = 3;
    }

    playerActionCounter.addShot();
  }

  private void removeShot() {
    numberOfShots--;
    if (numberOfShots < 0) {
      numberOfShots = 0;
    }
    playerActionCounter.removeShot();
  }

  public ActionCounter getPlayerActionCounter() {
    return playerActionCounter;
  }

  public boolean isHasShot() {
    return numberOfShots > 0;
  }

  public int getPlayerNumber() {
    return playerNumber;
  }

  public void setPlayerNumber(int playerNumber) {
    this.playerNumber = playerNumber;
  }

  public Directions getFacingDirection() {
    return facingDirection;
  }

  public void setFacingDirection(Directions facingDirection) {
    this.facingDirection = facingDirection;
  }

  public int getGridPositionX() {
    return gridPositionX;
  }

  public void setGridPositionX(int gridPositionX) {
    this.gridPositionX = gridPositionX;
  }

  public int getGridPositionY() {
    return gridPositionY;
  }

  public void setGridPositionY(int gridPositionY) {
    this.gridPositionY = gridPositionY;
  }

  public Move getTargetMove() {
    return targetMove;
  }

  public void setTargetMove(Move targetMove) {
    this.targetMove = targetMove;
  }

  public Node getPlayerNode() {
    return playerNode;
  }

  public void setPlayerNode(Node playerNode) {
    this.playerNode = playerNode;
  }

  public PlayerAction[] getPlayerActions() {
    return playerActions;
  }

  public void setPlayerActions(PlayerAction[] playerActions) {
    this.playerActions = playerActions;
  }

  public int getCurrentAction() {
    return currentAction;
  }

  public int getNumberOfShots() {
    return numberOfShots;
  }

  public void setCurrentAction(int currentAction) {
    this.currentAction = currentAction;
  }

  public int getCurrentAmo() {
    if (currentAmo == 0 || !isHasShot()) {
      //System.out.println("You don't have more Amo!!!");
    } else {
      //System.out.println("You have " + currentAmo + " of Amo!!!");
      currentAmo--;
    }
    return currentAmo;
  }

  public void setCurrentAmo(int currentAmo) {
    this.currentAmo = currentAmo;
  }

  private void setimage() {
    int playerNum = this.playerNumber;
    String playerelement = null;
    String dirction = null;

    if (this.getFacingDirection() == Directions.SOUTH) {
      dirction = "";
    } else if (this.getFacingDirection() == Directions.NORTH) {
      dirction = "Up";
    } else if (this.getFacingDirection() == Directions.WEST) {
      dirction = "L";
    } else if (this.getFacingDirection() == Directions.EAST) {
      dirction = "R";
    }

    if (playerNum == 1) {
      playerelement = "Fire";
    } else if (playerNum == 2) {
      playerelement = "Rock";
    } else if (playerNum == 3) {
      playerelement = "Water";
    } else if (playerNum == 4) {
      playerelement = "Wind";
    }

    String path =
        String.format("/images/Character/%s/%s%s.png", playerelement, playerelement, dirction);
    Image img = MediaUtil.createImage(path);
    ((Circle) this.playerNode).setFill(new ImagePattern(img));
  }

}
