package pcg_tools;

import java.util.concurrent.ThreadLocalRandom;

public class LookAheadDigger implements PCGTool {

    private boolean[][] map; // true if cell is traversable

    // parameters adjustable by user
    int mapWidth = 70, mapHeight = 50;
    int percentageTraversable = 20;
    int minimumRoomDimension = 2, maximumRoomDimension = 6;
    float roomGenerationChanceModifier = 2;
    float directionChangeChanceModifier = 0.5f;

    // state variables updated in code
    private boolean lookStep = true;
    private int[] direction;
    private Agent agent;
    private int targetTraversableCells, currentTraversableCells;
    private float directionChangeChance, roomGenerationChance;
    private boolean readyToDrawRoom = true;
    private boolean roomSelected;

    private int directionsChecked;

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
        agent = new Agent();

        directionChangeChance = 5;
        roomGenerationChance = 100; // always place room at start // todo: make this optional
        targetTraversableCells = (int) (mapWidth * mapHeight * (percentageTraversable / 100f));
        currentTraversableCells = 0;

        agent.x = mapWidth / 2; // start in centre
        agent.y = mapHeight / 2;

        direction = selectDirection(ThreadLocalRandom.current().nextInt(0, 4));
        finished = false;   
    }

    public void executeStep(){
        if (lookStep) {
            executeLookStep();
        } else {
            executeDigStep();
        }

        if (!finished) {
            finished = currentTraversableCells >= targetTraversableCells;
        }
    }

    private void executeLookStep(){
        // decide if room or corridor is being placed
        if (roomPlacementIsSelected()) {
            setLookAheadCoordinates(maximumRoomDimension, maximumRoomDimension);  // room

            if (canPlaceRoom()) {
                roomSelected = true; // tell draw step to draw a room
                lookStep = false; // switch to next step
                roomGenerationChance = -maximumRoomDimension; // reduces room overlapping by delaying placement of next room
            } else {
                readyToDrawRoom = false; // try looking for corridor next
            }

        } else {    // corridor lookahead
            setLookAheadCoordinates(3, 3);  // corridor
            if (canPlaceCorridor()){
                directionsChecked = 1;  // reset valid direction counter
                roomSelected = false; // tell draw step to draw a corridor
                lookStep = false; // switch to next step
                readyToDrawRoom = true;
                roomGenerationChance += roomGenerationChanceModifier;

            } else {
                changeDirection(); // no need to change state as lookahead will be repeated
                directionsChecked++;
            }


        }
    }

    private void executeDigStep(){
        if (roomSelected) {
            generateRoom();
        } else {
            placeCorridor();
        }
        lookStep = true;
    }

    private void setLookAheadCoordinates(int width, int height){
        if (direction[0] == -1) { // moving left
            agent.areaMinX = agent.x - width;
            agent.areaMaxX = agent.x - 1;
        } else if (direction[0] == 1) { // moving right
            agent.areaMinX = agent.x + 1;
            agent.areaMaxX = agent.x + width;
        } else {
            agent.areaMinX = agent.x - width / 2;
            agent.areaMaxX = agent.x + width / 2;
        }

        if (direction[1] == -1) { // moving up
            agent.areaMinY = agent.y - height;
            agent.areaMaxY = agent.y - 1;
        } else if (direction[1] == 1) { // moving down
            agent.areaMinY = agent.y + 1;
            agent.areaMaxY = agent.y + height;
        } else {
            agent.areaMinY= agent.y - height / 2;
            agent.areaMaxY = agent.y + height / 2;
        }
    }

    private boolean canPlaceCorridor(){
        // check for out of bounds
        if (agent.areaMinX < 0 || agent.areaMinY < 0 || agent.areaMaxX >= mapWidth || agent.areaMaxY >= mapHeight) return false;
        //todo: if out of bounds, update look ahead to show this

        boolean[][] lookaheadMap;

        //if horizontal movement, transpose
        if (direction[0] == 0) {
            lookaheadMap = createLookAheadMap();
        } else {
            lookaheadMap = createTransposedLookAheadMap();
        }

        // check all rows are internally consistent
        for (int y = 0 ; y < 3; y++) {
            boolean fill = lookaheadMap[0][y];
            for (int x = 0; x < 3; x++) {
                if (lookaheadMap[x][y] != fill) return false;
            }
        }

        boolean rowFar = lookaheadMap[0][0];
        boolean rowMid = lookaheadMap[0][1];
        boolean rowNear = lookaheadMap[0][2];

        //if left or down swap near and far
        if (direction[0] == 1 || direction[1] == 1){
            boolean swap = rowFar;
            rowFar = rowNear;
            rowNear = swap;
        }

        // check remaining cases
        if (!rowNear && (rowFar || rowMid)) return false;
        if (!rowMid && rowFar) return false;

        return true;
    }

    private boolean[][] createLookAheadMap(){
        boolean[][] lookAheadMap = new boolean[agent.areaMaxX - agent.areaMinX + 1][agent.areaMaxY - agent.areaMinY + 1];

        for (int y =  agent.areaMinY ; y <= agent.areaMaxY; y++) {
            for (int x = agent.areaMinX; x <= agent.areaMaxX; x++) {
                lookAheadMap[x - agent.areaMinX][y - agent.areaMinY] = map[x][y];
            }
        }

        return lookAheadMap;
    }
    private boolean[][] createTransposedLookAheadMap(){
        boolean[][] lookAheadMap = new boolean[agent.areaMaxX - agent.areaMinX + 1][agent.areaMaxY - agent.areaMinY + 1];

        for (int y =  agent.areaMinY ; y <= agent.areaMaxY; y++) {
            for (int x = agent.areaMinX; x <= agent.areaMaxX; x++) {
                lookAheadMap[y - agent.areaMinY][x - agent.areaMinX] = map[x][y];
            }
        }

        return lookAheadMap;
    }

    private boolean roomPlacementIsSelected(){
        if (!readyToDrawRoom) return false;
        int roomGenerationValue = ThreadLocalRandom.current().nextInt(0, 100);
        return roomGenerationValue < roomGenerationChance;
    }

    private void placeCorridor(){
        // move agent
        agent.x += direction[0];
        agent.y += direction[1];
        digOutCell(agent.x, agent.y);
    }

    private void changeDirection(){
        switch (directionsChecked) {
            case 1 -> {
                int directionOption = ThreadLocalRandom.current().nextInt(0, 2);
                if (direction[0] == 0) { // if moving along y axis
                    direction = selectDirection(directionOption);
                } else { // if moving along x axis
                    direction = selectDirection(directionOption + 2);
                }
                break;
            }
            case 2 -> {
                direction[0] = -direction[0];
                direction[1] = -direction[1];
                break;
            }
            case 3 -> finished = true; // dead end reached
        }
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


        int startX = agent.x - roomWidth / 2;    // centre room on digger
        int startY = agent.y - roomHeight / 2;
        int endX, endY;

        //todo: is this still needed with look ahead coordinate setting?
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

    private boolean canPlaceRoom(){
        // todo: temporary oob checking
        if (agent.areaMinX < 0 || agent.areaMinY < 0 || agent.areaMaxX >= mapWidth || agent.areaMaxY >= mapHeight) return false;

        //todo: only draws room if max room size is possible
        for (int x = agent.areaMinX; x <= agent.areaMaxX; x++) {
            for (int y = agent.areaMinY; y <= agent.areaMaxY; y++) {
                if (map[x][y]) return false;
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
        return new int[] {agent.x, agent.y};
    }

    public int[] getAgentAreaCoordinates() {
        return new int[]{agent.areaMinX, agent.areaMinY, agent.areaMaxX, agent.areaMaxY};
    }


}
