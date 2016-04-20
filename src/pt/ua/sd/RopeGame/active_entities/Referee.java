package pt.ua.sd.RopeGame.active_entities;


import pt.ua.sd.RopeGame.enums.RefState;
import pt.ua.sd.RopeGame.enums.WonType;
import pt.ua.sd.RopeGame.interfaces.IContestantsBenchReferee;
import pt.ua.sd.RopeGame.interfaces.IPlaygroundReferee;
import pt.ua.sd.RopeGame.interfaces.IRefereeSiteReferee;
import pt.ua.sd.RopeGame.interfaces.IRepoReferee;
import pt.ua.sd.RopeGame.structures.GameStat;
import pt.ua.sd.RopeGame.structures.TrialStat;



/**
 * Referee thread<br>
 *<b><center><font size=6>Referee thread</font></center></b><br>
 *     <font size=4>This class represents the thread of the referee, his life cycle ends when
 *     the internal flag MATCH_ENDED takes the positive notation.</font>
 *     Notes:
 *     -> the access to the shared memories is limited by the interfaces present in the interfaces package.
 *     -> the default state is START_OF_THE_MATCH
 *
 *
 */
public class Referee extends Thread {
    /**
     * Internal Data
     */
    private IContestantsBenchReferee contestants_bench;//represents the bench shared memory
    private IRefereeSiteReferee referee_site;//represents the referee site shared memory
    private IPlaygroundReferee playground;//represents the playground shared memory
    private IRepoReferee repo;//represents the general info repository of shared memory
    private int n_players;//number of players in each team, defined in rg.config
    private int n_players_pushing;//number of players in each team pushing at any given trial, defined in rg.config
    private int n_trials;//number of trials, defined in rg.config
    private int n_games;//number of games, defined in rg.config
    private int knockDif;//number of knockout difference needed to win, defined in rg.config



    /**
     * Constructor
     * @param playground playground shared memory instancy
     * @param referee_site referee site shared memory instancy
     * @param contestants_bench contestants bench shared memory instancy
     * @param repo general info repository shared memory instancy
     */
    public Referee(IPlaygroundReferee playground,
                   IRefereeSiteReferee referee_site,
                   IContestantsBenchReferee contestants_bench,
                   IRepoReferee repo,
                   int n_players, int n_players_pushing,
                   int n_trials, int n_games, int knockDif){
        this.playground = playground;
        this.referee_site = referee_site;
        this.contestants_bench = contestants_bench;
        this.repo = repo;
        this.n_players = n_players;
        this.n_players_pushing = n_players_pushing;
        this.n_trials = n_trials;
        this.n_games = n_games;
        this.knockDif = knockDif;
    }


