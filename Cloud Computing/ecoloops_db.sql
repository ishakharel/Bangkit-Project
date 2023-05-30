-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 31, 2023 at 12:35 AM
-- Server version: 10.4.25-MariaDB
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
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
(1, 'T-Shirt EcoLoops', 25000, 7, ''),
(2, 'Gopay Coin', 10, 10, '');

-- --------------------------------------------------------

--
-- Table structure for table `new_waste_category`
--

CREATE TABLE `new_waste_category` (
  `id` int(11) NOT NULL,
  `name` char(100) NOT NULL,
  `category` char(50) NOT NULL,
  `description` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
('Eq-vXTztcEo4LfJi', 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IkVxLXZYVHp0Y0VvNExmSmkiLCJpYXQiOjE2ODU0NDQ4NzMsImV4cCI6MTY5MzIyMDg3M30.__YOdxkfHZdA9xDio3lr5b2bbzpfwlGeF6KxvlY-Jnw', 'Azka Zufar Hanif', 'azkazhanif@gmail.com', '$2a$08$f.N6wv010.9i6xLQBufISOCNoN1iH/2l8oyskIuYfZgxdpWj8VfaK', '', 100);

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
('aH-1wGlNhtYB3c74q85_', 'CP84Nc5Uf7tuQL17', 'Notif 1', 'Hadiah mobil hotwheels', '2023-05-30');

-- --------------------------------------------------------

--
-- Table structure for table `users_otp`
--

CREATE TABLE `users_otp` (
  `email` text NOT NULL,
  `otp` char(6) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp()
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
(1, 'Jarum Suntik', '', 'Jarum suntik adalah alat medis yang digunakan untuk mengambil atau mengirimkan cairan dalam tubuh. Setelah digunakan, jarum suntik menjadi limbah medis yang harus dikelola dengan hati-hati karena dapat membawa risiko infeksi dan cedera jika tidak dibuang dengan benar.', 'Medis', 'Jarum suntik harus dikelola dengan hati-hati karena dapat membahayakan kesehatan. Beberapa daerah memiliki program pengumpulan jarum suntik bekas yang aman. Anda dapat menghubungi pihak berwenang setempat atau fasilitas medis terdekat untuk informasi lebih lanjut tentang tempat pengumpulan dan pengolahan jarum suntik bekas.', 0),
(2, 'Sarung Tangan Bedah', '', 'Sarung tangan bedah adalah alat pelindung yang dikenakan oleh tenaga medis selama operasi atau prosedur bedah. Biasanya terbuat dari lateks atau nitril, dan tersedia dalam berbagai ukuran. Sarung tangan bedah digunakan untuk menghindari kontaminasi silang antara tenaga medis dan pasien, serta melindungi tenaga medis dari bahan berbahaya atau infeksi.', 'Medis', 'Kumpulkan sarung tangan bedah yang digunakan dan pastikan tidak ada bahan berbahaya atau limbah medis lainnya yang melekat pada mereka. Hubungi fasilitas pengelolaan limbah medis terdekat atau perusahaan daur ulang yang menerima sarung tangan medis bekas.', 0),
(3, 'Masker Bedah', '', 'Masker bedah adalah alat pelindung yang biasanya terbuat dari kain non-daur ulang, kertas, atau polipropilena. Masker bedah adalah alat pelindung yang biasanya terbuat dari kain non-daur ulang, kertas, atau polipropilena.', 'Medis', 'Masker bedah umumnya sulit didaur ulang karena terbuat dari bahan non-daur. Anda dapat mempertimbangkan untuk menggunakan masker kain yang dapat dicuci ulang sebagai alternatif yang lebih ramah lingkungan. Jika memungkinkan, pisahkan tali pengikat masker dari bagian masker sebelum membuangnya agar dapat didaur ulang secara terpisah. Pastikan untuk membuang masker dengan benar sesuai dengan pedoman pengelolaan sampah medis di daerah Anda.', 0),
(4, 'Obat', '', 'Obat adalah substansi yang digunakan untuk mencegah, mengobati, atau meringankan penyakit atau kondisi medis. Obat dapat berbentuk tablet, kapsul, cairan, salep, atau suntikan. Ketika obat sudah kadaluwarsa atau tidak digunakan lagi, disarankan untuk membuangnya dengan benar untuk mencegah dampak negatif pada lingkungan dan kesehatan.', 'Medis', 'Jangan membuang obat-obatan ke dalam toilet atau saluran pembuangan, karena dapat mencemari air minum dan lingkungan. Banyak apotek atau rumah sakit memiliki program pengumpulan obat bekas yang tidak terpakai. Anda dapat mengembalikan obat-obatan tersebut ke tempat tersebut agar dapat didaur ulang dengan aman.', 0),
(5, 'Kaleng Semprot', '', 'Kaleng semprot adalah wadah logam atau logam dengan lapisan dalam yang berisi bahan cair atau gas di dalamnya. Kaleng semprot digunakan untuk menyimpan dan mengeluarkan berbagai produk, seperti cat, pengharum udara, insektisida, atau produk kosmetik dalam bentuk semprotan.', 'Logam', 'Pastikan untuk menggunakan seluruh isi spray can sebelum membuangnya. Anda dapat membawa kaleng semprot bekas ke pusat daur ulang logam atau menyampaikan kepada pihak pengelola sampah daur ulang untuk diproses secara tepat.', 0),
(6, 'Wadah Logam', '', 'Wadah logam merujuk pada berbagai jenis kontainer yang terbuat dari logam, seperti kaleng makanan, kaleng minuman, dan wadah logam lainnya. Wadah logam umumnya terbuat dari aluminium atau baja yang bisa didaur ulang.', 'Logam', 'Bersihkan wadah dari sisa makanan atau minuman sebelum membuangnya. Tempatkan wadah logam bersama dengan sampah daur ulang logam lainnya atau serahkan ke pusat daur ulang logam.', 0),
(7, 'Kaleng', '', 'Kaleng merujuk pada jenis wadah logam yang biasanya digunakan untuk mengemas minuman ringan, makanan kaleng, atau produk lainnya. Terbuat dari aluminium atau baja dan bisa didaur ulang. Dengan mendaur ulang kaleng, kita dapat mengurangi kebutuhan akan bahan baku baru, menghemat energi, dan mengurangi limbah yang berakhir di tempat pembuangan akhir.', 'Logam', 'Pastikan untuk membersihkan sisa makanan atau minuman dari kaleng sebelum membuangnya. Tempatkan kaleng bersama dengan sampah daur ulang logam lainnya atau serahkan ke pusat daur ulang logam.', 0),
(8, 'Kantong Plastik', '', 'Kantong plastik adalah wadah fleksibel yang terbuat dari plastik, seperti polietilena atau polipropilena. Kantong plastik sekali pakai sangat sulit didaur ulang karena bahan plastiknya yang tipis dan sulit terurai secara alami. Penggunaan berlebihan kantong plastik sekali pakai telah menjadi masalah lingkungan yang serius karena mereka dapat mencemari lingkungan dan membahayakan kehidupan laut.', 'Plastik', 'Banyak daerah sekarang memiliki program pengurangan penggunaan kantong plastik sekali pakai. Menggunakan kantong belanja kain atau kantong plastik yang dapat digunakan ulang merupakan alternatif yang lebih baik. Jika Anda memiliki kantong plastik sekali pakai, cari tahu apakah ada pusat daur ulang plastik di daerah Anda yang menerima kantong plastik bekas.', 0),
(9, 'Botol Plastik', '', 'Botol plastik adalah wadah berbentuk botol yang terbuat dari plastik, seperti PET (polyethylene terephthalate) atau HDPE (high-density polyethylene). Digunakan untuk mengemas minuman, produk pembersih, kosmetik, dan berbagai bahan lainnya. Botol plastik dapat didaur ulang menjadi bahan baku untuk membuat botol plastik baru atau produk lain yang menggunakan plastik daur ulang.', 'Plastik', 'Pastikan untuk membuang tutup botol dan membuang sisa minuman sebelum membuangnya. Tempatkan botol plastik bersama dengan sampah daur ulang plastik lainnya atau serahkan ke pusat daur ulang plastik.', 0),
(10, 'Gelas Plastik', '', 'Gelas plastik adalah wadah berbentuk gelas yang terbuat dari plastik, seperti polistirena atau polipropilena. Gelas plastik sekali pakai umumnya sulit didaur ulang karena bahan plastiknya yang tipis dan sulit terurai. Disarankan untuk menghindari penggunaan gelas plastik sekali pakai dan beralih ke gelas kaca atau tumbler yang dapat digunakan berulang kali.', 'Plastik', 'Gelas plastik sekali pakai umumnya sulit didaur ulang karena bahan plastiknya yang lebih tipis dan sulit didaur ulang. Sebaiknya hindari penggunaan gelas plastik sekali pakai dan beralih ke gelas kaca atau tumbler yang dapat digunakan berulang kali. Jika memungkinkan, cari tahu apakah ada program daur ulang khusus untuk gelas plastik di daerah Anda.', 0),
(11, 'Kaca', '', 'Kaca adalah bahan padat transparan yang terbuat dari bahan utama seperti pasir silika, soda, kapur, dan dolomit. Digunakan dalam berbagai bentuk, termasuk botol, wadah, gelas, dan kaca jendela. Kaca memiliki kemampuan yang baik untuk didaur ulang.', 'Kaca', 'Tempatkan kaca bersama dengan sampah daur ulang kaca lainnya atau serahkan ke pusat daur ulang kaca.', 0),
(12, 'Karton', '', 'Karton, juga dikenal sebagai kardus, adalah bahan padat yang terbuat dari serat selulosa yang dihasilkan dari kayu atau bahan daur ulang. Digunakan untuk membuat kotak kemasan, bungkus, dan berbagai produk lainnya. Karton dapat didaur ulang dengan proses pemrosesan yang melibatkan pemadatan, pemotongan, dan pembuatan serat karton baru.', 'Karton', 'Pastikan untuk menghapus pita perekat atau bahan berbahaya lainnya sebelum membuangnya. Lipat atau hancurkan karton untuk menghemat ruang dan tempatkan bersama dengan sampah daur ulang kertas atau karton lainnya.', 0),
(13, 'Tetra Pak', '', 'Tetra Pak merujuk pada merek dagang kemasan karton aseptic yang digunakan untuk mengemas susu, minuman kemasan, dan produk makanan lainnya. Kemasan Tetra Pak terdiri dari lapisan aluminium, lapisan plastik, dan lapisan karton. Proses daur ulang Tetra Pak melibatkan pemisahan lapisan-lapisan tersebut.', 'Kertas', 'Pastikan untuk membersihkan sisa minuman dari kemasan sebelum membuangnya. Tempatkan kemasan tetra pak bersama dengan sampah daur ulang karton atau ikuti petunjuk daur ulang yang ada di daerah Anda.', 0),
(14, 'Kertas', '', 'Kertas adalah bahan yang terbuat dari serat selulosa yang dihasilkan dari kayu atau bahan daur ulang. Kertas dapat didaur ulang dengan memisahkan kertas bekas dari sampah lainnya. Daur ulang kertas membantu mengurangi penebangan pohon, menghemat energi, dan mengurangi limbah sampah.', 'Kertas', 'Pastikan untuk membuang kertas bersih yang tidak terkontaminasi oleh bahan berbahaya atau limbah lainnya. Pisahkan kertas dari sampah organik atau plastik sebelum membuangnya. Tempatkan kertas bersama dengan sampah daur ulang kertas lainnya atau serahkan ke pusat daur ulang kertas.', 0),
(15, 'Gelas Kertas', '', 'Gelas kertas sekali pakai umumnya sulit didaur ulang karena lapisan plastik atau lilin di dalamnya. Sebaiknya hindari penggunaan gelas kertas sekali pakai dan beralih ke gelas yang dapat digunakan berulang kali.', 'Kertas', 'Gelas kertas sekali pakai umumnya sulit didaur ulang karena lapisan plastik atau lilin di dalamnya. Sebaiknya hindari penggunaan gelas kertas sekali pakai dan beralih ke gelas yang dapat digunakan berulang kali. Jika memungkinkan, cari tahu apakah ada program daur ulang khusus untuk gelas kertas di daerah Anda. Beberapa fasilitas daur ulang mungkin menerima gelas kertas yang tidak terkontaminasi.', 0),
(16, 'Koran', '', 'Koran adalah jenis kertas yang digunakan untuk mencetak berita, informasi, dan materi lainnya. Setelah dibaca, koran dapat didaur ulang untuk menghasilkan kertas daur ulang baru.', 'Kertas', 'Kumpulkan koran bekas dan pastikan tidak ada bahan berbahaya atau limbah lain yang melekat di atasnya. Tempatkan koran bersama dengan sampah daur ulang kertas lainnya atau serahkan ke pusat daur ulang kertas.', 0);

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
('48TEvD0OmdZnoGox', 'CP84Nc5Uf7tuQL17', 1, 'https://storage.googleapis.com/scan-waste-users/frame_4x.jpeg', 100, '2023-05-30'),
('5NpsxeQar5WhkFpt', 'CP84Nc5Uf7tuQL17', 6, 'https://storage.googleapis.com/scan-waste-users/MASTER.png', 100, '2023-05-30'),
('B3VZyR7uCHixQiu7', 'Eq-vXTztcEo4LfJi', 8, 'https://storage.googleapis.com/scan-waste-users/WhatsApp Image 2023-05-18 at 10.59.04.jpg', 100, '2023-05-30'),
('dmDiUahg0XBYYyp9', 'CP84Nc5Uf7tuQL17', 1, 'https://storage.googleapis.com/scan-waste-users/frame_4x.jpeg', 100, '2023-05-30'),
('g6NuBwUWQcBWF75l', 'CP84Nc5Uf7tuQL17', 1, 'https://storage.googleapis.com/scan-waste-users/frame_4x.jpeg', 100, '2023-05-30'),
('HY0MfESUEBN-ngNC', 'CP84Nc5Uf7tuQL17', 1, 'https://storage.googleapis.com/scan-waste-users/frame_4x.jpeg', 100, '2023-05-30'),
('ki4oO115g2-ZLcB9', 'CP84Nc5Uf7tuQL17', 8, 'https://storage.googleapis.com/scan-waste-users/MASTER.png', 100, '2023-05-30'),
('ucA8X-20S_b1yL3j', 'CP84Nc5Uf7tuQL17', 1, 'https://storage.googleapis.com/scan-waste-users/frame_4x.jpeg', 100, '2023-05-30'),
('UZZwsfAvAvQjlNz0', 'CP84Nc5Uf7tuQL17', 1, 'https://storage.googleapis.com/scan-waste-users/frame_4x.jpeg', 100, '2023-05-30'),
('YdBFsJOq0X6u1QHr', 'CP84Nc5Uf7tuQL17', 1, 'https://storage.googleapis.com/scan-waste-users/frame_4x.jpeg', 100, '2023-05-30');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `merch`
--
ALTER TABLE `merch`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `new_waste_category`
--
ALTER TABLE `new_waste_category`
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
  ADD KEY `fk_category_waste` (`category_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `merch`
--
ALTER TABLE `merch`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `new_waste_category`
--
ALTER TABLE `new_waste_category`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

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
