package gcodeCompressor;

import java.io.*;
import java.util.*;
import javax.swing.*;

public class GCodeCompressor {
	private final static String VERSION = "1.0.1";

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				System.out.println("Started, version: " + VERSION);

				JFileChooser inputDialog = new JFileChooser();
				if(inputDialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File inputFile = inputDialog.getSelectedFile();

					JFileChooser outputDialog = new JFileChooser();
					outputDialog.setSelectedFile(new File(inputFile.getParentFile(), "ZIP_" + inputFile.getName()));
					if(outputDialog.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
						File outputFile = outputDialog.getSelectedFile();

						System.out.println("Reading: " + inputFile.getAbsolutePath());
						System.out.println("Writing: " + outputFile.getAbsolutePath());

						Scanner reader = null;
						PrintWriter writer = null;
						long inputLen = 0, outputLen = 0;
						boolean exception = false;

						try {
							inputLen = inputFile.length();
						} catch(Exception e) {
							System.err.println("Exception: " + e);
							e.printStackTrace();
							exception = true;
						}

						System.out.println("Input length: " + inputLen);

						try {
							reader = new Scanner(inputFile);
							writer = new PrintWriter(outputFile);

							while(reader.hasNextLine()) {
								String line = reader.nextLine().trim();
								if(line.startsWith("N") || line.startsWith("n")) {
									int ind = 1;
									for( ; ind < line.length(); ind++) {
										if(!Character.isDigit(line.charAt(ind))) break;
									}
									line = line.substring(ind).trim();
								}
								if(line.length() > 0) writer.println(line);
							}
						} catch(Exception e) {
							System.err.println("Exception: " + e);
							e.printStackTrace();
							exception = true;
						}

						try {
							reader.close();
						} catch(Exception e) {
							System.err.println("Exception: " + e);
							e.printStackTrace();
							exception = true;
						}

						try {
							writer.close();
						} catch(Exception e) {
							System.err.println("Exception: " + e);
							e.printStackTrace();
							exception = true;
						}

						try {
							outputLen = outputFile.length();
						} catch(Exception e) {
							System.err.println("Exception: " + e);
							e.printStackTrace();
							exception = true;
						}

						System.out.println("Output length: " + outputLen);

						if(exception) JOptionPane.showMessageDialog(null, "WARNING\nEXCEPTION DURING PROCESSING");
						else if(inputLen > 0 && outputLen > 0) {
							long diff = inputLen - outputLen;
							double percent = 100.0 * diff / inputLen;
							System.out.println("Removed chars: " + diff + " (" + String.format("%.2f", percent) + "%)");

							JOptionPane.showMessageDialog(null, "Input length: " + inputLen + "\nOutput length: " + outputLen + "\nRemoved chars: " + diff + " (" + String.format("%.2f", percent) + "%)");
						}
						else JOptionPane.showMessageDialog(null, "WARNING\nInput length: " + inputLen + "\nOutput length: " + outputLen);
					}
				}

				System.out.println("Done");
				System.exit(0);
			}
		});
	}
}
