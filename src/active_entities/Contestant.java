package active_entities;


import interfaces.IContestantsBenchContestant;
import interfaces.IPlaygroundContestant;
import interfaces.IRefereeSiteContestant;
import shared_mem.MContestantsBench;
import shared_mem.MPlayground;
import shared_mem.MRefereeSite;

public class Contestant extends Thread {
    //IDENTIFIERS
    private int id;
    private int team_id;
    private int strength;
    private IContestantsBenchContestant contestants_bench;
    private IRefereeSiteContestant referee_site;
    private IPlaygroundContestant playground;
    //STATES
    private boolean SEAT_AT_THE_BENCH;
    private boolean STAND_IN_POSITION;
    private boolean DO_YOUR_BEST;

    public Contestant(int id, int team_id, int strength,
                      IPlaygroundContestant playground,
                      IRefereeSiteContestant referee_site,
                      IContestantsBenchContestant contestants_bench){
        this.id = id;
        this.team_id = team_id;
        this.strength = strength;
        this.playground = playground;
        this.referee_site = referee_site;
        this.contestants_bench = contestants_bench;
    }

    public void run() {
        contestants_bench.seatDown();
        this.SEAT_AT_THE_BENCH = true;

        playground.followCoachAdvice();
        this.SEAT_AT_THE_BENCH = false;
        this.STAND_IN_POSITION = true;

        playground.getReady();
        this.STAND_IN_POSITION = false;
        this.DO_YOUR_BEST = true;

        /*  this can't be done with a for loop, needs further analysis  */
        for(int i = 0; i < 6; i++){
            playground.pullTheRope();
        }

        playground.iAmDone();
        this.DO_YOUR_BEST = false;
        this.SEAT_AT_THE_BENCH = true;

        System.out.println("Contestant " + this.id + " finished execution");

    }

    public int getContestantId() {
        return id;
    }

    public int getTeam_id() {
        return team_id;
    }

    public int getStrength() {
        return strength;
    }
}
