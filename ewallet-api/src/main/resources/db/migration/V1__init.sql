create table wallet (
	  id INT AUTO_INCREMENT PRIMARY KEY,
	  wallet_name VARCHAR(250) NOT NULL,
	  fund_amount DECIMAL(12, 2) DEFAULT 0.00
);
