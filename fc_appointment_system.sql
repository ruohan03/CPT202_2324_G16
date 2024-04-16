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
  `member_id` varchar(4) DEFAULT NULL,
  `trainer_id` varchar(4) DEFAULT NULL,
  `content` text,
  `is_dealt` binary(1) DEFAULT NULL,
  PRIMARY KEY (`complaint_id`),
  UNIQUE KEY `complaint_id_UNIQUE` (`complaint_id`),
  KEY `fk_comp_memid` (`member_id`),
  KEY `fk_comp_trainerid` (`trainer_id`),
  CONSTRAINT `fk_comp_memid` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`),
  CONSTRAINT `fk_comp_trainerid` FOREIGN KEY (`trainer_id`) REFERENCES `trainer` (`trainer_id`)
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
  PRIMARY KEY (`course_id`),
  UNIQUE KEY `course_id_UNIQUE` (`course_id`)
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
  `locker_id` varchar(4) NOT NULL,
  `size` text,
  `location` text,
  `password` int DEFAULT NULL,
  `is_occupied` binary(1) DEFAULT NULL,
  `member_id` varchar(4) DEFAULT NULL,
  PRIMARY KEY (`locker_id`),
  UNIQUE KEY `locker_id_UNIQUE` (`locker_id`),
  KEY `fy_locker_memid_idx` (`member_id`),
  CONSTRAINT `fy_locker_memid` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `locker`
--

LOCK TABLES `locker` WRITE;
/*!40000 ALTER TABLE `locker` DISABLE KEYS */;
INSERT INTO `locker` VALUES ('L001','Large','1F-north',1024,_binary '1',NULL),('L002','Large','1F-north',6020,_binary '1',NULL),('L003','Large','1F-south',5018,_binary '0',NULL),('L004','Medium','1F-south',2136,_binary '0',NULL),('L005','Medium','2F-north',7851,_binary '0',NULL),('L006','Medium','2F-north',2103,_binary '1',NULL),('L007','Medium','2F-south',4120,_binary '0',NULL),('L008','Medium','2F-south',3024,_binary '0',NULL),('L009','Small','3F-south',7456,_binary '0',NULL),('L010','Small','3F-south',2317,_binary '0',NULL);
/*!40000 ALTER TABLE `locker` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `member_id` varchar(4) NOT NULL,
  `name` text,
  `phone` bigint DEFAULT NULL,
  `gender` text,
  `address` text,
  `password` text,
  `weight` int DEFAULT NULL,
  `height` int DEFAULT NULL,
  `is_purchased` binary(1) DEFAULT NULL,
  `is_active` binary(1) DEFAULT NULL,
  `locker_isagreed` binary(1) DEFAULT NULL,
  PRIMARY KEY (`member_id`),
  UNIQUE KEY `member_id_UNIQUE` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
INSERT INTO `member` VALUES ('M001','John Doe',9871234567,'Female','123 Main St','password123',175,180,_binary '1',_binary '1',_binary '1'),('M002','Jane Smith',9876543213,'Male','456 Oak Rd','abc123def',178,187,_binary '1',_binary '1',_binary '1'),('M003','Michael Brown',9573958294,'Male','789 Elm St','qwerty12',190,178,_binary '1',_binary '1',_binary '0'),('M004','Emily Davis',9672746283,'Female','321 Pine Rd','password456',130,165,_binary '0',_binary '0',_binary '0'),('M005','David Lee',9561284637,'Male','654 Cedar Ln','abc456def',186,184,_binary '0',_binary '0',_binary '0'),('M006','Sarah Wilson',9678456724,'Female','987 Oak St','password789',120,163,_binary '1',_binary '0',_binary '0'),('M007','William Chen',9074811235,'Male','321 Maple Ave','qwerty34',198,176,_binary '1',_binary '1',_binary '1'),('M008','Olivia Martinez',9897823541,'Female','654 Birch Rd','abc789def',133,170,_binary '1',_binary '1',_binary '1'),('M009','Christopher Patel',9008716573,'Female','987 Elm St','password012',115,160,_binary '1',_binary '1',_binary '1'),('M010','Emma Gonzalez',9786125674,'Male','321 Oak Ln','abc012def',174,178,_binary '1',_binary '0',_binary '0');
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase`
--

