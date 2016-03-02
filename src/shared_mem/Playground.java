package shared_mem;


import active_entities.Contestant;
import active_entities.Referee;

public class Playground {

    private boolean ref_teams_ready = false;


    /**
     * This function purpose is to put the contestants at sleep in playground until the referee call for the trial
     */
    public synchronized void followCoachAdvice()
    {
        //TODO-exit condition
        Contestant c = (Contestant) Thread.currentThread();
        System.out.println("Contestant " + c.getContestantId() + " of team " + c.getTeam_id() + " is asleep on followCoachAdvice");

        while (!this.ref_teams_ready){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    public synchronized void callTrial()
    {
        //TODO-
        Referee c = (Referee) Thread.currentThread();
        System.out.println("Referee is asleep on callTrial");

        this.ref_teams_ready = true;

        /*  wake up contestants in playground  */
        notifyAll();

        /*  wait for contestants to get ready  */
        while (true){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public synchronized void getReady()
    {
        //TODO-
        Contestant c = (Contestant) Thread.currentThread();
        System.out.println("Contestant " + c.getContestantId() + " of team " + c.getTeam_id() + " is asleep on getReady");

        while (true){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void startTrial()
    {
        //TODO-
    }

    public synchronized void pullTheRope()
    {
        //TODO-
    }

    public synchronized void iAmDone()
    {
        //TODO-
    }

}
