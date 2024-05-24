import processing.core.PApplet;
import processing.core.PVector;

public class Canvas extends UIElement{

    boolean[][] map;

    Button[] buttons;
    EventListener buttonHandler;

    PVector mapPosition = new PVector();
    float mapWidth, mapHeight;



    Canvas(ContentGenerationDemoTool app){
        position.x = app.width * 0.2f - 15;
        position.y = 0;
        width = app.width - (position.x * 2);
        height = app.height;

        mapWidth = width * 0.8f;   // centred in canvas area
        mapHeight = height * 0.6f;
        mapPosition.x = position.x + (width - mapWidth) / 2;
        mapPosition.y = (height - mapHeight) / 2;

        buttonHandler = new CanvasButtonHandler(app);


        String[] buttonText = {"Run", "Pause", "Play", "Step", "Clear"}; //todo: speed controls
        buttons = new Button[buttonText.length]; // todo: temporary placement - move to toolbar class?
        int buttonWidth = 100;
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button(i, mapPosition.x + buttonWidth * i, mapPosition.y + mapHeight + 10, buttonWidth, 50, buttonText[i]);
            buttons[i].addEventListener(buttonHandler);
        }


    }


    @Override
    public void render(PApplet app) {
        app.stroke(0);


        for (Button button: buttons){
            button.render(app);
        }


        if (map == null){ //todo: is this possible?
            return;
        }

        float cellHeight = mapHeight / map[0].length;
        float cellWidth = mapWidth / map.length;

        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                if (map[x][y]){
                    app.fill(20, 100, 10);
                } else {
                    app.fill(200, 10, 100);
                }
                app.rect(x * cellWidth + mapPosition.x, y * cellHeight + mapPosition.y, cellWidth, cellHeight);
            }
        }

        //app.fill(50, 80, 120);    // for testing
        //app.rect(position.x, position.y, width, height);
    }

    public void setMap(boolean[][] map) {
        this.map = map;
    }

    /**
     * Reposition the canvas and relevant elements
     * @param newPosition PVector of canvas top left corner
     */
    public void reposition(PVector newPosition){
        PVector difference = PVector.sub(newPosition, position);
        position = newPosition;

        for (Button button : buttons) {
            button.position = button.position.add(difference);
        }
    }
}
