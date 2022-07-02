theme: /

    state: BabushkaSay
        q!: $regex</start>
        script: 
            log(JSON.stringify($request));
        a: Что сказать?

    state: NoMatch
        event!: noMatch
        q!: set
        script:
            var toSendRightNowTime = "2022-07-02T10:00:00";
            $pushgate.createEvent(toSendRightNowTime, "SetBabushkaData", 
                    {"sayData": $request.query}, 
                    "telegram",
                    "1004467-tlgrm-1004467-Moz-94619",
                    "telegram-1004467-tlgrm-1004467-Moz-108345-234198008");
        a: Записала
        
        
    state: DanilaSay
        event: DanilaSay
        script: 
            var danilaSay = $request.rawRequest.eventData.sayData;
            
            $client.danilaAnswers = $client.danilaAnswers || [];
            $client.danilaAnswers.push(danilaSay);
        
        
    state: GetNewDataForBabushka
        q!: get
        script:
            var danilaAnswers = $client.danilaAnswers || [];
            if (danilaAnswers.length === 0) {
                $reactions.answer("Данил еще не успел ответить");
            } else {
                $reactions.answer("Данил говорит");
                for (var i = 0; i < danilaAnswers.length; i++) {
                    $reactions.answer(danilaAnswers[i]);
                }
                    
            }
            
            $client.danilaAnswers = [];