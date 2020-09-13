import org.apache.commons.cli.*;

public class CliParserStep {

    public static String dynamicFile;
    public static String outputDirectory;


    public static double L;
    public static int N;

    public static double W;
    public static double g;
    public static double dt;


    private static Options createOptions(){
        Options options = new Options();
        options.addOption("df", "dynamic_file", true, "Dynamic file with volatile information");
        options.addOption("od", "output_directory", true, "Path to output directory.");
        options.addOption("dt", "deltat", true, "delta t step");

        options.addOption("W", "width", true, "width");
        options.addOption("L", "height", true, "height");
        options.addOption("g", "groove", true, "groove openening");

        return options;
    }

    public static void parse(String[] args){
        Options options = createOptions();
        CommandLineParser parser = new DefaultParser();

        try{
            CommandLine cmd = parser.parse(options, args);

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

            if (!cmd.hasOption("df")){
                System.out.println("df is required");
                System.exit(1);
            }
            dynamicFile = cmd.getOptionValue("df");

            if (!cmd.hasOption("W")){
                System.out.println("W is required");
                System.exit(1);
            }

            W = Double.parseDouble(cmd.getOptionValue("W"));

            if (!cmd.hasOption("g")){
                System.out.println("g is required");
                System.exit(1);
            }

            g = Double.parseDouble(cmd.getOptionValue("g"));

            if (!cmd.hasOption("L")){
                System.out.println("L is required");
                System.exit(1);
            }

            L = Double.parseDouble(cmd.getOptionValue("L"));



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

