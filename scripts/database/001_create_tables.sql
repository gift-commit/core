CREATE TABLE account(
  accountId VARCHAR(255) PRIMARY KEY,
  groupId VARCHAR(255),
  firstName VARCHAR(255) NOT NULL,
  lastName VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL
);

CREATE TABLE item(
  itemId VARCHAR(255) PRIMARY KEY,
  accountId VARCHAR(255),
  event VARCHAR(255) NOT NULL,
  claimed BOOLEAN NOT NULL,
  name VARCHAR(255) NOT NULL,
  url VARCHAR(255),
  price DECIMAL,
  notes VARCHAR(255),
  tags VARCHAR(255),
  FOREIGN KEY (accountId) REFERENCES account(accountId)
);