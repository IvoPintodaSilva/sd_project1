package pt.ua.sd.RopeGame.shared_mem;


import pt.ua.sd.RopeGame.active_entities.Coach;
import pt.ua.sd.RopeGame.active_entities.Contestant;
import pt.ua.sd.RopeGame.active_entities.Referee;
import pt.ua.sd.RopeGame.enums.WonType;
import pt.ua.sd.RopeGame.interfaces.IPlaygroundCoach;
import pt.ua.sd.RopeGame.interfaces.IPlaygroundContestant;
import pt.ua.sd.RopeGame.interfaces.IPlaygroundReferee;
import pt.ua.sd.RopeGame.structures.TrialStat;

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
    private int n_trials_on_game = 0;

    private int n_contestants_done_awake = 0;

    private int n_coaches_reviewed_notes = 0;
    private boolean trial_decided_coach = false;
    private boolean trial_decided_contestants = false;
    private boolean contestants_are_done = false;
    private int n_contestants_done = 0;





    /**
     * Have contestants sleeping in the bench until they're waken up by both coaches
     */
    public synchronized void seatDown()
    {
        Contestant c = (Contestant) Thread.currentThread();
        //System.out.println("Contestant " + c.getContestantId() + " of team " + c.getTeam_id() + " is asleep on seatDown");

        while (!this.trial_decided_contestants){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.n_contestants_done_awake += 1;
        if(this.n_contestants_done_awake >= 6){
            /*  reset conditions for next trial  */
            this.n_contestants_done_awake = 0;
            this.trial_decided_contestants = false;
        }




    }


    /**
     * Contestants pull the rope
     */
    public synchronized void pullTheRope() {
        Contestant c = (Contestant) Thread.currentThread();
        this.ready_to_push += 1;

        /*  sleep only if the 6 players have not yet arrived and the push flag is not true  */
        /*  the flag is only set to false by the last player to finish pushing  */
        if (this.ready_to_push >= 6 && !this.push_at_all_force){
            this.ready_to_push = 0;
            this.push_at_all_force = true;
            center_rope=0;//reset center of rope
            notifyAll();
        }

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
            /*  reset push number  */
            this.n_contestant_pulls_team1[c.getContestantId()] = 0;
            /*  the last player to finish pushing in the trial, resets the push_at_all_force flag  */
            this.finished_pushing += 1;
            if(this.finished_pushing >= 6){
                this.finished_pushing = 0;
                this.push_at_all_force = false;
            }
            return;
        }
        else if (c.getTeam_id() == 2){
            center_rope += c.getStrength();//positive value for push to the right
            try {
                Thread.sleep((long)(Math.random() * 100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Contestant " + c.getContestantId() + " of team " + c.getTeam_id() + " pulled the rope. Center: " + center_rope);
            /*  reset push number  */
            this.n_contestant_pulls_team2[c.getContestantId()] = 0;
            /*  the last player to finish pushing in the trial, resets the push_at_all_force flag  */
            this.finished_pushing += 1;
            if(this.finished_pushing >= 6){
                this.finished_pushing = 0;
                this.push_at_all_force = false;
            }

            return;
        }
        return;
    }


    /**
     * Wakes up contestants in iAmDone and states if there is going to be a next trial or not
     * @return has_next_trial
     */
    public synchronized TrialStat assertTrialDecision() {
        Referee r = (Referee) Thread.currentThread();

        boolean decision=false;
        WonType decision_type = WonType.NONE;
        int winner = -1;

        //System.out.println("Referee is on assertTrialDecision");

        /*  wait for contestants to get be done pulling the rope  */
        while (!this.contestants_are_done){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //System.out.println("Referee is awake on startTrial");


        this.contestants_are_done = false;


        /*  increment the trials counter  */
        this.n_trials_on_game += 1;

        //System.out.printf("\n---------------- Trial #%d was played ----------------\n", this.n_trials_on_game);

        if(center_rope == 0)
        {
            decision_type = WonType.DRAW;//its a draw
            winner = 0;//none winner

        }
        else if(center_rope> 4 || center_rope<-4)
        {
            decision_type = WonType.KNOCKOUT;
        }
        else {
            decision_type = WonType.POINTS;
        }

        if(center_rope>0)
        winner=2;
        else if(center_rope<0)
        winner=1;

        /*  flag to tell that there was a trial decision  */
        this.trial_decided_contestants = true;
        this.trial_decided_coach = true;

        /*  wake up contestants in iAmDone and coaches in informReferee  */
        notifyAll();

        /*  return has_next_trial  */
        if(this.n_trials_on_game >= 6){
            /*  set number of trials to 0 for next game  */
            this.n_trials_on_game = 0;
            decision = false;
        }
        else{
            decision=true;
        }

        return new TrialStat(decision,winner, decision_type, center_rope);
    }

    /**
     * Coach reviews notes and makes changes in the teams and doesn't sleep
     */
    public synchronized int[] reviewNotes(int[] selected_contestants) {
        Coach c = (Coach) Thread.currentThread();

        //System.out.println("Coach " + c.getCoachId() + " from Team " + c.getTeam_id() + " is asleep at reviewNotes");
        while (!this.trial_decided_coach){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /*  substitutions implemented  */
        /*  only one player is substituted from each team at each trial  */
            if(selected_contestants[0] == 0){
                selected_contestants[0] = 4;
            }
            else{
                selected_contestants[0] -= 1;
            }
            if(selected_contestants[1] == 0){
                selected_contestants[1] = 4;
            }
            else{
                selected_contestants[1] -= 1;
            }
            if(selected_contestants[2] == 0){
                selected_contestants[2] = 4;
            }
            else{
                selected_contestants[2] -= 1;
            }
            //System.out.println(Arrays.toString(team1_selected_contestants));



        //System.out.println("Coach " + c.getCoachId() + " from Team " + c.getTeam_id() + " is woke up at reviewNotes");

        if(this.n_coaches_reviewed_notes >= 2){
            this.n_coaches_reviewed_notes = 0;
            this.trial_decided_coach = false;
        }

        return selected_contestants;
    }


    /**
     * Last contestant to be done wakes up referee and sleeps until referee wakes them up in assertTrialDecision
     */
    public synchronized void iAmDone()
    {
        Contestant c = (Contestant) Thread.currentThread();
        c.decrementStrength();
        this.n_contestants_done += 1;
        //System.out.println("Contestant " + c.getContestantId() + " of team " + c.getTeam_id() + " is asleep on iAmDone");

        /*  last contestant done wakes up referee  */
        if(this.n_contestants_done >= 6) {
            this.n_contestants_done = 0;
            this.contestants_are_done = true;
            //System.out.println("contestants are done");
            notifyAll();
        }



    }


}

