package model;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

public class LevelLoader {

    private Path levels;

    public LevelLoader(Path levels){
        this.levels = levels;
    }

    public GameObjects getLevel(int level) {
        int tmpLevel = level;
        if (tmpLevel > 60)
            tmpLevel -= 60;
        HashSet<Wall> walls = new HashSet<>();
        HashSet<Box> boxes = new HashSet<>();
        HashSet<Home> homes = new HashSet<>();
        Player player = null;
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(levels.toString()));
            int cellSize = Model.FIELD_SELL_SIZE;
            int x0 = cellSize / 2;
            int y0 = cellSize / 2;
            while (!fileReader.readLine().contains("Maze: " + tmpLevel)) ;
            fileReader.readLine();
            fileReader.readLine();
            double y = Double.parseDouble(fileReader.readLine().split(" ")[2]);
            fileReader.readLine();
            fileReader.readLine();
            fileReader.readLine();
            for (int i = 0; i < y; i++) {
                String read = fileReader.readLine();
                for (int j = 0; j < read.length(); j++)
                    switch (read.charAt(j)) {
                        case 'X':
                            walls.add(new Wall(x0 + j * cellSize, y0 + i * cellSize));
                            break;
                        case '@':
                            player = new Player(x0 + j * cellSize, y0 + i * cellSize);
                            break;
                        case '*':
                            boxes.add(new Box(x0 + j * cellSize, y0 + i * cellSize));
                            break;
                        case '.':
                            homes.add(new Home(x0 + j * cellSize, y0 + i * cellSize));
                            break;
                        case '&':
                            boxes.add(new Box(x0 + j * cellSize, y0 + i * cellSize));
                            homes.add(new Home(x0 + j * cellSize, y0 + i * cellSize));
                            break;
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new GameObjects(walls, boxes, homes, player);
    }
}
