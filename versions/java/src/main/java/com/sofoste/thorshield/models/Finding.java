package com.sofoste.thorshield.models;

import java.util.List;

public record Finding(String rule_id, String severity, List<String> observation_ids, String message, String suggestion) {}
