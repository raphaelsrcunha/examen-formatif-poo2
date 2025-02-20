package view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import controller.BibliothequeService;
import model.Livre;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LivreView extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable tableLivres;
	private JTextField textFieldId;
	private JTextField textFieldTitre;
	private JTextField textFieldAuteur;
	private JTextField textFieldIsbn;
	
	private DefaultTableModel tableModel;
	
	BibliothequeService service;

	public LivreView() {
		
		service =  new BibliothequeService();
		
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Livre");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(250, 11, 46, 14);
		add(lblNewLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(33, 239, 560, 166);
		add(scrollPane);
		
		tableModel = createTableLivresModel();
		
		tableLivres = new JTable(tableModel);
		tableLivres.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					int row = tableLivres.getSelectedRow();
					if (row != -1) {
						int id = (int) tableLivres.getValueAt(row, 0);
						Livre livre = service.getLivreById(id);
						textFieldId.setText(String.valueOf(livre.getId()));
						textFieldTitre.setText(livre.getTitre());
						textFieldAuteur.setText(livre.getAuteur());
						textFieldIsbn.setText(livre.getIsbn());
					}
				}
			}
		});

		scrollPane.setViewportView(tableLivres);
		
		JLabel lblId = new JLabel("Id");
		lblId.setBounds(33, 36, 46, 14);
		add(lblId);
		
		textFieldId = new JTextField();
		textFieldId.setBounds(34, 54, 136, 20);
		add(textFieldId);
		textFieldId.setColumns(10);
		
		textFieldTitre = new JTextField();
		textFieldTitre.setColumns(10);
		textFieldTitre.setBounds(34, 103, 136, 20);
		add(textFieldTitre);
		
		JLabel lblTitre = new JLabel("Titre");
		lblTitre.setBounds(33, 85, 46, 14);
		add(lblTitre);
		
		textFieldAuteur = new JTextField();
		textFieldAuteur.setColumns(10);
		textFieldAuteur.setBounds(34, 152, 136, 20);
		add(textFieldAuteur);
		
		JLabel lblAuteur = new JLabel("Auteur");
		lblAuteur.setBounds(33, 134, 46, 14);
		add(lblAuteur);
		
		textFieldIsbn = new JTextField();
		textFieldIsbn.setColumns(10);
		textFieldIsbn.setBounds(34, 201, 136, 20);
		add(textFieldIsbn);
		
		JLabel lblIsbn = new JLabel("ISBN");
		lblIsbn.setBounds(33, 183, 46, 14);
		add(lblIsbn);
		
		JButton btnAjouter = new JButton("Ajouter");
		btnAjouter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int response = javax.swing.JOptionPane.showConfirmDialog(
					null,
					"Voulez-vous vraiment ajouter ce livre?",
					"Confirmation",
					javax.swing.JOptionPane.YES_NO_OPTION,
					javax.swing.JOptionPane.QUESTION_MESSAGE
				);
				
				if (response == javax.swing.JOptionPane.YES_OPTION) {
					if(createNewBook()) {
						fillTableLivres();
						clearAllFields();
					}
					
				}
			}
		});
		btnAjouter.setBounds(185, 53, 127, 23);
		add(btnAjouter);
		
		JButton btnSupprimer = new JButton("Supprimer");
		btnSupprimer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int response = javax.swing.JOptionPane.showConfirmDialog(
					null,
					"Voulez-vous vraiment supprimer ce livre?",
					"Confirmation",
					javax.swing.JOptionPane.YES_NO_OPTION,
					javax.swing.JOptionPane.QUESTION_MESSAGE
				);
				
				if (response == javax.swing.JOptionPane.YES_OPTION) {
					if(deleteBook()) {
						fillTableLivres();
						clearAllFields();
					}
				}
			}
		});
		btnSupprimer.setBounds(185, 102, 127, 23);
		add(btnSupprimer);
		
		JButton btnMettreAJour = new JButton("Mettre à Jour");
		btnMettreAJour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int response = javax.swing.JOptionPane.showConfirmDialog(
					null,
					"Voulez-vous vraiment mettre à jour ce livre?",
					"Confirmation",
					javax.swing.JOptionPane.YES_NO_OPTION,
					javax.swing.JOptionPane.QUESTION_MESSAGE
				);
				
                if (response == javax.swing.JOptionPane.YES_OPTION) {
					if(updateBook()) {
						fillTableLivres();
						clearAllFields();
					}
			}
		}
	});
		
		btnMettreAJour.setBounds(185, 151, 127, 23);
		add(btnMettreAJour);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearAllFields();
				fillTableLivres();
			}
		});
		btnRefresh.setBounds(185, 200, 127, 23);
		add(btnRefresh);
		
		fillTableLivres();

	}
	
	private void clearAllFields() {
		textFieldId.setText("");
		textFieldTitre.setText("");
		textFieldAuteur.setText("");
		textFieldIsbn.setText("");
	}
	
	private void fillTableLivres() {
		tableModel.setRowCount(0);
		List<Livre> livres = service.getAllLivres();
		
		for(Livre livre : livres) {
			tableModel.addRow(new Object[] {
					livre.getId(),
					livre.getTitre(),
					livre.getAuteur(),
					livre.getIsbn()
			});
		}
		
	}
	
	private DefaultTableModel createTableLivresModel() {
		tableModel = new DefaultTableModel();
		
		tableModel.addColumn("Id");
		tableModel.addColumn("Titre");
		tableModel.addColumn("Auteur");
		tableModel.addColumn("ISBN");
		
		return tableModel;
	}
	
	private boolean createNewBook() {
		try {
			service.ajouterLivre(textFieldTitre.getText(), textFieldAuteur.getText(), textFieldIsbn.getText());
			javax.swing.JOptionPane.showMessageDialog(
				this,
				"Livre ajouté avec succès!",
				"Succès",
				javax.swing.JOptionPane.INFORMATION_MESSAGE
			);
			return true;
		} catch (IllegalArgumentException e) {
			javax.swing.JOptionPane.showMessageDialog(
				this,
				e.getMessage(),
				"Erreur de validation",
				javax.swing.JOptionPane.ERROR_MESSAGE
			);
		} catch (Exception e) {
			javax.swing.JOptionPane.showMessageDialog(
				this,
				"Une erreur s'est produite lors de l'ajout du livre",
				"Erreur",
				javax.swing.JOptionPane.ERROR_MESSAGE
			);
		}
		return false;
	}

	private boolean deleteBook() {
		try {
			int id = Integer.parseInt(textFieldId.getText());
			service.supprimerLivre(id);
			javax.swing.JOptionPane.showMessageDialog(
				this,
				"Livre supprimé avec succès!",
				"Succès",
				javax.swing.JOptionPane.INFORMATION_MESSAGE
			);
			return true;
		} catch (IllegalArgumentException e) {
			javax.swing.JOptionPane.showMessageDialog(
				this,
				e.getMessage(),
				"Erreur de validation",
				javax.swing.JOptionPane.ERROR_MESSAGE
			);
		} catch (Exception e) {
			javax.swing.JOptionPane.showMessageDialog(
				this,
				"Une erreur s'est produite lors de la suppression du livre",
				"Erreur",
				javax.swing.JOptionPane.ERROR_MESSAGE
			);
		}
		return false;
	}

	private boolean updateBook() {
		try {
			int id = Integer.parseInt(textFieldId.getText());
			service.modifierLivre(id, textFieldTitre.getText(), textFieldAuteur.getText(), textFieldIsbn.getText());
			javax.swing.JOptionPane.showMessageDialog(
				this,
				"Livre modifié avec succès!",
				"Succès",
				javax.swing.JOptionPane.INFORMATION_MESSAGE
			);
			return true;
		} catch (IllegalArgumentException e) {
			javax.swing.JOptionPane.showMessageDialog(
				this,
				e.getMessage(),
				"Erreur de validation",
				javax.swing.JOptionPane.ERROR_MESSAGE
			);
		} catch (Exception e) {
			javax.swing.JOptionPane.showMessageDialog(
				this,
				"Une erreur s'est produite lors de la modification du livre",
				"Erreur",
				javax.swing.JOptionPane.ERROR_MESSAGE
			);
		}
		return false;
	}
}
