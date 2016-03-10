/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author fitok
 */

public class BackgroundJob implements ServletContextListener{
    private ScheduledExecutorService scheduler;

   @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("INITIALIZED");
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new TradeJob(), 0, 1, TimeUnit.SECONDS);
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
     scheduler.shutdownNow();
    }

}

