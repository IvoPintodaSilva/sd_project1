package active_entities;

import interfaces.*;
import shared_mem.MContestantsBench;
import shared_mem.MPlayground;
import shared_mem.MRefereeSite;




public class Coach extends Thread {

    //IDENTIFIERS
    private int id;
    private int team_id;
    private IContestantsBenchCoach contestants_bench;
    private IRefereeSiteCoach referee_site;
    private IPlaygroundCoach playground;

    private enum State {
        WAIT_FOR_REFEREE_COMMAND, ASSEMBLE_TEAM, WATCH_TRIAL
    }

    public Coach(int id, int team_id, IPlaygroundCoach playground, IRefereeSiteCoach referee_site,
                 IContestantsBenchCoach contestants_bench) {
        this.id = id;
        this.team_id = team_id;
        this.playground = playground;
        this.referee_site = referee_site;
        this.contestants_bench = contestants_bench;
    }

    public void run() {

        State state = State.WAIT_FOR_REFEREE_COMMAND;

        while (true){
            switch (state){
                case WAIT_FOR_REFEREE_COMMAND:
                    this.referee_site.waitForNewGame();
                    state = State.ASSEMBLE_TEAM;
                    break;
                case ASSEMBLE_TEAM:
                    this.contestants_bench.callContestants();
                    state = State.WATCH_TRIAL;
                    break;
                case WATCH_TRIAL:
                    this.referee_site.informReferee();
                    state = State.WAIT_FOR_REFEREE_COMMAND;
            }
        }

        //System.out.println("Coach " + this.id + " finished execution");
    }

    public int getCoachId() {
        return id;
    }

    public int getTeam_id() {
        return team_id;
    }

}
