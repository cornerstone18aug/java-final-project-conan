package ca.ciccc.silverBullet.gameBoard;

import FileInput.FileInput;
import ca.ciccc.silverBullet.enums.gameplay.Directions;
import ca.ciccc.silverBullet.gridNodes.GridNode;
import ca.ciccc.silverBullet.gridNodes.Hole;
import ca.ciccc.silverBullet.gridNodes.Space;
import ca.ciccc.silverBullet.gridNodes.Wall;
import ca.ciccc.silverBullet.gridNodes.Water;
import ca.ciccc.silverBullet.playerElements.Bullet;
import ca.ciccc.silverBullet.playerElements.Player;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.GridPane;

public class GridBoard {

  GridNode[][] grid;
  public GridPane gridBoard;
  public List<Player> players;
  int gridSizeX;
  int gridSizeY;
  public static GridBoard instance;
  private FileInput fileInput = new FileInput();

  public GridBoard(int sizeX, int sizeY, int level) {
    generateBoard(sizeX, sizeY, level);
    players = new ArrayList<>();
    gridSizeX = sizeX - 1;
    gridSizeY = sizeY - 1;
    instance = this;

  }

  public Move tryMovePlayer(Player playerToMove) {

    GridNode originGrid = grid[playerToMove.getGridPositionY()][playerToMove.getGridPositionX()];
    int targetX = 0;
    int targetY = 0;
    GridNode targetNode;
    if (originGrid.hasPlayer()) {
      switch (originGrid.getPlayerInSpace().getFacingDirection()) {
        case NORTH:
          targetX = originGrid.getGridX();
          targetY = originGrid.getGridY() - 1;
          break;
        case SOUTH:
          targetX = originGrid.getGridX();
          targetY = originGrid.getGridY() + 1;
          break;
        case EAST:
          targetX = originGrid.getGridX() + 1;
          targetY = originGrid.getGridY();
          break;
        case WEST:
          targetX = originGrid.getGridX() - 1;
          targetY = originGrid.getGridY();
          break;
      }
      if ((targetX < 0 || targetY < 0) || (targetX > gridSizeX || targetY > gridSizeY)) {
        return null;
      }
      targetNode = grid[targetY][targetX];

      if (targetX < 0 || targetY < 0) {
        return null;
      } else {
        if (targetNode.isCanMoveTo() && checkOtherPlayerMoves(playerToMove)) {
          return new Move(targetX, targetY);
        }
      }
    }

    return null;
  }

  public boolean checkOtherPlayerMoves(Player playerToMove) {
    for (Player p : players) {
      if (!p.equals(playerToMove)) {
        if (p.getTargetMove() != null && (
            (p.getTargetMove().moveX == playerToMove.getTargetMove().moveX) && (
                p.getTargetMove().moveY == playerToMove.getTargetMove().moveY))) {
          return false;
        }
      }
    }
    return true;
  }

  public void movePlayer(Player playerToMove) {
    if (playerToMove.getTargetMove() != null) {
      GridNode targetNode = grid[playerToMove.getTargetMove().moveY][playerToMove
          .getTargetMove().moveX];
      grid[playerToMove.getGridPositionY()][playerToMove.getGridPositionX()].setPlayerInSpace(null);
      targetNode.setPlayerInSpace(playerToMove);

      playerToMove.setGridPositionX(targetNode.getGridX());
      playerToMove.setGridPositionY(targetNode.getGridY());

      playerToMove.getPlayerNode().setTranslateX(targetNode.getScreenX() + 30);
      playerToMove.getPlayerNode().setTranslateY(targetNode.getScreenY() + 30);
      System.out.println(targetNode.getGridX() + ", " + targetNode.getGridY());

      playerToMove.setTargetMove(null);
    }

  }

