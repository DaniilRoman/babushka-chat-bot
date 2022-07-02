theme: /
    
    init:
        bind("preMatch", function($context) {
            var currentUser = $context.request.channelUserId;
            var correctUser = $secrets.get("CORRECT_TLGRM_USER_ID");
            if (currentUser !== correctUser) {
                throw new Error("Illegal user");  
            }
        });

    state: Start
        q!: $regex</start>
        a: Вот, что ты можешь:
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
                log("================================");
                log($secrets.get("ALISA_CHANNEL_TYPE"));
                log($secrets.get("ALISA_BOT_ID"));
                log($secrets.get("ALISA_USER_ID"));
            
                var toSendRightNowTime = "2022-07-02T10:00:00";
                $pushgate.createEvent(toSendRightNowTime, "DanilaSay", 
                    {"sayData": $request.query}, 
                    $secrets.get("ALISA_CHANNEL_TYPE"),
                    $secrets.get("ALISA_BOT_ID"),
                    $secrets.get("ALISA_USER_ID"));
                $reactions.answer("Отправлено");

    state: NoMatch
        event!: noMatch
        a: Я не понял. Вы сказали: {{$request.query}}