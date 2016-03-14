package shared_mem;

import active_entities.Coach;
import active_entities.Referee;
import interfaces.IRefereeSiteCoach;
import interfaces.IRefereeSiteContestant;
import interfaces.IRefereeSiteReferee;

public class MRefereeSite implements IRefereeSiteCoach, IRefereeSiteReferee, IRefereeSiteContestant{
    private int n_trials;
    private int n_trials_played;
    private int n_games;
    private int n_games_played;
    private int team0_wins;
    private int team1_wins;

    private boolean new_game_announced = false;
    private int n_coaches_informed_referee = 0;


    /**
     * The referee announce a new game and all the threads are notified of that occurence.
     */
    public synchronized void announceNewGame() {

        Referee ref = (Referee) Thread.currentThread();

        this.new_game_announced = true;
        System.out.println("New game announced");

    }

    public synchronized void declareGameWinner() {
        //TODO-announce game  winner at referee
    }

    public synchronized void declareMatchWinner() {
        //TODO-announce match winner at referee
    }

}
