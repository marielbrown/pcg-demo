import pcg_tools.Digger;
import pcg_tools.LookAheadDigger;
import pcg_tools.PCGTool;
import processing.core.PApplet;
import processing.core.PVector;

public class ContentGenerationDemoTool extends PApplet {

    Sidebar sidebar;
    ParameterSidebar parameterSidebar;
    Canvas canvas;

    PCGTool activeTool;

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
        sidebar = new Sidebar(this);
        parameterSidebar = new ParameterSidebar(this);
        canvas = new Canvas(this);

        activeTool = new LookAheadDigger();
        activeTool.resetTool();
        canvas.setMap(activeTool.getMap()); //todo: this needs done whenever the tool is changed
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
        //stub
    }

    private void render(){
        background(50, 50, 50);
        sidebar.render(this);
        parameterSidebar.render(this);
        canvas.render(this);

        int[] agentCoords = activeTool.getAgentPosition();  //todo: temporary visual
        fill(10, 10, 200);

        circle(agentCoords[0] * canvas.cellWidth + canvas.mapPosition.x + canvas.cellWidth / 2, agentCoords[1] * canvas.cellHeight + canvas.mapPosition.y + canvas.cellHeight / 2, 10);
        int[] areaCoords = activeTool.getAgentAreaCoordinates();

        if (areaCoords != null) {
            System.out.println("" + areaCoords[0] + ", " + areaCoords[1] + ", " + areaCoords[2] + ", " + areaCoords[3]);
            strokeWeight(3);
            fill(0, 0);
            rect(areaCoords[0] * canvas.cellWidth + canvas.mapPosition.x, areaCoords[1] * canvas.cellHeight + canvas.mapPosition.y, (areaCoords[2] - areaCoords[0] + 1) * canvas.cellWidth, (areaCoords[3] - areaCoords[1] + 1) * canvas.cellHeight);
            strokeWeight(1);
        }
    }

    @Override
    public void mouseClicked() {
        for (Button button: sidebar.buttons){
           if (button.isMouseOver(mouseX, mouseY)){
                button.onClick();
                break;
            }
        }
        for (Button button: parameterSidebar.buttons){
            if (button.isMouseOver(mouseX, mouseY)){
                button.onClick();
                break;
            }
        }
        for (Button button: canvas.buttons){
            if (button.isMouseOver(mouseX, mouseY)){
                button.onClick();
                break;
            }
        }
    }
}
