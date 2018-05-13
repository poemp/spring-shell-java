package org.poem.shell;

import org.apache.commons.cli.*;

public class CommandParser {

    private Options searchOpts = new Options();
    private CommandLine cl = null;

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        CommandParser processer = new CommandParser();
        processer.run(new String[]{"select",
                "-h"
        });
        processer.validte();
    }

    /**
     * Instantiates a new search command line processer.
     */
    public CommandParser() {
        String desc = "Specify the directory where search start";
        Option optStartDir = OptionBuilder.withDescription(desc).isRequired(false)
                .hasArgs().withArgName("START_DIRECTORY").create('d');
        searchOpts.addOption(optStartDir);
    }

    /**
     * Set rule for command line parser, run parsing process
     *
     * @param args the args
     */
    private void run(String[] args) {
        setDate();
        setDateRange();
        setPrefix();
        setSuffix();
        setSize();
        setSizeRange();
        setHelp();
        test();
        runProcess(searchOpts, args, new PosixParser());
    }

    public void test(){
        Option o = OptionBuilder.withArgName("args")
                .withLongOpt("files")
                .hasArgs(2)
                .withValueSeparator(',')
                .withDescription("file names")
                .create("f");
        searchOpts.addOption(o);
    }
    public void setDate() {
        String desc = "Specify the file create date time";
        Option optDate = OptionBuilder.withDescription(desc).isRequired(false)
                .hasArgs().withArgName("FILE_CREATE_DATE").withLongOpt("date")
                .create('D');
        searchOpts.addOption(optDate);
    }

    public void setDateRange() {
        StringBuffer desc = new StringBuffer(
                "Specify acceptance date range for cutoff date specify by option -d");
        desc.append("if true, older files (at or before the cutoff)");
        desc.append("are accepted, else newer ones (after the cutoff)");
        Option optDateRange = null;
        optDateRange = OptionBuilder.withDescription(desc.toString())
                .isRequired(false).hasArg().withArgName("DATE_RANGE")
                .create('r');
        searchOpts.addOption(optDateRange);
    }

    public void setPrefix() {
        String desc = "Specify the prefix of file, multiple prefixes can be split by comma";
        Option optPrefix = OptionBuilder.withDescription(desc)
                .isRequired(false).hasArgs().withArgName("FILE_PREFIXES")
                .create('p');
        searchOpts.addOption(optPrefix);
    }

    public void setSuffix() {
        String desc = "Specify the suffix of file, multiple suffixes can be split by comma";
        Option optSuffix = OptionBuilder.withDescription(desc)
                .isRequired(false).hasArgs().withArgName("FILE_SUFFIXES")
                .create('s');
        searchOpts.addOption(optSuffix);
    }

    /**
     * Sets the size.
     */
    public void setSize() {
        String desc = "Spcify the file size";
        Option optSize = OptionBuilder.withDescription(desc).isRequired(false)
                .hasArg().withArgName("FILE_SIZE_WITH_LONG_VALUE").withLongOpt(
                        "file-size").create('S');
        searchOpts.addOption(optSize);
    }

    public void setSizeRange() {
        StringBuffer desc = new StringBuffer(
                "Specify acceptance size threshold for file specify by option -S");
        desc.append("if true, files equal to or larger are accepted,");
        desc.append("otherwise smaller ones (but not equal to)");
        Option optDateRange = null;
        optDateRange = OptionBuilder.withDescription(desc.toString())
                .isRequired(false).hasArg().withArgName("SIZE_THRESHOLD")
                .create('l');
        searchOpts.addOption(optDateRange);
    }

    public void setHelp() {
        String desc = "Print help message and all options information";
        Option optHelp = OptionBuilder.withDescription(desc).isRequired(false)
                .create('h');
        searchOpts.addOption(optHelp);
    }

    public void runProcess(Options opts, String[] args, CommandLineParser parser) {
        try {
            cl = process(searchOpts, args, parser);
        } catch (ParseException e) {
            System.out.println("Error on compile/parse command: "
                    + e.getMessage());
            printHelp(opts);
            System.exit(-1);
        }
        Option[] allOpts = cl.getOptions();
        Option opt = null;
        for (int i = 0; i < allOpts.length; i++) {
            opt = allOpts[i];
            if ("h".equals(opt.getOpt())) {
                printHelp(opts);
                System.exit(0);
            }
            System.out.println("Option name: -" + opt.getOpt()
                    + ", and value = " + getOptValues(opt.getOpt(), ","));
        }
    }

    public CommandLine process(Options options, String[] args,
                               CommandLineParser parser) throws ParseException {
        return parser.parse(options, args);
    }

    private void validte() {

        // Validate directory option
        String directory = getOptValue("d");
        if (directory == null) {
            System.out.println("Missing start directory, ignore and exit");
            System.exit(-1);
        }
        // Validate date option
        String date = (getOptValue("D") == null) ? getOptValue("date")
                : getOptValue("D");
        String dateRange = getOptValue("r");
        if (date != null && (dateRange == null)) {
            System.out.println("Missing option -D/--date, exit immediately");
            System.exit(-1);
        } else if (date == null && (dateRange != null)) {
            System.out.println("Date not specified, ignore option -r");
        }
        // Validate size option
        String size = (getOptValue("S") == null) ? getOptValue("file-size")
                : getOptValue("S");
        String sizeRange = getOptValue("l");
        if (size != null && (sizeRange == null)) {
            System.out.println("Missing option -S/--file-size, exit immediately");
            System.exit(-1);
        } else if (size == null && (sizeRange != null)) {
            System.out.println("File size not specified, ignore option -l");
        }
    }

    public void printHelp(Options options) {
        String formatstr = "java example.io.SearchCommandLineProcesser [-h][-d][-D/--date<-r>][-p][-s] [-S/--size<-l>]";
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(formatstr, options);
    }

    public String getOptValue(String opt) {
        return (cl != null) ? cl.getOptionValue(opt) : "";
    }

    public String[] getOptValues(String opt) {
        return (cl != null) ? cl.getOptionValues(opt) : new String[]{""};
    }

    public String getOptValues(String opt, String valueSeparater) {
        String[] values = getOptValues(opt);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < values.length; i++) {
            sb.append(values[i]).append(valueSeparater);
        }
        return sb.subSequence(0, sb.length() - 1).toString();
    }
}
