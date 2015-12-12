package io.mikael.demo;

import org.apache.activemq.apollo.broker.Broker;
import org.apache.activemq.apollo.broker.store.leveldb.dto.*;
import org.apache.activemq.apollo.dto.*;

import java.io.File;

import org.apache.activemq.apollo.stomp.dto.StompDTO;
import org.slf4j.LoggerFactory;
import org.springframework.context.Lifecycle;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class ApolloService implements Lifecycle {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ApolloService.class);

    private volatile boolean running = false;

    private Broker broker;

    private static BrokerDTO createConfig() {
        final BrokerDTO broker = new BrokerDTO();

        final VirtualHostDTO host = new VirtualHostDTO();
        host.id = "localhost";
        host.host_names.add("localhost");
        host.host_names.add("127.0.0.1");

        final LevelDBStoreDTO store = new LevelDBStoreDTO();
        store.directory = new File("./data");
        host.store = store;

        broker.virtual_hosts.add(host);

        final AcceptingConnectorDTO connector = new AcceptingConnectorDTO();
        connector.id = "ws";
        connector.bind = "ws://0.0.0.0:61623?binary_transfers=false";
        connector.protocol = "stomp";
        connector.protocols.add(new StompDTO());

        broker.connectors.add(connector);

        return broker;
    }

    @PostConstruct
    @Override
    public void start() {
        broker = new Broker();

        broker.setTmp(new File(System.getProperty("java.io.tmpdir")));
        broker.setConfig(createConfig());

        log.info("Starting the broker.");
        broker.start(() -> {
            running = true;
            log.info("The broker has now started.");
        });
    }

    @PreDestroy
    @Override
    public void stop() {
        broker.stop(() -> log.info("The broker has now stopped."));
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
