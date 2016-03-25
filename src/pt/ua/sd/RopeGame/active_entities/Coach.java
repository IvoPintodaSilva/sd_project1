package pt.ua.sd.RopeGame.active_entities;

import pt.ua.sd.RopeGame.enums.CoachState;
import pt.ua.sd.RopeGame.interfaces.IPlaygroundCoach;
import pt.ua.sd.RopeGame.interfaces.IContestantsBenchCoach;
import pt.ua.sd.RopeGame.interfaces.IRefereeSiteCoach;
import pt.ua.sd.RopeGame.interfaces.IRepoCoach;

public class Coach extends Thread {

    //IDENTIFIERS
    private int id;
    private int team_id;
    private int team_selected_contestants[] = {0, 1, 2};
    private IContestantsBenchCoach contestants_bench;
    private IRefereeSiteCoach referee_site;
    private IPlaygroundCoach playground;
    private IRepoCoach repo;

    public Coach(int id, int team_id, IPlaygroundCoach playground, IRefereeSiteCoach referee_site,
                 IContestantsBenchCoach contestants_bench, IRepoCoach repo) {
        this.id = id;
        this.team_id = team_id;
        this.playground = playground;
        this.referee_site = referee_site;
        this.contestants_bench = contestants_bench;
        this.repo = repo;
    }

    public void run() {

        CoachState state = CoachState.WAIT_FOR_REFEREE_COMMAND;
        repo.coachLog(this.team_id, state);
        boolean match_not_over = true;


        while (match_not_over){
            switch (state){
                case WAIT_FOR_REFEREE_COMMAND:
                    match_not_over = this.contestants_bench.callContestants();
                    state = CoachState.ASSEMBLE_TEAM;
                    repo.coachLog(this.team_id, state);
                    break;
                case ASSEMBLE_TEAM:
                    this.contestants_bench.informReferee();
                    state = CoachState.WATCH_TRIAL;
                    repo.coachLog(this.team_id, state);
                    break;
                case WATCH_TRIAL:
                    this.team_selected_contestants = this.playground.reviewNotes(this.team_selected_contestants);
                    state = CoachState.WAIT_FOR_REFEREE_COMMAND;
                    repo.coachLog(this.team_id, state);
                    break;
                default:
                    state= CoachState.WAIT_FOR_REFEREE_COMMAND;
                    repo.coachLog(this.team_id, state);
                    break;
            }
        }

        System.out.println("Coach " + this.id + " finished execution");
    }

    public int getCoachId() {
        return id;
    }

    public int getTeam_id() {
        return team_id;
    }

    public int[] getSelectedContestants(){
        return this.team_selected_contestants;
    }

}
