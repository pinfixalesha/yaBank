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
- **Контейнеризация:** Helm + Kubernetes
- **Оркестрация контейнеров:** Docker Compose
- **Архитектура:** Микросервисная
- **Авторизация:** OAuth 2.0 (JWT)
- **Service Discovery и Gateway API:** Kubernetes Ingress
- **Externalized/Distributed Config:** Kubernetes ConfigMaps
- **CI/CD:** Jenkins

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
5. Для Minikube включите Ingress-добавку и увеличим память
   ```bash
   minikube addons enable ingress
   minikube config view vm-driver
   minikube config get memory
   minikube config set memory 16384
   minikube stop
   minikube start
   ```
6. Minikube / локальный кластер не имеет правильной настройки DNS, поэтому ip указываем в /etc/hosts  
   ```bash
   minikube ip
   ```
Формат
<minikube-ip> gateway-ingress.yabank.local
- Пример
>192.168.49.2 gateway-ingress.yabank.local


7. Активируем Docker-окружение Minikube
   ```bash
   minikube docker-env >addenv.bat
   addenv.bat
   ```
8. Если нет доступа к bitnamicharts, то тянем к себе вручную
   ```bash
   helm pull oci://registry-1.docker.io/bitnamicharts/keycloak --version 24.7.3
   helm pull oci://registry-1.docker.io/bitnamicharts/postgresql --version 14.2.3
   ```
9. Сборка и загрузка Docker-образов в Minikube
   ```bash
   docker images
   minikube image ls   
   docker build -t exchange-generator-application:0.0.1-SNAPSHOT ./exchangeGeneratorApplication
   docker build -t notifications-application:0.0.1-SNAPSHOT ./notificationsApplication
   docker build -t blocker-application:0.0.1-SNAPSHOT ./blockerApplication
   docker build -t exchange-application:0.0.1-SNAPSHOT ./exchangeApplication
   docker build -t accounts-application:0.0.1-SNAPSHOT ./accountsApplication
   docker build -t cash-application:0.0.1-SNAPSHOT ./cashApplication   
   docker build -t transfer-application:0.0.1-SNAPSHOT ./transferApplication
   docker build -t bank-ui-application:0.0.1-SNAPSHOT ./bankUIApplication   
   ```

10. Проверка ingress
   ```bash
   kubectl get pods -n kube-system
   kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.10.1/deploy/static/provider/cloud/deploy.yaml
   kubectl get svc -n ingress-nginx
   ```
11. Обновление helm чартов
   ```bash
   cd ya-bank
   helm dependency update .
   ```
12. Запуск
   ```bash
   helm install yabank ./
   ```
13. Доступ к приложени с использованием команды minikube для отображения url приложения  
    ```bash 
    minikube service yabank-bank-ui-application --url
    
    >http://127.0.0.1:51899
    >! Because you are using a Docker driver on windows, the terminal needs to be open to run it.
    ```

14. После запуска выполним тест helm
    ```bash 
    helm test yabank
    ```
    Ожидаемый результат
    ```bash 
    NAME: yabank
    LAST DEPLOYED: Thu Jun  5 00:43:40 2025
    NAMESPACE: default
    STATUS: deployed
    REVISION: 5
    TEST SUITE:     yabank-accounts-application-test
    Last Started:   Thu Jun  5 00:43:47 2025
    Last Completed: Thu Jun  5 00:43:51 2025
    Phase:          Succeeded
    TEST SUITE:     yabank-bank-ui-application-test
    Last Started:   Thu Jun  5 00:43:51 2025
    Last Completed: Thu Jun  5 00:43:55 2025
    Phase:          Succeeded
    TEST SUITE:     yabank-blocker-application-test
    Last Started:   Thu Jun  5 00:43:55 2025
    Last Completed: Thu Jun  5 00:43:59 2025
    Phase:          Succeeded
    TEST SUITE:     yabank-cash-application-test
    Last Started:   Thu Jun  5 00:43:59 2025
    Last Completed: Thu Jun  5 00:44:03 2025
    Phase:          Succeeded
    TEST SUITE:     yabank-exchange-application-test
    Last Started:   Thu Jun  5 00:44:03 2025
    Last Completed: Thu Jun  5 00:44:07 2025
    Phase:          Succeeded
    TEST SUITE:     yabank-notifications-application-test
    Last Started:   Thu Jun  5 00:44:07 2025
    Last Completed: Thu Jun  5 00:44:11 2025
    Phase:          Succeeded
    TEST SUITE:     yabank-transfer-application-test
    Last Started:   Thu Jun  5 00:44:11 2025
    Last Completed: Thu Jun  5 00:44:15 2025
    Phase:          Succeeded    
    ```


