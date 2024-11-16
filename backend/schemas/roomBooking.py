from pydantic import BaseModel

class RoomBooking(BaseModel):
    user_id: int
    room_id: int
    start_time: str
    end_time: str
    status: str
    booking_date: str

class RoomBookingOut(RoomBooking):
    id: int