package org.example

import javafx.scene.control.Button

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
}
