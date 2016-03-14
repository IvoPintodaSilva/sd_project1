package active_entities;


import interfaces.IContestantsBenchContestant;
import interfaces.IPlaygroundContestant;
import interfaces.IRefereeSiteContestant;

public class Contestant extends Thread {
    //IDENTIFIERS
    private int id;
    private int team_id;
    private int strength;
    private IContestantsBenchContestant contestants_bench;
    private IRefereeSiteContestant referee_site;
    private IPlaygroundContestant playground;

    private enum State {
        SEAT_AT_THE_BENCH, STAND_IN_POSITION, DO_YOUR_BEST, START
    }

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

        State state= State.START;
        while (true){
            switch (state){

                case SEAT_AT_THE_BENCH:
                    contestants_bench.followCoachAdvice();
                    state = State.STAND_IN_POSITION;
                    break;
                case STAND_IN_POSITION:
                    playground.getReady();
                    state = State.DO_YOUR_BEST;
                    break;
                case DO_YOUR_BEST:
                    /*  this can't be done with a for loop, needs further analysis  */
                    for(int i = 0; i < 6; i++){
                        playground.pullTheRope();
                    }
                    contestants_bench.iAmDone();
                    System.exit(0);
                    state = State.START;
                    break;
                default:
                    state = State.SEAT_AT_THE_BENCH;
                    contestants_bench.seatDown();
                    break;
            }
        }

        //System.out.println("Contestant " + this.id + " finished execution");

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
