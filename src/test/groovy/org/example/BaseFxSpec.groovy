package org.example

import javafx.application.Application
import org.testfx.api.FxRobot
import org.testfx.api.FxToolkit
import spock.lang.Specification

import java.util.function.Supplier

class BaseFxSpec extends Specification {

    // Активируем виртуальный экран Monocle при передаче флага -Dheadless=true
    static {
        if (Boolean.getBoolean("headless")) {
            System.setProperty("testfx.robot", "glass")
            System.setProperty("testfx.headless", "true")
            System.setProperty("prism.order", "sw")
            System.setProperty("prism.text", "t2k")
            System.setProperty("java.awt.headless", "true")
        }
    }

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
