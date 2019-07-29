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

ALTER TABLE `services`.`product_history` 
ADD COLUMN `currentAmount` FLOAT(10,2) NULL DEFAULT 0.00 AFTER `creationDate`,
ADD COLUMN `oldAmount` FLOAT(10,2) NULL DEFAULT 0.00 AFTER `currentAmount`;

//Yamelis Almea 22-06-2019
ALTER TABLE `services`.`product_expiration` 
ADD COLUMN `cure` DATETIME NULL AFTER `expirationDate`;

ALTER TABLE `services`.`product` 
ADD COLUMN `isSerial` TINYINT(1) NULL AFTER `enabled`,
ADD COLUMN `isExpiration` TINYINT(1) NULL AFTER `isSerial`;

ALTER TABLE `services`.`transaction` 
DROP COLUMN `isSerial`;

ALTER TABLE `services`.`product_serie` 
CHANGE COLUMN `expirationDate` `expirationDate` DATETIME NULL DEFAULT NULL ;

DROP TABLE `services`.`product_expiration`;

ALTER TABLE `services`.`product_serie` 
CHARACTER SET = latin1 , COLLATE = latin1_bin ,
ADD COLUMN `quantity` INT(5) NOT NULL DEFAULT 0 AFTER `cure`;

ALTER TABLE `services`.`product_serie` 
ADD COLUMN `enabled` TINYINT(1) NOT NULL DEFAULT 0 AFTER `quantity`;


ALTER TABLE `services`.`product_serie` 
ADD COLUMN `creationDate` DATETIME NOT NULL AFTER `endingTransactionId`,
CHANGE COLUMN `amount` `amount` FLOAT(10,2) NOT NULL AFTER `creationDate`,
CHANGE COLUMN `enabled` `endingDate` DATETIME NULL AFTER `amount`,
CHANGE COLUMN `quantity` `quantity` INT(5) NOT NULL DEFAULT '0' AFTER `endingDate`,
CHANGE COLUMN `serie` `serie` VARCHAR(45) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_0900_ai_ci' NULL ;


ALTER TABLE `services`.`product_serie` 
CHANGE COLUMN `endingDate` `endingDate` DATETIME NULL DEFAULT NULL AFTER `creationDate`;

ALTER TABLE `services`.`product_serie` 
CHANGE COLUMN `serie` `serie` VARCHAR(45) CHARACTER SET 'latin1' COLLATE 'latin1_bin' NULL DEFAULT NULL ;


ALTER TABLE `services`.`transaction` 
ADD COLUMN `amount` FLOAT(10,2) NOT NULL DEFAULT 0.00 AFTER `quarantineReason`;

UPDATE `services`.`condition` SET `enabled` = '1' WHERE (`id` = '1');
UPDATE `services`.`condition` SET `enabled` = '1' WHERE (`id` = '2');
UPDATE `services`.`condition` SET `enabled` = '1' WHERE (`id` = '3');
UPDATE `services`.`condition` SET `enabled` = '1' WHERE (`id` = '4');


ALTER TABLE `services`.`condition` 
CHANGE COLUMN `enabled` `enabled` TINYINT(1) NOT NULL ;

ALTER TABLE `services`.`transaction` 
DROP FOREIGN KEY `FK_product_category`;
ALTER TABLE `services`.`transaction` 
CHANGE COLUMN `categoryIdl` `categoryId` INT(3) NOT NULL ;
ALTER TABLE `services`.`transaction` 
ADD CONSTRAINT `FK_product_category`
  FOREIGN KEY (`categoryId`)
  REFERENCES `services`.`category` (`id`);

ALTER TABLE `services`.`condition` 
RENAME TO  `services`.`condicion` ;  

DELETE FROM `services`.`currency` WHERE (`id` = '2');
UPDATE `services`.`currency` SET `name` = 'PESOS' WHERE (`id` = '1');

ALTER TABLE `services`.`transaction` 
CHANGE COLUMN `observation` `observation` VARCHAR(45) CHARACTER SET 'latin1' COLLATE 'latin1_bin' NULL ;

ALTER TABLE `services`.`transaction` 
CHANGE COLUMN `orderWord` `orderWord` VARCHAR(45) CHARACTER SET 'latin1' COLLATE 'latin1_bin' NULL ,
CHANGE COLUMN `quarantineReason` `quarantineReason` VARCHAR(45) CHARACTER SET 'latin1' COLLATE 'latin1_bin' NULL ;

