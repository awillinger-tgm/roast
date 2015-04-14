package roast.beef;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * This class configures the application to use the hibernate.cfg.xml configuring Hibernate.
 *
 * @author Andreas Willinger
 * @version 1.0
 */
@Configuration
@EnableTransactionManagement
@ImportResource({ "hibernate.cfg.xml" })
public class HibernateXMLConfig
{
}