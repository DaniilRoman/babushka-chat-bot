theme: /

    state: BabushkaSay
        q!: $regex</start>
        go!: /GetNewDataForBabushka
        
    state: GetNewDataForBabushka
        event!: noMatch
        
        GoogleSheets:
            operationType = readDataFromCells
            integrationId = {{ $secrets.get("INTEGRATION_ID") }}
            spreadsheetId = {{ $secrets.get("SPREADSHEET_ID") }}
            sheetName = Лист1
            body = [{"varName":"sayData","cell":"A1"}]
            errorState = /GoogleSheetError
        script:
            var danilAnswers = $session.sayData;
            if (danilAnswers === "underfined" || typeof danilAnswers === 'underfined' || danilAnswers === null) {
                danilAnswers = "";
            }
            $reactions.answer("================");
            $reactions.answer(danilAnswers);
            $reactions.answer(typeof danilAnswers === 'underfined');
            $reactions.answer(danilAnswers === "underfined");
        
            if (danilAnswers === "") {
                $reactions.answer("Данил еще не успел ответить");
            } else {
                $reactions.answer("Данил говорит");
                $reactions.answer(danilAnswers);
            }
        # GoogleSheets:
        #     operationType = deleteRowOrColumn
        #     integrationId = {{ $secrets.get("INTEGRATION_ID") }}
        #     spreadsheetId = {{ $secrets.get("SPREADSHEET_ID") }}
        #     sheetName = Лист1
        #     body = [{"values": ["A"]}]
        #     errorState = /GoogleSheetError
        GoogleSheets:
            operationType = writeDataToCells
            integrationId = {{ $secrets.get("INTEGRATION_ID") }}
            spreadsheetId = {{ $secrets.get("SPREADSHEET_ID") }}
            sheetName = Лист1
            body = [{"values": ["{{$session.tmpSheetsValue}}"],"cell":"A1"}]
            errorState = /AnswerToBabushka/Error
        

    state: GoogleSheetError
        a: Не получается, скачать данные, напишите Данилу