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
    private int ready_to_push;
    private boolean push_at_all_force = false;
    private int finished_pushing;


    /**
     * Contestants pull the rope
     */
    public synchronized boolean pullTheRope() {
        Contestant c = (Contestant) Thread.currentThread();

        this.ready_to_push += 1;

        /*  sleep only if the 6 players have not yet arrived and the push flag is not true  */
        /*  the flag is only set to false by the last player to finish pushing  */
        if (this.ready_to_push >= 6 && !this.push_at_all_force){
            this.ready_to_push = 0;
            this.push_at_all_force = true;
            notifyAll();
        }
        while (!this.push_at_all_force){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //System.out.println("Contestant " + c.getContestantId() + " of team " + c.getTeam_id() + " is pulling the rope");
        if(c.getTeam_id() == 1){
            this.n_contestant_pulls_team1[c.getContestantId()] += 1;
            if (this.n_contestant_pulls_team1[c.getContestantId()] >= 6){
                /*  reset push number  */
                this.n_contestant_pulls_team1[c.getContestantId()] = 0;
                /*  the last player to finish pushing in the trial, resets the push_at_all_force flag  */
                this.finished_pushing += 1;
                if(this.finished_pushing >= 6){
                    this.finished_pushing = 0;
                    this.push_at_all_force = false;
                }
                return false;
            }
            return true;
        }
        else if (c.getTeam_id() == 2){
            this.n_contestant_pulls_team2[c.getContestantId()] += 1;
            if (this.n_contestant_pulls_team2[c.getContestantId()] >= 6){
                /*  reset push number  */
                this.n_contestant_pulls_team2[c.getContestantId()] = 0;
                /*  the last player to finish pushing in the trial, resets the push_at_all_force flag  */
                this.finished_pushing += 1;
                if(this.finished_pushing >= 6){
                    this.finished_pushing = 0;
                    this.push_at_all_force = false;
                }
                return false;
            }
            return true;
        }
        return false;
    }



}
