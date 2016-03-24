package shared_mem;

import active_entities.Coach;
import active_entities.Referee;
import interfaces.IRefereeSiteCoach;
import interfaces.IRefereeSiteContestant;
import interfaces.IRefereeSiteReferee;

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
    private static int team0_wins=0;
    private static int team1_wins=0;

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
     */
    public synchronized void declareGameWinner() {
        //TODO-announce game  winner at referee
        n_games_played +=1;//increase number of games played
    }

    /**
     * Announce new game
     */
    public synchronized void declareMatchWinner() {
        //TODO-announce match winner at referee
    }

}
