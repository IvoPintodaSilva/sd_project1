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
        boolean match_not_over = true;
        //repo.contestantLog(this.id, this.team_id, state);
        while (match_not_over){
            switch (state){

                case SEAT_AT_THE_BENCH:
                    repo.updtRopeCenter(Integer.MAX_VALUE);
                    match_not_over = contestants_bench.followCoachAdvice();
                    if(!match_not_over){
                        break;
                    }
                    state = ContestantState.STAND_IN_POSITION;
                    repo.contestantLog(this.id, this.team_id, this.strength, state);
                    break;
                case STAND_IN_POSITION:
                    contestants_bench.getReady();
                    state = ContestantState.DO_YOUR_BEST;
                    repo.contestantLog(this.id, this.team_id, this.strength, state);
                    break;
                case DO_YOUR_BEST://Integer.Max_value stands for true, Integer.Min_value stands for false
                    int[] unpack_val;
                    do{
                        unpack_val = playground.pullTheRope();
                        //System.out.println("rope---------------->" + unpack_val[1]);
                        //repo.updtRopeCenter(unpack_val[1]);
                    }while(unpack_val[0] == 1);//while has next push
                    repo.contestantLog(this.id, this.team_id, this.strength, state);
                    playground.iAmDone();
                    playground.seatDown();
                    state = ContestantState.START;
                    //repo.contestantLog(this.id, this.team_id, state);
                    break;
                default:
                    state = ContestantState.SEAT_AT_THE_BENCH;
                    //contestants_bench.seatDown();
                    repo.contestantLog(this.id, this.team_id, this.strength, state);
                    break;
            }
        }

        System.out.println("Contestant " + this.id + " finished execution");

    }

    public int getContestantId() {
        return id;
    }

    public int getTeam_id() {
        return team_id;
    }

    public int getStrength() {
        return this.strength;
    }

    public void decrementStrength() {
        if (this.strength > 0){
            this.strength--;
        }
    }

    public void incrementStrength() {
        this.strength++;
    }
}
