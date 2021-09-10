package p3;

import java.util.List;

public interface AdresDAO {
    boolean save(Adres adres);
    boolean update(Adres adres);
    boolean delete(Adres adres);
    Adres findByReiziger(p3.Reiziger reiziger);
    List<Adres> findall();
    void connectRDAO(ReizigerDAO rdao);
}
