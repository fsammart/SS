/*
import org.apache.commons.cli.*;

public class CliParser {

    public static String staticFile;
    public static String dynamicFile;
    public static String outputFile;
    static int matrixSize;
    static double interactionRadius;
    static boolean periodicContour = false;
    static boolean bruteForce = false;

    private static Options createOptions(){
        Options options = new Options();
        options.addOption("h", "help", false, "Shows help description.");
        options.addOption("m","matrix", true, "Size of the squared matrix.");
        options.addOption("rc", "radius", true, "Radius of interaction between particles.");
        options.addOption("sf", "static_file", true, "Path to the file with the static values.");
        options.addOption("df", "dynamic_file", true, "Path to the file with the dynamic values.");
        options.addOption("pc", "periodic_contour", false, "Enables periodic contour conditions.");
        options.addOption("bf", "brute_force", false, "Uses brute force instead of CIM.");
        options.addOption("o", "output", true, "Path to output file.");
        return options;
    }

    public static void parseOptions(String[] args){
        Options options = createOptions();
        CommandLineParser parser = new BasicParser();

        try{
            CommandLine cmd = parser.parse(options, args);
            if(cmd.hasOption("h")){
                help(options);
            }

            if(cmd.hasOption("bf")){
                bruteForce = true;
            }

            if (!cmd.hasOption("sf")){
                System.out.println("You must specify a static file!");
                System.exit(1);
            }

            if (!cmd.hasOption("o")){
                System.out.println("You must specify an output file!");
                System.exit(1);
            }

            if(cmd.hasOption("df"))
                dynamicFile = cmd.getOptionValue("df");

            staticFile = cmd.getOptionValue("sf");
            outputFile = cmd.getOptionValue("o");

            if (cmd.hasOption("m")){
                matrixSize = Integer.parseInt(cmd.getOptionValue("m"));
            }
            if (cmd.hasOption("rc")){
                interactionRadius = Double.parseDouble(cmd.getOptionValue("rc"));
            }
            if (cmd.hasOption("pc")){
                periodicContour = true;
            }

        } catch (Exception e){
            System.out.println("Command not recognized.");
            help(options);
        }
    }

    private static void help(Options options){
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("Main", options);
        System.exit(0);
    }
}

 */
