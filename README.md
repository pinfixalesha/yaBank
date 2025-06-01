# Банковское Микросервисное Приложение

## Описание проекта

Проект представляет собой микросервисное приложение "Банк", реализованное с использованием Spring Boot и паттернов микросервисной архитектуры. Приложение предоставляет функциональность для работы с банковскими счетами, включая регистрацию пользователей, управление счетами, переводы между счетами с конвертацией валют и другие операции.

### Функциональность
- Регистрация пользователей.
- Добавление счетов в различных валютах.
- Пополнение и снятие виртуальных денег.
- Переводы между счетами одного пользователя или разных пользователей с конвертацией валют.
- Отслеживание курсов валют.
- Уведомления о выполненных операциях.

## Технологии

- **Язык программирования:** Java 21
- **Фреймворк:** Spring Boot
- **База данных:** PostgreSQL
- **Миграции базы данных:** Liquibase
- **Контейнеризация:** Docker
- **Оркестрация контейнеров:** Docker Compose
- **Архитектура:** Микросервисная
- **Авторизация:** OAuth 2.0 (JWT)
- **Service Discovery и Gateway API:** Eureka
- **Externalized/Distributed Config:** Spring Cloud Config

## Структура микросервисов

1. **Front UI** — Веб-интерфейс приложения.
2. **Accounts** — Сервис управления аккаунтами и счетами.
3. **Cash** — Сервис пополнения и снятия денег.
4. **Transfer** — Сервис перевода денег между счетами.
5. **Exchange** — Сервис конвертации валют.
6. **Exchange Generator** — Сервис генерации курсов валют.
7. **Blocker** — Сервис блокировки подозрительных операций.
8. **Notifications** — Сервис уведомлений.

## Запуск приложения

### Предварительные требования
- Установленный Docker и Docker Compose.
- PostgreSQL (если не используется Docker).
- JDK 21 (если собирается проект локально).

## Установка и запуск приложения

1. Клонирование репозитория

```bash
git clone https://github.com/pinfixalesha/yaBank.git 
```

2. Соберите приложение с помощью Gradle:
   ```bash
   gradle build
   ```
4. Запуск Minikube (если ещё не запущен)
   ```bash
   minikube start
   minikube status
   ```
5. Активируем Docker-окружение Minikube
   ```bash
   minikube docker-env >addenv.bat
   addenv.bat
   ```
3. Сборка и загрузка Docker-образов в Minikube
   ```bash
   docker build -t exchange-generator-application:0.0.1-SNAPSHOT ./exchangeGeneratorApplication
   docker build -t notifications-application:0.0.1-SNAPSHOT ./notificationsApplication
   ```

3) Проверка ingress
   ```bash
   kubectl get pods -n kube-system
   ```
4) Ingress
   ```bash
   kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.10.1/deploy/static/provider/cloud/deploy.yaml
   ```
5) Проверка ingress
   ```bash
   kubectl get svc -n ingress-nginx
   ```
6) Обновление helm чартов
   ```bash
   cd ya-bank
   helm dependency update .
    ```
Запуск
helm install yabank ./


Шаг 5. Проверьте, что сервис запущен и доступен внутри кластера
kubectl get svc


kubectl get pods

Кол-во реплик
kubectl get replicaset -l app=exchange-generator-application

kubectl exec -it yabank-db-0 -- bash
Вывод ключа пароля к БД
kubectl get secret yabank-db -o jsonpath='{.data}'
>'{"postgres-password":"VTdhSXVVN3EyZw=="}'
>echo "VTdhSXVVN3EyZw==" | base64 --decode
U7aIuU7q2g

СУБД настройки не применяются без пересоздания PVC
uninstall проекта найдите и удалите PVC:
kubectl get pvc
Затем удалите и затем install заново
kubectl delete pvc data-yabank-db-0

kubectl port-forward yabank-db-0 5432:5432

   
6. Запуск сервисов Docker Compose:
   ```bash
   docker-compose up --build -d
   ```
Приложение будет доступно по адресу: http://localhost:8080


## Тестирование

Приложение покрыто юнит- и интеграционными тестами с использованием JUnit 5, TestContext Framework и Spring Boot Test. Для запуска тестов выполните:

   ```bash
   gradle test
   ```

### Безопастность

- В проекте реализована система аутентификации и авторизации с использованием Spring Security.
- В качестве сервера авторизации OAuth 2.0 можно использовать Keycloak, любой другой, который можно установить локально, или самописный сервер авторизации OAuth 2.0 с использованием проекта Spring Security OAuth.

- Авторизационный сервер KeyCloak http://localhost:8282/ для обмена с платежным сервисом
  Данные для авторизации
   - KEYCLOAK_ADMIN: admin
   - KEYCLOAK_ADMIN_PASSWORD: adminpassword
   - Настройка клиента KeyCloak: realm-config.json
