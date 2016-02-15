package chatserverca1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author pagh
 */
public class ClientHandler extends Thread
{

    private Socket s;
    private Server ser;
    private boolean pendingUserName;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner scan;
    private String userName;

    public ClientHandler(Socket s, Server ser)
    {
        this.s = s;
        this.ser = ser;
        pendingUserName = true;
    }

    public void sendUserList(String list)
    {
        out.println(list);
    }

    public void run()
    {
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

        } catch (IOException e)
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
