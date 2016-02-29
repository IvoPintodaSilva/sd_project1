import active_entities.Coach;
import active_entities.Contestant;
import active_entities.Referee;
import shared_mem.ContestantsBench;
import shared_mem.Playground;
import shared_mem.RefereeSite;

public class RopeGame {
    public static void main(String[] args) {


        Coach coach_team1 = new Coach(1, 1);
        Coach coach_team2 = new Coach(2, 2);

        Contestant[] contestants_team1 = new Contestant[5];
        for(int i = 0; i < 5; i++){
            contestants_team1[i] = new Contestant(i, 1, 5);
            contestants_team1[i].start();
        }

        Contestant[] contestants_team2 = new Contestant[5];
        for(int i = 0; i < 5; i++){
            contestants_team2[i] = new Contestant(i, 2, 5);
            contestants_team2[i].start();
        }

        coach_team1.start();
        coach_team2.start();

        /*  threads join  */

        try {
            coach_team1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            coach_team2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < 5; i++){
            try {
                contestants_team1[i].join();
                contestants_team2[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
