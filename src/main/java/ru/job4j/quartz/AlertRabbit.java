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

    private static Connection getConn() throws ClassNotFoundException, SQLException {
        Properties pr = properties();
        Class.forName(pr.getProperty("driver-class-name"));
        String user = pr.getProperty("username");
        String pass = pr.getProperty("password");
        String url = pr.getProperty("url");
        return DriverManager.getConnection(url, user, pass);
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        try {
            Connection conn = getConn();
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("conn", conn);
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(Integer.parseInt(properties().getProperty("rabbit.interval")))
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(Long.parseLong(properties().getProperty("timeSleep")));
            scheduler.shutdown();
        } catch (SchedulerException se) {
            se.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Rabbit implements Job {
        private Connection connection = getConn();

        public Rabbit() throws SQLException, ClassNotFoundException {
        }

        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("Rabbit runs here ...");
            try {
                PreparedStatement preparedStatement = ((Connection) context.getJobDetail().getJobDataMap().get("conn")).prepareStatement("INSERT INTO rabbit(created) VALUES (?)");
                preparedStatement.setDate(1, new java.sql.Date(System.currentTimeMillis()));
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
