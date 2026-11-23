package org.example

import spock.lang.Unroll

class TaskManagerSpec extends BaseFxSpec {

    TaskManagerScreen screen = new TaskManagerScreen(robot)

    def 'Проверка что кнопка "Добавить" заблокирована по-умолчанию'() {
        given: "Открыто главное окно приложения"
        // окно итак открывается перед каждым тестом

        expect: "находим кнопку с id 'addButton' - она должна быть заблокирована"
        screen.addButton.isDisabled()
    }

    //statusLabel
    def "Текст статуса равен 'Задач: 0'"() {
        given: "Открыто главное окно приложения"
        // окно итак открывается перед каждым тестом

        expect: "находим текст статуса по id statusLabel и проверяем что он равен 'Задач: 0'"
        screen.statusLabel.text == 'Задач: 0'
    }

    def "Добавление новой срочной задачи обновляет список и сбрасывает форму"() {
        given: "Открыто главное окно приложения"
        // окно итак открывается перед каждым тестом

        when: "Вводим купить молоко, ставим флаг Срочно и нажимаем Добавить"
        screen.addTask("Купить молоко", "Средний", true)

        then: "Проверяем что задач теперь 1, в таблице 1 определенная строка, очищено поле ввода и кнопка Добавить заблокирована"
        def taskList = screen.taskList
        screen.statusLabel.text == 'Задач: 1'
        taskList.items.size() == 1
        taskList.items[0] == "[СРОЧНО] Купить молоко (Средний)"
        screen.titleInput.text.isEmpty()
        screen.addButton.isDisabled()
    }

    @Unroll
    def "При вводе '#inputText' состояние блокировки кнопки Добавить должно быть #expectedDisabled"() {
        given: "Открыто главное окно приложения"
        // окно итак открывается перед каждым тестом

        when: "Добавляем ряд значений в таблицу через текстое поле и кнопку Добавить"
        if (inputText) screen.inputText(inputText)

        then: "Проверяем доступность кнопки Добавить в зависимости от значения в текстовом поле"
        screen.addButton.isDisabled() == expectedDisabled

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
        screen.addTask("Создать отчет", "Высокий")

        expect: "Убедится что кнопка Удалить выбранную заблокирована так как строка таблицы не выделена"
        screen.deleteButton.isDisabled()

        when: "Выделяем строку в таблице, нажимаем Удалить выбранную и подтверждаем действие в окне Alert"
        screen.deleteTask("Создать отчет (Высокий)")

        then: "Убедится что список пуст, текст статуса 'Задач: 0', а кнопка Удалить выбранную заблокирована"
        screen.taskList.items.isEmpty()
        screen.statusLabel.text == 'Задач: 0'
        screen.deleteButton.isDisabled()
    }
}
