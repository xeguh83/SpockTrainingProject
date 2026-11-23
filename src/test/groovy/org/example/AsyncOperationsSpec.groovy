package org.example

import javafx.application.Platform
import javafx.scene.control.DialogPane
import javafx.scene.input.KeyCode
import org.testfx.util.WaitForAsyncUtils
import spock.util.concurrent.PollingConditions

import java.util.concurrent.TimeUnit

class AsyncOperationsSpec extends BaseFxSpec {

    TaskManagerScreen screen = new TaskManagerScreen(robot)

    def "Ожидание окна через WaitForAsyncUtils.waitFor(...)"() {
        given: "Открыто главное окно приложения"
        // окно итак открывается перед каждым тестом

        when: "Нажимаем на кнопку Инфо (500 мс)"
        clickOn(screen.infoButton)

        then: "Проверяем что надпись мгновенно изменилась на Загрузка... и потом появляется окно asyncInfoDialog"
        screen.statusLabel.text == "Загрузка..."
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS) {
            !screen.infoDialog.tryQuery().isEmpty()
        }
        lookup(".dialog-pane").queryAs(DialogPane).isVisible()

        when: "Нажимаем на кнопку ОК и окно alert закрывается"
        push(KeyCode.ENTER)

        then: "Окно alert больше не видно"
        lookup(".dialog-pane").tryQuery().isEmpty()
    }

    def "Ожидание окна через PollingConditions (идиома Spock)"() {
        given: "Открыто главное окно приложения"
        // окно итак открывается перед каждым тестом

        when: "Нажимаем на кнопку Инфо (500 мс)"
        clickOn(screen.infoButton)

        then: "Проверяем что надпись мгновенно изменилась на Загрузка... и потом появляется окно asyncInfoDialog"
        def conditions = new PollingConditions(timeout: 2)
        conditions.eventually {
            assert lookup("#statusLabel").queryLabeled().text == "Инфо получено"
            assert lookup("#asyncInfoDialog").query().isVisible()
        }

        when: "Нажимаем на кнопку ОК и окно alert закрывается"
        push(KeyCode.ENTER)

        then: "Окно alert больше не видно"
        lookup(".dialog-pane").tryQuery().isEmpty()
    }

    def "Изменение UI из тестового потока через WaitForAsyncUtils_asyncFx"() {
        given: "Открыто главное окно приложения"
        // окно итак открывается перед каждым тестом

        when: "Задаём текст в поле ввода внутри обертки WaitForAsyncUtils_asyncFx"
        WaitForAsyncUtils.asyncFx {
            screen.titleInput.setText("Быстрый ввод")
        }.get()

        then: "Проверим что кнопка Добавить разблокировалась"
        !screen.addButton.isDisabled()
    }

    def "Сброс очереди событий UI через WaitForAsyncUtils_waitForFxEvents"() {
        given: "Открыто главное окно приложения"
        // окно итак открывается перед каждым тестом

        when: "Задаём тест лейбла в UI потоке FX и ожидаем когда все события UI закончатся"
        Platform.runLater {
            screen.statusLabel.setText("Обновлено в очереди")
        }
        WaitForAsyncUtils.waitForFxEvents()

        then: "Проверяем что текст лейбла Обновлено в очереди"
        screen.statusLabel.text == "Обновлено в очереди"
    }

}
