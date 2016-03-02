package shared_mem;

import active_entities.Coach;
import active_entities.Contestant;

public class ContestantsBench {
    private int team_id;
    private int[] contestants_seated;
    private int[] contestants_played;

    private boolean team_assembled = false;



    public synchronized void seatDown()
    {
        Contestant c = (Contestant) Thread.currentThread();
        System.out.println("Contestant " + c.getContestantId() + " of team " + c.getTeam_id() + " is asleep");

        while (!this.team_assembled){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void callContestants()
    {
        Coach c = (Coach) Thread.currentThread();
        System.out.println("Coach " + c.getCoachId() + " called contestants");

        this.team_assembled = true;

        /*  wake up the contestants  */
        notifyAll();

    }


}
