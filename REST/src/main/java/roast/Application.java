package roast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

/**
 * -- DESCRIPTION --
 *
 * @author Andreas Willinger
 * @version 0.1
 * @since 27.03.2015 10:48
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args)
    {
        final File f = new File(Application.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        SpringApplication.run(Application.class, args);
    }
}
