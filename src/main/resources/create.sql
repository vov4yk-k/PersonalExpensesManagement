
CREATE SCHEMA IF NOT EXISTS personal_expenses;


CREATE TABLE IF NOT EXISTS expenses (
  id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
  exp_date Date,
  amount DOUBLE,
  curency VARCHAR(3),
  product_name VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS currency_rates(
  id INT PRIMARY KEY AUTO_INCREMENT  NOT NULL,
  rate_date Date,
  base_currency VARCHAR(3),
  currency VARCHAR(3),
  rate DOUBLE
);


