DROP TABLE IF EXISTS jobs;

CREATE TABLE jobs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    priority INT(8) NULL,
    title VARCHAR(128) NOT NULL,
    description VARCHAR(1024) NOT NULL,
    deadline timestamp NOT NULL,
    notification BOOLEAN NOT NULL,
    ended BOOLEAN NULL default false
);