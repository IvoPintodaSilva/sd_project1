package pt.ua.sd.RopeGame.active_entities;


import pt.ua.sd.RopeGame.enums.ContestantState;
import pt.ua.sd.RopeGame.interfaces.IContestantsBenchContestant;
import pt.ua.sd.RopeGame.interfaces.IPlaygroundContestant;
import pt.ua.sd.RopeGame.interfaces.IRefereeSiteContestant;
import pt.ua.sd.RopeGame.interfaces.IRepoContestant;


/**
 * Contestant thread<br>
 *<b><center><font size=6>Contestant thread</font></center></b><br>
 *     <font size=4>This class represents the thread of the contestant, his life cycle ends when
 *     the internal flag match_not_over takes the false notation.</font>
 *     Notes:
 *     -> the access to the shared memories is limited by the interfaces present in the interfaces package.
 *     -> the default state is SEAT_AT_THE_BENCH
 *
 *
 */
public class Contestant extends Thread {

    /**
     * Internal Data
     */
    private int id;//represents the id of the coach
    private int team_id;//represents the id of the team
    private int strength;//represents the strenght of the current player
    private IContestantsBenchContestant contestants_bench;//represents the bench shared memory
    private IRefereeSiteContestant referee_site;//represents the referee site shared memory
    private IPlaygroundContestant playground;//represents the playground shared memory
    private IRepoContestant repo;//represents the general info repository of shared memory
    private int n_players;//number of players in each team, defined in rg.config
    private int n_players_pushing;//number of players in each team pushing at any given trial, defined in rg.config
    private int n_trials;//number of trials, defined in rg.config
    private int n_games;//number of games, defined in rg.config


    /**
     * Constructor
     * @param id current contestant id
     * @param team_id current team id
     * @param strength strengt of current player
     * @param playground playground shared memory instancy
     * @param referee_site referee site shared memory instancy
     * @param contestants_bench contestants bench shared memory instancy
     * @param repo general info repository shared memory instancy
     */
    public Contestant(int id, int team_id, int strength,
                      IPlaygroundContestant playground,
                      IRefereeSiteContestant referee_site,
                      IContestantsBenchContestant contestants_bench,
                      IRepoContestant repo,
                      int n_players, int n_players_pushing,
                      int n_trials, int n_games){
        this.id = id;
        this.team_id = team_id;
        this.strength = strength;
        this.playground = playground;
        this.referee_site = referee_site;
        this.contestants_bench = contestants_bench;
        this.repo = repo;
        this.n_players = n_players;
        this.n_players_pushing = n_players_pushing;
        this.n_trials = n_trials;
        this.n_games = n_games;
    }

    /**
     * Thread life cycle
     */
    public void run() {

        ContestantState state = ContestantState.START;//initial state
        boolean[] unpack = new boolean[2];//for receive return from follow coach advice
        unpack[0]=false;//def
        unpack[1]=false;//def
        boolean match_not_over = true;


        while (match_not_over){//this value can change when contestant is in the begining of his cycle(SAB) by followCoachAdvice()
            switch (state){

                case SEAT_AT_THE_BENCH:
                    repo.updtRopeCenter(Integer.MAX_VALUE);//update central info repository the MAX_VALUE hides the log value
                    unpack = contestants_bench.followCoachAdvice(this.id,this.strength,this.team_id, this.n_players, this.n_players_pushing);
                    match_not_over = unpack[0];
                    if(unpack[1])
                    {
                        incrementStrength();
                    }
                    if(!match_not_over){
                        break;
                    }
                    state = ContestantState.STAND_IN_POSITION;//change state
                    repo.contestantLog(this.id, this.team_id, this.strength, state);//update central info repository
                    break;
                case STAND_IN_POSITION:
                    contestants_bench.getReady();
                    state = ContestantState.DO_YOUR_BEST;//change state
                    repo.contestantLog(this.id, this.team_id, this.strength, state);//update central info repository
                    break;
                case DO_YOUR_BEST:
                    playground.pullTheRope(this.team_id,this.strength,this.id);
                    repo.contestantLog(this.id, this.team_id, this.strength, state);//update central info repository
                    playground.iAmDone();
                    decrementStrength();//depois de am done decrementar a forca
                    playground.seatDown();
                    state = ContestantState.START;//change state
                    break;
                default:
                    state = ContestantState.SEAT_AT_THE_BENCH;//change state
                    repo.contestantLog(this.id, this.team_id, this.strength, state);//update central info repository
                    break;
            }
        }

        System.out.println("Contestant " + this.id + " finished execution");

    }

    /**
     *
     * @return the {@link Integer} repesentation of the contestant id
     */
    public int getContestantId() {
        return id;
    }
    /**
     *
     * @return the {@link Integer} repesentation of the team id
     */
    public int getTeam_id() {
        return team_id;
    }

    /**
     *
     * @return the {@link Integer} repesentation of the contestant strenght
     */
    public int getStrength() {
        return this.strength;
    }

    /**
     *Decrements one unit of strenght on current contestant
     */
    public void decrementStrength() {
        if (this.strength > 0){
            this.strength--;
        }
    }
    /**
     *
     *Increments one unit of strenght on current contestant
     */
    public void incrementStrength() {
        this.strength++;
    }
}
