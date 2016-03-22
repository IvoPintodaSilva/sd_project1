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
