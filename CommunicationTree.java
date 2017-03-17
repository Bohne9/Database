import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by jonahschueller on 31.01.17.
 * Die <code>CommunicationTree</code> Klasse stellt das Kommunikationsmodel von Client und Server dar.
 *
 */
public class CommunicationTree implements Runnable{



    /**
     * Festgelegte Zeichen um Key und Byte-Conent zu unterscheiden, bzw. um Byte-Content zu trennen.
     */
    public static final String sepatator = ":", CONTENT = ";";
    /**
     * Referenziert auf den zuletzt angeforderten Key.
     */
    private Content content;
    /**
     * Speichert alle <code>Node</code>.
     */
    private static ArrayList<Node> nodes;
    /**
     * Stellt das Root-<code>Node</code> dar, auf dieses wird referenziert, wenn ein angeforderter Key nicht existiert.
     *
     */
    private Node root;
    /**
     * Refernziert auf das <code>Node</code>, welches durch einen angeforderten Key festgelegt wurde.
     */
    private Node current;

    /**
     * Dieser <code>Thread</code> verarbeitet die <code>Tasks</code>.
     */
    private Thread executer;
    /**
     * Diese <code>Thread</code> ließt Tasks aus der Netzwerkverbundung und speichert die Daten in einem <code>Task</code> ab.
     */
    private Thread[] receiver;

    /**
     * Kontroliert, die Threads.
     */
    private boolean executing;
    /**
     * Referenziert auf den <code>InputStream</code> des <code>Sockets connect</code>.
     */
    private InputStream[] stream;
    /**
     * Referenziert auf den <code>Socket</code> des <code>NetworkingPart sender</code>.
     */
    private Socket[] connect;
    /**
     * Referenziert auf den <code>NetworkingPart</code>, welcher zum empfangen ausgewählt wurde.
     */
    private NetworkingPart[] network;
    /**
     * Stellt eine Multithreadingkompatible<code>ArrayList</code> dar.
     * Sie speichert alle <code>Tasks</code>.
     * Wird wie eine Queue behandelt.
     */
    private CopyOnWriteArrayList<Task> tasks;


