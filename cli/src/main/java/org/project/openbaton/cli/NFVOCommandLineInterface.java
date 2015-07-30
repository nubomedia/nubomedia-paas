package org.project.openbaton.cli;

import com.google.gson.Gson;
import jline.console.ConsoleReader;
import jline.console.completer.ArgumentCompleter;
import jline.console.completer.Completer;
import jline.console.completer.FileNameCompleter;
import jline.console.completer.StringsCompleter;
import org.project.openbaton.cli.model.Command;
import org.project.openbaton.sdk.NFVORequestor;
import org.project.openbaton.sdk.api.annotations.Help;
import org.project.openbaton.sdk.api.util.AbstractRestAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by lto on 14/07/15.
 *
 */
public class NFVOCommandLineInterface {

    private static Logger log = LoggerFactory.getLogger(NFVOCommandLineInterface.class);

    private static final Character mask = '*';
    private static final String CONFIGURATION_FILE = "/etc/openbaton/cli.properties";
    private static final String VERSION = "1";

    private final static LinkedHashMap<String, Command> commandList = new LinkedHashMap<>();
    private final static LinkedHashMap<String, String> helpCommandList = new LinkedHashMap<String, String>(){{
        put("help", "Print the usage");
        put("exit", "Exit the application");
        put("print properties", "print all the properties");
    }};

    public static void usage() {
        System.out.println("/~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/");
        System.out.println("Usage: java -jar build/libs/neutrino-<$version>.jar");
        System.out.println("Available commands are");
        for (Object entry : helpCommandList.entrySet()) {
            System.out.println("\t" + ((Map.Entry)entry).getKey() + ":\t" + ((Map.Entry)entry).getValue());
        }
        System.out.println("/~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/");
    }


