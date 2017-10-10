import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.LeagueOrganizer;

import java.util.Set;
import java.util.TreeSet;
import java.util.Arrays;

public class LeagueManager {

  public static void main(String[] args) {
    Player[] players = Players.load();
    System.out.printf("There are currently %d registered players.%n", players.length);
    // Your code here!
    System.out.println();
    Players allPlayers = new Players(new TreeSet<>(Arrays.asList(players)));
    LeagueOrganizer lo = new LeagueOrganizer(allPlayers);
    lo.run();
    System.out.println("Exiting....");
  }

}
