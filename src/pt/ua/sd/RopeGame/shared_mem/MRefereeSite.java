package pt.ua.sd.RopeGame.shared_mem;

import pt.ua.sd.RopeGame.active_entities.Referee;
import pt.ua.sd.RopeGame.enums.WonType;
import pt.ua.sd.RopeGame.interfaces.IRefereeSiteCoach;
import pt.ua.sd.RopeGame.interfaces.IRefereeSiteContestant;
import pt.ua.sd.RopeGame.interfaces.IRefereeSiteReferee;
import pt.ua.sd.RopeGame.structures.GameStat;

/**
 * Refereee Site shared memory<br>
 *<b><center><font size=6>Refereee Site shared memory</font></center></b><br>
 *     <font size=4>This class represents the monitor/shared memory of the referee site.</font>
 *
 *
 */
public class MRefereeSite implements IRefereeSiteCoach, IRefereeSiteReferee, IRefereeSiteContestant{

    /**
     *
     * @return the number of games
     */
    public int getN_games() {
        return n_games;
    }

    /**
     *
     * @return  the number of games played
     */
    public int getN_games_played() {
        return n_games_played;
    }

    /**
     * Internal Data
     */
    private static int n_games=3;
    private static int n_games_played=0;


    /**
     * The referee announce a new game
     */
    public synchronized void announceNewGame() {


    }

    /**
     * The number of played games is increased and the game winner is decided
     * @return GameStat data with the info of the winner
     */
    public synchronized GameStat declareGameWinner(int score_T1, int score_T2, int knock_out) {
        n_games_played +=1;//increase number of games played

        if(knock_out== 1 )
        {
            return new GameStat((n_games_played<n_games),knock_out, WonType.KNOCKOUT);
        }
        else if(knock_out == 2){

            return new GameStat((n_games_played<n_games),knock_out, WonType.KNOCKOUT);
        }
        else if(score_T1>score_T2)
        {

            return new GameStat((n_games_played<n_games),1,WonType.POINTS);
        }
        else if(score_T1<score_T2){

            return new GameStat((n_games_played<n_games),2,WonType.POINTS);
        }

        return new GameStat((n_games_played<n_games),0,WonType.DRAW);




    }

}
