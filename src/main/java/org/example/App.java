package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        // 1. Элементы формы добавления задачи
        TextField titleInput = new TextField();
        titleInput.setId("titleInput");
        titleInput.setPromptText("Введите название задачи...");

        ComboBox<String> priorityBox = new ComboBox<>();
        priorityBox.setId("priorityBox");
        priorityBox.getItems().addAll("Низкий", "Средний", "Высокий");
        priorityBox.setValue("Средний");

        CheckBox urgentCheck = new CheckBox("Срочно");
        urgentCheck.setId("urgentCheck");

        Button addButton = new Button("Добавить");
        addButton.setId("addButton");
        addButton.setDisable(true); // По умолчанию заблокирована

        // 2. Список задач и статус
        ListView<String> taskList = new ListView<>();
        taskList.setId("taskList");

        Label statusLabel = new Label("Задач: 0");
        statusLabel.setId("statusLabel");

        Button deleteButton = new Button("Удалить выбранную");
        deleteButton.setId("deleteButton");
        deleteButton.setDisable(true);

        // 3. Логика UI (валидация и обработчики)
        titleInput.textProperty().addListener((obs, oldVal, newVal) -> {
            addButton.setDisable(newVal == null || newVal.trim().length() < 3);
        });

        taskList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            deleteButton.setDisable(newVal == null);
        });

        addButton.setOnAction(e -> {
            String title = titleInput.getText().trim();
            String priority = priorityBox.getValue();
            String prefix = urgentCheck.isSelected() ? "[СРОЧНО] " : "";

            taskList.getItems().add(prefix + title + " (" + priority + ")");
            titleInput.clear();
            urgentCheck.setSelected(false);
            statusLabel.setText("Задач: " + taskList.getItems().size());
        });

        deleteButton.setOnAction(e -> {
            String selected = taskList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Удалить задачу?", ButtonType.YES, ButtonType.NO);
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        taskList.getItems().remove(selected);
                        statusLabel.setText("Задач: " + taskList.getItems().size());
                    }
                });
            }
        });

        // 4. Компоновка окна
        HBox inputRow = new HBox(10, titleInput, priorityBox, urgentCheck, addButton);
        HBox bottomRow = new HBox(10, statusLabel, deleteButton);
        VBox root = new VBox(12, inputRow, taskList, bottomRow);
        root.setPadding(new Insets(15));

        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Менеджер задач");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
