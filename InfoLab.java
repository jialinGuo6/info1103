// Code used by Dr Tasse in CS1083
// hacked up by OFK, March 2020, March 2021.

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import com.mysql.jdbc.jdbc2.optional.*;
import java.sql.*;
import javax.sql.*;
import java.util.*;

public class InfoLab extends Application
{
   TextArea mainText,mainText2,mainText5;
   TextField inputField;
   Button OKbutton,B1,A1,U1;
   ListView<String> list1,list2,list3;
   ChoiceBox<String> firstLetterLastNameChoice, DateChoice;
   ObservableList<String> items =FXCollections.observableArrayList ();
   ObservableList<String> items2 =FXCollections.observableArrayList ();
   ObservableList<String> items3 =FXCollections.observableArrayList ();

   String[] letterChoices = {"Alice", "S", "P", "L","Q","R","T","W"};
   String[] letterChoices1 = {"Alice", "S", "P", "L","Q","R","T","W"};
    // Put JDBC variable for connection here
    Connection con;

   public void start(Stage primaryStage)
   {
      Font mainFont = new Font("courier", 24);
      mainText = new TextArea();
      mainText.setFont(mainFont);
      mainText.setPrefRowCount(3);
      mainText.setPrefColumnCount(8);
      mainText.setWrapText(true);
//
        list1 = new ListView<String>();
  //    list1.setFont(mainFont);
        list1.setPrefWidth(250);
        list1.setPrefHeight(150);
  //    list1.setWrapText(true);
        list1.setItems(items);

        list2 = new ListView<String>();
        list2.setPrefWidth(250);
        list2.setPrefHeight(150);
        list2.setItems(items2);

        list3 = new ListView<String>();
        list3.setPrefWidth(250);
        list3.setPrefHeight(150);
        list3.setItems(items3);

      mainText5 = new TextArea();
      mainText5.setFont(mainFont);
      mainText5.setPrefRowCount(4);
      mainText5.setPrefColumnCount(8);
      mainText5.setWrapText(true);
      // open the JDBC connection, get the list of first letters
      // of Customer last names, overwriting letterChoices.  On exception write error msg
      // to mainText.

       try {

           MysqlDataSource mds = new MysqlDataSource();
    
           // in the connect string below, leave it as cs_owen because this
           // is the database that has table CUSTOMER

           mds.setURL("jdbc:mysql://pizza.unbsj.ca:3306/cs_JGUO6?useSSL=false");

	   // but update the MySQL username and password to be yours
           // n.b. including credentials in source code is HORRIBLE security
           // so don't do this in real life

           mds.setUser("JGUO6");  // MySQL username
           mds.setPassword("alien_replica");   //MySQL password

           // make the JDBC connection to server
           con=mds.getConnection();
           
           Statement s=con.createStatement();
           ResultSet r=s.executeQuery("select * from JUDGE");

           ArrayList<String> realLetterChoices = new ArrayList<>();

           while (r.next())
             realLetterChoices.add(r.getString("JUDGE_NAME")); 

           // bad style: should be in a finally block, really
           r.close(); s.close();  // at least we are doing closes if there is no exception
	   
          // realLetterChoices.add("Any"); 

           //  for(String i:letterChoices){
          //       realLetterChoices.add(i);
           //   }
           // move them over into an array
           letterChoices = new String[realLetterChoices.size()];
	   int ctr=0;
           for (String choice : realLetterChoices)
             letterChoices[ctr++]=choice;
   // ----------         
           Statement s1=con.createStatement();
           ResultSet r1=s1.executeQuery("select distinct CONTEST_DECISION_DATE from CONTEST");

           ArrayList<String> realLetterChoices1 = new ArrayList<>();

           while (r1.next())
             realLetterChoices1.add(r1.getString("CONTEST_DECISION_DATE")); 
           r1.close(); s1.close();  // at least we are doing closes if there is no exception

           // move them over into an array
           letterChoices1 = new String[realLetterChoices1.size()];
	   int ctr1=0;
           for (String choice1 : realLetterChoices1)
             letterChoices1[ctr1++]=choice1;

       } catch (Exception e) {
           mainText.setText("error "+e);
       }

     // inputField = new TextField();
     // inputField.setFont(mainFont);
     // inputField.setMinWidth(500);
     // inputField.setText("input area code here (try 615 or 713)");
      //inputField.setOnAction(this::processTextInput);  // no action

      // code for choice box      
      firstLetterLastNameChoice = new ChoiceBox<String>();
      firstLetterLastNameChoice.setStyle("-fx-font: 24px \"Courier\";");  
      firstLetterLastNameChoice.getItems().addAll(letterChoices);
      firstLetterLastNameChoice.getSelectionModel().select(0);
//-------------------------
      DateChoice = new ChoiceBox<String>();
      DateChoice.setStyle("-fx-font: 24px \"Courier\";");  
      DateChoice.getItems().addAll(letterChoices1);
      DateChoice.getSelectionModel().select(0);

      // code for CustomerSearch  button and label
      // The action for this button will eventually search for customers in the specified
      // area code whose last names start with the chosen letter

      // Only create a functioning button if we have a connection
      if (con != null) {
        OKbutton = new Button("      ");
        OKbutton.setFont(mainFont);
        OKbutton.setOnAction(this::processButton);
      }
      else {
         OKbutton = new Button("Oops No Connection");
         OKbutton.setFont(mainFont);
      }
      B1= new Button("Award");
      B1.setFont(mainFont);
      B1.setOnAction(this::processButton1);
      A1= new Button("Get Assignment");
      A1.setFont(mainFont);
      A1.setOnAction(this::processButton2);

      U1= new Button("Undo");
      U1.setFont(mainFont);
    //  U1.setOnAction(this::processButton3);

      Label lbl = new Label("Judge name");
      Label lb2 = new Label("Date");
      Label lb3 = new Label("Status Box");
      Label lb4 = new Label("Assignments Today");
      Label lb5 = new Label("Contest Rules");
      Label lb6 = new Label("Prizes");
      Label lb7 = new Label("Contest Entries");
      Label lb8 = new Label("Prize Details");
      lbl.setFont(mainFont);
      lb2.setFont(mainFont);
      lb3.setFont(mainFont);
      lb4.setFont(mainFont);
      lb5.setFont(mainFont);
      lb6.setFont(mainFont);
      lb7.setFont(mainFont);
      lb8.setFont(mainFont);
     
	lbl.setLayoutX(100);
	lbl.setLayoutY(100);
	firstLetterLastNameChoice.setLayoutX(100);
	firstLetterLastNameChoice.setLayoutY(150);
	lb2.setLayoutX(100);
	lb2.setLayoutY(200);
        DateChoice.setLayoutX(100);
	DateChoice.setLayoutY(250);
        A1.setLayoutX(100);
	A1.setLayoutY(350);
        lb3.setLayoutX(100);
	lb3.setLayoutY(400);
	OKbutton.setLayoutX(100);
	OKbutton.setLayoutY(450);
	B1.setLayoutX(100);
	B1.setLayoutY(550);
	U1.setLayoutX(100);
	U1.setLayoutY(700);

        lb4.setLayoutX(450);
	lb4.setLayoutY(100);
        mainText.setLayoutX(450);
	mainText.setLayoutY(340);
        lb5.setLayoutX(450);
	lb5.setLayoutY(300);
        list1.setLayoutX(450);
	list1.setLayoutY(140);
        lb6.setLayoutX(450);
	lb6.setLayoutY(550);
        list2.setLayoutX(450);
	list2.setLayoutY(600);

        lb7.setLayoutX(800);
	lb7.setLayoutY(300);
        list3.setLayoutX(800);
	list3.setLayoutY(340);
        lb8.setLayoutX(800);
	lb8.setLayoutY(550);
        mainText5.setLayoutX(800);
	mainText5.setLayoutY(600);

     // putting it all together
      Pane pane = new Pane(lbl,firstLetterLastNameChoice,DateChoice,lb2,lb3,list1,list3,
                           OKbutton,mainText,list2,lb4,lb5,lb6,lb7,lb8,mainText5,
                           B1,A1,U1);
     // pane.setPadding(new Insets(10, 50, 50, 50));
     // pane.setSpacing(10);
     // pane.setLayout(null);
      Scene theScene = new Scene(pane, 1200,900);
      primaryStage.setTitle("INFO 1103 Lab GUI");
      primaryStage.setScene(theScene);

      primaryStage.show();
  }
// award
      public void processButton1(ActionEvent event)
   {
     try {

           MysqlDataSource mds = new MysqlDataSource();

           mds.setURL("jdbc:mysql://pizza.unbsj.ca:3306/cs_JGUO6?useSSL=false");

           mds.setUser("JGUO6");  // MySQL username
           mds.setPassword("alien_replica");   //MySQL password

           // make the JDBC connection to server
           con=mds.getConnection();
           String sql = "{CALL T1(?,?,?,?)}"; //调用存储过程
           CallableStatement cstm = con.prepareCall(sql); //实例化对象cstm
           cstm.setInt(1, 1);
           cstm.setInt(2, 3); 
           cstm.setInt(3, 1);//
           cstm.registerOutParameter(4, Types.INTEGER); // 设置返回值类型
           cstm.execute(); // 执行存储过程
           //System.out.println(cstm.getInt(4));
           if(cstm.getInt(4)==1){
           OKbutton.setText("is not eligible");
           }else if(cstm.getInt(4)==2){
           OKbutton.setText("already been awarded");
           }else if(cstm.getInt(4)==3){
           OKbutton.setText("out of copies");
           }else{
           OKbutton.setText("success");
           }
           cstm.close();
         } catch (Exception e) {
           OKbutton.setText("error "+e);
         }
   }
//undo ???????
      public void processButton3(ActionEvent event)
   {
     try {

           MysqlDataSource mds = new MysqlDataSource();

           mds.setURL("jdbc:mysql://pizza.unbsj.ca:3306/cs_JGUO6?useSSL=false");

           mds.setUser("JGUO6");  // MySQL username
           mds.setPassword("alien_replica");   //MySQL password

           // make the JDBC connection to server
           con=mds.getConnection();
           String sql = "{CALL U1(?,?,?,?)}"; //调用存储过程
           CallableStatement cstm = con.prepareCall(sql); //实例化对象cstm
           cstm.setInt(1, 1);
           cstm.setInt(2, 3); 
           cstm.setInt(3, 1);//
           cstm.registerOutParameter(4, Types.INTEGER); // 设置返回值类型
           cstm.execute(); // 执行存储过程

           if(cstm.getInt(4)==1){
           OKbutton.setText("NOT undo");
           }else{
           OKbutton.setText("undo");
           }
           cstm.close();
         } catch (Exception e) {
           OKbutton.setText("error "+e);
         }

   }


