create table CUSTOMER
(
	ID   BIGINT primary key  AUTO_INCREMENT,
	NAME varchar(100) not null,
	SURNAME  varchar(100)      not null,
	CREATION_TIME TIMESTAMP,
	MODIFICATION_TIME TIMESTAMP
);

create table ACCOUNT
(
	ID   BIGINT primary key AUTO_INCREMENT,
	DESCRIPTION VARCHAR(225) not null,
	BALANCE NUMERIC,
	CUSTOMER_ID BIGINT,
	CREATION_TIME TIMESTAMP,
	MODIFICATION_TIME TIMESTAMP,
	CONSTRAINT FK_CUSTOMER_ACCOUNT FOREIGN KEY (CUSTOMER_ID)
    REFERENCES CUSTOMER(ID)
);


create table ACCOUNT_TRANSACTION
(
    ID         BIGINT primary key AUTO_INCREMENT,
    DESCRIPTION VARCHAR(225) not null,
    AMOUNT     NUMERIC not null,
    ACCOUNT_ID INT,
    CREATION_TIME TIMESTAMP,
    MODIFICATION_TIME TIMESTAMP,
    CONSTRAINT FK_ACCOUNT_TRANSACTION
        FOREIGN KEY (ACCOUNT_ID) REFERENCES ACCOUNT (ID)
);