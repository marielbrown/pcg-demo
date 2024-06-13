import pcg_tools.Digger;
import pcg_tools.LookAheadDigger;
import pcg_tools.PCGTool;
import processing.core.PApplet;

public class ContentGenerationDemoTool extends PApplet {
    Frame frame;

    PCGTool activeTool;
    boolean isRunning;
    float delay = 0.4f; //todo: set this
    float speedIncrement = 0.1f;
    float minDelay = 0.1f;
    float maxDelay = 1f;
    int frameCounter;

    public static void main(String[] args){
        String[] processingArgs = {"Level Designer"};
        ContentGenerationDemoTool app = new ContentGenerationDemoTool();
        PApplet.runSketch(processingArgs, app);
    }

    @Override
    public void settings(){
        // runs once before the processing sketch has been set up

        size(1920 - 70, 1080 - 70); // set initial window size
        noSmooth(); // turns off interpolation, must be run after size
    }

    @Override
    public void setup(){
        // runs once after the processing sketch has been set up
        frame = new Frame(this);
        activeTool = new Digger();
        activeTool.resetTool();

        frame.canvas.setMap(activeTool.getMap()); //todo: this needs done whenever the tool is changed
    }

    @Override
    public void draw(){
        // called every frame
        update();
        render();
    }

    public void setActiveTool(PCGTool activeTool) {
        this.activeTool = activeTool;
    }

    private void update(){
        if (isRunning){
            if (activeTool.isFinished()) {
                isRunning = false;
                return;
            }

            frameCounter++;

            if (frameCounter > delay * frameRate){
                activeTool.executeStep();
                frameCounter = 0;
            }
        }
    }

    private void render(){
        background(50, 50, 50);
        frame.render(this);

        PopUpDialog.renderAll(this);

        int[] agentCoords = activeTool.getAgentPosition();  //todo: temporary visual
        fill(10, 10, 200);

        circle(agentCoords[0] * frame.canvas.cellWidth + canvas.mapPosition.x + frame.canvas.cellWidth / 2, agentCoords[1] * frame.canvas.cellHeight + canvas.mapPosition.y + frame.canvas.cellHeight / 2, 10);
        int[] areaCoords = activeTool.getAgentAreaCoordinates();

        if (areaCoords != null) {
            System.out.println("" + areaCoords[0] + ", " + areaCoords[1] + ", " + areaCoords[2] + ", " + areaCoords[3]);
            strokeWeight(3);
            fill(0, 0);
            rect(areaCoords[0] * frame.canvas.cellWidth + frame.canvas.mapPosition.x, areaCoords[1] * frame.canvas.cellHeight + frame.canvas.mapPosition.y, (areaCoords[2] - areaCoords[0] + 1) * frame.canvas.cellWidth, (areaCoords[3] - areaCoords[1] + 1) * frame.canvas.cellHeight);
            strokeWeight(1);
        }
    }

    @Override
    public void mouseClicked() {
        frame.registerClick(mouseX, mouseY);

        PopUpDialog.registerClick(mouseX, mouseY);
    }
}
