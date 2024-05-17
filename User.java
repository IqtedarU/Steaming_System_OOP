import java.util.ArrayList;

/**
 * A person using the streaming service
 * 
 */
public class User {

	/**
	 * unique identification number assigned to user
	 */
	private static int id;

	/**
	 * Name assigned to a user
	 */
	private String name;

	/**
	 * Playlist that user has in the streaming service
	 */
	private Playlist playList;

	public User(String name) {
		id = id++;
		this.name = name;

		// an empty PlayList when user is created
		playList = new Playlist();
	}

	// getter
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Playlist getPlayList() {
		return playList;
	}

	@Override
	public String toString() {

		return name + ":" + id;
	}
	
	public void play(){
		playList.play();
	}
	
	public void play(int index){
		playList.play(index);
	}
	public void add(ArrayList<Recording> otherUserRecordingList) {
		
		ArrayList<Recording> currentUserPlaylist = this.getPlayList().getRecordingList();
		// loop through all the recording of the other user and add to the current user
		// playlist
		for (Recording r : otherUserRecordingList) {
			for(Recording rec : currentUserPlaylist) {
			boolean inPlaylist = false;
			String currentArtist = rec.getartist();
			String currentName = rec.getName();
			if(currentName.equalsIgnoreCase(r.getName()) && currentArtist.equalsIgnoreCase(r.getartist())) {
				inPlaylist = true;
			}
			if(inPlaylist == false) {
				currentUserPlaylist.add(r);
				}else {
				System.out.print("This Recording is in the playlist");
			}
			}
		}
	}
	public void add(VideoRecording video) {
		ArrayList<Recording> records = this.getPlayList().getRecordingList();
		for(Recording r : records ) {
			boolean inPlaylist = false;
			String currentArtist = r.getartist();
			String currentName = r.getName();
			if(currentName.equalsIgnoreCase(video.getName()) && currentArtist.equalsIgnoreCase(video.getartist())) {
				inPlaylist = true;
			}
			if(inPlaylist == false) {
				this.getPlayList().add(r);
			}else {
				System.out.print("This Recording is in the playlist");
			}
		}
	}
	public void add(AudioRecording audio) {
		boolean inPlaylist = false;
		ArrayList<Recording> records = this.getPlayList().getRecordingList();
		for(Recording r : records ) {
			String currentArtist = r.getartist();
			String currentName = r.getName();
			if(currentName.equalsIgnoreCase(audio.getName()) && currentArtist.equalsIgnoreCase(audio.getartist())) {
				inPlaylist = true;
			}
		}
		if(inPlaylist == false) {
			this.getPlayList().add(audio);
		}else {
			System.out.print("This Recording is in the playlist");
		}
	}	
	
	public void remove(int index) {
		this.getPlayList().getRecordingList().remove(index);
	}
	public void remove(String name) {
		Playlist playlist = this.getPlayList();
		// obtains recordings from playlist
		ArrayList<Recording> records = playlist.getRecordingList();
		boolean isRecordingRm = false;
		for (Recording r : records) {

			if (r.getName().equals(name)) {
				records.remove(r);
				System.out.println("Recording successfully removed from the playlist");
				isRecordingRm = true;
			}
	}
		if (!isRecordingRm) {
			System.out.println("The Recording was not found in the playlist");
		}
}
}
