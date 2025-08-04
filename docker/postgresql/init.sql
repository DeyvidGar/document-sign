create table docsign_log (
    id serial primary key,
    documentId int,
    ownerId varchar(50),
    "timestamp" timestamp,
    status text,
    errorDetails text
)