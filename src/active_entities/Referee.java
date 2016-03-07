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

    private enum State {
        START_OF_THE_MATCH, START_OF_A_GAME, TEAMS_READY, WAIT_FOR_TRIAL_CONCLUSION, END_OF_A_GAME, END_OF_A_MATCH
    }

    public Referee(IPlaygroundReferee playground,
                   IRefereeSiteReferee referee_site,
                   IContestantsBenchReferee contestants_bench) {
        this.playground = playground;
        this.referee_site = referee_site;
        this.contestants_bench = contestants_bench;
    }

    public void run() {

        State state = State.START_OF_THE_MATCH;

        while (true){
            switch (state){
                case START_OF_THE_MATCH:
                    this.referee_site.announceNewGame();
                    state = State.START_OF_A_GAME;
                    break;
                case START_OF_A_GAME:
                    this.playground.callTrial();
                    state = State.TEAMS_READY;
                    break;
                case TEAMS_READY:
                    this.playground.startTrial();
                    state = State.WAIT_FOR_TRIAL_CONCLUSION;
                    break;

            }
        }

/*        this.START_OF_THE_MATCH = true;

        this.referee_site.announceNewGame();
        this.START_OF_THE_MATCH = false;
        this.START_OF_A_GAME = true;

        this.playground.callTrial();
        this.START_OF_A_GAME = false;
        this.TEAMS_READY = true;


        this.TEAMS_READY = false;
        this.WAIT_FOR_TRIAL_CONCLUSION = true;*/

        //System.out.println("Referee finished execution");

    }
}
