package com.exasol.cloudetl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.Network;

import com.github.dockerjava.api.DockerClient;

public final class DockerNamedNetwork implements Network {
    private static final Logger LOGGER = Logger.getLogger(DockerNamedNetwork.class.getName());
    private static final ConcurrentHashMap<String, DockerNamedNetwork> NAMED_NETWORKS = new ConcurrentHashMap<>();
    private final String name;
    private final boolean reuse;
    private final String id;

    private DockerNamedNetwork(final String name, final boolean reuse) {
        this.name = name;
        this.reuse = reuse;
        this.id = getNetworkId();
    }

    public static DockerNamedNetwork create(final String name, final boolean reuse) {
        return NAMED_NETWORKS.computeIfAbsent(name, key -> new DockerNamedNetwork(key, reuse));
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void close() {
        if (this.reuse) {
            LOGGER.warning(() -> "Skipping the network termination because 'reuse' is enabled. Please destroy the network "
                    + "manually using 'docker network rm " + this.id + "'.");
        } else {
            getDockerClient().removeNetworkCmd(this.id).exec();
        }
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        throw new UnsupportedOperationException();
    }

    private String getNetworkId() {
        return getDockerClient().listNetworksCmd().withNameFilter(this.name).exec().stream().findAny()
                .map(com.github.dockerjava.api.model.Network::getId).orElseGet(this::createNetwork);
    }

    private String createNetwork() {
        return getDockerClient().createNetworkCmd().withName(this.name).exec().getId();
    }

    private DockerClient getDockerClient() {
        return DockerClientFactory.lazyClient();
    }
}
