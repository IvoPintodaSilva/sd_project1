package shared_mem;


import active_entities.Contestant;
import interfaces.IPlaygroundCoach;
import interfaces.IPlaygroundContestant;
import interfaces.IPlaygroundReferee;

public class MPlayground implements IPlaygroundContestant, IPlaygroundReferee, IPlaygroundCoach {

    private int n_contestants_ready = 0;
    private boolean all_contestants_ready = false;
    private boolean trial_started = false;

    private int n_ready_contestants_awake = 0;


    /**
     * Contestants sleep in the playground and the last one to get there wakes them up so that they pull at the same
     * time
     */
    public synchronized void getReady()
    {
        Contestant c = (Contestant) Thread.currentThread();

        n_contestants_ready += 1;
        if(this.n_contestants_ready >= 10){
            this.all_contestants_ready = true;
            notifyAll();
        }

        //System.out.println("Contestant " + c.getContestantId() + " of team " + c.getTeam_id() + " is asleep on getReady");
        /*  wait for every contestant to be ready  */
        /*  the last contestant to get ready wakes up everyone else  */
        while (!this.all_contestants_ready){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.n_ready_contestants_awake += 1;

        if(this.n_ready_contestants_awake >= 10){
            /*  restore contestants value for next trial  */
            this.n_contestants_ready = 0;
            this.n_ready_contestants_awake = 0;
            this.all_contestants_ready = false;
        }

    }


    /**
     * Contestants pull the rope
     */
    public synchronized void pullTheRope()
    {


        Contestant c = (Contestant) Thread.currentThread();
        //System.out.println("Contestant " + c.getContestantId() + " of team " + c.getTeam_id() + " is pulling the rope");

    }



}
