package pcg_tools;

import java.util.Arrays;
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

    private boolean finished;

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

        if (!finished) {
            finished = currentTraversableCells >= targetTraversableCells;
        }

    }

    private boolean roomPlacementIsSelected(){
        int roomGenerationValue = ThreadLocalRandom.current().nextInt(0, 100);
        return roomGenerationValue < roomGenerationChance;
    }

    private boolean roomPlacementIsPossible(){
        int halfMaximumRoomDimension = maximumRoomDimension / 2;

        // determine min and max co-ordinates
        int centreX = agentX + direction[0] * halfMaximumRoomDimension + 2;
        int centreY = agentY + direction[1] * halfMaximumRoomDimension + 2; // + 2 to add a 1 cell wide buffer

        int minX = centreX - halfMaximumRoomDimension;
        int minY = centreY - halfMaximumRoomDimension;
        int maxX = centreX + halfMaximumRoomDimension;
        int maxY = centreY + halfMaximumRoomDimension;

        // out of bounds handling
        // todo: can / should we instead change the max room size?
        if (minX < 0 || minY < 0 || maxX >= mapWidth || maxY >= mapHeight) return false;


        // call is area available
        return isAreaAvailable(minX, minY, maxX, maxY);
    }

    private void placeCorridor(){


        //todo: please clean this up :(

        if (!canPlaceCorridor()){ // agent must turn, dead end reached
            if (direction[0] == 0){ // up or down
                System.out.println(Arrays.toString(direction));
                direction = selectDirection(ThreadLocalRandom.current().nextInt(0, 2));
                System.out.println(Arrays.toString(direction));
                if (!canPlaceCorridor()) {
                    direction[0] = -direction[0];
                    System.out.println(Arrays.toString(direction));
                    if (!canPlaceCorridor()) {
                        // todo: backtrack or exit
                        finished = true;
                        System.out.println("fail1");
                        return;
                    }
                }
            } else { // left or right
                System.out.println(Arrays.toString(direction));
                direction = selectDirection(ThreadLocalRandom.current().nextInt(2, 4));
                System.out.println(Arrays.toString(direction));
                if (!canPlaceCorridor()) {
                    direction[1] = -direction[1];
                    System.out.println(Arrays.toString(direction));
                    if (!canPlaceCorridor()) {
                        // todo: backtrack or exit
                        finished = true;
                        System.out.println("fail2");
                        return;
                    }
                }

            }

        }

        // move agent
        agentX += direction[0];
        agentY += direction[1];
        // digOutCell
        digOutCell(agentX, agentY);
    }



    ////

    private int[] selectDirection(int directionValue){
        return switch (directionValue) {
            case 0 -> new int[]{1, 0};   // right
            case 1 -> new int[]{-1, 0};  // left
            case 2 -> new int[]{0, 1};   // down
            default -> new int[]{0, -1}; // up
        };
    }
    private void generateRoom(){
        System.out.println("room");
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

    private boolean canPlaceCorridor(){

        // get 3 by 3 area ahead of agent
        int xMin, yMin, xMax, yMax;

        if (direction[0] == -1) { // moving left
            xMin = agentX - 3;
            xMax = agentX - 1;
        } else if (direction[0] == 1) { // moving right
            xMin = agentX + 1;
            xMax = agentX + 3;
        } else {
            xMin = agentX - 1;
            xMax = agentX + 1;
        }

        if (direction[1] == -1) { // moving up
            yMin = agentY - 3;
            yMax = agentY - 1;
        } else if (direction[1] == 1) { // moving down
            yMin = agentY + 1;
            yMax = agentY + 3;
        } else {
            yMin = agentY - 1;
            yMax = agentY + 1;
        }


        // check for out of bounds
        // this does not allow digger to travel along edges
        if (xMin < 0 || yMin < 0 || xMax >= mapWidth || yMax >= mapHeight) return false;

        if (direction[0] == 0){ // y axis movement
            for (int y = yMin; y < yMax; y++) {
                boolean fill = map[xMin][y];
                for (int x = xMin; x < xMax; x++) {
                    if (map[x][y] != fill) return false;
                }
            }
        } else { // x axis movement
            for (int x = xMin; x < xMax; x++) {
                boolean fill = map[x][yMin];
                for (int y = yMin; y < yMax; y++) {
                    if (map[x][y] != fill) return false;
                }
            }
        }
        return true;
    }

    private boolean isAreaConsistent(int xMin, int yMin, int xMax, int yMax){
        boolean fill = map[xMin][xMax];
        for (int x = xMin; x < xMax; x++) {
            for (int y = yMin; y < yMax; y++) {
                if (map[x][y] != fill) return false;
            }
        }
        return true;
    }

    @Override
    public boolean isFinished(){
        return finished;
    }

    @Override
    public boolean[][] getMap() {
        return map;
    }

    public int[] getAgentPosition() {
        return new int[] {agentX, agentY};
    }


}
