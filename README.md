# adfmp1h23-crocodile: тег App is Ready

Видео работы приложения при статусе "App is ready":

https://disk.yandex.ru/i/sAp5yASnezj8mQ

Тег: UnitTests

-----

Частично работоспособный UI(github не потянул, пришлось закинуть на диск):


https://disk.yandex.ru/i/N8D307ska5Ilsg



# Макет

https://user-images.githubusercontent.com/22154737/229360419-0ae36464-b694-4719-b8cf-1fc8998bbcaf.mov

https://www.figma.com/file/Rg9foSqbSCnaXLQRs1oRaX/Croco?node-id=0%3A1&t=rnProNHS9xUChL43-1


# Объяснение функций

Стартовое окно:

1) Кнопка START:
*  запускает анимацию, закрывается рот у крокодила
*  переход на главное окно
2) Кнопка КНИЖКА: переход на страницу с правилами
3) Кнопка НАСТРОЙКИ: переход на страницу настроек

Главное игровое окно:

1) Кнопка START: 
* запускает таймер для текущего игрока
* пропадает после нажатия и появляются две кнопки SKIP и CROCO:
 - SKIP пропуск слова
 - CROCO засчитать слово. Появляется балл у игрока. Подтягивается слово из БД
* По окончанию таймера появляется кнопка START и пропадают кнопки SKIP и CROCO

2) Посередине слово, которое достается из БД
3) Снизу пишем описание слова

Окно правил:
* Реализован скроллинг для чтения правила
* Кнопка для выхода, если у телефона нет кнопки назад

Окно настроек:
* Можно отключить подсказку для слова
* Можно изменить таймер для игры

