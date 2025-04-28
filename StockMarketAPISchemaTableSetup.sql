CREATE SCHEMA stockmarket;

CREATE TABLE stockmarket.stockopening(
Stock varchar(255),
Dates varchar(255),
Opening float
);

CREATE TABLE stockmarket.stockclosing(
Stock varchar(255),
Dates varchar(255),
Closing float
);

CREATE TABLE stockmarket.stockhigh(
Stock varchar(255),
Dates varchar(255),
High float
);

CREATE TABLE stockmarket.stocklow(
Stock varchar(255),
Dates varchar(255),
Low float
);