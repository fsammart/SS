import org.apache.commons.cli.*;

public class CliParser {

    public static String staticFile;
    public static String dynamicFile;
    public static String outputDirectory;


    public static double L;
    public static int N;

    public static boolean random;
    public static double eta;
    public static int time;


    private static Options createOptions(){
        Options options = new Options();
        options.addOption("h", "help", false, "Shows help description.");
        options.addOption("sf", "static_file", true, "Static file with topology details");
        options.addOption("df", "dynamic_file", true, "Dynamic file with volatile information");
        options.addOption("od", "output_directory", true, "Path to output directory.");

        options.addOption("r", "random", false, "Generate random particles");
        options.addOption("L",  true, "Length of side");
        options.addOption("N",  true, "Number of Nodes");
        options.addOption("eta",  true, "Eta for noise");
        options.addOption("t",  true, "Time to run");

        return options;
    }

    public static void parse(String[] args){
        Options options = createOptions();
        CommandLineParser parser = new DefaultParser();

        try{
            CommandLine cmd = parser.parse(options, args);
            if(cmd.hasOption("h")){
                help(options);
            }

            if (!cmd.hasOption("eta")){
                System.out.println("Eta parameter is required");
                System.exit(1);
            }
            eta = Double.parseDouble(cmd.getOptionValue("eta"));

            if (!cmd.hasOption("od")){
                System.out.println("Output directory is required");
                System.exit(1);
            }
            outputDirectory = cmd.getOptionValue("od");

            if (!cmd.hasOption("t")){
                System.out.println("Time is required");
                System.exit(1);
            }

            time = Integer.parseInt(cmd.getOptionValue("t"));


            if(cmd.hasOption("r")){
                // we are generating random particles
                random = true;
                if(cmd.hasOption("L") && cmd.hasOption("N")){
                    L = Double.parseDouble(cmd.getOptionValue("L"));
                    N = Integer.parseInt(cmd.getOptionValue("N"));
                }else{
                    System.out.println("L and N are required");
                    System.exit(1);
                }

            } else {
                random = false;
                if (!cmd.hasOption("sf")){
                    System.out.println("Static File is required");
                    System.exit(1);
                }
                staticFile = cmd.getOptionValue("sf");

                if(cmd.hasOption("df")) {
                    dynamicFile = cmd.getOptionValue("df");
                    FileParser.parseFiles(staticFile, dynamicFile);
                    L = FileParser.L;
                    N = FileParser.N;
                }else{

                }

            }

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

