package roast.beef;

import io.spring.guides.gs_producing_web_service.Country;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;

@Component
public class ItemRepository {

    @PostConstruct
    public void initData() {
        // TODO initialize data source
    }

    public Item findItem(String name) {
        Assert.notNull(name);

        Item result = null;

        // TODO create result

        return result;
    }
}
