CREATE TABLE model_run_ids_by_context_name (
    camel_context_name text,
    model_run_id timeuuid,
    camel_exchange_id text,
    start timestamp,
    finish timestamp, 
    running_state text,
    PRIMARY KEY ((camel_context_name), model_run_id)
);

CREATE TABLE model_run_starts_by_context_name (
    camel_context_name text,
    camel_exchange_id text,
    start timestamp,
    finish timestamp, 
    model_run_id timeuuid,
    running_state text, 
    PRIMARY KEY ((camel_context_name), start)
);

create type message (
    message_id text,
    body_class text,
    body text,
    headers map<text,text>
);

create table exchange_details (
    camel_context_name text,
    model_run_id timeuuid,
    detail_id timeuuid,
    level text,
    logger_name text,
    camel_exchange_id text,
    endpoint_uri text,
    initial_route_id text,
    current_route_id text,
    exchange_created timestamp,
    monitored timestamp,
    in_message frozen<message>,
    out_message frozen<message>,
    properties map<text,text>,
    primary key ((camel_context_name, model_run_id), detail_id)
);