DROP TABLE IF EXISTS `purchase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase` (
  `purchase_id` varchar(4) NOT NULL,
  `member_id` varchar(4) DEFAULT NULL,
  `plan` text,
  `purchase_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  PRIMARY KEY (`purchase_id`),
  UNIQUE KEY `purchase_id_UNIQUE` (`purchase_id`),
  KEY `fk_purchase_memid` (`member_id`),
  CONSTRAINT `fk_purchase_memid` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase`
--

LOCK TABLES `purchase` WRITE;
/*!40000 ALTER TABLE `purchase` DISABLE KEYS */;
INSERT INTO `purchase` VALUES ('P001','M002','Diamond','2023-01-01','2024-01-01'),('P002','M001','Silver','2023-01-31','2024-07-31'),('P003','M003','Gold','2023-02-15','2023-08-15'),('P004','M004','Silver','2023-02-28','2023-08-28'),('P005','M007','Gold','2023-03-05','2024-03-05'),('P006','M009','Diamond','2023-03-20','2024-03-20'),('P007','M005','Silver','2023-03-20','2023-10-05'),('P008','M008','Gold','2023-05-10','2024-05-10'),('P009','M001','Gold','2023-08-03','2024-02-03'),('P010','M003','Diamond','2023-08-30','2024-08-30');
/*!40000 ALTER TABLE `purchase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation` (
  `reservation_id` varchar(4) NOT NULL,
  `member_id` varchar(4) DEFAULT NULL,
  `trainer_id` varchar(4) DEFAULT NULL,
  `lesson_starttime` datetime DEFAULT NULL,
  `is_canceled` binary(1) DEFAULT NULL,
  `is_signin` binary(1) DEFAULT NULL,
  PRIMARY KEY (`reservation_id`),
  UNIQUE KEY `reservation_id_UNIQUE` (`reservation_id`),
  KEY `fk_reserve_memid` (`member_id`),
  KEY `fk_reserve_trainerid` (`trainer_id`),
  CONSTRAINT `fk_reserve_memid` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`),
  CONSTRAINT `fk_reserve_trainerid` FOREIGN KEY (`trainer_id`) REFERENCES `trainer` (`trainer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation`
--

LOCK TABLES `reservation` WRITE;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
INSERT INTO `reservation` VALUES ('R001','M001','T001','2024-04-15 09:00:00',_binary '0',_binary '1'),('R002','M002','T007','2024-04-15 11:00:00',_binary '1',_binary '0'),('R003','M008','T002','2024-04-16 09:00:00',_binary '0',_binary '1'),('R004','M004','T010','2024-04-16 11:00:00',_binary '1',_binary '0'),('R005','M005','T003','2024-04-17 13:00:00',_binary '0',_binary '1'),('R006','M001','T006','2024-04-17 15:00:00',_binary '0',_binary '1'),('R007','M002','T003','2024-04-18 09:00:00',_binary '0',_binary '0'),('R008','M004','T004','2024-04-18 15:00:00',_binary '1',_binary '0'),('R009','M010','T006','2024-04-19 11:00:00',_binary '0',_binary '1'),('R010','M005','T005','2024-04-19 15:00:00',_binary '0',_binary '1'),('R011','M001','T001','2024-04-20 11:00:00',_binary '0',_binary '0'),('R012','M003','T007','2024-04-20 17:00:00',_binary '1',_binary '0'),('R013','M009','T009','2024-04-21 19:00:00',_binary '0',_binary '1'),('R014','M004','T010','2024-04-21 19:00:00',_binary '0',_binary '1'),('R015','M009','T003','2024-04-22 09:00:00',_binary '0',_binary '0'),('R016','M001','T006','2024-04-22 13:00:00',_binary '1',_binary '0'),('R017','M006','T007','2024-04-23 17:00:00',_binary '0',_binary '1'),('R018','M007','T004','2024-04-23 19:00:00',_binary '0',_binary '1'),('R019','M004','T007','2024-04-24 19:00:00',_binary '0',_binary '0'),('R020','M005','T005','2024-04-24 11:00:00',_binary '1',_binary '0');
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trainer`
--

DROP TABLE IF EXISTS `trainer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trainer` (
  `trainer_id` varchar(4) NOT NULL,
  `name` text,
  `phone` int DEFAULT NULL,
  `gender` text,
  `level` int DEFAULT NULL,
  `salary` int DEFAULT NULL,
  `course_id` varchar(4) DEFAULT NULL,
  PRIMARY KEY (`trainer_id`),
  UNIQUE KEY `trainer_id_UNIQUE` (`trainer_id`),
  KEY `fk_trainer_courseid` (`course_id`),
  CONSTRAINT `fk_trainer_courseid` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trainer`
--

LOCK TABLES `trainer` WRITE;
/*!40000 ALTER TABLE `trainer` DISABLE KEYS */;
INSERT INTO `trainer` VALUES ('T001','Soonfei',17792972,'Male',3,1000,'C001'),('T002','John',222782782,'Male',4,10000,'C001'),('T003','Mary',278227,'FeMale',5,100000,'C001'),('T004','Lily01',26727,'Female',3,1000,'C002'),('T005','Lily02',2782279,'Female',4,10000,'C002'),('T006','Momo',2289744,'Female',5,100000,'C002'),('T007','Chen',3473973,'Male',3,1000,'C003'),('T008','Wang ',4893833,'Male',4,10000,'C003'),('T009','Wang02',3469393,'Female',5,100000,'C003'),('T010','Wang03',9020390,'Male',3,1000,'C004'),('T011','Kiki',9077,'Male',4,10000,'C004'),('T012','Sufei',893892,'Female',5,100000,'C004'),('T013','Apple',739201,'Male',3,1000,'C005'),('T014','Banana',829812,'Male',4,10000,'C005'),('T015','Pink',281028,'Male',5,100000,'C005');
/*!40000 ALTER TABLE `trainer` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-16 20:21:59
