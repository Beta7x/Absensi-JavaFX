package Model;

public class Employee {

    private int id_absen;
    private String id_pegawai, tanggal, waktu, status;

    public Employee(int id_absen, String id_pegawai, String tanggal, String waktu, String status) {
        this.id_absen = id_absen;
        this.id_pegawai = id_pegawai;
        this.tanggal = tanggal;
        this.waktu = waktu;
        this.status = status;
    }

    public int getId_absen() {
        return id_absen;
    }

    public void setId_absen(int id_absen) {
        this.id_absen = id_absen;
    }

    public String getId_pegawai() {
        return id_pegawai;
    }

    public void setId_pegawai(String id_pegawai) {
        this.id_pegawai = id_pegawai;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
