package shared_mem;

import active_entities.Coach;
import active_entities.Contestant;
import interfaces.IContestantsBenchCoach;
import interfaces.IContestantsBenchContestant;
import interfaces.IContestantsBenchReferee;

public class MContestantsBench implements IContestantsBenchContestant, IContestantsBenchCoach, IContestantsBenchReferee {
    private int team_id;
    private int[] contestants_seated;
    private int[] contestants_played;

    private boolean contestants_called = false;
    private int advice_followed = 0;
    private boolean contestant_in_position = false;


    public synchronized void seatDown()
    {
        Contestant c = (Contestant) Thread.currentThread();
        System.out.println("Contestant " + c.getContestantId() + " of team " + c.getTeam_id() + " is asleep");

        while (!this.contestants_called){
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

        this.contestants_called = true;

        /*  wake up the contestants in bench  */
        notifyAll();

        while(this.contestant_in_position){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * This function purpose is to put the contestants at sleep in playground until the referee call for the trial
     */
    public synchronized void followCoachAdvice()
    {
        Contestant c = (Contestant) Thread.currentThread();
        System.out.println("Contestant " + c.getContestantId() + " of team " + c.getTeam_id() + " is asleep on followCoachAdvice");

        /*  when all the players have followed the advice, wake up coach  */
        this.advice_followed += 1;
        if(this.advice_followed >= 10){
            this.contestant_in_position = true;
            notifyAll();
        }

        /*while (!this.ref_teams_ready){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/

    }


}
