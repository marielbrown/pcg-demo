package pcg_tools;

public interface PCGTool {
    boolean[][] getMap();

    /**
     *  Run PGC tool to until the generation is complete
     */
    void runGeneration();

    /**
     * Run a single step of the generator
     */
    void executeStep();

    /**
     *  Clear any generation progress and return to start state
     */
    void resetTool();

    /**
     * Check end state of generator has been reached
     * @return true if generation is complete
     */
    boolean isFinished();

    int[] getAgentPosition();
}
