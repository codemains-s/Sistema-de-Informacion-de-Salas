from fastapi import APIRouter, Depends, HTTPException, Request, status
from schemas.roomBooking import RoomBooking, RoomBookingOut
from config.db import get_db
from sqlalchemy.orm import Session
from security.seguridad import Portador
from controllers.roomBookingController import (create_room_booking, all_room_bookings, get_room_booking_by_id,
                                               update_room_booking, delete_room_booking, exist_room_booking)

router = APIRouter()

@router.post("/new_roomBooking/", dependencies=[Depends(Portador())])
def new_roomBooking(room_booking: RoomBooking, db: Session = Depends(get_db)):
    return create_room_booking(room_booking, db)

@router.get("/all_roomBookings/", dependencies=[Depends(Portador())])
def all_roomBookings(db: Session = Depends(get_db)):
    return all_room_bookings(db)

@router.get("/get_roomBooking_id/{id}", dependencies=[Depends(Portador())])
def get_roomBooking_id(id: int, db: Session = Depends(get_db)):
    exist = exist_room_booking(id, db)
    if not exist:
        raise HTTPException(status_code=404, detail="The room booking does not exist")
    return get_room_booking_by_id(id, db)

@router.put("/update_roomBooking/{id}", dependencies=[Depends(Portador())])
def update_roomBooking(id: int, room_booking: RoomBooking, db: Session = Depends(get_db)):
    exist = exist_room_booking(id, db)
    if not exist:
        raise HTTPException(status_code=404, detail="The room booking does not exist")
    
    update = update_room_booking(id, room_booking, db)
    return RoomBooking(**update.__dict__)

@router.delete("/delete_roomBooking/{id}", dependencies=[Depends(Portador())])
def delete_roomBooking(id: int, db: Session = Depends(get_db)):
    exist = exist_room_booking(id, db)
    if not exist:
        raise HTTPException(status_code=404, detail="The room booking does not exist")
    delete = delete_room_booking(id, db)
    return {"message": "Room booking deleted successfully"}
    