package rsc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Livre;

class LivreTest {

	private Livre livre;

    @BeforeEach
    void setUp() {
        livre = new Livre(1, "Le Petit Prince", "Antoine de Saint-Exupéry", "978-3-16-148410-0");
    }

    @Test
    void testGetId() {
        assertEquals(1, livre.getId());
    }

    @Test
    void testSetId() {
        livre.setId(2);
        assertEquals(2, livre.getId());
    }

    @Test
    void testGetTitre() {
        assertEquals("Le Petit Prince", livre.getTitre());
    }

    @Test
    void testSetTitre() {
        livre.setTitre("L'Étranger");
        assertEquals("L'Étranger", livre.getTitre());
    }

    @Test
    void testGetAuteur() {
        assertEquals("Antoine de Saint-Exupéry", livre.getAuteur());
    }

    @Test
    void testSetAuteur() {
        livre.setAuteur("Albert Camus");
        assertEquals("Albert Camus", livre.getAuteur());
    }

    @Test
    void testGetIsbn() {
        assertEquals("978-3-16-148410-0", livre.getIsbn());
    }

    @Test
    void testSetIsbn() {
        livre.setIsbn("978-2-07-036822-8");
        assertEquals("978-2-07-036822-8", livre.getIsbn());
    }

    @Test
    void testToString() {
        assertEquals("Le Petit Prince", livre.toString());
    }

}
