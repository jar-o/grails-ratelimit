package org.grails.ratelimit

class RateLimit {

    String limitBy             // E.g. IP, accessToken, whatever
    long limitIntervalMs
    long lastTryEpochMs
    int failLimit

    static constraints = {
        limitBy unique: true
    }

    static mapping = {
        limitIntervalMs defaultValue: 0
        lastTryEpochMs defaultValue: 0
        failLimit defaultValue: 0
    }

}
