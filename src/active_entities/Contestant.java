package active_entities;


import enums.ContestantState;
import interfaces.IContestantsBenchContestant;
import interfaces.IPlaygroundContestant;
import interfaces.IRefereeSiteContestant;
import interfaces.IRepoContestant;

public class Contestant extends Thread {
    //IDENTIFIERS
    private int id;
    private int team_id;
    private int strength;
    private IContestantsBenchContestant contestants_bench;
    private IRefereeSiteContestant referee_site;
    private IPlaygroundContestant playground;
    private IRepoContestant repo;

    public Contestant(int id, int team_id, int strength,
                      IPlaygroundContestant playground,
                      IRefereeSiteContestant referee_site,
                      IContestantsBenchContestant contestants_bench,
                      IRepoContestant repo){
        this.id = id;
        this.team_id = team_id;
        this.strength = strength;
        this.playground = playground;
        this.referee_site = referee_site;
        this.contestants_bench = contestants_bench;
        this.repo = repo;
    }

    public void run() {

        ContestantState state = ContestantState.START;
        //repo.contestantLog(this.id, this.team_id, state);
        while (true){
            switch (state){

                case SEAT_AT_THE_BENCH:
                    boolean chosen = contestants_bench.followCoachAdvice();
                    if(chosen){
                        state = ContestantState.STAND_IN_POSITION;
                    }
                    else{
                        state = ContestantState.START;
                    }
                    repo.contestantLog(this.id, this.team_id, this.strength, state);
                    break;
                case STAND_IN_POSITION:
                    playground.getReady();
                    state = ContestantState.DO_YOUR_BEST;
                    repo.contestantLog(this.id, this.team_id, this.strength, state);
                    break;
                case DO_YOUR_BEST:
                    boolean has_next_push;
                    do{
                        has_next_push = playground.pullTheRope();
                        repo.contestantLog(this.id, this.team_id, this.strength, state);
                        System.out.println("push");
                    }while(has_next_push);
                    contestants_bench.iAmDone();
                    state = ContestantState.START;
                    //repo.contestantLog(this.id, this.team_id, state);
                    break;
                default:
                    state = ContestantState.SEAT_AT_THE_BENCH;
                    contestants_bench.seatDown();
                    repo.contestantLog(this.id, this.team_id, this.strength, state);
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

    public void decrementStrength() {
        this.strength--;
    }
}
