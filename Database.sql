-- MySQL dump 10.13  Distrib 8.0.36, for macos14 (arm64)
--
-- Host: localhost    Database: fc_appointment_system
-- ------------------------------------------------------
-- Server version	8.0.30

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `complaint`
--

DROP TABLE IF EXISTS `complaint`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `complaint` (
  `complaint_id` varchar(6) NOT NULL,
  `member_id` varchar(36) DEFAULT NULL,
  `staff_id` varchar(36) DEFAULT NULL,
  `content` text,
  `is_dealt` binary(1) DEFAULT NULL,
  PRIMARY KEY (`complaint_id`),
  KEY `fk_complaint_memid` (`member_id`),
  KEY `fk_complaint_stfid` (`staff_id`),
  CONSTRAINT `fk_complaint_memid` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`),
  CONSTRAINT `fk_complaint_stfid` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `complaint`
--

LOCK TABLES `complaint` WRITE;
/*!40000 ALTER TABLE `complaint` DISABLE KEYS */;
INSERT INTO `complaint` VALUES ('com001','M004','T010','Inappropriate guidance',_binary '1'),('com002','M008','T002','unprofessional guidance',_binary '1'),('com003','M009','T003','bad attitude',_binary '1'),('com004','M001','T006','Impatience',_binary '0'),('com005','M005','T005','bad attitude',_binary '0');
/*!40000 ALTER TABLE `complaint` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `course_id` varchar(4) NOT NULL,
  `name` text,
  `location` text,
  PRIMARY KEY (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES ('C001','yoga','1F-001'),('C002','Fat-burning class','1F-002'),('C003','Upper limb training','1F-003'),('C004','Strength training','2F-B'),('C005','badminton','2F-A');
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `locker`
--

DROP TABLE IF EXISTS `locker`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `locker` (
  `locker_id` varchar(4) DEFAULT NULL,
  `size` text,
  `location` text,
  `password` int DEFAULT NULL,
  `is_occupied` binary(1) DEFAULT NULL,
  `member_id` varchar(36) DEFAULT NULL,
  `is_reserved` bit(1) NOT NULL,
  KEY `fk_locker_memid` (`member_id`),
  CONSTRAINT `fk_locker_memid` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `locker`
--

LOCK TABLES `locker` WRITE;
/*!40000 ALTER TABLE `locker` DISABLE KEYS */;
INSERT INTO `locker` VALUES ('L001','Large','1F-north',1024,_binary '0',NULL,_binary '\0'),('L002','Large','1F-north',6020,_binary '0',NULL,_binary '\0'),('L003','Large','1F-south',5018,_binary '0',NULL,_binary '\0'),('L004','Medium','1F-south',2136,_binary '0',NULL,_binary '\0'),('L005','Medium','2F-north',7851,_binary '0',NULL,_binary '\0'),('L006','Medium','2F-north',2103,_binary '0',NULL,_binary '\0'),('L007','Medium','2F-south',4120,_binary '0',NULL,_binary '\0'),('L008','Medium','2F-south',3024,_binary '0',NULL,_binary '\0'),('L009','Small','3F-south',7456,_binary '0',NULL,_binary '\0'),('L010','Small','3F-south',2317,_binary '0',NULL,_binary '\0');
/*!40000 ALTER TABLE `locker` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `member_id` varchar(36) NOT NULL,
  `name` text,
  `phone` varchar(35) DEFAULT NULL,
  `gender` text,
  `address` text,
  `password` text,
  `encoded_password` varchar(255) DEFAULT NULL,
  `weight` int DEFAULT NULL,
  `height` int DEFAULT NULL,
  `is_purchased` bit(1) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `locker_isAgreed` bit(1) DEFAULT NULL,
  `remaining_sum` double DEFAULT '0',
  `plan` varchar(255) DEFAULT NULL,
  `enddate` date DEFAULT NULL,
  `end_date` datetime(6) DEFAULT NULL,
  `locker_is_agreed` bit(1) DEFAULT NULL,
  PRIMARY KEY (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
INSERT INTO `member` VALUES ('M001','John Doe','9871234567','Female','123 Main St','password123','$2a$10$J6ZjQ6TlkdAyNwVEXu4LkOTgQ3SZNzhPTeeYPk16T7uEbqqby7cBq',175,180,_binary '',_binary '',_binary '',10000,NULL,NULL,NULL,NULL),('M002','Jane Smith','9876543213','Male','456 Oak Rd','abc123def','$2a$10$YMldiyMcSDi9ojYCFVcEweOfS7uZQR9FbJJN6OmtiPkJ8QqDbkLAi',178,187,_binary '',_binary '',_binary '',0,NULL,NULL,NULL,NULL),('M003','Michael Brown','9573958294','Male','789 Elm St','qwerty12','$2a$10$p3T75mzRX708Zxe.OJ4yGuFT3Dd58zL34b37L.pU0.FA9HKKOAAQS',190,178,_binary '',_binary '',_binary '\0',0,NULL,NULL,NULL,NULL),('M004','Emily Davis','9672746283','Female','321 Pine Rd','password456','$2a$10$hsjflrtMQPd4Ev2ck5r0YuH8VGjjNnO/lP5fTfcJ7DJhEMmx9UDAi',130,165,_binary '\0',_binary '\0',_binary '\0',0,NULL,NULL,NULL,NULL),('M005','David Lee','9561284637','Male','654 Cedar Ln','abc456def','$2a$10$LCgiAc3tAdtrf0yDO.Tu0.6eYRkjJ9Voi5H0snqeONpZQpjhYmfBu',186,184,_binary '\0',_binary '\0',_binary '\0',0,NULL,NULL,NULL,NULL),('M006','Sarah Wilson','9678456724','Female','987 Oak St','password789','$2a$10$r/abIiahy1OQB70hCj6xMOevkQW7iDplLA9zotEYFmaLg0X368mVK',120,163,_binary '',_binary '\0',_binary '\0',0,NULL,NULL,NULL,NULL),('M007','William Chen','9074811235','Male','321 Maple Ave','qwerty34','$2a$10$HaIq5VM1HMuT1XS5bOvEEOF9yPa/RrW12db6nZz9yrmHYH.MaXe/i',198,176,_binary '',_binary '',_binary '',0,NULL,NULL,NULL,NULL),('M008','Olivia Martinez','9897823541','Female','654 Birch Rd','abc789def','$2a$10$AZefcSn/Y1F0yRjd7yjcXusgDEwUxhlkVI8h5zhSuRHBzyYFVPaum',133,170,_binary '',_binary '',_binary '',0,NULL,NULL,NULL,NULL),('M009','Christopher Patel','9008716573','Female','987 Elm St','password012','$2a$10$q3eO8xnfEx9/BnCVevuwZuCzg/2v50tUBGlDM7OIn95QSLLZsUcoO',115,160,_binary '',_binary '',_binary '',0,NULL,NULL,NULL,NULL),('M010','Emma Gonzalez','9786125674','Male','321 Oak Ln','abc012def','$2a$10$gOLfxOmxsINTcWc14bO9Q.h9yHUE5PASYIwiiwfTQDtwip8N3QPge',174,178,_binary '',_binary '\0',_binary '\0',0,NULL,NULL,NULL,NULL),('M18962125828','Chloe','18962125828','Female','ABs Avenue 188','Xuruoyu200366','$2a$10$XgevNsBckZxZIGvQKrDusulgdgo/7Z2qpRWnK09ux0JMtUNOBd9VO',NULL,165,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase`
--

DROP TABLE IF EXISTS `purchase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase` (
  `purchase_id` varchar(255) NOT NULL,
  `member_id` varchar(36) DEFAULT NULL,
  `plan` text,
  `purchase_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `duration` int DEFAULT NULL,
  PRIMARY KEY (`purchase_id`),
  KEY `fk_purchase_memid` (`member_id`),
  CONSTRAINT `fk_purchase_memid` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase`
--

LOCK TABLES `purchase` WRITE;
/*!40000 ALTER TABLE `purchase` DISABLE KEYS */;
INSERT INTO `purchase` VALUES ('P001','M002','Diamond','2023-01-01','2024-01-01',NULL),('P002','M001','Silver','2023-01-31','2024-07-31',NULL),('P003','M003','Gold','2023-02-15','2023-08-15',NULL),('P004','M004','Silver','2023-02-28','2023-08-28',NULL),('P005','M007','Gold','2023-03-05','2024-03-05',NULL),('P006','M009','Diamond','2023-03-20','2024-03-20',NULL),('P007','M005','Silver','2023-03-20','2023-10-05',NULL),('P008','M008','Gold','2023-05-10','2024-05-10',NULL),('P009','M001','Gold','2023-08-03','2024-02-03',NULL),('P010','M003','Diamond','2023-08-30','2024-08-30',NULL);
/*!40000 ALTER TABLE `purchase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation` (
  `reservation_id` varchar(255) NOT NULL,
  `member_id` varchar(36) DEFAULT NULL,
  `trainer_id` varchar(36) DEFAULT NULL,
  `is_canceled` binary(1) DEFAULT NULL,
  `is_signin` bit(1) DEFAULT NULL,
  `lesson_startDate` date DEFAULT NULL,
  `lesson_starttime` datetime(6) DEFAULT NULL,
  `lesson_endTime` time DEFAULT NULL,
  `is_cancelled` bit(1) DEFAULT NULL,
  `staff_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`reservation_id`),
  KEY `member_id` (`member_id`),
  KEY `trainer_id` (`trainer_id`),
  CONSTRAINT `reservation_ibfk_1` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`),
  CONSTRAINT `reservation_ibfk_2` FOREIGN KEY (`trainer_id`) REFERENCES `staff` (`staff_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation`
--

LOCK TABLES `reservation` WRITE;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
INSERT INTO `reservation` VALUES ('R001','M001','T001',_binary '0',_binary '','2023-02-13','2024-04-26 09:00:00.000000','11:00:00',NULL,NULL),('R002','M002','T007',_binary '1',_binary '\0','2023-03-13','2024-04-26 09:00:00.000000','11:00:00',NULL,NULL),('R003','M008','T002',_binary '0',_binary '','2023-05-22','2024-04-26 09:00:00.000000','11:00:00',NULL,NULL),('R004','M004','T010',_binary '1',_binary '\0','2023-08-08','2024-04-26 09:00:00.000000','11:00:00',NULL,NULL),('R005','M005','T003',_binary '0',_binary '','2023-10-04','2024-04-26 09:00:00.000000','11:00:00',NULL,NULL),('R006','M001','T006',_binary '0',_binary '','2023-12-01','2024-04-26 11:00:00.000000','13:00:00',NULL,NULL),('R007','M002','T003',_binary '0',_binary '\0','2023-12-22','2024-04-26 11:00:00.000000','13:00:00',NULL,NULL),('R008','M004','T004',_binary '1',_binary '\0','2024-01-14','2024-04-26 11:00:00.000000','13:00:00',NULL,NULL),('R009','M010','T006',_binary '0',_binary '','2024-01-30','2024-04-26 09:00:00.000000','11:00:00',NULL,NULL),('R010','M005','T005',_binary '0',_binary '','2024-02-19','2024-04-26 11:00:00.000000','13:00:00',NULL,NULL),('R011','M001','T001',_binary '0',_binary '\0','2024-03-12','2024-04-26 13:00:00.000000','15:00:00',NULL,NULL),('R012','M003','T007',_binary '1',_binary '\0','2024-03-16','2024-04-26 09:00:00.000000','11:00:00',NULL,NULL),('R013','M009','T009',_binary '0',_binary '','2024-04-04','2024-04-26 09:00:00.000000','11:00:00',NULL,NULL),('R014','M004','T010',_binary '0',_binary '','2024-04-14','2024-04-26 13:00:00.000000','15:00:00',NULL,NULL),('R015','M009','T003',_binary '0',_binary '\0','2024-04-17','2024-04-26 11:00:00.000000','13:00:00',NULL,NULL),('R016','M001','T006',_binary '1',_binary '\0','2024-04-20','2024-04-26 15:00:00.000000','17:00:00',NULL,NULL),('R017','M006','T007',_binary '0',_binary '\0','2024-04-27','2024-04-26 09:00:00.000000','11:00:00',NULL,NULL),('R018','M007','T004',_binary '0',_binary '\0','2024-05-01','2024-04-26 09:00:00.000000','11:00:00',NULL,NULL),('R019','M004','T007',_binary '0',_binary '\0','2024-05-17','2024-04-26 15:00:00.000000','17:00:00',NULL,NULL),('R020','M005','T005',_binary '1',_binary '\0','2024-05-26','2024-04-26 13:00:00.000000','15:00:00',NULL,NULL);
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `staff`
--

DROP TABLE IF EXISTS `staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `staff` (
  `staff_id` varchar(36) NOT NULL,
  `name` text,
  `password` text,
  `encoded_password` varchar(255) DEFAULT NULL,
  `phone` varchar(35) DEFAULT NULL,
  `gender` text,
  `level` int DEFAULT NULL,
  `salary` int DEFAULT NULL,
  `course_id` varchar(4) DEFAULT NULL,
  `is_manager` bit(1) NOT NULL,
  PRIMARY KEY (`staff_id`),
  KEY `fk_staff_courseid` (`course_id`),
  CONSTRAINT `fk_staff_courseid` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff`
--

LOCK TABLES `staff` WRITE;
/*!40000 ALTER TABLE `staff` DISABLE KEYS */;
INSERT INTO `staff` VALUES ('Man001','Manager01','Imanage1234','$2a$10$nO2dij2DyrPygtV8HSWF.ug8Zja7zdJsqOrRChdLyHbdvLgSVGXoO','74189470',NULL,NULL,NULL,NULL,_binary ''),('Man002','Manager02','Manana54321','$2a$10$9F4M8YlVvAqBKc7Hc26w..xSIfun7AHWlvJqb6NrHOJC3rZM1qHsi','572919348',NULL,NULL,NULL,NULL,_binary ''),('T001','Soonfei','IISdha17891','$2a$10$QybTGR/t9BzHnSd1/td7cOIxkF4vFk/nSY/.OkahSVGyoxs09AOTi','17792972','Male',3,1000,'C001',_binary '\0'),('T002','John','Gairb54810','$2a$10$ARBMfKxkkpVcGqJh71/Xb.MWlW77444v9ZlU7ZTZyv6RzeL74iN7O','222782782','Male',4,10000,'C001',_binary '\0'),('T003','Mary','iYaieb10347','$2a$10$C4/4bjbxNhp37h.owtPXUeCj9APvTWa4zA0SAC5JkVtmjisbTfUL.','278227','FeMale',5,100000,'C001',_binary '\0'),('T004','Lily01','Rbiam62038','$2a$10$zp3ePTRknlluVpsjb6oxo.nF1b9tQF3Bzg8ZAuM3GozeJqmHPWmaa','26727','Female',3,1000,'C002',_binary '\0'),('T005','Lily02','Phnmab0182','$2a$10$ur1CEoncbk6rUXq2IrgJ2.Ewr9n2dYsBbIj40amiqPb.6C3C03KBm','2782279','Female',4,10000,'C002',_binary '\0'),('T006','Momo','Bka8203929','$2a$10$yqtmdFe4OdIJhxAp9AMX6.F2aPKIdLJzagyMdfs/hv6aFSHg0WECu','2289744','Female',5,100000,'C002',_binary '\0'),('T007','Chen','Ybvjshd1812309','$2a$10$qtmbuaUwvHvcJx2UNtOwcexJYXYa9RJ5Z.IvgvpkQzo/DTQDCybHW','3473973','Male',3,1000,'C003',_binary '\0'),('T008','Wang ','TNaif874298','$2a$10$Oaq8UIew1pZw9SIuOxjhjeFiSaeaag6R3ViRwQlV2igkWS9ChgCPC','4893833','Male',4,10000,'C003',_binary '\0'),('T009','Wang02','EBiadm1984712','$2a$10$DMfikEW6bt3bUzGxPTAyJOPCPtYVjRCTj1rB9cJzXRYEAR1fOBSYm','3469393','Female',5,100000,'C003',_binary '\0'),('T010','Wang03','Biana704301','$2a$10$dsUIaaDekc8ma8v0K.XDfujphYYJMO2p7w5G90dTCukDGgyhduRNG','9020390','Male',3,1000,'C004',_binary '\0'),('T011','Kiki','Wbakl78934','$2a$10$2VwNtONawGKh.R72.cJ1Q.uY/11UBSlqTYwKE8HMXMROUkEbEUePG','9077','Male',4,10000,'C004',_binary '\0'),('T012','Sufei','Sunavh89147','$2a$10$Cb1/jRCk4Qu0EXFJceH8e.af5FT4hVjPAA1hdt9z3tuzMhEZx8pDO','893892','Female',5,100000,'C004',_binary '\0'),('T013','Apple','Ynwpoqm7912','$2a$10$2V6hCBlDkc/NvTK3zny4KOYVjFYsi8WwePoUemRHz9.wPGHittPGK','739201','Male',3,1000,'C005',_binary '\0'),('T014','Banana','Abc128937','$2a$10$0TlyrNhZkrflF9f6GLbytezrd4pNUw58c5JsnqdjoXbITDfdYWA0a','829812','Male',4,10000,'C005',_binary '\0'),('T015','Pink','Cba9831172','$2a$10$xDEoWQp.UqPCRm/S6.mqqu/w00x4SdW8WLCbtTkHVVGrZ6zilrove','281028','Male',5,100000,'C005',_binary '\0');
/*!40000 ALTER TABLE `staff` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-26 17:57:07
