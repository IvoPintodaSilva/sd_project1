package shared_mem;


import active_entities.Contestant;
import interfaces.IPlaygroundCoach;
import interfaces.IPlaygroundContestant;
import interfaces.IPlaygroundReferee;

import static java.lang.Thread.sleep;

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
    private static int center_rope= 0;


    /**
     * Contestants pull the rope
     */
    public synchronized int[] pullTheRope() {
        Contestant c = (Contestant) Thread.currentThread();
        int[] ret = new int[2];
        ret[0]=0;//false
        ret[1]=center_rope;// value of the deslocation of the rope

        this.ready_to_push += 1;

        /*  sleep only if the 6 players have not yet arrived and the push flag is not true  */
        /*  the flag is only set to false by the last player to finish pushing  */
        if (this.ready_to_push >= 6 && !this.push_at_all_force){
            this.ready_to_push = 0;
            this.push_at_all_force = true;
            center_rope=0;//reset center of rope
            notifyAll();
        }

        System.out.println("OIOIIOIOIOIOIOIOI");

        while (!this.push_at_all_force){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //System.out.println("+++++++++++Contestant " + c.getContestantId() + " of team " + c.getTeam_id() + " is pulling the rope with strenght " + c.getStrength()+"++++++++++");
        if(c.getTeam_id() == 1){
            //System.out.println("->->->antes:> "+center_rope + "n: " + c.getContestantId());
            center_rope -= c.getStrength();//subtract value for push to the left
            try {
                Thread.sleep((long)(Math.random() * 100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Contestant " + c.getContestantId() + " of team " + c.getTeam_id() + " pulled the rope. Center: " + center_rope);
            //System.out.println("-<-<-<-<depois:> "+ center_rope+ " n: " + c.getContestantId());
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
                ret[0]=0;//false
                ret[1]=center_rope;
                return ret;
            }
            ret[0]=1;//true
            ret[1]=center_rope;
            return ret;
        }
        else if (c.getTeam_id() == 2){
            center_rope += c.getStrength();//positive value for push to the right
            try {
                Thread.sleep((long)(Math.random() * 100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Contestant " + c.getContestantId() + " of team " + c.getTeam_id() + " pulled the rope. Center: " + center_rope);
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
                ret[0]=0;//false
                ret[1]=center_rope;
                return ret;
            }
            ret[0]=1;//true
            ret[1]=center_rope;
            return ret;
        }
        ret[0]=0;//false
        ret[1]=center_rope;
        return ret;
    }



}
