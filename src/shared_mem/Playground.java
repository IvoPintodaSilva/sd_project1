package shared_mem;


import active_entities.Contestant;

public class Playground {


    /**
     * This function purpose is to put the contestants at sleep in playground until the referee call for the trial
     */
    public synchronized void followCoachAdvice()
    {
        //TODO-exit condition
        Contestant c = (Contestant) Thread.currentThread();


        System.out.println("Contestant " + c.getContestantId() + " of team " + c.getTeam_id() + " is asleep on playground");

        while (true){
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
    }

    public synchronized void getReady()
    {
        //TODO-
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
