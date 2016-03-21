package shared_mem;

import enums.CoachState;
import enums.ContestantState;
import enums.RefState;
import interfaces.IRepoCoach;
import interfaces.IRepoContestant;
import interfaces.IRepoReferee;

import java.io.*;
import java.nio.file.*;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created by ivosilva on 22/02/16.
 */
public class MGeneralInfoRepo implements IRepoCoach, IRepoContestant, IRepoReferee{

    public enum refStates{
        SOM,SOG,TSR,WTC,EOM,EOG,NONE
    };
    public enum  coachStates{
        WRC,AST,WTR,NONE
    };
    public enum contestantStates{
        SAB,SIP,DYB,NONE
    };

    private static refStates referee_state;
    private static coachStates[] coach_state;
    private static contestantStates[] team1_state;
    private static contestantStates[] team2_state;
    private static int game_nr;
    private static int score_t1;
    private static int score_t2;
    private static File OUTPUT_FILE;
    private String TO_WRITE="";
    private static String LOG_LOCATION;
    private static Writer output=null;


    public MGeneralInfoRepo()
    {
        LOG_LOCATION = "RopeGame.log";
        TO_WRITE="";
        OUTPUT_FILE = new File(LOG_LOCATION);
        if(OUTPUT_FILE.exists())
        {
            deleteFile();
        }
        game_nr = 0;
        score_t1=0;
        score_t2=0;
        referee_state = refStates.NONE;
        coach_state = new coachStates[2];
        for (int i=0;i<coach_state.length;i++) {
            coach_state[i]= coachStates.NONE;
        }

        team1_state = new contestantStates[5];
        team2_state = new contestantStates[5];
        for (int i=0;i<team1_state.length;i++) {
            team1_state[i]= contestantStates.NONE;
            team2_state[i]= contestantStates.NONE;
        }
        Addheader(true);
    }

    private synchronized void Addheader(boolean first)
    {
        String temp="";//temporary string
        if(first) {
            temp="                               Game of the Rope - Description of the internal state" +
                    "\n\n" +
                    "Ref Coa 1 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5 Coa 2 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5     Trial    \n" +
                    "Sta  Stat Sta SG Sta SG Sta SG Sta SG Sta SG  Stat Sta SG Sta SG Sta SG Sta SG Sta SG 3 2 1 . 1 2 3 NB PS\n";
            System.out.printf(temp);
            TO_WRITE += temp;
        }
        else {
            temp = "Game " + this.game_nr +
                    " \nRef Coa 1 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5 Coa 2 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5     Trial    \n" +
                    "Sta  Stat Sta SG Sta SG Sta SG Sta SG Sta SG  Stat Sta SG Sta SG Sta SG Sta SG Sta SG 3 2 1 . 1 2 3 NB PS\n";
            System.out.printf(temp);
            TO_WRITE += temp;
        }
    }
    @Override
    public synchronized void coachLog(int team_id, CoachState state) {
        switch (state){
            case WAIT_FOR_REFEREE_COMMAND:
                this.coach_state[team_id - 1] = coachStates.WRC;
                break;
            case ASSEMBLE_TEAM:
                this.coach_state[team_id - 1] = coachStates.AST;
                break;
            case WATCH_TRIAL:
                this.coach_state[team_id - 1] = coachStates.WTR;
                break;
        }

        printStates();

    }

    /**
     * imprime resultado do jogo ou da partida, ou de ambos
     * @param id
     * @param team_id
     * @param wonType knock out, draw or points
     * @param nr_trials
     */
    private void printResult(int id, int team_id, String wonType, int nr_trials)
    {
        switch (id){
            case 0://game
                if(wonType.equalsIgnoreCase("knock out"))
                {
                    System.out.printf("Game %i was won by team %i by %d in %i trials.",this.game_nr,team_id,wonType,nr_trials);
                }
                else if(wonType.equalsIgnoreCase("draw"))
                {
                    System.out.printf("Game %i was a draw",this.game_nr);
                }
                else if(wonType.equalsIgnoreCase("points"))
                {
                    System.out.printf("Game %i was won by team %i by %d",this.game_nr,team_id,wonType);
                }
                break;
            case 1://match
                if(wonType.equalsIgnoreCase("draw"))
                    System.out.printf("Match was draw");
                else if(wonType.equalsIgnoreCase("won"))
                    System.out.printf("Match was won by team %i (%i-%i).",team_id, this.score_t1,this.score_t2);
                break;

            default:
                System.out.println("Invalid id must be between 0 and 1");
                break;
        }
    }

