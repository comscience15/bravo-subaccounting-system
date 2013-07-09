-- MySQL dump 10.13  Distrib 5.5.16, for Win32 (x86)
--
-- Host: localhost    Database: bnymsubaccounting
-- ------------------------------------------------------
-- Server version	5.5.24

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
-- Dumping data for table `checktransaction`
--

LOCK TABLES `checktransaction` WRITE;
/*!40000 ALTER TABLE `checktransaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `checktransaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES ('1','junior','bnym',NULL,NULL,NULL,'959 hilltop street','Munhall','1','Pennsylvania','15120'),('2','ashish','bnym',NULL,NULL,NULL,'q','q','1','Alabama','12355'),('3','yuwu','bnym',NULL,NULL,NULL,'q','q','1','Alabama','12345'),('4','daniel','bnym',NULL,NULL,NULL,'q','q','1','Alabama','12345'),('5','Josh','bnym',NULL,NULL,NULL,'500 Grant St','Pittsburgh','1','Alabama','15259'),('6','jib','bnym',NULL,NULL,NULL,'wightman street','pittsburgh','1','Pennsylvania','15217'),('7','vinayak','bnym',NULL,NULL,NULL,'wightman street','pittsburgh','1','Pennsylvania','15217'),('8','new','bnym',NULL,NULL,NULL,'5000 fifth ave','pittsburgh','1','Pennsylvania','15213');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `transaction`
--

LOCK TABLES `transaction` WRITE;
/*!40000 ALTER TABLE `transaction` DISABLE KEYS */;
INSERT INTO `transaction` VALUES (186,88.05,'2012-08-20 16:51:32',NULL,186,NULL,'Purchase Item : Dragon Girl','2012-08-20 16:51:32','300','2217'),(187,50.65,'2012-08-20 16:52:12',NULL,187,NULL,'Purchase Item : C++','2012-08-20 16:52:12','300','2269'),(188,89.12,'2012-08-20 16:52:34',NULL,188,NULL,'Purchase Item : My SQL','2012-08-20 16:52:34','300','2269'),(189,93.64,'2012-08-20 16:52:39',NULL,189,NULL,'Purchase Item : Python','2012-08-20 16:52:39','300','2217'),(215,56.40,'2012-08-21 03:24:24',NULL,215,NULL,'Purchase Item : Java Programming','2012-08-21 03:24:24','300','2880'),(216,31.03,'2012-08-21 03:24:26',NULL,216,NULL,'Purchase Item : Networks','2012-08-21 03:24:26','300','2880'),(217,20.00,'2012-08-21 03:27:21',NULL,215,NULL,'Get Refund','2012-08-21 03:27:21','300','2880'),(287,10.00,'2012-08-23 03:30:14',NULL,NULL,NULL,'Check Refill',NULL,'200','iil2a23v'),(288,-28.50,'2012-08-23 03:31:55',NULL,288,NULL,'Self Checkout',NULL,'200','iil2a23v'),(289,200.00,'2012-08-23 03:32:57',NULL,NULL,NULL,'Check Refill',NULL,'200','iil2a23v'),(290,25.00,'2012-08-23 03:51:55',NULL,NULL,NULL,'Check Refill',NULL,'200','iil2a23v'),(291,12.00,'2012-08-23 03:55:36',NULL,NULL,NULL,'Credit Card Refill',NULL,'200','iil2a23v'),(292,100.00,'2012-08-23 04:01:11',NULL,NULL,NULL,'Check Refill',NULL,'200','sm6gmd1u'),(293,100.00,'2012-08-23 04:05:33',NULL,NULL,'','Cash Refill',NULL,'200','sm6gmd1u'),(294,-20.50,'2012-08-23 04:12:25',NULL,294,'craig','Purchase Item','2012-08-23 04:12:12','200','sm6gmd1u'),(295,-20.50,'2012-08-23 04:13:15',NULL,295,'craig','Purchase Item','2012-08-23 04:12:54','200','sm6gmd1u'),(296,100.00,'2012-08-23 04:15:39',NULL,NULL,NULL,'Credit Card Refill',NULL,'200','77mi55sc'),(297,-50.00,'2012-08-23 04:16:47','Send to someone\n',NULL,NULL,'Send Gift',NULL,'200','77mi55sc'),(298,50.00,'2012-08-23 04:16:47','',NULL,NULL,'Receive Gift',NULL,'200','sm6gmd1u'),(299,-30.50,'2012-08-23 04:18:12',NULL,299,NULL,'Self Checkout',NULL,'200','77mi55sc'),(300,23.00,'2012-08-23 04:27:34',NULL,NULL,NULL,'Credit Card Refill',NULL,'200','gtuupt4s'),(301,-27.25,'2012-08-23 17:22:52',NULL,301,'craig','Purchase Item','2012-08-23 17:21:57','200','iil2a23v'),(302,100.00,'2012-08-23 17:23:33',NULL,NULL,NULL,'Credit Card Refill',NULL,'200','4cf6o9go'),(303,13.25,'2012-08-23 17:24:38',NULL,303,NULL,'Self Checkout',NULL,'200','4cf6o9go'),(304,-9.25,'2012-08-23 17:25:32',NULL,304,NULL,'Self Checkout',NULL,'200','iil2a23v'),(305,1000.00,'2012-08-23 17:34:27',NULL,NULL,NULL,'Check Refill',NULL,'200','hheibdoi'),(306,-100.00,'2012-08-23 17:35:00','Vhf\n',NULL,NULL,'Send Gift',NULL,'200','hheibdoi'),(307,100.00,'2012-08-23 17:35:00','',NULL,NULL,'Receive Gift',NULL,'200','iil2a23v'),(308,150.00,'2012-08-23 17:57:08',NULL,NULL,NULL,'Credit Card Refill',NULL,'200','4cf6o9go'),(309,14.25,'2012-08-23 17:58:54',NULL,309,'craig','Purchase Item','2012-08-23 17:57:22','200','4cf6o9go'),(310,13.25,'2012-08-23 17:59:37',NULL,310,NULL,'Self Checkout',NULL,'200','4cf6o9go'),(311,200.00,'2012-08-23 20:16:58',NULL,NULL,NULL,'Check Refill',NULL,'200','1111'),(312,-11.25,'2012-08-23 20:50:36',NULL,312,'craig','Purchase Item','2012-08-23 20:50:05','200','sm6gmd1u'),(313,-30.25,'2012-08-23 20:51:53',NULL,313,NULL,'Self Checkout',NULL,'200','sm6gmd1u'),(314,-100.00,'2012-08-23 20:54:07','',NULL,NULL,'Send Gift',NULL,'200','iil2a23v'),(315,100.00,'2012-08-23 20:54:07','',NULL,NULL,'Receive Gift',NULL,'200','sm6gmd1u'),(316,-34.50,'2012-08-23 22:01:59',NULL,316,'craig','Purchase Item','2012-08-23 22:01:45','200','iil2a23v'),(317,-16.25,'2012-08-23 22:33:29',NULL,317,'craig','Purchase Item','2012-08-23 22:33:18','200','iil2a23v'),(318,-20.25,'2012-08-23 23:14:06',NULL,318,'craig','Purchase Item','2012-08-23 23:13:57','200','iil2a23v'),(319,-14.25,'2012-08-24 01:13:07',NULL,319,'craig','Purchase Item','2012-08-24 01:12:50','200','iil2a23v'),(320,-100.00,'2012-08-24 01:15:43','gift',NULL,NULL,'Send Gift',NULL,'200','1111'),(321,100.00,'2012-08-24 01:15:43','',NULL,NULL,'Receive Gift',NULL,'200','iil2a23v'),(322,-7.25,'2012-08-24 01:17:17',NULL,322,NULL,'Self Checkout',NULL,'200','1111'),(323,500.00,'2012-08-24 02:49:26',NULL,NULL,NULL,'Check Refill',NULL,'200','1111'),(324,-10.25,'2012-08-24 02:54:22',NULL,324,'craig','Purchase Item','2012-08-24 02:54:00','200','iil2a23v'),(325,-100.00,'2012-08-24 02:56:38','',NULL,NULL,'Send Gift',NULL,'200','1111'),(326,100.00,'2012-08-24 02:56:38','',NULL,NULL,'Receive Gift',NULL,'200','iil2a23v'),(327,-1.25,'2012-08-24 02:57:57',NULL,327,NULL,'Self Checkout',NULL,'200','iil2a23v'),(328,-200.00,'2012-08-24 03:23:15','',NULL,NULL,'Send Gift',NULL,'200','iil2a23v'),(329,200.00,'2012-08-24 03:23:15','',NULL,NULL,'Receive Gift',NULL,'200','1111'),(330,-6.25,'2012-08-24 03:45:11',NULL,330,'craig','Purchase Item','2012-08-24 03:44:40','200','iil2a23v'),(331,-100.00,'2012-08-24 03:47:05','',NULL,NULL,'Send Gift',NULL,'200','1111'),(332,100.00,'2012-08-24 03:47:05','',NULL,NULL,'Receive Gift',NULL,'200','iil2a23v'),(333,-5.25,'2012-08-24 03:48:07',NULL,333,NULL,'Self Checkout',NULL,'200','iil2a23v'),(334,-21.25,'2012-08-24 04:25:22',NULL,334,'craig','Purchase Item','2012-08-24 04:24:44','200','iil2a23v'),(335,-100.00,'2012-08-24 04:27:33','',NULL,NULL,'Send Gift',NULL,'200','1111'),(336,100.00,'2012-08-24 04:27:33','',NULL,NULL,'Receive Gift',NULL,'200','iil2a23v'),(337,-49.25,'2012-08-24 04:28:52',NULL,337,NULL,'Self Checkout',NULL,'200','iil2a23v'),(338,50.00,'2012-08-24 13:15:39',NULL,NULL,NULL,'Credit Card Refill',NULL,'200','4cf6o9go'),(339,20.25,'2012-08-24 13:18:35',NULL,339,'craig','Purchase Item','2012-08-24 13:17:20','200','4cf6o9go'),(340,3.25,'2012-08-24 13:20:05',NULL,340,NULL,'Self Checkout',NULL,'200','4cf6o9go');
/*!40000 ALTER TABLE `transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `orderitem`
--

LOCK TABLES `orderitem` WRITE;
/*!40000 ALTER TABLE `orderitem` DISABLE KEYS */;
INSERT INTO `orderitem` VALUES (288,'012000071744','012000071744',1.00,0.00,6.25,'1'),(288,'739510100701','739510100701',1.00,0.00,22.25,'1'),(294,'739510100701','739510100701',2.00,0.00,20.50,'1'),(295,'739510100701','739510100701',2.00,0.00,20.50,'1'),(299,'coffee','coffee',3.00,0.00,58.75,'1'),(301,'The Hunger Games','The Hunger Games',1.00,0.00,27.25,'1'),(303,'311917045597','311917045597',1.00,0.00,13.25,'1'),(304,'Fifty Shades of Grey','Fifty Shades of Grey',1.00,0.00,9.25,'1'),(309,'636920000587','636920000587',1.00,0.00,14.25,'1'),(310,'9780596000585','9780596000585',1.00,0.00,13.25,'1'),(312,'The Hunger Games','The Hunger Games',1.00,0.00,11.25,'1'),(313,'Fifty Shades of Grey','Fifty Shades of Grey',1.00,0.00,30.25,'1'),(316,'The Hunger Games','The Hunger Games',2.00,0.00,34.50,'1'),(317,'The Hunger Games','The Hunger Games',1.00,0.00,16.25,'1'),(318,'The Hunger Games','The Hunger Games',1.00,0.00,20.25,'1'),(319,'The Hunger Games','The Hunger Games',1.00,0.00,14.25,'1'),(322,'Fifty Shades of Grey','Fifty Shades of Grey',1.00,0.00,7.25,'1'),(324,'The Hunger Games','The Hunger Games',1.00,0.00,10.25,'1'),(327,'Fifty Shades of Grey','Fifty Shades of Grey',1.00,0.00,1.25,'1'),(330,'The Hunger Games','The Hunger Games',1.00,0.00,6.25,'1'),(333,'Fifty Shades of Grey','Fifty Shades of Grey',1.00,0.00,5.25,'1'),(334,'Mockingjay','Mockingjay',1.00,0.00,21.25,'1'),(337,'Fifty Shades of Grey','Fifty Shades of Grey',1.00,0.00,49.25,'1'),(339,'636010000527','636010000527',1.00,0.00,20.25,'1'),(340,'636920000587','636920000587',1.00,0.00,3.25,'1');
/*!40000 ALTER TABLE `orderitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `merchant`
--

LOCK TABLES `merchant` WRITE;
/*!40000 ALTER TABLE `merchant` DISABLE KEYS */;
INSERT INTO `merchant` VALUES ('200','Pittsburgh Bookstore','5000 Forbes Ave, Pittsburgh, PA',12570.00),('300','UPitt Coffee','2650 Forbes Ave, Pittsburgh, PA',50000.00);
/*!40000 ALTER TABLE `merchant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `bnymadmin`
--

LOCK TABLES `bravoadmin` WRITE;
/*!40000 ALTER TABLE `bnymadmin` DISABLE KEYS */;
INSERT INTO `bravoadmin` VALUES ('admin@bnymellon.com','bnym','1'),('josh@alumni.cmu.edu','bnym','1');
/*!40000 ALTER TABLE `bnymadmin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `paymentprofile`
--

LOCK TABLES `paymentprofile` WRITE;
/*!40000 ALTER TABLE `paymentprofile` DISABLE KEYS */;
INSERT INTO `paymentprofile` VALUES (36,'test - 4567','A','124567','12345678',NULL,NULL,NULL,NULL,NULL,'P','0111','0111',NULL,NULL,NULL,NULL,'2'),(37,'yuwu - 1111','A','1111111','12345678',NULL,NULL,NULL,NULL,NULL,'P','0111','0111',NULL,NULL,NULL,NULL,'3'),(38,'Josh- visa - 3355','C','76653355',NULL,NULL,NULL,'Josh','','Bryant','P','0115','OP','500 Grant St','Pittsburgh','Pennsylvania','15259','5'),(39,'Josh checking - 3344','A','11223344','443322115',NULL,NULL,NULL,NULL,NULL,'P','0111','0111',NULL,NULL,NULL,NULL,'5'),(40,'Josh checking - 6678','A','12346678','112233445',NULL,NULL,NULL,NULL,NULL,'P','0111','0111',NULL,NULL,NULL,NULL,'5'),(41,'Josh Savings - 6678','A','12346678','112233445',NULL,NULL,NULL,NULL,NULL,'P','0111','0111',NULL,NULL,NULL,NULL,'5'),(42,'jr - 0000','A','10000','12345678',NULL,NULL,NULL,NULL,NULL,'P','0111','0111',NULL,NULL,NULL,NULL,'3'),(43,'Josh-Savings - 6678','A','12346678','112233445',NULL,NULL,NULL,NULL,NULL,'P','0111','0111',NULL,NULL,NULL,NULL,'5'),(44,'vin-credit - 5678','C','12345678',NULL,NULL,NULL,'vinayak','','agrawal','P','0114','OP','wightman','pittsburgh','Pennsylvania','15217','7'),(45,'A113455:5','A','113455','123456',NULL,NULL,NULL,NULL,NULL,'P','0111','0111',NULL,NULL,NULL,NULL,'5'),(46,'yuwu - 1111','A','1111','12345678',NULL,NULL,NULL,NULL,NULL,'P','0111','0111',NULL,NULL,NULL,NULL,'7'),(47,'Dan - 1111','A','1111','1111111',NULL,NULL,NULL,NULL,NULL,'P','0111','0111',NULL,NULL,NULL,NULL,'4'),(48,'Xiaoming - 1234','C','1234',NULL,NULL,NULL,'Xiaoming','M','Guo','P','0111','OP','A','A','Alabama','12345','4'),(49,'Xiaoming-new - 2356','C','12356',NULL,NULL,NULL,'Xg','M','Guo','P','0111','OP','A','A','Alabama','51537','4'),(50,'A12345:4','A','12345','13456',NULL,NULL,NULL,NULL,NULL,'P','0111','0111',NULL,NULL,NULL,NULL,'4'),(51,'Jib checking - 1763','A','123231763','84677425',NULL,NULL,NULL,NULL,NULL,'P','0111','0111',NULL,NULL,NULL,NULL,'6'),(52,'C1234133312341234:6','C','1234133312341234',NULL,NULL,NULL,'jib','','cha','P','0518','OP','544 beacon st','pittsburgh','Pennsylvania','15213','6'),(53,'hello - 4321','C','4321876598764321',NULL,NULL,NULL,'new','','guest','P','0116','OP','5677 murry ave','pittsburgh','Pennsylvania','15212','8'),(54,'Junior Checking - 5678','A','12345678','24225753',NULL,NULL,NULL,NULL,NULL,'P','0111','0111',NULL,NULL,NULL,NULL,'1');
/*!40000 ALTER TABLE `paymentprofile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `merchantrole`
--

LOCK TABLES `merchantrole` WRITE;
/*!40000 ALTER TABLE `merchantrole` DISABLE KEYS */;
INSERT INTO `merchantrole` VALUES (1,'jeff@cmu.edu','ROLE_MANAGER','200');
/*!40000 ALTER TABLE `merchantrole` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `creditcardtransaction`
--

LOCK TABLES `creditcardtransaction` WRITE;
/*!40000 ALTER TABLE `creditcardtransaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `creditcardtransaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `persistent_logins`
--

LOCK TABLES `persistent_logins` WRITE;
/*!40000 ALTER TABLE `persistent_logins` DISABLE KEYS */;
INSERT INTO `persistent_logins` VALUES ('merchant:ashish','+27bKZ6BSn4+YnkMZw19cA==','200/G3SWPjm7KdKJ2gZl78wEZw==','2012-08-23 22:01:58'),('merchant:jeff@cmu.edu','+htXQHCi5COZLX46kn7u1Q==','200/7M+PhBRyV/1gjpuuT6No5A==','2012-08-23 23:36:15'),('customer:junior','/hTNlcyWec252X7tiIG32g==','200/hl+N/IP0kIGU1uZMSfn7cA==','2012-08-24 02:40:50'),('merchant:ashish','0jBakVnF5P2ExA+RrfyAoQ==','200/JIQasFmjza6xv4vz6pJSyQ==','2012-08-23 21:53:40'),('customer:jib','1dX8Cg+6AozH5Hlofwo+hw==','200/7O0zL/xOVtigtl5Gzj6bag==','2012-08-24 01:13:32'),('customer:junior','5appwbqq5mVyAUEf5UhFRg==','200/56KvLHIAOgSH79NOg7Z3fQ==','2012-08-23 22:29:59'),('merchant:jeff@cmu.edu:200','5cTTJzoUUPaGxWTXnyI19w==','SCYAzW3xOjeEtTXesjXjmQ==','2012-08-24 13:18:34'),('merchant:tt','5VE4NpPOnzRquNi9kvCf4w==','200/Vx0j0+bIaz7YGfXer9vZXQ==','2012-08-23 19:50:02'),('merchant:ashish','8CVrhVB+k1Ci38IqHGNmQQ==','200/3CEOy7mTJWO9+DsmxUhjiw==','2012-08-23 17:22:52'),('customer:yuwu','8pF4LCYn3oXp+HL7eNZtfA==','200/ChUVe8DmXSW3LQXi+qCQFQ==','2012-08-23 17:18:27'),('merchant:tt','a7g4rpnyHF2nx0TTpGIDtQ==','200/e1kryweSunC/S96e3miLlg==','2012-08-24 00:27:42'),('merchant:tt','aq/N9JW05xWQV1Cgx3En7A==','200/yWl1Kb9vfi/7POKV+tvboQ==','2012-08-24 04:25:21'),('merchant:ashish','Azy985r3z97i3JQSVqh/Bg==','200/3ImqFIl4dowbg7/5pyUXzg==','2012-08-23 20:11:15'),('customer:jib','c4Nl8yc95WNKRVUoz/Bb4g==','200/0qvlBTvSpPFFokOxfsO1rg==','2012-08-23 04:14:08'),('customer:junior','CB7v1lwvAKRXLQzXFqnJWQ==','200/IM4MWREog/8IVwA+0Rxr2Q==','2012-08-24 04:27:34'),('customer:junior','EIiwmQ7T6FJCpcI13j5oVw==','200/jPSSF5wDW2khUzB4DyOqQw==','2012-08-24 00:28:38'),('merchant:ashish','EO5vVNRxD66k7H3a6GqlOQ==','200/mXzf5snMRmLu025X/2WlPA==','2012-08-23 23:14:05'),('merchant:tt','Eqva7wKNftFZMRMFtoydTQ==','200/PfgvKlQ4EaH49db7AJYINg==','2012-08-23 04:05:33'),('customer:jib','eXToK/PEdcX/sCmKVspJ6w==','200/DXN0i90Xx03ywfR6B1R2qw==','2012-08-23 22:02:04'),('customer:junior','fcurFNlY8N0WJMN+6IBd0g==','200/ktMMZgd4kZPrBMWaUcgQmg==','2012-08-24 00:28:09'),('customer:junior','FDC5ALfv9HJ02cp24+Vs8w==','200//fHUOWMtz27dbOKO5Z8nnw==','2012-08-23 22:30:30'),('customer:junior','FsrjYNA/Drs1U19vOoV0Jw==','200/ke7y2JZxnlEE6UOZDg5PcA==','2012-08-23 23:40:46'),('customer:junior','gsVbpT1u+7cbyWA8/+N4iQ==','200/BhOud8y+USdcLR/oPHQdiA==','2012-08-24 02:56:55'),('merchant:tt','htuAZwyExhHQoqEDdP8z/A==','200/Bul7BPuZUrzIQpNSByRKvQ==','2012-08-23 04:13:14'),('customer:daniel','iLWo6eo4UVLVRIYMyeSEww==','200/zxzeCS+J3FLTukTMQbKnBQ==','2012-08-23 19:02:07'),('customer:junior','IzcMPoqMg2JB1mFJkpRc0Q==','200/PpgpyJw94sJtO8oSiiylbg==','2012-08-24 03:47:06'),('customer:jib','KLC0hQQj70AU2NzsmDzCjA==','200/f6o4JYjy+RSRcoYcsyzaFw==','2012-08-24 02:58:39'),('customer:junior','kNauSyl8pgfIiNYLmBtKzg==','200/Uohqk1K8lSMu7hcoR9w3Ow==','2012-08-24 00:28:06'),('customer:jib','mrIEQCKfm+7x/gF9MK5qNQ==','200/bV5C4qn03pTOtku7RqmeCA==','2012-08-23 20:48:09'),('customer:jib','mu7GbmpbF1F7pLvWLNhLDA==','200/NOHDF7ENqj8bcRN/WPUAUg==','2012-08-24 03:48:09'),('customer:yuwu','MVtqtrSf+HeW97RSG7TNFg==','200/fmRYw68on1cUVFOVqKpYOg==','2012-08-23 21:55:00'),('customer:jib','noGjunm4bTPh4nq7zRxbBA==','200/HThHqkQVRqwah9TDyuiqJw==','2012-08-23 23:15:17'),('customer:jib','oGsUPnYd41YqtaOUVXXCHA==','200/EgTvCSDmiPeqhvD0tc/QoQ==','2012-08-24 01:10:31'),('customer:yuwu','P0qvVxIuwA3Z65TsQ8ocXQ==','200/PheEhq512TvbQXLFFDMmfw==','2012-08-23 20:10:46'),('customer:junior','p0S7zDF5XH2dNMF/Ss9JdQ==','200/xKG1M4iYvwB24Gox0t9+lw==','2012-08-23 20:53:21'),('customer:Josh:200','p6kIWl3ArFYxS00mLu5QJg==','cYaxYD6f1PavBTPSke4vNw==','2012-08-24 13:20:17'),('customer:junior','p9gKd0J7T4GIeQ0h1sv24w==','200/zMX3xM8dAu5JPWj0HyNVvA==','2012-08-23 20:39:00'),('customer:jib','PpqErXgfBv+Yl+uhAzC/rQ==','200/0ZfRx7CwqaJGejPZ3QDm9A==','2012-08-24 04:28:58'),('customer:jib','PYaEtGeiYpUOE9oZXPCOig==','200/5wG3BsWj9zGAvmJdDytaXA==','2012-08-23 17:33:33'),('customer:jib','QBdfRp2UBsw7LPMzMM3ulQ==','200/ZZDaiCvYrWwLksRIL0qlqA==','2012-08-24 03:11:49'),('customer:jib','RlNI9RQ9vs0EDonE0gMsaw==','200/foT38D/rOdh/p+oiO2aFkg==','2012-08-23 04:17:21'),('customer:daniel','tymdvfyVIIQ5bP1G/O6ufA==','200/YxzfZItyU1XmKv4VN49NtQ==','2012-08-23 04:17:38'),('merchant:ashish','vdWlrU2jLYrVsmqfSflDEg==','200/PvLqwz3rhbyyWta9tzHjJw==','2012-08-23 21:53:40'),('merchant:ashish','VS2s11KcPMkMIa1xkYsbOQ==','200/oQVf3KvBcKrZzKv40USYhA==','2012-08-24 02:54:21'),('customer:new','vS6472NAqS8m1yjUu6nPgA==','200/9YMdJVGuKGvTbkYhIBqE5w==','2012-08-23 17:33:38'),('merchant:ashish','w5MmNrGiZhETs70dKfyEZA==','200/hOrkwwHrX1s9PLz6aJmPFw==','2012-08-23 22:04:00'),('merchant:tt','WYSegBHwfcf6rxmilhx64w==','200/9UImvN0A/hK7xDXQ/zw7Fg==','2012-08-24 03:45:10'),('customer:jib','ytX8/fuip2bQV5s+MgCe6A==','200/iZiYH/5y0ISLabGQTTpFcw==','2012-08-23 21:53:14'),('customer:vinayak','Z8FauiD5SYtBLLe1LP1c7A==','200/nj/4DYcVrpKYqGfFoCpAPQ==','2012-08-23 04:18:40');
/*!40000 ALTER TABLE `persistent_logins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `merchantlogin`
--

LOCK TABLES `merchantlogin` WRITE;
/*!40000 ALTER TABLE `merchantlogin` DISABLE KEYS */;
INSERT INTO `merchantlogin` VALUES ('200','ashish','bnym','1'),('200','jeff@cmu.edu','bnym','1'),('200','tt','tt','1'),('300','sophie@cmu.edu','bnym','1');
/*!40000 ALTER TABLE `merchantlogin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `card`
--

LOCK TABLES `card` WRITE;
/*!40000 ALTER TABLE `card` DISABLE KEYS */;
INSERT INTO `card` VALUES ('200','109','6389',0,0.00,'0','2012-08-20 07:42:33','1',NULL),('200','1111','1111',0,492.75,'1','2012-08-24 02:49:26','1','1'),('200','1112','4292',0,500.00,'1','2012-08-20 20:18:07','1','5'),('200','1409','582',0,0.00,'0','2012-08-20 07:42:34','1',NULL),('200','1421','7162',0,0.00,'0','2012-08-20 07:42:30','1',NULL),('200','1447','4491',0,0.00,'0','2012-08-20 07:42:31','1',NULL),('200','1547','6248',0,0.00,'0','2012-08-20 07:42:30','1',NULL),('200','1619','8929',0,0.00,'0','2012-08-20 07:42:34','1',NULL),('200','1673','4595',0,0.00,'0','2012-08-20 07:42:27','1',NULL),('200','1982','9266',0,0.00,'0','2012-08-20 07:42:25','1',NULL),('200','2222','2222',0,0.00,'1','2012-08-20 20:14:30','1','2'),('200','2272','1385',0,0.00,'0','2012-08-20 07:42:33','1',NULL),('200','2349','2607',0,0.00,'0','2012-08-20 07:42:29','1',NULL),('200','2508','5030',0,0.00,'0','2012-08-20 07:42:28','1',NULL),('200','2596','6649',0,0.00,'0','2012-08-20 07:42:29','1',NULL),('200','284','9432',0,0.00,'0','2012-08-20 07:42:30','1',NULL),('200','3122','9185',0,0.00,'0','2012-08-20 07:42:32','1',NULL),('200','3333','3333',0,0.00,'0','2012-08-20 00:00:00','1',NULL),('200','345','7318',0,0.00,'0','2012-08-20 07:42:28','1',NULL),('200','382','4111',0,0.00,'0','2012-08-20 07:42:25','1',NULL),('200','4358','8551',0,0.00,'0','2012-08-20 07:42:32','1',NULL),('200','463','9918',0,0.00,'0','2012-08-20 07:42:27','1',NULL),('200','4751','5475',0,0.00,'0','2012-08-20 07:42:32','1',NULL),('200','4785','3326',0,0.00,'1','2012-08-23 02:06:50','1','4'),('200','4cf6o9go','1q3qf12e',0,335.75,'0','2012-08-24 13:20:05','1','5'),('200','5379','8583',0,0.00,'0','2012-08-20 07:42:28','1',NULL),('200','5398','2807',0,0.00,'0','2012-08-20 07:42:26','1',NULL),('200','5455','7330',0,0.00,'0','2012-08-20 07:42:29','1',NULL),('200','5558','5837',0,0.00,'0','2012-08-20 07:42:28','1',NULL),('200','5986','2111',0,0.00,'0','2012-08-20 07:42:29','1',NULL),('200','6158','2771',0,0.00,'0','2012-08-20 07:42:27','1',NULL),('200','6307','3239',0,0.00,'0','2012-08-20 07:42:30','1',NULL),('200','6365','215',0,0.00,'0','2012-08-20 07:42:24','1',NULL),('200','6516','6867',0,0.00,'0','2012-08-20 07:42:33','1',NULL),('200','6559','5238',0,0.00,'0','2012-08-20 07:42:33','1',NULL),('200','6598','4248',0,0.00,'0','2012-08-20 07:42:31','1',NULL),('200','6777','8135',0,0.00,'0','2012-08-20 07:42:28','1',NULL),('200','6823','4070',0,0.00,'0','2012-08-20 07:42:22','1',NULL),('200','7273','6648',0,0.00,'0','2012-08-20 07:42:30','1',NULL),('200','7627','3046',0,0.00,'0','2012-08-20 07:42:29','1',NULL),('200','77mi55sc','35gfic87',0,19.50,'1','2012-08-23 04:18:12','1','7'),('200','7844','799',0,0.00,'0','2012-08-20 07:42:26','1',NULL),('200','7958','6007',0,0.00,'0','2012-08-20 07:42:27','1',NULL),('200','8113','7090',0,0.00,'0','2012-08-20 07:42:31','1',NULL),('200','8125','7747',0,0.00,'0','2012-08-20 07:42:31','1',NULL),('200','8202','1256',0,0.00,'0','2012-08-20 07:42:27','1',NULL),('200','9412','4727',0,0.00,'0','2012-08-20 07:42:26','1',NULL),('200','9683','2760',0,0.00,'0','2012-08-20 07:42:32','1',NULL),('200','9789','2155',0,0.00,'0','2012-08-20 07:42:34','1',NULL),('200','9883','7203',0,0.00,'0','2012-08-20 07:42:23','1',NULL),('200','gtuupt4s','6jt0inq4',0,23.00,'1','2012-08-23 04:27:34','1','8'),('200','hheibdoi','t3ecoan7',0,900.00,'0','2012-08-23 17:34:27','1','4'),('200','iil2a23v','1adgh4i1',0,203.25,'1','2012-08-24 04:28:52','1','6'),('200','sm6gmd1u','4evhoje6',0,267.50,'1','2012-08-23 20:51:53','1','3'),('300','2217','4027',0,0.00,'1','2012-08-20 07:43:25','1','2'),('300','2269','6354',0,0.00,'1','2012-08-20 07:43:20','1','1'),('300','2880','7092',0,12.00,'0','2012-08-20 07:43:24','1','5'),('300','2908','3556',0,0.00,'0','2012-08-20 07:43:23','1',NULL),('300','3484','5339',0,0.00,'0','2012-08-20 07:43:25','1',NULL),('300','3631','9704',0,0.00,'0','2012-08-20 07:43:24','1',NULL),('300','3936','2378',0,0.00,'0','2012-08-20 07:43:21','1',NULL),('300','4426','11',0,0.00,'0','2012-08-20 07:43:21','1',NULL),('300','4966','5767',0,0.00,'0','2012-08-20 07:43:20','1',NULL),('300','5205','6594',0,0.00,'0','2012-08-20 07:43:23','1',NULL),('300','5855','5467',0,0.00,'0','2012-08-20 07:43:24','1',NULL),('300','6173','1011',0,0.00,'0','2012-08-20 07:43:21','1',NULL),('300','6534','9639',0,0.00,'0','2012-08-20 07:43:21','1',NULL),('300','6643','7424',0,0.00,'0','2012-08-20 07:43:22','1',NULL),('300','6778','3855',0,0.00,'0','2012-08-20 07:43:22','1',NULL),('300','6819','2817',0,0.00,'0','2012-08-20 07:43:24','1',NULL),('300','6838','3144',0,0.00,'0','2012-08-20 07:43:23','1',NULL),('300','7191','3682',0,0.00,'0','2012-08-20 07:43:23','1',NULL),('300','7355','6995',0,0.00,'0','2012-08-20 07:43:23','1',NULL),('300','7626','9019',0,0.00,'0','2012-08-20 07:43:25','1',NULL),('300','84','3285',0,0.00,'0','2012-08-20 07:43:21','1',NULL),('300','8591','4040',0,0.00,'0','2012-08-20 07:43:21','1',NULL),('300','8682','1663',0,0.00,'0','2012-08-20 07:43:09','1',NULL),('300','8780','8597',0,0.00,'0','2012-08-20 07:43:22','1',NULL),('300','8909','5101',0,0.00,'0','2012-08-20 07:43:22','1',NULL),('300','8943','3147',0,0.00,'0','2012-08-20 07:43:22','1',NULL),('300','9054','4604',0,0.00,'0','2012-08-20 07:43:23','1',NULL),('300','9767','2436',0,0.00,'0','2012-08-20 07:43:24','1',NULL);
/*!40000 ALTER TABLE `card` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
