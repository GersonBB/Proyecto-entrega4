import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class LoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private Timer welcomeTimer;

    public LoginGUI() {
        super("Programa de Citas Médicas");

        // Configurar colores
        getContentPane().setBackground(new Color(144, 202, 249)); // Color azul claro

        // Pantalla de bienvenida
        JOptionPane.showMessageDialog(LoginGUI.this, "Bienvenido al Programa de Citas Médicas", "Bienvenida",
                JOptionPane.INFORMATION_MESSAGE);

        // Crear componentes
        JButton loginButton = new JButton("Iniciar sesión");
        JButton registerButton = new JButton("Registrarse");

        // Agregar componentes al panel
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(loginButton);
        panel.add(registerButton);

        // Configurar el botón de inicio de sesión
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                promptLogin();
            }
        });

        // Configurar el botón de registro
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                promptRegister();
            }
        });

        // Configurar la ventana
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null); // Centrar la ventana
        setVisible(true);
    }

    private void promptLogin() {
        // Crear componentes para el inicio de sesión
        JLabel usernameLabel = new JLabel("Usuario:");
        JLabel passwordLabel = new JLabel("Contraseña:");
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        JButton confirmButton = new JButton("Confirmar");
        JButton backButton = new JButton("Atrás");

        // Agregar componentes al panel
        JPanel loginPanel = new JPanel(new GridLayout(4, 2));
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(new JLabel()); // Espacio en blanco
        loginPanel.add(confirmButton);
        loginPanel.add(new JLabel()); // Espacio en blanco
        loginPanel.add(backButton);

        // Configurar el botón de confirmar
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);

                if (login(username, password)) {
                    JOptionPane.showMessageDialog(LoginGUI.this, "¡Bienvenido al programa de citas médicas!");
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(LoginGUI.this, "Inicio de sesión fallido. Verifica tus credenciales.",
                            "Error de inicio de sesión", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Configurar el botón de atrás
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        // Mostrar el panel de inicio de sesión
        int result = JOptionPane.showOptionDialog(null, loginPanel, "Iniciar Sesión", JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, new Object[] {}, null);
        if (result == JOptionPane.CLOSED_OPTION) {
            // Si cierran la ventana, cerrar la aplicación
            System.exit(0);
        }
    }

    private void promptRegister() {
        // Crear componentes para el registro
        JLabel usernameLabel = new JLabel("Usuario:");
        JLabel passwordLabel = new JLabel("Contraseña:");
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        JButton confirmButton = new JButton("Confirmar");
        JButton backButton = new JButton("Atrás");

        // Agregar componentes al panel
        JPanel registerPanel = new JPanel(new GridLayout(4, 2));
        registerPanel.add(usernameLabel);
        registerPanel.add(usernameField);
        registerPanel.add(passwordLabel);
        registerPanel.add(passwordField);
        registerPanel.add(new JLabel()); // Espacio en blanco
        registerPanel.add(confirmButton);
        registerPanel.add(new JLabel()); // Espacio en blanco
        registerPanel.add(backButton);

        // Configurar el botón de confirmar
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);

                if (register(username, password)) {
                    JOptionPane.showMessageDialog(LoginGUI.this, "Registro exitoso. ¡Ahora puedes iniciar sesión!");
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(LoginGUI.this, "Error al registrar el usuario. Inténtalo de nuevo.",
                            "Error de registro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Configurar el botón de atrás
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        // Mostrar el panel de registro
        int result = JOptionPane.showOptionDialog(null, registerPanel, "Registrarse", JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, new Object[] {}, null);
        if (result == JOptionPane.CLOSED_OPTION) {
            // Si cierran la ventana, cerrar la aplicación
            System.exit(0);
        }
    }

    private boolean login(String username, String password) {
        // Verificar las credenciales en el archivo CSV
        try (BufferedReader br = new BufferedReader(new FileReader("usuarios.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userData = line.split(",");
                String storedUsername = userData[0];
                String storedPassword = userData[1];

                if (username.equals(storedUsername) && password.equals(storedPassword)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean register(String username, String password) {
        // Verificar si el usuario ya existe
        if (userExists(username)) {
            JOptionPane.showMessageDialog(LoginGUI.this,
                    "El usuario ya existe. Por favor, elige otro nombre de usuario.", "Error de registro",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Guardar las credenciales en el archivo CSV
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("usuarios.csv", true))) {
            bw.write(username + "," + password);
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean userExists(String username) {
        // Verificar si el usuario ya existe en el archivo CSV
        try (BufferedReader br = new BufferedReader(new FileReader("usuarios.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userData = line.split(",");
                String storedUsername = userData[0];

                if (username.equals(storedUsername)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void clearFields() {
        // Limpiar los campos de usuario y contraseña
        usernameField.setText("");
        passwordField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginGUI();
            }
        });
    }
}
