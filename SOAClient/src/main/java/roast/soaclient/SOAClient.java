package roast.soaclient;

import io.roast.GetItemRequest;
import io.roast.GetItemResponse;
import io.roast.Item;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.util.ClassUtils;
import org.springframework.ws.client.core.WebServiceTemplate;

import java.io.*;

/**
 * A SOA client implementation, querying the iKnow database.
 *
 * @author Andreas Willinger
 * @version 1.0
 */
public class SOAClient
{
    private String address;
    private boolean redirectToFile;
    private String filePath;

    public SOAClient(String address, boolean redirectToFile, String filePath)
    {
        this.address = address;
        this.redirectToFile = redirectToFile;
        this.filePath = filePath;
    }

    /**
     * Reads input from the user and sends/receives the messages to/from the server.
     */
    public void run()
    {
        String input;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        // this translates the itemRequest object into a XML structure
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        // set which packages contain POJOs mit XML annotations
        marshaller.setPackagesToScan(ClassUtils.getPackageName(GetItemRequest.class));
        GetItemRequest request = new GetItemRequest();

        FileOutputStream out = null;

        if(this.redirectToFile)
        {
            try
            {
                File f = new File(this.filePath);

                if(!f.exists()) f.createNewFile();
                out = new FileOutputStream(f);
            }
            catch(IOException ex)
            {
                System.out.println("File not found");
            }
        }

        while(true)
        {
            try
            {
                System.out.println("> Please enter a query or 'quit' to exit the application");
                input = in.readLine();

                if(input.equals("quit"))
                {
                    if(out != null) out.close();
                    break;
                }

                request.setQuery(input);
                // actually send the request
                GetItemResponse response = (GetItemResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(this.address, request);

                // prepare output
                StringBuilder buffer = new StringBuilder("\n\n> getItemResponse - results: "+response.getResponse().size());
                buffer.append("\n--------------------");

                int runner = 0;
                int total = response.getResponse().size();

                for(Item item: response.getResponse())
                {
                    buffer.append("\n-- ID: "+item.getId());
                    buffer.append("\n-- Title: "+item.getTitle());
                    buffer.append("\n-- Content: "+item.getContent());
                    buffer.append("\n-- Timestamp: "+item.getTimestamp());

                    if(runner != (total - 1)) buffer.append("\n=========================");
                    runner++;
                }

                buffer.append("\n--------------------");

                // either direct output or write into a file
                if(!this.redirectToFile)
                {
                    System.out.println(buffer.toString());
                }
                else
                {
                    out.write(buffer.toString().getBytes());
                    out.flush();

                    System.out.println(">> Output written to file "+this.filePath);
                }
            }
            catch(Exception ex)
            {
                break;
            }
        }
    }
}