ALTER TABLE `services`.`product_serie` 
CHANGE COLUMN `id` `id` BIGINT(10) NOT NULL ;

ALTER TABLE `services`.`product_serie` 
CHANGE COLUMN `id` `id` BIGINT(10) NOT NULL AUTO_INCREMENT ;


ALTER TABLE `services`.`transaction` 
;
ALTER TABLE `services`.`transaction` ALTER INDEX `FK_product_customer` INVISIBLE;
ALTER TABLE `services`.`transaction` 
ADD CONSTRAINT `FK_product_customer`
  FOREIGN KEY (`customerId`)
  REFERENCES `services`.`customer` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  
  ALTER TABLE `services`.`transaction` 
DROP FOREIGN KEY `Fk_product_transactionType`;
ALTER TABLE `services`.`transaction` 
CHANGE COLUMN `transactionTypeId` `transactionTypeId` INT(3) NOT NULL ;
ALTER TABLE `services`.`transaction` 
ADD CONSTRAINT `Fk_product_transactionType`
  FOREIGN KEY (`transactionTypeId`)
  REFERENCES `services`.`transaction_type` (`id`);
  
  ALTER TABLE `services`.`product` 
DROP COLUMN `isExpiration`,
DROP COLUMN `isSerial`;
  
  
 UPDATE `services`.`condicion` SET `name` = 'NEW' WHERE (`id` = '1');
UPDATE `services`.`condicion` SET `name` = 'REPAIRED' WHERE (`id` = '2');
UPDATE `services`.`condicion` SET `name` = 'OVERHAULD' WHERE (`id` = '3');
UPDATE `services`.`condicion` SET `name` = 'INPECTED' WHERE (`id` = '4');
 
 UPDATE `services`.`permission_data` SET `alias` = 'Add Stock', `description` = 'Add Stock' WHERE (`id` = '179');
