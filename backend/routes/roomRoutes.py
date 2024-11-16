from fastapi import APIRouter, Depends
from schemas.room import Room, RoomOut
from config.db import get_db
from sqlalchemy.orm import Session
from security.seguridad import Portador
from controllers.roomController import (
    create_room,
    all_rooms,
    get_room_by_name,
    update_room,
    delete_room,
    exist_room,
    get_room_by_id,
)

router = APIRouter()


# Create a new room
@router.post("/new_room/", dependencies=[Depends(Portador())])
def create_new_room(room: Room, db: Session = Depends(get_db)):
    exist = exist_room(room.name, db)
    if exist:
        return {"error": "Room already exists"}
    new_room = create_room(room, db)
    return Room(**new_room.__dict__)


# Get all rooms
@router.get("/all_rooms/", dependencies=[Depends(Portador())])
def get_all_rooms(db: Session = Depends(get_db)):
    rooms = all_rooms(db)
    return rooms


# Get room by id
@router.get("/room_by_id", dependencies=[Depends(Portador())])
def get_room_by_id_endpoint(id: int, db: Session = Depends(get_db)):
    room = get_room_by_id(id, db)
    if room is None:
        return {"error": "Room not found"}
    return RoomOut(**room.__dict__)


# Get room by name
@router.get("/room_by_name/", dependencies=[Depends(Portador())])
def get_room_by_name_endpoint(name: str, db: Session = Depends(get_db)):
    room = get_room_by_name(name, db)
    if room is None:
        return {"error": "Room not found"}
    return RoomOut(**room.__dict__)


# Update room
@router.put("/update_room/", dependencies=[Depends(Portador())])
def update_room_endpoint(id: int, room: Room, db: Session = Depends(get_db)):
    updated_room = update_room(id, room, db)
    if updated_room is None:
        return {"error": "Room not found"}
    return updated_room


# Delete room
@router.delete("/delete_room/", dependencies=[Depends(Portador())])
def delete_room_endpoint(id: int, db: Session = Depends(get_db)):
    room = delete_room(id, db)
    if room is None:
        return {"error": "Room not found"}
    return room
