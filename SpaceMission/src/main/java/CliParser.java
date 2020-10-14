import org.apache.commons.cli.*;

public class CliParser {

    public static String staticFile;
    public static String dynamicFile;
    public static String outputDirectory;


    public static double dt;
    public static double dt2;


    private static Options createOptions(){
        Options options = new Options();
        options.addOption("h", "help", false, "Shows help description.");
        options.addOption("dt", "dt", true, "dt.");
        options.addOption("dt2", "dt2", true, "print dt.");
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


            if (!cmd.hasOption("od")){
                System.out.println("Output directory is required");
                System.exit(1);
            }
            outputDirectory = cmd.getOptionValue("od");

            if (!cmd.hasOption("dt")){
                System.out.println("dt is required");
                System.exit(1);
            }

            dt = Double.parseDouble(cmd.getOptionValue("dt"));

            if (!cmd.hasOption("dt2")){
                System.out.println("dt is required");
                System.exit(1);
            }

            dt2 = Double.parseDouble(cmd.getOptionValue("dt2"));

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

