package roast;

import org.hibernate.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

/**
 * The main part of the application.
 * Handles all GET, PUT, DELETE and POST requests to manage the knowledge database.
 *
 * @author Andreas Willinger
 * @version 0.5
 */
@RestController
@Transactional
@RequestMapping("/items")
public class ItemController
{
    @Autowired
    private SessionFactory sessionFactory;

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ItemResponse getItem(@RequestParam(value="id", required = true) Integer id)
    {
        Session session = this.sessionFactory.getCurrentSession();

        Query query = session.getNamedQuery("getItemById").setParameter("id", id);
        List<Item> results = (List<Item>)query.list();

        logger.error("Result size: "+results.size());

        if(results.size() != 1)
        {
            return new ItemResponse(false, "The specified entry could not be found or too many items were found.", null);
        }

        return new ItemResponse(true, "", results.get(0));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ItemResponse addItem(@RequestBody Item item)
    {
        item.setId(null);

        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        try
        {
            session.save(item);
            tx.commit();

            return new ItemResponse(true, "", item);
        }
        catch(HibernateException ex)
        {
            tx.rollback();

            return new ItemResponse(false, String.format("Hibernate error: %s", ex.getMessage()), null);
        }
        finally
        {
            session.close();
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ItemResponse updateItem(@RequestParam(value="id", required = true) Integer id, @RequestBody Item item)
    {
        item.setId(id);

        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        try
        {
            session.update(item);
            tx.commit();

            return new ItemResponse(true, "", item);
        }
        catch(HibernateException ex)
        {
            tx.rollback();

            return new ItemResponse(false, String.format("Hibernate error: %s", ex.getMessage()), null);
        }
        finally
        {
            session.close();
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ItemResponse deleteItem(@RequestParam(value="id", required = true) Integer id)
    {
        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        try
        {
            session.delete(new Item(id, "", "", null));
            tx.commit();

            return new ItemResponse(true, "", null);
        }
        catch(HibernateException ex)
        {
            tx.rollback();

            return new ItemResponse(false, String.format("Hibernate error: %s", ex.getMessage()), null);
        }
        finally
        {
            session.close();
        }
    }
}
