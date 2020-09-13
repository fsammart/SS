public class CliParser {

    static String dynamicFile;
    static double dt;
    static String outputFile;
    static String ovitoFiles;

    private static Options createOptions(){
        Options options = new Options();

        options.addOption("h", "help", false, "Shows this screen.");
        options.addOption("df", "dynamic_file", true, "Path to the file with the dynamic values.");
        options.addOption("of", "ovito_file", true, "Path to the ovito file.");
        options.addOption("dt", "dt", true, "Dt to print to file.");
        options.addOption("o", "output", true, "Output file.");

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

            if(!cmd.hasOption("df"))
                throw new Exception("You must specify a dynamic file.");

            dynamicFile = cmd.getOptionValue("df");

            if(!cmd.hasOption("o"))
                throw new Exception("You must specify an output file.");

            outputFile = cmd.getOptionValue("o");

            if (!cmd.hasOption("dt"))
                throw new Exception("You must specify a dt.");

            dt = Double.parseDouble(cmd.getOptionValue("dt"));

            if (cmd.hasOption("of"))
                ovitoFiles = cmd.getOptionValue("of");

        } catch (Exception e){
            System.out.println("Command not recognized. " + e.getMessage());
            help(options);
        }
    }

    private static void help(Options options){
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("ar.edu.itba.ss.OffLatticeEngine", options);
        System.exit(0);
    }
}