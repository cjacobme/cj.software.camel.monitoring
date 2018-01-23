# Camel-Monitoring


### general ideas
This project aims to implement a monitoring for Apache Camel.
The idea is that from arbitrary Routes a wiretap is launched in which this monitoring tool is called.
This monitoring tool has just a Producer which stores information about the current exchange.

The code could look like this:

~~~
from ("direct:start")
    .to("sql:select * from foo")
    .wiretap("moni:blablabla?params")
    .to("bean:doSomething")
;
~~~

### how things are stored
It should be possible to store the exchange informations in several ways. Therefore, a data sink has to be registered in the camel context.

These implementations are possible:

1. print out on the console. This is suitable for unit tests.
2. use a logger. This is also suitable for unit tests.
3. save via JDBC and Hibernate. In that case, one has to install a process that somehow deletes database records that are too old, because otherwise the disk space is wasted with too much data in the database. Nobody cares for the Log entries of my grandfather on Debug level!
4. use Apache Cassandra

One of the first things I'm going to try out is to use Apache Cassandra, because it is __very__ fast, and one can use an optional Parameter _Time to Live_ in each
<tt>Insert</tt> statement. With this parameter provided, the database record is deleted from the database after a while without any further action. 

### how to use it
This component has two additional items:
- the __running context__ is a group of routes that are called one after the other. For example: route #1 reads data from a database,
route #2 transforms it, route #3 converts to csv and stores it on disk, route #4 sends an email. All these four routes are started by a timer.
And all these routes make up a running context.
- the __run id__ is an id provided by the monitor that identifies a run of a running context. If the monitor is using Apache Cassandra, this id should of course be a _time-uuid_. We seperate the run id from Camels Exchange-ID because with an own id we are able to generate IDs that have a timely order. This means: if one id is alphabetically seen larger than another one, the first one was started later.

Now, an application developer can use this component in this way:

~~~
from ("direct:start")
    .to("moni:start?runningContext=my-running-context&...")
    .doTry()
    .onException(to("moni:failed?..."))
        .handled(true)
        .to("sql:select * from foo")
        .wireTap("moni:entry?...")
        .to("bean:doSomething")
    .doFinally()
        .to("moni:finished")
    .end()
;
~~~

At the begin, a new run is started with **to("moni:start")**. Internally, the monitor is asked for a run-id, and this is stored as an exchange property.
After that, several entries can be added to the monitor by calls of **to("moni:entry")** or **wireTap("moni:entry")**. If an exception occured, **to("moni:failed")** should be invoked. The monitor can then mark this run as failed.
Finally, **to("moni:finished")** should be invoked. With this the monitor can close the run. But the monitor has to watch out that after the call of finished further entries that were routed through wiretap can arrive. 