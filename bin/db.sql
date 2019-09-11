-- mysqldump-php https://github.com/ifsnop/mysqldump-php
--
-- Host: 127.0.0.1	Database: septyan
-- ------------------------------------------------------
-- Server version 	5.5.5-10.3.16-MariaDB
-- Date: Wed, 11 Sep 2019 07:01:04 +0200

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(191) NOT NULL,
  `password` varchar(191) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `admin` VALUES (9,'admin','21232f297a57a5a743894a0e4a801fc3');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `admin` with 1 row(s)
--

--
-- Table structure for table `bayar_spp`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bayar_spp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_spp` int(11) NOT NULL,
  `id_siswa` int(11) NOT NULL,
  `tanggal` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_spp` (`id_spp`),
  KEY `id_siswa` (`id_siswa`),
  CONSTRAINT `bayar_spp_ibfk_1` FOREIGN KEY (`id_spp`) REFERENCES `spp` (`id`),
  CONSTRAINT `bayar_spp_ibfk_2` FOREIGN KEY (`id_siswa`) REFERENCES `siswa` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bayar_spp`
--

LOCK TABLES `bayar_spp` WRITE;
/*!40000 ALTER TABLE `bayar_spp` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `bayar_spp` VALUES (4,5,1,'2019-09-04'),(5,8,1,'2019-09-13'),(6,5,2,'2019-09-05');
/*!40000 ALTER TABLE `bayar_spp` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `bayar_spp` with 3 row(s)
--

--
-- Table structure for table `kelas`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kelas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `kelas` varchar(191) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kelas`
--

LOCK TABLES `kelas` WRITE;
/*!40000 ALTER TABLE `kelas` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `kelas` VALUES (7,'I'),(8,'II'),(9,'III');
/*!40000 ALTER TABLE `kelas` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `kelas` with 3 row(s)
--

--
-- Table structure for table `siswa`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `siswa` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nama` varchar(191) NOT NULL,
  `nis` varchar(191) NOT NULL,
  `jenis_kelamin` varchar(191) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `siswa`
--

LOCK TABLES `siswa` WRITE;
/*!40000 ALTER TABLE `siswa` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `siswa` VALUES (1,'Agung Sapto Margono Dh','15753003','Laki-Laki'),(2,'Perempuan 1','15753000','Perempuan');
/*!40000 ALTER TABLE `siswa` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `siswa` with 2 row(s)
--

--
-- Table structure for table `spp`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `spp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_kelas` int(11) NOT NULL,
  `id_tahun_ajaran` int(11) NOT NULL,
  `spp` int(11) NOT NULL,
  `operasional` int(11) NOT NULL,
  `beras` int(11) NOT NULL,
  `daftar_ulang` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_kelas` (`id_kelas`),
  KEY `id_tahun_ajaran` (`id_tahun_ajaran`),
  CONSTRAINT `spp_ibfk_2` FOREIGN KEY (`id_kelas`) REFERENCES `kelas` (`id`),
  CONSTRAINT `spp_ibfk_3` FOREIGN KEY (`id_tahun_ajaran`) REFERENCES `tahun_ajaran` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spp`
--

LOCK TABLES `spp` WRITE;
/*!40000 ALTER TABLE `spp` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `spp` VALUES (5,7,6,123123,410,141240,1241241240),(7,9,6,0,0,0,0),(8,7,7,0,0,0,0),(9,8,7,0,0,0,0);
/*!40000 ALTER TABLE `spp` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `spp` with 4 row(s)
--

--
-- Table structure for table `tahun_ajaran`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tahun_ajaran` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tahun_ajaran` varchar(191) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tahun_ajaran`
--

LOCK TABLES `tahun_ajaran` WRITE;
/*!40000 ALTER TABLE `tahun_ajaran` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `tahun_ajaran` VALUES (6,'2018/2019'),(7,'2019/2020');
/*!40000 ALTER TABLE `tahun_ajaran` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `tahun_ajaran` with 2 row(s)
--

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on: Wed, 11 Sep 2019 07:01:04 +0200
