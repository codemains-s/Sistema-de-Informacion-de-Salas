from pydantic import BaseModel

class RoomSchedules(BaseModel):
    room_id: int
    schedule_id: int
    date: str
    hour: str

class HorarioPorSalaOut(RoomSchedules):
    id: int