package roast.beef;

import io.spring.guides.gs_producing_web_service.GetCountryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 * Created by jakob on 3/27/15.
 */
@Endpoint
public class ItemEndpoint {
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

    private ItemRepository itemRepository;

    @Autowired
    public ItemEndpoint(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getItemRequest")
    @ResponsePayload
    public GetItemResponse getItem(@RequestPayload GetItemRequest request) {
        GetItemResponse response = new GetCountryResponse();
        response.setItem(itemRepository.findItem(request.getName()));

        return response;
    }
}
