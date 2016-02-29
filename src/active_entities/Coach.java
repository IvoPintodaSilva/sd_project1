package active_entities;

public class Coach extends Thread {
//IDENTIFIERS
    private int ID;
    private int TEAM_ID;
//STATES
    private boolean WAIT_FOR_REFEREE_COMMAND;
    private boolean ASSEMBLE_TEAM;
    private boolean WATCH_TRIAL;

    public Coach(int id, int team_id){
        this.ID = id;
        this.TEAM_ID = team_id;
    }

    public void run(){
        System.out.println("coach");
    }


}
