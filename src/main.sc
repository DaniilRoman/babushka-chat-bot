theme: /

    state: BabushkaSay
        q!: $regex</start>
        script:
            log("======================");
            log(JSON.stringify($request));
        go!: /GetNewDataForBabushka
        
        
    state: DanilaSay
        event: DanilaSay
        script: 
            log("++++++++++++++++++++++++++++=");
            log(JSON.stringify($request.rawRequest));
            var danilSay = $request.rawRequest.eventData.sayData;
            
            $client.danilAnswers = $client.danilAnswers || [];
            $client.danilAnswers.push(danilSay);
        
        
    state: GetNewDataForBabushka
        event!: noMatch
        script:
            log("======================");
            log(JSON.stringify($request));
            var danilAnswers = $client.danilAnswers || [];
            if (danilAnswers.length === 0) {
                $reactions.answer("Данил еще не успел ответить");
            } else {
                $reactions.answer("Данил говорит");
                for (var i = 0; i < danilAnswers.length; i++) {
                    $reactions.answer(danilAnswers[i]);
                }
                    
            }
            
            $client.danilaAnswers = [];