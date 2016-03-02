package shared_mem;


import active_entities.Contestant;

public class Playground {


    public synchronized void followCoachAdvice()
    {
        //TODO-verify if coach already called contestants
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
