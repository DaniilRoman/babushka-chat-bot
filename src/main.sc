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
            GoogleSheets:
                operationType = readDataFromCells
                integrationId = {{ $secrets.get("INTEGRATION_ID") }}
                spreadsheetId = {{ $secrets.get("SPREADSHEET_ID") }}
                sheetName = Лист1
                body = [{"varName":"sayData","cell":"A1"}]
                errorState = /AnswerToBabushka/Error
            script:
                if ($session.sayData === "underfined" || typeof $session.sayData === 'underfined' || $session.sayData === null) {
                    $session.sayData = ""
                }
                $session.tmpSheetsValue = $session.sayData + $request.query + ". ";
            GoogleSheets:
                operationType = writeDataToCells
                integrationId = {{ $secrets.get("INTEGRATION_ID") }}
                spreadsheetId = {{ $secrets.get("SPREADSHEET_ID") }}
                sheetName = Лист1
                body = [{"values": "{{$session.tmpSheetsValue}}","cell":"A1"}]
                errorState = /AnswerToBabushka/Error1
            script:
                $reactions.answer("Отправлено");
              
        state: Error
            a: Cannot interact with Google sheets

            

    state: NoMatch
        event!: noMatch
        a: Я не понял. Вы сказали: {{$request.query}}