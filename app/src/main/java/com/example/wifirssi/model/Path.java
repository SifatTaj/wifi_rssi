package com.example.wifirssi.model;

import java.util.List;

public class Path {
    private List<Node> nodes;

    public Path(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Node> getNodes() {
        return nodes;
    }
}
