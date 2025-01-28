import java.util.*;

public class InputHelper {
    //do you think it would be better to initialize a public static final scanner up here so you dont have to initialize the scanner in every method or am i crazy

    //[[[---INTEGERS---]]]

    //checksum for valid int input | any values are accepted, as long as it's an int.
    public static int getInt(final String message){
        Scanner scan = new Scanner(System.in);
        int input;
        System.out.println(message);

        do{
            if(scan.hasNextInt()){
                input = scan.nextInt();
                break;

            }else{System.out.println("Invalid input. Try again.");}

            scan.nextLine();
        }while(true);

        return input;
    }

    //checksum for valid, positive int input, (excl zero)
    public static int getPositiveInt(final String message){
        Scanner scan = new Scanner(System.in);
        int input;
        System.out.println(message);

        do{
            if(scan.hasNextInt()) {
                input = scan.nextInt();
                if (input>0) {break; //when input meets reqs

                }else{System.out.println("Please enter a positive, non-zero integer value.");} //if input is an int, but is 0 or lower

            }else{System.out.println("Invalid input. Try again.");} //if input is not int

            scan.nextLine();
        }while(true);

        return input;
    }

    //checksum for valid, positive int input within range
    public static int getRangedInt(final String message, final int MIN, final int MAX){
        Scanner scan = new Scanner(System.in);
        int input;
        final String bounds = " [MIN: "+MIN+" MAX: "+MAX+"]"; //is appended to the end of any statements when the user enters invalid nums. example: "Invalid Input. Try again. [MIN: 0 MAX: 300]
        System.out.println(message+bounds);


        do{
            if(scan.hasNextInt()) {
                input = scan.nextInt();
                if (input>=MIN&&input<=MAX) {break; //when input meets reqs

                }else{System.out.println("Please enter a positive, non-zero integer value."+bounds);} //if input is an int, but is 0 or lower

            }else{System.out.println("Invalid input. Try again."+bounds);} //if input is not int

            scan.nextLine();
        }while(true);

        return input;
    }



    //[[[---DOUBLES---]]]

    //checksum for valid double input | any values are accepted, as long as it's an double.
    public static double getDouble(final String message){
        Scanner scan = new Scanner(System.in);
        double input;
        System.out.println(message);

        do{
            if(scan.hasNextDouble()){
                input = scan.nextDouble();
                break;

            }else{System.out.println("Invalid input. Try again.");}

            scan.nextLine();
        }while(true);

        return input;
    }

    //checksum for valid, positive double input, (excl zero)
    public static double getPositiveDouble(final String message){
        Scanner scan = new Scanner(System.in);
        double input;
        System.out.println(message);

        do{
            if(scan.hasNextDouble()) {
                input = scan.nextDouble();
                if (input>0) {break; //when input meets reqs

                }else{System.out.println("Please enter a positive, non-zero decimal value.");} //if input is an double, but is 0 or lower

            }else{System.out.println("Invalid input. Try again.");} //if input is not double

            scan.nextLine();
        }while(true);

        return input;
    }

    //checksum for valid, positive double input within range
    public static double getRangedDouble(final String message, final double MIN, final double MAX){
        Scanner scan = new Scanner(System.in);
        double input;
        System.out.println(message);
        final String bounds = " [MIN: "+MIN+" MAX: "+MAX+"]"; //is appended to the end of any statements when the user enters invalid nums. example: "Invalid Input. Try again. [MIN: 0 MAX: 300]

        do{
            if(scan.hasNextDouble()) {
                input = scan.nextDouble();
                if (input>=MIN&&input<=MAX) {break; //when input meets reqs

                }else{System.out.println("Please enter a positive, non-zero decimal value."+bounds);} //if input is an double, but is 0 or lower

            }else{System.out.println("Invalid input. Try again."+bounds);} //if input is not double

            scan.nextLine();
        }while(true);

        return input;
    }



    //[[[---STRINGS---]]]

    //gets a string that is not null or a length of 0
    public static String getString(final String message){
        Scanner scan = new Scanner(System.in);
        String input;
        System.out.println(message);

        //tbh idk the difference between isblank and isempty so i did both
        do {
            input = scan.nextLine();
            if(!(input.isBlank() || input.isEmpty())){break;

            }else{System.out.println("Sorry, you've entered an invalid response, try again. Please enter a valid, non-zero string.");}

            scan.nextLine();
        }while (true);

        return input;
    }

    //checksum for a nonzero-length string that is in a REGULAR NON-LIST ARRAY
    //so if u have an array that's like [tomato, beans, carrots] you have to type in beans tomato or carrots
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
                if(input.equalsIgnoreCase("y")||input.equalsIgnoreCase("yes")){
                    output = true;
                }
                break;

                //casts options to arraylist because printing regular arrays isnt readable only lists are
            }else{System.out.println("Sorry, you've entered an invalid input. Please try again. | VALID INPUTS: "+Arrays.asList(options));}

        }while(true);

        return output;
    }

    //takes inputs until it matches specified regex pattern
    public static String getRegEx(final String message, final String regExPattern){
        Scanner scan = new Scanner(System.in);
        String input;

        do {input = scan.nextLine();}while (!input.matches(regExPattern));
        return input;
    }

    //[[[---EXTRAS---]]]
    //stuff that wasnt on the methods list that i still thought would be a good idea to make because im bored

    //checksum for a nonzero-length string that is in an arraylist
    //so if u have an array that's like [tomato, beans, carrots] you have to type in beans tomato or carrots
    public static String getStringInArrayList(final ArrayList<String> options, final String message){
        //init var
        Scanner scan = new Scanner(System.in);
        String input;
        System.out.println(message);

        do{
            input = scan.nextLine().toLowerCase(); //casts input to lower case to account for case differences

            if(options.contains(input)) {break;

            }else{System.out.println("Sorry, you've entered an invalid input. Please try again. | VALID INPUTS: "+options);}

            scan.nextLine();
        }while(true);

        return input;
    }

    //checksum for a nonzero-length string that is in a REGULAR NON-LIST ARRAY
    //so if u have an array that's like [tomato, beans, carrots] you have to type in beans tomato or carrots
    public static String getStringInArray(final String[] options, final String message){
        //init var
        Scanner scan = new Scanner(System.in);
        String input;
        final List<String> optionsList = Arrays.asList(options);

        System.out.println(message+" | VALID INPUTS: "+optionsList);
        do{
            input = scan.nextLine().toLowerCase(); //casts input to lower case to account for case differences

            if(optionsList.contains(input)) {break;

            }else{System.out.println("Sorry, you've entered an invalid input. Please try again. | VALID INPUTS: "+optionsList);}

            scan.nextLine();
        }while(true);

        return input;
    }
}