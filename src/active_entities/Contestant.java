package active_entities;


public class Contestant extends Thread {
    //IDENTIFIERS
    private int ID;
    private int TEAM_ID;
    private int STRENGTH;
    //STATES
    private boolean SEAT_AT_THE_BENCH;
    private boolean STAND_IN_POSITION;
    private boolean DO_YOUR_BEST;

    public Contestant(int id, int team_id, int strength){
        this.ID = id;
        this.TEAM_ID = id;
        this.STRENGTH = strength;
    }

    public void run(){

    }
}
