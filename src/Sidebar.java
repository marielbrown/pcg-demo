import processing.core.PApplet;

public class Sidebar extends UIElement{

    public Button[] buttons;
    EventListener buttonHandler;

    Sidebar(ContentGenerationDemoTool app) {
        width = app.width * 0.2f;   // 1/5 size of screen
        height = app.height;
        position.x = 0;
        position.y = 0;


        buttons = new Button[1];
        buttons[0] = new Button(0, position.x + 40, position.y + 45, 200, 40, "PCG Method 1");

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
}
