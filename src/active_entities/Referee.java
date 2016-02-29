package active_entities;


public class Referee extends Thread {
    //STATES
    private boolean START_OF_THE_MATCH;
    private boolean START_OF_A_GAME;
    private boolean TEAMS_READY;
    private boolean WAIT_FOR_TRIAL_CONCLUSION;
    private boolean END_OF_A_GAME;
    private boolean END_OF_A_MATCH;


    public Referee() {
    }

    public void run() {
    }

}
