package db;

import java.util.List;

import model.Emprunt;
import model.Livre;
import model.Membre;

public interface IDataAccess {

    Livre getLivre(int id);
    List<Livre> getAllLivres();
    void addLivre(Livre livre);
    void updateLivre(Livre livre);
    void deleteLivre(int id);

    Membre getMembre(int id);
    List<Membre> getAllMembres();
    void addMembre(Membre membre);
    void updateMembre(Membre membre);
    void deleteMembre(int id);

    Emprunt getEmprunt(int id);
    List<Emprunt> getAllEmprunts();
    void addEmprunt(Emprunt emprunt);
    void updateEmprunt(Emprunt emprunt);
    void deleteEmprunt(int id);
	
}
