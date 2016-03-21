import active_entities.Coach;
import active_entities.Contestant;
import active_entities.Referee;
import interfaces.*;
import shared_mem.MContestantsBench;
import shared_mem.MGeneralInfoRepo;
import shared_mem.MPlayground;
import shared_mem.MRefereeSite;

public class RopeGame {
    public static void main(String[] args) {

        MRefereeSite refereeSite = new MRefereeSite();
        MPlayground playground = new MPlayground();
        MContestantsBench bench = new MContestantsBench();
        MGeneralInfoRepo repo = new MGeneralInfoRepo();

        Coach coach_team1 = new Coach(1, 1,
                (IPlaygroundCoach) playground,
                (IRefereeSiteCoach) refereeSite,
                (IContestantsBenchCoach) bench,
                (IRepoCoach) repo);
        Coach coach_team2 = new Coach(2, 2,
                (IPlaygroundCoach) playground,
                (IRefereeSiteCoach) refereeSite,
                (IContestantsBenchCoach) bench,
                (IRepoCoach) repo);

        Referee ref = new Referee(
                (IPlaygroundReferee) playground,
                (IRefereeSiteReferee) refereeSite,
                (IContestantsBenchReferee) bench,
                (IRepoReferee) repo);



        Contestant[] contestants_team1 = new Contestant[5];
        for(int i = 0; i < 5; i++){
            contestants_team1[i] = new Contestant(i, 1, 5,
                    (IPlaygroundContestant) playground,
                    (IRefereeSiteContestant) refereeSite,
                    (IContestantsBenchContestant) bench,
                    (IRepoContestant) repo);
            contestants_team1[i].start();
        }

        Contestant[] contestants_team2 = new Contestant[5];
        for(int i = 0; i < 5; i++){
            contestants_team2[i] = new Contestant(i, 2, 5,
                    (IPlaygroundContestant) playground,
                    (IRefereeSiteContestant) refereeSite,
                    (IContestantsBenchContestant) bench,
                    (IRepoContestant) repo);
            contestants_team2[i].start();
        }

        coach_team1.start();
        coach_team2.start();
        ref.start();

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

        try {
            ref.join();
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
