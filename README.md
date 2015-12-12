POC: Spring Boot with an embedded Apache ActiveMQ Apollo STOMP WebSocket server.

Integrates Apollo 1.7.1 with Spring Boot 1.3.0.

Issues:

* The Apollo WebSocket support requires a specific version of Jetty 8 to be included,
  so the Spring Boot Jetty support can't be used, as it requires Jetty 9.

* Further, instead of websocket support you'll only get misleading error messages
  until you first include the `apollo-broker` dependency with its META-INF SPI file,
  and then figure out that the websocket protocol class quietly fails if it doesn't
  find the correct Jetty classes.
  
* The web admin interface has all kinds of funky dependencies, it really wasn't built to
  be embedded.
  
* I couldn't be arsed to read through enough of the documentation to figure out how to
  specify topics and queues in `application.yml`.

