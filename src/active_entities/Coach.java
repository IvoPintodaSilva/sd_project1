package active_entities;

public class Coach extends Thread {

    //IDENTIFIERS
    private int id;
    private int team_id;
    //STATES
    private boolean WAIT_FOR_REFEREE_COMMAND;
    private boolean ASSEMBLE_TEAM;
    private boolean WATCH_TRIAL;

    public Coach(int id, int team_id) {
        this.id = id;
        this.team_id = team_id;
    }

    public void run() {
        System.out.println("coach");
    }

    public int getCoachId() {
        return id;
    }

    public int getTeam_id() {
        return team_id;
    }

}
