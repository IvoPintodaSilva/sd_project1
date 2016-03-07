package active_entities;


import interfaces.IContestantsBenchReferee;
import interfaces.IPlaygroundReferee;
import interfaces.IRefereeSiteReferee;
import shared_mem.MContestantsBench;
import shared_mem.MPlayground;
import shared_mem.MRefereeSite;

public class Referee extends Thread {
    private IContestantsBenchReferee contestants_bench;
    private IRefereeSiteReferee referee_site;
    private IPlaygroundReferee playground;
    //STATES
    public boolean START_OF_THE_MATCH;
    public boolean START_OF_A_GAME;
    public boolean TEAMS_READY;
    public boolean WAIT_FOR_TRIAL_CONCLUSION;
    public boolean END_OF_A_GAME;
    public boolean END_OF_A_MATCH;

    public Referee(IPlaygroundReferee playground,
                   IRefereeSiteReferee referee_site,
                   IContestantsBenchReferee contestants_bench) {
        this.playground = playground;
        this.referee_site = referee_site;
        this.contestants_bench = contestants_bench;
    }

    public void run() {
        this.START_OF_THE_MATCH = true;

        this.referee_site.announceNewGame();
        this.START_OF_THE_MATCH = false;
        this.START_OF_A_GAME = true;

        this.playground.callTrial();
        this.START_OF_A_GAME = false;
        this.TEAMS_READY = true;

        this.playground.startTrial();
        this.TEAMS_READY = false;
        this.WAIT_FOR_TRIAL_CONCLUSION = true;

        System.out.println("Referee finished execution");

    }
}
