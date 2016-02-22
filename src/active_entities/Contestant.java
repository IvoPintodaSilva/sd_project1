package active_entities;

/**
 * Created by ivosilva on 22/02/16.
 */
public class Contestant extends Thread {
    private int id;
    private int team_id;
    private int strength;

    public Contestant(int id, int team_id, int strength){
        this.id = id;
        this.team_id = id;
        this.strength = strength;
    }

    public void run(){
    }
}
