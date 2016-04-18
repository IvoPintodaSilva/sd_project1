package pt.ua.sd.RopeGame.shared_mem;

import pt.ua.sd.RopeGame.active_entities.Coach;
import pt.ua.sd.RopeGame.active_entities.Contestant;
import pt.ua.sd.RopeGame.active_entities.Referee;
import pt.ua.sd.RopeGame.interfaces.IContestantsBenchCoach;
import pt.ua.sd.RopeGame.interfaces.IContestantsBenchContestant;
import pt.ua.sd.RopeGame.interfaces.IContestantsBenchReferee;

import java.lang.reflect.Array;
import java.util.Arrays;


/**
 * Contestant Bench shared memory<br>
 *<b><center><font size=6>Contestant Bench shared memory</font></center></b><br>
 *     <font size=4>This class represents the monitor/shared memory of the contestant bench.</font>
 *
 *
 */
public class MContestantsBench implements IContestantsBenchContestant, IContestantsBenchCoach, IContestantsBenchReferee {

    /**
     * Internal Data
     */
    private boolean trial_called = false;//flag that identifies if the trial was called

    private int n_coaches_called_contestants = 0;//number of players that already called the contestants, max 2
    private boolean contestants_called = false;//flag that identifies if the contestants were already called

    private int advice_followed = 0;//number of players that already follow the advice

    private boolean trial_started = false;//flag true if trial has started yet

    private int n_coaches_informed_referee = 0;//when a coach inform the referee this var is increased in 1 unit
    private boolean coaches_informed = false;//flag if the coaches were or not informed yet

    private int n_contestants_called = 0;//number of contestants called on callcontestants

    private int n_coaches_trial_decided = 0;//nr of coaches that were already informed that trial was decided

    /*new selection of the playing team*/
    //private boolean new_team1_selected[] = {true, true, true, true, true};
    //private boolean new_team2_selected[] = {true, true, true, true, true};
    private boolean new_team1_selected[];
    private boolean new_team2_selected[];

    /* arrays of selected contestants to play the trial*/
    //private static int team1_selected_contestants[] = {0, 1, 2};
    //private static int team2_selected_contestants[] = {0, 1, 2};
    private static int team1_selected_contestants[];
    private static int team2_selected_contestants[];

    private int n_ready_contestants_started;//nr of contestants that started trial and are ready
    private boolean followed_coach_advice;//flag for follow coach advice, true when the advce is followed
    /*team strenght*/
    private int[] team1_strength = {0, 0, 0, 0, 0};
    private int[] team2_strength = {0, 0, 0, 0, 0};

    private boolean match_ended = false;//flag for match ended, true if ended


    /**
     * Referee calls the trial
     */
    public synchronized void callTrial()
    {

        /*  wake up coaches in reviewNotes  */
        this.trial_called = true;


        notifyAll();

    }



