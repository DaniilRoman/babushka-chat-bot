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
            if (danilAnswers === "undefined" || typeof danilAnswers === 'undefined' || danilAnswers === null) {
                danilAnswers = "";
            }
        
            if (danilAnswers === "") {
                $reactions.answer("Данил еще не успел ответить");
            } else {
                $reactions.answer("Данил говорит");
                $reactions.answer(danilAnswers);
            }
        GoogleSheets:
            operationType = writeDataToCells
            integrationId = {{ $secrets.get("INTEGRATION_ID") }}
            spreadsheetId = {{ $secrets.get("SPREADSHEET_ID") }}
            sheetName = Лист1
            body = [{"values": [""],"cell":"A1"}]
            errorState = /GoogleSheetError
        

    state: GoogleSheetError
        a: Не получается, скачать данные, напишите Данилу