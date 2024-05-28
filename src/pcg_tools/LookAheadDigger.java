package pcg_tools;

import java.util.concurrent.ThreadLocalRandom;

public class LookAheadDigger implements PCGTool{

    private boolean[][] map; // true if cell is traversable

    // parameters adjustable by user
    int mapWidth = 70, mapHeight = 50;
    int percentageTraversable = 20;
    int minimumRoomDimension = 2, maximumRoomDimension = 6;
    float roomGenerationChanceModifier = 2;
    float directionChangeChanceModifier = 0.5f;

    // state variables updated in code
    private int[] direction;
    private int agentX, agentY;
    private int targetTraversableCells, currentTraversableCells;
    private float directionChangeChance, roomGenerationChance;

    @Override
    public void runGeneration(){
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

        direction = selectDirection(ThreadLocalRandom.current().nextInt(0, 4));
    }

    public void executeStep(){
        if (!roomPlacementIsPossible()){
            //todo: may wish to change direction
            placeCorridor();
        } else {
            if (roomPlacementIsSelected()) {
                generateRoom();
                roomGenerationChance = -maximumRoomDimension; // reduces room overlapping by delaying placement of next room
            } else {
                placeCorridor();
                roomGenerationChance += roomGenerationChanceModifier;
            }
        }

    }

    private boolean roomPlacementIsSelected(){
        int roomGenerationValue = ThreadLocalRandom.current().nextInt(0, 100);
        return roomGenerationValue < roomGenerationChance;
    }

    private boolean roomPlacementIsPossible(){
        // determine min and max co-ordinates
        // co-ordinates include a 1 cell buffer
        // call is area available
        return false;
    }

    private void placeCorridor(){
        // stub
        // move agent
        // digOutCell
    }



    ////

    private int[] selectDirection(int directionValue){
        return switch (directionValue) {
            case 0 -> new int[]{1, 0};
            case 1 -> new int[]{-1, 0};
            case 2 -> new int[]{0, 1};
            default -> new int[]{0, -1};
        };
    }
    private void generateRoom(){
        int totalWidth = mapWidth - 1;
        int totalHeight = mapHeight - 1;

        int roomWidth = ThreadLocalRandom.current().nextInt(minimumRoomDimension, maximumRoomDimension);
        int roomHeight = ThreadLocalRandom.current().nextInt(minimumRoomDimension, maximumRoomDimension);


        int startX = agentX - roomWidth / 2;    // centre room on digger
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
                digOutCell(x, y);
            }
        }
    }
    private void digOutCell(int x, int y){
        if (!map[x][y]){
            map[x][y] = true;
            currentTraversableCells++;
        }
    }

    private boolean isAreaAvailable(int xMin, int yMin, int xMax, int yMax){
        for (int x = xMin; x < xMax; x++) {
            for (int y = yMin; y < yMax; y++) {
                if (map[x][y]) return false;
            }
        }
        return true;
    }

    @Override
    public boolean isFinished(){
        //todo: may also need to heck if further movement is possible
        return currentTraversableCells >= targetTraversableCells;
    }

    @Override
    public boolean[][] getMap() {
        return map;
    }


}
