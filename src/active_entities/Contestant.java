package active_entities;


public class Contestant extends Thread {
    //IDENTIFIERS
    private int id;
    private int team_id;
    private int strength;
    //STATES
    private boolean SEAT_AT_THE_BENCH;
    private boolean STAND_IN_POSITION;
    private boolean DO_YOUR_BEST;

    public Contestant(int id, int team_id, int strength){
        this.id = id;
        this.team_id = team_id;
        this.strength = strength;
    }

    public void run(){
        System.out.println("contestant");
    }
}
