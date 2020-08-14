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

    public static boolean random;

    public static String BF = "BF";
    public static String CIM = "CIM";

    public static double rMin;
    public static double rMax;

    private static Options createOptions(){
        Options options = new Options();
        options.addOption("h", "help", false, "Shows help description.");
        options.addOption("sf", "static_file", true, "Static file with topology details");
        options.addOption("df", "dynamic_file", true, "Dynamic file with volatile information");
        options.addOption("o", "output", true, "Path to output file.");
        options.addOption("ir", "interaction_radius", true, "Interaction Radius");
        options.addOption("pc", "periodic_contour", false, "Enables periodic contour, connected edged");
        options.addOption("s", "strategy", true, "Choose Strategy: BF or CIM");
        options.addOption("M",  true, "Size of the squared matrix.");

        options.addOption("r", "random", false, "Generate random particles");
        options.addOption("L",  true, "Length of side");
        options.addOption("N",  true, "Number of Nodes");
        options.addOption("rmin", "min_radius", true, "Minimum radius for randomize");
        options.addOption("rmax", "max_radius", true, "Maximum radius for randomize");


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

            if(!cmd.hasOption("M")){
                System.out.println("Matrix size required");
                System.exit(1);
            }

            M = Integer.parseInt(cmd.getOptionValue("M"));

            if (!cmd.hasOption("o")){
                System.out.println("Output file is required");
                System.exit(1);
            }
            outputFile = cmd.getOptionValue("o");

            if (cmd.hasOption("ir")){
                interactionRadius = Double.parseDouble(cmd.getOptionValue("ir"));
            } else {
                // get interaction radius from property
                // TODO: get interaction radius from propery
            }

            if (cmd.hasOption("pc")){
                periodicContour = true;
            }

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
                if(cmd.hasOption("rmin") && cmd.hasOption("rmax")){
                    rMin = Double.parseDouble(cmd.getOptionValue("rmin"));
                    rMax = Double.parseDouble(cmd.getOptionValue("rmax"));
                    FileParser.minRadius = rMin;
                }else{
                    System.out.println("rmin and rmax are required");
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

