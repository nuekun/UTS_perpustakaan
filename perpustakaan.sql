-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 07 Okt 2019 pada 06.28
-- Versi server: 10.1.38-MariaDB
-- Versi PHP: 7.1.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `perpustakaan`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `buku`
--

CREATE TABLE `buku` (
  `id_buku` int(11) NOT NULL,
  `judul` varchar(50) NOT NULL,
  `pengarang` varchar(50) NOT NULL,
  `pemimjam` varchar(50) NOT NULL,
  `status` varchar(10) NOT NULL,
  `gambar` text NOT NULL,
  `jenis` varchar(30) NOT NULL,
  `riwayat` varchar(10) NOT NULL,
  `keterangan` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `buku`
--

INSERT INTO `buku` (`id_buku`, `judul`, `pengarang`, `pemimjam`, `status`, `gambar`, `jenis`, `riwayat`, `keterangan`) VALUES
(26, 'maling kundang', 'edy', '', 'tersedia', 'x46N3qm4A83.jpg', 'novel', '04/26/2019', 'harus dikembalikan'),
(27, 'click', 'Pengarang', '', 'tersedia', 'Qm4Uj1MK8Cu.jpg', 'bahasa', '04/26/2019', 'di samping yang ditakutkan'),
(28, 'cara mengatasi mimisan di', 'di dalam negerinya', '', 'tersedia', 'qWKv9HDFhGT.jpg', 'bahasa', '04/26/2019', 'cara mengatasi kelopak di dunia dan'),
(29, 'harga Rp', 'Penelitian', '', 'tersedia', 'jQjrbYk71zo.jpg', 'cerpen', '04/26/2019', 'cara mengatasi mimisan dan juga'),
(30, 'sssss', 'kkn', '', 'tersedia', 'dtELqWxhZot.jpg', 'cerpen', '04/26/2019', 'qda'),
(31, 'rft', 'fhjk', '', 'tersedia', 'Q6oj9RAEi97.jpg', 'cerpen', '04/26/2019', 'sdgn'),
(32, 'iiqh', 'jkakrji', '', 'tersedia', 'mUALj9ZGGd2.jpg', 'bahasa', '05/03/2019', 'yusqjjd hdiwosj dish parabola Venus dan mars yang dirahasiakan dan tidak hanya'),
(33, 'bahsa', 'Pengarang', '', 'tersedia', 'zCqKFW9M33G.jpg', 'baru', '05/05/2019', 'fghkkhg'),
(34, 'Muhd di', 'tidur KK dan KTP', '', 'tersedia', 'klyi06NDlul.jpg', 'jenis', '05/12/2019', 'giliran saya mahasiswa yang namanya tercantum dalam lampiran pengumuman ini adalah untuk'),
(35, 'uh yeah', 'Pengarang buku yang namanya tercantum', '', 'tersedia', 'EcpOEjVCcGL.jpg', 'bahasa', '05/12/2019', 'just enter the name singer to search'),
(36, 'gghh', 'Pengarang', '', 'tersedia', 'hIikdshdep1.jpg', 'jenis', '05/12/2019', 'yydry'),
(37, 'welek', 'welek', '', 'tersedia', 'BFuv4zESgrj.jpg', 'bahasa', '06/18/2019', 'welek');

-- --------------------------------------------------------

--
-- Struktur dari tabel `jenis`
--

CREATE TABLE `jenis` (
  `jenis` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `jenis`
--

INSERT INTO `jenis` (`jenis`) VALUES
('bahasa'),
('baru'),
('cerpen'),
('hp'),
('jenis'),
('novel'),
('sejarah');

-- --------------------------------------------------------

--
-- Struktur dari tabel `pengguna`
--

CREATE TABLE `pengguna` (
  `email` varchar(50) NOT NULL,
  `nama` varchar(30) NOT NULL,
  `password` text NOT NULL,
  `status` varchar(1) NOT NULL,
  `gambar` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `pengguna`
--

INSERT INTO `pengguna` (`email`, `nama`, `password`, `status`, `gambar`) VALUES
('', '', '', '0', ''),
('aaa@a.com', 'aaa', 'aaa', '0', ''),
('email@mail.com', 'user', '123', '0', ''),
('jjsis@gdjd.con', 'jjjjk', 'jjsjbdks', '0', ''),
('kkakdj@jjdkwk.com', 'jjsjsj', 'sjjslkfd', '0', ''),
('nkqkdk@jjdkak.com', 'apalah', '8wihd8aheo', '0', ''),
('reindrairawan@gmail.com', 'indra', '12061998', '0', ''),
('tidur@kasur.com', 'ngantuk', 'hkkdjskdjfl', '0', ''),
('ujjhh@ghhhj.jjj', 'hhjj', 'hhhjjkkhhji', '0', '');

-- --------------------------------------------------------

--
-- Struktur dari tabel `pinjam`
--

CREATE TABLE `pinjam` (
  `id_pinjam` int(11) NOT NULL,
  `tanggal` varchar(15) NOT NULL,
  `status` varchar(10) NOT NULL,
  `buku` varchar(10) NOT NULL,
  `pemimjam` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `pinjam`
--

INSERT INTO `pinjam` (`id_pinjam`, `tanggal`, `status`, `buku`, `pemimjam`) VALUES
(23, '04/26/2019', 'kembali', '26', 'saodah'),
(25, '04/26/2019', 'kembali', '24', 'adit'),
(26, '04/26/2019', 'kembali', '28', 'tukiyem'),
(27, '04/26/2019', 'kembali', '31', 'sholeh'),
(28, '05/01/2019', 'kembali', '26', 'ilham'),
(29, '05/03/2019', 'kembali', '30', 'kkkn'),
(30, '05/03/2019', 'kembali', '24', 'jejak'),
(31, '05/03/2019', 'kembali', '28', 'painem'),
(32, '05/05/2019', 'kembali', '33', 'eudi'),
(33, '05/12/2019', 'kembali', '33', 'muhkidi'),
(34, '05/12/2019', 'kembali', '35', 'muhkidi'),
(35, '05/12/2019', 'kembali', '33', 'udara Al'),
(36, '06/18/2019', 'kembali', '37', 'ilham'),
(37, '06/24/2019', 'kembali', '33', 'email@mail.com'),
(38, '06/24/2019', 'kembali', '34', 'email@mail.com'),
(39, '06/24/2019', 'kembali', '33', 'reindrairawan@gmail.com');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `buku`
--
ALTER TABLE `buku`
  ADD PRIMARY KEY (`id_buku`);

--
-- Indeks untuk tabel `jenis`
--
ALTER TABLE `jenis`
  ADD PRIMARY KEY (`jenis`);

--
-- Indeks untuk tabel `pengguna`
--
ALTER TABLE `pengguna`
  ADD PRIMARY KEY (`email`);

--
-- Indeks untuk tabel `pinjam`
--
ALTER TABLE `pinjam`
  ADD PRIMARY KEY (`id_pinjam`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `buku`
--
ALTER TABLE `buku`
  MODIFY `id_buku` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=38;

--
-- AUTO_INCREMENT untuk tabel `pinjam`
--
ALTER TABLE `pinjam`
  MODIFY `id_pinjam` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