UPDATE `services`.`permission_data` SET `alias` = 'Agregar Stock', `description` = 'Agregar Stock' WHERE (`id` = '180');
 UPDATE `services`.`permission` SET `action` = 'addStock', `name` = 'addStock' WHERE (`id` = '91');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('92', '1', 'removeStock', 'product', 'removeStock', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('93', '1', 'addTransist', 'product', 'addTransist', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('94', '1', 'removeTransist', 'product', 'removeTransist', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('95', '1', 'addQuarantine', 'product', 'addQuarantine', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('96', '1', 'removeQuarantine', 'product', 'removeQuarantine', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('97', '1', 'addWait', 'product', 'addWait', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('98', '1', 'removeWait', 'product', 'removeWait', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('99', '1', 'metereological_control', 'product', 'metereological_control', '1');
 
 INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('100', '1', 'monitoring', 'product', 'monitoring', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('101', '1', 'reportStock', 'product', 'reportStock', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('102', '1', 'reportTransit', 'product', 'reportTransit', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('103', '1', 'reportQuarantine', 'product', 'reportQuarantine', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('104', '1', 'reportWait', 'product', 'reportWait', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('105', '1', 'reportMeteorologicalControl', 'product', 'reportMeteorologicalControl', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('106', '1', 'Stock', 'product', 'Stock', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('107', '1', 'Transit', 'product', 'Transit', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('108', '1', 'Quarantine', 'product', 'Quarantine', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('109', '1', 'Wait', 'product', 'Wait', '1');
 
 INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('181', '92', '1', 'Egress Stock', 'Egress Stock');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('182', '92', '2', 'Egreso de Stock', 'Egreso de Stock');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('183', '93', '1', 'Add Transit', 'Add Transit');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('184', '93', '2', 'Agregar Transito', 'Agregar Transito');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('185', '94', '1', 'Egress Transit', 'Egress Transit');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('186', '94', '2', 'Egresar Transito', 'Egresar Transito');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('187', '95', '1', 'Add Quarantine', 'Add Quarantine');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('188', '95', '2', 'Agregar Cuarentena', 'Agregar Cuarentena');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('189', '96', '1', 'Egress Quarantine', 'Egress Quarantine');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('190', '96', '2', 'Egresar Cuarentena', 'Egresar Cuarentena');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('191', '97', '1', 'Add Wait', 'Add Wait');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('192', '97', '2', 'Agregar Espera', 'Agregar Espera');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('193', '98', '1', 'Egress Wait', 'Egress Wait');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('194', '98', '2', 'Egresar Espera', 'Egresar Espera');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('195', '99', '1', 'Meteorological Control', 'Meteorological Control');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('196', '99', '2', 'Control Meteorologico', 'Control Meteorologico');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('197', '100', '1', 'Monotoring', 'Monotoring');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('198', '100', '2', 'Monitoreo', 'Monitoreo');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('199', '101', '1', 'Report Stock', 'Report Stock');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('200', '101', '2', 'Reporte de Stock', 'Reporte de Stoc');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('201', '102', '1', 'Report Transit', 'Report Transit');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('202', '102', '2', 'Reporte de Transito', 'Reporte de Transito');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('203', '103', '1', 'Report Quarantine', 'Report Quarantine');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('204', '103', '2', 'Reporte de Cuarentena', 'Reporte de Cuarentena');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('205', '104', '1', 'Report Wait', 'Report Wait');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('206', '104', '2', 'Reporte de Espera', 'Reporte de Espera');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('207', '105', '1', 'Report Meteorological Control', 'Report Meteorological Control');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('208', '105', '2', 'Reporte de Control Meteorologico', 'Reporte de Control Meteorologico');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('209', '106', '1', 'Stock', 'Stock');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('210', '106', '2', 'Stock', 'Stock');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('211', '107', '1', 'Transit', 'Transit');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('212', '107', '2', 'Transito', 'Transito');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('213', '108', '1', 'Quarantine', 'Quarantine');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('214', '108', '2', 'Cuarentena', 'Cuarentena');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('215', '109', '1', 'Wait', 'Wait');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('216', '109', '2', 'Espera', 'Espera');
 
 UPDATE `services`.`permission_group_data` SET `alias` = 'Report Management', `description` = 'Report Management' WHERE (`id` = '7');
UPDATE `services`.`permission_group_data` SET `alias` = 'Gesti\F3n  Reportes', `description` = 'Gesti\F3n  Reportes' WHERE (`id` = '8');
 
 INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1477', '92', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1478', '93', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1479', '94', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1480', '95', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1481', '96', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1482', '97', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1483', '98', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1484', '99', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1485', '100', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1486', '101', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1487', '102', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1488', '103', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1489', '104', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1490', '105', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1491', '106', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1492', '107', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1493', '108', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1494', '109', '1');

//Yamelis 24-006-2019
INSERT INTO `services`.`customer` (`id`, `firstName`, `lastName`, `creationDate`, `email`, `phoneNumber`, `dni`, `enabled`, `address`) VALUES ('2', 'Luis', 'Ramos', '2013-09-09 11:39:17', 'luisrafa@hotmail.com', '9113562536', '95369258', '1', 'CABA');

ALTER TABLE `services`.`product_serie` 
ADD COLUMN `categoryId` INT(3) NULL AFTER `cure`,
ADD COLUMN `conditionId` INT(3) NULL AFTER `categoryId`;

ALTER TABLE `services`.`product_serie` 
ADD INDEX `fk_product_serie_category_idx` (`categoryId` ASC) VISIBLE,
ADD INDEX `fk_product_serie_condition_idx` (`conditionId` ASC) VISIBLE;
ALTER TABLE `services`.`product_serie` ALTER INDEX `fk_product_serie_transaction2` INVISIBLE;
ALTER TABLE `services`.`product_serie` 
ADD CONSTRAINT `fk_product_serie_category`
  FOREIGN KEY (`categoryId`)
  REFERENCES `services`.`category` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_product_serie_condition`
  FOREIGN KEY (`conditionId`)
  REFERENCES `services`.`condicion` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  
  UPDATE `services`.`category` SET `name` = ' TRANSIT' WHERE (`id` = '2');
UPDATE `services`.`category` SET `name` = 'QUARANTINE' WHERE (`id` = '3');
UPDATE `services`.`category` SET `name` = 'WAIT' WHERE (`id` = '4');
UPDATE `services`.`category` SET `name` = 'METEOROLOGICAL CONTROL' WHERE (`id` = '5');

UPDATE `services`.`product_serie` SET `categoryId` = '1', `conditionId` = '1' WHERE (`id` = '2');
UPDATE `services`.`product_serie` SET `categoryId` = '1', `conditionId` = '1' WHERE (`id` = '3');
UPDATE `services`.`product_serie` SET `categoryId` = '1', `conditionId` = '1' WHERE (`id` = '4');

ALTER TABLE `services`.`transaction` 
CHANGE COLUMN `invoice` `invoice` VARCHAR(45) CHARACTER SET 'latin1' COLLATE 'latin1_bin' NULL ;

// revisado con kerwin 25-06-2019

INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('110', '1', 'viewStock', 'product', 'viewStock', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('111', '1', 'editStock', 'product', 'editStock', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('112', '1', 'viewTransit', 'product', 'viewTransit', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('113', '1', 'editTransit', 'product', 'editTransit', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('114', '1', 'viewQuarantine', 'product', 'viewQuarantine', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('115', '1', 'editQuarantine', 'product', 'editQuarantine', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('116', '1', 'viewWait', 'producut', 'viewWait', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('117', '1', 'editWait', 'product', 'editWait', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('118', '1', 'viewMeterologicalControl', 'prodcut', 'viewMeterologicalControl', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('119', '1', 'editMeterologicalControl', 'product', 'editMeterologicalControl', '1');


INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1495', '110', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1496', '111', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1497', '112', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1498', '113', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1499', '114', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1500', '115', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1501', '116', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1502', '117', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1503', '118', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1504', '119', '1');

//YAMELIS 08/07/2019
UPDATE `services`.`permission` SET `action` = 'metereologicalControl', `name` = 'metereologicalControl' WHERE (`id` = '99');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('120', '1', 'addMetereologicalControl', 'product', 'addMetereologicalControl', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('121', '1', 'removeMetereologicalControl', 'product', 'removeMetereologicalControl', '1');

INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('217', '110', '1', 'Add Meteorological Control', 'Add Meteorological Control');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('218', '110', '2', 'Agregar Control Meteorologico', 'Agregar Control Meteorologico');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('219', '111', '1', 'Egress Meteorological Control', 'Egress Meteorological Control');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('220', '112', '2', 'Egresar Control Meteorologico', 'Egresar Control Meteorologico');

INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1505', '120', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1506', '121', '1');

ALTER TABLE `services`.`product_serie` 
ADD COLUMN `customerId` BIGINT(5) NULL DEFAULT NULL AFTER `conditionId`,
ADD COLUMN `orderWord` VARCHAR(150) CHARACTER SET 'latin1' COLLATE 'latin1_bin' NULL DEFAULT NULL AFTER `customerId`,
ADD INDEX `fk_product_serie_customer_idx` (`customerId` ASC) VISIBLE;
ALTER TABLE `services`.`product_serie` ALTER INDEX `fk_product_serie_condition_idx` INVISIBLE;
ALTER TABLE `services`.`product_serie` 
ADD CONSTRAINT `fk_product_serie_customer`
  FOREIGN KEY (`customerId`)
  REFERENCES `services`.`customer` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


ALTER TABLE `services`.`product_serie` 
ADD COLUMN `quarantineReason` VARCHAR(250) NULL DEFAULT NULL AFTER `orderWord`;

ALTER TABLE `services`.`transaction` 
ADD COLUMN `work` VARCHAR(150) CHARACTER SET 'latin1' COLLATE 'latin1_bin' NULL DEFAULT NULL AFTER `amount`;

ALTER TABLE `services`.`product_serie` 
ADD COLUMN `work` VARCHAR(150) CHARACTER SET 'latin1' COLLATE 'latin1_bin' NULL DEFAULT NULL AFTER `quarantineReason`;

INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('221', '112', '1', 'View Transit', 'View Transit');
UPDATE `services`.`permission_data` SET `permissionId` = '111', `alias` = 'Editar Stock', `description` = 'Editar Stock' WHERE (`id` = '220');
UPDATE `services`.`permission_data` SET `alias` = 'View Stock', `description` = 'View Stock' WHERE (`id` = '217');
UPDATE `services`.`permission_data` SET `alias` = 'Ver Stock', `description` = 'Ver Stock' WHERE (`id` = '218');
UPDATE `services`.`permission_data` SET `alias` = 'Edit Stock', `description` = 'Edit Stock' WHERE (`id` = '219');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('222', '112', '2', 'Ver Transito', 'Ver Transito');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('223', '113', '1', 'Edit Transit', 'Edit Transit');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('224', '113', '2', 'Editar Transito', 'Editar Transito');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('225', '114', '1', 'View Quarantine', 'View Quarantine');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('226', '114', '2', 'Ver Cuarentena', 'Ver Cuarentena');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('227', '115', '1', 'Edit Quarantine', 'Edit Quarantine');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('228', '115', '2', 'Editar Cuarentena', 'Editar Cuarentena');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('229', '116', '1', 'View Wait', 'View Wait');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('230', '116', '2', 'Ver Espera', 'Ver Espera');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('231', '117', '1', 'Edit Wait', 'Edit Wait');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('232', '117', '2', 'Editar Espera', 'Editar Espera');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('233', '118', '1', 'View Metrological Control', 'View Metrological Control');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('234', '118', '2', 'Ver Control Metrologico', 'Ver Control Metrologico');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('235', '119', '1', 'Edit Metrological Control', 'Edit Metrological Control');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('236', '119', '2', 'Editar Control Metrologico', 'Editar Control Metrologico');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('237', '120', '1', 'Add Metrological Control', 'Add Metrological Control');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('238', '120', '2', 'Agregar Control Metrologico', 'Agregar Control Metrologico');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('239', '121', '1', 'Egress Metrological Control', 'Egress Metrological Control');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('240', '121', '2', 'Egresar Control Metrologico', 'Egresar Control Metrologico');


CREATE TABLE `services`.`braund` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE `services`. `model`(
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `braundI` int(5) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_braund_model` (`braundI`),
  CONSTRAINT `fk_braund_model` FOREIGN KEY (`braundI`) REFERENCES `braund` (`id`)
) ENGINE=InnoDB;

CREATE TABLE `services`. `enter_calibration`(
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE `services`. `control_type`(
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;


CREATE TABLE `services`.`metrological_control` (
  `id` bigint(3) NOT NULL AUTO_INCREMENT,
  `braundId` int(5) NOT NULL,
  `modelId` int(5) NOT NULL,
  `name` varchar(75) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `serie` varchar(45) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `rango` varchar(45) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `creationDate` datetime NOT NULL,
  `enterCalibrationId` int(5) NOT NULL,
  `ubication` varchar(75) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `scale` varchar(75) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `controlTypeId` int(5) DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_metrological_control_braund` (`braunId`),
  KEY `fk_metrological_control_model` (`modelId`),
  KEY `fk_metrological_control_enterCalibration` (`enterCalibrationId`),
  KEY `fk_metrological_control_controlType` (`controlTypeId`),
  CONSTRAINT `fk_metrological_control_braund` FOREIGN KEY (`braundId`) REFERENCES `braund` (`id`),
  CONSTRAINT `fk_metrological_control_model` FOREIGN KEY (`modelId`) REFERENCES `model` (`id`),
  CONSTRAINT `fk_metrological_control_enterCalibration` FOREIGN KEY (`enterCalibrationId`) REFERENCES `enter_calibration` (`id`),
  CONSTRAINT `fk_metrological_control_controlType` FOREIGN KEY (`controlTypeId`) REFERENCES `control_type` (`id`)
) ENGINE=InnoDB;


CREATE TABLE `services`.`metrological_control_history` (
  `id` bigint(3) NOT NULL AUTO_INCREMENT,
  `metrologicalControlId` bigint(3) NOT NULL,
  `creationDate` datetime NOT NULL,
  `calibrationDate` datetime NOT NULL,
  `calibrationDateOld` datetime NOT NULL,
  `expirationDate` datetime DEFAULT NULL,
  `observation` varchar(150) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_metrological_control_history` (`metrologicalControlId`),
  CONSTRAINT `fk_metrological_control_history` FOREIGN KEY (`metrologicalControlId`) REFERENCES `metrological_control` (`id`)
) ENGINE=InnoDB;

ALTER TABLE `services`.`metrological_control_history` 
CHANGE COLUMN `calibrationDateOld` `calibrationDateOld` DATETIME NULL ;


INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('122', '1', 'listMarke', 'braund', 'listMarke', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('123', '1', 'addMarke', 'braund', 'addMarke', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('124', '1', 'editMarke', 'braund', 'editMarke', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('125', '1', 'viewMarke', 'braund', 'viewMarke', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('126', '1', 'listModels', 'model', 'listModels', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('127', '1', 'addModel', 'model', 'addModel', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('128', '1', 'editModel', 'model', 'editModel', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('129', '1', 'viewModel', 'model', 'viewModel', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('130', '1', 'removeMarke', 'braund', 'removeMarke', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('131', '1', 'removeModel', 'model', 'removeModel', '1');

INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('241', '122', '1', 'List Makes', 'List Makes');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('242', '122', '2', 'Listar Marcas', 'Listar Marcas');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('243', '123', '1', 'Add Make', 'Add Make');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('244', '123', '2', 'Agregar Marca', 'Agregar Marca');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('245', '124', '1', 'Edit Make', 'Edit  Make');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('246', '124', '2', 'Editar Marca', 'Editar Marca');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('247', '125', '1', 'View  Make', 'View  Make');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('248', '125', '2', 'Ver Marca', 'Ver Marca');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('249', '126', '1', 'List Models', 'List Models');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('250', '126', '2', 'Listar Modelos', 'Listar Modelos');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('251', '127', '1', 'Add Model', 'Add Model');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('252', '127', '2', 'Agregar Modelo', 'Agregar Modelo');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('253', '128', '1', 'Edit Model', 'Edit Model');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('254', '128', '2', 'Editar Modelo', 'Editar Modelo');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('255', '129', '1', 'View Model', 'View Model');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('256', '129', '2', 'Ver Modelo', 'Ver Modelo');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('257', '130', '1', 'Delete Marke', 'Delete Marke');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('258', '130', '2', 'Eliminar Marca', 'Eliminar Marca');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('259', '131', '1', 'Delete Model', 'Delete Model');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('260', '131', '2', 'Eliminar Modelo', 'Eliminar Modelo');

UPDATE `services`.`permission_data` SET `alias` = 'List Markes', `description` = 'List Markes' WHERE (`id` = '241');
UPDATE `services`.`permission_data` SET `alias` = 'Delete Mark', `description` = 'Delete Mark' WHERE (`id` = '257');
UPDATE `services`.`permission_data` SET `alias` = 'Add Mark', `description` = 'Add Mark' WHERE (`id` = '243');
UPDATE `services`.`permission_data` SET `alias` = 'Edit Mark', `description` = 'Edit  Mark' WHERE (`id` = '245');
UPDATE `services`.`permission_data` SET `alias` = 'View  Mark', `description` = 'View  Mark' WHERE (`id` = '247');

INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1507', '122', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1508', '123', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1509', '124', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1510', '125', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1511', '126', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1512', '127', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1513', '128', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1514', '129', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1515', '130', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1516', '131', '1');


ALTER TABLE `services`.`model` 
DROP FOREIGN KEY `fk_braund_model`;
ALTER TABLE `services`.`model` 
CHANGE COLUMN `braundI` `braundId` INT(5) NOT NULL ;
ALTER TABLE `services`.`model` 
ADD CONSTRAINT `fk_braund_model`
  FOREIGN KEY (`braundId`)
  REFERENCES `services`.`braund` (`id`);
  
  //Yamelis 29/07/2019
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('132', '1', 'listCalibrationEntity', 'enter_calibration', 'listCalibrationEntity', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('133', '1', 'addCalibrationEntity', 'enter_calibration', 'addCalibrationEntity', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('134', '1', 'editCalibrationEntity', 'enter_calibration', 'editCalibrationEntity', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('135', '1', 'viewCalibrationEntity', 'enter_calibration', 'viewCalibrationEntity', '1');
INSERT INTO `services`.`permission` (`id`, `permissionGroupId`, `action`, `entity`, `name`, `enabled`) VALUES ('136', '1', 'removeCalibrationEntity', 'enter_calibration', 'removeCalibrationEntity', '1');

INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('261', '132', '1', 'List Calibration Entities', 'List Calibration Entities');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('262', '132', '2', 'Listar Entes de Calibracion', 'Listar Entes de Calibracion');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('263', '133', '1', 'Add Calibration Entity', 'Add  Calibration Entity');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('264', '133', '2', 'Agregar Ente de Calibracion', 'Agragar Ente de Calibracion');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('265', '134', '1', 'Edit  Calibration Entity', 'Edit  Calibration Entity');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('266', '134', '2', 'Editar Ente de Calibracion', 'Editar Ente de Calibracion');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('267', '135', '1', 'View  Calibration Entity', 'View  Calibration Entity');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('268', '135', '2', 'Ver Ente de Calibracion', 'Ver Ente de Calibracion');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('269', '136', '1', 'Delete  Calibration Entity', 'Delete  Calibration Entity');
INSERT INTO `services`.`permission_data` (`id`, `permissionId`, `languageId`, `alias`, `description`) VALUES ('270', '136', '2', 'Eliminar Ente de Calibracion', 'Eliminar Ente de Calibracion');

INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1517', '132', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1518', '133', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1519', '134', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1520', '135', '1');
INSERT INTO `services`.`permission_has_profile` (`id`, `permissionId`, `profileId`) VALUES ('1521', '136', '1');




