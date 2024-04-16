package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class AlertRabbit {

    private static Properties properties() {
        Properties props = new Properties();
        try (InputStream in = AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            Properties config = new Properties();
            config.load(in);
            props = config;
        } catch (IOException io) {
            io.printStackTrace();
        }
        return props;
    }

    private static Connection connection;

    public static void main(String[] args) throws ClassNotFoundException {
        Properties pr = properties();
        Class.forName(pr.getProperty("driver-class-name"));
        String user = pr.getProperty("username");
        String pass = pr.getProperty("password");
        String url = pr.getProperty("url");
        try {
            connection = DriverManager.getConnection(url, user, pass);
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("connection", connection);
            JobDetail job = newJob(Rabbit.class).build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(Integer.parseInt(pr.getProperty("rabbit.interval")))
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(Long.parseLong(pr.getProperty("timeSleep")));
            scheduler.shutdown();


            connection.close();
        } catch (SchedulerException | SQLException se) {
            se.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("Rabbit runs here ...");
               try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO rabbit(CREATED) VALUES (?)")) {
                preparedStatement.setDate(1, new Date(1111-11-11));
                if (preparedStatement.executeUpdate() > 0) {
                    System.out.println("Изменения внесены в таблицу.");
                } else {
                    System.out.println("Изменения не были внесены в таблицу.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}
