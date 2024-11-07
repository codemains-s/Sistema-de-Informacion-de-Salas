from pydantic import BaseModel

class RoomSchedule(BaseModel):
    room_id: int
    schedule_id: int
    date: str
    hour: str

class RoomScheduleOut(RoomSchedule):
    id: int