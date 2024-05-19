import processing.core.PApplet;

public class ContentGenerationDemoTool extends PApplet {

    Sidebar sidebar;
    Canvas canvas;

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
        canvas = new Canvas(this);
    }

    @Override
    public void draw(){
        // called every frame
        update();
        render();
    }

    private void update(){
        //stub
    }

    private void render(){
        background(50, 50, 50);
        sidebar.render(this);
        canvas.render(this);
    }

    @Override
    public void mouseClicked() {
    }
}
