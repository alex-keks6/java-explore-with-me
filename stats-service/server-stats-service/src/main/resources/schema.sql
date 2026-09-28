CREATE TABLE IF NOT EXISTS hits
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY,
    app
    VARCHAR
(
    512
) NOT NULL,
    uri VARCHAR
(
    512
) NOT NULL,
    ip VARCHAR
(
    256
) NOT NULL,
    datetime TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_hits PRIMARY KEY
(
    id
)
    );