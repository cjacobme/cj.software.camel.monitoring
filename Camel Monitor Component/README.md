# Camel-Monitoring


#### general ideas
This project aims to implement a monitoring for Apache Camel.
The idea is that from arbitrary Routes a wiretap is launched in which this monitoring tool is called.
This monitoring tool has just a Producer which stores information about the current exchange.

The code could look like this:
```
from ("direct:start")
    .to("sql:select * from foo")
;
```

#### how things are stored
It should be possible to store the exchange informations in several ways. Therefore, a data sink has to be registered in the camel context.

1. print out on the console. This is suitable

