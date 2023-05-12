package com.example.ecommerse;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class UserInterface {
    GridPane loginPage;
    HBox headerBar;
    HBox footerBar;
    Button signInButton;
    Label welcomeLabel;
    VBox body;

    Customer loggedInCustomer;
    ProductList productList=new ProductList();
    VBox productPage;
    Button placedOrderButton=new Button("Place Order");
    ObservableList<Product>itemsInCart= FXCollections.observableArrayList();
   public BorderPane createContent()
    {
        BorderPane root=new BorderPane();

        root.setPrefSize(800,600);
        //root.getChildren().add(loginPage);//Method to add nodes as as children to pan
        root.setTop(headerBar);
       // root.setCenter(loginPage);
        body=new VBox();
        body.setPadding(new Insets(10));
        body.setAlignment(Pos.CENTER);
         productPage=productList.getAllProducts();
        body.getChildren().add(productPage);
        root.setCenter(body);
        root.setBottom(footerBar);

        return root;
    }
    public  UserInterface()
    {

        createLoginPage();
        createHeaderBar();
        createFooterBar();
    }
    private void createLoginPage()
    {
        Text userNameText=new Text("User Name");
        Text passwordText=new Text("Password");


        TextField userName=new TextField("angad@gmail.com");
        userName.setPromptText("Type Your Your UserName here");
        PasswordField password=new PasswordField();
        password.setText("Pass123");
        password.setPromptText("Type Your Password Here");
        Label messaageLabel=new Label("Hi");

        Button loginButton=new Button("Login");

        loginPage=new GridPane();
        loginPage.setStyle("-fx-background-color:grey;");
        loginPage.setAlignment(Pos.CENTER);
        loginPage.setHgap(10);
        loginPage.setVgap(10);
        loginPage.add(userNameText,0,0);
        loginPage.add(userName,1,0);
        loginPage.add(passwordText,0,1);
        loginPage.add(password,1,1);
        loginPage.add(messaageLabel,0,2);
        loginPage.add(loginButton,1,2);

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String name=userName.getText();
                String pass= password.getText();
                Login login=new Login();
                loggedInCustomer=login.customerLogin(name,pass);
                if(loggedInCustomer!=null)
                {
                    messaageLabel.setText("Welcome "+loggedInCustomer.getName());
                    welcomeLabel.setText("Welcome "+loggedInCustomer.getName());
                    headerBar.getChildren().add(welcomeLabel);
                    body.getChildren().clear();
                    body.getChildren().add(productPage);

                }
                else {
                    messaageLabel.setText("Login Failed!! Please provide correct name and password");
                }
            }
        });

    }
    private void createHeaderBar(){
       Button homeButton=new Button();
        Image image =new Image("C:\\Users\\yoges\\ECommerse\\src\\Ecommerce logo image.jfif");
        ImageView imageView=new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(40);
        imageView.setFitWidth(80);
        homeButton.setGraphic(imageView);

       TextField searchBar=new TextField();
       searchBar.setPromptText("Search here");
       searchBar.setPrefWidth(280); // size of search box

       Button searchButton=new Button("Search");

       signInButton=new Button("SIGN IN");
       welcomeLabel=new Label();

       Button cartButton=new Button("Cart");
       Button orderButton=new Button("Orders");

       headerBar=new HBox(20);
       headerBar.setStyle("-fx-background-color:Silver;");
       headerBar.setPadding(new Insets(10));// keep spacing between search bar from border
       headerBar.setAlignment(Pos.CENTER);
       headerBar.getChildren().addAll(homeButton,searchBar,searchButton,signInButton,cartButton,orderButton);

       signInButton.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent actionEvent) {
               body.getChildren().clear(); //clear everyThing from body(ie product list here)
               body.getChildren().add(loginPage);//put login page;
               headerBar.getChildren().remove(signInButton);
           }
       });
       cartButton.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent actionEvent) {
               body.getChildren().clear();
               VBox prodPage=productList.getProductIncart(itemsInCart);
               prodPage.setAlignment(Pos.CENTER);
               prodPage.setSpacing(10);
               prodPage.getChildren().add(placedOrderButton);
               body.getChildren().add(prodPage);
               footerBar.setVisible(false);//all cases need to be handle for this
           }
       });
       placedOrderButton.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent actionEvent) {
               //need list of product and customer

               if(itemsInCart==null){
                   //please select a product first to place order
                   showdialog("Plese add some products in cart to place order");
                   return;
               }
               if(loggedInCustomer==null)
               {
                   showdialog("Please login first to place order");
                   return;
               }
               int count=Order.placeMultipleOrder(loggedInCustomer,itemsInCart);
               if(count!=0)
               {
                   showdialog("Order for "+count+" products placed Succesfully!");
               }
               else
               {
                   showdialog("Order failed!!");
               }
           }
       });

       homeButton.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent actionEvent) {
               body.getChildren().clear();
               body.getChildren().add(productPage);
               footerBar.setVisible(true);
               if(loggedInCustomer==null && headerBar.getChildren().indexOf(signInButton)==-1)
               {
                  // System.out.println(headerBar.getChildren().indexOf(signInButton));
                       headerBar.getChildren().add(signInButton);
               }
           }
       });
    }
    private void createFooterBar(){


        Button buyNowButton=new Button("BuyNow");
        Button addToCartButton=new Button("add to cart");

        footerBar=new HBox(20);
        footerBar.setStyle("-fx-background-color:Silver;");
        footerBar.setPadding(new Insets(10));// keep spacing between search bar from border
        footerBar.setAlignment(Pos.CENTER);
        footerBar.getChildren().addAll(buyNowButton,addToCartButton);

        buyNowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product=productList.getSelectedProducr();
                if(product==null){
                    //please select a product first to place order
                    showdialog("Plese select a product to place Order");
                    return;
                }
                if(loggedInCustomer==null)
                {
                    showdialog("Please login first to place order");
                    return;
                }
                boolean status=Order.placeOrder(loggedInCustomer,product);
                if(status==true)
                {
                    showdialog("Order placed Succesfully!");
                }
                else
                {
                    showdialog("Order failed!!");
                }
            }
        });

        addToCartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product=productList.getSelectedProducr();
                if(product==null){
                    //please select a product first to place order
                    showdialog("Plese select a product to add it to Cart!");
                    return;
                }
                itemsInCart.add(product);
                showdialog("Selected item has been added to cart Successfully!!");
            }
        });
    }
    private void showdialog(String message){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setTitle("Message");
        alert.showAndWait();
    }
}
