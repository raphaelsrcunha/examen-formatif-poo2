package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Emprunt;
import model.Livre;
import model.Membre;

public class DatabaseManager implements IDataAccess {

	private static DatabaseManager instance;
	private Connection connection;
	private static final String DB_URL = "jdbc:sqlite:bibliotheque.db";
	
	private DatabaseManager() {
		try {
			connection = DriverManager.getConnection(DB_URL);
			initializeTables();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static DatabaseManager getInstance() {
		if (instance == null) {
			instance = new DatabaseManager();
		}
		return instance;
	}
	
	private void initializeTables() throws SQLException {
        String createLivresTable = "CREATE TABLE IF NOT EXISTS livres "
        		+ "(id INTEGER PRIMARY KEY AUTOINCREMENT,"
        		+ "titre TEXT NOT NULL,"
        		+ "auteur TEXT NOT NULL,"
        		+ "isbn TEXT NOT NULL UNIQUE"
        		+ ")";

        String createMembresTable = "CREATE TABLE IF NOT EXISTS membres ("
        		+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
        		+ "nom TEXT NOT NULL,"
        		+ "email TEXT NOT NULL UNIQUE"
        		+ ")";

        String createEmpruntsTable = "CREATE TABLE IF NOT EXISTS emprunts ("
        		+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
        		+ "livre_id INTEGER,"
        		+ "membre_id INTEGER,"
        		+ "date_emprunt DATE NOT NULL,"
        		+ "date_retour DATE,"
        		+ "FOREIGN KEY (livre_id) REFERENCES livres(id),"
        		+ "FOREIGN KEY (membre_id) REFERENCES membres(id)"
        		+ ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createLivresTable);
            stmt.execute(createMembresTable);
            stmt.execute(createEmpruntsTable);
        }
    }

