import processing.core.PApplet;

public class Frame extends UIElement{

    Sidebar sidebar;
    ParameterSidebar parameterSidebar;
    Canvas canvas;

    Frame(ContentGenerationDemoTool app){
        super(null); //top level UI Element with no parent

        sidebar = new Sidebar(app);
        parameterSidebar = new ParameterSidebar(app);
        canvas = new Canvas(app);
    }

    @Override
    public void render(PApplet app) {
        sidebar.render(app);
        parameterSidebar.render(app);
        canvas.render(app);
    }

    public void registerClick(int mouseX, int mouseY) {
        for (Button button: sidebar.buttons){
            if (button.isMouseOver(mouseX, mouseY)){
                button.onClick();
                break;
            }
        }
        for (Button button: parameterSidebar.buttons){
            if (button.isMouseOver(mouseX, mouseY)){
                button.onClick();
                break;
            }
        }
        for (Button button: canvas.buttons){
            if (button.isMouseOver(mouseX, mouseY)){
                button.onClick();
                break;
            }
        }
    }
}
