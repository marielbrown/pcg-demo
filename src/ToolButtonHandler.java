import pcg_tools.Digger;

public class ToolButtonHandler implements EventListener {
    ContentGenerationDemoTool app;

    ToolButtonHandler(ContentGenerationDemoTool app){
        this.app = app;
    }
    @Override
    public void actionPerformed(int actionID, Object source) {
        System.out.println("click!!");
        switch (actionID){
            case 0: //todo: change this to a constant
                app.setActiveTool(new Digger());
                break;
            case 9:
                app.sidebar.openClose();
                break;
            case 10: // todo: move to different handler?
                app.parameterSidebar.openClose();
                break;
        }
    }
}
