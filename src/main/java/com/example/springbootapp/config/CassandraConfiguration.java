package com.example.springbootapp.config;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.util.List;

@Configuration
public class CassandraConfiguration {
    @Value("${cassandra.host}")
    private String cassandraHost;

    @Value("${cassandra.port}")
    private int cassandraPort;

    @Value("${cassandra.cluster.pooling.minThread}")
    private int minThread;

    @Value("${cassandra.cluster.pooling.maxThread}")
    private int maxThread;

    @Value("${cassandra.cluster.pooling.timeout}")
    private int timeout;

    @Value("${cassandra.keyspaces.keyspace.name}")
    private String defaultKeyspaceName;

    @Value("${cassandra.keyspaces.keyspace.readConsistency}")
    private String keyspaceReadConsistency;

    @Value("${cassandra.keyspaces.keyspace.writeConsistency}")
    private String keyspaceWriteConsistency;

    private MappingManager mappingManager;

    private Session session;

    @Bean
    public Session session(Cluster cluster) {
        if (cluster == null) {
            throw new RuntimeException("Cassandra Cluster in NULL");
        }
        if (session == null) {
            session = cluster.connect();
        }
        return session;
    }

    @Bean(name = "MappingManager")
//    public Cluster cluster() {
    public MappingManager mappingManager() {
        try {
            PoolingOptions poolingOptions = new PoolingOptions();
            poolingOptions.setMaxConnectionsPerHost(HostDistance.LOCAL, maxThread);
            poolingOptions.setPoolTimeoutMillis(timeout);
            poolingOptions.setCoreConnectionsPerHost(HostDistance.LOCAL, maxThread);

//            PlainTextAuthProvider authProvider = new PlainTextAuthProvider(userName, password);

            Cluster cluster =  Cluster.builder()
                    .addContactPointsWithPorts(convertToInternetAddress())
//                    .withAuthProvider(authProvider)
                    .withoutJMXReporting()
                    .withPoolingOptions(poolingOptions)
                    .withPort(cassandraPort)
                    .build();
            session =  cluster.connect(defaultKeyspaceName);
            if (mappingManager == null)
                mappingManager = new MappingManager(session);
            return mappingManager;
        } catch (Exception ex) {
            System.out.println("could not connect to cassandra");
            throw ex;
        }
    }


    private List<InetSocketAddress> convertToInternetAddress() {
        List<InetSocketAddress> cassandraHosts = Lists.newArrayList();
        for (String host : cassandraHost.split(",")) {
            InetSocketAddress socketAddress = new InetSocketAddress(host, cassandraPort);
            cassandraHosts.add(socketAddress);
        }
        return cassandraHosts;
    }

}
