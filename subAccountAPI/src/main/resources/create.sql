SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `bravosubaccounting` DEFAULT CHARACTER SET latin1 ;
USE `bravosubaccounting` ;

-- -----------------------------------------------------
-- Table `bravosubaccounting`.`bravoadmin`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `bravosubaccounting`.`bravoadmin` (
  `email` VARCHAR(20) NOT NULL ,
  `password` VARCHAR(50) NOT NULL ,
  `enabled` BINARY(1) NOT NULL DEFAULT '1' ,
  PRIMARY KEY (`email`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `bravosubaccounting`.`customer`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `bravosubaccounting`.`customer` (
  `customerID` VARCHAR(32) NOT NULL ,
  `email` VARCHAR(20) NOT NULL ,
  `password` VARCHAR(50) NOT NULL ,
  `firstName` VARCHAR(35) NULL DEFAULT NULL ,
  `middleInitial` VARCHAR(1) NULL DEFAULT NULL ,
  `lastName` VARCHAR(35) NULL DEFAULT NULL ,
  `street` VARCHAR(35) NULL DEFAULT NULL ,
  `city` VARCHAR(35) NULL DEFAULT NULL ,
  `enabled` BINARY(1) NOT NULL DEFAULT '1' ,
  `state` VARCHAR(50) NULL DEFAULT NULL ,
  `zip` VARCHAR(5) NULL DEFAULT NULL ,
  PRIMARY KEY (`customerID`) ,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `bravosubaccounting`.`merchant`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `bravosubaccounting`.`merchant` (
  `merchantAccNo` VARCHAR(32) NOT NULL ,
  `companyName` VARCHAR(50) NOT NULL ,
  `companyAddress` VARCHAR(50) NOT NULL ,
  `balance` DECIMAL(10,2) NOT NULL ,
  PRIMARY KEY (`merchantAccNo`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `bravosubaccounting`.`card`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `bravosubaccounting`.`card` (
  `merchantAccNo` VARCHAR(32) NOT NULL ,
  `cardID` VARCHAR(32) NOT NULL ,
  `securityCode` VARCHAR(20) NOT NULL ,
  `loyaltyPoint` INT(11) NULL DEFAULT '0' ,
  `balance` DECIMAL(10,2) NOT NULL DEFAULT '0.00' ,
  `primaryCard` BINARY(1) NULL DEFAULT '0' ,
  `lastUse` DATETIME NULL DEFAULT NULL ,
  `enabled` BINARY(1) NOT NULL DEFAULT '1' ,
  `customerID` VARCHAR(32) NULL DEFAULT NULL ,
  PRIMARY KEY (`merchantAccNo`, `cardID`) ,
  INDEX `fk_Card_Customer1` (`customerID` ASC) ,
  INDEX `fk_Card_Merchant1` (`merchantAccNo` ASC) ,
  INDEX `fk_card_Customer` (`customerID` ASC) ,
  CONSTRAINT `fk_Card_Customer1`
    FOREIGN KEY (`customerID` )
    REFERENCES `bravosubaccounting`.`customer` (`customerID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Card_Merchant1`
    FOREIGN KEY (`merchantAccNo` )
    REFERENCES `bravosubaccounting`.`merchant` (`merchantAccNo` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `bravosubaccounting`.`transaction`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `bravosubaccounting`.`transaction` (
  `transactionID` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `totalAmount` DECIMAL(10,2) NOT NULL ,
  `transactionDate` DATETIME NOT NULL ,
  `note` VARCHAR(50) NULL DEFAULT NULL ,
  `receiptNo` INT(32) NULL DEFAULT NULL ,
  `location` VARCHAR(50) NULL DEFAULT NULL ,
  `transactionType` VARCHAR(50) NOT NULL ,
  `customerTimestamp` DATETIME NULL DEFAULT NULL ,
  `merchantAccNo` VARCHAR(32) NOT NULL ,
  `cardID` VARCHAR(32) NOT NULL ,
  PRIMARY KEY (`transactionID`) ,
  INDEX `fk_Transaction_Card1` (`merchantAccNo` ASC, `cardID` ASC) ,
  CONSTRAINT `fk_Transaction_Card1`
    FOREIGN KEY (`merchantAccNo` , `cardID` )
    REFERENCES `bravosubaccounting`.`card` (`merchantAccNo` , `cardID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 341
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `bravosubaccounting`.`checktransaction`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `bravosubaccounting`.`checktransaction` (
  `transactionID` BIGINT(20) NOT NULL ,
  `accountNumber` VARCHAR(20) NOT NULL ,
  `ABA` VARCHAR(9) NOT NULL ,
  `checkDate` VARCHAR(8) NOT NULL ,
  PRIMARY KEY (`transactionID`) ,
  INDEX `fk_Check_Transaction_Transaction1` (`transactionID` ASC) ,
  INDEX `fk_CheckTransaction_Transaction1` (`transactionID` ASC) ,
  CONSTRAINT `fk_CheckTransaction_Transaction1`
    FOREIGN KEY (`transactionID` )
    REFERENCES `bravosubaccounting`.`transaction` (`transactionID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `bravosubaccounting`.`creditcardtransaction`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `bravosubaccounting`.`creditcardtransaction` (
  `transactionID` BIGINT(20) NOT NULL ,
  `cardNumber` VARCHAR(20) NOT NULL ,
  `expirationDate` VARCHAR(4) NOT NULL ,
  `createDate` VARCHAR(8) NOT NULL ,
  PRIMARY KEY (`transactionID`) ,
  INDEX `fk_CreditCard_Transaction_Transaction1` (`transactionID` ASC) ,
  INDEX `fk_CreditCardTransaction_Transaction1` (`transactionID` ASC) ,
  CONSTRAINT `fk_CreditCardTransaction_Transaction1`
    FOREIGN KEY (`transactionID` )
    REFERENCES `bravosubaccounting`.`transaction` (`transactionID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `bravosubaccounting`.`merchantlogin`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `bravosubaccounting`.`merchantlogin` (
  `merchantAccNo` VARCHAR(32) NOT NULL ,
  `email` VARCHAR(20) NOT NULL ,
  `password` VARCHAR(50) NOT NULL ,
  `enabled` BINARY(1) NOT NULL ,
  PRIMARY KEY (`merchantAccNo`, `email`) ,
  INDEX `fk_MerchantLogin_Merchant1` (`merchantAccNo` ASC) ,
  CONSTRAINT `fk_MerchantLogin_Merchant1`
    FOREIGN KEY (`merchantAccNo` )
    REFERENCES `bravosubaccounting`.`merchant` (`merchantAccNo` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `bravosubaccounting`.`merchantrole`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `bravosubaccounting`.`merchantrole` (
  `userRoleID` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `email` VARCHAR(20) NOT NULL ,
  `authority` VARCHAR(45) NOT NULL ,
  `merchantAccNo` VARCHAR(32) NOT NULL ,
  PRIMARY KEY (`userRoleID`) ,
  INDEX `fk_MerchantAccount` (`merchantAccNo` ASC, `email` ASC) ,
  INDEX `fk_MerchantRole_MerchantLogin` (`merchantAccNo` ASC, `email` ASC) ,
  CONSTRAINT `fk_MerchantRole_MerchantLogin`
    FOREIGN KEY (`merchantAccNo` , `email` )
    REFERENCES `bravosubaccounting`.`merchantlogin` (`merchantAccNo` , `email` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `bravosubaccounting`.`orderitem`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `bravosubaccounting`.`orderitem` (
  `transactionID` BIGINT(20) NOT NULL ,
  `productID` VARCHAR(20) NOT NULL ,
  `productName` VARCHAR(100) NULL DEFAULT NULL ,
  `unit` DECIMAL(10,2) NOT NULL ,
  `tax` DECIMAL(10,2) NOT NULL DEFAULT '0.00' ,
  `totalPrice` DECIMAL(10,2) NOT NULL ,
  `refundable` BINARY(1) NOT NULL DEFAULT '1' ,
  PRIMARY KEY (`transactionID`, `productID`) ,
  INDEX `fk_OrderItem_Transaction1` (`transactionID` ASC) ,
  INDEX `fk_OrderItem_Product1` (`productID` ASC) ,
  CONSTRAINT `fk_OrderItem_Transaction1`
    FOREIGN KEY (`transactionID` )
    REFERENCES `bravosubaccounting`.`transaction` (`transactionID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `bravosubaccounting`.`paymentprofile`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `bravosubaccounting`.`paymentprofile` (
  `paymentProfileID` INT(11) NOT NULL AUTO_INCREMENT ,
  `Name` VARCHAR(64) NOT NULL ,
  `paymentType` VARCHAR(1) NOT NULL ,
  `accountNumber` VARCHAR(20) NOT NULL ,
  `ABA` VARCHAR(9) NULL DEFAULT NULL ,
  `name1` VARCHAR(35) NULL DEFAULT NULL ,
  `name2` VARCHAR(35) NULL DEFAULT NULL ,
  `firstName` VARCHAR(35) NULL DEFAULT NULL ,
  `middleInitial` VARCHAR(1) NULL DEFAULT NULL ,
  `lastName` VARCHAR(35) NULL DEFAULT NULL ,
  `checkProfileType` VARCHAR(1) NULL DEFAULT NULL ,
  `expirationDate` VARCHAR(4) NULL DEFAULT NULL ,
  `creditCardProcessingType` VARCHAR(4) NULL DEFAULT NULL ,
  `street` VARCHAR(35) NULL DEFAULT NULL ,
  `city` VARCHAR(35) NULL DEFAULT NULL ,
  `state` VARCHAR(50) NULL DEFAULT NULL ,
  `zip` VARCHAR(5) NULL DEFAULT NULL ,
  `customerID` VARCHAR(32) NOT NULL ,
  PRIMARY KEY (`paymentProfileID`) ,
  INDEX `fk_PaymentProfile_Customer1` (`customerID` ASC) ,
  INDEX `fk` (`customerID` ASC) ,
  CONSTRAINT `fk_PaymentProfile_Customer1`
    FOREIGN KEY (`customerID` )
    REFERENCES `bravosubaccounting`.`customer` (`customerID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 55
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `bravosubaccounting`.`persistent_logins`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `bravosubaccounting`.`persistent_logins` (
  `username` VARCHAR(64) NOT NULL ,
  `series` VARCHAR(64) NOT NULL ,
  `token` VARCHAR(64) NOT NULL ,
  `last_used` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`series`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
