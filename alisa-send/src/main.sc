theme: /

    state: BabushkaSay
        q!: $regex</start>
        a: Что сказать?

    state: NoMatch
        event!: noMatch
        script:
            var toSendRightNowTime = "2022-07-02T10:00:00";
            $pushgate.createEvent(toSendRightNowTime, "SetBabushkaData", 
                    {"sayData": $request.query}, 
                    "telegram",
                    $secrets.get("TLGRM_BOT_ID"),
                    $secrets.get("TLGRM_USER_ID"));
        a: Записала

