-- Menampilkan tanggal dengan format indonesia dari table kehadiran
SELECT id_absen, id_pegawai, DATE_FORMAT(tanggal, '%a, %e %M %Y') AS tanggal, waktu, status 
FROM kehadiran;

-- Menampilkan nama pegawai dan format tanggal(indonesia) dari table kehadiran
SELECT kehadiran.id_absen, pegawai.nama, DATE_FORMAT(kehadiran.tanggal, '%a, %e %M %Y') AS tanggal, kehadiran.waktu, kehadiran.status
FROM kehadiran INNER JOIN pegawai on pegawai.id_pegawai=kehadiran.id_pegawai
ORDER BY kehadiran.id_absen ASC;
