theme: /
    
    init:
        bind("preMatch", function($context) {
            var currentUser = $context.request.channelUserId;
            var correctUser = $env.get("CORRECT_TLGRM_USER_ID");
            if (currentUser !== correctUser) {
                $reactions.answer("Illegal user");
                throw new Error("Illegal user");  
            }
        });

    state: Start
        q!: $regex</start>
        a: Вот, что ты можешь:
        script:
            log(JSON.stringify($request));
        buttons:
            "Ответить бабушке" -> /AnswerToBabushka
        

    state: SetBabushkaData
        event: SetBabushkaData
        script:
            var babushkaSay = $request.rawRequest.eventData.sayData;
            $reactions.answer("Бабушка говорит: " + babushkaSay);
    
    state: AnswerToBabushka
        state: Listen
            q: *
            script:
                var toSendRightNowTime = "2022-07-02T10:00:00";
                $pushgate.createEvent(toSendRightNowTime, "DanilaSay", 
                    {"sayData": $request.query}, 
                    $env.get("ALISA_CHANNEL_TYPE"),
                    $env.get("ALISA_BOT_ID"),
                    $env.get("ALISA_USER_ID"));
                $reactions.answer("Отправлено");

    state: NoMatch
        event!: noMatch
        a: Я не понял. Вы сказали: {{$request.query}}