class RatelimitGrailsPlugin {
    def version = "0.1"
    def grailsVersion = "2.5 > *"
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]
    def title = "Ratelimit Plugin" // Headline display name of the plugin
    def author = "James R."
    def authorEmail = "traptoy@gmail.com"
    def description = '''\
Simple value-based rate limiting plugin. Not intended to replace actual server
rate limiting, this plugin gives you a low calorie way of ensuring some piece of
code, such as a controller action, can be limited by some piece of data,
such as IP address.

'''
}
