import pcg_tools.Digger;
import pcg_tools.PCGTool;
import processing.core.PApplet;

public class ContentGenerationDemoTool extends PApplet {
    Frame frame;

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
        frame = new Frame(this);
        activeTool = new Digger();
        activeTool.resetTool();

        frame.canvas.setMap(activeTool.getMap()); //todo: this needs done whenever the tool is changed

        PopUpDialog.makeDialog(this);
        PopUpDialog.makeDialog(this);
        PopUpDialog.makeDialog(this);
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
        frame.render(this);

        PopUpDialog.renderAll(this);
    }

    @Override
    public void mouseClicked() {
        frame.registerClick(mouseX, mouseY);

        PopUpDialog.registerClick(mouseX, mouseY);
    }
}
