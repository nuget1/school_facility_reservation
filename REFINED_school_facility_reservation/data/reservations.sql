-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: May 12, 2023 at 06:24 AM
-- Server version: 8.0.31
-- PHP Version: 8.0.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `reservations`
--

-- --------------------------------------------------------

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
CREATE TABLE IF NOT EXISTS `department` (
  `department_id` int NOT NULL AUTO_INCREMENT,
  `department_name` varchar(55) DEFAULT NULL,
  PRIMARY KEY (`department_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1005 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `department`
--

INSERT INTO `department` (`department_id`, `department_name`) VALUES
(1000, 'Computer Science'),
(1001, 'Information Technology'),
(1002, 'Multi Media Arts'),
(1003, 'Accountancy'),
(1004, 'Business');

-- --------------------------------------------------------

--
-- Table structure for table `facility`
--

DROP TABLE IF EXISTS `facility`;
CREATE TABLE IF NOT EXISTS `facility` (
  `facility_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(55) DEFAULT NULL,
  `type` varchar(55) DEFAULT NULL,
  `description` varchar(55) DEFAULT NULL,
  `capacity` int DEFAULT '0',
  PRIMARY KEY (`facility_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `facility`
--

INSERT INTO `facility` (`facility_id`, `name`, `type`, `description`, `capacity`) VALUES
(1, 'AU 111', 'Auditorium', NULL, 200),
(2, 'AQ 111', 'Aquatics Facility', NULL, 100),
(3, 'AP 111', 'Ampitheatre', NULL, 500),
(4, 'COMP 111', 'Computer Room', NULL, 40),
(5, 'RM 111', 'Rehearsal Room', 'Used for performance rehearsals.', 50);

-- --------------------------------------------------------

--
-- Table structure for table `faculty`
--

DROP TABLE IF EXISTS `faculty`;
CREATE TABLE IF NOT EXISTS `faculty` (
  `user_id` int DEFAULT NOT NULL,
  `faculty_id` int NOT NULL AUTO_INCREMENT,
  `department` int DEFAULT NULL,
  PRIMARY KEY (`faculty_id`),
  KEY `faculty_ibfk_1` (`user_id`),
  KEY `department` (`department`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `faculty`
--

INSERT INTO `faculty` (`user_id`, `faculty_id`, `department`) VALUES
(2, 1, 1000),
(4, 2, 1001),
(7, 3, 1002),
(8, 4, 1003),
(9, 5, 1004);

-- --------------------------------------------------------

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
CREATE TABLE IF NOT EXISTS `reservation` (
  `reservation_id` int NOT NULL AUTO_INCREMENT,
  `student_id` int NOT NULL,
  `facility_id` int NOT NULL,
  `faculty_id` int DEFAULT NULL,
  `reservation_status` int DEFAULT NULL,
  `purpose` varchar(55) DEFAULT NULL,
  `time_start` time(6) DEFAULT NULL,
  `time_end` time(6) DEFAULT NULL,
  `date_start` date DEFAULT NULL,
  `date_end` date DEFAULT NULL,
  PRIMARY KEY (`reservation_id`),
  KEY `reservation_status` (`reservation_status`),
  KEY `student_id` (`student_id`),
  KEY `faculty_id` (`faculty_id`),
  KEY `facility_id` (`facility_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `reservation_participants`
--

DROP TABLE IF EXISTS `reservation_participants`;
CREATE TABLE IF NOT EXISTS `reservation_participants` (
  `participant_id` int NOT NULL,
  `reservation_id` int NOT NULL,
  PRIMARY KEY (`participant_id`,`reservation_id`),
  KEY `reservation_id` (`reservation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `reservation_status`
--

DROP TABLE IF EXISTS `reservation_status`;
CREATE TABLE IF NOT EXISTS `reservation_status` (
  `reservation_status_id` int NOT NULL AUTO_INCREMENT,
  `reservation_status` varchar(55) DEFAULT NULL,
  PRIMARY KEY (`reservation_status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `reservation_status`
--

INSERT INTO `reservation_status` (`reservation_status_id`, `reservation_status`) VALUES
(1, 'PENDING'),
(2, 'APPROVED'),
(3, 'REJECTED');

-- --------------------------------------------------------

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
CREATE TABLE IF NOT EXISTS `student` (
  `user_id` int DEFAULT NOT NULL,
  `student_id` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`student_id`),
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `student`
--

INSERT INTO `student` (`user_id`, `student_id`) VALUES
(1, 1),
(3, 2),
(5, 3),
(6, 4),
(10, 5);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `fname` varchar(55) DEFAULT NULL,
  `lname` varchar(55) DEFAULT NULL,
  `password` varchar(55) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `fname`, `lname`, `password`) VALUES
(1, 'Mario', 'Mario', 'ItsaMeMario'),
(2, 'Miroku', 'Sano', 'GiveMeSweets'),
(3, 'Ash', 'Ketchum', 'Forever13'),
(4, 'Komi', 'Shouko', 'ToShyToTalk'),
(5, 'Rie', 'Takahashi', 'EKUSUPLOSION'),
(6, 'March', '7th', 'TheBestMascot'),
(7, 'KianaKaslana', '', 'DaTunaTriumphs'),
(8, 'Himeko', 'Murata', 'NeverLetYouGo'),
(9, 'Stelle', 'Stelle', 'TrashCanEnthusiast'),
(10, 'Pom', 'Pom', 'BestConductor');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `faculty`
--
ALTER TABLE `faculty`
  ADD CONSTRAINT `faculty_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `faculty_ibfk_2` FOREIGN KEY (`department`) REFERENCES `department` (`department_id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

--
-- Constraints for table `reservation`
--
ALTER TABLE `reservation`
  ADD CONSTRAINT `reservation_ibfk_1` FOREIGN KEY (`reservation_status`) REFERENCES `reservation_status` (`reservation_status_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `reservation_ibfk_2` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  ADD CONSTRAINT `reservation_ibfk_3` FOREIGN KEY (`faculty_id`) REFERENCES `faculty` (`faculty_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  ADD CONSTRAINT `reservation_ibfk_4` FOREIGN KEY (`facility_id`) REFERENCES `facility` (`facility_id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

--
-- Constraints for table `reservation_participants`
--
ALTER TABLE `reservation_participants`
  ADD CONSTRAINT `reservation_participants_ibfk_1` FOREIGN KEY (`reservation_id`) REFERENCES `reservation` (`reservation_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `student`
--
ALTER TABLE `student`
  ADD CONSTRAINT `student_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
