package ru.alishev.springcourse.third.config;

import com.jcabi.xml.XML;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TextXPath {

    private final XML xml;
    private final String node;

    public String toString() {
        return this.xml.nodes(this.node)
                .get(0)
                .xpath("text()")
                .get(0);
    }
}
