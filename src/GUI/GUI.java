/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.concurrent.TimeUnit;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import org.gstreamer.ClockTime;
import org.gstreamer.Format;
import org.gstreamer.Gst;
import org.gstreamer.event.SeekEvent;
import org.gstreamer.SeekType;
import org.gstreamer.Segment;
import org.gstreamer.State;
import org.gstreamer.elements.PlayBin;
import org.gstreamer.elements.PlayBin2;
import org.gstreamer.swing.VideoComponent;

import Client.Pipeline;

public class GUI {

	static TimeUnit _scaleUnit = TimeUnit.SECONDS;
	static VideoComponent videoComponent;
	static JProgressBar progbar;
	static JPanel panel;
	static JLabel _lblDuration;
	static JLabel _lblSpeed;
	static JLabel _lblVolume;
	final JFrame frame = new JFrame("AVPlayer");
	static Pipeline pipes;

	//GUI constructor, initializes the GUI and calls the Pipeline class so that the GUI can access the Pipeline's functions
	public GUI() { 
		pipes = new Pipeline(); 
		initGUI();		
	}

	private void initGUI() { 
		startpoll(); //function to set the duration time of the video and manage the progress bar
		videoComponent=pipes.getVideoComponent(); //gets the videoComponent from the Pipeline and is adding it to the frame
		frame.getContentPane().add(videoComponent, BorderLayout.CENTER);
		frame.setPreferredSize(new Dimension(800, 480));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		

		progbar = new JProgressBar();
		final JMenuBar menuBar = new JMenuBar();
		final JMenu menu = new JMenu("File");

		menuBar.add(menu);
		JMenuItem menuItem = new JMenuItem("Open");
		menu.add(menuItem);
		frame.setJMenuBar(menuBar);
		
		//adding a menu to the frame for loading a local video
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				menuFunction();
			}
		});

		progbar.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent arg0) {
				progbarFunction(arg0); //jumping to a specific frame of the video by clicking on the progress bar
			}

			@Override
			public void mousePressed(MouseEvent me) {
			}

			@Override
			public void mouseReleased(MouseEvent me) {
			}

			@Override
			public void mouseEntered(MouseEvent me) {
			}

			@Override
			public void mouseExited(MouseEvent me) {
			}
		});

		JButton PlayButton;
		PlayButton = new JButton();
		PlayButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/play-circle-outline-16.png")));
		PlayButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				
				//setting the speed of video to normal
				ClockTime b;
				b = pipes.getTotalQueryDuration();
				long position = pipes.getQueryPosition(_scaleUnit);
				long d = b.convertTo(TimeUnit.SECONDS);
				pipes.setLongSeek(position, d, 1);
				_lblSpeed.setText("|| Speed "+"normal");
				_lblSpeed.setVisible(true);
				
				//plays the video
				pipes.changeState(State.PLAYING);
				
			}
		});

		JButton PauseButton;
		PauseButton = new JButton();
		PauseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Pause_Icon-16.png")));
		PauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				//paysing the video
				pipes.changeState(State.PAUSED);
			}
		});

		JButton ForwardButton;
		ForwardButton = new JButton();
		ForwardButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/591263-fast-forward-16.png")));
		ForwardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				//increasing the speed of the video
				forwardBtn();
			}
		});

		JButton RewindButton;
		RewindButton = new JButton();
		RewindButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/591239-rewind-16.png")));
		RewindButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				//decrease the speed of the video
				rewindFunction();
			}
		});

		JButton VolumeUp;
		VolumeUp = new JButton();
		VolumeUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/519649-153_VolumeUp-16.png")));
		VolumeUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				
				//increasing the volume
				String VolUp="up";
				int percentTmp;
				percentTmp = pipes.getVolumePercen(); 
				pipes.setVolumePercen(percentTmp, VolUp);
				
				//after increasing the volume, display the new value on the label
				int tmp = pipes.getVolumePercen();
				System.out.println("Vol increased for "+tmp);
				_lblVolume.setText("|| Vol:" + tmp);
				_lblVolume.setVisible(true);
			}
		});
		
		JButton VolumeDown;
		VolumeDown = new JButton();
		VolumeDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/ic_volume_down_48px-16.png")));
		VolumeDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				
				//decreasing the volume
				String VolDown="down";
				int percentTmp;
				percentTmp = pipes.getVolumePercen();
				pipes.setVolumePercen(percentTmp, VolDown);

				//after decreasing the volume, display the new value on the label
				int tmp = pipes.getVolumePercen();
				System.out.println("Vol decreased for "+tmp);
				_lblVolume.setText("|| Vol:" + tmp);
				_lblVolume.setVisible(true);
			}
		});

		JButton Mute;
		Mute = new JButton();
		Mute.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/black_1_speaker_off-16.png")));
		Mute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				//mute the sound
				String mute="mute";
				pipes.setVolumePercen(0, mute);
			}
		});

		JButton FullScreen;
		FullScreen = new JButton();
		FullScreen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/fullscreen-16.png")));
		FullScreen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				
				//once the button is pressed, extend the size of the frame to full screen, hide the panel and the menu bar
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				panel.setVisible(false);
				frame.setJMenuBar(null);
				
				//exit the full screen by clicking on the frame
				frame.addMouseListener(new MouseListener() {
					@Override
					public void mouseReleased(MouseEvent e) {
					}
					
					@Override
					public void mousePressed(MouseEvent e) {
						System.out.println("exit full screen");
						
						//set the new frame size and show again the panel and the menu bar
						panel.setVisible(true);
						frame.setJMenuBar(menuBar);
						frame.setExtendedState(JFrame.NORMAL);
						frame.setPreferredSize(new Dimension(800, 480));
					}
					
					@Override
					public void mouseExited(MouseEvent e) {						
					}
					
					@Override
					public void mouseEntered(MouseEvent e) {
					}
					
					@Override
					public void mouseClicked(MouseEvent e) {
					}
				});
			}
		});

		panel = new JPanel();
		panel.add(PlayButton);
		panel.add(PauseButton);
		panel.add(ForwardButton);
		panel.add(RewindButton);
		panel.add(VolumeUp);
		panel.add(VolumeDown);
		panel.add(Mute);
		panel.add(progbar);
		panel.add(FullScreen);
		panel.add(new JLabel("time:"));
		_lblDuration = new JLabel();
		panel.add(_lblDuration);
		_lblVolume = new JLabel();
		_lblSpeed = new JLabel();
		panel.add(_lblVolume);
		panel.add(_lblSpeed);
		
		
		frame.add(panel, BorderLayout.SOUTH);
		frame.setVisible(true);
		
	}

	private void menuFunction() {
		final JFileChooser fc = new JFileChooser();

		if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
			File f1 = fc.getSelectedFile();
			if (f1.exists() && fc.getSelectedFile().getPath().toString().endsWith(".mpg")) {
				pipes.setInputFile(f1);
				pipes.changeState(State.PLAYING);
			} else {
				JOptionPane.showInputDialog("Selected file type is not supported. Try .mpg or .avi file");
				pipes.changeState(State.NULL);
			}

		}
	}

	private void progbarFunction(MouseEvent arg0) {
		// Avoid divide by zero exception
		if (progbar.getWidth() == 0) {
			return;
		}

		Point mouse = arg0.getPoint();
		double perc = (double) mouse.x / (double) progbar.getWidth();
		long duration = pipes.getQueryDuration(_scaleUnit);

		// Seek to that percentage of the song
		pipes.setNewSeek(duration, perc, _scaleUnit);
		
	}

	private void rewindFunction() {
		pipes.changeState(State.PAUSED);

		ClockTime b = pipes.getTotalQueryDuration();
		long speed = b.convertTo(TimeUnit.MILLISECONDS);
		pipes.getQuerySegment();
		TimeUnit _scaleUnit = TimeUnit.SECONDS;
		long position = pipes.getQueryPosition(_scaleUnit);
		double tmp = 0.5;
		int dd = 0;
		pipes.setShortSeek(position, _scaleUnit);
		pipes.setLongSeek(position, dd, tmp);
		_lblSpeed.setText("|| Speed -"+speed);
		_lblSpeed.setVisible(true);
		pipes.changeState(State.PLAYING);
	}
	
	private void forwardBtn() {
		pipes.changeState(State.PAUSED);
		 
		ClockTime b;
		b = pipes.getTotalQueryDuration();
		long speed = b.convertTo(TimeUnit.SECONDS);
		Segment s = pipes.getQuerySegment();  
		_lblSpeed.setText("|| Speed +"+speed);
		_lblSpeed.setVisible(true);
		long position = pipes.getQueryPosition(_scaleUnit);
		pipes.setShortSeek(position, _scaleUnit);
		double tmp = 2.0;
		pipes.setLongSeek(position, speed, tmp);
		
		pipes.changeState(State.PLAYING);

	}

	private static void startpoll() {
		Thread task = new Thread() {
			public void run() {
				while (true) {
					try {
						long position = pipes.getQueryPosition(_scaleUnit);
						long duration = pipes.getQueryDuration(_scaleUnit);

						progbar.setMaximum(100);
						progbar.setMinimum(0);
						progbar.setValue((int) ((position * 100) / duration));

						int intCurMin = (int) (position / 60);
						String curMin = null;

						if (intCurMin < 10) {
							curMin = "0" + intCurMin;
						} else {
							curMin = "" + intCurMin;
						}

						int intCurSec = (int) (position - (60 * intCurMin));

						String curSec = null;
						if (intCurSec < 10) {
							curSec = "0" + intCurSec;
						} else {
							curSec = "" + intCurSec;
						}

						int intMaxMin = (int) (duration / 60);

						String maxMin = null;
						if (intMaxMin < 10) {
							maxMin = "0" + intMaxMin;
						} else {
							maxMin = "" + intMaxMin;
						}

						int intMaxSec = (int) (duration - (60 * intMaxMin));

						String maxSec = null;
						if (intMaxSec < 10) {
							maxSec = "0" + intMaxSec;
						} else {
							maxSec = "" + intMaxSec;
						}

						_lblDuration.setText(curMin + ":" + curSec + "/" + maxMin + ":" + maxSec);
						_lblDuration.setVisible(true);

					} catch (Exception e) {
					}

					try {
						Thread.sleep(1000);
					} catch (Exception e) {
					}
				}
			}
		};

		task.start();
	}

	public void setVolText(int i) {

	}

}
