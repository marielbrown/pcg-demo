import processing.core.PApplet;

public class PopUpDialog extends UIElement{

    Button closeButton;
    Button topBar;
    int buttonSize;

    PopUpDialog(){
        position.x = 500;
        position.y = 200;
        height = 300;
        width = 400;
        buttonSize = 25;

        // todo: temp ID
        // todo: x
        // todo: buttonHandler
        closeButton = new Button(0, position.x + width - buttonSize, position.y, buttonSize, buttonSize, "");
        topBar = new Button(0, position.x, position.y, width - buttonSize, buttonSize, "");

    }

    @Override
    public void render(PApplet app) {
        app.fill(defaultColour);
        app.rect(position.x, position.y, width, height);
        app.rect(position.x, position.y, width, buttonSize);

        topBar.render(app);
        closeButton.render(app);
    }
}
