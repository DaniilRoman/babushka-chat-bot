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
                var answers = $integration.googleSheets.readDataFromCells(
                    $secrets.get("INTEGRATION_ID"),
                    $secrets.get("SPREADSHEET_ID"),
                    "Лист1",
                    ["A1"]
                );
            
                if (answers.length === 0) {
                    answers = ""
                } else {
                    answers = answers[0]["value"];
                    answers = answers.substring(1, answers.length-1);
                }
                $reactions.answer("=== ANSWER ===");
                $reactions.answer(answers);
                
                answers = answers + $request.query + ". ";

            
                $integration.googleSheets.writeDataToCells(
                    $secrets.get("INTEGRATION_ID"),
                    $secrets.get("SPREADSHEET_ID"),
                    "Лист1",
                    [{values: [answers], cell: "A1"}]
                );    
                
                $reactions.answer("Отправлено");
            go!: /Start
              
        state: Error
            a: Cannot interact with Google sheets

            

    state: NoMatch
        event!: noMatch
        a: Я не понял. Вы сказали: {{$request.query}}