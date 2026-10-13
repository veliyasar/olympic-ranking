package com.huawei.olympicranking.exception;

public class DuplicateMedalException extends DuplicateResourceException {

    public DuplicateMedalException(Long athleteId, Long sportId, String medalType) {
        super("Athlete " + athleteId + " already has a " + medalType + " medal in sport " + sportId);
    }
}
