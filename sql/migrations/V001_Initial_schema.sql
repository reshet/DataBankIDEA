-- phpMyAdmin SQL Dump
-- version 3.5.8.1deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Oct 27, 2014 at 06:13 PM
-- Server version: 5.5.29-0ubuntu1-log
-- PHP Version: 5.4.9-4ubuntu2.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `databankkiis`
--

-- --------------------------------------------------------

--
-- Table structure for table `ACCOUNT`
--

CREATE TABLE IF NOT EXISTS `ACCOUNT` (
  `ID` bigint(20) NOT NULL,
  `PASSWORD` varchar(255) DEFAULT NULL,
  `RIGHTS` varchar(255) DEFAULT NULL,
  `USERNAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `CONSULTATION`
--

CREATE TABLE IF NOT EXISTS `CONSULTATION` (
  `ID` bigint(20) NOT NULL,
  `ANSWER` text,
  `DATE_ANS` datetime DEFAULT NULL,
  `DATE_ASK` datetime DEFAULT NULL,
  `PUBLISHED` int(11) DEFAULT NULL,
  `QUESTION` text,
  `ENTITY_ITEM_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_CONSULTATION_ENTITY_ITEM_ID` (`ENTITY_ITEM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `DATABANKSTARTPAGE`
--

CREATE TABLE IF NOT EXISTS `DATABANKSTARTPAGE` (
  `ID` bigint(20) NOT NULL,
  `PUBS_LAST_SHOW` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `DATABANKSTARTPAGE_SOCIORESEARCH`
--

CREATE TABLE IF NOT EXISTS `DATABANKSTARTPAGE_SOCIORESEARCH` (
  `DatabankStartPage_ID` bigint(20) NOT NULL,
  `res_ID` bigint(20) NOT NULL,
  `res_ORDER` int(11) DEFAULT NULL,
  PRIMARY KEY (`DatabankStartPage_ID`,`res_ID`),
  KEY `FK_DATABANKSTARTPAGE_SOCIORESEARCH_res_ID` (`res_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `DATABANKSTARTPAGE_ZACON`
--

CREATE TABLE IF NOT EXISTS `DATABANKSTARTPAGE_ZACON` (
  `DatabankStartPage_ID` bigint(20) NOT NULL,
  `laws_ID` bigint(20) NOT NULL,
  `laws_ORDER` int(11) DEFAULT NULL,
  PRIMARY KEY (`DatabankStartPage_ID`,`laws_ID`),
  KEY `FK_DATABANKSTARTPAGE_ZACON_laws_ID` (`laws_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `DATABANKSTRUCTURE`
--

CREATE TABLE IF NOT EXISTS `DATABANKSTRUCTURE` (
  `META_IDENTITY` varchar(255) NOT NULL,
  `ROOT_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`META_IDENTITY`),
  KEY `FK_DATABANKSTRUCTURE_ROOT_ID` (`ROOT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `DATABANKSTRUCTURE_METAUNITENTITYITEM`
--

CREATE TABLE IF NOT EXISTS `DATABANKSTRUCTURE_METAUNITENTITYITEM` (
  `DatabankStructure_META_IDENTITY` varchar(255) NOT NULL,
  `items_ID` bigint(20) NOT NULL,
  `ITEMS_KEY` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`DatabankStructure_META_IDENTITY`,`items_ID`),
  KEY `FK_DATABANKSTRUCTURE_METAUNITENTITYITEM_items_ID` (`items_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ENTITYSEARCHREPRESENTER`
--

CREATE TABLE IF NOT EXISTS `ENTITYSEARCHREPRESENTER` (
  `ID` bigint(20) NOT NULL,
  `ENTITYTYPE` varchar(255) DEFAULT NULL,
  `ENTITY_ID` bigint(20) DEFAULT NULL,
  `TAGS` longblob,
  `TAGS_IDENTITIES` longblob,
  `TEXT_REPRESENT` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `METAUNIT`
--

CREATE TABLE IF NOT EXISTS `METAUNIT` (
  `ID` bigint(20) NOT NULL,
  `PROJ_TYPE` varchar(31) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `UNIQUE_NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `METAUNITDATE`
--

CREATE TABLE IF NOT EXISTS `METAUNITDATE` (
  `ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `METAUNITDOUBLE`
--

CREATE TABLE IF NOT EXISTS `METAUNITDOUBLE` (
  `ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `METAUNITENTITYITEM`
--

CREATE TABLE IF NOT EXISTS `METAUNITENTITYITEM` (
  `ID` bigint(20) NOT NULL,
  `ID_METAUNITENTITY` bigint(20) DEFAULT NULL,
  `MAPPED_VALUES` longblob,
  `TAGGED_ENTITIES_IDENTIFIERS` longblob,
  `TAGGED_ENTITIES_IDS` longblob,
  `V_VALUE` varchar(255) DEFAULT NULL,
  `PARENT_ITEM_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_METAUNITENTITYITEM_PARENT_ITEM_ID` (`PARENT_ITEM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `METAUNITFILE`
--

CREATE TABLE IF NOT EXISTS `METAUNITFILE` (
  `ID` bigint(20) NOT NULL,
  `FILE_ID` bigint(20) DEFAULT NULL,
  `FILE_NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `METAUNITINTEGER`
--

CREATE TABLE IF NOT EXISTS `METAUNITINTEGER` (
  `ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `METAUNITMULTIVALUED`
--

CREATE TABLE IF NOT EXISTS `METAUNITMULTIVALUED` (
  `ID` bigint(20) NOT NULL,
  `ISCATALOGIZABLE` bigint(20) DEFAULT NULL,
  `ISSPLITTINGENABLED` bigint(20) DEFAULT NULL,
  `TAGGED_ENTITIES` longblob,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `METAUNITMULTIVALUEDENTITY`
--

CREATE TABLE IF NOT EXISTS `METAUNITMULTIVALUEDENTITY` (
  `ID` bigint(20) NOT NULL,
  `ISMULTISELECTED` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `METAUNITMULTIVALUEDENTITY_METAUNITENTITYITEM`
--

CREATE TABLE IF NOT EXISTS `METAUNITMULTIVALUEDENTITY_METAUNITENTITYITEM` (
  `MetaUnitMultivaluedEntity_ID` bigint(20) NOT NULL,
  `items_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`MetaUnitMultivaluedEntity_ID`,`items_ID`),
  KEY `METAUNITMULTIVALUEDENTITYMETAUNITENTITYITEMitemsID` (`items_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `METAUNITMULTIVALUEDSTRUCTURE`
--

CREATE TABLE IF NOT EXISTS `METAUNITMULTIVALUEDSTRUCTURE` (
  `ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `METAUNITMULTIVALUED_METAUNIT`
--

CREATE TABLE IF NOT EXISTS `METAUNITMULTIVALUED_METAUNIT` (
  `MetaUnitMultivalued_ID` bigint(20) NOT NULL,
  `sub_meta_units_ID` bigint(20) NOT NULL,
  `sub_meta_units_ORDER` int(11) DEFAULT NULL,
  PRIMARY KEY (`MetaUnitMultivalued_ID`,`sub_meta_units_ID`),
  KEY `FK_METAUNITMULTIVALUED_METAUNIT_sub_meta_units_ID` (`sub_meta_units_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `METAUNITSTRING`
--

CREATE TABLE IF NOT EXISTS `METAUNITSTRING` (
  `ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `METAUNITVALUE`
--

CREATE TABLE IF NOT EXISTS `METAUNITVALUE` (
  `ID` bigint(20) NOT NULL,
  `ID_METAUNIT` bigint(20) DEFAULT NULL,
  `V_VALUE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ORGANIZATION`
--

CREATE TABLE IF NOT EXISTS `ORGANIZATION` (
  `ID` bigint(20) NOT NULL,
  `ADDRESS` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `TELEPHONE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `PUBLICATION`
--

CREATE TABLE IF NOT EXISTS `PUBLICATION` (
  `ID` bigint(20) NOT NULL,
  `CONTENTS` text,
  `DATE_PUBL` datetime DEFAULT NULL,
  `ENCLOSURE_KEY` bigint(20) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `SUBHEADING` varchar(255) DEFAULT NULL,
  `ENTITY_ITEM_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_PUBLICATION_ENTITY_ITEM_ID` (`ENTITY_ITEM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `RESEARCHFILESACCESSOR`
--

CREATE TABLE IF NOT EXISTS `RESEARCHFILESACCESSOR` (
  `ID` bigint(20) NOT NULL,
  `FILE_CATEGS` longblob,
  `FILE_IDS` longblob,
  `FILE_NAMES` longblob,
  `REQUESTACCESSEMAIL` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `RXBLOBSTORED`
--

CREATE TABLE IF NOT EXISTS `RXBLOBSTORED` (
  `ID` bigint(20) NOT NULL,
  `CONTENTS` longblob,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `FILENAME` varchar(255) DEFAULT NULL,
  `FILESIZE` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `SEQUENCE`
--

CREATE TABLE IF NOT EXISTS `SEQUENCE` (
  `SEQ_NAME` varchar(50) NOT NULL,
  `SEQ_COUNT` decimal(38,0) DEFAULT NULL,
  PRIMARY KEY (`SEQ_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `SOCIORESEARCH`
--

CREATE TABLE IF NOT EXISTS `SOCIORESEARCH` (
  `ID` bigint(20) NOT NULL,
  `DESCRIPTION_TEXT` text,
  `FILE_ACCESSOR_ID` bigint(20) DEFAULT NULL,
  `ID_SEARCH_REPRES` bigint(20) DEFAULT NULL,
  `JSON_DESCTIPTOR` text,
  `NAME` varchar(255) DEFAULT NULL,
  `SELECTION_SIZE` int(11) DEFAULT NULL,
  `SPSSFILE_BLOBKEY` bigint(20) DEFAULT NULL,
  `ENTITY_ITEM_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_SOCIORESEARCH_ENTITY_ITEM_ID` (`ENTITY_ITEM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `SocioResearch_FILES_DESCS`
--

CREATE TABLE IF NOT EXISTS `SocioResearch_FILES_DESCS` (
  `SocioResearch_ID` bigint(20) DEFAULT NULL,
  `FILES_DESCS` varchar(255) DEFAULT NULL,
  KEY `FK_SocioResearch_FILES_DESCS_SocioResearch_ID` (`SocioResearch_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `SocioResearch_FILES_IDS`
--

CREATE TABLE IF NOT EXISTS `SocioResearch_FILES_IDS` (
  `SocioResearch_ID` bigint(20) DEFAULT NULL,
  `FILES_IDS` varchar(255) DEFAULT NULL,
  KEY `FK_SocioResearch_FILES_IDS_SocioResearch_ID` (`SocioResearch_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `SocioResearch_VAR_IDS`
--

CREATE TABLE IF NOT EXISTS `SocioResearch_VAR_IDS` (
  `SocioResearch_ID` bigint(20) DEFAULT NULL,
  `VAR_IDS` bigint(20) DEFAULT NULL,
  KEY `FK_SocioResearch_VAR_IDS_SocioResearch_ID` (`SocioResearch_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `SocioResearch_VAR_WEIGHT_IDS`
--

CREATE TABLE IF NOT EXISTS `SocioResearch_VAR_WEIGHT_IDS` (
  `SocioResearch_ID` bigint(20) DEFAULT NULL,
  `VAR_WEIGHT_IDS` bigint(20) DEFAULT NULL,
  KEY `FK_SocioResearch_VAR_WEIGHT_IDS_SocioResearch_ID` (`SocioResearch_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `SocioResearch_VAR_WEIGHT_NAMES`
--

CREATE TABLE IF NOT EXISTS `SocioResearch_VAR_WEIGHT_NAMES` (
  `SocioResearch_ID` bigint(20) DEFAULT NULL,
  `VAR_WEIGHT_NAMES` varchar(255) DEFAULT NULL,
  KEY `FK_SocioResearch_VAR_WEIGHT_NAMES_SocioResearch_ID` (`SocioResearch_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `USERACCOUNT`
--

CREATE TABLE IF NOT EXISTS `USERACCOUNT` (
  `ID` bigint(20) NOT NULL,
  `ACCOUNTTYPE` varchar(255) DEFAULT NULL,
  `EMAILADDRESS` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `PASSWORD` varchar(255) DEFAULT NULL,
  `ANALYSIS_ID` varchar(255) DEFAULT NULL,
  `HISTORY_ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_USERACCOUNT_HISTORY_ID` (`HISTORY_ID`),
  KEY `FK_USERACCOUNT_ANALYSIS_ID` (`ANALYSIS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `USERHISTORY`
--

CREATE TABLE IF NOT EXISTS `USERHISTORY` (
  `ID` varchar(255) NOT NULL,
  `FAVOURITE_MASSIVES` longblob,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `USERHISTORY_USERMASSIVEANALISYS`
--

CREATE TABLE IF NOT EXISTS `USERHISTORY_USERMASSIVEANALISYS` (
  `RegisteredUserAnalysisRoot_ID` varchar(255) NOT NULL,
  `local_research_analisys_ID` int(11) NOT NULL,
  `local_research_analisys_ORDER` int(11) DEFAULT NULL,
  PRIMARY KEY (`RegisteredUserAnalysisRoot_ID`,`local_research_analisys_ID`),
  KEY `SRHISTORYUSERMASSIVEANALISYSlcalresearchanalisysID` (`local_research_analisys_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `USERHISTORY_USERMASSIVESETTING`
--

CREATE TABLE IF NOT EXISTS `USERHISTORY_USERMASSIVESETTING` (
  `RegisteredUserHistory_ID` varchar(255) NOT NULL,
  `local_research_settings_ID` int(11) NOT NULL,
  `local_research_settings_ORDER` int(11) DEFAULT NULL,
  PRIMARY KEY (`RegisteredUserHistory_ID`,`local_research_settings_ID`),
  KEY `SERHISTORYUSERMASSIVESETTINGlcalresearchsettingsID` (`local_research_settings_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `USERMASSIVEANALISYS`
--

CREATE TABLE IF NOT EXISTS `USERMASSIVEANALISYS` (
  `ID` int(11) NOT NULL,
  `COMMENT` varchar(255) DEFAULT NULL,
  `DATE_SAVED` datetime DEFAULT NULL,
  `DISTR_TYPE` varchar(255) DEFAULT NULL,
  `DISTRIBUTION` longblob,
  `DISTRIBUTION_VALID` longblob,
  `NAME` varchar(255) DEFAULT NULL,
  `USER2DD_CHOICE` varchar(255) DEFAULT NULL,
  `VAR_INVOLVED_FIRST_ID` bigint(20) DEFAULT NULL,
  `VAR_INVOLVED_SECOND_ID` bigint(20) DEFAULT NULL,
  `SETTING_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_USERMASSIVEANALISYS_SETTING_ID` (`SETTING_ID`),
  KEY `FK_USERMASSIVEANALISYS_VAR_INVOLVED_SECOND_ID` (`VAR_INVOLVED_SECOND_ID`),
  KEY `FK_USERMASSIVEANALISYS_VAR_INVOLVED_FIRST_ID` (`VAR_INVOLVED_FIRST_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `USERMASSIVESETTING`
--

CREATE TABLE IF NOT EXISTS `USERMASSIVESETTING` (
  `ID` int(11) NOT NULL,
  `FILTERS` longblob,
  `FILTERS_USAGE` longblob,
  `FILTERS_USE` int(11) DEFAULT NULL,
  `RESEARCH_ID` bigint(20) DEFAULT NULL,
  `WEIGHTS_USAGE` longblob,
  `WEIGHTS_USE` int(11) DEFAULT NULL,
  `WEIGHTS_VAR_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `VAR`
--

CREATE TABLE IF NOT EXISTS `VAR` (
  `ID` bigint(20) NOT NULL,
  `CODE` varchar(255) DEFAULT NULL,
  `CODE_SCHEMA_ID` varchar(255) DEFAULT NULL,
  `LABEL` text,
  `MISSING1` varchar(255) DEFAULT NULL,
  `MISSING2` varchar(255) DEFAULT NULL,
  `MISSING3` varchar(255) DEFAULT NULL,
  `RESEARCH_ID` bigint(20) DEFAULT NULL,
  `VAR_TYPE` varchar(255) DEFAULT NULL,
  `ENTITY_ITEM_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_VAR_ENTITY_ITEM_ID` (`ENTITY_ITEM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Var_CORTAGE`
--

CREATE TABLE IF NOT EXISTS `Var_CORTAGE` (
  `Var_ID` bigint(20) DEFAULT NULL,
  `CORTAGE` double DEFAULT NULL,
  KEY `FK_Var_CORTAGE_Var_ID` (`Var_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Var_CORTAGE_STRING`
--

CREATE TABLE IF NOT EXISTS `Var_CORTAGE_STRING` (
  `Var_ID` bigint(20) DEFAULT NULL,
  `CORTAGE_STRING` varchar(255) DEFAULT NULL,
  KEY `FK_Var_CORTAGE_STRING_Var_ID` (`Var_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Var_FILES_IDS`
--

CREATE TABLE IF NOT EXISTS `Var_FILES_IDS` (
  `Var_ID` bigint(20) DEFAULT NULL,
  `FILES_IDS` varchar(255) DEFAULT NULL,
  KEY `FK_Var_FILES_IDS_Var_ID` (`Var_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Var_FILLING`
--

CREATE TABLE IF NOT EXISTS `Var_FILLING` (
  `Var_ID` bigint(20) DEFAULT NULL,
  `FILLING` varchar(255) DEFAULT NULL,
  `FILLING_KEY` varchar(255) DEFAULT NULL,
  KEY `FK_Var_FILLING_Var_ID` (`Var_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Var_GENERALIZED_VAR_IDS`
--

CREATE TABLE IF NOT EXISTS `Var_GENERALIZED_VAR_IDS` (
  `Var_ID` bigint(20) DEFAULT NULL,
  `GENERALIZED_VAR_IDS` bigint(20) DEFAULT NULL,
  KEY `FK_Var_GENERALIZED_VAR_IDS_Var_ID` (`Var_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Var_V_LABEL_CODES`
--

CREATE TABLE IF NOT EXISTS `Var_V_LABEL_CODES` (
  `Var_ID` bigint(20) DEFAULT NULL,
  `V_LABEL_CODES` double DEFAULT NULL,
  KEY `FK_Var_V_LABEL_CODES_Var_ID` (`Var_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Var_V_LABEL_VALUES`
--

CREATE TABLE IF NOT EXISTS `Var_V_LABEL_VALUES` (
  `Var_ID` bigint(20) DEFAULT NULL,
  `V_LABEL_VALUES` varchar(255) DEFAULT NULL,
  KEY `FK_Var_V_LABEL_VALUES_Var_ID` (`Var_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ZACON`
--

CREATE TABLE IF NOT EXISTS `ZACON` (
  `ID` bigint(20) NOT NULL,
  `CONTENTS` text,
  `FILE_ACCESSOR_ID` bigint(20) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `NUMBER` varchar(255) DEFAULT NULL,
  `ENTITY_ITEM_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_ZACON_ENTITY_ITEM_ID` (`ENTITY_ITEM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `CONSULTATION`
--
ALTER TABLE `CONSULTATION`
  ADD CONSTRAINT `FK_CONSULTATION_ENTITY_ITEM_ID` FOREIGN KEY (`ENTITY_ITEM_ID`) REFERENCES `METAUNITENTITYITEM` (`ID`);

--
-- Constraints for table `DATABANKSTARTPAGE_SOCIORESEARCH`
--
ALTER TABLE `DATABANKSTARTPAGE_SOCIORESEARCH`
  ADD CONSTRAINT `DATABANKSTARTPAGESOCIORESEARCHDatabankStartPage_ID` FOREIGN KEY (`DatabankStartPage_ID`) REFERENCES `DATABANKSTARTPAGE` (`ID`),
  ADD CONSTRAINT `FK_DATABANKSTARTPAGE_SOCIORESEARCH_res_ID` FOREIGN KEY (`res_ID`) REFERENCES `SOCIORESEARCH` (`ID`);

--
-- Constraints for table `DATABANKSTARTPAGE_ZACON`
--
ALTER TABLE `DATABANKSTARTPAGE_ZACON`
  ADD CONSTRAINT `FK_DATABANKSTARTPAGE_ZACON_DatabankStartPage_ID` FOREIGN KEY (`DatabankStartPage_ID`) REFERENCES `DATABANKSTARTPAGE` (`ID`),
  ADD CONSTRAINT `FK_DATABANKSTARTPAGE_ZACON_laws_ID` FOREIGN KEY (`laws_ID`) REFERENCES `ZACON` (`ID`);

--
-- Constraints for table `DATABANKSTRUCTURE`
--
ALTER TABLE `DATABANKSTRUCTURE`
  ADD CONSTRAINT `FK_DATABANKSTRUCTURE_ROOT_ID` FOREIGN KEY (`ROOT_ID`) REFERENCES `METAUNIT` (`ID`);

--
-- Constraints for table `DATABANKSTRUCTURE_METAUNITENTITYITEM`
--
ALTER TABLE `DATABANKSTRUCTURE_METAUNITENTITYITEM`
  ADD CONSTRAINT `DTBNKSTRCTRMTAUNITENTITYITEMDtbnkStrctrMTAIDENTITY` FOREIGN KEY (`DatabankStructure_META_IDENTITY`) REFERENCES `DATABANKSTRUCTURE` (`META_IDENTITY`),
  ADD CONSTRAINT `FK_DATABANKSTRUCTURE_METAUNITENTITYITEM_items_ID` FOREIGN KEY (`items_ID`) REFERENCES `METAUNITENTITYITEM` (`ID`);

--
-- Constraints for table `METAUNITDATE`
--
ALTER TABLE `METAUNITDATE`
  ADD CONSTRAINT `FK_METAUNITDATE_ID` FOREIGN KEY (`ID`) REFERENCES `METAUNIT` (`ID`);

--
-- Constraints for table `METAUNITDOUBLE`
--
ALTER TABLE `METAUNITDOUBLE`
  ADD CONSTRAINT `FK_METAUNITDOUBLE_ID` FOREIGN KEY (`ID`) REFERENCES `METAUNIT` (`ID`);

--
-- Constraints for table `METAUNITENTITYITEM`
--
ALTER TABLE `METAUNITENTITYITEM`
  ADD CONSTRAINT `FK_METAUNITENTITYITEM_PARENT_ITEM_ID` FOREIGN KEY (`PARENT_ITEM_ID`) REFERENCES `METAUNITENTITYITEM` (`ID`);

--
-- Constraints for table `METAUNITFILE`
--
ALTER TABLE `METAUNITFILE`
  ADD CONSTRAINT `FK_METAUNITFILE_ID` FOREIGN KEY (`ID`) REFERENCES `METAUNIT` (`ID`);

--
-- Constraints for table `METAUNITINTEGER`
--
ALTER TABLE `METAUNITINTEGER`
  ADD CONSTRAINT `FK_METAUNITINTEGER_ID` FOREIGN KEY (`ID`) REFERENCES `METAUNIT` (`ID`);

--
-- Constraints for table `METAUNITMULTIVALUED`
--
ALTER TABLE `METAUNITMULTIVALUED`
  ADD CONSTRAINT `FK_METAUNITMULTIVALUED_ID` FOREIGN KEY (`ID`) REFERENCES `METAUNIT` (`ID`);

--
-- Constraints for table `METAUNITMULTIVALUEDENTITY`
--
ALTER TABLE `METAUNITMULTIVALUEDENTITY`
  ADD CONSTRAINT `FK_METAUNITMULTIVALUEDENTITY_ID` FOREIGN KEY (`ID`) REFERENCES `METAUNIT` (`ID`);

--
-- Constraints for table `METAUNITMULTIVALUEDENTITY_METAUNITENTITYITEM`
--
ALTER TABLE `METAUNITMULTIVALUEDENTITY_METAUNITENTITYITEM`
  ADD CONSTRAINT `METAUNITMULTIVALUEDENTITYMETAUNITENTITYITEMitemsID` FOREIGN KEY (`items_ID`) REFERENCES `METAUNITENTITYITEM` (`ID`),
  ADD CONSTRAINT `MTNTMLTVLDNTITYMETAUNITENTITYITEMMtntMltvldntityID` FOREIGN KEY (`MetaUnitMultivaluedEntity_ID`) REFERENCES `METAUNIT` (`ID`);

--
-- Constraints for table `METAUNITMULTIVALUEDSTRUCTURE`
--
ALTER TABLE `METAUNITMULTIVALUEDSTRUCTURE`
  ADD CONSTRAINT `FK_METAUNITMULTIVALUEDSTRUCTURE_ID` FOREIGN KEY (`ID`) REFERENCES `METAUNIT` (`ID`);

--
-- Constraints for table `METAUNITMULTIVALUED_METAUNIT`
--
ALTER TABLE `METAUNITMULTIVALUED_METAUNIT`
  ADD CONSTRAINT `FK_METAUNITMULTIVALUED_METAUNIT_sub_meta_units_ID` FOREIGN KEY (`sub_meta_units_ID`) REFERENCES `METAUNIT` (`ID`),
  ADD CONSTRAINT `METAUNITMULTIVALUED_METAUNITMetaUnitMultivalued_ID` FOREIGN KEY (`MetaUnitMultivalued_ID`) REFERENCES `METAUNIT` (`ID`);

--
-- Constraints for table `METAUNITSTRING`
--
ALTER TABLE `METAUNITSTRING`
  ADD CONSTRAINT `FK_METAUNITSTRING_ID` FOREIGN KEY (`ID`) REFERENCES `METAUNIT` (`ID`);

--
-- Constraints for table `PUBLICATION`
--
ALTER TABLE `PUBLICATION`
  ADD CONSTRAINT `FK_PUBLICATION_ENTITY_ITEM_ID` FOREIGN KEY (`ENTITY_ITEM_ID`) REFERENCES `METAUNITENTITYITEM` (`ID`);

--
-- Constraints for table `SOCIORESEARCH`
--
ALTER TABLE `SOCIORESEARCH`
  ADD CONSTRAINT `FK_SOCIORESEARCH_ENTITY_ITEM_ID` FOREIGN KEY (`ENTITY_ITEM_ID`) REFERENCES `METAUNITENTITYITEM` (`ID`);

--
-- Constraints for table `SocioResearch_FILES_DESCS`
--
ALTER TABLE `SocioResearch_FILES_DESCS`
  ADD CONSTRAINT `FK_SocioResearch_FILES_DESCS_SocioResearch_ID` FOREIGN KEY (`SocioResearch_ID`) REFERENCES `SOCIORESEARCH` (`ID`) ON DELETE CASCADE,
  ADD CONSTRAINT `SocioResearch_FILES_DESCS_ibfk_1` FOREIGN KEY (`SocioResearch_ID`) REFERENCES `SOCIORESEARCH` (`ID`) ON DELETE CASCADE;

--
-- Constraints for table `SocioResearch_FILES_IDS`
--
ALTER TABLE `SocioResearch_FILES_IDS`
  ADD CONSTRAINT `FK_SocioResearch_FILES_IDS_SocioResearch_ID` FOREIGN KEY (`SocioResearch_ID`) REFERENCES `SOCIORESEARCH` (`ID`) ON DELETE CASCADE,
  ADD CONSTRAINT `SocioResearch_FILES_IDS_ibfk_1` FOREIGN KEY (`SocioResearch_ID`) REFERENCES `SOCIORESEARCH` (`ID`) ON DELETE CASCADE;

--
-- Constraints for table `SocioResearch_VAR_IDS`
--
ALTER TABLE `SocioResearch_VAR_IDS`
  ADD CONSTRAINT `FK_SocioResearch_VAR_IDS_SocioResearch_ID` FOREIGN KEY (`SocioResearch_ID`) REFERENCES `SOCIORESEARCH` (`ID`) ON DELETE CASCADE,
  ADD CONSTRAINT `SocioResearch_VAR_IDS_ibfk_1` FOREIGN KEY (`SocioResearch_ID`) REFERENCES `SOCIORESEARCH` (`ID`) ON DELETE CASCADE;

--
-- Constraints for table `SocioResearch_VAR_WEIGHT_IDS`
--
ALTER TABLE `SocioResearch_VAR_WEIGHT_IDS`
  ADD CONSTRAINT `FK_SocioResearch_VAR_WEIGHT_IDS_SocioResearch_ID` FOREIGN KEY (`SocioResearch_ID`) REFERENCES `SOCIORESEARCH` (`ID`) ON DELETE CASCADE,
  ADD CONSTRAINT `SocioResearch_VAR_WEIGHT_IDS_ibfk_1` FOREIGN KEY (`SocioResearch_ID`) REFERENCES `SOCIORESEARCH` (`ID`) ON DELETE CASCADE;

--
-- Constraints for table `SocioResearch_VAR_WEIGHT_NAMES`
--
ALTER TABLE `SocioResearch_VAR_WEIGHT_NAMES`
  ADD CONSTRAINT `FK_SocioResearch_VAR_WEIGHT_NAMES_SocioResearch_ID` FOREIGN KEY (`SocioResearch_ID`) REFERENCES `SOCIORESEARCH` (`ID`) ON DELETE CASCADE,
  ADD CONSTRAINT `SocioResearch_VAR_WEIGHT_NAMES_ibfk_1` FOREIGN KEY (`SocioResearch_ID`) REFERENCES `SOCIORESEARCH` (`ID`) ON DELETE CASCADE;

--
-- Constraints for table `USERACCOUNT`
--
ALTER TABLE `USERACCOUNT`
  ADD CONSTRAINT `FK_USERACCOUNT_ANALYSIS_ID` FOREIGN KEY (`ANALYSIS_ID`) REFERENCES `USERHISTORY` (`ID`),
  ADD CONSTRAINT `FK_USERACCOUNT_HISTORY_ID` FOREIGN KEY (`HISTORY_ID`) REFERENCES `USERHISTORY` (`ID`);

--
-- Constraints for table `USERHISTORY_USERMASSIVEANALISYS`
--
ALTER TABLE `USERHISTORY_USERMASSIVEANALISYS`
  ADD CONSTRAINT `SRHISTORYUSERMASSIVEANALISYSlcalresearchanalisysID` FOREIGN KEY (`local_research_analisys_ID`) REFERENCES `USERMASSIVEANALISYS` (`ID`),
  ADD CONSTRAINT `SRHSTRYUSERMASSIVEANALISYSRgstrdUserAnalysisRootID` FOREIGN KEY (`RegisteredUserAnalysisRoot_ID`) REFERENCES `USERHISTORY` (`ID`),
  ADD CONSTRAINT `USERHISTORY_USERMASSIVEANALISYS_ibfk_1` FOREIGN KEY (`RegisteredUserAnalysisRoot_ID`) REFERENCES `USERHISTORY` (`ID`) ON DELETE CASCADE,
  ADD CONSTRAINT `USERHISTORY_USERMASSIVEANALISYS_ibfk_2` FOREIGN KEY (`local_research_analisys_ID`) REFERENCES `USERMASSIVEANALISYS` (`ID`) ON DELETE CASCADE;

--
-- Constraints for table `USERHISTORY_USERMASSIVESETTING`
--
ALTER TABLE `USERHISTORY_USERMASSIVESETTING`
  ADD CONSTRAINT `SERHISTORYUSERMASSIVESETTINGlcalresearchsettingsID` FOREIGN KEY (`local_research_settings_ID`) REFERENCES `USERMASSIVESETTING` (`ID`),
  ADD CONSTRAINT `SERHISTORYUSERMASSIVESETTINGRgisteredUserHistoryID` FOREIGN KEY (`RegisteredUserHistory_ID`) REFERENCES `USERHISTORY` (`ID`),
  ADD CONSTRAINT `USERHISTORY_USERMASSIVESETTING_ibfk_1` FOREIGN KEY (`RegisteredUserHistory_ID`) REFERENCES `USERHISTORY` (`ID`) ON DELETE CASCADE,
  ADD CONSTRAINT `USERHISTORY_USERMASSIVESETTING_ibfk_2` FOREIGN KEY (`local_research_settings_ID`) REFERENCES `USERMASSIVESETTING` (`ID`) ON DELETE CASCADE;

-- --
-- -- Constraints for table `USERMASSIVEANALISYS`
-- --
-- ALTER TABLE `USERMASSIVEANALISYS`
--   ADD CONSTRAINT `FK_USERMASSIVEANALISYS_VAR_INVOLVED_FIRST_ID` FOREIGN KEY (`VAR_INVOLVED_FIRST_ID`) REFERENCES `VAR` (`ID`),
--   ADD CONSTRAINT `FK_USERMASSIVEANALISYS_SETTING_ID` FOREIGN KEY (`SETTING_ID`) REFERENCES `USERMASSIVESETTING` (`ID`),
--   ADD CONSTRAINT `FK_USERMASSIVEANALISYS_VAR_INVOLVED_SECOND_ID` FOREIGN KEY (`VAR_INVOLVED_SECOND_ID`) REFERENCES `VAR` (`ID`),
--   ADD CONSTRAINT `USERMASSIVEANALISYS_ibfk_4` FOREIGN KEY (`VAR_INVOLVED_FIRST_ID`) REFERENCES `VAR` (`ID`) ON DELETE SET NULL ON UPDATE NO ACTION,
--   ADD CONSTRAINT `USERMASSIVEANALISYS_ibfk_5` FOREIGN KEY (`VAR_INVOLVED_SECOND_ID`) REFERENCES `VAR` (`ID`) ON DELETE SET NULL ON UPDATE NO ACTION,
--   ADD CONSTRAINT `USERMASSIVEANALISYS_ibfk_6` FOREIGN KEY (`SETTING_ID`) REFERENCES `USERMASSIVESETTING` (`ID`) ON DELETE SET NULL ON UPDATE NO ACTION;

--
-- Constraints for table `VAR`
--
ALTER TABLE `VAR`
  ADD CONSTRAINT `FK_VAR_ENTITY_ITEM_ID` FOREIGN KEY (`ENTITY_ITEM_ID`) REFERENCES `METAUNITENTITYITEM` (`ID`),
  ADD CONSTRAINT `VAR_ibfk_2` FOREIGN KEY (`ENTITY_ITEM_ID`) REFERENCES `METAUNITENTITYITEM` (`ID`) ON DELETE NO ACTION;

--
-- Constraints for table `Var_CORTAGE`
--
ALTER TABLE `Var_CORTAGE`
  ADD CONSTRAINT `FK_Var_CORTAGE_Var_ID` FOREIGN KEY (`Var_ID`) REFERENCES `VAR` (`ID`) ON DELETE CASCADE,
  ADD CONSTRAINT `Var_CORTAGE_ibfk_1` FOREIGN KEY (`Var_ID`) REFERENCES `VAR` (`ID`) ON DELETE CASCADE;

--
-- Constraints for table `Var_CORTAGE_STRING`
--
ALTER TABLE `Var_CORTAGE_STRING`
  ADD CONSTRAINT `FK_Var_CORTAGE_STRING_Var_ID` FOREIGN KEY (`Var_ID`) REFERENCES `VAR` (`ID`) ON DELETE CASCADE,
  ADD CONSTRAINT `Var_CORTAGE_STRING_ibfk_1` FOREIGN KEY (`Var_ID`) REFERENCES `VAR` (`ID`) ON DELETE CASCADE;

--
-- Constraints for table `Var_FILES_IDS`
--
ALTER TABLE `Var_FILES_IDS`
  ADD CONSTRAINT `FK_Var_FILES_IDS_Var_ID` FOREIGN KEY (`Var_ID`) REFERENCES `VAR` (`ID`) ON DELETE CASCADE;

--
-- Constraints for table `Var_FILLING`
--
ALTER TABLE `Var_FILLING`
  ADD CONSTRAINT `FK_Var_FILLING_Var_ID` FOREIGN KEY (`Var_ID`) REFERENCES `VAR` (`ID`) ON DELETE CASCADE,
  ADD CONSTRAINT `Var_FILLING_ibfk_1` FOREIGN KEY (`Var_ID`) REFERENCES `VAR` (`ID`) ON DELETE CASCADE;

--
-- Constraints for table `Var_GENERALIZED_VAR_IDS`
--
ALTER TABLE `Var_GENERALIZED_VAR_IDS`
  ADD CONSTRAINT `FK_Var_GENERALIZED_VAR_IDS_Var_ID` FOREIGN KEY (`Var_ID`) REFERENCES `VAR` (`ID`) ON DELETE CASCADE,
  ADD CONSTRAINT `Var_GENERALIZED_VAR_IDS_ibfk_1` FOREIGN KEY (`Var_ID`) REFERENCES `VAR` (`ID`) ON UPDATE NO ACTION;

--
-- Constraints for table `Var_V_LABEL_CODES`
--
ALTER TABLE `Var_V_LABEL_CODES`
  ADD CONSTRAINT `FK_Var_V_LABEL_CODES_Var_ID` FOREIGN KEY (`Var_ID`) REFERENCES `VAR` (`ID`) ON DELETE CASCADE,
  ADD CONSTRAINT `Var_V_LABEL_CODES_ibfk_1` FOREIGN KEY (`Var_ID`) REFERENCES `VAR` (`ID`) ON DELETE CASCADE;

--
-- Constraints for table `Var_V_LABEL_VALUES`
--
ALTER TABLE `Var_V_LABEL_VALUES`
  ADD CONSTRAINT `FK_Var_V_LABEL_VALUES_Var_ID` FOREIGN KEY (`Var_ID`) REFERENCES `VAR` (`ID`) ON DELETE CASCADE,
  ADD CONSTRAINT `Var_V_LABEL_VALUES_ibfk_1` FOREIGN KEY (`Var_ID`) REFERENCES `VAR` (`ID`) ON DELETE CASCADE;

--
-- Constraints for table `ZACON`
--
ALTER TABLE `ZACON`
  ADD CONSTRAINT `FK_ZACON_ENTITY_ITEM_ID` FOREIGN KEY (`ENTITY_ITEM_ID`) REFERENCES `METAUNITENTITYITEM` (`ID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;