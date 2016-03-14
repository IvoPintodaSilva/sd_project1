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

    private boolean contestants_called = false;
    private int advice_followed = 0;
    private boolean contestant_in_position = false;
    private boolean trial_started = false;
    private boolean coaches_informed = false;
    private int n_coaches_informed_referee = 0;
    private boolean trial_decided = false;
    private boolean contestants_are_done = false;
    private int n_contestants_done = 0;


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

        while (!this.trial_started){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * This function purpose is to wake up the contestants and put the referee to sleep
     */
    public synchronized void callTrial()
    {
        Referee c = (Referee) Thread.currentThread();
        System.out.println("Referee is asleep on callTrial");

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
     * This function purpose is to coach inform the referree that the contestants are ready to play and put the coach to sleep
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
        System.out.println("Coach " + c.getCoachId() + " from Team " + c.getTeam_id() + " is asleep at informReferee");
        while (!this.trial_decided){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public synchronized void startTrial()
    {
        Referee r = (Referee) Thread.currentThread();
        System.out.println("Referee is asleep on startTrial");

        this.trial_started = true;

        /*  wake up contestants in playground  */
        notifyAll();

        /*  wait for contestants to get be done pulling the rope  */
        while (!this.contestants_are_done){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public synchronized void iAmDone()
    {
        Contestant c = (Contestant) Thread.currentThread();
        this.n_contestants_done += 1;

        /*  last contestant done wakes up referee  */
        if(this.n_contestants_done >= 10) {
            this.contestants_are_done = true;
            notifyAll();
        }

        System.out.println("Contestant " + c.getContestantId() + " of team " + c.getTeam_id() + " is asleep on iAmDone");
        while (!this.trial_decided){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized boolean assertTrialDecision() {
        Referee r = (Referee) Thread.currentThread();

        this.trial_decided = true;

        /*  wake up contestants in iAmDone and coaches in informReferee  */
        notifyAll();

        /*  return has_next_trial  */
        return false;
    }

    public synchronized void reviewNotes() {
        Coach c = (Coach) Thread.currentThread();

        System.out.println("Coach " + c.getCoachId() + " from Team " + c.getTeam_id() + " is asleep at informReferee");
        while (true){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }






}
