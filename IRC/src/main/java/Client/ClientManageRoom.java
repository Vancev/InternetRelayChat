package Client;

import java.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ClientManageRoom extends ClientMain
{
    public String clientToServer(String name, BufferedReader br, PrintWriter os) throws IOException
    {
        StringBuilder builder = new StringBuilder();
        try
        {
            if (br.ready())
            {
                do
                {
                    String message = br.readLine();
                    if (message.startsWith("Secure"))
                    {
                        String userMessage = message.substring(message.indexOf(":") + 2);
                        String decrypted = decrypt(userMessage);
                        System.out.println(message.substring(0, message.indexOf(":") + 1) + " " + decrypted + "\n");
                    } else
                    {
                        builder.append(message + "\n");
                    }
                } while (br.ready());
                builder.append("\n");
            }

            os.println(name);
            os.flush();
            builder.append(br.readLine() + "\n");
            return builder.toString();
        } catch (Exception e)
        {
            return "Problem with server. " + e.getMessage();
        }
    }
    public String clientToServerSecure(String user, String message, BufferedReader br, PrintWriter os) throws IOException
    {
        StringBuilder builder = new StringBuilder();
        try
        {
            if (br.ready())
            {
                do
                {
                    String serverMessage = br.readLine();
                    if (serverMessage.startsWith("Secure"))
                    {
                        String userMessage = message.substring(message.indexOf(":") + 2);
                        String decrypted = decrypt(userMessage);
                        System.out.println(message.substring(0, message.indexOf(":") + 1) + " " + decrypted + "\n");
                    } else
                    {
                        builder.append(serverMessage + "\n");
                    }
                } while (br.ready());
                builder.append("\n");
            }
            os.println("SECURE " + user + " " + message);
            os.flush();
            builder.append(br.readLine() + "\n");
            return builder.toString();
        } catch (Exception e)
        {
            return "Problem with server. " + e.getMessage();
        }
    }
    public String clientShowRooms(BufferedReader br, PrintWriter os) throws IOException
    {
        StringBuilder builder = new StringBuilder();
        try
        {
            if (br.ready())
            {
                do
                {
                    String message = br.readLine();
                    if (message.startsWith("Secure"))
                    {
                        String userMessage = message.substring(message.indexOf(":") + 2);
                        String decrypted = decrypt(userMessage);
                        System.out.println(message.substring(0, message.indexOf(":") + 1) + " " + decrypted + "\n");
                    } else
                    {
                        builder.append(message + "\n");
                    }
                } while (br.ready());
                builder.append("\n");
            }

            os.println("SHOW ROOMS");
            os.flush();
            builder.append(br.readLine() + "\n");
            return builder.toString();
        } catch (Exception e)
        {
            return "Problem with server. " + e.getMessage();
        }
    }

    public String encrypt(String message)
    {
        String b64encoded = Base64.getEncoder().encodeToString(message.getBytes());

        String reverse = new StringBuffer(b64encoded).reverse().toString();

        StringBuilder tmp = new StringBuilder();
        final int OFFSET = 4;
        for (int i = 0; i < reverse.length(); i++)
        {
            tmp.append((char)(reverse.charAt(i) + OFFSET));
        }
        return tmp.toString();
    }

    public String decrypt(String message)
    {
        StringBuilder tmp = new StringBuilder();
        final int OFFSET = 4;
        for (int i = 0; i < message.length(); i++)
        {
            tmp.append((char)(message.charAt(i) - OFFSET));
        }

        String reversed = new StringBuffer(tmp.toString()).reverse().toString();
        return new String(Base64.getDecoder().decode(reversed));
    }
}

