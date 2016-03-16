package active_entities;


import interfaces.IContestantsBenchReferee;
import interfaces.IPlaygroundReferee;
import interfaces.IRefereeSiteReferee;


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
        Boolean has_next_trial = true;

        while (has_next_trial) {
            switch (state) {
                case START_OF_THE_MATCH:
                    this.referee_site.announceNewGame();
                    state = State.START_OF_A_GAME;
                    break;
                case START_OF_A_GAME:
                    this.contestants_bench.callTrial();
                    state = State.TEAMS_READY;
                    break;
                case TEAMS_READY:
                    this.contestants_bench.startTrial();
                    state = State.WAIT_FOR_TRIAL_CONCLUSION;
                    break;
                case WAIT_FOR_TRIAL_CONCLUSION:
                    has_next_trial = this.contestants_bench.assertTrialDecision();
                    /*  if the trial decision says that there is a next trial, the referee has to call it  */
                    if (has_next_trial == true) {
                        this.contestants_bench.callTrial();
                        state = State.TEAMS_READY;
                    }
                    /*  if not, the referee needs to declare a game winner  */
                    else{
                        this.referee_site.declareGameWinner();
                        state = State.END_OF_A_GAME;
                    }
                    break;
                case END_OF_A_GAME:
                    if(this.referee_site.getN_games() > this.referee_site.getN_games_played())
                    {
                    this.referee_site.announceNewGame();
                    state = state.START_OF_A_GAME;
                     break;
                    }
                    state = state.END_OF_A_MATCH;
                    this.referee_site.declareMatchWinner();
                    break;
                case END_OF_A_MATCH:
                    break;
                default:
                    state = State.START_OF_THE_MATCH;
                    break;

            }
        }


        System.out.println("Referee finished execution");

    }
}
