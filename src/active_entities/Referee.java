package active_entities;


import enums.RefState;
import interfaces.IContestantsBenchReferee;
import interfaces.IPlaygroundReferee;
import interfaces.IRefereeSiteReferee;
import interfaces.IRepoReferee;
import structures.GameStat;
import structures.TrialStat;


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
        int score_T1= 0;
        int score_T2 = 0;
        int knock_out=-1;

        RefState state = RefState.START_OF_THE_MATCH;
        Boolean has_next_trial = true;
        Boolean MATCH_ENDED = false;
        repo.refereeLog(state, trial_number);

        while (!MATCH_ENDED) {
            switch (state) {
                case START_OF_THE_MATCH:
                    this.referee_site.announceNewGame();
                    repo.updGame_nr();
                    state = RefState.START_OF_A_GAME;
                    repo.Addheader(false);
                    repo.refereeLog(state, trial_number);
                    break;
                case START_OF_A_GAME:
                    /*  At the start of a game, the trial number is always 0  */
                    this.repo.updtRopeCenter(Integer.MAX_VALUE);
                    trial_number = 1;
                    score_T1=0;
                    score_T2=0;
                    knock_out=-1;
                    this.contestants_bench.callTrial();
                    state = RefState.TEAMS_READY;
                    repo.refereeLog(state, trial_number);
                    break;
                case TEAMS_READY:
                    this.contestants_bench.startTrial();
                    this.repo.updtRopeCenter(0);
                    state = RefState.WAIT_FOR_TRIAL_CONCLUSION;
                    repo.refereeLog(state, trial_number);
                    break;
                case WAIT_FOR_TRIAL_CONCLUSION:
                    TrialStat unpack;
                    unpack = this.playground.assertTrialDecision();
                    has_next_trial = unpack.isHas_next_trial();
                    repo.updtRopeCenter(unpack.getCenter_rope());//update rope center
                    switch (unpack.getWonType()){
                        case DRAW:
                            score_T1 +=1;
                            score_T2 +=1;
                            System.out.println("draw");
                            break;
                        case KNOCKOUT:

                            has_next_trial = false;
                            System.out.println("knockout");
                            if(unpack.getTeam()==1)
                            {
                                knock_out=1;//team 2 was knocked out
                            }
                            else
                            {
                                knock_out=2;
                            }
                            break;
                        case POINTS:
                            if(unpack.getTeam()==1) {
                                score_T1 += 1;
                                System.out.println("points T1");
                            }else{
                                score_T2 +=1;
                                System.out.println("points T2");
                            }
                            break;
                    }


                    repo.refereeLog(state, trial_number);
                    /*  if the trial decision says that there is a next trial, the referee has to call it  */
                    if (has_next_trial == true) {
                        this.repo.updtRopeCenter(Integer.MAX_VALUE);
                        this.contestants_bench.callTrial();
                        /*  when new trial is called, increment trial number  */
                        trial_number += 1;
                        state = RefState.TEAMS_READY;
                    }
                    /*  if not, the referee needs to declare a game winner  */
                    else{

                        GameStat game_result=this.referee_site.declareGameWinner(score_T1,score_T2,knock_out);
                        this.repo.setResult(game_result.getWinnerTeam(),game_result.getWonType(),trial_number);
                        state = RefState.END_OF_A_GAME;

                    }
                    repo.refereeLog(state, trial_number);
                    break;
                case END_OF_A_GAME:
                    //this.repo.setResult(1,"knock out",3);

                    if(this.referee_site.getN_games() > this.referee_site.getN_games_played()){
//                        System.out.println("N games"+this.referee_site.getN_games() );
//                        System.out.println("N games played"+this.referee_site.getN_games_played() );
                        this.referee_site.announceNewGame();
                        repo.updGame_nr();
                        state = state.START_OF_A_GAME;
                        trial_number = 0;
                        repo.refereeLog(state, trial_number);
                        break;
                    }
                    state = state.END_OF_A_MATCH;
                    this.contestants_bench.declareMatchWinner();
                    repo.refereeLog(state, trial_number);
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
