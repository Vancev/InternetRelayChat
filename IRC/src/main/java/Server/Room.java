package Server;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Room
{
    String roomName = null;
    List<String> members = new ArrayList<String>();
    List<PrintWriter> printWriter = new ArrayList<PrintWriter>();

    public Room(String roomName, String members, PrintWriter printWriter)
    {
        this.roomName = roomName;
        this.members.add(members);
        this.printWriter.add(printWriter);
    }

    public String Create()
    {
        return ("Room " + roomName + " created! " + "Users: " + members);
    }
}
