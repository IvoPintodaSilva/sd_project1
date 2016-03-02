package shared_mem;

import active_entities.Coach;
import active_entities.Referee;

public class RefereeSite {
    private int n_trials;
    private int n_trials_played;
    private int n_games;
    private int n_games_played;
    private int team0_wins;
    private int team1_wins;

    private boolean new_game_announced = false;


    public synchronized void waitForNewGame() {
        //TODO-wait new game at referee, blocking mode
        Coach c = (Coach) Thread.currentThread();
        System.out.println("Coach " + c.getCoachId() + " from Team " + c.getTeam_id() + " WAIT at waitForNewGame");
        while (!this.new_game_announced) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Coach " + c.getCoachId() + " from Team " + c.getTeam_id() + " woke up");

    }

    public synchronized void announceNewGame() {
        //TODO-announce new game at referee, blocking mode
        Referee ref = (Referee) Thread.currentThread();

        ref.START_OF_THE_MATCH = false;
        ref.END_OF_A_GAME = false;
        ref.START_OF_A_GAME = true;

        this.new_game_announced = true;
        System.out.println("New game announced");

        notifyAll();

        System.out.println("Referee is sleeping at announceNewGame");
        while (true){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public synchronized void informReferee() {
        //TODO-inform referee at coach, blocking mode
        Coach c = (Coach) Thread.currentThread();

        System.out.println("Coach " + c.getCoachId() + " from Team " + c.getTeam_id() + " WAIT at informReferee");
        while (true){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void callTrial() {
        //TODO-announce trial at referee, blocking mode
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
