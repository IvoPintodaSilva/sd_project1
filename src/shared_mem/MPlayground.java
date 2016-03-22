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

    private int n_contestant_pulls_team1[] = {0,0,0,0,0};
    private int n_contestant_pulls_team2[] = {0,0,0,0,0};


    /**
     * Contestants sleep in the playground and the last one to get there wakes them up so that they pull at the same
     * time
     */
    public synchronized void getReady()
    {
        Contestant c = (Contestant) Thread.currentThread();

        this.n_contestants_ready += 1;
        if(this.n_contestants_ready >= 6){
            this.n_contestants_ready = 0;
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

        System.out.println(this.n_ready_contestants_awake);

        if(this.n_ready_contestants_awake >= 6){
            /*  restore contestants value for next trial  */
            this.n_ready_contestants_awake = 0;
            this.all_contestants_ready = false;
        }

    }


    /**
     * Contestants pull the rope
     */
    public synchronized boolean pullTheRope()
    {
        Contestant c = (Contestant) Thread.currentThread();

        c.decrementStrength();

        //System.out.println("Contestant " + c.getContestantId() + " of team " + c.getTeam_id() + " is pulling the rope");
        if(c.getTeam_id() == 1){
            this.n_contestant_pulls_team1[c.getContestantId()] += 1;
            if (this.n_contestant_pulls_team1[c.getContestantId()] >= 6){
                /*  reset push number  */
                this.n_contestant_pulls_team1[c.getContestantId()] = 0;
                return false;
            }
            return true;
        }
        else if (c.getTeam_id() == 2){
            this.n_contestant_pulls_team2[c.getContestantId()] += 1;
            if (this.n_contestant_pulls_team2[c.getContestantId()] >= 6){
                /*  reset push number  */
                this.n_contestant_pulls_team2[c.getContestantId()] = 0;
                return false;
            }
            return true;
        }
        return false;
    }



}
