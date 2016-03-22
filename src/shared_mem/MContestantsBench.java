package shared_mem;

import active_entities.Coach;
import active_entities.Contestant;
import active_entities.Referee;
import interfaces.IContestantsBenchCoach;
import interfaces.IContestantsBenchContestant;
import interfaces.IContestantsBenchReferee;

public class MContestantsBench implements IContestantsBenchContestant, IContestantsBenchCoach, IContestantsBenchReferee {
    private int team_id;
    private int[] contestants_seated;
    private int[] contestants_played;

    private boolean trial_called = false;

    private int n_coaches_called_contestants = 0;
    private boolean contestants_called = false;

    private int advice_followed = 0;
    private boolean contestant_in_position = false;

    private boolean trial_started = false;

    private int n_coaches_informed_referee = 0;
    private boolean coaches_informed = false;

    private boolean trial_decided_coach = false;
    private boolean trial_decided_contestants = false;


    private int n_contestants_done = 0;
    private boolean contestants_are_done = false;

    private int n_trials_on_game = 0;

    private int n_contestants_done_awake = 0;

    private int n_coaches_reviewed_notes = 0;

    private int n_contestants_called = 0;

    private int n_coaches_contestants_in_position = 0;

    private int n_contestants_trial_started = 0;

    private int n_coaches_trial_decided = 0;

    // arrays of selected contestants to play the trial
    private static int team1_selected_contestants[] = {0, 1, 2};
    private static int team2_selected_contestants[] = {0, 1, 2};

