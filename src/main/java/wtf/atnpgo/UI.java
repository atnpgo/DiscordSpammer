package wtf.atnpgo;

import javax.swing.*;
import java.awt.*;

class UI extends JFrame {

    UI() {

        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel pnlTop = new JPanel();
        pnlTop.setLayout(new GridLayout(0, 2));


        pnlTop.add(new JLabel("WebHook URL:", SwingConstants.RIGHT));
        JTextField jtfWebHook = new JTextField();
        pnlTop.add(jtfWebHook);

        pnlTop.add(new JLabel("Delay in minutes:", SwingConstants.RIGHT));
        JSpinner spDelay = new JSpinner(new SpinnerNumberModel(10, 1, 360, 1));
        pnlTop.add(spDelay);

        pnlTop.add(new JLabel("Name:", SwingConstants.RIGHT));
        JTextField jtfName = new JTextField();
        jtfName.setText("Spammer");
        pnlTop.add(jtfName);

        pnlTop.add(new JLabel("Avatar URL:", SwingConstants.RIGHT));
        JTextField jtfAvatar = new JTextField();
        pnlTop.add(jtfAvatar);

        this.add(pnlTop, BorderLayout.NORTH);

        JTextArea jtaMessage = new JTextArea();
        jtaMessage.setColumns(60);
        jtaMessage.setRows(20);

        JScrollPane jsp = new JScrollPane(jtaMessage);
        jsp.setBorder(BorderFactory.createTitledBorder("Message"));
        this.add(jsp, BorderLayout.CENTER);

        JButton jbtnStart = new JButton("Start");
        jbtnStart.addActionListener(e -> {
            jbtnStart.setEnabled(false);
            Pusher p = new Pusher(
                    jtfWebHook.getText(),
                    jtaMessage.getText(),
                    ((int) spDelay.getValue()) * 60L * 1000L,
                    jtfName.getText(),
                    jtfAvatar.getText()
            );
            p.start();
        });
        this.add(jbtnStart, BorderLayout.SOUTH);

        this.setTitle("Spammer");
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
    }

}
