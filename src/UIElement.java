import processing.core.PApplet;
import processing.core.PVector;

public abstract class UIElement {
    protected PVector position = new PVector();
    protected float width, height;

    boolean isMouseOver(int mouseX, int mouseY){
        return mouseX > position.x && mouseX < position.x + width &&
                mouseY > position.y && mouseY < position.y + height;
    }

    public abstract void render(PApplet app);
}
