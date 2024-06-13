import processing.core.PApplet;

public class Button extends UIElement{
    final int ID;
    String text;


    EventListener buttonHandler; // for now, just one listener per button

    Button(UIElement parent, int ID, float xPosition, float yPosition, float width, float height){
        this(parent, ID, xPosition, yPosition,  width,  height, "");
    }
    Button(UIElement parent, int ID, float xPosition, float yPosition, float width, float height, String text){
        super(parent);
        this.ID = ID;
        this.text = text;

        position.x = xPosition;
        position.y = yPosition;
        this.width = width;
        this.height = height;
    };

    @Override
    public void render(PApplet app) {
        app.stroke(0);
        app.fill(120, 120, 120);
        app.rect(position.x, position.y, width, height);

        app.fill(0);
        app.textSize(30);
        app.text(text, position.x + 10, position.y + 30);
    }

    public void addEventListener(EventListener buttonHandler) {
        this.buttonHandler = buttonHandler;
    }

    public void onClick(){
        buttonHandler.actionPerformed(ID, this);
    }
}
