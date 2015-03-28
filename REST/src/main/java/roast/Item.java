package roast;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * -- DESCRIPTION --
 *
 * @author Andreas Willinger
 * @version 0.1
 * @since 27.03.2015 10:41
 */

@NamedQueries({
        @NamedQuery(name = "getItemById", query = "FROM Item i WHERE i.id = :id")
})
@Entity
public class Item
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @NotNull
    private String title;
    @NotNull
    private String content;
    @NotNull
    private Date timestamp;

    public Item() {}

    public Item(Integer id, String title, String content, Date timestamp)
    {
        this.id = id;
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }
}