CREATE TABLE IF NOT EXISTS BOOK (
    IDT_BOOK VARCHAR(60) PRIMARY KEY,
    DES_TITLE VARCHAR(255) NOT NULL,
    DES_AUTHOR VARCHAR(255) NOT NULL,
    DES_YEAR_PUBLICATION VARCHAR(4) NOT NULL,
    DAT_CREATION TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS BOOK_VERSION (
    IDT_BOOK_VERSION VARCHAR(60) PRIMARY KEY,
    IDT_BOOK VARCHAR(60) NOT NULL,
    NUM_VERSION INT NOT NULL,
    FLG_ENABLE BOOLEAN NOT NULL,
    DAT_CREATION TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS LOAN (
    IDT_LOAN VARCHAR(60) PRIMARY KEY,
    IDT_BOOK VARCHAR(60) NOT NULL,
    IDT_CLIENT VARCHAR(60) NOT NULL,
    DAT_DEVOLUTION TIMESTAMP NOT NULL,
    DAT_CREATION TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS LOAN_VERSION (
    IDT_LOAN_VERSION VARCHAR(60) PRIMARY KEY,
    IDT_LOAN VARCHAR(60) NOT NULL,
    NUM_VERSION INT NOT NULL,
    DES_STATUS VARCHAR(255) NOT NULL,
    DAT_CREATION TIMESTAMP
);

CREATE TABLE IF NOT EXISTS CLIENT (
    IDT_CLIENT VARCHAR(60) PRIMARY KEY,
    DES_NAME VARCHAR(255) NOT NULL,
    DES_EMAIL VARCHAR(255) NOT NULL,
    DES_DOCUMENT_TYPE VARCHAR(255) NOT NULL,
    DES_DOCUMENT_VALUE VARCHAR(255) NOT NULL,
    DAT_CREATION TIMESTAMP NOT NULL
);

INSERT INTO BOOK (IDT_BOOK, DES_TITLE, DES_AUTHOR, DES_YEAR_PUBLICATION, DAT_CREATION) VALUES ('a5993416-4255-11ec-71d3-0242ac130004', 'Clean Code', 'Robert Cecil Martin', '2008', '2024-04-23T23:01:40.619-03:00');
INSERT INTO BOOK (IDT_BOOK, DES_TITLE, DES_AUTHOR, DES_YEAR_PUBLICATION, DAT_CREATION) VALUES ('a5993416-4255-11ec-71d3-0242ac130005', 'Design Patterns com Java', 'Eduardo Guerra', '2014', '2024-04-23T23:01:40.619-03:00');
INSERT INTO BOOK (IDT_BOOK, DES_TITLE, DES_AUTHOR, DES_YEAR_PUBLICATION, DAT_CREATION) VALUES ('a5993416-4255-11ec-71d3-0242ac130006', 'Google BigQuery', 'Valliappa Lakshmanan e Jordan Tigani', '2019', '2024-04-23T23:01:40.619-03:00');
INSERT INTO BOOK (IDT_BOOK, DES_TITLE, DES_AUTHOR, DES_YEAR_PUBLICATION, DAT_CREATION) VALUES ('a5993416-4255-11ec-71d3-0242ac130007', 'Observability Engineering', 'Liz Fong', '2022', '2024-04-23T23:01:40.619-03:00');
INSERT INTO BOOK (IDT_BOOK, DES_TITLE, DES_AUTHOR, DES_YEAR_PUBLICATION, DAT_CREATION) VALUES ('a5993416-4255-11ec-71d3-0242ac130008', 'Oracle Database', 'Jason Price', '2009', '2024-04-23T23:01:40.619-03:00');
INSERT INTO BOOK (IDT_BOOK, DES_TITLE, DES_AUTHOR, DES_YEAR_PUBLICATION, DAT_CREATION) VALUES ('a5993416-4255-11ec-71d3-0242ac130009', 'Kubernetes', 'Jason William', '2019', '2024-04-23T23:01:40.619-03:00');
INSERT INTO BOOK (IDT_BOOK, DES_TITLE, DES_AUTHOR, DES_YEAR_PUBLICATION, DAT_CREATION) VALUES ('a5993416-4255-11ec-71d3-0242ac130010', 'Docker', 'Jason William', '2020', '2024-04-23T23:01:40.619-03:00');

INSERT INTO BOOK_VERSION (IDT_BOOK_VERSION, IDT_BOOK, NUM_VERSION, FLG_ENABLE, DAT_CREATION) VALUES ('a5993416-4255-11ec-71d3-0242ac130004', 'a5993416-4255-11ec-71d3-0242ac130004', 1, 1, '2024-04-23T23:01:40.619-03:00');
INSERT INTO BOOK_VERSION (IDT_BOOK_VERSION, IDT_BOOK, NUM_VERSION, FLG_ENABLE, DAT_CREATION) VALUES ('a5993416-4255-11ec-71d3-0242ac130005', 'a5993416-4255-11ec-71d3-0242ac130005', 1, 1, '2024-04-23T23:01:40.619-03:00');
INSERT INTO BOOK_VERSION (IDT_BOOK_VERSION, IDT_BOOK, NUM_VERSION, FLG_ENABLE, DAT_CREATION) VALUES ('a5993416-4255-11ec-71d3-0242ac130006', 'a5993416-4255-11ec-71d3-0242ac130006', 1, 1, '2024-04-23T23:01:40.619-03:00');
INSERT INTO BOOK_VERSION (IDT_BOOK_VERSION, IDT_BOOK, NUM_VERSION, FLG_ENABLE, DAT_CREATION) VALUES ('a5993416-4255-11ec-71d3-0242ac130007', 'a5993416-4255-11ec-71d3-0242ac130007', 1, 1, '2024-04-23T23:01:40.619-03:00');
INSERT INTO BOOK_VERSION (IDT_BOOK_VERSION, IDT_BOOK, NUM_VERSION, FLG_ENABLE, DAT_CREATION) VALUES ('a5993416-4255-11ec-71d3-0242ac130008', 'a5993416-4255-11ec-71d3-0242ac130007', 1, 0, '2024-04-23T23:01:40.619-03:00');
INSERT INTO BOOK_VERSION (IDT_BOOK_VERSION, IDT_BOOK, NUM_VERSION, FLG_ENABLE, DAT_CREATION) VALUES ('a5993416-4255-11ec-71d3-0242ac130009', 'a5993416-4255-11ec-71d3-0242ac130008', 1, 1, '2024-04-23T23:01:40.619-03:00');
INSERT INTO BOOK_VERSION (IDT_BOOK_VERSION, IDT_BOOK, NUM_VERSION, FLG_ENABLE, DAT_CREATION) VALUES ('a5993416-4255-11ec-71d3-0242ac130010', 'a5993416-4255-11ec-71d3-0242ac130008', 1, 0, '2024-04-23T23:01:40.619-03:00');
INSERT INTO BOOK_VERSION (IDT_BOOK_VERSION, IDT_BOOK, NUM_VERSION, FLG_ENABLE, DAT_CREATION) VALUES ('a5993416-4255-11ec-71d3-0242ac130011', 'a5993416-4255-11ec-71d3-0242ac130009', 1, 1, '2024-04-23T23:01:40.619-03:00');
INSERT INTO BOOK_VERSION (IDT_BOOK_VERSION, IDT_BOOK, NUM_VERSION, FLG_ENABLE, DAT_CREATION) VALUES ('a5993416-4255-11ec-71d3-0242ac130012', 'a5993416-4255-11ec-71d3-0242ac130010', 1, 1, '2024-04-23T23:01:40.619-03:00');

INSERT INTO CLIENT (IDT_CLIENT, DES_NAME, DES_EMAIL, DES_DOCUMENT_TYPE, DES_DOCUMENT_VALUE, DAT_CREATION) VALUES ('a5993416-4255-11ec-71d3-0242ac130004', 'Renato Virto', 'renatovirtomoreira@outlook.com', 'CPF', '12345678998', '2024-04-23T23:01:40.619-03:00');
INSERT INTO CLIENT (IDT_CLIENT, DES_NAME, DES_EMAIL, DES_DOCUMENT_TYPE, DES_DOCUMENT_VALUE, DAT_CREATION) VALUES ('a5993416-4255-11ec-71d3-0242ac130005', 'Maria Eduarda Gomes', 'mariaeduardagomes@outlook.com', 'CPF', '09876543212', '2024-04-23T23:01:40.619-03:00');
INSERT INTO CLIENT (IDT_CLIENT, DES_NAME, DES_EMAIL, DES_DOCUMENT_TYPE, DES_DOCUMENT_VALUE, DAT_CREATION) VALUES ('a5993416-4255-11ec-71d3-0242ac130006', 'João Viel', 'joaoviel@outlook.com', 'CPF', '74185296345', '2024-04-23T23:01:40.619-03:00');

INSERT INTO LOAN (IDT_LOAN, IDT_BOOK, IDT_CLIENT, DAT_DEVOLUTION, DAT_CREATION) VALUES ('a5993416-4255-11ec-71d3-0242ac130004', 'a5993416-4255-11ec-71d3-0242ac130007', 'a5993416-4255-11ec-71d3-0242ac130004', '2024-08-23T23:01:40.619-03:00', '2024-04-23T23:01:40.619-03:00');
INSERT INTO LOAN (IDT_LOAN, IDT_BOOK, IDT_CLIENT, DAT_DEVOLUTION, DAT_CREATION) VALUES ('a5993416-4255-11ec-71d3-0242ac130005', 'a5993416-4255-11ec-71d3-0242ac130008', 'a5993416-4255-11ec-71d3-0242ac130004', '2024-08-23T23:01:40.619-03:00', '2024-04-23T23:01:40.619-03:00');
INSERT INTO LOAN (IDT_LOAN, IDT_BOOK, IDT_CLIENT, DAT_DEVOLUTION, DAT_CREATION) VALUES ('a5993416-4255-11ec-71d3-0242ac130006', 'a5993416-4255-11ec-71d3-0242ac130004', 'a5993416-4255-11ec-71d3-0242ac130004', '2024-08-23T23:01:40.619-03:00', '2024-04-23T23:01:40.619-03:00');

INSERT INTO LOAN_VERSION (IDT_LOAN_VERSION, IDT_LOAN, NUM_VERSION, COD_STATUS, DAT_CREATION) VALUES ('a5993416-4255-11ec-71d3-0242ac130004', 'a5993416-4255-11ec-71d3-0242ac130004', 1, 'BORROWED', '2024-04-23T23:01:40.619-03:00');
INSERT INTO LOAN_VERSION (IDT_LOAN_VERSION, IDT_LOAN, NUM_VERSION, COD_STATUS, DAT_CREATION) VALUES ('a5993416-4255-11ec-71d3-0242ac130005', 'a5993416-4255-11ec-71d3-0242ac130005', 1, 'BORROWED', '2024-04-23T23:01:40.619-03:00');
INSERT INTO LOAN_VERSION (IDT_LOAN_VERSION, IDT_LOAN, NUM_VERSION, COD_STATUS, DAT_CREATION) VALUES ('a5993416-4255-11ec-71d3-0242ac130006', 'a5993416-4255-11ec-71d3-0242ac130006', 1, 'BORROWED', '2024-04-23T23:01:40.619-03:00');
INSERT INTO LOAN_VERSION (IDT_LOAN_VERSION, IDT_LOAN, NUM_VERSION, COD_STATUS, DAT_CREATION) VALUES ('a5993416-4255-11ec-71d3-0242ac130007', 'a5993416-4255-11ec-71d3-0242ac130006', 2, 'RETURNED', '2024-04-23T23:01:40.619-03:00');



