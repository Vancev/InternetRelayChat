package Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientMain
{
    public static void main(String[] args) throws IOException
    {
        String username;
        String input;
        Socket socket = null;
        BufferedReader bufferedInput = null;
        PrintWriter printWriter = null;
        ClientManageRoom Room = new ClientManageRoom();

        Scanner scanner = new Scanner(System.in);

        try
        {
            socket = new Socket("localhost", 8080);
            bufferedInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream());
            System.out.println("Just connected to " + socket.getRemoteSocketAddress());
        } catch (Exception e)
        {
            System.out.println("Could not connect. " + e.getMessage());
            System.exit(0);
        }

        try
        {
            boolean inputError = false;
            boolean isUNcorrect = true;
            String status = "FAILURE";
            do
            {
                if (status.equalsIgnoreCase("NAMETAKEN"))
                {
                    System.out.println("Username taken, try again");
                }

                System.out.println("Enter your desired username: \n");
                username = scanner.nextLine();

                if (username.split(" ").length > 1)
                {
                    System.out.println("Usernames can not contain spaces, try again");
                    isUNcorrect = false;
                } else
                {
                    isUNcorrect = true;
                    printWriter.println("USERNAME " + username);
                    printWriter.flush();
                    status = bufferedInput.readLine();
                    if (status.equals("NAMETAKEN") == false)
                    {
                        System.out.println(status);
                    }
                }
            } while (isUNcorrect == false || status.equalsIgnoreCase("NAMETAKEN") == true);

            do
            {
                System.out.println("Enter a command. Type Help for commands\n");
                input = scanner.nextLine();
                if (input.equalsIgnoreCase("messages"))
                {
                    if (bufferedInput.ready())
                    {
                        do
                        {
                            String message = bufferedInput.readLine();
                            if (message.startsWith("Secure"))
                            {
                                String userMessage = message.substring(message.indexOf(":") + 2);
                                String decrypted = Room.decrypt(userMessage);
                                System.out.println(message.substring(0, message.indexOf(":") + 1) + " " + decrypted);
                            } else
                            {
                                System.out.println(message);
                            }
                        } while (bufferedInput.ready());
                    }
                    else
                    {
                        System.out.println("No messages at this time. Try again later.");
                    }
                }
                if (input.toUpperCase().startsWith("CREATE"))
                {
                    if (input.split(" ").length > 2)
                    {
                        System.out.println("Room names can not contain spaces");
                        inputError = true;
                    } else
                    {
                        String roomStatus = Room.clientToServer(input, bufferedInput, printWriter);
                        System.out.println(roomStatus);
                        if (roomStatus.startsWith("Problem with server. "))
                            input = "quit";
                    }
                }
                if (input.equalsIgnoreCase("show rooms"))
                {
                    String rooms = Room.clientShowRooms(bufferedInput, printWriter);
                    System.out.println(rooms);
                    if (rooms.startsWith("Problem with server. "))
                        input = "quit";
                }
                if (input.toUpperCase().startsWith("JOIN"))
                {
                    if (input.split(" ").length > 2)
                    {
                        System.out.println("Room names can not contain spaces");
                        inputError = true;
                    } else
                    {
                        String roomStatus = Room.clientToServer(input, bufferedInput, printWriter);
                        System.out.println(roomStatus);
                        if (roomStatus.startsWith("Problem with server. "))
                            input = "quit";
                    }
                }
                if (input.toUpperCase().startsWith("LEAVE"))
                {
                    if (input.split(" ").length > 2)
                    {
                        System.out.println("Room names can not contain spaces");
                        inputError = true;
                    } else
                    {
                        String roomStatus = Room.clientToServer(input, bufferedInput, printWriter);
                        System.out.println(roomStatus);
                        if (roomStatus.startsWith("Problem with server. "))
                            input = "quit";
                    }
                }
                if (input.toUpperCase().startsWith("USERS"))
                {
                    if (input.split(" ").length > 2)
                    {
                        System.out.println("Room names can not contain spaces");
                        inputError = true;
                    } else
                    {
                        String users = Room.clientToServer(input, bufferedInput, printWriter);
                        System.out.println(users);
                        if (users.startsWith("Problem with server. "))
                            input = "quit";
                    }
                }
                if (input.toUpperCase().startsWith("SENDROOM"))
                {
                    if (input.split(" ").length < 3)
                    {
                        System.out.println("Please include the room name and message to send.");
                        inputError = true;
                    } else
                    {
                        String response = Room.clientToServer(input, bufferedInput, printWriter);
                        System.out.println(response);
                        if (response.startsWith("Problem with server. "))
                            input = "quit";
                    }
                }
                if (input.toUpperCase().startsWith("SENDUSER"))
                {
                    if (input.split(" ").length < 3)
                    {
                        System.out.println("Please include the users name and the message to send.");
                        inputError = true;
                    } else
                    {
                        String response = Room.clientToServer(input, bufferedInput, printWriter);
                        System.out.println(response);
                        if (response.startsWith("Problem with server. "))
                            input = "quit";
                    }
                }
                if (input.toUpperCase().startsWith("SENDSECURE"))
                {
                    if (input.split(" ").length < 3)
                    {
                        System.out.println("Please include the users name and the secure message to send.");
                        inputError = true;
                    } else
                    {
                        String user = input.split("\\s+")[1];
                        String message = input.substring(input.indexOf(' ')+1);
                        message = message.substring(message.indexOf(' ')+1);
                        String encrypted = Room.encrypt(message);
                        String response = Room.clientToServerSecure(user, encrypted, bufferedInput, printWriter);
                        System.out.println(response);
                        if (response.startsWith("Problem with server. "))
                            input = "quit";
                    }
                }


            } while (input.equalsIgnoreCase("help") || inputError == true || input.equalsIgnoreCase("quit") == false);
        } finally
        {
            printWriter.println("QUIT");
            printWriter.flush();
            printWriter.close();
            socket.close();
        }
    }
}