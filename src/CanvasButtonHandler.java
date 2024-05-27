import processing.core.PApplet;

public class CanvasButtonHandler implements EventListener {

    ContentGenerationDemoTool app;

    CanvasButtonHandler(ContentGenerationDemoTool app) {
        this.app = app;
    }

    @Override
    public void actionPerformed(int actionID, Object source) {
        switch (actionID) {
            case Constants.BUTTON_ID_GENERATE:
                if (app.activeTool.isFinished()) {
                    app.activeTool.resetTool();
                }
                app.frame.canvas.setMap(app.activeTool.getMap()); //todo: this is upsetting to look at. maybe functions can be changed a bit?
                app.activeTool.runGeneration();
                break;
            case Constants.BUTTON_ID_PAUSE:
                //todo: pause
                break;
            case Constants.BUTTON_ID_PLAY:
                //todo: play
                break;
            case Constants.BUTTON_ID_STEP:
                if (app.activeTool.isFinished()) return;
                app.activeTool.executeStep();
                break;
            case Constants.BUTTON_ID_CLEAR:
                app.activeTool.resetTool();
                app.frame.canvas.setMap(app.activeTool.getMap()); // todo: see previous comment
        }
    }
}