# Полезные команды для работы с Kubernetes и Helm

## Получение ReplicaSet
```bash
kubectl get replicaset -l app=exchange-generator-application
```

## Вход в контейнер и вывод секрета БД

### Подключение к Pod'у:
```bash
kubectl exec -it yabank-db-0 -- bash
```

### Получение пароля из Secret:
```bash
kubectl get secret yabank-db -o jsonpath='{.data}'
# Пример вывода: {"postgres-password":"VTdhSXVVN3EyZw=="}
echo "VTdhSXVVN3EyZw==" | base64 --decode
# Расшифрованный пароль: U7aIuU7q2g
```

## Пересоздание PVC при изменении настроек СУБД

### Удаление PVC:
```bash
kubectl get pvc
kubectl delete pvc data-yabank-db-0
```

### После удаления — установите проект заново:
```bash
helm install yabank ./
```

## Проброс портов

### PostgreSQL:
```bash
kubectl port-forward yabank-db-0 5432:5432
```

### Keycloak:
```bash
kubectl port-forward yabank-keycloak-0 8080:8080
```

## Получение внешнего IP Ingress

```bash
kubectl get ingress
# Если IP = 127.0.0.1, выполните:
minikube addons enable ingress

kubectl get svc -A | grep ingress
# Пример вывода:
# ingress-nginx   ingress-nginx-controller             LoadBalancer   10.109.152.214   127.0.0.1     80:30813/TCP,443:32321/TCP   10d
> **IP:** `127.0.0.1` или другой указанный.
```

## Обновление/перезапуск приложения через Helm

### Удаление и повторная установка:
```bash
helm uninstall yabank
kubectl delete pvc data-yabank-postgresql-0
helm install yabank ./
```

### Обновление:
```bash
helm upgrade yabank ./
```

### Проверка конфигурации без применения:
```bash
helm install yabank ./ --dry-run >1.xxx
```

## Установка curl внутри контейнера и тестирование

### Вход в Pod:
```bash
kubectl exec -it yabank-exchange-generator-application-548c6798b7-lllcx -- sh
```

### Установка и использование curl:
```bash
apk add curl
curl -v http://yabank-exchange-application.default.svc.cluster.local:80/exchange/rates
curl -v http://gateway-ingress.yabank.local/exchangeApplication/exchange/rates
```

## Проброс сервиса и проверка health-check

```bash
kubectl port-forward svc/yabank-exchange-application 8080:80
curl http://localhost:8080/actuator/health
```

## Информационные команды

### Текущие ресурсы:
```bash
kubectl get ingress
kubectl get svc
kubectl get pods
```

### Детализация:
```bash
kubectl describe pod yabank-exchange-application-xxx
kubectl describe service yabank-exchange-application
kubectl get endpoints yabank-exchange-application
kubectl get events -n default
```

### Безопастность

- В проекте реализована система аутентификации и авторизации с использованием Spring Security.
- В качестве сервера авторизации OAuth 2.0 можно использовать Keycloak, любой другой, который можно установить локально, или самописный сервер авторизации OAuth 2.0 с использованием проекта Spring Security OAuth.

- Авторизационный сервер KeyCloak http://localhost:8282/ для обмена с платежным сервисом
  Данные для авторизации
   - KEYCLOAK_ADMIN: admin
   - KEYCLOAK_ADMIN_PASSWORD: adminpassword
   - Настройка клиента KeyCloak: realm-config.json

## CI/CD Jenkins

### 5. Создайте `.env` файл в папке [jenkins](jenkins)
Создайте файл `.env`:

```env
# Путь до локального kubeconfig-файла
KUBECONFIG_PATH=C:\Users\pav\.kube\config.yaml

# Параметры для GHCR
GITHUB_USERNAME=pinfixalesha
GITHUB_TOKEN=ghp_{токен}
GHCR_TOKEN=ghp_{токен}

# Docker registry (в данном случае GHCR)
DOCKER_REGISTRY=ghcr.io/your-username
GITHUB_REPOSITORY=pinfixalesha/yaBank

# Пароль к базе данных PostgreSQL
DB_PASSWORD=12345
```
> Убедитесь, что GitHub Token имеет права `write:packages`, `read:packages` и `repo`.

### 6. Запустите Jenkins

```bash
cd jenkins
docker compose up -d --build
```

Jenkins будет доступен по адресу: [http://localhost:8080](http://localhost:8080)


## Завершение работы и очистка

Для остановки Jenkins, удаления namespace, а также всех установленных ресурсов, используйте скрипт `nuke-all.sh`.
Он находится в папке `jenkins`:
```bash
cd jenkins
./nuke-all.sh
```