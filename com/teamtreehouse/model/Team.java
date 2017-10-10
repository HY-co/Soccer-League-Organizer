package com.teamtreehouse.model;

import com.teamtreehouse.model.Player;

import java.lang.Comparable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.Comparator;

public class Team implements Comparable<Team>{
  private String name;
  private String coach;
  private Set<Player> players;
  
  public Team(String name, String coach) {
    this.name = name;
    this.coach = coach;
    players = new TreeSet<>();
  }
  
  public void addPlayer(Player player) {
    players.add(player);  
  }
  
  public void removePlayer(Player player) {
    players.remove(player);  
  }
  
  public Set<Player> getPlayers() {
    return players;  
  }
  
  @Override
  public int compareTo(Team other) {
    return this.name.compareTo(other.name);  
  }
  
  public String getName() {
    return name;  
  }
  
  public String getCoach() {
    return coach;  
  }
  
  public Map<String, List<Player>> getByHeight() {
    Map<String, List<Player>> byHeight = new TreeMap<>();
    byHeight.put("35-40", new ArrayList<Player>());
    byHeight.put("41-46", new ArrayList<Player>());
    byHeight.put("47-50", new ArrayList<Player>());
    
    for (Player player : players) {
      int height = player.getHeightInInches();
      if (height >= 35 && height <= 40) byHeight.get("35-40").add(player);
      else if (height >= 41 && height <= 46) byHeight.get("41-46").add(player);
      else byHeight.get("47-50").add(player);
    }
    
    for (Map.Entry<String, List<Player>> group: byHeight.entrySet()) {
      group.getValue().sort(new Comparator<Player>(){
        @Override
        public int compare(Player p1, Player p2) {
          if (p1.getHeightInInches() == p2.getHeightInInches()) {
            return p1.compareTo(p2);  
          }
          
          return p1.getHeightInInches() - p2.getHeightInInches();
        }
      });  
    }
    
    return byHeight;
  }
  
  public Map<Integer, Integer> countHeight() {
    Map<Integer, Integer> countHeight = new TreeMap<>();
    for (Player player : players) {
      int height = player.getHeightInInches();
      countHeight.put(height, countHeight.getOrDefault(height, 0) + 1);  
    }
    
    return countHeight;
  }
  
  public int[] balanceReport() {
    int[] balance = new int[2];
    for (Player player : players) {
      balance[player.isPreviousExperience() ? 0 : 1]++;  
    }
    
    return balance;
  }
}