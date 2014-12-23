package org.openscore.content.ssh.entities;

import java.nio.file.Path;

/**
 * @author octavian-h
 * @since 0.0.7
 */
public class KnownHostsFile {
    private Path path;
    private String policy;

    public KnownHostsFile(Path path, String policy) {
        this.path = path;
        this.policy = policy;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }
}
