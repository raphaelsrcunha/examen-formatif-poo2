package rsc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import db.IDataAccess;
import model.Livre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import controller.BibliothequeService;

class BibliothequeServiceTest {
    
    private BibliothequeService bibliothequeService;
    private IDataAccess dataAccess;

    @BeforeEach
    void setUp() {
        dataAccess = mock(IDataAccess.class);
        bibliothequeService = new BibliothequeService(dataAccess);
    }

    @Test
    void testAjouterLivre_Success() {
        bibliothequeService.ajouterLivre("Le Petit Prince", "Antoine de Saint-Exupéry", "1234567890123");
        verify(dataAccess, times(1)).addLivre(any(Livre.class));
    }

    @Test
    void testAjouterLivre_InvalidTitre() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bibliothequeService.ajouterLivre("", "Antoine de Saint-Exupéry", "1234567890123");
        });
        assertEquals("Le titre ne peut pas être vide", exception.getMessage());
    }

    @Test
    void testModifierLivre_Success() {
        Livre livre = new Livre(1, "Original", "Auteur", "1234567890123");
        when(dataAccess.getLivre(1)).thenReturn(livre);

        bibliothequeService.modifierLivre(1, "Nouveau Titre", "Nouvel Auteur", "3210987654321");

        verify(dataAccess, times(1)).updateLivre(any(Livre.class));
        assertEquals("Nouveau Titre", livre.getTitre());
        assertEquals("Nouvel Auteur", livre.getAuteur());
        assertEquals("3210987654321", livre.getIsbn());
    }

    @Test
    void testModifierLivre_LivreNonTrouve() {
        when(dataAccess.getLivre(1)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bibliothequeService.modifierLivre(1, "Nouveau Titre", "Nouvel Auteur", "3210987654321");
        });
        assertEquals("Livre non trouvé", exception.getMessage());
    }
}
