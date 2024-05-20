package pcg_tools;

import processing.core.PApplet;

public class Digger implements PCGTool{

    private boolean[][] map; // true if cell is traversable
    private void generateNewMap(PApplet app, int mapWidth, int mapHeight){
        map = new boolean[mapWidth][mapHeight];

        int directionChangeChance = 5;
        int roomChance = 100; // always place room at start
        float minimumLevelSize = mapWidth * mapHeight * 0.7f;
        int currentLevelSize = 0;

        int agentX = mapWidth / 2; // start in centre
        int agentY = mapHeight / 2;

        int[] direction = selectDirection(app);
        while (currentLevelSize < minimumLevelSize){
            // dig out cell
            if (!map[agentX][agentY]){
                map[agentX][agentY] = true;
                currentLevelSize++;
            }

            // if on edge, face away
            if (agentX == 0) {
                direction[0] = 1;
            } else if (agentX == mapWidth - 1) {
                direction[0] = -1;
            }

            if (agentY == 0) {
                direction[1] = 1;
            } else if (agentY == mapHeight - 1) {
                direction[1] = -1;
            }

            agentX += direction[0];
            agentY += direction[1];

            int directionChangeValue = (int) app.random(0, 99);
            if (directionChangeValue < directionChangeChance) {
                //change direction
                direction = selectDirection(app);
                directionChangeChance = 0;
            } else {
                directionChangeChance += 5;
            }

            int roomGenerationValue = (int) app.random(0, 99);
            if (roomGenerationValue < roomChance) {
                //generate room
                currentLevelSize += addRoom(app, agentX, agentY, mapWidth - 1, mapHeight - 1);
                roomChance = 0;
            } else {
                roomChance += 2;
            }
        }
    }

    private int[] selectDirection(PApplet app){
        int directionValue = (int) app.random(0,5);
        switch (directionValue) {
            case 0:
                return new int[] {1, 0};
            case 1:
                return new int[] {-1, 0};
            case 2:
                return new int[] {0, 1};
            default:
                return new int[] {0, -1};
        }
    }

    private int addRoom(PApplet app, int agentX, int agentY, int totalWidth, int totalHeight){
        int maxSize = 10;
        int minSize = 2;
        int addedCells = 0;

        int roomWidth = Math.round(app.random(minSize, maxSize));
        int roomHeight = Math.round(app.random(minSize, maxSize));


        int startX = agentX - roomWidth / 2;
        int startY = agentY - roomHeight / 2;
        int endX, endY;

        // adjust for left and top edges
        if (startX < 0){
            startX = 0;
        }

        if (startY < 0){
            startY = 0;
        }

        endX = startX + roomWidth;
        endY = startY + roomHeight;

        // adjust for right and bottom edges
        // assumes rooms may not be bigger than map
        if (endX > totalWidth){
            startX -= endX - totalWidth;
            endX = totalWidth;
        }

        if (endY > totalHeight){
            startY -= endY - totalHeight;
            endY = totalHeight;
        }

        // draw room
        for (int x = startX; x <= endX; x++){
            for (int y = startY; y  <= endY; y++){
                // dig out cell
                if (!map[x][y]){
                    map[x][y] = true;
                    addedCells ++;
                }
            }
        }

        return addedCells;
    }
}