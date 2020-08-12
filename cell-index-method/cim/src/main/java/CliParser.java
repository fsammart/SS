import org.apache.commons.cli.*;

public class CliParser {

    public static String staticFile;
    public static String dynamicFile;
    public static String outputFile;

    public static String strategy;

    public static boolean periodicContour;
    public static int M;
    /* L needs to be readed from input file */
    public static double L;
    public static int N;
    public static double interactionRadius;

    public static String BF = "BF";
    public static String CIM = "CIM";

    private static Options createOptions(){
        Options options = new Options();
        options.addOption("h", "help", false, "Shows help description.");
        options.addOption("sf", "static_file", true, "Static file with topology details");
        options.addOption("df", "dynamic_file", true, "Dynamic file with volatile information");
        options.addOption("o", "output", true, "Path to output file.");
        options.addOption("ir", "interaction_radius", true, "Interaction Radius");
        options.addOption("pc", "periodic_contour", false, "Enables periodic contour, connected edged");
        options.addOption("s", "strategy", false, "Choose Strategy: BF or CIM");
        options.addOption("M",  true, "Size of the squared matrix.");
        options.addOption("L",  true, "Length of side");
        options.addOption("N",  true, "Number of Nodes");


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

            strategy = BF;
            if(cmd.hasOption("s")){
                strategy = cmd.getOptionValue("s");
            }

            if (!cmd.hasOption("sf")){
                System.out.println("Static File is required");
                System.exit(1);
            }

            if (!cmd.hasOption("o")){
                System.out.println("Output file is required");
                System.exit(1);
            }


            if(cmd.hasOption("df")) {
                dynamicFile = cmd.getOptionValue("df");
            }else{
                if(cmd.hasOption("L")){
                   L = Double.parseDouble(cmd.getOptionValue("L"));
                   M = Integer.parseInt(cmd.getOptionValue("M"));
                   N = Integer.parseInt(cmd.getOptionValue("N"));

                }else{
                    System.out.println("Dynamic File or L are required");
                    System.exit(1);
                }
            }

            staticFile = cmd.getOptionValue("sf");
            outputFile = cmd.getOptionValue("o");


            if (cmd.hasOption("ir")){
                interactionRadius = Double.parseDouble(cmd.getOptionValue("ir"));
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

