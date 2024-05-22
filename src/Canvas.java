import processing.core.PApplet;

public class Canvas extends UIElement{

    boolean[][] map;

    Button[] buttons;
    EventListener buttonHandler;

    Canvas(ContentGenerationDemoTool app){
        width = app.width * 0.6f;   // 3/5 size of screen
        height = app.height * 0.6f;
        position.x = (app.width * 1.2f - width) / 2;
        position.y = (app.height - height) / 2;

        buttonHandler = new CanvasButtonHandler(app);

        buttons = new Button[4]; // todo: temporary placement - move to toolbar class?
        String[] buttonText = {"Run", "Pause", "Play", "Step"}; //todo: speed controls
        int buttonWidth = 100;
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button(i, position.x + buttonWidth * i, position.y + height + 10, buttonWidth, 50, buttonText[i]);
            buttons[i].addEventListener(buttonHandler);
        }


    }


    @Override
    public void render(PApplet app) {
        app.stroke(0);
        app.fill(120, 120, 120);
        app.rect(position.x, position.y, width, height);

        for (Button button: buttons){
            button.render(app);
        }


        if (map == null){
            return;
        }

        float cellHeight = height / map[0].length;
        float cellWidth = width / map.length;

        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                if (map[x][y]){
                    app.fill(20, 100, 10);
                } else {
                    app.fill(200, 10, 100);
                }
                app.rect(x * cellWidth + position.x, y * cellHeight + position.y, cellWidth, cellHeight);
            }
        }
    }

    public void setMap(boolean[][] map) {
        this.map = map;
    }
}
