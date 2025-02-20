package controller;

import java.time.LocalDate;
import java.util.List;
import db.DatabaseManager;
import db.IDataAccess;
import model.Emprunt;
import model.Livre;
import model.Membre;

public class BibliothequeService {

    private final IDataAccess dataAccess;

    public BibliothequeService() {
        this.dataAccess = DatabaseManager.getInstance();
    }

    // LIVRES
    public void ajouterLivre(String titre, String auteur, String isbn) {
        if (titre == null || titre.trim().isEmpty()) {
            throw new IllegalArgumentException("Le titre ne peut pas être vide");
        }
        if (auteur == null || auteur.trim().isEmpty()) {
            throw new IllegalArgumentException("L'auteur ne peut pas être vide");
        }
        if (isbn == null || !isbn.matches("^\\d{13}$")) {
            throw new IllegalArgumentException("L'ISBN doit contenir 13 chiffres");
        }

        Livre livre = new Livre(0, titre, auteur, isbn);
        dataAccess.addLivre(livre);
    }

    public void modifierLivre(int id, String titre, String auteur, String isbn) {
        Livre livreExistant = dataAccess.getLivre(id);
        if (livreExistant == null) {
            throw new IllegalArgumentException("Livre non trouvé");
        }

        if (titre != null && !titre.trim().isEmpty()) {
            livreExistant.setTitre(titre);
        }
        if (auteur != null && !auteur.trim().isEmpty()) {
            livreExistant.setAuteur(auteur);
        }
        if (isbn != null && isbn.matches("^\\d{13}$")) {
            livreExistant.setIsbn(isbn);
        }

        dataAccess.updateLivre(livreExistant);
    }

    public void supprimerLivre(int id) {
        Livre livre = dataAccess.getLivre(id);
        if (livre == null) {
            throw new IllegalArgumentException("Livre non trouvé");
        }

        List<Emprunt> emprunts = dataAccess.getAllEmprunts();
        boolean livreEmprunte = emprunts.stream()
            .anyMatch(e -> e.getLivre().getId() == id && e.getDateRetour() == null);

        if (livreEmprunte) {
            throw new IllegalStateException("Impossible de supprimer un livre actuellement emprunté");
        }

        dataAccess.deleteLivre(id);
    }

    public List<Livre> rechercherLivres(String critere) {
        return dataAccess.getAllLivres().stream()
            .filter(l -> l.getTitre().toLowerCase().contains(critere.toLowerCase()) ||
                        l.getAuteur().toLowerCase().contains(critere.toLowerCase()) ||
                        l.getIsbn().contains(critere))
            .toList();
    }

    public List<Livre> getAllLivres() {
        return dataAccess.getAllLivres();
    }

    public Livre getLivreById(int id) {
        return dataAccess.getLivre(id);
    }

    // MEMBRES
    public void ajouterMembre(String nom, String email) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Email invalide");
        }

        Membre membre = new Membre(0, nom, email);
        dataAccess.addMembre(membre);
    }

    public void modifierMembre(int id, String nom, String email) {
        Membre membreExistant = dataAccess.getMembre(id);
        if (membreExistant == null) {
            throw new IllegalArgumentException("Membre non trouvé");
        }

        if (nom != null && !nom.trim().isEmpty()) {
            membreExistant.setNom(nom);
        }
        if (email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            membreExistant.setEmail(email);
        }

        dataAccess.updateMembre(membreExistant);
    }

    public void supprimerMembre(int id) {
        Membre membre = dataAccess.getMembre(id);
        if (membre == null) {
            throw new IllegalArgumentException("Membre non trouvé");
        }

        List<Emprunt> emprunts = dataAccess.getAllEmprunts();
        boolean membreADesEmprunts = emprunts.stream()
            .anyMatch(e -> e.getMembre().getId() == id && e.getDateRetour() == null);

        if (membreADesEmprunts) {
            throw new IllegalStateException("Impossible de supprimer un membre avec des emprunts en cours");
        }

        dataAccess.deleteMembre(id);
    }

    public List<Membre> getAllMembres() {
        return dataAccess.getAllMembres();
    }

    public Membre getMembreById(int id) {
        return dataAccess.getMembre(id);
    }

    // EMPRUNTS
    public void emprunterLivre(int livreId, int membreId) {
        Livre livre = dataAccess.getLivre(livreId);
        Membre membre = dataAccess.getMembre(membreId);

        if (livre == null) {
            throw new IllegalArgumentException("Livre non trouvé");
        }
        if (membre == null) {
            throw new IllegalArgumentException("Membre non trouvé");
        }

        List<Emprunt> emprunts = dataAccess.getAllEmprunts();
        boolean isLivreEmprunte = emprunts.stream()
            .anyMatch(e -> e.getLivre().getId() == livreId && e.getDateRetour() == null);

        if (isLivreEmprunte) {
            throw new IllegalStateException("Le livre est déjà emprunté");
        }

        Emprunt emprunt = new Emprunt(0, livre, membre, LocalDate.now(), null);
        dataAccess.addEmprunt(emprunt);
    }

    public void retournerLivre(int empruntId) {
        Emprunt emprunt = dataAccess.getEmprunt(empruntId);
        if (emprunt == null) {
            throw new IllegalArgumentException("Emprunt non trouvé");
        }
        if (emprunt.getDateRetour() != null) {
            throw new IllegalStateException("Ce livre a déjà été retourné");
        }

        emprunt.setDateRetour(LocalDate.now());
        dataAccess.updateEmprunt(emprunt);
    }

    public List<Emprunt> getEmpruntsEnCours() {
        return dataAccess.getAllEmprunts().stream()
            .filter(e -> e.getDateRetour() == null)
            .toList();
    }

    public List<Emprunt> getEmpruntsByMembre(int membreId) {
        return dataAccess.getAllEmprunts().stream()
            .filter(e -> e.getMembre().getId() == membreId)
            .toList();
    }

    public List<Emprunt> getEmpruntsByLivre(int livreId) {
        return dataAccess.getAllEmprunts().stream()
            .filter(e -> e.getLivre().getId() == livreId)
            .toList();
    }

    public Emprunt getEmpruntById(int id) {
        return dataAccess.getEmprunt(id);
    }

    public List<Emprunt> getAllEmprunts() {
        return dataAccess.getAllEmprunts();
    }
}