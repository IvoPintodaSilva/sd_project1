package active_entities;


import shared_mem.ContestantsBench;
import shared_mem.Playground;
import shared_mem.RefereeSite;

public class Referee extends Thread {
    private ContestantsBench contestants_bench;
    private RefereeSite referee_site;
    private Playground playground;
    //STATES
    public boolean START_OF_THE_MATCH;
    public boolean START_OF_A_GAME;
    public boolean TEAMS_READY;
    public boolean WAIT_FOR_TRIAL_CONCLUSION;
    public boolean END_OF_A_GAME;
    public boolean END_OF_A_MATCH;

    public Referee(Playground playground, RefereeSite referee_site, ContestantsBench contestants_bench) {
        this.playground = playground;
        this.referee_site = referee_site;
        this.contestants_bench = contestants_bench;
    }

    public void run() {
        this.START_OF_THE_MATCH = true;

        //while (true) {
        this.referee_site.announceNewGame();
        //}


        System.out.println("Referee finished execution");

    }
}
