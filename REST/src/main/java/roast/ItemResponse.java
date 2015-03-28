package roast;

/**
 * -- DESCRIPTION --
 *
 * @author Andreas Willinger
 * @version 0.1
 * @since 27.03.2015 10:52
 */
public class ItemResponse
{
    private boolean success;
    private String error;

    private Item item;

    public ItemResponse(boolean success, String error, Item item)
    {
        this.success = success;
        this.error = error;
        this.item = item;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public String getError()
    {
        return error;
    }

    public void setError(String error)
    {
        this.error = error;
    }

    public Item getItem()
    {
        return item;
    }

    public void setItem(Item item)
    {
        this.item = item;
    }
}
