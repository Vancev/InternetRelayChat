package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

class ServerThread extends Thread
{
    ManageRoom manageRoom;
    String line = null;
    String username = null;
    HashSet<String> userNames = new HashSet<String>();
    Map<String, PrintWriter> writersMap = new HashMap<String, PrintWriter>();
    boolean hasUsername = false;
    BufferedReader bufferedReader = null;
    PrintWriter printWriter = null;
    Socket socket = null;

    public ServerThread(Socket socket, ManageRoom manageRoom, HashSet userNames, Map writers)
    {
        this.socket = socket;
        this.manageRoom = manageRoom;
        this.userNames = userNames;
        this.writersMap = writers;
    }

    public void run()
    {
        try
        {
            bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream());
        } catch(IOException e)
        {
            System.out.println("IO Error");
            e.printStackTrace();
        }

        try
        {
            do
            {
                line = bufferedReader.readLine();
                if (line.startsWith("USERNAME"))
                {
                    username = line.split("\\s+")[1];
                    if (userNames.contains(username))
                    {
                        printWriter.println("NAMETAKEN");
                        printWriter.flush();
                        hasUsername = false;
                    }
                    else
                    {
                        userNames.add(username);
                        writersMap.put(username, printWriter);
                        printWriter.println("Welcome " + username);
                        printWriter.flush();
                        hasUsername = true;
                        System.out.println("User connected as: " + username);
                    }
                }

                //If client wishes to create a room
                if (line.toUpperCase().startsWith("CREATE"))
                {
                    String roomName = line.split("\\s+")[1];
                    String output = manageRoom.create(roomName, username, printWriter);
                    printWriter.println(output);
                    printWriter.flush();
                }
                if(line.equalsIgnoreCase("SHOW ROOMS"))
                {
                    String output = manageRoom.showRooms();
                    printWriter.println(output);
                    printWriter.flush();
                }
                if (line.toUpperCase().startsWith("JOIN"))
                {
                    String roomName = line.split("\\s+")[1];
                    String output = manageRoom.joinRoom(roomName, username, printWriter);
                    printWriter.println(output);
                    printWriter.flush();
                }
                if (line.toUpperCase().startsWith("LEAVE"))
                {
                    String roomName = line.split("\\s+")[1];
                    String output = manageRoom.leaveRoom(roomName, username, printWriter);
                    printWriter.println(output);
                    printWriter.flush();
                }
                if(line.toUpperCase().startsWith("USERS"))
                {
                    String roomName = line.split("\\s+")[1];
                    String output = manageRoom.showRoomUsers(roomName);
                    printWriter.println(output);
                    printWriter.flush();
                }
                if(line.toUpperCase().startsWith("SENDROOM"))
                {
                    String roomName = line.split("\\s+")[1];
                    String message = line.substring(line.indexOf(" ")+1);
                    message = message.substring(message.indexOf(' ')+1);
                    String output = manageRoom.sendRoom(roomName, message, printWriter);
                    printWriter.println(output);
                    printWriter.flush();
                }
                if(line.toUpperCase().startsWith("SENDUSER"))
                {
                    String userName = line.split("\\s+")[1];
                    String message = line.substring(line.indexOf(" ")+1);
                    message = message.substring(message.indexOf(' ')+1);
                    String output = manageRoom.sendUser(userName, username, message, writersMap, false);
                    printWriter.println(output);
                    printWriter.flush();
                }
                if(line.toUpperCase().startsWith("SECURE"))
                {
                    String userName = line.split("\\s+")[1];
                    String message = line.substring(line.indexOf(" ")+1);
                    message = message.substring(message.indexOf(' ')+1);
                    String output = manageRoom.sendUser(userName, username, message, writersMap, true);
                    printWriter.println(output);
                    printWriter.flush();
                }

            }while(line.equalsIgnoreCase("quit") == false || hasUsername == false);
        }catch(IOException e)
        {
            System.out.println("Client has crashed.");
            return;
        }
        finally
        {
            try
            {
                System.out.println("Connection with " + username + " Closing..");
                if (bufferedReader!=null){
                    bufferedReader.close();
                }
                if(printWriter!=null){
                    printWriter.flush();
                    printWriter.close();
                }
                if (socket!=null){
                    socket.close();
                }
                if(userNames != null)
                {
                    userNames.remove(username);
                }
                manageRoom.leaveAll(username, printWriter);
                System.out.println("Connection closed successfully");
                return;
            }
            catch(IOException ie){
                System.out.println("Socket Close Error");
            }
        }
    }
}