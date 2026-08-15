package View.Componentes;

import java.awt.event.*;

/**
 * Adaptador para convertir clics del panel en ActionListener.
 */
public class MouseAdapterMenu extends MouseAdapter {

    private final ActionListener listener;

    public MouseAdapterMenu(ActionListener listener) {

        this.listener = listener;

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        listener.actionPerformed(
                new java.awt.event.ActionEvent(
                        e.getSource(),
                        ActionEvent.ACTION_PERFORMED,
                        "click"));

    }

}