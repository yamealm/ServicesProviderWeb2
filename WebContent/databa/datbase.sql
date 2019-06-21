INSERT INTO `services`.`event` (`id`, `name`, `description`) VALUES ('1', 'create', 'create');
INSERT INTO `services`.`event` (`id`, `name`, `description`) VALUES ('2', 'read', 'read');
INSERT INTO `services`.`event` (`id`, `name`, `description`) VALUES ('3', 'update', 'update');
INSERT INTO `services`.`event` (`id`, `name`, `description`) VALUES ('4', 'delete', 'delete');
INSERT INTO `services`.`event` (`id`, `name`, `description`) VALUES ('5', 'web_services_call', 'web_services_call');


UPDATE `services`.`profile_data` SET `alias` = 'Perfil Prueba', `description` = 'Perfil Prueba' WHERE (`id` = '5');
UPDATE `services`.`profile_data` SET `description` = 'Perfil Test' WHERE (`id` = '6');
INSERT INTO `services`.`profile_data` (`id`, `profileId`, `languageId`, `alias`, `description`) VALUES ('7', '3', '1', 'Perfil 2', 'Perfil 2');
INSERT INTO `services`.`profile_data` (`id`, `profileId`, `languageId`, `alias`, `description`) VALUES ('8', '3', '2', 'Profile 2', 'Profile2');
INSERT INTO `services`.`profile_data` (`id`, `profileId`, `languageId`, `alias`, `description`) VALUES ('9', '4', '1', 'Perfil 3', 'Perfil 3');
INSERT INTO `services`.`profile_data` (`id`, `profileId`, `languageId`, `alias`, `description`) VALUES ('10', '4', '2', 'Profile 3', 'Profile 3');
INSERT INTO `services`.`profile_data` (`id`, `profileId`, `languageId`, `alias`, `description`) VALUES ('11', '5', '1', 'Perfil 5', 'Perfil 5');
INSERT INTO `services`.`profile_data` (`id`, `profileId`, `languageId`, `alias`, `description`) VALUES ('12', '5', '2', 'Profile 5', 'Profile 5');
INSERT INTO `services`.`profile_data` (`id`, `profileId`, `languageId`, `alias`, `description`) VALUES ('13', '6', '1', 'Perfil 6', 'Perfil 5');
INSERT INTO `services`.`profile_data` (`id`, `profileId`, `languageId`, `alias`, `description`) VALUES ('14', '6', '2', 'Profile 7', 'Profile 7');

UPDATE `services`.`category` SET `name` = 'STOCK' WHERE (`id` = '1');
UPDATE `services`.`category` SET `name` = 'EN TRANSTO' WHERE (`id` = '2');
UPDATE `services`.`category` SET `name` = 'CUARENTENA' WHERE (`id` = '3');
INSERT INTO `services`.`category` (`id`, `name`, `enabled`) VALUES ('4', 'EN ESPERA', '1');
INSERT INTO `services`.`category` (`id`, `name`, `enabled`) VALUES ('5', 'CONTROL METERIOLOGICO', '1');

DELETE FROM `services`.`product` WHERE (`id` = '1');
DELETE FROM `services`.`product` WHERE (`id` = '3');
DELETE FROM `services`.`product` WHERE (`id` = '2');
DELETE FROM `services`.`product` WHERE (`id` = '4');
DELETE FROM `services`.`product` WHERE (`id` = '5');

