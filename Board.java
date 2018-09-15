import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A Board represents the current state of the game. Boards know their dimension, 
 * the collection of tiles that are inside the current flooded region, and those tiles 
 * that are on the outside.
 * 
 * @author Mohammad Ainina
 */

public class Board {
  public Map<Coord, Tile> inside, outside;
  private int size;
  
  /**
   * Constructs a square game board of the given size, initializes the list of 
   * inside tiles to include just the tile in the upper left corner, and puts 
   * all the other tiles in the outside list.
   */
  public Board(int size) {
    // A tile is either inside or outside the current flooded region.
    inside = new HashMap<>();
    outside = new HashMap<>();
    this.size = size;
    for (int y = 0; y < size; y++)
      for (int x = 0; x < size; x++) {
        Coord coord = new Coord(x, y);
        outside.put(coord, new Tile(coord));
      }
    // Move the corner tile into the flooded region and run flood on its color.
    Tile corner = outside.remove(Coord.ORIGIN);
    inside.put(Coord.ORIGIN, corner);
    flood(corner.getColor());
  }
  
  /**
   * Returns the tile at the specified coordinate.
   */ 
  public Tile get(Coord coord) {
    if (outside.containsKey(coord))
      return outside.get(coord);
    return inside.get(coord);
  }
  
  /**
   * Returns the size of this board.
   */
  public int getSize() {
    return size;
  }
  
  /**
   * 
   * 
   * Returns true iff all tiles on the board have the same color.
   */
  public boolean fullyFlooded() {
	  if(outside.isEmpty()) {
		  return true;
	  }
    return false;
  }
  
  /**
   * 
   * 
   * Updates this board by changing the color of the current flood region 
   * and extending its reach.
   */
  public void flood(WaterColor color) {
	  for(Coord key: inside.keySet()) {
			  inside.get(key).setColor(color);
	  }

	  ArrayList<Coord> list = new ArrayList<Coord>();
	  boolean bool =true;
	  while(bool) {
		  bool = false;
	  for(Coord key: inside.keySet()){
		  for(Coord key2: key.neighbors(getSize())) {
			  if(outside.containsKey(key2)) {
				  if(outside.get(key2).getColor().equals(color)) {
					  list.add(key2);
					  bool = true;
				  }
			  }
		  }
	  }
	  Iterator<Entry<Coord, Tile>> itr = outside.entrySet().iterator(); 
	  while(itr.hasNext()) 
      { 
          Entry<Coord, Tile> entry = itr.next();
		  if(list.contains(entry.getKey())) {
			  inside.put(entry.getKey(),outside.get(entry.getKey()));
			  itr.remove();
		  }	 

	  }
	  }
  }


  
  
  /**
   * 
   * 
   * Returns the "best" GameColor for the next move. 
   * 
   * The suggest method iterates thru all the tiles adjoining the inside tiles and increases the count for a color  
   * or adds the color to the hash map and sets it to one if this the first time the color is seen. 
   * once its gone thru all bordering tiles the color that has the highest count gets suggested.
   * 
   */
  public WaterColor suggest() {
    WaterColor cornerColor = inside.get(Coord.ORIGIN).getColor();
    Map<WaterColor, Integer> freq = new HashMap<WaterColor, Integer>();
	  for(Coord key: inside.keySet()){
		  for(Coord key2: key.neighbors(getSize())) {
			  if(outside.containsKey(key2)) {
				  if(!outside.get(key2).getColor().equals(cornerColor)) {
					 if(freq.containsKey(outside.get(key2).getColor())) {
						 freq.put(outside.get(key2).getColor(), freq.get(outside.get(key2).getColor()).intValue() + 1);
					 }
					 else {
						 freq.put(outside.get(key2).getColor(), 1);
					 }
				  }
			  }
		  }
	  }
	  Integer max = 0;
	  WaterColor guess = null;
	  for(Entry<WaterColor, Integer> color: freq.entrySet()) {
		  if(color.getValue().compareTo(max.intValue()) > 0) {
			  max = color.getValue();
			  guess = color.getKey();
		  }
	  }
    return guess;
  }
  
  /**
   * Returns a string representation of this board. Tiles are given as their
   * color names, with those inside the flooded region written in uppercase.
   */ 
  public String toString() {
    StringBuilder ans = new StringBuilder();
    for (int y = 0; y < size; y++) {
      for (int x = 0; x < size; x++) {
        Coord curr = new Coord(x, y);
        WaterColor color = get(curr).getColor();
        ans.append(inside.containsKey(curr) ? color.toString().toUpperCase() : color);
        ans.append("\t");
      }
      ans.append("\n");
    }
    return ans.toString();
  }
  
  /**
   * Simple testing.
   */
  public static void main(String... args) {
    // Print out boards of size 1, 2, ..., 5
    int n = 5;
    for (int size = 1; size <= n; size++) {
      Board someBoard = new Board(size);
      System.out.println(someBoard);
    }
  }
}






