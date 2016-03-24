package active_entities;


import enums.RefState;
import interfaces.IContestantsBenchReferee;
import interfaces.IPlaygroundReferee;
import interfaces.IRefereeSiteReferee;
import interfaces.IRepoReferee;


public class Referee extends Thread {
    private IContestantsBenchReferee contestants_bench;
    private IRefereeSiteReferee referee_site;
    private IPlaygroundReferee playground;
    private IRepoReferee repo;

    public Referee(IPlaygroundReferee playground,
                   IRefereeSiteReferee referee_site,
                   IContestantsBenchReferee contestants_bench,
                   IRepoReferee repo){
        this.playground = playground;
        this.referee_site = referee_site;
        this.contestants_bench = contestants_bench;
        this.repo = repo;
    }

    public void run() {

        /*  to know which trial is it  */
        int trial_number = 1;
        RefState state = RefState.START_OF_THE_MATCH;
        Boolean has_next_trial = true;
        Boolean MATCH_ENDED = false;
        repo.refereeLog(state, trial_number);

        while (!MATCH_ENDED) {
            switch (state) {
                case START_OF_THE_MATCH:
                    this.referee_site.announceNewGame();
                    state = RefState.START_OF_A_GAME;
                    repo.Addheader(false);
                    repo.refereeLog(state, trial_number);
                    break;
                case START_OF_A_GAME:
                    /*  At the start of a game, the trial number is always 0  */
                    trial_number = 1;
                    this.contestants_bench.callTrial();
                    state = RefState.TEAMS_READY;
                    repo.refereeLog(state, trial_number);
                    break;
                case TEAMS_READY:
                    this.contestants_bench.startTrial();
                    state = RefState.WAIT_FOR_TRIAL_CONCLUSION;
                    repo.refereeLog(state, trial_number);
                    break;
                case WAIT_FOR_TRIAL_CONCLUSION:
                    has_next_trial = this.contestants_bench.assertTrialDecision();
                    repo.refereeLog(state, trial_number);
                    /*  if the trial decision says that there is a next trial, the referee has to call it  */
                    if (has_next_trial == true) {
                        this.contestants_bench.callTrial();
                        /*  when new trial is called, increment trial number  */
                        trial_number += 1;
                        state = RefState.TEAMS_READY;
                    }
                    /*  if not, the referee needs to declare a game winner  */
                    else{
                        this.referee_site.declareGameWinner();
                        state = RefState.END_OF_A_GAME;
                    }
                    repo.refereeLog(state, trial_number);
                    break;
                case END_OF_A_GAME:
                    if(this.referee_site.getN_games() > this.referee_site.getN_games_played()){
                        repo.Addheader(false);
                        this.referee_site.announceNewGame();
                        state = state.START_OF_A_GAME;
                        repo.refereeLog(state, trial_number);
                        break;
                    }
                    state = state.END_OF_A_MATCH;
                    repo.refereeLog(state, trial_number);
                    this.referee_site.declareMatchWinner();
                    break;
                case END_OF_A_MATCH:
                    MATCH_ENDED=true;
                    break;
                default:
                    state = RefState.START_OF_THE_MATCH;
                    repo.refereeLog(state, trial_number);
                    break;

            }
        }


        System.out.println("Referee finished execution");

    }
}
