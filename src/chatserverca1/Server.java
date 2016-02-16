package chatserverca1;

import chatserverca1.utils.Utils;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/**
 *
 * @author pagh
 */
public class Server
{

    private Socket s;
    private boolean running;
    private String ip;
    private int port;
    private HashMap<String, ClientHandler> users;
    private static final Properties properties = Utils.initProperties("server.properties");

    public Server()
    {
        running = true;
        users = new HashMap();
    }

    public void connect()
    {
        //Lad disse bliver sat fra server.properties filen i project root (se fredagsopgave 12/2)
        ip = properties.getProperty("serverIp");
        port = Integer.parseInt(properties.getProperty("port"));

        try
        {
            ServerSocket ss = new ServerSocket();
            ss.bind(new InetSocketAddress(ip, port));
            while (running)
            {
                s = ss.accept();
                ClientHandler ch = new ClientHandler(s, this);
                new Thread(ch).start();

            }

        } catch (IOException e)
        {
            System.err.println("Der var knas i serverens connection! (IOException)");
        }

    }

    public void addUser(String userName, ClientHandler ch)
    {
        users.put(userName, ch);

    }

    public void sendUserList()
    {
        String msg = "USERS#";

        for (String name : users.keySet())
        {
            msg = msg + name + ",";
        }
        for (ClientHandler ch : users.values())
        {
            ch.sendUserList(msg);
        }
    }

    public void removeUser(String userName, ClientHandler ch)
    {
        users.remove(userName, ch);

    }

    public static void main(String[] args)
    {

    }

}
