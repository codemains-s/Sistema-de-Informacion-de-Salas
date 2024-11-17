from pydantic import BaseModel
from datetime import date

class CompletedHour(BaseModel):
    room_booking_id: int
    user_id: int
    hour_completed: int
    date_register: str
    status: str
    observations: str

class CompletedHourOut(CompletedHour):
    id: int