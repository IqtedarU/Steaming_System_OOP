
public class AudioRecording extends Recording {

	private double rate;

	// constructor
	public AudioRecording(String artist, String name, int durationInSeconds, double rate) {

		// calls super class constructor with arguments
		super(artist, name, durationInSeconds);
		this.rate = rate;
	}

	// constructor
	public AudioRecording(String artist, String name, int durationInSeconds) {

		// calls super class constructor with arguments
		super(artist, name, durationInSeconds);
	}
}
