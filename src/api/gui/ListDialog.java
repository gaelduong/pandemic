package api.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ListDialog {
    private JList<String> list;
    private JLabel label;
    private JOptionPane optionPane;
    private JButton okButton, cancelButton;
    private ActionListener okEvent, cancelEvent;
    private JDialog dialog;

    private JList<String> results;

    public ListDialog(String message, JList<String> listToDisplay){
        list = listToDisplay;
        label = new JLabel(message);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        createAndDisplayOptionPane();
    }

    public ListDialog(String title, String message, JList<String> listToDisplay){
        this(message, listToDisplay);
        dialog.setTitle(title);
    }

    private void createAndDisplayOptionPane(){
        setupButtons();
        JPanel pane = layoutComponents();
        optionPane = new JOptionPane(pane);
        optionPane.setOptions(new Object[]{okButton, cancelButton});
        dialog = optionPane.createDialog("Select option");
    }

    private void setupButtons(){
        okButton = new JButton("Ok");
        okButton.addActionListener(e -> handleOkButtonClick(e));

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> handleCancelButtonClick(e));
    }

    private JPanel layoutComponents(){
        centerListElements();
        JPanel panel = new JPanel(new BorderLayout(5,5));
        panel.add(label, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(0,500));
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void centerListElements(){
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) list.getCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public void setOnOk(ActionListener event){ okEvent = event; }

    public void setOnClose(ActionListener event){
        cancelEvent  = event;
    }

    private void handleOkButtonClick(ActionEvent e){
        if(okEvent != null){ okEvent.actionPerformed(e); }
        hide();
    }

    private void handleCancelButtonClick(ActionEvent e){
        if(cancelEvent != null){ cancelEvent.actionPerformed(e);}
        hide();
    }

    public void show(){ dialog.setVisible(true); }

    private void hide(){ dialog.setVisible(false); }

    public java.util.List<String> getSelectedItems(){ return list.getSelectedValuesList(); }
}