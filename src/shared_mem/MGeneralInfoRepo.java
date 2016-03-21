package shared_mem;

import enums.CoachState;
import enums.ContestantState;
import enums.RefState;
import interfaces.IRepoCoach;
import interfaces.IRepoContestant;
import interfaces.IRepoReferee;

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

    private refStates referee_state;
    private coachStates[] coach_state;
    private contestantStates[] team1_state;
    private contestantStates[] team2_state;

    public MGeneralInfoRepo()
    {
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
    }

    private String Addheader(boolean first)
    {
        if(first)
            return "";
        else
            return "";
    }
    @Override
    public void coachLog(int team_id, CoachState state) {
        switch (state){
            case WAIT_FOR_REFEREE_COMMAND:
                this.coach_state[team_id - 1] = coachStates.WRC;
                break;
            case ASSEMBLE_TEAM:
                this.coach_state[team_id - 1] = coachStates.AST;
                break;
            case WATCH_TRIAL:
                this.coach_state[team_id - 1] = coachStates.WTR;
        }

        printStates();

    }

    @Override
    public void refereeLog(RefState state) {
        //Todo-add code
    }

    @Override
    public void contestantLog(int id, int team_id, ContestantState state) {
        //Todo-add code
    }


    public void printStates(){
        // Ref Coa 1 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5 Coa 2 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5 Trial
        // Sta Stat Sta SG Sta SG Sta SG Sta SG Sta SG Stat Sta SG Sta SG Sta SG Sta SG Sta SG 3 2 1 . 1 2 3 NB PS

        System.out.printf("### %s ### ## ### ## ### ## ### ## ### ## %s ### ## ### ## ### ## ### ## ### ## - - - . - - - -- --\n", coach_state[0], coach_state[1]);
    }

}
