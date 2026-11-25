# 1. Берем официальный образ Linux (Ubuntu/Debian) с предустановленным JDK 21 и Maven 3.9
FROM maven:3.9.9-eclipse-temurin-21

# 2. Обновляем список пакетов и ставим все необходимое для работы JavaFX в headless-режиме:
# - xfvb: виртуальный экран (X virtual Framebuffer)
# - libgtk-3-0, libgl1, libxtst6, libxi6: системные библиотеки графики и ввода которые требуют Glass/JavaFX
# - fonts-dejavu: шрифты с поддержкой кириллицы (чтобы корректно считались размеры кнопок и надписей)
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    xvfb \
    libgtk-3-0 \
    libgl1 \
    libxtst6 \
    libxi6 \
    fonts-dejavu && \
    rm -rf /var/lib/apt/lists/*

# 3. Задаём рабочую папку внутри контейнера
WORKDIR /app