package utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

public class ConnectionInstance {

    private static Connection connection;


    public static synchronized Connection getConnection(Configuration configuration){
        if(connection == null || connection.isClosed()){
            try {
                connection = ConnectionFactory.createConnection(configuration);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return connection;
    }

}
