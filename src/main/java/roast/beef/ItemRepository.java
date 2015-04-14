package roast.beef;

import io.roast.Item;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metamodel.relational.Datatype;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

/**
 * This class handles the actual querying of the data and is called by the ItemEndpoint.
 *
 * @author Andreas Willinger
 * @version 1.0
 */
@Component
// required for transactions to work
@Transactional
public class ItemRepository
{
    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Queries the database for entries whose title or content contain the supplied String.
     * Then returns all of them, their timestamps converted to a XML Calendar, as a List.
     * @param query The keyword to query for.
     * @return A List of XML items, can be empty if no such entries exist.
     */
    public List<Item> findItems(String query)
    {
        Assert.notNull(query);
        // this is going to hold the response items
        List<Item> items = new LinkedList<Item>();

        Session session = this.sessionFactory.getCurrentSession();

        // here, we want the actual hibernate entity
        Criteria criteria = session.createCriteria(roast.beef.Item.class);
        criteria.add(Restrictions.or(
            Restrictions.like("title", "%"+query+"%"),
            Restrictions.like("content", "%"+query+"%")
        ));

        // send the query and cache all results
        List<roast.beef.Item> result = criteria.list();

        // this will allow us to convert java.util.Date
        GregorianCalendar c = new GregorianCalendar();

        // the converted date
        XMLGregorianCalendar date_converted = null;

        for(roast.beef.Item source:result)
        {
            Item item = new Item();
            item.setId(source.getId());
            item.setTitle(source.getTitle());
            item.setContent(source.getContent());

            // set the time retrieved from the database
            c.setTime(source.getTimestamp());

            // convert it into a XML-compatible date
            try
            {
                date_converted = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
            }
            catch(DatatypeConfigurationException ex)
            {
                date_converted = null;
            }

            item.setTimestamp(date_converted);
            items.add(item);
        }

        // and finally send the converted items back
        return items;
    }
}
