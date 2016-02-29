package shared_mem;

import active_entities.Contestant;

public class ContestantsBench {
    private int team_id;
    private int[] contestants_seated;
    private int[] contestants_played;



    public synchronized void seatDown()
    {
        Contestant c = (Contestant) Thread.currentThread();
        System.out.println("Contestant " + c.getContestantId() + "of team " + c.getTeam_id() + "is asleep");

        while (true){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void callContestants()
    {
        //TODO-call playing contestants at coach
    }


}
