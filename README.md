# grails-ratelimit
Simple value-based rate limiting plugin that ensures that some logic is limited to run `1` time every `X` milliseconds.

E.g. a common usage might be:

     if (rateLimitService.limit(request.remoteAddr + request.remotePort, 1000)) {
          def result = [error:'too fast yo!']
          respond result
          return
     }

This does not try to do lockout. Meaning after some number of attempts all
requests matching `limitBy` will be dropped. If you want that, you could subclass
(but should really look at server-based options). `getStatus()` would be
useful there.
