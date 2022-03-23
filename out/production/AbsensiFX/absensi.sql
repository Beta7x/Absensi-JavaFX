-- phpMyAdmin SQL Dump
-- version 5.1.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Mar 23, 2022 at 01:30 PM
-- Server version: 10.7.3-MariaDB
-- PHP Version: 8.1.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `absensi`
--

-- --------------------------------------------------------

--
-- Table structure for table `absence`
--

CREATE TABLE `absence` (
  `id_absen` int(11) NOT NULL,
  `id_pegawai` int(2) NOT NULL,
  `tanggal` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `waktu` varchar(8) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('ALPA','HADIR','SAKIT','IZIN') COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `absence`
--

INSERT INTO `absence` (`id_absen`, `id_pegawai`, `tanggal`, `waktu`, `status`) VALUES
(1, 1, '2022-03-22', '08:00', 'HADIR'),
(2, 2, '2022-03-22', '08:30', 'SAKIT'),
(3, 1, '2022-03-22', '09:00', 'SAKIT'),
(4, 1, '2022-3-16', '09:00', 'HADIR');

-- --------------------------------------------------------

--
-- Table structure for table `admins`
--

CREATE TABLE `admins` (
  `id` int(2) NOT NULL,
  `username` varchar(11) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `admins`
--

INSERT INTO `admins` (`id`, `username`, `password`) VALUES
(1, 'admin', 'admin123'),
(2, 'root', 'root123');

-- --------------------------------------------------------

--
-- Table structure for table `employee`
--

CREATE TABLE `employee` (
  `id_pegawai` int(11) NOT NULL,
  `nomor_pegawai` char(18) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nama` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ttl` date DEFAULT NULL,
  `alamat` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `employee`
--

INSERT INTO `employee` (`id_pegawai`, `nomor_pegawai`, `nama`, `ttl`, `alamat`) VALUES
(1, '200106302002061001', 'Widies Ade Priyanto', '2001-06-30', 'Gumayun RT 14/RW 05, Kecamatan Dukuhwaru, Kabupaten Tegal'),
(2, '200106302002061002', 'Ade Priyanto', '2001-06-30', 'Gumayun RT 14/RW 05, Kecamatan Dukuhwaru, Kabupaten Tegal'),
(3, '200106302002061003', 'Siti Desi Arisandi', '2001-06-30', 'Gumayun RT 14/RW 05, Kecamatan Dukuhwaru, Kabupaten Tegal'),
(4, '200007282002061003', 'Aulia Insani', '2001-06-30', 'Gumayun RT 14/RW 05, Kecamatan Dukuhwaru, Kabupaten Tegal');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `absence`
--
ALTER TABLE `absence`
  ADD PRIMARY KEY (`id_absen`),
  ADD KEY `FK_EmployeeAbsence` (`id_pegawai`);

--
-- Indexes for table `admins`
--
ALTER TABLE `admins`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `employee`
--
ALTER TABLE `employee`
  ADD PRIMARY KEY (`id_pegawai`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `absence`
--
ALTER TABLE `absence`
  MODIFY `id_absen` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `admins`
--
ALTER TABLE `admins`
  MODIFY `id` int(2) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `employee`
--
ALTER TABLE `employee`
  MODIFY `id_pegawai` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `absence`
--
ALTER TABLE `absence`
  ADD CONSTRAINT `FK_EmployeeAbsence` FOREIGN KEY (`id_pegawai`) REFERENCES `employee` (`id_pegawai`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
