# sequra-test

# Sequra Backend Engineering Test

### Reference Documentation


Assumptions:

- every merchant receives one disbursement por week containing the amounts of the orders
- only weeks with orders are included in the disbursement (though this can be configured)
- unfortunately not all the given task in the challenge are fully completed or tested

Technologies:
- Java 17
- Maven 3.6 +
- in-memory H2 database (for testing purposes)

Commands to build:
- mvn clean install

Commands to run:
- java -jar target/sequra-test-0.0.1-SNAPSHOT.jar


Nota bene:
3 hours for a fully built operational system are not sufficient for the challenge;
database configuration, security, and other things are not included in the test.




