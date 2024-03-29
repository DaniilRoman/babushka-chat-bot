theme: /

    state: BabushkaSay
        q!: $regex</start>
        go!: /GetNewDataForBabushka
        
    state: GetNewDataForBabushka || noContext = true
        event!: noMatch
        script:
            var danilAnswers = $integration.googleSheets.readDataFromCells(
                $secrets.get("INTEGRATION_ID"),
                $secrets.get("SPREADSHEET_ID"),
                "Лист1",
                ["A1"]
            );
        
            if (danilAnswers.length === 0) {
                $reactions.answer("Данил еще не успел ответить");
            } else {
                $reactions.answer("Данил говорит");
                var answers = danilAnswers[0]["value"];
                $reactions.answer(answers)

                $integration.googleSheets.writeDataToCells(
                    $secrets.get("INTEGRATION_ID"),
                    $secrets.get("SPREADSHEET_ID"),
                    "Лист1",
                    [{values: [""], cell: "A1"}]
                );
            }
        

    state: GoogleSheetError
        a: Не получается, скачать данные, напишите Данилу
