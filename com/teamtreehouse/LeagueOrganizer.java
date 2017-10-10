package com.teamtreehouse;

import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Team;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class LeagueOrganizer {
  private BufferedReader reader;
  private Players players;
  private List<String> menu;
  private Set<Team> teams;
  
  public LeagueOrganizer(Players players) {
    reader = new BufferedReader(new InputStreamReader(System.in));
    this.players = players;
    teams = new TreeSet<>();
    menu = new ArrayList<>();
    menu.add("Create - Create a new team");
    menu.add("Add - Add a player to a team");
    menu.add("Remove - Remove a plyaer from a team");
    menu.add("Report - View a report of a team by height");
    menu.add("Balance - View the League Balance Report");
    menu.add("Roster - View roster");
    menu.add("Quit - Exits the program");
  }
  
  private String promptForAction() throws IOException{
    System.out.println("menu");
    for (String option : menu) {
      System.out.printf("%s%n", option);
    }
    
    System.out.println();
    System.out.print("Select an option: ");
    String choice = reader.readLine();
    System.out.println();
    choice = choice.trim();
    if (choice.length() < 1) return choice;
    return choice.substring(0, 1).toUpperCase() + choice.substring(1).toLowerCase();
  }
  
  public void run() {
    String choice = "";
    do {
      try {
        choice = promptForAction();
        switch(choice) {
          case "Create":
            if (!canCreate()) {
              System.out.printf("You can not create more teams otherwise you will have more teams than"+                                  "available players%n%n");  
            }else {
              Team team = promptNewTeam();
              teams.add(team);
              System.out.printf("Team %s coached by %s added%n%n", team.getName(), team.getCoach());
            }
            break;
          case "Add":
            Player playerToAdd = promptPlayer(players.getPlayers());
            Team teamToAdd = promptTeam();
            teamToAdd.addPlayer(playerToAdd);
            System.out.printf("%s %s has been added to %s%n%n", 
                              playerToAdd.getFirstName(), 
                              playerToAdd.getLastName(),
                              teamToAdd.getName());
            players.removePlayer(playerToAdd);
            break;
          case "Remove":
            Team teamToRemoveFrom = promptTeam();
            Player playerToRemove = promptPlayer(teamToRemoveFrom.getPlayers());
            teamToRemoveFrom.removePlayer(playerToRemove);
            System.out.printf("%s %s has been removed from %s%n%n",
                              playerToRemove.getFirstName(),
                              playerToRemove.getLastName(),
                              teamToRemoveFrom.getName());
            players.addPlayer(playerToRemove);
            break;
          case "Report":
            Team teamToView = promptTeam();
            viewHeightReport(teamToView);
            System.out.println();
            break;
          case "Balance":
            displayBalanceReport();
            System.out.println();
            break;
          case "Roster":
            displayRoster();
            System.out.println();
            break;
          case "Quit":
            System.out.println("Thanks for using Soccer League Organizer!");
            break;
          default:
            System.out.printf("Unknown choice: '%s'. Try again. %n%n", choice);
        }
      }catch (IOException ioe) {
        System.out.print(ioe.getMessage());
        ioe.printStackTrace();
      }
    }while (!choice.equals("Quit"));
  }
  
  private Team promptNewTeam() throws IOException{
    System.out.print("What is the team name? ");
    String teamName = reader.readLine();
    System.out.print("What is the coach name? ");
    String coachName = reader.readLine();
    return new Team(teamName, coachName);
  }
  
  private Player promptPlayer(Set<Player> players) throws IOException {
    System.out.println("Available Players:");
    List<Player> playerList = new ArrayList<>(players);
    int count = 1;
    for (Player player : playerList) {
      System.out.printf("%s.) %s %s (%s inches - %s) %n",
                        count++, player.getFirstName(), player.getLastName(),
                        player.getHeightInInches(), 
                        player.isPreviousExperience() ? "experienced" : "inexperienced");
    }
    int index = getIndex(count);
    return playerList.get(index);
  }
  
  private Team promptTeam() throws IOException {
    System.out.println("Available Teams");
    List<Team> teamList = new ArrayList<>(teams);
    int count = 1;
    for (Team team : teams) {
      System.out.printf("%s.) %s coached by %s%n", count++, team.getName(), team.getCoach());  
    }
    
    int index = getIndex(count);
    return teamList.get(index);
  }
  
  private int getIndex(int count) throws IOException {
    int index = -1;
    if (count == 1) throw new IOException();
    do {
      try {
        System.out.print("Select an option: ");
        String optionChoosen = reader.readLine();
        System.out.println();
        index = Integer.parseInt(optionChoosen);
        if (index <= 0 || index > count) {
          System.out.println("Please enter a value in the range");  
        }
      }catch (NumberFormatException nfe) {
        System.out.printf("%s it is not a valid number, please use a valid value%n", nfe.getMessage());  
      }
    }while (index <= 0 || index >= count);
    
    return index - 1;  
  }
  
  private void viewHeightReport(Team team) {
    // display players in each height group
    Map<String, List<Player>> heightGroup = team.getByHeight();
    for (Map.Entry<String, List<Player>> group : heightGroup.entrySet()) {
      System.out.printf("%s inches:%n", group.getKey());
      for (Player player : group.getValue()) {
        System.out.printf("%s %s (%s inches - %s)%n", 
                          player.getFirstName(),
                          player.getLastName(),
                          player.getHeightInInches(),
                          player.isPreviousExperience() ? "experienced" : "inexperienced");
      }
      System.out.println();
    }
    // show average experience level
    int[] balance = team.balanceReport();
    System.out.printf("%nThe average experience level of %s is %s%%%n%n",
                       team.getName(),
                       (balance[0] + balance[1] == 0) ? "N/A" : 
                       "" + Math.round(100 * (double)balance[0] / (balance[0] + balance[1])));
    
    // count number of players for each height
    System.out.println("Number of players on each height:");
    Map<Integer, Integer> countHeight = team.countHeight();
    for (int height : countHeight.keySet()) {
      System.out.printf("%d inches: %d player%s%n", height, countHeight.get(height),
                       countHeight.get(height) == 1 ? "" : "s"); 
    }
  }
  
  private void displayBalanceReport() {
    System.out.printf("This is a balance report for all the teams in the league:%n%n");
    for (Team team : teams) {
      int[] balance = team.balanceReport();
      System.out.printf("Balance report of %s:%n", team.getName());
      System.out.printf("Experienced Players: %d%n" + 
                        "Inexperienced Players: %d%n" +
                        "Average Experience Level: %s%%%n%n", 
                        balance[0], 
                        balance[1], 
                        (balance[0] + balance[1] == 0) ? "N/A" : 
                        "" + Math.round(100 * (double)balance[0]/(balance[0] + balance[1])));
    }
  }
                                 
  private void displayRoster() {
    for (Team team : teams) {
      System.out.printf("%s: - ", team.getName());
      System.out.printf("%s ", team.getPlayers());
      System.out.println();
    }
  }
                                 
  private boolean canCreate() {
    int teamWithoutPlayer = 0, availablePlayer = players.getPlayers().size();
    for (Team team : teams) {
      if (team.getPlayers().size() == 0) teamWithoutPlayer++;  
    }
    
    if (teamWithoutPlayer >= availablePlayer) return false;
    return true;
  }
                                 
}