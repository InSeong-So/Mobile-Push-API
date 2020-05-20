# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: dev.korearunner.com (MySQL 5.5.5-10.4.12-MariaDB)
# Database: MobilePMS
# Generation Time: 2020-05-18 05:39:33 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table APPINFO
# ------------------------------------------------------------

DROP TABLE IF EXISTS `APPINFO`;

CREATE TABLE `APPINFO` (
  `APPID` varchar(40) NOT NULL DEFAULT '',
  `APIKEY` varchar(255) NOT NULL DEFAULT '',
  `CRDT` datetime DEFAULT NULL,
  `TOKEN` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`APPID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table TB_DEVICE
# ------------------------------------------------------------

DROP TABLE IF EXISTS `TB_DEVICE`;

CREATE TABLE `TB_DEVICE` (
  `UUID` varchar(255) NOT NULL,
  `APPID` varchar(128) NOT NULL,
  `TOKEN` varchar(256) NOT NULL,
  `OS` char(1) NOT NULL DEFAULT 'A',
  `AGRDT` datetime DEFAULT NULL,
  `RCVYN` char(1) NOT NULL DEFAULT 'Y',
  `MBRID` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`UUID`,`APPID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
