package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class ServerMain
{

    public static void main(String[] args) throws IOException
    {
        ManageRoom manageRoom = new ManageRoom();
        HashSet<String> userNames = new HashSet<String>();
        Map<PrintWriter, String> writersMap = new HashMap<PrintWriter, String>();
        final Scanner scanner = new Scanner(System.in);

        Socket socket;
        ServerSocket server = null;
        try
        {
            server = new ServerSocket(8080);
        } catch (Exception e)
        {
            System.out.println("Could not connect to the client\n");
            e.printStackTrace();
        }
        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                if (scanner.nextLine().equalsIgnoreCase("quit"))
                {
                    System.exit(0);
                }
            }
        };

        Timer timer = new Timer();
        try
        {
            while (true)
            {
                socket = server.accept();
                System.out.println("connection Established");
                try
                {
                    timer.schedule(task, 1);
                } catch (Exception e)
                {
                    //catch exception thrown when process terminates
                }
                ServerThread serverThread = new ServerThread(socket, manageRoom, userNames, writersMap);
                serverThread.start();
            }
        } catch (Exception e)
        {
            System.out.println("connection error: " + e);
        } finally
        {
            server.close();
        }
    }
}