    public static void main(String[] args) {

        log.info("\n" +
                " _______  _______________   ____________            _________ .____    .___ \n" +
                " \\      \\ \\_   _____/\\   \\ /   /\\_____  \\           \\_   ___ \\|    |   |   |\n" +
                " /   |   \\ |    __)   \\   Y   /  /   |   \\   ______ /    \\  \\/|    |   |   |\n" +
                "/    |    \\|     \\     \\     /  /    |    \\ /_____/ \\     \\___|    |___|   |\n" +
                "\\____|__  /\\___  /      \\___/   \\_______  /          \\______  /_______ \\___|\n" +
                "        \\/     \\/                       \\/                  \\/        \\/    ");
        log.info("Nfvo OpenBaton Command Line Interface");
        log.info("Log class: { " + log.getClass().getName() + " }");

        ConsoleReader reader = getConsoleReader();
        Properties properties = new Properties();

        File file = new File(CONFIGURATION_FILE);

        String line;
        PrintWriter out = new PrintWriter(reader.getOutput());

        if(file.exists()){
            try {
                log.trace("File exists");
                properties.load(new FileInputStream(file));
                log.trace("Properties are: " + properties);
            } catch (IOException e) {
                log.warn("Error reading /etc/openbaton/cli.properties, trying with Environment Variables");
                readEnvVars(properties);
            }
        }else {
            log.warn("File [" + CONFIGURATION_FILE + "] not found, looking for Environment Variables");
            readEnvVars(properties);
        }

        getProperty(reader, properties, "nfvo-usr","");
        getProperty(reader, properties, "nfvo-pwd","");
        getProperty(reader, properties, "nfvo-ip","127.0.0.1");
        getProperty(reader, properties, "nfvo-port","8080");
        getProperty(reader, properties, "nfvo-version",VERSION);

        NFVORequestor nfvo = new NFVORequestor(properties.getProperty("nfvo-usr"), properties.getProperty("nfvo-pwd"), properties.getProperty("nfvo-ip"),properties.getProperty("nfvo-port"),properties.getProperty("nfvo-version"));

        fillCommands(nfvo);

        List<Completer> completors = new LinkedList<Completer>();
        completors.add(new StringsCompleter(helpCommandList.keySet()));
        completors.add(new FileNameCompleter());
//        completors.add(new NullCompleter());

        reader.addCompleter(new ArgumentCompleter(completors));
        reader.setPrompt("\u001B[135m" + properties.get("nfvo-usr") + "@[\u001B[32mopen-baton\u001B[0m]~> ");

        try {
            reader.setPrompt("\u001B[135m" + properties.get("nfvo-usr") + "@[\u001B[32mopen-baton\u001B[0m]~> ");

            while ((line = reader.readLine()) != null) {
                out.flush();
                line = line.trim();
                if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) {
                    exit(0);
                }else
                if (line.equalsIgnoreCase("cls")) {
                    reader.clearScreen();
                }else
                if (line.equalsIgnoreCase("help")) {
                    usage();
                }else
                if (line.equalsIgnoreCase("print properties")) {
                    log.info("" + properties.toString());
                }else
                if (line.equalsIgnoreCase("")) {
                    continue;
                }else
                    try {
                        System.out.println(executeCommand(line));
                    }catch (Exception e){
                        e.printStackTrace();
                        log.error("Error while invoking command");
                    }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static void getProperty(ConsoleReader reader, Properties properties, String property, String defaultProperty) {
        if (properties.get(property) == null){
            log.warn(property+" property was not found neither in the file [" + CONFIGURATION_FILE + "] nor in Environment Variables");
            try {
                String insertedProperty = reader.readLine(property + "[" + defaultProperty + "]: ");
                if (insertedProperty == null || insertedProperty.equals("")){
                    insertedProperty = defaultProperty;
                }
                properties.put(property, insertedProperty);

            } catch (IOException e) {
                log.error("Oops, Error while reading from input");
                exit(990);
            }
        }
    }

    private static Object executeCommand(String line) throws InvocationTargetException, IllegalAccessException, FileNotFoundException {
        StringBuffer sb = new StringBuffer();
        StringTokenizer st = new StringTokenizer(line);
        sb.append(st.nextToken());
        log.debug(sb.toString());
        String commandString = sb.toString();
        Command command = commandList.get(commandString);
        if (command == null){
            return "Command: " + commandString + " not found!";
        }
        log.debug("invoking method: " + command.getMethod().getName() + " with parameters: " + command.getParams());
        List<Object> params = new LinkedList<>();
        for (Type t : command.getParams()){
            log.debug("type is: " + t.getClass().getName());
            if (t.equals(String.class)){ //for instance an id
                params.add(st.nextToken());
            }else {// for instance waiting for an obj so passing a file
                String pathname = st.nextToken();
                log.debug("the path is: " + pathname);
                File f = new File(pathname);
                Gson gson = new Gson();
                FileInputStream fileInputStream = new FileInputStream(f);
                String file = getString(fileInputStream);
                log.debug(file);
                log.debug("waiting for an object of type " + command.getClazz().getName());
                Object casted = command.getClazz().cast(gson.fromJson(file, command.getClazz()));
                log.debug("Parameter added is: " + casted);
                params.add(casted);
            }
        }
        String parameters ="";
        for (Object type : params){
            parameters += type.getClass().getSimpleName() + " ";
        }
        log.debug("invoking method: " + command.getMethod().getName() + " with parameters: " + parameters);
        return command.getMethod().invoke(command.getInstance(), params.toArray());
    }

    private static String  getString(FileInputStream fileInputStream){
        StringBuilder builder = new StringBuilder();
        int ch;
        try {
            while((ch = fileInputStream.read()) != -1){
                builder.append((char)ch);
            }
        } catch (IOException e) {
            e.printStackTrace();
            exit(333);
        }
        return builder.toString();
    }


    private static void fillCommands(NFVORequestor nfvo) {
        getMethods(nfvo.getNetworkServiceRecordAgent());
        getMethods(nfvo.getConfigurationAgent());
        getMethods(nfvo.getImageAgent());
        getMethods(nfvo.getEventAgent());
        getMethods(nfvo.getVNFFGAgent());
        getMethods(nfvo.getVimInstanceAgent());
        getMethods(nfvo.getNetworkServiceDescriptorAgent());
        getMethods(nfvo.getVirtualLinkAgent());

    }

    private static void getMethods(AbstractRestAgent agent) {
        String className = agent.getClass().getSimpleName();
        log.trace(className);
        Class clazz = null;
        try {
            clazz = (Class) agent.getClass().getSuperclass().getDeclaredMethod("getClazz").invoke(agent);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            exit(123);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            exit(123);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            exit(123);
        }
        String replacement = null;
        if (className.endsWith("RestRequest")){
            replacement = className.substring(0, className.indexOf("RestRequest"));
        }
        else if (className.endsWith("RestAgent")){
            replacement = className.substring(0, className.indexOf("RestAgent"));
        }
        else if (className.endsWith("Agent")){
            replacement = className.substring(0, className.indexOf("Agent"));
        }
        else
            exit(700);
        log.debug("Clazz: " + clazz);
        log.debug("Replacement: " + replacement);
        for (Method method : agent.getClass().getSuperclass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Help.class)) {
                log.debug("Method: " + method.getName());
                helpCommandList.put(replacement + "-" + method.getName(), method.getAnnotation(Help.class).help().replace("{#}", replacement));
                Command command = new Command(agent, method, method.getParameterTypes(), clazz);
                commandList.put(replacement + "-" + method.getName(), command);
            }
        }
        for (Method method : agent.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Help.class)) {
                Command command = new Command(agent, method, method.getParameterTypes(), clazz);
                commandList.put(replacement + "-" + method.getName(), command);
                helpCommandList.put(replacement + "-" + method.getName(), method.getAnnotation(Help.class).help());
            }
        }
    }

    private static ConsoleReader getConsoleReader() {
        ConsoleReader reader = null;
        try {
            reader = new ConsoleReader();
        } catch (IOException e) {
            log.error("Oops, Error while creating ConsoleReader");
            exit(999);
        }
        return reader;
    }

    private static void readEnvVars(Properties properties) {
        try {
            properties.put("nfvo-usr", System.getenv().get("nfvo_usr"));
            properties.put("nfvo-pwd", System.getenv().get("nfvo_pwd"));
            properties.put("nfvo-ip", System.getenv().get("nfvo_ip"));
            properties.put("nfvo-port", System.getenv().get("nfvo_port"));
            properties.put("nfvo-version", System.getenv().get("nfvo_version"));
        }catch (NullPointerException e){
            return;
        }
    }

    /**
     * * * EXIT STATUS CODE
     *
     *  @param status:
     *
     * *) 1XX Variable errors
     * *    *) 100: variable not found
     *
     * *) 8XX: reflection Errors
     * *    *) 801 ConsoleReader not able to read
     * *) 9XX: Libraries Errors
     * *    *) 990 ConsoleReader not able to read
     * *    *) 999: ConsoleReader not created
     *
     */
    private static void exit(int status) {
        System.exit(status);
    }
}