CREATE TABLE `services`.`condition` (
  `id` int(3) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `services`.`condition` (`id`, `name`) VALUES ('1', 'new');
INSERT INTO `services`.`condition` (`id`, `name`) VALUES ('2', 'repaired');
INSERT INTO `services`.`condition` (`id`, `name`) VALUES ('3', 'overhauld');
INSERT INTO `services`.`condition` (`id`, `name`) VALUES ('4', 'inpected');

ALTER TABLE `services`.`product` 
ADD COLUMN `realAmount` INT NULL AFTER `inictialAmount`,
ADD COLUMN `batchNumber` VARCHAR(45) NULL AFTER `cure`,
ADD COLUMN `stockMin` INT NULL AFTER `batchNumber`,
ADD COLUMN `stockMax` INT NULL AFTER `stockMin`,
ADD COLUMN `amount` FLOAT(10,2) NULL AFTER `stockMax`,
CHANGE COLUMN `name` `partNumber` VARCHAR(45) NOT NULL ,
CHANGE COLUMN `taxInclude` `act_np_nsn` VARCHAR(45) NOT NULL ,
CHANGE COLUMN `enabled` `description` VARCHAR(45) NOT NULL ,
CHANGE COLUMN `referenceCode` `ubication` VARCHAR(45) NOT NULL ,
CHANGE COLUMN `ratesUrl` `inictialAmount` INT NULL DEFAULT NULL ,
CHANGE COLUMN `accessNumberUrl` `invoice` VARCHAR(45) NULL DEFAULT NULL ,
CHANGE COLUMN `isFree` `cure` VARCHAR(45) NULL DEFAULT NULL ;

ALTER TABLE `services`.`product` 
CHANGE COLUMN `id` `id` BIGINT(5) NOT NULL ;
ALTER TABLE `services`.`product` 
DROP COLUMN `invoice`;

ALTER TABLE `services`.`provider` 
CHANGE COLUMN `id` `id` BIGINT(5) NOT NULL ;

ALTER TABLE `services`.`product` 
DROP FOREIGN KEY `fk_product_service1`;
ALTER TABLE `services`.`product` 
DROP COLUMN `cure`,
DROP COLUMN `categoryId`,
DROP INDEX `fk_product_service1` ;
ALTER TABLE `services`.`category` 
CHANGE COLUMN `id` `id` INT(3) NOT NULL ;


CREATE TABLE `services`.`stock` (
  `id` BIGINT(5) NOT NULL,
  `productId` BIGINT(5) NOT NULL,
  `providerId` BIGINT(3) NOT NULL,
  `categoryIdl` INT(3) NOT NULL,
  `quantity` INT NOT NULL,
  `invoice` VARCHAR(45) NOT NULL,
  `serial` VARCHAR(45) NULL,
  `creationDate` DATETIME NOT NULL,
  `EndingDate` DATETIME NULL DEFAULT NULL,
  `conditionId` INT(3) NOT NULL,
  `observation` VARCHAR(255) NULL,
  `form` BLOB NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_product_category` (`categoryIdl` ASC) INVISIBLE,
  INDEX `FK_product_provider` (`providerId` ASC) INVISIBLE,
  INDEX `FK_product_product` (`productId` ASC) INVISIBLE,
  CONSTRAINT `FK_product_category`
    FOREIGN KEY (`categoryIdl`)
    REFERENCES `services`.`category` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `FK_product_provider`
    FOREIGN KEY (`providerId`)
    REFERENCES `services`.`provider` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `FK_product_product`
    FOREIGN KEY (`productId`)
    REFERENCES `services`.`product` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

	ALTER TABLE `services`.`stock` 
ADD COLUMN `userId` BIGINT(10) NULL AFTER `form`,
ADD INDEX `FK_product_user` (`userId` ASC) VISIBLE;
;
ALTER TABLE `services`.`stock` 
ADD CONSTRAINT `FK_product_user`
  FOREIGN KEY (`userId`)
  REFERENCES `services`.`user` (`id`)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;
;
ALTER TABLE `services`.`customer` 
DROP FOREIGN KEY `fk_customer_enterprise1`,
DROP FOREIGN KEY `fk_customer_address`;
ALTER TABLE `services`.`customer` 
DROP COLUMN `twitterAccount`,
DROP COLUMN `facebookAccount`,
DROP COLUMN `civilState`,
DROP COLUMN `birthDate`,
DROP COLUMN `password`,
DROP COLUMN `login`,
DROP COLUMN `enterpriseId`,
CHANGE COLUMN `addressId` `address` VARCHAR(255) NULL DEFAULT NULL ,
DROP INDEX `fk_customer_address` ,
DROP INDEX `fk_customer_enterprise1` ,
DROP INDEX `login` ;
;

ALTER TABLE `services`.`customer` 
CHANGE COLUMN `gender` `DNI` VARCHAR(45) NULL DEFAULT NULL ;
INSERT INTO `services`.`customer` (`id`, `firstName`, `lastName`, `creationDate`, `email`, `phoneNumber`, `DNI`, `enabled`, `address`) VALUES ('1', 'Yamelis', 'Almea', '2013-09-09 11:39:17', 'yamealm@gmail.com', '2612594080', '95931962', '1', 'CABA');

ALTER TABLE `services`.`customer` 
CHANGE COLUMN `id` `id` BIGINT(5) NOT NULL ;

ALTER TABLE `services`.`stock` 
ADD COLUMN `customerId` BIGINT(5) NULL AFTER `userId`,
ADD INDEX `FK_product_customer` (`customerId` ASC) VISIBLE;
;
ALTER TABLE `services`.`stock` 
ADD CONSTRAINT `FK_product_customer`
  FOREIGN KEY (`id`)
  REFERENCES `services`.`customer` (`id`)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;

  ALTER TABLE `services`.`stock` 
ADD COLUMN `orderWord` VARCHAR(255) NULL AFTER `customerId`;
ALTER TABLE `services`.`stock` 
ADD COLUMN `quarantineReason` VARCHAR(255) NULL DEFAULT NULL AFTER `orderWord`;

ALTER TABLE `services`.`stock` 
ADD COLUMN `departureDate` VARCHAR(45) NULL AFTER `categoryIdl`,
CHANGE COLUMN `customerId` `customerId` BIGINT(5) NULL DEFAULT NULL AFTER `departureDate`,
CHANGE COLUMN `EndingDate` `endingDate` DATETIME NULL DEFAULT NULL ;

//nuevo
ALTER TABLE `services`.`condition` 
ADD COLUMN `enabled` TINYINT(1) NOT NULL DEFAULT 0 AFTER `name`;

ALTER TABLE `services`.`customer` 
CHANGE COLUMN `DNI` `dni` VARCHAR(45) NULL DEFAULT NULL ;

ALTER TABLE `services`.`product` 
CHANGE COLUMN `partNumber` `partNumber` VARCHAR(45) NOT NULL AFTER `id`,
CHANGE COLUMN `description` `description` VARCHAR(45) NOT NULL AFTER `partNumber`;

ALTER TABLE `services`.`product` 
ADD COLUMN `enabled` TINYINT(1) NOT NULL DEFAULT 0 AFTER `amount`;

ALTER TABLE `services`.`product` 
CHANGE COLUMN `act_np_nsn` `actNpNsn` VARCHAR(45) NOT NULL ;

ALTER TABLE `services`.`product` 
ADD COLUMN `ubicationFolder` VARCHAR(45) NULL AFTER `ubicationBox`,
CHANGE COLUMN `ubication` `ubicationBox` VARCHAR(45) NOT NULL ;

ALTER TABLE `services`.`stock` 
;
ALTER TABLE `services`.`stock` 
ADD CONSTRAINT `FK_product_category`
  FOREIGN KEY (`categoryId`)
  REFERENCES `services`.`category` (`id`)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT,
ADD CONSTRAINT `FK_product_customer`
  FOREIGN KEY (`customerId`)
  REFERENCES `services`.`customer` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `Fk_product_condicion`
  FOREIGN KEY (`conditionId`)
  REFERENCES `services`.`condition` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  
  ALTER TABLE `services`.`stock` 
RENAME TO  `services`.`transaction` ;

ALTER TABLE `services`.`transaction` 
DROP COLUMN `endingDate`,
DROP COLUMN `departureDate`,
CHANGE COLUMN `serial` `isSerial` TINYINT(1) NULL DEFAULT NULL ;

ALTER TABLE `services`.`provider` 
DROP COLUMN `isSMSProvider`,
CHANGE COLUMN `url` `address` VARCHAR(255) NOT NULL ;


ALTER TABLE `services`.`transaction` 
ADD PRIMARY KEY (`id`);
;

CREATE TABLE `services`.`product_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `productId` bigint(5) NOT NULL,
  `transactionId` bigint(5) NOT NULL,
  `currentQuantity` int(3) NOT NULL,
  `oldQuantity` int(3) DEFAULT NULL,
  `creationDate` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_product_history_product1` (`productId`),
  KEY `fk_product_history_transaction` (`transactionId`),
  CONSTRAINT `fk_product_history_product1` FOREIGN KEY (`productId`) REFERENCES `product` (`id`),
  CONSTRAINT `fk_product_history_transaction` FOREIGN KEY (`transactionId`) REFERENCES `transaction` (`id`)
)ENGINE=InnoDB;

CREATE TABLE `services`.`product_serie` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `productId` bigint(5) NOT NULL,
  `providerId` bigint(5) NOT NULL,
  `beginTransactionId` bigint(5) NOT NULL,
  `endingTransactionId` bigint(5) DEFAULT NULL,
  `serie` VARCHAR(45) NOT NULL,
  `amount` Float(10,2) NOT NULL,
  `expirationDate` datetime NOT NULL,
  `cure` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_product_serie_product` (`productId`),
  KEY `fk_product_serie_provider` (`providerId`),
  KEY `fk_product_serie_transaction1` (`beginTransactionId`),
  KEY `fk_product_serie_transaction2` (`endingTransactionId`),
  CONSTRAINT `fk_product_serie_product` FOREIGN KEY (`productId`) REFERENCES `product` (`id`),
  CONSTRAINT `fk_product_serie_provider` FOREIGN KEY (`providerId`) REFERENCES `provider` (`id`),
  CONSTRAINT `fk_product_serie_transaction1` FOREIGN KEY (`beginTransactionId`) REFERENCES `transaction` (`id`),
  CONSTRAINT `fk_product_serie_transaction2` FOREIGN KEY (`endingTransactionId`) REFERENCES `transaction` (`id`)
)ENGINE=InnoDB;

CREATE TABLE `services`.`product_expiration` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `productId` bigint(5) NOT NULL,
  `providerId` bigint(5) NOT NULL,
  `beginTransactionId` bigint(5) NOT NULL,
  `quantity` int(4) NOT NULL,
  `amount` Float(10,2) NOT NULL,
  `expirationDate` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_product_expiration_product` (`productId`),
  KEY `fk_product_expiration_provider` (`providerId`),
  KEY `fk_product_expiration_transaction1` (`beginTransactionId`),
  CONSTRAINT `fk_product_expiration_product` FOREIGN KEY (`productId`) REFERENCES `product` (`id`),
  CONSTRAINT `fk_product_expiration_provider` FOREIGN KEY (`providerId`) REFERENCES `provider` (`id`),
  CONSTRAINT `fk_product_expiration_transaction1` FOREIGN KEY (`beginTransactionId`) REFERENCES `transaction` (`id`)
)ENGINE=InnoDB;


//13-06-2019
CREATE TABLE `services`.`transaction_type` (
  `id` int(3) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

ALTER TABLE `services`.`transaction` 
ADD COLUMN `transactionTypeId` INT(3) NULL AFTER `userId`,
ADD INDEX `Fk_product_transactionType` (`transactionTypeId` ASC) INVISIBLE;
;
ALTER TABLE `services`.`transaction` 
ADD CONSTRAINT `Fk_product_transactionType`
  FOREIGN KEY (`transactionTypeId`)
  REFERENCES `services`.`transaction_type` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

  
  //Yamelis 20-06-2019
UPDATE `services`.`provider` SET `name` = 'provider1', `address` = 'CABA' WHERE (`id` = '1');
UPDATE `services`.`provider` SET `name` = 'provider2', `address` = 'Cordoba', `enabled` = '1' WHERE (`id` = '2');

INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('91', '1', 'listStock', 'product', 'listStock', '1');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('179', '91', '1', 'Stock Product', 'Stock Product');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('180', '91', '1', 'Stock de Productos', 'Stock de Productos');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1476', '91', '1');


