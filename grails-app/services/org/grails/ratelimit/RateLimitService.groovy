package org.grails.ratelimit

import grails.transaction.Transactional

/**
 * Rate limiting service that ensures that some logic is limited to run {@code 1} time every {@code X} milliseconds.
 * <p>
 * E.g. a common usage might be:
 * <pre>
 *     if (rateLimitService.limit(request.remoteAddr + request.remotePort, 1000)) {
 *          def result = [error:'too fast yo! ']
 *          respond result
 *          return
 *     }
 * </pre>
 * This does not try to do 'lockout'. Meaning after some number of attempts all requests matching {@code limitBy} will
 * be dropped. If you want that, you could subclass (but should really look at server-based options).
 * {@link #getStatus(String)} would be useful there.
 *
 */
@Transactional
class RateLimitService {

    //

    /**
     * The primary function. Given a unique identifier, determines whether we should proceed or not.
     *
     * @param limitBy The unique value by which we represent the entity making the attempt. Often,
     * this will be {@code IP:Port}, but may also be an access token or other session-based value.
     * @param limitIntervalMs The number of milliseconds by which to limit
     * @return boolean
     */
    def limit(String limitBy, long limitIntervalMs) {

        def r = RateLimit.findByLimitBy(limitBy)

        def now = new Date().time

        // First hit. Create and persist a record referenced by limitBy in the database. If limitBy is not unique an
        // exception is thrown.
        if (!r) {
            r = new RateLimit()
            r.limitIntervalMs = limitIntervalMs
            r.lastTryEpochMs = now
            r.limitBy = limitBy
            save(r)
            return false
        }

        def ok = (now - r.lastTryEpochMs < limitIntervalMs) ? true : false

        r.lastTryEpochMs = now
        save(r)

        return ok
    }    // limit

    /**
     * Returns the {@link org.grails.ratelimit.RateLimit} object identified by {@code limitBy}.
     *
     * @param limitBy The unique identifier used for limiting.
     * @return {@link org.grails.ratelimit.RateLimit}
     */
    def getStatus(String limitBy) {
        return RateLimit.findByLimitBy(limitBy)
    }

    private save(RateLimit r) {
        r.save(flush:  true, failOnError: true)
    }
}
