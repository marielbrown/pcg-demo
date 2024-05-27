import processing.core.PApplet;
import processing.core.PConstants;

import java.awt.*;
import java.util.ArrayList;

public class PopUpDialog extends UIElement{

    Button closeButton;
    Button topBar;
    int buttonSize;

    static int currentDepth = 0;
    static ArrayList<PopUpDialog> dialogs = new ArrayList<>();
    static EventListener dialogHandler;

    PopUpDialog(ContentGenerationDemoTool app){
        super(app.frame);
        if (dialogHandler == null){
            dialogHandler = new DialogHandler();
        }

        position.x = 500 + currentDepth * 15;
        position.y = 200 +  currentDepth * 15;
        height = 300;
        width = 400;
        buttonSize = 25;

        // todo: temp ID
        // todo: x
        closeButton = new Button(this, Constants.BUTTON_ID_CLOSE, position.x + width - buttonSize, position.y, buttonSize, buttonSize);
        topBar = new Button(this, 0, position.x, position.y, width - buttonSize, buttonSize);

        closeButton.addEventListener(dialogHandler);

        currentDepth += 1;
    }

    public void closeDialog(){
        dialogs.remove(this);
        if (dialogs.size() == 0) {
            currentDepth = 0;
        }
    }

    public static void makeDialog(ContentGenerationDemoTool app){
        dialogs.add(new PopUpDialog(app));
        currentDepth += 1;
    }

    public static void renderAll(PApplet app){
        for (PopUpDialog dialog: dialogs) {
            dialog.render(app);
        }
    }

    @Override
    public void render(PApplet app) {
        app.fill(defaultColour);
        app.rect(position.x, position.y, width, height);
        app.rect(position.x, position.y, width, buttonSize);

        topBar.render(app);
        closeButton.render(app);
    }

    public static void registerClick(int mouseX, int mouseY){
        for (PopUpDialog dialog: PopUpDialog.dialogs){
            Button button = dialog.closeButton;
            if (button.isMouseOver(mouseX, mouseY)){
                button.onClick();
                break;
            }
        }
    }

    private static class DialogHandler implements EventListener {

        @Override
        public void actionPerformed(int actionID, Object source) {
            switch (actionID){
                case Constants.BUTTON_ID_CLOSE:
                    PopUpDialog dialog = (PopUpDialog) ((UIElement) source).getParent(); //todo: this is messy
                    dialog.closeDialog();

            }
        }
    }

}
