package pt.ua.sd.RopeGame.shared_mem;

import pt.ua.sd.RopeGame.active_entities.Referee;
import pt.ua.sd.RopeGame.enums.WonType;
import pt.ua.sd.RopeGame.interfaces.IRefereeSiteCoach;
import pt.ua.sd.RopeGame.interfaces.IRefereeSiteContestant;
import pt.ua.sd.RopeGame.interfaces.IRefereeSiteReferee;
import pt.ua.sd.RopeGame.structures.GameStat;

public class MRefereeSite implements IRefereeSiteCoach, IRefereeSiteReferee, IRefereeSiteContestant{
    private int n_trials;
    private int n_trials_played;

    public int getN_games() {
        return n_games;
    }

    public int getN_games_played() {
        return n_games_played;
    }

    private static int n_games=3;
    private static int n_games_played=0;
    private static int team1_wins=0;
    private static int team2_wins=0;

    private boolean new_game_announced = false;
    private int n_coaches_informed_referee = 0;


    /**
     * The referee announce a new game and all the threads are notified of that occurence.
     */
    public synchronized void announceNewGame() {

        Referee ref = (Referee) Thread.currentThread();

        this.new_game_announced = true;

        //System.out.println("New game announced");

    }

    /**
     * The number of played games is increased
     * @return GameStat
     */
    public synchronized GameStat declareGameWinner(int score_T1, int score_T2, int knock_out) {
        //TODO-announce game  winner at referee
        n_games_played +=1;//increase number of games played

        if(knock_out== 1 )
        {
            team1_wins +=1;
            return new GameStat((n_games_played<n_games),knock_out, WonType.KNOCKOUT);
        }
        else if(knock_out == 2){
            team2_wins +=1;
            return new GameStat((n_games_played<n_games),knock_out, WonType.KNOCKOUT);
        }
        else if(score_T1>score_T2)
        {
            team1_wins +=1;
            return new GameStat((n_games_played<n_games),1,WonType.POINTS);
        }
        else if(score_T1<score_T2){
            team2_wins +=1;
            return new GameStat((n_games_played<n_games),2,WonType.POINTS);
        }

            return new GameStat((n_games_played<n_games),0,WonType.DRAW);




    }

}
