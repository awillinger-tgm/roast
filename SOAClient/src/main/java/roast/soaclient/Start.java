package roast.soaclient;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * -- DESCRIPTION --
 *
 * @author Andreas Willinger
 * @version 0.1
 * @since 16.04.2015 13:30
 */
public class Start
{
    public static void main(String[] args)
    {
        String buffer;
        String address;
        boolean redirectToFile = false;
        String fileName = "";
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("\n\nWelcome to SOAClient v.1.0\nThis application allows you to send queries to the iKnow database.\n\n");
        System.out.println("Please enter the IP address, port and 'file' of the SOA server\nExample: http://localhost:8080/searchItem\n> ");

        while(true)
        {
            try
            {
                buffer = in.readLine();

                Pattern p = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
                Matcher m = p.matcher(buffer);

                if(!m.matches())
                {
                    System.out.println("\nInvalid URL specified");
                    System.out.println("\n> ");
                    continue;
                }

                address = buffer;

                System.out.println("\nDo you want to redirect all itemResponses to a file? [yes/no]\n> ");

                buffer = in.readLine().trim();

                if(buffer.indexOf("yes") == -1 && buffer.indexOf("no") == -1)
                {
                    System.out.println("\nInvalid answer specified");
                    System.out.println("\n> ");
                    continue;
                }

                redirectToFile = (buffer.equals("yes"));

                if(redirectToFile)
                {
                    System.out.println("\nPlease specify a file name\n> ");
                    buffer = in.readLine();

                    fileName = buffer;
                }

                break;
            }
            catch(IOException ex)
            {
                System.out.println("Something went wrong: "+ex.getMessage());
            }
        }
        SOAClient client = new SOAClient(address, redirectToFile, fileName);
        client.run();
    }
}
