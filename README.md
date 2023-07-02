REST сервис по регистрации погодных данных
[Техническое задание](./ТЗ.pdf)
REST API спроектирован и разработан на стеке:
JPA. Hibernate (Postgres)
Spring Boot 3
Spring validator
Spring MVC
Maven
Spring security with ROLES
Authentication with JSON Web Token (JWT)
Logback
Junit
AJAX

API реализует следующие возможности:
1. Без аутентификации:
   регистрация нового пользователя ("/auth/registration")
   аутентификация пользователя ("/auth/login")
   На основании успешной аутентификации пользователь получает jwt-Token
2. Авторизованный пользователь (ROLE.USER) получает:
   получение количества дождливых измерений ("/measurements/rainyDaysCount")
   получение всех измерений ("/measurements")
   получение всех измерений для сенсора с именем sensorName ("/measurements/name/{sensorName}")
   получение измерения по его id ("/measurements/{id}")
   добавление измерения ("/measurements/add")
   -значение "value" должно быть не пустым и находиться в диапазоне от -100 до 100.
   -значение "raining" должно быть не пустым.
   -значение "sensor" должно быть не пустым. При этом, название датчика должно валидироваться в БД. Датчик с таким названием должен быть зарегистрирован в системе (должен быть в БД). Если такого датчика нет в БД - выдавать ошибку
3. Авторизованный admin (ROLE.ADMIN) получает:
   получение всех измерений ("/sensors")
   получение сенсора по его id ("/sensors/{id}")
   добавление сенсора в базу ("/sensors/registration")
   -проверяется то, что датчика с таким названием еще нет в БД. Если датчик с таким названием есть в БД - возвращается клиенту сообщение с ошибкой.
   -если название сенсора пустое или содержит менее 3 или более 30 символов, клиенту возвращаться сообщение с ошибкой
   удаление сенсора из базы ("/sensors/delete")
   удаление пользователя ("/auth/delete")

Также реализован "ручной" способ добавления измерений через web-клиент:
Пользователь переходит по адресу "/ui/auth", проходит аутентификацию.
В случае успешной аутентификации происходит redirect на страницу ввода параметров измерений (value, isRaining, sensorName)

Я написал функциональные тесты для контроллеров. Теперь нужно написать интеграционные тесты. Как это сделать?