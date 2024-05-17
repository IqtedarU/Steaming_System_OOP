import java.util.ArrayList;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    


/**
 * Manages users streaming services
 */
public class StreamingSystem {

	/**
	 * object for reading user input
	 */
	private Scanner sc = new Scanner(System.in);

	/**
	 * List of all users in the streaming service
	 */
	private ArrayList<User> streamingUsers = new ArrayList<User>();

	/**
	 * The current user using the streaming service
	 */
	private User currentUser = null;

	// displays the menu of the program
	public void menu() {

		// loop control variable
		boolean quit = false;

		while (!quit) {
			// prints menu
			System.out.println(
					"Main menu:\n[1] Add new user\n[2] Remove user\n[3] List all users\n[4] User menu\n[5] Exit\nWhat is your choice?");

			// holds choice selected by user
			int choice;

			// ensuring user enters only a number
			try {

				choice = Integer.parseInt(sc.nextLine());

			} catch (Exception e) {

				System.out.println("only number options accepted 1...5");
	

				return;
			}

			switch (choice) {

			case 1:
				handleAddNewUser();
				break;
			case 2:
				handleRemoveUser();
				break;
			case 3:
				handleListAllUsers();
				break;
			case 4:
				handleUserMenu();
				break;
			case 5:
				// exits the application
				System.out.println("Bye..");
				quit = true;
				break;
			default:
				System.out.println("Option not recognised only number options accepted 1...5\"");
			}

		}
		// close resource to relieve operating system of resources being managed after
		// use
		sc.close();
	}

	/**
	 * Handles services a streaming user might request
	 */
	private void handleUserMenu() {

		try {

			// before a user can request service we have to know their id so that many users
			// can be managed
			System.out.println("Enter your ID");
			int userID = Integer.parseInt(sc.nextLine());

			boolean isUserFound = false;

			if (getUserWIthID(userID) != null) {

				isUserFound = true;

				// make identified user current user
				currentUser = getUserWIthID(userID);

			}

			// if user is not found,force user to re enter id again
			if (!isUserFound) {

				System.out.println("User with ID " + userID
						+ " not found! please register by\n using add user from main menu before using service");
				return;
			}

			// display user menu
			System.out.println(
					"User menu:\n[1] Add recording\n[2] Add playlist from file\n[3] Add playlist from another user\n[4] Remove recording from playlist\n[5] Play individual recording\n[6] Play entire playlist in order ONCE\n[7] Play entire playlist shuffled ONCE\n[8] Save playlist to a file\n[9] Display playlist stats\n[10] Go back to previous menu\nWhat is your choice?");

			// using nextLine instead of nextInt to avoid errors 
			int choice = Integer.parseInt(sc.nextLine());

			switch (choice) {
			case 1:

				System.out.println("Which recording do you want to add? \'A\' for audio \'V\' for video");

				String recordingType = sc.nextLine();

				String artist;
				String name;
				int durationInSeconds;

				// obtaining recording information to add
				System.out.println("Enter artist name ");
				artist = sc.nextLine();

				System.out.println("Enter recording name ");
				name = sc.nextLine();

				System.out.println("Enter recording duration in seconds");
				durationInSeconds = Integer.parseInt(sc.nextLine());

				if (recordingType.equalsIgnoreCase("A")) {

					// creating recording based on information provided
					AudioRecording re = new AudioRecording(artist, name, durationInSeconds);

					// add recording of the user
					currentUser.add(re);

				} else if (recordingType.equals("V")) {
					// creating recording based on information provided
					VideoRecording r = new VideoRecording(artist, name, durationInSeconds);

					// add recording of the user
					currentUser.add(r);
				} else {
					System.out.println("The only recorgnized recording types are A for audio and V for video");
					return;
				}

				break;
			case 2:

				// obtain path to playlist and load the file
				System.out.println("Enter playList file name[path] to load");
				String playListPath = sc.nextLine();
				currentUser.getPlayList().load(playListPath);
				currentUser.getPlayList().remove();

				break;
			case 3:

				System.out.println("Enter id of the user to add playlist from");

				int otherUserUniqueID = Integer.parseInt(sc.nextLine());
				User otherUser = getUserWIthID(otherUserUniqueID);

				// ensure other user exists
				if (otherUser != null && otherUser.getId() != currentUser.getId()) {

					// obtaining recording of the other user
					ArrayList<Recording> otherUserRecordingList = otherUser.getPlayList().getRecordingList();

					currentUser.add(otherUserRecordingList);

					System.out.println("All the other user's recording playlist added to your playlist successfully");
				}

				break;
			case 4:

				System.out.println("Would you like to remove by Index or Name?");	
				String removeOption = sc.nextLine();
				if(removeOption.equalsIgnoreCase("Index")) {
					int removeIndex = Integer.parseInt(sc.nextLine());
					currentUser.remove(removeIndex);
				}
				
				if(removeOption.equalsIgnoreCase("Name")) {
				System.out.println("Enter the name of recording to remove from playlist");
				String nameOfRecording = sc.nextLine();
				currentUser.remove(nameOfRecording);
				}else {
					System.out.print("You did not enter a valid option!");
				}
			case 5:
				System.out.println("Would you like to play by Index or Name?");	
				String playOption = sc.nextLine();
				if(playOption.equalsIgnoreCase("Index")) {
					int playIndex = Integer.parseInt(sc.nextLine());
					currentUser.getPlayList().play(playIndex);
					
				}else if(playOption.equalsIgnoreCase("Name")) {
				System.out.println("Enter the name of recording to play");
				String recordnNam = sc.nextLine();

				// get recordings
				// get current user playlist
				Playlist plist = currentUser.getPlayList();
				ArrayList<Recording> recs = plist.getRecordingList();
				boolean isRecordingFnd = false;

				for (Recording rc : recs) {

					if (rc.getName().equals(recordnNam)) {
						rc.play();
						isRecordingFnd = true;
					}
					if (!isRecordingFnd) {
						System.out.println("The Recording was not found in the playlist");
					}
					break;
					}
				}else{
					System.out.print("You did not enter a valid option!");
				}
				
			case 6:

				currentUser.getPlayList().play(); 

				break;
			case 7:

				// current user playlist
				Playlist pl = currentUser.getPlayList();

				// play and shuffle current user playlist once
				pl.shuffle(pl.getRecordingList().size() - 1);
				break;
			case 8:

				// saves playlist to file
				Playlist plistToSave = currentUser.getPlayList();
				LocalDateTime timeNow = LocalDateTime.now(); 
				DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("MM_dd_year_HH_mm_ss");  
				plistToSave.saveToFile(currentUser.getName()+"_"+"PLAYLIST_"+timeFormat.format(timeNow));

				System.out.println("playlist saved successfully to file with name " + currentUser.getName()+"_"+"PLAYLIST_"+timeFormat.format(timeNow));
				break;
			case 9:

				// displays playlist stats
				Playlist plistStats = currentUser.getPlayList();
				plistStats.stats();
				break;
			case 10:
				break;

			default:
				throw new IllegalArgumentException("Unexpected value: " + choice);
			}

		} catch (

		Exception e) {
			System.out.println("An ERRO occured");
		}

	}

