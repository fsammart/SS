import org.apache.commons.cli.*;

public class CliParser {

    public static String staticFile;
    public static String dynamicFile;
    public static String outputDirectory;


    public static double L;
    public static int N;

    public static boolean random;
    public static double m;
    public static double v;
    public static double radius;
    public static double W;
    public static double g;

    public static boolean dynamicOutput;


    private static Options createOptions(){
        Options options = new Options();
        options.addOption("h", "help", false, "Shows help description.");
        options.addOption("sf", "static_file", true, "Static file with topology details");
        options.addOption("df", "dynamic_file", true, "Dynamic file with volatile information");
        options.addOption("od", "output_directory", true, "Path to output directory.");

        options.addOption("r", "random", false, "Generate random particles");
        options.addOption("L",  true, "width");
        options.addOption("W",  true, "height");
        options.addOption("N",  true, "Number of Nodes");
        options.addOption("v",  true, "velocity absolute value");
        options.addOption("m",  true, "particle mass");
        options.addOption("g",  true, "groove size");
        options.addOption("radius",  true, "particle radius");

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


            if (!cmd.hasOption("od")){
                System.out.println("Output directory is required");
                System.exit(1);
            }
            outputDirectory = cmd.getOptionValue("od");


            if(cmd.hasOption("r")){
                // we are generating random particles
                random = true;
                if(cmd.hasOption("L") && cmd.hasOption("N")){
                    L = Double.parseDouble(cmd.getOptionValue("L"));
                    W = Double.parseDouble(cmd.getOptionValue("W"));
                    N = Integer.parseInt(cmd.getOptionValue("N"));
                    m = Double.parseDouble(cmd.getOptionValue("m"));
                    radius = Double.parseDouble(cmd.getOptionValue("radius"));
                    v = Double.parseDouble(cmd.getOptionValue("v"));
                    g = Double.parseDouble(cmd.getOptionValue("g"));
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
                    W = FileParser.W;
                    N = FileParser.N;
                    m = FileParser.m;
                    v = FileParser.v;
                    g = FileParser.g;
                    radius = FileParser.radius;
                }else{
                    System.out.println("Dynamic File is required");
                    System.exit(1);
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

