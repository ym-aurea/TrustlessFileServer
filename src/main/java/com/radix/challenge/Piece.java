package com.radix.challenge;

import lombok.Value;

import java.util.List;

@Value
public class Piece {
    private final String content;
    private final List<String> proof;
}
