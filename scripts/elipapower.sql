-- MySQL dump 10.16  Distrib 10.2.14-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: elipapower
-- ------------------------------------------------------
-- Server version	10.2.14-MariaDB-10.2.14+maria~xenial-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `elipapower`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `elipapower` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `elipapower`;

--
-- Table structure for table `errorcode`
--

DROP TABLE IF EXISTS `errorcode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `errorcode` (
  `errorcodeid` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) NOT NULL,
  `messagecode` varchar(20) NOT NULL,
  PRIMARY KEY (`errorcodeid`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `errorcode`
--

LOCK TABLES `errorcode` WRITE;
/*!40000 ALTER TABLE `errorcode` DISABLE KEYS */;
INSERT INTO `errorcode` VALUES (1,'Successful','elec000'),(2,'General Error (see message for details)','elec001'),(3,'Service not available (e.g. our connection to a service provider is down)','elec002'),(4,'No record of previous transaction. The value of <ref> in the XML messages sent to iPay is not recognized. Typically used when trying to specify the ref of a previous message (e.g. reversal).','elec003'),(5,'Warning - reversals are not supported by the suppliers system. The supplier\'s system does not support reversals. Note that the supplier\'s system might very well support an automatic re-vend of the transaction that is being reversed.','elec004'),(6,'Unknown meter number.','elec010'),(7,'Meter is blocked','elec011'),(8,'Too much debt. An account with a debt to be repaid is linked to this meter. This debt must be repaid before vending can take place.','elec012'),(9,'Invalid Amount. The upper or lower limit on the amount of elec that may be purchased in a single transaction or over a period of time has been passed.','elec013'),(10,'Invalid Number of Tokens. The upper or lower limit on the number of tokens that may be purchased in a single transaction or over a period of time has been passed.','elec014'),(11,'Amount too high. Request amount exceeds the maximum for this meter. Try a smaller amount.','elec015'),(12,'Amount too low. Request amount exceeds the minimum amount for this meter. Try a larger amount.','elec016'),(13,'No free BSST token is due.','elec017'),(14,'Multiple tokens not supported. Vending of multiple tokens in one transaction not supported.','elec018'),(15,'Already reversed. The request cannot be processed due to the past transaction it refers to already having been reversed.','elec019'),(16,'Transaction already completed. The request cannot be processed due to the past transaction it refers to already having been completed.','elec020'),(17,'Duplicate meter number. Meter number is supported by more than 1 supplier.','elec021'),(18,'Meter is blocked. The token supplier refuses to vend to this meter.','elec022'),(19,'Invalid PaymentType. The payment type specified in the request in not recognized.','elec023'),(20,'Balance too low. For those on a prepaid account with the supplier','elec028'),(21,'Invalid replace reference. The reference provided in the request for a replacement token is invalid.','elec029'),(22,'Invalid XmlVend Format. The format of XmlVend request or response is incorrect.','elec030'),(23,'BSST token must be vended as part of sale. A BSST token cannot be obtained unless a standard resource token is purchased.','elec031'),(24,'Update meter key. Information supplied not the same as database. First Update Meter Key and encode Meter Card.','elec032'),(25,'Track 2 meter mismatch. The meter number and the track 2 data are not consistent.','elec033'),(26,'Test vend not supported. The supplier does not support doing test vends. See isTrial attribute.','elec034'),(27,'Pending key change tokens','elec038'),(28,'Invalid meter length. Meter length is outside of configured restrictions for minimum and maximum length.','elec041'),(29,'General system error. (see message for details)','elec900'),(30,'Unsupported message version number.','elec901'),(31,'Invalid Reference. The value of the <ref> element in invalid (e.g. It\'s too long or empty)','elec902');
/*!40000 ALTER TABLE `errorcode` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `serialnumber`
--

DROP TABLE IF EXISTS `serialnumber`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `serialnumber` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `value` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `serialnumber`
--

LOCK TABLES `serialnumber` WRITE;
/*!40000 ALTER TABLE `serialnumber` DISABLE KEYS */;
/*!40000 ALTER TABLE `serialnumber` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tokenrequest`
--

DROP TABLE IF EXISTS `tokenrequest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tokenrequest` (
  `requestid` int(11) NOT NULL AUTO_INCREMENT,
  `amount` double DEFAULT NULL,
  `clientref` varchar(50) DEFAULT NULL,
  `meterno` varchar(100) NOT NULL,
  `oref` double NOT NULL,
  `receipt` tinyint(4) DEFAULT NULL,
  `ref` double DEFAULT NULL,
  `requestdate` datetime(6) NOT NULL,
  `requestedby` bigint(20) DEFAULT NULL,
  `requestxml` varchar(1000) NOT NULL,
  `seqnum` int(11) NOT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `type` tinyint(4) NOT NULL,
  PRIMARY KEY (`requestid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tokenrequest`
--

LOCK TABLES `tokenrequest` WRITE;
/*!40000 ALTER TABLE `tokenrequest` DISABLE KEYS */;
/*!40000 ALTER TABLE `tokenrequest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tokenresponse`
--

DROP TABLE IF EXISTS `tokenresponse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tokenresponse` (
  `reponseid` int(11) NOT NULL AUTO_INCREMENT,
  `jsonresponse` varchar(1500) DEFAULT NULL,
  `meterno` varchar(100) DEFAULT NULL,
  `origxml` varchar(1500) DEFAULT NULL,
  `osysdate` datetime(6) DEFAULT NULL,
  `ref` double DEFAULT NULL,
  `responsedate` datetime(6) DEFAULT NULL,
  `responsexml` varchar(1500) DEFAULT NULL,
  `errorcodeid` int(11) DEFAULT NULL,
  `requestid` int(11) DEFAULT NULL,
  PRIMARY KEY (`reponseid`),
  KEY `fk_tokenresponse_errorcode` (`errorcodeid`),
  KEY `fk_tokenresponse_tokenrequest` (`requestid`),
  CONSTRAINT `fk_tokenresponse_errorcode` FOREIGN KEY (`errorcodeid`) REFERENCES `errorcode` (`errorcodeid`),
  CONSTRAINT `fk_tokenresponse_tokenrequest` FOREIGN KEY (`requestid`) REFERENCES `tokenrequest` (`requestid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tokenresponse`
--

LOCK TABLES `tokenresponse` WRITE;
/*!40000 ALTER TABLE `tokenresponse` DISABLE KEYS */;
/*!40000 ALTER TABLE `tokenresponse` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-09-14 14:25:20
