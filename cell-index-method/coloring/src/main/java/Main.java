import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static int particleID;
    private static String staticFile;
    private static String dynamicFile;
    private static String outputFile;
    private static String resultFile;
    private static String inputFile;
    private static List<Integer> ids;

    private static final int COMMON = 0;
    private static final int CHOSEN = 1;
    private static final int NEIGHBOUR = 2;

    public static void main(String[] args) throws FileNotFoundException {
        parseArguments(args);

        FileParser.parseFiles(staticFile,dynamicFile);
        parseResults();
        createXYZFile();
    }

    private static Options createOptions(){
        Options options = new Options();
        options.addOption("h", "help", false, "Help.");
        options.addOption("sf", "static_file", true, "Topography file.");
        options.addOption("df", "dynamic_file", true, "Dynamic file with positions.");
        options.addOption("i", "input_file", true, "Path to the file with the neighbour list.");
        options.addOption("p", "particle", true, "Particle id");
        options.addOption("o", "output", true, "Path to output file.");
        return options;
    }

    private static void parseArguments(String[] args){
        Options options = createOptions();
        CommandLineParser parser = new BasicParser();

        try{
            CommandLine cmd = parser.parse(options, args);
            if(cmd.hasOption("h")){
                help(options);
            }


            if (!cmd.hasOption("sf")){
                System.out.println("Static File is mandatory!");
                System.exit(1);
            }

            if (!cmd.hasOption("o")){
                System.out.println("Output File is Mandatory");
                System.exit(1);
            }

            if (!cmd.hasOption("df")){
                System.out.println("Dynamic File is Mandatory");
                System.exit(1);
            }

            if (!cmd.hasOption("i")){
                System.out.println("Input neighbour file is Mandatory");
                System.exit(1);
            }

            if (!cmd.hasOption("p")){
                System.out.println("You must specify particle id");
                System.exit(1);
            }

            staticFile = cmd.getOptionValue("sf");
            outputFile = cmd.getOptionValue("o");
            dynamicFile = cmd.getOptionValue("df");
            particleID = Integer.parseInt(cmd.getOptionValue("p"));
            inputFile = cmd.getOptionValue("i");

        } catch (Exception e){
            System.out.println("Command not recognized.");
            help(options);
        }
    }

    private static void parseResults() throws FileNotFoundException {
        File file = new File(inputFile);
        Scanner sc = new Scanner(file);
        for (int i = 1; i < particleID; i++){
            sc.nextLine();
        }

        // this is the particle

        String line = sc.nextLine();
        List<String> string_ids = Arrays.asList(line.split(","));
        ids = string_ids.stream().map(Integer::parseInt).collect(Collectors.toList());

    }

    private static void createXYZFile() throws FileNotFoundException {
        System.out.println("The output has been written into a file.");
        final String filename = "./" + outputFile;
        File file = new File(filename);
        FileOutputStream fos = new FileOutputStream(file);
        PrintStream ps = new PrintStream(fos);
        System.setOut(ps);

        // Writing file config
        System.out.println(FileParser.particleList.size());
        System.out.println("ParticleX,ParticleY,ParticleRadius,ParticleType");

        String z = "0";
        FileParser.particleList.forEach( particle -> {
                    int color = COMMON;


                    if(particle.id == particleID){
                        color = CHOSEN;
                    }else if(ids.contains(particle.getId())) {
                        color = NEIGHBOUR;
                    }
                    System.out.println(
                            particle.getX() + "," + particle.getY() + "," + particle.radius + "," + color
                    );
                }
        );
    }
    private static void help(Options options){
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("Main", options);
        System.exit(0);
    }
}
