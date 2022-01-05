package com.antsol.op.gui.market.index;

import com.antsol.op.asset.Company;
import com.antsol.op.gui.common.AbstractCreateController;
import com.antsol.op.market.StockMarket;
import com.antsol.op.market.index.Index;
import com.antsol.op.market.index.MarketIndex;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CreateIndexController extends AbstractCreateController<Index> {

    @FXML
    private TextField nameTextField;
    @FXML
    private ChoiceBox<TYPE> typeChoiceBox;
    @FXML
    private TextField valueTextField;
    @FXML
    private MenuButton companyMenuButton;
    @Setter
    private StockMarket market;
    private Map<String, Company> companies;

    @Override
    protected void init() {
        nameTextField.setText(randomDataSupplier.pokemon().name());
        typeChoiceBox.setItems(FXCollections.observableList(List.of(TYPE.values())));
        typeChoiceBox.setOnAction((x) -> onChoiceTypeAction());
        valueTextField.setDisable(true);
        valueTextField.setOnAction((x) -> enableCreate());
        companyMenuButton.setDisable(true);
        companyMenuButton.getItems().setAll(companies.keySet().stream()
                .map(CheckMenuItem::new).collect(Collectors.toList()));
        companyMenuButton.getItems().forEach(x -> x.setOnAction(y -> enableCreate()));
        dialogPane.lookupButton(ButtonType.OK).setDisable(true);
    }

    private void onChoiceTypeAction() {
        if (typeChoiceBox.getValue().equals(TYPE.STATIC)) {
            valueTextField.setDisable(true);
            companyMenuButton.setDisable(false);
        } else {
            companyMenuButton.setDisable(true);
            valueTextField.setDisable(false);
        }
    }

    private void enableCreate() {
        dialogPane.lookupButton(ButtonType.OK).setDisable(false);
    }

    @Override
    public Index create() {
        if (typeChoiceBox.getValue().equals(TYPE.STATIC)) {
            final Index index = new Index(nameTextField.getText(), market, market.getSettings());
            companyMenuButton.getItems().stream().filter(x -> ((CheckMenuItem) x).isSelected())
                    .map(x -> companies.get(x.getText())).forEach(index::add);
            return index;
        } else {
            return new MarketIndex(nameTextField.getText(), market, market.getSettings(), Integer.parseInt(valueTextField.getText()));
        }
    }

    public void setCompanies(List<Company> companies) {
        this.companies = new HashMap<>();
        companies.forEach(x -> this.companies.put(x.toString(), x));
    }

    private enum TYPE {
        STATIC, DYNAMIC
    }
}
