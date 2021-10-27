# MiniAppTest
Приложение для прохождения тестирования. Вопрос состоит из четырех вариантов ответа. При выборе ответа, пользователь сразу видит правильный выбран ответ или нет.
По окончании теста выводится процент и количество правильных/неправильных ответов, а так же список всех вопросов. По нажатию на любой из вопросов, пользователь может вернуться к выбранному вопросу для просмотра правильного варианта ответа.
## Пояснения по работе
- Вопросы размещаются в папке `assets` в формате `json`;
- При каждом начале теста, порядок вопросов и ответов произвольно изменяется;
- В случае прекращения прохождения тестирования, пользователь может в любой момент вернуться к приложению и продолжить работу (приложение переживает смену конфигурации и убийство приложения системой).
- Приложение построено на **Single Activity Application**

## Структура json вопросов
```js
{"questions":[ 
{
"question": "Вопрос 1",
"trueAnswer": "Правильный ответ",
"falseAnswer1":"Неправильный ответ 1",
"falseAnswer2":"Неправильный ответ 2",
"falseAnswer3":"Неправильный ответ 3"
},
{
"question": "Вопрос 2",
"trueAnswer": "Правильный ответ",
"falseAnswer1":"Неправильный ответ 1",
"falseAnswer2": "Неправильный ответ 2",
"falseAnswer3": "Неправильный ответ 3"
}
]}
```

## App preview
<p float="left">
<img src="https://github.com/AntonAlekseevich93/AppTest/blob/master/app/src/main/res/preview/Screenshot1.png" width="250" height="450">
<img src="https://github.com/AntonAlekseevich93/AppTest/blob/master/app/src/main/res/preview/Screenshot2.png" width="250" height="450">
<img src="https://github.com/AntonAlekseevich93/AppTest/blob/master/app/src/main/res/preview/Screenshot3.png" width="250" height="450">
<img src="https://github.com/AntonAlekseevich93/AppTest/blob/master/app/src/main/res/preview/Screenshot4.png" width="250" height="450">
  </p>