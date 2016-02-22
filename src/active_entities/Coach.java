package active_entities;

/**
 * Created by ivosilva on 22/02/16.
 */
public class Coach extends Thread {
    private int id;
    private int team_id;

    public Coach(int id, int team_id){
        this.id = id;
        this.team_id = id;
    }

    public void run(){
    }
}
