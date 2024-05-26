import processing.core.PApplet;
import processing.core.PVector;


public abstract class UIElement {
    protected PVector position = new PVector();
    protected float width, height;
    protected int defaultColour = -8882056; // obtained from processing color function

    protected Object parent; //todo: less general? UIElment?

    UIElement(Object parent){
        this.parent = parent;
    }

    boolean isMouseOver(int mouseX, int mouseY){
        return mouseX > position.x && mouseX < position.x + width &&
                mouseY > position.y && mouseY < position.y + height;
    }

    public Object getParent() {
        return parent;
    }

    public abstract void render(PApplet app);
}
