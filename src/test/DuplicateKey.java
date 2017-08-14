package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
 
public class DuplicateKey {
 
 static Map<String, ArrayList<String>> hashMap = new HashMap<String,  ArrayList<String>>();
  
 public static void main(String[] args) {
  retrieveValues();
 } 
 public static void retrieveValues()
 {   
  addValues("A", "a1");
  addValues("A", "a2");
  addValues("B", "b1");
  addValues("B", "b2");
  addValues("C", "c1");   
   
  Iterator<String> it = hashMap.keySet().iterator(); 
  ArrayList<String> tempList = null;
 
       while(it.hasNext()){ 
           String key = it.next().toString();
             tempList = hashMap.get(key);
            if(tempList != null){
                for(String value: tempList){
                	if(key.equals("A")) {
                   System.out.println("Key : "+key+ " ,  Value : "+value);
                	}
                }
               } 
        }
 }  
 //Method to add duplicate key and values
 private static void addValues(String key, String value)
 { 
  ArrayList<String> tempList = null;      
  if(hashMap.containsKey(key)){  
   tempList=hashMap.get(key); 
   if(tempList == null)    
     tempList = new ArrayList<String>();     
   tempList.add(value);    
  }
  else
  {     
   tempList = new ArrayList();  
   tempList.add(value);     
   }     
  hashMap.put(key,tempList);
 } 
}