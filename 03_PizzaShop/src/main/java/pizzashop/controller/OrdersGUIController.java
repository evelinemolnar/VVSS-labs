package pizzashop.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import pizzashop.exceptions.MenuException;
import pizzashop.exceptions.PaymentException;
import pizzashop.model.MenuDataModel;
import pizzashop.gui.OrdersGUI;
import pizzashop.service.PaymentAlert;
import pizzashop.service.PizzaService;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrdersGUIController {
    @FXML
    private ComboBox<Integer> orderQuantity;
    @FXML
    private TableView orderTable;
    @FXML
    private TableColumn tableQuantity;
    @FXML
    protected TableColumn tableMenuItem;
    @FXML
    private TableColumn tablePrice;
    @FXML
    private Label pizzaTypeLabel;
    @FXML
    private Button addToOrder;
    @FXML
    private Label orderStatus;
    @FXML
    private Button placeOrder;
    @FXML
    private Button orderServed;
    @FXML
    private Button payOrder;
    @FXML
    private Button newOrder;

    private   List<String> orderList = FXCollections.observableArrayList();
    private List<Double> orderPaymentList = FXCollections.observableArrayList();
    public static double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    private PizzaService service;
    private int tableNumber;

    public ObservableList<String> observableList;
    private TableView<MenuDataModel> table = new TableView<MenuDataModel>();
    private ObservableList<MenuDataModel> menuData;// = FXCollections.observableArrayList();
    private Calendar now = Calendar.getInstance();
    private static double totalAmount;

    public OrdersGUIController(){ }

    public void setService(PizzaService service, int tableNumber) throws MenuException {
        this.service=service;
        this.tableNumber=tableNumber;
        initData();
    }

    private void initData() throws MenuException {
        menuData = FXCollections.observableArrayList(service.getMenuData());
        menuData.setAll(service.getMenuData());
        orderTable.setItems(menuData);

        //Controller for Place Order Button
        placeOrder.setOnAction(event ->{
            orderList= menuData.stream()
                    .filter(x -> x.getQuantity()>0)
                    .map(menuDataModel -> menuDataModel.getQuantity() +" "+ menuDataModel.getMenuItem())
                    .collect(Collectors.toList());
            observableList = FXCollections.observableList(orderList);
            KitchenGUIController.order.add("Table" + tableNumber +" "+ orderList.toString());
            orderStatus.setText("Order placed at: " +  now.get(Calendar.HOUR)+":"+now.get(Calendar.MINUTE));
        });

        //Controller for Order Served Button
        orderServed.setOnAction(event -> {orderStatus.setText("Served at: " + now.get(Calendar.HOUR)+":"+now.get(Calendar.MINUTE));
        });

        //Controller for Pay Order Button
        payOrder.setOnAction(event -> {
            orderPaymentList= menuData.stream()
                    .filter(x -> x.getQuantity()>0)
                    .map(menuDataModel -> menuDataModel.getQuantity()*menuDataModel.getPrice())
                    .collect(Collectors.toList());
            setTotalAmount(orderPaymentList.stream().mapToDouble(e->e.doubleValue()).sum());
            orderStatus.setText("Total amount: " + getTotalAmount());
            System.out.println("--------------------------");
            System.out.println("Table: " + tableNumber);
            System.out.println("Total: " + getTotalAmount());
            System.out.println("--------------------------");
            PaymentAlert pay = new PaymentAlert(service);
            try {
                pay.showPaymentAlert(tableNumber, this.getTotalAmount());
            } catch (PaymentException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void initialize(){

        //populate table view with menuData from OrderGUI
        table.setEditable(true);
        tableMenuItem.setCellValueFactory(
                new PropertyValueFactory<MenuDataModel, String>("menuItem"));
        tablePrice.setCellValueFactory(
                new PropertyValueFactory<MenuDataModel, Double>("price"));
        tableQuantity.setCellValueFactory(
                new PropertyValueFactory<MenuDataModel, Integer>("quantity"));

        //bind pizzaTypeLabel and quantity combo box with the selection on the table view
        orderTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MenuDataModel>() {
        @Override
        public void changed(ObservableValue<? extends MenuDataModel> observable, MenuDataModel oldValue, MenuDataModel newValue) {
           pizzaTypeLabel.textProperty().bind(newValue.menuItemProperty());
              }
        });

        //Populate Combo box for Quantity
        ObservableList<Integer> quantityValues =  FXCollections.observableArrayList(0, 1, 2,3,4,5);
        orderQuantity.getItems().addAll(quantityValues);
        orderQuantity.setPromptText("Quantity");

        //Controller for Add to order Button
        addToOrder.setOnAction(event -> {
            Integer quantity = orderQuantity.getValue();
            if (quantity == null) {
                // Dacă nu a fost selectată nicio cantitate, puteți seta o valoare implicită sau afișa un mesaj de eroare
                System.out.println("Vă rugăm să selectați o cantitate.");
                // O opțiune ar putea fi să folosiți un dialog de alertă pentru a informa utilizatorul
                Alert alert = new Alert(Alert.AlertType.WARNING, "Vă rugăm să selectați o cantitate pentru a adăuga la comandă.", ButtonType.OK);
                alert.showAndWait();
                return; // Încetați executarea metodei pentru a evita erorile ulterioare
            }
            orderTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MenuDataModel>(){
            @Override
            public void changed(ObservableValue<? extends MenuDataModel> observable, MenuDataModel oldValue, MenuDataModel newValue){
            oldValue.setQuantity(orderQuantity.getValue());
            orderTable.getSelectionModel().selectedItemProperty().removeListener(this);
                }
            });
        });

        //Controller for Exit table Button
        newOrder.setOnAction(event -> {
            Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION, "Exit table?",ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = exitAlert.showAndWait();
            if (result.get() == ButtonType.YES){
                Stage stage = (Stage) newOrder.getScene().getWindow();
                stage.close();
                }
        });
    }
}