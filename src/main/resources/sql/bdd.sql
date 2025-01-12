--
-- Base de données : `dario_3`
--
CREATE DATABASE IF NOT EXISTS `dario_3` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `dario_3`;

-- --------------------------------------------------------

--
-- Structure de la table `gender`
--

DROP TABLE IF EXISTS `gender`;
CREATE TABLE IF NOT EXISTS `gender` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `gender`
--

INSERT INTO `gender` (`Id`, `Name`) VALUES
(1, 'Action'),
(2, 'Science-Fiction'),
(3, 'Romantique');

-- --------------------------------------------------------

--
-- Structure de la table `movie`
--

DROP TABLE IF EXISTS `movie`;
CREATE TABLE IF NOT EXISTS `movie` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) NOT NULL,
  `Details` text DEFAULT NULL,
  `ReleaseDate` date NOT NULL,
  `Duration` int(11) NOT NULL,
  `GenderId` int(11) NOT NULL,
  `Color` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `Movie_GenderId` (`GenderId`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `movie`
--

INSERT INTO `movie` (`Id`, `Name`, `Details`, `ReleaseDate`, `Duration`, `GenderId`, `Color`) VALUES
(4, 'Dune 2', 'Paul Atréides se rallie à Chani et aux Fremen tout en préparant sa revanche contre ceux qui ont détruit sa famille. Alors qu\'il doit faire un choix entre l\'amour de sa vie et le destin de la galaxie, il devra néanmoins tout faire pour empêcher un terrible futur que lui seul peut prédire.', '2024-02-28', 166, 2, '#FFB3B3'),
(9, 'Star Wars, épisode IX : L\'Ascension de Skywalker', 'Un an a passé depuis que Kylo Ren a tué Snoke, le Leader suprême et pris sa place. Bien que largement décimée, la Résistance est prête à renaître de ses cendres. Rey, Poe, Leia et leurs alliés se préparent à reprendre le combat. Mais ils vont devoir faire face à un vieil ennemi : l\'empereur Palpatine.', '2019-09-18', 142, 2, '#8099FF');

-- --------------------------------------------------------

--
-- Structure de la table `price`
--

DROP TABLE IF EXISTS `price`;
CREATE TABLE IF NOT EXISTS `price` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) NOT NULL,
  `Cost` float NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `price`
--

INSERT INTO `price` (`Id`, `Name`, `Cost`) VALUES
(3, '-18ans & Etudiant', 5),
(4, 'Plein', 10),
(5, 'Réduit', 8);

-- --------------------------------------------------------

--
-- Structure de la table `room`
--

DROP TABLE IF EXISTS `room`;
CREATE TABLE IF NOT EXISTS `room` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(75) NOT NULL,
  `Capacity` int(11) NOT NULL,
  `Color` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `room`
--

INSERT INTO `room` (`Id`, `Name`, `Capacity`, `Color`) VALUES
(1, 'Salle 1', 120, '#FF00FF'),
(2, 'Salle 2', 150, '#A9A9A9');

-- --------------------------------------------------------

--
-- Structure de la table `slot`
--

DROP TABLE IF EXISTS `slot`;
CREATE TABLE IF NOT EXISTS `slot` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `RoomId` int(11) NOT NULL,
  `MovieId` int(11) NOT NULL,
  `StartHour` int(11) NOT NULL,
  `Date` date NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `Slot_MovieId` (`MovieId`),
  KEY `Slot_RoomId` (`RoomId`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `slot`
--

INSERT INTO `slot` (`Id`, `RoomId`, `MovieId`, `StartHour`, `Date`) VALUES
(1, 2, 4, 6, '2024-03-08'),
(4, 1, 4, 1, '2024-03-11'),
(7, 2, 4, 9, '2024-03-12'),
(9, 2, 4, 7, '2024-03-13'),
(10, 2, 4, 6, '2024-03-14'),
(11, 1, 9, 10, '2024-03-14');

-- --------------------------------------------------------

--
-- Structure de la table `slotpricing`
--

DROP TABLE IF EXISTS `slotpricing`;
CREATE TABLE IF NOT EXISTS `slotpricing` (
  `SlotId` int(11) NOT NULL,
  `PriceId` int(11) NOT NULL,
  `OccupiedSeat` int(11) NOT NULL,
  KEY `PricingSlot_PriceId` (`PriceId`),
  KEY `PricingSlot_SlotId` (`SlotId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `slotpricing`
--

INSERT INTO `slotpricing` (`SlotId`, `PriceId`, `OccupiedSeat`) VALUES
(7, 3, 10),
(9, 3, 54),
(9, 3, 54),
(10, 3, 1),
(11, 3, 28);

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `movie`
--
ALTER TABLE `movie`
  ADD CONSTRAINT `Movie_GenderId` FOREIGN KEY (`GenderId`) REFERENCES `gender` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `slot`
--
ALTER TABLE `slot`
  ADD CONSTRAINT `Slot_MovieId` FOREIGN KEY (`MovieId`) REFERENCES `movie` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Slot_RoomId` FOREIGN KEY (`RoomId`) REFERENCES `room` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `slotpricing`
--
ALTER TABLE `slotpricing`
  ADD CONSTRAINT `PricingSlot_PriceId` FOREIGN KEY (`PriceId`) REFERENCES `price` (`Id`) ON DELETE CASCADE,
  ADD CONSTRAINT `PricingSlot_SlotId` FOREIGN KEY (`SlotId`) REFERENCES `slot` (`Id`) ON DELETE CASCADE;
COMMIT;