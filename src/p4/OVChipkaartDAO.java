package p4;

import java.util.List;

public interface OVChipkaartDAO {
        boolean save(OVChipkaart ov);
        boolean update(OVChipkaart ov);
        boolean delete(OVChipkaart ov);
    List<OVChipkaart> findByReiziger(Reiziger reiziger);
    List<OVChipkaart> findall();


}

