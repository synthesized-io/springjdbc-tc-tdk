package io.synthesized.jdbcdemo.domain;

import lombok.Data;
import lombok.With;

import java.util.Set;

@Data
public class Talk {
    private final int id;
    private final String name;
    private final Conference conference;
    @With
    private final Status status;
    @With
    private final String feedback;
    private final Set<Speaker> speakers;
}
