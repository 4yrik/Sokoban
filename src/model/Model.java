package model;

import controller.EventListener;
import java.nio.file.Paths;

public class Model {

    private EventListener eventListener;
    public final static int FIELD_SELL_SIZE = 20;
    private GameObjects gameObjects;
    private int currentLevel = 1;
    private LevelLoader levelLoader = new LevelLoader(Paths.get("./src/res/levels.txt"));

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public GameObjects getGameObjects(){
        return gameObjects;
    }

    public void restartLevel(int level){
        gameObjects = levelLoader.getLevel(level);
    }

    public void restart(){
        restartLevel(currentLevel);
    }

    public void startNextLevel(){
        restartLevel(++currentLevel);
    }

    public void move(Direction direction){
        Player player = gameObjects.getPlayer();
        if(checkWallCollision(player, direction)){
            return;
        }
        if(checkBoxCollision(direction)){
            return;
        }
        if(direction == Direction.DOWN) player.move(0, FIELD_SELL_SIZE);
        else if(direction == Direction.UP) player.move(0, -FIELD_SELL_SIZE);
        else if(direction == Direction.LEFT) player.move(-FIELD_SELL_SIZE, 0);
        else if(direction == Direction.RIGHT) player.move(FIELD_SELL_SIZE, 0);

        checkCompletion();
    }

    public boolean checkBoxCollision(Direction direction){

        Player player = gameObjects.getPlayer();

        for(Box box1: gameObjects.getBoxes()){
            if(player.isCollision(box1, direction)){
                if(checkWallCollision(box1, direction)){
                    return true;
                }
                for(Box box2: gameObjects.getBoxes()){
                    if(box1 != box2) {
                        if (box1.isCollision(box2, direction)) {
                            return true;
                        }
                    }
                }
                if(direction == Direction.DOWN) box1.move(0, FIELD_SELL_SIZE);
                else if(direction == Direction.UP) box1.move(0, -FIELD_SELL_SIZE);
                else if(direction == Direction.LEFT) box1.move(-FIELD_SELL_SIZE, 0);
                else if(direction == Direction.RIGHT) box1.move(FIELD_SELL_SIZE, 0);
                break;
            }
        }
        return false;
    }

    public void checkCompletion(){
        boolean[] isCompletes = new boolean[gameObjects.getHomes().size()];
        int index = 0;
        for(Home home: gameObjects.getHomes()){
            for(Box box: gameObjects.getBoxes()){
                if(home.getX() == box.getX() && home.getY() == box.getY()){
                    isCompletes[index++] = true;
                    break;
                }
            }
        }
        boolean isComplete = true;
        for(boolean a: isCompletes){
            if(!a){
                isComplete = false;
                break;
            }
        }
        if(isComplete){
            eventListener.levelCompleted(currentLevel);
        }
    }

    public boolean checkWallCollision(CollisionObject gameObject, Direction direction){

        for(Wall wall: gameObjects.getWalls()){
            if(gameObject.isCollision(wall, direction)){
                return true;
            }
        }
        return false;
    }
}
