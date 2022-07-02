theme: /

    state: BabushkaSay
        q!: $regex</start>
        script: 
            log(JSON.stringify($request));
        go!: /GetNewDataForBabushka
        
        
    state: DanilaSay
        event: DanilaSay
        script: 
            var danilaSay = $request.rawRequest.eventData.sayData;
            
            $client.danilaAnswers = $client.danilaAnswers || [];
            $client.danilaAnswers.push(danilaSay);
        
        
    state: GetNewDataForBabushka
        event!: noMatch
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