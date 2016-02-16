package chatserverca1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pagh
 */
public class ClientHandler extends Thread {

    private Socket s;
    private Server ser;
    private boolean pendingUserName;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner scan;
    private String userName;

    public ClientHandler(Socket s, Server ser) throws IOException {
        this.s = s;
        this.ser = ser;
        pendingUserName = true;
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new PrintWriter(s.getOutputStream(), true);
    }
    
    public void killThisClient()
    {
        ser.removeUser(userName, this);
        ser.sendUserList();
        try
        {
            s.close();   
        }
        catch (IOException ex)
        {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Kunne ikke lukke clientHandlers socket");
        }
    }

    public void sendUserList(String list) {
        out.println(list);
    }

    public void chat() {

        String firstPartOfLine = "";
        String middlePartOfLine = "";
        String lastPartOfLine = "";
        try
        {
            String incommingMsg = in.readLine();
            scan = new Scanner(incommingMsg);
            scan.useDelimiter("#");
            while (scan.hasNext())
            {
                if (!firstPartOfLine.equals("LOGOUT"))
                {
                    middlePartOfLine = scan.next();
                    lastPartOfLine = scan.next();
                }
                switch (firstPartOfLine)
                {
                    case "LOGOUT":
                        killThisClient();
                        break;
                    case "SEND":
                        System.out.println("JEG ER I MSG I SWITCH");
                        //TODO, lav metode til send i server
                        ser.sendMessage(lastPartOfLine, INSERT_NAME_OF_SEND_METHOD_FROM_SERVER(middlePartOfLine));
                        break;
                }
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.err.println("der er knas i chat function");

    }

    public void run() {
        try
        {

            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);
            while (pendingUserName)
            {
                String input = in.readLine();
                scan = new Scanner(input);
                scan.useDelimiter("#");
                while (scan.hasNext())
                {
                    String a = scan.next();

                    if (a.equals("USER"))
                    {
                        userName = scan.next();
                        ser.addUser(userName, this);
                        pendingUserName = false;
                    }
                }
            }
        }
        catch (IOException e)
        {
        }
        ser.sendUserList();
        if (!s.isClosed())
        {
            while (true)
            {
                //Her skal den generelle chat() metode kaldes!
            }
        }
    }
}