    /**
     * Thread life cycle
     */
    public void run() {

        int trial_number = 1;//to know which trial is it
        int score_T1= 0;//score of trials for team 1 in the current game
        int score_T2 = 0;//score of trials for team 2 in the current game
        int knock_out=-1;//indicates who knocked out the other team, the int represents the team id. -1 is invalid
        int gamesWon_T1=0;//score of games for team 1
        int gamesWon_T2=0;//score of games for team 2

        RefState state = RefState.START_OF_THE_MATCH;
        Boolean has_next_trial;
        Boolean MATCH_ENDED = false;//flag for end the life cycle
        repo.refereeLog(state, trial_number);//update refereee state in central info repository

        while (!MATCH_ENDED) {//this value can change when the referee reaches the state END_OF_A_MATCH
            switch (state) {
                case START_OF_THE_MATCH:
                    this.referee_site.announceNewGame();
                    repo.updGame_nr();
                    state = RefState.START_OF_A_GAME;
                    repo.Addheader(false);//update central info repository adding the header with game nr
                    repo.refereeLog(state, trial_number);//update central info repository
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
                    repo.refereeLog(state, trial_number);//update central info repository
                    break;
                case TEAMS_READY:
                    this.contestants_bench.startTrial();
                    this.repo.updtRopeCenter(0);//update rope center in central info repository
                    state = RefState.WAIT_FOR_TRIAL_CONCLUSION;
                    repo.refereeLog(state, trial_number);
                    break;
                case WAIT_FOR_TRIAL_CONCLUSION:
                    TrialStat unpack;
                    GameStat game_result=null;
                    unpack = this.playground.assertTrialDecision(n_players_pushing, knockDif);
                    has_next_trial = unpack.isHas_next_trial();
                    repo.updtRopeCenter(unpack.getCenter_rope());//update rope center in central info repository
                    switch (unpack.getWonType()){
                        case DRAW://if in trial was declared a draw increase both team scores
                            score_T1 +=1;
                            score_T2 +=1;
                            break;
                        case KNOCKOUT:

                            has_next_trial = false;//if one team is knocked out the game ends
                            if(unpack.getTeam()==1)
                            {
                                knock_out=1;//team 2 was knocked out
                            }
                            else
                            {
                                knock_out=2;//team 1 was knocked out
                            }
                            break;
                        case POINTS:// trial victory by points
                            if(unpack.getTeam()==1) {//if team 1 wins the trial
                                score_T1 += 1;
                            }else{//if team 2 wins the trial
                                score_T2 +=1;
                            }
                            break;
                    }

                    repo.refereeLog(state, trial_number);//update the referee state in central info repo
                    /*  if the trial decision says that there is a next trial, the referee has to call it  */
                    if (has_next_trial) {
                        this.repo.updtRopeCenter(Integer.MAX_VALUE);//MAX_VALUE hides/resets the rope center in the log
                        this.contestants_bench.callTrial();//new trial
                        /*  when new trial is called, increment trial number  */
                        trial_number += 1;//increase nr of trials
                        state = RefState.TEAMS_READY;//change state
                    }
                    /*  if not, the referee needs to declare a game winner  */
                    else{

                        game_result=this.referee_site.declareGameWinner(score_T1, score_T2, knock_out, n_games);
                        if(game_result.getWinnerTeam() == 1)
                        {
                            gamesWon_T1 +=1;//increase nr of games won by team 1
                        }
                        else if(game_result.getWinnerTeam()==2)
                        {
                            gamesWon_T2 +=1;//increase nr of games won by team 2
                        }
                        /*update the central info repository with the result of the game */

                        state = RefState.END_OF_A_GAME;

                    }
                    repo.refereeLog(state, trial_number);//update the referee state in central info repository
                    if(state == RefState.END_OF_A_GAME && game_result != null)
                        this.repo.setResult(game_result.getWinnerTeam(),game_result.getWonType(),trial_number);
                    break;
                case END_OF_A_GAME:

                    if(n_games > this.referee_site.getN_games_played()){//if less than 3 games played
                        this.referee_site.announceNewGame();//new game announced
                        repo.updGame_nr();//updte the nr of games in central info repo
                        state = RefState.START_OF_A_GAME;
                        repo.refereeLog(state, trial_number);//update the referee state in central info repo
                        repo.Addheader(false);//add header with the nr of games in central info repo
                        trial_number = 0;//reset nr of trials played
                        break;
                    }
                    state = RefState.END_OF_A_MATCH;
                    int match_winner = this.contestants_bench.declareMatchWinner(gamesWon_T1,gamesWon_T2);//declaring the match winner
                    repo.refereeLog(state, trial_number);//update the referee state in central info repo
                    repo.printMatchResult(match_winner,gamesWon_T1,gamesWon_T2);//update the centrla info repo with the winner of the match
                    break;
                case END_OF_A_MATCH:
                    MATCH_ENDED=true;
                    break;
                default:
                    state = RefState.START_OF_THE_MATCH;//default referee state
                    repo.refereeLog(state, trial_number);
                    break;

            }
        }


        System.out.println("Referee finished execution");

    }
}
