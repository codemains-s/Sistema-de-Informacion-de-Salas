from pydantic import BaseModel

class UserRoom(BaseModel):
    id_user: int
    id_room: int
    date: str
    hour: str

class UserRoomsOut(UserRoom):
    id: int