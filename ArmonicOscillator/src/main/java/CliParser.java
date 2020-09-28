import org.apache.commons.cli.*;

public class CliParser {

    public static String staticFile;
    public static String dynamicFile;
    public static String outputDirectory;


    public static int base, exp;
    public static Algorithm algorithm;


    private static Options createOptions(){
        Options options = new Options();
        options.addOption("h", "help", false, "Shows help description.");
        options.addOption("b", "base", true, "Base of dt.");
        options.addOption("e", "exp", true, "Exp of dt.");
        options.addOption("a", "algorithm", true, "algorithm to use:  GEAR, BEEMAN, VERLET");
        options.addOption("od", "output_directory", true, "Path to output directory.");

        return options;
    }

    public static void parseOptions(String[] args){
        Options options = createOptions();
        CommandLineParser parser = new DefaultParser();

        try{
            CommandLine cmd = parser.parse(options, args);
            if(cmd.hasOption("h")){
                help(options);
            }

            if (!cmd.hasOption("a")){
                System.out.println("Algorithm is required");
                System.exit(1);
            }
            algorithm = Algorithm.valueOf(cmd.getOptionValue("a"));


            if (!cmd.hasOption("od")){
                System.out.println("Output directory is required");
                System.exit(1);
            }
            outputDirectory = cmd.getOptionValue("od");

            if (!cmd.hasOption("base")){
                System.out.println("base of dt is required");
                System.exit(1);
            }

            base = Integer.parseInt(cmd.getOptionValue("base"));

            if (!cmd.hasOption("exp")){
                System.out.println("exponent of dt is required");
                System.exit(1);
            }

            exp = Integer.parseInt(cmd.getOptionValue("exp"));




        } catch (Exception e){
            e.printStackTrace();
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

