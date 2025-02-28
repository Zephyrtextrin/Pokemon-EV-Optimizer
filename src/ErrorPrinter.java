import java.io.IOException;
import java.util.*;

public class ErrorPrinter {
    private static final Map<ERROR_CODE,Error> errorDB = new HashMap<>();
    private static String additionalDetails;

    public enum ERROR_CODE{
        //UI-related
        ABN_UI_MALFORMED_IMAGE_FILE,
        ERR_UI_UNKNOWN_COMPONENT,

        //calc-related
        ERR_CC_DAMAGE_CALCULATION_ERROR,
        ERR_CC_STAT_CALCULATION_ERROR,

        //database-related
        //all these correspond to calling something that does not exist. theyre all references too :3
        ABN_DB_MISSINGNO, //unknown pokemon. the ref here is obvious
        ABN_DB_BIRDTYPE, //unknown type. the bird-type was an unused type in gen 1
        ABN_DB_TERUSAMA, //unknown item. TERU-SAMA is a placeholder item in gen 2 only available via glitch
        ABN_DB_CACOPHONY, //unknown ability. Cacophony is an unused ability in gen 3 with the same effect as Soundproof
        ABN_DB_STAT_DNE, //attempting to call a base stat of a pokemon and the base stat doesnt exist
        ABN_DB_TYPE_DNE, //attempting to call a type of a pokemon and the base stat doesnt exist

