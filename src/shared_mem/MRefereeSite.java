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
     * Function that put the coaches at sleep until a new game is announced by the referee.
     */
    public synchronized void waitForNewGame() {

        Coach c = (Coach) Thread.currentThread();
        System.out.println("Coach " + c.getCoachId() + " from Team " + c.getTeam_id() + " is asleep at waitForNewGame");

        /*  wait until game is announced  */
        while (!this.new_game_announced) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Coach " + c.getCoachId() + " from Team " + c.getTeam_id() + " woke up");

    }

    /**
     * The referee announce a new game and all the threads are notified of that occurence.
     */
    public synchronized void announceNewGame() {

        Referee ref = (Referee) Thread.currentThread();

        this.new_game_announced = true;
        System.out.println("New game announced");

        /*  wake up coaches  */
        notifyAll();

        /*  wait to be informed by coaches  */
        System.out.println("Referee is sleeping at announceNewGame");
        while (this.n_coaches_informed_referee < 2){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This function purpose is to coach inform the referree that the contestants are ready to play and put the coach to sleep
     */
    public synchronized void informReferee() {

        Coach c = (Coach) Thread.currentThread();
        this.n_coaches_informed_referee += 1;

        /*  wake up referee  */
        notifyAll();

        /*  wait for trial decision  */
        System.out.println("Coach " + c.getCoachId() + " from Team " + c.getTeam_id() + " is asleep at informReferee");
        while (true){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public synchronized void assertTrialDecision() {
        //TODO-announce new game at referee
    }


    public synchronized void reviewNotes() {
        //TODO-review notes at coach, blocking mode
    }

    public synchronized void declareGameWinner() {
        //TODO-announce game  winner at referee
    }

    public synchronized void declareMatchWinner() {
        //TODO-announce match winner at referee
    }

}
