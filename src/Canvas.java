import processing.core.PApplet;

public class Canvas extends UIElement{

    Canvas(PApplet app){
        width = app.width * 0.6f;   // 3/5 size of screen
        height = app.height * 0.6f;
        position.x = (app.width * 1.2f - width) / 2;
        position.y = (app.height - height) / 2;
    }


    @Override
    public void render(PApplet app) {
        app.stroke(0);
        app.fill(120, 120, 120);
        app.rect(position.x, position.y, width, height);
    }
}
