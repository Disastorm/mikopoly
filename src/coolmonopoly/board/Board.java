/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coolmonopoly.board;

import coolmonopoly.cards.Chance;
import coolmonopoly.cards.CommunityChest;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Disastorm
 */
public class Board {
        private static final String BOARD_IMG_PATH="data/board/monopoly_original.jpg";
        private Image boardImage;
        private Tile[] tiles;
        private Stack<Chance> chances;
        private Stack<CommunityChest> communityChests;
        
        
        public Board(){
            tiles = new Tile[40];
            chances = Chance.getNewDeck();
            communityChests = CommunityChest.getNewDeck();
            initializeTiles();
            try {
                boardImage = new Image(BOARD_IMG_PATH);
            } catch (SlickException ex) {
                Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public void render(int x, int y, float scale){
            boardImage.draw(x, y, scale);
        }
        
        public Chance drawChance(){
            return chances.pop();
        }
        public CommunityChest drawCommunityChest(){
            return communityChests.pop();
        }        
        
        private void initializeTiles(){
            PropertyGroup group1 = new PropertyGroup();     
            String name1="Mediterranean Avenue";
            String name2="Baltic Avenue";
            tiles[1]=new Property(name1,60,group1,50,2,10,30,90,160,250);
            tiles[3]=new Property(name2,60,group1,50,4,20,60,180,320,450);
            group1.setProperties(new Property[]{(Property)tiles[1],(Property)tiles[3]});
            
            PropertyGroup group2 = new PropertyGroup();     
            name1="Oriental Avenue";
            name2="Vermont Avenue";
            String name3="Connecticut Avenue";
            tiles[6]=new Property(name1,100,group2,50,6,30,90,270,400,550);
            tiles[8]=new Property(name2,100,group2,50,6,30,90,270,400,550);
            tiles[9]=new Property(name3,120,group2,50,8,40,100,300,450,600);
            group2.setProperties(new Property[]{(Property)tiles[6],(Property)tiles[8],(Property)tiles[9]});
            
            PropertyGroup group3 = new PropertyGroup();     
            name1="St. Charles Place";
            name2="States Avenue";
            name3="Virginia Avenue";
            tiles[11]=new Property(name1,140,group3,100,10,50,150,450,625,750);
            tiles[13]=new Property(name2,140,group3,100,10,50,150,450,625,750);
            tiles[14]=new Property(name3,160,group3,100,12,60,180,500,700,900);
            group3.setProperties(new Property[]{(Property)tiles[11],(Property)tiles[13],(Property)tiles[14]});
            
            PropertyGroup group4 = new PropertyGroup();     
            name1="St. James Place";
            name2="Tennessee Avenue";
            name3="New York Avenue";
            tiles[16]=new Property(name1,180,group4,100,14,70,200,550,700,900);
            tiles[18]=new Property(name2,180,group4,100,14,70,200,550,700,950);
            tiles[19]=new Property(name3,200,group4,100,16,80,220,600,800,1000);
            group4.setProperties(new Property[]{(Property)tiles[16],(Property)tiles[18],(Property)tiles[19]});
            
            PropertyGroup group5 = new PropertyGroup();     
            name1="Kentucky Avenue";
            name2="Indiana Avenue";
            name3="Illinois Avenue";
            tiles[21]=new Property(name1,220,group5,150,18,90,250,700,875,1050);
            tiles[23]=new Property(name2,220,group5,150,18,90,250,700,875,1050);
            tiles[24]=new Property(name3,240,group5,150,20,100,300,750,925,1100);
            group5.setProperties(new Property[]{(Property)tiles[21],(Property)tiles[23],(Property)tiles[24]});
            
            PropertyGroup group6 = new PropertyGroup();     
            name1="Atlantic Avenue";
            name2="Ventnor Avenue";
            name3="Marvin Gardens";
            tiles[26]=new Property(name1,260,group6,150,22,110,330,800,975,1150);
            tiles[27]=new Property(name2,260,group6,150,22,110,330,800,975,1150);
            tiles[29]=new Property(name3,280,group6,150,24,120,360,850,1025,1200);
            group6.setProperties(new Property[]{(Property)tiles[26],(Property)tiles[27],(Property)tiles[29]});
            
            PropertyGroup group7 = new PropertyGroup();     
            name1="Pacific Avenue";
            name2="North Carolina Avenue";
            name3="Pennsylvania Avenue";
            tiles[31]=new Property(name1,300,group7,200,26,130,390,900,1100,1275);
            tiles[32]=new Property(name2,300,group7,200,26,130,390,900,1100,1275);
            tiles[34]=new Property(name3,320,group7,200,28,150,450,1000,1200,1400);
            group7.setProperties(new Property[]{(Property)tiles[31],(Property)tiles[32],(Property)tiles[34]});
            
            PropertyGroup group8 = new PropertyGroup();     
            name1="Park Place";
            name2="Boardwalk";
            tiles[37]=new Property(name1,350,group8,200,35,175,500,1100,1300,1500);
            tiles[39]=new Property(name2,400,group8,200,50,200,600,1400,1700,2000);
            group8.setProperties(new Property[]{(Property)tiles[37],(Property)tiles[39]});
            
            RailroadGroup rrGroup = new RailroadGroup();
            name1="Reading Railroad";
            name2="Pennsylvania Railroad";
            name3="B. & O. Railroad";
            String name4="Short Line";
            tiles[5]=new Railroad(name1,200,rrGroup,25,50,100,200);   
            tiles[15]=new Railroad(name2,200,rrGroup,25,50,100,200);       
            tiles[25]=new Railroad(name3,200,rrGroup,25,50,100,200);       
            tiles[35]=new Railroad(name4,200,rrGroup,25,50,100,200);                   
            rrGroup.setRailroads(new Railroad[]{(Railroad)tiles[5],(Railroad)tiles[15],(Railroad)tiles[25],(Railroad)tiles[35]});

            GenericTile visitJail = new GenericTile(TileType.VISITING_JAIL);
            GenericTile go = new GenericTile(TileType.GO);
            GenericTile parking = new GenericTile(TileType.FREE_PARKING);
            GenericTile gotoJail = new GenericTile(TileType.GOTO_JAIL);
            tiles[0]=go;
            tiles[10]=visitJail;
            tiles[20]=parking;
            tiles[30]=gotoJail;
            
            GenericTile chanceTile = new GenericTile(TileType.CHANCE);
            GenericTile communityChest = new GenericTile(TileType.COMMUNITY_CHEST);
            tiles[2]=communityChest;
            tiles[7]=chanceTile;
            tiles[17]=communityChest;
            tiles[22]=chanceTile;
            tiles[33]=communityChest;
            tiles[36]=chanceTile;
            
            UtilityPropertyGroup utilGroup = new UtilityPropertyGroup();
            name1="Electric Company";
            name2="Water Works";
            tiles[12]=new UtilityProperty(name1,150,utilGroup,4,10);   
            tiles[28]=new UtilityProperty(name2,150,utilGroup,4,10);       
            utilGroup.setUtilities(new UtilityProperty[]{(UtilityProperty)tiles[12],(UtilityProperty)tiles[28]});
            
            GenericTile incomeTax = new GenericTile(TileType.INCOME_TAX);
            GenericTile luxuryTax = new GenericTile(TileType.LUXURY_TAX);
            tiles[4]=incomeTax;
            tiles[38]=luxuryTax;
        }
        
        public Tile getTile(int i){
            return tiles[i];
        }
        
        public Tile[] getTiles(){
            return tiles;
        }
        
}
