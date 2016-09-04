DROP TABLE IF EXISTS messages;
CREATE TABLE messages (
       id VARCHAR(256) PRIMARY KEY,
       ts TIMESTAMP,
       rowid INT,
       mycall VARCHAR(8),
       rpt1 VARCHAR(8),
       rpt2 VARCHAR(8),
       urcall VARCHAR(8),
       qsostarted BOOLEAN,
       flags VARCHAR(6),
       myradio VARCHAR(4),
       dest VARCHAR(8),
       txstats VARCHAR(20)
);
