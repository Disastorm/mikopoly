/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coolmonopoly.board;

import coolmonopoly.Player;

/**
 *
 * @author Disastorm
 */
public class PropertyGroup {
     private Property[] properties;
     
     public PropertyGroup(){
     }
     
     public void setProperties(Property[] properties){
         this.properties=properties;
     }
     
     public boolean isBuildable(){
         Player owner = null;
         for(Property property : properties){
             if(property.getOwner()==null){
                 return false;
             }
             if(owner == null){
                owner=property.getOwner();
             }else{
                 if(owner != property.getOwner()){
                     return false;
                 }
             }
         }
         return true;
     }
}
