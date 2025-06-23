package objectrepo;

import com.google.protobuf.Empty;
import hb.HeartbeatProto;
import hb.HeartbeatServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.*;
import java.util.concurrent.*;

public class LeaderElectionTask implements Runnable {
    private final ObjectRepositoryImpl registry;
    private final List<Config.Peer> peers;
    private final String selfId;
    private final long startMillis;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private String lastKnownLeader = null;
    private boolean firstRun = true;

    public LeaderElectionTask(ObjectRepositoryImpl registry, List<Config.Peer> peers, String selfId) {
        this.registry = registry;
        this.peers = peers;
        this.selfId = selfId;
        this.startMillis = System.currentTimeMillis();
    }

    public void start() {
        scheduler.scheduleAtFixedRate(this, 2, 2, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        try {
            List<NodeStat> stats = new ArrayList<>();
            stats.add(new NodeStat(selfId, uptimeSeconds()));

            for (Config.Peer peer : peers) {
                String target = peer.host + ":" + peer.hb_port;
                try {
                    ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
                            .usePlaintext()
                            .build();
                    HeartbeatServiceGrpc.HeartbeatServiceBlockingStub stub =
                            HeartbeatServiceGrpc.newBlockingStub(channel);

                    HeartbeatProto.UptimeInfo info = stub.getUptime(Empty.getDefaultInstance());
                    stats.add(new NodeStat(info.getNodeId(), (long) info.getUptimeSec()));
                    channel.shutdownNow();
                } catch (Exception ignored) {}
            }

            stats.sort((a, b) -> Long.compare(b.uptime, a.uptime));
            String newLeader = stats.get(0).id;
            boolean amLeader = selfId.equals(newLeader);

            boolean wasLeader = registry.isLeader();
            boolean roleChanged = (wasLeader != amLeader);
            boolean leaderChanged = !Objects.equals(lastKnownLeader, newLeader);

            if (leaderChanged || roleChanged || firstRun) {
                lastKnownLeader = newLeader;
                registry.setLeader(amLeader);
                System.out.println("[ROLE] now " + (amLeader ? "PRIMARY" : "BACKUP") + "  (leader=" + newLeader + ")");
                firstRun = false;
            }

        } catch (Exception e) {
            System.err.println("[ELECT] error: " + e.getMessage());
        }
    }

    private long uptimeSeconds() {
        return (System.currentTimeMillis() - startMillis) / 1000;
    }

    private static class NodeStat {
        public final String id;
        public final long uptime;

        public NodeStat(String id, long uptime) {
            this.id = id;
            this.uptime = uptime;
        }
    }
}
