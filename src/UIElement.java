import processing.core.PApplet;
import processing.core.PVector;

public abstract class UIElement {
    protected PVector position = new PVector();
    protected float width, height;

    public abstract void render(PApplet app);
}
