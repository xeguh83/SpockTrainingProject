package org.example

import javafx.collections.ObservableList
import javafx.scene.control.Button
import spock.lang.Unroll

class TaskManagerSpec extends BaseFxSpec {

    def 'Проверка что кнопка "Добавить" заблокирована по-умолчанию'() {
        given: "Открыто главное окно приложения"
        // окно итак открывается перед каждым тестом

        expect: "находим кнопку с id 'addButton' - она должна быть заблокирована"
        lookup("#addButton").queryButton().isDisabled()
    }

    //statusLabel
    def "Текст статуса равен 'Задач: 0'"() {
        given: "Открыто главное окно приложения"
        // окно итак открывается перед каждым тестом

        expect: "находим текст статуса по id statusLabel и проверяем что он равен 'Задач: 0'"
        lookup("#statusLabel").queryLabeled().text == 'Задач: 0'
    }

    def "Добавление новой срочной задачи обновляет список и сбрасывает форму"() {
        given: "Открыто главное окно приложения"
        // окно итак открывается перед каждым тестом

        when: "Вводим купить молоко, ставим флаг Срочно и нажимаем Добавить"
        clickOn("#titleInput").write("Купить молоко")
        clickOn("#urgentCheck")
        clickOn("#addButton")

        then: "Проверяем что задач теперь 1, в таблице 1 определенная строка, очищено поле ввода и кнопка Добавить заблокирована"
        def taskList = lookup("#taskList").queryListView()
        lookup("#statusLabel").queryLabeled().text == 'Задач: 1'
        taskList.items.size() == 1
        taskList.items[0] == "[СРОЧНО] Купить молоко (Средний)"
        lookup("#titleInput").queryTextInputControl().text.isEmpty()
        lookup("#addButton").queryButton().isDisabled()
    }

    @Unroll
    def "При вводе '#inputText' состояние блокировки кнопки Добавить должно быть #expectedDisabled"() {
        given: "Открыто главное окно приложения"
        // окно итак открывается перед каждым тестом

        when: "Добавляем ряд значений в таблицу через текстое поле и кнопку Добавить"
        if (inputText) clickOn("#titleInput").write(inputText)

        then: "Проверяем доступность кнопки Добавить в зависимости от значения в текстовом поле"
        lookup("#addButton").queryButton().isDisabled() == expectedDisabled

        where: "Таблица значений параметрического теста"
        inputText | expectedDisabled
        ""        | true
        "   "     | true
        "АБ"      | true
        "АБВ"     | false
        " Задача" | false
    }

    def "Создание задачи с высоким приоритетом и её удаление после подтверждения"() {
        given: "Создаём запись в таблице 'Создать отчет' с высоким приоритетом"
        clickOn("#priorityBox")
        clickOn("Высокий")
        clickOn("#titleInput").write("Создать отчет")
        clickOn("#addButton")

        expect: "Убедится что кнопка Удалить выбранную заблокирована так как строка таблицы не выделена"
        lookup("#deleteButton").queryButton().isDisabled()

        when: "Выделяем строку в таблице, нажимаем Удалить выбранную и подтверждаем действие в окне Alert"
        clickOn("Создать отчет (Высокий)")
        clickOn("#deleteButton")
        clickOn("Yes")

        then: "Убедится что список пуст, текст статуса 'Задач: 0', а кнопка Удалить выбранную заблокирована"
        lookup("#taskList").queryListView().items.isEmpty()
        lookup("#statusLabel").queryLabeled().text == 'Задач: 0'
        lookup("#deleteButton").queryButton().isDisabled()
    }
}
