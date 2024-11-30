from pydantic import BaseModel
from datetime import date

class RoomSchedule(BaseModel):
    room_id : int
    hour_start : str
    hour_end : str
    day_of_week : str
    status : str

class RoomScheduleOut(RoomSchedule):
    id : int
    class Config:
        orm_mode = True