  public void generateBoard(int sizeX, int sizeY, int levelNumber) {
    grid = new GridNode[sizeY][sizeX];
    gridBoard = new GridPane();
    ca.ciccc.silverBullet.FileReader.FileRead read = new ca.ciccc.silverBullet.FileReader.FileRead();
    char[][] imageToPrint = read.getLevel(levelNumber);

    for (int i = 0; i < sizeY; i++) {
      for (int j = 0; j < sizeX; j++) {
        if (imageToPrint[i][j] == 'S') {
          GridNode nodeToAdd = new Space(j, i);
          gridBoard.add(nodeToAdd.getImage(), j, i);
          grid[i][j] = nodeToAdd;
          nodeToAdd.setGridX(j);
          nodeToAdd.setGridY(i);
        } else if (imageToPrint[i][j] == 'W') {
          GridNode nodeToAdd = new Wall(j, i);
          gridBoard.add(nodeToAdd.getImage(), j, i);
          grid[i][j] = nodeToAdd;
          nodeToAdd.setGridX(j);
          nodeToAdd.setGridY(i);
        } else if (imageToPrint[i][j] == 'E') {
          GridNode nodeToAdd = new Water(j, i);
          gridBoard.add(nodeToAdd.getImage(), j, i);
          grid[i][j] = nodeToAdd;
          nodeToAdd.setGridX(j);
          nodeToAdd.setGridY(i);
        } else if (imageToPrint[i][j] == 'H') {
          GridNode nodeToAdd = new Hole(j, i);
          gridBoard.add(nodeToAdd.getImage(), j, i);
          grid[i][j] = nodeToAdd;
          nodeToAdd.setGridX(j);
          nodeToAdd.setGridY(i);
        }
      }
    }

    gridBoard.setTranslateX(50);
    gridBoard.setTranslateY(50);
    for (int i = 0; i < sizeY; i++) {
      for (int j = 0; j < sizeX; j++) {
        grid[j][i].setScreenX((i * 60) + 50);
        grid[j][i].setScreenY((j * 60) + 50);
      }
    }
  }

  public Player addPlayer(int gridX, int gridY, int playerNumber) {
    GridNode targetNode = grid[gridY][gridX];
    if (targetNode.hasPlayer() == false) {
      Player playerToAdd = new Player(true, playerNumber, gridX, gridY, Directions.SOUTH);
      players.add(playerToAdd);
      targetNode.setPlayerInSpace(playerToAdd);
      playerToAdd.getPlayerNode().setTranslateX(targetNode.getScreenX() + 30);
      playerToAdd.getPlayerNode().setTranslateY(targetNode.getScreenY() + 30);
      return playerToAdd;
    }
    return null;
  }

  public Move tryShoot(Player playerShooting) {

    if (playerShooting.isHasShot()) {

      GridNode playerStartingNode = grid[playerShooting.getGridPositionY()][playerShooting
          .getGridPositionX()];

      List<GridNode> nodesAffected = new ArrayList<>();

      GridNode currentTargetNode;

      int gridIterator = 1;

      System.out.println(playerShooting.getFacingDirection().name());

      switch (playerShooting.getFacingDirection()) {
        case NORTH:
          while (true) {
            if (playerShooting.getGridPositionY() - gridIterator >= 0) {
              currentTargetNode = grid[playerStartingNode.getGridY()
                  - gridIterator][playerStartingNode.getGridX()];
              if (currentTargetNode.isCanMoveTo()) {
                nodesAffected.add(currentTargetNode);
              } else {
                break;
              }
              gridIterator++;
            } else {
              break;
            }
          }
          break;
        case SOUTH:
          while (playerShooting.getGridPositionY() + gridIterator <= gridSizeY) {

            currentTargetNode = grid[playerStartingNode.getGridY()
                + gridIterator][playerStartingNode.getGridX()];
            if (currentTargetNode.isCanMoveTo()) {
              nodesAffected.add(currentTargetNode);
            } else {
              break;
            }
            gridIterator++;
          }
          break;
        case EAST:
          while (playerShooting.getGridPositionX() + gridIterator <= gridSizeX) {

            currentTargetNode = grid[playerStartingNode.getGridY()][playerStartingNode.getGridX()
                + gridIterator];
            if (currentTargetNode.isCanMoveTo()) {
              nodesAffected.add(currentTargetNode);
            } else {
              break;
            }
            gridIterator++;
          }
          break;
        case WEST:
          while (playerShooting.getGridPositionX() - gridIterator >= 0) {

            currentTargetNode = grid[playerStartingNode.getGridY()][playerStartingNode.getGridX()
                - gridIterator];
            if (currentTargetNode.isCanMoveTo()) {
              nodesAffected.add(currentTargetNode);
            } else {
              break;
            }
            gridIterator++;
          }
      }
      //playerShooting.hasShot = false;
      if (!nodesAffected.isEmpty()) {
        return new Move(nodesAffected.get(nodesAffected.size() - 1).getGridX(),
            nodesAffected.get(nodesAffected.size() - 1).getGridY());
      }
    }
    return null;
  }

  public void removePlayer(Player playerToRemove) {
    GameScene.instance.getChildren().remove(playerToRemove.getPlayerNode());
  }

  public void shootBullet(Player player) {
    Move finalLocation = tryShoot(player);
    if (finalLocation != null) {
      Bullet bullet = new Bullet(new Move(player.getGridPositionX(), player.getGridPositionY()),
          finalLocation, player);
      gridBoard.getChildren().add(bullet);
    }
  }

  public GridNode getNodeFromGrid(int x, int y) {
    return grid[y][x];
  }

  public boolean areAllFull() {
    return players.stream().allMatch(Player::isActionsFull);
  }
}

