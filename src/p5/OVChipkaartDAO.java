package p5;

import java.util.ArrayList;
import java.util.List;

public interface OVChipkaartDAO {
    boolean save(OVChipkaart ovChipkaart);
    boolean saveList(ArrayList<OVChipkaart> ovChipkaartArrayList);
    boolean update(OVChipkaart ovChipkaart);
    boolean updateList(ArrayList<OVChipkaart> ovChipkaartArrayList);
    boolean delete(OVChipkaart ovChipkaart);
    boolean deleteList(ArrayList<OVChipkaart> ovChipkaartArrayList);
    ArrayList<OVChipkaart> findByReiziger(Reiziger reiziger);
    ArrayList<OVChipkaart> findall();
    ArrayList<OVChipkaart> findByProduct(Product pr);


}

