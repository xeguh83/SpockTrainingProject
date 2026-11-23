package org.example

import javafx.scene.control.Button
import javafx.scene.control.Labeled
import javafx.scene.control.ListView
import javafx.scene.control.TextInputControl
import org.testfx.api.FxRobot

class TaskManagerScreen {

    // Подключаем возможности робота TestFX прямо внутрь класса экрана
    @Delegate
    private final FxRobot robot

    TaskManagerScreen(FxRobot robot) {
        this.robot = robot
    }

    // 1. Геттеры для UI элементов (все ID в одном месте)
    Button getAddButton() {
        lookup("#addButton").queryButton()
    }

    Button getDeleteButton() {
        lookup("#deleteButton").queryButton()
    }

    TextInputControl getTitleInput() {
        lookup("#titleInput").queryTextInputControl()
    }

    Labeled getStatusLabel() {
        lookup("#statusLabel").queryLabeled()
    }

    ListView<String> getTaskList() {
        lookup("#taskList").queryListView()
    }

    // 2. Бизнес-действия пользователя (высокоуровневые методы)
    TaskManagerScreen addTask(String title, String priority = "Средний", boolean urgent = false) {
        if (priority != "Средний") {
            clickOn("#priorityBox")
            clickOn(priority)
        }
        if (urgent) {
            clickOn("#urgentCheck")
        }
        if (title) {
            clickOn(titleInput).write(title)
        }
        if (!addButton.isDisabled()) {
            clickOn(addButton)
        }
        return this // возвращаем this для удобного вызова по цепочке
    }

    TaskManagerScreen inputText(String title) {
        if (title) {
            clickOn(titleInput).write(title)
        }
        return this
    }

    TaskManagerScreen deleteTask(String taskText) {
        clickOn(taskText)
        clickOn(deleteButton)
        clickOn("Yes")
        return this
    }
}
