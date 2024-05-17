import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Playlist implements Playable {

	private String name;
	private int numberOfRecordings = 0;
	private int durationInSeconds = 0;
	private final int MAX_PLAYLIST_SIZE; // need to be constant
	private ArrayList<Recording> recordingList;

	// Non-parametrized constructor
	Playlist() {

		this.name = "Unknown";
		this.MAX_PLAYLIST_SIZE = 5;
		this.recordingList = new ArrayList<Recording>();

	}

	// Parametrized constructor
	Playlist(String name, int MAX_PLAYLIST_SIZE) {

		// Check if arguments are valid...
		if (name != null && MAX_PLAYLIST_SIZE > 0) {

			// ...if yes, use them to set attribute values
			this.name = name;
			this.MAX_PLAYLIST_SIZE = MAX_PLAYLIST_SIZE;
			this.recordingList = new ArrayList<Recording>();

		} else {

			// ...if not, fall back on non-parametrized constructor behavior
			this.name = "Unknown";
			this.MAX_PLAYLIST_SIZE = 5;
			this.recordingList = new ArrayList<Recording>();

		}
	}

// Setters
	public void setName(String name) {
		if (name != null) {
			this.name = name;
		}
	}

// Getters
	public int getNumberOfRecordings() {
		return this.numberOfRecordings;
	}

	public String getName() {
		return this.name;
	}

	public int getDuration() {
		return this.durationInSeconds;
	}

	public ArrayList<Recording> getRecordingList() {
		return recordingList;
	}

	public void setRecordingList(ArrayList<Recording> recordingList) {
		this.recordingList = recordingList;
	}

	// A method that adds a new recording
	public boolean add(Recording newRecording) {

		// Make sure we can add this recording first (not null and enough space)
		if (newRecording != null && numberOfRecordings < MAX_PLAYLIST_SIZE) {

			// ...if we can, add it...
			this.recordingList.add(newRecording);

			// ...increment the number of recordings...
			this.numberOfRecordings++;

			// ...and add its duration to total playlist duration
			this.durationInSeconds = this.durationInSeconds + newRecording.getDuration();
			// everything worked - return false

			return true;

		} else {

			// ...if we cannot - return false
			return false;
		}
	}

	// play method
	public void play() {

		// Check if the playlist is empty...
		if (this.numberOfRecordings > 0) {
			// ...if not, iterate over all array objects ...
			for (int index = 0; index < this.numberOfRecordings; index++) {
				// ... and invoke their play method
				recordingList.get(index).play();
			}
			System.out.println();
		} else {

			// ...if empty, display this error message
			System.out.println("ERROR: Empty playlist.");
		}
	}

	public void play(int index) {

		// Check if the playlist is empty...
		if (this.numberOfRecordings > 0) {
			// ...if not, get index and play ..
				recordingList.get(index).play();
			System.out.println();
		} else {

			// ...if empty, display this error message
			System.out.println("ERROR: Empty playlist.");
		}
	}
	
	// shuffle method
	public void shuffle(int numberOfRecordingsToPlay) {

		// Check if the playlist is empty...
		if (this.numberOfRecordings > 0 && numberOfRecordingsToPlay > 0) {

			// Set up "already played" counter
			int recordingsPlayedCounter = 0;

			// Set up the random number generator object
			Random randomNumberGenerator = new Random();

			// ...if not, randomly choose numberOfRecordings...
			while (recordingsPlayedCounter < numberOfRecordingsToPlay) {

				// ... and invoke their play method
				recordingList.get(randomNumberGenerator.nextInt(this.numberOfRecordings)).play();
				recordingsPlayedCounter++;
			}
			System.out.println();
		} else {

			// ...if empty, display this error message
			System.out.println("ERROR: Empty playlist.");

		}
	}

	public void load(String fileName) {

		if (fileName != null) {
			try {
				File playlistFile = new File(fileName);

				Scanner fileScanner = new Scanner(playlistFile);

				System.out.println("Processing playlist file " + playlistFile + ":");

				while (fileScanner.hasNextLine()) {

					String line = fileScanner.nextLine();

					if (line != null) {
						String[] lineAsArray = line.split(",");

						if (lineAsArray != null && lineAsArray.length == 5) {
							if (lineAsArray[0].equals("A") || lineAsArray[0].equals("V")) {
								String name = lineAsArray[1];
								String artist = lineAsArray[2];
								try {
									int durationInSeconds = Integer.parseInt(lineAsArray[3]);
									double rate = Double.parseDouble(lineAsArray[4]);

									if (lineAsArray[0].equals("A")) {
										AudioRecording newRecording = new AudioRecording(artist, name,
												durationInSeconds, rate);
										this.add(newRecording);
									}
									if (lineAsArray[0].equals("V")) {
										VideoRecording newRecording = new VideoRecording(artist, name,
												durationInSeconds, rate);
										this.add(newRecording);
									}

								} catch (NumberFormatException nfe) {
									System.out.println(
											"ERROR: Number format exception. Recording rejected (" + line + ").");
								}
							} else {
								System.out.println("ERROR: Unknown recording type data (" + line + ").");
							}

						} else {
							System.out.println("ERROR: Inconsistent or no data read (" + line + ").");
						}
					} else {
						System.out.println("ERROR: Empty line read from a file");
					}
				}
				fileScanner.close();
			} catch (FileNotFoundException fnfe) {
				System.out.println("ERROR: File " + fileName + " not found!");
			}
		} else {
			System.out.println("ERROR: No file name provided.");
		}
	}

	// saves recording to file
	public void saveToFile(String fileName) {
		try {

			// a file object
			File playListFilePath = new File(fileName);

			if (playListFilePath.createNewFile()) {

				// creating a writer to write to file
				FileWriter recWriter = new FileWriter(fileName);

				for (Recording rec : recordingList) {

					// check type of recording
					if (rec instanceof AudioRecording) {
						recWriter.write(
								"A" + "," + rec.getartist() + "," + rec.getName() + "," + rec.getDuration() + "," + 0);

					} else if (rec instanceof VideoRecording) {
						recWriter.write(
								"V" + "," + rec.getartist() + "," + rec.getName() + "," + rec.getDuration() + "," + 0);
					}
				}
				recWriter.close();
			} else {
				// ignore when file exits
			}
		} catch (IOException e) {
			System.out.println("An error occured");
	
		}
	}

	// toString method
	public String toString() {
		String returnString = "Playlist: " + this.name + " [" + ((int) Math.floor(this.durationInSeconds / 60)) + "m"
				+ (this.durationInSeconds % 60) + "s]:\n";
		if (this.numberOfRecordings > 0) {
			for (int index = 0; index < this.numberOfRecordings; index++) {

				Recording rec = recordingList.get(index);
				returnString = returnString + rec.toString() + "\n";
			}
		}
		return returnString;
	}
	
	public void stats() {
		ArrayList<Recording> rec = this.getRecordingList();
		for(Recording r : rec) {
			System.out.println(r.toString());	
		}
	}
	
	public void remove() {
		ArrayList<Recording> rec = this.getRecordingList();
		for(int i = 0; i<rec.size();i++) {
			for(int j = i+1; j<rec.size();i++){
				if(rec.get(i).getartist().equals(rec.get(j).getartist()) && rec.get(i).getName().equals(rec.get(j).getName())) {
					rec.remove(j);
				}
			}  
		}
	}
}