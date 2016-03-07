package shared_mem;


import active_entities.Contestant;
import active_entities.Referee;
import interfaces.IPlaygroundCoach;
import interfaces.IPlaygroundContestant;
import interfaces.IPlaygroundReferee;

public class MPlayground implements IPlaygroundContestant, IPlaygroundReferee, IPlaygroundCoach {

    private int n_contestants_ready = 0;
    private boolean all_contestants_ready = false;
    private boolean trial_started = false;




    /**
     * This function purpose is to wake up the contestants and put the referee to sleep
     */
    public synchronized void callTrial()
    {
        Referee c = (Referee) Thread.currentThread();
        System.out.println("Referee is asleep on callTrial");

        /*  wait for contestants to get ready  */
        while (!this.all_contestants_ready){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * This function purpose is to put the contestants at sleep in the playground
     */
    public synchronized void getReady()
    {
        Contestant c = (Contestant) Thread.currentThread();
        System.out.println("Contestant " + c.getContestantId() + " of team " + c.getTeam_id() + " is asleep on getReady");

        n_contestants_ready += 1;
        if(this.n_contestants_ready >= 10){
            this.all_contestants_ready = true;

            /*  wake up referee  */
            notifyAll();
        }

        /*  wait for referee to start trial  */
        while (!this.trial_started){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void startTrial()
    {
        Referee c = (Referee) Thread.currentThread();
        System.out.println("Referee is asleep on startTrial");

        this.trial_started = true;

        /*  wake up contestants in playground  */
        notifyAll();

        /*  wait for contestants to get be done pulling the rope  */
        while (true){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void pullTheRope()
    {
        Contestant c = (Contestant) Thread.currentThread();
        System.out.println("Contestant " + c.getContestantId() + " of team " + c.getTeam_id() + " is asleep on pullTheRope");

        /*  wake up referee  */
        //notifyAll();

//        while (true){
//            try {
//                wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public synchronized void iAmDone()
    {
        Contestant c = (Contestant) Thread.currentThread();
        System.out.println("Contestant " + c.getContestantId() + " of team " + c.getTeam_id() + " is asleep on iAmDone");

        /*  wake up referee  */
        //notifyAll();

        while (true){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