	/**
	 * Lists all streaming users in the system
	 */
	private void handleListAllUsers() {

		for (User usr : streamingUsers) {

			System.out.println(usr.toString());
		}

	}

	/**
	 * Handles the removal of a user from the streaming service users list
	 */
	private void handleRemoveUser() {

		// display remove user menu
		System.out.println(
				"Remove user menu:\n[1] Remove user by ID\n[2] Remove user by name\n[3] Go back to previous menu\nWhat is your choice?");

		// holds a user's choice
		int choice = 0;

		try {
			choice = Integer.parseInt(sc.nextLine());

			switch (choice) {
			case 1:
				System.out.println("Enter user's ID to remove");
				int userID = Integer.parseInt(sc.nextLine());

				// check if user with the id exists and remove

				boolean isUserRemoved = false;

				if (getUserWIthID(userID) != null) {
					isUserRemoved = streamingUsers.remove(getUserWIthID(userID));
					break;
				}

				if (isUserRemoved)
					System.out.println("User Revomed successfully from streaming users");
				else
					System.out.println("User with " + userID + " was not found");
				break;
			case 2:

				System.out.println("Enter user's name to remove");
				String userName = sc.nextLine();

				// check if user with the id exists and remove

				boolean isUserWithNameRemoved = false;

				for (User user : streamingUsers) {
					if (user.getName() == userName) {
						isUserWithNameRemoved = streamingUsers.remove(user);
						break;
					}
				}

				if (isUserWithNameRemoved)
					System.out.println("User Revomed successfully from streaming users");
				else
					System.out.println("User with " + userName + " was not found");
				break;
			case 3:
				// do nothing and takes users to previous menu
				break;
			default:
				System.out.println("numbers recognized are 1,2 and 3 only");
			}

		} catch (Exception e) {

			System.out.println("only numbers accepted for input !");
		}

	}

	/**
	 * Handles the addition of a new streaming user to the system
	 */
	private void handleAddNewUser() {

		String userName;

		// obtain name
		System.out.println("Enter a user name to add");
		userName = sc.nextLine();

		// create user using parameters entered
		User user = new User(userName);

		// adding the user to all the users list managed by the streaming system
		boolean isAdded = streamingUsers.add(user);

		// inform the program user if the new user was added successfully
		if (isAdded) {
			System.out.println("User added Successfully");
			System.out.println("Your User ID is: "+ user.getId());
		}else {
			System.out.println("Could not add the user to the list of streaming users");
		}
	}

	/**
	 * helper method to get user by id from streaming service users. It return user found or null otherwise
	 */

	public User getUserWIthID(int userID) {

		for (User user : streamingUsers) {
			if (user.getId() == userID) {
				return user;
			}
		}

		return null;
	}

	/**
	 * A helper method to get current user's recordings
	 * 
	 * return current user recording list
	 */

}
