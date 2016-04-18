package pt.ua.sd.RopeGame.active_entities;

import pt.ua.sd.RopeGame.enums.CoachState;
import pt.ua.sd.RopeGame.interfaces.IPlaygroundCoach;
import pt.ua.sd.RopeGame.interfaces.IContestantsBenchCoach;
import pt.ua.sd.RopeGame.interfaces.IRefereeSiteCoach;
import pt.ua.sd.RopeGame.interfaces.IRepoCoach;

/**
 * Coach thread<br>
 *<b><center><font size=6>Coach thread</font></center></b><br>
 *     <font size=4>This class represents the thread of the coach, her life cycle ends when
 *     the internal flag match_not_over takes the false notation.</font>
 *     Notes:
 *     -> the access to the shared memories is limited by the interfaces present in the interfaces package.
 *     -> the default selected team to play is the first 3 contestants (id: 0,1 and 2).
 *     -> the default state is WAIT_FOR_REFEREE_COMMAND
 *
 *
 */

public class Coach extends Thread {

    /**
     * Internal Data
     */
    private int id;//represents the id of the coach
    private int team_id;//represents the id of the team
    private int team_selected_contestants[] = {0, 1, 2};//each coach has 3 selected contestants to play
    private IContestantsBenchCoach contestants_bench;//represents the bench shared memory
    private IRefereeSiteCoach referee_site;//represents the referee site shared memory
    private IPlaygroundCoach playground;//represents the playground shared memory
    private IRepoCoach repo;//represents the general info repository of shared memory


    /**
     * Constructor
     * @param id current coach id
     * @param team_id current team id
     * @param playground playground shared memory instancy
     * @param referee_site referee site shared memory instancy
     * @param contestants_bench contestants bench shared memory instancy
     * @param repo general info repository shared memory instancy
     */
    public Coach(int id, int team_id, IPlaygroundCoach playground, IRefereeSiteCoach referee_site,
                 IContestantsBenchCoach contestants_bench, IRepoCoach repo) {
        this.id = id;
        this.team_id = team_id;
        this.playground = playground;
        this.referee_site = referee_site;
        this.contestants_bench = contestants_bench;
        this.repo = repo;
    }

    /**
     * Thread life cycle
     */
    public void run() {

        CoachState state = CoachState.WAIT_FOR_REFEREE_COMMAND;//initial state
        repo.coachLog(this.team_id, state);//update repo
        boolean match_not_over = true;


        while (match_not_over){//this value can change when coach is in the begining of his cycle(WRC) by callContestants
            switch (state){
                case WAIT_FOR_REFEREE_COMMAND:
                    match_not_over = this.contestants_bench.callContestants(this.team_id,this.team_selected_contestants);
                    state = CoachState.ASSEMBLE_TEAM;//change state
                    repo.coachLog(this.team_id, state);//update central info repository
                    break;
                case ASSEMBLE_TEAM:
                    this.contestants_bench.informReferee();
                    state = CoachState.WATCH_TRIAL;
                    repo.coachLog(this.team_id, state);//update central info repository
                    break;
                case WATCH_TRIAL:
                    this.team_selected_contestants = this.playground.reviewNotes(this.team_selected_contestants);
                    state = CoachState.WAIT_FOR_REFEREE_COMMAND;
                    repo.coachLog(this.team_id, state);//update central info repository
                    break;
                default:
                    state= CoachState.WAIT_FOR_REFEREE_COMMAND;//default state
                    repo.coachLog(this.team_id, state);//update central info repository
                    break;
            }
        }

        System.out.println("Coach " + this.id + " finished execution");
    }

    /**
     *
     * @return the {@link Integer} repesentation of the coach id
     */
    public int getCoachId() {
        return id;
    }

    /**
     *
     * @return the {@link Integer} representation of the team id
     */
    public int getTeam_id() {
        return team_id;
    }

    /**
     *
     * @return the {@link Integer}[] representation of the contestants
     */
    public int[] getSelectedContestants(){
        return this.team_selected_contestants;
    }

}
