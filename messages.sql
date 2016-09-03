DROP TABLE IF EXISTS messages;
CREATE TABLE messages (
       id VARCHAR(256) PRIMARY KEY,
       ts TIMESTAMP,
       rowID INT,
       myCall VARCHAR(8),
       rpt1 VARCHAR(8),
       rpt2 VARCHAR(8),
       urCall VARCHAR(8),
       qsoStarted BOOLEAN,
       flags VARCHAR(6),
       myRadio VARCHAR(4),
       dest VARCHAR(8),
       txStatus VARCHAR(20)
);
