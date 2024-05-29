-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 24, 2024 at 10:36 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `munchmap`
--

-- --------------------------------------------------------

--
-- Table structure for table `tblaccount`
--

CREATE TABLE `tblaccount` (
  `acc_id` int(11) NOT NULL,
  `username` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `admin_status` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tblaccount`
--

INSERT INTO `tblaccount` (`acc_id`, `username`, `email`, `password`, `admin_status`) VALUES
(1, 'kiyo', 'kiyo@email.com', '123', 0);

-- --------------------------------------------------------

--
-- Table structure for table `tblbarangay`
--

CREATE TABLE `tblbarangay` (
  `barangay_id` int(10) NOT NULL,
  `barangay_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tblbarangay`
--

INSERT INTO `tblbarangay` (`barangay_id`, `barangay_name`) VALUES
(3, 'guadalupe'),
(4, 'talamban'),
(2, 'tisa'),
(1, 'urgello');

-- --------------------------------------------------------

--
-- Table structure for table `tblplace`
--

CREATE TABLE `tblplace` (
  `place_id` int(10) NOT NULL,
  `barangay_id` int(10) NOT NULL,
  `place_name` varchar(255) NOT NULL,
  `place_type` varchar(255) NOT NULL,
  `place_address` varchar(255) NOT NULL,
  `place_landmark` varchar(255) NOT NULL,
  `place_about` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tblplace`
--

INSERT INTO `tblplace` (`place_id`, `barangay_id`, `place_name`, `place_type`, `place_address`, `place_landmark`, `place_about`) VALUES
(1, 1, 'the crunch', 'unli rice', 'sambag 1', 'Beside EDCH Foodpark', 'Unlimited Java Rice and Friend Chicken'),
(2, 1, 'ngohiong express', '24hrs Food', 'Sambag 1', 'Beside EDCH Foodpark', '24 hours, fried food, noodles, silogan'),
(3, 2, 'Siomai Express', 'Siomai', 'Tisa', 'Beside Chatime', 'Siomai place');

-- --------------------------------------------------------

--
-- Table structure for table `tblreviews`
--

CREATE TABLE `tblreviews` (
  `review_id` int(11) NOT NULL,
  `place_id` int(10) NOT NULL,
  `acc_id` int(11) NOT NULL,
  `rating` decimal(2,1) NOT NULL CHECK (`rating` >= 0.0 and `rating` <= 5.0),
  `comment` varchar(255) DEFAULT NULL,
  `isApproved` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tblreviews`
--

INSERT INTO `tblreviews` (`review_id`, `place_id`, `acc_id`, `rating`, `comment`, `isApproved`) VALUES
(1, 1, 1, 5.0, 'crunchy yum', 1),
(2, 1, 1, 2.0, 'meh?', 1),
(3, 3, 1, 3.0, 'cool', 1),
(4, 2, 1, 4.0, 'decent', 1),
(5, 2, 1, 2.0, 'okay lang pozxc', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tblaccount`
--
ALTER TABLE `tblaccount`
  ADD PRIMARY KEY (`acc_id`);

--
-- Indexes for table `tblbarangay`
--
ALTER TABLE `tblbarangay`
  ADD PRIMARY KEY (`barangay_id`),
  ADD UNIQUE KEY `barangay_name` (`barangay_name`);

--
-- Indexes for table `tblplace`
--
ALTER TABLE `tblplace`
  ADD PRIMARY KEY (`place_id`),
  ADD KEY `barangay_id` (`barangay_id`);

--
-- Indexes for table `tblreviews`
--
ALTER TABLE `tblreviews`
  ADD PRIMARY KEY (`review_id`),
  ADD KEY `place_id` (`place_id`),
  ADD KEY `acc_id` (`acc_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tblaccount`
--
ALTER TABLE `tblaccount`
  MODIFY `acc_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `tblbarangay`
--
ALTER TABLE `tblbarangay`
  MODIFY `barangay_id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `tblplace`
--
ALTER TABLE `tblplace`
  MODIFY `place_id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `tblreviews`
--
ALTER TABLE `tblreviews`
  MODIFY `review_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tblplace`
--
ALTER TABLE `tblplace`
  ADD CONSTRAINT `tblplace_ibfk_1` FOREIGN KEY (`barangay_id`) REFERENCES `tblbarangay` (`barangay_id`) ON DELETE CASCADE;

--
-- Constraints for table `tblreviews`
--
ALTER TABLE `tblreviews`
  ADD CONSTRAINT `tblreviews_ibfk_1` FOREIGN KEY (`place_id`) REFERENCES `tblplace` (`place_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `tblreviews_ibfk_2` FOREIGN KEY (`acc_id`) REFERENCES `tblaccount` (`acc_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
