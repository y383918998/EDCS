// File: ObjectRepositoryServer.java
package objectrepo;

import objectrepo.Config;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import objectrepo.ObjectRepositoryImpl;
import objectrepo.HeartbeatServiceImpl;

import java.util.concurrent.Executors;

public class ObjectRepositoryServer {
    public static void main(String[] args) throws Exception {
        Config config = Config.load("config.json");

        ObjectRepositoryImpl repoImpl = new ObjectRepositoryImpl(config.ttl_seconds);

        HeartbeatServiceImpl hbImpl = new HeartbeatServiceImpl(repoImpl);

        Server bizServer = ServerBuilder.forPort(extractPort(config.self_address))
                .addService(repoImpl)
                .executor(Executors.newFixedThreadPool(10))
                .build().start();

        Server hbServer = ServerBuilder.forPort(extractPort(config.hb_address))
                .addService(hbImpl)
                .executor(Executors.newFixedThreadPool(4))
                .build().start();

        System.out.println("biz @ " + config.self_address);
        System.out.println("hb  @ " + config.hb_address);

        new LeaderElectionTask(repoImpl, config.peers, config.node_id).start();

        bizServer.awaitTermination();
        hbServer.awaitTermination();
    }

    private static int extractPort(String addr) {
        return Integer.parseInt(addr.substring(addr.lastIndexOf(':') + 1));
    }
}
