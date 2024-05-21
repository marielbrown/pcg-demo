import processing.core.PApplet;

public class CanvasButtonHandler implements EventListener {

    ContentGenerationDemoTool app;

    CanvasButtonHandler(ContentGenerationDemoTool app) {
        this.app = app;
    }

    @Override
    public void actionPerformed(int ID) {
        switch (ID){
            case 1:
                System.out.println("map button clicked!");
                app.activeTool.generateNewMap(app, 70, 50);
                app.canvas.setMap(app.activeTool.getMap()); //todo: temporary solution
        }
    }
}