    /**
     * Have contestants sleeping in the bench until they're waken up by both coaches
     */
    public synchronized void seatDown()
    {
        Contestant c = (Contestant) Thread.currentThread();
        //System.out.println("Contestant " + c.getContestantId() + " of team " + c.getTeam_id() + " is asleep on seatDown");

            while (!this.contestants_called){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        this.n_contestants_called += 1;
        if (this.n_contestants_called >= 10){
            this.contestants_called = false; // the last contestant to get here, resets the seatDown flag
            this.n_contestants_called = 0;
        }

    }



    /**
     * Referee sleeps until the last coach wakes him up on informReferee
     */
    public synchronized void callTrial()
    {
        Referee c = (Referee) Thread.currentThread();
        //System.out.println("Referee is asleep on callTrial");

        /*  wake up coaches in reviewNotes  */
        this.trial_called = true;
        notifyAll();

        /*  wait for coaches to inform referee  */
        while (!this.coaches_informed){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }



    /**
     * Call contestants and sleep until they're waken up by the last contestant in position
     */
    public synchronized void callContestants()
    {
        Coach c = (Coach) Thread.currentThread();
        //System.out.println("Coach " + c.getCoachId() + " called contestants");

        this.n_coaches_called_contestants += 1;

        if(this.n_coaches_called_contestants >= 2){
            this.n_coaches_called_contestants = 0;
            this.contestants_called = true;
        }

        /*  wake up the contestants in bench  */
        notifyAll();

        while(!this.contestant_in_position){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.n_coaches_contestants_in_position += 1;
        if (this.n_coaches_contestants_in_position >= 2) {
            this.n_coaches_contestants_in_position = 0;
            this.contestant_in_position = false;
        }

    }

    /**
     * Follow coach advice and sleep until referee wakes them up on start trial\
     * Returns true if player is playing and false if he's going to sit down
     */
    public synchronized boolean followCoachAdvice()
    {
        Contestant c = (Contestant) Thread.currentThread();
        //System.out.println("Contestant " + c.getContestantId() + " of team " + c.getTeam_id() + " is asleep on followCoachAdvice");

        /*  when all the players have followed the advice, wake up coach  */
        this.advice_followed += 1;
        if(this.advice_followed >= 10){
            this.contestant_in_position = true;
            this.advice_followed = 0;
            notifyAll();
        }

        while (!this.trial_started){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.n_contestants_trial_started += 1;
        if (this.n_contestants_trial_started >= 10) {
            this.n_contestants_trial_started = 0;
            this.trial_started = false;
        }


        if(c.getTeam_id() == 1) {
            if ((c.getContestantId() != team1_selected_contestants[0]) &&
                            (c.getContestantId() != team1_selected_contestants[1]) &&
                            (c.getContestantId() != team1_selected_contestants[2])){
                return false;
            }
            return true;

        }
        else if(c.getTeam_id() == 2) {
            if ((c.getContestantId() != team2_selected_contestants[0]) &&
                    (c.getContestantId() != team2_selected_contestants[1]) &&
                    (c.getContestantId() != team2_selected_contestants[2])){
                return false;
            }
            return true;
        }
        return false;
    }


    /**
     * Last coach wakes up referee and sleeps until the referee wakes him up on assertTrialDecision
     */
    public synchronized void informReferee() {

        Coach c = (Coach) Thread.currentThread();
        this.n_coaches_informed_referee += 1;

        /*  wake up referee when both coaches have informed them  */
        if(this.n_coaches_informed_referee >= 2){
            this.coaches_informed = true;
            notifyAll();
        }

        /*  wait for trial decision  */
        //System.out.println("Coach " + c.getCoachId() + " from Team " + c.getTeam_id() + " is asleep at informReferee");
        while (!this.trial_decided_coach){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.n_coaches_trial_decided += 1;
        if (this.n_coaches_trial_decided >= 2) {
            this.n_coaches_trial_decided = 0;
            this.trial_decided_coach = false;
        }
    }


    /**
     * Wakes up contestants in followCoachAdvice and sleeps until contestants are done pulling the rope
     */
    public synchronized void startTrial()
    {
        Referee r = (Referee) Thread.currentThread();
        //System.out.println("Referee is asleep on startTrial");

        this.trial_started = true;

        /*  wake up contestants in the bench  */
        notifyAll();

        /*  wait for contestants to get be done pulling the rope  */
        while (!this.contestants_are_done){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.contestants_are_done = false;
    }

    /**
     * Last contestant to be done wakes up referee and sleeps until referee wakes them up in assertTrialDecision
     */
    public synchronized void iAmDone()
    {
        Contestant c = (Contestant) Thread.currentThread();
        this.n_contestants_done += 1;

        /*  last contestant done wakes up referee  */
        if(this.n_contestants_done >= 6) {
            this.n_contestants_done = 0;
            this.contestants_are_done = true;
            notifyAll();
        }

        //System.out.println("Contestant " + c.getContestantId() + " of team " + c.getTeam_id() + " is asleep on iAmDone");
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
     * Wakes up contestants in iAmDone and states if there is going to be a next trial or not
     * @return has_next_trial
     */
    public synchronized boolean assertTrialDecision() {
        Referee r = (Referee) Thread.currentThread();

        //System.out.println("Referee is on assertTrialDecision");

        /*  increment the trials counter  */
        this.n_trials_on_game += 1;

        //System.out.printf("\n---------------- Trial #%d was played ----------------\n", this.n_trials_on_game);

        /*  flag to tell that there was a trial decision  */
        this.trial_decided_contestants = true;
        this.trial_decided_coach = true;

        /*  wake up contestants in iAmDone and coaches in informReferee  */
        notifyAll();

        /*  return has_next_trial  */
        if(this.n_trials_on_game >= 6){
            /*  set number of trials to 0 for next game  */
            this.n_trials_on_game = 0;
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * Coach reviews notes and makes changes in the teams and doesn't sleep
     */
    public synchronized void reviewNotes() {
        Coach c = (Coach) Thread.currentThread();

        //System.out.println("Coach " + c.getCoachId() + " from Team " + c.getTeam_id() + " is asleep at reviewNotes");
        while (!this.trial_called){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //System.out.println("Coach " + c.getCoachId() + " from Team " + c.getTeam_id() + " is woke up at reviewNotes");

        if(this.n_coaches_reviewed_notes >= 2){
            this.n_coaches_reviewed_notes = 0;
            this.trial_called = false;
        }


    }






}
