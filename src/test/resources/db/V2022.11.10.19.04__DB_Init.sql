CREATE TABLE ORGANIZATION
(
    id               BIGSERIAL PRIMARY KEY,
    INN              int8 NOT NULL,
    CHECKING_ACCOUNT int8 NOT NULL
);

CREATE TABLE PRODUCT
(
    id            BIGSERIAL PRIMARY KEY,
    name          varchar(64) NOT NULL,
    internal_code int8        NOT NULL
);


CREATE TABLE INVOICE
(
    id           BIGSERIAL PRIMARY KEY,
    number       int8                                                                  NOT NULL,
    invoice_date date                                                             NOT NULL,
    sender_id    int8 references ORGANIZATION (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL
);


CREATE TABLE INVOICE_POSITION
(
    id         BIGSERIAL PRIMARY KEY,
    price      float                                                            NOT NULL,
    product_id int8 references PRODUCT (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
    amount     int                                                              NOT NULL DEFAULT 1,
    invoice_id int8 references INVOICE (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL
)

