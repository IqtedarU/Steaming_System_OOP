
public class VideoRecording extends Recording {

	private double rate;

	// constructor
	public VideoRecording(String artist, String name, int durationInSeconds, double rate) {

		// passing arguments to super constructor
		super(artist, name, durationInSeconds);
		this.rate = rate;
	}

	public VideoRecording(String artist, String name, int durationInSeconds) {

		// passing arguments to super constructor
		super(artist, name, durationInSeconds);
	}
	
	
}
