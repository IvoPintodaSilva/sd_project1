package shared_mem;

import active_entities.Coach;
import active_entities.Contestant;
import active_entities.Referee;
import interfaces.IContestantsBenchCoach;
import interfaces.IContestantsBenchContestant;
import interfaces.IContestantsBenchReferee;

import java.util.Arrays;

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







    private int n_contestants_called = 0;

    //private int n_coaches_contestants_in_position = 0;

    private static int n_contestants_trial_started = 0;

    private int n_coaches_trial_decided = 0;

    private boolean new_team1_selected[] = {true, true, true, true, true};
    private boolean new_team2_selected[] = {true, true, true, true, true};

    // arrays of selected contestants to play the trial
    private static int team1_selected_contestants[] = {0, 1, 2};
    private static int team2_selected_contestants[] = {0, 1, 2};
    private int n_ready_contestants_awake;
    private int n_ready_contestants_started;
    private boolean followed_coach_advice;
    private int[] team1_strength = {0, 0, 0, 0, 0};
    private int[] team2_strength = {0, 0, 0, 0, 0};
    private int i = 0;
    private boolean match_ended = false;


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

    }



    /**
     * Call contestants and sleep until they're waken up by the last contestant in position
     */
    public synchronized boolean callContestants()
    {
        Coach c = (Coach) Thread.currentThread();
        //System.out.println("Coach " + c.getCoachId() + " called contestants");

        /*  to know when to increment contestants strength  */
        for (int i = 0; i < 5; i++){
            if(c.getTeam_id() == 1){
                this.new_team1_selected[i] = true;
            }else if(c.getTeam_id() == 2){
                this.new_team2_selected[i] = true;
            }
        }

        while(!this.trial_called || this.match_ended){

            if(this.match_ended){
                return false;
            }

            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.n_coaches_called_contestants += 1;

        if(this.n_coaches_called_contestants >= 2){
            /*  wake up the contestants in bench  */
            notifyAll();
            this.n_coaches_called_contestants = 0;
            this.contestants_called = true;
            this.trial_called = false;
        }

        if(c.getTeam_id() == 1){
            team1_selected_contestants = c.getSelectedContestants();
        }else if(c.getTeam_id() == 2){
            team2_selected_contestants = c.getSelectedContestants();
        }

        return true;

    }

    /**
     * Follow coach advice and sleep until referee wakes them up on start trial\
     * Returns true if player is playing and false if he's going to sit down
     */
    public synchronized boolean followCoachAdvice()
    {
        Contestant c = (Contestant) Thread.currentThread();


        if(c.getTeam_id() == 1){
            this.team1_strength[c.getContestantId()] = c.getStrength();
        }
        else if(c.getTeam_id() == 2){
            this.team2_strength[c.getContestantId()] = c.getStrength();
        }

        if(c.getTeam_id() == 1){

            while (
                    ((!this.contestants_called) ||
                    ((c.getContestantId() != team1_selected_contestants[0]) &&
                            (c.getContestantId() != team1_selected_contestants[1]) &&
                            (c.getContestantId() != team1_selected_contestants[2])))

                    ||

                            this.match_ended

                    ){


                if(this.match_ended){
                    return false;
                }

                if( new_team1_selected[c.getContestantId()] ){
                    new_team1_selected[c.getContestantId()] = false;

                    if(((c.getContestantId() != team1_selected_contestants[0]) &&
                            (c.getContestantId() != team1_selected_contestants[1]) &&
                            (c.getContestantId() != team1_selected_contestants[2]))){
                        //System.out.println("Contestant " + c.getContestantId() + " of team " + c.getTeam_id() + " " + this.team1_strength[c.getContestantId()] + " - > " + c.getStrength());
                        c.incrementStrength();
                    }

                }


                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }else if(c.getTeam_id() == 2){
            while (
                    ((!this.contestants_called) ||
                    ((c.getContestantId() != team2_selected_contestants[0]) &&
                            (c.getContestantId() != team2_selected_contestants[1]) &&
                            (c.getContestantId() != team2_selected_contestants[2])))
                    ||
                            this.match_ended
                    ){


                if(this.match_ended){
                    return false;
                }

                if( new_team2_selected[c.getContestantId()] ){
                    new_team2_selected[c.getContestantId()] = false;

                    if(((c.getContestantId() != team2_selected_contestants[0]) &&
                            (c.getContestantId() != team2_selected_contestants[1]) &&
                            (c.getContestantId() != team2_selected_contestants[2]))){
                        //System.out.println("Contestant " + c.getContestantId() + " of team " + c.getTeam_id() + " " + this.team2_strength[c.getContestantId()] + " - > " + c.getStrength());
                        c.incrementStrength();
                    }

                }

                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }



        if(c.getTeam_id() == 1){
            if (this.new_team1_selected[c.getContestantId()]) {
                this.new_team1_selected[c.getContestantId()] = false;
            }
        }
        else if(c.getTeam_id() == 2){
            if (this.new_team2_selected[c.getContestantId()]) {
                this.new_team2_selected[c.getContestantId()] = false;
            }
        }

        this.n_contestants_called += 1;
        if (this.n_contestants_called >= 6){
            this.contestants_called = false; // the last contestant to get here, resets the seatDown flag
            this.n_contestants_called = 0;
        }


        /*  when all the players have followed the advice, wake up coach  */
        this.advice_followed += 1;
        if(this.advice_followed >= 6){
            this.followed_coach_advice = true;
            this.advice_followed = 0;
            notifyAll();
        }

        return true;
    }


    /**
     * Last coach wakes up referee and sleeps until the referee wakes him up on assertTrialDecision
     */
    public synchronized void informReferee() {

        Coach c = (Coach) Thread.currentThread();
        this.n_coaches_informed_referee += 1;

        //System.out.println("Coach " + c.getCoachId() + " from Team " + c.getTeam_id() + " is asleep at informReferee");

        /*  wake up referee when both coaches have informed them  */
        if(this.n_coaches_informed_referee >= 2){
            this.n_coaches_informed_referee = 0;
            this.coaches_informed = true;
            notifyAll();
        }

        /*  wait for trial decision  */
        //System.out.println("Coach " + c.getCoachId() + " from Team " + c.getTeam_id() + " is asleep at informReferee");
        //System.out.println(this.followed_coach_advice);
        while (!this.followed_coach_advice){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.n_coaches_trial_decided += 1;
        if (this.n_coaches_trial_decided >= 2) {
            this.n_coaches_trial_decided = 0;
            this.followed_coach_advice = false;
        }

        //System.out.println("Coach " + c.getCoachId() + " from Team " + c.getTeam_id() + " is awake at informReferee");

    }


    /**
     * Wakes up contestants in followCoachAdvice and sleeps until contestants are done pulling the rope
    */
    public synchronized void startTrial()
    {
        Referee r = (Referee) Thread.currentThread();
        //System.out.println("Referee is asleep on startTrial");

        /*  wait for coaches to inform referee  */
        while (!this.coaches_informed){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.coaches_informed = false;
        //System.out.println("Referee is awake on startTrial");

        this.trial_started = true;
        /*  wake up contestants in the bench  */
        notifyAll();

    }


    /**
     * Contestants sleep in the playground and the last one to get there wakes them up so that they pull at the same
     * time
     */
    public synchronized void getReady()
    {
        Contestant c = (Contestant) Thread.currentThread();

        /*  wait for every contestant to be ready  */
        /*  the last contestant to get ready wakes up everyone else  */
        while (!this.trial_started){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //System.out.println("Contestant " + c.getContestantId() + " of team " + c.getTeam_id() + " is asleep on getReady");
        
        this.n_ready_contestants_started += 1;
        if(this.n_ready_contestants_started >= 6){
            /*  restore contestants value for next trial  */
            this.n_ready_contestants_started = 0;
            this.trial_started = false;
        }
    }



    /**
     * Announce new game
     */
    public synchronized void declareMatchWinner() {
        this.match_ended = true;
        notifyAll();
    }

}
