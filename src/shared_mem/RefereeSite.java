package shared_mem;

public class RefereeSite {
    private int n_trials;
    private int n_trials_played;
    private int n_games;
    private int n_games_played;
    private int team0_wins;
    private int team1_wins;


    public synchronized void waitForNewGame() {
        //TODO-wait new game at referee, blocking mode
        while (true) {
            try {
                System.out.println("RefereeSite.waitForNewGame-wait");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("RefereeSite.waitForNewGame-wait ERR");
            }

        }
    }

    public synchronized void announceNewGame() {
        //TODO-announce new game at referee, blocking mode
        //newGame=true;
        //notifyAll();
    }


    public synchronized void informReferee() {
        //TODO-inform referee at coach, blocking mode
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
