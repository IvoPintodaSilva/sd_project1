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
     * This function purpose is to put the contestants at sleep in the playground
     */
    public synchronized void getReady()
    {
        Contestant c = (Contestant) Thread.currentThread();

        n_contestants_ready += 1;
        if(this.n_contestants_ready >= 10){
            this.all_contestants_ready = true;
            notifyAll();
        }

        System.out.println("Contestant " + c.getContestantId() + " of team " + c.getTeam_id() + " is asleep on getReady");
        /*  wait for every contestant to be ready  */
        /*  the last contestant to get ready wakes up everyone else  */
        while (!this.all_contestants_ready){
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
        System.out.println("Contestant " + c.getContestantId() + " of team " + c.getTeam_id() + " is pulling the rope");

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



}