        //other
        ABSTRUSE
    }
    public static void handler(ERROR_CODE code, Exception e){
        System.out.println("------------------------------------------------------------------------------------------------");
        Error error = errorDB.get(code);
        if(error==null){
            error = errorDB.get(ERROR_CODE.ABSTRUSE);
            error.details = "[ATTEMPTED-CALL]: "+code+"\n"+additionalDetails;
        }

        //refreshes err details
        init.updateValues();

        //first portion of error printing system: prints out details of what happened
        //ERROR is for actual game-impeding issues; ABNORMALITY is for unintended things of lower destructiveness/priority. (honestly most of these are fringe cases that never happen ever but its good to have a handler)
        headerBuilder(error.isError, true,"");

        System.out.println(error.cause); //explains what happened and a likely explanation for why
        System.out.println("[ERROR-CODE]: "+error.code);

        //second part: prints out relevant variable values, usually for my own debugging use
        if(error.details!=null) {
            headerBuilder(error.isError, false, "DETAILS");
            System.out.println(error.details);
        }

        //additional details, usually aimed at end-users
        if(error.additional!=null){
            headerBuilder(error.isError, false,"ADDITIONAL-DETAILS");
            System.out.println(error.additional);
        }
        if(error.code==ERROR_CODE.ERR_UI_UNKNOWN_COMPONENT){EVCalculatorUI.printAllComponentNames();}

        System.out.println("------------------------------------------------------------------------------------------------");

        if(error.isError){System.out.println("\n[a message from alex regarding errors]\n-this is automatically appended to all errors-\nso there's actually two types of issues in the error handler i wrote: abnormalities and errors\nabnormaities are just unintended issues i should probably fix\nand errors are active issues that completely impede the functioning of the program\nso it's really important you report errors to me\nthanks bro\n-alexander");}

        boolean stackYN = false;
        if(error.isError){stackYN = getYN("Would you like to print a stack-trace? (if you're reading this, and you arent me, you should probably say yes.");}
            if(stackYN&&e!=null){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }

    private static void headerBuilder(boolean isError, boolean isMainHeader, String message){
        String header = "[[--%s--]]";
        if(isMainHeader){
            if(isError){
                header = "[[[[----ERROR!----]]]]";
            }else{
                header = "[[[---ABNORMALITY---]]]";}
        }else if(isError){
            header ="\n[[[---%s---]]]";}

        System.out.printf("\n"+ header +"\n",message);
    }

    public ErrorPrinter(){new init();}

    public static void setDetails(String errorDetails, boolean append){
        if(append){additionalDetails += errorDetails;
        }else{additionalDetails=errorDetails;}
    }


    public static boolean getYN(final String message){
        //init var
        Scanner scan = new Scanner(System.in);
        String input;
        System.out.println(message+" [Y/N]");
        final String[] options = {"y","n","yes","no"};
        boolean output = false;

        do{
            input = scan.nextLine().toLowerCase(); //casts input to lower case to account for case differences

            if(Arrays.asList(options).contains(input)) {
                if(input.equalsIgnoreCase("y")||input.equalsIgnoreCase("yes")){output = true;}
                break;

                //casts options to arraylist because printing regular arrays isnt readable only lists are
            }else{System.out.println("Sorry, you've entered an invalid input. Please try again. | VALID INPUTS: "+Arrays.asList(options));}

        }while(true);

        return output;
    }

    public static void init(){init.initialize();}

    private static class init{
        static final String genericError = "Unfortunately, this is a very generic error which can be applied to just about anything and if the stacktrace isn't useful there's just about nothing I can actually do about it :/\nbut for the love of god do print the stack trace if you're prompted for it";
        //some of these errors have values that need updates so they are initialized in the updateValues method instead which is why some have placeholder details
        private static void initialize(){

            //UI-related
            new Error(ERROR_CODE.ABN_UI_MALFORMED_IMAGE_FILE, false, "The specified image does not exist!","[SPECIFIED IMAGE]: ", "All Paldea Pokemon do not have a sprite.\nSprites for all regional/Mega forms exist, but the filenames and the dex numbers have to be manually changed to align with each-other and omg its so much effort I don't fucking care");
            new Error(ERROR_CODE.ERR_UI_UNKNOWN_COMPONENT, true, "The specified component does not exist!","[SPECIFIED COMPONENT]: ","Begin printing all Component names.\n");

            //calc related
            new Error(ERROR_CODE.ERR_CC_DAMAGE_CALCULATION_ERROR, true, "An error occurred during the damage calculation process!",null,genericError);
            new Error(ERROR_CODE.ERR_CC_STAT_CALCULATION_ERROR, true, "An error occurred during the stat calculation process!",null,genericError);

            //database related
            new Error(ERROR_CODE.ABN_DB_MISSINGNO, false, "The requested Pokemon does not exist!","[SPECIFIED POKEMON]: ",null);
            new Error(ERROR_CODE.ABN_DB_TERUSAMA, false, "The requested item does not exist!","[SPECIFIED ITEM]: ",null);
            new Error(ERROR_CODE.ABN_DB_BIRDTYPE, false, "The requested Type does not exist!","[SPECIFIED TYPE]: ","Be aware this error is *specifically* for referring to a type that is not in the type-database.\nAttempting to call a type of a Pokemon and getting an error (due to the Pokemon's type being invalid or null) is a different error with a different message.");
            new Error(ERROR_CODE.ABN_DB_CACOPHONY, false, "The requested Ability does not exist!","[SPECIFIED ABILITY]: ",null);
            new Error(ERROR_CODE.ABN_DB_TYPE_DNE, false, "The requested Pokemon's Type does not exist!","[REQUESTED POKEMON]: ","Be aware this error is *specifically* for Pokemon with an incorrectly formatted or otherwise malformed Type in their database.\nAttempting to call a Type that is not in the database at all is a different error with a different message.");
            new Error(ERROR_CODE.ABN_DB_STAT_DNE, false, "The requested Pokemon's Base Stat does not exist!","{placeholder. this shouldnt be seen. additionalDetails should pass requested pokemon and requested stat}",null);

            //unique
            new Error(ERROR_CODE.ABSTRUSE, true, "This is a fallback error: Something called the ErrorPrinter class, but the error-code specified is malformed or does not exist. ", null, "It's very likely I just made a typo somewhere, or forgot to add any details to an error.");
        }

        private init(){initialize();}

        //refreshes values for any error that requires a variable
        private static void updateValues(){
            //do not use += for this shit fix rest later idc
            errorDB.get(ERROR_CODE.ABN_UI_MALFORMED_IMAGE_FILE).details = "[SPECIFIED FILE]: "+additionalDetails;
            errorDB.get(ERROR_CODE.ERR_UI_UNKNOWN_COMPONENT).details = "[SPECIFIED COMPONENT]: "+ additionalDetails;
            errorDB.get(ERROR_CODE.ABN_DB_MISSINGNO).details += additionalDetails;
            errorDB.get(ERROR_CODE.ABN_DB_BIRDTYPE).details += additionalDetails;
            errorDB.get(ERROR_CODE.ABN_DB_TERUSAMA).details += additionalDetails;
            errorDB.get(ERROR_CODE.ABN_DB_CACOPHONY).details += additionalDetails;
            errorDB.get(ERROR_CODE.ABN_DB_TYPE_DNE).details += additionalDetails;
            errorDB.get(ERROR_CODE.ABN_DB_STAT_DNE).details += additionalDetails;
        }
    }

    private static class Error {
        ERROR_CODE code;
        String cause;
        boolean isError;
        String details;
        String additional;

        private Error(ERROR_CODE code, boolean isError, String cause, String details, String additional) {
            this.code = code;
            this.cause = cause;
            this.details = details;
            this.isError = isError;
            this.additional = additional;

            errorDB.put(code, this);
        }
    }
}