    /**
     * Initialisiert die Attribute
     * @param part Instanz der Klasse <code>Sender</code>, mit der Netzwerkverbindung
     */
    public CommunicationTree(NetworkingPart part){
        nodes = new ArrayList<>();
        tasks = new CopyOnWriteArrayList<>();
        root = createNode("root", (bytes)->{});
        toRoot();
        executing = true;
        //Referenz auf die Netzwerkverbindungen
        network = new NetworkingPart[1];
        network[0] = part;
        connect = new Socket[1];
        connect[0] = network[0].getSocket();
        try {
            stream = new InputStream[1];
            stream[0] = connect[0].getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public CommunicationTree(NetworkingPart... part){
        nodes = new ArrayList<>();
        tasks = new CopyOnWriteArrayList<>();
        root = createNode("root", (bytes)->{});
        toRoot();
        executing = true;
        //Referenz auf die Netzwerkverbindung
        network = part;
        connect = new Socket[network.length];
        stream = new InputStream[network.length];
        try {
            for (int i = 0; i < connect.length; i++) {
                connect[i] = network[i].getSocket();
                stream[i] = connect[i].getInputStream();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void reInit(NetworkingPart... part){
        network = part;
        connect = new Socket[network.length];
        stream = new InputStream[network.length];
        try {
            for (int i = 0; i < connect.length; i++) {
                connect[i] = network[i].getSocket();
                stream[i] = connect[i].getInputStream();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        startReceiver();
    }

    /**
     * Gibt das aktuelle <code>Content</code> Interface zurück
     * @return content
     */
    public Content getContent() {
        return content;
    }


    /**
     * Setzt das <code>Content</code> Interface, basierend auf dem String-Parameter command und dem dazu äquivalenten Key eines <code>Node</code>.
     * @param command
     * @throws CommunicationTreeExeption
     */
    public void setup(String command){

        if (command != null){
            for (Node node :
                    nodes) {
                if (node.getReference().equals(command)){
                    current = node;
                    content = current.getContent();
                    return;
                }
            }
        }
        try {
            throw new CommunicationTreeExeption(command);
        } catch (CommunicationTreeExeption communicationTreeExeption) {
            communicationTreeExeption.printStackTrace();
        }
        toRoot();
        content = current.getContent();
    }


    /**
     * Fügt eine neue Instanz der Klasse <code>Node</code> hinzu.
     * @param node
     */
    public void addNode(Node node){
        if (node != null)
            nodes.add(node);
    }


    /**
     * Fügt eine endliche Liste von Instanzen der Klasse <code>Node</code> hinzu.
     * @param nodes
     */
    public void addNode(Node... nodes){
        for (Node node:
             nodes) {
            addNode(node);
        }

    }

    /**
     * Beendet die Threads <code>executer</code> und <code>receiver</code>, nachdem dem nächsten Schleifendurchlauf
     */
    public void stopExecuting(){
        executing = false;
    }


    /**
     * Verarbeitet die übergebenen Bytes, je nach <code>Content</code> Implementation.
     * @param bytes
     */
    public void execute(byte[] bytes){
        content.content(bytes);
    }


    /**
     * Überschreibt die Methode <code>run</code> aus dem Interface <code>Runnable</code>.
     * Diese wird von dem Thread receiver ausgeführt.
     */
    @Override
    public void run() {

        while (executing){

            while (executing){
                if (!tasks.isEmpty()) {
                    Task task = tasks.get(0);
                    tasks.remove(0);
                    setup(task.getCommand());
                    execute(task.getContent());
                }
            }
            System.out.println("Stop Executing");


        }
    }

    /**
     * Startet den Datenempfang von jedem verbundenen Netzwerkteilnehmer.
     */
    private void startReceiver(){
        receiver = new Thread[network.length];
        for (int i = 0; i < receiver.length; i++) {
            final NetworkingPart networks = network[i];
            receiver[i] = new Thread(() -> {
                while (executing){
                    try {
                        String command = readCommand(networks);
                        if (!command.equals("")) {
                            Task task = createTask(networks, command);
                            tasks.add(task);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            receiver[i].start();
        }
    }


    /**
     * Startet das Datenempfangen und die Datenverarbeitung.
     */
    public void start(){
        startReceiver();
        executer = new Thread(this);
        executer.start();
    }

    /**
     * Überprüft, ob der übergebene String ein Command-Key darstellt.
     * @param com
     * @return
     */
    private boolean isCommand(String com){
        for (Node node :
                nodes) {
            if (node.getReference().equals(com)){
                return true;
            }
        }
        return false;
    }


    /**
     * Ließt einen Key aus dem Inputstream, der Netzwerkverbindung.
     * @return
     * @throws IOException
     */
    private String readCommand(NetworkingPart part) throws IOException{

        StringBuilder sb = new StringBuilder();
        InputStream stream = part.getSocket().getInputStream();
        byte i;
        while ((i = (byte) stream.read()) != -1){
            sb.append((char)i);
        }

       return sb.toString();
    }


    /**
     * Ließt den Byte-Content aus dem Inputstream, der Netzwerkverbindung.
     * @return
     * @throws IOException
     */
    private byte[] readContent(NetworkingPart part, int len) throws IOException{
        if (len == 0){
            return null;
        }

        byte[] bytes = new byte[len];
        byte i;
        int index = 0;
        InputStream stream = part.getSocket().getInputStream();
        while ((i = (byte) stream.read()) != -1){
            //bytes.add(i);
            bytes[index] = i;
            index++;
        }

        return bytes;
    }


    /**
     * Erstellt einen neuen <code>Task</code>.
     * @param str
     * @return
     * @throws IOException
     */
    private Task createTask(NetworkingPart part, String str) throws IOException{
        String[] parts = str.split(sepatator);
        String command = parts[0];
        int len = Integer.parseInt(parts[1]);
        byte[] bytes = readContent(part, len);

        return new Task(command, bytes);
    }


    /**
     * Referenziert das Attribut <code>current</code> auf das Attribut <code>root</code>.
     */
    public void toRoot(){
        current = root;
    }


    /**
     * Erstellt aus den Parametern ein neues <code>Node</code> und fügt es der Node-Liste hinzu.
     * @param ref
     * @param con
     * @return
     */
    public static Node createNode(String ref, Content con){
        for (Node node : nodes){
            if (node.getReference().equals(ref)){
                try {
                    throw new KeyAlreadyInUseException(ref);
                } catch (KeyAlreadyInUseException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
        Node node = new Node(ref, con);
        nodes.add(node);
        return node;
    }


    /**
     * Referenziert das Attribut <code>content</code> auf den übergebenen Parameter <code>c</code>.
     * @param c
     */
    private void setContent(Content c){
        content = c;
    }


    /**
     * Implementiert einen Knoten der Klasse <code>CommuicationTree</code>.
     */
    public static class Node{

        private String reference;
        private Content content;

        /**
         * Speichert Key des Nodes und die <code>Conent</code> Implementation.
         * @param ref
         * @param co
         */
        public Node(String ref, Content co){
            reference = ref;
            content = co;
        }

        /**
         * Gibt die <code>Content</code> Implementation zurück.
         * @return
         */
        public Content getContent() {
            return content;
        }


        /**
         * Gibt den Key des <code>Node</code> zurück.
         * @return
         */
        public String getReference() {
            return reference;
        }
    }


    /**
     * Stellt ein Interface bereit, welches für jedes <code>Node</code> erforderlich ist.
     *
     */
    public interface Content{
        /**
         * Implementiert die Verwendung der übergebenen Parameter Bytes.
         * @param data
         */
        void content(byte[] data);
    }


    /**
     * Exception, welche geworfen wird, wenn ein Angeforderter Key nicht existiert.
     */
    private class CommunicationTreeExeption extends Exception{
        public CommunicationTreeExeption(String node) {
            super("Description " + node + " not found!");
        }
    }


    /**
     * Die Klasse Task stellt eine Schnittstelle bereit, welche verwendet wird um Empfangene Daten zu speichern und
     * später zu verarbeiten.
     */
    private class Task{

        /**
         * Schlüsselwort, bzw. Key
         */
        private String command;
        /**
         * Auftags-content
         */
        private byte[] content;


        public Task(String command, byte[] content) {
            this.command = command;
            this.content = content;
        }

        public byte[] getContent() {
            return content;
        }

        public String getCommand() {
            return command;
        }
    }


    /**
     * Trennt die übergebenen Strings mit einem ; , um beim verabeiten der Daten mehere Attribute auseinander halten zu können.
     * @param strings
     * @return
     */
    public static String content(String... strings){
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            string.append(strings[i]);
            if (i < strings.length - 1)
                string.append(CONTENT);
        }
        return string.toString();
    }

    /**
     * Exception, welche geworfen wird, wenn ein <code>Node</code> erstellt, dessen
     * Key bereits existiert.
     */
    private static class KeyAlreadyInUseException extends Exception{
        public KeyAlreadyInUseException(String message) {
            super(message);
        }
    }


}
