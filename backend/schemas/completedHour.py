from pydantic import BaseModel
from datetime import date

class CompletedHour(BaseModel):
    room_booking_id: int
    hour_completed: str
    date_registered: str
    status: str
    observation: str

class CompletedHourOut(CompletedHour):
    id: int