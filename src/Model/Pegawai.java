package Model;

public class Pegawai {

    private int id_pegawai;
    private String nomor_pegawai, nama, golongan, jabatan, ttl, alamat;

    public Pegawai(int id_pegawai, String nomor_pegawai, String nama, String golongan, String jabatan, String ttl, String alamat) {
        this.id_pegawai = id_pegawai;
        this.nomor_pegawai = nomor_pegawai;
        this.nama = nama;
        this.golongan = golongan;
        this.jabatan = jabatan;
        this.ttl = ttl;
        this.alamat = alamat;
    }

    public int getId_pegawai() {
        return id_pegawai;
    }

    public void setId_pegawai(int id_pegawai) {
        this.id_pegawai = id_pegawai;
    }

    public String getNomor_pegawai() {
        return nomor_pegawai;
    }

    public void setNomor_pegawai(String nomor_pegawai) {
        this.nomor_pegawai = nomor_pegawai;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getGolongan() {
        return golongan;
    }

    public void setGolongan(String golongan) {
        this.golongan = golongan;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
