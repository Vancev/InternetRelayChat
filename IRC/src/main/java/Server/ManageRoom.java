package Server;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ManageRoom
{
    int roomIndex;
    int maxRoom = 50;
    Room[] room = new Room[maxRoom];
    public String create(String roomName, String username, PrintWriter printWriter)
    {
        for (roomIndex = 0; roomIndex < maxRoom; roomIndex++)
        {
            if(room[roomIndex] == null)
            {
                break;
            }
            if(room[roomIndex].roomName.equalsIgnoreCase(roomName))
            {
                return("Room name is taken, try again.");
            }
        }
        room[roomIndex] = new Room(roomName, username, printWriter);
        String output = room[roomIndex].Create();
        return output;
    }

    public String showRooms()
    {
        List<String> rooms = new ArrayList<String>();
        for (roomIndex = 0; roomIndex < maxRoom; roomIndex++)
        {
            if (room[roomIndex] != null)
            {
                rooms.add(room[roomIndex].roomName);
            }
        }
        if (rooms.isEmpty())
        {
            return "No rooms have been created";
        }
        return rooms.toString();
    }

    public String joinRoom(String roomName, String username, PrintWriter printWriter)
    {
        for (roomIndex = 0; roomIndex < maxRoom; roomIndex++)
        {
            if (room[roomIndex] == null)
            {
                return ("Room does not exist. Try again, or create the room.");
            }
            if(room[roomIndex].roomName.equalsIgnoreCase(roomName))
            {
                if (room[roomIndex].members.contains(username))
                {
                    return ("You are already in this room.");
                }
                room[roomIndex].members.add(username);
                room[roomIndex].printWriter.add(printWriter);
                return("You have been added to the room!");
            }
        }
        return ("Room does not exist. Try again, or create the room.");
    }

    public String leaveRoom(String roomName, String username, PrintWriter printWriter)
    {
        for (roomIndex = 0; roomIndex < maxRoom; roomIndex++)
        {
            if (room[roomIndex] == null)
            {
                return ("Room does not exist. Try again, or create the room.");
            }
            if(room[roomIndex].roomName.equalsIgnoreCase(roomName))
            {
                if (room[roomIndex].members.contains(username))
                {
                    room[roomIndex].members.remove(username);
                    room[roomIndex].printWriter.remove(printWriter);
                    return ("You have been removed.");
                }
                return("You are not in this room!");
            }
        }
        return ("Room does not exist. Try again");
    }
    public void leaveAll(String username, PrintWriter printWriter)
    {
        for (roomIndex = 0; roomIndex < maxRoom; roomIndex++)
        {
            if (room[roomIndex] == null)
                break;
            if (room[roomIndex].members.contains(username) && room[roomIndex].printWriter.contains(printWriter))
            {
                room[roomIndex].members.remove(username);
                room[roomIndex].printWriter.remove(printWriter);
            }
        }
    }

    public String showRoomUsers(String roomName)
    {
        for (roomIndex = 0; roomIndex < maxRoom; roomIndex++)
        {
            if (room[roomIndex] == null)
            {
                return ("Room does not exist");
            }
            if(room[roomIndex].roomName.equalsIgnoreCase(roomName))
            {
                return (room[roomIndex].members.toString());

            }
        }
        return ("No rooms have been created");
    }
    public String sendRoom(String roomName, String message, PrintWriter printWriter)
    {
        for (roomIndex = 0; roomIndex < maxRoom; roomIndex++)
        {
            if (room[roomIndex] == null)
            {
                return ("Room does not exist. Try again.");
            }
            if(room[roomIndex].roomName.equalsIgnoreCase(roomName))
            {
                if (room[roomIndex].printWriter.contains(printWriter) == false)
                {
                    return ("You are not in this room. You must first join to send a message.");
                }
                int size =room[roomIndex].printWriter.size();
                for (int i=0; i < size; i++)
                {
                    if (room[roomIndex].printWriter.get(i) != printWriter)
                    room[roomIndex].printWriter.get(i).println("Message from room " + roomName + ": " + message);
                    room[roomIndex].printWriter.get(i).flush();
                }
                return ("Message sent!");
            }
        }
        return ("Room does not exist. Try again, or create the room.");
    }
    public String sendUser(String userToSend, String userSending, String message, Map<String, PrintWriter> writersMap, boolean encrypted)
    {
        PrintWriter printer;
        if (writersMap.containsKey(userToSend))
        {
            printer = writersMap.get(userToSend);
        }
        else
            return ("User not found.");
        if(encrypted)
        {
            printer.println("Secure private message from user " + userSending + ": " + message);
            printer.flush();
        }
        else
        {
            printer.println("Private message from user " + userSending + ": " + message);
            printer.flush();
        }
        return ("Message sent.");
    }

}
