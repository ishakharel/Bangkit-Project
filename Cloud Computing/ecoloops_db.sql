-- phpMyAdmin SQL Dump
-- version 4.7.1
-- https://www.phpmyadmin.net/
--
-- Host: 34.101.76.118
-- Generation Time: Jun 04, 2023 at 02:16 PM
-- Server version: 5.6.51-google-log
-- PHP Version: 7.0.33-0ubuntu0.16.04.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ecoloops_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `merch`
--

CREATE TABLE `merch` (
  `id` int(11) NOT NULL,
  `name` char(100) NOT NULL,
  `points` int(11) NOT NULL,
  `stok` int(11) NOT NULL,
  `image` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `merch`
--

INSERT INTO `merch` (`id`, `name`, `points`, `stok`, `image`) VALUES
(1, 'T-Shirt EcoLoops', 25000, 98, 'https://storage.googleapis.com/ecoloops_bucket/merch_photo/t-shirt.png'),
(2, 'Gopay Coin', 50000, 100, 'https://storage.googleapis.com/ecoloops_bucket/merch_photo/gopaycoins.png');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` char(16) NOT NULL,
  `token` text NOT NULL,
  `name` text NOT NULL,
  `email` text NOT NULL,
  `password` text NOT NULL,
  `image` text NOT NULL,
  `total_points` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `token`, `name`, `email`, `password`, `image`, `total_points`) VALUES
('1a52iANbZpmhLG-K', 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjFhNTJpQU5iWnBtaExHLUsiLCJpYXQiOjE2ODU0NDM1MDIsImV4cCI6MTY5MzIxOTUwMn0.qA5HgvhhW3WAxbIbxeBOwK0uWRHJyU6ggBOyZUv19Og', 'Azka Z H', 'azka@gmail.com', '$2a$08$I2xkNDdO6jrtyczNjCG3ZO9FzJNnLkxeYWK9cqGa62OXjO2NiCYvG', '', 100),
('CP84Nc5Uf7tuQL17', 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IkNQODROYzVVZjd0dVFMMTciLCJpYXQiOjE2ODU0NDI5NzcsImV4cCI6MTY5MzIxODk3N30.-TejwzSM2DaGiGK1CGkrKGhe6bwR6Pnz8TbNFVQKUMY', 'Azka Z Hanif', 'azka@mail.com', '$2a$08$Pcc0PO4SENzDryX8Wre3IucuYs4Z.NQEeSNG5Xka3K2R.J.gQNuBC', '', 100),
('D09UvoyWJ_hQ6oqh', 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IkQwOVV2b3lXSl9oUTZvcWgiLCJpYXQiOjE2ODU4MDE4NTQsImV4cCI6MTY5MzU3Nzg1NH0.o2WJO8IROVSx4vQ1smYay5bSpBsDatdKevJtvd5lmZw', 'Cahya Diantoni', 'cahya@gmail.com', '$2a$08$S9nIrKu3kFlOmGXDJ2lsFu0dkTLvq4y8Zw5eCU8roqbOvrhdtX5S2', '', 0),
('dem2mLniTb-sxjEl', 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImRlbTJtTG5pVGItc3hqRWwiLCJpYXQiOjE2ODU4ODA2NTgsImV4cCI6MTY5MzY1NjY1OH0.3O25OzW7yf5jRv4ikpHP-N4GNIQ-TPoNKRGfH5CR9EE', 'Agil Fikriawan', 'agilfikriawan020328@gmail.com', '$2a$08$erttEnt222CeS47yDorqcOF7lzi4TtGmfTId5M9jToFaWsazjH6G2', 'https://storage.googleapis.com/ecoloops_bucket/user_profile/dem2mLniTb-sxjEl', 1000),
('Eq-vXTztcEo4LfJi', 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IkVxLXZYVHp0Y0VvNExmSmkiLCJpYXQiOjE2ODU1NzQyMDcsImV4cCI6MTY5MzM1MDIwN30.hKbfG_NrDIAGdzaKRo1HCGRyXMYcrAFQdC0Ju8XnIKM', 'Azka Z Hanif', 'azkazhanif@gmail.com', '$2a$08$M77KPQd927S4wRxkN5Gv3uwJg0SK5g5LgfJF0RAX6K.kIyRtko/uy', 'https://storage.googleapis.com/ecoloops_bucket/user_profile/Eq-vXTztcEo4LfJi', 5000),
('QSCUdp3ngslvAJJ9', '', 'Azka Student Account', 'azka.hanif@student.president.ac.id', '$2a$08$hzSd.3AcTeW6/zKIhNiqguX.wO/5F62fUh1LZqPaJWOmwV3rTdgoy', '', 0),
('usxeVikNKW7Lqg79', '', 'Agil Fikriawan', 'agilfikriawan0203@gmail.com', '$2a$08$s8gsJWFNcoLEh6Fj.ZNX3OdrmddkMoxaxZc/77tWGlaRrfKlzuPwm', '', 0);

-- --------------------------------------------------------

--
-- Table structure for table `users_merch_redeem`
--

CREATE TABLE `users_merch_redeem` (
  `id` char(20) NOT NULL,
  `user_id` char(16) NOT NULL,
  `merch_id` int(11) NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users_merch_redeem`
--

INSERT INTO `users_merch_redeem` (`id`, `user_id`, `merch_id`, `date`) VALUES
('Cb1HyDvLE1DIOGpZyJqH', 'Eq-vXTztcEo4LfJi', 1, '2023-05-31'),
('Iioap0FRiOBeYmmoayUq', 'dem2mLniTb-sxjEl', 1, '2023-05-31'),
('qkVybmDnshBL03re_fHA', 'dem2mLniTb-sxjEl', 1, '2023-05-31');

-- --------------------------------------------------------

--
-- Table structure for table `users_notifications`
--

CREATE TABLE `users_notifications` (
  `id` char(20) NOT NULL,
  `user_id` char(16) NOT NULL,
  `title` text NOT NULL,
  `message` text NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users_notifications`
--

INSERT INTO `users_notifications` (`id`, `user_id`, `title`, `message`, `date`) VALUES
('zsVUwQu1v2s58sEqmd7Y', 'dem2mLniTb-sxjEl', 'Selamat Datang di Aplikasi Ecoloops', 'Bersama Ecoloops membuat lingkungan lebih bersih!!!', '2023-05-31');

-- --------------------------------------------------------

--
-- Table structure for table `users_otp`
--

CREATE TABLE `users_otp` (
  `id` char(16) NOT NULL,
  `email` text NOT NULL,
  `otp` char(6) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `waste_category`
--

CREATE TABLE `waste_category` (
  `id` int(11) NOT NULL,
  `name` char(100) NOT NULL,
  `image` text NOT NULL,
  `description` text NOT NULL,
  `category` char(50) NOT NULL,
  `description_recycle` text NOT NULL,
  `points` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `waste_category`
--

INSERT INTO `waste_category` (`id`, `name`, `image`, `description`, `category`, `description_recycle`, `points`) VALUES
(0, 'Kaleng', 'https://storage.googleapis.com/ecoloops_bucket/waste_categories/kaleng.jpg', 'Kaleng merujuk pada jenis wadah logam yang biasanya digunakan untuk mengemas minuman ringan, makanan kaleng, atau produk lainnya. Terbuat dari aluminium atau baja dan bisa didaur ulang. Dengan mendaur ulang kaleng, kita dapat mengurangi kebutuhan akan bahan baku baru, menghemat energi, dan mengurangi limbah yang berakhir di tempat pembuangan akhir.', 'Logam', 'Pastikan untuk membersihkan sisa makanan atau minuman dari kaleng sebelum membuangnya. Tempatkan kaleng bersama dengan sampah daur ulang logam lainnya atau serahkan ke pusat daur ulang logam.', 100),
(1, 'Karton', 'https://storage.googleapis.com/ecoloops_bucket/waste_categories/karton.jpg', 'Karton, juga dikenal sebagai kardus, adalah bahan padat yang terbuat dari serat selulosa yang dihasilkan dari kayu atau bahan daur ulang. Digunakan untuk membuat kotak kemasan, bungkus, dan berbagai produk lainnya. Karton dapat didaur ulang dengan proses pemrosesan yang melibatkan pemadatan, pemotongan, dan pembuatan serat karton baru.', 'Karton', 'Pastikan untuk menghapus pita perekat atau bahan berbahaya lainnya sebelum membuangnya. Lipat atau hancurkan karton untuk menghemat ruang dan tempatkan bersama dengan sampah daur ulang kertas atau karton lainnya.', 100),
(2, 'Kaca', 'https://storage.googleapis.com/ecoloops_bucket/waste_categories/kaca.jpg', 'Kaca adalah bahan padat transparan yang terbuat dari bahan utama seperti pasir silika, soda, kapur, dan dolomit. Digunakan dalam berbagai bentuk, termasuk botol, wadah, gelas, dan kaca jendela. Kaca memiliki kemampuan yang baik untuk didaur ulang.', 'Kaca', 'Tempatkan kaca bersama dengan sampah daur ulang kaca lainnya atau serahkan ke pusat daur ulang kaca.', 100),
(3, 'Obat-Obatan', 'https://storage.googleapis.com/ecoloops_bucket/waste_categories/obat.jpg', 'Obat adalah substansi yang digunakan untuk mencegah, mengobati, atau meringankan penyakit atau kondisi medis. Obat dapat berbentuk tablet, kapsul, cairan, salep, atau suntikan. Ketika obat sudah kadaluwarsa atau tidak digunakan lagi, disarankan untuk membuangnya dengan benar untuk mencegah dampak negatif pada lingkungan dan kesehatan.', 'Medis', 'Jangan membuang obat-obatan ke dalam toilet atau saluran pembuangan, karena dapat mencemari air minum dan lingkungan. Banyak apotek atau rumah sakit memiliki program pengumpulan obat bekas yang tidak terpakai. Anda dapat mengembalikan obat-obatan tersebut ke tempat tersebut agar dapat didaur ulang dengan aman.', 100),
(4, 'Wadah Logam', 'https://storage.googleapis.com/ecoloops_bucket/waste_categories/wadah_logam.jpg', 'Wadah logam merujuk pada berbagai jenis kontainer yang terbuat dari logam, seperti kaleng makanan, kaleng minuman, dan wadah logam lainnya. Wadah logam umumnya terbuat dari aluminium atau baja yang bisa didaur ulang.', 'Logam', 'Bersihkan wadah dari sisa makanan atau minuman sebelum membuangnya. Tempatkan wadah logam bersama dengan sampah daur ulang logam lainnya atau serahkan ke pusat daur ulang logam.', 100),
(5, 'Koran', 'https://storage.googleapis.com/ecoloops_bucket/waste_categories/koran.jpg', 'Koran adalah jenis kertas yang digunakan untuk mencetak berita, informasi, dan materi lainnya. Setelah dibaca, koran dapat didaur ulang untuk menghasilkan kertas daur ulang baru.', 'Kertas', 'Kumpulkan koran bekas dan pastikan tidak ada bahan berbahaya atau limbah lain yang melekat di atasnya. Tempatkan koran bersama dengan sampah daur ulang kertas lainnya atau serahkan ke pusat daur ulang kertas.', 100),
(6, 'Kertas', 'https://storage.googleapis.com/ecoloops_bucket/waste_categories/kertas.jpg', 'Kertas adalah bahan yang terbuat dari serat selulosa yang dihasilkan dari kayu atau bahan daur ulang. Kertas dapat didaur ulang dengan memisahkan kertas bekas dari sampah lainnya. Daur ulang kertas membantu mengurangi penebangan pohon, menghemat energi, dan mengurangi limbah sampah.', 'Kertas', 'Pastikan untuk membuang kertas bersih yang tidak terkontaminasi oleh bahan berbahaya atau limbah lainnya. Pisahkan kertas dari sampah organik atau plastik sebelum membuangnya. Tempatkan kertas bersama dengan sampah daur ulang kertas lainnya atau serahkan ke pusat daur ulang kertas.', 100),
(7, 'Gelas Kertas', 'https://storage.googleapis.com/ecoloops_bucket/waste_categories/gelas_kertas.jpg', 'Gelas kertas sekali pakai umumnya sulit didaur ulang karena lapisan plastik atau lilin di dalamnya. Sebaiknya hindari penggunaan gelas kertas sekali pakai dan beralih ke gelas yang dapat digunakan berulang kali.', 'Kertas', 'Gelas kertas sekali pakai umumnya sulit didaur ulang karena lapisan plastik atau lilin di dalamnya. Sebaiknya hindari penggunaan gelas kertas sekali pakai dan beralih ke gelas yang dapat digunakan berulang kali. Jika memungkinkan, cari tahu apakah ada program daur ulang khusus untuk gelas kertas di daerah Anda. Beberapa fasilitas daur ulang mungkin menerima gelas kertas yang tidak terkontaminasi.', 100),
(8, 'Kantong Plastik', 'https://storage.googleapis.com/ecoloops_bucket/waste_categories/kantong_plastik.jpg', 'Kantong plastik adalah wadah fleksibel yang terbuat dari plastik, seperti polietilena atau polipropilena. Kantong plastik sekali pakai sangat sulit didaur ulang karena bahan plastiknya yang tipis dan sulit terurai secara alami. Penggunaan berlebihan kantong plastik sekali pakai telah menjadi masalah lingkungan yang serius karena mereka dapat mencemari lingkungan dan membahayakan kehidupan laut.', 'Plastik', 'Banyak daerah sekarang memiliki program pengurangan penggunaan kantong plastik sekali pakai. Menggunakan kantong belanja kain atau kantong plastik yang dapat digunakan ulang merupakan alternatif yang lebih baik. Jika Anda memiliki kantong plastik sekali pakai, cari tahu apakah ada pusat daur ulang plastik di daerah Anda yang menerima kantong plastik bekas.', 100),
(9, 'Botol Plastik', 'https://storage.googleapis.com/ecoloops_bucket/waste_categories/botol_plastik.jpg', 'Botol plastik adalah wadah berbentuk botol yang terbuat dari plastik, seperti PET (polyethylene terephthalate) atau HDPE (high-density polyethylene). Digunakan untuk mengemas minuman, produk pembersih, kosmetik, dan berbagai bahan lainnya. Botol plastik dapat didaur ulang menjadi bahan baku untuk membuat botol plastik baru atau produk lain yang menggunakan plastik daur ulang.', 'Plastik', 'Pastikan untuk membuang tutup botol dan membuang sisa minuman sebelum membuangnya. Tempatkan botol plastik bersama dengan sampah daur ulang plastik lainnya atau serahkan ke pusat daur ulang plastik.', 100),
(10, 'Gelas Plastik', 'https://storage.googleapis.com/ecoloops_bucket/waste_categories/gelas_plastik.jpg', 'Gelas plastik adalah wadah berbentuk gelas yang terbuat dari plastik, seperti polistirena atau polipropilena. Gelas plastik sekali pakai umumnya sulit didaur ulang karena bahan plastiknya yang tipis dan sulit terurai. Disarankan untuk menghindari penggunaan gelas plastik sekali pakai dan beralih ke gelas kaca atau tumbler yang dapat digunakan berulang kali.', 'Plastik', 'Gelas plastik sekali pakai umumnya sulit didaur ulang karena bahan plastiknya yang lebih tipis dan sulit didaur ulang. Sebaiknya hindari penggunaan gelas plastik sekali pakai dan beralih ke gelas kaca atau tumbler yang dapat digunakan berulang kali. Jika memungkinkan, cari tahu apakah ada program daur ulang khusus untuk gelas plastik di daerah Anda.', 100),
(11, 'Kaleng Semprot', 'https://storage.googleapis.com/ecoloops_bucket/waste_categories/kaleng_semprot.jpg', 'Kaleng semprot adalah wadah logam atau logam dengan lapisan dalam yang berisi bahan cair atau gas di dalamnya. Kaleng semprot digunakan untuk menyimpan dan mengeluarkan berbagai produk, seperti cat, pengharum udara, insektisida, atau produk kosmetik dalam bentuk semprotan.', 'Logam', 'Pastikan untuk menggunakan seluruh isi spray can sebelum membuangnya. Anda dapat membawa kaleng semprot bekas ke pusat daur ulang logam atau menyampaikan kepada pihak pengelola sampah daur ulang untuk diproses secara tepat.', 100),
(12, 'Masker', 'https://storage.googleapis.com/ecoloops_bucket/waste_categories/masker.jpg', 'Masker bedah adalah alat pelindung yang biasanya terbuat dari kain non-daur ulang, kertas, atau polipropilena. Masker bedah adalah alat pelindung yang biasanya terbuat dari kain non-daur ulang, kertas, atau polipropilena.', 'Medis', 'Masker bedah umumnya sulit didaur ulang karena terbuat dari bahan non-daur. Anda dapat mempertimbangkan untuk menggunakan masker kain yang dapat dicuci ulang sebagai alternatif yang lebih ramah lingkungan. Jika memungkinkan, pisahkan tali pengikat masker dari bagian masker sebelum membuangnya agar dapat didaur ulang secara terpisah. Pastikan untuk membuang masker dengan benar sesuai dengan pedoman pengelolaan sampah medis di daerah Anda.', 100),
(13, 'Tetra Pak', 'https://storage.googleapis.com/ecoloops_bucket/waste_categories/tetra_pak.jpg', 'Tetra Pak merujuk pada merek dagang kemasan karton aseptic yang digunakan untuk mengemas susu, minuman kemasan, dan produk makanan lainnya. Kemasan Tetra Pak terdiri dari lapisan aluminium, lapisan plastik, dan lapisan karton. Proses daur ulang Tetra Pak melibatkan pemisahan lapisan-lapisan tersebut.', 'Kertas', 'Pastikan untuk membersihkan sisa minuman dari kemasan sebelum membuangnya. Tempatkan kemasan tetra pak bersama dengan sampah daur ulang karton atau ikuti petunjuk daur ulang yang ada di daerah Anda.', 100);

-- --------------------------------------------------------

--
-- Table structure for table `waste_history`
--

CREATE TABLE `waste_history` (
  `id` char(16) NOT NULL,
  `user_id` char(16) NOT NULL,
  `category_id` int(11) NOT NULL,
  `image` text NOT NULL,
  `point` int(11) NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `waste_history`
--

INSERT INTO `waste_history` (`id`, `user_id`, `category_id`, `image`, `point`, `date`) VALUES
('48TEvD0OmdZnoGox', 'dem2mLniTb-sxjEl', 3, 'https://storage.googleapis.com/scan-waste-users/frame_4x.jpeg', 100, '2023-05-30'),
('4Rpl1uLwO01XGWJy', 'dem2mLniTb-sxjEl', 5, 'https://storage.googleapis.com/ecoloops_bucket/waste_history/4Rpl1uLwO01XGWJy', 100, '2023-06-04'),
('5NpsxeQar5WhkFpt', 'CP84Nc5Uf7tuQL17', 6, 'https://storage.googleapis.com/scan-waste-users/MASTER.png', 100, '2023-05-30'),
('B3VZyR7uCHixQiu7', 'Eq-vXTztcEo4LfJi', 8, 'https://storage.googleapis.com/scan-waste-users/WhatsApp Image 2023-05-18 at 10.59.04.jpg', 100, '2023-05-30'),
('dmDiUahg0XBYYyp9', 'CP84Nc5Uf7tuQL17', 4, 'https://storage.googleapis.com/scan-waste-users/frame_4x.jpeg', 100, '2023-05-30'),
('F-TvO-KhJbuqUPPp', 'dem2mLniTb-sxjEl', 1, 'https://storage.googleapis.com/ecoloops_bucket/waste_history/F-TvO-KhJbuqUPPp', 100, '2023-06-04'),
('g6NuBwUWQcBWF75l', 'CP84Nc5Uf7tuQL17', 5, 'https://storage.googleapis.com/scan-waste-users/frame_4x.jpeg', 100, '2023-05-30'),
('HY0MfESUEBN-ngNC', 'dem2mLniTb-sxjEl', 10, 'https://storage.googleapis.com/scan-waste-users/frame_4x.jpeg', 100, '2023-05-30'),
('ki4oO115g2-ZLcB9', 'CP84Nc5Uf7tuQL17', 8, 'https://storage.googleapis.com/scan-waste-users/MASTER.png', 100, '2023-05-30'),
('ucA8X-20S_b1yL3j', 'CP84Nc5Uf7tuQL17', 7, 'https://storage.googleapis.com/scan-waste-users/frame_4x.jpeg', 100, '2023-05-30'),
('UZZwsfAvAvQjlNz0', 'dem2mLniTb-sxjEl', 5, 'https://storage.googleapis.com/scan-waste-users/frame_4x.jpeg', 100, '2023-05-30'),
('yCbtFCuzt6r-0o4M', 'dem2mLniTb-sxjEl', 5, 'https://storage.googleapis.com/ecoloops_bucket/waste_history/yCbtFCuzt6r-0o4M', 100, '2023-06-04'),
('YdBFsJOq0X6u1QHr', 'usxeVikNKW7Lqg79', 3, 'https://storage.googleapis.com/scan-waste-users/frame_4x.jpeg', 100, '2023-05-30'),
('yjaZaPCzwMAnkHri', 'dem2mLniTb-sxjEl', 11, 'https://storage.googleapis.com/ecoloops_bucket/waste_history/yjaZaPCzwMAnkHri', 100, '2023-06-04');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `merch`
--
ALTER TABLE `merch`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users_merch_redeem`
--
ALTER TABLE `users_merch_redeem`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users_notifications`
--
ALTER TABLE `users_notifications`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `waste_category`
--
ALTER TABLE `waste_category`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `waste_history`
--
ALTER TABLE `waste_history`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_users_to_waste` (`user_id`),
  ADD KEY `fk_category_waste` (`category_id`) USING BTREE;

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `merch`
--
ALTER TABLE `merch`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `waste_category`
--
ALTER TABLE `waste_category`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `waste_history`
--
ALTER TABLE `waste_history`
  ADD CONSTRAINT `fk_category_waste` FOREIGN KEY (`category_id`) REFERENCES `waste_category` (`id`),
  ADD CONSTRAINT `fk_users_to_waste` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