   // code for the Fire button, needs to be completed...
   public void processButton(ActionEvent event)
   {
      String msg = "";  // the formatted result of the query should be put in this string
      String usersChoiceForFirstLetterOfLastName = firstLetterLastNameChoice.getSelectionModel().getSelectedItem();

      // Do a JDBC lookup here and send the output to mainText via msg
      // ... code to be written:
      // You already have Connection con set up from earlier.

 
      // form and execute a query like
      //   select ... from cs_owen.CUSTOMER where CUS_LNAME like 'Z%'
      //      AND CUS_AREACODE=506
      
      // but if user chose "Any" for first letter of last name, 
      //   you should omit the      CUS_LNAME like 'Z%' in above example
      //   and do the selection only on the basis of CUS_AREACODE

      String q = "select * from cs_owen.CUSTOMER where "; 

      // code for students to write next...to handle the first letters
      // of the name, which can include Any

      if (!usersChoiceForFirstLetterOfLastName.equals("Any")) {
          // student code to go here: replace "" with something more interesting
          q += "cus_lname like '"+usersChoiceForFirstLetterOfLastName+"%' and ";
      }
      q += "CUS_AREACODE=?";

      // query string is now complete and ready to be passed to the database
      // form a Statement, do an executeQuery
        try{
           PreparedStatement s=con.prepareStatement(q);
           s.setString(1,inputField.getText());
           ResultSet r=s.executeQuery();

            while (r.next())
             msg += "\n"+r.getString("cus_lname") + " , " +r.getString("cus_fname");

           // bad style: should be in a finally block, really
           r.close(); s.close();  // at least we are doing closes if there is no exception
	  }catch(Exception e){msg= ""+e;}

      // iterate over the resulting ResultSet
      // output chosen stuff about person (fname, lname, phone num) to msg

      // ONLY ONE OF THE BELOW SHOULD BE USED

     // mainText.setText("Query: " +q); // to debug
      mainText.setText(msg);  // TO SEE THE OUTPUT
   }
// assigenment today
  public void processButton2(ActionEvent event)
   {      
     try{
           MysqlDataSource mds = new MysqlDataSource();

           mds.setURL("jdbc:mysql://pizza.unbsj.ca:3306/cs_JGUO6?useSSL=false");

           mds.setUser("JGUO6");  // MySQL username
           mds.setPassword("alien_replica");   //MySQL password

           // make the JDBC connection to server
           con=mds.getConnection();


          String CName = firstLetterLastNameChoice.getSelectionModel().getSelectedItem();
          String CDate = DateChoice.getSelectionModel().getSelectedItem();

          String q1 = "select CONTEST_TITLE from CONTEST where JUDGE_ID= (select JUDGE_ID from JUDGE where JUDGE_NAME='"; 
          q1 += CName+ "') and CONTEST_DECISION_DATE= '"+CDate+"'";
   //      mainText3.setText(q1);

           PreparedStatement s=con.prepareStatement(q1);
           ResultSet r=s.executeQuery();

            while (r.next())
            items.add(r.getString("CONTEST_TITLE"));

           r.close(); s.close();  
//  -----------------------?????????
   /*      String q2 = "select count(CONTEST_TITLE) from CONTEST where JUDGE_ID= (select JUDGE_ID from JUDGE where JUDGE_NAME='"; 
          q2 += CName+ "') and CONTEST_DECISION_DATE= '"+CDate+"'";
           PreparedStatement s1=con.prepareStatement(q2);
           ResultSet r1=s.executeQuery();
      
         OKbutton.setText(r.getString("count(CONTEST_TITLE)"));
     */  

	 }catch(Exception e){OKbutton.setText("error");}
         
// -------------------?????
      try{
           MysqlDataSource mds = new MysqlDataSource();

           mds.setURL("jdbc:mysql://pizza.unbsj.ca:3306/cs_JGUO6?useSSL=false");

           mds.setUser("JGUO6");  // MySQL username
           mds.setPassword("alien_replica");   //MySQL password

           // make the JDBC connection to server
           con=mds.getConnection();

          String CC = list1.getSelectionModel().getSelectedItem();
          String q3 = "select CONTEST_RULES from CONTEST where CONTEST_TITLE='"; 
          q3 += CC+ "'";
          PreparedStatement s=con.prepareStatement(q3);
          ResultSet r=s.executeQuery();
           mainText2.setText(q3);

	 }catch(Exception e){OKbutton.setText("error");}
   }


   public static void main(String[] args)
   {
      launch(args);
   }
}



