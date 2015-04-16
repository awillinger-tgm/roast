package roast.rest;

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
 * @version 1.0
 */
@RestController
@Transactional
@RequestMapping("/items")
public class ItemController
{
    @Autowired
    private SessionFactory sessionFactory;

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    /**
     * Gets a single item from the knowledge base, identified by its unique id.
     *
     * @param id The id of the entry to get.
     * @return An ItemResponse object, with the queried Item in its body
     */
    @RequestMapping(method = RequestMethod.GET)
    public ItemResponse getItem(@RequestParam(value="id", required = true) Integer id)
    {
        Session session = this.sessionFactory.getCurrentSession();

        // setup query and fill in the supplied ID
        Query query = session.getNamedQuery("getItemById").setParameter("id", id);
        // perform query
        List<Item> results = (List<Item>)query.list();

        // either more than, or less than one item was returned
        if(results.size() != 1)
        {
            return new ItemResponse(false, "The specified entry could not be found or too many items were found.", null);
        }

        return new ItemResponse(true, "", results.get(0));
    }

    /**
     * Adds a new item to the knowledge base.
     *
     * @param item The new item to add
     * @return An ItemResponse object, with the new item in its body on success. On failure, an error message.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ItemResponse addItem(@RequestBody Item item)
    {
        // let hibernate handle the id's
        item.setId(null);

        // create a new transaction
        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        try
        {
            session.save(item);

            // temporary
            /*
            for(int i = 0; i < 1000000; i++)
            {
                Item item2 = new Item();
                item2.setId(null);
                item2.setTitle("Knowledge base entry #"+i);
                item2.setContent("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse egestas aliquet suscipit. Sed blandit dui ut metus. ");
                item2.setTimestamp(new Date());

                session.save(item2);
            } */
            // commit
            tx.commit();

            return new ItemResponse(true, "", item);
        }
        catch(HibernateException ex)
        {
            // rollback and send a json message with the "error" field set
            tx.rollback();

            return new ItemResponse(false, String.format("Hibernate error: %s", ex.getMessage()), null);
        }
        finally
        {
            session.close();
        }
    }

    /**
     * Updates an existing item in the knowledge base.
     *
     * @param id The id of the item to update.
     * @param item The 'new' item with changed values.
     * @return An ItemResponse object, containing the updated item on success. On failure, an error message
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ItemResponse updateItem(@RequestParam(value="id", required = true) Integer id, @RequestBody Item item)
    {
        // we specify the id via the url parameters anywayss
        item.setId(id);

        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        try
        {
            // here, update is used
            // save would create a new entry
            session.update(item);
            tx.commit();

            return new ItemResponse(true, "", item);
        }
        catch(HibernateException ex)
        {
            // rollback and send a json message with the "error" field set
            tx.rollback();

            return new ItemResponse(false, String.format("Hibernate error: %s", ex.getMessage()), null);
        }
        finally
        {
            session.close();
        }
    }

    /**
     * Deletes an existing item from the knowledge base.
     *
     * @param id The id of the entry to delete.
     * @return An ItemResponse object with item = null on success. On failure, an error message.
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ItemResponse deleteItem(@RequestParam(value="id", required = true) Integer id)
    {
        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        try
        {
            // we must create an Item object shortly
            // and let hibernate handle the rest
            session.delete(new Item(id, "", "", null));
            tx.commit();

            return new ItemResponse(true, "", null);
        }
        catch(HibernateException | NumberFormatException ex)
        {
            tx.rollback();

            return new ItemResponse(false, String.format("An error occured: %s", ex.getMessage()), null);
        }
        finally
        {
            session.close();
        }
    }
}
