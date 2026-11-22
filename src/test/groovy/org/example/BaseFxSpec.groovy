package org.example

import javafx.application.Application
import org.testfx.api.FxRobot
import org.testfx.api.FxToolkit
import spock.lang.Specification

import java.util.function.Supplier

class BaseFxSpec extends Specification {

    // Робот TestFX, через которого мы будем кликать и вводить текст в тестах
    @Delegate
    FxRobot robot = new FxRobot()

    def setup() {
        // Запускаем наше приложение перед каждым тестом
        FxToolkit.registerPrimaryStage()
        // Передаём замыкание (Supplier). которое возвращает новый экземпляр App
        FxToolkit.setupApplication({ new App() } as Supplier<Application>)
    }

    def cleanup() {
        // Закрываем окна и очищаем состояние после каждого теста
        FxToolkit.cleanupStages()
    }
}
