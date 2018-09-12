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
  `errorcodeid` bigint(20) NOT NULL AUTO_INCREMENT,
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
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `value` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `serialnumber`
--

LOCK TABLES `serialnumber` WRITE;
/*!40000 ALTER TABLE `serialnumber` DISABLE KEYS */;
INSERT INTO `serialnumber` VALUES (1,'refcount',25),(2,'seqcount',25),(3,'revcount',0);
/*!40000 ALTER TABLE `serialnumber` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tokenrequest`
--

DROP TABLE IF EXISTS `tokenrequest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tokenrequest` (
  `requestid` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` double DEFAULT NULL,
  `meterno` varchar(100) NOT NULL,
  `ref` double DEFAULT NULL,
  `requestdate` datetime(6) DEFAULT NULL,
  `requestedby` bigint(20) DEFAULT NULL,
  `requestxml` varchar(1000) DEFAULT NULL,
  `seqnum` int(11) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `clientref` varchar(50) DEFAULT NULL,
  `oref` double NOT NULL,
  `receipt` tinyint(4) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`requestid`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tokenrequest`
--

LOCK TABLES `tokenrequest` WRITE;
/*!40000 ALTER TABLE `tokenrequest` DISABLE KEYS */;
INSERT INTO `tokenrequest` VALUES (12,500,'A34345610',825515030025,'2018-09-12 15:03:40.511000',NULL,'<ipayMsg client=\"IPAYAFRICA\" term=\"00001\" seqNum=\"00025\" time=\"2018-09-12 15:03:40 +0300\"><elecMsg ver=\"2.44\"><vendReq><ref>825515030025</ref><amt cur=\"KES\">50000</amt><numTokens>1</numTokens><meter>A34345610</meter><payType>cash</payType></vendReq></elecMsg></ipayMsg>',25,'2','16061100',825515030025,1,1);
/*!40000 ALTER TABLE `tokenrequest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tokenresponse`
--

DROP TABLE IF EXISTS `tokenresponse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tokenresponse` (
  `reponseid` bigint(20) NOT NULL AUTO_INCREMENT,
  `errorcodeid` varchar(255) DEFAULT NULL,
  `meterno` varchar(20) DEFAULT NULL,
  `osysdate` datetime(6) DEFAULT NULL,
  `ref` bigint(20) DEFAULT NULL,
  `responsedate` datetime(6) DEFAULT NULL,
  `responsexml` varchar(2000) DEFAULT NULL,
  `type` tinyint(4) DEFAULT NULL,
  `jsonresponse` varchar(2000) DEFAULT NULL,
  `origxml` varchar(1500) DEFAULT NULL,
  PRIMARY KEY (`reponseid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tokenresponse`
--

LOCK TABLES `tokenresponse` WRITE;
/*!40000 ALTER TABLE `tokenresponse` DISABLE KEYS */;
INSERT INTO `tokenresponse` VALUES (3,'1','A34345610','2018-09-12 15:03:43.729000',825515030025,'2018-09-12 15:03:44.000000','<ipayMsg  client=\"IPAYAFRICA\"  term=\"00001\"   seqNum=\"00025\"  time=\"2018-09-12 14:03:44 +0200\"><elecMsg  ver=\"2.48\"  finAdj=\"-50000\"><vendRes  supGrpRef=\"100405\"  tariffIdx=\"52\"   keyRevNum=\"1\"  tokenTechCode=\"02\"  algCode=\"05\"  daysLastVend=\"19999\"  resource=\"elec\"><ref>825515030025</ref><res  code=\"elec000\">OK</res><util  addr=\"59 WaterFront, Durban. 5899.\"  taxRef=\"3988339883\"  distId=\"6004708001509\">Eskom Online</util><stdToken   units=\"1666.6666\"  amt=\"31304\"  tax=\"4696\"  tariff=\"aaaa.aa kWh @ 065.72 c/kWh: bbbb.bb kWh @ 075.42 c/kWh: cccc.cc kWh @ 109.50 c/kWh: dddd.dd kWh @ 120.10 c/kWh : \"  desc=\"Normal Sale\"    unitsType=\"kWh\"  rctNum=\"233875822915\">71396306497885587040</stdToken><debt  amt=\"8500\"  tax=\"0\"  rem=\"7700\"  desc=\"1122\">Debt Recovery</debt><fixed  amt=\"4782\"  tax=\"718\">Fixed</fixed><rtlrMsg>Hello Operator Message.</rtlrMsg><customerMsg>Good day dear customer. This is a test customer message from customer. We have vended a token for the customer. The message can be upto 160 characters long....</customerMsg></vendRes></elecMsg></ipayMsg>',NULL,'{\"reason\":\"OK\",\"unitsAmt\":313.04,\"tax\":46.96,\"units\":\"1666.6666\",\"unitsType\":\"kWh\",\"token\":\"71396306497885587040\",\"ref\":\"16061100\",\"tarrif\":{\"t4\":\" dddd.dd kWh @ 120.10 c/kWh \",\"t5\":\" \",\"t1\":\"aaaa.aa kWh @ 065.72 c/kWh\",\"t2\":\" bbbb.bb kWh @ 075.42 c/kWh\",\"t3\":\" cccc.cc kWh @ 109.50 c/kWh\"},\"debtAmt\":85.0,\"response\":\"Please try again later\",\"ourref\":\"825515030025\",\"fixedamt\":47.82,\"fixedtax\":7.18,\"status\":\"Successful\"}','response: <ipayMsg client=\"IPAYAFRICA\" term=\"00001\" seqNum=\"00025\" time=\"2018-09-12 14:03:44 +0200\"><elecMsg ver=\"2.48\" finAdj=\"-50000\"><vendRes supGrpRef=\"100405\" tariffIdx=\"52\" keyRevNum=\"1\" tokenTechCode=\"02\" algCode=\"05\" daysLastVend=\"19999\" resource=\"elec\"><ref>825515030025</ref><res code=\"elec000\">OK</res><util addr=\"59 WaterFront, Durban. 5899.\" taxRef=\"3988339883\" distId=\"6004708001509\">Eskom Online</util><stdToken units=\"1666.6666\" amt=\"31304\" tax=\"4696\" tariff=\"aaaa.aa kWh @ 065.72 c/kWh: bbbb.bb kWh @ 075.42 c/kWh: cccc.cc kWh @ 109.50 c/kWh: dddd.dd kWh @ 120.10 c/kWh : \" desc=\"Normal Sale\" unitsType=\"kWh\" rctNum=\"233875822915\">71396306497885587040</stdToken><debt amt=\"8500\" tax=\"0\" rem=\"7700\" desc=\"1122\">Debt Recovery</debt><fixed amt=\"4782\" tax=\"718\">Fixed</fixed><rtlrMsg>Hello Operator Message.</rtlrMsg><customerMsg>Good day dear customer. This is a test customer message from customer. We have vended a token for the customer. The message can be upto 160 characters long....</customerMsg></vendRes></elecMsg></ipayMsg>');
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

-- Dump completed on 2018-09-12 16:31:08