    /**
     * Coach sleeps while the trial was not called or the mach is not started yet and then
     * calls the contestants to play
     */
    public synchronized boolean callContestants(int team_id,int[] selected_contestants, int n_players)
    {
        /*  if arrays are not initilialized  */
        if (new_team1_selected == null){
            new_team1_selected = new boolean[n_players];
            Arrays.fill(new_team1_selected, true);
        }
        if (new_team2_selected == null){
            new_team2_selected = new boolean[n_players];
            Arrays.fill(new_team1_selected, true);

        }

        /*  to know when to increment contestants strength  */
        for (int i = 0; i < n_players; i++){
            if(team_id == 1){
                this.new_team1_selected[i] = true;
            }else if(team_id == 2){
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

        if(team_id == 1){
            team1_selected_contestants = selected_contestants;
        }else if(team_id == 2){
            team2_selected_contestants = selected_contestants;
        }

        return true;

    }

    /**
     * Follow coach advice and sleep until referee wakes them up on start trial. Only the contestants that
     * are playing the trial are waken up. The other ones gain one strength point.
     * @return  true if player is playing and false if he's going to sit down
     */
    public synchronized boolean[] followCoachAdvice(int contestant_id,int strength, int team_id, int n_players, int n_players_pushing)
    {
        boolean[] ret =new boolean[2];
        ret[1]=false;//not increment by default
        ret[0]=false;//return false by default

        /*  if arrays are not initilialized  */
        if (new_team1_selected == null){
            new_team1_selected = new boolean[n_players];
            Arrays.fill(new_team1_selected, true);
        }
        if (new_team2_selected == null){
            new_team2_selected = new boolean[n_players];
            Arrays.fill(new_team1_selected, true);
        }


        if (team1_selected_contestants == null){
            team1_selected_contestants = new int[n_players_pushing];
            for(int i = 0; i < n_players_pushing; i++){
                team1_selected_contestants[i] = i;
            }
        }

        if (team2_selected_contestants == null){
            team2_selected_contestants = new int[n_players_pushing];
            for(int i = 0; i < n_players_pushing; i++){
                team2_selected_contestants[i] = i;
            }
        }


        if(team_id == 1){
            this.team1_strength[contestant_id] = strength;
        }
        else if(team_id == 2){
            this.team2_strength[contestant_id] = strength;
        }

        if(team_id == 1){

            while (
                    ((!this.contestants_called) ||
                    ((contestant_id != team1_selected_contestants[0]) &&
                            (contestant_id != team1_selected_contestants[1]) &&
                            (contestant_id != team1_selected_contestants[2])))

                    ||

                            this.match_ended

                    ){


                if(this.match_ended){
                    ret[0]=false;//return false
                    return ret;
                }

                if( new_team1_selected[contestant_id] ){
                    new_team1_selected[contestant_id] = false;

                    if(((contestant_id != team1_selected_contestants[0]) &&
                            (contestant_id != team1_selected_contestants[1]) &&
                            (contestant_id != team1_selected_contestants[2]))){
                        ret[1]=true;//increment strenght
                    }

                }


                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }else if(team_id == 2){
            while (
                    ((!this.contestants_called) ||
                    ((contestant_id != team2_selected_contestants[0]) &&
                            (contestant_id != team2_selected_contestants[1]) &&
                            (contestant_id != team2_selected_contestants[2])))
                    ||
                            this.match_ended
                    ){


                if(this.match_ended){
                    ret[1]=false;//not increment by default
                    return ret;
                }

                if( new_team2_selected[contestant_id] ){
                    new_team2_selected[contestant_id] = false;

                    if(((contestant_id != team2_selected_contestants[0]) &&
                            (contestant_id != team2_selected_contestants[1]) &&
                            (contestant_id != team2_selected_contestants[2]))){
                        ret[1]=true;
                    }

                }

                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }



        if(team_id == 1){//if team 1
            if (this.new_team1_selected[contestant_id]) {//if the current contestant is selected
                this.new_team1_selected[contestant_id] = false;
            }
        }
        else if(team_id == 2){
            if (this.new_team2_selected[contestant_id]) {
                this.new_team2_selected[contestant_id] = false;
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
        ret[0]=true;
        return ret;
    }


    /**
     * Last coach wakes up referee and sleeps until all the contestants have followed their coache's advice
     */
    public synchronized void informReferee() {

        this.n_coaches_informed_referee += 1;

        /*  wake up referee when both coaches have informed them  */
        if(this.n_coaches_informed_referee >= 2){
            this.n_coaches_informed_referee = 0;
            this.coaches_informed = true;
            notifyAll();
        }

        /*  wait for trial decision  */
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
    }


    /**
     * Referee waits for coaches to inform the referee and then starts trial and wakes up the contestants in bench
    */
    public synchronized void startTrial()
    {

        /*  wait for coaches to inform referee  */
        while (!this.coaches_informed){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.coaches_informed = false;
        this.trial_started = true;
        notifyAll();

    }


    /**
     * Contestants sleep until trial is started
     *
     */
    public synchronized void getReady()
    {

        /*  wait for every contestant to be ready  */
        /*  the last contestant to get ready wakes up everyone else  */
        while (!this.trial_started){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.n_ready_contestants_started += 1;
        if(this.n_ready_contestants_started >= 6){
            /*  restore contestants value for next trial  */
            this.n_ready_contestants_started = 0;
            this.trial_started = false;
        }
    }


    /**
     * Declares the winner of the match
     * @param games1 number of games won by team 1
     * @param games2 number of games won by team 2
     * @return winner team id
     */
    public synchronized int declareMatchWinner(int games1, int games2) {

        this.match_ended = true;
        notifyAll();
        if (games1 > games2) {
            return 1;
        } else if(games2 > games1){
            return 2;
        }
        return 0;

    }

}
