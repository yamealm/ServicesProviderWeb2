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

CREATE TABLE `condition` (
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
DROP INDEX `fk_customer_address` ;
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