    @Override
    public synchronized void refereeLog(RefState state) {
        // START_OF_THE_MATCH, START_OF_A_GAME, TEAMS_READY, WAIT_FOR_TRIAL_CONCLUSION, END_OF_A_GAME, END_OF_A_MATCH
        switch (state){
            case START_OF_THE_MATCH:
                this.referee_state = refStates.SOM;
                break;
            case START_OF_A_GAME:
                this.referee_state = refStates.SOG;
                break;
            case TEAMS_READY:
                this.referee_state = refStates.TSR;
                break;
            case WAIT_FOR_TRIAL_CONCLUSION:
                this.referee_state = refStates.WTC;
                break;
            case END_OF_A_GAME:
                this.referee_state = refStates.EOG;
                break;
            case END_OF_A_MATCH:
                this.referee_state = refStates.EOM;
                break;
        }

        printStates();
        writeToFile();
//        if(state== RefState.END_OF_A_GAME){
//            writeToFile(true);
//        }
    }

    @Override
    public synchronized void contestantLog(int id, int team_id, ContestantState state) {
        //    SEAT_AT_THE_BENCH, STAND_IN_POSITION, DO_YOUR_BEST, START
        switch (state){
            case SEAT_AT_THE_BENCH:
                if(team_id == 1){
                    this.team1_state[id] = contestantStates.SAB;
                }else if (team_id == 2){
                    this.team2_state[id] = contestantStates.SAB;
                }
                break;
            case STAND_IN_POSITION:
                if(team_id == 1){
                    this.team1_state[id] = contestantStates.SIP;
                }else if (team_id == 2){
                    this.team2_state[id] = contestantStates.SIP;
                }
                break;
            case DO_YOUR_BEST:
                if(team_id == 1){
                    this.team1_state[id] = contestantStates.DYB;
                }else if (team_id == 2){
                    this.team2_state[id] = contestantStates.DYB;
                }
                break;
        }
        printStates();
        writeToFile();
    }


    public synchronized void printStates(){
        // Ref Coa 1 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5 Coa 2 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5 Trial
        // Sta Stat Sta SG Sta SG Sta SG Sta SG Sta SG Stat Sta SG Sta SG Sta SG Sta SG Sta SG 3 2 1 . 1 2 3 NB PS

        TO_WRITE += String.format("%s %s %s ## %s ## %s ## %s ## %s ## %s %s ## %s ## %s ## %s ## %s ## - - - . - - - -- --\n",
                referee_state,
                coach_state[0],
                team1_state[0],
                team1_state[1],
                team1_state[2],
                team1_state[3],
                team1_state[4],
                coach_state[1],
                team2_state[0],
                team2_state[1],
                team2_state[2],
                team2_state[3],
                team2_state[4]);
    }

    public synchronized void writeToFile(){
        //use buffering

        try {
            output = new BufferedWriter(new FileWriter(OUTPUT_FILE,true));
            //FileWriter always assumes default encoding is OK!
            output.write(TO_WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) try {
                output.close();
                TO_WRITE="";
            } catch (IOException ignore) {}
        }
    }

    public void deleteFile()
    {
        try {
            Files.delete(OUTPUT_FILE.toPath());
        } catch (NoSuchFileException x) {
            System.err.format("%s: no such" + " file or directory%n", OUTPUT_FILE.toPath());
        } catch (DirectoryNotEmptyException x) {
            System.err.format("%s not empty%n", OUTPUT_FILE.toPath());
        } catch (IOException x) {
            // File permission problems are caught here.
            System.err.println(x);
        }
    }


}
