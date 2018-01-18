# Camel-Monitoring


### general ideas
This project aims to implement a monitoring for Apache Camel.
The idea is that from arbitrary Routes a wiretap is launched in which this monitoring tool is called.
This monitoring tool has just a Producer which stores information about the current exchange.

The code could look like this:
```
from ("direct:start")
    .to("sql:select * from foo")
    .wiretap("moni:?params)
    .to("bean:doSomething")
;
```

### how things are stored
It should be possible to store the exchange informations in several ways. Therefore, a data sink has to be registered in the camel context.

1. print out on the console. This is suitable for unit tests
2. use a logger. This is also suitable for unit tests
3. save via JDBC and Hibernate.
4. use Apache Cassandra

One of the first things I'm going to try out is to use Apache Cassandra, because it is __very__ fast, and one can use an optional Parameter _Time to Live_ in each
<tt>Insert</tt> statement. With this parameter provided, the database record is delete from the database without any further action. 