	@Override
	public Livre getLivre(int id) {
	    String sql = "SELECT * FROM livres WHERE id = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setInt(1, id);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            return new Livre(
	                rs.getInt("id"),
	                rs.getString("titre"),
	                rs.getString("auteur"),
	                rs.getString("isbn")
	            );
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	@Override
	public List<Livre> getAllLivres() {
	    List<Livre> livres = new ArrayList<>();
	    String sql = "SELECT * FROM livres";
	    try (Statement stmt = connection.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {
	        while (rs.next()) {
	            livres.add(new Livre(
	                rs.getInt("id"),
	                rs.getString("titre"),
	                rs.getString("auteur"),
	                rs.getString("isbn")
	            ));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return livres;
	}

	@Override
	public void addLivre(Livre livre) {
	    String sql = "INSERT INTO livres (titre, auteur, isbn) VALUES (?, ?, ?)";
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setString(1, livre.getTitre());
	        pstmt.setString(2, livre.getAuteur());
	        pstmt.setString(3, livre.getIsbn());
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	@Override
	public void updateLivre(Livre livre) {
	    String sql = "UPDATE livres SET titre = ?, auteur = ?, isbn = ? WHERE id = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setString(1, livre.getTitre());
	        pstmt.setString(2, livre.getAuteur());
	        pstmt.setString(3, livre.getIsbn());
	        pstmt.setInt(4, livre.getId());
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	@Override
	public void deleteLivre(int id) {
	    String sql = "DELETE FROM livres WHERE id = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setInt(1, id);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	@Override
	public Membre getMembre(int id) {
	    String sql = "SELECT * FROM membres WHERE id = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setInt(1, id);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            return new Membre(
	                rs.getInt("id"),
	                rs.getString("nom"),
	                rs.getString("email")
	            );
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	@Override
	public List<Membre> getAllMembres() {
	    List<Membre> membres = new ArrayList<>();
	    String sql = "SELECT * FROM membres";
	    try (Statement stmt = connection.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {
	        while (rs.next()) {
	            membres.add(new Membre(
	                rs.getInt("id"),
	                rs.getString("nom"),
	                rs.getString("email")
	            ));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return membres;
	}

	@Override
	public void addMembre(Membre membre) {
	    String sql = "INSERT INTO membres (nom, email) VALUES (?, ?)";
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setString(1, membre.getNom());
	        pstmt.setString(2, membre.getEmail());
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	@Override
	public void updateMembre(Membre membre) {
	    String sql = "UPDATE membres SET nom = ?, email = ? WHERE id = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setString(1, membre.getNom());
	        pstmt.setString(2, membre.getEmail());
	        pstmt.setInt(3, membre.getId());
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	@Override
	public void deleteMembre(int id) {
	    String sql = "DELETE FROM membres WHERE id = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setInt(1, id);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	@Override
	public Emprunt getEmprunt(int id) {
	    String sql = "SELECT e.*, l.*, m.* "
	    		+ "FROM emprunts e "
	    		+ "JOIN livres l ON e.livre_id = l.id "
	    		+ "JOIN membres m ON e.membre_id = m.id "
	    		+ "WHERE e.id = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setInt(1, id);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            Livre livre = new Livre(
	                rs.getInt("livre_id"),
	                rs.getString("titre"),
	                rs.getString("auteur"),
	                rs.getString("isbn")
	            );
	            Membre membre = new Membre(
	                rs.getInt("membre_id"),
	                rs.getString("nom"),
	                rs.getString("email")
	            );
	            return new Emprunt(
	                rs.getInt("id"),
	                livre,
	                membre,
	                rs.getDate("date_emprunt").toLocalDate(),
	                rs.getDate("date_retour") != null ? rs.getDate("date_retour").toLocalDate() : null
	            );
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	@Override
	public List<Emprunt> getAllEmprunts() {
	    List<Emprunt> emprunts = new ArrayList<>();
	    String sql = "SELECT e.*, l.*, m.* "
	    		+ "FROM emprunts e "
	    		+ "JOIN livres l ON e.livre_id = l.id "
	    		+ "JOIN membres m ON e.membre_id = m.id";
	    try (Statement stmt = connection.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {
	        while (rs.next()) {
	            Livre livre = new Livre(
	                rs.getInt("livre_id"),
	                rs.getString("titre"),
	                rs.getString("auteur"),
	                rs.getString("isbn")
	            );
	            Membre membre = new Membre(
	                rs.getInt("membre_id"),
	                rs.getString("nom"),
	                rs.getString("email")
	            );
	            emprunts.add(new Emprunt(
	                rs.getInt("id"),
	                livre,
	                membre,
	                rs.getDate("date_emprunt").toLocalDate(),
	                rs.getDate("date_retour") != null ? rs.getDate("date_retour").toLocalDate() : null
	            ));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return emprunts;
	}

	@Override
	public void addEmprunt(Emprunt emprunt) {
	    String sql = "INSERT INTO emprunts (livre_id, membre_id, date_emprunt, date_retour) VALUES (?, ?, ?, ?)";
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setInt(1, emprunt.getLivre().getId());
	        pstmt.setInt(2, emprunt.getMembre().getId());
	        pstmt.setDate(3, java.sql.Date.valueOf(emprunt.getDateEmprunt()));
	        pstmt.setDate(4, emprunt.getDateRetour() != null ? 
	            java.sql.Date.valueOf(emprunt.getDateRetour()) : null);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	@Override
	public void updateEmprunt(Emprunt emprunt) {
	    String sql = "UPDATE emprunts SET livre_id = ?, membre_id = ?, date_emprunt = ?, date_retour = ? WHERE id = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setInt(1, emprunt.getLivre().getId());
	        pstmt.setInt(2, emprunt.getMembre().getId());
	        pstmt.setDate(3, java.sql.Date.valueOf(emprunt.getDateEmprunt()));
	        pstmt.setDate(4, emprunt.getDateRetour() != null ? 
	            java.sql.Date.valueOf(emprunt.getDateRetour()) : null);
	        pstmt.setInt(5, emprunt.getId());
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	@Override
	public void deleteEmprunt(int id) {
	    String sql = "DELETE FROM emprunts WHERE id = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setInt(1, id);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	
	
}
