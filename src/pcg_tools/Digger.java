package pcg_tools;

import java.util.concurrent.ThreadLocalRandom;

public class Digger implements PCGTool{

    private boolean[][] map; // true if cell is traversable

    // parameters adjustable by user
    int mapWidth = 70, mapHeight = 50;
    int percentageTraversable = 20;
    int minimumRoomDimension = 2, maximumRoomDimension = 6;
    float roomGenerationChanceModifier = -100;
    float directionChangeChanceModifier = 1;    //todo: bug if less than 1

    // state variables updated in code
    private int[] direction;
    private int agentX, agentY;
    private int targetTraversableCells, currentTraversableCells;
    private int directionChangeChance, roomGenerationChance;

    @Override
    public void runGeneration(){
        resetTool();
        direction = selectDirection();

        while (!isFinished()){
            executeStep();
        }
    }

    @Override
    public void resetTool() {
        map = new boolean[mapWidth][mapHeight];

        directionChangeChance = 5;
        roomGenerationChance = 100; // always place room at start // todo: make this optional
        targetTraversableCells = (int) (mapWidth * mapHeight * (percentageTraversable / 100f));
        currentTraversableCells = 0;

        agentX = mapWidth / 2; // start in centre
        agentY = mapHeight / 2;
    }

    public void executeStep(){
        // dig out cell
        if (!map[agentX][agentY]){
            map[agentX][agentY] = true;
            currentTraversableCells++;
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

        int directionChangeValue = ThreadLocalRandom.current().nextInt(0, 100);
        if (directionChangeValue < directionChangeChance) {
            //change direction
            direction = selectDirection();
            directionChangeChance = 0;
        } else {
            directionChangeChance += directionChangeChanceModifier;
        }

        int roomGenerationValue = ThreadLocalRandom.current().nextInt(0, 100);
        if (roomGenerationValue < roomGenerationChance) {
            //generate room
            currentTraversableCells += addRoom(agentX, agentY, mapWidth - 1, mapHeight - 1); //todo: are arguments needed here?
            roomGenerationChance = -10; //todo: remove magic number
        } else {
            roomGenerationChance += roomGenerationChanceModifier;
        }
    }

    private int[] selectDirection(){
        int directionValue = ThreadLocalRandom.current().nextInt(0, 4);;
        return switch (directionValue) {
            case 0 -> new int[]{1, 0};
            case 1 -> new int[]{-1, 0};
            case 2 -> new int[]{0, 1};
            default -> new int[]{0, -1};
        };
    }

    private int addRoom(int agentX, int agentY, int totalWidth, int totalHeight){
        int addedCells = 0;

        int roomWidth = Math.round(ThreadLocalRandom.current().nextInt(minimumRoomDimension, maximumRoomDimension));
        int roomHeight = Math.round(ThreadLocalRandom.current().nextInt(minimumRoomDimension, maximumRoomDimension));


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
    public boolean isFinished(){
        return currentTraversableCells >= targetTraversableCells;
    }

    public boolean[][] getMap() {
        return map;
    }
}