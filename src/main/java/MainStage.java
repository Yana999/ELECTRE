
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.util.LinkedList;
import java.util.Optional;

public class MainStage {
MethodRealisation electre = new MethodRealisation();
    @FXML
    private AnchorPane table;

    @FXML
    private Button addCritery;

    @FXML
    private Button addAlteretivy;

    @FXML
    private Button scale;

    @FXML
    private Button weight;

    @FXML
    private  int scaleLengh;

    private LinkedList<TextField> fields = new LinkedList<>(); //список полей таблицы дл€ критериев
    private int columnCount = electre.getCriteriesCount();
    private int rowsCount = electre.getAlternativesCount();
    private LinkedList<TextField> scales = new LinkedList<>(); //список дл€ длин шкал
    private LinkedList<TextField> weights = new LinkedList<>(); //список дл€ весов
    private LinkedList<TextField> suggestions = new LinkedList<>(); //список дл€ значений гипотез о превосходстве гипотез
    boolean isSaveValues = false;
    private TextField alpha;
    private TextField beta;

    @FXML
    private void initialize (){
        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                TextField field = new TextField();
                field.setPrefWidth(40);
                field.setPrefHeight(40);
                field.setLayoutY(40+45 * i);
                field.setLayoutX(120+45*j);
                fields.add(field);
                table.getChildren().add(field);

                Label crit = new Label();
                crit.setText(" р. " + (j+1));
                crit.setLayoutX(120 + 45*j);
                crit.setLayoutY(10);
                table.getChildren().add(crit);
            }
            Label al = new Label();
            al.setText("јльтер." + (i+1));
            al.setLayoutY(40 + 45*i);
            al.setLayoutX(10);
            table.getChildren().add(al);
        }
    }

    @FXML
    private void addColumn(){ // добавление пол€ дл€ внесени€ критери€ при нажатии на кнопку
        if(!isSaveValues) {
            for (int i = 0; i < rowsCount; ++i) {
                TextField field = new TextField();
                field.setPrefWidth(40);
                field.setPrefHeight(40);
                field.setLayoutY(40 + 45 * i);
                field.setLayoutX(120 + 45 * columnCount);
                fields.add(field);
                table.getChildren().add(field);
            }
            Label crit = new Label();
            crit.setText(" р. " + (columnCount + 1));
            crit.setLayoutX(120 + 45 * columnCount);
            crit.setLayoutY(10);
            table.getChildren().add(crit);

            electre.setCriteriesCount(++columnCount);
        }
    }

    @FXML
    private void addRow(){// добавление пол€ дл€ внесени€ альтернативы при нажатии на кнопку
        if(!isSaveValues) {
            for (int i = 0; i < columnCount; ++i) {
                TextField field = new TextField();
                field.setPrefWidth(40);
                field.setPrefHeight(40);
                field.setLayoutY(40 + 45 * rowsCount);
                field.setLayoutX(120 + 45 * i);
                fields.add(field);
                table.getChildren().add(field);

            }
            Label al = new Label();
            al.setText("јльтер." + (rowsCount + 1));
            al.setLayoutY(40 + 45 * rowsCount);
            al.setLayoutX(10);
            table.getChildren().add(al);
            electre.setAlternativesCount(++rowsCount);
        }
    }

    private double [][] getDataMatrix(){
        double data [][] = new double[rowsCount][columnCount];
        for (int i = 0; i < rowsCount;++i){
            for(int j = 0; j < columnCount; ++j){
                int x = (int)((fields.get(i*columnCount + j).getLayoutX()-120)/45);
                int y = (int)((fields.get(i*columnCount + j).getLayoutY()-40)/45);
                data[y][x] = Double.parseDouble(fields.get(i*columnCount + j).getText());
            }
        }
        System.out.println("matrix:");
        for (int i = 0; i < rowsCount;++i){
            for(int j = 0; j < columnCount; ++j){
                System.out.print(data[i][j]);
            }
            System.out.println();
        }
        return data;
    }

    @FXML
    private void saveValues(){
        isSaveValues = true;
        electre.setMatrix(getDataMatrix()); // сохранение значений альт и крит
        electre.countScales(); // подсчет длин шкал
        double [] out =  electre.getScales();
        for (int  i = 0; i < columnCount; i ++){  // вывод расчитанных длин шкал
            TextField field = new TextField();
            field.setPrefWidth(40);
            field.setPrefHeight(40);
            field.setLayoutY(70 + 45 * rowsCount);
            field.setLayoutX(120+45*i);
            field.setText(String.valueOf(out[i]));
            scales.add(field);
            table.getChildren().add(field);
        }
        Label additionalName = new Label();
        additionalName.setText("ƒлины шкал:");
        additionalName.setLayoutX(10);
        additionalName.setLayoutY(70 + 45 * rowsCount);
        table.getChildren().add(additionalName);
    }

    @FXML
    private void saveScale(){
        double [] userScales = new double[columnCount]; // длины шкал, отредактированные пользователем
        for (int i = 0; i < columnCount; i++){
            userScales[i] = Double.parseDouble(scales.get(i).getText());
        }
        electre.setScales(userScales);

        for (int  i = 0; i < columnCount; i ++){ // вывод полей дл€ весов
            TextField field = new TextField();
            field.setPrefWidth(40);
            field.setPrefHeight(40);
            field.setLayoutY(70 + 45 * (rowsCount+1));
            field.setLayoutX(120+45*i);
            weights.add(field);
            table.getChildren().add(field);
        }
        Label additionalName = new Label();
        additionalName.setText("¬еса критериев:");
        additionalName.setLayoutX(10);
        additionalName.setLayoutY(70 + 45 * (rowsCount+1));
        table.getChildren().add(additionalName);
    }

    @FXML
    private void saveWeight(){
        double [] userWeghts = new double[weights.size()]; // веса, внесенные пользователем
        for (int i = 0; i < weights.size(); i++){
            userWeghts[i] = Double.parseDouble(weights.get(i).getText());
        }
        electre.setWieghts(userWeghts);

        for (int  i = 0; i < columnCount; i ++){ // вывод полей дл€ гипотез
            TextField field = new TextField();
            field.setPrefWidth(40);
            field.setPrefHeight(40);
            field.setLayoutY(70 + 45 * (rowsCount+2));
            field.setLayoutX(120+45*i);
            suggestions.add(field);
            table.getChildren().add(field);
        }
        Label additionalName = new Label();
        additionalName.setText("√ипотезы о превосходстве альтернатив:");
        additionalName.setLayoutX(10);
        additionalName.setPrefWidth(105);
        additionalName.setLayoutY(70 + 45 * (rowsCount+2));
        table.getChildren().add(additionalName);
    }

    @FXML
    private void saveSug(){
        int [] userSug = new int[columnCount]; // гипотезы, внесенные пользователем
        for (int i = 0; i < columnCount; i++){
            if(suggestions.get(i).getText().equals("+")) {
                userSug[i] = 1;
            } else if (suggestions.get(i).getText().equals("-")){
                userSug[i] = -1;
            }
        }
        electre.setSuggestions(userSug);

        alpha = new TextField();// поле дл€ задани€ уровн€ согласи€
        alpha.setPrefWidth(40);
        alpha.setPrefHeight(40);
        alpha.setLayoutY(70 + 45 * (rowsCount+3));
        alpha.setLayoutX(120);
        table.getChildren().add(alpha);
        Label alphaName = new Label();
        alphaName.setText("”ровень согласи€:");
        alphaName.setLayoutX(10);
        alphaName.setPrefWidth(105);
        alphaName.setLayoutY(70 + 45 * (rowsCount+3));
        table.getChildren().add(alphaName);

        beta = new TextField();// поле дл€ задани€ уровн€ несогласи€
        beta.setPrefWidth(40);
        beta.setPrefHeight(40);
        beta.setLayoutY(70 + 45 * (rowsCount+4));
        beta.setLayoutX(120);
        table.getChildren().add(beta);
        Label betaName = new Label();
        betaName.setText("”ровень несогласи€:");
        betaName.setLayoutX(10);
        betaName.setPrefWidth(105);
        betaName.setLayoutY(70 + 45 * (rowsCount+4));
        table.getChildren().add(betaName);
    }

    @FXML
    private void solution(){
        Label result = new Label();
        result.setText(electre.solution(alpha,beta));
        result.setLayoutY(70 + 45 * (rowsCount+5));
        result.setLayoutX(10);
        table.getChildren().add(result);

        /*Alert alert = new Alert(Alert.AlertType.CONFIRMATION);               // ћожешь попробовать раскоментить и посмотреть, не знаю, как лучше сделать
        alert.setTitle("–езультат");
        alert.setHeaderText("∆елаете продолжить расчет с новыми значени€ми альфа и бета?");

        // option != null.
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == null) {
        } else if (option.get() == ButtonType.OK) {
            alpha.clear();
            beta.clear();
            result.setText("");
        }*/
    }
}
