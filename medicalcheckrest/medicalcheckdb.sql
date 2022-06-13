-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 07, 2021 at 01:46 AM
-- Server version: 10.4.21-MariaDB
-- PHP Version: 8.0.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `medicalcheckdb`
--
CREATE DATABASE IF NOT EXISTS `medicalcheckdb` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `medicalcheckdb`;

-- --------------------------------------------------------

--
-- Table structure for table `follow`
--

DROP TABLE IF EXISTS `follow`;
CREATE TABLE `follow` (
  `id` int(11) NOT NULL,
  `iduserpatient` int(11) NOT NULL,
  `idusercarekeeper` int(11) NOT NULL,
  `relationship` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `follow`
--

INSERT INTO `follow` (`id`, `iduserpatient`, `idusercarekeeper`, `relationship`) VALUES
(2, 2, 1, 'friend'),
(3, 4, 2, 'friend'),
(4, 4, 6, 'bestfriend');

-- --------------------------------------------------------

--
-- Table structure for table `measure`
--

DROP TABLE IF EXISTS `measure`;
CREATE TABLE `measure` (
  `id` int(11) NOT NULL,
  `iduser` int(11) NOT NULL,
  `heartrate` int(11) NOT NULL,
  `date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `measure`
--

INSERT INTO `measure` (`id`, `iduser`, `heartrate`, `date`) VALUES
(1, 2, 100, '2021-11-07 00:18:53'),
(2, 2, 120, '2021-11-03 00:18:34'),
(4, 1, 1500, '2021-11-07 00:18:53');

-- --------------------------------------------------------

--
-- Table structure for table `medicine`
--

DROP TABLE IF EXISTS `medicine`;
CREATE TABLE `medicine` (
  `id` int(11) NOT NULL,
  `iduser` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  `begindate` timestamp NOT NULL DEFAULT current_timestamp(),
  `enddate` timestamp NULL DEFAULT NULL,
  `dose` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `medicine`
--

INSERT INTO `medicine` (`id`, `iduser`, `name`, `begindate`, `enddate`, `dose`) VALUES
(6, 1, 'Dafalgan forte', '2021-11-05 13:33:30', '2021-11-25 21:26:08', '50'),
(7, 1, 'Tramactile', '2021-11-05 13:33:51', NULL, '5'),
(9, 2, 'Solmucalm', '2021-11-05 13:48:54', NULL, '500ml');

-- --------------------------------------------------------

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification` (
  `id` int(11) NOT NULL,
  `iduser` int(11) NOT NULL,
  `notificationtype` varchar(50) NOT NULL,
  `date` timestamp NOT NULL DEFAULT current_timestamp(),
  `longitude` DOUBLE NOT NULL,
  `latitude` DOUBLE NOT NULL,
  `isclosed` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `notification`
--

INSERT INTO `notification` (`id`, `iduser`, `notificationtype`, `date`, `longitude`, `latitude`, `isclosed`) VALUES
(1, 4, 'FALL', '2021-11-07 00:36:28', 55, 100, 0),
(2, 4, 'HEART_HIGH', '2021-11-09 00:35:52', 130, 106, 1),
(4, 2, 'TREATMENT_NOT_TAKEN', '2021-11-07 00:36:28', 55, 100, 0);

-- --------------------------------------------------------

--
-- Table structure for table `planning`
--

DROP TABLE IF EXISTS `planning`;
CREATE TABLE `planning` (
  `id` int(11) NOT NULL,
  `idmedicine` int(11) NOT NULL,
  `day` varchar(10) DEFAULT NULL,
  `time` time NOT NULL,
  `quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `planning`
--

INSERT INTO `planning` (`id`, `idmedicine`, `day`, `time`, `quantity`) VALUES
(3, 6, 'MONDAY', '15:30:00', 10),
(4, 9, NULL, '10:00:00', 5),
(6, 6, 'MONDAY', '19:30:00', 99);

-- --------------------------------------------------------

--
-- Table structure for table `traitement`
--

DROP TABLE IF EXISTS `traitement`;
CREATE TABLE `traitement` (
  `id` int(11) NOT NULL,
  `idplanning` int(11) NOT NULL,
  `date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `istaken` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `traitement`
--

INSERT INTO `traitement` (`id`, `idplanning`, `date`, `istaken`) VALUES
(1, 4, '2021-11-06 23:17:27', 0),
(2, 4, '2021-11-22 23:16:59', 1),
(3, 6, '2021-11-06 23:17:46', 1);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `username` varchar(30) NOT NULL,
  `password` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `firstname` varchar(30) NOT NULL,
  `lastname` varchar(30) NOT NULL,
  `picture` varchar(250) DEFAULT NULL,
  `iscarekeeper` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `username`, `password`, `email`, `firstname`, `lastname`, `picture`, `iscarekeeper`) VALUES
(1, 'ilyas.boillat', '81dc9bdb52d04dc20036dbd8313ed055', 'ilyas.boillat@gmail.com', 'Ilyas', 'Boillat', 'ilyas.png', 0),
(2, 'david.oktay', '81dc9bdb52d04dc20036dbd8313ed055', 'david.oktay@gmail.com', 'David', 'Oktay', 'david.pnj', 1),
(4, 'william.bikuta', '81dc9bdb52d04dc20036dbd8313ed055', 'william.bikuta@gmail.com', 'William', 'Bikuta', 'william.png', 0),
(6, 'milan.cervino', '81dc9bdb52d04dc20036dbd8313ed055', 'milan.cervino@gmail.com', 'Milan', 'Cervino', 'milan.png', 0);

--
-- Indexes for dumped tables
--
  

--
-- Indexes for table `follow`
--
ALTER TABLE `follow`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk1` (`iduserpatient`,`idusercarekeeper`),
  ADD KEY `fk_user1_follow` (`idusercarekeeper`);

--
-- Indexes for table `measure`
--
ALTER TABLE `measure`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk1` (`iduser`,`heartrate`);

--
-- Indexes for table `medicine`
--
ALTER TABLE `medicine`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK1` (`iduser`,`name`,`begindate`);

--
-- Indexes for table `notification`
--
ALTER TABLE `notification`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk1` (`iduser`,`notificationtype`,`date`);

--
-- Indexes for table `planning`
--
ALTER TABLE `planning`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK1` (`idmedicine`,`day`,`time`);

--
-- Indexes for table `traitement`
--
ALTER TABLE `traitement`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk1` (`idplanning`,`date`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk1` (`username`),
  ADD UNIQUE KEY `uk2` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `follow`
--
ALTER TABLE `follow`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `measure`
--
ALTER TABLE `measure`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `medicine`
--
ALTER TABLE `medicine`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `notification`
--
ALTER TABLE `notification`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `planning`
--
ALTER TABLE `planning`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `traitement`
--
ALTER TABLE `traitement`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `follow`
--
ALTER TABLE `follow`
  ADD CONSTRAINT `fk_user1_follow` FOREIGN KEY (`idusercarekeeper`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_user2_follow` FOREIGN KEY (`iduserpatient`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `measure`
--
ALTER TABLE `measure`
  ADD CONSTRAINT `fk_user_measure` FOREIGN KEY (`iduser`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `medicine`
--
ALTER TABLE `medicine`
  ADD CONSTRAINT `FK_user_medicine` FOREIGN KEY (`iduser`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `notification`
--
ALTER TABLE `notification`
  ADD CONSTRAINT `fk_notification_user` FOREIGN KEY (`iduser`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `traitement`
--
ALTER TABLE `traitement`
  ADD CONSTRAINT `fk_planning_traitement` FOREIGN KEY (`idplanning`) REFERENCES `planning` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
