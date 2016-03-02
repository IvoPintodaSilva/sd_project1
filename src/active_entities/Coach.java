package active_entities;

import shared_mem.ContestantsBench;
import shared_mem.Playground;
import shared_mem.RefereeSite;

public class Coach extends Thread {

    //IDENTIFIERS
    private int id;
    private int team_id;
    private ContestantsBench contestants_bench;
    private RefereeSite referee_site;
    private Playground playground;
    //STATES
    private boolean WAIT_FOR_REFEREE_COMMAND;
    private boolean ASSEMBLE_TEAM;
    private boolean WATCH_TRIAL;

    public Coach(int id, int team_id, Playground playground, RefereeSite referee_site,
                 ContestantsBench contestants_bench) {
        this.id = id;
        this.team_id = team_id;
        this.playground = playground;
        this.referee_site = referee_site;
        this.contestants_bench = contestants_bench;
    }

    public void run() {

        this.referee_site.waitForNewGame();
        this.WAIT_FOR_REFEREE_COMMAND = true;

        this.contestants_bench.callContestants();
        this.WAIT_FOR_REFEREE_COMMAND = false;
        this.ASSEMBLE_TEAM = true;

        this.referee_site.informReferee();
        this.ASSEMBLE_TEAM = false;
        this.WATCH_TRIAL = true;

        System.out.println("Coach " + this.id + " finished execution");
    }

    public int getCoachId() {
        return id;
    }

    public int getTeam_id() {
        return team_id;
    }

}
