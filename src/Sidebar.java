import processing.core.PApplet;

public class Sidebar extends UIElement{

    public Button[] buttons;
    EventListener buttonHandler;

    private boolean closed;

    Sidebar(ContentGenerationDemoTool app) {
        width = app.width * 0.2f - 35;   // 1/5 size of screen
        height = app.height;
        position.x = 0;
        position.y = 0;


        buttons = new Button[2];
        buttons[0] = new Button(0, position.x + 40, position.y + 45, 200, 40, "PCG Method 1");

        buttons[1] = new Button(9, position.x + width, position.y, 25, height); //todo: in progress, close button

        buttonHandler = new ToolButtonHandler(app);

        for (Button button: buttons){
            button.addEventListener(buttonHandler);
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

    }

    public void openClose(){
        if (closed) {
            open();
            closed = false;
        } else {
            close();
            closed = true;
        }
    }

    private void open() {
        position.x += width;
        for (Button button : buttons){
            button.position.x += width;
        }
    }

    private void close() {
        position.x -= width;
        for (Button button : buttons){
            button.position.x -= width;
        }
    }
}
