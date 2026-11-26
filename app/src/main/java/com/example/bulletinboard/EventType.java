package com.example.bulletinboard;

public enum EventType
{
    CLUB(0),
    FOOD(1),
    SPORT(2),
    SOCIAL(3),
    STUDENT_GOV(4),
    OTHER(5);

    private final int code;

    EventType(int code)
    {
        this.code = code;
    }

    public int getCode()
    {
        return code;
    }
}
