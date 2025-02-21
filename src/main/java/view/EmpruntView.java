package view;

import java.util.List;

import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import controller.BibliothequeService;
import model.Emprunt;
import model.Livre;
import model.Membre;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import com.toedter.calendar.JDateChooser;
import java.util.Date;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EmpruntView extends JPanel {

	private static final long serialVersionUID = 1L;

	private DefaultTableModel tableModel;

	private BibliothequeService service;
	private JTable tableEmprunts;
	private JTextField textField;
	private JComboBox comboBoxLivre;
	private JComboBox comboBoxMembre;

	public EmpruntView() {
		setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(29, 230, 572, 163);
		add(scrollPane);
		
		tableEmprunts = new JTable();
		scrollPane.setViewportView(tableEmprunts);
		service = new BibliothequeService();

		tableModel = createTableLivresModel();
		tableEmprunts.setModel(tableModel);
		
		JLabel lblNewLabel = new JLabel("Id");
		lblNewLabel.setBounds(29, 38, 46, 14);
		add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(29, 56, 116, 20);
		add(textField);
		textField.setColumns(10);
		
		JLabel lblLivre = new JLabel("Livre");
		lblLivre.setBounds(29, 87, 46, 14);
		add(lblLivre);
		
		comboBoxLivre = new JComboBox();
		comboBoxLivre.setBounds(29, 105, 116, 22);
		add(comboBoxLivre);
		
		comboBoxMembre = new JComboBox();
		comboBoxMembre.setBounds(29, 156, 116, 22);
		add(comboBoxMembre);
		
		JLabel lblMembre = new JLabel("Membre");
		lblMembre.setBounds(29, 138, 46, 14);
		add(lblMembre);
		
		JButton btnAjouter = new JButton("Ajouter");
		btnAjouter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int response = javax.swing.JOptionPane.showConfirmDialog(EmpruntView.this, "Voulez-vous ajouter cet emprunt?", "Confirmation", javax.swing.JOptionPane
				.YES_NO_OPTION);
				if (response == JOptionPane.YES_OPTION) {
                    empruntLivre();
					clearAllFields();
					fillTableEmprunts();
                }
			}
		});
		btnAjouter.setBounds(155, 55, 89, 23);
		add(btnAjouter);

        fillTableEmprunts();
		getAllLivres();
		getAllMembres();
	}

	private void empruntLivre() {
		try {
			int livreId = (int) comboBoxLivre.getSelectedItem().hashCode();
            int membreId = (int) comboBoxMembre.getSelectedItem().hashCode();
            service.emprunterLivre(livreId, membreId);
            fillTableEmprunts();
		} catch (IllegalArgumentException e) {
			javax.swing.JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			javax.swing.JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}

	private void clearAllFields() {
		textField.setText("");
		comboBoxLivre.setSelectedIndex(0);
		comboBoxMembre.setSelectedIndex(0);
	}

	private void getAllLivres() {
		List<Livre> livres = service.getAllLivres();
		for(Livre livre : livres) {
			comboBoxLivre.addItem(livre);
		}
	}

	private void getAllMembres() {
		List<Membre> membres = service.getAllMembres();
		for(Membre membre : membres) {
			comboBoxMembre.addItem(membre);
		}
    }
	private void fillTableEmprunts() {
		tableModel.setRowCount(0);
		List<Emprunt> emprunts = service.getAllEmprunts();
		
        for(Emprunt emprunt : emprunts) {
			tableModel.addRow(new Object[] {
					emprunt.getId(),
					emprunt.getLivre().getId(),
					emprunt.getMembre().getId(),
					emprunt.getDateEmprunt(),
					emprunt.getDateRetour()
			});
		}
		
	}
	
	private DefaultTableModel createTableLivresModel() {
		tableModel = new DefaultTableModel();
		
		tableModel.addColumn("Id");
		tableModel.addColumn("Livre ID");
		tableModel.addColumn("Membre ID");
		tableModel.addColumn("Date Emprunt");
		tableModel.addColumn("Date Retour");
		
		return tableModel;
	}


}
