package roast.beef;

import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

/**
 * This class takes care of configuring the web service, as in, registering the end point and providing the WSDL.
 *
 * @author Andreas Willinger
 * @version 1.0
 */
@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter
{
    /**
     * Registers the web service itself.
     *
     * @param applicationContext The application context. Autowired by Spring.
     * @return A ServletRegistrationBean
     */
    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext)
    {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/searchItem/*");
    }

    /**
     * Registers an URL which provides the compiled WSDL file.
     *
     * @param itemSchema The item schema to return once the WSDL file is accessed. Autowired by Spring.
     * @return A DefaultWsdl11Definition containing the schema in WSDL format.
     */
    @Bean(name = "item")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema itemSchema)
    {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        // the following three will directly be added to the WSDL, the XSD is not modified
        wsdl11Definition.setPortTypeName("ItemPort");
        wsdl11Definition.setLocationUri("/searchItem");
        wsdl11Definition.setTargetNamespace("http://roast.io");
        wsdl11Definition.setSchema(itemSchema);
        return wsdl11Definition;
    }

    /**
     * Creates a simple XSD schema based on our item.xsd file
     *
     * @return A XsdSchema instance based on the item.xsd file.
     */
    @Bean
    public XsdSchema itemSchema() {
        return new SimpleXsdSchema(new ClassPathResource("item.xsd"));
    }
}
