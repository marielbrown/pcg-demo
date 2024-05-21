package pcg_tools;

import processing.core.PApplet;

public interface PCGTool {
    boolean[][] getMap();

    void generateNewMap(PApplet app, int mapWidth, int mapHeight);
}
