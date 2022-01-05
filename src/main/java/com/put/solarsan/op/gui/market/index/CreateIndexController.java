package com.put.solarsan.op.gui.market.index;

import com.put.solarsan.op.asset.Company;
import com.put.solarsan.op.gui.common.AbstractCreateController;
import com.put.solarsan.op.market.StockMarket;
import com.put.solarsan.op.market.index.Index;
import com.put.solarsan.op.market.index.MarketIndex;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CreateIndexController extends AbstractCreateController<Index> {

    private enum TYPE {
        STATIC, DYNAMIC;
    }

    private StockMarket market;
    private Map<String, Company> companies;

    @Override
    protected void init() {
        nameTextField.setText("malyWig");
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

    @FXML protected TextField nameTextField;
    @FXML protected ChoiceBox<TYPE> typeChoiceBox;
    @FXML protected TextField valueTextField;
    @FXML protected MenuButton companyMenuButton;

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

    public void setMarket(StockMarket market) {
        this.market = market;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = new HashMap<>();
        companies.forEach(x -> this.companies.put(x.toString(), x));
    }
}
