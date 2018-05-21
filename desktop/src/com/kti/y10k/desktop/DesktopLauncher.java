package com.kti.y10k.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kti.y10k.MainLoop;
import com.kti.y10k.utilities.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DesktopLauncher extends JFrame implements ActionListener {

	private static final int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private static final int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	private static final String[] resolutions = {"1920x1080", "1280x720", "800x640", "480x360", "320x200"};
	private static final String version = "0.03 - \"Juno\"";

	private JComboBox resolutionSelector;
	private JCheckBox fullscreenBox;
	private JCheckBox vsyncBox;
	private JButton launchButton;

	public DesktopLauncher() {
		super("y10k " + version);

		resolutionSelector = new JComboBox();
		resolutionSelector.addItem(width + "x" + height);

		for (int i = 0; i < resolutions.length; i++) {
			if (!resolutions[i].equals(width + "x" + height))
				resolutionSelector.addItem(resolutions[i]);
		}

		resolutionSelector.addActionListener(this);
		resolutionSelector.setVisible(true);

		fullscreenBox = new JCheckBox();
		fullscreenBox.setSelected(true);
		vsyncBox = new JCheckBox();

		launchButton = new JButton("Ad Astra");
		launchButton.setActionCommand("Launch");
		launchButton.addActionListener(this);

		getContentPane().setLayout(new FlowLayout());
		getContentPane().add(new JLabel("Resolution: "));
		getContentPane().add(resolutionSelector);
		getContentPane().add(new JLabel("Fullscreen: "));
		getContentPane().add(fullscreenBox);
		getContentPane().add(new JLabel("Vsync: "));
		getContentPane().add(vsyncBox);
		getContentPane().add(launchButton);

		setIconImage(new ImageIcon("assets/img/ico.png").getImage());

		setSize(230, 130);
		setLocation(width / 2 - 110, height / 2 - 60);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public static void main (String[] arg) {
		Logger.log(Logger.LogLevel.INFO, "Starting launcher...");
		DesktopLauncher d = new DesktopLauncher();
	}

	public void launch() {
		Logger.log(Logger.LogLevel.INFO, "Version: " + version);
		Logger.log(Logger.LogLevel.INFO, "Starting on: " + System.getProperty("os.name"));

		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "y10k";

		String[] res = ((String)resolutionSelector.getSelectedItem()).split("x");
		config.width = Integer.parseInt(res[0]);
		config.height = Integer.parseInt(res[1]);

		config.fullscreen = fullscreenBox.isSelected();
		if (!fullscreenBox.isSelected())
			config.resizable = false;
		config.vSyncEnabled = vsyncBox.isSelected();
		config.backgroundFPS = 0;
		config.foregroundFPS = 0;
		if (System.getProperty("os.name").contains("Linux") || System.getProperty("os.name").contains("Win"))
			config.addIcon("assets/img/ico.png", Files.FileType.Absolute);

		new LwjglApplication(new MainLoop(), config);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Launch"))
			launch();
	}
}
