# Используем базовый образ Ubuntu
FROM ubuntu:22.04

# Устанавливаем переменные окружения для non-interactive режима
ENV DEBIAN_FRONTEND=noninteractive
ENV VITE_API_URL=""

# Обновляем пакеты и устанавливаем необходимые инструменты
RUN apt-get update && apt-get install -y \
    curl \
    gnupg \
    software-properties-common \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

# Установка последней версии OpenJDK
RUN add-apt-repository -y ppa:openjdk-r/ppa && \
    apt-get update && \
    apt-get install -y openjdk-23-jdk && \
    rm -rf /var/lib/apt/lists/*

# Установка последней версии Node.js
RUN curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs && \
    rm -rf /var/lib/apt/lists/*

# Установка Maven
RUN curl -fsSL https://downloads.apache.org/maven/maven-3/3.9.5/binaries/apache-maven-3.9.5-bin.tar.gz -o /tmp/maven.tar.gz && \
    tar -xvzf /tmp/maven.tar.gz -C /opt && \
    ln -s /opt/apache-maven-3.9.5/bin/mvn /usr/bin/mvn && \
    rm /tmp/maven.tar.gz

# Установка переменных окружения для Maven
ENV MAVEN_HOME=/opt/apache-maven-3.9.5
ENV PATH=$MAVEN_HOME/bin:$PATH

# Проверяем версии Java и Node.js
RUN java -version && node -v && npm -v

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем проект в контейнер
COPY . .

RUN (cd /app/src/main/resources/frontend/;npm i && npm run build-only)
RUN mvn clean install

# Устанавливаем пользовательские команды, если требуется
CMD ["java" ,"-jar", "target/flashcards-0.0.1.jar"]